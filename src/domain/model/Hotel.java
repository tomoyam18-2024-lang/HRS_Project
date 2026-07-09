/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.model;

import java.util.Date;

import domain.DaoFactory;
import domain.room.AvailableQty;
import domain.room.AvailableQtyDao;
import domain.room.RoomDao;
import domain.room.RoomException;
import util.DateUtil;

/**
 * Entity class corresponding to the Hotel class in the Astah design.
 */
public class Hotel {

	public static final int PRICE_PER_NIGHT = 8000;

	public boolean confirmPossibleReservation(Date stayingDate) throws RoomException {
		return getAvailableRoomCount(stayingDate) > 0;
	}

	public boolean confirmPossibleReservation(Date checkinDate, Date checkoutDate)
			throws RoomException {
		validatePeriod(checkinDate, checkoutDate);
		Date cursor = checkinDate;
		while (cursor.before(checkoutDate)) {
			if (!confirmPossibleReservation(cursor)) {
				return false;
			}
			cursor = DateUtil.addDays(cursor, 1);
		}
		return true;
	}

	public int getAvailableRoomCount(Date stayingDate) throws RoomException {
		AvailableQty availableQty = getAvailableQtyDao().getAvailableQty(stayingDate);
		if (availableQty == null || availableQty.getQty() == AvailableQty.AVAILABLE_ALL) {
			return getRoomDao().getRooms().size();
		}
		return availableQty.getQty();
	}

	public int getPrice() {
		return PRICE_PER_NIGHT;
	}

	public int getPrice(Date checkinDate, Date checkoutDate) {
		validatePeriod(checkinDate, checkoutDate);
		return PRICE_PER_NIGHT * DateUtil.getDays(checkinDate, checkoutDate);
	}

	private void validatePeriod(Date checkinDate, Date checkoutDate) {
		if (checkinDate == null) {
			throw new NullPointerException("checkinDate");
		}
		if (checkoutDate == null) {
			throw new NullPointerException("checkoutDate");
		}
		if (!checkinDate.before(checkoutDate)) {
			throw new IllegalArgumentException("checkoutDate must be after checkinDate");
		}
	}

	private AvailableQtyDao getAvailableQtyDao() {
		return DaoFactory.getInstance().getAvailableQtyDao();
	}

	private RoomDao getRoomDao() {
		return DaoFactory.getInstance().getRoomDao();
	}
}
