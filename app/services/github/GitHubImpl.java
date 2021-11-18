package services.github;

import com.fasterxml.jackson.databind.JsonNode;

import models.Repository;
import play.libs.Json;
import play.libs.ws.WSClient;

import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GitHubImpl implements GitHubApi {
    private String API_URL = "https://bb94d78479b70367def7:fc2fc9c20d3586664dd0d3e0799b0f5be456a462@api.github.com";

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
        String url = API_URL + "/users/" + username + "/repos";
        new Thread( () -> {
            ws.url(url).get()
                    .thenApplyAsync(response -> (futureRepositories.complete(response.asJson())));
        }).start();
        return futureRepositories;
    }
}
