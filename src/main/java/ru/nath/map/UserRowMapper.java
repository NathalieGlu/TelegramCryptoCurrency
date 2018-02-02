package ru.nath.map;

import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<PGobject> {

    @Override
    public PGobject mapRow(ResultSet rs, int row) throws SQLException {
        PGobject jsonb = new PGobject();
        jsonb.setType("json");
        jsonb.setValue(rs.getString("settings"));
        return jsonb;
    }
}