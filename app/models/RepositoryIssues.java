/**
 * 
 */
package models;

import java.util.Date;
import java.util.List;

/**
 * @author Nazanin
 *
 *API https://docs.github.com/en/rest/reference/issues#list-repository-issues
 *
 *
 *curl \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/repos/octocat/hello-world/issues
  
  
  await octokit.request('GET /repos/{owner}/{repo}/issues', {
  owner: 'octocat',
  repo: 'hello-world'
})

 */
public class RepositoryIssues {
	
	public String title;
	
	public String repo;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}
	
	

}
