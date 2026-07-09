/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import domain.DatabaseInitializer;
import util.DateUtil;

/**
 * DB SQL implementation of Reservation Data Object interface<br>
 * 
 */
public class ReservationSqlDao implements ReservationDao {

	private static final String ID = "sa";

	private static final String PASSWORD = "";

	private static final String DRIVER_NAME = "org.hsqldb.jdbcDriver";

	private static final String URL = "jdbc:hsqldb:hsql://localhost;shutdown=true";

	private static final String TABLE_NAME = "RESERVATION";

	public Reservation getReservation(String reservationNumber) throws ReservationException {
		StringBuffer sql = new StringBuffer();
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		Reservation reservation = null;
		try {
			connection = getConnection();
			ensureCheckoutDateColumn(connection);
			statement = connection.createStatement();
			sql.append("SELECT reservationnumber, stayingdate, checkoutdate, status FROM ");
			sql.append(TABLE_NAME);
			sql.append(" WHERE RESERVATIONNUMBER= '");
			sql.append(reservationNumber);
			sql.append("';");
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next() == true) {
				reservation = new Reservation();
				reservation.setReservationNumber(reservationNumber);
				reservation.setStatus(resultSet.getString("status"));
				reservation.setCheckinDate(DateUtil.convertToDate(resultSet
						.getString("stayingDate")));
				reservation.setCheckoutDate(DateUtil.convertToDate(resultSet
						.getString("checkoutDate")));
				if (reservation.getCheckoutDate() == null) {
					reservation.setCheckoutDate(DateUtil.addDays(reservation.getCheckinDate(), 1));
				}
			}
		}
		catch (SQLException e) {
			ReservationException exception = new ReservationException(
					ReservationException.CODE_DB_EXEC_QUERY_ERROR, e);
			exception.getDetailMessages().add("getReservation()");
			throw exception;
		}
		finally {
			close(resultSet, statement, connection);
		}
		return reservation;
	}

	public void updateReservation(Reservation reservation) throws ReservationException {
		StringBuffer sql = new StringBuffer();
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = getConnection();
			ensureCheckoutDateColumn(connection);
			statement = connection.createStatement();
			sql.append("UPDATE ");
			sql.append(TABLE_NAME);
			sql.append(" set status = '");
			sql.append(reservation.getStatus());
			sql.append("' where reservationNumber='");
			sql.append(reservation.getReservationNumber());
			sql.append("';");
			resultSet = statement.executeQuery(sql.toString());
		}
		catch (SQLException e) {
			ReservationException exception = new ReservationException(
					ReservationException.CODE_DB_EXEC_QUERY_ERROR, e);
			exception.getDetailMessages().add("updateReservation()");
			throw exception;
		}
		finally {
			close(resultSet, statement, connection);
		}
	}

	public void createReservation(Reservation reservation) throws ReservationException {
		StringBuffer sql = new StringBuffer();
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = getConnection();
			ensureCheckoutDateColumn(connection);
			statement = connection.createStatement();
			sql.append("INSERT INTO ");
			sql.append(TABLE_NAME);
			sql.append(" (reservationNumber, stayingDate, checkoutDate, status) ");
			sql.append("values ('");
			sql.append(reservation.getReservationNumber());
			sql.append("', '");
			sql.append(DateUtil.convertToString(reservation.getCheckinDate()));
			sql.append("', '");
			sql.append(DateUtil.convertToString(reservation.getCheckoutDate()));
			sql.append("', '");
			sql.append(reservation.getStatus());
			sql.append("');");
			resultSet = statement.executeQuery(sql.toString());
		}
		catch (SQLException e) {
			ReservationException exception = new ReservationException(
					ReservationException.CODE_DB_EXEC_QUERY_ERROR, e);
			exception.getDetailMessages().add("createReservation()");
			throw exception;
		}
		finally {
			close(resultSet, statement, connection);
		}
	}

	private Connection getConnection() throws ReservationException {
		Connection connection = null;
		try {
			Class.forName(DRIVER_NAME);
			connection = DriverManager.getConnection(URL, ID, PASSWORD);
			DatabaseInitializer.ensureInitialized(connection);
		}
		catch (Exception e) {
			throw new ReservationException(ReservationException.CODE_DB_CONNECT_ERROR, e);
		}
		return connection;
	}

	private void ensureCheckoutDateColumn(Connection connection) throws ReservationException {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT checkoutdate FROM " + TABLE_NAME
					+ " WHERE 1 = 0;");
		}
		catch (SQLException e) {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				statement = connection.createStatement();
				statement.executeUpdate("ALTER TABLE " + TABLE_NAME
						+ " ADD checkoutdate VARCHAR(20);");
			}
			catch (SQLException alterException) {
				ReservationException exception = new ReservationException(
						ReservationException.CODE_DB_EXEC_QUERY_ERROR, alterException);
				exception.getDetailMessages().add("ensureCheckoutDateColumn()");
				throw exception;
			}
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException e) {
				throw new ReservationException(ReservationException.CODE_DB_CLOSE_ERROR, e);
			}
		}
	}

	private void close(ResultSet resultSet, Statement statement, Connection connection)
			throws ReservationException {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		catch (SQLException e) {
			throw new ReservationException(ReservationException.CODE_DB_CLOSE_ERROR, e);
		}
	}
}
