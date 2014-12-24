package no.api.youngman.neo4j;

import no.api.youngman.model.Collaborator;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import org.neo4j.helpers.collection.IteratorUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.helpers.collection.MapUtil.map;

/**
 *
 */
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
                return new JdbcCypherExecutor(uri,parts[0],parts[1]);
            }
            return new JdbcCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public void createProjectNode(Project project){

        Map<String, Object> params = new HashMap<>();
        params.put("1",project.getProjectName());
        params.put("2",project.getProjectFullName());
        params.put("3",project.getDesc());
        params.put("4",project.getLang());
        Iterable<Map<String,Object>> iterable = getProjectsByName(project.getProjectName());
        if(!iterable.iterator().hasNext()){
            cypher.query("CREATE (p:Project {name:{1}, projectfullname:{2}, desc:{3}, lang:{4}}) RETURN \"hello\", p.name",
                    params);
        }

    }

    public void createPeopleNode(People people){
        Map<String, Object> params = new HashMap<>();
        params.put("1",people.getUsername());
        params.put("2",people.getAvatarUrl());
        params.put("3",people.getEmail());
        Iterable<Map<String,Object>> iterable = getPeopleByUsername(people.getUsername());
        if(!iterable.iterator().hasNext()) {
            cypher.query("CREATE (p:People {username:{1}, avatarurl:{2}, email:{3}}) RETURN \"hello\", " +
                    "p.name", params);
        }

    }

    public void createCollaborateRelations(Collaborator collaborators){
        Iterable<Map<String,Object>> iterable = getRelation(collaborators);
        if(!iterable.iterator().hasNext()) {
            cypher.query("MATCH (pp:People) WHERE pp.username = '" + collaborators.getPeople() +
                    "' MATCH (p:Project) WHERE p.name = " +
                    "'" + collaborators.getProjectName() + "'" +
                    " CREATE (p)-[:COLLABORATED_BY]->(pp)");
        }

    }

    public Iterable<Map<String,Object>> getProjects() {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (n:Project) RETURN n LIMIT 25"));
    }

    public Iterable<Map<String,Object>> getPeople() {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (n:People) RETURN n LIMIT 25"));
    }

    public Iterable<Map<String,Object>> getProjectsByName(String name) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (n:Project) WHERE n.name = '"+name+"' RETURN n"));
    }

    public Iterable<Map<String,Object>> getPeopleByUsername(String username) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (n:People) WHERE n.username = '"+username+"' RETURN n"));
    }

    public Iterable<Map<String,Object>> getRelation(Collaborator collaborators) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (a:Project{name:'"+collaborators.getProjectName()+"'})-[:COLLABORATED_BY]->" +
                        "(b:People{username:'"+collaborators.getPeople()+"'}) RETURN a,b LIMIT 1"));
    }


}
