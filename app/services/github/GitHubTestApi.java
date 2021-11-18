package services.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Repository;
import play.libs.Json;
import play.libs.ws.WSClient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
public class GitHubTestApi implements GitHubApi{

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
                System.out.println(node);
                List<Repository> repoList = Arrays.asList();
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
    public CompletableFuture<JsonNode> userProfile(String username, WSClient ws) {
        CompletableFuture<JsonNode> futureUser = new CompletableFuture<>();
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
                futureUser.complete(node);
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
}
