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
		return confirmReservation(stayingDate, DateUtil.addDays(stayingDate, 1));
	}

	public ReservationResult confirmReservation(Date checkinDate, Date checkoutDate)
			throws AppException {
		validatePeriod(checkinDate, checkoutDate);
		confirmVacancy(checkinDate, checkoutDate);
		return new ReservationResult(checkinDate, checkoutDate, getPrice(checkinDate, checkoutDate));
	}

	public void confirmVacancy(Date stayingDate) throws AppException {
		confirmVacancy(stayingDate, DateUtil.addDays(stayingDate, 1));
	}

	public void confirmVacancy(Date checkinDate, Date checkoutDate) throws AppException {
		try {
			if (!hotel.confirmPossibleReservation(checkinDate, checkoutDate)) {
				AppException exception = new AppException("Failed to reserve");
				exception.getDetailMessages().add("No vacant room between "
						+ DateUtil.convertToString(checkinDate) + " and "
						+ DateUtil.convertToString(checkoutDate));
				throw exception;
			}
		}
		catch (RoomException e) {
			throw createAppException("Failed to reserve", e);
		}
	}

	public String reserveRoom(Date stayingDate) throws AppException {
		return reserveRoom(stayingDate, DateUtil.addDays(stayingDate, 1));
	}

	public String reserveRoom(Date checkinDate, Date checkoutDate) throws AppException {
		validatePeriod(checkinDate, checkoutDate);
		confirmVacancy(checkinDate, checkoutDate);
		try {
			getRoomManager().updateRoomAvailableQty(checkinDate, checkoutDate,
					AVAILABLE_QTY_CHANGE_FOR_ONE_NIGHT);
			reservationNumber = createReservation(checkinDate, checkoutDate);
			return reservationNumber;
		}
		catch (RoomException e) {
			throw createAppException("Failed to reserve", e);
		}
	}

	public String createReservation(Date stayingDate) throws AppException {
		return createReservation(stayingDate, DateUtil.addDays(stayingDate, 1));
	}

	public String createReservation(Date checkinDate, Date checkoutDate) throws AppException {
		try {
			return getReservationManager().createReservation(checkinDate, checkoutDate);
		}
		catch (ReservationException e) {
			throw createAppException("Failed to reserve", e);
		}
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	private int getPrice(Date checkinDate, Date checkoutDate) {
		return getPaymentManager().getRatePerDay(null) * DateUtil.getDays(checkinDate, checkoutDate);
	}

	private void validatePeriod(Date checkinDate, Date checkoutDate) throws AppException {
		if (checkinDate == null || checkoutDate == null || !checkinDate.before(checkoutDate)) {
			AppException exception = new AppException("Failed to reserve");
			exception.getDetailMessages().add("Checkout date must be after check-in date.");
			throw exception;
		}
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
