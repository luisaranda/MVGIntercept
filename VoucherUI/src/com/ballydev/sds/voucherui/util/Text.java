/*****************************************************************************
 * $Id: Text.java,v 1.13, 2011-02-09 07:29:56Z, Verma, Nitin Kumar$
 * $Date: 2/9/2011 1:29:56 AM$
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
package com.ballydev.sds.voucherui.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * Provides utility methods on text related function
 * @author retrived from 10.x and updated by abbas
 * @version $Revision 1.1$
 */
public class Text {
	
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	private static final long TRILLIONS = 100000000000000L;
	
	private static final long BILLIONS = 100000000000L;
	/** Number of pennies in a million dollars */
	private static final long MILLIONS = 100000000L;

	/** Number of pennies in a thousand dollars */
	private static final int THOUSANDS = 100000;

	/** Number of ones in a hundred */
	private static final int HUNDREDS = 100;

	/** Number of pennies in a one dollar bill ME! */
	private static final int ONES = 100;
	
	private static final int euro = 213;

	private static String asciiValue = null;

	private static int convertToAscii;

	private static String currencyText = null;

	private static int asciiCode = -1;

	/**
	 * Pad a text field with blanks 
	 * @param value text to pad
	 * @param length length of text field to pad
	 * @return padded text field
	 * @throws TextException if there is an error in the length
	 */
	public static String blankFill(String value, int length) throws TextException {
		return fill(value, length, ' ', false);
	}

	/**
	 * Given an amount in pennies convert it to dollar string 
	 * @param amount amount to convert
	 * @return dollar string
	 */
	public static String convertAmount(int amount) {
		String dollarsText = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_DOLLARS);
		StringBuffer sb    = new StringBuffer();
		int tmp            = 0;

