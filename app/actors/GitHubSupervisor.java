package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GitHubSupervisor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    public static Props props() {
        return Props.create(GitHubSupervisor.class);
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
        return receiveBuilder().build();
    }
}