/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.model;

import java.util.Date;

/**
 * Result displayed by RoomManagementUI after check-out.
 */
public class CheckoutResult {

	private String roomNumber;

	private Date checkinDate;

	private Date checkoutDate;

	private int price;

	public CheckoutResult(String roomNumber, Date checkinDate, Date checkoutDate, int price) {
		this.roomNumber = roomNumber;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.price = price;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public int getPrice() {
		return price;
	}
}
