package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.ContributorDAO;
import no.api.youngman.model.Contributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class ContributorDAOImpl implements ContributorDAO {

    private static final String SQL_INSERT = "INSERT INTO collaborator (peopleid, projectid, username, projectname) " +
            "VALUES (?, ?)";

    private static final String SQL_DELETE = "DELETE FROM collaborator WHERE projectid = ?";

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
            ps.setLong(index++, model.getProjectId());
            ps.setString(index++, model.getUserName());
            ps.setString(index, model.getProjectName());
            return ps;
        });
        return model;
    }

    @Override
    public void deleteByProjectId(Long id) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE);
            int index = 1;
            ps.setLong(index++, id);
            return ps;
        });
    }
}
