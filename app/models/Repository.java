package models;

import java.util.List;
import java.util.Date;

/**
 * @author 
 *
 */
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

    /**
     * @return name as string
     */
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return created_at;
    }
    /**
     * @return owner as Owner Object
     */
    public Owner getOwner() {
        return owner;
    }


    public License getLicense() {
        return license;
    }
    
    /**
     * @return topics List
     */
    public List<String> getTopics() {
        return topics;
    }

    /**
     * @return html_url as String
     */
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
