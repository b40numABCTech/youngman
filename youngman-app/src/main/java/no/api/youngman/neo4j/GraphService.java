package no.api.youngman.neo4j;

import no.api.youngman.model.Contributor;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import org.joda.time.DateTime;
import org.neo4j.helpers.collection.IteratorUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GraphService {

    private final CypherExecutor cypher;

    public GraphService(String uri) {
        cypher = createCypherExecutor(uri);
    }

    private CypherExecutor createCypherExecutor(String uri) {
        try {
            String auth = new URL(uri).getUserInfo();
            if (auth != null) {
                String[] parts = auth.split(":");
                return new JdbcCypherExecutor(uri, parts[0], parts[1]);
            }
            return new JdbcCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public void createProjectNode(List<Project> projects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < projects.size(); i++) {
            System.out.println("Projects ProjectName : " + projects.get(i).getProjectName());
            if (getProjectsByName(projects.get(i).getProjectName()) == null) {
                stringBuilder.append("CREATE (p" + i + ":Project {projectname:'" + projects.get(i).getProjectName() +
                        "', projectfullname:'" + projects.get(i).getProjectFullName() +
                        "', description:'" +
                        projects.get(i).getDescription().replace("'", "\\'").replace("\"", "\\\"") +
                        "', lang:'" + projects.get(i).getLang() +
                        "', " +
                        "projecturl:'" + projects.get(i).getProjectUrl() +
                        "', contributorurl:'" + projects.get(i).getContributorUrl() +
                        "', id:'" + projects.get(i).getId() +
                        "', lastupdate:'" + projects.get(i).getLastupdate().getMillis() +
                        "'}) ");
            }
        }
        cypher.query(stringBuilder.toString());

    }

    public void createPeopleNode(List<People> peoples) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < peoples.size(); i++) {
            System.out.println("people username : " + peoples.get(i).getUsername());
            if (getPeopleByUsername(peoples.get(i).getUsername()) == null) {
                stringBuilder.append("CREATE (p" + i + ":People {username:'" + peoples.get(i).getUsername() +
                        "', avatarurl:'" + peoples.get(i).getAvatarUrl() +
                        "', email:'" + peoples.get(i).getEmail() +
                        "', realname:'" + peoples.get(i).getRealname() +
                        "', id:'" + peoples.get(i).getId() +
                        "', lastupdate:'" + peoples.get(i).getLastupdate().getMillis() +
                        "'}) ");
            }
        }
        cypher.query(stringBuilder.toString());
    }

    public void createCollaborateRelations(List<Contributor> collaborators) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < collaborators.size(); i++) {
            System.out.println("collaborators round : " + i);
            Iterable<Map<String, Object>> iterable = getRelation(collaborators.get(i));
            if (!iterable.iterator().hasNext()) {
                cypher.query(
                        "MATCH (pp" + i + ":People) WHERE pp" + i + ".id = '" + collaborators.get(i).getPeopleId() +
                                "' MATCH (p" + i + ":Project) WHERE p" + i + ".id = " +
                                "'" + collaborators.get(i).getProjectId() + "'" +
                                " CREATE (p" + i + ")-[:COLLABORATED_BY]->(pp" + i + ") ");
            }
        }

    }

    public List<Project> getProjects() {
        List<Project> lstProject = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = cypher.query("MATCH (n:Project) RETURN n");
             iterator.hasNext(); ) {
            Map<String, Object> project = iterator.next();
            lstProject.add((convertMapToProject((Map<String, Object>) project.get("n"))));
        }
        return lstProject;
    }

    public List<People> getPeople() {
        List<People> lstPeople = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = cypher.query("MATCH (n:People) RETURN n");
             iterator.hasNext(); ) {
            Map<String, Object> project = iterator.next();
            lstPeople.add((convertMapToPeople((Map<String, Object>) project.get("n"))));
        }
        return lstPeople;
    }

    public Project getProjectsByName(String name) {
        for (Iterator<Map<String, Object>> iterator = cypher.query("MATCH (n:Project) WHERE n.projectname = '" + name + "' RETURN n");
             iterator.hasNext(); ) {
            Map<String, Object> project = iterator.next();
            return (convertMapToProject((Map<String, Object>) project.get("n")));
        }
        return null;
    }

    public People getPeopleByUsername(String username) {
        for (Iterator<Map<String, Object>> iterator = cypher.query("MATCH (n:People) WHERE n.username = '" + username + "' RETURN n");
             iterator.hasNext(); ) {
            Map<String, Object> project = iterator.next();
            return convertMapToPeople((Map<String, Object>) project.get("n"));
        }
        return null;
    }

    public Iterable<Map<String, Object>> getRelation(Contributor collaborators) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (a:Project{projectname:'" + collaborators.getProjectId() + "'})-[:COLLABORATED_BY]->" +
                        "(b:People{username:'" + collaborators.getProjectId() + "'}) RETURN a,b LIMIT 1"));
    }

    public List<Project> getProjectsByUsername(String username) {
        List<Project> lstProject = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = cypher.query("MATCH (a:Project)-[:COLLABORATED_BY]->" +
                "(b:People{username:'" + username + "'}) RETURN a");
             iterator.hasNext(); ) {
            Map<String, Object> project = iterator.next();
            lstProject.add((convertMapToProject((Map<String, Object>) project.get("a"))));
        }
        return lstProject;
    }

    public List<People> getPeopleByProjectName(String projectName) {
        List<People> lstPeople = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = cypher.query("MATCH (a:Project{projectname:'" + projectName +
                "'})-[:COLLABORATED_BY]->(b:People) RETURN b");
             iterator.hasNext(); ) {
            Map<String, Object> project = iterator.next();
            lstPeople.add((convertMapToPeople((Map<String, Object>) project.get("b"))));
        }
        return lstPeople;
    }

    private Project convertMapToProject(Map<String, Object> map) {
        Project project = new Project();
        project.setId((Long) map.get("id"));
        project.setProjectName((String) map.get("projectname"));
        project.setProjectFullName((String) map.get("projectfullname"));
        project.setDescription((String) map.get("description"));
        project.setLang((String) map.get("lang"));
        project.setProjectUrl((String) map.get("projecturl"));
        project.setContributorUrl((String) map.get("contributorurl"));
        project.setLastupdate(new DateTime(map.get("lastupdate")));
        return project;
    }

    private People convertMapToPeople(Map<String, Object> map) {
        People people = new People();
        people.setId((Long) map.get("id"));
        people.setUsername((String) map.get("username"));
        people.setEmail((String) map.get("email"));
        people.setRealname((String) map.get("realname"));
        people.setAvatarUrl((String) map.get("avatarurl"));
        people.setLastupdate(new DateTime(map.get("lastupdate")));
        return people;
    }

}
