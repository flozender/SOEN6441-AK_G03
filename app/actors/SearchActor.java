package actors;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import actors.SupervisorActor;
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
    private static GitHubApi ghImpl;
    private static WSClient ws;
    
    public static Props props(WSClient ws, GitHubApi ghImpl) {
        return Props.create(SearchActor.class, ws, ghImpl);
    }

    @Inject
    public SearchActor(WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(GitHubActorProtocol.Search.class, search -> {
            CompletableFuture<List<Repository>> reply = ghImpl.searchRepositories(search.keywords, ws);
            pipe(reply, getContext().dispatcher()).to(sender());
        })
        .build();
    }
}
