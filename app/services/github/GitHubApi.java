package services.github;

import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import play.libs.ws.WSClient;
import java.util.List;
import models.Repository;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for the GitHubApi Service
 * @author Tayeeb Hasan & Pedram Nouri & Vedasree Reddy Sapatapu
 * @version 1.0.0
 */
public interface GitHubApi {
    public CompletableFuture<List<Repository>> searchRepositories(String keywords, WSClient ws);
    public CompletableFuture<List<Repository>> searchTopicRepositories(String keywords, WSClient ws);
    public CompletableFuture<Owner> userProfile(String username, WSClient ws);
    public CompletableFuture<JsonNode> userRepository(String username, WSClient ws);
    public CompletableFuture<Repository> repositoryProfile(String username, String repository, WSClient ws);
    public CompletableFuture<JsonNode> getRepositoryIssues(String username, String repository, WSClient ws);
    public CompletableFuture<JsonNode> getRepositoryContributors(String username, String repository, WSClient ws);
    public CompletableFuture<JsonNode> getRepositoryCommits(String username, String repository, WSClient ws);
    public CompletableFuture<JsonNode> getRepositoryIssuesTittles(String username, String repository, WSClient ws);
    
}
