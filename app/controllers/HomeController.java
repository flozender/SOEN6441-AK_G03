package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import actors.SearchActor;
import actors.SearchGroupActor;

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
import play.mvc.Result;
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
    final ActorRef searchGroupActor;
    @Inject @Named("githubactorsupervisor") private ActorRef supervisor;

    /**
     * Home Controller Constructor
     * 
     * @param ws Handles the ws dependency for sending HTTP requests
     * @param gitHubApi Handles the GitHubApi service
     * @author Pedram Nouri & Tayeeb Hasan
     * @return HomeController instance
     */
    @Inject
    public HomeController(WSClient ws, GitHubApi gitHubApi, ActorSystem system) {
        this.ws = ws;
        this.ghImpl = gitHubApi;
        this.storage = new Hashtable<>();
        this.searchTerms = new Hashtable<>();
        this.searchGroupActor = system.actorOf(SearchGroupActor.props());
        this.system = system;
    }

    /**
     * index page action
     * 
     * @param request Contains the HTTP request
     * @author Pedram Nouri & Tayeeb Hasan
     * @return index view that contains the search bar and previously searched results
     * @version 1.0.0
     */
    public CompletableFuture<Result> index(Http.Request request) {
        String userSession;
        if (request.cookie("GITTERIFIC") != null){
            userSession = request.cookie("GITTERIFIC").value();
        } else {
            userSession = String.valueOf(Math.random());
        }
        CompletableFuture<GitHubActorProtocol.SearchResults> searchResults = FutureConverters.toJava(ask(searchGroupActor, new GitHubActorProtocol.GetSearchResults(userSession), 5000)).toCompletableFuture().thenApplyAsync(res-> (GitHubActorProtocol.SearchResults) res);
        return searchResults.thenApplyAsync((sr)-> {
            if (request.cookie("GITTERIFIC") == null){
                return ok(views.html.index.render(sr.repositories, sr.searchTerms))
                .withCookies(Cookie.builder("GITTERIFIC", userSession).build());
            }
            return ok(views.html.index.render(sr.repositories, sr.searchTerms));
        });     
    }

    /**
     * It searches for the repositories matching the string passed by the user in the search bar.
     * <p>
     * It will generate the results related to the search string and render the repositories on index view.
     * The result will include username and the repository name and topics related to each repository.
     * </p>
     * @author Pedram Nouri & Tayeeb Hasan
     * @param request Contains the HTTP request
     * @param keywords Contains the keywords which the user entered in the search bar
     * @return index page that contains search results (repositories)
     * 
     */
    public CompletableFuture<Result> searchRepositories(Http.Request request, String keywords) {
        String userSession;
        if (request.cookie("GITTERIFIC") != null){
            userSession = request.cookie("GITTERIFIC").value();
        } else {
            userSession = String.valueOf(Math.random());
        }
        
        CompletableFuture<Object> searchObj = FutureConverters.toJava(ask(searchGroupActor, new GitHubActorProtocol.Search(userSession, keywords, ghImpl, ws), 5000)).toCompletableFuture();
        CompletableFuture<List<Repository>> search = searchObj.thenApplyAsync(repos -> (List<Repository>) repos);
        
        CompletableFuture<Result> returnObj = search.thenCompose(repos -> {
            CompletableFuture<GitHubActorProtocol.AddedSearchResponse> adding = FutureConverters.toJava(ask(searchGroupActor, new GitHubActorProtocol.AddSearchResponse(userSession, repos, keywords), 5000)).toCompletableFuture().thenApplyAsync(res-> (GitHubActorProtocol.AddedSearchResponse) res);
            return adding.thenCompose((results)-> {
                CompletableFuture<GitHubActorProtocol.SearchResults> searchResults = FutureConverters.toJava(ask(searchGroupActor, new GitHubActorProtocol.GetSearchResults(userSession), 5000)).toCompletableFuture().thenApplyAsync(res-> (GitHubActorProtocol.SearchResults) res);
                return searchResults.thenApplyAsync((sr)-> {
                    System.out.println(sr.searchTerms);
                    if (request.cookie("GITTERIFIC") == null){
                        return ok(views.html.index.render(sr.repositories, sr.searchTerms))
                        .withCookies(Cookie.builder("GITTERIFIC", userSession).build());
                    }
                    return ok(views.html.index.render(sr.repositories, sr.searchTerms));
                });
            });
        });
        
        return returnObj;
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
     * @return The user page containing all the information about the requested user
     */
    public CompletionStage<Result> userProfile(String username) {
        CompletableFuture<Owner> user = ghImpl.userProfile(username, ws);

        return user.thenApplyAsync(response -> {
            try {
                return ok(views.html.user.render(response));
            }catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return badRequest(views.html.error.render());
            }
        });
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
        String url = "https://bb94d78479b70367def7:"+clientSecret+"@api.github.com/repos/" + username + "/" + repository + "/issues?state=all";
        
        return ws.url(url).get().thenApplyAsync(response -> {
            try {
                JsonNode tempResponse = response.asJson();
                ArrayList<String> issuetitles = new ArrayList<>();
                tempResponse.forEach(item -> {
                    issuetitles.add(item.get("title").textValue());
                });
                
                
                
                return ok(views.html.repoissues.render(this.repoIssuesStats(issuetitles).toString().replace("{", "").replace("}", "").replace("=", "      =      ")));
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
