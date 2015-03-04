/*****************************************************************************
 * $Id: ReprintController.java,v 1.67, 2011-03-05 10:37:03Z, Ambereen Drewitt$
 * $Date: 3/5/2011 4:37:03 AM$
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
package com.ballydev.sds.slipsui.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.EnumOperation;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSRadioImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.slips.dto.SlipsDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slips.filter.SlipsFilter;
import com.ballydev.sds.slipsui.composite.ReprintComposite;
import com.ballydev.sds.slipsui.constants.FormConstants;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.IAuditTrailConstants;
import com.ballydev.sds.slipsui.constants.ILookupTableConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.form.ReprintForm;
import com.ballydev.sds.slipsui.print.SlipImageBuilderUtility;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.FocusUtility;
import com.ballydev.sds.slipsui.util.SlipUILogger;
import com.ballydev.sds.slipsui.util.SlipsExceptionUtil;
import com.ballydev.sds.slipsui.util.UserFunctionsUtil;

/**
 * This class acts as a controller for all the events performed in the
 * ReprintComposite
 *
 */
public class ReprintController extends SDSBaseController{

	/**
	 * ReprintForm instance
	 */
	private ReprintForm form;
	/**
	 * ReprintComposite instance
	 */
	private ReprintComposite reprintComposite;
	/**
	 * SlipsFilter instance
	 */
	private SlipsFilter filter =new SlipsFilter();
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * ReprintController constructor
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public ReprintController(Composite parent, int style,ReprintForm form,SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		this.form.setBeef(true);
		try {
			createReprintComposite(parent, style, getSiteConfigurationParameters());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		TopMiddleController.setCurrentComposite(reprintComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
		super.registerEvents(reprintComposite);
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(reprintComposite);		
		FocusUtility.setTextFocus(reprintComposite);
	}

	/**
	 * This method is used to perform action for widgetselected event
	 */
	/*@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			Control control = (Control) e.getSource();

			if (!(control instanceof CbctlButton)
					&& !(control instanceof TouchScreenRadioButton)) {
				return;
			}

			if (control instanceof CbctlButton) {
			if(((CbctlButton)control).getName().equalsIgnoreCase("reprint")){
				
				 Populating the form with screen values 
				populateForm(reprintComposite);
				
				List<String> fieldNames = new ArrayList<String>();
				
				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
					fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
				}
				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
					fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
				}				
				if(form.getSequenceNo()!=null && (!form.getSequenceNo().equalsIgnoreCase(IAppConstants.PRIOR_SLIP_REPRINT) || form.getSequenceNo().length()!=1)){
					fieldNames.add(FormConstants.FORM_SEQUENCE_NO);
				}
				boolean validate = validate("ReprintForm", fieldNames, form, reprintComposite);
				filter.setSlipTypeId(IAppConstants.BEEF_SLIP_TYPE_ID);
				log.info("SLIP TYPE: "+filter.getSlipTypeId());
				 Populating the form with screen values 

				if(!validate){
					log.info("Client side validation failed");
					return;
				}
				SessionUtility sessionUtility = new SessionUtility();
				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
						&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
					if(form.getEmpPassword().length() < 5){
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
						reprintComposite.getTxtEmployeePwd().forceFocus();
						return;
					}
					else{
						try{
							log.info("Called framework's authenticate user method");
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmpPassword(), MainMenuController.slipForm.getSiteId());
							if(userDtoEmpPasswordChk!=null){
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;
								}
								else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
	                                    
                                	ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeePwd().forceFocus();
									return;
								}
								else if(!UserFunctionsUtil.isReprintAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
									
									log.info("User does not have PROCESS REPRINT FUNCTION");										
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;																	
								}
								else{
									MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
								}
							}else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
								reprintComposite.getTxtEmployeeId().forceFocus();
								return;
							}
						}catch(Exception ex){
							log.error("Exception while checking authenticateUser with framework");
							SlipsExceptionUtil.getGeneralCtrllerException(ex);							
							log.error("Exception while checking the user authentication",ex);
							return;
						}
					//}
				}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
					if(form.getEmpPassword().length() < 5){
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
						reprintComposite.getTxtEmployeePwd().forceFocus();
						return;
					}
					else{
						try{
							log.info("Called framework's authenticate user method");
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.slipForm.getActorLogin(), form.getEmpPassword(), MainMenuController.slipForm.getSiteId());
							log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
							log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
							if(userDtoEmpPasswordChk!=null){
								if(userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey()))
								{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									reprintComposite.getTxtEmployeePwd().forceFocus();
									return;
								}else if(!UserFunctionsUtil.isReprintAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
									
									log.info("User does not have PROCESS REPRINT FUNCTION");										
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeePwd().forceFocus();
									return;																	
								}
								else{
									MainMenuController.slipForm.setEmployeeIdPrintedSlip(MainMenuController.slipForm.getActorLogin());
								}
							}else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
								reprintComposite.getTxtEmployeePwd().forceFocus();
								return;
							}
						}catch(Exception ex){
							SlipsExceptionUtil.getGeneralCtrllerException(ex);							
							log.error("Exception while checking the user authentication",ex);
							return;
						}
					//}
				}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
					try{
						log.info("Called framework's authenticate user method");
						UserDTO userDTOEmpIdCheck = sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.slipForm.getSiteId());
						if(userDTOEmpIdCheck!=null){
							log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
							if(userDTOEmpIdCheck.getUserName()==null){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								reprintComposite.getTxtEmployeeId().forceFocus();
								return;
							}
							else if(!UserFunctionsUtil.isReprintAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
								
								log.info("User does not have PROCESS REPRINT FUNCTION");										
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED));
								reprintComposite.getTxtEmployeeId().forceFocus();
								return;																	
							}
							else{
								MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
							}
						}
						else{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
							reprintComposite.getTxtEmployeeId().forceFocus();
							return;
						}
					}catch(Exception ex){
						SlipsExceptionUtil.getGeneralCtrllerException(ex);							
						log.error("Exception while checking the user authentication",ex);
						return;
					}
				}else if(!UserFunctionsUtil.isReprintAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
					
					log.info("User does not have PROCESS REPRINT FUNCTION");										
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
					reprintComposite.getTxtSequenceNo().forceFocus();
					return;																	
				}

				//	PRIOR SLIP REPRINT LOGIC
				if(form.getSequenceNo().equalsIgnoreCase(IAppConstants.PRIOR_SLIP_REPRINT)) {
					SlipsDTO slipsDTO = null;
					try {
						slipsDTO = (SlipsDTO)SlipsServiceLocator.getService().getReprintPriorSlipDetails(MainMenuController.slipForm.getSiteId());
						
						if(slipsDTO != null && slipsDTO.isPriorSlipVoided()) {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+ " "
										+slipsDTO.getSequenceNumber() +" "+LabelLoader.getLabelValue(MessageKeyConstants.PRIOR_SLIP_VOIDED_ENTER_MANUALLY));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;
						}
						else if(slipsDTO != null && slipsDTO.getSequenceNumber() != 0 
								&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("YES")) {
							
							if(slipsDTO.getStatusFlagId()==ILookupTableConstants.PRINTED_STATUS_ID
									||slipsDTO.getStatusFlagId()==ILookupTableConstants.REPRINT_STATUS_ID) {
								
								//ALLOW TO REPRINT THE SLIP
								form.setReprintJackpotSlipDTO(slipsDTO);
								MainMenuController.slipForm.setSequenceNumber(slipsDTO.getSequenceNumber());
								form.setSequenceNo(Long.toString(slipsDTO.getSequenceNumber()));
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								reprintSlip();
								
							}else{
								//WHEN THE STATUS IS PROCESSED,
								//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS ALREADY PROCESSED
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+ " "
										+slipsDTO.getSequenceNumber() +" "+LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PROCESSED));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;							
							}							
						}else if(slipsDTO != null && slipsDTO.getSequenceNumber() != 0 
								&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("NO")) {
							
							if(slipsDTO.getStatusFlagId()==ILookupTableConstants.PROCESSED_STATUS_ID
									||slipsDTO.getStatusFlagId()==ILookupTableConstants.REPRINT_STATUS_ID) {
								
								//ALLOW TO REPRINT THE SLIP
								form.setReprintJackpotSlipDTO(slipsDTO);
								MainMenuController.slipForm.setSequenceNumber(slipsDTO.getSequenceNumber());
								form.setSequenceNo(Long.toString(slipsDTO.getSequenceNumber()));
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								reprintSlip();								
								
							}else{
								//WHEN THE STATUS IS PRINTED (will not occur if site is NO)
								//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS IN PRINTED STATE
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+ " "
										+slipsDTO.getSequenceNumber() +" "+LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;		
							}						
						}
						else{
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil
							.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.NO_PRIOR_SLIPS_TO_REPRINT));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						}					
						
					} catch (SlipsEngineServiceException exception) {
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(exception, exception.getMessage());
						MessageDialogUtil
						.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
						reprintComposite.getTxtSequenceNo().forceFocus();
						return;
					} catch (Exception e2) {
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
						log.error("SERVICE_DOWN",e2);
						return;
					} finally {
						ProgressIndicatorUtil.closeInProgressWindow();
					}
				} // LOGIC WHEN THE INPUT IS SEQUENCE NUMBER
				else {
					short statusId = 0;
					try {
						
						filter.setSiteId(MainMenuController.slipForm.getSiteId());
						log.info("Called getSlipStatus web method");
						statusId = SlipsServiceLocator.getService().getSlipStatusId(
								Long.parseLong(form.getSequenceNo()), filter);
					}
					catch (SlipsEngineServiceException e1) {
						log.info("EXCEPTION calling getSlipStatus for REPRINT", e1);
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(e1, e1.getMessage());
						return;
					}
					catch (Exception e2) {
						log.info("EXCEPTION calling getSlipStatus for REPRINT", e2);
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
						return;
					}
					if(statusId==0){
						log.debug("Returned value is null as de seq no is not present in the DB");
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
						reprintComposite.getTxtSequenceNo().forceFocus();
						return;
					}
					else{
					log.debug("*************** Slip Status Id: " + statusId+ " **************************");

						if (statusId ==  ILookupTableConstants.VOID_STATUS_ID ) {
							
							log.info("Slip is already voided hence cannot be reprinted");
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue
									(MessageKeyConstants.SLIP_ALREADY_VOIDED));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						}else if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("YES")) {
							
							if(statusId == ILookupTableConstants.PRINTED_STATUS_ID
									||statusId == ILookupTableConstants.REPRINT_STATUS_ID) {
								
								//ALLOW TO REPRINT THE SLIP
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								log.info("Calling reprint slip.....");
								reprintSlip();
								
							}else if(statusId == ILookupTableConstants.PROCESSED_STATUS_ID){
								//WHEN THE STATUS IS PROCESSED,
								//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS ALREADY PROCESSED
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+ " "
										+form.getSequenceNo() +" "+LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PROCESSED));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;	
							}else {
								log.info("Reprint - Slip is not found for seq: "+form.getSequenceNo());
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;
							}
						}else if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("NO")) {
							
							if(statusId == ILookupTableConstants.PROCESSED_STATUS_ID
									||statusId == ILookupTableConstants.REPRINT_STATUS_ID) {
								
								//ALLOW TO REPRINT THE SLIP
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								log.info("Calling reprint slip.....");
								reprintSlip();							
								
							}else if(statusId == ILookupTableConstants.PRINTED_STATUS_ID){
								//WHEN THE STATUS IS PRINTED (will not occur if site is NO)
								//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS IN PRINTED STATE
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+ " "
										+form.getSequenceNo() +" "+LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;	
							}else {
								log.info("Reprint - Slip is not found for seq: "+form.getSequenceNo());
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;
							}		
						}
					}
				}
			}
			else if (((CbctlButton) control).getName().equalsIgnoreCase("cancel")) {
				if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
					TopMiddleController.getCurrentComposite().dispose();
				}
			}
			}
			else
			{

				System.out.println("inside touch screen radio button event");
				reprintComposite.getButtonGroup().setSelectedButton(
						(TouchScreenRadioButton) control);
				populateForm(reprintComposite);
			}
		}
		catch(SlipsEngineServiceException serviceException){
			MessageDialogUtil
			.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
		}
		catch(Exception ex){
			log.error(ex);
		}
	}*/

	/**
	 * ReprintMouseListener class
	 *
	 */
	private class ReprintMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			
		}

		public void mouseDown(MouseEvent e) {
			try {
				Control control = (Control) e.getSource();

				if (!(control instanceof SDSImageLabel)
						&& !(control instanceof SDSRadioImageLabel)) {
					return;
				}

				if (control instanceof SDSImageLabel) {
					if(((SDSImageLabel)control).getName().equalsIgnoreCase("reprint")){
						

						
						/* Populating the form with screen values */
						populateForm(reprintComposite);
						
						List<String> fieldNames = new ArrayList<String>();
						
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
						}
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
						}				
//						if(form.getSequenceNo()!=null && (!form.getSequenceNo().equalsIgnoreCase(IAppConstants.PRIOR_SLIP_REPRINT) 
//								|| form.getSequenceNo().length()!=1)){
//							fieldNames.add(FormConstants.FORM_SEQUENCE_NO);
//						}
						
						if(form.getSequenceNo()!=null && (form.getSequenceNo().length()!=1)){
							fieldNames.add(FormConstants.FORM_SEQUENCE_NO);
						}
						
						boolean validate = validate("ReprintForm", fieldNames, form, reprintComposite);
						filter.setSlipTypeId(IAppConstants.BEEF_SLIP_TYPE_ID);
						log.info("SLIP TYPE: "+filter.getSlipTypeId());
						/* Populating the form with screen values */

						if(!validate){
							log.info("Client side validation failed");
							return;
						}
						SessionUtility sessionUtility = new SessionUtility();
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
								&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							/*if(form.getEmpPassword().length() < 5){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
								reprintComposite.getTxtEmployeePwd().forceFocus();
								return;
							}
							else{*/
								try{
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmpPassword(), MainMenuController.slipForm.getSiteId());
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
														|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
											
											log.info("Invalid emp id");	
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
											//reprintComposite.getTxtEmployeeId().forceFocus();
											reprintComposite.getTxtEmployeeId().forceFocus();
											return;
										}
										else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
			                                    
											log.info("Invalid emp pwd");	
		                                	ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											reprintComposite.getTxtEmployeePwd().setText("");
											reprintComposite.getTxtEmployeePwd().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isReprintAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
											
											log.info("User does not have PROCESS REPRINT FUNCTION");										
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED));
											reprintComposite.getTxtEmployeePwd().setText("");
											reprintComposite.getTxtEmployeeId().forceFocus();
											return;																	
										}
										else{
											MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										}
									}else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
										reprintComposite.getTxtEmployeeId().forceFocus();
										return;
									}
								}catch(Exception ex){
									log.error("Exception while checking authenticateUser with framework", ex);
									SlipsExceptionUtil.getGeneralCtrllerException(ex);							
									log.error("Exception while checking the user authentication",ex);
									return;
								}
							//}
						}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							/*if(form.getEmpPassword().length() < 5){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
								reprintComposite.getTxtEmployeePwd().forceFocus();
								return;
							}
							else{*/
								try{
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.slipForm.getActorLogin(), form.getEmpPassword(), MainMenuController.slipForm.getSiteId());
									log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
									log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
									if(userDtoEmpPasswordChk!=null){
										if(userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey()))
										{
											log.info("Invalid emp pwd");	
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											reprintComposite.getTxtEmployeePwd().forceFocus();
											return;
										}else if(!UserFunctionsUtil.isReprintAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
											
											log.info("User does not have PROCESS REPRINT FUNCTION");										
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
											reprintComposite.getTxtEmployeePwd().setText("");
											reprintComposite.getTxtEmployeePwd().forceFocus();
											return;																	
										}
										else{
											MainMenuController.slipForm.setEmployeeIdPrintedSlip(MainMenuController.slipForm.getActorLogin());
										}
									}else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
										reprintComposite.getTxtEmployeePwd().forceFocus();
										return;
									}
								}catch(Exception ex){
									SlipsExceptionUtil.getGeneralCtrllerException(ex);							
									log.error("Exception while checking the user authentication",ex);
									return;
								}
							//}
						}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							try{
								log.info("Called framework's authenticate user method");
								UserDTO userDTOEmpIdCheck = sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.slipForm.getSiteId());
								if(userDTOEmpIdCheck!=null){
									log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
									log.info(userDTOEmpIdCheck.getMessageKey());
									if (userDTOEmpIdCheck.isErrorPresent() && userDTOEmpIdCheck.getMessageKey() != null && 
											(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDTOEmpIdCheck.getMessageKey())
													|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDTOEmpIdCheck.getMessageKey())
													|| IAppConstants.ANA_MESSAGES_USER_NOT_PRESENT.equalsIgnoreCase(userDTOEmpIdCheck.getMessageKey())))
									{
										log.info("Invalid emp id");	
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
										reprintComposite.getTxtEmployeeId().forceFocus();
										return;
									}
									else if(!UserFunctionsUtil.isReprintAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
										
										log.info("Invalid emp pwd");	
										log.info("User does not have PROCESS REPRINT FUNCTION");										
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED));
										reprintComposite.getTxtEmployeeId().forceFocus();
										return;																	
									}
									else{
										MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
									}
								}
								else{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;
								}
							}catch(Exception ex){
								SlipsExceptionUtil.getGeneralCtrllerException(ex);							
								log.error("Exception while checking the user authentication",ex);
								return;
							}
						}else if(!UserFunctionsUtil.isReprintAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
							
							log.info("User does not have PROCESS REPRINT FUNCTION");										
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;																	
						}

						//	PRIOR SLIP REPRINT LOGIC
						/*if(form.getSequenceNo().equalsIgnoreCase(IAppConstants.PRIOR_SLIP_REPRINT)) {
							SlipsDTO slipsDTO = null;
							try {								
								slipsDTO = (SlipsDTO)SlipsServiceLocator.getService().getReprintPriorSlipDetails(MainMenuController.slipForm.getSiteId());
								String postToAccounting = ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING;
								if(slipsDTO != null && slipsDTO.getPostToAccounting() != null){
									postToAccounting = slipsDTO.getPostToAccounting();
								}
								if(slipsDTO != null && slipsDTO.isPriorSlipVoided()) {
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+ " "
												+slipsDTO.getSequenceNumber() +" "+LabelLoader.getLabelValue(MessageKeyConstants.PRIOR_SLIP_VOIDED_ENTER_MANUALLY));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;
								}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("YES")
										&& postToAccounting.equalsIgnoreCase(ILookupTableConstants.POSTED_TO_ACCOUNTING)){
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenInfoDialog(
											LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PROCESSED));
									reprintComposite.getTxtSequenceNo().forceFocus();
									if(log.isInfoEnabled()) {
										log.info("Slip sequence " + form.getSequenceNo() + " is already processed");
									}

									return;
								}else if(slipsDTO != null && slipsDTO.getSequenceNumber() != 0 
										&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("YES")) {
									
									if(slipsDTO.getStatusFlagId()==ILookupTableConstants.PRINTED_STATUS_ID
											||slipsDTO.getStatusFlagId()==ILookupTableConstants.REPRINT_STATUS_ID
											&& postToAccounting.equalsIgnoreCase(ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING)) {
										
										//ALLOW TO REPRINT THE SLIP
										form.setReprintJackpotSlipDTO(slipsDTO);
										MainMenuController.slipForm.setSequenceNumber(slipsDTO.getSequenceNumber());
										form.setSequenceNo(Long.toString(slipsDTO.getSequenceNumber()));
										filter.setSiteId(MainMenuController.slipForm.getSiteId());
										filter.setSlotNumber(slipsDTO.getAssetConfigNumber());
										reprintSlip();
										
									}else{
										//WHEN THE STATUS IS PROCESSED,
										//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS ALREADY PROCESSED
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenInfoDialog(
												LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PROCESSED));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;							
									}							
								}else if(slipsDTO != null && slipsDTO.getSequenceNumber() != 0 
										&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("NO")) {
									
									if(slipsDTO.getStatusFlagId()==ILookupTableConstants.PROCESSED_STATUS_ID
											||slipsDTO.getStatusFlagId()==ILookupTableConstants.REPRINT_STATUS_ID) {
										
										//ALLOW TO REPRINT THE SLIP
										form.setReprintJackpotSlipDTO(slipsDTO);
										MainMenuController.slipForm.setSequenceNumber(slipsDTO.getSequenceNumber());
										form.setSequenceNo(Long.toString(slipsDTO.getSequenceNumber()));
										filter.setSiteId(MainMenuController.slipForm.getSiteId());
										filter.setSlotNumber(slipsDTO.getAssetConfigNumber());
										reprintSlip();								
										
									}else{
										//WHEN THE STATUS IS PRINTED (will not occur if site is NO)
										//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS IN PRINTED STATE
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenInfoDialog(
												LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;		
									}						
								}
								else{
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenInfoDialog(
											LabelLoader.getLabelValue(MessageKeyConstants.NO_PRIOR_SLIPS_TO_REPRINT));
									reprintComposite.getTxtSequenceNo().forceFocus();
									return;
								}					
								
							} catch (SlipsEngineServiceException exception) {
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(exception, exception.getMessage());
								MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;
							} catch (Exception e2) {
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							} finally {
								ProgressIndicatorUtil.closeInProgressWindow();
							}
						} 
						else {*/
							// LOGIC WHEN THE INPUT IS SEQUENCE NUMBER
							short statusId = 0;
							SlipsDTO slipsDTO = null;
							String postToAccounting = ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING;
							try {
								
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								log.info("Called getSlipStatus web method");
								statusId = SlipsServiceLocator.getService().getSlipStatusId(
										Long.parseLong(form.getSequenceNo()), filter);
								slipsDTO = SlipsServiceLocator.getService().getSlipsDetails(
										MainMenuController.slipForm.getSiteId(), Long.parseLong(form.getSequenceNo()));
								if(slipsDTO != null && slipsDTO.getPostToAccounting() != null){
									postToAccounting = slipsDTO.getPostToAccounting();
								}
							}
							catch (SlipsEngineServiceException e1) {
								log.info("EXCEPTION calling getSlipStatus/getSlipsDetails for REPRINT", e1);
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1, e1.getMessage());
								return;
							}
							catch (Exception e2) {
								log.info("EXCEPTION calling getSlipStatus/getSlipsDetails for REPRINT", e2);
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
								return;
							}
							if(statusId==0){
								log.debug("Returned value is null as de seq no is not present in the DB");
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
								reprintComposite.getTxtSequenceNo().forceFocus();
								return;
							}
							else{
								log.debug("*************** Slip Status Id: " + statusId+ " **************************");

								if (statusId ==  ILookupTableConstants.VOID_STATUS_ID ) {
									
									log.info("Slip is already voided hence cannot be reprinted");
									MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue
											(MessageKeyConstants.SLIP_ALREADY_VOIDED));
									reprintComposite.getTxtSequenceNo().forceFocus();
									return;
								}else if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("YES")) {
									
									if(statusId == ILookupTableConstants.PRINTED_STATUS_ID
											||statusId == ILookupTableConstants.REPRINT_STATUS_ID
											&& postToAccounting.equalsIgnoreCase(ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING)) {
										
										//ALLOW TO REPRINT THE SLIP
										filter.setSiteId(MainMenuController.slipForm.getSiteId());
										filter.setSlotNumber(slipsDTO.getAssetConfigNumber());
										log.info("Calling reprint slip.....");
										reprintSlip();
										
									}else if(statusId == ILookupTableConstants.PROCESSED_STATUS_ID
											&& postToAccounting.equalsIgnoreCase(ILookupTableConstants.POSTED_TO_ACCOUNTING)){
										//WHEN THE STATUS IS PROCESSED,
										//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS ALREADY PROCESSED
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenInfoDialog(
												LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PROCESSED));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;	
									}else if(statusId == ILookupTableConstants.REPRINT_STATUS_ID
											&& postToAccounting.equalsIgnoreCase(ILookupTableConstants.POSTED_TO_ACCOUNTING)){
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenInfoDialog(
												LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PROCESSED));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;	
									}else {
										log.info("Reprint - Slip is not found for seq: "+form.getSequenceNo());
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;
									}
								}else if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("NO")) {
									
									if(statusId == ILookupTableConstants.PROCESSED_STATUS_ID
											||statusId == ILookupTableConstants.REPRINT_STATUS_ID) {
										
										//ALLOW TO REPRINT THE SLIP
										filter.setSiteId(MainMenuController.slipForm.getSiteId());
										filter.setSlotNumber(slipsDTO.getAssetConfigNumber());
										log.info("Calling reprint slip.....");
										reprintSlip();							
										
									}else if(statusId == ILookupTableConstants.PRINTED_STATUS_ID){
										//WHEN THE STATUS IS PRINTED (will not occur if site is NO)
										//DISPLAY A MSG PROMPT THAT THE SLIP CAN'T BE REPRINTED AS IT IS IN PRINTED STATE
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenInfoDialog(
												LabelLoader.getLabelValue(MessageKeyConstants.REPRINT_SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;	
									}else {
										log.info("Reprint - Slip is not found for seq: "+form.getSequenceNo());
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
										reprintComposite.getTxtSequenceNo().forceFocus();
										return;
									}		
								}
							}
						//}
						
					}
				}
			} catch (SlipsEngineServiceException serviceException) {				
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
						.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
			} catch (Exception ex) {				
				log.error(ex);
			}
		}

		public void mouseUp(MouseEvent e) {			
			
		}
		
	}
	
	
	/**
	 * This method is used to register customized listeners
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ReprintMouseListener listener = new ReprintMouseListener();
		reprintComposite.getButtonComposite().getBtnReprint().addMouseListener(listener);
		reprintComposite.getButtonComposite().getBtnReprint().getTextLabel().addTraverseListener(this);

	}

	/**
	 * A method to create ReprintComposite
	 * @param p
	 * @param s
	 * @param paramFlags
	 */
	public void createReprintComposite(Composite p, int s,boolean[] paramFlags)
	{
		reprintComposite = new ReprintComposite(p,s,paramFlags);
	}

	/**
	 * A method to get the boolean values based on the configuration parameters
	 * enabled/disabled
	 * @return paramFlags
	 */
	public boolean[] getSiteConfigurationParameters() {
		boolean[] paramFlags = new boolean[2];
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
			paramFlags[0] = true;
		} else {
			paramFlags[0] = false;
		}
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
			paramFlags[1] = true;
		} else {
			paramFlags[1] = false;
		}
		log.debug("Employee Id Enabled..." + paramFlags[0]);
		log.debug("Employee Password Enabled..." + paramFlags[0]);
		return paramFlags;
	}

	/**
	 * This method returns ReprintComposite
	 */
	@Override
	public Composite getComposite() {
		return reprintComposite;
	}

	/**
	 * A method to get the keyboard focus
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		super.focusLost(e);
		reprintComposite.setRedraw(true);
		reprintComposite.redraw();
	}
	
	/**
	 * @return the form
	 */
	public ReprintForm getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(ReprintForm form) {
		this.form = form;
	}


	private void reprintSlip() {
		boolean reprintPosted = false;
		try{
			String printerUsed = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
			if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
				reprintPosted = SlipsServiceLocator.getService().postSlipReprint(new Long(form.getSequenceNo()),printerUsed, filter, form.getEmployeeId());
			}else{
				reprintPosted = SlipsServiceLocator.getService().postSlipReprint(new Long(form.getSequenceNo()),printerUsed, filter, MainMenuController.slipForm.getActorLogin());
			}
		}catch (SlipsEngineServiceException exception) {
			log.info("EXCEPTION calling postSlipReprint", exception);	
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(exception,  exception.getMessage());	
			return;
		}
		catch (Exception exception) {
			log.info("EXCEPTION calling postSlipReprint", exception);	
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(exception,  MessageKeyConstants.SERVICE_DOWN);	
			return;
		}

		if(reprintPosted)
		{			
			filter.setSiteId(MainMenuController.slipForm.getSiteId());
			filter.setType("getJackpotDetailsForSequenceNumber");

			SlipsDTO reprintSlipDTO = new SlipsDTO();
			try {
				log.info("Called getReprintSlipDetails web method");
				reprintSlipDTO = SlipsServiceLocator.getService().getReprintSlipDetails(Long.parseLong(form.getSequenceNo()), filter);

				if(reprintSlipDTO !=null)
				{	
					// BUILD SLIP IMAGE AND PRINT SLIP
					SlipImageBuilderUtility printUtil = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_BEEF);
					printUtil.buildSlipImage(reprintSlipDTO);
					
					log.info("Slip has been reprinted successfully");
					StringBuilder msg=new StringBuilder("");
					msg.append(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPRINT_SUCCESS));
					msg.append(" ");
					msg.append(LabelLoader.getLabelValue(MessageKeyConstants.FOR_THE_SEQUENCE));
					msg.append(" ");
					msg.append(form.getSequenceNo());
					msg.append(".");
					
					MessageDialogUtil.displayTouchScreenInfoDialog(msg.toString());					
					
					form.setReprintJackpotSlipDTO(reprintSlipDTO);
				}
			}
			catch (SlipsEngineServiceException e1) {
				log.info("EXCEPTION calling getReprintSlipDetails", e1);
				SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
				handler.handleException(e1, e1.getMessage());
				return;
			}
			catch (Exception e2) {
				log.info("SERVICE_DOWN calling getReprintSlipDetails", e2);	
				SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
				return;
			}

			try{
				log.info("BEFORE SENDING AUDIT TRAIL INFO for REPRINT");
				Util.sendDataToAuditTrail(IAuditTrailConstants.AUDIT_MODULE_NAME,
					LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_DISPUTE_SCR_NAME),
					null,
					null,
					null,
					LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID)+": "+ MainMenuController.slipForm.getEmployeeIdPrintedSlip()
					+LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER)+": "+reprintSlipDTO.getAssetConfigNumber()
					+LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+": "+form.getSequenceNo(),
					EnumOperation.REPRINT_BEEF, reprintSlipDTO.getAssetConfigNumber());		
				log.info("AFTER SENDING AUDIT TRAIL INFO");
			}catch (Exception ex) {
				log.error("Exception when sending audit trail info",ex);
			}
			
		}else{
			log.info("Reprint of the slip has failed");
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_REPRINT_FAILED));
		}
		CallInitialScreen callInitialScreen = new CallInitialScreen();
		callInitialScreen.callReprintScreen();
	}
}
