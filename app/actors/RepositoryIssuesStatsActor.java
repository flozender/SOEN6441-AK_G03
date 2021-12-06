package actors;

import com.google.inject.Inject;

import akka.actor.AbstractActor;
import akka.actor.Props;
import play.libs.ws.WSClient;
import services.github.GitHubApi;

public class RepositoryIssuesStatsActor extends AbstractActor{

	final String userId;
	final String repo;
	private static GitHubApi ghImpl;
	private static WSClient ws;
	
	 // need to inject ws and ghimpl here -> 
    public static Props props(String userId) {
        return Props.create(RepositoryIssuesStatsActor.class, userId, ws, ghImpl);
    }

    
	@Inject
	public RepositoryIssuesStatsActor(String userID,String repo) {
		this.userId = userID;
		this.repo = repo;
		
	}
	
	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return receiveBuilder()
				.match(GitHubActorProtocol., null)
		return null;
	}

	
}
