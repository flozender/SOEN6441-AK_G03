package controllers;

import org.junit.*;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import play.test.Helpers;
import play.libs.ws.*;
import org.junit.Test;


import static play.inject.Bindings.bind;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import services.github.GitHubApi;
import services.github.GitHubTestApi;

public class HomeControllerTest extends WithApplication {
    private static Application testApp;
    @Inject private WSClient ws;

    @BeforeClass
      public static void initTestApp() {
      testApp = new GuiceApplicationBuilder()
      .overrides(bind(GitHubApi.class).to(GitHubTestApi.class))
      .build();
    }

    @AfterClass
      public static void stopTestApp() {
      Helpers.stop(testApp);
    }

    @Test
      public final void testEmptySearch() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<JsonNode> res = testGitHub.searchRepositories("facebook", ws);
        try{
          JsonNode repositories = res.get();
          assertEquals("facebook-tools-new", repositories.get(0).get("name").asText());
          assertEquals("Python", repositories.get(1).get("language").asText());
        } catch (Exception e) {
          System.out.println(e);
      }
    }

}
