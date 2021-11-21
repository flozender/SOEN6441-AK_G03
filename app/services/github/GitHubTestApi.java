package services.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Owner;
import models.Repository;
import play.libs.Json;
import play.mvc.Result;
import play.libs.ws.WSClient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

/**
 * This class implements the GitHubApi interface and returns predefined
 * JSON files for each controller of the web server controller.
 * It is used for testing.
 * @author Tayeeb Hasan & Pedram Nouri
 * @since 1.0.0
 */
public class GitHubTestApi implements GitHubApi{

    /**
     * Service to return the JSON file for searchRepositories dummy method.
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
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/searchRepositories.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                jsonString += line;
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                List<Repository> repoList = new ArrayList<>();
                for (JsonNode repo : node){
                    Repository res = Json.fromJson(repo, Repository.class);
                    repoList.add(res);
                }
                futureRepos.complete(repoList);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();

        return futureRepos;
    }

    /**
     * Service to return the JSON file for searchTopicRepositories dummy method.
     * <p>
     * It will load the results related to the given string and return it.
     * The result will include username and the repository name and topics related to each repository of the topic.
     * </p>
     * @author Vedasree Reddy Sapatapu
     * @param keywords Contains the keywords passed by the controller
     * @param ws WSClient to make HTTP requests
     * @return CompletableFuture<List<Repository>> that contains search results (repositories)
     *
     */

    @Override
    public CompletableFuture<List<Repository>> searchTopicRepositories(String keywords, WSClient ws){
        CompletableFuture<List<Repository>> futureRepos = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/searchTopicRepositories.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                    jsonString += line;
                }
            }
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                List<Repository> repoList = new ArrayList<>();
                for (JsonNode repo : node){
                    Repository res = Json.fromJson(repo, Repository.class);
                    repoList.add(res);
                }
                futureRepos.complete(repoList);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();

        return futureRepos;
    }



    @Override
    public CompletableFuture<Owner> userProfile(String username, WSClient ws) {
        CompletableFuture<Owner> futureUser = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/userProfile.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                    jsonString += line;
                }
            }
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                Owner o1 = Json.fromJson(node, Owner.class);
                futureUser.complete(o1);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();

        return futureUser;
    }

    @Override
    public CompletableFuture<JsonNode> userRepository(String username, WSClient ws) {
        CompletableFuture<JsonNode> futureRepositories = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/userRepository.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                    jsonString += line;
                }
            }
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                futureRepositories.complete(node);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();

        return futureRepositories;
    }

    /**
     * Dummy Service method repositoryProfile returns the JSON file for repositoryProfile method
     * It is an Async call.
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
    public CompletableFuture<Repository> repositoryProfile(String username, String repository, WSClient ws){
        CompletableFuture<Repository> futureRepo = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/repositoryProfile.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                jsonString += line;
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                Repository repo = Json.fromJson(node, Repository.class);
                futureRepo.complete(repo);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();
        return futureRepo;
    }

    /**
     * Dummy Service method getRepositoryIssues returns a JSON file for getRepositoryIssues method
     * It is an Async call.
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
    public CompletableFuture<JsonNode> getRepositoryIssues(String username, String repository, WSClient ws){
        CompletableFuture<JsonNode> futureIssues = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/getRepositoryIssues.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                jsonString += line;
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                futureIssues.complete(node);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();
        return futureIssues;
    }

    /**
     * Dummy Service method getRepositoryContributors returns a JSON file for the getRepositoryContributors method
     * It is an Async call.
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
    public CompletableFuture<JsonNode> getRepositoryContributors(String username, String repository, WSClient ws){
        CompletableFuture<JsonNode> futureContributors = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/getRepositoryContributors.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                jsonString += line;
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                futureContributors.complete(node);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();
        return futureContributors;
    }

    /**
     * Dummy Service method getRepositoryCommits returns a JSON file for the getRepositoryCommits method
     * It is an Async call.
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
    public CompletableFuture<JsonNode> getRepositoryCommits(String username, String repository, WSClient ws){
        CompletableFuture<JsonNode> futureContributors = new CompletableFuture<>();
        new Thread( () -> {
            Path fileName = Paths.get("./app/services/github/resources/getRepositoryCommits.json");
            Charset charset = Charset.forName("ISO-8859-1");
            String jsonString = "";
            try {
                List<String> lines = Files.readAllLines(fileName, charset);
                for (String line : lines) {
                jsonString += line;
                }
            } 
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                futureContributors.complete(node);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }).start();
        return futureContributors;
    }
}
