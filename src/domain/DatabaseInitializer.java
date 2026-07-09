/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates the small HRS schema when the embedded test database is empty.
 */
public class DatabaseInitializer {

	private static boolean initialized = false;

	public static synchronized void ensureInitialized(Connection connection) throws SQLException {
		if (initialized && hasTable(connection, "ROOM") && hasTable(connection, "AVAILABLEQTY")
				&& hasTable(connection, "RESERVATION") && hasTable(connection, "PAYMENT")) {
			return;
		}

		if (!hasTable(connection, "ROOM")) {
			executeUpdate(connection,
					"CREATE TABLE ROOM(ROOMNUMBER VARCHAR(50) NOT NULL PRIMARY KEY,STAYINGDATE VARCHAR(20));");
		}
		ensureRooms(connection);

		if (!hasTable(connection, "AVAILABLEQTY")) {
			executeUpdate(connection,
					"CREATE TABLE AVAILABLEQTY(DATE VARCHAR(20) NOT NULL PRIMARY KEY,QTY INTEGER);");
		}

		if (!hasTable(connection, "RESERVATION")) {
			executeUpdate(connection,
					"CREATE TABLE RESERVATION(RESERVATIONNUMBER VARCHAR(50) NOT NULL PRIMARY KEY,STAYINGDATE VARCHAR(20),CHECKOUTDATE VARCHAR(20),STATUS VARCHAR(10));");
		}
		else if (!hasColumn(connection, "RESERVATION", "CHECKOUTDATE")) {
			executeUpdate(connection, "ALTER TABLE RESERVATION ADD CHECKOUTDATE VARCHAR(20);");
		}

		if (!hasTable(connection, "PAYMENT")) {
			executeUpdate(connection,
					"CREATE TABLE PAYMENT(ROOMNUMBER VARCHAR(50) NOT NULL,STAYINGDATE VARCHAR(20) NOT NULL,AMOUNT INTEGER,STATUS VARCHAR(10),CONSTRAINT ROOMNUMBER_STAYINGDATE PRIMARY KEY(ROOMNUMBER,STAYINGDATE));");
		}

		initialized = true;
	}

	private static boolean hasTable(Connection connection, String tableName) {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE 1 = 0;");
			return true;
		}
		catch (SQLException e) {
			return false;
		}
		finally {
			close(resultSet, statement);
		}
	}

	private static boolean hasColumn(Connection connection, String tableName, String columnName) {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT " + columnName + " FROM " + tableName
					+ " WHERE 1 = 0;");
			return true;
		}
		catch (SQLException e) {
			return false;
		}
		finally {
			close(resultSet, statement);
		}
	}

	private static void ensureRooms(Connection connection) throws SQLException {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT COUNT(*) FROM ROOM;");
			if (resultSet.next() && resultSet.getInt(1) > 0) {
				return;
			}
		}
		finally {
			close(resultSet, statement);
		}

		executeUpdate(connection, "INSERT INTO ROOM VALUES('1001','');");
		executeUpdate(connection, "INSERT INTO ROOM VALUES('1002','');");
		executeUpdate(connection, "INSERT INTO ROOM VALUES('1003','');");
		executeUpdate(connection, "INSERT INTO ROOM VALUES('1004','');");
		executeUpdate(connection, "INSERT INTO ROOM VALUES('1005','');");
	}

	private static void executeUpdate(Connection connection, String sql) throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		}
		finally {
			close(null, statement);
		}
	}

	private static void close(ResultSet resultSet, Statement statement) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		catch (SQLException e) {
		}
	}
}
