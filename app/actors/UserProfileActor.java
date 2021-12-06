package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import models.Owner;
import models.Repository;
import play.libs.ws.WSClient;
import services.github.GitHubApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.Patterns.pipe;

public class UserProfileActor extends AbstractActor {

    private static GitHubApi ghImpl;
    private static WSClient ws;

    public static Props props(WSClient ws, GitHubApi ghImpl) {
        return Props.create(UserProfileActor.class, ws, ghImpl);
    }

    @Inject
    public UserProfileActor(WSClient ws, GitHubApi ghImpl) {
        this.ws = ws;
        this.ghImpl = ghImpl;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GitHubActorProtocol.UserProfile.class, search -> {
                    CompletableFuture<Owner> reply = ghImpl.userProfile(search.username, ws);
                    pipe(reply, getContext().dispatcher()).to(sender());
                })
                .build();
    }
}
