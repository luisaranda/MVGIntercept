/*****************************************************************************
 * $Id: DisplayJackpotController.java,v 1.27, 2010-08-31 15:25:31Z, Subha Viswanathan$
 * $Date: 8/31/2010 10:25:31 AM$
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
package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
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
import com.ballydev.sds.jackpotui.composite.DisplayJackpotComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.DisplayForm;
import com.ballydev.sds.jackpotui.form.DisplayJackpotForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.MultiAreaSupportUtil;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;

/**
 * This class acts as a controller for all the events performed in the
 * DisplayJackpotComposite
 * 
 * @author vijayrajm
 * @version $Revision: 28$
 */
public class DisplayJackpotController extends SDSBaseController {

	/**
	 * DisplayJackpotForm instance
	 */
	private static DisplayJackpotForm form;

	/**
	 * DisplayJackpotComposite instance
	 */
	private DisplayJackpotComposite dispJackpotComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);
	/**
	 * This variable hold the value of slot or stand number based on site config.
	 */
	private String slotOrStandCurrentValue=null;
	
	/**
	 * DisplayJackpotController constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public DisplayJackpotController(Composite parent, int style,
			DisplayJackpotForm form, SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);	
		createDisplayJackpotComposite(parent, style,
				getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(dispJackpotComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
		//parent.layout();
		super.registerEvents(dispJackpotComposite);
		form.addPropertyChangeListener(this);
		setDefaultValue();
		FocusUtility.setTextFocus(dispJackpotComposite);
		FocusUtility.focus = false;
		registerCustomizedListeners(dispJackpotComposite);
	}
	
	private void setDefaultValue(){
		if(dispJackpotComposite.getRadioButtonControl() != null){
			dispJackpotComposite.getRadioButtonControl().setSelectedButton(dispJackpotComposite.getStandSlotRadioImage());
		}
		
	}

	/**
	 * This method is used to perform action for widgetselected event
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {}

	/**
	 * This method is used to register customized listeners
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		DisplayMouseListener listener = new DisplayMouseListener();
		dispJackpotComposite.getSeqRadioImage().addMouseListener(listener);
		dispJackpotComposite.getSeqRadioImage().addTraverseListener(this);
		dispJackpotComposite.getStandSlotRadioImage().addMouseListener(listener);
		dispJackpotComposite.getStandSlotRadioImage().addTraverseListener(this);
		dispJackpotComposite.getButtonComposite().getBtnDisplay().addMouseListener(listener);
		dispJackpotComposite.getButtonComposite().getBtnDisplay().getTextLabel().addTraverseListener(this);

	}

	/**
	 * A method to create DisplayJackpotComposite
	 * 
	 * @param p
	 * @param s
	 * @param paramFlags
	 */
	public void createDisplayJackpotComposite(Composite p, int s,
			String[] paramFlags) {
		dispJackpotComposite = new DisplayJackpotComposite(p, s, paramFlags);
	}

	/**
	 * A method to get the boolean values based on the configuration parameters
	 * enabled/disabled
	 * 
	 * @return paramFlags
	 */
	public String[] getSiteConfigurationParameters() {
		String[] paramFlags = new String[3];
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
			paramFlags[0] = "yes";
		} else {
			paramFlags[0] = "no";
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.DISPLAY_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
			paramFlags[1] = "yes";
		} else {
			paramFlags[1] = "no";
		}
		if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
			paramFlags[2] = "1";
		} else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))== IAppConstants.PROMPT_SLOT_LOCATION){
			paramFlags[2] = "2";
		}
		return paramFlags;
	}

	/**
	 * Local method to set the fields in the AssetSearchFilter that is common for all calls to Asset
	 * @return AssetSearchFilter
	 */
	/*public AssetSearchFilter setAssetSearchFilter(){
		AssetSearchFilter filter = new AssetSearchFilter();		
		filter.setGameInfoRequired(true);
		filter.setHopperEnabledRequired(true);
		filter.setMultiGameRequired(true);
		filter.setMultiDenomRequired(true);
		return filter;
	}*/
	
	/**
	 * This method returns DisplayJackpotComposite
	 */
	@Override
	public Composite getComposite() {
		return dispJackpotComposite;
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());	
	}
	
	private class DisplayMouseListener implements MouseListener{

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
				if(control instanceof SDSImageLabel) {
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("display")) {

						/** Populating the form with screen values * */
						populateForm(dispJackpotComposite);
						if(form.getEmployeeId() != null)
						{
							MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
						}				
						if(form.getSlotNo()!=null)
						{
							slotOrStandCurrentValue=form.getSlotNo().trim();
						}
						else if(form.getSlotLocationNo()!=null)
						{
							slotOrStandCurrentValue=form.getSlotLocationNo().trim();
						}
						List<String> fieldNames = new ArrayList<String>() ;
						fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
						fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
										
						if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))== IAppConstants.PROMPT_SLOT_NO){
							fieldNames.add(FormConstants.FORM_SLOT_NO);
						}
						else if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))== IAppConstants.PROMPT_SLOT_LOCATION){
							fieldNames.add(FormConstants.FORM_SLOT_LOCATION_NO);
						}
						if(!form.getSequenceNumber().equalsIgnoreCase("a") || form.getSequenceNumber().length()!=1){
							fieldNames.add(FormConstants.FORM_SEQUENCE_NO);
						}
										
						/** Validation of the PendingJackpotForm done field by field. */
						log.info("validate method is called");
						boolean validate = validate("DisplayJackpotForm",fieldNames, form, dispJackpotComposite);
						log.info("Validate result: " + validate);
						if(!validate){
							log.info("Validate FAILED");
							return;
						}				
						SessionUtility sessionUtility = new SessionUtility();
						if(form.getSequenceNumber().length() == 0 && slotOrStandCurrentValue.length()==0)
							{
								if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==IAppConstants.PROMPT_SLOT_NO){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO));
									dispJackpotComposite.getTxtSlotNumber().forceFocus();
								}
								else if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))== IAppConstants.PROMPT_SLOT_LOCATION){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));
									dispJackpotComposite.getTxtSlotNumber().forceFocus();
								}
								return;
							}
							else if(form.getSequenceNumber().length()>0 && slotOrStandCurrentValue.length()>0)
							{
								if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==IAppConstants.PROMPT_SLOT_NO){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO));
									dispJackpotComposite.getTxtSlotNumber().forceFocus();
								}
								else if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==IAppConstants.PROMPT_SLOT_LOCATION){
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));
									dispJackpotComposite.getTxtSlotNumber().forceFocus();
								}
								return;
							}
							
							//PROCESS EMPLOYEE ID VALIDATION
							if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
									&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.DISPLAY_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
								ProgressIndicatorUtil.openInProgressWindow();
								try{	
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getEmployeeId(), form.getEmpPassword(), MainMenuController.jackpotForm.getSiteId());
									
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
														|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
											dispJackpotComposite.getTxtEmployeeId().forceFocus();
											return;
										}
										else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
			                                    
		                                	ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											dispJackpotComposite.getTxtEmployeePwd().setText("");
											dispJackpotComposite.getTxtEmployeePwd().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isDisplayAllowed(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId()))
										{
											log.info("User does not contain the DISPLAY JP PROCESS function");									
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.DISPLAY_PROCESS_FUNC_REQUIRED));
											dispJackpotComposite.getTxtEmployeePwd().setText("");
											dispJackpotComposite.getTxtEmployeeId().forceFocus();
											return;																
										}
										else{								
											MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										}								
									}else{
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
										dispJackpotComposite.getTxtEmployeePwd().setText("");
										dispJackpotComposite.getTxtEmployeePwd().forceFocus();
										return;
									}
								}catch(Exception ex){
										log.error("Exception while checking the user authentication",ex);
								}
								finally
								{
									ProgressIndicatorUtil.closeInProgressWindow();
								}
							}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.DISPLAY_PASSWORD_ENABLED).equalsIgnoreCase("yes")){
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
											dispJackpotComposite.getTxtEmployeePwd().setText("");
											dispJackpotComposite.getTxtEmployeePwd().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isDisplayAllowed(MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getSiteId()))
										{
											log.info("User does not contain the DISPLAY JP PROCESS function");									
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.DISPLAY_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
											dispJackpotComposite.getTxtEmployeePwd().setText("");
											dispJackpotComposite.getTxtEmployeePwd().forceFocus();
											return;																
										}
										else{								
											MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										}	
									}else{
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
										dispJackpotComposite.getTxtEmployeePwd().setText("");
										dispJackpotComposite.getTxtEmployeePwd().forceFocus();
										return;
									}
								}catch(Exception ex){
									log.error("Exception while checking the employee id and password with framework");
								}
								finally
								{
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
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
											dispJackpotComposite.getTxtEmployeeId().forceFocus();
											return;
										}
										else if(!UserFunctionsUtil.isDisplayAllowed(form.getEmployeeId(), MainMenuController.jackpotForm.getSiteId()))
										{
											log.info("User does not contain the DISPLAY JP PROCESS function");									
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.DISPLAY_PROCESS_FUNC_REQUIRED));
											dispJackpotComposite.getTxtEmployeeId().forceFocus();
											return;																
										}
										else{								
											MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
										}
									}else{
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
										dispJackpotComposite.getTxtEmployeeId().forceFocus();
										return;
									}															
								}catch(Exception ex){
										log.error("Exception while checking the employee id with framework");
								}
								finally
								{
									ProgressIndicatorUtil.closeInProgressWindow();
								}
						}else if(!UserFunctionsUtil.isDisplayAllowed(MainMenuController.jackpotForm.getActorLogin(), MainMenuController.jackpotForm.getSiteId()))
						{
							log.info("User does not contain the DISPLAY JP PROCESS function");									
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.DISPLAY_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
							dispJackpotComposite.getTxtSequenceNumber().forceFocus();
							return;																
						}
						//}
						
						/*if(form.getSequenceNumber().length() == 0 && slotOrStandCurrentValue.length()==0)
						{
							if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==1){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO));
								dispJackpotComposite.getTxtSlotNumber().forceFocus();
							}
							else if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==2){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));
								dispJackpotComposite.getTxtSlotNumber().forceFocus();
							}
							return;
						}
						else if(form.getSequenceNumber().length()>0 && slotOrStandCurrentValue.length()>0)
						{
							if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==1){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO));
								dispJackpotComposite.getTxtSlotNumber().forceFocus();
							}
							else if(new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION))==2){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO));
								dispJackpotComposite.getTxtSlotNumber().forceFocus();
							}
							return;
						}*/
						
						
						
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
							MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId().trim());
						}
						
						
						if(form.getSequenceNumber().length()>0){
							MainMenuController.jackpotForm.setSequenceNoOrAll(form.getSequenceNumber().trim());
						}else{
							MainMenuController.jackpotForm.setSequenceNoOrAll("");
						
						JackpotAssetInfoDTO jackpotAssetInfoDTO = null;
						//AssetSearchFilter assetSearchFilter = setAssetSearchFilter();
						if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
							/** for slot no */	
							try{
								log.debug("Calling the getJackpotAssetInfo web method");
								log.debug("*******************************************");
								log.debug("Slot no: "+form.getSlotNo());
								log.debug("Jackpot Asset Type: "+JackpotAssetParamType.ASSET_CONFIG_NUMBER);
								log.debug("*******************************************");
								jackpotAssetInfoDTO = JackpotServiceLocator.getService().getJackpotAssetInfo( StringUtil.padAcnfNo(form.getSlotNo()), 
										JackpotAssetParamType.ASSET_CONFIG_NUMBER, MainMenuController.jackpotForm.getSiteId());
								if(jackpotAssetInfoDTO !=null){
									log.debug("Values returned after calling the getJackpotAssetInfo web method");
									log.debug("*******************************************");
									log.debug("JackpotAssetInfoDTO Values: "+jackpotAssetInfoDTO.toString());					
									log.debug("*******************************************");
									
									if(jackpotAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
										log.info("Invalid slot no");
										dispJackpotComposite.getTxtSlotNumber().forceFocus();
										return;
									}
									else if(!jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_ONLINE_STATUS)
											&& !jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_CET_STATUS)){
										
										log.info("Display jackpot process is not allowed for offline slots");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
										dispJackpotComposite.getTxtSlotNumber().setFocus();
										return;
									}
									else if(!jackpotAssetInfoDTO.getSiteId().equals(MainMenuController.jackpotForm.getSiteId())){
										log.info("Slot does not belong to this site");
										log.info("jackpotAssetInfoDTO.getSiteId(): "+jackpotAssetInfoDTO.getSiteId());
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
										dispJackpotComposite.getTxtSlotNumber().setFocus();
										return;
									}else{
										
										/** MULTI AREA SUPPORT CHECK */
										if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && jackpotAssetInfoDTO.getAreaShortName()!=null) {
											if(!MultiAreaSupportUtil.validateSlotForSelectedArea(jackpotAssetInfoDTO.getAreaShortName())){
												log.info("Slot does not belong to this area");
												MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
												dispJackpotComposite.getTxtSlotNumber().setFocus();
												return;
											}
										}
										
										MainMenuController.jackpotForm.setSlotNo(StringUtil.padAcnfNo(form.getSlotNo()));
										MainMenuController.jackpotForm.setSlotLocationNo(jackpotAssetInfoDTO.getAssetConfigLocation().toUpperCase());
									}							
								}
							}catch (JackpotEngineServiceException ex) {
								log.info("Error while calling asset to check if slot no entered is correct");
								log.error(ex);
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(ex,LabelLoader.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
								dispJackpotComposite.getTxtSlotNumber().setFocus();
								return;
							}catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							}	
						}
						else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
							/** for slot location no */
							try{
								/*log.debug("Calling the getJackpotAssetInfo web method");
								log.debug("*******************************************");
								log.debug("Slot no: "+form.getSlotNo());
								log.debug("Jackpot Asset Type: "+JackpotAssetParamType.ASSET_CONFIG_LOCATION);
								log.debug("*******************************************");
								jackpotAssetInfoDTO = JackpotServiceLocator.getService().getJackpotAssetInfo(form.getSlotLocationNo(), JackpotAssetParamType.ASSET_CONFIG_LOCATION);
								
								log.debug("Values returned after calling the getJackpotAssetInfo web method");
								log.debug("*******************************************");
								log.debug("JackpotAssetInfoDTO Values: "+jackpotAssetInfoDTO.toString());					
								log.debug("*******************************************");*/
								List<JackpotAssetInfoDTO> jpAssInfoDTOLst = null;
								log.info("Calling the getJackpotAssetInfoList web method");
								log.info("*******************************************");
								log.info("Slot location no: "+form.getSlotLocationNo());
								log.info("JackpotAssetParamType.ASSET_CONFIG_LOCATION: "+JackpotAssetParamType.ASSET_CONFIG_LOCATION);						
								log.info("*******************************************");
								jpAssInfoDTOLst = JackpotServiceLocator.getService().getJackpotListAssetInfoForLocation(form.getSlotLocationNo().toUpperCase(), MainMenuController.jackpotForm.getSiteId());
								
								log.info("Values returned after calling the getJackpotAssetInfoList web method");
								log.info("*******************************************");	
								
								if(jpAssInfoDTOLst !=null && jpAssInfoDTOLst.size()>0){
									
									if(jpAssInfoDTOLst.get(0).getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE){
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
										log.info("Invalid slot location no");
										dispJackpotComposite.getTxtSlotNumber().forceFocus();
										return;
									}
								
									else if(!jpAssInfoDTOLst.get(0).getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_ONLINE_STATUS)
											&& !jpAssInfoDTOLst.get(0).getAssetConfigStatus().equalsIgnoreCase(IAppConstants.ASSET_CET_STATUS)){
										
										log.info("Display jackpot process is not allowed for offline slots");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
										dispJackpotComposite.getTxtSlotNumber().setFocus();
										return;
									}
									/** THIS CHECK IS NOT REQUIRED AS PER SDS11.0 */
									
									/*else if(!jackpotAssetInfoDTO.getSiteId().equals(MainMenuController.jackpotForm.getSiteId())){
										log.info("Slot location no does not belong to this site");
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE));
										dispJackpotComposite.getTxtSlotNumber().setFocus();
										return;
									}*/else{
										
										/** MULTI AREA SUPPORT CHECK */
										if(MultiAreaSupportUtil.isMultiAreaSupportEnabled() && jpAssInfoDTOLst.get(0).getAreaShortName()!=null) {
											if(!MultiAreaSupportUtil.validateSlotForSelectedArea(jpAssInfoDTOLst.get(0).getAreaShortName())){
												log.info("Slot does not belong to this area");
												MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA));
												dispJackpotComposite.getTxtSlotNumber().setFocus();
												return;
											}
										}								
										MainMenuController.jackpotForm.setSlotLocationNo(form.getSlotLocationNo().toUpperCase());
										//MainMenuController.jackpotForm.setSlotNo(jackpotAssetInfoDTO.getAssetConfigNumber());
										List lstSlotNo = new ArrayList();
										for(int j=0; j< jpAssInfoDTOLst.size(); j++){									
											lstSlotNo.add(jpAssInfoDTOLst.get(j).getAssetConfigNumber());	
										}
										if(lstSlotNo!=null && lstSlotNo.size()>0){
											MainMenuController.jackpotForm.setLstSlotNo(lstSlotNo);
										}
										
									}							
								}else{
									log.debug("jpAssInfoDTOLst is null");
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
									log.info("Invalid slot location no");
									dispJackpotComposite.getTxtSlotNumber().forceFocus();
									return;
								}
							}catch (JackpotEngineServiceException ex) {
								log.info("Error while calling asset to check if slot location no entered is correct");
								log.error(ex);
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(ex,LabelLoader.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
								dispJackpotComposite.getTxtSlotNumber().setFocus();
								return;
							}catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							}	
						}						
						}
										
						if (TopMiddleController.getCurrentComposite() != null
								&& !(TopMiddleController.getCurrentComposite()
										.isDisposed())) {
							//TopMiddleController.getCurrentComposite().dispose();

							new DisplayController(
									TopMiddleController.jackpotMiddleComposite,
									SWT.NONE, new DisplayForm(), new SDSValidator(
											getClass()));
						}
					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase(
							"cancel")) {				
						if (TopMiddleController.getCurrentComposite() != null
								&& !(TopMiddleController.getCurrentComposite()
										.isDisposed())) {
							TopMiddleController.getCurrentComposite().dispose();
						}
					}
				}else if (control instanceof TSButtonLabel) {
					TSButtonLabel touchScreenRadioButton = (TSButtonLabel) control;
					String radioImageName = touchScreenRadioButton.getName();
					System.out.println("Radio Image Name:" + radioImageName);

					if (radioImageName.equalsIgnoreCase("slotstandradio")) {
						dispJackpotComposite.getTxtSlotNumber().setEnabled(true);
						dispJackpotComposite.getTxtSequenceNumber().setEnabled(false);
						dispJackpotComposite.getTxtSequenceNumber().setText("");
						
					} else if (radioImageName.equalsIgnoreCase("seqradio")) {
						dispJackpotComposite.getTxtSequenceNumber().setEnabled(true);
						dispJackpotComposite.getTxtSlotNumber().setEnabled(false);
						dispJackpotComposite.getTxtSlotNumber().setText("");
						
					} 
					dispJackpotComposite
							.getRadioButtonControl().setSelectedButton(
									touchScreenRadioButton);
					MainMenuController.jackpotForm
							.setMiddleControl(touchScreenRadioButton
									.getName());
					log.info("The selected shift is "
							+ MainMenuController.jackpotForm.getMiddleControl());
					populateForm(dispJackpotComposite);
				
				}


				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
