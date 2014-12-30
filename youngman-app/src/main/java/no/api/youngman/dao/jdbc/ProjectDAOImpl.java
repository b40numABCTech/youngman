package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.ProjectDAO;
import no.api.youngman.model.Project;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    private Logger log = LoggerFactory.getLogger(ProjectDAO.class.getName());

    private static final String COLUMNS = "id, projectname, projectfullname, description, lang,  " +
            "projecturl, contributorurl, lastupdate";

    private static final String SQL_INSERT = "INSERT INTO project (" + COLUMNS + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE project SET projectname = ?, projectfullname = ?, " +
            "description = ?, lang = ?, projecturl = ?, contributorurl = ?, lastupdate = ? WHERE id = ?";

    private static final String SQL_GET = "SELECT " + COLUMNS + " FROM project WHERE projectname = ?";

    private static final String SQL_SELECT = "SELECT " + COLUMNS + " FROM project";

    private static final String SQL_SELECT_BY_USERNAME = "SELECT p.id, p.projectname, p.projectfullname, " +
            "p.description, p.lang, p.projecturl, p.contributorurl, p.lastupdate FROM project p, contributor c " +
            "WHERE p.id = c.projectid and c.username = ?";

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
    public Project get(String projectName) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET, new Object[] {projectName}, new ProjectMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Project> select() {
        try {
            return jdbcTemplate.query( SQL_SELECT, new ProjectMapper());
        }catch (Exception ex) {
            log.error("cannot retrive peoples");
            throw new RuntimeException( ex );
        }
    }

    @Override
    public List<Project> selectByUsername(String username) {
        try {
            return jdbcTemplate.query( SQL_SELECT_BY_USERNAME, new ProjectMapper(), username);
        }catch (Exception ex) {
            log.error("cannot retrive project model by username={}", username);
            throw new RuntimeException( ex );
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
