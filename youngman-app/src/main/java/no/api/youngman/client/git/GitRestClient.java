package no.api.youngman.client.git;

import no.api.youngman.model.Collaborators;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class GitRestClient {

    @Autowired
    private RestTemplate restClient;

    public List<People> getAllMember() {
        return null;
    }

    public List<Project> getAllProject() {
        return null;
    }

    public List<Collaborators> getAllCollaborators() {
        return null;
    }


}
