package services.github;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.ws.WSClient;

import java.util.concurrent.CompletableFuture;

public interface GitHubApi {
    public CompletableFuture<JsonNode> searchRepositories(String keywords, WSClient ws);
    public CompletableFuture<JsonNode> userProfile(String username, WSClient ws);
    public CompletableFuture<JsonNode> userRepository(String username, WSClient ws);

}
