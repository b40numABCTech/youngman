package youngman.properties;

import no.api.youngman.properties.CoreProperties;
import org.eclipse.jetty.util.StringUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@Ignore
@ContextConfiguration(locations ="/youngman-applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CorePropertiesIntegrationTest {

    @Autowired
    private CoreProperties coreProperties;

    @Test
    public void testAvalibleProperties() {
        assertTrue(StringUtil.isNotBlank(coreProperties.getGitUsername()));
    }

}
