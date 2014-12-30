package no.api.youngman.dao;

import no.api.youngman.model.People;

import java.util.List;

public interface PeopleDAO {
    People insert(People model);
    boolean update(People model);
    People get(String username);
    List<People> select();
    List<People> selectByProjectName(String projectName);
}
