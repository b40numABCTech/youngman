package no.api.youngman.neo4j;

import no.api.youngman.model.Contributor;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import org.neo4j.helpers.collection.IteratorUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
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
                return new JdbcCypherExecutor(uri,parts[0],parts[1]);
            }
            return new JdbcCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public void createProjectNode(List<Project> projects){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i< projects.size(); i++){
            System.out.println("Projects ProjectName : " + projects.get(i).getProjectName());
            Iterable<Map<String,Object>> iterable = getProjectsByName(projects.get(i).getProjectName());
            if(!iterable.iterator().hasNext()){
                stringBuilder.append("CREATE (p"+i+":Project {projectname:'" + projects.get(i).getProjectName() +
                        "', projectfullname:'" + projects.get(i).getProjectFullName() +
                        "', description:'" + projects.get(i).getDescription().replace("'","\\'").replace("\"","\\\"")+
                        "', lang:'" +projects.get(i).getLang()+
                        "', " +
                                "projecturl:'" +projects.get(i).getProjectUrl()+
                        "', contributorurl:'" +projects.get(i).getContributorUrl()+
                        "', id:'" +projects.get(i).getId()+
                        "', lastupdate:'" +projects.get(i).getLastupdate().toString("dd/MM/yyyy")+
                        "'}) ");
            }
        }
        cypher.query(stringBuilder.toString());

    }

    public void createPeopleNode(List<People> peoples){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i< peoples.size(); i++){
            System.out.println("people username : " + peoples.get(i).getUsername());
            Iterable<Map<String,Object>> iterable = getPeopleByUsername(peoples.get(i).getUsername());
            if(!iterable.iterator().hasNext()) {
                stringBuilder.append("CREATE (p"+i+":People {username:'" + peoples.get(i).getUsername()+
                        "', avatarurl:'" + peoples.get(i).getAvatarUrl()+
                        "', email:'" + peoples.get(i).getEmail()+
                        "', realname:'" + peoples.get(i).getRealname()+
                        "', id:'" + peoples.get(i).getId()+
                        "', lastupdate:'" + peoples.get(i).getLastupdate().toString("dd/MM/yyyy")+
                        "'}) ");
            }
        }
        cypher.query(stringBuilder.toString());
    }

    public void createCollaborateRelations(List<Contributor> collaborators){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i< collaborators.size(); i++){
            System.out.println("collaborators round : " + i);
            Iterable<Map<String,Object>> iterable = getRelation(collaborators.get(i));
            if(!iterable.iterator().hasNext()) {
                cypher.query("MATCH (pp"+i+":People) WHERE pp"+i+".id = '" + collaborators.get(i).getPeopleId() +
                        "' MATCH (p"+i+":Project) WHERE p"+i+".id = " +
                        "'" + collaborators.get(i).getProjectId() + "'" +
                        " CREATE (p"+i+")-[:COLLABORATED_BY]->(pp"+i+") ");
            }
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
                "MATCH (n:Project) WHERE n.projectname = '"+name+"' RETURN n"));
    }

    public Iterable<Map<String,Object>> getPeopleByUsername(String username) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (n:People) WHERE n.username = '"+username+"' RETURN n"));
    }

    public Iterable<Map<String,Object>> getRelation(Contributor collaborators) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (a:Project{projectname:'"+collaborators.getProjectId()+"'})-[:COLLABORATED_BY]->" +
                        "(b:People{username:'"+collaborators.getProjectId()+"'}) RETURN a,b LIMIT 1"));
    }

    public Iterable<Map<String,Object>> getProjectsByUsername(String username) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (a:Project)-[:COLLABORATED_BY]->" +
                        "(b:People{username:'"+username+"'}) RETURN a"));
    }

    public Iterable<Map<String,Object>> getPeopleByProjectName(String projectName) {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (a:Project{projectname:'"+projectName+"'})-[:COLLABORATED_BY]->" +
                        "(b:People) RETURN b"));
    }


}
