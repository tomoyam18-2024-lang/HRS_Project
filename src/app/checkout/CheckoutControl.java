/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkout;

import java.util.Date;

import app.AppException;
import app.ManagerFactory;
import app.model.CheckoutResult;
import domain.payment.PaymentException;
import domain.payment.PaymentManager;
import domain.room.Room;
import domain.room.RoomException;
import domain.room.RoomManager;

/**
 * Control class corresponding to CheckoutControl in the Astah design.
 */
public class CheckoutControl {

	private String roomNumber;

	public CheckoutResult checkout(String roomNumber) throws AppException {
		this.roomNumber = roomNumber;
		CheckoutResult result = getRoomNumberAndPrice(roomNumber);
		try {
			getPaymentManager().consumePayment(result.getCheckinDate(), result.getCheckoutDate(),
					roomNumber);
			clearRoom(roomNumber);
			return result;
		}
		catch (PaymentException e) {
			throw createAppException("Failed to check-out", e);
		}
	}

	public CheckoutResult getRoomNumberAndPrice(String roomNumber) throws AppException {
		try {
			Room room = getRoomManager().getRoom(roomNumber);
			Date checkinDate = room.getStayingDate();
			if (checkinDate == null) {
				RoomException exception = new RoomException(RoomException.CODE_ROOM_NOT_FULL);
				exception.getDetailMessages().add("room_number[" + roomNumber + "]");
				throw exception;
			}
			Date checkoutDate = getPaymentManager().getCheckoutDate(checkinDate, roomNumber);
			int price = getPaymentManager().getPaymentAmount(checkinDate, checkoutDate, roomNumber);
			return new CheckoutResult(roomNumber, checkinDate, checkoutDate, price);
		}
		catch (RoomException e) {
			throw createAppException("Failed to check-out", e);
		}
		catch (PaymentException e) {
			throw createAppException("Failed to check-out", e);
		}
	}

	public int getPrice(String roomNumber) throws AppException {
		return getRoomNumberAndPrice(roomNumber).getPrice();
	}

	public void clearRoom(String roomNumber) throws AppException {
		try {
			getRoomManager().removeCustomer(roomNumber);
		}
		catch (RoomException e) {
			throw createAppException("Failed to check-out", e);
		}
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	private RoomManager getRoomManager() {
		return ManagerFactory.getInstance().getRoomManager();
	}

	private PaymentManager getPaymentManager() {
		return ManagerFactory.getInstance().getPaymentManager();
	}

	private AppException createAppException(String message, Exception cause) {
		AppException exception = new AppException(message, cause);
		exception.getDetailMessages().add(cause.getMessage());
		if (cause instanceof RoomException) {
			exception.getDetailMessages().addAll(((RoomException) cause).getDetailMessages());
		}
		if (cause instanceof PaymentException) {
			exception.getDetailMessages().addAll(((PaymentException) cause).getDetailMessages());
		}
		return exception;
	}
}
