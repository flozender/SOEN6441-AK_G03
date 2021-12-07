package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Owner;
import play.libs.ws.WSClient;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.Duration;
import services.github.GitHubApi;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public class UserRepositorySocketActor extends AbstractActorWithTimers {
    private final ActorRef out;
    private final WSClient ws;
    private final ActorRef supervisorActor;
    private final GitHubApi ghImpl;
    private String username = "";

    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(10, TimeUnit.SECONDS));
    }

    public static Props props(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        return Props.create(UserRepositorySocketActor.class, out, ws, ghImpl);
    }

    public UserRepositorySocketActor(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        this.out = out;
        this.ws = ws;
        this.ghImpl = ghImpl;
        this.supervisorActor = getContext().actorOf(SupervisorActor.props(ws, ghImpl), "supervisorActor");
    }

    public static final class Tick{
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::handleUserRepository)
                .match(Tick.class, (r)->updatePage())
                .build();
    }

    private void handleUserRepository(String username) {
        this.username = username;
        CompletableFuture<JsonNode> user = FutureConverters.toJava(ask(supervisorActor, new GitHubActorProtocol.UserRepository(username), 5000)).toCompletableFuture().thenApplyAsync(reply -> (JsonNode) reply);
        CompletableFuture<JsonNode> userResult = user.thenApplyAsync((JsonNode us) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String resp = mapper.writeValueAsString(us);
                out.tell(resp, self());
            } catch (Exception e) {}
            return null;
        });
    }

    private void updatePage() {
        CompletableFuture<JsonNode> user = FutureConverters.toJava(ask(supervisorActor, new GitHubActorProtocol.UserRepository(this.username), 5000)).toCompletableFuture().thenApplyAsync(reply -> (JsonNode) reply);
        CompletableFuture<JsonNode> userResult = user.thenApplyAsync((JsonNode us) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String resp = mapper.writeValueAsString(us);
                out.tell(resp, self());
            } catch (Exception e) {}
            return null;
        });
    }
}
