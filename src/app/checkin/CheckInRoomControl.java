/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkin;

import app.AppException;

/**
 * Control class for Check-in Customer
 * 
 */
public class CheckInRoomControl {

	public String checkIn(String reservationNumber) throws AppException {
		return new CheckinControl().checkin(reservationNumber).getRoomNumber();
	}
}
