/*****************************************************************************
 * $Id: SlipImageBuilderUtility.java,v 1.36, 2011-02-02 06:07:14Z, Ambereen Drewitt$
 * $Date: 2/2/2011 12:07:14 AM$
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
package com.ballydev.sds.slipsui.print;

import java.awt.print.PrinterException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.slips.dto.SlipsDTO;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.controller.MainMenuController;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.util.ConversionUtil;
import com.ballydev.sds.slipsui.util.PadderUtil;
import com.ballydev.sds.slipsui.util.PrintDTO;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * This is an Utility Class which builds the SlipImage for Printing
 * @author gsrinivasulu
 * @version $Revision: 37$
 */
public class SlipImageBuilderUtility {
	
	/**
	 * Slip Type
	 */
	protected String slipType;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Constructor for PrintUtil which takes String argument
	 * @param slipType
	 */
	public SlipImageBuilderUtility(String slipType) {
		this.slipType = slipType;
	}
	/**
	 * This method will convert the given Slips DTO in to the PrintDTO
	 * which is used to build the Slip Image
	 * @param slipsDTO
	 * @return
	 */
	private PrintDTO convertPrintFormat(SlipsDTO slipsDTO) {
		PrintDTO printDTO = null;
		if(slipsDTO != null) {
			printDTO = new PrintDTO();
			
			printDTO.setCasinoName(MainMenuController.slipForm.getSiteLongName());
			
			printDTO.setPrinterSchema(slipsDTO.getPrinterSchema());
			printDTO.setSlipSchema(slipsDTO.getSlipSchema());
			
		
			printDTO.setEmpID(PadderUtil.lPad(slipsDTO.getPrintEmployeeLogin(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
			
			if(slipsDTO.getEmployeeName()!=null) {
				printDTO.setEmpName(slipsDTO.getEmployeeName());
			}else {
				UserDTO userDTO = null;
				try {				
					SessionUtility sessionUtil = new SessionUtility();
					userDTO = sessionUtil.getUserDetails(PadderUtil.lPad(slipsDTO.getPrintEmployeeLogin(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH),
							MainMenuController.slipForm.getSiteId());
					printDTO.setEmpName(userDTO.getFirstName()+ " "+ userDTO.getLastName());					
				} catch (Exception e) {				
					log.error("Unable to get the Print Emp Name", e);
				}
			}
			
			
			StringBuilder seqNum = new StringBuilder(); 
			if(slipsDTO.getSlipTypeId()==IAppConstants.FILL_SLIP_TYPE_ID) {
				seqNum.append(PrinterConstants.FILL_PRINT_PREFIX);
			} else if(slipsDTO.getSlipTypeId() == IAppConstants.BEEF_SLIP_TYPE_ID) {
				seqNum.append(PrinterConstants.BEEF_PRINT_PREFIX);
			} else if(slipsDTO.getSlipTypeId() == IAppConstants.BLEED_SLIP_TYPE_ID) {
				seqNum.append(PrinterConstants.BLEED_PRINT_PREFIX);
			}
			seqNum.append(slipsDTO.getSequenceNumber());
			printDTO.setSequenceNumber(seqNum.toString());
			/*printDTO.setAssetConfigNumber(MainMenuController.slipForm
					.getSlotNo());
			printDTO.setAssetConfigLocation(MainMenuController.slipForm.getSlotLocationNo());*/
			printDTO.setAssetConfigNumber(StringUtil.trimAcnfNo(slipsDTO.getAssetConfigNumber()));
			printDTO.setAssetConfigLocation(slipsDTO.getAssetConfigLocation());
			if (!MainMenuController.slipsSiteConfigParams.get(
					ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT)
					.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
				
				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD) )
					printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
				else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY) )
					printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
				else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_SWING) )
					printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
			
			}else{
				String shift=slipsDTO.getShift();
				
				if(shift!=null && shift.length()>0)  {
										
					if(shift.equalsIgnoreCase("G")) {
						printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
					}
					else if(shift.equalsIgnoreCase("D")) {
						printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
					}
					else if(shift.equalsIgnoreCase("S")) {
						printDTO.setShift(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
					}
					//slipsDTO.getShift());
				}
				else
					printDTO.setShift(MainMenuController.slipForm.getShift());
			}
			//if(this.slipType.equals(PrinterConstants.SLIP_TYPE_FILL)) {
			
				printDTO.setOriginalAmount(MainMenuController.slipForm.getSiteCurrencySymbol() 
						+ ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(slipsDTO.getSlipAmount())));
					
