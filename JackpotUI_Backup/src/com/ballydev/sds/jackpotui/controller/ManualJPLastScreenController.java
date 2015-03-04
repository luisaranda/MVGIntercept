/*****************************************************************************
 * $Id: ManualJPLastScreenController.java,v 1.33.1.0.2.0, 2013-10-14 16:34:33Z, SDS12.3.3 Checkin User$
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
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.composite.ManualJPLastScreenComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.ManualJPAuthMachineAmtTaxForm;
import com.ballydev.sds.jackpotui.form.ManualJPHandPaidAmtPromoJackpotTypeForm;
import com.ballydev.sds.jackpotui.form.ManualJPLastScreenForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.ManualJackpotProcess;

/**
 * Controller class for the ManualJackpotLastScreenComposite
 * @author dambereen
 * @version $Revision: 36$
 */
public class ManualJPLastScreenController extends SDSBaseController {

	/**
	 * ManualJackpotLastScreenForm Instance
	 */
	private ManualJPLastScreenForm form;
	
	/**
	 * ManualJackpotLastScreenComposite Instance
	 */
	private ManualJPLastScreenComposite manualJpLastScreenComp;

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
	public ManualJPLastScreenController(Composite parent, int style,
			ManualJPLastScreenForm form, SDSValidator validator)
			throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.setMandatoryLabelOff();
		createCompositeOnSiteConfigParam(parent, style);
		setPlayerNameForAccountNumber();
		// createWinningCombinationComposite(parent, style);
		TopMiddleController.setCurrentComposite(manualJpLastScreenComp);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
		//parent.layout();		
		/** Data set for the Static Display Composite */
		setDataForManualStaticDisplay();		
		super.registerEvents(manualJpLastScreenComp);
		form.addPropertyChangeListener(this);
		FocusUtility.setTextFocus(manualJpLastScreenComp);
		FocusUtility.focus = false;
		registerCustomizedListeners(manualJpLastScreenComp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ManualJPLastScreenListener listener = new ManualJPLastScreenListener();
		manualJpLastScreenComp.getButtonComposite().getBtnNext().addMouseListener(listener);
		manualJpLastScreenComp.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		manualJpLastScreenComp.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		manualJpLastScreenComp.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		manualJpLastScreenComp.getButtonComposite().getBtnCancel().addMouseListener(listener);
		manualJpLastScreenComp.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
	}

	public void createCompositeOnSiteConfigParam(Composite parent, int style) {
		createManualJackpotLastScreenComposite(parent, style,
				getSiteConfigurationParameters());
	}

