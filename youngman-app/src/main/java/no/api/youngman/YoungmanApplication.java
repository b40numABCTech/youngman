package no.api.youngman;

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
    }
}
