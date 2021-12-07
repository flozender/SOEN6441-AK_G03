package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.Owner;
import models.Repository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import akka.testkit.javadsl.TestKit;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.libs.ws.WSClient;
import scala.compat.java8.FutureConverters;
import services.github.GitHubApi;
import services.github.GitHubTestApi;

import java.util.concurrent.CompletableFuture;

import static akka.pattern.Patterns.ask;
import static org.junit.Assert.assertEquals;
import static play.inject.Bindings.bind;

public class UserRepositorySocketActorTest {

    static ActorSystem system;
    private static Application testApp;
    @Inject
    private WSClient ws;

    @BeforeClass
    public static void setup() {
        testApp = new GuiceApplicationBuilder()
                .overrides(bind(GitHubApi.class).to(GitHubTestApi.class))
                .build();
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Test the UserRepository Actor
     *
     * @author Pedram Nouri
     */
    @Test
    public void testUserRepositorySocket() {
        final GitHubApi gitHubApi = testApp.injector().instanceOf(GitHubApi.class);
        final TestKit probe = new TestKit(system);
        ActorRef probeRef = probe.getRef();
        final ActorRef ursActor = system.actorOf(UserRepositorySocketActor.props(probeRef, ws, gitHubApi));
        CompletableFuture<String> search = FutureConverters.toJava(ask(ursActor, "pedram", 5000)).toCompletableFuture().thenApplyAsync(result -> (String) result);
        System.out.println("KIr");
        try {
            String jsonString = search.get();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonString);
            Repository repo = Json.fromJson(node, Repository.class);
            assertEquals("pedram", repo.getName());
        } catch (Exception e) {
        }

    }

}
