/*****************************************************************************
 * $Id: EmployeeShiftSlotDetailsController.java,v 1.46, 2010-11-30 13:12:11Z, Subha Viswanathan$
 * $Date: 11/30/2010 7:12:11 AM$
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
import org.eclipse.swt.events.TraverseEvent;
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
import com.ballydev.sds.jackpotui.composite.EmployeeShiftSlotDetailsCompoisite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.EmployeeShiftSlotDetailsForm;
import com.ballydev.sds.jackpotui.form.PendingJackpotDisplayForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.MultiAreaSupportUtil;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;

/**
 * Controller class for the EmployeeShiftSlotDetailsComposite
 * 
 * @author anantharajr
 * @version $Revision: 47$
 */
public class EmployeeShiftSlotDetailsController extends SDSBaseController implements FormConstants {
	/**
	 * EmployeeShiftSlotDetailsForm Instance
	 */
	public static EmployeeShiftSlotDetailsForm form;

	/**
	 * employeeShiftSlotDetailsCompoisite Instance
	 */
	private EmployeeShiftSlotDetailsCompoisite employeeShiftSlotDetailsComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * This variable hold the value of slot or stand number based on site
	 * config.
	 */
	private String slotOrStandCurrentValue = null;

	/**
	 * For checking if the slot / slot location no field was populated String
	 * can contain either "slot" or "stand"
	 */
	private String slotOrSlotLocationSiteField = IAppConstants.EMPTY_STRING;

	/**
	 * PendingJackpotController Constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public EmployeeShiftSlotDetailsController(Composite parent, int style, EmployeeShiftSlotDetailsForm form,
			SDSValidator validator) throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);
		createCompositeOnSiteConfigParam(parent, style);
		// parent.layout();
		// setting the process flag id to a NORMAL jackpot process
		MainMenuController.jackpotForm.setProcessFlagId(ILookupTableConstants.NORMAL_PROCESS_FLAG);
		super.registerEvents(employeeShiftSlotDetailsComposite);
		form.addPropertyChangeListener(this);
		populateScreen(employeeShiftSlotDetailsComposite);
		setPrevSelectedBtnGrp();
		FocusUtility.setTextFocus(employeeShiftSlotDetailsComposite);
		FocusUtility.focus = false;
		registerCustomizedListeners(employeeShiftSlotDetailsComposite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected
	 * (org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#
	 * registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		EmployeeMouseListener listener = new EmployeeMouseListener();
		if (employeeShiftSlotDetailsComposite.getShiftControl() != null) {
			employeeShiftSlotDetailsComposite.getShiftControl().getBtnDay().addMouseListener(listener);
			employeeShiftSlotDetailsComposite.getShiftControl().getBtnDay().addTraverseListener(this);
			employeeShiftSlotDetailsComposite.getShiftControl().getBtnSwing().addMouseListener(listener);
			employeeShiftSlotDetailsComposite.getShiftControl().getBtnSwing().addTraverseListener(this);
			employeeShiftSlotDetailsComposite.getShiftControl().getBtnGraveyard().addMouseListener(listener);
			employeeShiftSlotDetailsComposite.getShiftControl().getBtnGraveyard().addTraverseListener(this);
		}

		employeeShiftSlotDetailsComposite.getStandSlotRadioImage().addMouseListener(listener);
		employeeShiftSlotDetailsComposite.getStandSlotRadioImage().addTraverseListener(this);
		employeeShiftSlotDetailsComposite.getSeqRadioImage().addMouseListener(listener);
		employeeShiftSlotDetailsComposite.getSeqRadioImage().addTraverseListener(this);
		employeeShiftSlotDetailsComposite.getMinRadioImage().addMouseListener(listener);
		employeeShiftSlotDetailsComposite.getMinRadioImage().addTraverseListener(this);
		employeeShiftSlotDetailsComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		employeeShiftSlotDetailsComposite.getButtonComposite().getBtnNext().getTextLabel()
				.addTraverseListener(this);

	}

	@Override
	public void keyTraversed(TraverseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {

	}

	public void createCompositeOnSiteConfigParam(Composite parent, int style) {
		if (log.isInfoEnabled()) {
			log.info("inside createCompositeOnSiteConfigParam");
		}
		createPendingJackpotComposite(parent, style, getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(employeeShiftSlotDetailsComposite);
		if (log.isInfoEnabled()) {
			log.info("*************" + TopMiddleController.getCurrentComposite());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	public EmployeeShiftSlotDetailsCompoisite getComposite() {
		return employeeShiftSlotDetailsComposite;
	}

	/**
	 * Method to create the PendingJackpotComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createPendingJackpotComposite(Composite p, int s, String[] paramFlags) {
		employeeShiftSlotDetailsComposite = new EmployeeShiftSlotDetailsCompoisite(p, s, paramFlags);
	}

	public String[] getSiteConfigurationParameters() {
		String[] paramFlags = null;
		try {
			if (log.isInfoEnabled()) {
				log.info("inside getSiteConfigurationParameters of EmployeeShiftSlotDetailsCtrller");
			}
			paramFlags = new String[4];
			if (log.isInfoEnabled()) {
				log.info("test boolean " + paramFlags[0]);
			}

			if (MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
				paramFlags[0] = "true";
			} else {
				paramFlags[0] = "false";
			}

			if (MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PENDING_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
				paramFlags[1] = "true";
			} else {
				paramFlags[1] = "false";
			}

			if (new Long(
					MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
				paramFlags[2] = "1";
			} else if (new Long(
					MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
				paramFlags[2] = "2";
			}
			if (MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
					IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
				MainMenuController.jackpotForm.setShift(IAppConstants.SITE_VALUE_SHIFT_DAY);
				paramFlags[3] = "P";
			} else if (MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
					IAppConstants.SITE_VALUE_SHIFT_DAY)) {
				MainMenuController.jackpotForm.setShift(IAppConstants.SITE_VALUE_SHIFT_DAY);
				paramFlags[3] = "NoP";
			} else if (MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
					IAppConstants.SITE_VALUE_SHIFT_SWING)) {
				MainMenuController.jackpotForm.setShift(IAppConstants.SITE_VALUE_SHIFT_SWING);
				paramFlags[3] = "NoP";
			} else if (MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase(
					IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
				MainMenuController.jackpotForm.setShift(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD);
				paramFlags[3] = "NoP";
			} else {
				MainMenuController.jackpotForm.setShift(IAppConstants.SITE_VALUE_SHIFT_DAY);
				paramFlags[3] = "P";
			}
		} catch (Exception e) {
			log.error(e);
		}
		return paramFlags;
	}

	/**
	 * @return the slotOrStandCurrentValue
	 */
	public String getSlotOrStandCurrentValue() {
		return slotOrStandCurrentValue;
	}

