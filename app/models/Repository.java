package models;

import java.util.List;

import java.util.Date;

/**
 * Repository model for GitHub code repositories.
 * 
 * @author Tayeeb Hasan
 * @version 1.0.0
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
    // extra info
    public int id;
    public String node_id;
    public String full_name;
    public boolean fork;
    public String url;
    public String forks_url;
    public String keys_url;
    public String collaborators_url;
    public String teams_url;
    public String hooks_url;
    public String issue_events_url;
    public String events_url;
    public String assignees_url;
    public String branches_url;
    public String tags_url;
    public String blobs_url;
    public String git_tags_url;
    public String git_refs_url;
    public String trees_url;
    public String statuses_url;
    public String languages_url;
    public String stargazers_url;
    public String contributors_url;
    public String subscribers_url;
    public String subscription_url;
    public String commits_url;
    public String git_commits_url;
    public String comments_url;
    public String issue_comment_url;
    public String contents_url;
    public String compare_url;
    public String merges_url;
    public String archive_url;
    public String downloads_url;
    public String issues_url;
    public String pulls_url;
    public String milestones_url;
    public String notifications_url;
    public String labels_url;
    public String releases_url;
    public String deployments_url;
    public Date updated_at;
    public Date pushed_at;
    public String git_url;
    public String ssh_url;
    public String clone_url;
    public String svn_url;
    public String homepage;
    public int size;
    public int watchers_count;
    public boolean has_issues;
    public boolean has_projects;
    public boolean has_downloads;
    public boolean has_wiki;
    public boolean has_pages;
    public String mirror_url;
    public boolean archived;
    public boolean disabled;
    public boolean allow_forking;
    public boolean is_template;
    public String visibility;
    public int forks;
    public int open_issues;
    public int watchers;
    public String default_branch;
    public String temp_clone_token;
    public int network_count;
    public int subscribers_count;

    /**
     * @return String repository name
     */
    public String getName() {
        return name;
    }

    /**
     * @return String repository description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Date repository creation date and time
     */
    public Date getCreatedAt() {
        return created_at;
    }
    /**
     * @return Owner repository Owner object
     * @see Owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * @return License repository License Object
     * @See License
     */
    public License getLicense() {
        return license;
    }
    
    /**
     * @return List<String> list of topics for repository
     */
    public List<String> getTopics() {
        return topics;
    }

    /**
     * @return String HTML URL of the repository
     */
    public String getHtmlUrl() {
        return html_url;
    }

    /**
     * @return String primary language of the repository
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return int count of stars on the repository
     */
    public int getStargazersCount() {
        return stargazers_count;
    }

    /**
     * @return int count of current open issues of the repository
     */
    public int getOpenIssuesCount() {
        return open_issues_count;
    }

    /**
     * @return int count of total forks of the repository
     */
    public int getForksCount() {
        return forks_count;
    }

    /**
     * This method sets the name of the repository.
     * @param name Name of the repository.
     * @return Nothing.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the description of the repository.
     * @param description description text.
     * @return Nothing.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * This method sets the date of creation of the repository.
     * @param created_at date of the repository.
     * @return Nothing.
     */
    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    /**
     * This method sets the Owner of the repository.
     * @param owner owner of the repository.
     * @return Nothing.
     */
    public void setOwner(Owner owner) {
		this.owner = owner;
	}

    /**
     * This method sets the License of the repository.
     * @param license license of the repository.
     * @return Nothing.
     */
	public void setLicense(License license) {
		this.license = license;
	}

    /**
     * This method sets the HTML URL of the repository.
     * @param html_url HTML URL of the repository.
     * @return Nothing.
     */
	public void setHtmlUrl(String html_url) {
		this.html_url = html_url;
	}

    /**
     * This method sets the Language of the repository.
     * @param language primary language of the repository.
     * @return Nothing.
     */
	public void setLanguage(String language) {
		this.language = language;
	}

    /**
     * This method sets the Topics of the repository.
     * @param topics list of topics of the repository.
     * @return Nothing.
     */
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

    /**
     * This method sets the stargazer count of the repository.
     * @param stargazers_count star count of the repository.
     * @return Nothing.
     */
	public void setStargazersCount(int stargazers_count) {
		this.stargazers_count = stargazers_count;
	}

    /**
     * This method sets the open issues count of the repository.
     * @param open_issues_count open issues count of the repository.
     * @return Nothing.
     */
	public void setOpenIssuesCount(int open_issues_count) {
		this.open_issues_count = open_issues_count;
	}

    /**
     * This method sets the total fork count of the repository.
     * @param forks_count forks count of the repository.
     * @return Nothing.
     */
	public void setForksCount(int forks_count) {
		this.forks_count = forks_count;
	}

    // Extra info
    
    /**
     * @return int ID of the repository
     */
    public int getId() {
		return this.id;
	}
    
    /**
     * @return String NodeID of the repository
     */
	public String getNode_id() {
		return this.node_id;
	}
    
    /**
     * @return String Full name of the repository
     */
	public String getFull_name() {
		return this.full_name;
	}
    
    /**
     * @return boolean if forking allowed for the repository
     */
	public boolean isFork() {
		return this.fork;
	}
    
    /**
     * @return String URL of the repository
     */
	public String getUrl() {
		return this.url;
	}
    
    /**
     * @return String Forks URL of the repository
     */
	public String getForks_url() {
		return this.forks_url;
	}
    
    /**
     * @return String Keys URL of the repository
     */
	public String getKeys_url() {
		return this.keys_url;
	}

    
    /**
     * @return String Collaborators URL of the repository
     */
	public String getCollaborators_url() {
		return this.collaborators_url;
	}
    
    /**
     * @return String Teams URL of the repository
     */
	public String getTeams_url() {
		return this.teams_url;
	}
    
    /**
     * @return String Hooks URL of the repository
     */
	public String getHooks_url() {
		return this.hooks_url;
	}
    
    /**
     * @return String Issue Events URL of the repository
     */
	public String getIssue_events_url() {
		return this.issue_events_url;
	}
    
    /**
     * @return String Events URL of the repository
     */
	public String getEvents_url() {
		return this.events_url;
	}
    
    /**
     * @return String Assignees URL of the repository
     */
	public String getAssignees_url() {
		return this.assignees_url;
	}
    
    /**
     * @return String Branches URL of the repository
     */
	public String getBranches_url() {
		return this.branches_url;
	}
    
    /**
     * @return String Tags URL of the repository
     */
	public String getTags_url() {
		return this.tags_url;
	}
    
    /**
     * @return String Blobs URL of the repository
     */
	public String getBlobs_url() {
		return this.blobs_url;
	}
    
    /**
     * @return String Git Tags URL of the repository
     */
	public String getGit_tags_url() {
		return this.git_tags_url;
	}
    
    /**
     * @return String Git Refs URL of the repository
     */
	public String getGit_refs_url() {
		return this.git_refs_url;
	}
    
    /**
     * @return String Trees URL of the repository
     */
	public String getTrees_url() {
		return this.trees_url;
	}
    
    /**
     * @return String Statuses URL of the repository
     */
	public String getStatuses_url() {
		return this.statuses_url;
	}
    
    /**
     * @return String Languages URL of the repository
     */
	public String getLanguages_url() {
		return this.languages_url;
	}
    
    /**
     * @return String Stargazers URL of the repository
     */
	public String getStargazers_url() {
		return this.stargazers_url;
	}
    
    /**
     * @return String Contributors URL of the repository
     */
	public String getContributors_url() {
		return this.contributors_url;
	}
    
    /**
     * @return String Subscribers URL of the repository
     */
	public String getSubscribers_url() {
		return this.subscribers_url;
	}
    
    /**
     * @return String Subscription URL of the repository
     */
	public String getSubscription_url() {
		return this.subscription_url;
	}
    
    /**
     * @return String Commits URL of the repository
     */
	public String getCommits_url() {
		return this.commits_url;
	}
    
    /**
     * @return String Git Commits URL of the repository
     */
	public String getGit_commits_url() {
		return this.git_commits_url;
	}
    
    /**
     * @return String Comments URL of the repository
     */
	public String getComments_url() {
		return this.comments_url;
	}
    
    /**
     * @return String Issue comment URL of the repository
     */
	public String getIssue_comment_url() {
		return this.issue_comment_url;
	}
    
    /**
     * @return String Contents URL of the repository
     */
	public String getContents_url() {
		return this.contents_url;
	}
    
    /**
     * @return String Compare URL of the repository
     */
	public String getCompare_url() {
		return this.compare_url;
	}
    
    /**
     * @return String Merges URL of the repository
     */
	public String getMerges_url() {
		return this.merges_url;
	}
    
    /**
     * @return String Archive URL of the repository
     */
	public String getArchive_url() {
		return this.archive_url;
	}
    
    /**
     * @return String Downloads URL of the repository
     */
	public String getDownloads_url() {
		return this.downloads_url;
	}
    
    /**
     * @return String Issues URL of the repository
     */
	public String getIssues_url() {
		return this.issues_url;
	}
    
    /**
     * @return String Pulls URL of the repository
     */
	public String getPulls_url() {
		return this.pulls_url;
	}
    
    /**
     * @return String Milestones URL of the repository
     */
	public String getMilestones_url() {
		return this.milestones_url;
	}
    
    /**
     * @return String Notifications URL of the repository
     */
	public String getNotifications_url() {
		return this.notifications_url;
	}
    
    /**
     * @return String Labels URL of the repository
     */
	public String getLabels_url() {
		return this.labels_url;
	}
    
    /**
     * @return String Releases URL of the repository
     */
	public String getReleases_url() {
		return this.releases_url;
	}
    
    /**
     * @return String Deployments URL of the repository
     */
	public String getDeployments_url() {
		return this.deployments_url;
	}
    
    /**
     * @return Date Update Date of the repository
     */
	public Date getUpdated_at() {
		return this.updated_at;
	}    

    /**
     * @return Date Push Date of the repository
     */
	public Date getPushed_at() {
		return this.pushed_at;
	}
    
    /**
     * @return String Git URL of the repository
     */
	public String getGit_url() {
		return this.git_url;
	}
    
    /**
     * @return String SSH URL of the repository
     */
	public String getSsh_url() {
		return this.ssh_url;
	}
    
    /**
     * @return String Clone URL of the repository
     */
	public String getClone_url() {
		return this.clone_url;
	}
    
    /**
     * @return String SVN URL of the repository
     */
	public String getSvn_url() {
		return this.svn_url;
	}
    
    /**
     * @return String Homepage of the repository
     */
	public String getHomepage() {
		return this.homepage;
	}
    
    /**
     * @return int Size of the repository
     */
	public int getSize() {
		return this.size;
	}
    
    /**
     * @return int Watchers Count of the repository
     */
	public int getWatchers_count() {
		return this.watchers_count;
	}
    
    /**
     * @return boolean if issues exist for the repository
     */
	public boolean isHas_issues() {
		return this.has_issues;
	}
    
    /**
     * @return boolean if projects exist for the repository
     */
	public boolean isHas_projects() {
		return this.has_projects;
	}
    
    /**
     * @return boolean if downloads exist for the repository
     */
	public boolean isHas_downloads() {
		return this.has_downloads;
	}
    
    /**
     * @return boolean if wiki exists for the repository
     */
	public boolean isHas_wiki() {
		return this.has_wiki;
	}
    
    /**
     * @return boolean if pages exist for the repository
     */
	public boolean isHas_pages() {
		return this.has_pages;
	}
    
    /**
     * @return String Mirror URL of the repository
     */
	public String getMirror_url() {
		return this.mirror_url;
	}
    
    /**
     * @return boolean if the repository is archived
     */
	public boolean isArchived() {
		return this.archived;
	}

   /**
     * @return boolean if the repository is disabled
     */
	public boolean isDisabled() {
		return this.disabled;
	}

   /**
     * @return boolean if the repository allows forking
     */
	public boolean isAllow_forking() {
		return this.allow_forking;
	}
    
   /**
     * @return boolean if the repository is a template
     */
	public boolean isIs_template() {
		return this.is_template;
	}
    
   /**
     * @return String visibility of the repository
     */
	public String getVisibility() {
		return this.visibility;
	}
    
    /**
     * @return int forks of the repository
     */
	public int getForks() {
		return this.forks;
	}
    
    /**
     * @return int open issues of the repository
     */
	public int getOpen_issues() {
		return this.open_issues;
	}
    
    /**
     * @return int watchers of the repository
     */
	public int getWatchers() {
		return this.watchers;
	}
    
    /**
     * @return String default branch of the repository
     */
	public String getDefault_branch() {
		return this.default_branch;
	}
    
    /**
     * @return String Temporary Clone Token of the repository
     */
	public String getTemp_clone_token() {
		return this.temp_clone_token;
	}
    
    /**
     * @return int Network Count of the repository
     */
	public int getNetwork_count() {
		return this.network_count;
	}
    
    /**
     * @return int Subscribers Count of the repository
     */
	public int getSubscribers_count() {
		return this.subscribers_count;
	}
}

