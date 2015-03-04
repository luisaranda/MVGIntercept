/*****************************************************************************
 * $Id: AmountSlotAttendantIdController.java,v 1.64.1.6.1.0.3.1, 2013-10-16 16:38:14Z, SDS12.3.3 Checkin User$
 * $Date: 10/16/2013 11:38:14 AM$
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

import com.ballydev.sds.ecash.enumconstants.AccountTransTypeEnum;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.NumericListener;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.composite.AmountSlotAttendantIdComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.AmountSlotAttendantIdForm;
import com.ballydev.sds.jackpotui.form.AuthorizationTaxMPAmountForm;
import com.ballydev.sds.jackpotui.form.PendingJackpotDetailsForm;
import com.ballydev.sds.jackpotui.form.PendingJackpotDisplayForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.PendingJackpotProcess;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;

/**
 * This Class controls the HandPaidAmountChangeComposite
 * @author anantharajr
 * @version $Revision: 75$
 */
public class AmountSlotAttendantIdController extends SDSBaseController {

	/**
	 * HandPaidAmountChangeForm instance
	 */
	public static AmountSlotAttendantIdForm form;

	/**
	 * HandPaidAmountChangeComposite instance
	 */
	private AmountSlotAttendantIdComposite amountSlotAttendantIdComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public AmountSlotAttendantIdController(Composite parent, int style,
			AmountSlotAttendantIdForm form, SDSValidator validator, boolean notFirstCall)
	throws Exception {
		super(form, validator);
		this.form = form;
		
		long expressJPlimit = 0;
		expressJPlimit = (long) Double.parseDouble(MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.EXPRESS_JACKPOT_LIMIT));
		expressJPlimit = ConversionUtil.dollarToCentsRtnLong(String.valueOf(expressJPlimit));
				
		CbctlUtil.displayMandatoryLabel(true);	
		createCompositeOnSiteConfigParam(parent, style);
		
		//DISPLAY THE MASKED AMT FOR JPS UNDER EXP LIMIT(CARDED & NON-CARDED)
		//& CARDED EXP JPS THAT EXCEEDED BLIND ATT - NJ ENHANCEMENT		
		if (MainMenuController.jackpotForm.getBlindAttempts() == IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT) {
			
			//set authorization req field to TRUE as the Express Jackpot Process was not successful
			//keep the amount as masked
			
			if(MainMenuController.jackpotForm.isDisplayMaskAmt()){
				addNumericListenerToHPJPAmtTxtField();
			} else {
				amountSlotAttendantIdComposite.getTxtHandPaidAmount().setText(
						JackpotUIUtil.getFormattedAmounts(""
								+ ConversionUtil.roundHalfUp(ConversionUtil
										.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()))));
			}
			
