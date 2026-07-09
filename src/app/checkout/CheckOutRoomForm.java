/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkout;

import app.AppException;
import app.model.CheckoutResult;
import app.roommanagement.RoomManagementUI;

/**
 * Form class for Check-out Customer
 * 
 */
public class CheckOutRoomForm {

	private RoomManagementUI roomManagementUI = new RoomManagementUI();

	private RoomManagementUI getRoomManagementUI() {
		return roomManagementUI;
	}

	private String roomNumber;

	public void checkOut() throws AppException {
		checkOutDetail();
	}

	public CheckoutResult checkOutDetail() throws AppException {
		RoomManagementUI roomManagementUI = getRoomManagementUI();
		return roomManagementUI.checkout(roomNumber);
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

}
