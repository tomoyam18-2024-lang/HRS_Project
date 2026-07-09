/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkout;

import app.AppException;

/**
 * Control class for Check-out Customer
 * 
 */
public class CheckOutRoomControl {
	
	public void checkOut(String roomNumber) throws AppException {
		new CheckoutControl().checkout(roomNumber);
	}
}