			MainMenuController.jackpotForm.setAuthorizationRequired(true);
			MainMenuController.jackpotForm.setExpressSuccess(false);			
			
		}else if(MainMenuController.jackpotForm.isDisplayMaskAmt()){	
			
			//COND FOR MASKED FLOW WITHIN EXP LIMIT
			addNumericListenerToHPJPAmtTxtField();
			
			// RESETTING THE DISPLAY STATIC HPJP AMT FIELD TO FALSE
			MainMenuController.jackpotForm.setDisplayMaskAmt(false);	
			
			//SET AUTH REQUIRED & EXP FALSE WHEN BLIND ATT==2 (NEEDED MAINLY FOR PREV BTN FUNC FLOW)
			if(MainMenuController.jackpotForm.getBlindAttempts() == IAppConstants.MAX_BLIND_ATTEMPTS){
				MainMenuController.jackpotForm.setAuthorizationRequired(true);
				MainMenuController.jackpotForm.setExpressSuccess(false);
			}
			
		}else if(MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED)
				.equalsIgnoreCase("Yes")
				&& MainMenuController.jackpotForm.getOriginalAmount() < expressJPlimit){
			
			//COND FOR UN-MASKED WITHIN EXP LIMIT FLOW			
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().setText(
					JackpotUIUtil.getFormattedAmounts(""
							+ ConversionUtil.roundHalfUp(ConversionUtil
									.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()))));
			
			//SET AUTH REQUIRED & EXP FALSE WHEN BLIND ATT==2 (NEEDED MAINLY FOR PREV BTN FUNC FLOW)
			if(MainMenuController.jackpotForm.getBlindAttempts() == IAppConstants.MAX_BLIND_ATTEMPTS){
				MainMenuController.jackpotForm.setAuthorizationRequired(true);
				MainMenuController.jackpotForm.setExpressSuccess(false);
			}
			
		}else {
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().setText(
					JackpotUIUtil.getFormattedAmounts(""
							+ ConversionUtil.roundHalfUp(ConversionUtil
									.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()))));
			amountSlotAttendantIdComposite
					.getSlipStaticInfoComposite()
					.getLblAdjustedAmountLbl()
					.setText(
							MainMenuController.jackpotForm.getSiteCurrencySymbol()
									+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
											.centsToDollar(MainMenuController.jackpotForm.getOriginalAmount())));
		}
		if (MainMenuController.jackpotForm != null
					&& MainMenuController.jackpotForm.getSdsCalculatedAmount() != null
					&& MainMenuController.jackpotForm.getSdsCalculatedAmount().longValue() != 0
					&& amountSlotAttendantIdComposite.getTxtCalcSDSAmount() != null) {
				amountSlotAttendantIdComposite.getTxtCalcSDSAmount().setText(
						JackpotUIUtil.getFormattedAmounts(""
								+ ConversionUtil.centsToDollar(MainMenuController.jackpotForm
										.getSdsCalculatedAmount().longValue())));
				amountSlotAttendantIdComposite.getTxtCalcSDSAmount().setEnabled(false);
		}
			
		TopMiddleController.setCurrentComposite(amountSlotAttendantIdComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
		//parent.layout();
		super.registerEvents(amountSlotAttendantIdComposite);
		form.addPropertyChangeListener(this);
		if(notFirstCall){
			populateScreen(amountSlotAttendantIdComposite);
		}
		
		amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();
		registerCustomizedListeners(amountSlotAttendantIdComposite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		AmountSlotAttendantMouseListener listener = new AmountSlotAttendantMouseListener();
		amountSlotAttendantIdComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		amountSlotAttendantIdComposite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		amountSlotAttendantIdComposite.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		amountSlotAttendantIdComposite.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		amountSlotAttendantIdComposite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		amountSlotAttendantIdComposite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
		}

	/**
	 * @return the handPaidAmountChangeComposite
	 */
	public AmountSlotAttendantIdComposite getHandPaidAmountChangeComposite() {
		return amountSlotAttendantIdComposite;
	}

	/**
	 * @param handPaidAmountChangeComposite
	 *            the handPaidAmountChangeComposite to set
	 */
	public void setHandPaidAmountChangeComposite(
			AmountSlotAttendantIdComposite handPaidAmountChangeComposite) {
		this.amountSlotAttendantIdComposite = handPaidAmountChangeComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {

		return null;
	}

	public void createCompositeOnSiteConfigParam(Composite parent, int style) {
		createHandPaidAmountChangeComposite(parent, style,
				getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(amountSlotAttendantIdComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
	}

	/**
	 * Method to create the AuthorizationComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createHandPaidAmountChangeComposite(Composite p, int s,
			boolean[] paramFlags) {
		amountSlotAttendantIdComposite = new AmountSlotAttendantIdComposite(p,
				s, paramFlags);
	}

	/**
	 * getSiteConfigurationParameters
	 * @return
	 */
	public boolean[] getSiteConfigurationParameters() {

		boolean[] paramFlags = { false, false, false, false };

		log.debug("test boolean " + paramFlags[0]);
		/*if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[0] = true;
		}*/
		
//		if (MainMenuController.jackpotForm.isPouchPay() && MainMenuController.jackpotForm.getSlotAttentantId()==null) {
//			paramFlags[1] = true;
//		}
		
		if (MainMenuController.jackpotForm.isPouchPay() && MainMenuController.jackpotForm.isShowSlotAttnId()) {
			paramFlags[1] = true;
		}
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SHOW_EXPIRY_DATE_FOR_PRINTED_SLIP).equalsIgnoreCase("Yes")) {
			paramFlags[2] = true;
		}		
		
		//FOR PROG CONTROLLER INITIATED JPS- AS ACC NO INFO IS NOT KNOWN TO THE CONTROLLER, IF REQUIRED USER HAS TO ENTER 
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("yes")
				&& MainMenuController.jackpotForm.isJpProgControllerGenerated()) {
				
			paramFlags[3] = true;
		} else {
			paramFlags[3] = false;
		}
		return paramFlags;
	}

	/**
	 * CODE FOR ROUNDING
	 */
	public void performRounding(){

		if(log.isDebugEnabled()) {
			log.debug("Jackpot Type: " + MainMenuController.jackpotForm.getJackpotTypeId());
			log.debug("ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR: "
					+ MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR));
		}

		if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
				&& (MainMenuController.jackpotForm.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
				|| MainMenuController.jackpotForm
						.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE)
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase(
						"yes")
				&& JackpotUIUtil.isRoundingAllowedForJPAmount(form.getHandPaidAmount())) {
			if(log.isInfoEnabled()) {
				log.info("Rounding is done");
			}
			MainMenuController.jackpotForm.setRoundedHPJPAmount(ConversionUtil
					.dollarToCentsRtnLong(ConversionUtil.performRounding(form.getHandPaidAmount())));
			if(log.isInfoEnabled()) {
				log.info("The rounded HPJP amount is " + MainMenuController.jackpotForm.getRoundedHPJPAmount());
			}
		} else if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
				&& (MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE 
				&& MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE)
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ROUND_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase("yes")
				&& JackpotUIUtil.isRoundingAllowedForJPAmount(form.getHandPaidAmount())
				&& JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()) != null
				&& MainMenuController.jackpotSiteConfigParams.get(
						JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()))
						.equalsIgnoreCase("yes")) {
			if(log.isInfoEnabled()) {
				log.info("Rounding is done");
			}
			MainMenuController.jackpotForm.setRoundedHPJPAmount(ConversionUtil
					.dollarToCentsRtnLong(ConversionUtil.performRounding(form.getHandPaidAmount())));
			if(log.isInfoEnabled()) {
				log.info("The rounded HPJP amount is " + MainMenuController.jackpotForm.getRoundedHPJPAmount());
			}
		}


	}

	/*
	 * Method to get the keyboard focus
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}

	/**
	 * @return the hPJPAmountJackpotTypeComposite
	 */
	public AmountSlotAttendantIdComposite getHPJPAmountJackpotTypeComposite() {
		return amountSlotAttendantIdComposite;
	}

	/**
	 * @param amountJackpotTypeComposite
	 *            the hPJPAmountJackpotTypeComposite to set
	 */
	public void setHPJPAmountJackpotTypeComposite(
			AmountSlotAttendantIdComposite amountJackpotTypeComposite) {
		amountSlotAttendantIdComposite = amountJackpotTypeComposite;
	}

	/**
	 * processes non express jackpot
	 */
	private void processNotAsExpress(String oldFormHandPaidAmtVal) {
		try {
			//FIX FOR CR 99486 - EXTRA AUTHORIZER IS NEEDED IF THE JP AMT EXCEEDS THE EMP JOB JP LIMIT
			String empJobLimitNVarianceAuthMsg = null;
						
			if(MainMenuController.jackpotForm.isPouchPay() && MainMenuController.jackpotForm.getSlotAttentantId()==null){
				try {
					MainMenuController.jackpotForm.setInsertPouchPayAttn(true);
					form.getSlotAttendantId();
					SessionUtility sessionUtility = new SessionUtility();
					ProgressIndicatorUtil.openInProgressWindow();
					UserDTO userDTOEmpIdCheck = sessionUtility.getUserDetails(form.getSlotAttendantId(), MainMenuController.jackpotForm.getSiteId());
					if(userDTOEmpIdCheck != null) {		
						if (userDTOEmpIdCheck.isErrorPresent() && userDTOEmpIdCheck.getMessageKey() != null
								&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDTOEmpIdCheck.getMessageKey())||
										IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDTOEmpIdCheck.getMessageKey())||
										IAppConstants.ANA_MESSAGES_USER_NOT_PRESENT.equals(userDTOEmpIdCheck.getMessageKey()))) {
							log.info("User name1 : " + userDTOEmpIdCheck.getUserName());
							ProgressIndicatorUtil.closeInProgressWindow();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
							amountSlotAttendantIdComposite.getTxtSlotAttendantId().forceFocus();
							return;
						}else {	
							MainMenuController.jackpotForm.setInsertPouchPayAttn(true);
							MainMenuController.jackpotForm.setSlotAttentantId(form.getSlotAttendantId());
							MainMenuController.jackpotForm.setSlotAttentantFirstName(userDTOEmpIdCheck.getFirstName());
							MainMenuController.jackpotForm.setSlotAttentantLastName(userDTOEmpIdCheck.getLastName());
						}
					}
					else{
						ProgressIndicatorUtil.closeInProgressWindow();
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
						amountSlotAttendantIdComposite.getTxtSlotAttendantId().forceFocus();
						return;
					}
				}catch (Exception e) {
					JackpotExceptionUtil.getGeneralCtrllerException	(e);
					log.error("Exception while checking the slot attnd id with framework", e);
					return;
				}finally{
					ProgressIndicatorUtil.closeInProgressWindow();
				}
			}
			
			double hpjpAmt = Double.parseDouble(ConversionUtil.roundHalfUp(form.getHandPaidAmount()));
						
			try {
				// CHECKING FOR JACKPOT AMOUNT LIMIT FOR AUTHORIZATION TODO
				if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.JACKPOT_AMOUNT_OVERIDE_AUTH_ENABLED).equalsIgnoreCase("yes")) {
					double hpjpAuthAmount = (double)Double.parseDouble(
							MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JACKPOT_AMOUNT_OVERIDE_AUTH_LIMIT));
					
					if (hpjpAmt > hpjpAuthAmount && form.getAmountAuthEmpId() != null && form.getAmountAuthEmpId().length() == 0) {
						MessageDialogUtil.displayTouchScreenInfoDialog(
								LabelLoader.getLabelValue(MessageKeyConstants.HPJP_EXCEED_OVERIDE_AUTH_LIMIT));
						if(log.isInfoEnabled()) {
							log.info(MessageKeyConstants.HPJP_EXCEED_OVERIDE_AUTH_LIMIT);
						}
						amountSlotAttendantIdComposite.getTxtAmountAuthEmpId().forceFocus();
						return;
					} 
					
					if (hpjpAmt > hpjpAuthAmount) {
						//OVERRIDE EMPLOYEE ID VALIDATION
						try {
							SessionUtility sessionUtility = new SessionUtility();
							ProgressIndicatorUtil.openInProgressWindow();
							if(log.isInfoEnabled()) {
								log.info("Called framework's authenticate user method");
							}		
							UserDTO userDtoEmpIdChk = sessionUtility.authenticateUser(form.getAmountAuthEmpId(), 
									form.getAmountAuthPassword(), MainMenuController.jackpotForm.getSiteId());
							if(userDtoEmpIdChk != null) {
								if (userDtoEmpIdChk.isErrorPresent() && userDtoEmpIdChk.getMessageKey() != null
										&& (IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpIdChk.getMessageKey())
												|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpIdChk.getMessageKey()))) {
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									amountSlotAttendantIdComposite.getTxtAmountAuthEmpId().forceFocus();
									return;
								} else if(amountSlotAttendantIdComposite.getTxtAmountAuthPassword().getText() != null && 
										amountSlotAttendantIdComposite.getTxtAmountAuthPassword().getText().length() == 0) {
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_VALIDATION_OVERIDE_EMP_PASSWORD) 
											+ " " + LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().forceFocus();
									return;
								} else if (userDtoEmpIdChk.isErrorPresent() && userDtoEmpIdChk.getMessageKey() != null
										&& IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpIdChk.getMessageKey())) {	
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().forceFocus();
									return;
								}  else if (MainMenuController.jackpotForm.getEmployeeIdPrintedSlip().equalsIgnoreCase(
												form.getAmountAuthEmpId())) {
									//PRINT EMP AND OVERRIDE EMPLOYEE SHOULD NOT BE THE SAME
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.ALREADY_USED_FOR_THIS_TRANSACTION));
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
									amountSlotAttendantIdComposite.getTxtAmountAuthEmpId().forceFocus();
									return;
								} else if(MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ENABLE_OVERRIDE_AUTH_LEVELS).equalsIgnoreCase("yes")
										&& UserFunctionsUtil.getOverrideAuthLevel(form.getAmountAuthEmpId(), 
											MainMenuController.jackpotForm.getSiteId()) <= UserFunctionsUtil.getOverrideAuthLevel(
													MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), 
													MainMenuController.jackpotForm.getSiteId())) {
										//OVERRIDE EMP AUTH LEVEL SHD BE GREATER THAN PRINT EMP	
										ProgressIndicatorUtil.closeInProgressWindow();
										if(log.isInfoEnabled()) {
											log.info("Auth level of Override Emp: " + 
													UserFunctionsUtil.getOverrideAuthLevel(form.getAmountAuthEmpId(), 
															MainMenuController.jackpotForm.getSiteId()));
											log.info("Auth level of Print Emp: " + 
													UserFunctionsUtil.getOverrideAuthLevel(
															MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), 
															MainMenuController.jackpotForm.getSiteId()));
										}
										
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
										log.info("LOW_OVERRIDE_AUTH_LEVEL");
										amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
										amountSlotAttendantIdComposite.getTxtAmountAuthEmpId().forceFocus();
										return;
								}//OVERRIDE EMP SHD HAVE THE JP DOLLAR LIMIT IN HIS JOB DESC
								else if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT)
										.equalsIgnoreCase("yes") 
										&& hpjpAmt > UserFunctionsUtil.getJackpotAmount(form.getAmountAuthEmpId(), MainMenuController.jackpotForm.getSiteId())) {
										
									if(log.isInfoEnabled()) {
										log.info("The JACKPOT AMOUNT is"+UserFunctionsUtil.getJackpotAmount(form.getAmountAuthEmpId()
												, MainMenuController.jackpotForm.getSiteId()));
									}
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
									amountSlotAttendantIdComposite.getTxtAmountAuthEmpId().forceFocus();
									log.info("Hp Amt exceeds jackpot limit for Override Emp");
									return;													
								}
								//OVERRIDE EMP'S - SUPERVISORY AUTH JOB FUNC CHK 
								else if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.REQUIRE_SUPERVISORY_AUTHORITY)
										.equalsIgnoreCase("yes") 
										&& UserFunctionsUtil.isSupervisoryAuthority(form.getAmountAuthEmpId(),
												MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")) {
									ProgressIndicatorUtil.closeInProgressWindow();
									if(log.isDebugEnabled()) {
										log.debug("SUPERVISORY_AUTHORITY_REQUIRED");
									}
									MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
									amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
									amountSlotAttendantIdComposite.getTxtAmountAuthEmpId().forceFocus();
									if(log.isInfoEnabled()) {
										log.info("Hp Amt exceeds jackpot limit for Override Emp");
									}
									return;
								} else {
									MainMenuController.jackpotForm.setAmountAuthEmpId(form.getAmountAuthEmpId());
								}
								
							}
							else {
								ProgressIndicatorUtil.closeInProgressWindow();
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(
										LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
								amountSlotAttendantIdComposite.getTxtAmountAuthPassword().setText("");
								amountSlotAttendantIdComposite.getTxtAmountAuthPassword().forceFocus();
								return;
							}						
						} catch (Exception e1) {
							JackpotExceptionUtil.getGeneralCtrllerException(e1);
							log.error(e1);
							return;
						} finally {
							ProgressIndicatorUtil.closeInProgressWindow();
						}
					} else {
						// DO NOT SET THE OVERIDE EMPLOYEE DETAILS TO THE JACKPOT FORM FOR INSERTING INTO THE SLIP_DATA TABLE
						// AND ALSO DO NOT VALIDATE THE OVERIDE EMPLOYEE ID AND PASSWORD
						form.setAmountAuthEmpId(null);
						form.setAmountAuthPassword(null);
						MainMenuController.jackpotForm.setAmountAuthEmpId(null);
					}
				} else {
					form.setAmountAuthEmpId(null);
					form.setAmountAuthPassword(null);
					MainMenuController.jackpotForm.setAmountAuthEmpId(null);
				}
			} catch(Exception e) {
				JackpotExceptionUtil.getGeneralCtrllerException(e);
				log.error(e);
				return;
			}
			
			// HPJP AMT CHK WITH THE EMPLOYEE JOB JP AMOUNT BASED ON THE SITE PARAMETER -
			// HPJP REQUIRES VALID HPJP AMOUNT - YES
			if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {

				double jackpotAmount =	UserFunctionsUtil.getJackpotAmount(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), MainMenuController.jackpotForm.getSiteId());
				log.info("JP_REQUIRES_VALID_EMP_ID_HP_JP_AUTH_AMOUNT  --------> Yes");

				log.info("The JACKPOT AMOUNT is : "+jackpotAmount);
				if (hpjpAmt > jackpotAmount
						&& new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES))>=1) {
					
					empJobLimitNVarianceAuthMsg = LabelLoader.getLabelValue(MessageKeyConstants.HPJP_EXCEED_OVERIDE_AUTH_LIMIT_REQ_AUTH);
					amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();
					log.info("Hp Amt exceeds jackpot limit, ask for authorization");
					//FIX FOR CR 99486 - EXTRA AUTHORIZER IS NEEDED IF THE JP AMT EXCEEDS THE EMP JOB JP LIMIT 
					MainMenuController.jackpotForm.setAuthorizationRequired(true);
					MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(true);					
				}else if (hpjpAmt > jackpotAmount){
					MessageDialogUtil
					.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
					amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();
					MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);
					log.info("Hp Amt exceeds jackpot limit");
					return;
				}
				else{
					
					//SET THE AUTH REQ FIELD TO FALSE FOR A NON EXP LIMIT JP THAT IS NOT CARDED
					//ALSO APPLICABLE FOR EXP CONVERTED JPS WITH BLIND ATT != 2/-1
					long expressJPlimit = 0;
					expressJPlimit = (long) Double.parseDouble(MainMenuController.jackpotSiteConfigParams
							.get(ISiteConfigConstants.EXPRESS_JACKPOT_LIMIT));
					expressJPlimit = ConversionUtil.dollarToCentsRtnLong(String.valueOf(expressJPlimit));
					
					long orgJPAmt = MainMenuController.jackpotForm.getOriginalAmount();
					if(!((MainMenuController.jackpotForm.getBlindAttempts()== IAppConstants.MAX_BLIND_ATTEMPTS
							|| MainMenuController.jackpotForm.getBlindAttempts()== IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT)
							|| ((MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("Yes")
									&& (orgJPAmt < expressJPlimit) 
									&& (MainMenuController.jackpotForm.getSlotAttentantName()==null
											|| MainMenuController.jackpotForm.getSlotAttentantName().trim().length() == 0))))){
						MainMenuController.jackpotForm.setAuthorizationRequired(false);
					}
					MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);									
				}		
			}
						
			if(log.isDebugEnabled()) {
				log.debug("form.getHandPaidAmount(): " + form.getHandPaidAmount());
			}
			//String denomInDollar=ConversionUtil.twoPlaceDecimalOf(Double.parseDouble(ConversionUtil.centsToDollar(denomInCents)));

			/** CODE FOR ROUNDING */
			if (log.isDebugEnabled()) {
				log.debug("Jackpot Type: " + MainMenuController.jackpotForm.getJackpotTypeId());
				log.debug("ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR: "
						+ MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR));
			}
			MainMenuController.jackpotForm.setRoundedHPJPAmount(0); // FIX for CR 94609
			if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
					&& (MainMenuController.jackpotForm.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE || MainMenuController.jackpotForm
							.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE)
					&& MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase(
							"yes")
					&& JackpotUIUtil.isRoundingAllowedForJPAmount(form.getHandPaidAmount())) {
				if(log.isInfoEnabled()) {
					log.info("Rounding is done");
				}
				MainMenuController.jackpotForm.setRoundedHPJPAmount(ConversionUtil
						.dollarToCentsRtnLong(ConversionUtil.performRounding(form.getHandPaidAmount())));
				if(log.isInfoEnabled()) {
					log.info("The rounded HPJP amount is "
							+ MainMenuController.jackpotForm.getRoundedHPJPAmount());
				}						
			} else if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
					&& (MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE && MainMenuController.jackpotForm
							.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_lINKED_PROGRESSIVE)
					&& MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.ROUND_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase("yes")
					&& JackpotUIUtil.isRoundingAllowedForJPAmount(form.getHandPaidAmount())
					&& JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()) != null
					&& MainMenuController.jackpotSiteConfigParams.get(
							JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()))
							.equalsIgnoreCase("yes")) {

				if (log.isInfoEnabled()) {
					log.info("Rounding is done");
				}
				MainMenuController.jackpotForm.setRoundedHPJPAmount(ConversionUtil
						.dollarToCentsRtnLong(ConversionUtil.performRounding(form.getHandPaidAmount())));
				if (log.isInfoEnabled()) {
					log.info("The rounded HPJP amount is "
							+ MainMenuController.jackpotForm.getRoundedHPJPAmount());
				}
			}

			MainMenuController.jackpotForm.setHandPaidAmount(ConversionUtil.dollarToCentsRtnLong(ConversionUtil.roundHalfUp(form.getHandPaidAmount())));

			if(log.isDebugEnabled()) {
				log.debug("The HPJP amount is " + MainMenuController.jackpotForm.getHandPaidAmount());
			}

			double hpjpAmount = ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount());
			double originalAmount = ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getOriginalAmount());
			
			// Setting Overide Authorization employee Id value
			MainMenuController.jackpotForm.setAmountAuthEmpId(form.getAmountAuthEmpId());

			double variance = 0;
			
			//Rounding for very large values when using a double value eg. for a value like 1.940000098 it would give 1.94
			variance = ConversionUtil.roundHalfUp((originalAmount - hpjpAmount),2);
			
			if (variance < 0)
				variance = (variance * -1);

			if(log.isInfoEnabled()) {
				log.info("Hpjp amount is " + hpjpAmount);
				log.info("Original amount is " + originalAmount);
				log.info("Variance is " + variance);
			}

			double originalAmtPercentVar =0;
			double finalPercentVar = 0;
			if (originalAmount != 0) {
				originalAmtPercentVar = ConversionUtil.roundHalfUp(((hpjpAmount * 100) / originalAmount), 2);
				if (log.isInfoEnabled()) {
					log.info("Org Amt Percentage var: " + originalAmtPercentVar);
				}

				if (originalAmtPercentVar > 100) {
					finalPercentVar = ConversionUtil.roundHalfUp(originalAmtPercentVar - 100);
				} else {
					finalPercentVar = ConversionUtil.roundHalfUp(100 - originalAmtPercentVar);
				}
			}

			if(log.isInfoEnabled()) {
				log.info("Final percentage Variance is " + finalPercentVar);
			}		

			long siteSlipAmtVar = new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PERCENT_SLIP_AMT_VARIANCE_QUERY_USER));
			if (log.isInfoEnabled()) {
				log.info("PERCENT_SLIP_AMT_VARIANCE_QUERY_USER: " + siteSlipAmtVar);
				log.info("Original Amt: " + originalAmount);
			}
			double queryVar = 0;

            if(originalAmount != 0){
                  queryVar = ((variance * 100) / originalAmount);
            }
            
			if (log.isInfoEnabled()) {
				log.info("Variance Diff: " + variance);
				log.info("Query Var: " + queryVar);
			}

			if (new Long(MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PERCENT_SLIP_AMT_VARIANCE_QUERY_USER)) != 0) {
				boolean userResp = false;
				if (originalAmount != hpjpAmount && queryVar != 0.0 && queryVar >= siteSlipAmtVar) {
					userResp = MessageDialogUtil.displayTouchScreenQuestionDialog(
									LabelLoader.getLabelValue(MessageKeyConstants.ORIGINAL_AMOUNT)
											+ " "
											+ MainMenuController.jackpotForm.getSiteCurrencySymbol()
											+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(
													String.valueOf(MainMenuController.jackpotForm.getOriginalAmount())))
											+ " "
											+ LabelLoader.getLabelValue(MessageKeyConstants.ADJUSTED_AMOUNT)
											+ " "
											+ MainMenuController.jackpotForm.getSiteCurrencySymbol()
											+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(
													String.valueOf(MainMenuController.jackpotForm.getHandPaidAmount())))
											+ ". "
											+ LabelLoader.getLabelValue(MessageKeyConstants.THIS_SLIP_IS)
											+ " "
											+ +siteSlipAmtVar
											+ " "
											+ LabelLoader.getLabelValue(MessageKeyConstants.PERCENT_GREATER_ORG_AMT_IS_AMT_CORRECT),
									amountSlotAttendantIdComposite);
					if (!userResp) {
						amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();
						return;
					}
				}
			}

			double siteJPDollarVarValue = new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS));
			if (log.isInfoEnabled()) {
				log.info("siteJPDollarVarValue: " + siteJPDollarVarValue);
			}
			
			long siteJPPercentVarValue = new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS));
			if (log.isInfoEnabled()) {
				log.info("siteJPPercentVarValue: " + siteJPPercentVarValue);
			}
						
			if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) != 0
					&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PENDING_NON_EXP_JPS_ALWAYS_REQUIRE_EXTRA_AUTHORIZERS).equalsIgnoreCase("yes")) {
				
				if(log.isInfoEnabled()) {
					log.info("PENDING_NON_EXP_JPS_ALWAYS_REQUIRE_EXTRA_AUTHORIZERS--------Yes");
				}

				MainMenuController.jackpotForm.setAuthorizationRequired(true);

				//USED VARIABLE TO ENABLE SENDING ONLY A SINGLE COMBINED MSG FOR EMP JOB JP LIMIT EXCEED AND VAR AUTH
				if(empJobLimitNVarianceAuthMsg!=null){
					MessageDialogUtil.displayTouchScreenInfoDialog(empJobLimitNVarianceAuthMsg);
				}
				
				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}
				//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
				setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
				
				new AuthorizationTaxMPAmountController(
						TopMiddleController.jackpotMiddleComposite, SWT.NONE,
						new AuthorizationTaxMPAmountForm(), new SDSValidator(
								getClass(), true));			
				
			} else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) != 0					
					&& siteJPDollarVarValue!=0					
					&& variance >= siteJPDollarVarValue) {

				if(log.isInfoEnabled()) {
					log.info("JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS--------Yes");
				}

				MainMenuController.jackpotForm.setAuthorizationRequired(true);
			
				//USED VARIABLE TO ENABLE SENDING ONLY A SINGLE COMBINED MSG FOR EMP JOB JP LIMIT EXCEED AND VAR AUTH
				if(empJobLimitNVarianceAuthMsg!=null){
					//SEND COMBINED MSG
					MessageDialogUtil
					.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.EMP_JOB_LIMIT_N_DOLLAR_VARIANCE_EXCEEDED_AUTH));
				}else{
					MessageDialogUtil
					.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.ADJ_AMT_GRT_VAR_LIMIT_REQ_AUTH));
				}
				
				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}
				//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
				setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
				
				new AuthorizationTaxMPAmountController(
						TopMiddleController.jackpotMiddleComposite, SWT.NONE,
						new AuthorizationTaxMPAmountForm(), new SDSValidator(
								getClass(), true));

			} else if (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) != 0
					&& siteJPPercentVarValue != 0					
					&& finalPercentVar >= siteJPPercentVarValue) {
				
				if(log.isInfoEnabled()) {
					log.info("JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS--------Yes");
				}
				//USED VARIABLE TO ENABLE SENDING ONLY A SINGLE COMBINED MSG FOR EMP JOB JP LIMIT EXCEED AND VAR AUTH
				if(empJobLimitNVarianceAuthMsg!=null){
					//SEND COMBINED MSG
					MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
							.getLabelValue(MessageKeyConstants.EMP_JOB_LIMIT_N_PERC_VARIANCE_EXCEEDED_AUTH)
							+ " " + JackpotUIUtil.getFormattedAmounts(finalPercentVar)
							+ " " + LabelLoader.getLabelValue(MessageKeyConstants.PERCENT_OF_ORIG_AMOUNT));
					
				}else{
					MessageDialogUtil
					.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.ADJUSTED_JACKPOT_AMOUNT_VARIES)
							+ " " + JackpotUIUtil.getFormattedAmounts(finalPercentVar)
							+ " " + LabelLoader.getLabelValue(MessageKeyConstants.PERCENT_OF_ORIG_AMOUNT));
				}			
				
				MainMenuController.jackpotForm.setAuthorizationRequired(true);
				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}
				//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
				setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
				
				new AuthorizationTaxMPAmountController(
						TopMiddleController.jackpotMiddleComposite, SWT.NONE,
						new AuthorizationTaxMPAmountForm(), new SDSValidator(
								getClass(),true));

			} else if ((MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes")
							&& (new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0
									|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) !=0
									|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) !=0))
							|| (MainMenuController.jackpotForm.getAuthorizationRequired())){
				
				if(log.isInfoEnabled()) {
					log.info("Inside hpamount controller inside tax withholding amount");
				}
				//PROMPT FOR AUTH REQ MSG WHEN JP AMT EXCEEDS EMP JOB JP LIMIT
				if(empJobLimitNVarianceAuthMsg!=null){
					MessageDialogUtil.displayTouchScreenInfoDialog(empJobLimitNVarianceAuthMsg);
				}
				
				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}
				
				//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
				setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
				
				new AuthorizationTaxMPAmountController(
						TopMiddleController.jackpotMiddleComposite, SWT.NONE,
						new AuthorizationTaxMPAmountForm(), new SDSValidator(
								getClass(),true));				
			} else if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")
					|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")
					|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")
					|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase("yes")
					|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase("yes")
					|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
				log.debug("inside winning combo screeen Yes");

				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}
				//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
				setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
				
				new PendingJackpotDetailsController(
						TopMiddleController.jackpotMiddleComposite, SWT.NONE,
						new PendingJackpotDetailsForm(), new SDSValidator(
								getClass(), true));
			} else {				
				/** SETTING THE JACKPTO NET AMOUNT */				
				if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
					MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm
							.getRoundedHPJPAmount());
				} else if (MainMenuController.jackpotForm.getHandPaidAmount() != 0) {
					MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm
							.getHandPaidAmount());
				}
							
				if(log.isInfoEnabled()) {
					log.info("--------------Update pending process------------------");
				}
				/** To update the DB with the Pending Jackpot Details */
				PendingJackpotProcess pendingProcess = new PendingJackpotProcess();
				pendingProcess.processPending();
				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();					
				}
				CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
				initialScreen.callPendingJPFirstScreen();	
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@Override
	public void keyTraversed(TraverseEvent e) {
		super.keyTraversed(e);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double hpjpAmount = 12;
		double originalAmount = 12.50;
		double variance = 0;

		variance = ConversionUtil.roundHalfUp((originalAmount - hpjpAmount), 2);

		if (variance < 0)
			variance = (variance * -1);

		System.out.println("variance: " + variance);

		System.out.println("Hpjp amount is " + hpjpAmount);
		System.out.println("Original amount is " + originalAmount);

		double finalPercentVar = 0;

		double originalAmtPercentVar = 0;
		originalAmtPercentVar = ConversionUtil.roundHalfUp(((hpjpAmount * 100) / originalAmount), 2);

		System.out.println("Org Amt Percentage var: " + originalAmtPercentVar);
		if (originalAmtPercentVar > 100) {
			finalPercentVar = ConversionUtil.roundHalfUp(originalAmtPercentVar - 100);
		} else {
			finalPercentVar = ConversionUtil.roundHalfUp(100 - originalAmtPercentVar);
		}

		System.out.println("Final percentage Variance is " + finalPercentVar);

	}
	
	private class AmountSlotAttendantMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			
		}

		public void mouseDown(MouseEvent e) {
			
			try {
				populateForm(amountSlotAttendantIdComposite);
				
				String unformatAmt = CurrencyUtil.getStrUnformattedAmt(amountSlotAttendantIdComposite.getTxtHandPaidAmount().getText());
				if(unformatAmt!=null){
					form.setHandPaidAmount(unformatAmt);
				}else{
					form.setHandPaidAmount(amountSlotAttendantIdComposite.getTxtHandPaidAmount().getText());
				}
								
				Control control = (Control) e.getSource();

				if (!(control instanceof SDSImageLabel)) {
					return;
				}

				if (control instanceof SDSImageLabel) {
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {
						log.info("inside next button of HandPaidAmountChangeController");
						
						String oldFormHandPaidAmtVal = form.getHandPaidAmount();
												
						List<String> fieldNames = new ArrayList<String>();
						
						long expressJPlimit = 0;
						expressJPlimit = (long) Double
								.parseDouble(MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.EXPRESS_JACKPOT_LIMIT));
						expressJPlimit = ConversionUtil.dollarToCentsRtnLong(String
								.valueOf(expressJPlimit));
						
						
						if((MainMenuController.jackpotForm.getOriginalAmount() < expressJPlimit
								&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("Yes")
								&& MainMenuController.jackpotForm.getSlotAttentantId() !=null && MainMenuController.jackpotForm.getSlotAttentantId().length()>0
								&&  (MainMenuController.jackpotForm.getBlindAttempts()==0 || MainMenuController.jackpotForm.getBlindAttempts()==1) )
								//VALIDATION FOR NON EXP CARDED/NON CARDED JPS
							|| (MainMenuController.jackpotForm.getOriginalAmount() >= expressJPlimit
										&& MainMenuController.jackpotForm.getBlindAttempts()==0)){
							
							fieldNames.add(FormConstants.FORM_HAND_PAID_AMOUNT);
							
						}else{
							//COND WHERE STARS CAN BE AVAILABLE
							//IF THE USER HAS ENTERED A VALUE, THEN VALIDATE HPJP AMT FIELD
							if(!JackpotUIUtil.validateForMaskedAmount(form.getHandPaidAmount())){
								fieldNames.add(FormConstants.FORM_HAND_PAID_AMOUNT);								
							}						
						}						
						
						fieldNames.add(FormConstants.FORM_SLOT_ATTN_ID);
					
						if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.JACKPOT_AMOUNT_OVERIDE_AUTH_ENABLED).equalsIgnoreCase("yes")) {
							fieldNames.add(FormConstants.FORM_AMOUNT_AUTH_EMP_ID);
							fieldNames.add(FormConstants.FORM_AMOUNT_AUTH_PASSWORD);
						}
						
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SHOW_EXPIRY_DATE_FOR_PRINTED_SLIP).equalsIgnoreCase("Yes")) {
							fieldNames.add(FormConstants.FORM_CHECK_EXPIRY_DATE);
						}
					
						if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("yes")
										&& MainMenuController.jackpotForm.isJpProgControllerGenerated()){
							fieldNames.add(FormConstants.FORM_ACCOUNT_NUMBER);
							fieldNames.add(FormConstants.FORM_CONFIRM_ACCOUNT_NUMBER);
						}
						
						/**
						 * Validation of the PendingJackpotForm done field by field.
						 */
						boolean validate = validate("AmountSlotAttendantIdForm", fieldNames, form,
								amountSlotAttendantIdComposite);	

						if (!validate) {
							log.debug("Validation has failed");
							return;
						}					
						
						// VALIDATING CASHLESS ACCOUNT NUMBER WITH ECASH ACCOUNT DETAILS
						//FOR PROG CONTROLLER INITIATED JPS- AS ACC NO INFO IS NOT KNOWN TO THE CONTROLLER, IF REQUIRED USER HAS TO ENTER
						if (form.getAccountNumber() != null
								&& form.getAccountNumber().trim().length()>0) {
							//VALIDATE IF THE ACC NO IS ZERO, IF YES DISPLAY A MSG PROMPT
							if(Long.parseLong(form.getAccountNumber()) == 0) {
								log.info("ACCOUNT_NO_IS_ZERO");		
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(
										LabelLoader.getLabelValue(ILabelConstants.ACCOUNT_NO_INVALID));
								amountSlotAttendantIdComposite.getTxtAccountNumber().forceFocus();
								return;
							}
							
							
							
							CashlessAccountDTO cashlessAccountDTO = JackpotServiceLocator.getService()
									.isJackpotOperationAllowed(form.getAccountNumber(),
											MainMenuController.jackpotForm.getSiteId(),
											AccountTransTypeEnum.JACKPOT.getTransId());
							if (!cashlessAccountDTO.isSuccess()) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.JKPT_INVALID_ACCOUNT_NUMBER));
								amountSlotAttendantIdComposite.getTxtAccountNumber().setFocus();
								return;
							} else if (cashlessAccountDTO.isSuccess()) {
								if (log.isDebugEnabled()) {
									log.debug("Account " + form.getAccountNumber() + " is active");
								}
								// Account number is VALID and hence set to JACKPOT FORM
								MainMenuController.jackpotForm.setAccountNumber(form
										.getAccountNumber());
								MainMenuController.jackpotForm.setAccountType(cashlessAccountDTO
										.getAcntAccessType());
							}
						}
						
						//IF FORM CONTAINS MASKED AMT,THEN SET FORM WITH ORG AMT AS THIS WOULD BE USED FOR FURTHER CALCULATIONS
						if(form.getHandPaidAmount().contains(IAppConstants.MASKED_AMOUNT_STRING)){
							
							form.setHandPaidAmount(ConversionUtil.roundHalfUp(ConversionUtil.centsToDollar(String.valueOf(MainMenuController.jackpotForm.getOriginalAmount()))));													
						}
												
						if(form.getExpiryDate() != null) {
							MainMenuController.jackpotForm.setExpiryDate(form.getExpiryDate());
						}
						if(!ConversionUtil.isFractionsOfCentsNormal(form.getHandPaidAmount())){
							log.info("Fractions of a cent is not allowed");
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.FRACTIONS_OF_CENT_NOT_ALLOWED));
							amountSlotAttendantIdComposite.getTxtHandPaidAmount().setFocus();
							return;						
						}
						
						//MACHINE PAID AMOUNT JPS- NOT SUPPORTED
						/*if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS).equalsIgnoreCase("yes")){
							if(!ConversionUtil.isFractionsOfCentsNormal(form.getMachinePaidAmount())){
								log.info("Fractions of a cent is not allowed");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.FRACTIONS_OF_CENT_NOT_ALLOWED));
								amountSlotAttendantIdComposite.getTxtMachinePaidAmount().setFocus();
								return;						
							}						
							MainMenuController.jackpotForm
							.setMachinePaidAmount(ConversionUtil
									.dollarToCents(ConversionUtil.roundHalfUp(Double
											.parseDouble(form
													.getMachinePaidAmount()))));
							log.info("The machine paid amount is "+MainMenuController.jackpotForm.getMachinePaidAmount());
						}*/

						if (MainMenuController.jackpotForm.isProcessAsExpress()) {
							log.info("isProcessAsExpress is true");

							String hpjpAmtStr = CurrencyUtil.getStrUnformattedAmt(amountSlotAttendantIdComposite
							.getTxtHandPaidAmount().getText());
							String s1 = ConversionUtil.dollarToCents(hpjpAmtStr);
							String s2 = ((Long) MainMenuController.jackpotForm
									.getHandPaidAmount()).toString();

							log.debug("Testing s1 " + s1 + "\nTesting s2 " + s2);
							long sequenceNum = -1;
							try {
								sequenceNum = MainMenuController.jackpotForm
								.getSequenceNumber();
							} catch (RuntimeException e2) {
								e2.printStackTrace();
							}
							short blindAttempt = 0;
							try {
								log.debug("Calling the getBlindAttemptsCount web method");
								log.debug("*******************************************");
								log.debug("Sequence no: "+sequenceNum);				
								log.debug("*******************************************");						

								blindAttempt = JackpotServiceLocator.getService().getExpressJackpotBlindAttempts(sequenceNum, MainMenuController.jackpotForm.getSiteId());
								log.debug("Values returned after calling the getBlindAttemptsCount web method");
								log.debug("*******************************************");
								log.debug("Blind Attempt: "+blindAttempt);								
								log.debug("*******************************************");
							} catch (JackpotEngineServiceException e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,"Exception in getBlindAttemptsCount");
								log.error("Exception in getBlindAttemptsCount",e2);
								return;
							}catch (Exception e2) {
								JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
								handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
								log.error("SERVICE_DOWN",e2);
								return;
							}
							log.debug("The blind attempt count is " + blindAttempt);						
							
							if (ConversionUtil.dollarToCents(hpjpAmtStr).equals(
									((Long) MainMenuController.jackpotForm
											.getHandPaidAmount()).toString())) {
								
								
								//FIX FOR CR 99325
								double hpjpAmount = new Double(hpjpAmtStr).doubleValue();
								log.info("HP JP Amt entered in UI: "+hpjpAmount);
								
								if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {

									double jackpotAmount =	UserFunctionsUtil.getJackpotAmount(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), MainMenuController.jackpotForm.getSiteId());
									log.info("JP_REQUIRES_VALID_EMP_ID_HP_JP_AUTH_AMOUNT  --------> Yes");

									log.info("The EMP JACKPOT AMOUNT is : "+jackpotAmount);
									//FOR PENDING JPS ONLY, IF THE EMP JP LIMIT IS EXCEEDED
									//PROMPT A MSG TO THE USER AND ASK FOR EXTRA AUTHORIZER ON NEXT SCREEN
									if (hpjpAmount > jackpotAmount
											&& new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES))>=1) {
										
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.HPJP_EXCEED_OVERIDE_AUTH_LIMIT_REQ_AUTH));
										amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();
										MainMenuController.jackpotForm.setAuthorizationRequired(true);									
										MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(true);
										log.info("Hp Amt exceeds jackpot limit,Requires authorization");
										
									}else if (hpjpAmount > jackpotAmount) {
										MessageDialogUtil
										.displayTouchScreenInfoDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
										amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();
										MainMenuController.jackpotForm.setAuthorizationRequired(false);	
										MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);
										log.info("Hp Amt exceeds jackpot limit");
										return;
										
									}else{
										MainMenuController.jackpotForm.setAuthorizationRequired(false);	
										MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);
									}
								}
								
								MainMenuController.jackpotForm.setProcessFlagId(ILookupTableConstants.EXPRESS_PROCESS_FLAG);
								MainMenuController.jackpotForm
								.setHandPaidAmount(ConversionUtil
										.dollarToCentsRtnLong(ConversionUtil.roundHalfUp(form
														.getHandPaidAmount())));

								/** PERFORM ROUNDING */
								performRounding();

								/*amountSlotAttendantIdComposite
										.getSlipStaticInfoComposite()
										.getLblAdjustedAmountLbl()
										.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
														+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
																.centsToDollar(MainMenuController.jackpotForm
																		.getOriginalAmount())));*/

								/** SETTING THE JACKPOT NET AMOUNT */					
								
								if(MainMenuController.jackpotForm.getRoundedHPJPAmount()!=0){
									MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm.getRoundedHPJPAmount());
								}else if(MainMenuController.jackpotForm.getHandPaidAmount()!=0){
									MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm.getHandPaidAmount());
								}
								
								if ((MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes")
											&& (new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0
												|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) !=0
												|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) !=0))
										|| MainMenuController.jackpotForm.getAuthorizationRequired()){

									if (TopMiddleController.getCurrentComposite() != null
											&& !(TopMiddleController
													.getCurrentComposite().isDisposed())) {
										TopMiddleController.getCurrentComposite()
										.dispose();
									}
									//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
									setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
									
									//MainMenuController.jackpotForm.setShowJPAmountAuth(false);
									new AuthorizationTaxMPAmountController(
											TopMiddleController.jackpotMiddleComposite,
											SWT.NONE,
											new AuthorizationTaxMPAmountForm(),
											new SDSValidator(getClass(),true));

									log.info("After displaying the AuthorizationTaxMPAmountController");
								}else if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")
										|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")
										|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")
										|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase("yes")
										|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase("yes")
										|| MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")) {

									log.debug("inside winning combo screen Yes");								
									if (TopMiddleController.getCurrentComposite() != null
											&& !(TopMiddleController.getCurrentComposite()
													.isDisposed())) {
										TopMiddleController.getCurrentComposite().dispose();
									}
									//CHK TO SET THE DISPLAY MASK AMT FIELD ACCORDINGLY
									setJackpotFormMaskAmountFlag(oldFormHandPaidAmtVal);
									new PendingJackpotDetailsController(
											TopMiddleController.jackpotMiddleComposite, SWT.NONE,
											new PendingJackpotDetailsForm(), new SDSValidator(getClass(), true));
									log.info("After displaying the PendingJackpotDetailsController");
								}
								else{								
									log.info("Express Jackpot - Success, Auth is not required and Last Screen is disabled");
									log.info("--------------Update pending process------------------");
									/** To update the DB with the Pending Jackpot Details */
									PendingJackpotProcess pendingProcess = new PendingJackpotProcess();
									pendingProcess.processPending();
									if (TopMiddleController.getCurrentComposite() != null
											&& !(TopMiddleController.getCurrentComposite()
													.isDisposed())) {
										TopMiddleController.getCurrentComposite().dispose();									
									}
									CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
									initialScreen.callPendingJPFirstScreen();	

								}
								MainMenuController.jackpotForm.setExpressSuccess(true);
							} else if (blindAttempt < 1) {

								log.info("Inside else of blindAttempt < 1");

								boolean success = false;
								try {
									log.debug("Calling the postBlindAttempst web method");
									log.debug("*******************************************");
									log.debug("Sequence no: "+sequenceNum);				
									log.debug("*******************************************");
									success = JackpotServiceLocator.getService().postExpressJackpotBlindAttempts(sequenceNum, MainMenuController.jackpotForm.getSiteId());
									log.debug("Values returned after calling the postBlindAttempst web method");
									log.debug("*******************************************");
									log.debug("postBlindAttempst response: "+success);								
									log.debug("*******************************************");
								} catch (JackpotEngineServiceException e2) {
									JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
									handler.handleException(e2,
									"Exception in postBlindAttempst");
									log.error("Exception in postBlindAttempst",e2);
									return;
								}catch (Exception e2) {
									JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
									handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
									log.error("SERVICE_DOWN",e2);
									return;
								}

								log.debug("express jackpot posting sucess"
										+ success);
								MessageDialogUtil
								.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.INVALID_AMOUNT));
								//SET THE ALTERED BLIND ATTEMPTS TO THE JACKPOT FORM
								MainMenuController.jackpotForm.setBlindAttempts((short)(blindAttempt+1));
								
								amountSlotAttendantIdComposite.getTxtHandPaidAmount().forceFocus();

							} else if (blindAttempt == 1) {

								log.info("Inside else of blindAttempt == 1");

								boolean success = false;
								try {
									log.debug("Calling the postBlindAttempst web method");
									log.debug("*******************************************");
									log.debug("Sequence no: "+sequenceNum);				
									log.debug("*******************************************");
									success = JackpotServiceLocator.getService().postExpressJackpotBlindAttempts(sequenceNum, MainMenuController.jackpotForm.getSiteId());
									log.debug("Values returned after calling the postBlindAttempst web method");
									log.debug("*******************************************");
									log.debug("postBlindAttempst response: "+success);								
									log.debug("*******************************************");
								} catch (Exception e2) {
									JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
									handler.handleException(e2,
									"Exception in postBlindAttempst");
									log.error("Exception in postBlindAttempst",e2);
								}
								log.debug("express jackpot posting sucess"
										+ success);


								boolean continueAsNormal = MessageDialogUtil
								.displayTouchScreenQuestionDialog(
										LabelLoader
										.getLabelValue(MessageKeyConstants.EXP_JP_BLIND_ATTEMPTS_EXCEEDED),
										amountSlotAttendantIdComposite);

								/** PERFORM ROUNDING */
								performRounding();

								if (continueAsNormal) {
									log.info("Continue as normal jp - YES");
									MainMenuController.jackpotForm.setAuthorizationRequired(true);	
																
									addNumericListenerToHPJPAmtTxtField();									
									
									//SET THE ALTERED BLIND ATTEMPTS TO THE JACKPOT FORM
									MainMenuController.jackpotForm.setBlindAttempts((short)(blindAttempt+1));
									
									MainMenuController.jackpotForm.setProcessAsExpress(false);
									MainMenuController.jackpotForm.setExpressSuccess(false);
									return;
								} else {
									log.info("Continue as normal jp - NO, and ask for supervisory id later");
																		
									//UPDATE THE BLIND ATTEMPTS TO -1 SINCE THE EXP JP BLIND ATT IS EXCEEDED AND UR ABORTING THE NORMAL JP PROCESS
									boolean isBlindAttUpdated = JackpotUIUtil.postUnsuccessfulExpJpBlindAttemptsAbort(sequenceNum, MainMenuController.jackpotForm.getSiteId());
									if(isBlindAttUpdated){
										MainMenuController.jackpotForm.setBlindAttempts(IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT);
									}
									
									if (TopMiddleController.getCurrentComposite() != null
											&& !(TopMiddleController
													.getCurrentComposite()
													.isDisposed())) {
										TopMiddleController.getCurrentComposite()
										.dispose();
									}
									CallInitialScreenUtil initailScreen = new CallInitialScreenUtil();
									initailScreen.callPendingJPFirstScreen();								
								}

							}

						}					
						else {
							processNotAsExpress(oldFormHandPaidAmtVal);
						}
						/** SETTING THE JACKPOT NET AMOUNT */					
															
						if(MainMenuController.jackpotForm.getRoundedHPJPAmount()!=0){
							MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm.getRoundedHPJPAmount());
						}else if(MainMenuController.jackpotForm.getHandPaidAmount()!=0){
							MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm.getHandPaidAmount());
						}
					}
					// End of Next button code
					else if (((SDSImageLabel) control).getName().equalsIgnoreCase(
					"cancel")) {
						boolean response= false;				
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS), amountSlotAttendantIdComposite);
						if(response){
							
							if(MainMenuController.jackpotForm.getBlindAttempts()==IAppConstants.MAX_BLIND_ATTEMPTS){
								//when canceling if it is an express jackpot bilnd attempts exceeded normal jp process in progress
								//set update the blind attempts to -1
								//UPDATE THE BLIND ATTEMPTS TO -1 SINCE THE EXP JP BLIND ATT IS EXCEEDED AND UR ABORTING THE NORMAL JP PROCESS
								JackpotUIUtil.postUnsuccessfulExpJpBlindAttemptsAbort(MainMenuController.jackpotForm.getSequenceNumber(), MainMenuController.jackpotForm.getSiteId());
							}
														
							log.debug("Pending Jackpot process aborted");
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite()
											.isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();							
							}
							//MainMenuController.jackpotForm.setShowJPAmountAuth(false);
							CallInitialScreenUtil initailScreen = new CallInitialScreenUtil();
							initailScreen.callPendingJPFirstScreen();
						}
					}
					else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {

						log.debug("Previous button action clause");				
						if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG) , amountSlotAttendantIdComposite)){
							JackpotUIUtil.disposeCurrentMiddleComposite();	
							PendingJackpotDisplayForm prevScrForm = new PendingJackpotDisplayForm();
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(PendingJackpotDisplayController.form, prevScrForm);
							
							if(MainMenuController.jackpotForm.getPendingDisplayFilter().equalsIgnoreCase("getJackpotDetailsForSlotNumber")
									|| MainMenuController.jackpotForm.getPendingDisplayFilter().equalsIgnoreCase("getJackpotDetailsForSlotLocation")) {
								MainMenuController.jackpotForm.setSequenceNumber(0);							
							}
							MainMenuController.jackpotForm.setPouchPay(false);
//							MainMenuController.jackpotForm.setShowJPAmountAuth(false);
							new PendingJackpotDisplayController(TopMiddleController.jackpotMiddleComposite, SWT.NONE,
									prevScrForm, new SDSValidator(getClass(),true));
							return;				
						}
					}
				}

			}// End of try
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void mouseUp(MouseEvent e) {			
			
		}
	}
	
	public void addNumericListenerToHPJPAmtTxtField(){
		NumericListener numericListener = amountSlotAttendantIdComposite.getTxtHandPaidAmount().getNumericListener();
		if(numericListener!=null){
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().removeListener(SWT.Verify,numericListener);
			
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().setText(ConversionUtil.maskAmount());
			form.setHandPaidAmount(ConversionUtil.maskAmount());
			
			NumericListener newNumericListener = new NumericListener();
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().addListener(SWT.Verify, newNumericListener);
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().setNumericListener(newNumericListener);
		}else{
			amountSlotAttendantIdComposite.getTxtHandPaidAmount().setText(ConversionUtil.maskAmount());
			form.setHandPaidAmount(ConversionUtil.maskAmount());
		}
	}
	
	
	public void setJackpotFormMaskAmountFlag(String oldFormHandPaidAmtVal){
		if(oldFormHandPaidAmtVal!=null && oldFormHandPaidAmtVal.contains(IAppConstants.MASKED_AMOUNT_STRING)){
			// RESETTING THE DISPLAY MASK AMT FIELD TO TRUE
			MainMenuController.jackpotForm.setDisplayMaskAmt(true);
		}else{
			// RESETTING THE DISPLAY MASK AMT FIELD TO FALSE
			MainMenuController.jackpotForm.setDisplayMaskAmt(false);
		}
	}
	
}

