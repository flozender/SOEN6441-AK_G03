package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Owner;
import models.Repository;
import play.libs.ws.WSClient;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.Duration;
import services.github.GitHubApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static akka.pattern.Patterns.ask;

public class UserProfileSocketActor extends AbstractActorWithTimers {
    private final ActorRef out;
    private final WSClient ws;
    private final ActorRef supervisorActor;
    private final GitHubApi ghImpl;


    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new WebSocketActor.Tick(), Duration.create(10, TimeUnit.SECONDS));
    }

    public static Props props(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        return Props.create(UserProfileSocketActor.class, out, ws, ghImpl);
    }

    public UserProfileSocketActor(ActorRef out, WSClient ws, GitHubApi ghImpl) {
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
                .match(String.class, this::handleUserProfile)
//                .match(WebSocketActor.Tick.class, (r)->updatePage())
                .build();
    }

    private void handleUserProfile(String username) {
        CompletableFuture<Owner> user = FutureConverters.toJava(ask(supervisorActor, new GitHubActorProtocol.UserProfile(username), 5000)).toCompletableFuture().thenApplyAsync(reply -> (Owner) reply);
        CompletableFuture<Owner> userResult = user.thenApplyAsync((Owner us) -> {
            System.out.println(us);

            try {
                ObjectMapper mapper = new ObjectMapper();
                String resp = mapper.writeValueAsString(us);
                out.tell(resp, self());
            } catch (Exception e) {}
            return null;
        });
    }
}
