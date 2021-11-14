/**
 * 
 */
package models;

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
	
	String owner;
	
	String repo;
	
	String state;
	
	int page;
	
	String title;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	

}
