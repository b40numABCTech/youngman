package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.ContributorsDAO;
import no.api.youngman.model.Contributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class ContributorDAOImpl implements ContributorsDAO {

    private static final String SQL_INSERT = "INSERT INTO collaborator (peopleid, projectid) " +
            "VALUES (?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Contributor insert(Contributor model) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
            int index = 1;
            ps.setLong(index++, model.getPeopleId());
            ps.setLong(index, model.getProjectId());
            return ps;
        });
        return model;
    }
}