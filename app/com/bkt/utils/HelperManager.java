/**
 * 
 */
package com.bkt.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author pc
 *
 */
public class HelperManager {

	
	public static Date getDateToday() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		return date;

	}

	public static int getYearToday() {
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		/*int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();*/
		
		
		return year;
	    }
	
	public static String converDateFormat(Date myDateStr) {
		String today = null;

		SimpleDateFormat dataBaseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		today = dataBaseTime.format(myDateStr);
		System.out.println("date today is::" + today);
		return today;
	}
	public static void main(String[] args){
		System.out.println("Year now is..."+getYearToday());
	}
}
