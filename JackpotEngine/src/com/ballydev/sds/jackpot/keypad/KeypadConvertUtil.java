package com.ballydev.sds.jackpot.keypad;

import java.math.BigDecimal;
import java.util.StringTokenizer;



/**
 * Utility Class to provide utils for basic needed conversions.
 * @author ranjithkumarm
 *
 */
public class KeypadConvertUtil {


	/**
	 * This method is used to convert a dollar amount to cents given double as
	 * input
	 * 
	 * @param amount
	 * @return
	 */
	public static long dollarToCents(double amount) {

		return (long) (amount * 100);
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
	 * 
	 * @param amount
	 * @return
	 */
	public static long dollarToCentsRtnLong(String amount) {

		double doubleVal = Double.parseDouble(amount);
		long longVal = (long) (doubleVal * 100);
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
		String secondStr = amount.substring(amount.length() - 2, amount
				.length());
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
		String maskAmountStr = PadderUtil.leftPad("*", firstStr.length() - 3,
				'*')
				+ PadderUtil.rightPad(".", 3, '*');
		return maskAmountStr;
	}
	
	/**
	 * Method that will return a string of stars for the jackpot amount
	 * @return String
	 */
	public static String maskAmount() {		
		return "******";
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
				if(firstToken.length()>2 && Integer.parseInt(firstToken.substring(2))!=0){				
					return false;
				}
				else{				
						return true;
					}
			} else if (tokenCount == 2) {
				stringToken.nextToken();
				String decimalPart = stringToken.nextToken();
				if(decimalPart.length()>2 && !decimalPart.substring(2,3).equalsIgnoreCase("0") && Integer.parseInt(decimalPart.substring(2))!=0){
					return false;
				}else if(decimalPart.length()>2 && Integer.parseInt(decimalPart.substring(2))==0){				
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
	
	/**
	 * rounds upper half
	 * 
	 * @param val
	 * @param decPlaces
	 * @return
	 */
	public static double roundHalfUp(double val, int decPlaces) {
		BigDecimal bd = new BigDecimal(val);
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
		BigDecimal bd = new BigDecimal(val);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		val = bd.doubleValue();
		return val;
	}

	/**
	 * rounds to next int
	 * 
	 * @param val
	 * @param decPlaces
	 * @return
	 */
	public static double roundUp(double val, int decPlaces) {
		BigDecimal bd = new BigDecimal(val);
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
	public static double roundUp(double val) {
		BigDecimal bd = new BigDecimal(val);
		bd = bd.setScale(0, BigDecimal.ROUND_UP);
		val = bd.doubleValue();
		return val;
	}
	
	
	public static String trimAcnfNo(String acnfNo) {
		if(acnfNo == null) {
			return acnfNo;
		}
		return String.valueOf(Integer.parseInt(acnfNo));
	}

	



}
