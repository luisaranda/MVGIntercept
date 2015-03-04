/*****************************************************************************
 * $Id: ConversionUtil.java,v 1.13.1.0.2.2, 2013-09-12 08:30:15Z, SDS12.3.3 Checkin User$
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
package com.ballydev.sds.jackpotui.util;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;

/**
 * This utility contains some base conversion methods
 * 
 * @author anantharajr
 * @version $Revision: 18$
 */
public class ConversionUtil {

	/**
	 * This method is used to convert a dollar amount to cents given double as
	 * input
	 * 
	 * @param amount
	 * @return
	 */
	/*
	 * public static long dollarToCents(double amount) { long rtnLongVal = 0;
	 * try { String amt = new Double(amount).toString(); int index =
	 * amt.indexOf("."); if(index == -1) { amt = new Double(amount).toString() +
	 * "00"; } else if(amt.length() - index - 1 == 2){ amt = amt.replace(".",
	 * ""); } else if(amt.length() - index - 1 == 1){ amt = amt.replace(".", "")
	 * + "0"; }
	 * 
	 * rtnLongVal = Long.parseLong(amt); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return rtnLongVal; }
	 */

	/**
	 * This method is used to convert a dollar amount to cents given String as
	 * input
	 * 
	 * @param amount
	 * @return
	 */
	public static String dollarToCents(String amount) {

		double doubleVal = Double.parseDouble(amount);
		long longVal = (long) (Math.round(doubleVal * 100));
		return ((Long) longVal).toString();
	}

	/**
	 * This method is used to convert a dollar amount to cents given String as
	 * input and returns a long value
	 * 
	 * @param amount
	 * @return
	 */
	public static long dollarToCentsRtnLong(String amount) {

		double doubleVal = Double.parseDouble(amount);
		long longVal = (long) (Math.round(doubleVal * 100));
		return ((Long) longVal);
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
		// String finalStr = firstStr + "." + secondStr;
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
			// double doubleValue = Double.parseDouble(finalStr);

			return Double.parseDouble(finalStr);
		} else if (amountStr.length() == 1) {

			return (amount / 100d);
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
	 * Method that will return a string of stars for the jackpot amount
	 * 
	 * @return String
	 */
	public static String maskAmount() {
		return IAppConstants.MASKED_AMOUNT_STRING;
	}

	/**
	 * method give formatted two place decimal
	 * 
	 * @param amount
	 * @return
	 */
	public static String twoPlaceDecimalOf(double amount) {

		String stringArray[] = new String[3];
		String doubleAmtStr = ((Double) amount).toString();
		StringTokenizer stringToken = new StringTokenizer(doubleAmtStr, ".");
		int len = stringToken.countTokens();
		stringArray[0] = stringToken.nextToken();
		if (len == 2) {
			stringArray[1] = stringToken.nextToken();
			if (stringArray[1].length() < 2) {
				stringArray[1] = PadderUtil.rightPad(stringArray[1], 2, '0');
			}
			stringArray[0] = stringArray[0] + "." + stringArray[1];
		}
		return stringArray[0];

	}

	/**
	 * Method give formatted two place decimal
	 * 
	 * @param amount
	 *            (String)
	 * @return String
	 */
	public static String twoPlaceDecimalOf(String amount) {

		String stringArray[] = new String[3];
		String doubleAmtStr = amount;
		StringTokenizer stringToken = new StringTokenizer(doubleAmtStr, ".");
		int len = stringToken.countTokens();
		stringArray[0] = stringToken.nextToken();
		if (len == 2) {
			stringArray[1] = stringToken.nextToken();
			if (stringArray[1].length() < 2) {
				stringArray[1] = PadderUtil.rightPad(stringArray[1], 2, '0');
			}
			stringArray[0] = stringArray[0] + "." + stringArray[1];
		}
		System.out.println("stringArray[0]: " + stringArray[0]);
		return stringArray[0];

	}

	/**
	 * checks for a proper fraction for cents
	 * 
	 * @param amount
	 *            RETURN TRUE - IF PASSED 
	 *            RETURN FALSE - IF FAILED
	 */
	public static boolean isFractionsOfCentsNormal(String amount) {
		if (amount.contains(".")) {
			StringTokenizer stringToken = new StringTokenizer(amount, ".");
			int tokenCount = stringToken.countTokens();
			if (tokenCount == 1) {
				String firstToken = stringToken.nextToken();
				if (firstToken.length() > 2 && Integer.parseInt(firstToken.substring(2)) != 0) {
					return false;
				} else {
					return true;
				}
			} else if (tokenCount == 2) {
				stringToken.nextToken();
				String decimalPart = stringToken.nextToken();
				if (decimalPart.length() > 2 && !decimalPart.substring(2, 3).equalsIgnoreCase("0")
						&& Long.parseLong(decimalPart.substring(2)) != 0) {
					return false;
				} else if (decimalPart.length() > 2 && Long.parseLong(decimalPart.substring(2)) == 0) {
					return true;
				} else if (decimalPart.length() > 2) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * rounds upper half
	 * 
	 * @param val
	 * @param decPlaces
	 * @return
	 */
	public static double roundHalfUp(double val, int decPlaces) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(decPlaces, BigDecimal.ROUND_HALF_UP);
		val = bd.doubleValue();
		return val;
	}

	/**
	 * rounds upper half
	 * 
	 * @param val
	 * @return
	 */
	public static double roundHalfUp(double val) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		val = bd.doubleValue();
		return val;
	}
	
	public static double roundHalfUpForTax(double val, double tax) {
		BigDecimal bd = new BigDecimal(""+val);
		bd  = bd.multiply(new BigDecimal(tax)).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		val = bd.doubleValue();
		return val;
	}

	public static String roundHalfUp(String val) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		String val1 = null;
		val1 = bd.toString();
		return val1;
	}

	/**
	 * rounds to next int
	 * 
	 * @param val
	 * @param decPlaces
	 * @return
	 */
	public static double roundUp(double val, int decPlaces) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(decPlaces, BigDecimal.ROUND_UP);
		val = bd.doubleValue();
		return val;
	}

