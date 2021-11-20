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
        CompletableFuture<Owner> res = testGitHub.userProfile("justin", ws);
        try{
            Owner user = res.get();
            assertEquals("justin", user.getLogin());
            assertEquals("Justin Williams", user.getName());
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

    /**
     * Test the repositoryProfile service
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testRepositoryProfile() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<Repository> res = testGitHub.repositoryProfile("facebook", "jest", ws);
        try{
            Repository repository = res.get();
            assertEquals("facebook/jest", repository.getFull_name());
            assertEquals("Delightful JavaScript Testing.", repository.getDescription());
            assertEquals("TypeScript", repository.getLanguage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * Test the getRepositoryIssues service
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testGetRepositoryIssues() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<JsonNode> res = testGitHub.getRepositoryIssues("facebook", "jest", ws);
        try{
            JsonNode issues = res.get().get(0);
            assertEquals("https://api.github.com/repos/facebook/jest/issues/11864", issues.get("url").textValue());
            assertEquals("[Bug]: False negative (instanceof Float32Array)", issues.get("title").textValue());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
        
    /**
     * Test the getRepositoryContributors service
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testGetRepositoryContributors() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<JsonNode> res = testGitHub.getRepositoryContributors("facebook", "jest", ws);
        try{
            JsonNode contributors = res.get().get(0);
            assertEquals("SimenB", contributors.get("login").textValue());
            assertEquals("https://github.com/SimenB", contributors.get("html_url").textValue());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Test the getRepositoryCommits service
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testGetRepositoryCommits() {
        GitHubApi testGitHub = testApp.injector().instanceOf(GitHubApi.class);
        CompletableFuture<JsonNode> res = testGitHub.getRepositoryCommits("facebook", "jest", ws);
        try{
            JsonNode commits = res.get().get(0);
            assertEquals("7bb400c373a6f90ba956dd25fe24ee4d4788f41e", commits.get("sha").textValue());
            assertEquals("https://github.com/facebook/jest/commit/7bb400c373a6f90ba956dd25fe24ee4d4788f41e", commits.get("html_url").textValue());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
