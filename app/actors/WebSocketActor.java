package actors;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import actors.SupervisorActor;
import models.Owner;
import models.Repository;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import services.github.GitHubApi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.Duration;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static akka.pattern.Patterns.ask;
import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithTimers;

import com.fasterxml.jackson.databind.ObjectMapper;
import akka.actor.Props;
import java.util.concurrent.TimeUnit;

import actors.GitHubActorProtocol.*;

public class WebSocketActor extends AbstractActorWithTimers {
    private final ActorRef out;
    private final WSClient ws;
    private final ActorRef supervisorActor;
    private final GitHubApi ghImpl;
    private ArrayList<List<Repository>> repositories;
    private ArrayList<String> searchTerms;


    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(10, TimeUnit.SECONDS));
    }

    public static Props props(ActorRef out, WSClient ws, GitHubApi ghImpl) {
        return Props.create(WebSocketActor.class, out, ws, ghImpl);
    }    

    public WebSocketActor(ActorRef out, WSClient ws, GitHubApi ghImpl) {
      this.out = out;
      this.ws = ws;
      this.ghImpl = ghImpl;
      this.supervisorActor = getContext().actorOf(SupervisorActor.props(ws, ghImpl), "supervisorActor");
      this.repositories = new ArrayList<>();
      this.searchTerms = new ArrayList<>();
    }
  
    public static final class Tick{
    }

    @Override
    public Receive createReceive() {
      return receiveBuilder()
          .match(String.class, this::handleSearch)
          .match(Tick.class, (r)->updateSearch())
          .build();
    }

    private void handleSearch(String keywords) {
        CompletableFuture<List<Repository>> search = FutureConverters.toJava(ask(supervisorActor, new GitHubActorProtocol.Search(keywords), 5000)).toCompletableFuture().thenApplyAsync(repos -> (List<Repository>) repos);
        CompletableFuture<List<Repository>> result = search.thenApplyAsync((List<Repository> repos) -> {
            this.repositories.add(0, repos);
            this.searchTerms.add(0, keywords);
            this.repositories = this.repositories.stream().limit(10).collect(Collectors.toCollection(ArrayList::new));
            this.searchTerms = this.searchTerms.stream().limit(10).collect(Collectors.toCollection(ArrayList::new));
            GitHubActorProtocol.SearchResults results = new GitHubActorProtocol.SearchResults(this.repositories, this.searchTerms, false, 0);
            try {
                ObjectMapper mapper = new ObjectMapper();  
                String resp = mapper.writeValueAsString(results);
                out.tell(resp, self());
            } catch (Exception e) {}
            return null;
        });
    }

    private void updateSearch() {
        Boolean updated = false;
        ArrayList<List<Repository>> repoList = new ArrayList<>();
        for (int i=0; i < searchTerms.size(); i++){
            final int index = i;
            CompletableFuture<List<Repository>> search = FutureConverters.toJava(ask(supervisorActor, new GitHubActorProtocol.Search(searchTerms.get(i)), 5000)).toCompletableFuture().thenApplyAsync(repos -> (List<Repository>) repos);
            CompletableFuture<List<Repository>> result = search.thenApplyAsync((List<Repository> repos) -> {
                ArrayList<List<Repository>> tempRepos = new ArrayList<>();
                ArrayList<String> tempSearchTerms = new ArrayList<>();
                tempRepos.add(repos);
                tempSearchTerms.add(searchTerms.get(index));
                GitHubActorProtocol.SearchResults results = new GitHubActorProtocol.SearchResults(tempRepos, tempSearchTerms, true, index);
                try {
                    ObjectMapper mapper = new ObjectMapper();  
                    String resp = mapper.writeValueAsString(results);
                    out.tell(resp, self());
                } catch (Exception e) {}
                return null;
            });
        }
        

    }

}