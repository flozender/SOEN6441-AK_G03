package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import play.libs.ws.WSClient;
import services.github.GitHubApi;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.Patterns.pipe;

public class UserRepositoryActor extends AbstractActor {

    private static GitHubApi ghImpl;
    private static WSClient ws;

    public static Props props(WSClient ws, GitHubApi ghImpl) {
        return Props.create(UserRepositoryActor.class, ws, ghImpl);
    }

    @Inject
    public UserRepositoryActor(WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GitHubActorProtocol.UserRepository.class, search -> {
                    CompletableFuture<JsonNode> reply = ghImpl.userRepository(search.username, ws);
                    pipe(reply, getContext().dispatcher()).to(sender());
                })
                .build();
    }
}
