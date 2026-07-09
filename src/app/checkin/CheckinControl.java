/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkin;

import java.util.Date;

import app.AppException;
import app.ManagerFactory;
import app.model.CheckinResult;
import domain.payment.PaymentException;
import domain.payment.PaymentManager;
import domain.reservation.Reservation;
import domain.reservation.ReservationException;
import domain.reservation.ReservationManager;
import domain.room.RoomException;
import domain.room.RoomManager;

/**
 * Control class corresponding to CheckinControl in the Astah design.
 */
public class CheckinControl {

	private String reservationNumber;

	private String roomNumber;

	private Date checkoutDate;

	public CheckinResult checkin(String reservationNumber) throws AppException {
		this.reservationNumber = reservationNumber;
		return processcheckin(reservationNumber);
	}

	public String getreservationNumber() {
		return reservationNumber;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public CheckinResult processcheckin(String reservationNumber) throws AppException {
		try {
			Reservation reservation = getReservationManager().consumeReservationDetail(reservationNumber);
			Date checkinDate = reservation.getCheckinDate();
			checkoutDate = reservation.getCheckoutDate();
			roomNumber = getRoomManager().assignCustomer(checkinDate);
			getPaymentManager().createPayment(checkinDate, checkoutDate, roomNumber);
			return new CheckinResult(reservationNumber, roomNumber, checkinDate, checkoutDate);
		}
		catch (ReservationException e) {
			throw createAppException("Failed to check-in", e);
		}
		catch (RoomException e) {
			throw createAppException("Failed to check-in", e);
		}
		catch (PaymentException e) {
			throw createAppException("Failed to check-in", e);
		}
	}

	public Date getcheckoutDate() {
		return checkoutDate;
	}

	private ReservationManager getReservationManager() {
		return ManagerFactory.getInstance().getReservationManager();
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
		if (cause instanceof ReservationException) {
			exception.getDetailMessages().addAll(
					((ReservationException) cause).getDetailMessages());
		}
		if (cause instanceof RoomException) {
			exception.getDetailMessages().addAll(((RoomException) cause).getDetailMessages());
		}
		if (cause instanceof PaymentException) {
			exception.getDetailMessages().addAll(((PaymentException) cause).getDetailMessages());
		}
		return exception;
	}
}
