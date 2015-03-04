/*****************************************************************************
 * $Id: VoidController.java,v 1.43, 2011-03-05 10:37:03Z, Ambereen Drewitt$
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

import java.text.DecimalFormat;

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
import com.ballydev.sds.slips.dto.SlipsCashlessAccountDTO;
import com.ballydev.sds.slips.dto.SlipsDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slips.filter.SlipsFilter;
import com.ballydev.sds.slips.util.SlipsOperationType;
import com.ballydev.sds.slipsui.composite.VoidComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.IAuditTrailConstants;
import com.ballydev.sds.slipsui.constants.ILookupTableConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.form.VoidForm;
import com.ballydev.sds.slipsui.print.SlipImageBuilderUtility;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.ConversionUtil;
import com.ballydev.sds.slipsui.util.FocusUtility;
import com.ballydev.sds.slipsui.util.SlipUILogger;
import com.ballydev.sds.slipsui.util.SlipsExceptionUtil;
import com.ballydev.sds.slipsui.util.UserFunctionsUtil;

/**
 * This class acts as a controller for all the events performed in the
 * VoidComposite
 *
 */
public class VoidController extends SDSBaseController {

	/**
	 * VoidForm instance
	 */
	private VoidForm form;

	/**
	 * VoidComposite instance
	 */
	private VoidComposite voidComposite;
	/**
	 * SlipsFilter instance
	 */
	private SlipsFilter filter =new SlipsFilter();

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * VoidController constructor
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public VoidController(Composite parent, int style, VoidForm form,
			SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		//Since Fill and Bleed are not there
		this.form.setBeef(true);
		createVoidComposite(parent, style, getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(voidComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
		form.setFill(true);
		super.registerEvents(voidComposite);
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(voidComposite);
		FocusUtility.setTextFocus(voidComposite);
	}
/*
	*//**
	 * This method is used to perform action for widgetselected event
	 *//*
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			Control control = (Control) e.getSource();

			if (!(control instanceof CbctlButton)
					&& !(control instanceof TouchScreenRadioButton)) {
				return;
			}

			if (control instanceof CbctlButton) {
			if (((CbctlButton) control).getName().equalsIgnoreCase("void")) {
				try {
					
					 Populating the form with screen values 
					populateForm(voidComposite);
					
					boolean validate = validate("VoidForm", form, voidComposite);
					filter.setSlipTypeId(IAppConstants.BEEF_SLIP_TYPE_ID);
					filter.setSiteId(MainMenuController.slipForm.getSiteId());

					if(!validate){
						log.info("Client side validation failed");
						return;
					}
				} catch (Exception e3) {
					log.error(e3);
				}
				SessionUtility sessionUtility = new SessionUtility();
				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
						&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
					if(form.getEmployeePwd().length() < 5){
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
						voidComposite.getTxtEmployeePwd().forceFocus();
						return;
					}
					else{
						try{
							log.info("Called framework's authenticate user method");
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmployeePwd(), MainMenuController.slipForm.getSiteId());
							if(userDtoEmpPasswordChk!=null){
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									voidComposite.getTxtEmployeeId().forceFocus();
									return;
								}
								else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
	                                    
                                	ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									voidComposite.getTxtEmployeePwd().setText("");
									voidComposite.getTxtEmployeePwd().forceFocus();
									return;
								}
								else if(!UserFunctionsUtil.isVoidAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
									
									log.info("User does not have PROCESS VOID FUNCTION");										
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED));
									voidComposite.getTxtEmployeePwd().setText("");
									voidComposite.getTxtEmployeeId().forceFocus();
									return;																	
								}
								else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
										&& !userDtoEmpPasswordChk.isSignedOn())
								{
									log.info("SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
									voidComposite.getTxtEmployeePwd().setText("");
									voidComposite.getTxtEmployeeId().forceFocus();
									return;																	
								}
								else{
									MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
								}
							}							
							else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
								voidComposite.getTxtEmployeeId().forceFocus();
								return;
							}
						}catch(Exception ex){
							SlipsExceptionUtil.getGeneralCtrllerException(ex);							
							log.error("Exception while checking the user authentication",ex);
							return;
						}
					//}
				}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
					if(form.getEmployeePwd().length() < 5){
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
						voidComposite.getTxtEmployeePwd().forceFocus();
						return;
					}
					else{
						try{
							log.info("Called framework's authenticate user method");
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.slipForm.getActorLogin(), form.getEmployeePwd(), MainMenuController.slipForm.getSiteId());
							log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
							log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
							if(userDtoEmpPasswordChk!=null){
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
		                                    
                                	ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									voidComposite.getTxtEmployeePwd().setText("");
									voidComposite.getTxtEmployeePwd().forceFocus();
									return;
								}
								else if(!UserFunctionsUtil.isVoidAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
									
									log.info("User does not have PROCESS VOID FUNCTION");										
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
									voidComposite.getTxtEmployeePwd().setText("");
									voidComposite.getTxtEmployeePwd().forceFocus();
									return;																	
								}
								else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes"))
								{
									System.out.println("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
									if(!userDtoEmpPasswordChk.isSignedOn()){
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
										voidComposite.getTxtEmployeePwd().setText("");
										voidComposite.getTxtEmployeePwd().forceFocus();
										return;
									}								
								}else{
									MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
								}
							}							
							else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
								voidComposite.getTxtEmployeePwd().forceFocus();
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
						UserDTO userDTOEmpIdCheck= sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.slipForm.getSiteId());
						if(userDTOEmpIdCheck!=null){
							log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
							if(userDTOEmpIdCheck.getUserName()==null){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								voidComposite.getTxtEmployeeId().forceFocus();
								return;
							}
							else if(!UserFunctionsUtil.isVoidAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
								
								log.info("User does not have PROCESS VOID FUNCTION");										
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED));
								voidComposite.getTxtEmployeeId().forceFocus();
								return;																	
							}
							else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes"))
							{
								System.out.println("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
								if(!userDTOEmpIdCheck.isSignedOn()){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
									voidComposite.getTxtEmployeeId().forceFocus();
									return;
								}								
							}
							else{
								MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
							}
						}else{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
							voidComposite.getTxtEmployeeId().forceFocus();
							return;
						}
					}catch(Exception ex){
						SlipsExceptionUtil.getGeneralCtrllerException(ex);							
						log.error("Exception while checking the user authentication",ex);
						return;
					}
				}else if(!UserFunctionsUtil.isVoidAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
					
					log.info("User does not have PROCESS VOID FUNCTION");										
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
					voidComposite.getTxtSequenceNo().forceFocus();
					return;																	
				}
				
				short statusId = 0;
				try {
					log.info(" getSlipStatus is called");
					statusId = SlipsServiceLocator.getService().getSlipStatusId(Long.parseLong(form.getSequenceNo()), filter);
				}
				catch (SlipsEngineServiceException e1) {
					log.error("Exception while calling getSlipStatus for VOID", e1);
					SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
					handler.handleException(e1, e1.getMessage());
					return;
				}
				catch (Exception e2) {
					log.error("SERVICE_DOWN while calling getSlipStatus for VOID", e2);
					SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
					handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
					return;
				}
				
				if(statusId==0){
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
					log.info("The slip is not found");
					voidComposite.getTxtSequenceNo().forceFocus();
					return;
				}
				else if (statusId == ILookupTableConstants.PROCESSED_STATUS_ID 
						|| statusId == ILookupTableConstants.REPRINT_STATUS_ID
						|| statusId == ILookupTableConstants.PRINTED_STATUS_ID)
				{
					*//** To change the stauts of the slip to void *//*
					boolean status = false;
					try {
						log.info("postSlipVoidStatus is called");
						filter.setSiteId(MainMenuController.slipForm.getSiteId());
						String printerUsed = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							status = SlipsServiceLocator.getService().postSlipVoidStatus(Long.parseLong(form.getSequenceNo()),printerUsed, filter, form.getEmployeeId());
						}else{
							status = SlipsServiceLocator.getService().postSlipVoidStatus(Long.parseLong(form.getSequenceNo()),printerUsed, filter, MainMenuController.slipForm.getActorLogin());
						}
					}
					catch (SlipsEngineServiceException e1) {
						log.error("Exception while calling postSlipVoidStatus", e1);
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(e1, e1.getMessage());
						return;
					}
					catch (Exception e2) {
						log.error("SERVICE_DOWN while calling postSlipVoidStatus", e2);
						SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
						handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
						return;
					}
					if (status) {
						MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_SUCCESS));
						filter.setSiteId(MainMenuController.slipForm.getSiteId());
						filter.setSequenceNumber(Long.parseLong(form.getSequenceNo()));
						filter.setType("getJackpotDetailsForSequenceNumber");

						SlipsDTO slipsDTO = null;
						try {
							slipsDTO = SlipsServiceLocator.getService().getVoidSlipDetails(Long.parseLong(form.getSequenceNo()), filter);
						} catch (SlipsEngineServiceException e1) {
							log.error("Exception while calling getVoidSlipDetails", e1);
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							return;
						}
						catch (Exception e2) {
							log.error("SERVICE_DOWN while calling getVoidSlipDetails", e2);
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
							return;
						}
						if(slipsDTO !=null)
						{

							SlipImage slipImage = new SlipImage();
							String image = slipImage.buildSlipImage(slipsDTO, PrinterConstants.SLIP_TYPE_VOID);
							SlipPrinting slipPrinting = new SlipPrinting();
							slipPrinting.printSlip(image);
							printDTO.setSlipSchema(slipsDTO.getSlipSchema());
							printDTO.setPrinterSchema(slipsDTO.getPrinterSchema());
						}

						Timestamp printDate = new Timestamp(System.currentTimeMillis());
						String str1[] = (printDate.toString().trim().replace(".", "_"))
						.split("_");
						printDTO.setPrintDate(str1[0]);
						printDTO.setSequenceNumber(Long.toString(MainMenuController.slipForm.getSequenceNumber()));
						SlipImage slipImage = new SlipImage();
						String image = slipImage.buildSlipImage(printDTO, PrinterConstants.SLIP_TYPE_VOID);
						SlipPrinting slipPrinting = new SlipPrinting();
						slipPrinting.printSlip(image);			
						
						String empID;
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
								&& form.getEmployeeId()!=null
								&& form.getEmployeeId().length()>0) {
							empID=form.getEmployeeId();
						}
						else {							
							empID=MainMenuController.slipForm.getActorLogin();
						}
						
						slipsDTO.setSlotDenomination(slipsDTO.getSlotDenomination());
						UserDTO userDTO=sessionUtility.getUserDetails(empID, MainMenuController.slipForm.getSiteId());
						StringBuilder uname=new StringBuilder("");
						uname.append(userDTO.getFirstName());
						uname.append(" ");
						uname.append(userDTO.getLastName());
						slipsDTO.setEmployeeName(uname.toString());
						slipsDTO.setPrintEmployeeLogin(empID);
											
						
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PRINT_VOID_SLIP_FOR_VOID_COMMAND).equalsIgnoreCase("yes")){
							if(slipsDTO != null) {
								// BUILD SLIP IMAGE AND PRINT SLIP
								SlipImageBuilderUtility slipImageBuilder = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_VOID);
								slipImageBuilder.buildSlipImage(slipsDTO);
							}
						}
						try{
							log.info("BEFORE SENDING AUDIT TRAIL INFO for VOID");
							Util.sendDataToAuditTrail(IAuditTrailConstants.AUDIT_MODULE_NAME,
								IAuditTrailConstants.VOID_SCREEN,
								IAuditTrailConstants.SLIP_AMOUNT_FIELD,
								String.valueOf(new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(slipsDTO.getSlipAmount()))),
								String.valueOf(0),
								LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID)+": "+ MainMenuController.slipForm.getEmployeeIdPrintedSlip()
									+LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER)+": "+slipsDTO.getAssetConfigNumber()
									+LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+": "+form.getSequenceNo(),
								EnumOperation.VOID_BEEF, slipsDTO.getAssetConfigNumber());		
							log.info("AFTER SENDING AUDIT TRAIL INFO");
						}catch (Exception ex) {
							log.error("Exception when sending audit trail info",ex);
						}	
						
					} else {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_FAILED));
						log.info("Void for the slip sequence : "+form.getSequenceNo()+ "FAILED");
						voidComposite.getTxtSequenceNo().forceFocus();
						return;
					}
				}
				else if (statusId == ILookupTableConstants.VOID_STATUS_ID) {
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_ALREADY_VOIDED));
					log.info("Void for the slip sequence : "+form.getSequenceNo()+ "is already voided");
					voidComposite.getTxtSequenceNo().forceFocus();
					return;
				} else {
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
					log.info("Void for the slip sequence : "+form.getSequenceNo()+ "is not printed yet");
					voidComposite.getTxtSequenceNo().forceFocus();
					return;
				}
			}
			else if (((CbctlButton) control).getName().equalsIgnoreCase("cancel")) {
				if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
					TopMiddleController.getCurrentComposite().dispose();
				}
			}
			CallInitialScreen callInitialScr = new CallInitialScreen();
			callInitialScr.callVoidScreen();
			}
			else if(voidComposite.getSlipTypeButtonGroup().getGroupName().equalsIgnoreCase("slipType"))
			{
				voidComposite.getSlipTypeButtonGroup().setSelectedButton(
						(TouchScreenRadioButton) control);
				populateForm(voidComposite);
			}
		} catch (Exception ex) {
			log.error(ex);
		}
	}
*/
	/**
	 * VoidMouseListener class
	 *
	 */
	private class VoidMouseListener implements MouseListener{

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
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("void")) {
						try {
							
							/* Populating the form with screen values */
							populateForm(voidComposite);
							
							boolean validate = validate("VoidForm", form, voidComposite);
							filter.setSlipTypeId(IAppConstants.BEEF_SLIP_TYPE_ID);
							filter.setSiteId(MainMenuController.slipForm.getSiteId());

							if(!validate){
								log.info("Client side validation failed");
								return;
							}
						} catch (Exception e3) {
							log.error(e3);
						}
						SessionUtility sessionUtility = new SessionUtility();
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
								&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							/*if(form.getEmployeePwd().length() < 5){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
								voidComposite.getTxtEmployeePwd().forceFocus();
								return;
							}
							else{*/
								try{
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmployeePwd(), MainMenuController.slipForm.getSiteId());
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
														|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
											log.info("Invalid emp id");
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
											voidComposite.getTxtEmployeeId().forceFocus();
											return;
										}
										else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
			                                    
											log.info("Invalid emp pwd");
		                                	ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeePwd().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isVoidAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
											
											log.info("User does not have PROCESS VOID FUNCTION");										
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeeId().forceFocus();
											return;																	
										}
										else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
												&& !userDtoEmpPasswordChk.isSignedOn())
										{
											log.info("SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeeId().forceFocus();
											return;																	
										}
										else{
											MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										}
									}							
									else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;
									}
								}catch(Exception ex){
									SlipsExceptionUtil.getGeneralCtrllerException(ex);							
									log.error("Exception while checking the user authentication",ex);
									return;
								}
							//}
						}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							/*if(form.getEmployeePwd().length() < 5){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
								voidComposite.getTxtEmployeePwd().forceFocus();
								return;
							}
							else{*/
								try{
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.slipForm.getActorLogin(), form.getEmployeePwd(), MainMenuController.slipForm.getSiteId());
									log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
									log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
				                                    
											log.info("Invalid emp pwd");
		                                	ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeePwd().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isVoidAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
											
											log.info("User does not have PROCESS VOID FUNCTION");										
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeePwd().forceFocus();
											return;																	
										}
										else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
												&& !userDtoEmpPasswordChk.isSignedOn())
										{
											log.info("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
											voidComposite.getTxtEmployeePwd().setText("");
											voidComposite.getTxtEmployeePwd().forceFocus();
											return;				
										}else{
											MainMenuController.slipForm.setEmployeeIdPrintedSlip(MainMenuController.slipForm.getActorLogin());
										}
									}							
									else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
										voidComposite.getTxtEmployeePwd().forceFocus();
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
								UserDTO userDTOEmpIdCheck= sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.slipForm.getSiteId());
								if(userDTOEmpIdCheck!=null){
									log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
									
									log.info(userDTOEmpIdCheck.getMessageKey());
									if(userDTOEmpIdCheck.isErrorPresent() && userDTOEmpIdCheck.getMessageKey() != null && 
											(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDTOEmpIdCheck.getMessageKey())
													|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDTOEmpIdCheck.getMessageKey())
													|| IAppConstants.ANA_MESSAGES_USER_NOT_PRESENT.equalsIgnoreCase(userDTOEmpIdCheck.getMessageKey())))
									{
										log.info("Invalid emp id");	
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;
									}
									else if(!UserFunctionsUtil.isVoidAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
										
										log.info("User does not have PROCESS VOID FUNCTION");										
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;																	
									}
									else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
											&& !userDTOEmpIdCheck.isSignedOn())
									{
										log.info("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
										voidComposite.getTxtEmployeeId().forceFocus();
										return;				
									}
									else{
										MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
									}
								}else{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									voidComposite.getTxtEmployeeId().forceFocus();
									return;
								}
							}catch(Exception ex){
								SlipsExceptionUtil.getGeneralCtrllerException(ex);							
								log.error("Exception while checking the user authentication",ex);
								return;
							}
						}else if(!UserFunctionsUtil.isVoidAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
							
							log.info("User does not have PROCESS VOID FUNCTION");										
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
							voidComposite.getTxtSequenceNo().forceFocus();
							return;																	
						}
						
						short statusId = 0;
						String postToAccountingFlg = IAppConstants.POST_TO_ACC_FLG_NO;
						try {
							log.info(" getSlipStatus is called");
							SlipsDTO slipsStatusDTO = SlipsServiceLocator.getService().getSlipStatusId(Long.parseLong(form.getSequenceNo()), MainMenuController.slipForm.getSiteId());
							if(slipsStatusDTO!=null) {
								statusId = slipsStatusDTO.getStatusFlagId();
								MainMenuController.slipForm.setCashlessAccountNumber(slipsStatusDTO.getCashlessAccountNumber());
								postToAccountingFlg = slipsStatusDTO.getPostToAccounting();
							}
						}
						catch (SlipsEngineServiceException e1) {
							log.error("Exception while calling getSlipStatus for VOID", e1);
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							return;
						}
						catch (Exception e2) {
							log.error("SERVICE_DOWN while calling getSlipStatus for VOID", e2);
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
							return;
						}
						
						if(statusId==0){
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
							log.info("The slip is not found");
							voidComposite.getTxtSequenceNo().forceFocus();
							return;
						}else if (statusId == ILookupTableConstants.PROCESSED_STATUS_ID 
								|| statusId == ILookupTableConstants.REPRINT_STATUS_ID
								|| statusId == ILookupTableConstants.PRINTED_STATUS_ID)
						{
							//VALIDATE ACCOUNT NO ONLY FOR A VOID OPERATION DONE ON A PROCESSED SLIP 
							//SO CHECK THE POST TO ACCOUNTING FLAG
							if(IAppConstants.POST_TO_ACC_FLG_YES.equalsIgnoreCase(postToAccountingFlg)) {
								
								if (MainMenuController.slipForm.getCashlessAccountNumber()!=null && MainMenuController.slipForm.getCashlessAccountNumber().length()>0
										&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ALLOW_AUTOMATIC_DEPOSIT_TO_ECASH).equalsIgnoreCase("yes")) {
									SlipsCashlessAccountDTO slipCashlessAccDTO = SlipsServiceLocator.getService().validateCashlessAccountNo(MainMenuController.slipForm.getSiteId(), MainMenuController.slipForm.getCashlessAccountNumber(), SlipsOperationType.VOID);
									if(slipCashlessAccDTO!=null && !slipCashlessAccDTO.isSuccess()) {
										log.info("ERROR: Invalid/Non Active Acc No assigned to the slip for void: "+MainMenuController.slipForm.getCashlessAccountNumber());
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_ACCOUNT_NUMBER_FOR_VOID, new Object[] {MainMenuController.slipForm.getCashlessAccountNumber()}));
										voidComposite.getTxtSequenceNo().forceFocus();							
										return;
									}
								}else if (MainMenuController.slipForm.getCashlessAccountNumber()!=null && MainMenuController.slipForm.getCashlessAccountNumber().length()>0
										&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ALLOW_AUTOMATIC_DEPOSIT_TO_ECASH).equalsIgnoreCase("no")) {
									log.warn("WARN: Post to Ecash is disabled but an account no is associated to the slip");
								}
							}						
							
							/** To change the status of the slip to void */
							boolean status = false;
							try {
								log.info("postSlipVoidStatus is called");
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								String printerUsed = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
								if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
									status = SlipsServiceLocator.getService().postSlipVoidStatus(Long.parseLong(form.getSequenceNo()),printerUsed, filter, form.getEmployeeId());
								}else{
									status = SlipsServiceLocator.getService().postSlipVoidStatus(Long.parseLong(form.getSequenceNo()),printerUsed, filter, MainMenuController.slipForm.getActorLogin());
								}
							}
							catch (SlipsEngineServiceException e1) {
								log.error("Exception while calling postSlipVoidStatus", e1);
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1, e1.getMessage());
								return;
							}
							catch (Exception e2) {
								log.error("SERVICE_DOWN while calling postSlipVoidStatus", e2);
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
								return;
							}
							if (status) {
								MessageDialogUtil
										.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_SUCCESS));
								filter.setSiteId(MainMenuController.slipForm.getSiteId());
								filter.setSequenceNumber(Long.parseLong(form.getSequenceNo()));
								filter.setType("getJackpotDetailsForSequenceNumber");

								SlipsDTO slipsDTO = null;
								try {
									slipsDTO = SlipsServiceLocator.getService().getVoidSlipDetails(Long.parseLong(form.getSequenceNo()), filter);
								} catch (SlipsEngineServiceException e1) {
									log.error("Exception while calling getVoidSlipDetails", e1);
									SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
									handler.handleException(e1, e1.getMessage());
									return;
								}
								catch (Exception e2) {
									log.error("SERVICE_DOWN while calling getVoidSlipDetails", e2);
									SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
									handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN); // Chk on the message passed
									return;
								}
		/*						if(slipsDTO !=null)
								{

									SlipImage slipImage = new SlipImage();
									String image = slipImage.buildSlipImage(slipsDTO, PrinterConstants.SLIP_TYPE_VOID);
									SlipPrinting slipPrinting = new SlipPrinting();
									slipPrinting.printSlip(image);
									printDTO.setSlipSchema(slipsDTO.getSlipSchema());
									printDTO.setPrinterSchema(slipsDTO.getPrinterSchema());
								}

								Timestamp printDate = new Timestamp(System.currentTimeMillis());
								String str1[] = (printDate.toString().trim().replace(".", "_"))
								.split("_");
								printDTO.setPrintDate(str1[0]);
								printDTO.setSequenceNumber(Long.toString(MainMenuController.slipForm.getSequenceNumber()));
								SlipImage slipImage = new SlipImage();
								String image = slipImage.buildSlipImage(printDTO, PrinterConstants.SLIP_TYPE_VOID);
								SlipPrinting slipPrinting = new SlipPrinting();
								slipPrinting.printSlip(image);			*/
								
								String empID;
								if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
										&& form.getEmployeeId()!=null
										&& form.getEmployeeId().length()>0) {
									empID=form.getEmployeeId();
								}
								else {							
									empID=MainMenuController.slipForm.getActorLogin();
								}
								
								slipsDTO.setSlotDenomination(slipsDTO.getSlotDenomination());
								UserDTO userDTO=sessionUtility.getUserDetails(empID, MainMenuController.slipForm.getSiteId());
								StringBuilder uname=new StringBuilder("");
								uname.append(userDTO.getFirstName());
								uname.append(" ");
								uname.append(userDTO.getLastName());
								slipsDTO.setEmployeeName(uname.toString());
								slipsDTO.setPrintEmployeeLogin(empID);
													
								
								if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PRINT_VOID_SLIP_FOR_VOID_COMMAND).equalsIgnoreCase("yes")){
									if(slipsDTO != null) {
										// BUILD SLIP IMAGE AND PRINT SLIP
										SlipImageBuilderUtility slipImageBuilder = new SlipImageBuilderUtility(PrinterConstants.SLIP_TYPE_VOID);
										slipImageBuilder.buildSlipImage(slipsDTO);
									}
								}
								try{
									log.info("BEFORE SENDING AUDIT TRAIL INFO for VOID");
									Util.sendDataToAuditTrail(IAuditTrailConstants.AUDIT_MODULE_NAME,
											LabelLoader.getLabelValue(LabelKeyConstants.VOID_DISPUTE_SCR_NAME),
											LabelLoader.getLabelValue(LabelKeyConstants.SLIP_AMOUNT),
										String.valueOf(new DecimalFormat("0.00").format(ConversionUtil.centsToDollar(slipsDTO.getSlipAmount()))),
										String.valueOf(0),
										LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID)+": "+ MainMenuController.slipForm.getEmployeeIdPrintedSlip()
											+LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER)+": "+slipsDTO.getAssetConfigNumber()
											+LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER)+": "+form.getSequenceNo(),
										EnumOperation.VOID_BEEF, slipsDTO.getAssetConfigNumber());		
									log.info("AFTER SENDING AUDIT TRAIL INFO");
								}catch (Exception ex) {
									log.error("Exception when sending audit trail info",ex);
								}	
								
							} else {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_VOID_FAILED));
								log.info("Void for the slip sequence : "+form.getSequenceNo()+ "FAILED");
								voidComposite.getTxtSequenceNo().forceFocus();
								return;
							}
						}
						else if (statusId == ILookupTableConstants.VOID_STATUS_ID) {
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_ALREADY_VOIDED));
							log.info("Void for the slip sequence : "+form.getSequenceNo()+ "is already voided");
							voidComposite.getTxtSequenceNo().forceFocus();
							return;
						} else {
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
							log.info("Void for the slip sequence : "+form.getSequenceNo()+ "is not printed yet");
							voidComposite.getTxtSequenceNo().forceFocus();
							return;
						}
					
						
						
					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase(
							"cancel")) {
						if (TopMiddleController.getCurrentComposite() != null
								&& !(TopMiddleController.getCurrentComposite()
										.isDisposed())) {
							TopMiddleController.getCurrentComposite().dispose();
						}
					}
					CallInitialScreen callInitialScr = new CallInitialScreen();
					callInitialScr.callVoidScreen();
				} 
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
		VoidMouseListener listener = new VoidMouseListener();			
		voidComposite.getButtonComposite().getBtnVoid().addMouseListener(listener);
		voidComposite.getButtonComposite().getBtnVoid().getTextLabel().addTraverseListener(this);
	}

	/**
	 * A method to create VoidComposite
	 * @param p
	 * @param s
	 * @param paramFlags
	 */
	public void createVoidComposite(Composite p, int s, boolean[] paramFlags) {
		voidComposite = new VoidComposite(p, s, paramFlags);
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

		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
			paramFlags[1] = true;
		} else {
			paramFlags[1] = false;
		}

		log.debug("Employee Id Enabled..." + paramFlags[0]);
		log.debug("Employee Password Enabled..." + paramFlags[1]);
		return paramFlags;
	}

	/**
	 * This method returns VoidComposite
	 */
	@Override
	public Composite getComposite() {
		return voidComposite;
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
	
		
}
