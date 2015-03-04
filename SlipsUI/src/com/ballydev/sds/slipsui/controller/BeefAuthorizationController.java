/*****************************************************************************
 * $Id: BeefAuthorizationController.java,v 1.20, 2010-04-12 13:07:32Z, Ambereen Drewitt$
 * $Date: 4/12/2010 8:07:32 AM$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.slipsui.composite.BeefAuthorizationComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.form.BeefAuthorizationForm;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.FillUtil;
import com.ballydev.sds.slipsui.util.FocusUtility;
import com.ballydev.sds.slipsui.util.ProcessBeef;
import com.ballydev.sds.slipsui.util.SlipUILogger;
import com.ballydev.sds.slipsui.util.SlipsExceptionUtil;
import com.ballydev.sds.slipsui.util.UserFunctionsUtil;

/**
 * Controller Class for the BeefAuthorization Composite
 *
 * @author anantharajr
 * @version $Revision: 21$
 */
public class BeefAuthorizationController extends SDSBaseController {
	/**
	 * BeefAuthorizationForm Instance
	 */
	private static BeefAuthorizationForm form;

	/**
	 * BeefAuthorizationComposite Instance
	 */
	private BeefAuthorizationComposite authorizationComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * BeefAuthorizationController constructor
	 *
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public BeefAuthorizationController(Composite parent, int style,
			BeefAuthorizationForm form, SDSValidator validator)
			throws Exception {
		super(form, validator);
		this.form = form;
		createCompositeOnSiteConfigParam(parent, style);
		super.registerEvents(authorizationComposite);
		form.addPropertyChangeListener(this);
		registerCustomizedListeners(authorizationComposite);
		FocusUtility.setTextFocus(authorizationComposite);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	/*@Override
	public void widgetSelected(SelectionEvent e) {

		try {
			Control control = (Control) e.getSource();

			if (!((Control) e.getSource() instanceof CbctlButton)) {
				return;
			}

			if (((CbctlButton) control).getName().equalsIgnoreCase("next")) {
				populateForm(authorizationComposite);
				boolean validate = validate("BeefAuthorizationForm", form,
						authorizationComposite);
				log.debug("---------Validate Result: " + validate);
				if (!validate) {
					log.debug("Validation of authorizationComposite failed");
					return;
				}
				SessionUtility sessionUtility = new SessionUtility();
				
				if (form.getAuthPasswordOne().length() < 5) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
					authorizationComposite.getTxtAuthPasswordOne().forceFocus();
					return;
				}else{
					log.info("Called framework's authenticate user method");

					try {
						UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdOne(), form.getAuthPasswordOne(), MainMenuController.slipForm.getSiteId());
						if(userDtoEmpPasswordChk!=null){
							System.out.println("User name1 : "+userDtoEmpPasswordChk.getUserName());
							System.out.println("User password1 : "+userDtoEmpPasswordChk.getPassword());
							if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
								return;
							}
							else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
								IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
						            
						    	ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
								authorizationComposite.getTxtAuthPasswordOne().setText("");
								authorizationComposite.getTxtAuthPasswordOne().forceFocus();
								return;
							}else{
								MainMenuController.slipForm.setAuthEmployeeIdOne(form
										.getAuthEmployeeIdOne());
							}
						}else{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
							authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
							log.error("Error while checking the user authentication");
							return;
						}
					} catch (Exception e1) {
						log.error("Exception while checking authenticateUser of AuthEmpIdOne with framework");
						SlipsExceptionUtil.getGeneralCtrllerException(e1);					
						log.error("Exception while checking the user authentication",e1);
						return;
					}
			//}
				
			if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
				
				if (form.getAuthPasswordTwo().length() < 5) {
					MessageDialogUtil
							.displayTouchScreenErrorMsgDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
					authorizationComposite.getTxtAuthPasswordTwo()
							.forceFocus();
					return;
				}else{
					log.info("Called framework's authenticate user method");

					try {
						UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdTwo(), form.getAuthPasswordTwo(), MainMenuController.slipForm.getSiteId());
						if(userDtoEmpPasswordChk!=null){
							System.out.println("User name1 : "+userDtoEmpPasswordChk.getUserName());
							System.out.println("User password1 : "+userDtoEmpPasswordChk.getPassword());
							if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
								return;
							}
							else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
								IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
						            
						    	ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
								authorizationComposite.getTxtAuthPasswordTwo().setText("");
								authorizationComposite.getTxtAuthPasswordTwo().forceFocus();
								return;
							}else{
								MainMenuController.slipForm.setAuthEmployeeIdOne(form.getAuthEmployeeIdOne());
							}
						}else{
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
							authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
							log.error("Error while checking the user authentication");
							return;
						}
					} catch (Exception e1) {
						log.error("Exception while checking authenticateUser of AuthEmpIdTwo with framework");
						SlipsExceptionUtil.getGeneralCtrllerException(e1);							
						log.error("Exception while checking the user authentication",e1);
						return;
					}
				//}
				}
				if (MainMenuController.slipForm.getEmployeeIdPrintedSlip() != null) {
					if (MainMenuController.slipForm.getEmployeeIdPrintedSlip()
							.equalsIgnoreCase(form.getAuthEmployeeIdOne())) {
						MessageDialogUtil
								.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.EMP_ID_ONE_ALREADY_USED_FOR_TRANS));
						authorizationComposite.getTxtAuthPasswordOne().setText(
								"");
						authorizationComposite.getTxtAuthEmployeeIdOne()
								.setFocus();
						return;
					}
					if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
						if (MainMenuController.slipForm
								.getEmployeeIdPrintedSlip().equalsIgnoreCase(
										form.getAuthEmployeeIdTwo())) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
							authorizationComposite.getTxtAuthPasswordTwo()
									.setText("");
							authorizationComposite.getTxtAuthEmployeeIdTwo()
									.setFocus();
							return;
						} else if (form.getAuthEmployeeIdOne()
								.equalsIgnoreCase(form.getAuthEmployeeIdTwo())) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
							authorizationComposite.getTxtAuthPasswordTwo()
									.setText("");
							authorizationComposite.getTxtAuthEmployeeIdTwo()
									.setFocus();
							return;
						}
					}
				}

				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REQ_SUPERVISORY_AUTH_FOR_AUTH_EMPLOYEE).equalsIgnoreCase("yes") && 
						new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) >=1){
					ProgressIndicatorUtil.openInProgressWindow();
					if (!UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdOne(),MainMenuController.slipForm.getSiteId())) {		
						log.debug("SUPERVISORY_AUTHORITY_REQUIRED for auth one");
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
						authorizationComposite.getTxtAuthPasswordOne()
						.setText("");
						authorizationComposite.getTxtAuthPasswordOne().setFocus();
						return;
					}
					ProgressIndicatorUtil.closeInProgressWindow();
					if(new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) ==2
							&& !UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdTwo(),MainMenuController.slipForm.getSiteId())){
			
						log.debug("SUPERVISORY_AUTHORITY_REQUIRED for auth two");
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil
								.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
						authorizationComposite.getTxtAuthPasswordTwo()
						.setText("");
						authorizationComposite.getTxtAuthPasswordTwo().setFocus();
						return;
					}
					ProgressIndicatorUtil.closeInProgressWindow();
				}
				
				if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ENABLE_OVERRIDE_AUTH_LEVELS).equalsIgnoreCase("yes") && 
						new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) >=1){
					ProgressIndicatorUtil.openInProgressWindow();
					
					int authEmpOneAuthlevel = 0;
					int authEmpTwoAuthlevel = 0;
					if(form.getAuthEmployeeIdOne()!=null) {
						authEmpOneAuthlevel = UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdOne(), MainMenuController.slipForm.getSiteId());
					}
					int printEmpAuthlevel = UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.slipForm.getEmployeeIdPrintedSlip(), MainMenuController.slipForm.getSiteId());
					log.info("auth level of Emp id One: "+authEmpOneAuthlevel);
					log.info("auth level of emp print slip: "+printEmpAuthlevel);
					
					if(form.getAuthEmployeeIdOne() !=null && authEmpOneAuthlevel <= printEmpAuthlevel)
					{	
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
						log.info("LOW_OVERRIDE_AUTH_LEVEL for Auth One");
						authorizationComposite.getTxtAuthEmployeeIdOne().setFocus();
						authorizationComposite.getTxtAuthPasswordOne().setText("");
						return;
					}
					if(form.getAuthEmployeeIdTwo()!=null) {
						authEmpTwoAuthlevel = UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdTwo(), MainMenuController.slipForm.getSiteId());
					}
					log.info("auth level of Emp id two: "+authEmpTwoAuthlevel);
					log.info("auth level of emp print slip: "+printEmpAuthlevel);
					if(form.getAuthEmployeeIdTwo() !=null && UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdTwo(), MainMenuController.slipForm.getSiteId()) <= UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.slipForm.getEmployeeIdPrintedSlip(), MainMenuController.slipForm.getSiteId()))
					{		
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
						log.info("LOW_OVERRIDE_AUTH_LEVEL for Auth two");
						authorizationComposite.getTxtAuthEmployeeIdTwo().setFocus();
						authorizationComposite.getTxtAuthPasswordTwo().setText("");
						return;
					}
				}
				if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 1) {
					MainMenuController.slipForm.setAuthEmployeeIdOne(form
							.getAuthEmployeeIdOne());
				}
				if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
					MainMenuController.slipForm.setAuthEmployeeIdOne(form
							.getAuthEmployeeIdOne());
					MainMenuController.slipForm.setAuthEmployeeIdTwo(form
							.getAuthEmployeeIdTwo());
				}

				//try {
				ProcessBeef processBeef = new ProcessBeef();
				processBeef.processBeefSlip();
				//}
				
				new CallInitialScreen().callBeefScreen();
			}else if (((CbctlButton) control).getName().equalsIgnoreCase(
					"cancel")) {
				boolean response= false;
				response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_SLIP_PROCESS), authorizationComposite);
				if(response){
					new CallInitialScreen().callBeefScreen();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
*/
	/*
	 * (non-Javadoc)
	 *
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		BeefAuthMouseListener listener = new BeefAuthMouseListener();
		authorizationComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		authorizationComposite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		authorizationComposite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		authorizationComposite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
	}

	private class BeefAuthMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {			
			
		}

		public void mouseDown(MouseEvent e) {

			try {
				Control control = (Control) e.getSource();

				if (!((Control) e.getSource() instanceof SDSImageLabel)) {
					return;
				}

				if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {
					

					populateForm(authorizationComposite);
					boolean validate = validate("BeefAuthorizationForm", form,
							authorizationComposite);
					log.debug("---------Validate Result: " + validate);
					if (!validate) {
						log.debug("Validation of authorizationComposite failed");
						return;
					}
					SessionUtility sessionUtility = new SessionUtility();
					
					/*if (form.getAuthPasswordOne().length() < 5) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
						authorizationComposite.getTxtAuthPasswordOne().forceFocus();
						return;
					}else{*/
						log.info("Called framework's authenticate user method");

						try {
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdOne(), form.getAuthPasswordOne(), MainMenuController.slipForm.getSiteId());
							if(userDtoEmpPasswordChk!=null){
								log.debug("User name1 : "+userDtoEmpPasswordChk.getUserName());
								log.debug("User password1 : "+userDtoEmpPasswordChk.getPassword());
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
												|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
									log.info("Invalid emp id");
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
									return;
								}
								else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
							         
									log.info("Invalid emp pwd");
							    	ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									authorizationComposite.getTxtAuthPasswordOne().setText("");
									authorizationComposite.getTxtAuthPasswordOne().forceFocus();
									return;
								}else{
									MainMenuController.slipForm.setAuthEmployeeIdOne(form
											.getAuthEmployeeIdOne());
								}
							}else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
								authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
								log.error("Error while checking the user authentication");
								return;
							}
						} catch (Exception e1) {
							log.error("Exception while checking authenticateUser of AuthEmpIdOne with framework");
							SlipsExceptionUtil.getGeneralCtrllerException(e1);					
							log.error("Exception while checking the user authentication",e1);
							return;
						}
				//}
					
				if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
					
					/*if (form.getAuthPasswordTwo().length() < 5) {
						MessageDialogUtil
								.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
						authorizationComposite.getTxtAuthPasswordTwo()
								.forceFocus();
						return;
					}else{*/
						log.info("Called framework's authenticate user method");

						try {
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdTwo(), form.getAuthPasswordTwo(), MainMenuController.slipForm.getSiteId());
							if(userDtoEmpPasswordChk!=null){
								log.debug("User name1 : "+userDtoEmpPasswordChk.getUserName());
								log.debug("User password1 : "+userDtoEmpPasswordChk.getPassword());
								if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
										(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
												|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
									log.info("Invalid emp id");
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
									return;
								}
								else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
									IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){
							            
									log.info("Invalid emp pwd");
							    	ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									authorizationComposite.getTxtAuthPasswordTwo().setText("");
									authorizationComposite.getTxtAuthPasswordTwo().forceFocus();
									return;
								}else{
									MainMenuController.slipForm.setAuthEmployeeIdOne(form.getAuthEmployeeIdOne());
								}
							}else{
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
								authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
								log.error("Error while checking the user authentication");
								return;
							}
						} catch (Exception e1) {
							log.error("Exception while checking authenticateUser of AuthEmpIdTwo with framework");
							SlipsExceptionUtil.getGeneralCtrllerException(e1);							
							log.error("Exception while checking the user authentication",e1);
							return;
						}
					//}
					}
					if (MainMenuController.slipForm.getEmployeeIdPrintedSlip() != null) {
						if (MainMenuController.slipForm.getEmployeeIdPrintedSlip()
								.equalsIgnoreCase(form.getAuthEmployeeIdOne())) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.EMP_ID_ONE_ALREADY_USED_FOR_TRANS));
							authorizationComposite.getTxtAuthPasswordOne().setText(
									"");
							authorizationComposite.getTxtAuthEmployeeIdOne()
									.setFocus();
							return;
						}
						if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
							if (MainMenuController.slipForm
									.getEmployeeIdPrintedSlip().equalsIgnoreCase(
											form.getAuthEmployeeIdTwo())) {
								MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
								authorizationComposite.getTxtAuthPasswordTwo()
										.setText("");
								authorizationComposite.getTxtAuthEmployeeIdTwo()
										.setFocus();
								return;
							} else if (form.getAuthEmployeeIdOne()
									.equalsIgnoreCase(form.getAuthEmployeeIdTwo())) {
								MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
								authorizationComposite.getTxtAuthPasswordTwo()
										.setText("");
								authorizationComposite.getTxtAuthEmployeeIdTwo()
										.setFocus();
								return;
							}
						}
					}

					if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REQ_SUPERVISORY_AUTH_FOR_AUTH_EMPLOYEE).equalsIgnoreCase("yes") && 
							new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) >=1){
						ProgressIndicatorUtil.openInProgressWindow();
						if (!UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdOne(),MainMenuController.slipForm.getSiteId())) {		
							log.debug("SUPERVISORY_AUTHORITY_REQUIRED for auth one");
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil
									.displayTouchScreenInfoDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
							authorizationComposite.getTxtAuthPasswordOne()
							.setText("");
							authorizationComposite.getTxtAuthPasswordOne().setFocus();
							return;
						}
						ProgressIndicatorUtil.closeInProgressWindow();
						if(new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) ==2
								&& !UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdTwo(),MainMenuController.slipForm.getSiteId())){
				
							log.debug("SUPERVISORY_AUTHORITY_REQUIRED for auth two");
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil
									.displayTouchScreenInfoDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
							authorizationComposite.getTxtAuthPasswordTwo()
							.setText("");
							authorizationComposite.getTxtAuthPasswordTwo().setFocus();
							return;
						}
						ProgressIndicatorUtil.closeInProgressWindow();
					}
					
					if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.ENABLE_OVERRIDE_AUTH_LEVELS).equalsIgnoreCase("yes") && 
							new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) >=1){
						ProgressIndicatorUtil.openInProgressWindow();
						
						int authEmpOneAuthlevel = 0;
						int authEmpTwoAuthlevel = 0;
						if(form.getAuthEmployeeIdOne()!=null) {
							authEmpOneAuthlevel = UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdOne(), MainMenuController.slipForm.getSiteId());
						}
						int printEmpAuthlevel = UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.slipForm.getEmployeeIdPrintedSlip(), MainMenuController.slipForm.getSiteId());
						log.info("auth level of Emp id One: "+authEmpOneAuthlevel);
						log.info("auth level of emp print slip: "+printEmpAuthlevel);
						
						if(form.getAuthEmployeeIdOne() !=null && authEmpOneAuthlevel <= printEmpAuthlevel)
						{	
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
							log.info("LOW_OVERRIDE_AUTH_LEVEL for Auth One");
							authorizationComposite.getTxtAuthEmployeeIdOne().setFocus();
							authorizationComposite.getTxtAuthPasswordOne().setText("");
							return;
						}
						if(form.getAuthEmployeeIdTwo()!=null) {
							authEmpTwoAuthlevel = UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdTwo(), MainMenuController.slipForm.getSiteId());
						}
						log.info("auth level of Emp id two: "+authEmpTwoAuthlevel);
						log.info("auth level of emp print slip: "+printEmpAuthlevel);
						if(form.getAuthEmployeeIdTwo() !=null && UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdTwo(), MainMenuController.slipForm.getSiteId()) <= UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.slipForm.getEmployeeIdPrintedSlip(), MainMenuController.slipForm.getSiteId()))
						{		
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
							log.info("LOW_OVERRIDE_AUTH_LEVEL for Auth two");
							authorizationComposite.getTxtAuthEmployeeIdTwo().setFocus();
							authorizationComposite.getTxtAuthPasswordTwo().setText("");
							return;
						}
					}
					if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 1) {
						MainMenuController.slipForm.setAuthEmployeeIdOne(form
								.getAuthEmployeeIdOne());
					}
					if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
						MainMenuController.slipForm.setAuthEmployeeIdOne(form
								.getAuthEmployeeIdOne());
						MainMenuController.slipForm.setAuthEmployeeIdTwo(form
								.getAuthEmployeeIdTwo());
					}

					
					ProcessBeef processBeef = new ProcessBeef();
					processBeef.processBeefSlip();
					FillUtil.disposeCurrentMiddleComposite();					
					new CallInitialScreen().callBeefScreen();
					
				} else if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
					
					boolean response= false;
					response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_SLIP_PROCESS), authorizationComposite);
					if(response){
						new CallInitialScreen().callBeefScreen();
					}
				}
			} catch (Exception ex) {
				log.error(ex);
			}	
		}

		public void mouseUp(MouseEvent e) {
		}
		
	}
	
	
	@Override
	public Composite getComposite() {
		
		return authorizationComposite;
	}

	public void createCompositeOnSiteConfigParam(Composite parent, int style) {
		createAuthorizationComposite(parent, style,
				getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(authorizationComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
	}

	/**
	 * Method to create the AuthorizationComposite
	 *
	 * @param p
	 * @param s
	 */
	public void createAuthorizationComposite(Composite p, int s,
			boolean[] paramFlags) {
		authorizationComposite = new BeefAuthorizationComposite(p, s,
				paramFlags);
	}

	public boolean[] getSiteConfigurationParameters() {

		boolean[] paramFlags = new boolean[2];

		System.out.println("test boolean " + paramFlags[0]);
		if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 1) {
			paramFlags[0] = true;
		} else if (new Long(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.NO_OF_AUTH_SIGN_FOR_EXCESSIVE_VARIANCES)) == 2) {
			paramFlags[0] = true;
			paramFlags[1] = true;
		}
		return paramFlags;
	}

	/*
	 * Method to get the keyboard focus1600
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}
	
}
