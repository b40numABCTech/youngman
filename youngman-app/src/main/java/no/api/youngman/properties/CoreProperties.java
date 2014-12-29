package no.api.youngman.properties;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.InitializingBean;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

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

        Console console = System.console();
        if(StringUtil.isBlank(getGitUsername()) || StringUtil.isBlank(getGitPassword())) {
            System.out.println("missing git.username & git.password");
            mapset.put("git.username", console.readLine("Enter username: "));
            mapset.put("git.password", new String( console.readPassword("Enter password :") ));
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
