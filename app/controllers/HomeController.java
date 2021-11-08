package controllers;

import models.Resp;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
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

        return ok(views.html.index.render());
    }

    public CompletionStage<Result> repSearch(String keywords) {

        String url = "https://api.github.com/search/repositories?q="+keywords;
        return ws.url(url)
                .get()
                .thenApply((WSResponse r) -> {
                    Resp resp = Json.fromJson(r.asJson(), Resp.class);
//                    resp.getItems().stream().map(Item::getOwner).map(Owner::getLogin).limit(10).forEach(System.out::println);
                    return ok(views.html.repResult.render(resp.getItems().subList(0, Math.min(10, resp.getItems().size()))));
                });
    }

//    public CompletionStage<Result> userSearch(String username) {
//
//        String url = "https://api.github.com/users/"+username;
//        return ws.url(url)
//                .get()
//                .thenApply((WSResponse r) -> {
//                    Resp resp = Json.fromJson(r.asJson(), Resp.class);
////                    resp.getItems().stream().map(Item::getOwner).map(Owner::getLogin).limit(10).forEach(System.out::println);
//                    return ok(views.html.repResult.render(resp.getItems().subList(0, Math.min(10, resp.getItems().size()))));
//                });
//    }
}
