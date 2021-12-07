package actors;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import models.Repository;
import java.util.*;

import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WSClient;
import play.test.Helpers;
import play.test.WithApplication;
import services.github.GitHubApi;
import services.github.GitHubTestApi;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import static play.inject.Bindings.bind;
import scala.compat.java8.FutureConverters;
import play.libs.Json;
import static akka.pattern.Patterns.ask;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import akka.actor.Props;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;

/**
 * Test the RepositoryProfileSocket Actor
 *
 * @author Tayeeb Hasan
 */
public class RepositoryProfileSocketActorTest {
    static ActorSystem system;
    private static Application testApp;
    @Inject private WSClient ws;

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
     * Test the RepositoryProfileSocket Actor
     *
     * @author Tayeeb Hasan
     */
    @Test
    public void testRepositoryProfileSocket() {
        final GitHubApi gitHubApi = testApp.injector().instanceOf(GitHubApi.class);
        new TestKit(system) {
        {
            final TestKit probe = new TestKit(system);
            ActorRef probeRef = probe.getRef();
            final ActorRef rpsActor = system.actorOf(RepositoryProfileSocketActor.props(probeRef, ws, gitHubApi));
            rpsActor.tell("facebook/jest", probeRef);
            awaitCond(probe::msgAvailable);
            String response = probe.expectMsgClass(String.class);
            assertThat(response, containsString("facebook/jest"));
            assertThat(response, containsString("[Bug]: False negative (instanceof Float32Array)"));
        }
        };
    }
}
