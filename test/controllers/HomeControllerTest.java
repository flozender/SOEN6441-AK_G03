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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;

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
        CompletionStage<Result> preCSResult = controller.searchRepositories(request, "facebook");
        try{
            Result preResult = preCSResult.toCompletableFuture().get();
            // simulate a second search request
            CompletionStage<Result> csResult = controller.searchRepositories(request, "facebook");
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
     * Test the searchTopicRepositories controller action
     *
     * @author Vedasree Reddy Sapatapu
     */
    @Test
    public final void testSearchTopicRepositories() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.searchTopicRepositories(request, "facebook");

    /**
     * Test the searchRepositories controller action without cookie
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public final void testSearchRepositoriesWithoutCookie() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        RequestBuilder requestBuilder = Helpers.fakeRequest();
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.searchRepositories(request, "facebook");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            assertThat("Optional[text/html]", is(result.contentType().toString()));

            assertThat(parsedResult, containsString("FBLinkTester"));
            assertThat(parsedResult, containsString("facebook-login-page"));

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
            JsonNode contributors = mapper.readTree(parsedResult).get(0);
            assertThat("Optional[application/json]", is(result.contentType().toString()));
            assertThat(contributors.get("login").textValue(), equalTo("SimenB"));
            assertThat(contributors.get("html_url").textValue(), equalTo("https://github.com/SimenB"));
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

    /**
     * Test the userRepository controller action
     *
     * @author Pedram Nouri
     */
    @Test
    public final void testUserRepository() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.userRepository("erfan-gh");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userRepository = mapper.readTree(parsedResult).get(0);
            assertThat("Optional[application/json]", is(result.contentType().toString()));
            assertThat(userRepository.get("owner").get("login").textValue(), equalTo("erfan-gh"));
            assertThat(userRepository.get("html_url").textValue(), equalTo("https://github.com/erfan-gh/compose"));
            assertThat(userRepository.get("teams_url").textValue(), equalTo("https://api.github.com/repos/erfan-gh/compose/teams"));

        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Test the userProfile controller action
     *
     * @author Pedram Nouri
     */
    @Test
    public final void testUserProfile() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        CompletionStage<Result> csResult = controller.userProfile("justin");
        try{
            Result result = csResult.toCompletableFuture().get();
            String parsedResult = Helpers.contentAsString(result);
            assertThat("Optional[text/html]", is(result.contentType().toString()));
            assertThat(parsedResult, containsString("Justin Williams"));
            assertThat(parsedResult, containsString("Mobile application developer located in sunny Denver, Colorado."));

        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Test the index controller action
     *
     * @author Pedram Nouri
     */
    @Test
    public final void testIndex() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        Result csResult = controller.index(request);
        try{
            String parsedResult = Helpers.contentAsString(csResult);
            assertThat("Optional[text/html]", is(csResult.contentType().toString()));
            assertThat(parsedResult, containsString("Welcome to Gitterific!"));
        } catch (Exception e){
            System.out.println(e);
        }
    }


    /**
     * Test the index controller action without user cookie
     *
     * @author Tayeeb Hasan
     */
    @Test
    public final void testIndexWithoutCookie() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        RequestBuilder requestBuilder = Helpers.fakeRequest();
        Request request = requestBuilder.build();
        Result csResult = controller.index(request);
        try{
            String parsedResult = Helpers.contentAsString(csResult);
            assertThat("Optional[text/html]", is(csResult.contentType().toString()));
            assertThat(parsedResult, containsString("Welcome to Gitterific!"));
            assertThat(csResult.cookie("GITTERIFIC"), is(notNullValue()));
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Test the index controller action when a search has already been performed
     *
     * @author Tayeeb Hasan
     */
    @Test
    public final void testIndexWithSearch() {
        final HomeController controller = testApp.injector().instanceOf(HomeController.class);
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();

        CompletionStage<Result> csResult = controller.searchRepositories(request, "facebook");

        try{
            Result result = csResult.toCompletableFuture().get();
            Result indexPage = controller.index(request);
            String parsedResult = Helpers.contentAsString(indexPage);
            assertThat("Optional[text/html]", is(indexPage.contentType().toString()));
            assertThat(parsedResult, containsString("Welcome to Gitterific!"));
            assertThat(parsedResult, containsString("facebook-tools-new"));
        } catch (Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Test the helper method for getRepositoryIssuesTittles. It computes the word stats on a parsed list of issues
     *
     * @author Nazanin
     */
    @Test
    public final void testrepoIssuesStats() {
    	
    	String inputstr = "Bump url-parse from 1.5.1 to 1.5.3, Bump tmpl from 1.0.4 to 1.0.5, Bump path-parse from 1.0.6 to 1.0.7, Bump merge-deep from 3.0.2 to 3.0.3, Bump dns-packet from 1.3.1 to 1.3.4, Bump lodash from 4.17.15 to 4.17.21, Bump hosted-git-info from 2.8.4 to 2.8.9, Bump url-parse from 1.4.7 to 1.5.1, Bump ssri from 6.0.1 to 6.0.2, Bump y18n from 3.2.1 to 3.2.2, Bump http-proxy from 1.17.0 to 1.18.1, Bump dot-prop from 4.2.0 to 4.2.1, Bump handlebars from 4.1.2 to 4.7.7, Bump yargs-parser from 5.0.0 to 5.0.1, Bump elliptic from 6.5.0 to 6.5.4";
    	List<String> inputList = Arrays.asList(inputstr.split("\\s*,\\s*"));
    
    	String outputstr ="Bump=15, from=15, to=15, url-parse=2, 1.5.1=2, 4.17.21=1, lodash=1, tmpl=1, 4.2.0=1, 1.4.7=1, 4.1.2=1, 4.2.1=1, path-parse=1, 6.5.4=1, dot-prop=1, merge-deep=1, yargs-parser=1, 2.8.9=1, 2.8.4=1, handlebars=1, 3.0.2=1, 3.2.1=1, 3.0.3=1, 3.2.2=1, ssri=1, http-proxy=1, hosted-git-info=1, 6.5.0=1, 1.3.1=1, 1.0.4=1, 6.0.2=1, 1.0.5=1, 1.0.6=1, 4.7.7=1, 1.3.4=1, 1.0.7=1, 6.0.1=1, 1.5.3=1, dns-packet=1, 5.0.0=1, 1.17.0=1, y18n=1, 5.0.1=1, 1.18.1=1, elliptic=1, 4.17.15=1";
    	Map<String,Long> resultmap = Arrays.stream(outputstr.split(","))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0].toString(), s -> Long.parseLong(s[1])));
    	
    	
    	final HomeController controller = testApp.injector().instanceOf(HomeController.class);
    	
    	Map<String,Long> actualMap = controller.repoIssuesStats(inputList);
    			
    	try {
    		assertTrue("Size matches", resultmap.size()==actualMap.size());

    		assertTrue("element present as expected", actualMap.get("Bump")==resultmap.get("Bump"));
    		
    	} catch (Exception e){
    		System.out.print(e);
    	}
    }
    
    /**
     * Test getRepositoryIssuesTittles controller action
     *
     * @author Nazanin
     */
    
    @Test
    public final void testgetRepositoryIssuesTittles() {
    	//http://localhost:9000/reposissues/m4thieulavoie/brorganized/issues
    	 final HomeController controller = testApp.injector().instanceOf(HomeController.class);
         Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
         RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
         Request request = requestBuilder.build();
         CompletionStage<Result> csResult = controller.getRepositoryIssuesTittles("m4thieulavoie","brorganized");
         try{
             Result result = csResult.toCompletableFuture().get();
             String parsedResult = Helpers.contentAsString(result);
             assertThat("Optional[text/html]", is(result.contentType().toString()));
             assertThat(parsedResult, containsString("Bump      =      15"));
             assertThat(parsedResult, containsString("repository-results"));

         } catch (Exception e){
             System.out.println(e);
         }
    		
    }
    
    /**
     * Test the helper method for getRepositoryIssuesTittles for negative input values. It computes the word stats on a parsed list of issues
     *
     * @author Nazanin
     */
    @Test
    public final void testrepoIssuesStatsNegate() {
    	
    	String inputstr = "Bump url-parse from 1.5.1 to 1.5.3, Bump tmpl from 1.0.4 to 1.0.5, Bump path-parse from 1.0.6 to 1.0.7, Bump merge-deep from 3.0.2 to 3.0.3, Bump dns-packet from 1.3.1 to 1.3.4, Bump lodash from 4.17.15 to 4.17.21, Bump hosted-git-info from 2.8.4 to 2.8.9, Bump url-parse from 1.4.7 to 1.5.1, Bump ssri from 6.0.1 to 6.0.2, Bump y18n from 3.2.1 to 3.2.2, Bump http-proxy from 1.17.0 to 1.18.1, Bump dot-prop from 4.2.0 to 4.2.1, Bump handlebars from 4.1.2 to 4.7.7, Bump yargs-parser from 5.0.0 to 5.0.1, Bump elliptic from 6.5.0 to 6.5.4";
    	List<String> inputList = Arrays.asList(inputstr.split("\\s*,\\s*"));
    
    	String outputstr ="to=15, url-parse=2, 1.5.1=2, 4.17.21=1, lodash=1, tmpl=1, 4.2.0=1, 1.4.7=1, 4.1.2=1, 4.2.1=1, path-parse=1, 6.5.4=1, dot-prop=1, merge-deep=1, yargs-parser=1, 2.8.9=1, 2.8.4=1, handlebars=1, 3.0.2=1, 3.2.1=1, 3.0.3=1, 3.2.2=1, ssri=1, http-proxy=1, hosted-git-info=1, 6.5.0=1, 1.3.1=1, 1.0.4=1, 6.0.2=1, 1.0.5=1, 1.0.6=1, 4.7.7=1, 1.3.4=1, 1.0.7=1, 6.0.1=1, 1.5.3=1, dns-packet=1, 5.0.0=1, 1.17.0=1, y18n=1, 5.0.1=1, 1.18.1=1, elliptic=1, 4.17.15=1";
    	Map<String,Long> resultmap = Arrays.stream(outputstr.split(","))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0].toString(), s -> Long.parseLong(s[1])));
    	
    	
    	final HomeController controller = testApp.injector().instanceOf(HomeController.class);
    	
    	Map<String,Long> actualMap = controller.repoIssuesStats(inputList);
    			
    	try {
    		assertFalse("Size is different as expected in a negative case test", resultmap.size()==actualMap.size());

    		
    	} catch (Exception e){
    		System.out.print(e);
    	}
    }
    
    
}
