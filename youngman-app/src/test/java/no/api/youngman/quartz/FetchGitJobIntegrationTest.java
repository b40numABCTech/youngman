package no.api.youngman.quartz;

import no.api.youngman.client.git.GitRestClient;
import no.api.youngman.dao.ContributorDAO;
import no.api.youngman.dao.PeopleDAO;
import no.api.youngman.dao.ProjectDAO;
import no.api.youngman.neo4j.GraphService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@ContextConfiguration(locations ="/youngman-applicationContext-integrationtest.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FetchGitJobIntegrationTest {

    private FetchGitJob fetchGitJob;

    @Autowired
    private GraphService graphService;

    @Autowired
    private GitRestClient gitRestClient;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private PeopleDAO peopleDAO;

    @Autowired
    private ContributorDAO contributorDAO;

    @Test
    public void testRunQuartz() throws JobExecutionException{
        fetchGitJob = new FetchGitJob();
        fetchGitJob.setContributorDAO(contributorDAO);
        fetchGitJob.setPeopleDAO(peopleDAO);
        fetchGitJob.setProjectDAO(projectDAO);
        fetchGitJob.setGitRestClient(gitRestClient);
        fetchGitJob.setGraphService(graphService);
        fetchGitJob.executeInternal(null);
    }
}
