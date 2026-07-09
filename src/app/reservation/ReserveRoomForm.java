/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.reservation;

import java.util.Date;

import app.AppException;
import app.model.ReservationResult;
import util.DateUtil;

/**
 * Form class for Reserve Room
 * 
 */
public class ReserveRoomForm {

	private ReservationUI reservationUI = new ReservationUI();

	private Date stayingDate;

	private Date checkoutDate;

	private ReservationUI getReservationUI() {
		return reservationUI;
	}

	public String submitReservation() throws AppException {
		return submitReservationDetail().getReservationNumber();
	}

	public ReservationResult submitReservationDetail() throws AppException {
		ReservationUI reservationUI = getReservationUI();
		return reservationUI.reserveRoom(stayingDate, getEffectiveCheckoutDate());
	}

	public Date getStayingDate() {
		return stayingDate;
	}

	public void setStayingDate(Date stayingDate) {
		this.stayingDate = stayingDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	private Date getEffectiveCheckoutDate() {
		if (checkoutDate == null && stayingDate != null) {
			return DateUtil.addDays(stayingDate, 1);
		}
		return checkoutDate;
	}
}
