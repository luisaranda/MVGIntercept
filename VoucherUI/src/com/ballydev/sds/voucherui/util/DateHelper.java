/*****************************************************************************
 * $Id: DateHelper.java,v 1.3, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:46:20 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;


/**
 * Date Helper
 *
 * @author $author$
 * @version $Revision: 4$
 */
public class DateHelper
{

	private static final XMLGregorianCalendarImpl gregorianCalendarImpl = new XMLGregorianCalendarImpl();

	private static final  SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	/** Logger  */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	/** Version  */
	public static final String VERSION = "$Revision: 4$";

	/**
	 * Creates a new DateHelper object.
	 */
	private DateHelper()
	{
		super();
	}

	/**
	 * Returns current Time in milliseconds 
	 * @return
	 */
	public static long currentUTCMillis()
	{
		long _currentTime = System.currentTimeMillis();

		return _currentTime - TimeZone.getDefault().getOffset(_currentTime);
	}




	public static Date getTimeStamp(String dateString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			return dateFormat.parse(dateString);
		}
		catch (ParseException e)
		{
			e.printStackTrace();

			return null;
		}
	}

	public static String getTimeStampString(String dateInMMDDYYYY){
		String targetFormat=null;
		try {
			SimpleDateFormat inputFormt = new SimpleDateFormat("MM/dd/yyyy");
			targetFormat = new SimpleDateFormat(IVoucherConstants.DATE_FORMAT).format(inputFormt.parse(dateInMMDDYYYY));
		} catch (ParseException e) {
			log.error(e);
			e.printStackTrace();
		}
		return targetFormat;
	}
	/**
	 * Gives the formated Date as String
	 * 
	 * @param inputDate
	 *            Date needs to be formated
	 * @param inputDateFormat
	 *            Input Date format, format of the parameter <i>inputDate</i>
	 * @param outputformat
	 *            target format
	 * @return formated String
	 */
	public static String getFormatedDate(final String inputDate,final String inputDateFormat, final String outputformat) {
		Date dateValue;
		String targetDate = "";
		try {
			dateValue = new SimpleDateFormat(inputDateFormat).parse(inputDate);
			SimpleDateFormat dtFormat = new SimpleDateFormat(outputformat);
			targetDate = dtFormat.format(dateValue);
		} catch (ParseException e) {
			log.error(e);
			e.printStackTrace();
		}
		return targetDate;
	}


	/**
	 * Returnd xmlGregorian Calendar
	 * 
	 * @param date
	 *            input Date
	 * @return xmlGregorian Calendar
	 */
	@SuppressWarnings("static-access")
	public static XMLGregorianCalendar getXMLGregorianCalendar(Date date){
		String targetFormat = timestampFormat.format(date);
		return gregorianCalendarImpl.parse(targetFormat);
	}
}
