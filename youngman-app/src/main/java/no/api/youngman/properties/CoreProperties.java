package no.api.youngman.properties;

import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CoreProperties implements InitializingBean {

    private Properties properties;

    private final Map<String,String> mapset = new HashMap<>();

    private final String DEFAULT_URL = "http://localhost:7474";

    @Override
    public void afterPropertiesSet() throws Exception {
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/core/youngman.properties"));

        for (Map.Entry<?, ?> eachEntry : properties.entrySet()) {
            mapset.put((String) eachEntry.getKey() ,(String) eachEntry.getValue());
        }
    }

    public String getGitUsername() {
        return mapset.get("git.username");
    }

    public String getGitPassword() {
        return mapset.get("git.password");
    }

    public String getGitApiEndpoint() {
        return mapset.get("git.endpoint");
    }

    public String getNeo4jUrl() {
        return mapset.get("neo4j.endpoint") == null ? DEFAULT_URL : mapset.get("neo4j.endpoint") ;
    }


}
