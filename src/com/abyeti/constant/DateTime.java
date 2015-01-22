/**
 * 
 */
package com.abyeti.constant;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Pushpendra
 *
 */
public class DateTime {

	private static Date dateobj = new Date();
	private static Calendar cal = Calendar.getInstance();

	/**
	 *  Function to get instance Date and Time in Milli-Second
	 * @return
	 */
	public static long getDateTime() {
		cal.setTime(dateobj);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
}
