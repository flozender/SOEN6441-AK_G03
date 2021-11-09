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
import play.mvc.Http.Cookie;
import scala.util.parsing.combinator.token.StdTokens.Keyword;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.typed.delivery.internal.ProducerControllerImpl.Request;
import akka.japi.Option;
import akka.parboiled2.support.OpTreeContext.Optional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.CompletionStage;
import java.util.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Hashtable<String, ArrayList<JsonNode>> storage;

    @Inject
    public HomeController(WSClient ws) {
        this.ws = ws;
        this.storage = new Hashtable<String, ArrayList<JsonNode>>();
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    public Result index(Http.Request request) {
        if (request.cookie("GITTERIFIC") != null){
            String userSession = request.cookie("GITTERIFIC").value();
            this.storage.put(userSession, new ArrayList<JsonNode>());
            System.out.println("Cleared storage " + this.storage.get(userSession));
        }
        return ok(views.html.index.render(Arrays.asList())).withCookies(Cookie.builder("GITTERIFIC", String.valueOf(Math.random())).build());
    }

    public CompletionStage<Result> searchRepositories(Http.Request request, String keywords) {
        JsonNode body = request.body().asJson();
        String userSession = request.cookie("GITTERIFIC").value();
        String url = "https://api.github.com/search/repositories?q="+keywords+"&per_page=10";
        return ws.url(url).get().thenApplyAsync(response -> {
            try {
                JsonNode tempResponse = response.asJson().get("items");
                ArrayList<JsonNode> collectRepos = new ArrayList<JsonNode>();
                collectRepos.add(tempResponse);
                if (this.storage.containsKey(userSession)){
                    ArrayList<JsonNode> tempStorage = this.storage.get(userSession);
                    tempStorage.stream().forEach(e->collectRepos.add(e));
                }
                this.storage.put(userSession, collectRepos);
                return ok(views.html.index.render(this.storage.get(userSession)));
            } catch (Exception e) {
                System.out.println("CAUGHT EXCEPTION: " + e);
                return ok(views.html.error.render());
            }
        });
    }
}
