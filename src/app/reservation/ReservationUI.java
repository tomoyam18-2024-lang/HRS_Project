/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.reservation;

import java.util.Date;

import app.AppException;
import app.model.ReservationResult;
import util.DateUtil;

/**
 * Boundary class corresponding to ReservationUI in the Astah design.
 */
public class ReservationUI {

	private ReservationControl reservationControl = new ReservationControl();

	public Date inputstayDate(String stayingDateText) {
		return DateUtil.convertToDate(stayingDateText);
	}

	public ReservationResult selectRoomAndInputGuestInfo(Date stayingDate) throws AppException {
		return reservationControl.confirmReservation(stayingDate);
	}

	public String showDateAndPrice(ReservationResult result) {
		return "Arrival date is " + DateUtil.convertToString(result.getStayingDate())
				+ ". Price is " + result.getPrice() + ".";
	}

	public String showReservationNumber(String reservationNumber) {
		return "Reservation number is " + reservationNumber + ".";
	}

	public String confirmReservation(Date stayingDate) throws AppException {
		return reservationControl.reserveRoom(stayingDate);
	}

	public String comfirmReservatin(Date stayingDate) throws AppException {
		return confirmReservation(stayingDate);
	}

	public ReservationResult reserveRoom(Date stayingDate) throws AppException {
		ReservationResult result = selectRoomAndInputGuestInfo(stayingDate);
		result.setReservationNumber(confirmReservation(stayingDate));
		return result;
	}

	private ReservationControl getReservationControl() {
		return reservationControl;
	}
}
