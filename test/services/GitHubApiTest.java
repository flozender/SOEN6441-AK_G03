package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Repository;
import models.Owner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WSClient;
import play.test.Helpers;
import play.test.WithApplication;
import services.github.GitHubApi;
import services.github.GitHubTestApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static play.inject.Bindings.bind;

public class GitHubApiTest extends WithApplication {
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
    public final void testSearchRepositories() {
      GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
      CompletableFuture<List<Repository>> res = testGitHub.searchRepositories("facebook", ws);
      try {
        List<Repository> repositories = res.get();
        assertEquals("facebook-tools-new", repositories.get(0).getName());
        assertEquals("Python", repositories.get(1).getLanguage());
      } catch (Exception e) {
        System.out.println("Exeception: " + e);
      }
    }

    @Test
    public final void testUserProfile() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<JsonNode> res = testGitHub.userProfile("justin", ws);
        try{
            JsonNode user = res.get();
            assertEquals("justin", user.get("login").asText());
            assertEquals("Justin Williams", user.get("name").asText());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public final void testUserRepository() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<JsonNode> res = testGitHub.userRepository("erfan", ws);
        try{
            JsonNode repositories = res.get();
            assertEquals("compose", repositories.get(0).get("name").asText());
            assertEquals("Docker_training_with_DockerMe", repositories.get(1).get("name").asText());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}