	/**
	 * Method to create the WinningCombinationComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createManualJackpotLastScreenComposite(Composite p, int s,
			String[] siteParam) {
		manualJpLastScreenComp = new ManualJPLastScreenComposite(p, s,
				siteParam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return manualJpLastScreenComp;
	}

	/**
	 * Method to check the site configuration parameters and returns a String
	 * array of the parameters to the composite , to create the controls based
	 * on the same.
	 * 
	 * @return
	 */
	public String[] getSiteConfigurationParameters() {
		String[] paramFlags = new String[6];
		if(log.isInfoEnabled()) {
			log.info("test boolean " + paramFlags[0]);
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[0] = "yes";
		} else {
			paramFlags[0] = "no";
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[1] = "yes";
		} else {
			paramFlags[1] = "no";
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
			paramFlags[2] = "yes";
		} else {
			paramFlags[2] = "no";
		}
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
	
	public void setPlayerNameForAccountNumber() {
		if (MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("Yes")
				&& MainMenuController.jackpotForm.getAccountNumber() != null
				&& !MainMenuController.jackpotForm.getAccountNumber().trim().isEmpty()
				&& Long.parseLong(MainMenuController.jackpotForm.getAccountNumber()) > 0) {
			// SETTING PLAYER NAME FOR THE CASHLESS ACCOUNT NUMBER
			try {
				CashlessAccountDTO cashlessAccountDTO = JackpotServiceLocator.getService()
						.validateCashlessAccountNo(MainMenuController.jackpotForm.getSiteId(),
								MainMenuController.jackpotForm.getAccountNumber());
				if(cashlessAccountDTO.isSuccess()) {
					String playerCardNumber = cashlessAccountDTO.getPlayerCardNumber();
					if(playerCardNumber != null && !playerCardNumber.trim().isEmpty()) {
						MainMenuController.jackpotForm.setAssociatedPlayerCard(playerCardNumber);
						String playerName = JackpotServiceLocator.getService().getPlayerCardName(
								playerCardNumber, MainMenuController.jackpotForm.getSiteId());
						if(playerName != null && !playerName.trim().isEmpty()) {
							if (MainMenuController.jackpotSiteConfigParams.get(
									ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase(
									"yes")) {
								manualJpLastScreenComp.getTxtPlayerName().setText(playerName.trim());
								manualJpLastScreenComp.getTxtPlayerName().setEditable(false);
							}
							MainMenuController.jackpotForm.setPlayerName(playerName.trim());
						}
					} else if(log.isInfoEnabled()){
						log.info("Cashless account's player number not available. Hence Player Name is not retrieved.");
					}
				}
			} catch (JackpotEngineServiceException e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, "Exception in setPlayerNameForAccountNumber");
				log.error("Exception in setPlayerNameForAccountNumber", e2);
				return;
			} catch (Exception e2) {
				JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
				handler.handleException(e2, MessageKeyConstants.SERVICE_DOWN);
				log.error("SERVICE_DOWN", e2);
				return;
			}
		}
	}
	
	/**
	 * Method to set the data for the Manual Jackpot static display
	 */
	public void setDataForManualStaticDisplay(){
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblEmployeeIdInfo().setText(""+MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
		}
		//manualJpLastScreenComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+MainMenuController.jackpotForm.getShift());
		
		if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Day")) {
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Swing")) {
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Graveyard")) {
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
		}else {
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}
		
		manualJpLastScreenComp.getSlipStaticInfoComposite().getLblSlotInfo().setText(""+StringUtil.trimAcnfNo(MainMenuController.jackpotForm.getSlotNo()));
		if(MainMenuController.jackpotForm.getSlotLocationNo()!=null) {
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblStandInfo().setText(""+MainMenuController.jackpotForm.getSlotLocationNo().toUpperCase());
		}
		manualJpLastScreenComp.getSlipStaticInfoComposite().getLblHandPaidAmount().setVisible(true);
		manualJpLastScreenComp.getSlipStaticInfoComposite().getLblHandPaidAmountInfo().setVisible(true);
		
		if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
				&& MainMenuController.jackpotForm.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase(
						"yes")
				&& JackpotUIUtil.isRoundingAllowedForJPAmount(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
						.getHandPaidAmount()))) {
			
			manualJpLastScreenComp
					.getSlipStaticInfoComposite()
					.getLblHandPaidAmountInfo()
					.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
									+ JackpotUIUtil.getFormattedAmounts( 
											ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getRoundedHPJPAmount())));
			
		} else if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
				&& MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ROUND_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase("yes")
				&& JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()) != null
				&& JackpotUIUtil.isRoundingAllowedForJPAmount(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
						.getHandPaidAmount()))
				&& MainMenuController.jackpotSiteConfigParams.get(
						JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()))
						.equalsIgnoreCase("yes")) {

			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblHandPaidAmountInfo().setText(
					MainMenuController.jackpotForm.getSiteCurrencySymbol()
							+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
									.centsToDollar(MainMenuController.jackpotForm.getRoundedHPJPAmount())));
		} else {
				manualJpLastScreenComp.getSlipStaticInfoComposite().getLblHandPaidAmountInfo().setText(
						MainMenuController.jackpotForm.getSiteCurrencySymbol()
							+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
									.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())));
		}
		manualJpLastScreenComp.getSlipStaticInfoComposite().getLblJackpotType().setVisible(true);
		manualJpLastScreenComp.getSlipStaticInfoComposite().getLblJackpotTypeInfo().setVisible(true);		
		manualJpLastScreenComp.getSlipStaticInfoComposite().getLblJackpotTypeInfo().setText(""+MainMenuController.jackpotForm.getJackpotType());
		
		/*if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS).equalsIgnoreCase("yes")){
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblMachinePaidAmount().setVisible(true);
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblMachinePaidAmountInfo().setVisible(true);
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblMachinePaidAmountInfo().setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()+ConversionUtil.twoPlaceDecimalOf(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getMachinePaidAmount())));
		}*/	
		/*if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes")
				&& (new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) !=0 || new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT))!=0)){
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblTaxType().setVisible(true);
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblTaxTypeInfo().setVisible(true);
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblTaxTypeInfo().setText(""+MainMenuController.jackpotForm.getTaxType());
		}*/
		// Showing the cardless account number info when Allow auto deposit to
		// eCash account flag is set to YES.
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("Yes")) {
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblAccountNumber().setVisible(true);
			manualJpLastScreenComp.getSlipStaticInfoComposite().getLblAccountNumberInfo().setVisible(true);
		}
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
	
	private class ManualJPLastScreenListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {


			try {
				Control control = (Control) e.getSource();

				if (!((Control) e.getSource() instanceof SDSImageLabel)) {
					return;
				}
				if (control instanceof SDSImageLabel) {
				if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
					boolean response= false;				
					response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS), manualJpLastScreenComp);
					log.info("Manual Jackpot process aborted");
					if(response){
						JackpotUIUtil.disposeCurrentMiddleComposite();
						CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
						initialScreen.callManualJPFirstScreen();
					}
				} 
				 else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {
						
					log.debug("Previous button action clause");
					if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG) , manualJpLastScreenComp)){
						JackpotUIUtil.disposeCurrentMiddleComposite();
						
						//COND CHK IF THE AUTHORIZATION SCREEN WAS DISPLAYED
						if(ManualJPAuthMachineAmtTaxController.form!=null
								&& ((MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes")
										&& ((new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0)
												|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) !=0 
												|| new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) !=0 ))
								|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq()
								|| (new Double(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
										.getHandPaidAmount())) >= new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS))
									&& new Long(MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) >= 1))){
							
							ManualJPAuthMachineAmtTaxForm prevScrForm = new ManualJPAuthMachineAmtTaxForm();					
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(ManualJPAuthMachineAmtTaxController.form, prevScrForm);					
							new ManualJPAuthMachineAmtTaxController(TopMiddleController.jackpotMiddleComposite, SWT.NONE, 
									prevScrForm, new SDSValidator(getClass(),true));
						}//ELSE COND CHK IF THE AUTHORIZATION SCREEN WAS NOT DISPLAYED, THEN USE THE AMOUNT SCREEN FORM
						else{
							ManualJPHandPaidAmtPromoJackpotTypeForm prevScrForm = new ManualJPHandPaidAmtPromoJackpotTypeForm();					
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(ManualJPHandPaidAmtPromoJackpotTypeController.form, prevScrForm);					
							new ManualJPHandPaidAmtPromoJackpotTypeController(TopMiddleController.jackpotMiddleComposite, SWT.NONE, 
									prevScrForm, new SDSValidator(getClass(),true));
						}
						return;		
					}							
					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {
					log.debug("************ After next button is clicked:**********");
					populateForm(manualJpLastScreenComp);

					// VALIDATION IF FIELDS ARE MANDATORY
					
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_WINNING_COMB_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getWinningCombination())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.WINNING_COMBINATION)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						manualJpLastScreenComp.getTxtWinningCombination().setFocus();
						return;					
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PAYLINE_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getPayline())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.PAY_LINE)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						manualJpLastScreenComp.getTxtPayline().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_COINS_PLAYED_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getCoinsPlayed())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.COINS_PLAYED)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						manualJpLastScreenComp.getTxtCoinsPlayed().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_WINDOW_NO_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getWindowNo())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.WINDOW_NUMBER)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						manualJpLastScreenComp.getTxtWindowNo().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PLAYER_CARD_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getPlayerCard())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_CARD)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						manualJpLastScreenComp.getTxtPlayerCard().setFocus();
						return;
					}
					if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase("yes")
							&& MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_PLAYER_NAME_MANDATORY).equalsIgnoreCase("yes")
							&& GenericValidator.isBlankOrNull(form.getPlayerName())) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(LabelKeyConstants.PLAYER_NAME)+" "+LabelLoader.getLabelValue(MessageKeyConstants.FIELD_IS_REQUIRED));
						manualJpLastScreenComp.getTxtPlayerName().setFocus();
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
					
					boolean	validate = validate("ManualJPLastScreenForm", fieldNames, form, manualJpLastScreenComp);
								
					if (!validate) {
						log.info("Validation failed");
						return;
					}
					if(form.getWinningCombination() != null){
						MainMenuController.jackpotForm.setWinningCombination(form.getWinningCombination());
					}					
					if(form.getPayline() != null){
						MainMenuController.jackpotForm.setPayline(form.getPayline());
					}					
					if (form.getCoinsPlayed() != null && !form.getCoinsPlayed().equals("")) {
						MainMenuController.jackpotForm.setCoinsPlayed(Integer.parseInt(form.getCoinsPlayed()));			
					}						
					if(form.getWindowNo() != null){
						MainMenuController.jackpotForm.setWindowNumber(form.getWindowNo());			
					}				
					if (form.getPlayerCard() != null && form.getPlayerCard().length() >= 1) {
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
							manualJpLastScreenComp.getTxtPlayerCard().setFocus();
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
					} 
					JackpotUIUtil.disposeCurrentMiddleComposite();	
					CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
					callInitialScreenUtil.callManualJPFirstScreen();
				 }
				}
			} catch (Exception ex) {
				log.error(ex);
			}
		
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
