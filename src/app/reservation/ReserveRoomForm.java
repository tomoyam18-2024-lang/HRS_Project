/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.reservation;

import java.util.Date;

import app.AppException;
import app.model.ReservationResult;

/**
 * Form class for Reserve Room
 * 
 */
public class ReserveRoomForm {

	private ReservationUI reservationUI = new ReservationUI();

	private Date stayingDate;

	private ReservationUI getReservationUI() {
		return reservationUI;
	}

	public String submitReservation() throws AppException {
		return submitReservationDetail().getReservationNumber();
	}

	public ReservationResult submitReservationDetail() throws AppException {
		ReservationUI reservationUI = getReservationUI();
		return reservationUI.reserveRoom(stayingDate);
	}

	public Date getStayingDate() {
		return stayingDate;
	}

	public void setStayingDate(Date stayingDate) {
		this.stayingDate = stayingDate;
	}

}
