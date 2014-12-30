package no.api.youngman;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.api.youngman.dao.PeopleDAO;
import no.api.youngman.dao.ProjectDAO;
import no.api.youngman.neo4j.GraphService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spark.Request;

import static spark.Spark.get;
import static spark.SparkBase.port;

public class YoungmanApplication {
    public static void main(String[] args) {
        port(5555);
        ApplicationContext context = new ClassPathXmlApplicationContext("youngman.xml");
        GraphService graphService = context.getBean(GraphService.class);
        PeopleDAO peopleDAO = context.getBean(PeopleDAO.class);
        ProjectDAO projectDAO = context.getBean(ProjectDAO.class);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        get("/project", (request, response) -> {
            if(isRDBMS(request)){
                return gson.toJson(projectDAO.select());
            } else {
                return gson.toJson(graphService.getProjects());
            }
        });

        get("/project/:name", (request, response) -> {
            if(isRDBMS(request)){
                return gson.toJson(projectDAO.get(request.params(":name")));
            } else {
                return gson.toJson(graphService.getProjectsByName(request.params(":name")));
            }
        });

        get("/people", (request, response) -> {
            if(isRDBMS(request)){
                return gson.toJson(peopleDAO.select());
            } else {
                return gson.toJson(graphService.getPeople());
            }
        });

        get("/people/:name", (request, response) -> {
            if(isRDBMS(request)){
                return gson.toJson(peopleDAO.get(request.params(":name")));
            } else {
                return gson.toJson(graphService.getPeopleByUsername(request.params(":name")));
            }
        });

        get("/people/:projectname", (request, response) -> {
            GraphService service=new GraphService(Neo4jUtil.getNeo4jUrl());
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(service.getPeopleByProjectName(request.params(":projectname")));
        });

        get("/projects/:username", (request, response) -> {
            GraphService service=new GraphService(Neo4jUtil.getNeo4jUrl());
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(service.getProjectsByUsername(request.params(":username")));
        });
    }

    private static boolean isRDBMS(Request request) {
        return "true".equalsIgnoreCase(request.params("rdbms"));
    }
}
