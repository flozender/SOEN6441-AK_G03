package actors;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import models.Repository;

import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
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
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import akka.actor.Props;
import actors.GitHubActorProtocol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;

/**
 * Test the GithubActorProtocols
 *
 * @author Tayeeb Hasan
 */
public class GitHubActorProtocolTest {

    /**
     * Test the Search protocol
     *
     * @author Tayeeb Hasan
     */
    @Test
    public void testSearchProtocol() {
        GitHubActorProtocol.Search search = new GitHubActorProtocol.Search("facebook");
        assertEquals(search.keywords, "facebook");
    }

    /**
     * Test the SearchResults protocol
     *
     * @author Tayeeb Hasan
     */
    @Test
    public void testSearchResultsProtocol() {
        boolean update = false;
        int index = 3;
        Repository repo = new Repository();
        repo.setName("Repository 1");

        List<Repository> repoList = Arrays.asList(repo);

        ArrayList<List<Repository>> repoArray = new ArrayList<>();
        ArrayList<String> searchTerms = new ArrayList<>();
        repoArray.add(repoList);
        searchTerms.add("searching");

        GitHubActorProtocol.SearchResults search = new GitHubActorProtocol.SearchResults(repoArray, searchTerms, update, 3);
        assertEquals(search.repositories.get(0).get(0).getName(), "Repository 1");
        assertEquals(search.searchTerms.get(0), "searching");
        assertEquals(search.update, update);
        assertEquals(search.index, index);
    }

    /**
     * Test the RepositoryProfile protocol
     *
     * @author Tayeeb Hasan
     */
    @Test
    public void testRepositoryProfileProtocol() {
        GitHubActorProtocol.RepositoryProfile repositoryProfile = new GitHubActorProtocol.RepositoryProfile("facebook", "jest");
        assertEquals(repositoryProfile.username, "facebook");
        assertEquals(repositoryProfile.repository, "jest");
    }

    /**
     * Test the RepositoryInformation protocol
     *
     * @author Tayeeb Hasan
     */
    @Test
    public void testRepositoryInformationProtocol() {
        String userId = "facebook";

        Repository repository = new Repository();
        repository.setName("jest");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode issues = mapper.createObjectNode();
        JsonNode commits = mapper.createObjectNode();
        JsonNode contributors = mapper.createObjectNode();

        GitHubActorProtocol.RepositoryInformation repositoryInformation = new GitHubActorProtocol.RepositoryInformation(userId, repository, issues, commits, contributors);
        assertEquals(repositoryInformation.userId, "facebook");
        assertEquals(repositoryInformation.repository.getName(), "jest");
    }
}
