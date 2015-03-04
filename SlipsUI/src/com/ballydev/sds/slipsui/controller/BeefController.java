/*****************************************************************************
 * $Id: BeefController.java,v 1.53, 2011-03-05 10:37:03Z, Ambereen Drewitt$
 * $Date: 3/5/2011 4:37:03 AM$
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
package com.ballydev.sds.slipsui.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.slips.dto.SlipsAssetInfoDTO;
import com.ballydev.sds.slips.dto.SlipsAssetParamType;
import com.ballydev.sds.slips.dto.SlipsCashlessAccountDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slips.util.SlipsOperationType;
import com.ballydev.sds.slipsui.composite.BeefComposite;
import com.ballydev.sds.slipsui.constants.FormConstants;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.form.BeefAuthorizationForm;
import com.ballydev.sds.slipsui.form.BeefForm;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.ConversionUtil;
import com.ballydev.sds.slipsui.util.FillUtil;
import com.ballydev.sds.slipsui.util.FocusUtility;
import com.ballydev.sds.slipsui.util.MultiAreaSupportUtil;
import com.ballydev.sds.slipsui.util.ProcessBeef;
import com.ballydev.sds.slipsui.util.SlipUILogger;
import com.ballydev.sds.slipsui.util.SlipsExceptionUtil;
import com.ballydev.sds.slipsui.util.UserFunctionsUtil;

/**
 * Controller class for the Beef Composite
 *
 * @author anantharajr
 * @version $Revision: 54$
 */
