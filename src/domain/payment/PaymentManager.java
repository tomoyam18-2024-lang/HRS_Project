/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.payment;

import java.util.Date;

import util.DateUtil;
import domain.DaoFactory;

/**
 * Manager for payments<br>
 * 
 */
public class PaymentManager {

	private static final int RATE_PER_DAY = 8000;

	public void createPayment(Date stayingDate, String roomNumber) throws PaymentException,
			NullPointerException {
		if (stayingDate == null) {
			throw new NullPointerException("stayingDate");
		}
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}

		Payment payment = new Payment();
		payment.setStayingDate(stayingDate);
		payment.setRoomNumber(roomNumber);
		payment.setAmount(getRatePerDay(roomNumber));
		payment.setStatus(Payment.PAYMENT_STATUS_CREATE);

		PaymentDao paymentDao = getPaymentDao();
		paymentDao.createPayment(payment);
	}

	public void createPayment(Date checkinDate, Date checkoutDate, String roomNumber)
			throws PaymentException, NullPointerException {
		validatePeriod(checkinDate, checkoutDate);
		Date cursor = checkinDate;
		while (cursor.before(checkoutDate)) {
			createPayment(cursor, roomNumber);
			cursor = DateUtil.addDays(cursor, 1);
		}
	}

	public int getRatePerDay(String roomNumber) {
		return RATE_PER_DAY;
	}

	public Payment getPayment(Date stayingDate, String roomNumber) throws PaymentException,
			NullPointerException {
		if (stayingDate == null) {
			throw new NullPointerException("stayingDate");
		}
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}

		PaymentDao paymentDao = getPaymentDao();
		Payment payment = paymentDao.getPayment(stayingDate, roomNumber);
		if (payment == null) {
			PaymentException exception = new PaymentException(PaymentException.CODE_PAYMENT_NOT_FOUND);
			exception.getDetailMessages().add("staying_date[" + DateUtil.convertToString(stayingDate) + "]");
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		return payment;
	}

	public Date getCheckoutDate(Date checkinDate, String roomNumber) throws PaymentException {
		if (checkinDate == null) {
			throw new NullPointerException("checkinDate");
		}
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}

		PaymentDao paymentDao = getPaymentDao();
		Date cursor = checkinDate;
		boolean paymentFound = false;
		while (true) {
			Payment payment = paymentDao.getPayment(cursor, roomNumber);
			if (payment == null || Payment.PAYMENT_STATUS_CONSUME.equals(payment.getStatus())) {
				break;
			}
			paymentFound = true;
			cursor = DateUtil.addDays(cursor, 1);
		}
		if (!paymentFound) {
			PaymentException exception = new PaymentException(PaymentException.CODE_PAYMENT_NOT_FOUND);
			exception.getDetailMessages().add("staying_date[" + DateUtil.convertToString(checkinDate) + "]");
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		return cursor;
	}

	public int getPaymentAmount(Date checkinDate, Date checkoutDate, String roomNumber)
			throws PaymentException {
		validatePeriod(checkinDate, checkoutDate);
		int amount = 0;
		Date cursor = checkinDate;
		while (cursor.before(checkoutDate)) {
			amount += getPayment(cursor, roomNumber).getAmount();
			cursor = DateUtil.addDays(cursor, 1);
		}
		return amount;
	}

	public void consumePayment(Date stayingDate, String roomNumber) throws PaymentException,
			NullPointerException {
		if (stayingDate == null) {
			throw new NullPointerException("stayingDate");
		}
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}

		PaymentDao paymentDao = getPaymentDao();
		Payment payment = paymentDao.getPayment(stayingDate, roomNumber);
		if (payment == null) {
			PaymentException exception = new PaymentException(
					PaymentException.CODE_PAYMENT_NOT_FOUND);
			exception.getDetailMessages().add("staying_date[" + DateUtil.convertToString(stayingDate) + "]");
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		if (payment.getStatus().equals(Payment.PAYMENT_STATUS_CONSUME)) {
			PaymentException exception = new PaymentException(
					PaymentException.CODE_PAYMENT_ALREADY_CONSUMED);
			exception.getDetailMessages().add("staying_date[" + DateUtil.convertToString(stayingDate) + "]");
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		
		payment.setStatus(Payment.PAYMENT_STATUS_CONSUME);
		paymentDao.updatePayment(payment);
	}

	public void consumePayment(Date checkinDate, Date checkoutDate, String roomNumber)
			throws PaymentException, NullPointerException {
		validatePeriod(checkinDate, checkoutDate);
		Date cursor = checkinDate;
		while (cursor.before(checkoutDate)) {
			consumePayment(cursor, roomNumber);
			cursor = DateUtil.addDays(cursor, 1);
		}
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

	private PaymentDao getPaymentDao() {
		return DaoFactory.getInstance().getPaymentDao();
	}
}
