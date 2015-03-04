/*****************************************************************************
 * $Id: JackpotUIUtil.java,v 1.5.1.1, 2011-04-20 11:06:16Z, SDS12.3.2CheckinUser$
 * $Date: 4/20/2011 6:06:16 AM$
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
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.controller.TopMiddleController;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;

/**
 * Util class for Jackpot
 * 
 * @author dambereen
 * @version $Revision: 8$
 */
public class JackpotUIUtil {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

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

	/**
	 * Method to dispose the current midle composite
	 */
	public static void disposeCurrentMiddleComposite() {
		if (TopMiddleController.getCurrentComposite() != null
				&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
			TopMiddleController.getCurrentComposite().dispose();
		}
	}

	public static String getFormattedAmounts(Double amountToFormat) {
		String formattedAmount = null;
		try {
			formattedAmount = CurrencyUtil.getCurrencyFormat(amountToFormat);
			if (formattedAmount == null && amountToFormat != null) {
				formattedAmount = String.valueOf(amountToFormat.doubleValue());
			}

		} catch (Exception e) {

		}
		return formattedAmount;
	}

	public static String getFormattedAmounts(String amountToFormat) {
		String formattedAmount = null;
		try {
			double doubleValueToFormat = Double.parseDouble(amountToFormat.trim());
			formattedAmount = CurrencyUtil.getCurrencyFormat(doubleValueToFormat);
			if (formattedAmount == null && amountToFormat != null) {
				formattedAmount = amountToFormat.trim();
			}

		} catch (Exception e) {
			formattedAmount = amountToFormat;
		}
		return formattedAmount;
	}

