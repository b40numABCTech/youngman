package no.api.youngman.Utils;

import no.api.youngman.neo4j.GraphService;

import java.util.Map;

/**
 *
 */
public class Test {

    public static void main(String[] args) {
        GraphService service=new GraphService(Neo4jUtil.getNeo4jUrl());
        service.createProjectNode("transition");
        Iterable<Map<String,Object>> projects = service.getProjects();
    }

}
