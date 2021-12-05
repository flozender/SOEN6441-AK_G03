package actors;

import java.util.ArrayList;
import java.util.List;

import models.Repository;
import play.libs.ws.WSClient;
import services.github.GitHubApi;

public class GitHubActorProtocol {
    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }   

    public static class Search {
        public final String keywords;
        public final String userId;
        public final GitHubApi gitHubApi;
        public final WSClient ws;

        public Search(String userId, String keywords, GitHubApi gitHubApi, WSClient ws) {
            this.userId = userId;
            this.keywords = keywords;
            this.gitHubApi = gitHubApi;
            this.ws = ws;
        }
    } 

    public static class SearchResults{
        public final ArrayList<List<Repository>> repositories;
        public final ArrayList<String> searchTerms;

        public SearchResults(ArrayList<List<Repository>> repositories, ArrayList<String> searchTerms){
            this.repositories = repositories;
            this.searchTerms = searchTerms;
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
}