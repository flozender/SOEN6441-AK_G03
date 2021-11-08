package models;

import java.util.List;

public class Repository {
    public String name;
    public Owner owner;
    public String html_url;
    public List<String> topics;

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getHtmlUrl() {
        return html_url;
    }
}
