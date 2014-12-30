package no.api.youngman.Utils;

import no.api.youngman.model.Contributor;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import no.api.youngman.neo4j.GraphService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class Test {
    /*
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("youngman.xml");
        GraphService service = context.getBean(GraphService.class);


        Project project = new Project();
        project.setProjectName("AID");
        project.setProjectFullName("AID");
        project.setDescription("Online statistics for the newspapers. Used for \"most read\" lists in addition to " +
                "looking good.");
        project.setLang("EN");
        project.setId(10L);


        System.out.println(project.getDescription().replace("\"","\\\""));

        //service.createProjectNode(project);

        People people = new People();
        people.setUsername("user1");
        people.setAvatarUrl("sdjklfkjldsfnds");
        people.setEmail("user1@sldfsd.com");
        people.setId(18L);

        //service.createPeopleNode(people);

        Contributor collaborator = new Contributor();
        collaborator.setProjectId(10L);
        collaborator.setProjectId(18L);

        //service.createCollaborateRelations(collaborator);
    }
    */
}
