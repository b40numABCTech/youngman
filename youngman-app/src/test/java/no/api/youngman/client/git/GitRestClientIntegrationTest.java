package no.api.youngman.client.git;

import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;

@Ignore
@ContextConfiguration(locations ="/youngman-applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class GitRestClientIntegrationTest {

    private static Logger log = LoggerFactory.getLogger(GitRestClientIntegrationTest.class.getName());

    @Autowired
    private GitRestClient gitRestClient;

    @Test
    public void testDownloadPeopleFromGit() {
        gitRestClient.getAllMember();
    }

    @Test
    public void testDownloadPeopleFromUsername() {
        People people = gitRestClient.getPeopleByUsername("amedia");
        Assert.assertEquals("amedia",people.getUsername());
    }

    @Test
    public void testDownloadProjectFromGit() {
        gitRestClient.getAllProject();
    }

    @Test
    public void testDownloadContributorFromGit() {
        List<Project> projectList = gitRestClient.getAllProject();
        gitRestClient.getAllContributor(projectList);

    }


}
