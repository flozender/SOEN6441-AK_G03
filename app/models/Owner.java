package models;

import java.util.Date;

/**
 * @author 
 *
 */

public class Owner {
    private String login;
    private String html_url;
    private String name;
    private String company;
    private Date created_at;
    private int following;
    private int followers;
    private String email;
    private String public_repos;

    /**
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return company name
     */
    public String getCompany() {
        return company;
    }

    /**
     * @return html_url
     */
    public String getHtml_url() {
        return html_url;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public int getFollowing() {
        return following;
    }

    public int getFollowers() {
        return followers;
    }

    public String getEmail() {
        return email;
    }

    public String getPublic_repos() {
        return public_repos;
    }

}
