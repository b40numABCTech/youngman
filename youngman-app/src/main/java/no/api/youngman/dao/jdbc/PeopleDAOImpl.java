package no.api.youngman.dao.jdbc;

import no.api.youngman.dao.PeopleDAO;
import no.api.youngman.model.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class PeopleDAOImpl implements PeopleDAO {
    private static final String SQL_INSERT = "INSERT INTO collaborator (username, email, realname, avatarurl) " +
            "VALUES (?, ?, ?)";

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
            ps.setString(index++, model.getUsername());
            ps.setString(index++, model.getEmail());
            ps.setString(index++, model.getRealname());
            ps.setString(index, model.getAvatarUrl());
            return ps;
        });
        return model;
    }
}
