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
import akka.actor.AbstractActor.Receive;

import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

import actors.GitHubActorProtocol;

public class RepoIssuesTitleActor extends AbstractActor { 
    private static WSClient ws;
    private static GitHubApi ghImpl;
    private String username; 
    private String repository;

    public static Props props(WSClient ws, GitHubApi ghImpl) {
        return Props.create(RepoIssuesTitleActor.class, ws, ghImpl);
    }

    @Inject
    public RepoIssuesTitleActor(WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(GitHubActorProtocol.RepoIssuesTitle.class, this::handleSearchtitles)
        .build();
    }

    private void handleSearchtitles(GitHubActorProtocol.RepoIssuesTitlereq repoIssuesTitlereq) {
        String username = repoIssuesTitlereq.username;
        String repository = repoIssuesTitlereq.repository;
        CompletableFuture<JsonNode> titles = ghImpl.getRepositoryIssuesTittles(username, repository, ws);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(titles);
        CompletableFuture<GitHubActorProtocol.RepoIssuesTitle> response = allFutures.thenCompose(t->{
        
          
        	
	        return titles.thenApplyAsync(cr -> {
	            GitHubActorProtocol.RepoIssuesTitle rf = new GitHubActorProtocol.RepoIssuesTitle(titles);
	            return rf;
            });
        });
        pipe(response, getContext().dispatcher()).to(sender());
    };
}
    