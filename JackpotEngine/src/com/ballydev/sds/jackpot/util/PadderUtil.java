/*****************************************************************************
 * $Id: PadderUtil.java,v 1.2, 2008-06-03 11:33:41Z, Ambereen Drewitt$
 * $Date: 6/3/2008 6:33:41 AM$
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


/**
 * Class to Right pad or Left pad a string with the user mentioned no of zeros
 * @author anantharajr
 * @version $Revision: 3$
 */
public class PadderUtil {
	
	/**
	 *	Right Pads the string with given character till specified length 
	 * 
	 * @param String - string to pad 
	 * @param String - padLength, length upto which char is to be padded
	 * @param char - pad Character
	 * @return paddedString  
	 */
	public static String rightPad(String str, int padLength, char padChar) {
		StringBuffer sb = new StringBuffer(str);	
		int strLength = str.length();
		if (strLength < padLength){
			for (int i=0; i<padLength-strLength; i++){
				sb.append(padChar);
			}
		}
		return sb.toString();
	}
	
	/**
	 *	Left Pads the string with given character till specified length 
	 * 
	 * @param String - string to pad 
	 * @param String - padLength, length upto which char is to be padded
	 * @param char - pad Character
	 * @return paddedString  
	 */
	public static String leftPad(String str, int padLength, char padChar) {
		StringBuffer sb = new StringBuffer(str);	
		int strLength = str.length();
		if (strLength < padLength){
			for (int i=0; i<padLength-strLength; i++){
				sb.insert(0, padChar);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method to left pad the value with zeros
	 * @param str
	 * @param length
	 * @return String
	 */
	public static String lPad(String str, int length) {
		 str = (str == null)? "" : str;
		if(str.length()<1 || str.length() >= length) {
			System.out.println("str is null");
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		for(int i = str.length(); i < length; ++i) {
			sb.append("0");
		}
		sb.append(str);
		return sb.toString();
	}
	
		
}
