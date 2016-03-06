package org.hr.project3_4.straalBetaal.dao.dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hr.project3_4.straalBetaal.dao.dbUtil.SqlBindingUtil;
import org.hr.project3_4.straalBetaal.dao.dbUtil.SqlParameterValues;
import org.hr.project3_4.straalBetaal.dao.dbUtil.SqlRowMapper;

public abstract class dbTemplate<T> {

	/*
	protected void update(String sql, SqlParameterValues values) throws Exception {
		Connection con = null;
		PreparedStatement preparedStatement = null;

		HashMap<String, Integer> sqlBindings = (HashMap<String, Integer>) SqlBindingUtil.getSqlParameters(sql);

		sql = SqlBindingUtil.convertToStandartBindings(sql);

		con = getConnection();

		preparedStatement = con.prepareStatement(sql);

		putBindingsToPreparedStatement(preparedStatement, values, sqlBindings);

		preparedStatement.executeUpdate();
	}
	*/


	protected T queryForObject(String sql, SqlParameterValues values, SqlRowMapper<T> mapper) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		HashMap<String, Integer> sqlBindings = (HashMap<String, Integer>) SqlBindingUtil.getSqlParameters(sql);

		sql = SqlBindingUtil.convertToStandartBindings(sql);

		connection = getConnection();

		preparedStatement = connection.prepareStatement(sql);

		putBindingsToPreparedStatement(preparedStatement, values, sqlBindings);

		resultSet = preparedStatement.executeQuery();

		List<T> objectList = convertResultSetToObjectList(mapper, resultSet);

		return objectList != null && objectList.size() > 0 ? objectList.get(0) : null;
	}

	private void putBindingsToPreparedStatement(PreparedStatement ps, SqlParameterValues values,
			HashMap<String, Integer> sqlBindings) throws Exception {
		for(Entry<String, Object> entry : values.getValues().entrySet()) {
			Object bindingValue = entry.getValue();
			String bindingKey = entry.getKey();
			Integer bindingOrder = null;
			if (sqlBindings.containsKey(bindingKey))
				bindingOrder = sqlBindings.get(bindingKey);
			else
				throw new Exception(bindingKey + " parameter is not bound");

			if (bindingValue instanceof String)
				ps.setString(bindingOrder, (String) bindingValue);
			else if (bindingValue instanceof Long)
				ps.setLong(bindingOrder, (Long) bindingValue);
			else if (bindingValue instanceof Integer)
				ps.setInt(bindingOrder, (Integer) bindingValue);
			else if (bindingValue instanceof Boolean)
				ps.setBoolean(bindingOrder, (Boolean) bindingValue);
			else if (bindingValue instanceof Date)
				ps.setDate(bindingOrder, new java.sql.Date(((Date) bindingValue).getTime()));
		}
	}

	private List<T> convertResultSetToObjectList(SqlRowMapper<T> mapper, ResultSet resultSet) {
		List<T> resultList = new ArrayList<T>();

		if (mapper == null) {
			System.out.println("No rowmapper assigned.");
			return null;
		}

		try {
			while (resultSet.next()) {
				T item = mapper.mapSqlToObject(resultSet);
				resultList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	protected Connection getConnection() throws SQLException, ClassNotFoundException {
		String host = "jdbc:mysql://localhost:3306/straalbetaal";
		String uName = "root";
		String uPass = "root";

		Class.forName("com.mysql.jdbc.Driver");

		return DriverManager.getConnection(host, uName, uPass);
	}

	protected void closeResources(Connection con, Statement stmt, ResultSet rs) throws SQLException {
		if(con != null)
			con.close();
		if(stmt != null)
			stmt.close();
		if(rs != null)
			rs.close();
	}

}
