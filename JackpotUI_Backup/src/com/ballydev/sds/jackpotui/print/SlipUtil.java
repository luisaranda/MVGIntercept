/*****************************************************************************
 * $Id: SlipUtil.java,v 1.43.6.3, 2013-10-16 08:03:24Z, SDS12.3.3 Checkin User$
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.DateUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.PadderUtil;
import com.ballydev.sds.jackpotui.util.PrintDTO;
import com.ballydev.sds.jackpotui.util.XMLClientUtil;

/**
 * Slip Utility Class.
 * 
 * @author anantharajr
 * @version $Revision: 48$
 */
public class SlipUtil implements ILookupTableConstants{

	/**
	 * Jackpot slip map
	 */
	public static Map jackpotSlipMap;
	
	/**
	 * Check slip map
	 */
	public static Map checkSlipMap;

	/**
	 * Void slip map
	 */
	public static Map voidSlipMap;

	/**
	 * Report slip map
	 */
	public static Map reportSlipMap;

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
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

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
			log.error("Exception in getSlipKeyValues: ",e);
		}
		for (int i = 0; i < keyList.size(); i++) {
			slipKeyMap.put(keyList.get(i), LabelLoader.getLabelValue(keyList
					.get(i)));
			// slipKeyMap.put(keyList.get(i), keyList.get(i));
		}
		return slipKeyMap;
	}
	
	/**
	 * Method to get the key values for the schema file passwd
	 * @param schemaFile
	 * @param slipType
	 * @return
	 * @author vsubha
	 */
	public static HashMap<String, String> getSlipKeyValues(String schemaFile, String slipType) {
		HashMap<String, String> slipKeyMap = new HashMap<String, String>();
		List<String> keyList = null;
		try {
			keyList = getSlipKeys(slipType, schemaFile);
		} catch (Exception e) {
			log.error("Exception in getSlipKeyValues: ", e);
		}
		for (int i = 0; i < keyList.size(); i++) {
			slipKeyMap.put(keyList.get(i), LabelLoader.getLabelValue(keyList.get(i)));
			// slipKeyMap.put(keyList.get(i), keyList.get(i));
		}
		return slipKeyMap;
	}

	public static void getSlipSchema() {
		//List<String> schemaFileList = null;
		// File file =null;
		
		String[] slipSchemaArray = null;
		try {
			slipSchemaArray = JackpotServiceLocator.getService().getJackpotXMLInfo();
			/*
			 * 
			 * COMMENTED LIST OF SLIP SCHEMA
			 */
			
			//schemaFileList = JackpotServiceLocator.getService().getJackpotXMLInfo();
			// log.info("The siz of list is "+schemaFileList.size());
			
			if(slipSchemaArray!=null) {
				printerSchemaAsString = slipSchemaArray[0];
				slipsSchemaAsString = slipSchemaArray[1];
			}
			/*
			 * \
			 * COMMENTED LIST OF SLIP SCHEMA
			 */
			
			/*if(schemaFileList!=null) {
				printerSchemaAsString = schemaFileList.get(0);
				slipsSchemaAsString = schemaFileList.get(1);
			}*/			
			if(log.isInfoEnabled()) {
				log.info("SSF :" + slipsSchemaAsString);
			}

		} catch (JackpotEngineServiceException e) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, e.getMessage());
			log.error("Exception getJackpotXMLInfo web method in SlipUtil class",e);
		}catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
			log.error("SERVICE_DOWN",e2);
			return;
		}
		try {
			// schemaFiles[0] =
			// XMLClientUtil.stringToFile(schemaFileList.get(0),"printerSchema");
			
			/*
			 * 
			 * COMMENTED LIST OF SLIP SCHEMA
			 */
			
			/*if(schemaFileList!=null) {
				XMLClientUtil.stringToFile(schemaFileList.get(1), IAppConstants.SLIP_SCHEMA_FILE_KEY);
			}*/	
			if(slipSchemaArray!=null) {
				XMLClientUtil.stringToFile(slipSchemaArray[1], IAppConstants.SLIP_SCHEMA_FILE_KEY);
			}
			voidSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.VOID_PRINT_SLIP_TYPE);
			if(log.isInfoEnabled()) {
				log.info("The size of voidSlipMap: " + voidSlipMap.size());
			}
			jackpotSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.JACKPOT_PRINT_SLIP_TYPE);
			if(log.isInfoEnabled()) {
				log.info("The size of jackpotSlipMap: " + jackpotSlipMap.size());
			}
			checkSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.CHECK_PRINT_SLIP_TYPE);
			if(log.isInfoEnabled()) {
				log.info("The size of checkSlipMap : " + checkSlipMap.size());
			}
			reportSlipMap = SlipUtil.getSlipKeyValues(IAppConstants.REPORT_PRINT_SLIP_TYPE);
			if(log.isInfoEnabled()) {
				log.info("The size of reportSlipMap: " + reportSlipMap.size());
			}
		} catch (IOException e) {			
			log.error("IOException in getSlipSchema: ",e);
		}
	}

	/**
	 * Formats the given String
	 * @param fieldStr
	 * @return
	 */
	public static String formatField(String fieldStr, int formatLength) {

		StringBuffer retStr = null;
		if(fieldStr!=null) {
			retStr  = new StringBuffer(fieldStr);
			int length = fieldStr.length();
			if (length < formatLength) {
				for (int i = length; i < formatLength; i++) {

					retStr.append(" ");
				}
			}
			return retStr.toString();
		}else{
			return "";
		}
	}

	/**
	 * Method to set the field values in JackpotDTo to the PrintDTO fields
	 * @param jackpotDTO
	 * @return
	 */
	public static PrintDTO setPrintDTOValues(JackpotDTO jackpotDTO) {
		PrintDTO printDTO = new PrintDTO();
		//printDTO.setAccountPeriod()
		
		if (jackpotDTO.getHpjpAmount() != 0) {
			printDTO.setActualAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
							.getHpjpAmount())));
		}	
		if(jackpotDTO.getAreaLongName()!=null) {
			printDTO.setArea(jackpotDTO.getAreaLongName());
		}
		if(jackpotDTO.getAssetConfigLocation()!=null) {
			printDTO.setAssetConfigLocation(jackpotDTO.getAssetConfigLocation());
		}
		if(jackpotDTO.getAssetConfigNumber()!=null) {
			printDTO.setAssetConfigNumber(StringUtil.trimAcnfNo(jackpotDTO.getAssetConfigNumber()));	
		}	
		//printDTO.setAssociatedPlayerCard(associatedPlayerCard)
		if(jackpotDTO.getAuthEmployeeId1()!=null && jackpotDTO.getAuthEmployeeId1().length()>0) {
			printDTO.setAuthEmpIdOne(PadderUtil.lPad(jackpotDTO.getAuthEmployeeId1(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			SessionUtility sessionUtil = new SessionUtility();
			
			UserDTO userDTO = null;
			try {
				userDTO = sessionUtil.getUserDetails(jackpotDTO.getAuthEmployeeId1(), MainMenuController.jackpotForm.getSiteId());
				
				printDTO.setAuthEmpNameOne(userDTO.getFirstName()+ " "+ userDTO.getLastName());				
			} catch (Exception e) {				
				log.error("Unable to get the First Auth Emp Name", e);
			}
		}
	
		if(jackpotDTO.getAuthEmployeeId2()!=null && jackpotDTO.getAuthEmployeeId2().length()>0) {
			printDTO.setAuthEmpIdTwo(PadderUtil.lPad(jackpotDTO.getAuthEmployeeId2(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			SessionUtility sessionUtil = new SessionUtility();
			
			UserDTO userDTO = null;
			try {
				userDTO = sessionUtil.getUserDetails(jackpotDTO.getAuthEmployeeId2(), MainMenuController.jackpotForm.getSiteId());
				
				printDTO.setAuthEmpNameTwo(userDTO.getFirstName()+ " "+ userDTO.getLastName());				
			} catch (Exception e) {				
				log.error("Unable to get the Second Auth Emp Name", e);
			}
		}
		//printDTO.setBarcode(barcode)
		//printDTO.setCardInDate(cardInDate)
				
		printDTO.setCasinoName(MainMenuController.jackpotForm.getSiteLongName());		
		
		if(jackpotDTO.getCoinsPlayed()!=0) {
			printDTO.setCoinsPlayed(jackpotDTO.getCoinsPlayed());
		}
		if (jackpotDTO.getHpjpAmountRounded() != 0) {
			printDTO.setHpjpAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
							.getHpjpAmountRounded())));
		} else if (jackpotDTO.getHpjpAmount() != 0) {
			printDTO.setHpjpAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
							.getHpjpAmount())));
		}
		if (jackpotDTO.getJackpotNetAmount() != 0) {
			printDTO.setJackpotNetAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
							.getJackpotNetAmount())));
		}
			
		printDTO.setJackpotTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
				+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
						.getTaxAmount())));
		
		
		// setting the individual tax amounts and tax rates in print DTO ST:0.0^0.0|FT:0.0^0.0|MT:0.0^0.0
		if(jackpotDTO.getTaxRateAmount() != null && !jackpotDTO.getTaxRateAmount().isEmpty()){
			String taxDetailsArray[] = jackpotDTO.getTaxRateAmount().split("\\|");
			String stateTaxArray[] = taxDetailsArray[0].substring(3).split("\\^");
			String fedTaxArray[] = taxDetailsArray[1].substring(3).split("\\^");
			String municipalTaxArray[] = taxDetailsArray[2].substring(3).split("\\^");
			printDTO.setJpStateTaxRate(stateTaxArray[0]);
			printDTO.setJpStateTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(stateTaxArray[1]));
			printDTO.setJpFederalTaxRate(fedTaxArray[0]);
			printDTO.setJpFederalTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(fedTaxArray[1]));
			printDTO.setJpMunicipalTaxRate(municipalTaxArray[0]);
			printDTO.setJpMunicipalTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(municipalTaxArray[1]));
		} else {
			printDTO.setJpStateTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(new Double(0)));
			printDTO.setJpFederalTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(new Double(0)));
			printDTO.setJpMunicipalTaxAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol() + JackpotUIUtil.getFormattedAmounts(new Double(0)));
		}
			
			
		/** TO BE DONE AFTER CONFIRMATION */
		//printDTO.setJackpotType(jackpotDTO.getJackpotId());
		
		//printDTO.setJpDescription(jpDescription)
		if(jackpotDTO.getAssetConfNumberUsed()!=null) {
			printDTO.setKeyboard(jackpotDTO.getAssetConfNumberUsed());
		}
		
		printDTO.setOriginalAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
				+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
						.getOriginalAmount())));
		
		if (jackpotDTO.getPayline() != null) {
			printDTO.setPayline(jackpotDTO.getPayline());
		}
		if (jackpotDTO.getPrinterUsed() != null) {
			printDTO.setPKeyboard(jackpotDTO.getPrinterUsed());
		}
		if (jackpotDTO.getPlayerCard() != null && !jackpotDTO.getPlayerCard().trim().isEmpty() && !jackpotDTO.getPlayerCard().equals(IAppConstants.DEFAULT_PLAYER_CARD)) {
			printDTO.setPlayerCard(jackpotDTO.getPlayerCard());
		} else if (jackpotDTO.getAssociatedPlayerCard() != null
				& !jackpotDTO.getAssociatedPlayerCard().trim().isEmpty()) {
			printDTO.setPlayerCard(jackpotDTO.getAssociatedPlayerCard());
		} else {
			printDTO.setPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
		}
		
		if(jackpotDTO.getPlayerName()!=null) {
			printDTO.setPlayerName(jackpotDTO.getPlayerName());
		}
		//printDTO.setPrintDate(jackpotDTO.getPrintDate());
		//printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getDateString(com.ballydev.sds.framework.util.DateUtil.getCurrentServerDate(), IAppConstants.SLIP_DATE_FORMAT));
		printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(com.ballydev.sds.framework.util.DateUtil.getCurrentServerDate()));
	
		if(jackpotDTO.getPrinterSchema()!=null) {
			printDTO.setPrinterSchema(jackpotDTO.getPrinterSchema());
		}
		if(jackpotDTO.getPrintEmployeeLogin()!=null) {
			printDTO.setProcessEmpId(PadderUtil.lPad(jackpotDTO.getPrintEmployeeLogin(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
		}
		if (jackpotDTO.getEmployeeName() != null) {
			printDTO.setProcessEmpName(jackpotDTO.getEmployeeName());
		} else {
			UserDTO userDTO = null;
			try {
				SessionUtility sessionUtil = new SessionUtility();
				userDTO = sessionUtil.getUserDetails(jackpotDTO.getPrintEmployeeLogin(),
						MainMenuController.jackpotForm.getSiteId());
				printDTO.setProcessEmpName(userDTO.getFirstName() + " " + userDTO.getLastName());
			} catch (Exception e) {
				log.error("Unable to get the Print Emp Name", e);
			}
		}
		
		printDTO.setSeal(jackpotDTO.getSealNumber());
		if(jackpotDTO.getSequenceNumber()!=0){
			printDTO.setSequenceNumber(Long.toString(jackpotDTO.getSequenceNumber()));			
		}
		
		printDTO.setShift(getShiftDescription(jackpotDTO.getShift()));
		
		if(jackpotDTO.getSlipSchema()!=null) {
			printDTO.setSlipSchema(jackpotDTO.getSlipSchema());
		}
		if(jackpotDTO.getSlotAttendantId()!=null) {
			printDTO.setSlotAttendantId(PadderUtil.lPad(jackpotDTO.getSlotAttendantId(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
		}	
		if(jackpotDTO.getSlotAttendantFirstName()!=null && jackpotDTO.getSlotAttendantLastName()!=null) {
			printDTO.setSlotAttendantName(jackpotDTO.getSlotAttendantFirstName() +" "+ jackpotDTO.getSlotAttendantLastName());
		}			
		if(jackpotDTO.getSlotDenomination()!=0) {
			printDTO.setSlotDenomination(JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO.getSlotDenomination())));
		}
		if(jackpotDTO.getSiteId()!=0) {
			printDTO.setSiteId(jackpotDTO.getSiteId());
		}
		if(jackpotDTO.getTransactionDate()!=null) {
			/*printDTO.setTransactionDate(com.ballydev.sds.framework.util.DateUtil.getDateString(DateUtil
					.getTime(jackpotDTO.getTransactionDate().getTime()), IAppConstants.SLIP_DATE_FORMAT));*/
			
			printDTO.setTransactionDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(DateUtil
					.getTime(jackpotDTO.getTransactionDate().getTime())));
		}	
		if(jackpotDTO.getWindowNumber()!=null) {
			printDTO.setWindow(jackpotDTO.getWindowNumber());
		}
		if(jackpotDTO.getWinningComb()!=null) {
			printDTO.setWinningComb(jackpotDTO.getWinningComb());
		}
			
		printDTO.setJackpotType(getJackpotType(jackpotDTO.getProcessFlagId(), jackpotDTO.getJackpotTypeId()));
		// Setting cashless account type and account number to the DTO
		if(jackpotDTO.getAccountNumber() != null) {
			printDTO.setAccountNumber(jackpotDTO.getAccountNumber().toString());			
		}
		
		printDTO.setInterceptAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
				+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(jackpotDTO
						.getInterceptAmount())));
		
		return printDTO;
	}
	
	/**
	 * Method to set the field values in JackpotDTO to the PrintDTO fields for CHECK schema 
	 * @param jackpotDTO
	 * @return
	 * @author vsubha
	 */
	public static PrintDTO setCheckDTOValues(JackpotDTO jackpotDTO) {
		PrintDTO printDTO = new PrintDTO();
		// Setting jackpot slip/sequence number
		printDTO.setJackpotSlipNumber(Long.toString(jackpotDTO.getSequenceNumber()));
		printDTO.setDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(com.ballydev.sds.framework.util.DateUtil.getCurrentServerDate()));
		if(jackpotDTO.getPlayerName() != null) {
			printDTO.setPay(jackpotDTO.getPlayerName());
		}
		if(jackpotDTO.getJackpotNetAmount() != 0) {
			printDTO.setAmount(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
							.centsToDollar(jackpotDTO.getJackpotNetAmount())));
		}
		if(jackpotDTO.getAssetConfigNumber() != null) {
			printDTO.setMachineNo(jackpotDTO.getAssetConfigNumber());
		}
		if(jackpotDTO.getAssetConfigLocation() != null) {
			printDTO.setStandNo(jackpotDTO.getAssetConfigLocation());
		}
		printDTO.setPayoutType(getJackpotType(jackpotDTO.getProcessFlagId(), jackpotDTO.getJackpotTypeId()));
		if(jackpotDTO.getSlipSchema() != null) {
			printDTO.setSlipSchema(jackpotDTO.getSlipSchema());
		}
		if(jackpotDTO.getPrinterSchema() != null) {
			printDTO.setPrinterSchema(jackpotDTO.getPrinterSchema());
		}
		
		return printDTO;
	}
	
	/**
	 * Gets the shift description value for the shift code
	 * @param shift
	 * @return
	 * @author vsubha
	 */
	private static String getShiftDescription(String shift) {
		String shiftDescription = "";		
		if(shift != null) {					
			if(shift.equalsIgnoreCase("d"))
				shiftDescription = LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL);
			else if(shift.equalsIgnoreCase("s"))
				shiftDescription = LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL);
			else if(shift.equalsIgnoreCase("g"))
				shiftDescription = LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL);				
		}
		return shiftDescription;
	}
	
	/**
	 * Gets the jackpot type for the process flag Id and jackpot type Id
	 * @param processFlagId
	 * @param jackpotTypeId
	 * @return
	 * @author vsubha
	 */
	private static String getJackpotType(short processFlagId, short jackpotTypeId) {
		String jackpotType = "";
		if(processFlagId != 0 && jackpotTypeId != 0) {
			if(processFlagId == NORMAL_PROCESS_FLAG) {
				if(jackpotTypeId== JACKPOT_TYPE_REGULAR) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_NORMAL_REGULAR);
				} else if(jackpotTypeId== JACKPOT_TYPE_PROGRESSIVE) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_NORMAL_PROGRESSIVE);
				} else if(jackpotTypeId== JACKPOT_TYPE_lINKED_PROGRESSIVE) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_NORMAL_LINKED_PROGRESSIVE);
				} else if(jackpotTypeId== JACKPOT_TYPE_SYSTEM_GAME) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_NORMAL_SYSTEM_GAME);
				} else if(jackpotTypeId== JACKPOT_TYPE_CANCEL_CREDIT) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_NORMAL_CANCEL_CREDIT);
				}
			} else if(processFlagId == EXPRESS_PROCESS_FLAG) {
				if(jackpotTypeId== JACKPOT_TYPE_REGULAR) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_EXPRESS_REGULAR);
				} else if(jackpotTypeId== JACKPOT_TYPE_PROGRESSIVE) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_EXPRESS_PROGRESSIVE);
				} else if(jackpotTypeId== JACKPOT_TYPE_lINKED_PROGRESSIVE) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_EXPRESS_LINKED_PROGRESSIVE);
				} else if(jackpotTypeId== JACKPOT_TYPE_SYSTEM_GAME) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_EXPRESS_SYSTEM_GAME);
				} else if(jackpotTypeId== JACKPOT_TYPE_CANCEL_CREDIT) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_EXPRESS_CANCEL_CREDIT);
				}
			} else if(processFlagId == POUCH_PAY_PROCESS_FLAG) {
				if(jackpotTypeId== JACKPOT_TYPE_REGULAR) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_REGULAR);
				} else if(jackpotTypeId== JACKPOT_TYPE_SYSTEM_GAME) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_SYSTEM_GAME);
				} else if(jackpotTypeId== JACKPOT_TYPE_CANCEL_CREDIT) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_CANCEL_CREDIT);
				} else if(jackpotTypeId== JACKPOT_TYPE_PROGRESSIVE) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_POUCH_PAY_PROGRESSIVE);
				}
			} else if(processFlagId == MANUAL_PROCESS_FLAG) {
				if(jackpotTypeId== JACKPOT_TYPE_REGULAR) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_MANUAL_REGULAR);
				} else if(jackpotTypeId== JACKPOT_TYPE_PROGRESSIVE) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_MANUAL_PROGRESSIVE);
				} else if(jackpotTypeId== JACKPOT_TYPE_CANCEL_CREDIT) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_MANUAL_CANCEL_CREDIT);
				} else if(jackpotTypeId== JACKPOT_TYPE_PROMOTIONAL) {
					jackpotType = LabelLoader.getLabelValue(LabelKeyConstants.PRINT_JP_TYPE_MANUAL_PROMOTIONAL);
				}
			}
		}
		return jackpotType;
	}
	
	public static String getUnderLineCharForStringLength(int strLength) {
		String str = IAppConstants.EMPTY_STRING;
		int i = 0;
		while(i < strLength) {
			str += IAppConstants.UNDERLINE_CHAR;
			i++;
		}
		return str;
	}
	
	public static String getSpacesForStringLength(int strLength) {
		String str = IAppConstants.EMPTY_STRING;
		int i = 0;
		while(i < strLength) {
			str += IAppConstants.SIGLE_SPACE;
			i++;
		}
		return str;
	}
	public static void main(String[] args) {
		
		SlipUtil slipUtil = new SlipUtil();
		System.out.println("test value: "+ConversionUtil.centsToDollar(1000000000));
	}
	
}
