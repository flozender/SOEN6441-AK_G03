package models;

import java.util.List;
import java.util.Date;

public class Repository {
    public String name;
    public String description;
    public Date created_at;
    public Owner owner;
    public License license;
    public String html_url;
    public String language;
    public List<String> topics;
    public int stargazers_count;
    public int open_issues_count;
    public int forks_count;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public Owner getOwner() {
        return owner;
    }

    public License getLicense() {
        return license;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    public String getLanguage() {
        return language;
    }

    public int getStarGazersCount() {
        return stargazers_count;
    }

    public int getOpenIssuesCount() {
        return open_issues_count;
    }
    
    public int getForksCount() {
        return forks_count;
    }
}
