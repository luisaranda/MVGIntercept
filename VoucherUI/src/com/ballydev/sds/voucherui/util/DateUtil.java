/*
 * $Id: DateUtil.java,v 1.3, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$
 *
 * Copyright Bally Gaming Inc. 2001-2004
 *
 * All programs and files on this medium are operated under U.S. patents
 * Nos. 5,429,361 & 5,470,079.
 *
 * All programs and files on this medium are copyrighted works and all
 * rights are reserved.
 * 
 * Duplication of these in whole or in part is prohibited without written
 * permission from Bally Gaming Inc., Las Vegas, Nevada, U.S.A.
 *
 */
package com.ballydev.sds.voucherui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.ballydev.sds.framework.SessionUtility;



public class DateUtil {

	private Calendar date;

	/* Create the current local timezone calendar object */
	public DateUtil() {
		date = Calendar.getInstance();
	}

	/* Create a calendar object given the db2 format of
	   YYYY-MM-DD.HH.MM.SS.MMMMMM */
	public DateUtil(String db2Date) {
		if(db2Date!=null ){
			String year = db2Date.substring(0, 4);
			String month = db2Date.substring(5, 7);
			String day = db2Date.substring(8, 10);
			String hour = db2Date.substring(11, 13);
			String min = db2Date.substring(14, 16);
			String sec = db2Date.substring(17, 19);
			String mil = "000";
			if(db2Date.length()>=23){
				mil = db2Date.substring(20, 23);
			}

			date = new GregorianCalendar();

			try {
				date.set(Integer.parseInt(year), 
						Integer.parseInt(month) - 1,
						Integer.parseInt(day),
						Integer.parseInt(hour),
						Integer.parseInt(min),
						Integer.parseInt(sec));

				date.set(Calendar.MILLISECOND, Integer.parseInt(mil));

			} catch (Exception e) {
				//Debug.print(-1, "Can not create date", e);
			}
		}
	}

	public DateUtil(Calendar date) {
		this.date = date;
	}

	public Calendar getCalendar() {
		return date;
	}



	public void convertToUTC() {
		/* Correct for TIMEZONE */
		date.add(Calendar.MILLISECOND, 
				-1 * date.get(Calendar.ZONE_OFFSET));
		/* Correct for Daylight Savings */
		date.add(Calendar.MILLISECOND, 
				-1 * date.get(Calendar.DST_OFFSET));
	}

	public void convertToLocal() {
		/* Correct for TIMEZONE */
		date.add(Calendar.MILLISECOND, 
				date.get(Calendar.ZONE_OFFSET));
		/* Correct for Daylight Savings */
		date.add(Calendar.MILLISECOND, 
				date.get(Calendar.DST_OFFSET));
	}

//	public static String convertDate(Calendar date, boolean toUTC) {
//	Date sdate = new Date(date);
//	if (toUTC) {
//	sdate.convertToUTC();
//	} else {
//	sdate.convertToLocal();
//	}
//	return sdate.toString();
//	}


	public static String convertDate(String strDate, boolean toUTC) {
		DateUtil sdate = new DateUtil(strDate);
		if (toUTC) {
			sdate.convertToUTC();
		} else {
			sdate.convertToLocal();
		}
		return sdate.toString();
	}

	public static String toString(Calendar calendar) {
		DateUtil date = new DateUtil(calendar);
		return date.toString();
	}

	public String toString() {
		GregorianCalendar d = new GregorianCalendar(
				date.get(Calendar.YEAR),
				date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY),
				date.get(Calendar.MINUTE),
				date.get(Calendar.SECOND));
		d.set(Calendar.MILLISECOND, date.get(Calendar.MILLISECOND));

		StringBuffer sb = new StringBuffer();
		try {
			sb.append(Text.zeroFill(d.get(Calendar.YEAR), 4));
			sb.append("-");
			sb.append(Text.zeroFill(d.get(Calendar.MONTH) + 1, 2));
			sb.append("-");
			sb.append(Text.zeroFill(d.get(Calendar.DAY_OF_MONTH), 2));
			sb.append("-");
			sb.append(Text.zeroFill(d.get(Calendar.HOUR_OF_DAY), 2));
			sb.append(".");
			sb.append(Text.zeroFill(d.get(Calendar.MINUTE), 2));
			sb.append(".");
			sb.append(Text.zeroFill(d.get(Calendar.SECOND), 2));
			sb.append(".");
			sb.append(Text.zeroFill(d.get(Calendar.MILLISECOND), 3));
			sb.append("000");
		} catch (Exception e) {
			//Debug.print(-1, "Error creating date string", e);
		}	
		return sb.toString();
	}

	public static String convertDisplayToDB2(String strDatetime) {
		String strDB2Date = "";
		String returnDate = "";
		strDB2Date = strDatetime.substring(0, strDatetime.indexOf(" "));
		strDB2Date = strDB2Date + "-" + strDatetime.substring(strDatetime.indexOf(" ") + 1);
		returnDate = strDB2Date.substring(0, strDB2Date.indexOf(":"));
		returnDate = returnDate + "." + strDB2Date.substring(strDB2Date.indexOf(":") + 1);
		return returnDate + ".00.000000";
	}

	public static String convertDB2ToDisplay(String strDB2Date) {
		if(strDB2Date.length() > 19)
			return strDB2Date.substring(0, 19);
		else
			return strDB2Date;
	}

	public static String getDateTime(String format) {
//		String sTime = "";
		if(format == null || format.trim().length() == 0)
			format = "yyyy-MM-dd hh:mm a";
		Calendar date = new GregorianCalendar();
		java.text.DateFormat df = new java.text.SimpleDateFormat(format);
		return df.format(date.getTime());
	}

	public static String timeFormate(int iHM) {
		if(iHM < 10)
			return "0"+iHM;
		else
			return ""+iHM;
	}

	public static void main(String[] args) {
		Calendar now = Calendar.getInstance();
		System.out.println("Current Local Time: " + 
				DateUtil.toString(now));
		//
		// System.out.println("Current UTC Time  : " + 
		//			Date.convertDate(now, true));
	}
	
	/**
	 * Returns the formatted date string for the date passed using the application locale and date format set
	 * in the date preference page. 
	 * 
	 * @param date
	 * @return formatted date
	 */
	public static String getFormattedDateWithoutTime(Date date){
		SessionUtility utility = new SessionUtility();
		Locale applicationLocale = utility.getApplicationLocale();
		String formattedDate = null;
		String format = utility.getApplicationDateFormat();
		if(format!=null) {
			format = format.substring(0,format.indexOf(" "));			
		}
		if (applicationLocale != null){
			SimpleDateFormat dateFormat = new SimpleDateFormat(format, applicationLocale);
			formattedDate = dateFormat.format(date);			
		}
		return formattedDate;
	}
}		

