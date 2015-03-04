/*****************************************************************************
 * $Id: DateUtil.java,v 1.3, 2010-08-04 14:38:24Z, Ambereen Drewitt$
 * $Date: 8/4/2010 9:38:24 AM$
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
package com.ballydev.sds.slipsui.util;

import java.sql.Timestamp;
import java.util.TimeZone;

/**
 * Class to get the timestamp value  
 * 
 *
 */
public class DateUtil {

	public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );	
	
	/**
	 * A method to get the timestamp value  
	 * @param seconds
	 * @return inputValue
	 */
	public static Timestamp getTime(long seconds){
		Timestamp inputValue = null;
		inputValue = new Timestamp(seconds);
		return inputValue;
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
}
