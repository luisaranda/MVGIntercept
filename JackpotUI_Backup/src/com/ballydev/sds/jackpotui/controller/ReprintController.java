/*****************************************************************************
 * $Id: ReprintController.java,v 1.35.1.0, 2013-10-16 07:50:09Z, SDS12.3.3 Checkin User$
 * $Date: 10/16/2013 2:50:09 AM$
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
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.composite.ReprintComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.ReprintForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.ReprintJackpotProcess;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;

/**
 * This class acts as a controller for all the events performed in the
 * ReprintComposite
 * 
 * @author vijayrajm
 * @version $Revision: 37$
 */
public class ReprintController extends SDSBaseController {

	/**
	 * ReprintForm instance
	 */
	private static ReprintForm form;

	/**
	 * ReprintComposite instance
	 */
	private ReprintComposite reprintComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * ReprintController constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public ReprintController(Composite parent, int style, ReprintForm form, SDSValidator validator)
			throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);
		createReprintComposite(parent, style, getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(reprintComposite);
		if (log.isInfoEnabled()) {
			log.info("*************" + TopMiddleController.getCurrentComposite());
		}

		// parent.layout();
		super.registerEvents(reprintComposite);
		form.addPropertyChangeListener(this);
		FocusUtility.setTextFocus(reprintComposite);
		FocusUtility.focus = false;
		registerCustomizedListeners(reprintComposite);
	}

	/**
	 * This method is used to perform action for widgetselected event
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
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
	 * 
	 * @param p
	 * @param s
	 * @param paramFlags
	 */
	public void createReprintComposite(Composite p, int s, boolean[] paramFlags) {
		reprintComposite = new ReprintComposite(p, s, paramFlags);
	}

	/**
	 * A method to get the boolean values based on the configuration parameters
	 * enabled/disabled
	 * 
	 * @return paramFlags
	 */
	public boolean[] getSiteConfigurationParameters() {
		boolean[] paramFlags = new boolean[2];
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
			paramFlags[0] = true;
		} else {
			paramFlags[0] = false;
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.REPRINT_PASSWORD_ENABLED)
				.equalsIgnoreCase("yes")) {
			paramFlags[1] = true;
		} else {
			paramFlags[1] = false;
		}

		if (log.isInfoEnabled()) {
			log.info("Employee Id Enabled..." + paramFlags[0]);
			log.info("Employee Password Enabled..." + paramFlags[0]);
		}
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

	/**
	 * @return the form
	 */
	public static ReprintForm getForm() {
		return form;
	}

	/**
	 * @param form
	 *            the form to set
	 */
	public static void setForm(ReprintForm form) {
		ReprintController.form = form;
	}

	private class ReprintMouseListener implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseDown(MouseEvent e) {

			try {
				Control control = (Control) e.getSource();

				if (!((Control) e.getSource() instanceof SDSImageLabel)) {
					return;
				}

				if (((SDSImageLabel) control).getName().equalsIgnoreCase("reprint")) {

					/** Populating the form with screen values */
					populateForm(reprintComposite);
					List<String> fieldNames = new ArrayList<String>();

					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
					}
					fieldNames.add(FormConstants.FORM_SEQUENCE_NO);

					boolean validate = validate("ReprintForm", fieldNames, form, reprintComposite);

					if (!validate) {
						if (log.isInfoEnabled()) {
							log.info("Client side validation failed");
						}
						return;
					}
					SessionUtility sessionUtility = new SessionUtility();

					// PROCESS EMPLOYEE ID VALIDATION
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
						ProgressIndicatorUtil.openInProgressWindow();
						try {
							if (log.isInfoEnabled()) {
								log.info("Called framework's authenticate user method");
							}
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form
									.getEmployeeId(), form.getEmpPassword(), MainMenuController.jackpotForm
									.getSiteId());
							if (userDtoEmpPasswordChk != null) {
								if (userDtoEmpPasswordChk.isErrorPresent()
										&& userDtoEmpPasswordChk.getMessageKey() != null
										&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY
												.equals(userDtoEmpPasswordChk.getMessageKey()) || IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE
												.equals(userDtoEmpPasswordChk.getMessageKey()))) {
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;
								} else if (userDtoEmpPasswordChk.isErrorPresent()
										&& userDtoEmpPasswordChk.getMessageKey() != null
										&& IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY
												.equals(userDtoEmpPasswordChk.getMessageKey())) {

									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeePwd().forceFocus();
									return;
								} else if (!UserFunctionsUtil.isReprintAllowed(form.getEmployeeId(),
										MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("User does not contain the PROCESS REPRINT JP function");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(
											MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;
								} else {
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form
											.getEmployeeId());
								}
							} else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(
										MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD));
								reprintComposite.getTxtEmployeePwd().setText("");
								reprintComposite.getTxtEmployeeId().forceFocus();
								return;
							}
						} catch (Exception ex) {
							JackpotExceptionUtil.getGeneralCtrllerException(ex);
							log.error("Exception while checking the user authentication", ex);
							return;
						} finally {
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					} else if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.REPRINT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
						ProgressIndicatorUtil.openInProgressWindow();
						try {
							if (log.isInfoEnabled()) {
								log.info("Called framework's authenticate user method");
							}
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(
									MainMenuController.jackpotForm.getActorLogin(), form.getEmpPassword(),
									MainMenuController.jackpotForm.getSiteId());
							if (log.isInfoEnabled()) {
								log.info("User name1 : " + userDtoEmpPasswordChk.getUserName());
								log.info("User password1 : " + userDtoEmpPasswordChk.getPassword());
							}
							if (userDtoEmpPasswordChk != null) {
								if (userDtoEmpPasswordChk.isErrorPresent()
										&& userDtoEmpPasswordChk.getMessageKey() != null
										&& IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY
												.equals(userDtoEmpPasswordChk.getMessageKey())) {

									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeePwd().forceFocus();
									return;
								} else if (!UserFunctionsUtil.isReprintAllowed(MainMenuController.jackpotForm
										.getActorLogin(), MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("User does not contain the PROCESS REPRINT JP function");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
									reprintComposite.getTxtEmployeePwd().setText("");
									reprintComposite.getTxtEmployeePwd().forceFocus();
									return;
								} else {
									MainMenuController.jackpotForm
											.setEmployeeIdPrintedSlip(MainMenuController.jackpotForm
													.getActorLogin());
								}
							} else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_PASSWORD));
								reprintComposite.getTxtEmployeePwd().setText("");
								reprintComposite.getTxtEmployeePwd().forceFocus();
								return;
							}
						} catch (Exception ex) {
							JackpotExceptionUtil.getGeneralCtrllerException(ex);
							log.error("Exception while checking the employee password with framework", ex);
							return;
						} finally {
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					} else if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
						ProgressIndicatorUtil.openInProgressWindow();
						try {
							if (log.isInfoEnabled()) {
								log.info("Called framework's authenticate user method");
							}
							UserDTO userDTOEmpIdCheck = sessionUtility.getUserDetails(form.getEmployeeId(),
									MainMenuController.jackpotForm.getSiteId());
							if (userDTOEmpIdCheck != null) {
								if (log.isInfoEnabled()) {
									log.info("User name1 : " + userDTOEmpIdCheck.getUserName());
								}
								if (userDTOEmpIdCheck.isErrorPresent()
										&& userDTOEmpIdCheck.getMessageKey() != null
										&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY
												.equals(userDTOEmpIdCheck.getMessageKey())
												|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE
														.equals(userDTOEmpIdCheck.getMessageKey()) || IAppConstants.ANA_MESSAGES_USER_NOT_PRESENT
												.equals(userDTOEmpIdCheck.getMessageKey()))) {
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED_CHK_USERNAME));
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;
								} else if (!UserFunctionsUtil.isReprintAllowed(form.getEmployeeId(),
										MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("User does not contain the PROCESS REPRINT JP function");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED));
									reprintComposite.getTxtEmployeeId().forceFocus();
									return;
								} else {
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form
											.getEmployeeId());
								}
							} else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								reprintComposite.getTxtEmployeeId().forceFocus();
								return;
							}
						} catch (Exception ex) {
							JackpotExceptionUtil.getGeneralCtrllerException(ex);
							log.error("Exception while checking the employee id with framework", ex);
							return;
						} finally {
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					} else if (!UserFunctionsUtil.isReprintAllowed(MainMenuController.jackpotForm
							.getActorLogin(), MainMenuController.jackpotForm.getSiteId())) {
						if (log.isInfoEnabled()) {
							log.info("User does not contain the PROCESS REPRINT JP function");
						}
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil
								.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
						reprintComposite.getTxtSequenceNo().forceFocus();
						return;
					}

					String cashierDeskEnabled = MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.IS_CASHIER_DESK_ENABLED);
					String enableCheckPrint = MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP);

					// SLIP SEQUENCE NO WEB CALL
					String statusDesc = null;
					short statusFlagId = 0;
					String postToAccounting = "N";
					try {
						if (log.isInfoEnabled()) {
							log.info("Calling the getJackpotStatus web method");
						}
						if (log.isDebugEnabled()) {
							log.debug("*******************************************");
						}
						if (log.isInfoEnabled()) {
							log.info("Sequence no: " + form.getSequenceNumber());
							log.info("Site id: " + MainMenuController.jackpotForm.getSiteId());
						}
						if (log.isDebugEnabled()) {
							log.debug("*******************************************");
						}

						statusFlagId = JackpotServiceLocator.getService().getJackpotStatus(
								Long.parseLong(form.getSequenceNumber()),
								MainMenuController.jackpotForm.getSiteId());
						postToAccounting = JackpotServiceLocator.getService()
								.getJackpotPostToAccountDetail(Long.parseLong(form.getSequenceNumber()),
										MainMenuController.jackpotForm.getSiteId());

						/*
						 * JackpotStatusDTO jpStatusDTO =
						 * JackpotServiceLocator.getService().getJackpotStatusAuditCode(
						 * Long.parseLong(form.getSequenceNumber()),
						 * MainMenuController.jackpotForm.getSiteId());
						 * 
						 * if(jpStatusDTO.getStatusFlagId()!=0){
						 * statusFlagId = jpStatusDTO.getStatusFlagId(); }
						 */

						if (statusFlagId != 0) {
							if (statusFlagId == ILookupTableConstants.PROCESSED_STATUS_ID) {
								statusDesc = ILookupTableConstants.PROCESSED_ST_DESC;
							} else if (statusFlagId == ILookupTableConstants.CHANGE_STATUS_ID) {
								statusDesc = ILookupTableConstants.CHANGE_ST_DESC;
							} else if (statusFlagId == ILookupTableConstants.REPRINT_STATUS_ID) {
								statusDesc = ILookupTableConstants.REPRINT_ST_DESC;
							} else if (statusFlagId == ILookupTableConstants.VOID_STATUS_ID) {
								statusDesc = ILookupTableConstants.VOID_ST_DESC;
							} else if (statusFlagId == ILookupTableConstants.PENDING_STATUS_ID) {
								statusDesc = ILookupTableConstants.PENDING_ST_DESC;
							} else if (statusFlagId == ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID) {
								statusDesc = ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC;
							} else if(statusFlagId == ILookupTableConstants.MECHANICS_DELTA_STATUS_ID) {
								statusDesc = ILookupTableConstants.MECHANICS_DELTA_ST_DESC;
							} else if(statusFlagId == ILookupTableConstants.INVALID_STATUS_ID) {
								statusDesc = ILookupTableConstants.INVALID_ST_DESC;
							} else if (statusFlagId == ILookupTableConstants.PRINTED_STATUS_ID) {
								statusDesc = ILookupTableConstants.PRINTED_ST_DESC; // FOR CASHIER DESK
							}
						}
						if (log.isInfoEnabled()) {
							log.info("Values returned after calling the getJackpotStatus web method");
							log.info("*******************************************");
							log.info("Status Description: " + statusDesc);
							log.info("*******************************************");
						}
					} catch (JackpotEngineServiceException e1) {
						JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
						handler.handleException(e1, e1.getMessage());
						return;
					} catch (Exception e2) {
						JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
						handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
						log.error("SERVICE_DOWN", e2);
						return;
					}
					if (log.isInfoEnabled()) {
						log.info("Reprint slip status: " + statusDesc);
					}
					if (statusDesc == null) {
						if (log.isInfoEnabled()) {
							log.info("Returned value is null as de seq no is not present in the DB");
						}
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
						reprintComposite.getTxtSequenceNo().forceFocus();
						return;
					} else {
						if (log.isInfoEnabled()) {
							log.info("*************** Slip Status..." + statusDesc
									+ " **************************");
						}

						/**
						 * OLD FLOW - IS_CASHIER_DESK_ENABLED = NO
						 * ***********************************************
						 * 1. allow reprint on PROCESS, CHANGE and REPRINT
						 * slips
						 * -------------------------------------------------
						 * NEW FLOW - IS_CASHIER_DESK_ENABLED = YES
						 * ***********************************************
						 * 1. Do not allow reprint if posted to accounting
						 * even if the status is CHANGE or REPRINT which
						 * means the slip is CHANGE or REPRINT in the old
						 * flow 2. Allow REPRINT if not posted to accounting
						 */

						if ( // NEW FLOW
						((cashierDeskEnabled.equalsIgnoreCase("Yes"))
								&& (postToAccounting
										.equals(ILookupTableConstants.NOT_POSTED_TO_ACCOUNTING)) && (statusDesc
								.equalsIgnoreCase(ILookupTableConstants.REPRINT_ST_DESC)
								|| statusDesc.equalsIgnoreCase(ILookupTableConstants.CHANGE_ST_DESC) || statusDesc
								.equalsIgnoreCase(ILookupTableConstants.PRINTED_ST_DESC)))
								// OLD FLOW
								|| (cashierDeskEnabled.equalsIgnoreCase("No") && (statusDesc
										.equalsIgnoreCase(ILookupTableConstants.REPRINT_ST_DESC)
										|| statusDesc
												.equalsIgnoreCase(ILookupTableConstants.CHANGE_ST_DESC) || statusDesc
										.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC)))) {
							MainMenuController.jackpotForm.setSequenceNumber(new Long(form
									.getSequenceNumber()));
							JackpotDTO reprintJackpotSlipDTO = null;
							try {
								if (log.isInfoEnabled()) {
									log.info("Calling the getReprintJackpotSlipDetails web method");
								}
								if (log.isDebugEnabled()) {
									log.debug("*******************************************");
								}
								if (log.isInfoEnabled()) {
									log.info("Sequence no: " + form.getSequenceNumber());
									log.info("Site id: " + MainMenuController.jackpotForm.getSiteId());
								}
								if (log.isDebugEnabled()) {
									log.debug("*******************************************");
								}
								reprintJackpotSlipDTO = JackpotServiceLocator.getService()
										.getReprintJackpotSlipDetails(
												Long.parseLong(form.getSequenceNumber()),
												MainMenuController.jackpotForm.getSiteId());

								System.out.println("++++++++++++ "
										+ reprintJackpotSlipDTO.getPrinterSchema());
								System.out.println("++++++++++++ "
										+ reprintJackpotSlipDTO.getSlipSchema());

								if (log.isDebugEnabled()) {
									log.debug("Values returned after calling the getReprintJackpotSlipDetails web method");
									log.debug("*******************************************");
								}
								if (log.isInfoEnabled()) {
									log.info("DTO Values: " + reprintJackpotSlipDTO.toString());
								}
								if (log.isDebugEnabled()) {
									log.debug("*******************************************");
								}
								form.setReprintJackpotSlipDTO(reprintJackpotSlipDTO);

								if (log.isDebugEnabled()) {
									log.debug("****************************************************************");
								}
								if (log.isInfoEnabled()) {
									log.info("Sequence number..." + form.getSequenceNumber());
									log.info("Transaction date..."
											+ reprintJackpotSlipDTO.getTransactionDate());
									log.info("Jackpot Amount..."
											+ reprintJackpotSlipDTO.getOriginalAmount());
									log.info("Jackpot Tax Rate Amount..."
											+ reprintJackpotSlipDTO.getTaxRateAmount());
								}
								if (log.isDebugEnabled()) {
									log.debug("****************************************************************");
								}
							} catch (JackpotEngineServiceException e1) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e1, e1.getMessage());
								return;
							} catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN", e2);
								return;
							}

							ReprintJackpotProcess reprintJpProcess = new ReprintJackpotProcess();
							reprintJpProcess.reprintJackpot();

						} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.VOID_ST_DESC)) {
							if (log.isInfoEnabled()) {
								log.info("Slip sequence " + form.getSequenceNumber()
										+ " is already voided");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.SLIP_ALREADY_VOIDED)
									+ " "
									+ LabelLoader.getLabelValue(MessageKeyConstants.FOR_THE_SEQUENCE)
									+ " " + form.getSequenceNumber());
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC)) {
							if (log.isInfoEnabled()) {
								log.info("Slip sequence " + form.getSequenceNumber()
										+ " is already credit keyed off");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.SLIP_CREDIT_KEYED_OFF_CANT_PRINT));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						} else if (statusDesc
								.equalsIgnoreCase(ILookupTableConstants.MECHANICS_DELTA_ST_DESC)) {
							if (log.isInfoEnabled()) {
								log.info("SliP has Mechanics Delta Status");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.JP_SLIP_MECH_DELTA));
							reprintComposite.getTxtSequenceNo().setFocus();
							return;
						} else if (statusDesc
								.equalsIgnoreCase(ILookupTableConstants.INVALID_ST_DESC)) {
							if (log.isInfoEnabled()) {
								log.info("SliP is Invalid");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.JP_SLIP_INVALID));
							reprintComposite.getTxtSequenceNo().setFocus();
							return;
						} else if ((cashierDeskEnabled.equalsIgnoreCase("Yes") && statusDesc
								.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC))
								|| (postToAccounting.equals(ILookupTableConstants.POSTED_TO_ACCOUNTING))) {
							if (log.isInfoEnabled()) {
								log.info("Slip sequence " + form.getSequenceNumber()
										+ " is already processed");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PROCESSED));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						} else if (cashierDeskEnabled.equalsIgnoreCase("No")
								&& statusDesc.equalsIgnoreCase(ILookupTableConstants.PRINTED_ST_DESC)) {
							if (log.isInfoEnabled()) {
								log.info("Slip sequence " + form.getSequenceNumber() + " is already printed and cannot be reprinted as cash desk is disabled.");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						} else {
							if (log.isInfoEnabled()) {
								log.info("Slip sequence " + form.getSequenceNumber()
										+ " is not printed yet");
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.SLIP_NOT_PRINTED));
							reprintComposite.getTxtSequenceNo().forceFocus();
							return;
						}
					}
					// reprintComposite.getTxtSequenceNo().setText("");
					// reprintComposite.getTxtSequenceNo().forceFocus();
					/*
					 * if(form.getEmpPassword()!=null) {
					 * reprintComposite.getTxtEmployeePwd().setText("");
					 * reprintComposite.getTxtEmployeePwd().forceFocus(); }
					 * if(form.getEmployeeId()!=null) {
					 * reprintComposite.getTxtEmployeeId().setText("");
					 * reprintComposite.getTxtEmployeeId().forceFocus(); }
					 * if(form.get)
					 */

					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					CallInitialScreenUtil initialUtil = new CallInitialScreenUtil();
					initialUtil.callReprintJPFirstScreen();
				}
			} catch (JackpotEngineServiceException ex) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(ex, ex.getMessage());
				log.error(ex);
				return;
			} catch (Exception ex) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(ex, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", ex);
				return;
			}
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
