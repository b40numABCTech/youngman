package no.api.youngman.Utils;

import no.api.youngman.model.Contributor;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import no.api.youngman.neo4j.GraphService;

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
        project.setDescription("111");
        project.setLang("EN");
        project.setId(10L);

        service.createProjectNode(project);

        People people = new People();
        people.setUsername("user1");
        people.setAvatarUrl("sdjklfkjldsfnds");
        people.setEmail("user1@sldfsd.com");
        people.setId(18L);

        service.createPeopleNode(people);

        Contributor collaborator = new Contributor();
        collaborator.setProjectId(10L);
        collaborator.setProjectId(18L);

        service.createCollaborateRelations(collaborator);
    }

}