	/**
	 * @param slotOrStandCurrentValue
	 *            the slotOrStandCurrentValue to set
	 */
	public void setSlotOrStandCurrentValue(String slotOrStandCurrentValue) {
		this.slotOrStandCurrentValue = slotOrStandCurrentValue;
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}

	/**
	 * @return the form
	 */
	public EmployeeShiftSlotDetailsForm getForm() {
		return form;
	}

	/**
	 * @param form
	 *            the form to set
	 */
	public void setForm(EmployeeShiftSlotDetailsForm form) {
		this.form = form;
	}

	/**
	 * Method to set the previous selected button in the SHIFT group
	 */
	public void setPrevSelectedBtnGrp() {
		if (employeeShiftSlotDetailsComposite.getBottomComposite() != null
				&& !employeeShiftSlotDetailsComposite.getBottomComposite().isDisposed()
				&& employeeShiftSlotDetailsComposite.getShiftControl() != null) {
			if (form.getDayYes()) {
				employeeShiftSlotDetailsComposite.getShiftControl().getShiftRadioButtonComposite()
						.setSelectedButton(employeeShiftSlotDetailsComposite.getShiftControl().getBtnDay());
			} else if (form.getSwingYes()) {
				employeeShiftSlotDetailsComposite.getShiftControl().getShiftRadioButtonComposite()
						.setSelectedButton(employeeShiftSlotDetailsComposite.getShiftControl().getBtnSwing());
			} else if (form.getGraveyardYes()) {
				employeeShiftSlotDetailsComposite
						.getShiftControl()
						.getShiftRadioButtonComposite()
						.setSelectedButton(
								employeeShiftSlotDetailsComposite.getShiftControl().getBtnGraveyard());
			}
		}

		if (employeeShiftSlotDetailsComposite.getMiddleComposite() != null
				&& !employeeShiftSlotDetailsComposite.getMiddleComposite().isDisposed()) {
			if (form.getSlotStandYes()) {
				employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setEditable(true);
				employeeShiftSlotDetailsComposite.getRadioButtonControl().setSelectedButton(
						employeeShiftSlotDetailsComposite.getStandSlotRadioImage());
				employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setText(IAppConstants.EMPTY_STRING);
				employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setEnabled(false);
				employeeShiftSlotDetailsComposite.getTxtNumOfMins().setText(IAppConstants.EMPTY_STRING);
				employeeShiftSlotDetailsComposite.getTxtNumOfMins().setEnabled(false);
			} else if (form.getSequenceYes()) {
				employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setEnabled(true);
				employeeShiftSlotDetailsComposite.getRadioButtonControl().setSelectedButton(
						employeeShiftSlotDetailsComposite.getSeqRadioImage());
				employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setText(
						IAppConstants.EMPTY_STRING);
				employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setEnabled(false);
				employeeShiftSlotDetailsComposite.getTxtNumOfMins().setText(IAppConstants.EMPTY_STRING);
				employeeShiftSlotDetailsComposite.getTxtNumOfMins().setEnabled(false);
			} else if (form.getMinutesYes()) {
				employeeShiftSlotDetailsComposite.getTxtNumOfMins().setEnabled(true);
				employeeShiftSlotDetailsComposite.getRadioButtonControl().setSelectedButton(
						employeeShiftSlotDetailsComposite.getMinRadioImage());
				employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setText(
						IAppConstants.EMPTY_STRING);
				employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setEnabled(false);
				employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setText(IAppConstants.EMPTY_STRING);
				employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setEnabled(false);
			}
		}
	}

