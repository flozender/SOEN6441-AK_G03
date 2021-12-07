package actors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Owner;
import models.Repository;
import models.RepositoryIssues;
import play.libs.ws.WSClient;
import services.github.GitHubApi;

/**
 * Protocols for the various Actors
 *
 * @author Tayeeb Hasan
 */
public class GitHubActorProtocol {

    /**
     * Object used to perform a repository search
     *
     * @author Tayeeb Hasan
     */
    public static class Search {
        public final String keywords;

        public Search(String keywords) {
            this.keywords = keywords;
        }
    } 

    /**
     * Object used to return the repository search results
     *
     * @author Tayeeb Hasan
     */
    public static class SearchResults{
        public final ArrayList<List<Repository>> repositories;
        public final ArrayList<String> searchTerms;
        public final boolean update;
        public final int index;

        public SearchResults(ArrayList<List<Repository>> repositories, ArrayList<String> searchTerms, boolean update, int index){
            this.repositories = repositories;
            this.searchTerms = searchTerms;
            this.update = update;
            this.index = index;
        }
    }
    
    public static class UserProfile {
        public final String username;

        public UserProfile(String username) {
            this.username = username;
        }
    }

    public static class UserRepository {
        public final String username;

        public UserRepository(String username) {
            this.username = username;
        }
    }

    /**
     * Object used to request the repository information
     *
     * @author Tayeeb Hasan
     */
    public static class RepositoryProfile {
        public final String username;
        public final String repository;

        public RepositoryProfile(String username, String repository) {
            this.username = username;
            this.repository = repository;
        }
    }

    /**
     * Object used to return the repository information
     *
     * @author Tayeeb Hasan
     */
    public static class RepositoryInformation{
        public final String userId;
        public final Repository repository;
        public final JsonNode issues;
        public final JsonNode commits;
        public final JsonNode contributors;

        public RepositoryInformation(String userId, Repository repository, JsonNode issues, JsonNode commits, JsonNode contributors){
            this.userId = userId;
            this.repository = repository;
            this.issues = issues;
            this.commits = commits;
            this.contributors = contributors;
        }
    }
}