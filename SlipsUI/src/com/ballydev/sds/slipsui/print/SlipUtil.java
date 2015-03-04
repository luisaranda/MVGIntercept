/**
 * 
 */
package com.ballydev.sds.slipsui.print;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.slips.dto.SlipsXMLInfoDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;
import com.ballydev.sds.slipsui.util.SlipUILogger;
import com.ballydev.sds.slipsui.util.XMLClientUtil;

/**
 * @author gsrinivasulu
 *
 */
public class SlipUtil {
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
	 * Beef slip map
	 */
	public static HashMap beefSlipMap;

	/**
	 * Void slip map
	 */
	public static HashMap voidSlipMap;

	/**
	 * Report slip map
	 */
	public static HashMap reportSlipMap;

	/**
	 * Printer schema as string
	 */
	public static String printerSchemaAsString;

	/**
	 * Slip schema as string
	 */
	public static String slipsSchemaAsString;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * This method will return the list of Slip keys.
	 * 
	 * @param slipType
	 * @param slipSchemaFileName
	 * @return
	 * @throws Exception
	 */
	public static List<String> getSlipKeys(String slipType,
			String slipSchemaFileName) throws Exception {

		List<String> keyList = new ArrayList<String>();
		Element fieldElement = null;
		InputStream inStream = new FileInputStream(
				IAppConstants.SLIP_SCHEMA_FILE);

		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(inStream);
		Element slips = doc.getRootElement();
		List slipLst = slips.getChildren();
		Element e = null;
		for (int ii = 0; ii < slipLst.size(); ii++) {
			e = (Element) slipLst.get(ii);

			if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {
				log.info("slip Type :" + e.getAttributeValue("name"));
				break;
			}
		}
		List fieldList = e.getChildren();
		for (int i = 0; i < fieldList.size(); i++) {

			fieldElement = (Element) fieldList.get(i);
			keyList.add(fieldElement.getAttributeValue("key"));
		}

		return keyList;

	}

