package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import models.Owner;
import models.Repository;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import services.github.GitHubApi;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 * This controller contains several actions to handle HTTP requests
 * to the application's home page.
 */
/**
 * @author Pedram & Tayeeb Hasan
 * @since 1.1.0
 * @version 1.1.3
 * 
 */
public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Hashtable<String, ArrayList<List<Repository>>> storage;
    private Hashtable<String, ArrayList<String>> searchTerms;

    private final GitHubApi ghImpl;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    /**
     * @param ws Handles the ws dependency for sending HTTP requests
     *
     *
     */
    @Inject
    public HomeController(WSClient ws, GitHubApi gitHubApi) {
        this.ws = ws;
        this.ghImpl = gitHubApi;
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
     * @param request Contains the HTTP request
     * @return Empty index page that contains the search bar
     * @author Pedram & Tayeeb Hasan
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
     * @author Pedram & Tayeeb Hasan
     *
     * @param request Contains the HTTP request
     * @param keywords Contains the keywords which user entered in the search bar
     * @return index page that contains search results (repositories)
     * 
     * It searches for the repositories matching the string passed by the user in the search bar.
     * <p>
     * It will generate the results related to the search string and render on the page.
     * The result will include username and the repository name and topics related to each repository.
     * </p>
     * 
     * 
     */
  
    public CompletionStage<Result> searchRepositories(Http.Request request, String keywords) {
        String userSession = request.cookie("GITTERIFIC").value();
        CompletableFuture<List<Repository>> res = ghImpl.searchRepositories(keywords, ws);
        return res.thenApplyAsync((List<Repository> tempResponse) -> {
            try {
                ArrayList<List<Repository>> collectRepos = new ArrayList<>();
                ArrayList<String> collectSearchTerms = new ArrayList<>();
                collectRepos.add(tempResponse);
                collectSearchTerms.add(keywords);
                if (this.storage.containsKey(userSession)){
                    ArrayList<List<Repository>> tempStorage = this.storage.get(userSession);
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
     * @author Pedram
     *
     * @param username Contains the github username of the user
     * @return The user page containing all the information about the requested user
     */
    public CompletionStage<Result> userProfile(String username) {
        CompletableFuture<JsonNode> user = ghImpl.userProfile(username, ws);

        return user.thenApplyAsync(response -> {
            try {
                Owner userProfileInfo = Json.fromJson(response, Owner.class);
                return ok(views.html.user.render(userProfileInfo));
            }catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return ok(views.html.error.render());
            }
        });
    }
    
    
    /**
     * @author Pedram Nouri
     * @param username Contains the github username of the user
     * @return A Json file that consume by the user page in order to show all the repositories of the user
     * 
     * Method userRepository returns the results based on the username passed 
     * to it. It is an Async call.
     * 
     * The response contains all the public information of the user. Also, returns
     * repositories of the current user.
     *
     */
    public CompletionStage<Result> userRepository(String username) {

        CompletableFuture<JsonNode> repos = ghImpl.userRepository(username, ws);
        
        return repos.thenApplyAsync(response -> {
            try{
                return ok((response));
            }catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return ok(views.html.error.render());
            }
        });
    }

    public CompletionStage<Result> repositoryProfile(String username, String repository) {
        String clientSecret = "fc2fc9c20d3586664dd0d3e0799b0f5be456a462";
        String url = "https://bb94d78479b70367def7:"+clientSecret+"@api.github.com/repos/" + username + "/" + repository;
        
        return ws.url(url).get().thenApplyAsync(response -> {
            Repository repo = Json.fromJson(response.asJson(), Repository.class);
            return ok(views.html.repo.render(username, repo));
        });
    }

    public CompletionStage<Result> getRepositoryIssues(String username, String repository) {
        String clientSecret = "fc2fc9c20d3586664dd0d3e0799b0f5be456a462";
        String url = "https://bb94d78479b70367def7:"+clientSecret+"@api.github.com/repos/" + username + "/" + repository + "/issues?per_page=20&sort=updated";

        return ws.url(url).get().thenApplyAsync(response -> ok((response.asJson())));
    }
    
    /**
     * @author Nazanin
     * @version 1.1.5
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

    
    public CompletionStage<Result> getRepositoryIssuesTittles(String username, String repository) {
        String clientSecret = "fc2fc9c20d3586664dd0d3e0799b0f5be456a462";
        String url = "https://bb94d78479b70367def7:"+clientSecret+"@api.github.com/repos/" + username + "/" + repository + "/issues";
        
        return ws.url(url).get().thenApplyAsync(response -> {
            try {
                JsonNode tempResponse = response.asJson();
                ArrayList<String> issuetitles = new ArrayList<>();
                tempResponse.forEach(item -> {
                    issuetitles.add(item.get("title").textValue());
                });
                return ok(this.repoIssuesStats(issuetitles).toString());
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return ok(views.html.error.render());
            }
        });
        
    } 

    /**
     * @author Nazanin
     * @version 1.1.5
     * @since 1.1.3
     * @param tittles
     * @return
     * 
     * Method computes a word level statistics of the issues titles.
     * It takes input of the titles. All titles are compared and collected into a list in a reverse order.
     * 
     * example https://api.github.com/repos/octocat/hello-world/issues
     * 
     */
    public Map<String, Integer> repoIssuesStats (List<String> titles) {
    	ArrayList<String> strValues = new ArrayList<>();
    	titles.forEach(item -> {
    		String[] val = item.split(" ");
    		for(int count=0; count < val.length; count++) {
    			strValues.add(val[count]);
    		}
    	});
    	List<String> words = strValues.stream()
                .map(String::toLowerCase)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long> comparingByValue(reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .limit(100)
                .map(Map.Entry::getKey)
                .collect(toList());
    	
    	Map<String, Integer> counts = words.parallelStream().
                collect(Collectors.toConcurrentMap(
                    w -> w, w -> 1, Integer::sum));
    	
    	counts.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue(reverseOrder()).thenComparing(Map.Entry.comparingByKey())).collect(toList());
    	return counts;
    	
    }

    public CompletionStage<Result> getRepositoryContributors(String username, String repository) {
        String clientSecret = "fc2fc9c20d3586664dd0d3e0799b0f5be456a462";
        String url = "https://bb94d78479b70367def7:"+clientSecret+"@api.github.com/repos/" + username + "/" + repository + "/contributors";

        return ws.url(url).get().thenApplyAsync(response -> ok((response.asJson())));
    }
    
    public CompletionStage<Result> getRepositoryCommits(String username, String repository) {
        String clientSecret = "fc2fc9c20d3586664dd0d3e0799b0f5be456a462";
        String url = "https://bb94d78479b70367def7:"+clientSecret+"@api.github.com/repos/" + username + "/" + repository + "/commits";

        return ws.url(url).get().thenApplyAsync(response -> ok((response.asJson())));
    }
}
