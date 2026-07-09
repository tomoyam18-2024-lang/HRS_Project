/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.reservation;

import java.util.Date;

import app.AppException;
import app.ManagerFactory;
import app.model.ReservationResult;
import domain.model.Hotel;
import domain.reservation.ReservationException;
import domain.reservation.ReservationManager;
import domain.room.RoomException;
import domain.room.RoomManager;
import util.DateUtil;

/**
 * Control class corresponding to ReservationControl in the Astah design.
 */
public class ReservationControl {

	private static final int AVAILABLE_QTY_CHANGE_FOR_ONE_NIGHT = -1;

	private Hotel hotel = new Hotel();

	private String reservationNumber;

	public ReservationResult confirmReservation(Date stayingDate) throws AppException {
		confirmVacancy(stayingDate);
		return new ReservationResult(stayingDate, getPrice());
	}

	public void confirmVacancy(Date stayingDate) throws AppException {
		try {
			if (!hotel.confirmPossibleReservation(stayingDate)) {
				AppException exception = new AppException("Failed to reserve");
				exception.getDetailMessages().add("No vacant room on "
						+ DateUtil.convertToString(stayingDate));
				throw exception;
			}
		}
		catch (RoomException e) {
			throw createAppException("Failed to reserve", e);
		}
	}

	public String reserveRoom(Date stayingDate) throws AppException {
		confirmVacancy(stayingDate);
		try {
			getRoomManager().updateRoomAvailableQty(stayingDate,
					AVAILABLE_QTY_CHANGE_FOR_ONE_NIGHT);
			reservationNumber = createReservation(stayingDate);
			return reservationNumber;
		}
		catch (RoomException e) {
			throw createAppException("Failed to reserve", e);
		}
	}

	public String createReservation(Date stayingDate) throws AppException {
		try {
			return getReservationManager().createReservation(stayingDate);
		}
		catch (ReservationException e) {
			throw createAppException("Failed to reserve", e);
		}
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	private int getPrice() {
		return getPaymentManager().getRatePerDay(null);
	}

	private ReservationManager getReservationManager() {
		return ManagerFactory.getInstance().getReservationManager();
	}

	private RoomManager getRoomManager() {
		return ManagerFactory.getInstance().getRoomManager();
	}

	private domain.payment.PaymentManager getPaymentManager() {
		return ManagerFactory.getInstance().getPaymentManager();
	}

	private AppException createAppException(String message, Exception cause) {
		AppException exception = new AppException(message, cause);
		exception.getDetailMessages().add(cause.getMessage());
		if (cause instanceof RoomException) {
			exception.getDetailMessages().addAll(((RoomException) cause).getDetailMessages());
		}
		if (cause instanceof ReservationException) {
			exception.getDetailMessages().addAll(
					((ReservationException) cause).getDetailMessages());
		}
		return exception;
	}
}
