/*****************************************************************************
 * $Id: ConversionUtil.java,v 1.7, 2009-11-18 14:54:35Z, Ambereen Drewitt$
 * $Date: 11/18/2009 8:54:35 AM$
 * $Log$
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

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.ballydev.sds.framework.util.CurrencyUtil;

/**
 * This utility contains some base conversion methods
 * 
 * @author anantharajr
 * @version $Revision: 8$
 */
public class ConversionUtil {
	/**
	 * This method is used to convert a dollar amount to cents given double as
	 * input
	 * 
	 * @param amount
	 * @return
	 */
	public static long dollarToCents(double amount) {

		return (long) Math.round(( amount * 100));
	}

	/**
	 * This method is used to convert a dollar amount to cents given String as
	 * input
	 * 
	 * @param amount
	 * @return
	 */
	public static String dollarToCents(String amount) {

		double doubleVal = Double.parseDouble(amount);
		long longVal = (long) Math.round((doubleVal * 100));
		return ((Long) longVal).toString();
	}

	/**
	 * This method is to convert a cent to dollar amount given String as input
	 * 
	 * @param amount
	 * @return
	 */
	public static String centsToDollar(String amount) {
		if (amount.length() < 2) {
			amount = PadderUtil.leftPad(amount, 3, '0');
		}
		String firstStr = amount.substring(0, amount.length() - 2);
		String secondStr = amount.substring(amount.length() - 2, amount.length());
		//String finalStr = firstStr + "." + secondStr;
		return firstStr + "." + secondStr;

	}

	/**
	 * This method is to convert a cent to dollar amount given double as input
	 * 
	 * @param amount
	 * @return
	 */
	public static double centsToDollar(double amount) {

/*		String amountStr = ((Double) amount).toString();
		if (amountStr.length() > 1) {
			String firstStr = amountStr.substring(0, amountStr.length() - 2);
			String secondStr = amountStr.substring(amountStr.length() - 2, amountStr.length());
			String finalStr = firstStr + "." + secondStr;
			//double doubleValue = Double.parseDouble(finalStr);
			return Double.parseDouble(finalStr);
		} else {
			return 0;
		}
*/
		return amount;
	}
	
	/**
	 * This method is to convert a cent to dollar amount given double as input
	 * 
	 * @param amount
	 * @return
	 */	
	public static double centsToDollar(long amount) {

		String amountStr = ((Long) amount).toString();
		if (amountStr.length() > 1) {
			String firstStr = amountStr.substring(0, amountStr.length() - 2);
			String secondStr = amountStr.substring(amountStr.length() - 2,
					amountStr.length());
			String finalStr = firstStr + "." + secondStr;
			// double doubleValue = Double.parseDouble(finalStr);
			return Double.parseDouble(finalStr);
		} else if (amountStr.length() == 1) {

			return (amount / 100d);

		}

		else {
			return 0;
		}

	}

	/**
	 * This method will mask the amount and display echo character. It works
	 * only for two place decimal values. Accepts amount as String
	 * 
	 * @param amount
	 * @return
	 */
	public static String maskAmount(String amount) {
		String maskAmtStr = PadderUtil.leftPad("*", amount.length() - 3, '*')
				+ PadderUtil.rightPad(".", 3, '*');
		return maskAmtStr;
	}

	/**
	 * This method will mask the amount and display echo character. It works
	 * only for two place decimal values. Accepts amount as double
	 * 
	 * @param amount
	 * @return
	 */
	public static String maskAmount(double amount) {
		String firstStr = ((Double) amount).toString();
		String maskAmountStr = PadderUtil.leftPad("*", firstStr.length() - 3, '*')
				+ PadderUtil.rightPad(".", 3, '*');
		return maskAmountStr;
	}
	
	public static String twoPlaceDecimalOf(double amount)
	{
		
		String stringArray[] = new String[3];
		String doubleAmtStr = ((Double) amount).toString();
		StringTokenizer stringToken = new StringTokenizer(doubleAmtStr, ".");
		int len = stringToken.countTokens();
		stringArray[0] = stringToken.nextToken();
		if(len==2)
		{
			stringArray[1] = stringToken.nextToken();
			if (stringArray[1].length() < 2) {
				stringArray[1] = PadderUtil.rightPad(stringArray[1], 2, '0');
			}
			stringArray[0] = stringArray[0] +"."+ stringArray[1];
		}
		return stringArray[0];
		
	}	
	
	/**
	 * checks for a proper fraction for cents
	 * 
	 * @param amount
	 * RETURN TRUE - IF PASSED
	 * RETURN FALSE - IF FAILED
	 */
	public static boolean isFractionsOfCentsNormal(String amount) 
	{
		if(amount.contains(".")){
			StringTokenizer stringToken = new StringTokenizer(amount, ".");
			int tokenCount = stringToken.countTokens();
			if (tokenCount == 1) {
				String firstToken = stringToken.nextToken();
				if(firstToken.length()>2 && Long.parseLong(firstToken.substring(2))!=0){				
					return false;
				}
				else{				
						return true;
					}
			} else if (tokenCount == 2) {
				stringToken.nextToken();
				String decimalPart = stringToken.nextToken();
				if(decimalPart.length()>2 && !decimalPart.substring(2,3).equalsIgnoreCase("0") && Long.parseLong(decimalPart.substring(2))!=0){
					return false;
				}else if(decimalPart.length()>2 && Long.parseLong(decimalPart.substring(2))==0){				
					return true;
				}
				else if (decimalPart.length() > 2) {				
					return false;
				} else {				
					return true;
				}		
			}
			else {
				return false;
			}
		}else{
			return true;
		}		
	}
	
	public static double roundHalfUp(double val)
	{
	BigDecimal bd = new BigDecimal(val);
	bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
	val = bd.doubleValue( );
	return val;
	}
	
	
	
	public static double roundUp(double val, int decPlaces)
	{
	BigDecimal bd = new BigDecimal(val);
	bd = bd.setScale(decPlaces, BigDecimal.ROUND_UP);
	val = bd.doubleValue( );
	return val;
	}

	public static double roundUp(double val)
	{
	BigDecimal bd = new BigDecimal(val);
	bd = bd.setScale(0, BigDecimal.ROUND_UP);
	val = bd.doubleValue( );
	return val;
	}

	/**
	 * This method returns the currency format 
	 * for the value passed according to the language selected 
	 * (an exact copy of Framework's method)
	 * @returns String currency format
	 */
	
	public static String getCurrencyFormat(double value){
		String retVal = CurrencyUtil.getCurrencyFormat(value);
		System.out.println("retval:" +retVal);
		return retVal;
	}
	
	public static void main(String args[]) {

		/*log.debug(dollarToCents("12.3"));
		log.debug(dollarToCents(".32"));
		log.debug(dollarToCents(6.1));
		log.debug(centsToDollar("123456"));
		log.debug(centsToDollar(33));
		log.debug(maskAmount(centsToDollar("123456789")));
		log.debug(maskAmount(23.43));
		log.debug(twoPlaceDecimalOf(23.124));*/
		System.out.println("Org no: 363377.468");
		System.out.println(ConversionUtil.roundHalfUp(363377.468));
		System.out.println(ConversionUtil.roundUp(363377.489));
		System.out.println(ConversionUtil.roundUp(363377.46888888, 6));
	}

}