	private class EmployeeMouseListener implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseDown(MouseEvent e) {
			try {
				Control control = (Control) e.getSource();
				if (!(control instanceof TSButtonLabel) && !(control instanceof SDSImageLabel)) {
					return;
				}
				if (control instanceof SDSImageLabel) {
					SessionUtility sessionUtility = new SessionUtility();

					populateForm(employeeShiftSlotDetailsComposite);

					if (form.getEmployeeId() != null) {
						MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
					}
					if (form.getSlotNo() != null) {
						slotOrStandCurrentValue = form.getSlotNo().trim();
					} else if (form.getSlotLocationNo() != null) {
						slotOrStandCurrentValue = form.getSlotLocationNo().trim();
					}
					List<String> fieldNames = new ArrayList<String>();
					fieldNames.add(FormConstants.FORM_NUMBER_OF_MINUTES);
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_EMPLOYEE_ID);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.PENDING_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_EMP_PASSWORD);
					}
					if (new Long(
							MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
						slotOrSlotLocationSiteField = "slot";
						fieldNames.add(FormConstants.FORM_SLOT_NO);
					} else if (new Long(
							MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
						slotOrSlotLocationSiteField = "stand";
						fieldNames.add(FormConstants.FORM_SLOT_LOCATION_NO);
					}
					fieldNames.add(FormConstants.FORM_SEQUENCE_NO);

					/**
					 * Validation of the PendingJackpotForm done field by field.
					 */

					boolean validate = validate("EmployeeShiftSlotDetailsForm", fieldNames, form,
							employeeShiftSlotDetailsComposite);
					if (!validate) {
						if (log.isInfoEnabled()) {
							log.info("Validate FAILED");
						}
						return;
					}

					if (form.getNumOfMinutes() != null
							&& !form.getNumOfMinutes().trim().isEmpty()) {
						try {
							if (log.isInfoEnabled()) {
								log.info("No of Minutes : " + form.getNumOfMinutes());
							}
							MainMenuController.jackpotForm
									.setNumOfMinutes(new Integer(form.getNumOfMinutes()));
						} catch (Exception e1) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							log.error(e1);
							return;
						}
					} else {
						MainMenuController.jackpotForm.setNumOfMinutes(0);
					}

					if (form.getSequenceNumber().length() == 0 && slotOrStandCurrentValue.length() == 0
							&& form.getNumOfMinutes().length() == 0) {
						if (new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO_OR_MINUTES));

							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
						} else if (new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO_OR_MINUTES));
							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
						}
						return;
					}

					int count = 0;
					if (form.getSequenceNumber() != null && form.getSequenceNumber().length() != 0) {
						++count;
					}
					if (slotOrStandCurrentValue != null && slotOrStandCurrentValue.length() != 0) {
						++count;
					}
					if (form.getNumOfMinutes() != null && (form.getNumOfMinutes().length() != 0)) {
						++count;
					}
					if (form.getNumOfMinutes() != null && form.getNumOfMinutes().equals("0")) {
						--count;
					}
					if (count != 1) {
						if (new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO_OR_MINUTES));
						} else if (new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {
							MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO_OR_MINUTES));
						}
						employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
						return;
					}

					// PROCESS EMPLOYEE ID VALIDATION
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.PENDING_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase(
									"yes")) {
						ProgressIndicatorUtil.openInProgressWindow();
						try {
							if (log.isInfoEnabled()) {
								log.info("Called framework's authenticate user method");
							}
							UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(
									form.getEmployeeId(), form.getEmpPassword(),
									MainMenuController.jackpotForm.getSiteId());
							if (userDtoEmpPasswordChk != null) {
								if (userDtoEmpPasswordChk.isErrorPresent()
										&& userDtoEmpPasswordChk.getMessageKey() != null
										&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY
												.equals(userDtoEmpPasswordChk.getMessageKey()) || IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE
												.equals(userDtoEmpPasswordChk.getMessageKey()))) {
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else if (userDtoEmpPasswordChk.isErrorPresent()
										&& userDtoEmpPasswordChk.getMessageKey() != null
										&& IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY
												.equals(userDtoEmpPasswordChk.getMessageKey())) {

									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().forceFocus();
									return;
								} else if (!UserFunctionsUtil.isPendingJackpotAllowed(form.getEmployeeId(),
										MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("User does not contain the PENDING JP PROCESS function");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.PENDING_PROCESS_FUNC_REQUIRED));
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS)
										.equalsIgnoreCase("yes")
										&& !userDtoEmpPasswordChk.isSignedOn()) {
									if (log.isInfoEnabled()) {
										log.info("-----JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS-----");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else {
									ProgressIndicatorUtil.closeInProgressWindow();
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form
											.getEmployeeId());
								}
							} else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
								employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
										IAppConstants.EMPTY_STRING);
								employeeShiftSlotDetailsComposite.getTxtEmpPassword().forceFocus();
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
							ISiteConfigConstants.PENDING_JACKPOT_PASSWORD_ENABLED).equalsIgnoreCase("yes")) {
						ProgressIndicatorUtil.openInProgressWindow();
						try {
							if (log.isInfoEnabled()) {
								log.info("Called framework's authenticate user method");
							}
							// UserDTO userDtoEmpPasswordChk =
							// FrameworkServiceLocator.getService().authenticateUser(MainMenuController.jackpotForm.getActorLogin(),
							// form.getEmpPassword(),
							// MainMenuController.jackpotForm.getSiteId());
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
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().forceFocus();
									return;
								} else if (!UserFunctionsUtil.isPendingJackpotAllowed(
										MainMenuController.jackpotForm.getActorLogin(),
										MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("User does not contain the PENDING JP PROCESS function");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.PENDING_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().forceFocus();
									return;
								} else if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS)
										.equalsIgnoreCase("yes")
										&& !userDtoEmpPasswordChk.isSignedOn()) {
									if (log.isInfoEnabled()) {
										log.info("-----JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS-----");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else {
									ProgressIndicatorUtil.closeInProgressWindow();
									MainMenuController.jackpotForm
											.setEmployeeIdPrintedSlip(MainMenuController.jackpotForm
													.getActorLogin());
								}
							} else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
								employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
										IAppConstants.EMPTY_STRING);
								employeeShiftSlotDetailsComposite.getTxtEmpPassword().forceFocus();
								return;
							}
						} catch (Exception ex) {
							JackpotExceptionUtil.getGeneralCtrllerException(ex);
							log.error("Exception while checking the employee id and password with framework",
									ex);
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
							// UserDTO
							// userDTOEmpIdCheck=FrameworkServiceLocator.getService().getUserDetails(form.getEmployeeId(),
							// MainMenuController.jackpotForm.getSiteId());
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
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else if (!UserFunctionsUtil.isPendingJackpotAllowed(form.getEmployeeId(),
										MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("User does not contain the PENDING JP PROCESS function");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.PENDING_PROCESS_FUNC_REQUIRED));
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS)
										.equalsIgnoreCase("yes")
										&& !userDTOEmpIdCheck.isSignedOn()) {
									if (log.isInfoEnabled()) {
										log.info("-----JACKPOT_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS-----");
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.JACKPOT_FUNC_REQ_SIGNED_ON_USER));
									employeeShiftSlotDetailsComposite.getTxtEmpPassword().setText(
											IAppConstants.EMPTY_STRING);
									employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
									return;
								} else {
									ProgressIndicatorUtil.closeInProgressWindow();
									MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form
											.getEmployeeId());
								}
							} else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
								employeeShiftSlotDetailsComposite.getTxtEmployeeId().forceFocus();
								return;
							}
						} catch (Exception ex) {
							JackpotExceptionUtil.getGeneralCtrllerException(ex);
							log.error("Exception while checking the employee id with framework", ex);
							return;
						} finally {
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					} else if (!UserFunctionsUtil.isPendingJackpotAllowed(
							MainMenuController.jackpotForm.getActorLogin(),
							MainMenuController.jackpotForm.getSiteId())) {
						if (log.isInfoEnabled()) {
							log.info("User does not contain the PENDING JP PROCESS function");
						}
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil
								.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.PENDING_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER));
						employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
						return;
					}

					// }

					/*
					 * if(form.getSequenceNumber().length()==0 &&
					 * slotOrStandCurrentValue.length()==0 &&
					 * form.getNumOfMinutes().length()==0) { if (new
					 * Long(MainMenuController
					 * .jackpotSiteConfigParams.get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO_OR_MINUTES
					 * )); } else if (new
					 * Long(MainMenuController.jackpotSiteConfigParams
					 * .get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue(MessageKeyConstants.
					 * PLZ_ENTER_STAND_OR_SEQUENCE_NO_OR_MINUTES)); }
					 * employeeShiftSlotDetailsComposite
					 * .getTxtSlotSeqLocationNo().setFocus(); return; }
					 * 
					 * int count=0; if(form.getSequenceNumber()!=null &&
					 * form.getSequenceNumber().length()!=0) { ++count; }
					 * if(slotOrStandCurrentValue!=null &&
					 * slotOrStandCurrentValue.length()!=0) { ++count; }
					 * if(form.getNumOfMinutes()!=null &&
					 * (form.getNumOfMinutes().length()!=0 )) { ++count; }
					 * if(form.getNumOfMinutes()!=null &&
					 * form.getNumOfMinutes().equals("0")) { --count; }
					 * if(count!=1) { MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO_OR_MINUTES
					 * )); return; }
					 */

					/** DO VALIDATION OF THE THREE OPTIONAL TEXT BOXES */
					/*
					 * if ((form.getSequenceNumber().length() == 0 &&
					 * slotOrStandCurrentValue.length() == 0 &&
					 * (form.getNumOfMinutes().equals("0"))) ||
					 * (form.getSequenceNumber().length() > 0 &&
					 * slotOrStandCurrentValue.length() > 0 &&
					 * !form.getNumOfMinutes().equals("0"))) { if (new
					 * Long(MainMenuController
					 * .jackpotSiteConfigParams.get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO_OR_MINUTES
					 * )); } else if (new
					 * Long(MainMenuController.jackpotSiteConfigParams
					 * .get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue(MessageKeyConstants.
					 * PLZ_ENTER_STAND_OR_SEQUENCE_NO_OR_MINUTES)); }
					 * employeeShiftSlotDetailsComposite
					 * .getTxtSlotSeqLocationNo().setFocus(); return; } else if
					 * ((form.getSequenceNumber().length() > 0 &&
					 * slotOrStandCurrentValue.length() > 0 &&
					 * form.getNumOfMinutes().equals("0"))) { if (new
					 * Long(MainMenuController
					 * .jackpotSiteConfigParams.get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_SLOT_OR_SEQUENCE_NO)); }
					 * else if (new
					 * Long(MainMenuController.jackpotSiteConfigParams
					 * .get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_STAND_OR_SEQUENCE_NO)); }
					 * employeeShiftSlotDetailsComposite
					 * .getTxtSlotSeqLocationNo().setFocus(); return; }else if
					 * ((form.getSequenceNumber().length() == 0 &&
					 * slotOrStandCurrentValue.length() > 0 &&
					 * !form.getNumOfMinutes().equals("0"))) { if (new
					 * Long(MainMenuController
					 * .jackpotSiteConfigParams.get(ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 1) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_SLOT_OR_MINUTES)); } else
					 * if (new
					 * Long(MainMenuController.jackpotSiteConfigParams.get
					 * (ISiteConfigConstants
					 * .PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == 2) {
					 * MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_STAND_OR_MINUTES)); }
					 * employeeShiftSlotDetailsComposite
					 * .getTxtSlotSeqLocationNo().setFocus(); return; } else
					 * if((form.getSequenceNumber().length() > 0 &&
					 * slotOrStandCurrentValue.length() ==0 &&
					 * !form.getNumOfMinutes().equals("0"))){ MessageDialogUtil
					 * .displayTouchScreenErrorMsgDialog(LabelLoader
					 * .getLabelValue
					 * (MessageKeyConstants.PLZ_ENTER_SEQUENCE_NO_OR_MINUTES));
					 * employeeShiftSlotDetailsComposite
					 * .getTxtSequenceNumber().setFocus(); return; }
					 */

					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
						MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(form.getEmployeeId());
					}

					if (form.getSequenceNumber().length() > 0) {
						String statusDesc = null;
						short statusFlagId = 0;
						try {
							if (log.isInfoEnabled()) {
								log.info("Calling the web method getJackpotStatus");
								log.info("Calling the getJackpotStatus web method");
								log.info("*******************************************");
								log.info("Sequence no: " + form.getSequenceNumber());
								log.info("Site id: " + MainMenuController.jackpotForm.getSiteId());
								log.info("*******************************************");
							}
							statusFlagId = JackpotServiceLocator.getService().getJackpotStatus(
									Long.parseLong(form.getSequenceNumber()),
									MainMenuController.jackpotForm.getSiteId());
							if (log.isInfoEnabled()) {
								log.info("++++++++++ Status Flag Id: " + statusFlagId);
							}
							switch (statusFlagId) {
							case ILookupTableConstants.PRINTED_STATUS_ID: 
								// Added Printed Status ID for cashier desk flow
								statusDesc = ILookupTableConstants.PRINTED_ST_DESC;
								break;
							case ILookupTableConstants.PROCESSED_STATUS_ID:
								statusDesc = ILookupTableConstants.PROCESSED_ST_DESC;
								break;

							case ILookupTableConstants.CHANGE_STATUS_ID:
								statusDesc = ILookupTableConstants.CHANGE_ST_DESC;
								break;

							case ILookupTableConstants.REPRINT_STATUS_ID:
								statusDesc = ILookupTableConstants.REPRINT_ST_DESC;
								break;

							case ILookupTableConstants.VOID_STATUS_ID:
								statusDesc = ILookupTableConstants.VOID_ST_DESC;
								break;

							case ILookupTableConstants.CREDIT_KEY_OFF_STATUS_ID:
								statusDesc = ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC;
								break;

							case ILookupTableConstants.PENDING_STATUS_ID:
								statusDesc = ILookupTableConstants.PENDING_ST_DESC;
								break;
								
							case ILookupTableConstants.MECHANICS_DELTA_STATUS_ID:
								statusDesc = ILookupTableConstants.MECHANICS_DELTA_ST_DESC;
								break;
								
							case ILookupTableConstants.INVALID_STATUS_ID:
								statusDesc = ILookupTableConstants.INVALID_ST_DESC;
								break;
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
							log.error("Exception while calling the getJackpotStatus web method", e1);
							return;
						} catch (Exception e2) {
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN", e2);
							return;
						}
						if (statusDesc != null) {
							if ((MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("No") && statusDesc
									.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC))
									|| (MainMenuController.jackpotSiteConfigParams.get(
											ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase(
											"Yes") && statusDesc
											.equalsIgnoreCase(ILookupTableConstants.PRINTED_ST_DESC))
									|| statusDesc.equalsIgnoreCase(ILookupTableConstants.REPRINT_ST_DESC)
									|| statusDesc.equalsIgnoreCase(ILookupTableConstants.CHANGE_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("Slip already printed");
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PRINTED));
								employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
								return;
							} else if (MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.IS_CASHIER_DESK_ENABLED).equalsIgnoreCase("Yes")
									&& statusDesc.equalsIgnoreCase(ILookupTableConstants.PROCESSED_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("Slip already processed");
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.SLIP_ALREADY_PROCESSED));
								employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
								return;
							} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.VOID_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("Slip already voided");
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.JP_SLIP_ALREADY_VOIDED));
								employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
								return;
							} else if (statusDesc
									.equalsIgnoreCase(ILookupTableConstants.CREDIT_KEY_OFF_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("Slip already credit keyed off");
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.SLIP_CREDIT_KEYED_OFF_CANT_PRINT));
								employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
								return;
							} else if (statusDesc
									.equalsIgnoreCase(ILookupTableConstants.MECHANICS_DELTA_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("SliP has Mechanics Delta Status");
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.JP_SLIP_MECH_DELTA));
								employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
								return;
							} else if (statusDesc
									.equalsIgnoreCase(ILookupTableConstants.INVALID_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("SliP is Invalid");
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.JP_SLIP_INVALID));
								employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
								return;
							} else if (statusDesc.equalsIgnoreCase(ILookupTableConstants.PENDING_ST_DESC)) {
								if (log.isInfoEnabled()) {
									log.info("Slip is pending");
								}
								MainMenuController.jackpotForm.setSequenceNumber(new Long(form
										.getSequenceNumber().trim()));
							}
						} else if (statusDesc == null) {
							if (log.isInfoEnabled()) {
								log.info("Slip is not found for the sequence: " + form.getSequenceNumber());
							}
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.SLIP_NOT_FOUND));
							employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setFocus();
							return;
						}
					} else if (slotOrSlotLocationSiteField.equals("slot") ? form.getSlotNo().length() > 0
							: form.getSlotLocationNo().length() > 0) {

						// SLOT NO VALIDATION

						// form.getSlotNo().length()>0 ||
						// form.getSlotLocationNo().length()>0){
						JackpotAssetInfoDTO jackpotAssetInfoDTO = null;
						if (new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_NO) {
							/** for slot no */
							try {
								if (log.isInfoEnabled()) {
									log.info("Calling the getJackpotAssetInfo web method");
									log.info("*******************************************");
									log.info("Slot no: " + form.getSlotNo());
									log.info("JackpotAssetParamType.ASSET_CONFIG_NUMBER: "
											+ JackpotAssetParamType.ASSET_CONFIG_NUMBER);
									log.info("*******************************************");
								}
								jackpotAssetInfoDTO = JackpotServiceLocator.getService().getJackpotAssetInfo(
										StringUtil.padAcnfNo(form.getSlotNo()),
										JackpotAssetParamType.ASSET_CONFIG_NUMBER,
										MainMenuController.jackpotForm.getSiteId());

								if (log.isInfoEnabled()) {
									log.info("Values returned after calling the getJackpotAssetInfo web method");
									log.info("*******************************************");
									log.info("JackpotAssetInfoDTO values returned: "
											+ jackpotAssetInfoDTO.toString());
									log.info("*******************************************");
								}
							} catch (JackpotEngineServiceException ex) {
								log.info("Error while calling asset to check if slot no entered is correct");
								log.error(ex);
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(ex, LabelLoader
										.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
								employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setFocus();
								return;
							} catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN", e2);
								return;
							}
							if (jackpotAssetInfoDTO != null) {
								if (jackpotAssetInfoDTO.getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_SLOT_NO));
									if (log.isInfoEnabled()) {
										log.info("Invalid slot no");
									}
									employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
									return;
								}
								if (!jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(
										IAppConstants.ASSET_ONLINE_STATUS)
										&& !jackpotAssetInfoDTO.getAssetConfigStatus().equalsIgnoreCase(
												IAppConstants.ASSET_CET_STATUS)) {

									if (log.isInfoEnabled()) {
										log.info("Manual jackpot process is not allowed for offline slots");
									}
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.SLOT_IS_NOT_ONLINE));
									employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setFocus();
									return;
								}
								if (!jackpotAssetInfoDTO.getSiteId().equals(
										MainMenuController.jackpotForm.getSiteId())) {
									if (log.isInfoEnabled()) {
										log.info("Slot does not belong to this site");
									}
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_SITE));
									employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setFocus();
									return;
								} else {
									/** MULTI AREA SUPPORT CHECK */
									if (MultiAreaSupportUtil.isMultiAreaSupportEnabled()
											&& jackpotAssetInfoDTO.getAreaShortName() != null) {
										if (!MultiAreaSupportUtil
												.validateSlotForSelectedArea(jackpotAssetInfoDTO
														.getAreaShortName())) {
											if (log.isInfoEnabled()) {
												log.info("Slot does not belong to this area");
											}
											MessageDialogUtil
													.displayTouchScreenErrorMsgDialog(LabelLoader
															.getLabelValue(MessageKeyConstants.SLOT_DOES_NOT_BELONG_TO_THIS_AREA));
											employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo()
													.setFocus();
											return;
										}
									}

									/*
									 * List<JackpotGameDetailsDTO>
									 * gameDetailsList =
									 * jackpotAssetInfoDTO.getListGameDetails();
									 * if(gameDetailsList != null){
									 * log.info("gameDetailsList size: "
									 * +gameDetailsList.size());
									 * if(gameDetailsList.size() > 0){ for(int
									 * i=0; i< gameDetailsList.size(); i++){
									 * MainMenuController
									 * .jackpotForm.setThemeName
									 * (gameDetailsList.get(i).getThemeName());
									 * MainMenuController
									 * .jackpotForm.setDenomination
									 * (ConversionUtil
									 * .dollarToCents(gameDetailsList
									 * .get(i).getDenomAmount()));
									 * log.info("Denom: "
									 * +MainMenuController.jackpotForm
									 * .getDenomination());
									 * log.info("theme name: "
									 * +gameDetailsList.get(i).getThemeName());
									 * } } }
									 */
									MainMenuController.jackpotForm.setSlotNo(StringUtil.padAcnfNo(form
											.getSlotNo()));
									MainMenuController.jackpotForm.setSlotLocationNo(jackpotAssetInfoDTO
											.getAssetConfigLocation().toUpperCase());
									MainMenuController.jackpotForm.setArea(jackpotAssetInfoDTO
											.getAreaShortName());
									MainMenuController.jackpotForm.setSealNumber(jackpotAssetInfoDTO
											.getSealNumber());
								}
							}
							/*
							 * }catch (JackpotEngineServiceException ex) {
							 * log.info(
							 * "Error while calling asset to check if slot no entered is correct"
							 * ); log.error(ex); JackpotUiExceptionHandler
							 * handler = new JackpotUiExceptionHandler();
							 * handler
							 * .handleException(ex,LabelLoader.getLabelValue
							 * (MessageKeyConstants
							 * .UNABLE_TO_FETCH_ASSET_DETAILS));
							 * employeeShiftSlotDetailsComposite
							 * .getTxtSlotSeqLocationNo().setFocus(); return;
							 * }catch (Exception e2) { JackpotUiExceptionHandler
							 * handler = new JackpotUiExceptionHandler();
							 * handler
							 * .handleException(e2,MessageKeyConstants.SERVICE_DOWN
							 * ); log.error("SERVICE_DOWN",e2); return; }
							 */
						} else if (new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION)) == IAppConstants.PROMPT_SLOT_LOCATION) {

							// STAND NO VALIDATION
							List<JackpotAssetInfoDTO> jpAssInfoDTOLst = null;
							try {
								if (log.isInfoEnabled()) {
									log.info("Calling the getJackpotAssetInfoList web method");
									log.info("*******************************************");
									log.info("Slot location no: " + form.getSlotLocationNo());
									log.info("JackpotAssetParamType.ASSET_CONFIG_LOCATION: "
											+ JackpotAssetParamType.ASSET_CONFIG_LOCATION);
									log.info("*******************************************");
								}
								jpAssInfoDTOLst = JackpotServiceLocator.getService()
										.getJackpotListAssetInfoForLocation(
												form.getSlotLocationNo().toUpperCase(),
												MainMenuController.jackpotForm.getSiteId());

								if (log.isInfoEnabled()) {
									log.info("Values returned after calling the getJackpotAssetInfoList web method");
									log.info("*******************************************");
								}
							} catch (JackpotEngineServiceException ex) {
								log.info("Error while calling asset to check if slot location no entered is correct");
								log.error(ex);
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(ex, LabelLoader
										.getLabelValue(MessageKeyConstants.UNABLE_TO_FETCH_ASSET_DETAILS));
								employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setFocus();
								return;
							} catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN", e2);
								return;
							}

							if (jpAssInfoDTOLst != null && jpAssInfoDTOLst.size() > 0) {
								if (log.isInfoEnabled()) {
									log.info("JackpotAssetInfoDTO values returned: "
											+ jpAssInfoDTOLst.toString());
									log.info("*******************************************");
								}

								if (jpAssInfoDTOLst.get(0).getErrorCode() == IAppConstants.ASSET_INVALID_SLOT_ERROR_CODE) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
									if (log.isInfoEnabled()) {
										log.info("Invalid slot location no");
									}
									employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
									return;
								}
								/**
								 * DO THIS CHECK IN THE PENDING JP DETAILS
								 * COMPOSITE
								 */

								/*
								 * if(!jackpotAssetInfoDTO.getAssetConfigStatus()
								 * .equalsIgnoreCase(IAppConstants.
								 * ASSET_ONLINE_STATUS) &&
								 * !jackpotAssetInfoDTO.getAssetConfigStatus
								 * ().equalsIgnoreCase
								 * (IAppConstants.ASSET_CET_STATUS)){
								 * 
								 * log.info(
								 * "Pending jackpot process is not allowed for offline slots"
								 * ); MessageDialogUtil.
								 * displayTouchScreenErrorMsgDialog
								 * (LabelLoader.getLabelValue
								 * (MessageKeyConstants.SLOT_IS_NOT_ONLINE));
								 * employeeShiftSlotDetailsComposite
								 * .getTxtSlotSeqLocationNo().setFocus();
								 * return; }
								 */
								/** THIS CHECK IS NOT REQUIRED AS PER SDS11.0 */

								/*
								 * if(!jackpotAssetInfoDTO.getSiteId().equals(
								 * MainMenuController.jackpotForm.getSiteId())){
								 * log.info(
								 * "Slot location no does not belong to this site"
								 * ); MessageDialogUtil.
								 * displayTouchScreenErrorMsgDialog
								 * (LabelLoader.getLabelValue
								 * (MessageKeyConstants
								 * .SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE
								 * )); employeeShiftSlotDetailsComposite.
								 * getTxtSlotSeqLocationNo().setFocus(); return;
								 * }
								 */
								else {
									/** MULTI AREA SUPPORT CHECK */
									if (MultiAreaSupportUtil.isMultiAreaSupportEnabled()
											&& jpAssInfoDTOLst.get(0).getAreaShortName() != null) {
										if (!MultiAreaSupportUtil.validateSlotForSelectedArea(jpAssInfoDTOLst
												.get(0).getAreaShortName())) {
											if (log.isInfoEnabled()) {
												log.info("Slot loc does not belong to this area");
											}
											MessageDialogUtil
													.displayTouchScreenErrorMsgDialog(LabelLoader
															.getLabelValue(MessageKeyConstants.SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA));
											employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo()
													.setFocus();
											return;
										}
									}

									/*
									 * List<JackpotGameDetailsDTO>
									 * gameDetailsList =
									 * jackpotAssetInfoDTO.getListGameDetails();
									 * if(gameDetailsList != null){
									 * log.info("gameDetailsList size: "
									 * +gameDetailsList.size());
									 * if(gameDetailsList.size() > 0){ for(int
									 * i=0; i< gameDetailsList.size(); i++){
									 * MainMenuController
									 * .jackpotForm.setThemeName
									 * (gameDetailsList.get(i).getThemeName());
									 * MainMenuController
									 * .jackpotForm.setDenomination
									 * (ConversionUtil
									 * .dollarToCents(gameDetailsList
									 * .get(i).getDenomAmount()));
									 * log.info("Denom: "
									 * +MainMenuController.jackpotForm
									 * .getDenomination());
									 * log.info("theme name: "
									 * +gameDetailsList.get(i).getThemeName());
									 * } } }
									 */
									MainMenuController.jackpotForm.setSlotLocationNo(form.getSlotLocationNo()
											.toUpperCase());
									List lstSlotNo = new ArrayList();
									for (int j = 0; j < jpAssInfoDTOLst.size(); j++) {
										lstSlotNo.add(jpAssInfoDTOLst.get(j).getAssetConfigNumber());
									}
									if (lstSlotNo != null && lstSlotNo.size() > 0) {
										MainMenuController.jackpotForm.setLstSlotNo(lstSlotNo);
									}
									/**
									 * SET THE SLOT NO FIELD AFTER THE USER
									 * SELECTS THE SELECTED PENDING RECORD
									 */

									// MainMenuController.jackpotForm.setSlotNo(jackpotAssetInfoDTO.getAssetConfigNumber());
									MainMenuController.jackpotForm.setArea(jpAssInfoDTOLst.get(0)
											.getAreaShortName());
								}
							} else {
								if (log.isDebugEnabled()) {
									log.debug("the asset rtnd list is null");
								}
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.INVALID_SLOT_LOCATION_NO));
								if (log.isInfoEnabled()) {
									log.info("Invalid slot location no");
								}
								employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().forceFocus();
								return;
							}
							/*
							 * }catch (JackpotEngineServiceException ex) {
							 * log.info(
							 * "Error while calling asset to check if slot location no entered is correct"
							 * ); log.error(ex); JackpotUiExceptionHandler
							 * handler = new JackpotUiExceptionHandler();
							 * handler
							 * .handleException(ex,LabelLoader.getLabelValue
							 * (MessageKeyConstants
							 * .UNABLE_TO_FETCH_ASSET_DETAILS));
							 * employeeShiftSlotDetailsComposite
							 * .getTxtSlotSeqLocationNo().setFocus(); return;
							 * }catch (Exception e2) { JackpotUiExceptionHandler
							 * handler = new JackpotUiExceptionHandler();
							 * handler
							 * .handleException(e2,MessageKeyConstants.SERVICE_DOWN
							 * ); log.error("SERVICE_DOWN",e2); return; }
							 */
						}
					}
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
						// TopMiddleController.getCurrentComposite().dispose();

						new PendingJackpotDisplayController(TopMiddleController.jackpotMiddleComposite,
								SWT.NONE, new PendingJackpotDisplayForm(), new SDSValidator(getClass(), true));
					}
				} else if (control instanceof TSButtonLabel) {
					TSButtonLabel touchScreenRadioButton = (TSButtonLabel) control;
					String radioImageName = touchScreenRadioButton.getName();
					System.out.println("Radio Image Name:" + radioImageName);
					if (radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_DAY)
							|| radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_SWING)
							|| radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
						employeeShiftSlotDetailsComposite.getShiftControl().getShiftRadioButtonComposite()
								.setSelectedButton(touchScreenRadioButton);
						MainMenuController.jackpotForm.setShift(touchScreenRadioButton.getName());
						if (log.isInfoEnabled()) {
							log.info("The selected shift is " + MainMenuController.jackpotForm.getShift());
						}
						if (radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_DAY)) {
							form.setDayYes(true);
							form.setSwingYes(false);
							form.setGraveyardYes(false);
						} else if (radioImageName.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_SWING)) {
							form.setSwingYes(true);
							form.setDayYes(false);
							form.setGraveyardYes(false);
						} else if (radioImageName
								.equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
							form.setGraveyardYes(true);
							form.setDayYes(false);
							form.setSwingYes(false);
						}
						populateForm(employeeShiftSlotDetailsComposite);
					} else {
						if (radioImageName.equalsIgnoreCase("slotradio")
								|| radioImageName.equalsIgnoreCase("standradio")) {
							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setEnabled(true);
							employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setEnabled(false);
							employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setText(
									IAppConstants.EMPTY_STRING);
							employeeShiftSlotDetailsComposite.getTxtNumOfMins().setEnabled(false);
							employeeShiftSlotDetailsComposite.getTxtNumOfMins().setText(
									IAppConstants.EMPTY_STRING);
							form.setSlotStandYes(true);
							form.setSequenceYes(false);
							form.setMinutesYes(false);
						} else if (radioImageName.equalsIgnoreCase("seqradio")) {
							employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setEnabled(true);
							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setEnabled(false);
							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setText(
									IAppConstants.EMPTY_STRING);
							employeeShiftSlotDetailsComposite.getTxtNumOfMins().setEnabled(false);
							employeeShiftSlotDetailsComposite.getTxtNumOfMins().setText(
									IAppConstants.EMPTY_STRING);
							form.setSequenceYes(true);
							form.setSlotStandYes(false);
							form.setMinutesYes(false);
						} else if (radioImageName.equalsIgnoreCase("minradio")) {
							employeeShiftSlotDetailsComposite.getTxtNumOfMins().setEnabled(true);
							employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setEnabled(false);
							employeeShiftSlotDetailsComposite.getTxtSequenceNumber().setText(
									IAppConstants.EMPTY_STRING);
							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setEnabled(false);
							employeeShiftSlotDetailsComposite.getTxtSlotSeqLocationNo().setText(
									IAppConstants.EMPTY_STRING);
							form.setSlotStandYes(false);
							form.setSequenceYes(false);
							form.setMinutesYes(true);
						}
						employeeShiftSlotDetailsComposite.getRadioButtonControl().setSelectedButton(
								touchScreenRadioButton);
						MainMenuController.jackpotForm.setMiddleControl(touchScreenRadioButton.getName());
						if (log.isInfoEnabled()) {
							log.info("The selected shift is "
									+ MainMenuController.jackpotForm.getMiddleControl());
						}
						populateForm(employeeShiftSlotDetailsComposite);
					}
				}
			} catch (Exception ex) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(ex, ex.getMessage());
				log.error("Exception: ", ex);
			}
		}

		public void mouseUp(MouseEvent e) {

		}
	}
}