	/**
	 * rounds to next int
	 * 
	 * @param val
	 * @return
	 */
	/*
	 * public static double roundUp(double val) { BigDecimal bd = new
	 * BigDecimal(val); bd = bd.setScale(0, BigDecimal.ROUND_UP); val =
	 * bd.doubleValue(); return val; }
	 */

	public static String roundUp(String val) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(0, BigDecimal.ROUND_UP);
		String val1 = null;
		val1 = bd.toString();
		return val1;
	}

	/**
	 * roundDown Round to previous int
	 * 
	 * @param val
	 * @return
	 */
	public static String roundDown(String val) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(0, BigDecimal.ROUND_DOWN);
		String val1 = null;
		val1 = bd.toString();
		return val1;
	}

	/**
	 * roundNear Round to nearest value
	 * 
	 * @param val
	 * @return
	 */
	public static String roundNear(String val) {
		BigDecimal bd = new BigDecimal(""+val);
		bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
		String val1 = null;
		val1 = bd.toString();
		return val1;
	}

	/**
	 * performRounding Round to different values ased on siteconfig
	 * 
	 * @param val
	 * @return
	 */

	public static String performRounding(String val) {
		String roundedValue = null;
		String roundingOptions = MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.JACKPOT_ROUNDING_OPTIONS);
		System.out.println("Jackpot Rounding Options:" + roundingOptions);
		if (roundingOptions != null) {
			if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_NEAREST)) {
				roundedValue = roundNear(val);
			} else if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_UP)) {
				roundedValue = roundUp(val);
			} else if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_DOWN)) {
				roundedValue = roundDown(val);
			}
		} else {
			roundedValue = roundNear(val);
		}
		System.out.println("Rounded value:" + roundedValue);
		return roundedValue;

	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		/*
		 * log.debug(dollarToCents("12.3")); log.debug(dollarToCents(".32"));
		 * log.debug(dollarToCents(6.1)); log.debug(centsToDollar("123456"));
		 * log.debug(centsToDollar(33));
		 * log.debug(maskAmount(centsToDollar("123456789")));
		 * log.debug(maskAmount(23.43)); log.debug(twoPlaceDecimalOf(23.124));
		 */
		// log.info(ConversionUtil.roundHalfUp(363377.467));
		// log.info(ConversionUtil.twoPlaceDecimalOf(1.00));
		// log.info(ConversionUtil.roundUp(12.33));
		// log.info(ConversionUtil.isFractionsOfCentsNormal("22333333.3"));

		double amt = 80000;

		// System.out.println("Amt : "+amt);
		// System.out.println(ConversionUtil.maskAmount(amt));
		// System.out.println("----: "+ConversionUtil.maskAmount(ConversionUtil
		// .centsToDollar("80000")));

		System.out.println(ConversionUtil.dollarToCentsRtnLong("1.13"));
		System.out.println(ConversionUtil.dollarToCents("1.13"));
		System.out.println(ConversionUtil.dollarToCents("1.13"));
		System.out.println(ConversionUtil.roundUp("1.13"));
		System.out.println(ConversionUtil.roundHalfUp("1.13"));
	}

}
