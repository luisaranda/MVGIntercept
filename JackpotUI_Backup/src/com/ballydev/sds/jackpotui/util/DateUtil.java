/*****************************************************************************
 * $Id: DateUtil.java,v 1.1, 2008-11-25 05:46:33Z, Ambereen Drewitt$
 * $Date: 11/24/2008 11:46:33 PM$
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
package com.ballydev.sds.jackpotui.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * Date utilities
 * @author anantharajr
 * @version $Revision: 2$
 */
public class DateUtil {

	
	public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );	
	
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
	public Timestamp getTimeStamp(String date, String pattern){
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
	 * getMilliSeconds
	 * @param inputValue
	 * @return
	 */
	public static long getMilliSeconds(Timestamp inputValue){
		long milliSeconds = 0;
		milliSeconds = inputValue.getTime();		
		return milliSeconds;
	}
	/**
	 * getTime
	 * @param seconds
	 * @return
	 */
	public static Timestamp getTime(long seconds){
		if(seconds==0) {
			seconds=System.currentTimeMillis();
		}
		Timestamp inputValue = null;
		inputValue = new Timestamp(seconds);
		return inputValue;
	}

	/**
	 * getTimeOfXXMinutesBack
	 * @param minutes
	 * @return
	 */
	public static Timestamp getTimeOfXXMinutesBack(int minutes)
	{
		long currentTime=0;
		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		Date utcDate = null;
		utcDate = com.ballydev.sds.framework.util.DateUtil.getUTCTimeFromLocal(timestamp2);
		currentTime=utcDate.getTime();
		long timeNoOfMinutesBefore=0;
		timeNoOfMinutesBefore = currentTime- (minutes*60000);
		Timestamp timestamp = new Timestamp(timeNoOfMinutesBefore);
		return timestamp;
	}
	
	
    /**
     * getLocalTimeFromUTC
     * @param utcTimestamp
     * @return
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
     * getLocalTimeFromUTC
     * @param utcTimeInMillis
     * @return
     */
    /*public static Timestamp getLocalTimeFromUTC(long utcTimeInMillis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtil.getTime(utcTimeInMillis));
        calendar.add( Calendar.MILLISECOND, calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET ));
        calendar.setTimeZone( UTC );
        Timestamp localTimestamp = new Timestamp(calendar.getTimeInMillis());
        return localTimestamp;
    }*/
  
    
    
    /**
     * getUTCTimeFromLocal
     * @param localTimeInMillis
     * @return
     */
    /*public static Timestamp getUTCTimeFromLocal(long localTimeInMillis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime( DateUtil.getTime(localTimeInMillis) );
        calendar.add( Calendar.MILLISECOND, (calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET )) * -1);
        Timestamp utcTimestamp = new Timestamp(calendar.getTimeInMillis());
        return utcTimestamp;
    }*/
    
    /**
     * getUTCTimeFromLocal
     * @param localTimestamp
     * @return
     */
    /*public static Timestamp getUTCTimeFromLocal(Timestamp localTimestamp) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(localTimestamp);
        calendar.add( Calendar.MILLISECOND, (calendar.get( Calendar.ZONE_OFFSET ) + calendar.get( Calendar.DST_OFFSET )) * -1);
        Timestamp utcTimestamp = new Timestamp(calendar.getTimeInMillis());
        return utcTimestamp;
    }*/
    
    
    /**
     * main method
     * @param args
     */
    public static void main(String args[])
    {
    	//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	//log.info(getUTCTimeFromLocal(timestamp));	
     }
	
}
