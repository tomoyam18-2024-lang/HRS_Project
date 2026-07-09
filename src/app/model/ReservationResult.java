/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.model;

import java.util.Date;

/**
 * Result displayed by ReservationUI.
 */
public class ReservationResult {

	private Date checkinDate;

	private Date checkoutDate;

	private int price;

	private String reservationNumber;

	public ReservationResult(Date checkinDate, Date checkoutDate, int price) {
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.price = price;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public Date getStayingDate() {
		return checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public int getPrice() {
		return price;
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}
}
