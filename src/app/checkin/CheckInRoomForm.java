/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkin;

import app.AppException;
import app.model.CheckinResult;
import app.roommanagement.RoomManagementUI;

/**
 * Form class for Check-in Customer
 * 
 */
public class CheckInRoomForm {

	private RoomManagementUI roomManagementUI = new RoomManagementUI();

	private RoomManagementUI getRoomManagementUI() {
		return roomManagementUI;
	}

	private String reservationNumber;

	public String checkIn() throws AppException {
		return checkInDetail().getRoomNumber();
	}

	public CheckinResult checkInDetail() throws AppException {
		RoomManagementUI roomManagementUI = getRoomManagementUI();
		return roomManagementUI.checkin(reservationNumber);
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}
	
}
