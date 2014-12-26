package no.api.youngman.manager;

import no.api.youngman.Utils.Neo4jUtil;
import no.api.youngman.client.git.GitRestClient;
import no.api.youngman.model.People;
import no.api.youngman.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */

@Service
public class PeopleManager{

    private GraphService service = new GraphService(Neo4jUtil.getNeo4jUrl());

    @Autowired
    private GitRestClient gitRestClient;

    public void importPeople() {
        List<People> peopleList = gitRestClient.getAllMember();
        System.out.println("total : " + peopleList.size());
        if(peopleList.size() != 0){
            service.createPeopleNode(peopleList);
        }
    }

}
