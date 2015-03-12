/*****************************************************************************
 * $Id: ConversionUtil.java,v 1.2.1.2, 2013-09-12 08:30:15Z, SDS12.3.3 Checkin User$
 * $Date: 9/12/2013 3:30:15 AM$
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

import java.math.BigDecimal;
import java.util.Random;
import java.util.StringTokenizer;


/**
 * This utility contains some base conversion methods
 * 
 * @author anantharajr
 * @version $Revision: 6$
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

		return (long) ( amount * 100);
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
		long longVal = (long) (doubleVal * 100);
		return ((Long) longVal).toString();
	}

	/**
	 * This method is used to convert a dollar amount to cents given String as
	 * input and returns a long value
	 * @param amount
	 * @return
	 */
	public static long dollarToCentsRtnLong(String amount) {

		double doubleVal = Double.parseDouble(amount);
		long longVal = (long) (doubleVal * 100);
		return longVal;
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
	public static double centsToDollar(long amount) {

		String amountStr = ((Long) amount).toString();
		if (amountStr.length() > 1) {
			String firstStr = amountStr.substring(0, amountStr.length() - 2);
			String secondStr = amountStr.substring(amountStr.length() - 2, amountStr.length());
			String finalStr = firstStr + "." + secondStr;
			//double doubleValue = Double.parseDouble(finalStr);
			return Double.parseDouble(finalStr);
		} else {
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
	
	/**
	 * This method will give the formatted decimal values
	 * @param amount
	 * @return
	 */
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
	 * will check whether the fracations are for two place
	 * @param amount
	 * @return
	 */
	public static boolean isFractionsOfCentsNormal(String amount)
	{
		
		StringTokenizer stringToken = new StringTokenizer(amount, ".");
		int tokenCount = stringToken.countTokens();
		if(tokenCount==1)
		{
			return true;
		}
		else if(tokenCount==2)
		{
			
			stringToken.nextToken();
			String decimalPart = stringToken.nextToken();
			if(decimalPart.length()>2)
			{
				return false;
			}
			else
			{
				return true;
			}
		
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * round upper half of decimal
	 * @param val
	 * @return
	 */
	public static double roundHalfUp(double val) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		val = bd.doubleValue( );
		return val;
	}
	
	/**
	 * Left Pads the string with given character till specified length 
	 * @param str
	 * @param padLength
	 * @param padChar
	 * @return
	 */
	public static String leftPad(String str, int padLength, char padChar) {
        StringBuffer strBuf = new StringBuffer(str);    
        int strLength = str.length();
        if (strLength < padLength){
              for (int i=0; i<padLength-strLength; i++){
                    strBuf.insert(0, padChar);
              }
        }
        return strBuf.toString();
  }
	
	/**
	 * Convert the byte array to char array for the length specified.
	 * @param byteArr
	 * @param arrLen
	 * @return
	 * @throws NegativeArraySizeException
	 */
	public static char[] convertByteArrToCharArr(byte[] byteArr, int arrLen) throws NegativeArraySizeException {
		char[] charArr;
		if (byteArr == null){
			// input arg is null
			// return empty char array
			charArr = new char[]{};
			
		} else {
			charArr = new char[arrLen];
			for (int i=arrLen-1, j=0; i>=0 ; i--, j++){
				charArr[j] = (char)(byteArr[i] & 0xFF);
			}
		}

		return charArr;
	}

	/**
	 * Method to generate a 4 digit random number for jackpot sequence
	 * @return
	 * @author vsubha
	 */
	public static String getRandomSequenceNumber() {
		Long randomNumber = 0L;
		Random rnd = new Random();
		do {
			randomNumber = new Long(rnd.nextInt(9999));
		} while (randomNumber < 1000);
		return randomNumber.toString();
	}
	/**
	 * Method to get the substring of length n from the end of a string.
	 * @param inputString - String from which substring to be extracted.
	 * @param subStringLength- int Desired length of the substring.
	 * @return lastCharacters- String
	 * @author vsubha
	*/
	public String getLastnCharacters(String inputString, int subStringLength) {
		int length = inputString.length();
		if (length <= subStringLength) {
			return inputString;
		}
		int startIndex = length - subStringLength;
		return inputString.substring(startIndex);
	}
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String args[]) {

		/*log.debug(dollarToCents("12.3"));
		log.debug(dollarToCents(".32"));
		log.debug(dollarToCents(6.1));
		log.debug(centsToDollar("123456"));
		log.debug(centsToDollar(33));
		log.debug(maskAmount(centsToDollar("123456789")));
		log.debug(maskAmount(23.43));
		log.debug(twoPlaceDecimalOf(23.124));*/
		//log.info(ConversionUtil.roundHalfUp(363377.467));
	
		//log.info(ConversionUtil.twoPlaceDecimalOf(1.00));
		//log.info(ConversionUtil.roundUp(12.33));
		//log.info(ConversionUtil.isFractionsOfCentsNormal("22333333.3"));		
	}

}
