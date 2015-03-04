/*****************************************************************************
 * $Id: PendingJackpotDetailsController.java,v 1.25.1.1.2.0, 2013-10-14 16:34:33Z, SDS12.3.3 Checkin User$
 * $Date: 10/14/2013 11:34:33 AM$
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

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpotui.composite.PendingJackpotDetailsComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.AmountSlotAttendantIdForm;
import com.ballydev.sds.jackpotui.form.AuthorizationTaxMPAmountForm;
import com.ballydev.sds.jackpotui.form.PendingJackpotDetailsForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.ManualJackpotProcess;
import com.ballydev.sds.jackpotui.util.PendingJackpotProcess;

/**
 * Controller Class for the WinningCombinationComposite
 * 
 * @author dambereen
 * @version $Revision: 29$
 */
public class PendingJackpotDetailsController extends SDSBaseController {
	/**
	 * WinningCombinationForm Instance
	 */
	private PendingJackpotDetailsForm form;

	
	/**
	 * WinningCombinationComposite Instance
	 */
	private PendingJackpotDetailsComposite pendingJackpotDetailsComposite;

	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * WinningCombinationController constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public PendingJackpotDetailsController(Composite parent, int style,
			PendingJackpotDetailsForm form, SDSValidator validator)
			throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.setMandatoryLabelOff();
		createCompositeOnSiteConfigParam(parent, style);
		setPlayerNameForAccountNumber();
		TopMiddleController.setCurrentComposite(pendingJackpotDetailsComposite);

		log.debug("*************" + TopMiddleController.getCurrentComposite());
		
