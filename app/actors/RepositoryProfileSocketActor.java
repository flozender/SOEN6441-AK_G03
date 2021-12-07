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

public class RepositoryProfileSocketActor extends AbstractActorWithTimers {
    private final ActorRef out;
    private final WSClient ws;
    private final ActorRef supervisorActor;
    private final GitHubApi ghImpl;
    private GitHubActorProtocol.RepositoryProfile repositoryProfile;

    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(5, TimeUnit.SECONDS));
    }

    public static Props props(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        return Props.create(RepositoryProfileSocketActor.class, out, ws, ghImpl);
    }

    public RepositoryProfileSocketActor(ActorRef out, WSClient ws, GitHubApi ghImpl) {
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
                .match(String.class, this::handleRepositoryProfile)
                .match(Tick.class, (r) -> this.handleRepositoryProfile("/"))
                .build();
    }

    private void handleRepositoryProfile(String search) {
        System.out.println("HITTING " + search);
        String[] data = search.split("/");
        if (this.repositoryProfile == null){
            this.repositoryProfile = new GitHubActorProtocol.RepositoryProfile(data[0], data[1]);
        }
        CompletableFuture<GitHubActorProtocol.RepositoryInformation> repositoryInformation = FutureConverters.toJava(ask(supervisorActor, this.repositoryProfile, 5000)).toCompletableFuture().thenApplyAsync(reply -> (GitHubActorProtocol.RepositoryInformation) reply);
        System.out.println("REPOS _>>>" + repositoryInformation);
        
        CompletableFuture<JsonNode> repo = repositoryInformation.thenApplyAsync((GitHubActorProtocol.RepositoryInformation rf) -> {
            System.out.println("REPOS _>>>" + rf.repository);
            try {
                ObjectMapper mapper = new ObjectMapper();
                String resp = mapper.writeValueAsString(rf);
                out.tell(resp, self());
            } catch (Exception e) {}
            return null;
        });
    }
}
