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

public class RepoIssuesTitleSocketActor extends AbstractActorWithTimers {
    private final ActorRef out;
    private final WSClient ws;
    private final ActorRef supervisorActor;
    private final GitHubApi ghImpl;
    private GitHubActorProtocol.RepoIssuesTitle repoIssuesTitle;

    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(5, TimeUnit.SECONDS));
    }

    public static Props props(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        return Props.create(RepoIssuesTitleSocketActor.class, out, ws, ghImpl);
    }

    public RepoIssuesTitleSocketActor(ActorRef out, WSClient ws, GitHubApi ghImpl) {
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
                .match(String.class, this::handleRepoIssuesTitle)
                .match(Tick.class, (r) -> this.handleRepoIssuesTitle("/"))
                .build();
    }

	private <P extends Object> void handleRepoIssuesTitle(P p1) {
		
	     CompletableFuture<GitHubActorProtocol.RepoIssuesTitle> RepoIssuesTitle = FutureConverters.toJava(ask(supervisorActor, this.repoIssuesTitle, 5000)).toCompletableFuture().thenApplyAsync(reply -> (GitHubActorProtocol.RepoIssuesTitle) reply);
	        CompletableFuture<JsonNode> titles = RepoIssuesTitle.thenApplyAsync((GitHubActorProtocol.RepoIssuesTitle rf) -> {
	            try {
	                ObjectMapper mapper = new ObjectMapper();
	                String resp = mapper.writeValueAsString(rf);
	                out.tell(resp, self());
	            } catch (Exception e) {}
	            return null;
	        });
	    }
	
}