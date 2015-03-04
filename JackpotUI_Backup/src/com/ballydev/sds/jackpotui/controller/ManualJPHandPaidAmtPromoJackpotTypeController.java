/*****************************************************************************
 * $Id: ManualJPHandPaidAmtPromoJackpotTypeController.java,v 1.54.1.0.2.0, 2013-10-14 16:34:33Z, SDS12.3.3 Checkin User$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.ecash.dto.account.PlayerSessionDTO;
import com.ballydev.sds.ecash.enumconstants.AccountTransTypeEnum;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpotui.composite.ManualJPHandPaidAmtPromoJackpotTypeComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.form.ManualJPAuthMachineAmtTaxForm;
import com.ballydev.sds.jackpotui.form.ManualJPEmployeeSlotStandShiftForm;
import com.ballydev.sds.jackpotui.form.ManualJPHandPaidAmtPromoJackpotTypeForm;
import com.ballydev.sds.jackpotui.form.ManualJPLastScreenForm;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.ManualJackpotProcess;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;

/**
 * This Class controls the events of the ManualJPHandPaidAmtPromoJackpotTypeComposite
 * 
 * @author anantharajr
 * @version $Revision: 57$
 */
public class ManualJPHandPaidAmtPromoJackpotTypeController extends SDSBaseController {

	/**
	 * ManualJPHandPaidAmtPromoJackpotTypeForm instance
	 */
	public static ManualJPHandPaidAmtPromoJackpotTypeForm form;

	/**
	 * ManualJPHandPaidAmtPromoJackpotTypeComposite instance
	 */
	private ManualJPHandPaidAmtPromoJackpotTypeComposite manualJpScreen2HPComposite;
	
	boolean isProgressive = false;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	
	/**
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public ManualJPHandPaidAmtPromoJackpotTypeController(Composite parent, int style,
			ManualJPHandPaidAmtPromoJackpotTypeForm form, SDSValidator validator)
			throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);	
		createManualJPHandPaidAmountComposite(parent, style, getSiteConfigurationParameters());
		TopMiddleController
				.setCurrentComposite(manualJpScreen2HPComposite);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
		//parent.layout();		
		/** Data set for the static display composite */
		setDataForManualStaticDisplay();		
		super.registerEvents(manualJpScreen2HPComposite);
		form.addPropertyChangeListener(this);
		populateScreen(manualJpScreen2HPComposite);
		populateForm(manualJpScreen2HPComposite);
		setPrevSelectedBtnGrp();
		registerCustomizedListeners(manualJpScreen2HPComposite);
		manualJpScreen2HPComposite.getTxtHandPaidAmount().forceFocus();
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
		ManualJPHPMouseListener listener = new ManualJPHPMouseListener();
		if(manualJpScreen2HPComposite.getJackpotTypeControl() != null){
			manualJpScreen2HPComposite.getJackpotTypeControl().getBtnNormal().addMouseListener(listener);
			manualJpScreen2HPComposite.getJackpotTypeControl().getBtnNormal().addTraverseListener(this);
			if(isProgressive){
				manualJpScreen2HPComposite.getJackpotTypeControl().getBtnProgressive().addMouseListener(listener);
				manualJpScreen2HPComposite.getJackpotTypeControl().getBtnProgressive().addTraverseListener(this);
			}
			manualJpScreen2HPComposite.getJackpotTypeControl().getBtnCanceledCredit().addMouseListener(listener);
			manualJpScreen2HPComposite.getJackpotTypeControl().getBtnCanceledCredit().addTraverseListener(this);
		}
		
