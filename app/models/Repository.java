package models;

import java.util.List;

/**
 * @author 
 *
 */
public class Repository {
    public String name;
    public Owner owner;
    public String html_url;
    public List<String> topics;

    /**
     * @return name as string
     */
    public String getName() {
        return name;
    }

    /**
     * @return owner as Owner Object
     */
    public Owner getOwner() {
        return owner;
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
}
