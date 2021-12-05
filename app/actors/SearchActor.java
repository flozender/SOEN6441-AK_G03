package actors;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import actors.GitHubActor;
import models.Owner;
import models.Repository;
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
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;
import akka.actor.AbstractActor;
import akka.actor.Props;

import actors.GitHubActorProtocol;

public class SearchActor extends AbstractActor { 
    final String userId;
    private static GitHubApi ghImpl;
    private static WSClient ws;
    private ArrayList<List<Repository>> repositories;
    private ArrayList<String> searchTerms;
    
    // need to inject ws and ghimpl here -> 
    public static Props props(String userId) {
        return Props.create(SearchActor.class, userId, ws, ghImpl);
    }


    @Inject
    public SearchActor(String userId, WSClient ws, GitHubApi gitHubApi) {
        this.userId = userId;
        this.ws = ws;
        this.ghImpl = gitHubApi;
        this.repositories = new ArrayList<>();
        this.searchTerms = new ArrayList<>();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(GitHubActorProtocol.Search.class, search -> {
            CompletableFuture<List<Repository>> reply = search.gitHubApi.searchRepositories(search.keywords, search.ws);
            pipe(reply, getContext().dispatcher()).to(sender());
        })
        .match(GitHubActorProtocol.AddSearchResponse.class, searchResponse -> {
            this.repositories.add(0, searchResponse.repositories);
            this.searchTerms.add(0, searchResponse.searchTerms);
            this.repositories = this.repositories.stream().limit(10).collect(Collectors.toCollection(ArrayList::new));
            this.searchTerms = this.searchTerms.stream().limit(10).collect(Collectors.toCollection(ArrayList::new));
            sender().tell(new GitHubActorProtocol.AddedSearchResponse(searchResponse.userId), getSelf());
        })
        .match(GitHubActorProtocol.GetSearchResults.class, search -> {
            GitHubActorProtocol.SearchResults sr = new GitHubActorProtocol.SearchResults(this.repositories, this.searchTerms);
            sender().tell(sr, getSelf());
        })
        .build();
    }
}
