package actors;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import actors.SupervisorActor;
import models.Owner;
import models.Repository;
import models.RepositoryIssues;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import services.github.GitHubApi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;
import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithTimers;
import akka.actor.Props;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

import actors.GitHubActorProtocol;

public class RepositoryProfileActor extends AbstractActor { 
    private static WSClient ws;
    private static GitHubApi ghImpl;
    private String username; 
    private String repository;

    public static Props props(WSClient ws, GitHubApi ghImpl) {
        return Props.create(RepositoryProfileActor.class, ws, ghImpl);
    }

    @Inject
    public RepositoryProfileActor(WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(GitHubActorProtocol.RepositoryProfile.class, this::handleSearch)
        .build();
    }

    private void handleSearch(GitHubActorProtocol.RepositoryProfile repositoryProfile) {
        String username = repositoryProfile.username;
        String repository = repositoryProfile.repository;
        CompletableFuture<Repository> repo = ghImpl.repositoryProfile(username, repository, ws);
        CompletableFuture<JsonNode> commits = ghImpl.getRepositoryCommits(username, repository, ws);
        CompletableFuture<JsonNode> issues = ghImpl.getRepositoryIssues(username, repository, ws);
        CompletableFuture<JsonNode> contributors = ghImpl.getRepositoryContributors(username, repository, ws);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(repo, commits, issues, contributors);
        CompletableFuture<GitHubActorProtocol.RepositoryInformation> response = allFutures.thenCompose((v)->{
            return repo.thenCompose(r -> {
                return commits.thenCompose(c -> {
                    return issues.thenCompose(i -> {
                        return contributors.thenApplyAsync(cr -> {
                            GitHubActorProtocol.RepositoryInformation rf = new GitHubActorProtocol.RepositoryInformation(username, r, i, c, cr);
                            return rf;
                        });
                    });
                });
            });                
        });
        pipe(response, getContext().dispatcher()).to(sender());
    };
}
