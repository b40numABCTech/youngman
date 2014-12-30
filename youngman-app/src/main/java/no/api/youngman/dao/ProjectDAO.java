package no.api.youngman.dao;

import no.api.youngman.model.Project;

import java.util.List;

public interface ProjectDAO {
    Project insert(Project model);
    boolean update(Project model);
    Project get(String projectName);
    List<Project> select();
    List<Project> selectByUsername(String username);
}
