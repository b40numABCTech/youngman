package no.api.youngman.manager;

import no.api.youngman.Utils.Neo4jUtil;
import no.api.youngman.client.git.GitRestClient;
import no.api.youngman.model.Contributor;
import no.api.youngman.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class ContributorsManager{
    private GraphService service = new GraphService(Neo4jUtil.getNeo4jUrl());

    @Autowired
    private GitRestClient gitRestClient;

    public void importContributors() {
        List<Contributor> contributorList = gitRestClient.getAllContributor(gitRestClient.getAllProject());
        if(contributorList.size() != 0){
            for(int i = 0; i < contributorList.size(); i++ ){
                service.createCollaborateRelations(contributorList.get(i));
            }
        }
    }
}
