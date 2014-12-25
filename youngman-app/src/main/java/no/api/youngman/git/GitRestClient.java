package no.api.youngman.git;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.api.youngman.model.Collaborator;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import no.api.youngman.properties.CoreProperties;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
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
        boolean readAgain = false;
        List<People> peoples = new ArrayList<>();
        do {
            String endpoint = properties.getGitApiEndpoint() + "/orgs/amedia/members";
            ResponseEntity<String> response = getResponseFromGit(endpoint);
            peoples.addAll(transformPeople(response));
        } while (readAgain);
        return peoples;
    }

    private List<People> transformPeople(ResponseEntity<String> responseEntity) {
        String jsonString = responseEntity.getBody();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(jsonString);
        if(elements.isJsonArray()) {
            return readPeopleJSONArray(elements.getAsJsonArray());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private List<People> readPeopleJSONArray(JsonArray elementList) {
        List<People> peopleLists = new ArrayList<>();
        for(JsonElement eachElement: elementList) {
            JsonObject jsonObject = eachElement.getAsJsonObject();
            People people = readPeopleFromGit(jsonObjectLoader(jsonObject,"url"));
            if(people != null) {
                peopleLists.add(people);
            }
        }
        return peopleLists;
    }

    private People readPeopleFromGit(String endpoint) {
        ResponseEntity<String> responseEntity = getResponseFromGit(endpoint);
        String jsonResponse = responseEntity.getBody();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonResponse);

        if(element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            People people = new People();
            people.setUsername(jsonObjectLoader(jsonObject,"login"));
            people.setAvatarUrl(jsonObjectLoader(jsonObject,"avatar_url"));
            people.setRealname(jsonObjectLoader(jsonObject,"name"));
            people.setEmail(jsonObjectLoader(jsonObject,"email"));
            return people;
        }else{
            return null;
        }
    }
    private String jsonObjectLoader(JsonObject jsonObject,String member) {
        JsonElement element = jsonObject.get(member);
        if(element != null && element.isJsonPrimitive()) {
            return element.getAsString();
        }else{
            return null;
        }
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
