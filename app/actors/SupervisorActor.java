package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import actors.GitHubActorProtocol;
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

import actors.GitHubActorProtocol.*;
// import akka.actor.OneForOneStrategy;
// import akka.actor.SupervisorStrategy;
// import scala.concurrent.duration.Duration;
// import scala.concurrent.duration.Deadline;

public class SupervisorActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final ActorRef searchActor;
    private final WSClient ws;
    private final GitHubApi ghImpl;

    public static Props props(WSClient ws, GitHubApi ghImpl) {
        return Props.create(SupervisorActor.class, ws, ghImpl);
    }

    public SupervisorActor(WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
        this.searchActor = getContext().actorOf(SearchActor.props(ws, ghImpl), "searchActor");
      }

    @Override
    public void preStart() {
        log.info("Server supervisor started");
    }

    @Override
    public void postStop() {
        log.info("Server supervisor stopped");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(GitHubActorProtocol.Search.class, this::Search)
        .build();
    }

    private void Search(GitHubActorProtocol.Search search) {
        searchActor.forward(search, getContext());
    }
}