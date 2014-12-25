package no.api.youngman.model;

import org.joda.time.DateTime;

public class Project {

    private Long id;
    private String projectName;
    private String projectFullName;
    private String description;
    private String lang;
    private String projectUrl;
    private String contributorUrl;
    private DateTime lastupdate;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectFullName() {
        return projectFullName;
    }

    public void setProjectFullName(String projectFullName) {
        this.projectFullName = projectFullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getContributorUrl() {
        return contributorUrl;
    }

    public void setContributorUrl(String contributorUrl) {
        this.contributorUrl = contributorUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(DateTime lastupdate) {
        this.lastupdate = lastupdate;
    }
}
