package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import models.Repository;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
/**
 * Home Controller Test class
 * 
 * @author Tayeeb Hasan
 */
public class HomeControllerTest extends WithApplication {
    private static Application testApp;
    @Inject private WSClient ws;

    @BeforeClass
        public static void initTestApp() {
        testApp = new GuiceApplicationBuilder()
        .overrides(bind(GitHubApi.class).to(GitHubTestApi.class))
        .build();
        
        Helpers.start(testApp);
    }

    @AfterClass
        public static void stopTestApp() {
        Helpers.stop(testApp);
    }

    /**
     * Test the searchRepositories controller action
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testSearchRepositories() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.searchRepositories(request, "facebook");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            assertThat("Optional[text/html]", is(result.contentType().toString()));
            assertThat(parsedResult, containsString("Welcome to Gitterific!"));
            assertThat(parsedResult, containsString("facebook-tools-new"));
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Test the repositoryProfile controller action
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testRepositoryProfile() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.repositoryProfile("facebook", "jest");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            assertThat("Optional[text/html]", is(result.contentType().toString()));
            assertThat(parsedResult, containsString("facebook/jest"));
            assertThat(parsedResult, containsString("Delightful JavaScript Testing."));
            assertThat(parsedResult, containsString("15062869"));
            assertThat(parsedResult, containsString("TypeScript"));
        } catch (Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Test the getRepositoryIssues controller action
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testGetRepositoryIssues() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.getRepositoryIssues("facebook", "jest");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode repoIssue = mapper.readTree(parsedResult).get(0);
            assertThat("Optional[application/json]", is(result.contentType().toString()));
            assertThat(repoIssue.get("url").textValue(), equalTo("https://api.github.com/repos/facebook/jest/issues/11864"));
            assertThat(repoIssue.get("title").textValue(), equalTo("[Bug]: False negative (instanceof Float32Array)"));
        } catch (Exception e){
            System.out.println(e);
        }
    }
   
    /**
     * Test the getRepositoryContributors controller action
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testGetRepositoryContributors() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.getRepositoryContributors("facebook", "jest");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode repoIssue = mapper.readTree(parsedResult).get(0);
            assertThat("Optional[application/json]", is(result.contentType().toString()));
            assertThat(repoIssue.get("login").textValue(), equalTo("SimenB"));
            assertThat(repoIssue.get("html_url").textValue(), equalTo("https://github.com/SimenB"));
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Test the getRepositoryCommits controller action
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testGetRepositoryCommits() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.getRepositoryCommits("facebook", "jest");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode repoIssue = mapper.readTree(parsedResult).get(0);
            assertThat("Optional[application/json]", is(result.contentType().toString()));
            assertThat(repoIssue.get("sha").textValue(), equalTo("7bb400c373a6f90ba956dd25fe24ee4d4788f41e"));
            assertThat(repoIssue.get("html_url").textValue(), equalTo("https://github.com/facebook/jest/commit/7bb400c373a6f90ba956dd25fe24ee4d4788f41e"));
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