		//display amt only for non exp jps alone, do include the non exp blind attempts exceeded jps
		//if(MainMenuController.jackpotForm.getBlindAttempts() != IAppConstants.UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT){
		if(!MainMenuController.jackpotForm.isDisplayMaskAmt()){
			if(MainMenuController.jackpotForm.getRoundedHPJPAmount()!=0){
				pendingJackpotDetailsComposite.getSlipStaticInfoComposite().getLblAdjustedAmountLbl().setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
						+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getRoundedHPJPAmount())));
			}
			else{
				pendingJackpotDetailsComposite.getSlipStaticInfoComposite().getLblAdjustedAmountLbl().setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
						+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())));
			}
		}
					
		//parent.layout();		
		super.registerEvents(pendingJackpotDetailsComposite);
		form.addPropertyChangeListener(this);
		FocusUtility.setTextFocus(pendingJackpotDetailsComposite);
		FocusUtility.focus = false;
		registerCustomizedListeners(pendingJackpotDetailsComposite);
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
		PendingJackpotDetailsListener listener = new PendingJackpotDetailsListener();
		pendingJackpotDetailsComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		pendingJackpotDetailsComposite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		pendingJackpotDetailsComposite.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		pendingJackpotDetailsComposite.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		pendingJackpotDetailsComposite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		pendingJackpotDetailsComposite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
	}

	public void createCompositeOnSiteConfigParam(Composite parent, int style) {

		createWinningCombinationComposite(parent, style,
				getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(pendingJackpotDetailsComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());

	}

	/**
	 * Method to create the WinningCombinationComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createWinningCombinationComposite(Composite p, int s,
			String[] siteParam) {
		pendingJackpotDetailsComposite = new PendingJackpotDetailsComposite(p,
				s, siteParam);
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

	/*
	 * Method to get the keyboard focus in de text box
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}

	
	/**
	 * Method to check the site configuration parameters and returns a String
	 * array of the parameters to the composite , to create the controls based
	 * on the same.
	 * 
	 * @return
	 */
	public String[] getSiteConfigurationParameters() {
		String[] paramFlags = { "no", "no", "no", "no", "no", "no" };
		log.info("test boolean " + paramFlags[0]);
		// if(SiteConfigurationConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS.equalsIgnoreCase("yes")){
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[0] = "yes";
		} else {
			paramFlags[0] = "no";
		}
		// if(SiteConfigurationConstants.ALLOW_PAYLINE_ON_JP_SLIPS.equalsIgnoreCase("yes")){
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[1] = "yes";
		} else {
			paramFlags[1] = "no";
		}
		// if(SiteConfigurationConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS.equalsIgnoreCase("yes")){
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[2] = "yes";
		} else {
			paramFlags[2] = "no";
		}
		// if(SiteConfigurationConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS.equalsIgnoreCase("yes")){
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[3] = "yes";
		} else {
			paramFlags[3] = "no";
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[4] = "yes";
		} else {
			paramFlags[4] = "no";
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[5] = "yes";
		} else {
			paramFlags[5] = "no";
		}

		return paramFlags;
	}
	
	/**
	 * Method to set the player name associated with the cashless account number in the player name text box
	 * @author vsubha
	 */
	public void setPlayerNameForAccountNumber() {
		if (MainMenuController.jackpotForm.getPlayerName() != null
				&& !MainMenuController.jackpotForm.getPlayerName().trim().isEmpty()
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			pendingJackpotDetailsComposite.getTxtPlayerName().setText(
					MainMenuController.jackpotForm.getPlayerName());
			pendingJackpotDetailsComposite.getTxtPlayerName().setEditable(false);
		}
	}
	
	private class PendingJackpotDetailsListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {
			try {
				Control control = (Control) e.getSource();

				if (!((Control) e.getSource() instanceof SDSImageLabel)) {
					return;
				}

				if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
					boolean response= false;				
					response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS), pendingJackpotDetailsComposite);
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
							CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
							initialScreen.callPendingJPFirstScreen();	
						}
					}
				} else if (((SDSImageLabel) control).getName().equalsIgnoreCase(
						"next")) {
					log.debug("************ After next button is clicked:**********");
					populateForm(pendingJackpotDetailsComposite);
				
					// VALIDATION IF FIELDS ARE MANDATORY
					
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_WINNING_COMB_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getWinningCombination())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.WINNING_COMBINATION)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						pendingJackpotDetailsComposite.getTxtWinningCombination().setFocus();
						return;					
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PAYLINE_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getPayline())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.PAY_LINE)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						pendingJackpotDetailsComposite.getTxtPayline().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_COINS_PLAYED_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getCoinsPlayed())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.COINS_PLAYED)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						pendingJackpotDetailsComposite.getTxtCoinsPlayed().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_WINDOW_NO_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getWindowNo())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.WINDOW_NUMBER)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						pendingJackpotDetailsComposite.getTxtWindowNo().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PLAYER_CARD_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getPlayerCard())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_CARD)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						pendingJackpotDetailsComposite.getTxtPlayerCard().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PLAYER_NAME_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getPlayerName())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_NAME)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						pendingJackpotDetailsComposite.getTxtPlayerName().setFocus();
						return;
					}				
					
					List<String> fieldNames = new ArrayList<String>();

					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_WINNING_COMB);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_PAYLINE);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_COINS_PLAYED);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_WINDOW_NO);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_PLAYER_CARD);
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
						fieldNames.add(FormConstants.FORM_PLAYER_NAME);
					}
					boolean validate = validate("PendingJackpotDetailsForm",
							fieldNames, form, pendingJackpotDetailsComposite);
					
					if (!validate) {
						log.info("Validation failed");
						return;
					}
									
					if (form.getWinningCombination() != null) {
						MainMenuController.jackpotForm.setWinningCombination(form.getWinningCombination());
					}				
					MainMenuController.jackpotForm.setPayline(form.getPayline());
					if (form.getCoinsPlayed()!=null && form.getCoinsPlayed().length()>=1) {
						MainMenuController.jackpotForm.setCoinsPlayed(Integer
								.parseInt(form.getCoinsPlayed()));
					}
					if (form.getWindowNo() != null) {
						MainMenuController.jackpotForm.setWindowNumber(form.getWindowNo());
					}				
					if (form.getPlayerCard() != null && form.getPlayerCard().length() > 0) {
						ProgressIndicatorUtil.openInProgressWindow();
						long playerCardId = JackpotServiceLocator.getService().getPlayerCardId(
								form.getPlayerCard(), MainMenuController.jackpotForm.getSiteId());
						ProgressIndicatorUtil.closeInProgressWindow();
						if(playerCardId == 0) {
							if(log.isInfoEnabled()) {
								log.info("Player card " + form.getPlayerCard() + " is not valid as playerCardId is " + playerCardId);
							}
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.JKPT_INVALID_PLAYER_CARD));
							pendingJackpotDetailsComposite.getTxtPlayerCard().setFocus();
							return;
						} else if(log.isDebugEnabled()) {
							log.debug("Player card " + form.getPlayerCard() + " is valid as playerCardId is " + playerCardId);
						}
						MainMenuController.jackpotForm.setPlayerCard(form.getPlayerCard());
					}
					if (form.getPlayerName() != null) {
						MainMenuController.jackpotForm.setPlayerName(form.getPlayerName());
					}

					if (MainMenuController.jackpotForm.getSelectedJackpotFunction()
							.equalsIgnoreCase("Manual")) {
						/** If this is the last screen and it is for Manual Jackpot */
						ManualJackpotProcess processCall = new ManualJackpotProcess();
						processCall.processManual();
					} else if (MainMenuController.jackpotForm
							.getSelectedJackpotFunction().equalsIgnoreCase(
									"Pending")) {
						/** If this is the last screen and it is for Pending Jackpot */
						PendingJackpotProcess processPendingObj = new PendingJackpotProcess();
						processPendingObj.processPending();
					}
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
					callInitialScreenUtil.callPendingJPFirstScreen();
				}else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {
					
					log.debug("Previous button action clause");				
					if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG) , pendingJackpotDetailsComposite)){
						JackpotUIUtil.disposeCurrentMiddleComposite();
						
						//COND CHK IF THE AUTHORIZATION SCREEN WAS DISPLAYED
						if(AuthorizationTaxMPAmountController.form != null
								&& ((MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes")
										&& ((new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0)
												|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) !=0
												|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) != 0))
								|| MainMenuController.jackpotForm.getAuthorizationRequired())){
								
								//&& MainMenuController.jackpotForm.getAuthorizationRequired()){
							AuthorizationTaxMPAmountForm prevScrForm = new AuthorizationTaxMPAmountForm();
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(AuthorizationTaxMPAmountController.form, prevScrForm);
											
							new AuthorizationTaxMPAmountController(TopMiddleController.jackpotMiddleComposite, SWT.NONE,
									prevScrForm, new SDSValidator(getClass(),true));
						}//ELSE COND CHK IF THE AUTHORIZATION SCREEN WAS NOT DISPLAYED, THEN USE THE AMOUNT SCREEN FORM
						else{						
							AmountSlotAttendantIdForm prevScrForm = new AmountSlotAttendantIdForm();
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(AmountSlotAttendantIdController.form, prevScrForm);
											
							new AmountSlotAttendantIdController(TopMiddleController.jackpotMiddleComposite, SWT.NONE,
									prevScrForm, new SDSValidator(getClass(),true), true);						
						}
						return;				
					}
				}
			} catch (Exception ex) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(ex,MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN",ex);
				return;
			}
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}