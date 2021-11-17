package modules;
import com.google.inject.*;
import services.github.GitHubApi; 
import services.github.GitHubImpl;

public class GitHubModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GitHubApi.class).to(GitHubImpl.class);
    }
}