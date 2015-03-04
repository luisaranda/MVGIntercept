/*****************************************************************************
 * $Id: ManualJPEmployeeSlotStandShiftController.java,v 1.37, 2010-09-23 04:29:41Z, Subha Viswanathan$
 * $Date: 9/22/2010 11:29:41 PM$
 *****************************************************************************
 *           Copyright  f-(c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.ILabelConstants;
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
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.composite.ManualJPEmployeeSlotStandShiftComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.ManualJPEmployeeSlotStandShiftForm;
import com.ballydev.sds.jackpotui.form.ManualJPHandPaidAmtPromoJackpotTypeForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.MultiAreaSupportUtil;
import com.ballydev.sds.jackpotui.util.SlotCreatedTsComparator;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;

/**
 * First screen for a manual jackpot process
 * @author dambereen
 * @version $Revision: 38$
 */
public class ManualJPEmployeeSlotStandShiftController extends SDSBaseController{

	/**
	 * ManualJPEmployeeSlotStandShiftForm Instance
	 */
	public static ManualJPEmployeeSlotStandShiftForm form;
	
	/**
	 * ManualJackpotScreenController Instance
	 */
	private ManualJPEmployeeSlotStandShiftComposite manualJackpotScr1Composite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * ManualJackpotScreen1Controller Constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception1
	 */	
	public ManualJPEmployeeSlotStandShiftController(Composite parent, int style,ManualJPEmployeeSlotStandShiftForm form
			, SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);	
		createCompositeOnSiteConfigParam(parent, style);
		//parent.layout();
		
