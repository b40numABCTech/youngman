package no.api.youngman.quartz;

import no.api.youngman.client.git.GitRestClient;
import no.api.youngman.dao.ContributorDAO;
import no.api.youngman.dao.PeopleDAO;
import no.api.youngman.dao.ProjectDAO;
import no.api.youngman.model.Contributor;
import no.api.youngman.model.People;
import no.api.youngman.model.Project;
import no.api.youngman.neo4j.GraphService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FetchGitJob extends QuartzJobBean {

    private GraphService graphService;
    private GitRestClient gitRestClient;
    private ProjectDAO projectDAO;
    private PeopleDAO peopleDAO;
    private ContributorDAO contributorDAO;

    public void setContributorDAO(ContributorDAO contributorDAO) {
        this.contributorDAO = contributorDAO;
    }

    public void setGitRestClient(GitRestClient gitRestClient) {
        this.gitRestClient = gitRestClient;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void setPeopleDAO(PeopleDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    public void setGraphService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        Set<String> stPeopleId = new HashSet<>();
        List<Project> lstProject = gitRestClient.getAllProject();
        for(Project project : lstProject) {
            Project projectDB = projectDAO.get(project.getId());
            if(projectDB == null) {
                projectDAO.insert(project);
            } else {
                projectDAO.update(project);
            }
            contributorDAO.deleteByProjectId(project.getId());
            List<Contributor>  lstContributor = gitRestClient.getContributorByProject(project);
            for(Contributor contributor : lstContributor) {
                String username = contributor.getUserName();
                if(!stPeopleId.contains(username)){
                    People people = gitRestClient.getPeopleByUsername(username);
                    People peopleDB = peopleDAO.get(username);
                    if(peopleDB == null) {
                        peopleDAO.insert(people);
                    } else {
                        if(peopleDB.getLastupdate().getMillis() != people.getLastupdate().getMillis()) {
                            peopleDAO.update(people);
                        }
                    }
                    stPeopleId.add(contributor.getUserName());
                }
                contributorDAO.insert(contributor);
            }
        }
    }
}
