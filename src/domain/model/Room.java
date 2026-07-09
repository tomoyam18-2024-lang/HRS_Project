/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.model;

import java.util.Date;

/**
 * Entity class corresponding to the Room class in the Astah design.
 */
public class Room {

	private String roomNumber;

	private Date checkinDate;

	private Date checkoutDate;

	private int price;

	public Room(String roomNumber, Date checkinDate, Date checkoutDate, int price) {
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