		if ((amount >= 100) & (amount < 200)) {
			dollarsText = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_DOLLAR);
		}

		if (amount >= ONES) {
			/* Determine how many millions */
			tmp = (int) (amount / MILLIONS);
			if (tmp > 0 ) {
				sb.append(convertThreeDigits(tmp));
				sb.append(" Million ");
				amount -= (tmp * MILLIONS);
			}
			/* Determine how many 100's of thousands */
			tmp = amount / THOUSANDS;

			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp));
				sb.append(" Thousand ");
				amount -= (tmp * THOUSANDS);
			}
			tmp = amount / ONES;

			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp));
				amount -= (tmp * ONES);
			}
		} else {
			sb.append("Zero");
		}

		sb.append(" " + dollarsText + " and ");

		if (amount == 0) {
			sb.append("No "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CENTS));
		} else if ( amount == 1) {
			sb.append("One "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CENT));
		} else {
			sb.append(convertTwoDigits(amount) + " "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CENTS));
		}

		return sb.toString();
	}

	 /**
	 * Given an amount in pennies convert it to dollar string 
	 * @param amount amount to convert
	 * @return dollar string
	 */
	public static String convertAmount(long amount) {
		
		String dollarsText = getCurrencyText(amount);
		StringBuffer sb    = new StringBuffer();
		int tmp            = 0;

		if (amount >= ONES) {	
			
			/* Determine how many trillions */
			tmp = (int)(amount / TRILLIONS);			
			if (tmp > 0 ) {
				sb.append(convertThreeDigits(tmp));
				sb.append(" Trillion ");
				amount -= (tmp * TRILLIONS);
			}

			/* Determine how many billions */
			tmp = (int)(amount / BILLIONS);			
			if (tmp > 0 ) {
				sb.append(convertThreeDigits(tmp));
				sb.append(" Billion ");
				amount -= (tmp * BILLIONS);
			}

			/* Determine how many millions */
			tmp = (int)(amount / MILLIONS);			
			if (tmp > 0 ) {
				sb.append(convertThreeDigits(tmp));
				sb.append(" Million ");
				amount -= (tmp * MILLIONS);
			}

			/* Determine how many 100's of thousands */
			tmp = (int)(amount / THOUSANDS);

			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp));
				sb.append(" Thousand ");
				amount -= (tmp * THOUSANDS);
			}

			tmp = (int)(amount / ONES);

			if (tmp > 0) {
				sb.append(convertThreeDigits(tmp));
				amount -= (tmp * ONES);
			}
		} else {
			sb.append("Zero");
		}

		sb.append(" " + dollarsText + " ");

		if (amount == 0) {
			String patern = CurrencyUtil.getCurrencyPatern();
			String decimalSeperator = CurrencyUtil.getDecmSeperator();
			if(patern.indexOf(decimalSeperator)!=-1){
				sb.append("and No "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CENTS));
			}
		} else if ( amount == 1) {
			sb.append("and One "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CENT));
		} else {
			sb.append("and "+convertTwoDigits(amount) + " "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CENTS));
		}
		return sb.toString();
	}

	/**
	 * HardCoding the value for now. 
	 * TOD0:Currency Description needs to configurable through the UI as we already parameters for it.
	 * @param amount
	 * @return
	 */
	private static String getCurrencyText(long amount) {
		
		String currencySymbol = CurrencyUtil.getCurrencySymbol();			
		if( currencySymbol != null && currencySymbol.trim().equals("\u20AC") ){
			currencyText = "Euros";
		}
		else {
			currencyText = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_DOLLARS);
		}		
		if (currencySymbol != null && currencySymbol.equals("\u20AC") && ((amount >= 100) & (amount < 200))  ) {
			currencyText = "Euro";
		}
		else if((amount >= 100) & (amount < 200)){
		currencyText = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_DOLLAR);
		}
				
		return currencyText;
	}

	/**
	 * Given an amount convert the amount to a numeric dollar amount
	 * @param amount value to convert
	 * @return dollar amount with leading $
	 * @throws TextException if there is an error with the amount
	 */
	public static String convertDollar(int amount) throws TextException {
		StringBuffer sb = new StringBuffer();
		//sb.append(CurrencyUtil.getCurrencySymbol());

		int dollars = amount / 100;
		int cents   = amount % 100;
		sb.append(dollars);
		sb.append(".");
		sb.append(zeroFill(cents, 2));
		String strAmt = CurrencyUtil.getCurrencyFormat(sb.toString());		
		return CurrencyUtil.getCurrencySymbol() + strAmt;
	}

	public static String convertDollar(long amount) throws TextException {
		StringBuffer sb = new StringBuffer();
		//sb.append(CurrencyUtil.getCurrencySymbol());

		long dollars = amount / 100;
		long cents   = amount % 100;
		sb.append(dollars);
		sb.append(".");
		sb.append(zeroFill((int)cents, 2));
		String strAmt = CurrencyUtil.getCurrencyFormat(sb.toString());		
		String currencySymbol = CurrencyUtil.getCurrencySymbol();
		
		return currencySymbol + strAmt;
	}

	/**
	 * this method is used to used convert dollar to be printed on Ithaca printer, because Ithaca takes different ascii values
	 * to print different currency symbols. The ascii value is being read from voucher.properties file.
	 * In case of  any exception while reading or converting the asciicode it will fall back to the Currency symbol obtained from 
	 * the server 
	 */
	public static String convertDollarForTicket(long amount) throws TextException {
		StringBuffer sb = new StringBuffer();
		//sb.append(CurrencyUtil.getCurrencySymbol());

		long dollars = amount / 100;
		long cents   = amount % 100;
		sb.append(dollars);
		sb.append(".");
		sb.append(zeroFill((int)cents, 2));
		String strAmt = CurrencyUtil.getCurrencyFormat(sb.toString());		
		String currencySymbol = CurrencyUtil.getCurrencySymbol();
		
		//Get Ascii code from properties if not initialized.
		if(asciiValue == null)
			asciiValue = getAsciiValue(amount);
		
		if(asciiValue != null){
			//if asciiCode is initialized properly then convert
			if(convertToAscii == 1 && asciiCode != -1 ) {
				asciiValue = null;
				return (char)(asciiCode) + strAmt;
			}
			//if asciiCode is not initialized properly then default to currencySymbol
			else if(convertToAscii == 1 && asciiCode == -1 ) {
				asciiValue = null;
				return currencySymbol + strAmt;
			}
			//Use As is
			else {
				return asciiValue + strAmt;
			}
		}
		else
			return currencySymbol + strAmt;
	}

	/**
	 * Method to retrieve the asciiCode of a CurrencySymbol from the Voucher Properties file
	 * @return
	 */
	private static String getAsciiValue(long amount) {
		
		Properties voucherProps = null;
		String asciiValue = "-1";
		FileInputStream fileInputStream = null;
		try {		
			if(voucherProps == null){
				voucherProps = new Properties();
				fileInputStream = new FileInputStream(IVoucherConstants.VOUCHER_PROPERTIES);
				voucherProps.load(fileInputStream);
			}
			//Get the Currency text for a singular value
			String currencyText = getCurrencyText(100);
			convertToAscii = Integer.parseInt(voucherProps.get(IVoucherConstants.VCH_CONVERT_TO_ASCII).toString().trim());		
			asciiValue = (voucherProps.get(currencyText.toUpperCase()).toString().trim());
						
			try {
				asciiCode = Integer.parseInt(asciiValue);
			}
			catch (NumberFormatException e) {
				log.error(e.getMessage());
				asciiCode = -1;
			}
			log.debug("ASCII CODE for "+currencyText+" read from voucher propertes is "+asciiValue);
		}
		catch (Exception e) {			
			log.error(e.getMessage(), e);
			asciiValue = null;
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException ioe) {
					// eat up after logging
					log.error(ioe.getMessage(), ioe);
				}
			}
		}
		return asciiValue;
	}

	/**
	 * Given a barcode object, put the hash marks into the barcode
	 * @param raw a barcode object that was probably used in a container
	 * @return barcode with hash marks
	 */
	public static String expandBarcode(Object raw) {
		return expandBarcode((String)raw);
	}

	/**
	 * Given a barcode string, put the hash marks into the barcode
	 * @param raw a string with a raw barcode (18-digits)
	 * @return barcode with hash marks 
	 */
	public static String expandBarcode(String raw) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(raw.substring(0, 4));
			sb.append("-");
			sb.append(raw.substring(4, 9));
			sb.append("-");
			sb.append(raw.substring(9, 14));
			sb.append("-");
			sb.append(raw.substring(14));
		} catch (IndexOutOfBoundsException outOfBounds) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String maskBarcode(String raw) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(raw.substring(0, 4));
			sb.append("-");
			sb.append(raw.substring(4, 9));
			sb.append("-");
			sb.append(raw.substring(9, 14));
			sb.append("-XXXX");
		} catch (IndexOutOfBoundsException outOfBounds) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * Fill a text field with the given value
	 *
	 * @param value text field to be filled
	 * @param length length of the new text field
	 * @param fill what to fill the text field with
	 * @param start should we pad at the beginning or the end
	 *
	 * @return padded field
	 *
	 * @throws TextException if there is an error with the length
	 */
	public static String fill(
			String value, int length, char fill, boolean start)
	throws TextException {
		//Debug.print("value = " + value);
		//Debug.print("length = " + length);
		//Debug.print("fill = >" + fill + "<");
		//Debug.print("start = " + start);
		if (value.length() > length) {
			throw new TextException("Invalid length");
		}

		StringBuffer sb = new StringBuffer();
		if (start) {
			sb.append(value);
		}
		for (int i = length - value.length(); i > 0; i--) {
			sb.append(fill);
		}
		if (!start) {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String formatDouble(double d, int precision) {
		String s = Double.toString(d);

		int length = s.length();
		int index  = s.lastIndexOf('.');

		//Debug.print("Double(" + precision + "): " + d + " " +
		//	length + " " + index); 
		if (index > 0) {
			if (length > (index + 3)) {
				s = s.substring(0, index + 3);
			}
		}

		length = s.length();
		index  = s.lastIndexOf('.');

		int numberOfTrailingDigits = length - index;

		for (int i = 0; i <= (precision - numberOfTrailingDigits); i++) {
			s += "0";
		}
		return s;
	}

	public static String formatDouble(Double d, int precision) {
		return formatDouble(d.doubleValue(), precision);
	}

	public static String formatLong(long lValue, int precision) {
		String strValue = "";
		int len         = 0;
		try {
			strValue     = String.valueOf(lValue);
			len          = strValue.length();

			if (len > precision) {
				strValue = strValue.substring(0, len - precision) + "." +
				strValue.substring(len - 2);
			} else if (len == precision) {
				strValue = "0." + strValue;
			} else if (len == 1) {
				strValue = "0.0" + strValue;
			}
		} catch (Exception e) {
			strValue = "***************";
		}
		return strValue;
	}

	public static void main(String[] args) {
		try {
			//System.out.println(Text.fill(Text.formatDouble(0.25, 2), 12, ' ' , false));
			//System.out.println(Text.formatDouble(25.002, 2));
			//System.out.println(Text.formatDouble(25.2, 2));
			//System.out.println(Text.formatDouble(25, 2));
			/*System.out.println(
					"After formatting :: " + CopyOfText.formatLong(12345, 2));
			System.out.println("After formatting :: " + CopyOfText.formatLong(12, 2));
			System.out.println("After formatting :: " + CopyOfText.formatLong(1, 2));
			System.out.println("After formatting :: " + CopyOfText.formatLong(0, 2));
		*/	
			//Text text = new Text();
			//999,999,999,999.99
			System.out.println("Value: "+convertAmount(77777777l));
		

			//System.out.println(Text.blankFill("123", 13));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String zeroFill(int value, int length)
	throws TextException, NumberFormatException {
		String v = Integer.toString(value);

		if (v.length() > length) {
			throw new TextException("Invalid length");
		}

		StringBuffer sb = new StringBuffer();

		for (int i = length - v.length(); i > 0; i--) {
			sb.append("0");
		}

		sb.append(v);

		return sb.toString();
	}

	public static String zeroFill(String value, int length)
	throws TextException {
		return fill(value, length, '0', false);
	}

	/**
	 * convert one digit amount to a text representation.
	 *
	 * @param amount digit to convert
	 *
	 * @return string representation of the digit
	 */
	private static String convertOneDigit(int amount) {
		String text = null;		
		switch (amount) {
		case 0:
			break;

		case 1:
			text = "One";
			break;

		case 2:
			text = "Two";
			break;

		case 3:
			text = "Three";
			break;

		case 4:
			text = "Four";
			break;

		case 5:
			text = "Five";
			break;

		case 6:
			text = "Six";
			break;

		case 7:
			text = "Seven";
			break;

		case 8:
			text = "Eight";
			break;

		case 9:
			text = "Nine";
			break;
		}
		return text;
	}

	/**
	 * Convert three digits to a text string
	 * @param amount value to convert
	 * @return text of the converted digits
	 */
	private static String convertThreeDigits(int amount) {
		StringBuffer sb = new StringBuffer();		
		int tmp         = amount / HUNDREDS;
		
		if (tmp > 0) {		
			sb.append(convertOneDigit(tmp) + " Hundred");
			amount -= (tmp * HUNDREDS);
			if (amount > 0) {
				sb.append(" ");
			}
		}
		if (amount > 0) {
			sb.append(convertTwoDigits(amount));
		}
		return sb.toString();
	}

	/**
	 * Convert two digits to text representation
	 * @param amount value to convert 
	 * @return text of the converted date
	 */
	private static String convertTwoDigits(int amount) {
		StringBuffer sb = new StringBuffer();

		if (amount < 10) {
			return convertOneDigit(amount);
		} else if (amount == 10) {
			return "Ten";
		} else if (amount == 11) {
			return "Eleven";
		} else if (amount == 12) {
			return "Twelve";
		} else if (amount == 13) {
			return "Thirteen";
		} else if (amount == 14) {
			return "Fourteen";
		} else if (amount == 15) {
			return "Fifteen";
		} else if (amount == 16) {
			return "Sixteen";
		} else if (amount == 17) {
			return "Seventeen";
		} else if (amount == 18) {
			return "Eighteen";
		} else if (amount == 19) {
			return "Nineteen";
		} else if ((amount >= 20) && (amount < 30)) {
			sb.append("Twenty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 30) && (amount < 40)) {
			sb.append("Thirty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 40) && (amount < 50)) {
			sb.append("Fourty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 50) && (amount < 60)) {
			sb.append("Fifty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 60) && (amount < 70)) {
			sb.append("Sixty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 70) && (amount < 80)) {
			sb.append("Seventy");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 80) && (amount < 90)) {
			sb.append("Eighty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 90) && (amount < 100)) {
			sb.append("Ninety");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Convert two digits to text representation
	 * @param amount value to convert 
	 * @return text of the converted date
	 */
	private static String convertTwoDigits(long amount) {
		StringBuffer sb = new StringBuffer();

		if (amount < 10) {
			return convertOneDigit((int)amount);
		} else if (amount == 10) {
			return "Ten";
		} else if (amount == 11) {
			return "Eleven";
		} else if (amount == 12) {
			return "Twelve";
		} else if (amount == 13) {
			return "Thirteen";
		} else if (amount == 14) {
			return "Fourteen";
		} else if (amount == 15) {
			return "Fifteen";
		} else if (amount == 16) {
			return "Sixteen";
		} else if (amount == 17) {
			return "Seventeen";
		} else if (amount == 18) {
			return "Eighteen";
		} else if (amount == 19) {
			return "Nineteen";
		} else if ((amount >= 20) && (amount < 30)) {
			sb.append("Twenty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 30) && (amount < 40)) {
			sb.append("Thirty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 40) && (amount < 50)) {
			sb.append("Fourty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 50) && (amount < 60)) {
			sb.append("Fifty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 60) && (amount < 70)) {
			sb.append("Sixty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 70) && (amount < 80)) {
			sb.append("Seventy");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 80) && (amount < 90)) {
			sb.append("Eighty");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		} else if ((amount >= 90) && (amount < 100)) {
			sb.append("Ninety");

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int)(amount % 10)));
			}
		}
		return sb.toString();
	}

	public static String formatDate(long timeInMills) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(timeInMills);	
		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.MONTH) + 1);
		sb.append("/");
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append("/");
		sb.append(cal.get(Calendar.YEAR));
		return sb.toString();
	}

	public static String formatHour(long timeInMills) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(timeInMills);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(zeroFill(cal.get(Calendar.HOUR_OF_DAY), 2));
		} catch (Exception e1) {
			sb.append("00");
		}
		sb.append(":");
		try {
			sb.append(Text.zeroFill(cal.get(Calendar.MINUTE), 2));
		}
		catch (Exception e2) {
			sb.append("00");
		}
		sb.append(":");
		try {
			sb.append(Text.zeroFill(cal.get(Calendar.SECOND), 2));
		}
		catch (Exception e3) {
			sb.append("00");
		}
		return sb.toString();
	}

	public static String formatTimestamp(long timeInMills) {
		Calendar currentTime = GregorianCalendar.getInstance();
		currentTime.setTimeInMillis(timeInMills);			
		StringBuffer sb = new StringBuffer();
		sb.append(currentTime.get(Calendar.MONTH) + 1);
		sb.append("/");
		sb.append(currentTime.get(Calendar.DAY_OF_MONTH));
		sb.append("/");
		sb.append(currentTime.get(Calendar.YEAR));
		sb.append(" ");
		try {
			sb.append(zeroFill(currentTime.get(Calendar.HOUR_OF_DAY), 2));
		} catch (Exception e1) {
			sb.append("00");
		}
		sb.append(":");
		try {
			sb.append(Text.zeroFill(currentTime.get(Calendar.MINUTE), 2));
		}
		catch (Exception e2) {
			sb.append("00");
		}
		sb.append(":");
		try {
			sb.append(Text.zeroFill(currentTime.get(Calendar.SECOND), 2)+" ");
		}
		catch (Exception e2) {
			sb.append("00");
		}
		return sb.toString();
	}
	
	public static String formatTimestampWithSec(long timeInMills) {
		Calendar currentTime = GregorianCalendar.getInstance();
		currentTime.setTimeInMillis(timeInMills);			
		StringBuffer sb = new StringBuffer();
		sb.append(currentTime.get(Calendar.MONTH) + 1);
		sb.append("/");
		sb.append(currentTime.get(Calendar.DAY_OF_MONTH));
		sb.append("/");
		sb.append(currentTime.get(Calendar.YEAR));
		sb.append(" ");
		try {
			sb.append(zeroFill(currentTime.get(Calendar.HOUR_OF_DAY), 2));
		} catch (Exception e1) {
			sb.append("00");
		}
		sb.append(":");
		try {
			sb.append(Text.zeroFill(currentTime.get(Calendar.MINUTE), 2));
		}
		catch (Exception e2) {
			sb.append("00");
		}
		sb.append(":");
		try {
			sb.append(Text.zeroFill(currentTime.get(Calendar.SECOND), 2)+" ");
		}
		catch (Exception e2) {
			sb.append("00");
		}
		return sb.toString();
	}	
}
