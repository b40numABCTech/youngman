package no.api.youngman.client.git;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import no.api.youngman.model.Collaborator;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import no.api.youngman.properties.CoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.web.client.RestTemplate;

import org.apache.commons.codec.binary.Base64;
import java.util.List;

public class GitRestClient {


    private RestTemplate restClient;

    private CoreProperties properties;

    @Autowired
    public void setRestClient(RestTemplate restClient) {
        this.restClient = restClient;
    }

    @Autowired
    public void setProperties(CoreProperties properties) {
        this.properties = properties;
    }

    public List<People> getAllMember() {
        String endpoint = properties.getGitApiEndpoint() + "/orgs/amedia/members";
        ResponseEntity<String> response = getResponseFromGit(endpoint);
        return null;
    }

    private List<People> transform(ResponseEntity<String> responseEntity) {
        String jsonString = responseEntity.getBody();
        Gson gson = new GsonBuilder().create();
        JsonElement element = gson.toJsonTree(jsonString);
        return null;
    }

    public List<Project> getAllProject() {
        return null;
    }

    public List<Collaborator> getAllCollaborators() {
        return null;
    }


    private HttpHeaders addAuthentication(){
        HttpHeaders headers = new HttpHeaders();
        String plainCreds = new StringBuilder(properties.getGitUsername()).append(":").append(properties.getGitPassword()).toString();

        String base64Creds = Base64.encodeBase64String(plainCreds.getBytes());
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    private ResponseEntity<String> getResponseFromGit(String address){
        HttpHeaders headers = addAuthentication();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restClient.exchange(address, HttpMethod.GET, request, String.class);
        return response;
    }

}
