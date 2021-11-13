package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.Owner;
import models.Repository;
import models.Response;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Cookie;
import scala.util.parsing.combinator.token.StdTokens.Keyword;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.concurrent.CompletionStage;
import play.mvc.Result;

/**
 * This controller contains several actions to handle HTTP requests
 * to the application's home page.
 */
/**
 * @author 
 * @since 1.1.0
 * @version 1.1.3
 * 
 */
public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Hashtable<String, ArrayList<JsonNode>> storage;
    private Hashtable<String, ArrayList<String>> searchTerms;

    @Inject
    public HomeController(WSClient ws) {
        this.ws = ws;
        this.storage = new Hashtable<>();
        this.searchTerms = new Hashtable<>();
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    /**
     * @param request
     * @return
     * 
     * @version 1.1.2
     * @since 1.1.0
     */
    
    
    public Result index(Http.Request request) {
        if (request.cookie("GITTERIFIC") != null){
            String userSession = request.cookie("GITTERIFIC").value();
            this.storage.put(userSession, new ArrayList<>());
            this.searchTerms.put(userSession, new ArrayList<>());
            System.out.println("Cleared storage " + this.storage.get(userSession));
        }
        return ok(views.html.index.render(Arrays.asList(), Arrays.asList())).withCookies(Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build());
    }


    /**
     * @author {FN}
     * @version 1.1.2
     * @since 1.1.0
     *
     * @param request
     * @param keywords
     * @return
     * 
     * It search for the repositories matching the string passed by the user in the search bar.
     * <p>
     * It will generate the results related to the search string and render on the page.
     * The result will include username and the repository name.
     * </p>
     * 
     * 
     */
  
    public CompletionStage<Result> searchRepositories(Http.Request request, String keywords) {
        JsonNode body = request.body().asJson();
        String userSession = request.cookie("GITTERIFIC").value();
        String url = "https://api.github.com/search/repositories?q="+keywords+"&per_page=10&sort=updated";
        return ws.url(url).get().thenApplyAsync(response -> {
            try {
                JsonNode tempResponse = response.asJson().get("items");
                ArrayList<JsonNode> collectRepos = new ArrayList<>();
                ArrayList<String> collectSearchTerms = new ArrayList<>();
                collectRepos.add(tempResponse);
                collectSearchTerms.add(keywords);
                if (this.storage.containsKey(userSession)){
                    ArrayList<JsonNode> tempStorage = this.storage.get(userSession);
                    ArrayList<String> tempSearchTerms = this.searchTerms.get(userSession);
                    tempStorage.stream().limit(9).forEach(e->collectRepos.add(e));
                    tempSearchTerms.stream().limit(9).forEach(e->collectSearchTerms.add(e));
                }
                this.storage.put(userSession, collectRepos);
                this.searchTerms.put(userSession, collectSearchTerms);
                return ok(views.html.index.render(this.storage.get(userSession), this.searchTerms.get(userSession)));
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return ok(views.html.error.render());
            }
        });
    }

    /**
     * @author {FN}
     * @since 1.1.3
     * @version 1.1.3
     * {@summary} returns the user profile information.
     *
     * @param username
     * @return
     */
    public CompletionStage<Result> userProfile(String username) {
        String url = "https://api.github.com/users/" + username;
        return ws.url(url).get().thenApplyAsync(response -> {
            Owner user = Json.fromJson(response.asJson(), Owner.class);
            return ok(views.html.user.render(user));
        });
    }

    /**
     * @author 
     * @apiNote gets information related to user's repository
     * @version 1.1.3
     * @since 1.1.3
     * @param username
     * @return
     * 
     * Method userRepository returns the results based on the username passed 
     * to it. It is a Async call.
     * 
     * The response contains all the public information of the user. Also, returns
     * repositories of the current user.
     *
     */
    public CompletionStage<Result> userRepository(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        return ws.url(url).get().thenApplyAsync(response -> ok((response.asJson())));
    }



    /**
     * @author Nazanin
     * @version 1.1.3
     * @since 1.1.3
     * @param username
     * @return
     * @see API https://docs.github.com/en/rest/reference/issues#list-repository-issue
     * 
     * Method returns all the repository issues of the parsed user and repository.
     * It is an Aysnc call. 
     * 
     * example https://api.github.com/repos/octocat/hello-world/issues
     * 
     */
    public CompletionStage<Result> userRepositoryIssues(String username, String repository) {
        String url = "https://api.github.com/repos/" + username +"/"+ repository +"/issues";
        return ws.url(url).get().thenApplyAsync(response -> ok((response.asJson())));

    }




    }
