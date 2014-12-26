package no.api.youngman.dao;

import no.api.youngman.model.Project;

public interface ProjectDAO {
    Project insert(Project model);
    boolean update(Project model);
    Project get(Long id);
}
