/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.reservation;

import java.util.Date;

/**
 * Reservation entity<br>
 * 
 */
public class Reservation {

	public static final String RESERVATION_STATUS_CREATE = "create";

	public static final String RESERVATION_STATUS_CONSUME = "consume";

	private String reservationNumber;

	private Date stayingDate;

	private Date checkoutDate;

	private String status;

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	public Date getStayingDate() {
		return stayingDate;
	}

	public void setStayingDate(Date stayingDate) {
		this.stayingDate = stayingDate;
	}

	public Date getCheckinDate() {
		return stayingDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.stayingDate = checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
