package modules;
import com.google.inject.*;

import play.libs.akka.AkkaGuiceSupport;
import services.github.GitHubApi; 
import services.github.GitHubImpl;

/**
 * Aims for binding the GitHibImpl for the controller actions.
 * @see GitHubImpl
 * @see GitHubApi
 * @author Tayeeb Hasan
 *
 */

public class GitHubApiModule extends AbstractModule implements AkkaGuiceSupport {
    protected void configure() {
        bind(GitHubApi.class).to(GitHubImpl.class);
    }
}
