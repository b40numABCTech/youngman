package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.ProjectDAO;
import no.api.youngman.model.Project;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDAOImpl implements ProjectDAO {

    private Logger log = LoggerFactory.getLogger(ProjectDAO.class.getName());

    private static final String COLUMNS = "id, projectname, projectfullname, description, lang,  " +
            "projecturl, contributorurl, lastupdate";

    private static final String SQL_INSERT = "INSERT INTO project (" + COLUMNS + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE project SET projectname = ?, projectfullname = ?, " +
            "description = ?, lang = ?, projecturl = ?, contributorurl = ?, lastupdate = ? WHERE id = ?";

    private static final String SQL_GET = "SELECT " + COLUMNS + " FROM project WHERE id = ?";

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
            ps.setLong(index++, model.getId());
            ps.setString(index++, model.getProjectName());
            ps.setString(index++, model.getProjectFullName());
            ps.setString(index++, model.getDescription());
            ps.setString(index++, model.getLang());
            ps.setString(index++, model.getProjectUrl());
            ps.setString(index++, model.getContributorUrl());
            ps.setLong(index, model.getLastupdate().getMillis());
            return ps;
        });
        return model;
    }

    @Override
    public boolean update(Project model) {
        try {
    return jdbcTemplate.update( SQL_UPDATE
            , model.getProjectName()
            , model.getProjectFullName()
            , model.getDescription()
            , model.getLang()
            , model.getProjectUrl()
            , model.getContributorUrl()
            , model.getLastupdate().getMillis()
            , model.getId()
            ) > 0;
        }catch (Exception ex) {
            log.error("cannot save project model projectid={}, projectname={}, projecturl={}",model.getId(),model.getProjectName(),model.getProjectUrl());
            throw new RuntimeException( ex );
        }
    }

    @Override
    public Project get(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET, new Object[] {id}, new ProjectMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class ProjectMapper implements RowMapper<Project> {

        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            Project project = new Project();
            project.setId(rs.getLong("id"));
            project.setProjectName(rs.getString("projectname"));
            project.setProjectFullName(rs.getString("projectfullname"));
            project.setDescription(rs.getString("description"));
            project.setLang(rs.getString("lang"));
            project.setProjectUrl(rs.getString("projecturl"));
            project.setContributorUrl(rs.getString("contributorurl"));
            project.setLastupdate(new DateTime(rs.getLong("lastupdate")));
            return project;
        }
    }
}
