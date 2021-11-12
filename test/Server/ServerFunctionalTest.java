package Server;

import static org.junit.Assert.*;

import java.util.OptionalInt;
import java.util.concurrent.CompletionStage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.WithServer;

public class ServerFunctionalTest extends WithServer {

	private static final Object NOT_FOUND = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	 @Test
	  public void testInServer() throws Exception {
	    OptionalInt optHttpsPort = testServer.getRunningHttpsPort();
	    String url;
	    int port;
	    if (optHttpsPort.isPresent()) {
	      port = optHttpsPort.getAsInt();
	      url = "https://localhost:" + port;
	    } else {
	      port = testServer.getRunningHttpPort().getAsInt();
	      url = "http://localhost:" + port;
	    }
	    try (WSClient ws = play.test.WSTestClient.newClient(port)) {
	      CompletionStage<WSResponse> stage = ws.url(url).get();
	      WSResponse response = stage.toCompletableFuture().get();
	      assertEquals(NOT_FOUND, response.getStatus());
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	  }
}
