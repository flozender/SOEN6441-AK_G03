package views;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.Owner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import models.Repository;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.test.Helpers;
import play.test.WithApplication;
import play.twirl.api.Content;
import services.github.GitHubApi;
import services.github.GitHubTestApi;
import play.mvc.Result;
import play.mvc.Http.RequestBuilder;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import static play.inject.Bindings.bind;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import play.test.Helpers;

import static org.junit.Assert.assertEquals;

public class ViewTest {
    
    /**
     * Test the index template
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public void renderIndex() {
        Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
        RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
        Request request = requestBuilder.build();
        Content html = views.html.index.render(request);
        assertThat("text/html", is(html.contentType()));
        assertThat(html.body(), containsString("Welcome to Gitterific!"));
    }

    /**
     * Test the repo template
     * 
     * @author Tayeeb Hasan
     */
    @Test
    public void renderRepo() {
        Path fileName = Paths.get("./app/services/github/resources/repositoryProfile.json");
        Charset charset = Charset.forName("ISO-8859-1");
        String jsonString = "";
        try {
            List<String> lines = Files.readAllLines(fileName, charset);
            for (String line : lines) {
            jsonString += line;
            }
        } 
        catch (IOException e) {
            System.out.println(e);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonString);
            Repository repository = Json.fromJson(node, Repository.class);
            Content html = views.html.repo.render("facebook", repository);
            assertThat("text/html", is(html.contentType()));
            assertThat(html.body(), containsString("facebook/jest"));
            assertThat(html.body(), containsString("Delightful JavaScript Testing."));
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }


    /**
     * Test the user profile template
     *
     * @author Pedram Nouri
     */
    @Test
    public void testUserProfileTemplate() {
        Path fileName = Paths.get("./app/services/github/resources/userProfile.json");
        Charset charset = Charset.forName("ISO-8859-1");
        String jsonString = "";
        try {
            List<String> lines = Files.readAllLines(fileName, charset);
            for (String line : lines) {
                jsonString += line;
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonString);
            Owner user = Json.fromJson(node, Owner.class);
            Cookie cookie = Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build();
            RequestBuilder requestBuilder = Helpers.fakeRequest().cookie(cookie);
            Request request = requestBuilder.build();
            Content html = views.html.user.render(request);
            assertThat("text/html", is(html.contentType()));
            assertThat(html.body(), containsString("https://justinw.me"));
            assertThat(html.body(), containsString("https://avatars.githubusercontent.com/u/1384?v=4"));
            assertThat(html.body(), containsString("Justin Williams"));
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
