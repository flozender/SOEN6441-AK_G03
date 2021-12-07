package actors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Owner;
import models.Repository;
import models.RepositoryIssues;
import play.libs.ws.WSClient;
import services.github.GitHubApi;

public class GitHubActorProtocol {
    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
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

    public static class Search {
        public final String keywords;

        public Search(String keywords) {
            this.keywords = keywords;
        }
    } 

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

    public static class StoreSearch{
        public final String userId;
        public final List<Repository> repositories;
        public final String searchTerms;

        public StoreSearch(String userId, List<Repository> repositories, String searchTerms){
            this.userId = userId;
            this.repositories = repositories;
            this.searchTerms = searchTerms;
        }
    }

    public static class GetSearchResults{
        public final String userId;

        public GetSearchResults(String userId){
            this.userId = userId;
        }
    }

    public static class AddedSearchResponse{
        public final String userId;

        public AddedSearchResponse(String userId){
            this.userId = userId;
        }
    }

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