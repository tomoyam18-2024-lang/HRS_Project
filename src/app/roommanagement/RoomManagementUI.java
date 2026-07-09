/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.roommanagement;

import app.AppException;
import app.checkin.CheckinControl;
import app.checkout.CheckoutControl;
import app.model.CheckinResult;
import app.model.CheckoutResult;
import util.DateUtil;

/**
 * Boundary class corresponding to RoomManagementUI in the Astah design.
 */
public class RoomManagementUI {

	private CheckinControl checkinControl = new CheckinControl();

	private CheckoutControl checkoutControl = new CheckoutControl();

	public String selectCheckinCheckout(String selectedOperation) {
		return selectedOperation;
	}

	public String inputReservationNumber(String reservationNumber) {
		return reservationNumber;
	}

	public String inputRoomNumber(String roomNumber) {
		return roomNumber;
	}

	public CheckinResult checkin(String reservationNumber) throws AppException {
		return checkinControl.checkin(inputReservationNumber(reservationNumber));
	}

	public CheckoutResult checkout(String roomNumber) throws AppException {
		return checkoutControl.checkout(inputRoomNumber(roomNumber));
	}

	public String showRoomNumberAndDate(CheckinResult result) {
		return "Room number is " + result.getRoomNumber() + ". Check-in date is "
				+ DateUtil.convertToString(result.getCheckinDate()) + ". Check-out date is "
				+ DateUtil.convertToString(result.getCheckoutDate()) + ".";
	}

	public String showCheckoutDataAndPriice(CheckoutResult result) {
		return "Room number is " + result.getRoomNumber() + ". Check-in date is "
				+ DateUtil.convertToString(result.getCheckinDate()) + ". Check-out date is "
				+ DateUtil.convertToString(result.getCheckoutDate()) + ". Price is "
				+ result.getPrice() + ".";
	}

	public String showCheckoutDataAndPrice(CheckoutResult result) {
		return showCheckoutDataAndPriice(result);
	}

	public String showWarning(String message) {
		return message;
	}
}