public class BeefController extends SDSBaseController {
	/**
	 * BeefForm Instance
	 */
	private static BeefForm form;
	/**
	 * BeefComposite Instance
	 */
	private BeefComposite beefComposite; 
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * BeefController Constructor
	 *
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public BeefController(Composite parent, int style,
			BeefForm form, SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		createCompositeOnSiteConfigParam(parent, style);
		MainMenuController.slipForm.setShift(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		super.registerEvents(beefComposite);
		form.addPropertyChangeListener(this);
		setSelectedBtnGrp();
		registerCustomizedListeners(beefComposite);
		FocusUtility.setTextFocus(beefComposite);
	}

	private void setSelectedBtnGrp() {
		if(beefComposite.getShiftControl()!=null) {
			beefComposite.getShiftControl().getShiftRadioButtonComposite()
			.setSelectedButton(beefComposite.getShiftControl().getBtnDay());	
		}	
		
		/*if(beefComposite.getAmountTypeControl()!=null) {
			beefComposite.getAmountTypeControl().getAmountTypeRadioButtonComposite()
			.setSelectedButton(beefComposite.getAmountTypeControl().getBtnCashAmt());	
		}*/
	}
	
	/**
	 * This method creates the composite's control based on the site
	 * configuration parameters.
	 *
	 * @param parent
	 * @param style
	 */
	public void createCompositeOnSiteConfigParam(Composite parent, int style) {

		createBeefComposite(parent, style,
				getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(beefComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
	}

	/*@Override
	public void widgetSelected(SelectionEvent e) {

		try {
			Control control = (Control) e.getSource();

			log.info("Inside widget selected of slip controller before button type check");
			if (!(control instanceof CbctlButton)
					&& !(control instanceof TouchScreenRadioButton)) {
				return;
			}

			log.info("Inside widget selected of slip controller");

			if (control instanceof CbctlButton) {
				if (((CbctlButton) control).getName().equalsIgnoreCase("next")) {
					log.debug("************ After next button is clicked:**********");
					if (form.getEmployeeId() != null) {
						MainMenuController.slipForm
								.setEmployeeIdPrintedSlip(form.getEmployeeId());
					}
					populateForm(beefComposite);
					List<String> fieldNames = new ArrayList<String>();
					fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
					fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
					if(getSiteConfigurationParameters()[2].equals("1"))
					{
						fieldNames.add(FormConstants.FORM_SLOT_NO);
						MainMenuController.slipForm.setSlotNo(form
								.getSlotLocationNo());
					}
					else if(getSiteConfigurationParameters()[2].equals("2"))
					{
						fieldNames.add(FormConstants.FORM_SLOT_LOCATION_NO);
						MainMenuController.slipForm.setSlotLocationNo(form
								.getSlotLocationNo());
					}
					fieldNames.add(FormConstants.FORM_AMOUNT);
					fieldNames.add(FormConstants.FORM_WINDOW_NO);
					boolean validate = validate("BeefForm", fieldNames,
							form, beefComposite);
					if (!validate) {
						return;
					}
					SessionUtility sessionUtility = new SessionUtility();
					if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
							&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.BEEF_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
						if(form.getEmpPassword().length() < 5){
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
							beefComposite.getTxtEmpPassword().forceFocus();
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
										beefComposite.getTxtEmployeeId().forceFocus();
										return;
									}
									else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
		                                    
	                                	ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										beefComposite.getTxtEmpPassword().setText("");
										beefComposite.getTxtEmpPassword().forceFocus();
										return;
									}
									else if(!UserFunctionsUtil.isBeefAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
									
										log.info("User does not have PROCESS BEEF FUNCTION");										
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED));
										beefComposite.getTxtEmpPassword().setText("");
										beefComposite.getTxtEmployeeId().forceFocus();
										return;																	
									}
									else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes"))
									{
										log.debug("SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS");
										if(!userDtoEmpPasswordChk.isSignedOn()){
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmployeeId().forceFocus();
											return;
										}								
									}else{
										MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
									}
								}else{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
									beefComposite.getTxtEmployeeId().forceFocus();
									return;
								}
							}catch(Exception ex){
								SlipsExceptionUtil.getGeneralCtrllerException(ex);							
								log.error("Exception while checking the user authentication",ex);
								return;
							}
						//}
					}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.BEEF_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
						if(form.getEmpPassword().length() < 5){
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
							beefComposite.getTxtEmpPassword().forceFocus();
							return;
						}
						else{
							try{
								log.info("Called framework's authenticate user method");
								UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.slipForm.getActorLogin(), form.getEmpPassword(), MainMenuController.slipForm.getSiteId());
								log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
								log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
								if(userDtoEmpPasswordChk!=null){
									if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
			                                    
	                                	ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										beefComposite.getTxtEmpPassword().setText("");
										beefComposite.getTxtEmpPassword().forceFocus();
										return;
									}
									else if(!UserFunctionsUtil.isBeefAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
										
										log.info("User does not have PROCESS BEEF FUNCTION");										
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
										beefComposite.getTxtEmpPassword().setText("");
										beefComposite.getTxtEmpPassword().forceFocus();
										return;																	
									}
									else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes"))
									{
										System.out.println("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
										if(!userDtoEmpPasswordChk.isSignedOn()){
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmpPassword().forceFocus();
											return;
										}								
									}
									else{
										MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
									}
								}								
								else{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
									beefComposite.getTxtEmpPassword().forceFocus();
									return;
								}
							}catch(Exception ex){
								log.error("Exception while checking the employee password with framework");
								SlipsExceptionUtil.getGeneralCtrllerException(ex);							
								log.error("Exception while checking the user authentication",ex);
								return;
							}
						//}
					}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
						try{
							log.info("Called framework's authenticate user method");
							UserDTO userDTOEmpIdCheck=sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.slipForm.getSiteId());
							if(userDTOEmpIdCheck!=null){
								log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
								if(userDTOEmpIdCheck.getUserName()==null){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									beefComposite.getTxtEmployeeId().forceFocus();
									return;
								}
								else if(!UserFunctionsUtil.isBeefAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
									
									log.info("User does not have PROCESS BEEF FUNCTION");										
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED));
									beefComposite.getTxtEmployeeId().forceFocus();
									return;																	
								}
								else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes"))
								{
									System.out.println("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
									if(!userDTOEmpIdCheck.isSignedOn()){
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
										beefComposite.getTxtEmployeeId().forceFocus();
										return;
									}								
								}
								else{
									MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
								}
							}else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								beefComposite.getTxtEmployeeId().forceFocus();
								return;
							}
						}catch(Exception ex){
							SlipsExceptionUtil.getGeneralCtrllerException(ex);							
							log.error("Exception while checking the user authentication",ex);
							return;							
						}
					}else if(!UserFunctionsUtil.isBeefAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
						
						log.info("User does not have PROCESS BEEF FUNCTION");										
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
						beefComposite.getTxtSlotSeqLocationNo().forceFocus();
						return;																	
					}
					
					SlipsAssetInfoDTO slipsAssetInfoDTO = null;
					if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
						*//** for slot no *//*
						try {
							log.info("Calling Asset's web method to check if the slot  no is valid");
							slipsAssetInfoDTO = SlipsServiceLocator.getService().getSlipsAssetInfo(StringUtil.padAcnfNo(form.getSlotNo()), SlipsAssetParamType.ASSET_CONFIG_NUMBER, MainMenuController.slipForm.getSiteId());
							if(slipsAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
								log.info("Invalid slot no");
								beefComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}else if(!slipsAssetInfoDTO.getSiteId().equals(MainMenuController.slipForm.getSiteId())){
								log.info("Slot does not belong to this site");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
								beefComposite.getTxtSlotSeqLocationNo().setFocus();
								return;
							}
							else if(!slipsAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase("ONLINE")){
								log.info("Beef process is not allowed for offline slots");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
								beefComposite.getTxtSlotSeqLocationNo().setFocus();
								return;
							}
							else{								
								*//** MULTI AREA SUPPORT CHECK *//*
								if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && slipsAssetInfoDTO.getAreaShortName()!=null) {
										
									if(!MultiAreaSupportUtil.validateSlotForSelectedArea(slipsAssetInfoDTO.getAreaShortName())){
										log.info("Slot does not belong to this area");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
										beefComposite.getTxtSlotSeqLocationNo().setFocus();
										return;
									}
								}
								
								MainMenuController.slipForm.setSlotNo(StringUtil.padAcnfNo(form.getSlotNo()));
								MainMenuController.slipForm.setSlotLocationNo(slipsAssetInfoDTO.getAssetConfigLocation().toUpperCase());
							}
						} catch (SlipsEngineServiceException e1) {
							log.info("EXCEPTION_WHILE_CHECKING_SLOT_NO_WITH_ASSET", e1);							
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1, MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS);							
							beefComposite.getTxtSlotSeqLocationNo().forceFocus();
							return;
						}
						catch (Exception e1) {
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e1);
							beefComposite.getTxtSlotSeqLocationNo().forceFocus();
							return;
						}
					}
					else if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
						*//** for slot location no *//*
						try {
							log.info("Calling Asset's web method to check if the slot location no is valid");
							slipsAssetInfoDTO = SlipsServiceLocator.getService().getSlipsAssetInfo(form.getSlotLocationNo().toUpperCase(), SlipsAssetParamType.ASSET_CONFIG_LOCATION, MainMenuController.slipForm.getSiteId());
							if(slipsAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
								log.info("Invalid slot location no");
								beefComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}else if(!slipsAssetInfoDTO.getSiteId().equals(MainMenuController.slipForm.getSiteId())){
								log.info("Slot location no does not belong to this site");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE));
								beefComposite.getTxtSlotSeqLocationNo().setFocus();
								return;
							}
							else if(!slipsAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase("ONLINE")){
								log.info("Beef process is not allowed for offline slots");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
								beefComposite.getTxtSlotSeqLocationNo().setFocus();
								return;
							}else{
								*//** MULTI AREA SUPPORT CHECK *//*
								if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && slipsAssetInfoDTO.getAreaShortName()!=null) {
									
									if(!MultiAreaSupportUtil.validateSlotForSelectedArea(slipsAssetInfoDTO.getAreaShortName())){
										log.info("Slot does not belong to this area");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA));
										beefComposite.getTxtSlotSeqLocationNo().setFocus();
										return;
									}
								}
								
								MainMenuController.slipForm.setSlotLocationNo(form.getSlotLocationNo().toUpperCase());
								MainMenuController.slipForm.setSlotNo(StringUtil.padAcnfNo(slipsAssetInfoDTO.getAssetConfigNumber()));
							}
						} catch (SlipsEngineServiceException e1) {
							log.info("EXCEPTION_WHILE_CHECKING_SLOT_LOCATION_WITH_ASSET", e1);							
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1, MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS);							
							beefComposite.getTxtSlotSeqLocationNo().forceFocus();
							return;
						}
						catch (Exception e1) {
							SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
							handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e1);
							beefComposite.getTxtSlotSeqLocationNo().forceFocus();
							return;
						}
					}
					if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("no")){
						MainMenuController.slipForm
						.setEmployeeIdPrintedSlip(MainMenuController.slipForm.getActorLogin());
					}
					if(!ConversionUtil.isFractionsOfCentsNormal(form.getAmount())){
						log.info("Fractions of a cent are too low, please re-enter the slip amount");
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.FRACTIONS_OF_A_CENT_TOO_LOW_SLIP_AMOUNT));
						beefComposite.getTxtAmount().setFocus();
						return;
					}
					
					if(new Integer(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.MAX_BEEF_SLIP_ALLOWED))>0){
						
						if(Double.parseDouble(form.getAmount()) > new Integer(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.MAX_BEEF_SLIP_ALLOWED))){
							log.info("Amount exceeds maximum dispute allowed limit");
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_AMT_EXCEEDS_SITE_MAXIMUM_ALLOWED_LIMIT));
							beefComposite.getTxtAmount().forceFocus();
							return;						
						}						
					}
					MainMenuController.slipForm.setSlipAmount(new Long(ConversionUtil.dollarToCents(Double.parseDouble(form.getAmount()))));
					
					log.info("++++++Slip AMT: "+MainMenuController.slipForm.getSlipAmount());
					if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ALLOW_WINDOW_NO_ON_BEEF_SLIPS).equalsIgnoreCase("yes")){
						MainMenuController.slipForm.setWindowNumber(form.getWindowNo());
					}
					if(!MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES).equalsIgnoreCase("0") && MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REQ_AUTH_SIGN_FOR_BEEFS).equalsIgnoreCase("yes")){
						if (TopMiddleController.getCurrentComposite() != null
								&& !(TopMiddleController.getCurrentComposite()
										.isDisposed())) {
							TopMiddleController.getCurrentComposite().dispose();
						}
						new BeefAuthorizationController(
								TopMiddleController.slipMiddleComposite, SWT.NONE,
							new BeefAuthorizationForm(), new SDSValidator(getClass(),true));
					}else {
						try {
							ProcessBeef processBeef = new ProcessBeef();
							processBeef.processBeefSlip();
						}catch (Exception e1) {
							log.error(e);
							
							return;
						}
							catch (SlipsEngineServiceException e1) {
								
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								
								handler.handleClientException(e1);
							}
							new CallInitialScreen().callBeefScreen();
					}

				}else if (((CbctlButton) control).getName().equalsIgnoreCase("cancel")) {

					log.debug("inside button cancel");
					boolean response= false;
					response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_SLIP_PROCESS), beefComposite);
					if(response){
						if (TopMiddleController.getCurrentComposite() != null
								&& !(TopMiddleController.getCurrentComposite()
										.isDisposed())) {
							TopMiddleController.getCurrentComposite().dispose();
						}
					}
				}
				}
			else
			{
				TouchScreenRadioButton touchScreenRadioButton = (TouchScreenRadioButton) control;
				beefComposite.getButtonGroup()
						.setSelectedButton(touchScreenRadioButton);
				if(touchScreenRadioButton.getName().equalsIgnoreCase("day")){
					MainMenuController.slipForm.setShift(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
				}else if(touchScreenRadioButton.getName().equalsIgnoreCase("swing")){
					MainMenuController.slipForm.setShift(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
				}else if(touchScreenRadioButton.getName().equalsIgnoreCase("graveyard")){
					MainMenuController.slipForm.setShift(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
				}
				MainMenuController.slipForm.setShift(touchScreenRadioButton
						.getName());
				log.info("The selected shift is "
						+ MainMenuController.slipForm.getShift());
				populateForm(beefComposite);
			}
			} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/

	
	
	/**
	 * BeefMouseListener class
	 *
	 */
	private class BeefMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {			
			
		}

		public void mouseDown(MouseEvent e) {

			try {
				Control control = (Control) e.getSource();
				if (!(control instanceof SDSImageLabel)
						&& !(control instanceof TSButtonLabel)) {
					return;
				}

				if (control instanceof SDSImageLabel) {
					
					//IF NEXT BUTTON IS CLICKED
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {
						log.debug("************ After next button is clicked:**********");
						if (form.getEmployeeId() != null) {
							MainMenuController.slipForm
									.setEmployeeIdPrintedSlip(form.getEmployeeId());
						}
						populateForm(beefComposite);
						List<String> fieldNames = new ArrayList<String>();
						fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
						fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
						if(getSiteConfigurationParameters()[2].equals("1"))
						{
							fieldNames.add(FormConstants.FORM_SLOT_NO);
							MainMenuController.slipForm.setSlotNo(form
									.getSlotLocationNo());
						}
						else if(getSiteConfigurationParameters()[2].equals("2"))
						{
							fieldNames.add(FormConstants.FORM_SLOT_LOCATION_NO);
							MainMenuController.slipForm.setSlotLocationNo(form
									.getSlotLocationNo());
						}
						fieldNames.add(FormConstants.FORM_AMOUNT);
						if(getSiteConfigurationParameters()[5].equalsIgnoreCase("Yes")) {
							fieldNames.add(FormConstants.FORM_ACCOUNT_NO);
							fieldNames.add(FormConstants.FORM_CONFIRM_ACCOUNT_NO);
						}
						fieldNames.add(FormConstants.FORM_WINDOW_NO);
						boolean validate = validate("BeefForm", fieldNames,
								form, beefComposite);
						if (!validate) {
							return;
						}
						SessionUtility sessionUtility = new SessionUtility();
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
								&& MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.BEEF_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							/*if(form.getEmpPassword().length() < 5){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
								beefComposite.getTxtEmpPassword().forceFocus();
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
											beefComposite.getTxtEmployeeId().forceFocus();
											return;
										}
										else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
			                                    
			                                log.info("Invalid emp pwd");
		                                	ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmpPassword().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isBeefAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
										
											log.info("User does not have PROCESS BEEF FUNCTION");										
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmployeeId().forceFocus();
											return;																	
										}
										else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
												&& !userDtoEmpPasswordChk.isSignedOn())
										{
											log.info("SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmployeeId().forceFocus();
											return;					
										}else{
											MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										}
									}else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
										beefComposite.getTxtEmployeeId().forceFocus();
										return;
									}
								}catch(Exception ex){
									SlipsExceptionUtil.getGeneralCtrllerException(ex);							
									log.error("Exception while checking the user authentication",ex);
									return;
								}
							//}
						}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.BEEF_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
							/*if(form.getEmpPassword().length() < 5){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));//LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));//"The" + MessageKeyConstants.VALIDATION_EMPLOYEE_PASSWORD + "should have a minimum length of 5 characters"));
								beefComposite.getTxtEmpPassword().forceFocus();
								return;
							}
							else{*/
								try{
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.slipForm.getActorLogin(), form.getEmpPassword(), MainMenuController.slipForm.getSiteId());
									log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
									log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
				                                    
		                                	ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmpPassword().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isBeefAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
											
											log.info("User does not have PROCESS BEEF FUNCTION");										
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmpPassword().forceFocus();
											return;																	
										}
										else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
												&& !userDtoEmpPasswordChk.isSignedOn())
										{
											log.info("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
											beefComposite.getTxtEmpPassword().setText("");
											beefComposite.getTxtEmpPassword().forceFocus();
											return;					
										}
										else{
											MainMenuController.slipForm.setEmployeeIdPrintedSlip(MainMenuController.slipForm.getActorLogin());
										}
									}								
									else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
										beefComposite.getTxtEmpPassword().forceFocus();
										return;
									}
								}catch(Exception ex){
									log.error("Exception while checking the employee password with framework");
									SlipsExceptionUtil.getGeneralCtrllerException(ex);							
									log.error("Exception while checking the user authentication",ex);
									return;
								}
							//}
						}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							try{
								log.info("Called framework's authenticate user method");
								UserDTO userDTOEmpIdCheck=sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.slipForm.getSiteId());
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
										beefComposite.getTxtEmployeeId().forceFocus();
										return;
									}
									else if(!UserFunctionsUtil.isBeefAllowed(form.getEmployeeId(), MainMenuController.slipForm.getSiteId())){
										
										log.info("User does not have PROCESS BEEF FUNCTION");										
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED));
										beefComposite.getTxtEmployeeId().forceFocus();
										return;																	
									}
									else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
											&& !userDTOEmpIdCheck.isSignedOn())
									{
										log.info("-----SLIPS_COMMANDS_REQUIRE_SIGNED_ON_CARDS-----");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIPS_FUNC_REQ_SIGNED_ON_USER));
										beefComposite.getTxtEmployeeId().forceFocus();
										return;					
									}
									else{
										MainMenuController.slipForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
									}
								}else{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									beefComposite.getTxtEmployeeId().forceFocus();
									return;
								}
							}catch(Exception ex){
								SlipsExceptionUtil.getGeneralCtrllerException(ex);							
								log.error("Exception while checking the user authentication",ex);
								return;							
							}
						}else if(!UserFunctionsUtil.isBeefAllowed(MainMenuController.slipForm.getActorLogin(), MainMenuController.slipForm.getSiteId())){
							
							log.info("User does not have PROCESS BEEF FUNCTION");										
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
							beefComposite.getTxtSlotSeqLocationNo().forceFocus();
							return;																	
						}
						
						SlipsAssetInfoDTO slipsAssetInfoDTO = null;
						if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
							/** for slot no */
							try {
								log.info("Calling Asset's web method to check if the slot  no is valid");
								slipsAssetInfoDTO = SlipsServiceLocator.getService().getSlipsAssetInfo(StringUtil.padAcnfNo(form.getSlotNo()), SlipsAssetParamType.ASSET_CONFIG_NUMBER, MainMenuController.slipForm.getSiteId());
								if(slipsAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
									log.info("Invalid slot no");
									beefComposite.getTxtSlotSeqLocationNo().forceFocus();
									return;
								}else if(!slipsAssetInfoDTO.getSiteId().equals(MainMenuController.slipForm.getSiteId())){
									log.info("Slot does not belong to this site");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
									beefComposite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}
								else if(!slipsAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase("ONLINE")){
									log.info("Beef process is not allowed for offline slots");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
									beefComposite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}
								else{								
									/** MULTI AREA SUPPORT CHECK */
									if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && slipsAssetInfoDTO.getAreaShortName()!=null) {
											
										if(!MultiAreaSupportUtil.validateSlotForSelectedArea(slipsAssetInfoDTO.getAreaShortName())){
											log.info("Slot does not belong to this area");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
											beefComposite.getTxtSlotSeqLocationNo().setFocus();
											return;
										}
									}
									
									MainMenuController.slipForm.setSlotNo(StringUtil.padAcnfNo(form.getSlotNo()));
									MainMenuController.slipForm.setSlotLocationNo(slipsAssetInfoDTO.getAssetConfigLocation().toUpperCase());
								}
							} catch (SlipsEngineServiceException e1) {
								log.info("EXCEPTION_WHILE_CHECKING_SLOT_NO_WITH_ASSET", e1);							
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1, MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS);							
								beefComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}
							catch (Exception e1) {
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e1);
								beefComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}
						}
						else if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
							/** for slot location no */
							try {
								log.info("Calling Asset's web method to check if the slot location no is valid");
								slipsAssetInfoDTO = SlipsServiceLocator.getService().getSlipsAssetInfo(form.getSlotLocationNo().toUpperCase(), SlipsAssetParamType.ASSET_CONFIG_LOCATION, MainMenuController.slipForm.getSiteId());
								if(slipsAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
									log.info("Invalid slot location no");
									beefComposite.getTxtSlotSeqLocationNo().forceFocus();
									return;
								}else if(!slipsAssetInfoDTO.getSiteId().equals(MainMenuController.slipForm.getSiteId())){
									log.info("Slot location no does not belong to this site");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE));
									beefComposite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}
								else if(!slipsAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase("ONLINE")){
									log.info("Beef process is not allowed for offline slots");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
									beefComposite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}else{
									/** MULTI AREA SUPPORT CHECK */
									if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && slipsAssetInfoDTO.getAreaShortName()!=null) {
										
										if(!MultiAreaSupportUtil.validateSlotForSelectedArea(slipsAssetInfoDTO.getAreaShortName())){
											log.info("Slot does not belong to this area");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA));
											beefComposite.getTxtSlotSeqLocationNo().setFocus();
											return;
										}
									}
									
									MainMenuController.slipForm.setSlotLocationNo(form.getSlotLocationNo().toUpperCase());
									MainMenuController.slipForm.setSlotNo(StringUtil.padAcnfNo(slipsAssetInfoDTO.getAssetConfigNumber()));
								}
							} catch (SlipsEngineServiceException e1) {
								log.info("EXCEPTION_WHILE_CHECKING_SLOT_LOCATION_WITH_ASSET", e1);							
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1, MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS);							
								beefComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}
							catch (Exception e1) {
								SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
								handler.handleException(e1,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e1);
								beefComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}
						}
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("no")){
							MainMenuController.slipForm
							.setEmployeeIdPrintedSlip(MainMenuController.slipForm.getActorLogin());
						}
						if(!ConversionUtil.isFractionsOfCentsNormal(form.getAmount())){
							log.info("Fractions of a cent are too low, please re-enter the slip amount");
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.FRACTIONS_OF_A_CENT_TOO_LOW_SLIP_AMOUNT));
							beefComposite.getTxtAmount().setFocus();
							return;
						}
						
						if(getSiteConfigurationParameters()[5].equalsIgnoreCase("Yes")){
															
							//VALIDATE THE CASHLESS ACCOUNT NO WITH ECASH ENGINE
							log.info("Calling the validateCashlessAccountNo() to validate the cashless acc no");
							SlipsCashlessAccountDTO slipCashlessAccDTO = SlipsServiceLocator.getService().validateCashlessAccountNo(MainMenuController.slipForm.getSiteId(), form.getAccountNo(), SlipsOperationType.PRINT);
							if(slipCashlessAccDTO!=null) {
								if(!slipCashlessAccDTO.isSuccess()){
									log.info("ERROR: Invalid Acc No");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_ACCOUNT_NUMBER));
									beefComposite.getTxtCashlessAccountNo().forceFocus();									
									return;
								}/*
								if(slipCashlessAccDTO.getStateId()!= ILookupTableConstants.ACCOUNT_ACTIVE_STATE_ID) {
									log.info("ERROR: Account is not active");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INACTIVE_ACCOUNT_NUMBER));
									beefComposite.getTxtCashlessAccountNo().forceFocus();									
									return;
								}*/
								/*if(slipCashlessAccDTO.getAcntTypeId() == ILookupTableConstants.ACCOUNT_TYPE_CASHLESS_VAL) {
									MainMenuController.slipForm.setCashlessAccountType(ILookupTableConstants.ACCOUNT_TYPE_CASHLESS);
								}else if(slipCashlessAccDTO.getAcntTypeId() == ILookupTableConstants.ACCOUNT_TYPE_SMART_CARD_VAL) {
									MainMenuController.slipForm.setCashlessAccountType(ILookupTableConstants.ACCOUNT_TYPE_SMART_CARD);
								}else {
									MainMenuController.slipForm.setCashlessAccountType(ILookupTableConstants.ACCOUNT_TYPE_MAGSTRIPECARD);
								}*/							
								MainMenuController.slipForm.setCashlessAccountNumber(form.getAccountNo());
								
							}else {
								if(log.isInfoEnabled()) {
									log.info("Account details returned are null for the cashless acc no: "+form.getAccountNo());
								}	
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_ACCOUNT_NUMBER));
								beefComposite.getTxtCashlessAccountNo().forceFocus();									
								return;
							}
						}						
					
					/*	if(new Integer(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.MAX_BEEF_SLIP_ALLOWED))>0){
							
							if(Double.parseDouble(form.getAmount()) > new Integer(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.MAX_BEEF_SLIP_ALLOWED))){
								log.info("Amount exceeds maximum dispute allowed limit");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.BEEF_AMT_EXCEEDS_SITE_MAXIMUM_ALLOWED_LIMIT));
								beefComposite.getTxtAmount().forceFocus();
								return;						
							}						
						}*/
						MainMenuController.slipForm.setSlipAmount(new Long(ConversionUtil.dollarToCents(Double.parseDouble(form.getAmount()))));
						
						log.info("++++++Slip AMT: "+MainMenuController.slipForm.getSlipAmount());
						if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ALLOW_WINDOW_NO_ON_BEEF_SLIPS).equalsIgnoreCase("yes")){
							MainMenuController.slipForm.setWindowNumber(form.getWindowNo());
						}
						MainMenuController.slipForm.setReason(form.getReason());
						if(!MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES).equalsIgnoreCase("0") && MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REQ_AUTH_SIGN_FOR_BEEFS).equalsIgnoreCase("yes")){
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite()
											.isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
							}
							new BeefAuthorizationController(
									TopMiddleController.slipMiddleComposite, SWT.NONE,
								new BeefAuthorizationForm(), new SDSValidator(getClass(),true));
						}else {
							try {
								ProcessBeef processBeef = new ProcessBeef();
								processBeef.processBeefSlip();
							}catch (Exception e1) {
								log.error(e);
								
								return;
							}
							FillUtil.disposeCurrentMiddleComposite();	
							new CallInitialScreen().callBeefScreen();
						}

					}//IF CANCEL BUTTON CLICKED
					else if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {

						log.debug("inside button cancel");
						boolean response= false;
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_SLIP_PROCESS), beefComposite);
						if(response){
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite()
											.isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
							}
						}
					}
					
				}// IF  SDSRadioImageLabel IS CLICKED FOR SHIFT SELECTION / AMOUNT TYPE SELECTION
				else if (control instanceof TSButtonLabel) {
					TSButtonLabel touchScreenRadioButton = (TSButtonLabel) control;
					String radioImageName = touchScreenRadioButton.getName();
					log.info("Radio Image Name:" + radioImageName);
					if (radioImageName.equalsIgnoreCase("day")
							|| radioImageName.equalsIgnoreCase("swing")
							|| radioImageName
									.equalsIgnoreCase("graveyard")) {
						beefComposite.getShiftControl().getShiftRadioButtonComposite().setSelectedButton(
								touchScreenRadioButton);
						MainMenuController.slipForm.setShift(touchScreenRadioButton.getName());
						log.info("The selected shift is "+ MainMenuController.slipForm.getShift());
						populateForm(beefComposite);					
					}
					
					/*if (radioImageName.equalsIgnoreCase("cashAmt")) {
						beefComposite.getAmountTypeControl().getAmountTypeRadioButtonComposite().setSelectedButton(
								touchScreenRadioButton);
						MainMenuController.slipForm.setCashableAmount(true);						
						populateForm(beefComposite);					
					}else if (radioImageName.equalsIgnoreCase("nonCashAmt")) {
						beefComposite.getAmountTypeControl().getAmountTypeRadioButtonComposite().setSelectedButton(
								touchScreenRadioButton);
						MainMenuController.slipForm.setCashableAmount(false);
						populateForm(beefComposite);					
					}
					log.info("The selected amount type is "+ MainMenuController.slipForm.isCashableAmount());*/
					populateForm(beefComposite);
				}					
	
			} catch (Exception ex) {
				log.error("Exception in mouseDown for BeefController" ,ex);
			}			
		}

		public void mouseUp(MouseEvent e) {
			
		}
		
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		BeefMouseListener listener = new BeefMouseListener();
		if(beefComposite.getShiftControl()!=null) {
			beefComposite.getShiftControl().getBtnDay().addMouseListener(listener);
			beefComposite.getShiftControl().getBtnDay().addTraverseListener(this);
			beefComposite.getShiftControl().getBtnSwing().addMouseListener(listener);
			beefComposite.getShiftControl().getBtnSwing().addTraverseListener(this);
			beefComposite.getShiftControl().getBtnGraveyard().addMouseListener(listener);
			beefComposite.getShiftControl().getBtnGraveyard().addTraverseListener(this);
		}	
		
		/*if(beefComposite.getAmountTypeControl()!=null) {
			beefComposite.getAmountTypeControl().getBtnCashAmt().addMouseListener(listener);
			beefComposite.getAmountTypeControl().getBtnCashAmt().addTraverseListener(this);
			beefComposite.getAmountTypeControl().getBtnNonCashAmount().addMouseListener(listener);
			beefComposite.getAmountTypeControl().getBtnNonCashAmount().addTraverseListener(this);
		}*/
		beefComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		beefComposite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
	}


	/**
	 * Method to create the BeefComposite
	 *
	 * @param p
	 * @param s
	 */
	public void createBeefComposite(Composite p, int s, String[] flags) {
		beefComposite = new BeefComposite(p, s, flags);

	}

	/**
	 * Method to return a String array of the site configuration paramters that
	 * are checked.
	 *
	 * @return
	 */
	public String[] getSiteConfigurationParameters() {

		String[] flags =new String[6];
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
			flags[0]="yes";
		}else{
			flags[0]="no";
		}
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.BEEF_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
			flags[1]="yes";
		}else{
			flags[1]="no";
		}
		if(new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1)
		{
			flags[2]="1";
		}else if(new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2){
			flags[2]="2";
		}
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ALLOW_WINDOW_NO_ON_BEEF_SLIPS).equalsIgnoreCase("yes")){
			flags[3]="yes";
		}else{
			flags[3]="no";
		}
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)){
			flags[4]="p";
		}else{
			flags[4]="no";
		}
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ALLOW_AUTOMATIC_DEPOSIT_TO_ECASH).equalsIgnoreCase("yes")){
			flags[5]="yes";
		}else{
			flags[5]="no";
		}
		return flags;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}


	@Override
	public Composite getComposite() {		
		return null;
	}

/*	@Override
	public void keyTraversed(TraverseEvent e) {
		
		super.keyTraversed(e);
		e.detail = SWT.TRAVERSE_RETURN;
	}*/
}
