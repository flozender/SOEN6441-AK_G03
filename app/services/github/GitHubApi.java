package services.github;

import play.libs.ws.*;
import java.util.concurrent.*;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public interface GitHubApi {
    public CompletableFuture<JsonNode> searchRepositories(String keywords, WSClient ws);
}
