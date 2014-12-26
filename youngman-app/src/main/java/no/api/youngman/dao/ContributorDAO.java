package no.api.youngman.dao;

import no.api.youngman.model.Contributor;

public interface ContributorDAO {
    Contributor insert(Contributor model);
    void deleteByProjectId(Long id);
}
