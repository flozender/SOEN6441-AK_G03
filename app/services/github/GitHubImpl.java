package services.github;

import com.fasterxml.jackson.databind.JsonNode;

import models.Repository;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Result;

import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This class implements the GitHubApi interface and makes ws requests to
 * the Github REST API for the web server controller.
 * @author Tayeeb Hasan & Pedram Nouri
 * @since 1.0.0
 */
public class GitHubImpl implements GitHubApi {
    private String API_URL = "https://bb94d78479b70367def7:fc2fc9c20d3586664dd0d3e0799b0f5be456a462@api.github.com";
    
    /**
     * Service to search for the repositories for given keywords.
     * <p>
     * It will load the results related to the search string and return it.
     * The result will include username and the repository name and topics related to each repository.
     * </p>
     * @author Tayeeb Hasan
     * @param keywords Contains the keywords passed by the controller
     * @param ws WSClient to make HTTP requests
     * @return CompletableFuture<List<Repository>> that contains search results (repositories)
     * 
     */
    @Override
    public CompletableFuture<List<Repository>> searchRepositories(String keywords, WSClient ws){
        CompletableFuture<List<Repository>> futureRepos = new CompletableFuture<>();
        String url = API_URL + "/search/repositories?q="+keywords+"&per_page=10&sort=updated";
        new Thread( () -> {
            ws.url(url).get()
            .thenApplyAsync(response -> {
                List<Repository> repoList = new ArrayList<>();
                for (JsonNode repo : response.asJson().get("items")){
                    Repository res = Json.fromJson(repo, Repository.class);
                    repoList.add(res);
                }
                return futureRepos.complete(repoList);
            });
            }).start();
        return futureRepos;
    }

    @Override
    public CompletableFuture<JsonNode> userProfile(String username, WSClient ws) {
        CompletableFuture<JsonNode> futureUser = new CompletableFuture<>();
        String url = API_URL + "/users/"+username;
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureUser.complete(response.asJson())));
        }).start();
        return futureUser;
    }

    @Override
    public CompletableFuture<JsonNode> userRepository(String username, WSClient ws) {
        CompletableFuture<JsonNode> futureRepositories = new CompletableFuture<>();
        String url = API_URL + "/users/" + username + "/repos?per_page=100";
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureRepositories.complete(response.asJson())));
        }).start();
        return futureRepositories;
    }

    /**
     * Service method repositoryProfile returns the details of the repository based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the repository. Also, displays the 20 latest issues
     * of the repository.
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @param ws WSClient to make HTTP requests
     * @return CompletableFuture<Repository> with the public information of the requested repository
     */
    @Override
    public CompletableFuture<Repository> repositoryProfile(String username, String repository, WSClient ws) {
        CompletableFuture<Repository> futureRepository = new CompletableFuture<>();
        String url = API_URL + "/repos/" + username + "/" + repository;
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureRepository.complete(Json.fromJson(response.asJson(), Repository.class))));
        }).start();
        return futureRepository;
    }

    /**
     * Service method getRepositoryIssues returns a JSON with the 20 latest issues based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the issues. 
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @param ws WSClient to make HTTP requests
     * @return CompletableFuture<JsonNode> containing all the public information of the requested repository issues
     */
    @Override
    public CompletableFuture<JsonNode> getRepositoryIssues(String username, String repository, WSClient ws) {
        CompletableFuture<JsonNode> futureIssues = new CompletableFuture<>();
        String url = API_URL + "/repos/" + username + "/" + repository + "/issues?per_page=20&sort=updated&state=all";
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureIssues.complete(response.asJson())));
        }).start();
        return futureIssues;
    }

    /**
     * Service method getRepositoryContributors returns a JSON with the contributors based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the contributors. 
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @param ws WSClient to make HTTP requests
     * @return CompletableFuture<JsonNode> containing all the public information of the requested repository contributors
     */
    @Override
    public CompletableFuture<JsonNode> getRepositoryContributors(String username, String repository, WSClient ws) {
        CompletableFuture<JsonNode> futureContributors = new CompletableFuture<>();
        String url = API_URL + "/repos/" + username + "/" + repository + "/contributors";
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureContributors.complete(response.asJson())));
        }).start();
        return futureContributors;
    }

    /**
     * Service method getRepositoryCommits returns a JSON with the 100 latest commits based on the username and repository 
     * name passed to it. It is an Async call.
     * 
     * The response contains all the public information of the commits. 
     *
     * @author Tayeeb Hasan
     * @param username the github username of the user
     * @param repository the repository name
     * @param ws WSClient to make HTTP requests
     * @return CompletableFuture<JsonNode> containing all the public information of the requested repository commits
     */
    @Override
    public CompletableFuture<JsonNode> getRepositoryCommits(String username, String repository, WSClient ws) {
        CompletableFuture<JsonNode> futureCommits = new CompletableFuture<>();
        String url = API_URL + "/repos/" + username + "/" + repository + "/commits?per_page=100";
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureCommits.complete(response.asJson())));
        }).start();
        return futureCommits;
    }
}
