package org.hr.project3_4.straalBetaal.dao.dbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hr.project3_4.straalBetaal.entities.User;

public class UserInfoRowMapper implements SqlRowMapper<User> {

	public User mapSqlToObject(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setUserID(resultSet.getString(1));
		user.setSaldo(resultSet.getString(2));
		return user;
	}

}
