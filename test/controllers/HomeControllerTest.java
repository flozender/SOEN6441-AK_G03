package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
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
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static play.inject.Bindings.bind;

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
      public final void testSearchRepositories() {
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
    public final void ownerTest() throws Exception{
        Owner o1 = new Owner();
        String stringDate="31/12/2018";
        String stringTestDate = "31/12/2018";
        Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse(stringTestDate);

        o1.setName("Pedram Nouri");
        o1.setEmail("noori.pedram816@gmail.com");
        o1.setCompany("Concordia");
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        o1.setCreated_at(date);
        o1.setFollowers(49);
        o1.setFollowing(29);
        o1.setLogin("pedramnoori");
        o1.setHtml_url("github.com/pedramnoori");
        o1.setPublic_repos(12);

        assertEquals("Pedram Nouri", o1.getName());
        assertEquals("noori.pedram816@gmail.com", o1.getEmail());
        assertEquals("Concordia", o1.getCompany());
        assertEquals(testDate, o1.getCreated_at());
        assertEquals(49, o1.getFollowers());
        assertEquals(29, o1.getFollowing());
        assertEquals("pedramnoori", o1.getLogin());
        assertEquals("github.com/pedramnoori", o1.getHtml_url());
        assertEquals(12, o1.getPublic_repos());

    }

}
