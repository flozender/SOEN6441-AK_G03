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
	
	int count;
	
	// Passing a fixed repo name as of now. I need to map it to the Repository once we have the page build.
	
	
	// example repo wesbos/JavaScript30
	
	
	String RepoName;
	
	String tittle;
	
	

}
