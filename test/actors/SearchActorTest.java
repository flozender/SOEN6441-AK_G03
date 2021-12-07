// package actors;
// import akka.actor.ActorRef;
// import akka.actor.ActorSystem;
// import akka.testkit.javadsl.TestKit;
// import models.Repository;

// import static org.junit.Assert.assertEquals;
// import org.junit.AfterClass;
// import org.junit.BeforeClass;
// import org.junit.Test;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.inject.Inject;

// import static org.hamcrest.CoreMatchers.*;
// import static org.hamcrest.MatcherAssert.assertThat;

// import play.Application;
// import play.inject.guice.GuiceApplicationBuilder;
// import play.libs.ws.WSClient;
// import play.test.Helpers;
// import play.test.WithApplication;
// import services.github.GitHubApi;
// import services.github.GitHubTestApi;
// import play.mvc.Result;
// import play.mvc.Http.RequestBuilder;
// import play.mvc.Http.Cookie;
// import play.mvc.Http.Request;
// import play.inject.Injector;
// import play.inject.guice.GuiceInjectorBuilder;
// import static play.inject.Bindings.bind;

// import java.text.SimpleDateFormat;
// import java.util.Arrays;
// import java.util.Date;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.CompletionStage;
// import akka.actor.Props;

// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;

// import java.util.stream.Collectors;



// public class SearchActorTest {
//     static ActorSystem system;
//     private static Application testApp;
//     @Inject private WSClient ws;

//     @BeforeClass
//     public static void setup() {
//         testApp = new GuiceApplicationBuilder()
//             .overrides(bind(GitHubApi.class).to(GitHubTestApi.class))
//             .build();
//         system = ActorSystem.create();
//     }
  
//     @AfterClass
//     public static void teardown() {
//         TestKit.shutdownActorSystem(system);
//         system = null;
//     }

//     @Test
//     public void testSearchActor() {
//         final GitHubApi gitHubApi = testApp.injector().instanceOf(GitHubApi.class);
//         new TestKit(system) {
//         {
//             final Props props = Props.create(SearchActor.class);
//             final ActorRef subject = system.actorOf(props);
    
//             // can also use JavaTestKit “from the outside”
//             final TestKit probe = new TestKit(system);
//             // “inject” the probe by passing it to the test subject
//             // like a real resource would be passed in production
//             subject.tell(probe.getRef(), getRef());
//             // await the correct response
//             expectMsg(Duration.ofSeconds(1), "done");
    
//             // the run() method needs to finish within 3 seconds
//             within(
//                 Duration.ofSeconds(3),
//                 () -> {
//                 subject.tell("hello", getRef());
    
//                 // This is a demo: would normally use expectMsgEquals().
//                 // Wait time is bounded by 3-second deadline above.
//                 awaitCond(probe::msgAvailable);
    
//                 // response must have been enqueued to us before probe
//                 expectMsg(Duration.ZERO, "world");
//                 // check that the probe we injected earlier got the msg
//                 probe.expectMsg(Duration.ZERO, "hello");
//                 Assert.assertEquals(getRef(), probe.getLastSender());
    
//                 // Will wait for the rest of the 3 seconds
//                 expectNoMessage();
//                 return null;
//                 });
//         }
//         };
//     }

// }
