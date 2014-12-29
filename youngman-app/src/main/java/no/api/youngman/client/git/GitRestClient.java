package no.api.youngman.client.git;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.api.youngman.Utils.JsonUtil;
import no.api.youngman.model.Contributor;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import no.api.youngman.properties.CoreProperties;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
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
        List<People> peoples = new ArrayList<>();
        String endpoint = properties.getGitApiEndpoint() + "/orgs/amedia/members";
        do {

            ResponseEntity<String> response = getResponseFromGit(endpoint);
            peoples.addAll(transformPeople(response));
            endpoint = extractEndpoint(response.getHeaders());
        } while (StringUtil.isNotBlank(endpoint));
        return peoples;
    }

    public People getPeopleByUsername(String username) {
        String endpoint = properties.getGitApiEndpoint() + "/users/" + username;
        return readPeopleFromGit(endpoint);
    }

    private String extractEndpoint(HttpHeaders header) {
        List<String> links = header.get("Link");
        if (links != null && !links.isEmpty()) {
            String[] parts = links.get(0).split(",");
            for (String eachParts : parts) {
                if (eachParts.contains("rel=\"next\"")) {
                    return eachParts.substring(eachParts.indexOf("<") + 1, eachParts.indexOf(">"));
                }
            }
        }
        return null;
    }

    private List<People> transformPeople(ResponseEntity<String> responseEntity) {
        JsonArray jsonArray = JsonUtil.transformToArray(responseEntity);
        if (jsonArray != null) {
            return readPeopleJSONArray(jsonArray);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private List<People> readPeopleJSONArray(JsonArray elementList) {
        List<People> peopleLists = new ArrayList<>();
        for (JsonElement eachElement : elementList) {
            JsonObject jsonObject = eachElement.getAsJsonObject();
            People people = readPeopleFromGit(JsonUtil.jsonObjectLoader(jsonObject, "url"));
            if (people != null) {
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

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            People people = new People();
            people.setUsername(JsonUtil.jsonObjectLoader(jsonObject, "login"));
            people.setAvatarUrl(JsonUtil.jsonObjectLoader(jsonObject, "avatar_url"));
            people.setRealname(JsonUtil.jsonObjectLoader(jsonObject, "name"));
            people.setEmail(JsonUtil.jsonObjectLoader(jsonObject, "email"));
            people.setId(JsonUtil.jsonObjectLoaderLong(jsonObject, "id"));
            people.setLastupdate(JsonUtil.jsonObjectLoaderDateTime(jsonObject, "updated_at"));
            return people;
        } else {
            return null;
        }
    }

    public List<Project> getAllProject() {
        List<Project> projects = new ArrayList<>();
        String endpoint = properties.getGitApiEndpoint() + "/orgs/amedia/repos";
        do {
            ResponseEntity<String> response = getResponseFromGit(endpoint);
            projects.addAll(transformProject(response));
            endpoint = extractEndpoint(response.getHeaders());
        } while (StringUtil.isNotBlank(endpoint));
        return projects;
    }

    private List<Project> transformProject(ResponseEntity<String> responseEntity) {
        JsonArray jsonArray = JsonUtil.transformToArray(responseEntity);
        if (jsonArray != null) {
            return readProjectJSONArray(jsonArray);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private List<Project> readProjectJSONArray(JsonArray elementList) {
        List<Project> projectLists = new ArrayList<>();
        for (JsonElement eachElement : elementList) {
            if (eachElement.isJsonObject()) {
                JsonObject jsonObject = eachElement.getAsJsonObject();
                Project project = readProjectFromGit(JsonUtil.jsonObjectLoader(jsonObject, "url"));
                if (project != null) {
                    projectLists.add(project);
                }
            }
        }
        return projectLists;
    }

    private Project readProjectFromGit(String endpoint) {
        ResponseEntity<String> responseEntity = getResponseFromGit(endpoint);
        String jsonResponse = responseEntity.getBody();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonResponse);

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            Project project = new Project();
            project.setProjectName(JsonUtil.jsonObjectLoader(jsonObject, "name"));
            project.setProjectFullName(JsonUtil.jsonObjectLoader(jsonObject, "full_name"));
            project.setDescription(JsonUtil.jsonObjectLoader(jsonObject, "description"));
            project.setLang(JsonUtil.jsonObjectLoader(jsonObject, "language"));
            project.setProjectUrl(JsonUtil.jsonObjectLoader(jsonObject, "url"));
            project.setContributorUrl(JsonUtil.jsonObjectLoader(jsonObject, "contributors_url"));
            project.setLastupdate(JsonUtil.jsonObjectLoaderDateTime(jsonObject, "updated_at"));
            project.setId(JsonUtil.jsonObjectLoaderLong(jsonObject, "id"));
            return project;
        } else {
            return null;
        }
    }

    public List<Contributor> getAllContributor(List<Project> projects) {

        List<Contributor> contributors = new ArrayList<>();
        for (Project eachProject : projects) {
            List<Contributor> eachContributors = getContributorByProject(eachProject);
            contributors.addAll(eachContributors);
        }
        return contributors;
    }

    public List<Contributor> getContributorByProject(Project project) {

        List<Contributor> contributors = new ArrayList<>();
        if (project.getContributorUrl() != null) {
            String endpoint = project.getContributorUrl();
            do {
                ResponseEntity<String> responseEntity = getResponseFromGit(endpoint);
                JsonArray members = JsonUtil.transformToArray(responseEntity);
                List<Contributor> eachContributors =
                        readContributorJSONArray(members, project.getId(), project.getProjectName());
                contributors.addAll(eachContributors);
                endpoint = extractEndpoint(responseEntity.getHeaders());
            } while (StringUtil.isNotBlank(endpoint));
        }
        return contributors;
    }

    private List<Contributor> readContributorJSONArray(JsonArray elementList, Long projectId, String projectName) {
        List<Contributor> contributors = new ArrayList<>();
        for (JsonElement eachElement : elementList) {
            if (eachElement.isJsonObject()) {
                JsonObject jsonObject = eachElement.getAsJsonObject();
                Contributor contributor = new Contributor();
                contributor.setPeopleId(JsonUtil.jsonObjectLoaderLong(jsonObject, "id"));
                contributor.setUserName(JsonUtil.jsonObjectLoader(jsonObject, "login"));
                contributor.setProjectId(projectId);
                contributor.setProjectName(projectName);
                contributors.add(contributor);
            }
        }
        return contributors;
    }


    private HttpHeaders addAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        String plainCreds =
                new StringBuilder(properties.getGitUsername()).append(":").append(properties.getGitPassword())
                        .toString();

        String base64Creds = Base64.encodeBase64String(plainCreds.getBytes());
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    private ResponseEntity<String> getResponseFromGit(String address) {
        HttpHeaders headers = addAuthentication();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> response = restClient.exchange(address, HttpMethod.GET, request, String.class);
            return response;
        }catch (HttpClientErrorException ex){
            System.out.println("error cann't read " + address);
            throw ex;
        }
    }

}