		manualJpScreen2HPComposite.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		manualJpScreen2HPComposite.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		
		manualJpScreen2HPComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		manualJpScreen2HPComposite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		
		manualJpScreen2HPComposite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		manualJpScreen2HPComposite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		
		return manualJpScreen2HPComposite;
	}

	/**
	 * this method is to call the ManualJPHandPaidAmountComposite
	 * 
	 * @param p
	 * @param s
	 */
	public void createManualJPHandPaidAmountComposite(Composite p, int s, String[] siteConfigParam) {
		if(!(MainMenuController.jackpotForm.getProgressiveSlot().equalsIgnoreCase(IAppConstants.N_CONSTANT))){
				//&& manualJpScreen2HPComposite.getJackpotTypeControl()!=null && !manualJpScreen2HPComposite.getBottomComposite().isDisposed()){
			this.isProgressive = true;
		}
		manualJpScreen2HPComposite = new ManualJPHandPaidAmtPromoJackpotTypeComposite(p, s, siteConfigParam, isProgressive);
	}

	public String[] getSiteConfigurationParameters()
	{
		String[] siteConfigParam = new String[3];
		if(!MainMenuController.jackpotForm.isPromotionalJackpot()) {
			 siteConfigParam[0] = "yes";
		} else {
			siteConfigParam[0] = "no";
		}
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("yes")) {
			siteConfigParam[1] = "Yes";
		} else {
			siteConfigParam[1] = "No";
		}
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SHOW_EXPIRY_DATE_FOR_PRINTED_SLIP).equalsIgnoreCase("Yes")) {
			siteConfigParam[2] = "Yes";
		} else {
				siteConfigParam[2] = "No";
		}
		return siteConfigParam;
	}
	
	public void setDataForManualStaticDisplay(){

		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")){
			manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblEmployeeIdInfo().setText(""+MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
		}
		/*if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT).equalsIgnoreCase("p")){			
			if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("d")){
				manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
			}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("s")){
				manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
			}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("g")){
				manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
			}else{
				manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
			}
		}*/
		
		if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Day")) {
			manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Swing")) {
			manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Graveyard")) {
			manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
		}else {
			manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}
		
		manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblSlotInfo().setText(""+StringUtil.trimAcnfNo(MainMenuController.jackpotForm.getSlotNo()));
		if(MainMenuController.jackpotForm.getSlotLocationNo()!=null) {
			manualJpScreen2HPComposite.getSlipStaticInfoComposite().getLblStandInfo().setText(""+MainMenuController.jackpotForm.getSlotLocationNo().toUpperCase());
		}		
		if(MainMenuController.jackpotForm.isPromotionalJackpot()){
			MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.PROMOTIONAL_JACKPOT_YES_LABEL));
		} else {
			if(form.getNormalYes()) {
				MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));
			} else if(form.getCanceledCreditsYes()) {
				MainMenuController.jackpotForm.setJackpotType(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_CANCELED_CREDITS_LABEL));
			}			
		}
	}
	
	/*
	 * Method to get the keyboard focus in the text box
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());		
	}
	
	/**
	 * Method to set the previous selected button in the JACKPOT TYPE group    
	 */
	public void setPrevSelectedBtnGrp() {
		if (manualJpScreen2HPComposite.getJackpotTypeControl() != null
				&& !manualJpScreen2HPComposite.getBottomComposite().isDisposed()) {
			if (form.getNormalYes()) {
				manualJpScreen2HPComposite.getJackpotTypeControl().getJpTypeRadioButtonComposite()
						.setSelectedButton(manualJpScreen2HPComposite.getJackpotTypeControl().getBtnNormal());
			} else if (form.getProgressiveYes()) {
				manualJpScreen2HPComposite
						.getJackpotTypeControl()
						.getJpTypeRadioButtonComposite()
						.setSelectedButton(
								manualJpScreen2HPComposite.getJackpotTypeControl().getBtnProgressive());
				manualJpScreen2HPComposite.getTxtProgressiveLevel().setEnabled(true);
			} else if (form.getCanceledCreditsYes()) {
				manualJpScreen2HPComposite
						.getJackpotTypeControl()
						.getJpTypeRadioButtonComposite()
						.setSelectedButton(
								manualJpScreen2HPComposite.getJackpotTypeControl().getBtnCanceledCredit());
			}
		}
	}
	
	@Override
	public void keyTraversed(TraverseEvent e) {
		super.keyTraversed(e);
	}
	
	private class ManualJPHPMouseListener implements MouseListener{

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
					populateForm(manualJpScreen2HPComposite);
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
						boolean response = false;
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(
								LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS),
								manualJpScreen2HPComposite);
						if (response) {
							if (log.isInfoEnabled()) {
								log.info("Manual Jackpot Process is aborted");
							}
							JackpotUIUtil.disposeCurrentMiddleComposite();
							CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
							initialScreen.callManualJPFirstScreen();
						}
					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase("next")) {
						// populateForm(manualJpScreen2HPComposite);
						boolean validate = validate("ManualJPHandPaidAmtPromoJackpotTypeForm", form,
								manualJpScreen2HPComposite);
						if (!validate) {
							if (log.isInfoEnabled()) {
								log.info("Validation failed");
							}
							return;
						}
						// Setting expiry date to JackpotForm
						if (form.getExpiryDate() != null) {
							MainMenuController.jackpotForm.setExpiryDate(form.getExpiryDate());
						}
						if (!ConversionUtil.isFractionsOfCentsNormal(form.getHandPaidAmount())) {
							if (log.isInfoEnabled()) {
								log.info("Fractions of a cent is not allowed");
							}
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
									.getLabelValue(MessageKeyConstants.FRACTIONS_OF_CENT_NOT_ALLOWED));
							manualJpScreen2HPComposite.getTxtHandPaidAmount().setFocus();
							return;
						}
						MainMenuController.jackpotForm.setRoundedHPJPAmount(0); // FIX for CR 94609
						if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
								&& MainMenuController.jackpotForm.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
								&& MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR)
										.equalsIgnoreCase("yes")
								&& JackpotUIUtil.isRoundingAllowedForJPAmount(form.getHandPaidAmount())) {
							if (log.isInfoEnabled()) {
								log.info("jackpot amt shd be rounded now for Progressive jp");
							}
							MainMenuController.jackpotForm.setRoundedHPJPAmount(ConversionUtil
									.dollarToCentsRtnLong(ConversionUtil.performRounding(form
											.getHandPaidAmount())));
						} else if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
								&& MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
								&& MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ROUND_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase("yes")
								&& JackpotUIUtil.isRoundingAllowedForJPAmount(form.getHandPaidAmount())
								&& JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()) != null
								&& MainMenuController.jackpotSiteConfigParams.get(
										JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm
												.getDenomination())).equalsIgnoreCase("yes")) {

							if (log.isDebugEnabled()) {
								log.debug("jackpot amt shd be rounded now");
							}
							MainMenuController.jackpotForm.setRoundedHPJPAmount(ConversionUtil
									.dollarToCentsRtnLong(ConversionUtil.performRounding(form
											.getHandPaidAmount())));
						}

						if (log.isInfoEnabled()) {
							log.info("form.getHandPaidAmount() : " + form.getHandPaidAmount());
						}
						MainMenuController.jackpotForm.setHandPaidAmount(ConversionUtil
								.dollarToCentsRtnLong(form.getHandPaidAmount()));
						if (log.isInfoEnabled()) {
							log.info(" 1: MainMenuController.jackpotForm.getHandPaidAmount(): "
									+ MainMenuController.jackpotForm.getHandPaidAmount());
						}
						long hpjpLongAmount = ConversionUtil.dollarToCentsRtnLong(form.getHandPaidAmount());

						if (log.isInfoEnabled()) {
							log.info("dollarToCents: " + hpjpLongAmount);
						}

						double hpjpDoubleAmount = (double) ConversionUtil.centsToDollar(hpjpLongAmount);

						if (log.isInfoEnabled()) {
							log.info("centsToDollar: " + hpjpDoubleAmount);
						}

						/************************* Authorization check *******************/

						if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {
							double jackpotAmount = UserFunctionsUtil.getJackpotAmount(
									MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(),
									MainMenuController.jackpotForm.getSiteId());
							/*
							 * SessionUtility sessionUtility = new
							 * SessionUtility(); int size =
							 * sessionUtility.getUserDetails
							 * ().getParameterList().size(); for(int
							 * i=0;i<size;i++) {
							 * log.info(sessionUtility.getUserDetails
							 * ().getParameterList().get(i).getParameterName());
							 * }
							 */

							if (log.isInfoEnabled()) {
								log.info("The JACKPOT AMOUNT is" + jackpotAmount);
							}
							if (hpjpDoubleAmount > jackpotAmount 
									&& new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES))>=1) {
								
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.HPJP_EXCEED_OVERIDE_AUTH_LIMIT_REQ_AUTH));
								manualJpScreen2HPComposite.getTxtHandPaidAmount().forceFocus();
								if (log.isInfoEnabled()) {
									log.info("Hp Amt exceeds jackpot limit. Requires Authorization");
								}
								MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(true);
								
							}else if (hpjpDoubleAmount > jackpotAmount) {
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
								manualJpScreen2HPComposite.getTxtHandPaidAmount().forceFocus();
								if (log.isInfoEnabled()) {
									log.info("Hp Amt exceeds jackpot limit");
								}
								MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);
								return;
							}else{
								MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);
							}							
						}

						// VALIDATING CASHLESS ACCOUNT NUMBER WITH ECASH ACCOUNT DETAILS
						if (form.getAccountNumber() != null) {
							//VALIDATE IF THE ACC NO IS ZERO, IF YES DISPLAY A MSG PROMPT
							if(Long.parseLong(form.getAccountNumber()) == 0) {
								log.info("ACCOUNT_NO_IS_ZERO");		
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(
										LabelLoader.getLabelValue(ILabelConstants.ACCOUNT_NO_INVALID));
								manualJpScreen2HPComposite.getTxtAccountNumber().forceFocus();
								return;
							}
							CashlessAccountDTO cashlessAccountDTO = JackpotServiceLocator.getService()
									.isJackpotOperationAllowed(form.getAccountNumber(),
											MainMenuController.jackpotForm.getSiteId(),
											AccountTransTypeEnum.JACKPOT.getTransId());
							if (!cashlessAccountDTO.isSuccess()) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.JKPT_INVALID_ACCOUNT_NUMBER));
								manualJpScreen2HPComposite.getTxtAccountNumber().setFocus();
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
						
						// VALIDATING PROGRESSIVE LEVEL
						if (form.getProgressiveYes()
								&& manualJpScreen2HPComposite.getTxtProgressiveLevel().isEnabled()) {
							if (form.getProgressiveLevel() != null
									&& form.getProgressiveLevel().trim().isEmpty()) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.PROG_LEVEL_CANNOT_BE_EMPTY));
								manualJpScreen2HPComposite.getTxtProgressiveLevel().setFocus();
								return;
							} else {
								ArrayList<Integer> progressiveLevel = JackpotUIUtil.getProgressiveLevelValues(form.getProgressiveLevel());
								if(!JackpotServiceLocator.getService().validateProgressiveLevel(MainMenuController.jackpotForm.getSiteId(), 
										progressiveLevel, MainMenuController.jackpotForm.getSlotNo())) {
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
											.getLabelValue(MessageKeyConstants.ENTER_VALID_PROG_LEVEL));	
									manualJpScreen2HPComposite.getTxtProgressiveLevel().setFocus();
									return;
								} else {
									MainMenuController.jackpotForm.setLstProgressiveLevel(progressiveLevel);
									MainMenuController.jackpotForm
											.setProgressiveLevel(manualJpScreen2HPComposite
													.getTxtProgressiveLevel().getText());
								}
							}
						}

						// SHOWING WARNING MESSAGE IF PLAYER IS NOT ACTIVE IN THE SLOT
						if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("Yes")
								&& MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.PROMPT_FOR_ACTIVE_PLAYER_SESSION_ON_JP)
										.equalsIgnoreCase("Yes")) {
							PlayerSessionDTO playerSessionDTO = JackpotServiceLocator.getService()
									.getActivePlayerSession(form.getAccountNumber(),
											MainMenuController.jackpotForm.getSlotNo(),
											MainMenuController.jackpotForm.getSiteId());
							if (!playerSessionDTO.isSuccess()) {
								// PLAYER NOT ACTIVE IN THE ASSET
								if (log.isInfoEnabled()) {
									log.info("Player is not active in the slot "
											+ MainMenuController.jackpotForm.getSlotNo());
								}
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader
										.getLabelValue(MessageKeyConstants.PLAYER_SESSION_NOT_ACTIVE)
										+ MainMenuController.jackpotForm.getSlotNo());
							} else if (log.isDebugEnabled()) {
								// PLAYER ACTIVE IN THE ASSET
								log.debug("Player is active in the slot " + MainMenuController.jackpotForm.getSlotNo());
							}
						}

						/** SETTING THE JACKPTO NET AMOUNT */
						if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
							MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm
									.getRoundedHPJPAmount());
						} else if (MainMenuController.jackpotForm.getHandPaidAmount() != 0) {
							MainMenuController.jackpotForm.setJackpotNetAmount(MainMenuController.jackpotForm
									.getHandPaidAmount());
						}

						/*
						 * if(MainMenuController.jackpotSiteConfigParams.get(
						 * ISiteConfigConstants
						 * .MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS
						 * ).equalsIgnoreCase("yes") ||
						 * MainMenuController.jackpotSiteConfigParams
						 * .get(ISiteConfigConstants
						 * .ALLOW_MACHINE_PAY_ON_JP_SLIPS
						 * ).equalsIgnoreCase("yes") ||
						 * (MainMenuController.jackpotSiteConfigParams
						 * .get(ISiteConfigConstants
						 * .TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes") &&
						 * (new
						 * Long(MainMenuController.jackpotSiteConfigParams.get
						 * (ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT))!=0
						 * || new
						 * Long(MainMenuController.jackpotSiteConfigParams
						 * .get(ISiteConfigConstants
						 * .FEDERAL_TAX_RATE_FOR_JAKCPOT)) !=0))){
						 */

						/*
						 * if(MainMenuController.jackpotSiteConfigParams.get(
						 * ISiteConfigConstants
						 * .MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS
						 * ).equalsIgnoreCase("yes") ||
						 * (MainMenuController.jackpotSiteConfigParams
						 * .get(ISiteConfigConstants
						 * .TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes") &&
						 * (new
						 * Long(MainMenuController.jackpotSiteConfigParams.get
						 * (ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT))!=0
						 * || new
						 * Long(MainMenuController.jackpotSiteConfigParams
						 * .get(ISiteConfigConstants
						 * .FEDERAL_TAX_RATE_FOR_JAKCPOT)) !=0))){
						 */

						if ((new Double(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
								.getHandPaidAmount())) >= new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS)) && new Long(
								MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) >=1)
								|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq()
								|| (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase("yes") && (new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0 || new Double(
										MainMenuController.jackpotSiteConfigParams
												.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) != 0 || new Double(
														MainMenuController.jackpotSiteConfigParams
														.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) != 0))) {

							JackpotUIUtil.disposeCurrentMiddleComposite();
							new ManualJPAuthMachineAmtTaxController(
									TopMiddleController.jackpotMiddleComposite, SWT.NONE,
									new ManualJPAuthMachineAmtTaxForm(), new SDSValidator(getClass(), true));
						} else if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS).equalsIgnoreCase("yes")
								|| MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS).equalsIgnoreCase(
										"yes")
								|| MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS)
										.equalsIgnoreCase("yes")
								|| MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS).equalsIgnoreCase(
										"yes")
								|| MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS).equalsIgnoreCase(
										"yes")
								|| MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS).equalsIgnoreCase(
										"yes")) {

							JackpotUIUtil.disposeCurrentMiddleComposite();
							new ManualJPLastScreenController(TopMiddleController.jackpotMiddleComposite,
									SWT.NONE, new ManualJPLastScreenForm(),
									new SDSValidator(getClass(), true));
						} else if (MainMenuController.jackpotForm.getSelectedJackpotFunction()
								.equalsIgnoreCase("Manual")) {
							/**
							 * If this is the last screen and it is for Manual
							 * Jackpot
							 */
							ManualJackpotProcess processCall = new ManualJackpotProcess();
							processCall.processManual();
							JackpotUIUtil.disposeCurrentMiddleComposite();
							CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
							initialScreen.callManualJPFirstScreen();
						}

					} else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {
						if (log.isDebugEnabled()) {
							log.debug("Previous button action clause");
						}
						if (MessageDialogUtil.displayTouchScreenQuestionDialog(
								LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG),
								manualJpScreen2HPComposite)) {
							JackpotUIUtil.disposeCurrentMiddleComposite();
							ManualJPEmployeeSlotStandShiftForm prevScrForm = new ManualJPEmployeeSlotStandShiftForm();
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(ManualJPEmployeeSlotStandShiftController.form,
									prevScrForm);

							new ManualJPEmployeeSlotStandShiftController(
									TopMiddleController.jackpotMiddleComposite, SWT.NONE, prevScrForm,
									new SDSValidator(getClass(), true));
							return;
						}
					}
				} else if (control instanceof TSButtonLabel) {
					TSButtonLabel jpTypeRadioImage = (TSButtonLabel) control;
					manualJpScreen2HPComposite.getJackpotTypeControl().getJpTypeRadioButtonComposite()
							.setSelectedButton(jpTypeRadioImage);
					String radioImageName = manualJpScreen2HPComposite.getJackpotTypeControl()
							.getJpTypeRadioButtonComposite().getSelectedButton().getName();
					System.out.println("Radio Image Name:" + radioImageName);
					if (radioImageName.equalsIgnoreCase("normal")
							|| radioImageName.equalsIgnoreCase("progressive")
							|| radioImageName.equalsIgnoreCase("canceledCredits")) {
						if (manualJpScreen2HPComposite.getJackpotTypeControl()
								.getJpTypeRadioButtonComposite().getSelectedButton().getName()
								.equalsIgnoreCase("normal")) {
							MainMenuController.jackpotForm.setJackpotType(LabelLoader
									.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));// ("Normal");
							MainMenuController.jackpotForm.setJackpotID("FC");
							MainMenuController.jackpotForm
									.setJackpotTypeId((short) ILookupTableConstants.JACKPOT_TYPE_REGULAR);
							form.setNormalYes(true);
							form.setProgressiveYes(false);
							form.setCanceledCreditsYes(false);
							manualJpScreen2HPComposite.getTxtProgressiveLevel().setEnabled(false);
						}
						if (manualJpScreen2HPComposite.getJackpotTypeControl()
								.getJpTypeRadioButtonComposite().getSelectedButton().getName()
								.equalsIgnoreCase("progressive")) {
							MainMenuController.jackpotForm.setJackpotType(LabelLoader
									.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_PROGRESSIVE_LABEL));// ("Progressive");
							MainMenuController.jackpotForm.setJackpotID("97");
							MainMenuController.jackpotForm
									.setJackpotTypeId(ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE);
							form.setNormalYes(false);
							form.setProgressiveYes(true);
							form.setCanceledCreditsYes(false);
							manualJpScreen2HPComposite.getTxtProgressiveLevel().setEnabled(true);
						} else if (manualJpScreen2HPComposite.getJackpotTypeControl()
								.getJpTypeRadioButtonComposite().getSelectedButton().getName()
								.equalsIgnoreCase("canceledCredits")) {
							MainMenuController.jackpotForm.setJackpotType(LabelLoader
									.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_CANCELED_CREDITS_LABEL));// ("Canceled Credits");
							MainMenuController.jackpotForm.setJackpotID("FE");
							MainMenuController.jackpotForm
									.setJackpotTypeId(ILookupTableConstants.JACKPOT_TYPE_CANCEL_CREDIT);
							form.setNormalYes(false);
							form.setProgressiveYes(false);
							form.setCanceledCreditsYes(true);
						} else {
							MainMenuController.jackpotForm.setJackpotType(LabelLoader
									.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));// ("Normal");
							MainMenuController.jackpotForm.setJackpotID("FC");
							MainMenuController.jackpotForm
									.setJackpotTypeId(ILookupTableConstants.JACKPOT_TYPE_REGULAR);
							form.setNormalYes(true);
							form.setProgressiveYes(false);
							form.setCanceledCreditsYes(false);
							manualJpScreen2HPComposite.getTxtProgressiveLevel().setEnabled(false);
						}
						if(log.isInfoEnabled()) {
							log.info("The selected JackpotType is " + MainMenuController.jackpotForm.getJackpotType());
						}
						populateForm(manualJpScreen2HPComposite);
					}
				}
			} catch (Exception ex) {
				log.error(ex);
				ex.printStackTrace();
			}

		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