	/**
	 * This method will return the key value pair for the slip items.
	 * 
	 * @param slipType
	 * @return
	 */
	public static HashMap<String, String> getSlipKeyValues(String slipType) {
		HashMap<String, String> slipKeyMap = new HashMap<String, String>();
		List<String> keyList = null;
		try {
			keyList = getSlipKeys(slipType, IAppConstants.SLIP_SCHEMA_FILE);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		for (int i = 0; i < keyList.size(); i++) {
			slipKeyMap.put(keyList.get(i), LabelLoader.getLabelValue(keyList
					.get(i)));
			// slipKeyMap.put(keyList.get(i), keyList.get(i));
		}
		return slipKeyMap;
	}

	public static void getSlipSchema() {

		SlipsXMLInfoDTO schemaFileList = null;
		// File file =null;
		try {
			schemaFileList = SlipsServiceLocator.getService().getSlipsXMLInfo();
			// log.info("The siz of list is "+schemaFileList.size());
			if(schemaFileList!=null){
				printerSchemaAsString = schemaFileList.getPrinterSchema();
				slipsSchemaAsString = schemaFileList.getSlipsSchema();
			}			
			log.info("SSF :" + slipsSchemaAsString);

		} catch (SlipsEngineServiceException e) {
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception getJackpotXMLInfo web method in SlipUtil class",e);
		}catch (Exception e2) {
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);						
			log.error("SERVICE_DOWN",e2);	
			return;
		}
		try {
			// schemaFiles[0] =
			// XMLClientUtil.stringToFile(schemaFileList.get(0),"printerSchema");
			if(schemaFileList!=null){
				XMLClientUtil.stringToFile(schemaFileList.getSlipsSchema(), IAppConstants.SLIP_SCHEMA_FILE_KEY);
			}			
			voidSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.VOID_PRINT_SLIP_TYPE);
			log.info("The size of voidSlipMap: " + voidSlipMap.size());
			beefSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.BEEF_PRINT_SLIP_TYPE);
			log.info("The size of jackpotSlipMap: "
					+ beefSlipMap.size());
			reportSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.REPORT_PRINT_SLIP_TYPE);
			log.info("The size of reportSlipMap: "
					+ reportSlipMap.size());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Formats the given String
	 * @param fieldStr
	 * @return
	 */
	public static String formatField(String fieldStr, int formatLength) {

		StringBuffer retStr = new StringBuffer(fieldStr);

		int length = fieldStr.length();
		if (length < formatLength) {
			for (int i = length; i < formatLength; i++) {

				retStr.append(" ");
			}
		}

		return retStr.toString();

	}

	
	 
		public static String getAmountInWords(long jpNetAmount) {
			String rtnValue = null;
			rtnValue = convertAmount(jpNetAmount);
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
				String dollarsText = LabelKeyConstants.LBL_DOLLARS;

				int tmp = 0;

				if ((amount >= 100) & (amount < 200)) {
					dollarsText = LabelKeyConstants.LBL_DOLLAR;
				}

				if (amount >= ONES) {
					/* Determine how many trillions */
					tmp = (int) (amount / TRILLIONS);
					if (tmp > 0) {
						sb.append(convertThreeDigits(tmp));
						sb.append(" " + LabelKeyConstants.LBL_TRILLION + " ");
						amount -= (tmp * TRILLIONS);
					}

					/* Determine how many billions */
					tmp = (int) (amount / BILLIONS);
					if (tmp > 0) {
						sb.append(convertThreeDigits(tmp));
						sb.append(" " + LabelKeyConstants.LBL_BILLION + " ");
						amount -= (tmp * BILLIONS);
					}

					/* Determine how many millions */
					tmp = (int) (amount / MILLIONS);
					if (tmp > 0) {
						sb.append(convertThreeDigits(tmp));
						sb.append(" " + LabelKeyConstants.LBL_MILLION + " ");
						amount -= (tmp * MILLIONS);
					}

					/* Determine how many 100's of thousands */
					tmp = (int) (amount / THOUSANDS);

					if (tmp > 0) {
						sb.append(convertThreeDigits(tmp));
						sb.append(" " + LabelKeyConstants.LBL_THOUSAND + " ");
						amount -= (tmp * THOUSANDS);
					}

					tmp = (int) (amount / ONES);

					if (tmp > 0) {
						sb.append(convertThreeDigits(tmp));
						amount -= (tmp * ONES);
					}
				} else {
					sb.append(LabelKeyConstants.LBL_ZERO);
				}

				sb.append(" " + dollarsText + " ");

				if (amount == 0) {
					String patern = CurrencyUtil.getCurrencyPatern();
					String decimalSeperator = CurrencyUtil.getDecmSeperator();
					if (patern.indexOf(decimalSeperator) != -1) {
						sb.append(LabelKeyConstants.LBL_AND_NO + " "
								+ LabelKeyConstants.LBL_CENTS);
					}
				} else if (amount == 1) {
					sb.append(LabelKeyConstants.LBL_AND_ONE + " "
							+ LabelKeyConstants.LBL_CENT);
				} else {
					sb.append(LabelKeyConstants.LBL_AND + " "
							+ convertTwoDigits(amount) + " "
							+ LabelKeyConstants.LBL_CENTS);
				}

			} catch (Exception e) {
				log.error("Exception in Util.convertAmount: ", e);
			}
			return sb.toString();
		}

		private static String convertThreeDigits(int amount) {
			StringBuffer sb = new StringBuffer();
			int tmp = amount / HUNDREDS;

			if (tmp > 0) {
				sb.append(convertOneDigit(tmp) + " " + LabelKeyConstants.LBL_HUNDRED);
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
				text = LabelKeyConstants.LBL_ONE;
				break;
			case 2:
				text = LabelKeyConstants.LBL_TWO;
				break;
			case 3:
				text = LabelKeyConstants.LBL_THREE;
				break;
			case 4:
				text = LabelKeyConstants.LBL_FOUR;
				break;
			case 5:
				text = LabelKeyConstants.LBL_FIVE;
				break;
			case 6:
				text = LabelKeyConstants.LBL_SIX;
				break;
			case 7:
				text = LabelKeyConstants.LBL_SEVEN;
				break;
			case 8:
				text = LabelKeyConstants.LBL_EIGHT;
				break;
			case 9:
				text = LabelKeyConstants.LBL_NINE;
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
				return LabelKeyConstants.LBL_TEN;
			} else if (amount == 11) {
				return LabelKeyConstants.LBL_ELEVEN;
			} else if (amount == 12) {
				return LabelKeyConstants.LBL_TWELVE;
			} else if (amount == 13) {
				return LabelKeyConstants.LBL_THIRTEEN;
			} else if (amount == 14) {
				return LabelKeyConstants.LBL_FOURTEEN;
			} else if (amount == 15) {
				return LabelKeyConstants.LBL_FIFTEEN;
			} else if (amount == 16) {
				return LabelKeyConstants.LBL_SIXTEEN;
			} else if (amount == 17) {
				return LabelKeyConstants.LBL_SEVENTEEN;
			} else if (amount == 18) {
				return LabelKeyConstants.LBL_EIGHTEEN;
			} else if (amount == 19) {
				return LabelKeyConstants.LBL_NINETEEN;
			} else if ((amount >= 20) && (amount < 30)) {
				sb.append(LabelKeyConstants.LBL_TWENTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 30) && (amount < 40)) {
				sb.append(LabelKeyConstants.LBL_THIRTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 40) && (amount < 50)) {
				sb.append(LabelKeyConstants.LBL_FORTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 50) && (amount < 60)) {
				sb.append(LabelKeyConstants.LBL_FIFTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 60) && (amount < 70)) {
				sb.append(LabelKeyConstants.LBL_SIXTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 70) && (amount < 80)) {
				sb.append(LabelKeyConstants.LBL_SEVENTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 80) && (amount < 90)) {
				sb.append(LabelKeyConstants.LBL_EIGHTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit(amount % 10));
				}
			} else if ((amount >= 90) && (amount < 100)) {
				sb.append(LabelKeyConstants.LBL_NINETY);

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
				return LabelKeyConstants.LBL_TEN;
			} else if (amount == 11) {
				return LabelKeyConstants.LBL_ELEVEN;
			} else if (amount == 12) {
				return LabelKeyConstants.LBL_TWELVE;
			} else if (amount == 13) {
				return LabelKeyConstants.LBL_THIRTEEN;
			} else if (amount == 14) {
				return LabelKeyConstants.LBL_FOURTEEN;
			} else if (amount == 15) {
				return LabelKeyConstants.LBL_FIFTEEN;
			} else if (amount == 16) {
				return LabelKeyConstants.LBL_SIXTEEN;
			} else if (amount == 17) {
				return LabelKeyConstants.LBL_SEVENTEEN;
			} else if (amount == 18) {
				return LabelKeyConstants.LBL_EIGHTEEN;
			} else if (amount == 19) {
				return LabelKeyConstants.LBL_NINETEEN;
			} else if ((amount >= 20) && (amount < 30)) {
				sb.append(LabelKeyConstants.LBL_TWENTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 30) && (amount < 40)) {
				sb.append(LabelKeyConstants.LBL_THIRTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 40) && (amount < 50)) {
				sb.append(LabelKeyConstants.LBL_FORTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 50) && (amount < 60)) {
				sb.append(LabelKeyConstants.LBL_FIFTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 60) && (amount < 70)) {
				sb.append(LabelKeyConstants.LBL_SIXTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 70) && (amount < 80)) {
				sb.append(LabelKeyConstants.LBL_SEVENTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 80) && (amount < 90)) {
				sb.append(LabelKeyConstants.LBL_EIGHTY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			} else if ((amount >= 90) && (amount < 100)) {
				sb.append(LabelKeyConstants.LBL_NINETY);

				if ((amount % 10) > 0) {
					sb.append(" " + convertOneDigit((int) (amount % 10)));
				}
			}

			return sb.toString();
		}
}
