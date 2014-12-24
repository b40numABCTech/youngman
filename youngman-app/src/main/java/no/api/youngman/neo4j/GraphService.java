package no.api.youngman.neo4j;

import org.neo4j.helpers.collection.IteratorUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
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
    public void createProjectNode(String projectName){
        cypher.query("CREATE (p:Project {name:{1}}) RETURN \"hello\", p1.name",map("1",projectName));

    }

    public Iterable<Map<String,Object>> getProjects() {
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (n:Project) RETURN n LIMIT 25"));
    }



}
