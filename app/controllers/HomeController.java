package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import actors.SearchActor;

import models.Owner;
import models.Repository;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.*;
import services.github.GitHubApi;

import akka.actor.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import scala.compat.java8.FutureConverters;
import actors.GitHubActorProtocol;
import actors.*;
import akka.stream.*;
import play.libs.streams.ActorFlow;


import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static akka.pattern.Patterns.ask;


/**
 * This controller contains several actions to handle HTTP requests
 * for the web application.
 * @author Pedram Nouri & Tayeeb Hasan
 * @version 1.0.0
 * 
 */
public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Hashtable<String, ArrayList<List<Repository>>> storage;
    private Hashtable<String, ArrayList<String>> searchTerms;
    private final GitHubApi ghImpl;
    private final ActorSystem system;
    private final Materializer materializer;
    // final ActorRef supervisorActor;

    /**
     * Home Controller Constructor
     * 
     * @param ws Handles the ws dependency for sending HTTP requests
     * @param gitHubApi Handles the GitHubApi service
     * @author Pedram Nouri & Tayeeb Hasan
     * @return HomeController instance
     */
    @Inject
    public HomeController(WSClient ws, GitHubApi gitHubApi, ActorSystem system, Materializer materializer) {
        this.ws = ws;
        this.ghImpl = gitHubApi;
        this.storage = new Hashtable<>();
        this.searchTerms = new Hashtable<>();
        this.system = system;
        this.materializer = materializer;
    }

    /**
     * index page action
     * 
     * @param request Contains the HTTP request
     * @author Pedram Nouri & Tayeeb Hasan
     * @return index view that contains the search bar and previously searched results
     * @version 1.0.0
     */
    public Result index(Http.Request request) {
        String userSession;
        if (request.cookie("GITTERIFIC") != null){
            userSession = request.cookie("GITTERIFIC").value();
        } else {
            userSession = String.valueOf(Math.random());
        }
        if (request.cookie("GITTERIFIC") == null){
            return ok(views.html.index.render(request))
            .withCookies(Cookie.builder("GITTERIFIC", userSession).build());
        }
        return ok(views.html.index.render(request));
    }

    /**
     * It creates the WS connection for the repositories search page.
     * <p>
     * It will generate the results related to the search string
     * The result will include username and the repository name and topics related to each repository.
     * </p>
     * @author Tayeeb Hasan
     * @return ws connection contains search results (repositories)
     * 
     */
    public WebSocket wsSearch() {
        return WebSocket.Text.accept(
            request -> ActorFlow.actorRef((r)->SearchSocketActor.props(r, ws, ghImpl), system, materializer));
    }

    /**
     * It creates the WS connection for the user profile page.
     * <p>
     * It will generate the results related to the specified user
     * The result will include all the information of the user, along with his/her repositories.
     * </p>
     * @author Pedram Nouri
     * @return ws connection contains user profile
     *
     */
    public WebSocket wsUserProfile() {
        return WebSocket.Text.accept(
            request -> ActorFlow.actorRef((r)->UserProfileSocketActor.props(r, ws, ghImpl), system, materializer));
    }
    
    
    
    /**
     * It creates the WS connection for the user profile page.
     * <p>
     * It will generate the results related to the specified user
     * The result will include all the information of the user, along with his/her repositories.
     * </p>
     * @author Nazanin
     * @return ws connection contains user profile
     *
     */
    public WebSocket wsRepositoryIssuesTittles() {
        return WebSocket.Text.accept(
            request -> ActorFlow.actorRef((r)->RepoIssuesTitleActor.props(r, ws, ghImpl), system, materializer));
    }
    
    

    /**
     * It creates the WS connection for the user repositories.
     * <p>
     * It will generate the results related to the specified user (his/her repositories)
     * The result will include all user's repositories.
     * </p>
     * @author Pedram Nouri
     * @return ws connection contains user repositories
     *
     */
    public WebSocket wsUserRepositories() {
        return WebSocket.Text.accept(
                request -> ActorFlow.actorRef((r)->UserRepositorySocketActor.props(r, ws, ghImpl), system, materializer));
    }

    /**
     * It searches for the repositories matching the string passed (topic) by the user's click from the topics.
     * <p>
     * It will generate the results related to the user given topic keyword and render the repositories on topic_repos view.
     * The result will include username and the repository name and topics related to each repository.
     * </p>
     * @author Vedasree Reddy Sapatapu
     * @param request Contains the HTTP request
     * @param keyword Contains the keywords which the user clicked from the topics
     * @return topic_repos page that contains search results (repositories) of the clicked topic
     *
     */

    public CompletionStage<Result> searchTopicRepositories(Http.Request request, String keyword) {
        CompletableFuture<List<Repository>> res = ghImpl.searchTopicRepositories(keyword, ws);
        return res.thenApplyAsync((List<Repository> tempResponse) -> {
            try {
                return ok(views.html.topic_repos.render(tempResponse, keyword));
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return ok(views.html.error.render());
            }
        });
    }

    /**
     * @author Pedram Nouri
     * @param username Contains the github username of the user
     * @param request Contains the HTTP request
     * @return The user page containing all the information about the requested user
     */
    public Result userProfile(String username, Http.Request request) {
        try {
            return ok(views.html.user.render(request));
        }catch (Exception e) {
            System.out.println("CAUGHT EXCEPTION: " + e);
            return badRequest(views.html.error.render());
        }
    }
    
    
    /**
     * Method userRepository returns the results based on the username passed 
     * to it. It is an Async call.
     * 
     * The response contains all the public information of the user. Also, returns
     * repositories of the current user.
     *
     * @author Pedram Nouri
     * @param username Contains the github username of the user
     * @return A Json file that consume by the user page in order to show all the repositories of the user
     */
    public CompletionStage<Result> userRepository(String username) {

        CompletableFuture<JsonNode> repos = ghImpl.userRepository(username, ws);
        
        return repos.thenApplyAsync(response -> {
            try{
                return ok((response));
            }catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest(views.html.error.render());
            }
        });
    }

    /**
     * Method repositoryProfile returns the details of the repository based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the repository. Also, displays the 20 latest issues
     * of the repository.
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @return the Repo view with the public information of the requested repository
     */
    public CompletionStage<Result> repositoryProfile(String username, String repository) {
        CompletableFuture<Repository> repo = ghImpl.repositoryProfile(username, repository, ws);
        
        return repo.thenApplyAsync(response -> {
            try {
                return ok(views.html.repo.render(username, response));
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest(views.html.error.render());
            }
        });
    }

     /**
     * It creates the WS connection for the repository profile page.
     * <p>
     * It will generate the results related to the repository name string
     * The result will include the repository name, topics, etc.
     * </p>
     * @author Tayeeb Hasan
     * @return ws connection contains search results (repositories)
     * 
     */
    public WebSocket wsRepositoryProfile() {
        return WebSocket.Text.accept(
            request -> ActorFlow.actorRef((r)->RepositoryProfileSocketActor.props(r, ws, ghImpl), system, materializer));
    }
    
    /**
     * Method getRepositoryIssues returns a JSON with the 20 latest issues based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the issues. 
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @return A JSON containing all the public information of the requested repository issues
     */
    public CompletionStage<Result> getRepositoryIssues(String username, String repository) {
        CompletableFuture<JsonNode> res = ghImpl.getRepositoryIssues(username, repository, ws);
        return res.thenApplyAsync(response -> {
            try {
                return ok(response);
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest("Invalid request!");
            }
        });
    }
    
    /**
     * Method getRepositoryContributors returns a JSON with the contributors based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the contributors. 
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @return A JSON containing all the public information of the requested repository contributors
     */
    public CompletionStage<Result> getRepositoryContributors(String username, String repository) {
        CompletableFuture<JsonNode> res = ghImpl.getRepositoryContributors(username, repository, ws);
        return res.thenApplyAsync(response -> {
            try {
                return ok(response);
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest("Invalid request!");
            }
        });
    }
    
    /**
     * Method getRepositoryCommits returns a JSON with the 100 latest commits based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the commits. 
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @return A JSON containing all the public information of the requested repository commits
     */
    public CompletionStage<Result> getRepositoryCommits(String username, String repository) {
        CompletableFuture<JsonNode> res = ghImpl.getRepositoryCommits(username, repository, ws);
        return res.thenApplyAsync(response -> {
            try {
                return ok(response);
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest("Invalid request!");
            }
        });
    }
    
    /**
     * @author Nazanin
     * @version 1.1.7
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
        CompletableFuture<JsonNode> res = ghImpl.getRepositoryIssuesTittles(username, repository, ws);
        return res.thenApplyAsync(response -> {
            try {
                JsonNode tempResponse = response;
                ArrayList<String> issuetitles = new ArrayList<>();
                tempResponse.forEach(item -> {
                    issuetitles.add(item.get("title").textValue());
                });
                return ok(views.html.repoissues.render(this.repoIssuesStats(issuetitles).toString().replace("{", "").replace("}", "").replace("=", "      =      ")));
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest("Invalid request!");
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
    public Map<String, Long> repoIssuesStats (List<String> titles) {
    	ArrayList<String> strValues = new ArrayList<>();
    	titles.forEach(item -> {
    		String[] val = item.split(" ");
    		for(int count=0; count < val.length; count++) {
    			strValues.add(val[count]);
    		}
    	});
    	
    	Map<String, Long> counts =  strValues.stream().collect(groupingBy(Function.identity(), counting()));
    	
    	Map<String, Long> reverseCounts = new LinkedHashMap<>();
    	 
    	counts.entrySet()
    	    .stream()
    	    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
    	    .forEachOrdered(x -> reverseCounts.put(x.getKey(), x.getValue()));
    	
	 	return reverseCounts;
	 	
	 	
    	
    }

   
}
