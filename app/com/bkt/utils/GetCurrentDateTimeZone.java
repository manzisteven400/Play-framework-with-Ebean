package com.bkt.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GetCurrentDateTimeZone {
	private static String datePattern = "yyyy-MM-dd HH:mm:ss";

	public static String getZone() {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	   // dateFormat.setTimeZone(TimeZone.getTimeZone("CAT"));
	    Calendar cal = Calendar.getInstance();
	    System.out.println("DATE with ZONE:......................................."+dateFormat.format(cal.getTime()));
	    String myZone=dateFormat.format(cal.getTime()).split(" ")[2];
	    return myZone;
	     }
	 public static void main(String[] args) {
		 //System.out.println("Get time zone:"+getZone());
		 
		 String sourceDate = USSDHelperUtils.getDateNow();
	        String resultDate;
			try {
				resultDate = convertTimeZone(sourceDate);
				
				System.out.println(getZone()+": "+ sourceDate);
		        System.out.println("CET: "+ resultDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        
	        
	 }public static String convertTimeZone(String date) throws ParseException {
	        long timestamp = stringToTimestamp(date, getZone());
	        String result = timestampToString(timestamp, "CAT");
	        return result;
	    }

	    public static long stringToTimestamp(String time, String timeZone) throws ParseException {
	        DateFormat format = getDateFormat(timeZone);
	        return format.parse(time).getTime();
	    }

	    public static String timestampToString(long timestamp, String timeZone) {
	        DateFormat format = getDateFormat(timeZone);
	        return format.format(new Date(timestamp));
	    }

	    private static DateFormat getDateFormat(String timeZone) {
	        DateFormat format = new SimpleDateFormat(datePattern);
	        format.setTimeZone(TimeZone.getTimeZone(timeZone));
	        return format;
	    }

}
