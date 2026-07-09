/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.model;

import java.util.Date;

/**
 * Result displayed by ReservationUI.
 */
public class ReservationResult {

	private Date stayingDate;

	private int price;

	private String reservationNumber;

	public ReservationResult(Date stayingDate, int price) {
		this.stayingDate = stayingDate;
		this.price = price;
	}

	public Date getStayingDate() {
		return stayingDate;
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
