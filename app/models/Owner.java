package models;

import java.util.Date;

/**
 * Contains information about users in Github
 * @author Pedram
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
     * @return returns the username of the user
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return returns the company name of the user
     */
    public String getCompany() {
        return company;
    }

    /**
     * @return returns the html_url of the user's page as string
     */
    public String getHtml_url() {
        return html_url;
    }

    /**
     * @return returns name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * @return returns the date that this user created his/her profile on github
     */
    public Date getCreated_at() {
        return created_at;
    }

    /**
     * @return returns the number of followings
     */
    public int getFollowing() {
        return following;
    }

    /**
     * @return returns the number of followers
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * @return returns the email address of the user as string
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return returns the number of public repositories of the user
     */
    public String getPublic_repos() {
        return public_repos;
    }
}
