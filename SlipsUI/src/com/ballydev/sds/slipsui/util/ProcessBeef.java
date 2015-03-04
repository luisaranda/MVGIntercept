/*****************************************************************************
 * $Id: ProcessBeef.java,v 1.24, 2011-01-24 09:17:14Z, Ambereen Drewitt$
 * $Date: 1/24/2011 3:17:14 AM$
 * $Log$
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
package com.ballydev.sds.slipsui.util;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.jboss.remoting.CannotConnectException;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slips.dto.SlipsDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slips.filter.SlipsFilter;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.IAuditTrailConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.controller.MainMenuController;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.print.SlipImageBuilderUtility;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;

/**
 * Class to update all the Beef process values in the SlipsDTO and process the slip
 * @version $Revision: 25$
 */
public class ProcessBeef {

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
			.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Method to process a beef slip and print it
	 */
	public void processBeefSlip() {
		SlipsDTO slipsDTO = new SlipsDTO();
		slipsDTO.setAssetConfNumberUsed(MainMenuController.slipForm.getAssetConfigNumberUsed());

		if (MainMenuController.slipForm.getSlipAmount() != 0) {
			slipsDTO.setSlipAmount(MainMenuController.slipForm.getSlipAmount());
		}
		if (MainMenuController.slipForm.getSlotNo() != null) {
			slipsDTO.setAssetConfigNumber(MainMenuController.slipForm
					.getSlotNo());
		}
		if (MainMenuController.slipForm.getSlotLocationNo() != null) {
			slipsDTO.setAssetConfigLocation(MainMenuController.slipForm
					.getSlotLocationNo());
		}

		if(MainMenuController.slipForm.getAuthEmployeeIdOne()!=null){
			slipsDTO.setAuthEmployeeId1(MainMenuController.slipForm.getAuthEmployeeIdOne());
		}
		if(MainMenuController.slipForm.getAuthEmployeeIdTwo()!=null){
			slipsDTO.setAuthEmployeeId2(MainMenuController.slipForm.getAuthEmployeeIdTwo());
		}
		
		if (MainMenuController.slipForm.getActorLogin() != null) {
			slipsDTO.setActorLogin(MainMenuController.slipForm.getActorLogin());
		}
		if (MainMenuController.slipForm.getEmployeeIdPrintedSlip() != null) {
			slipsDTO.setPrintEmployeeLogin(MainMenuController.slipForm.getEmployeeIdPrintedSlip());
		}
		if (MainMenuController.slipForm.getWindowNumber() != null) {
			slipsDTO.setWindowNumber(MainMenuController.slipForm.getWindowNumber());
		}
	
		if (MainMenuController.slipsSiteConfigParams.get(
				ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
			if (MainMenuController.slipForm.getShift()
					.equalsIgnoreCase("day")) {
				slipsDTO.setShift("D");
			} else if (MainMenuController.slipForm.getShift()
					.equalsIgnoreCase("swing")) {
				slipsDTO.setShift("S");
			} else if (MainMenuController.slipForm.getShift()
					.equalsIgnoreCase("Graveyard")) {
				slipsDTO.setShift("G");
			}
		} else if (MainMenuController.slipsSiteConfigParams.get(
				ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY)) {
			slipsDTO.setShift("D");
		} else if (MainMenuController.slipsSiteConfigParams.get(
				ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_SWING)) {
			slipsDTO.setShift("S");
		} else if (MainMenuController.slipsSiteConfigParams.get(
				ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
			slipsDTO.setShift("G");
		}
		
		SlipsFilter filter = new SlipsFilter();
		//filter.setSlotDenomination(100);// TO BE GOT FROM ASSET
		slipsDTO.setMessageId((long) (Math.random() * 97367));
		slipsDTO.setSiteId(MainMenuController.slipForm.getSiteId());
		slipsDTO.setSiteNo(MainMenuController.slipForm.getSiteNo());
		
		slipsDTO.setCashlessAccountNumber(MainMenuController.slipForm.getCashlessAccountNumber());
//		slipsDTO.setCashlessAccountType(MainMenuController.slipForm.getCashlessAccountType());
//		
//		if(MainMenuController.slipForm.isCashableAmount()) {
//			slipsDTO.setCashableAmountFlag(IAppConstants.CASHABLE_AMT_FLAG);
//		}else {
//			slipsDTO.setCashableAmountFlag(IAppConstants.NONCASHABLE_AMT_FLAG);
//		}
		
		String printerUsed = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
		if(printerUsed!=null)
			slipsDTO.setPrinterUsed(printerUsed);
		if (new Long(
				MainMenuController.slipsSiteConfigParams
						.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
			filter.setFieldType("Slot");
		} else if (new Long(
				MainMenuController.slipsSiteConfigParams
						.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
			filter.setFieldType("Stand");
		}
		filter.setSiteId(MainMenuController.slipForm.getSiteId());
		slipsDTO.setReason(MainMenuController.slipForm.getReason());

		SlipsDTO processedDTO=null; 
		try {
			processedDTO = SlipsServiceLocator.getService().processBeefSlip(slipsDTO,filter);

		}catch (SlipsEngineServiceException e1) {	
			log.info("EXCEPTION calling processBeefSlip", e1);
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e1, e1.getMessage());
			return;
		}
		catch (Exception e2) {
			log.info("EXCEPTION calling processBeefSlip", e2);
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
			return;
		}
			
			if (processedDTO.getSequenceNumber()!=0) {
				MessageDialogUtil
						.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.SLIP_PROCESS_SUCCESS)
								+ " "
								+ LabelLoader
										.getLabelValue(MessageKeyConstants.WITH_THE_SEQUENCE)
								+ " " + processedDTO.getSequenceNumber());
				// to dispose the current screen
				/*if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}*/
				
				String usrName;
				try {
					SessionUtility sessionUtility = new SessionUtility();
					
					UserDTO userDTO=sessionUtility.getUserDetails(processedDTO.getPrintEmployeeLogin(), MainMenuController.slipForm.getSiteId());
					StringBuilder uname=new StringBuilder("");
					uname.append(userDTO.getFirstName());
					uname.append(" ");
					uname.append(userDTO.getLastName());
					usrName = uname.toString();
					processedDTO.setEmployeeName(usrName);
				}catch(Exception ex){
					if (ex instanceof CannotConnectException) {
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(ex, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
					}
					else if(ex instanceof RuntimeException && ex.getMessage()!=null && ((ex.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_UNREACH_SERVICE_MSG)) ||
		                    (ex.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG)))){
						
						log.error("Exception while checking the employee id with framework");
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(ex, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
					}else{
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(ex,  ex.getMessage());
					}							
					log.error("Exception while checking the user authentication",ex);
				}
				
				try {
					// BUILD SLIP IMAGE AND PRINT SLIP
					SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
					printUtil.buildSlipImage(processedDTO);					
				
				} catch (Exception e1) {
					log.error(e1);
				}
				
				try{
					log.info("BEFORE SENDING AUDIT TRAIL INFO");
					Util.sendDataToAuditTrail(IAuditTrailConstants.AUDIT_MODULE_NAME,
							LabelLoader.getLabelValue(LabelKeyConstants.DISPUTE_SCR_NAME),
							LabelLoader.getLabelValue(LabelKeyConstants.SLIP_AMOUNT),
						null,
						String.valueOf(new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(MainMenuController.slipForm.getSlipAmount()))),
						LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID)+": "+ MainMenuController.slipForm.getEmployeeIdPrintedSlip()
							+LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER)+": "+MainMenuController.slipForm.getSlotNo()
							+LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+": "+processedDTO.getSequenceNumber(),
						EnumOperation.BEEF, MainMenuController.slipForm.getSlotNo());		
					log.info("AFTER SENDING AUDIT TRAIL INFO");
				}catch (Exception e) {
					log.error("Exception when sending audit trail info",e);
				}				
				
			} else {
				MessageDialogUtil
						.displayTouchScreenInfoDialog(MessageKeyConstants.SLIP_PROCESS_FAILED);
			}

			System.out.println("Sequence No : " + processedDTO.getSequenceNumber());
		

	}

}
