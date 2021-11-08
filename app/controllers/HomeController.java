package controllers;

import models.Repository;
import models.Response;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.libs.ws.WSRequest;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.util.parsing.combinator.token.StdTokens.Keyword;
import scala.util.parsing.json.JSON;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.typed.delivery.internal.ProducerControllerImpl.Request;
import akka.japi.Option;
import akka.parboiled2.support.OpTreeContext.Optional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;

    @Inject
    public HomeController(WSClient ws) {
        this.ws = ws;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render(Arrays.asList()));
    }

    public CompletionStage<Result> searchRepositories(Http.Request request) {
        java.util.Optional<String> contentType = request.contentType();
        JsonNode body = request.body().asJson();
        String keywords = body.get("keywords").toString();
        String url = "https://api.github.com/search/repositories?q="+keywords;
        CompletionStage<WSResponse> repositories = ws.url(url).get();
        return repositories.thenApplyAsync(response -> ok(response.asJson()));
    }

}
