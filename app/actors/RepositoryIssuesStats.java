package actors;

import com.google.inject.Inject;

import akka.actor.AbstractActor;

public class RepositoryIssuesStats extends AbstractActor{

	final String userId;
	
	@Inject
	public RepositoryIssuesStats(String userID,String Repo) {
		this.userId = userID;
	}
	
	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
