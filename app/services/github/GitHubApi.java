package services.github;

import play.libs.ws.*;
import java.util.concurrent.*;


public interface GitHubApi {
    public CompletionStage<WSResponse> searchRepositories(String keywords, WSClient ws);
}
