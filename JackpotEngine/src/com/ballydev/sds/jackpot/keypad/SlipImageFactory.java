package com.ballydev.sds.jackpot.keypad;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.ConversionUtil;
import com.ballydev.sds.jackpot.util.JackpotUtil;

/**
 * 
 * Image builder class for printing a valid slip and sending it to printer.
 * 
 * @author ranjithkumarm
 * 
 */
public class SlipImageFactory {

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * Builds slip image
	 * 
	 * @param dto
	 *            The dto to be used to build slip image
	 * @param slipType
	 *            Type of the slip image to be built
	 * @return slip image
	 * @throws JackpotEngineServiceException
	 *             if there is an issue
	 */
	public SlipImageFactory() {

	}

	/**
	 * Method to build the Slip image that will be printed on the slip for an
	 * EPSON 40 column printer
	 * 
	 * @param dto
	 * @param slipType
	 * @param slipI18NMap
	 * @return String
	 * @throws Exception
	 */
	public String buildSlipImage(Object dto, String slipType, Map<String, String> slipI18NMap,
			Long jpHitAmountToProcess, String languageSelectedToProcess, Integer siteNumber) throws Exception {
		// String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		// String schemaContents[] = new String[2];
		String method = IKeypadProcessConstants.EMPTY_STRING;
		String value = IKeypadProcessConstants.EMPTY_STRING;
		StringBuffer image = new StringBuffer(IKeypadProcessConstants.EMPTY_STRING);
		String printerName = "EPSON";
		String currencySymbol = JackpotUtil.getSiteParamValue(
				IKeypadProcessConstants.CURRENCY_SYMBOL, siteNumber);
		/**
		 * PRINTER NAME - the commands below can be used only by EPSON 40 column
		 * printers
		 */
		String retImage = IKeypadProcessConstants.EMPTY_STRING;

		Class dtoClass = null;
		String slipFieldValue = IKeypadProcessConstants.EMPTY_STRING;
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;

		/*
		 * for (int i = 0; i < schemaMethods.length; i++) { getDTOMethod =
		 * dtoClass.getMethod(schemaMethods[i], new Class[] {});
		 * schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
		 * log.info(schemaContents[i]); }
		 */
		PrinterCommandParser pc = new PrinterCommandParser();
		// sending of schemaContents[0] is not required below.
		Map<String, String> printerCmd = pc.getCommandList(printerName);
		InputStream inStream = null;
		try {
			// File slipschema = XMLClientUtil.stringToFile(schemaContents[1]);
			inStream = SlipImageFactory.class
					.getResourceAsStream(IKeypadProcessConstants.SLIP_SCHEMA_XML_FILE_PATH_KEYPAD);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(inStream);
			Element slips = doc.getRootElement();
			List slipLst = slips.getChildren();
			Element e = null;
			for (int ii = 0; ii < slipLst.size(); ii++) {
				e = (Element) slipLst.get(ii);

				if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {
					break;
				}
			}
			List fieldList = e.getChildren();
			
			image = image.append(getCommand(printerCmd.get("PRINT_INIT")));
			PrintDTO printDTO = (PrintDTO)dto;
			for (int k = 0; k < 2; k++) {
				Element fieldElement = (Element) fieldList.get(k);
				
				if (fieldElement.getAttributeValue("display").equalsIgnoreCase("TRUE")) {
					method = "get" + fieldElement.getName();
					image = image.append(getCommand(printerCmd.get(fieldElement.getAttributeValue("align")))
							+ getCommand(printerCmd.get(fieldElement.getAttributeValue("font")))
							+ getCommand(printerCmd.get(fieldElement.getAttributeValue("color"))));
					try {
						getDTOMethod = dtoClass.getMethod(method, new Class[] {});
						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}
					if (fieldValue != null) {
						value = fieldValue.toString();
					}/*
						 * else{ value = "_______________"; }
						 */
					if (k == 0) {
						slipFieldValue = IKeypadProcessConstants.EMPTY_STRING;
					} else if (k == 1) {
						value = slipType;
						slipFieldValue = IKeypadProcessConstants.EMPTY_STRING;
					} else {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
					}
					image = image.append(slipFieldValue + value
							+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
				}
				fieldValue = null;
			}
			for (int k = 2; k < fieldList.size(); k++) {
				Element fieldElement = (Element) fieldList.get(k);
				String appendStr = "";
				String newLine = "\r\n";
				if( fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("sameline")){//These are tags added for GNBX
					int spaceLen	=	Integer.parseInt(fieldElement.getAttributeValue("spaceBtwFields").toString());
					int i = 0;
					while(i < spaceLen) {
						appendStr = appendStr +" ";
						i++;
					}
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_1")) {
					appendStr = newLine;
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_2"))  {
					appendStr = newLine+ "   " + newLine;
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_3")) {
					appendStr = newLine + "   " + newLine+ "   " + newLine;
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_4")) {
					appendStr = newLine + "   " + newLine+ "   " + newLine+ "   " + newLine;
				}
				if (fieldElement.getAttributeValue("display").equalsIgnoreCase("TRUE")) {
					method = "get" + fieldElement.getName();
					image = image.append(getCommand(printerCmd.get(fieldElement.getAttributeValue("align")))
							+ getCommand(printerCmd.get(fieldElement.getAttributeValue("font")))
							+ getCommand(printerCmd.get(fieldElement.getAttributeValue("color"))));
					try {
						getDTOMethod = dtoClass.getMethod(method, new Class[] {});
						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}
					if (fieldValue != null) {
						value = fieldValue.toString();
					} else {
						value = "_______________";
					}
					if (fieldElement != null && fieldElement.getName().equalsIgnoreCase("AmountInWriting")) {
						if (log.isInfoEnabled()) {
							log.info("Keypad words for amount printing - Epson.");
						}
						try {
							if (jpHitAmountToProcess != null) {
								value = KeypadUtil.convertAmount(jpHitAmountToProcess.longValue(),
										languageSelectedToProcess);
								if (log.isInfoEnabled()) {
									log.info("Amount in words - Epson:" + value);
								}

								slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
										.get(fieldElement.getAttributeValue("key"))
										: fieldElement.getAttributeValue("key");
								image = image.append(slipFieldValue
										+ ": "
										+ value
										+ getCommand(printerCmd.get(fieldElement
												.getAttributeValue("linefeed"))));
							} else {
								value = "_______________";
								if (log.isInfoEnabled()) {
									log.info("Amount in words cannot be processed since JP hit amount field is null - Epson.");
								}
							}
						} catch (Exception excp) {

						}
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpStateTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
								System.out.println("Value :"+printDTO.getHpjpAmount());
						slipFieldValue = slipFieldValue	+ "(" +printDTO.getHpjpAmount() + " * " + "0.0" + "%)"; 		
						image = image.append(slipFieldValue + ": " + currencySymbol + "0.00"
								+ appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpFederalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
								slipFieldValue = slipFieldValue	+ "(" +printDTO.getHpjpAmount() + " * " + "0.0" + "%)";	
								image = image.append(slipFieldValue + ": " + currencySymbol + "0.00"
										+ appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpMunicipalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
								slipFieldValue = slipFieldValue	+ "(" +printDTO.getHpjpAmount() + " * " + "0.0" + "%)";		
								image = image.append(slipFieldValue + ": " + currencySymbol + "0.00"
										+ appendStr);
					}else if (slipType.equalsIgnoreCase(IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE)
							&& JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.ENABLE_JACKPOT_MULTI_RANGE_SIGN, siteNumber)
									.equalsIgnoreCase("yes")) {

						if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("preparer"))) {

							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							image = image.append(slipFieldValue + ": " + value
									+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("supervisor"))) {

							boolean printValue = false;
							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) >= 2) {

								if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_1, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_2,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 2 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_2,
														siteNumber)))) {

									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_2, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 3 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_3, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 4 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 5 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
														siteNumber)))) {
									printValue = true;
								}

								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(slipFieldValue
											+ ": "
											+ value
											+ getCommand(printerCmd.get(fieldElement
													.getAttributeValue("linefeed"))));
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("manager"))) {

							boolean printValue = false;
							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) >= 3) {

								if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_2, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 3 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_3, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 4 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 5 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
														siteNumber)))) {
									printValue = true;
								}
								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(slipFieldValue
											+ ": "
											+ value
											+ getCommand(printerCmd.get(fieldElement
													.getAttributeValue("linefeed"))));
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("executiveOne"))) {

							boolean printValue = false;
							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) >= 4) {

								if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_3, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 4 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
										JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)) && ConversionUtil
										.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
												siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 5 && ConversionUtil
												.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
														siteNumber)))) {
									printValue = true;
								}

								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(slipFieldValue
											+ ": "
											+ value
											+ getCommand(printerCmd.get(fieldElement
													.getAttributeValue("linefeed"))));
									printValue = false;
								}

							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("executiveTwo"))) {

							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) == 5
									&& ((ConversionUtil.centsToDollar(jpHitAmountToProcess) > new Double(
											JackpotUtil.getSiteParamValue(
													IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)) && ConversionUtil
											.centsToDollar(jpHitAmountToProcess) <= new Double(JackpotUtil.getSiteParamValue(
													IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber))) || (ConversionUtil
											.centsToDollar(jpHitAmountToProcess) > new Double(JackpotUtil.getSiteParamValue(
													IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber))))) {

								slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
										.get(fieldElement.getAttributeValue("key"))
										: fieldElement.getAttributeValue("key");
								image = image.append(slipFieldValue
										+ ": "
										+ value
										+ getCommand(printerCmd.get(fieldElement
												.getAttributeValue("linefeed"))));

							}
						} else {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							image = image.append(slipFieldValue + ": " + value
									+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
						}

					} else {

						log.debug("ENABLE_JACKPOT_MULTI_RANGE_SIGN is no");

						if (slipType.equalsIgnoreCase(IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE)
								&& (fieldElement.getName() != null)
								&& (fieldElement.getName().equalsIgnoreCase("preparer")
										|| fieldElement.getName().equalsIgnoreCase("supervisor")
										|| fieldElement.getName().equalsIgnoreCase("manager")
										|| fieldElement.getName().equalsIgnoreCase("executiveOne") || fieldElement
										.getName().equalsIgnoreCase("executiveTwo"))) {
							// do not print it on the slip
							log.debug("do not print it on the slip");
						} else {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							image = image.append(slipFieldValue + ": " + value
									+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
						}
					}
				}
				fieldValue = null;
			}

			image = image.append(getCommand(printerCmd.get("LINE_FEED_4"))
					+ getCommand(printerCmd.get("COLOR_DEFAULT")) + getCommand(printerCmd.get("CENTER"))
					+ "*****" + getCommand(printerCmd.get("LINE_FEED_4")));
			// log.info("image :" + image);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
		retImage = image.toString();
		if (log.isInfoEnabled()) {
			log.info("SLIP Image: " + retImage);
		}
		return retImage;
	}

	/**
	 * Method to build the Slip image that will be printed on the slip for a
	 * Laser Jet printer
	 * 
	 * @param dto
	 * @param slipType
	 * @param slipI18NMap
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String getLaserPrinterImage(Object dto, String slipType, Map<String, String> slipI18NMap,
			Long jpHitAmount, String langSelectedToProcess, Integer siteNumber) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {

		String method = IKeypadProcessConstants.EMPTY_STRING;
		String value = IKeypadProcessConstants.EMPTY_STRING;
		StringBuffer image = new StringBuffer(IKeypadProcessConstants.EMPTY_STRING);
		image.append("\r");
		image.append("\t\t\t");
		String currencySymbol = JackpotUtil.getSiteParamValue(
				IKeypadProcessConstants.CURRENCY_SYMBOL, siteNumber);
		String printerName = IKeypadProcessConstants.LASER_JET_PRINTER_SUBSTRING;
		String retImage = IKeypadProcessConstants.EMPTY_STRING;

		Class dtoClass = null;
		String slipFieldValue = IKeypadProcessConstants.EMPTY_STRING;
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;

		InputStream inStream = null;
		try {
			inStream = SlipImageFactory.class
					.getResourceAsStream(IKeypadProcessConstants.SLIP_SCHEMA_XML_FILE_PATH_KEYPAD);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(inStream);
			Element slips = doc.getRootElement();
			List slipLst = slips.getChildren();
			Element e = null;
			PrinterCommandParser pc = new PrinterCommandParser();
			// sending of schemaContents[0] is not required below.
			Map<String, String> printerCmd = pc.getCommandList(printerName);
			for (int ii = 0; ii < slipLst.size(); ii++) {
				e = (Element) slipLst.get(ii);
				if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {
					break;
				}
			}
			
			List fieldList = e.getChildren();
			PrintDTO printDTO = (PrintDTO)dto;
			for (int k = 0; k < fieldList.size(); k++) {
				Element fieldElement = (Element) fieldList.get(k);
				String appendStr = "";
				String newLine = "\r\n";
				if( fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("sameline")){//These are tags added for GNBX
					int spaceLen	=	Integer.parseInt(fieldElement.getAttributeValue("spaceBtwFields").toString());
					int i = 0;
					while(i < spaceLen) {
						appendStr = appendStr +" ";
						i++;
					}
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_1")) {
					appendStr = newLine;
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_2"))  {
					appendStr = newLine+ "   " + newLine;
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_3")) {
					appendStr = newLine + "   " + newLine+ "   " + newLine;
				} else if (fieldElement.getAttributeValue("linefeed").equalsIgnoreCase("LINE_FEED_4")) {
					appendStr = newLine + "   " + newLine+ "   " + newLine+ "   " + newLine;
				}
				
				if (fieldElement.getAttributeValue("display").equalsIgnoreCase("TRUE")) {
					method = "get" + fieldElement.getName();
					try {
						getDTOMethod = dtoClass.getMethod(method, new Class[] {});
						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}
					if (fieldValue != null && !fieldValue.equals("")) {
						value = fieldValue.toString();
					} else {
						if (fieldElement.getAttributeValue("printKeyOrData") != null
								&& fieldElement.getAttributeValue("printKeyOrData").equalsIgnoreCase("key"))
							value = "";
						else
							value = "_____________";
					}
					if (k == 0) {
						image.append(value + "\r\n");
					} else if((fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("CasinoName")))){
						String header = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						image.append(value);
						image.append(appendStr);
					} else if (slipType.equalsIgnoreCase(IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE)
							&& k == 1) {
						String header = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
						image.append(header);
						image.append(appendStr);
					} else if (fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("EmptyRow"))) {
						int noOfRows = 0;
						try {
							noOfRows = (fieldElement.getAttributeValue("noOfRows") != null) ? Integer
									.parseInt(fieldElement.getAttributeValue("noOfRows")) : 0;
						} catch (NumberFormatException badNum) {
							log.error("Invalid number of rows in emptyrow tag, check slipschema.xml");
						}
						for (int i = 0; i < noOfRows; i++) {
							appendStr += "\r\n ";
						}
						appendStr = appendStr.substring(0, appendStr.length() - 1);
						image.append(appendStr);
					} else if (fieldElement != null && fieldElement.getName().equalsIgnoreCase("Barcode")) {
						if (log.isInfoEnabled()) {
							log.info("Keypad print barcode enabled.");
						}
						try {
							String methodNameToSetBarcode = "set" + fieldElement.getName().trim();
							Method methodToSetBarcode = dtoClass.getMethod(methodNameToSetBarcode,
									new Class[] { String.class });
							methodToSetBarcode.invoke(dto, new String(fieldElement
									.getAttributeValue("display")));

						} catch (Exception exc) {
						}
					} else if (fieldElement != null
							&& fieldElement.getName().equalsIgnoreCase("AmountInWriting")) {
						if (log.isInfoEnabled()) {
							log.info("Keypad words for amount printing.");
						}
						try {
							if (jpHitAmount != null) {
								value = KeypadUtil.convertAmount(jpHitAmount.longValue(),
										langSelectedToProcess);
								if (log.isInfoEnabled()) {
									log.info("Amount in words :" + value);
								}
							} else {
								value = "_____________";
								if (log.isInfoEnabled()) {
									log.info("Amount in words cannot be processed since JP hit amount field is null.");
								}
							}
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							image = image.append(KeypadUtil.formatField(slipFieldValue, 0)+ ": " + value);
							image.append(appendStr);
						} catch (Exception excp) {

						}
					}else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpStateTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
								System.out.println("Value :"+printDTO.getHpjpAmount());
						slipFieldValue = slipFieldValue	+ "(" +printDTO.getHpjpAmount() + " * " + "0.0" + "%)"; 		
						image = image.append(slipFieldValue + ": " + currencySymbol + "0.00"
								+ appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpFederalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
								slipFieldValue = slipFieldValue	+ "(" +printDTO.getHpjpAmount() + " * " + "0.0" + "%)";	
								image = image.append(slipFieldValue + ": " + currencySymbol + "0.00"
										+ appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpMunicipalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
								slipFieldValue = slipFieldValue	+ "(" +printDTO.getHpjpAmount() + " * " + "0.0" + "%)";		
								image = image.append(slipFieldValue + ": " + currencySymbol + "0.00"
										+ appendStr);
					}  else if(fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("AccountType")) {
						if(value != null) {
							value = ExternalizationUtil.getInstance().getExternalizationValue(
									value, langSelectedToProcess);
							if(value == null) {
								value = "_____________";
							}
						} 
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
						image.append(KeypadUtil.formatField(slipFieldValue, 0) + ": " + value);
						image.append(appendStr);						
					} else if (slipType.equalsIgnoreCase(IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE)
							&& JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.ENABLE_JACKPOT_MULTI_RANGE_SIGN, siteNumber)
									.equalsIgnoreCase("yes")) {
						if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("preparer"))) {

							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							image = image.append(KeypadUtil.formatField(slipFieldValue, 0) + ": " + value);
							image.append("\r\n");
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("supervisor"))) {

							boolean printValue = false;
							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) >= 2) {

								if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_1,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_2, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 2 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_2,
														siteNumber)))) {

									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_2,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_3, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 3 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 4 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 5 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
														siteNumber)))) {
									printValue = true;
								}

								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(KeypadUtil.formatField(slipFieldValue,0)
											+ ": "
											+ value);
									image.append("\r\n");
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("manager"))) {

							boolean printValue = false;
							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) >= 3) {

								if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_2,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_3, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 3 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 4 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 5 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
														siteNumber)))) {
									printValue = true;
								}

								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(KeypadUtil.formatField(slipFieldValue,0)
											+ ": "
											+ value);
									image.append("\r\n");
									
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("executiveOne"))) {

							boolean printValue = false;
							if (new Long(JackpotUtil.getSiteParamValue(
									IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) >= 4) {

								if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_3,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 4 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
														siteNumber)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4,
												siteNumber)) && ConversionUtil.centsToDollar(jpHitAmount) <= new Double(
														JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber)))
										|| (new Long(JackpotUtil.getSiteParamValue(
												IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS,
												siteNumber)) == 5 && ConversionUtil.centsToDollar(jpHitAmount) > new Double(
														JackpotUtil.getSiteParamValue(
														IKeypadProcessConstants.MULTI_RANGE_SIGN_5,
														siteNumber)))) {
									printValue = true;
								}

								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(KeypadUtil.formatField(slipFieldValue,0)
											+ ": "
											+ value);
									image.append("\r\n");
									printValue = false;
								}

							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("executiveTwo"))) {

							if (new Long(JackpotUtil.getSiteParamValue(IKeypadProcessConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS, siteNumber)) == 5
									&& ((ConversionUtil.centsToDollar(jpHitAmount) > new Double(
											JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_4, siteNumber)) && ConversionUtil
											.centsToDollar(jpHitAmount) <= new Double(
													JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber))) || (ConversionUtil
											.centsToDollar(jpHitAmount) > new Double(
													JackpotUtil.getSiteParamValue(IKeypadProcessConstants.MULTI_RANGE_SIGN_5, siteNumber))))) {

								slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
										.get(fieldElement.getAttributeValue("key"))
										: fieldElement.getAttributeValue("key");
								image = image.append(KeypadUtil.formatField(slipFieldValue,0)
										+ ": "
										+ value);
								image.append("\r\n");
							}
						} else {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							if (fieldElement.getAttributeValue("printKeyOrData") != null
									&& fieldElement.getAttributeValue("printKeyOrData").equalsIgnoreCase("key")) {
								image = image.append(KeypadUtil.formatField(slipFieldValue, 0) + " " + value);
							} else {
								image = image.append(KeypadUtil.formatField(slipFieldValue, 0) + ": " + value);
							}
							image.append(appendStr);
						}
					} else {
						log.debug("ENABLE_JACKPOT_MULTI_RANGE_SIGN is no");
						if (slipType.equalsIgnoreCase(IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE)
								&& (fieldElement.getName() != null)
								&& (fieldElement.getName().equalsIgnoreCase("preparer")
										|| fieldElement.getName().equalsIgnoreCase("supervisor")
										|| fieldElement.getName().equalsIgnoreCase("manager")
										|| fieldElement.getName().equalsIgnoreCase("executiveOne") || fieldElement
										.getName().equalsIgnoreCase("executiveTwo"))) {
							// do not print it on the slip
							log.debug(fieldElement.getName() + " do not print it on the slip");
						} else {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							if (fieldElement.getAttributeValue("printKeyOrData") != null
									&& fieldElement.getAttributeValue("printKeyOrData").equalsIgnoreCase("Data")) {
								image = image.append(KeypadUtil.formatField(slipFieldValue, 0) + " " + value);
							} else {
								image = image.append(KeypadUtil.formatField(slipFieldValue, 0) + ": " + value);
							}
							image.append(appendStr);
						}
					}
				}
				fieldValue = null;			
			}
		}  catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
		retImage = image.toString();
		if (log.isInfoEnabled()) {
			log.info("SLIP Image: " + retImage);
		}
		return retImage;
	}

	/**
	 * Method to build the Slip image that will be printed on the slip for a
	 * Laser Jet printer
	 * 
	 * @param dto
	 * @param slipType
	 * @param slipI18NMap
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public String getCheckLaserPrinterImage(Object dto, String slipType,
			Map<String, String> slipI18NMap, Long jpNetAmount, String langSelectedToProcess) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		String method = IKeypadProcessConstants.EMPTY_STRING;
		String value = IKeypadProcessConstants.EMPTY_STRING;
		StringBuffer image = new StringBuffer(IKeypadProcessConstants.EMPTY_STRING);
		image.append(IKeypadProcessConstants.NEW_LINE_FEED);
		image.append(KeypadUtil.getLabelValue(IKeypadProcessConstants.CHECK_PRINT_JACKPOT_VALID_DATE_STATIC, langSelectedToProcess));
		image.append(IKeypadProcessConstants.NEW_LINE_FEED + IKeypadProcessConstants.NEW_LINE_FEED);

		String retImage = IKeypadProcessConstants.EMPTY_STRING;

		Class dtoClass = null;
		String slipFieldValue = IKeypadProcessConstants.EMPTY_STRING;
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;
		try {
			InputStream inStream = SlipImageFactory.class.getResourceAsStream(IKeypadProcessConstants.SLIP_SCHEMA_XML_FILE_PATH_KEYPAD);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(inStream);
			Element slips = doc.getRootElement();
			List slipLst = slips.getChildren();
			Element e = null;
			for (int ii = 0; ii < slipLst.size(); ii++) {
				e = (Element) slipLst.get(ii);
				if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {
					break;
				}
			}
			List fieldList = e.getChildren();
			
			String textSignatute = KeypadUtil.getSpacesForStringLength(5)
					+ slipI18NMap.get(IKeypadProcessConstants.CHECK_PRINT_JACKPOT_SIGN)
					+ KeypadUtil.getSpacesForStringLength(10);
			String idNo = KeypadUtil.getSpacesForStringLength(2)
					+ slipI18NMap.get(IKeypadProcessConstants.CHECK_PRINT_JACKPOT_ID_NO)
					+ KeypadUtil.getSpacesForStringLength(2);
			String stamp = KeypadUtil.getSpacesForStringLength(2)
					+ slipI18NMap.get(IKeypadProcessConstants.CHECK_PRINT_JACKPOT_STAMP)
					+ KeypadUtil.getSpacesForStringLength(2);
			String signatureValue = KeypadUtil.getUnderLineCharForStringLength(textSignatute.length())
					+ IKeypadProcessConstants.NEW_TAB_FEED + KeypadUtil.getUnderLineCharForStringLength(idNo.length())
					+ IKeypadProcessConstants.NEW_TAB_FEED + KeypadUtil.getUnderLineCharForStringLength(stamp.length())
					+ IKeypadProcessConstants.NEW_LINE_FEED;
			String signatureText = textSignatute + IKeypadProcessConstants.NEW_TAB_FEED + idNo
					+ IKeypadProcessConstants.NEW_TAB_FEED + stamp + IKeypadProcessConstants.NEW_LINE_FEED;
			String custSignatureText = IKeypadProcessConstants.EMPTY_STRING;
			String custSignSpace = IKeypadProcessConstants.EMPTY_STRING;
			String receiveText = IKeypadProcessConstants.EMPTY_STRING;
			
			for (int k = 0; k < fieldList.size(); k++) {
				Element fieldElement = (Element) fieldList.get(k);
				method = "get" + fieldElement.getName();
				try {
					getDTOMethod = dtoClass.getMethod(method, new Class[] {});
					fieldValue = getDTOMethod.invoke(dto);
				} catch (Exception e1) {

				}

				if (fieldValue != null && !fieldValue.equals(IKeypadProcessConstants.EMPTY_STRING)) {
					value = fieldValue.toString();
				} else {
					value = KeypadUtil.getUnderLineCharForStringLength(15);
				}
				if (fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("AmountInWriting"))) {
					if(log.isDebugEnabled()) {
						log.debug("Amt in writing - jackpotNetAmt: " + jpNetAmount);
					}
					value = KeypadUtil.convertAmount(jpNetAmount, langSelectedToProcess);
					if(log.isDebugEnabled()) {
						log.debug("value: " + value);
					}
					slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
					image = image.append(KeypadUtil.formatField(slipFieldValue, 0) + ": " + value);
					image.append(IKeypadProcessConstants.NEW_LINE_FEED);
				} else if (fieldElement.getName() != null
						&& fieldElement.getName().equalsIgnoreCase("Signature1")) {
					image = image.append(signatureValue + signatureText);
				} else if (fieldElement.getName() != null
						&& fieldElement.getName().equalsIgnoreCase("Signature2")) {		
					image = image.append(signatureValue + signatureText);
				} else if (fieldElement.getName() != null
						&& fieldElement.getName().equalsIgnoreCase("Signature3")) {
					image = image.append(signatureValue + signatureText);
				} else if (fieldElement.getName() != null
						&& fieldElement.getName().equalsIgnoreCase("CustomerSignature")) {
					custSignatureText = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
					custSignSpace = KeypadUtil.getSpacesForStringLength(custSignatureText.length());						
				} else if(fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("Received")) {
					receiveText = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
					receiveText = KeypadUtil.getSpacesForStringLength(2) + receiveText;
				} else if(fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("ReceiptCash")) {
					String receiptCashText = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
					receiptCashText = KeypadUtil.getSpacesForStringLength(2) + receiptCashText;
					// CUSTOMER SIGNATURE LINE
					image.append(custSignatureText + IKeypadProcessConstants.NEW_TAB_FEED
							+ KeypadUtil.getUnderLineCharForStringLength(receiveText.length())
							+ KeypadUtil.getUnderLineCharForStringLength(5)
							+ IKeypadProcessConstants.NEW_TAB_FEED
							+ KeypadUtil.getUnderLineCharForStringLength(receiptCashText.length())
							+ KeypadUtil.getUnderLineCharForStringLength(5)
							+ IKeypadProcessConstants.NEW_LINE_FEED);
					// RECEIVED AND RECEIPT CASH TEXT LINE
					image.append(custSignSpace + IKeypadProcessConstants.NEW_TAB_FEED + receiveText
							+ KeypadUtil.getSpacesForStringLength(5) + IKeypadProcessConstants.NEW_TAB_FEED
							+ receiptCashText + KeypadUtil.getSpacesForStringLength(5)
							+ IKeypadProcessConstants.NEW_LINE_FEED);
				} else if (fieldElement.getName() != null
						&& fieldElement.getName().equalsIgnoreCase("Dummy")) {
					image.append(IKeypadProcessConstants.NEW_LINE_FEED);
				} else if (fieldElement.getName() != null
						&& (fieldElement.getName().equalsIgnoreCase("Signature")
								|| fieldElement.getName().equalsIgnoreCase("IdNo") || fieldElement
								.getName().equalsIgnoreCase("Stamp"))) {						
					// Dont do anything						
				} else if (fieldElement != null && fieldElement.getName().equalsIgnoreCase("Barcode")) {
					if (log.isInfoEnabled()) {
						log.info("Keypad print barcode enabled.");
					}
					try {
						String methodNameToSetBarcode = "set" + fieldElement.getName().trim();
						Method methodToSetBarcode = dtoClass.getMethod(methodNameToSetBarcode,
								new Class[] { String.class });
						methodToSetBarcode.invoke(dto, new String(fieldElement
								.getAttributeValue("display")));

					} catch (Exception exc) {

					}
				} else {
					String header = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
					image.append(header + " : ");
					image.append(value + IKeypadProcessConstants.NEW_LINE_FEED);
				}
			fieldValue = null;
			}

		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.debug("Exception in getLaserPrinterImage: ", e);
			}
		}
		retImage = image.toString();
		if(log.isInfoEnabled()) {
			log.info("SLIP Image: " + retImage);
		}
		return retImage;
	}
	/*
	public String buildCheckSlipImage(Object dto, String slipType, Map<String, String> slipI18NMap, 
			Long jpHitAmountToProcess, String languageSelectedToProcess, Integer siteNumber) throws Exception {
		return null;
	}
	*/
	/**
	 * Converts String as printer command
	 * 
	 * @param commandStr
	 *            Printer Command String
	 * @return Converted printer command
	 * @throws Exception
	 */
	public String getCommand(String commandStr) throws Exception {
		String retString = IKeypadProcessConstants.EMPTY_STRING;
		if (commandStr != null) {
			String sa[] = {};
			sa = commandStr.split(",");

			int ia[] = new int[sa.length];

			for (int i = 0; i < sa.length; i++) {
				ia[i] = Integer.parseInt(sa[i].trim());
				retString = retString + (char) ia[i];
			}
		}
		return retString;
	}
}
