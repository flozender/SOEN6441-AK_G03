package actors;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import models.Repository;

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
import static akka.pattern.Patterns.ask;

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

public class SupervisorActorTest {
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

    @Test
    public void testSearchActor() {
        final GitHubApi gitHubApi = testApp.injector().instanceOf(GitHubApi.class);
        final ActorRef supervisorActor = system.actorOf(SupervisorActor.props(ws, gitHubApi));
        CompletableFuture<List<Repository>> search = FutureConverters.toJava(ask(supervisorActor, new GitHubActorProtocol.Search("facebook"), 5000)).toCompletableFuture().thenApplyAsync(repos -> (List<Repository>) repos);
        try {
            List<Repository> repos = search.get();
            System.out.println("REPOS>>>>>>>" + repos);
            assertEquals(repos.get(0).getName(),"facebook-tools-new");
            assertEquals(repos.get(0).getOwner().getLogin(),"thinhlx1993");
            assertEquals(repos.get(1).getName(),"rasa");
            assertEquals(repos.get(1).getOwner().getLogin(),"RasaHQ");
        } catch (Exception e) {}
  
    }

}