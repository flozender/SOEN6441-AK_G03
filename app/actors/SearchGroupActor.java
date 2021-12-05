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
import akka.actor.AbstractActor;
import akka.actor.Props;

import actors.GitHubActorProtocol.*;

public class SearchGroupActor extends AbstractActor{
    final Map<String, ActorRef> userIdToActor = new HashMap<>();

    public static Props props() {
        return Props.create(SearchGroupActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(GitHubActorProtocol.Search.class, this::searchRepository)
        .match(GitHubActorProtocol.AddSearchResponse.class, this::addSearchResponse)
        .match(GitHubActorProtocol.GetSearchResults.class, this::GetSearchResults)
        .build();
    }

    private void searchRepository(GitHubActorProtocol.Search search) {
        ActorRef userActor = userIdToActor.get(search.userId);
        if (userActor != null) {
            userActor.forward(search, getContext());
        } else {
            userActor = getContext().actorOf(SearchActor.props(search.userId), "user-" + search.userId);
            userIdToActor.put(search.userId, userActor);
            userActor.forward(search, getContext());
        }
    }

    private void addSearchResponse(GitHubActorProtocol.AddSearchResponse searchResponse) {
        ActorRef userActor = userIdToActor.get(searchResponse.userId);
        if (userActor != null) {
            userActor.forward(searchResponse, getContext());
        }
    }

    private void GetSearchResults(GitHubActorProtocol.GetSearchResults search) {
        ActorRef userActor = userIdToActor.get(search.userId);
        if (userActor != null) {
            userActor.forward(search, getContext());
        } else {
            userActor = getContext().actorOf(SearchActor.props(search.userId), "user-" + search.userId);
            userIdToActor.put(search.userId, userActor);
            userActor.forward(search, getContext());
        }
    }
}
