/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.reservation;

import java.util.Date;

import app.AppException;

/**
 * Control class for Reserve Room
 * 
 */
public class ReserveRoomControl {

	public String makeReservation(Date stayingDate) throws AppException {
		return new ReservationControl().reserveRoom(stayingDate);
	}
}
