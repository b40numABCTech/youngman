package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.CollaboratorDAO;
import no.api.youngman.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class CollaboratorDAOImpl implements CollaboratorDAO {

    private static final String SQL_INSERT = "INSERT INTO collaborator (projectname, people) " +
            "VALUES (?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Collaborator insert(Collaborator model) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
            int index = 1;
            ps.setString(index++, model.getProjectName());
            ps.setString(index, model.getPeople());
            return ps;
        });
        return model;
    }
}
