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

	public Date inputCheckoutDate(String checkoutDateText) {
		return DateUtil.convertToDate(checkoutDateText);
	}

	public ReservationResult selectRoomAndInputGuestInfo(Date stayingDate) throws AppException {
		return reservationControl.confirmReservation(stayingDate);
	}

	public ReservationResult selectRoomAndInputGuestInfo(Date checkinDate, Date checkoutDate)
			throws AppException {
		return reservationControl.confirmReservation(checkinDate, checkoutDate);
	}

	public String showDateAndPrice(ReservationResult result) {
		return "Check-in date is " + DateUtil.convertToString(result.getCheckinDate())
				+ ". Check-out date is " + DateUtil.convertToString(result.getCheckoutDate())
				+ ". Price is " + result.getPrice() + ".";
	}

	public String showReservationNumber(String reservationNumber) {
		return "Reservation number is " + reservationNumber + ".";
	}

	public String confirmReservation(Date stayingDate) throws AppException {
		return reservationControl.reserveRoom(stayingDate);
	}

	public String confirmReservation(Date checkinDate, Date checkoutDate) throws AppException {
		return reservationControl.reserveRoom(checkinDate, checkoutDate);
	}

	public String comfirmReservatin(Date stayingDate) throws AppException {
		return confirmReservation(stayingDate);
	}

	public ReservationResult reserveRoom(Date stayingDate) throws AppException {
		ReservationResult result = selectRoomAndInputGuestInfo(stayingDate);
		result.setReservationNumber(confirmReservation(stayingDate));
		return result;
	}

	public ReservationResult reserveRoom(Date checkinDate, Date checkoutDate) throws AppException {
		ReservationResult result = selectRoomAndInputGuestInfo(checkinDate, checkoutDate);
		result.setReservationNumber(confirmReservation(checkinDate, checkoutDate));
		return result;
	}
}
