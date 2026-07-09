/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.model;

import java.util.Date;

/**
 * Entity class corresponding to the Reservations class in the Astah design.
 */
public class Reservations {

	private String reservationNumber;

	private Date checkinDate;

	private Date checkoutDate;

	public Reservations(String reservationNumber, Date checkinDate, Date checkoutDate) {
		this.reservationNumber = reservationNumber;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public Date getcheckinDate() {
		return checkinDate;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}
}
