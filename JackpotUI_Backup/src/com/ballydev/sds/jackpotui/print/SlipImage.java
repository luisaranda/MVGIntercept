/*****************************************************************************
 * $Id: SlipImage.java,v 1.36.6.3, 2013-10-16 08:03:24Z, SDS12.3.3 Checkin User$
 * $Date: 10/16/2013 3:03:24 AM$
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

package com.ballydev.sds.jackpotui.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.PadderUtil;
import com.ballydev.sds.jackpotui.util.PrintDTO;
import com.ballydev.sds.jackpotui.util.VoidReportDTO;
import com.ballydev.sds.jackpotui.util.XMLClientUtil;

/**
 * This class builds slip image whith printer commands
 * 
 * @author Govindharaju Mohanasundaram email:GMohanasundaram@ballytech.com
 * @version $Revision: 41$
 */

public class SlipImage {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * Format length const for the Void Report
	 */
	private int VOID_REPORT_FORMAT_LENGTH = 11;

	/**
	 * Builds slip image
	 * 
	 * @param dto
	 *            The dto to be used to build slip image
	 * 
	 * @param slipType
	 *            Type of the slip image to be built
	 * 
	 * @return slip image
	 * 
	 * @throws JackpotEngineServiceException
	 *             if there is an issue
	 * 
	 */
	public SlipImage() {

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
			long hpjpAmount, long jpNetAmount) throws Exception {
		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");
		String printerName = "EPSON";
		/**
		 * PRINTER NAME - the commands below can be used only by EPSON 40 column
		 * printers
		 */
		String retImage = "";

		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;

		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});
			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			if(log.isInfoEnabled()) {
				log.info(schemaContents[i]);
			}
		}
		PrinterCommand pc = new PrinterCommand();
		Map<String, String> printerCmd = pc.getCommandList(printerName, schemaContents[0]);

		try {
			if(log.isInfoEnabled()) {
				log.info("ISiteConfigConstants.ENABLE_JACKPOT_MULTI_RANGE_SIGN: " + ISiteConfigConstants.ENABLE_JACKPOT_MULTI_RANGE_SIGN);
			}
			File slipschema = XMLClientUtil.stringToFile(schemaContents[1],
					IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);

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
						slipFieldValue = "";
					} else if (k == 1) {
						value = "";
						slipFieldValue=(slipI18NMap.get(fieldElement.getAttributeValue("key"))!=null)?slipI18NMap.get(fieldElement.getAttributeValue("key")):fieldElement.getAttributeValue("key");
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
			// getting State,Federal,Municipal tax rates from PrintDTO since they are not a part of Slip Schema
			PrintDTO printDTO =(PrintDTO)dto;
			double stateTaxRate = 0.00;
			double fedTaxRate =0.00;
			double municipalTaxRate =0.00;
			String hpjpAmnt = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts("0.00");
			if(printDTO.getJpStateTaxRate() != null){
				stateTaxRate =Double.parseDouble(printDTO.getJpStateTaxRate());
			}
			if(printDTO.getJpFederalTaxRate() != null){
				fedTaxRate =Double.parseDouble(printDTO.getJpFederalTaxRate());
			}
			if(printDTO.getJpMunicipalTaxRate() != null){
				municipalTaxRate =Double.parseDouble(printDTO.getJpMunicipalTaxRate());
			}
		
			if(printDTO.getHpjpAmount() != null){
				hpjpAmnt = printDTO.getHpjpAmount();
			}
			
			for (int k = 2; k < fieldList.size(); k++) {

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

					// ORIGINAL CODE
					if (fieldValue != null && !fieldValue.equals("")) {
						value = fieldValue.toString();
					} else {
						value = "_______________";
					}

					if (fieldElement.getName() != null
							&& (fieldElement.getName().equalsIgnoreCase("AmountInWriting"))) {

						log.debug("amt in writing - jackpotNetAmt: " + jpNetAmount);
						value = JackpotUIUtil.getAmountInWords(jpNetAmount);
						log.debug("value: " + value);
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) 
								? slipI18NMap.get(fieldElement.getAttributeValue("key"))
										: fieldElement.getAttributeValue("key");
								image = image.append(slipFieldValue + ": " + value
										+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));

					}else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpStateTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						slipFieldValue = slipFieldValue + "(" + hpjpAmnt + " * " + stateTaxRate + "%)"; 		
						image = image.append(slipFieldValue + ": " + value
								+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpFederalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						slipFieldValue = slipFieldValue + "(" + hpjpAmnt + " * " + fedTaxRate + "%)"; 		
						image = image.append(slipFieldValue + ": " + value
								+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpMunicipalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						slipFieldValue = slipFieldValue + "(" + hpjpAmnt + " * " + municipalTaxRate + "%)"; 		
						image = image.append(slipFieldValue + ": " + value
								+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));
					} else if (slipType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)
							&& MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.ENABLE_JACKPOT_MULTI_RANGE_SIGN).equalsIgnoreCase(
											"yes")) {

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
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) >= 2) {

								if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_1)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 2 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)))) {

									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 3 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 4 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))) {
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
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) >= 3) {

								if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 3 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 4 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))) {
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
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) >= 4) {

								if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 4 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
												MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))
												|| (new Long(MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5 && ConversionUtil
														.centsToDollar(hpjpAmount) > new Double(
																MainMenuController.jackpotSiteConfigParams
																.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))) {
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

							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5
									&& ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
											MainMenuController.jackpotSiteConfigParams
											.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
											.centsToDollar(hpjpAmount) <= new Double(
													MainMenuController.jackpotSiteConfigParams
													.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5))) || (ConversionUtil
															.centsToDollar(hpjpAmount) > new Double(
																	MainMenuController.jackpotSiteConfigParams
																	.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5))))) {

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

						if (slipType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)
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
					// slipFieldValue=(slipI18NMap.get(fieldElement.getAttributeValue("key"))!=null)?slipI18NMap.get(fieldElement.getAttributeValue("key")):fieldElement.getAttributeValue("key");
				}
				fieldValue = null;
			}
			image = image.append(getCommand(printerCmd.get("LINE_FEED_4"))
					+ getCommand(printerCmd.get("COLOR_DEFAULT")) + getCommand(printerCmd.get("CENTER"))
					+ "*****" + getCommand(printerCmd.get("LINE_FEED_4")));
			// log.info("image :" + image);
			slipschema.delete();

		} catch (Exception e) {

			e.printStackTrace();

		}
		retImage = image.toString();
		log.debug("retImage: " + retImage);
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
	public static String getLaserPrinterImage(Object dto, String slipType, Map<String, String> slipI18NMap,
			long hpjpAmount, long jpNetAmount) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");
		String retImage = "";
		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;
		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});
			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			log.info(schemaContents[i]);
		}
		try {
			File slipschema = XMLClientUtil.stringToFile(schemaContents[1], IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);
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
				
				
				
				// getting State,Federal,Municipal tax rates from PrintDTO since they are not a part of Slip Schema
				PrintDTO printDTO =(PrintDTO)dto;
				double stateTaxRate = 0.00;
				double fedTaxRate =0.00;
				double municipalTaxRate =0.00;
				String hpjpAmnt = MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts("0.00");
				if(printDTO.getJpStateTaxRate() != null){
					stateTaxRate =Double.parseDouble(printDTO.getJpStateTaxRate());
				}
				if(printDTO.getJpFederalTaxRate() != null){
					fedTaxRate =Double.parseDouble(printDTO.getJpFederalTaxRate());
				}
				if(printDTO.getJpMunicipalTaxRate() != null){
					municipalTaxRate =Double.parseDouble(printDTO.getJpMunicipalTaxRate());
				}
			
				if(printDTO.getHpjpAmount() != null){
					hpjpAmnt = printDTO.getHpjpAmount();
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
					if ((fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("CasinoName")))) {
						String header = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						image.append(value);
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
					} else if (fieldElement.getName() != null
							&& (fieldElement.getName().equalsIgnoreCase("AmountInWriting"))) {
						log.debug("amt in writing - jackpotNetAmt: " + jpNetAmount);
						value = JackpotUIUtil.getAmountInWords(jpNetAmount);
						log.debug("value: " + value);
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
						image.append(appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("AccountType")) {
						value = LabelLoader.getLabelValue(value);
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
						image.append(appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpStateTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						//slipFieldValue = LabelLoader.getLabelValue(slipFieldValue);	
						slipFieldValue = slipFieldValue + "(" + hpjpAmnt + " * " + stateTaxRate + "%)"; 		
						image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
						image.append(appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpFederalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						//slipFieldValue = LabelLoader.getLabelValue(slipFieldValue);		
						slipFieldValue = slipFieldValue + "(" + hpjpAmnt + " * " + fedTaxRate + "%)"; 		
						image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
						image.append(appendStr);
					} else if (fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("JpMunicipalTaxAmount")) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key")) : fieldElement.getAttributeValue("key");
						//slipFieldValue = LabelLoader.getLabelValue(slipFieldValue);
						slipFieldValue = slipFieldValue + "(" + hpjpAmnt + " * " + municipalTaxRate + "%)"; 		
						image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
						image.append(appendStr);
					}
					// CONDITION TO CHECK IF THE BARCODE FLAG IS ENABLED, THEN
					// SET THE JP FORM FIELD TO CREATE AND PRINT THE BARCODE
					else if (fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("Barcode"))) {
						MainMenuController.jackpotForm.setPrintBarcode(true);
					} else if (slipType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)
							&& MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.ENABLE_JACKPOT_MULTI_RANGE_SIGN).equalsIgnoreCase("yes")) {
						log.debug("ENABLE_JACKPOT_MULTI_RANGE_SIGN is yes");
						if (fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("preparer"))) {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							image = image.append(SlipUtil.formatField(slipFieldValue, 0) + " " + value);
							image.append(appendStr);
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("supervisor"))) {
							boolean printValue = false;
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) >= 2) {

								if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_1)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 2 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)))) {

									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 3 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 4 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))) {
									printValue = true;
								}
								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
									image.append(appendStr);
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("manager"))) {
							boolean printValue = false;
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) >= 3) {
								if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_2)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 3 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 4 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))) {
									printValue = true;
								}
								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
									image.append(appendStr);
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("executiveOne"))) {
							boolean printValue = false;
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) >= 4) {
								if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_3)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 4 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)))) {
									printValue = true;
								} else if ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
										.centsToDollar(hpjpAmount) <= new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))
										|| (new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5 && ConversionUtil
												.centsToDollar(hpjpAmount) > new Double(
												MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5)))) {
									printValue = true;
								}
								if (printValue) {
									slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
											.get(fieldElement.getAttributeValue("key"))
											: fieldElement.getAttributeValue("key");
									image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
									image.append(appendStr);
									printValue = false;
								}
							}
						} else if (fieldElement.getName() != null
								&& (fieldElement.getName().equalsIgnoreCase("executiveTwo"))) {

							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NO_OF_MULTI_RANGE_SIGN_LEVELS)) == 5
									&& ((ConversionUtil.centsToDollar(hpjpAmount) > new Double(
											MainMenuController.jackpotSiteConfigParams
													.get(ISiteConfigConstants.MULTI_RANGE_SIGN_4)) && ConversionUtil
											.centsToDollar(hpjpAmount) <= new Double(
											MainMenuController.jackpotSiteConfigParams
													.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5))) || (ConversionUtil
											.centsToDollar(hpjpAmount) > new Double(
											MainMenuController.jackpotSiteConfigParams
													.get(ISiteConfigConstants.MULTI_RANGE_SIGN_5))))) {

								slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
										.get(fieldElement.getAttributeValue("key"))
										: fieldElement.getAttributeValue("key");
								image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
								image.append(appendStr);

							}
						} else {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							if (fieldElement.getAttributeValue("printKeyOrData") != null
									&& fieldElement.getAttributeValue("printKeyOrData").equalsIgnoreCase("key")) {
								image = image.append(SlipUtil.formatField(slipFieldValue, 0) + " " + value);
							} else {
								image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
							}
							image.append(appendStr);
						}
					} else {
						log.debug("ENABLE_JACKPOT_MULTI_RANGE_SIGN is no");
						if (slipType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)
								&& (fieldElement.getName() != null)
								&& (fieldElement.getName().equalsIgnoreCase("preparer")
										|| fieldElement.getName().equalsIgnoreCase("supervisor")
										|| fieldElement.getName().equalsIgnoreCase("manager")
										|| fieldElement.getName().equalsIgnoreCase("executiveOne") || fieldElement
										.getName().equalsIgnoreCase("executiveTwo"))) {
							// do not print it on the slip
						} else {
							slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
									.get(fieldElement.getAttributeValue("key"))
									: fieldElement.getAttributeValue("key");
							if (fieldElement.getAttributeValue("printKeyOrData") != null
									&& fieldElement.getAttributeValue("printKeyOrData").equalsIgnoreCase("Data")) {
								image = image.append(SlipUtil.formatField(slipFieldValue, 0) + " " + value);
							} else {
								image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
							}
							image.append(appendStr);
						}
					}
				}
				fieldValue = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception in getLaserPrinterImage: ", e);
		}
		image.append("\r\n");// End of image spacing
		retImage = image.toString();
		log.info("SLIP Image: " + retImage);
		return retImage;

	}

	/**
	 * Builds Voided Jackpot Report Slip Details
	 * 
	 * 
	 * @param lstDTO
	 *            VoidReportDTO List
	 * @param i18nMap
	 *            Internationalization Key/Value pair map
	 * @return slip details
	 */
	public String getVoidJackpotReportSlip(List<VoidReportDTO> lstDTO, HashMap<String, String> i18nMap) {

		StringBuffer image = new StringBuffer("");

		image.append("\r\n");
		image.append("\t\t\t\t");
		String header = i18nMap.get(LabelKeyConstants.PRINT_JPVOID_HEADING) != null ? i18nMap
				.get(LabelKeyConstants.PRINT_JPVOID_HEADING) : LabelKeyConstants.PRINT_JPVOID_HEADING;
				image.append(header + "\r\n\r\n\r\n");
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_SEQ_NO),
						VOID_REPORT_FORMAT_LENGTH));
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_DATE),
						VOID_REPORT_FORMAT_LENGTH));
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_TIME),
						VOID_REPORT_FORMAT_LENGTH));
				// image.append("----------");
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_SLOT_NO),
						VOID_REPORT_FORMAT_LENGTH));
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_STAND_NO),
						VOID_REPORT_FORMAT_LENGTH));
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_DENOM),
						VOID_REPORT_FORMAT_LENGTH));
				image.append(SlipUtil.formatField(i18nMap.get(LabelKeyConstants.PRINT_JPVOID_AMOUNT),
						VOID_REPORT_FORMAT_LENGTH));
				image.append("\r\n\r\n");
				image.append("---------------------------------------------------------------------------");
				image.append("\r\n");
				if (lstDTO != null) {
					for (VoidReportDTO voidReportDTO : lstDTO) {

						image.append(SlipUtil.formatField(voidReportDTO.getSeq(), 11));
						image.append(SlipUtil.formatField(voidReportDTO.getDate(), 11));
						// image.append(SlipUtil.formatField(voidReportDTO.getTime(),11));
						image.append("    ");
						image.append(SlipUtil.formatField(voidReportDTO.getSlotNo(), 11));
						if (voidReportDTO.getStandNo() != null) {
							image.append(SlipUtil.formatField(voidReportDTO.getStandNo(), 11));
						} else {
							image.append(SlipUtil.formatField(null, 11));
						}
						if (voidReportDTO.getDenom() != null) {
							image.append(SlipUtil.formatField(voidReportDTO.getDenom(), 11));
						} else {
							image.append(SlipUtil.formatField(null, 11));
						}
						if (voidReportDTO.getAmount() != null) {
							image.append(SlipUtil.formatField(voidReportDTO.getAmount(), 11));
						} else {
							image.append(SlipUtil.formatField(null, 11));
						}
						image.append("\r\n\r\n");
					}
				}

				return image.toString();

	}

	public String buildSlipImageSlipRpt(Object dto, String slipType, Map<String, String> slipI18NMap)
			throws Exception {
		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");
		String printerName = "EPSON";
		/**
		 * PRINTER NAME - the commands below can be used only by EPSON 40 column
		 * printers
		 */
		String retImage = "";

		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;

		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});
			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			log.info(schemaContents[i]);
		}
		PrinterCommand pc = new PrinterCommand();
		Map<String, String> printerCmd = pc.getCommandList(printerName, schemaContents[0]);

		try {
			File slipschema = XMLClientUtil.stringToFile(schemaContents[1],
					IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);

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
						slipFieldValue = "";
					} else if (k == 1) {
						value = slipType;
						slipFieldValue = "";
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

				if (k == 7 || k == 8 || k == 9 || k == 10) {
					// DO NOT PRINT THE RPT TRANSACTION HISTORY IN THE SLIP
					// PRINTER SLIP
				} else {
					if (fieldElement.getAttributeValue("display").equalsIgnoreCase("TRUE")) {
						method = "get" + fieldElement.getName();
						image = image.append(getCommand(printerCmd.get(fieldElement
								.getAttributeValue("align")))
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
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
								image = image.append(slipFieldValue + ": " + value
										+ getCommand(printerCmd.get(fieldElement.getAttributeValue("linefeed"))));

					}
					fieldValue = null;
				}

			}
			image = image.append(getCommand(printerCmd.get("LINE_FEED_4"))
					+ getCommand(printerCmd.get("COLOR_DEFAULT")) + getCommand(printerCmd.get("CENTER"))
					+ "*****" + getCommand(printerCmd.get("LINE_FEED_4")));
			// log.info("image :" + image);
			slipschema.delete();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		retImage = image.toString();
		log.debug("retImage: " + retImage);
		return retImage;

	}

	/**
	 * Method for printing the Jackpot UI slip report on a Laser Printer
	 * 
	 * @param dto
	 * @param slipType
	 * @param slipI18NMap
	 * @param jpRptsDTOList
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String getLaserPrinterImageSlipRpt(Object dto, String slipType,
			Map<String, String> slipI18NMap, List<JackpotReportsDTO> jpRptsDTOList) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");

		final int REPORT_LASER_TRANS_REC_FORMAT_SPACE = 20;

		image.append("\r\n");
		// image.append("\t\t\t");

		String retImage = "";

		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();

		} catch (Exception e) {

			e.printStackTrace();

		}
		Method getDTOMethod;
		Object fieldValue = null;

		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});

			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			log.info(schemaContents[i]);

		}

		try {

			File slipschema = XMLClientUtil.stringToFile(schemaContents[1],
					IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);

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

			for (int k = 0; k < fieldList.size(); k++) {

				Element fieldElement = (Element) fieldList.get(k);

				if (fieldElement.getAttributeValue("display").equalsIgnoreCase("TRUE")) {

					method = "get" + fieldElement.getName();

					try {
						getDTOMethod = dtoClass.getMethod(method, new Class[] {});
						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}

					if (fieldValue != null)
						value = fieldValue.toString();
					else
						value = "_______________";

					if (k == 0) {

						image.append(value + "\r\n\r\n");

						/*
						 * String header = (slipI18NMap.get(fieldElement
						 * .getAttributeValue("key")) != null) ? slipI18NMap
						 * .get(fieldElement.getAttributeValue("key")) :
						 * fieldElement.getAttributeValue("key");
						 */
						// image.append(value + "\r\n\r\n");
						// }else if
						// (slipType.equalsIgnoreCase(IAppConstants.JACKPOT_PRINT_SLIP_TYPE)
						// && k == 1) {
					} else if (k == 1) {
						String header = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
								// image.append("\t "+header + "\r\n\n\n");
								image.append(header + "\r\n\r\n");
					} else if (k == 7 || k == 8 || k == 9 || k == 10) {
						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
								char c = '\u0020';
								image = image.append(PadderUtil.leftPad(slipFieldValue,
										REPORT_LASER_TRANS_REC_FORMAT_SPACE, c));
								// + "\t");

								// image.append("\r\n\r\n");
					} else {

						slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
								.get(fieldElement.getAttributeValue("key"))
								: fieldElement.getAttributeValue("key");
								image = image.append(SlipUtil.formatField(slipFieldValue, 0)

										+ ": " + value);
								image.append("\r\n\r\n");
					}

					// LOGIC FOR THE TRANSACTION HISTORY
					if (k == 10 && jpRptsDTOList != null) {
						image.append("\r\n\r\n");
						image
						.append("--------------------------------------------------------------------------------");
						image.append("\r\n");
						SortedMap<Long, JackpotReportsDTO> map = new TreeMap<Long, JackpotReportsDTO>();
						for (int i = 0; i < jpRptsDTOList.size(); i++) {
							long seq = jpRptsDTOList.get(i).getSequenceNo();
							if (!(map.containsKey(seq))) {
								map.put(seq, jpRptsDTOList.get(i));
							}
						}
						List<JackpotReportsDTO> jpRptsDTOListNew = new ArrayList<JackpotReportsDTO>();
						jpRptsDTOListNew.addAll(map.values());
						log.debug("------------------ list size after set: " + jpRptsDTOListNew.size());
						char c = '\u0020';
						for (JackpotReportsDTO jpRptsDTO : jpRptsDTOListNew) {
							image.append(PadderUtil.leftPad(Long.toString(jpRptsDTO.getSequenceNo()),
									REPORT_LASER_TRANS_REC_FORMAT_SPACE, c));
							image.append(PadderUtil.leftPad(jpRptsDTO.getSlotNo(),
									REPORT_LASER_TRANS_REC_FORMAT_SPACE, c));
							image.append(PadderUtil.leftPad(MainMenuController.jackpotForm
									.getSiteCurrencySymbol()
									+ ConversionUtil.centsToDollar(String.valueOf(jpRptsDTO.getHpjpAmt())),
									REPORT_LASER_TRANS_REC_FORMAT_SPACE, c));
							image.append(PadderUtil.leftPad(MainMenuController.jackpotForm
									.getSiteCurrencySymbol()
									+ ConversionUtil.centsToDollar(String.valueOf(jpRptsDTO.getNetAmt())),
									REPORT_LASER_TRANS_REC_FORMAT_SPACE, c));
							image.append("\r\n\r\n");
						}
					}
				}
				fieldValue = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		retImage = image.toString();
		log.info("SLIP Image: " + retImage);
		return retImage;

	}

	/**
	 * Converts String as printer command
	 * 
	 * @param commandStr
	 *            Printer Command String
	 * 
	 * @return Converted printer command
	 * @throws Exception
	 */
	public String getCommand(String commandStr) throws Exception {

		String retString = "";
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
	public static String getCheckLaserPrinterImage(Object dto, String slipType,
			Map<String, String> slipI18NMap, long hpjpAmount, long jpNetAmount, Date expiryDate) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");
		image.append(IAppConstants.NEW_LINE_FEED);
		if(expiryDate != null ) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			String expiryDateFormat = df.format(expiryDate);
			image.append(LabelLoader.getLabelValue(LabelKeyConstants.CHECK_PRINT_JACKPOT_VALID_DATE) + expiryDateFormat +")");
		} else {
			image.append(LabelLoader.getLabelValue(LabelKeyConstants.CHECK_PRINT_JACKPOT_VALID_DATE_STATIC));
		}
		image.append(IAppConstants.NEW_LINE_FEED + IAppConstants.NEW_LINE_FEED);

		String retImage = "";

		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		Method getDTOMethod;
		Object fieldValue = null;

		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});
			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			if(log.isInfoEnabled()) {
				log.info(schemaContents[i]);
			}
		}
		try {
			File slipschema = XMLClientUtil.stringToFile(schemaContents[1], IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);
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
			String textSignatute = SlipUtil.getSpacesForStringLength(5)
					+ slipI18NMap.get(LabelKeyConstants.CHECK_PRINT_JACKPOT_SIGN)
					+ SlipUtil.getSpacesForStringLength(10);
			String idNo = SlipUtil.getSpacesForStringLength(2)
					+ slipI18NMap.get(LabelKeyConstants.CHECK_PRINT_JACKPOT_ID_NO)
					+ SlipUtil.getSpacesForStringLength(2);
			String stamp = SlipUtil.getSpacesForStringLength(2)
					+ slipI18NMap.get(LabelKeyConstants.CHECK_PRINT_JACKPOT_STAMP)
					+ SlipUtil.getSpacesForStringLength(2);
			String signatureValue = SlipUtil.getUnderLineCharForStringLength(textSignatute.length())
					+ IAppConstants.NEW_TAB_FEED + SlipUtil.getUnderLineCharForStringLength(idNo.length())
					+ IAppConstants.NEW_TAB_FEED + SlipUtil.getUnderLineCharForStringLength(stamp.length())
					+ IAppConstants.NEW_LINE_FEED;
			String signatureText = textSignatute + IAppConstants.NEW_TAB_FEED + idNo
					+ IAppConstants.NEW_TAB_FEED + stamp + IAppConstants.NEW_LINE_FEED;
			String custSignatureText = IAppConstants.EMPTY_STRING;
			String custSignSpace = IAppConstants.EMPTY_STRING;
			String receiveText = IAppConstants.EMPTY_STRING;

			for (int k = 0; k < fieldList.size(); k++) {
				Element fieldElement = (Element) fieldList.get(k);
				method = "get" + fieldElement.getName();
				try {
					getDTOMethod = dtoClass.getMethod(method, new Class[] {});
					fieldValue = getDTOMethod.invoke(dto);
				} catch (Exception e1) {

				}

				if (fieldValue != null && !fieldValue.equals("")) {
					value = fieldValue.toString();
				} else {
					value = "_______________";
				}
				if (fieldElement.getName() != null && (fieldElement.getName().equalsIgnoreCase("AmountInWriting"))) {
					if(log.isDebugEnabled()) {
						log.debug("amt in writing - jackpotNetAmt: " + jpNetAmount);
					}
					value = JackpotUIUtil.getAmountInWords(jpNetAmount);
					if(log.isDebugEnabled()) {
						log.debug("value: " + value);
					}
					slipFieldValue = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
							image = image.append(SlipUtil.formatField(slipFieldValue, 0) + ": " + value);
							image.append(IAppConstants.NEW_LINE_FEED);
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
							custSignSpace = SlipUtil.getSpacesForStringLength(custSignatureText.length());	
				} else if(fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("Received")) {
					receiveText = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
							receiveText = SlipUtil.getSpacesForStringLength(2) + receiveText;
				} else if(fieldElement.getName() != null && fieldElement.getName().equalsIgnoreCase("ReceiptCash")) {
					String receiptCashText = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
							receiptCashText = SlipUtil.getSpacesForStringLength(2) + receiptCashText;
							// CUSTOMER SIGNATURE LINE
							image.append(custSignatureText + IAppConstants.NEW_TAB_FEED
									+ SlipUtil.getUnderLineCharForStringLength(receiveText.length())
									+ SlipUtil.getUnderLineCharForStringLength(5)
									+ IAppConstants.NEW_TAB_FEED
									+ SlipUtil.getUnderLineCharForStringLength(receiptCashText.length())
									+ SlipUtil.getUnderLineCharForStringLength(5)
									+ IAppConstants.NEW_LINE_FEED);
							// RECEIVED AND RECEIPT CASH TEXT LINE
							image.append(custSignSpace + IAppConstants.NEW_TAB_FEED + receiveText
									+ SlipUtil.getSpacesForStringLength(5) + IAppConstants.NEW_TAB_FEED
									+ receiptCashText + SlipUtil.getSpacesForStringLength(5)
									+ IAppConstants.NEW_LINE_FEED);
				} else if (fieldElement.getName() != null
						&& fieldElement.getName().equalsIgnoreCase("Dummy")) {
					image.append(IAppConstants.NEW_LINE_FEED);
				} else if (fieldElement.getName() != null
						&& (fieldElement.getName().equalsIgnoreCase("Signature")
								|| fieldElement.getName().equalsIgnoreCase("IdNo") || fieldElement
								.getName().equalsIgnoreCase("Stamp"))) {
					// Dont do anything

				} else if (fieldElement.getName() != null
						&& (fieldElement.getName().equalsIgnoreCase("Barcode"))) {
					// CONDITION TO CHECK IF THE BARCODE FLAG IS ENABLED, THEN
					// SET THE JP FORM FIELD TO CREATE AND PRINT THE BARCODE
					MainMenuController.jackpotForm.setPrintBarcode(true);
				} else {
					String header = (slipI18NMap.get(fieldElement.getAttributeValue("key")) != null) ? slipI18NMap
							.get(fieldElement.getAttributeValue("key"))
							: fieldElement.getAttributeValue("key");
							image.append(header + ": ");
							image.append(value + IAppConstants.NEW_LINE_FEED);
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

}
