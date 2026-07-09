/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.model;

import java.util.Date;

/**
 * Result displayed by RoomManagementUI after check-in.
 */
public class CheckinResult {

	private String reservationNumber;

	private String roomNumber;

	private Date checkinDate;

	private Date checkoutDate;

	public CheckinResult(String reservationNumber, String roomNumber, Date checkinDate,
			Date checkoutDate) {
		this.reservationNumber = reservationNumber;
		this.roomNumber = roomNumber;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}
}
