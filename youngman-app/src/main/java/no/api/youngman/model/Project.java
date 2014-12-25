package no.api.youngman.model;

public class Project {
    private String projectName;
    private String projectFullName;
    private String desc;
    private String lang;
    private String projectUrl;
    private String contributorUrl;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
