package no.api.youngman.Utils;

import no.api.youngman.model.Project;
import no.api.youngman.neo4j.GraphService;

import java.util.Map;

/**
 *
 */
public class Test {

    public static void main(String[] args) {
        GraphService service=new GraphService(Neo4jUtil.getNeo4jUrl());
        //service.createProjectNode("transition");
        //Iterable<Map<String,Object>> projects = service.getProjects();

        Project project = new Project();
        project.setProjectName("AID");
        project.setProjectFullName("AID");
        project.setDesc("111");
        project.setLang("EN");

        service.createProjectNode(project);
    }

}