			if(slipsDTO.getAuthEmployeeId1()!=null){
				printDTO.setAuthEmpIdOne(PadderUtil.lPad(slipsDTO.getAuthEmployeeId1(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				SessionUtility sessionUtil = new SessionUtility();
				
				UserDTO userDTO = null;
				try {
					userDTO = sessionUtil.getUserDetails(printDTO.getAuthEmpIdOne(), MainMenuController.slipForm.getSiteId());
					
					printDTO.setAuthEmpNameOne(userDTO.getFirstName()+ " "+ userDTO.getLastName());				
				} catch (Exception e) {				
					log.error("Unable to get the First Auth Emp Name", e);
				}
			}
			
			if(slipsDTO.getAuthEmployeeId2()!=null){
				printDTO.setAuthEmpIdTwo(PadderUtil.lPad(slipsDTO.getAuthEmployeeId2(), IAppConstants.EMPLOYEE_ID_PAD_LENGTH));
				SessionUtility sessionUtil = new SessionUtility();
				
				UserDTO userDTO = null;
				try {
					userDTO = sessionUtil.getUserDetails(printDTO.getAuthEmpIdTwo(), MainMenuController.slipForm.getSiteId());
					
					printDTO.setAuthEmpNameTwo(userDTO.getFirstName()+ " "+ userDTO.getLastName());				
				} catch (Exception e) {				
					log.error("Unable to get the Second Auth Emp Name", e);
				}
			}			
			
			if(slipsDTO.getWindowNumber()!=null) {
				printDTO.setWindowNumber(slipsDTO.getWindowNumber());
			}
			
			//String[] printDate = new Timestamp(DateUtil.getCurrentServerDate().getTime()).toString().trim().replace(".", "_").split("_");
			//String[] transactionDate = new Timestamp(slipsDTO.getTransactionDate().getTime()).toString().trim().replace(".", "_").split("_");
			
			printDTO.setSiteId(slipsDTO.getSiteId());
			printDTO.setSeal(slipsDTO.getSealNumber());	
			printDTO.setPrintDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(com.ballydev.sds.framework.util.DateUtil.getCurrentServerDate()));
			printDTO.setTransactionDate(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(slipsDTO.getTransactionDate()));
			
			//printDTO.setDenomination(ConversionUtil.centsToDollar(MainMenuController.slipForm.getDenomination()));
		
			//printDTO.setDenomination(ConversionUtil.centsToDollar(slipsDTO.getSlotDenomination()));
			printDTO.setDenomination(ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(slipsDTO.getSlotDenomination())));
			//printDTO.setDenomination(MainMenuController.slipForm.getDenomination());
//			if(slipsDTO.getCashlessAccountType()!=null) {
//				printDTO.setAccountType(LabelLoader.getLabelValue(slipsDTO.getCashlessAccountType()));
//			}			
			printDTO.setAccountNo(slipsDTO.getCashlessAccountNumber()!=null? String.valueOf(slipsDTO.getCashlessAccountNumber()):"");
//			if(slipsDTO.getCashableAmountFlag()!=null && slipsDTO.getCashableAmountFlag()==1) {
//				printDTO.setAmountType(ILabelConstants.AMOUNT_TYPE_CASHABLE);
//			}else {
//				printDTO.setAmountType(ILabelConstants.AMOUNT_TYPE_NONCASHABLE);
//			}
			printDTO.setReason(slipsDTO.getReason());
		}
		return printDTO;
	}
	
	/**
	 * This method will format the given SlipsDTO information 
	 * in to a Print Format that can be printed by SlipPrinter methods and sens it for print to the user selected printer
	 * @param slipsDTO
	 * @return
	 */
	public String buildSlipImage(SlipsDTO slipsDTO) throws Exception {
		SlipImage slipImage = new SlipImage();
		String image = null;
		try {
			PrintDTO dto = convertPrintFormat(slipsDTO);
			SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
			PrintWithGraphics printGrap = new PrintWithGraphics();
			HashMap<String, String> slipI18NMap = null;
			if(slipType.equalsIgnoreCase(IAppConstants.BEEF_PRINT_SLIP_TYPE)) {
				slipI18NMap = SlipUtil.beefSlipMap;
			} else if(slipType.equalsIgnoreCase(IAppConstants.VOID_PRINT_SLIP_TYPE)) {
				slipI18NMap = SlipUtil.voidSlipMap;
			}
			if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.EPSON_PRINTER.toUpperCase())) {
				image = slipImage.buildSlipImage(dto, slipType,slipI18NMap);	
				log.debug("Slip Image: "+image);
				/** SEND THE SLIP IMAGE FOR PRINT for the EPSON printer slip format*/ 
				printUtil.printSlipImage(image);
			} else if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).toUpperCase().contains(PrinterConstants.LASER_PRINTER.toUpperCase())) {
				image = slipImage.getLaserPrinterImage(dto, slipType, slipI18NMap);
				log.debug("Slip Image: "+image);
				/** SEND THE SLIP IMAGE FOR PRINT */ 
				printGrap.printTextImage(image);
			} else {
				image = slipImage.getLaserPrinterImage(dto, slipType, slipI18NMap);
				log.debug("Slip Image: "+image);
				/** SEND THE SLIP IMAGE FOR PRINT */ 
				printGrap.printTextImage(image);
			}			
		}catch (PrinterException e) {
			log.error("PrinterException - Failed to build the slip Image for printing the Slip ", e);
			new SlipUIExceptionHandler().handleException(e, MessageKeyConstants.PRINTER_NOT_ACCEPTING_JOB);
		} 
		catch (Exception e) {
			log.error("Failed to build the slip Image for printing the Slip ", e);
			throw new Exception("Failed to build the slip Image for printing the Slip ", e);
		}
		return image;
	}
	
	/**
	 * This method will print the given slip image in to print hand out
	 * @param slipImage
	 */
	public void printSlipImage(String slipImage) throws Exception {
		SlipPrinting print = new SlipPrinting();
		print.printSlip(slipImage);
	}
}