	/**
	 * Method that retuns the round jackpot site config key
	 * 
	 * @param slotDenom
	 * @return
	 */
	public static String getSiteConfigKeyDenom(String slotDenom) {
		String rtnString = null;
		try {
			log.info("inside getSiteConfigKeyDenom");
			log.debug("slotDenom: " + slotDenom);
			rtnString = null;
			for (int i = 0; i < 16; i++) {
				String siteKey = "DENOM." + i + ".VALUE";
				if (slotDenom != null
						&& slotDenom
								.equalsIgnoreCase(MainMenuController.jackpotSiteConfigParams.get(siteKey))) {

					String[] strArray = siteKey.trim().split(".VALUE");
					if (strArray != null) {
						log.info("Sub Appended Rounded key: " + ISiteConfigConstants.ROUND_JACKPOTS
								+ strArray[0]);
						rtnString = ISiteConfigConstants.ROUND_JACKPOTS + strArray[0];
						break;
					} else {
						log.info("String Array is null");
						rtnString = null;
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return rtnString;
	}

	/**
	 * Method to return the jp net amount in words
	 * 
	 * @param jpNetAmount
	 * @return
	 */
	public static String getAmountInWords(long jpNetAmount) {
		String rtnValue = null;
		log.debug("jpNetAmount: " + jpNetAmount);
		rtnValue = JackpotUIUtil.convertAmount(jpNetAmount);
		return rtnValue;
	}

	/**
	 * Given an amount in pennies convert it to dollar string 
	 * @param amount amount to convert 
	 * @return dollar string
	 */
	public static String convertAmount(long amount) {
		StringBuffer sb = new StringBuffer();
		try {
			String dollarsText = LabelLoader.getLabelValue(LabelKeyConstants.LBL_DOLLARS);

			int tmp = 0;

			if ((amount >= 100) & (amount < 200)) {
				dollarsText = LabelLoader.getLabelValue(LabelKeyConstants.LBL_DOLLAR);
			}

			if (amount >= ONES) {
				/* Determine how many trillions */
				tmp = (int) (amount / TRILLIONS);
				if (tmp > 0) {
					sb.append(convertThreeDigits(tmp));
					sb.append(" " + LabelLoader.getLabelValue(LabelKeyConstants.LBL_TRILLION) + " ");
					amount -= (tmp * TRILLIONS);
				}

				/* Determine how many billions */
				tmp = (int) (amount / BILLIONS);
				if (tmp > 0) {
					sb.append(convertThreeDigits(tmp));
					sb.append(" " + LabelLoader.getLabelValue(LabelKeyConstants.LBL_BILLION) + " ");
					amount -= (tmp * BILLIONS);
				}

				/* Determine how many millions */
				tmp = (int) (amount / MILLIONS);
				if (tmp > 0) {
					sb.append(convertThreeDigits(tmp));
					sb.append(" " + LabelLoader.getLabelValue(LabelKeyConstants.LBL_MILLION) + " ");
					amount -= (tmp * MILLIONS);
				}

				/* Determine how many 100's of thousands */
				tmp = (int) (amount / THOUSANDS);

				if (tmp > 0) {
					sb.append(convertThreeDigits(tmp));
					sb.append(" " + LabelLoader.getLabelValue(LabelKeyConstants.LBL_THOUSAND) + " ");
					amount -= (tmp * THOUSANDS);
				}

				tmp = (int) (amount / ONES);

				if (tmp > 0) {
					sb.append(convertThreeDigits(tmp));
					amount -= (tmp * ONES);
				}
			} else {
				sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_ZERO));
			}

			sb.append(" " + dollarsText + " ");

			if (amount == 0) {
				String patern = CurrencyUtil.getCurrencyPatern();
				String decimalSeperator = CurrencyUtil.getDecmSeperator();
				if (patern.indexOf(decimalSeperator) != -1) {
					sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_AND_NO) + " "
							+ LabelLoader.getLabelValue(LabelKeyConstants.LBL_CENTS));
				}
			} else if (amount == 1) {
				sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_AND_ONE) + " "
						+ LabelLoader.getLabelValue(LabelKeyConstants.LBL_CENT));
			} else {
				sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_AND) + " "
						+ convertTwoDigits(amount) + " "
						+ LabelLoader.getLabelValue(LabelKeyConstants.LBL_CENTS));
			}

		} catch (Exception e) {
			log.error("Exception in Util.convertAmount: ", e);
		}
		return sb.toString();
	}

	/**
	 * Convert three digits to a text string 
	 * @param amount value to convert
	 * @return text of the converted digits
	 */
	private static String convertThreeDigits(int amount) {
		StringBuffer sb = new StringBuffer();
		int tmp = amount / HUNDREDS;

		if (tmp > 0) {
			sb.append(convertOneDigit(tmp) + " " + LabelLoader.getLabelValue(LabelKeyConstants.LBL_HUNDRED));
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
	 * convert one digit amount to a text representation.
	 * 
	 * @param amount
	 *            digit to convert
	 * 
	 * @return string representation of the digit
	 */
	private static String convertOneDigit(int amount) {
		String text = null;
		switch (amount) {
		case 0:
			break;
		case 1:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_ONE);
			break;
		case 2:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_TWO);
			break;
		case 3:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_THREE);
			break;
		case 4:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_FOUR);
			break;
		case 5:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_FIVE);
			break;
		case 6:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_SIX);
			break;
		case 7:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEVEN);
			break;
		case 8:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_EIGHT);
			break;
		case 9:
			text = LabelLoader.getLabelValue(LabelKeyConstants.LBL_NINE);
			break;
		}
		return text;
	}

	/**
	 * Convert two digits to text representation
	 * 
	 * @param amount
	 *            value to convert
	 * 
	 * @return text of the converted date
	 */
	private static String convertTwoDigits(int amount) {
		StringBuffer sb = new StringBuffer();

		if (amount < 10) {
			return convertOneDigit(amount);
		} else if (amount == 10) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_TEN);
		} else if (amount == 11) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_ELEVEN);
		} else if (amount == 12) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_TWELVE);
		} else if (amount == 13) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_THIRTEEN);
		} else if (amount == 14) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_FOURTEEN);
		} else if (amount == 15) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_FIFTEEN);
		} else if (amount == 16) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_SIXTEEN);
		} else if (amount == 17) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEVENTEEN);
		} else if (amount == 18) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_EIGHTEEN);
		} else if (amount == 19) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_NINETEEN);
		} else if ((amount >= 20) && (amount < 30)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_TWENTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 30) && (amount < 40)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_THIRTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 40) && (amount < 50)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_FORTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 50) && (amount < 60)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_FIFTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 60) && (amount < 70)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_SIXTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 70) && (amount < 80)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEVENTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 80) && (amount < 90)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_EIGHTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		} else if ((amount >= 90) && (amount < 100)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_NINETY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit(amount % 10));
			}
		}
		return sb.toString();
	}

	/**
	 * Convert two digits to text representation
	 * 
	 * @param amount
	 *            value to convert
	 * 
	 * @return text of the converted date
	 */
	private static String convertTwoDigits(long amount) {
		StringBuffer sb = new StringBuffer();

		if (amount < 10) {
			return convertOneDigit((int) amount);
		} else if (amount == 10) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_TEN);
		} else if (amount == 11) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_ELEVEN);
		} else if (amount == 12) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_TWELVE);
		} else if (amount == 13) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_THIRTEEN);
		} else if (amount == 14) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_FOURTEEN);
		} else if (amount == 15) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_FIFTEEN);
		} else if (amount == 16) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_SIXTEEN);
		} else if (amount == 17) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEVENTEEN);
		} else if (amount == 18) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_EIGHTEEN);
		} else if (amount == 19) {
			return LabelLoader.getLabelValue(LabelKeyConstants.LBL_NINETEEN);
		} else if ((amount >= 20) && (amount < 30)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_TWENTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 30) && (amount < 40)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_THIRTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 40) && (amount < 50)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_FORTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 50) && (amount < 60)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_FIFTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 60) && (amount < 70)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_SIXTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 70) && (amount < 80)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEVENTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 80) && (amount < 90)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_EIGHTY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		} else if ((amount >= 90) && (amount < 100)) {
			sb.append(LabelLoader.getLabelValue(LabelKeyConstants.LBL_NINETY));

			if ((amount % 10) > 0) {
				sb.append(" " + convertOneDigit((int) (amount % 10)));
			}
		}

		return sb.toString();
	}

	public static Color getDefaultBackGroudColor() {
		return new Color(Display.getCurrent(), 137, 176, 213);

	}

	/**
	 * Method to check if rounding can be done for the jackpot amount
	 * 
	 * @param hpjpAmount
	 * @return boolean
	 * @author vsubha
	 */
	public static boolean isRoundingAllowedForJPAmount(String hpjpAmount) {
		String roundingOptions = MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.JACKPOT_ROUNDING_OPTIONS);
		if (log.isInfoEnabled()) {
			log.info("Jackpot Rounding Option : " + roundingOptions);
		}
		BigDecimal bd = new BigDecimal(hpjpAmount);
		if (roundingOptions != null) {
			if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_NEAREST)
					&& bd.doubleValue() >= IAppConstants.HALF_DOLLAR) {
				if (log.isInfoEnabled()) {
					log.info("HPJP Amount " + hpjpAmount + " is greater than " + IAppConstants.HALF_DOLLAR
							+ ", so rounding allowed.");
				}
				return true;
			} else if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_DOWN)
					&& bd.doubleValue() >= IAppConstants.ONE_DOLLAR) {
				if (log.isInfoEnabled()) {
					log.info("HPJP Amount " + hpjpAmount + " is greater than " + IAppConstants.ONE_DOLLAR
							+ ", so rounding allowed.");
				}
				return true;
			} else if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_UP)) {
				return true;
			}
		}
		if (log.isInfoEnabled()) {
			log.info("Jackpot rounding is not allowed for the amount " + hpjpAmount);
		}
		return false;
	}

	/**
	 * Method to check if rounding can be done for the jackpot amount
	 * 
	 * @param hpjpAmount
	 * @return boolean
	 * @author vsubha
	 */
	public static boolean isRoundingAllowedForJPAmount(double hpjpAmount) {
		String roundingOptions = MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.JACKPOT_ROUNDING_OPTIONS);
		if (log.isInfoEnabled()) {
			log.info("Jackpot Rounding Option : " + roundingOptions);
		}
		if (roundingOptions != null) {
			if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_NEAREST)
					&& hpjpAmount >= IAppConstants.HALF_DOLLAR) {
				if (log.isInfoEnabled()) {
					log.info("HPJP Amount " + hpjpAmount + " is greater than " + IAppConstants.HALF_DOLLAR
							+ ", so rounding allowed.");
				}
				return true;
			} else if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_DOWN)
					&& hpjpAmount >= IAppConstants.ONE_DOLLAR) {
				if (log.isInfoEnabled()) {
					log.info("HPJP Amount " + hpjpAmount + " is greater than " + IAppConstants.ONE_DOLLAR
							+ ", so rounding allowed.");
				}
				return true;
			} else if (roundingOptions.equalsIgnoreCase(ISiteConfigConstants.JACKPOT_ROUNDING_UP)) {
				return true;
			}
		}
		if (log.isInfoEnabled()) {
			log.info("Jackpot rounding is not allowed for the amount " + hpjpAmount);
		}
		return false;
	}
	
	/**
	 * Method to get the list of progressive level values
	 * @param progressiveLevels
	 * @return
	 * @author vsubha
	 */
	public static ArrayList<Integer> getProgressiveLevelValues(String progressiveLevels) {
		ArrayList<Integer> lstProgLevel = new ArrayList<Integer>();
		
		String[] stringArray = progressiveLevels.split(",");
		for (int i = 0; i < stringArray.length; i++) {
			char[] chars = stringArray[i].toCharArray();
			int index = 0;
			for (; index < stringArray[i].length(); index++) {
				if (chars[index] != '0') {
					break;
				}
			}
			lstProgLevel.add(new Integer((index == 0) ? stringArray[i] : stringArray[i].substring(index)));
		}
		return lstProgLevel;
	}

	
	/**
	 * Method to update the blind attempts to -1 when the invalid blind attmpts exceeded exp jp process is aborted/cancelled
	 * @param sequenceNo
	 * @param siteId
	 * @return boolean
	 * @author dambereen
	 */
	public static boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNo, int siteId){
		boolean isBlindAttUpdated = false;
		try {
			log.debug("Calling the postUnsuccessfulExpJpBlindAttemptsAbort web method");
			log.debug("*******************************************");
			log.debug("Sequence no: "+sequenceNo);				
			log.debug("*******************************************");
			isBlindAttUpdated = JackpotServiceLocator.getService().postUnsuccessfulExpJpBlindAttemptsAbort(sequenceNo, siteId);
			log.debug("Values returned after calling the postUnsuccessfulExpJpBlindAttemptsAbort web method");
			log.debug("*******************************************");
			log.debug("postUnsuccessfulExpJpBlindAttemptsAbort response: "+isBlindAttUpdated);								
			log.debug("*******************************************");
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2,"Exception in postUnsuccessfulExpJpBlindAttemptsAbort");
			log.error("Exception in postUnsuccessfulExpJpBlindAttemptsAbort",e2);
		}
		return isBlindAttUpdated;
	}
	
	
	/**
	 * Method to validate if the amount field contains the MASKED STRING
	 * @param fieldValue
	 * @return boolean
	 * @author dambereen
	 */
	public static boolean validateForMaskedAmount(String fieldValue) {
		if (fieldValue!=null && fieldValue.equalsIgnoreCase(IAppConstants.MASKED_AMOUNT_STRING)){	
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		// String amount = "1.9";
		// System.out.println("amt: "+amount);
		// System.out.println("write amt: "+Util.isRoundingAllowedForJPAmount(amount));
		/*ArrayList<Integer> lstProgLevel = getProgressiveLevelValues("2,34,46");
		for(int i =0 ; i<lstProgLevel.size();i++) {
			System.out.println(lstProgLevel.get(i));
		}*/
	}
}