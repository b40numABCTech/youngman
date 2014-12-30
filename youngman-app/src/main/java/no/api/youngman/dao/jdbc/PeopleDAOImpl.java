package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.PeopleDAO;
import no.api.youngman.model.People;
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

public class PeopleDAOImpl implements PeopleDAO {

    private Logger log = LoggerFactory.getLogger(PeopleDAOImpl.class.getName());

    private static final String COLUMNS = "id, username, email, realname, avatarurl, lastupdate";

    private static final String SQL_INSERT = "INSERT INTO people (id, "+
            "username, email, realname, avatarurl, lastupdate) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE people SET username = ?, email = ?, " +
            "realname = ?, avatarurl = ?, lastupdate = ? WHERE id = ?";

    private static final String SQL_GET = "SELECT " + COLUMNS + " FROM people WHERE username = ?";

    private static final String  SQL_SELECT = "SELECT " + COLUMNS + " FROM people";

    private static final String SQL_SELECT_BY_PROJECTNAME = "SELECT p.id, p.username, p.email, p.realname, p.avatarurl, p.lastupdate FROM people p, contributor c " +
            "WHERE p.id = c.peopleid and c.projectname = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public People insert(People model) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
            int index = 1;
            ps.setLong(index++, model.getId());
            ps.setString(index++, model.getUsername());
            ps.setString(index++, model.getEmail());
            ps.setString(index++, model.getRealname());
            ps.setString(index++, model.getAvatarUrl());
            ps.setLong(index, model.getLastupdate().getMillis());
            return ps;
        });
        return model;
    }

    @Override
    public boolean update(People model) {
        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            int index = 1;
            ps.setString(index++, model.getUsername());
            ps.setString(index++, model.getEmail());
            ps.setString(index++, model.getRealname());
            ps.setString(index++, model.getAvatarUrl());
            ps.setLong(index++, model.getLastupdate().getMillis());
            ps.setLong(index, model.getId());
            return ps;
        }) > 0;
    }

    @Override
    public People get(String username) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET, new Object[] {username}, new PeopleMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<People> select() {
        try {
            return jdbcTemplate.query( SQL_SELECT, new PeopleMapper());
        }catch (Exception ex) {
            log.error("cannot retrive peoples");
            throw new RuntimeException( ex );
        }
    }

    @Override
    public List<People> selectByProjectName(String projectName) {
        try {
            return jdbcTemplate.query( SQL_SELECT_BY_PROJECTNAME, new PeopleMapper(), projectName);
        }catch (Exception ex) {
            log.error("cannot retrive people model by project name={}", projectName);
            throw new RuntimeException( ex );
        }
    }

    private static class PeopleMapper implements RowMapper<People> {

        public People mapRow(ResultSet rs, int rowNum) throws SQLException {
            People people = new People();
            people.setId(rs.getLong("id"));
            people.setUsername(rs.getString("username"));
            people.setEmail(rs.getString("email"));
            people.setRealname(rs.getString("realname"));
            people.setAvatarUrl(rs.getString("avatarurl"));
            people.setLastupdate(new DateTime(rs.getLong("lastupdate")));
            return people;
        }
    }
}
