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

public class RepositoryProfileActor extends AbstractActorWithTimers { 
    private final ActorRef out;
    private static WSClient ws;
    private static GitHubApi ghImpl;
    private String username; 
    private String repository;
    
    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(10, TimeUnit.SECONDS));
    }

    public static Props props(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        return Props.create(RepositoryProfileActor.class, out, ws, ghImpl);
    }

    @Inject
    public RepositoryProfileActor(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
        this.out = out;
    }
    
    public static final class Tick{}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(String.class, this::handleSearch)
        .match(Tick.class, (r)->{
            if(this.username != null){
                this.handleSearch(this.username+"/"+this.repository);
            }
        })
        .build();
    }

    private void handleSearch(String search) {
        String[] data = search.split("/");
        this.username = data[0];
        this.repository = data[1];
        CompletableFuture<Repository> repo = ghImpl.repositoryProfile(data[0], data[1], ws);
        CompletableFuture<JsonNode> commits = ghImpl.getRepositoryCommits(data[0], data[1], ws);
        CompletableFuture<JsonNode> issues = ghImpl.getRepositoryIssues(data[0], data[1], ws);
        CompletableFuture<JsonNode> contributors = ghImpl.getRepositoryContributors(data[0], data[1], ws);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(repo, commits, issues, contributors);
        allFutures.thenApplyAsync((v)->{
            repo.thenApplyAsync(r -> {
                commits.thenApplyAsync(c -> {
                    issues.thenApplyAsync(i -> {
                        contributors.thenApplyAsync(cr -> {
                            GitHubActorProtocol.RepositoryInformation rf = new GitHubActorProtocol.RepositoryInformation(data[0], r, i, c, cr);
                            try {
                                ObjectMapper mapper = new ObjectMapper();  
                                String resp = mapper.writeValueAsString(rf);
                                out.tell(resp, self());
                            } catch (Exception e) {}
                            return null;
                        });
                        return null;
                    });
                    return null;
                });
                return null;
            });                
            return null;
        });
    };
}