		/**Data to be set for the static data info display */
		setDataForManualStaticDisplay();	
		super.registerEvents(manualJackpotScr1Composite);
		form.addPropertyChangeListener(this);
		populateScreen(manualJackpotScr1Composite);
		setPrevSelectedBtnGrp();
		FocusUtility.setTextFocus(manualJackpotScr1Composite);
		FocusUtility.focus = false;
		registerCustomizedListeners(manualJackpotScr1Composite);
	}

	/**
	 * This method creates the composite's control based on the site
	 * configuration parameters.
	 * 
	 * @param parent
	 * @param style
	 */
	public void createCompositeOnSiteConfigParam(Composite parent, int style) {
		createManualJackpotScreen1Composite(parent, style,	getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(manualJackpotScr1Composite);
		log.info("*************" + TopMiddleController.getCurrentComposite());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ManualJPMouseListener listener = new ManualJPMouseListener();
		if(manualJackpotScr1Composite.getShiftControl() != null){
			manualJackpotScr1Composite.getShiftControl().getBtnDay().addMouseListener(listener);
			manualJackpotScr1Composite.getShiftControl().getBtnDay().addTraverseListener(this);
			manualJackpotScr1Composite.getShiftControl().getBtnSwing().addMouseListener(listener);
			manualJackpotScr1Composite.getShiftControl().getBtnSwing().addTraverseListener(this);
			manualJackpotScr1Composite.getShiftControl().getBtnGraveyard().addMouseListener(listener);
			manualJackpotScr1Composite.getShiftControl().getBtnGraveyard().addTraverseListener(this);
		}
		
		manualJackpotScr1Composite.getButtonComposite().getBtnNext().addMouseListener(listener);
		manualJackpotScr1Composite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	public ManualJPEmployeeSlotStandShiftComposite getComposite() {
		return manualJackpotScr1Composite;
	}

	/**
	 * Method to create the ManualJackpotScreen1Composite
	 * 
	 * @param p
	 * @param s
	 */
	public void createManualJackpotScreen1Composite(Composite p, int s, String[] flags) {
		manualJackpotScr1Composite = new ManualJPEmployeeSlotStandShiftComposite(p, s, flags);

	}

	/**
	 * Method to return a String array of the site configuration paramters that
	 * are checked.
	 * 
	 * @return
	 */
	public String[] getSiteConfigurationParameters() {
		String[] flags = new String[4];
		
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
			flags[0] = "yes";
		} else {
			flags[0] = "no";
		}		
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
			flags[1] = "yes";
		} else {
			flags[1] = "no";
		}
		if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
			flags[2] = "1";
		}
		else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
			flags[2] = "2";
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
			flags[3] = "yes";
		}
		else{
			flags[3] = "no";
		}
		return flags;
	}
	
	
	/**
	 * method setDataForManualStaticDisplay 
	 */
	public void setDataForManualStaticDisplay(){
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)){
			MainMenuController.jackpotForm.setShift(IAppConstants.SITE_VALUE_SHIFT_DAY);
			//form.setDayYes(true);
		}
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
	
	/**
	 * Method to set the previous selected button in the SHIFT group    
	 */
	public void setPrevSelectedBtnGrp(){
		if(manualJackpotScr1Composite.getBottomComposite()!=null && !manualJackpotScr1Composite.getBottomComposite().isDisposed()
				&& manualJackpotScr1Composite.getShiftControl() != null){
			if(form.getDayYes()){
				manualJackpotScr1Composite.getShiftControl().getShiftRadioButtonComposite().setSelectedButton(manualJackpotScr1Composite.getShiftControl().getBtnDay());
			}else if(form.getSwingYes()){
				manualJackpotScr1Composite.getShiftControl().getShiftRadioButtonComposite().setSelectedButton(manualJackpotScr1Composite.getShiftControl().getBtnSwing());
			}else if(form.getGraveyardYes()){
				manualJackpotScr1Composite.getShiftControl().getShiftRadioButtonComposite().setSelectedButton(manualJackpotScr1Composite.getShiftControl().getBtnGraveyard());
			}
		}
	}
	
	private class ManualJPMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {


			try {
				Control control = (Control) e.getSource();
				if (!(control instanceof TSButtonLabel)
						&& !(control instanceof SDSImageLabel)) {
					return;
				}
				
				if (control instanceof SDSImageLabel) {
					
				if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {
					log.info("************ After next button is clicked:**********");				
					try {
						populateForm(manualJackpotScr1Composite);
					} catch (Exception e1) {
						
					}
					MainMenuController.jackpotForm.setJackpotID("FC");
					ArrayList<String> fieldNames = new ArrayList<String>();
					if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
						fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);										
					}
					if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
						fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
					}
					if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
						fieldNames.add(FormConstants.FORM_SLOT_NO);
					}
					else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
						fieldNames.add(FormConstants.FORM_SLOT_LOCATION_NO);
					}
					/** Validation of the ManualJackpotScreen1Form done field by field. */
					log.info("Validating the entered values");
					boolean validate = validate("ManualJPEmployeeSlotStandShiftForm", fieldNames, form, manualJackpotScr1Composite);
					if (!validate) {
						log.info("Validation failed");
						return;
					}
					log.info("Site Param Chk- JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS:");
					log.info("Value : "+MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS));
					SessionUtility sessionUtility = new SessionUtility();
					
					//PROCESS EMPLOYEE ID VALIDATION
					
					if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
						ProgressIndicatorUtil.openInProgressWindow();
						try{							
							log.debug("Called framework's authenticate user method");
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmpPassword(), MainMenuController.jackpotForm.getSiteId());
							log.debug("UserDTO obj : "+userDtoEmpPasswordChk);
							
							if(userDtoEmpPasswordChk!=null){
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
												|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;
								}
								else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
	                                    
	                                	ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										manualJackpotScr1Composite.getTxtEmpPassword().setText("");
										manualJackpotScr1Composite.getTxtEmpPassword().forceFocus();
										return;
								}
								else if(!UserFunctionsUtil.isManualJackpotAllowed(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId()))
								{
									log.info("User does not contain the PROCESS MANUAL JP function");									
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.MANUAL_PROCESS_FUNC_REQUIRED));
									manualJackpotScr1Composite.getTxtEmpPassword().setText("");
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;																
								}
								else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
									&& !userDtoEmpPasswordChk.isSignedOn())
								{
									log.info("JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS");
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
									manualJackpotScr1Composite.getTxtEmpPassword().setText("");
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;					
								}
								else{
									ProgressIndicatorUtil.closeInProgressWindow();
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
								}								
							}
							else{
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
								manualJackpotScr1Composite.getTxtEmpPassword().setText("");
								manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
								return;
							}
						}catch(Exception ex){
							JackpotExceptionUtil.getGeneralCtrllerException	(ex);	
							log.error("Exception while checking the user authentication",ex);
							return;
						}finally{
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
						ProgressIndicatorUtil.openInProgressWindow();
						try{							
							log.info("Called framework's authenticate user method");
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(MainMenuController.jackpotForm.getActorLogin(), form.getEmpPassword(), MainMenuController.jackpotForm.getSiteId());
							
							log.info("User name1 : "+userDtoEmpPasswordChk.getUserName());
							log.info("User password1 : "+userDtoEmpPasswordChk.getPassword());
							if(userDtoEmpPasswordChk!=null){
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
	                                    
	                                	ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										manualJackpotScr1Composite.getTxtEmpPassword().setText("");
										manualJackpotScr1Composite.getTxtEmpPassword().forceFocus();
										return;
								}
								else if(!UserFunctionsUtil.isManualJackpotAllowed(MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getSiteId()))
								{
									log.info("User does not contain the PROCESS MANUAL JP function");									
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.MANUAL_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
									manualJackpotScr1Composite.getTxtEmpPassword().setText("");
									manualJackpotScr1Composite.getTxtEmpPassword().forceFocus();
									return;																
								}
								else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
										&& !userDtoEmpPasswordChk.isSignedOn())
								{
									log.info("JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS");
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
									manualJackpotScr1Composite.getTxtEmpPassword().setText("");
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;					
								}
								else{
									ProgressIndicatorUtil.closeInProgressWindow();
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(MainMenuController.jackpotForm.getActorLogin());
								}	
							}							
							else{
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
								manualJackpotScr1Composite.getTxtEmpPassword().setText("");
								manualJackpotScr1Composite.getTxtEmpPassword().forceFocus();
								return;
							}
						}catch(Exception ex){
							JackpotExceptionUtil.getGeneralCtrllerException	(ex);	
							log.error("Exception while checking the employee id and password with framework");
							return;
						}finally{
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
						ProgressIndicatorUtil.openInProgressWindow();
						try{							
							log.info("Called framework's authenticate user method");							
							UserDTO userDTOEmpIdCheck = sessionUtility.getUserDetails(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId());
							if(userDTOEmpIdCheck!=null){							
								log.info("User name1 : "+userDTOEmpIdCheck.getUserName());
								if (userDTOEmpIdCheck.isErrorPresent() && userDTOEmpIdCheck.getMessageKey() != null
										&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDTOEmpIdCheck.getMessageKey())||
												IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDTOEmpIdCheck.getMessageKey())||
												IAppConstants.ANA_MESSAGES_USER_NOT_PRESENT.equals(userDTOEmpIdCheck.getMessageKey()))) {
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;
								}
								else if(!UserFunctionsUtil.isManualJackpotAllowed(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId()))
								{
									log.info("User does not contain the PROCESS MANUAL JP function");									
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.MANUAL_PROCESS_FUNC_REQUIRED));
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;																
								}
								else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS).equalsIgnoreCase("yes")
										&& !userDTOEmpIdCheck.isSignedOn())
								{
									log.info("JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
									manualJackpotScr1Composite.getTxtEmpPassword().setText("");
									manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
									return;					
								}								
								else{								
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
								}
							}						
							else{
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								manualJackpotScr1Composite.getTxtEmployeeId().forceFocus();
								return;
							}															
						}catch(Exception ex){
							JackpotExceptionUtil.getGeneralCtrllerException	(ex);	
							log.error("Exception while checking the employee id with framework");
							return;
						}	
						finally{
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					}else if(!UserFunctionsUtil.isManualJackpotAllowed(MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getSiteId()))
					{
						log.info("User does not contain the PROCESS MANUAL JP function");									
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.MANUAL_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
						manualJackpotScr1Composite.getTxtSlotSeqLocationNo().forceFocus();
						return;																
					}
					
					//SLOT / STAND NO VALIDATION
					JackpotAssetInfoDTO jackpotAssetInfoDTO = null;
					if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
						/** for slot no */					
						try{
							log.info("Calling the getJackpotAssetInfo web method");
							log.info("*******************************************");
							log.debug("****Slot no: "+StringUtil.padAcnfNo(form.getSlotNo()));
							log.info("JackpotAssetParamType.ASSET_CONFIG_NUMBER: "+JackpotAssetParamType.ASSET_CONFIG_NUMBER);						
							log.info("*******************************************");
							
							jackpotAssetInfoDTO = JackpotServiceLocator.getService().getJackpotAssetInfo(StringUtil.padAcnfNo(form.getSlotNo()),
									JackpotAssetParamType.ASSET_CONFIG_NUMBER, MainMenuController.jackpotForm.getSiteId());
							
							log.info("Values returned after calling the getJackpotAssetInfo web method");
							log.info("*******************************************");
							
							if(jackpotAssetInfoDTO !=null){
								log.info("JackpotAssetInfoDTO values returned: "+jackpotAssetInfoDTO.toString());					
								log.info("*******************************************");	
								if(jackpotAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
									log.info("Invalid slot no");
									manualJackpotScr1Composite.getTxtSlotSeqLocationNo().forceFocus();
									return;
								}
								if(!jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_ONLINE_STATUS)
										&& !jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_CET_STATUS)){
									
										log.info("Manual jackpot process is not allowed for offline slots");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
										manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
										return;
								}
								if(!jackpotAssetInfoDTO.getSiteId().equals(MainMenuController.jackpotForm.getSiteId())){
									log.info("Slot does not belong to this site");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
									manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}							
								else{
									/** MULTI AREA SUPPORT CHECK */
									if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && jackpotAssetInfoDTO.getAreaShortName()!=null) {
										if(!MultiAreaSupportUtil.validateSlotForSelectedArea(jackpotAssetInfoDTO.getAreaShortName())){
											log.info("Slot does not belong to this area");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
											manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
											return;
										}
									}								
									
									if(jackpotAssetInfoDTO.getSlotDenomination()!=null){
										String slotDenom = jackpotAssetInfoDTO.getSlotDenomination();
										String slotDenomStr = ConversionUtil.dollarToCents(slotDenom);
										long slotDenomLong = Long.parseLong(slotDenomStr);
										MainMenuController.jackpotForm.setDenomination(ConversionUtil.twoPlaceDecimalOf(ConversionUtil.centsToDollar(slotDenomLong)));
									}									
									//if(jackpotAssetInfoDTO.getAreaName()!=null)
										//MainMenuController.jackpotForm.setArea(jackpotAssetInfoDTO.getAreaName());
									/*List<JackpotGameDetailsDTO>  gameDetailsList = jackpotAssetInfoDTO.getListGameDetails();
									if(gameDetailsList != null){
										log.info("gameDetailsList size: "+gameDetailsList.size());
										if(gameDetailsList.size() > 0){
											for(int i=0; i< gameDetailsList.size(); i++){
												MainMenuController.jackpotForm.setThemeName(gameDetailsList.get(i).getThemeName());
												MainMenuController.jackpotForm.setDenomination(ConversionUtil.dollarToCents(gameDetailsList.get(i).getDenomAmount()));
												log.info("Denom: "+MainMenuController.jackpotForm.getDenomination());
												log.info("theme name: "+gameDetailsList.get(i).getThemeName());
											}	
											MainMenuController.jackpotForm.setAssetConfigurationId(jackpotAssetInfoDTO.getAssetConfigId());
										}	
									}*/
									
									MainMenuController.jackpotForm.setProgressiveSlot(jackpotAssetInfoDTO.getProgressive());
									MainMenuController.jackpotForm.setSlotNo(StringUtil.padAcnfNo(form.getSlotNo()));
									MainMenuController.jackpotForm.setSlotLocationNo(jackpotAssetInfoDTO.getAssetConfigLocation().toUpperCase());
									MainMenuController.jackpotForm.setArea(jackpotAssetInfoDTO.getAreaShortName());
								}							
							}
						}catch (JackpotEngineServiceException ex) {
							log.info("Error while calling asset to check if slot no entered is correct");
							log.error(ex);
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(ex,LabelLoader.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
							manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
							return;
						}catch (Exception e2) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e2);
							return;
						}	
					}
					else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
						
						List<JackpotAssetInfoDTO> jpAssInfoDTOLst = new ArrayList<JackpotAssetInfoDTO>();
						/** for slot location no */
						try{
							log.info("Calling the getJackpotAssetInfo web method");
							log.info("*******************************************");
							log.info("Slot location no: "+form.getSlotLocationNo());
							log.info("JackpotAssetParamType.ASSET_CONFIG_LOCATION: "+JackpotAssetParamType.ASSET_CONFIG_LOCATION);						
							log.info("*******************************************");

							jpAssInfoDTOLst = JackpotServiceLocator.getService().getJackpotListAssetInfoForLocation(
									form.getSlotLocationNo().toUpperCase(),MainMenuController.jackpotForm.getSiteId());
							
							log.info("Values returned after calling the getJackpotAssetInfo web method");
							log.info("*******************************************");
							
							if(jpAssInfoDTOLst !=null && jpAssInfoDTOLst.size()>0){			
								log.info("*******************************************");
															
								log.debug("jpAssInfoDTOLst: "+jpAssInfoDTOLst);
								log.debug("jpAssInfoDTOLst.size(): "+jpAssInfoDTOLst.size());							
								Collections.sort(jpAssInfoDTOLst, new SlotCreatedTsComparator());						
								if(log.isDebugEnabled()) {
									for(JackpotAssetInfoDTO assetDto :jpAssInfoDTOLst) {
										log.debug("Sorted slot no: "+assetDto.getAssetConfigNumber());
									}								
								}
															
								if(jpAssInfoDTOLst.get(0).getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
									log.info("Invalid slot location no");
									manualJackpotScr1Composite.getTxtSlotSeqLocationNo().forceFocus();
									return;
								}
								if(!jpAssInfoDTOLst.get(0).getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_ONLINE_STATUS)
										&& !jpAssInfoDTOLst.get(0).getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_CET_STATUS)){
									
										log.info("Manual jackpot process is not allowed for offline slots");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
										manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
										return;
								}
								if(!jpAssInfoDTOLst.get(0).getSiteId().equals(MainMenuController.jackpotForm.getSiteId())){
									log.info("Slot location no does not belong to this site");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE));
									manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}
								else{
									/** MULTI AREA SUPPORT CHECK */
									if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && jpAssInfoDTOLst.get(0).getAreaShortName()!=null) {
										if(!MultiAreaSupportUtil.validateSlotForSelectedArea(jpAssInfoDTOLst.get(0).getAreaShortName())){
											log.info("Slot does not belong to this area");
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA));
											manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
											return;
										}
									}
									
									if(jpAssInfoDTOLst.get(0).getSlotDenomination()!=null){
										String slotDenom = jpAssInfoDTOLst.get(0).getSlotDenomination();
										String slotDenomStr = ConversionUtil.dollarToCents(slotDenom);
										long slotDenomLong = Long.parseLong(slotDenomStr);
										MainMenuController.jackpotForm.setDenomination(ConversionUtil.twoPlaceDecimalOf(ConversionUtil.centsToDollar(slotDenomLong)));
									}
									//if(jackpotAssetInfoDTO.getAreaName()!=null)
										//MainMenuController.jackpotForm.setArea(jackpotAssetInfoDTO.getAreaName());
									/*List<JackpotGameDetailsDTO>  gameDetailsList = jackpotAssetInfoDTO.getListGameDetails();
									if(gameDetailsList != null){s
										log.info("gameDetailsList size: "+gameDetailsList.size());
										if(gameDetailsList.size() > 0){
											for(int i=0; i< gameDetailsList.size(); i++){
												MainMenuController.jackpotForm.setThemeName(gameDetailsList.get(i).getThemeName());
												MainMenuController.jackpotForm.setDenomination(ConversionUtil.dollarToCents(gameDetailsList.get(i).getDenomAmount()));
												log.info("Denom: "+MainMenuController.jackpotForm.getDenomination());
												log.info("theme name: "+gameDetailsList.get(i).getThemeName());
											}	
											MainMenuController.jackpotForm.setAssetConfigurationId(jackpotAssetInfoDTO.getAssetConfigId());
										}	
									}*/
									
									MainMenuController.jackpotForm.setProgressiveSlot(jpAssInfoDTOLst.get(0).getProgressive());
									MainMenuController.jackpotForm.setSlotLocationNo(form.getSlotLocationNo().toUpperCase());
									MainMenuController.jackpotForm.setSlotNo(jpAssInfoDTOLst.get(0).getAssetConfigNumber());
									MainMenuController.jackpotForm.setArea(jpAssInfoDTOLst.get(0).getAreaShortName());
								}							
							}
						}catch (JackpotEngineServiceException ex) {
							log.info("Error while calling asset to check if slot location no entered is correct");
							log.error(ex);
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(ex,LabelLoader.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
							manualJackpotScr1Composite.getTxtSlotSeqLocationNo().setFocus();
							return;
						}catch (Exception e2) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e2);
							return;
						}	
					}
						
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
							.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
						if (form.getDayYes()) {
							MainMenuController.jackpotForm.setShift("Day");
						}
						else if (form.getSwingYes()) {
							MainMenuController.jackpotForm.setShift("Swing");
						}
						else if (form.getGraveyardYes()) {
							MainMenuController.jackpotForm.setShift("Graveyard");
						}else{
							MainMenuController.jackpotForm.setShift("Day");
						}					
					} else{
						MainMenuController.jackpotForm.setShift(MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT));
					}
									
					/*else if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
							.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY)) {
						MainMenuController.jackpotForm.setShift("Day");
					} else if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
							.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_SWING)) {
						MainMenuController.jackpotForm.setShift("Swing");
					} else if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
							.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
						MainMenuController.jackpotForm.setShift("Graveyard");
					}	*/		
					
					populateForm(manualJackpotScr1Composite);				
					
					boolean promotionalResponse = false;
					if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SEPARATE_REPORTING_FOR_PROMOTIONAL_JACKPOT).equalsIgnoreCase("yes")){
						
						
						promotionalResponse = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.IS_THIS_A_PROMOTIONAL_JACKPOT), manualJackpotScr1Composite);
						log.info("Question asked : if this is a promotional jackpot?");
						if(promotionalResponse){
							MainMenuController.jackpotForm.setJackpotID("BA");
							MainMenuController.jackpotForm.setJackpotTypeId((short)ILookupTableConstants.JACKPOT_TYPE_PROMOTIONAL);
							MainMenuController.jackpotForm.setPromotionalJackpot(true);
							MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.PROMOTIONAL_JACKPOT_YES_LABEL));//("Promotional");
							log.info("Its a promotional manual jackpot");
						}
						/*else{
								//SET DEFAULT JP TYPE AS REGULAR - FC
								MainMenuController.jackpotForm.setPromotionalJackpot(false);
								MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));//("Normal");
								MainMenuController.jackpotForm.setJackpotID("FC");
								MainMenuController.jackpotForm.setJackpotTypeId((short)ILookupTableConstants.JACKPOT_TYPE_REGULAR);
								
								log.info("Its not a promotional manual jackpot");
						}*/					
					}/*else {
						//SET DEFAULT JP TYPE AS REGULAR - FC
						MainMenuController.jackpotForm.setPromotionalJackpot(false);
						MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));//("Normal");
						MainMenuController.jackpotForm.setJackpotID("FC");
						MainMenuController.jackpotForm.setJackpotTypeId((short)ILookupTableConstants.JACKPOT_TYPE_REGULAR);				
						log.info("Its not a promotional manual jackpot");					
					}*/
					
					if(!promotionalResponse) {
						//SET DEFAULT JP TYPE AS REGULAR - FC
						MainMenuController.jackpotForm.setPromotionalJackpot(false);
						MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));//("Normal");
						MainMenuController.jackpotForm.setJackpotID("FC");
						MainMenuController.jackpotForm.setJackpotTypeId((short)ILookupTableConstants.JACKPOT_TYPE_REGULAR);				
						log.info("Its not a promotional manual jackpot");
					}
					
					JackpotUIUtil.disposeCurrentMiddleComposite();
					new ManualJPHandPaidAmtPromoJackpotTypeController(TopMiddleController.jackpotMiddleComposite, SWT.NONE, 
							new ManualJPHandPaidAmtPromoJackpotTypeForm(), new SDSValidator(getClass(),true));
				  }
				}else if (control instanceof TSButtonLabel) {
					TSButtonLabel touchScreenRadioButton = (TSButtonLabel) control;
					String radioImageName = touchScreenRadioButton.getName();
					System.out.println("Radio Image Name:" + radioImageName);
					if (radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_DAY)
							|| radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_SWING)
							|| radioImageName
									.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
						manualJackpotScr1Composite
								.getShiftControl().getShiftRadioButtonComposite().setSelectedButton(
										touchScreenRadioButton);
						MainMenuController.jackpotForm
								.setShift(touchScreenRadioButton.getName());
						if(radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_DAY)){
							form.setDayYes(true);
							form.setSwingYes(false);
							form.setGraveyardYes(false);
						}else if(radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_SWING)){
							form.setSwingYes(true);
							form.setDayYes(false);
							form.setGraveyardYes(false);
						}else if(radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_GRAVEYARD)){
							form.setGraveyardYes(true);
							form.setDayYes(false);
							form.setSwingYes(false);
						}
						log.info("The selected shift is "
								+ MainMenuController.jackpotForm.getShift());
						populateForm(manualJackpotScr1Composite);
					} 

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex);
			}
		
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
