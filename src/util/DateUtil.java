/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for date<br>
 * 
 */
public class DateUtil {

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy/MM/dd");
	static {
		FORMATTER.setLenient(false);
	}

	public static Date createDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getMidnightDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return createDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
				.get(Calendar.DATE));
	}

	public static Date addDays(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, amount);
		return getMidnightDate(calendar.getTime());
	}

	public static int getDays(Date checkinDate, Date checkoutDate) {
		if (checkinDate == null || checkoutDate == null) {
			throw new NullPointerException("date");
		}
		int days = 0;
		Date cursor = getMidnightDate(checkinDate);
		Date end = getMidnightDate(checkoutDate);
		while (cursor.before(end)) {
			days++;
			cursor = addDays(cursor, 1);
		}
		return days;
	}

	public static String convertToString(Date date) {
		return FORMATTER.format(date);
	}

	public static Date convertToDate(String dateStr) {
		Date result = null;
		try {
			if (dateStr != null) {
				result = FORMATTER.parse(dateStr);
			}
		}
		catch (ParseException e) {
		}
		return result;
	}
}
