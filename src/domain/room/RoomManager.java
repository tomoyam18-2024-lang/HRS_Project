/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.room;

import java.util.Date;
import java.util.List;

import util.DateUtil;
import domain.DaoFactory;

/**
 * Manager for rooms<br>
 * 
 */
public class RoomManager {
	
	public void updateRoomAvailableQty(Date stayingDate, int qtyOfChange) throws RoomException,
			NullPointerException {
		if (stayingDate == null) {
			throw new NullPointerException("stayingDate");
		}
		if (qtyOfChange == 0) {
			return;
		}

		AvailableQtyDao availableQtyDao = getAvailableQtyDao();
		AvailableQty availableQty = availableQtyDao.getAvailableQty(stayingDate);
		if (availableQty == null) {
			availableQty = new AvailableQty();
			availableQty.setQty(AvailableQty.AVAILABLE_ALL);
			availableQty.setDate(stayingDate);
		}

		int maxAvailableQty = getMaxAvailableQty();
		if (availableQty.getQty() == AvailableQty.AVAILABLE_ALL) {
			availableQty.setQty(maxAvailableQty);
			availableQtyDao.createAbailableQty(availableQty);
		}

		int changedAvailableQty = availableQty.getQty() + qtyOfChange;
		if (changedAvailableQty >= 0 && changedAvailableQty <= maxAvailableQty) {
			availableQty.setQty(changedAvailableQty);
			availableQty.setDate(stayingDate);
			availableQtyDao.updateAvailableQty(availableQty);
		}
		else {
			RoomException exception = new RoomException(
					RoomException.CODE_AVAILABLE_QTY_OUT_OF_BOUNDS);
			exception.getDetailMessages().add("staying_date[" + DateUtil.convertToString(stayingDate) + "]");
			throw exception;

		}
	}

	public void updateRoomAvailableQty(Date checkinDate, Date checkoutDate, int qtyOfChange)
			throws RoomException, NullPointerException {
		if (checkinDate == null) {
			throw new NullPointerException("checkinDate");
		}
		if (checkoutDate == null) {
			throw new NullPointerException("checkoutDate");
		}
		if (!checkinDate.before(checkoutDate)) {
			throw new IllegalArgumentException("checkoutDate must be after checkinDate");
		}
		Date cursor = checkinDate;
		while (cursor.before(checkoutDate)) {
			updateRoomAvailableQty(cursor, qtyOfChange);
			cursor = DateUtil.addDays(cursor, 1);
		}
	}

	private int getMaxAvailableQty() throws RoomException {
		RoomDao roomDao = getRoomDao();
		List rooms = roomDao.getRooms();
		return rooms.size();
	}

	public Room getRoom(String roomNumber) throws RoomException, NullPointerException {
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}
		RoomDao roomDao = getRoomDao();
		Room room = roomDao.getRoom(roomNumber);
		if (room == null) {
			RoomException exception = new RoomException(RoomException.CODE_ROOM_NOT_FOUND);
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		return room;
	}

	public String assignCustomer(Date stayingDate) throws RoomException, NullPointerException {
		if (stayingDate == null) {
			throw new NullPointerException("stayingDate");
		}
		RoomDao roomDao = getRoomDao();
		List emptyRooms = roomDao.getEmptyRooms();
		if (emptyRooms.size() == 0) {
			RoomException exception = new RoomException(RoomException.CODE_EMPTYROOM_NOT_FOUND);
			throw exception;
		}
		Room room = (Room) emptyRooms.get(0);
		String roomNumber = room.getRoomNumber();
		room.setStayingDate(stayingDate);
		roomDao.updateRoom(room);
		return roomNumber;
	}

	public Date removeCustomer(String roomNumber) throws RoomException, NullPointerException {
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}
		RoomDao roomDao = getRoomDao();
		Room room = roomDao.getRoom(roomNumber);
		if (room == null) {
			RoomException exception = new RoomException(RoomException.CODE_ROOM_NOT_FOUND);
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		Date stayingDate = room.getStayingDate();
		if (stayingDate == null) {
			RoomException exception = new RoomException(RoomException.CODE_ROOM_NOT_FULL);
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		room.setStayingDate(null);
		roomDao.updateRoom(room);
		return stayingDate;
	}

	private AvailableQtyDao getAvailableQtyDao() {
		return DaoFactory.getInstance().getAvailableQtyDao();
	}

	private RoomDao getRoomDao() {
		return DaoFactory.getInstance().getRoomDao();
	}
}
