package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import actors.GitHubActorProtocol;
// import akka.actor.OneForOneStrategy;
// import akka.actor.SupervisorStrategy;
// import scala.concurrent.duration.Duration;
// import scala.concurrent.duration.Deadline;

public class GitHubActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(GitHubActor.class);
    }

    @Override
    public void preStart() {
        log.info("Server supervisor started");
    }

    @Override
    public void postStop() {
        log.info("Server supervisor stopped");
    }

    // private final SupervisorStrategy strategy = new OneForOneStrategy(-1, Duration.Inf(), DeciderBuilder.match(TimeoutException.class, e -> SupervisorStrategy.restart()).match(FatalException.class, e -> SupervisorStrategy.escalate()).build());

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .build();
    }
}