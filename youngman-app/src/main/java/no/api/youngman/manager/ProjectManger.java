package no.api.youngman.manager;

import no.api.youngman.Utils.Neo4jUtil;
import no.api.youngman.client.git.GitRestClient;
import no.api.youngman.model.Project;
import no.api.youngman.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class ProjectManger{

    private GraphService service = new GraphService(Neo4jUtil.getNeo4jUrl());

    @Autowired
    private GitRestClient gitRestClient;

    public void importProject() {
        List<Project> projectList = gitRestClient.getAllProject();
        if(projectList.size() != 0){
            for(int i = 0; i < projectList.size(); i++ ){
                service.createProjectNode(projectList.get(i));
            }
        }

    }
}
