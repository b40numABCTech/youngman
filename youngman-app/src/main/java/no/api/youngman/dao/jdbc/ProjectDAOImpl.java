package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.ProjectDAO;
import no.api.youngman.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class ProjectDAOImpl implements ProjectDAO {
    private static final String SQL_INSERT = "INSERT INTO collaborator (projectname, projectfullname, desc, lang) " +
            "VALUES (?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Project insert(Project model) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
            int index = 1;
            ps.setString(index++, model.getProjectName());
            ps.setString(index++, model.getProjectFullName());
            ps.setString(index++, model.getDesc());
            ps.setString(index, model.getLang());
            return ps;
        });
        return model;
    }
}