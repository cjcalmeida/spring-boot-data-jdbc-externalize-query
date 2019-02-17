package com.cjcalmeida.spring.data.jdbc.externalize.query.sample;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<PersonEntity> {

    @Override
    public PersonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PersonEntity(rs.getInt("id"),
                rs.getString("firstname"),
                rs.getString("lastname"));
    }
}
