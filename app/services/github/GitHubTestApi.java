package services.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.ws.WSClient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
public class GitHubTestApi implements GitHubApi{

    @Override
    public CompletableFuture<JsonNode> searchRepositories(String keywords, WSClient ws){
        CompletableFuture<JsonNode> futureRepos = new CompletableFuture<>();
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
                futureRepos.complete(node);
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
