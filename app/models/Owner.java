package models;

import java.util.Date;

/**
 * Contains information about users in Github. Owner model for user profiles in GitHub.
 * @author Pedram Nouri
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
    private int public_repos;
    private String bio;
    private int id;
    private String node_id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String followers_url;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String events_url;
    private String received_events_url;
    private String type;
    private boolean site_admin;
    private String blog;
    private String location;
    private int public_gists;
    private Date updated_at;

    /**
     * @return returns node id of the user
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the user.
     * @param id id.
     * @return Nothing.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return returns node id of the user
     */
    public String getNode_id() {
        return node_id;
    }

    /**
     * This method sets the node id of the user.
     * @param node_id node id.
     * @return Nothing.
     */
    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    /**
     * @return returns url of the user's avatar
     */
    public String getAvatar_url() {
        return avatar_url;
    }

    /**
     * This method sets the avatar url of the user.
     * @param avatar_url avatar url.
     * @return Nothing.
     */
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    /**
     * @return returns user's gravatar id
     */
    public String getGravatar_id() {
        return gravatar_id;
    }

    /**
     * This method sets the gravatar id of the user.
     * @param gravatar_id gravatar id.
     * @return Nothing.
     */
    public void setGravatar_id(String gravatar_id) {
        this.gravatar_id = gravatar_id;
    }

    /**
     * @return returns url of the user's profile (API)
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method sets the url of the user profile (API).
     * @param url url of the user (API).
     * @return Nothing.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return returns url of the user's followers
     */
    public String getFollowers_url() {
        return followers_url;
    }

    /**
     * This method sets the url of followers users of the user.
     * @param followers_url url of follower users.
     * @return Nothing.
     */
    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    /**
     * @return returns url of the user's followings
     */
    public String getFollowing_url() {
        return following_url;
    }

    /**
     * This method sets the url of following users of the user.
     * @param following_url url of following users.
     * @return Nothing.
     */
    public void setFollowing_url(String following_url) {
        this.following_url = following_url;
    }

    /**
     * @return returns url of the user's gists
     */
    public String getGists_url() {
        return gists_url;
    }

    /**
     * This method sets the gists url of the user.
     * @param gists_url gists url.
     * @return Nothing.
     */
    public void setGists_url(String gists_url) {
        this.gists_url = gists_url;
    }

    /**
     * @return returns url of the user's starred repositories
     */
    public String getStarred_url() {
        return starred_url;
    }

    /**
     * This method sets the starred repositories' url of the user.
     * @param starred_url starred repositories' url.
     * @return Nothing.
     */
    public void setStarred_url(String starred_url) {
        this.starred_url = starred_url;
    }

    /**
     * @return returns url of the user's subscription
     */
    public String getSubscriptions_url() {
        return subscriptions_url;
    }

    /**
     * This method sets the subscriptions url of the user.
     * @param subscriptions_url subscriptions url.
     * @return Nothing.
     */
    public void setSubscriptions_url(String subscriptions_url) {
        this.subscriptions_url = subscriptions_url;
    }

    /**
     * @return returns url of the user's organization
     */
    public String getOrganizations_url() {
        return organizations_url;
    }

    /**
     * This method sets the organization url of the user.
     * @param organizations_url organization url.
     * @return Nothing.
     */
    public void setOrganizations_url(String organizations_url) {
        this.organizations_url = organizations_url;
    }

    /**
     * @return returns url of the user's repositories
     */
    public String getRepos_url() {
        return repos_url;
    }

    /**
     * This method sets the repositories' url of the user.
     * @param repos_url url of the user's repositories.
     * @return Nothing.
     */
    public void setRepos_url(String repos_url) {
        this.repos_url = repos_url;
    }

    /**
     * @return returns event url of the user
     */
    public String getEvents_url() {
        return events_url;
    }

    /**
     * This method sets the events url of the user.
     * @param events_url events url.
     * @return Nothing.
     */
    public void setEvents_url(String events_url) {
        this.events_url = events_url;
    }

    /**
     * @return returns received url of the user
     */
    public String getReceived_events_url() {
        return received_events_url;
    }

    /**
     * This method sets the received events url of the user.
     * @param received_events_url received event url.
     * @return Nothing.
     */
    public void setReceived_events_url(String received_events_url) {
        this.received_events_url = received_events_url;
    }

    /**
     * @return returns type of the user.
     */
    public String getType() {
        return type;
    }

    /**
     * This method sets the type of the user.
     * @param type user type.
     * @return Nothing.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return returns if the user is site admin or not.
     */
    public boolean isSite_admin() {
        return site_admin;
    }

    /**
     * This method sets if the user is admin or not.
     * @param site_admin is admin.
     * @return Nothing.
     */
    public void setSite_admin(boolean site_admin) {
        this.site_admin = site_admin;
    }

    /**
     * @return returns blog of the user
     */
    public String getBlog() {
        return blog;
    }

    /**
     * This method sets the blog of the user
     * @param blog blog address.
     * @return Nothing.
     */
    public void setBlog(String blog) {
        this.blog = blog;
    }

    /**
     * @return returns location of the user
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method sets the location of the user.
     * @param location location.
     * @return Nothing.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return returns the number of public gists.
     */
    public int getPublic_gists() {
        return public_gists;
    }

    /**
     * This method sets the number of public gists of the user.
     * @param public_gists number of public gists.
     * @return Nothing.
     */
    public void setPublic_gists(int public_gists) {
        this.public_gists = public_gists;
    }

    /**
     * @return returns the update date of the profile.
     */
    public Date getUpdated_at() {
        return updated_at;
    }

    /**
     * This method sets the update date of the profile.
     * @param updated_at last update date.
     * @return Nothing.
     */
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

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
    public int getPublic_repos() {
        return public_repos;
    }

    /**
     * @return returns the bio of user in String
     */
    public String getBio() {
        return bio;
    }

    /**
     * This method sets the username(login) of the user.
     * @param login username.
     * @return Nothing.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * This method sets the html_url of the user.
     * @param html_url url of the profile.
     * @return Nothing.
     */
    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    /**
     * This method sets the name of the user.
     * @param name complete name of the user.
     * @return Nothing.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the company name of the user.
     * @param company company name of the user.
     * @return Nothing.
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * This method sets the date at which the profile was created.
     * @param created_at created date.
     * @return Nothing.
     */
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    /**
     * This method sets the number of following of the user.
     * @param following number of following.
     * @return Nothing.
     */
    public void setFollowing(int following) {
        this.following = following;
    }

    /**
     * This method sets the number of followers of the user.
     * @param followers number of followers.
     * @return Nothing.
     */
    public void setFollowers(int followers) {
        this.followers = followers;
    }

    /**
     * This method sets the email address of the user.
     * @param email email address.
     * @return Nothing.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method sets the number of public repositories of the user.
     * @param public_repos number of public repositories.
     * @return Nothing.
     */
    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    /**
     * This method sets the bio information of the user.
     * @param bio bio text.
     * @return Nothing.
     */
    public void setBio(String bio) {
        this.bio = bio;
    }
}
