package controllers;

import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.api.test.Helpers;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import play.libs.oauth.OAuth.RequestToken;
import play.mvc.Http.Session;
import play.data.FormFactory;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class HomeControllerTest extends WithApplication {

	

  
    @Test
    public void testIndex() throws Exception{
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
/*    
 * 
 * 	private static Application testApp;
	private FormFactory formFactory;
	static Session session;
	
 *   @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

  @Test
  public void CompletionStage() {
	String  searchKeyword = "SquidGame";
	String emptykeyword = "";
	
	Http.RequestBuilder search1 = new Http.RequestBuilder()
			.method(GET)
			.uri("https://api.github.com/search/repositories?q=\"+emptykeyword+\"&per_page=10");
	
	 Result result = route(app, search1);
     assertEquals(OK, result.status());
  }*/

}
