/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import util.DateUtil;
import app.AppException;
import app.checkin.CheckInRoomForm;
import app.checkout.CheckOutRoomForm;
import app.model.CheckinResult;
import app.model.CheckoutResult;
import app.model.ReservationResult;
import app.reservation.ReserveRoomForm;

/**
 * CUI class for Hotel Reservation Systems
 * 
 */
public class CUI {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private BufferedReader reader;

	CUI() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void execute() throws IOException {
		try {
			while (true) {
				int selectMenu;
				System.out.println("");
				System.out.println("Menu");
				System.out.println("1: Reservation");
				System.out.println("2: Check-in");
				System.out.println("3: Check-out");
				System.out.println("9: End");
				System.out.print("> ");

				try {
					String menu = reader.readLine();
					selectMenu = Integer.parseInt(menu);
				}
				catch (NumberFormatException e) {
					selectMenu = 4;
				}

				if (selectMenu == 9) {
					break;
				}

				switch (selectMenu) {
					case 1:
						reserveRoom();
						break;
					case 2:
						checkInRoom();
						break;
					case 3:
						checkOutRoom();
						break;
				}
			}
			System.out.println("Ended");
		}
		catch (AppException e) {
			System.err.println("Error");
			System.err.println(e.getFormattedDetailMessages(LINE_SEPARATOR));
		}
		finally {
			reader.close();
		}
	}

	private void reserveRoom() throws IOException, AppException {
		System.out.println("Input check-in date in the form of yyyy/mm/dd");
		System.out.print("> ");
		Date checkinDate = DateUtil.convertToDate(reader.readLine());
		if (checkinDate == null) {
			System.out.println("Invalid input");
			return;
		}

		System.out.println("Input check-out date in the form of yyyy/mm/dd");
		System.out.print("> ");
		Date checkoutDate = DateUtil.convertToDate(reader.readLine());
		if (checkoutDate == null || !checkinDate.before(checkoutDate)) {
			System.out.println("Invalid check-out date");
			return;
		}

		ReserveRoomForm reserveRoomForm = new ReserveRoomForm();
		reserveRoomForm.setStayingDate(checkinDate);
		reserveRoomForm.setCheckoutDate(checkoutDate);
		ReservationResult result = reserveRoomForm.submitReservationDetail();
		String reservationNumber = result.getReservationNumber();

		System.out.println("Reservation has been completed.");
		System.out.println("Check-in date is "
				+ DateUtil.convertToString(result.getCheckinDate()) + ".");
		System.out.println("Check-out date is "
				+ DateUtil.convertToString(result.getCheckoutDate()) + ".");
		System.out.println("Price is " + result.getPrice() + ".");
		System.out.println("Reservation number is " + reservationNumber + ".");
	}

	private void checkInRoom() throws IOException, AppException {
		System.out.println("Input reservation number");
		System.out.print("> ");

		String reservationNumber = reader.readLine();
		if (reservationNumber == null || reservationNumber.length() == 0) {
			System.out.println("Invalid reservation number");
			return;
		}

		CheckInRoomForm checkInRoomForm = new CheckInRoomForm();
		checkInRoomForm.setReservationNumber(reservationNumber);
		CheckinResult result = checkInRoomForm.checkInDetail();
		String roomNumber = result.getRoomNumber();
		System.out.println("Check-in has been completed.");
		System.out.println("Room number is " + roomNumber + ".");
		System.out.println("Check-in date is " + DateUtil.convertToString(result.getCheckinDate()) + ".");
		System.out.println("Check-out date is " + DateUtil.convertToString(result.getCheckoutDate()) + ".");
	}

	private void checkOutRoom() throws IOException, AppException {
		System.out.println("Input room number");
		System.out.print("> ");

		String roomNumber = reader.readLine();
		if (roomNumber == null || roomNumber.length() == 0) {
			System.out.println("Invalid room number");
			return;
		}

		CheckOutRoomForm checkoutRoomForm = new CheckOutRoomForm();
		checkoutRoomForm.setRoomNumber(roomNumber);
		CheckoutResult result = checkoutRoomForm.checkOutDetail();
		System.out.println("Check-out has been completed.");
		System.out.println("Room number is " + result.getRoomNumber() + ".");
		System.out.println("Check-in date is " + DateUtil.convertToString(result.getCheckinDate()) + ".");
		System.out.println("Check-out date is " + DateUtil.convertToString(result.getCheckoutDate()) + ".");
		System.out.println("Price is " + result.getPrice() + ".");
	}

	public static void main(String[] args) throws Exception {
		CUI cui = new CUI();
		cui.execute();
	}
}
