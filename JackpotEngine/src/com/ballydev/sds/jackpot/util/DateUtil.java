/*****************************************************************************
 * $Id: DateUtil.java,v 1.3, 2010-01-21 13:32:51Z, Anbuselvi, Balasubramanian$
 * $Date: 1/21/2010 7:32:51 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ballydev.sds.db.util.DateHelper;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;

/**
 * Class to convert the date format
 * @author anantharajr
 * @version $Revision: 4$
 *
 */
public class DateUtil {
	
	/**
	 * UTC time
	 */
	public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );	
	
	/**
	 *Instance of the Logger 
	 */
	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * 
	 * This method converts the given date from String into a TimeStamp obect. The pattern input
	 * specifies the date format type. It can be any of the following,
	 * 
	 * "MM/dd/yyyy"
	 * "dd/MM/yyyy"
	 * 
	 * For more information please refer to Date and Time Pattern Examples at
	 * http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html
	 * 
	 * @param date	Date in String Format
	 * @param pattern	Date Form
	 * @return	TimeStamp
	 */
	public static Timestamp getTimeStamp(String date, String pattern){
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormatter.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timestamp timeStamp = new Timestamp(calendar.getTimeInMillis());
		return timeStamp;
		
	}
	
	/**
	 * Method to get the time in  milliseconds
	 * @param inputValue
	 * @return long
	 */
	public static long getMilliSeconds(Timestamp inputValue){
		long milliSeconds = 0;
		milliSeconds = inputValue.getTime();		
		return milliSeconds;
	}
	
	/**
	 * Method to get the time in  Timestamp
	 * @param seconds
	 * @return Timestamp
	 */
	public static Timestamp getTime(long seconds){
		Timestamp inputValue = null;
		inputValue = new Timestamp(seconds);
		return inputValue;
	}

	/**
	 * Method to return the time before the no of minutes specifies as input
	 * @param minutes
	 * @return Timestamp
	 */
	public static Timestamp getTimeOfXXMinutesBack(int minutes)
	{
		long currentTime=0;
		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		Date utcDate = null;
		utcDate = DateHelper.getUTCTimeFromLocal(timestamp2);
		currentTime=utcDate.getTime();
		long timeNoOfMinutesBefore=0;
		long minsInSecs = (long)minutes * 60000;
		timeNoOfMinutesBefore = currentTime- minsInSecs;
		Timestamp timestamp = new Timestamp(timeNoOfMinutesBefore);
		return timestamp;
	}
	
	
    /**
     * Method to get the local time from UTC
     * @param utcTimestamp
     * @return Timestamp
     */
    /*public static Timestamp getLocalTimeFromUTC(Timestamp utcTimestamp) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime( utcTimestamp );
        calendar.add( Calendar.MILLISECOND, calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET ));
        calendar.setTimeZone( UTC );
        Timestamp localTimestamp = new Timestamp(calendar.getTimeInMillis());
        return localTimestamp;
    }*/
  
    /**
     * Method to get the local time from UTC
     * @param utcTimeInMillis
     * @return Timestamp
     */
    /*public static Timestamp getLocalTimeFromUTC(long utcTimeInMillis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtil.getTime(utcTimeInMillis));
        calendar.add( Calendar.MILLISECOND, calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET ));
        calendar.setTimeZone( UTC );
        Timestamp localTimestamp = new Timestamp(calendar.getTimeInMillis());
        return localTimestamp;
    }
  */
    
    
    /**
     * Method to get UTC time from Local
     * @param localTimeInMillis
     * @return Timestamp
     */
    /*public static Timestamp getUTCTimeFromLocal(long localTimeInMillis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime( DateUtil.getTime(localTimeInMillis) );
        calendar.add( Calendar.MILLISECOND, (calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET )) * -1);
        Timestamp utcTimestamp = new Timestamp(calendar.getTimeInMillis());
        return utcTimestamp;
    }*/
    
    /**
     * Method to get UTC time from Local
     * @param localTimestamp
     * @return Timestamp
     */
  /*  public static Timestamp getUTCTimeFromLocal(Timestamp localTimestamp) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(localTimestamp);
        calendar.add( Calendar.MILLISECOND, (calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET )) * -1);
        Timestamp utcTimestamp = new Timestamp(calendar.getTimeInMillis());
        return utcTimestamp;
    }*/
    
    /**
	 * This method converts the given seconds to its date equivalent and returns
	 * the string representation of that date in the format mm/dd/yyyy
	 * @param seconds
	 * @returns string representation of date
	 */
	public static String getStringDate(long seconds){
		String resultDate = null;
		if(seconds==0){
			return resultDate=null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(seconds);
		String date = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		resultDate = month+"/"+date+"/"+year;
		return resultDate;
	}
    
	/**
	 * This method converts the given seconds and returns its 
	 * date equivalent in the pattern M/d/yyyy
	 * @param seconds
	 * @returns Date equivalent of the given seconds
	 */
	public static String getDateInString(long seconds){
		Date date = null;
		String strDate = null;
		if(seconds==0){
			
			return null;
		}
		String dateToParse = getStringDate(seconds);
		String pattern = "M/d/yyyy";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormatter.parse(dateToParse));
			date = calendar.getTime();
			strDate = date.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	
	/**
	 * This method converts the given seconds and returns its 
	 * date equivalent in the pattern M/d/yyyy
	 * @param seconds
	 * @returns Date equivalent of the given seconds
	 */
	public static Date getDate(long seconds){
		Date date = null;
		if(seconds==0){
			return date=null;
		}
		String dateToParse = getStringDate(seconds);
		String pattern = "M/d/yyyy";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormatter.parse(dateToParse));
			date = calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
     * Method to get the Formatted date for the pattern yyyyMMddHHmm
     * @param seconds
     * @return Date
     */
    public static Date getFormattedDate(long seconds)
    {    	
    	Date date = null;
    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmm");
    	dateFormatter.format(seconds);
    	 Calendar calendar = Calendar.getInstance();
    	 try {
			calendar.setTime(dateFormatter.parse(dateFormatter.format(seconds)));
			date = calendar.getTime();		
		} catch (ParseException e) {
			log.error(e);
		}
		return date;
    }
	
    /**
     * Class' Main Method
     * @param args
     */
    public static void main(String args[])
    {
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    //	System.out.println(getUTCTimeFromLocal(timestamp));	
    	
    	DateUtil.getTimeOfXXMinutesBack(44444);
     }
	
}
