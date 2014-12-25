package no.api.youngman.model;

import org.joda.time.DateTime;

public class People {

    private Long id;
    private String username;
    private String avatarUrl;
    private String realname;
    private String email;
    private DateTime lastupdate;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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
