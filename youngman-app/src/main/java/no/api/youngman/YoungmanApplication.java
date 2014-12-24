package no.api.youngman;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.api.youngman.Utils.Neo4jUtil;
import no.api.youngman.neo4j.GraphService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static spark.Spark.get;
import static spark.SparkBase.port;

public class YoungmanApplication {
    public static void main(String[] args) {
        port(5555);
        ApplicationContext context = new ClassPathXmlApplicationContext("youngman.xml");
        get("/", (request, response) -> {
            return "root";
        });

        get("/projects", (request, response) -> {
            GraphService service=new GraphService(Neo4jUtil.getNeo4jUrl());
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(service.getProjects());
        });
    }
}
