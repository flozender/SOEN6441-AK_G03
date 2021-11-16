package services.github;

import play.libs.ws.*;
import java.util.concurrent.*;

import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;

public class GitHubImpl implements GitHubApi {
    private String API_URL = "https://bb94d78479b70367def7:fc2fc9c20d3586664dd0d3e0799b0f5be456a462@api.github.com";

    @Override
    public CompletableFuture<JsonNode> searchRepositories(String keywords, WSClient ws){
        CompletableFuture<JsonNode> futureRepos = new CompletableFuture<>();
        String url = API_URL + "/search/repositories?q="+keywords+"&per_page=10&sort=updated";
        new Thread( () -> {
            ws.url(url).get()
            .thenApplyAsync(response -> (futureRepos.complete(response.asJson().get("items"))));
            }).start();
        return futureRepos;
    }
}
