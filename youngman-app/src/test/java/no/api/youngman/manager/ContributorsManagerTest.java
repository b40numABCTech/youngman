package no.api.youngman.manager;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@Ignore
@ContextConfiguration(locations ="/youngman-applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ContributorsManagerTest {

    @Autowired
    private ContributorsManager contributorsManager;

    @Test
    public void testImportContributors() throws Exception {
        contributorsManager.importContributors();
    }
}