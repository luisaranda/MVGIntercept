/*****************************************************************************
 * $Id: ManualJPAuthMachineAmtTaxController.java,v 1.47.1.0.3.5, 2013-10-17 13:07:15Z, SDS12.3.3 Checkin User$
 * $Date: 10/17/2013 8:07:15 AM$
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpotui.composite.ManualJPAuthMachineAmtTaxComposite;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.form.ManualJPAuthMachineAmtTaxForm;
import com.ballydev.sds.jackpotui.form.ManualJPHandPaidAmtPromoJackpotTypeForm;
import com.ballydev.sds.jackpotui.form.ManualJPLastScreenForm;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.ManualJackpotProcess;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;




/**
 * Controller for ManualJPAuthMachineAmtTaxComposite
 * 
 * @author anantharajr
 * @version $Revision: 55$
 */
public class ManualJPAuthMachineAmtTaxController extends SDSBaseController {

	/**
	 * ManualJackpotScreen3AuthForm Instance
	 */
	public static ManualJPAuthMachineAmtTaxForm form;

	/**
	 * ManualJackpotScreen3AuthComposite Instance
	 */
	private ManualJPAuthMachineAmtTaxComposite manualJpScreen3AuthComp;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
	.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * ManualJPAuthorizationController constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public ManualJPAuthMachineAmtTaxController(Composite parent, int style,
			ManualJPAuthMachineAmtTaxForm form, SDSValidator validator)
	throws Exception {
		super(form, validator);
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);	
		createCompositeOnSiteConfigParam(parent, style);
		//parent.layout();
		/** Label controls to be set as visible for the static info display */
		setDataForManualStaticDisplay();
		super.registerEvents(manualJpScreen3AuthComp);
		form.addPropertyChangeListener(this);
		setButtonSelection();
		populateScreen(manualJpScreen3AuthComp);
		setPrevSelectedBtnGrp();
		FocusUtility.setTextFocus(manualJpScreen3AuthComp);
		FocusUtility.focus = false;
		registerCustomizedListeners(manualJpScreen3AuthComp);
	}
	/*
	 * Method to change images of check boxes
	 */
	public void setButtonSelection() {
		if(manualJpScreen3AuthComp.getTaxComposite() != null){	
			if(manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox() != null){
				if(form.getStateTax()) {			
					manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().setSelected(true);
				}else{			
					manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().setSelected(false);
				}
			}
			if(manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox() != null){
				if(form.getFederalTax()) {
					manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().setSelected(true);
				}else{
					manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().setSelected(false);
				}
			}
			if(manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox() != null) {
				if(form.getMunicipalTax()) {
					manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().setSelected(true);
				}else	 {
					manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().setSelected(false);
				}
			}
			if(manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox() != null) {
				if(form.getIntercept()) {
					manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().setSelected(true);
					manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEditable(true);
					manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEnabled(true);
					
				}else	 {
					manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().setSelected(false);
					manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEditable(false);
					manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEnabled(false);
					
				}
			}
		}
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
		ManualJPAuthMachineAmtTaxListener listener = new ManualJPAuthMachineAmtTaxListener();
		manualJpScreen3AuthComp.getButtonComposite().getBtnNext().addMouseListener(listener);
		manualJpScreen3AuthComp.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		manualJpScreen3AuthComp.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		manualJpScreen3AuthComp.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		manualJpScreen3AuthComp.getButtonComposite().getBtnCancel().addMouseListener(listener);
		manualJpScreen3AuthComp.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);

		if(manualJpScreen3AuthComp.getTaxComposite() != null){	
			if(manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox() != null){
				manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().addMouseListener(listener);
				manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().addTraverseListener(this);
			}
			if(manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox() != null){
				manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().addMouseListener(listener);
				manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().addTraverseListener(this);
			}
			if(manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox() != null){
				manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().addMouseListener(listener);
				manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().addTraverseListener(this);
			}
			if(manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox() != null){ 
				manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().addMouseListener(listener); 
				manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().addTraverseListener(this); 
			} 

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		return manualJpScreen3AuthComp;
	}

	/**
	 * This method calls the ManualJPmanualJpScreen3AuthComp based on siteconfig
	 * params.
	 * 
	 * @param parent
	 * @param style
	 */
	public void createCompositeOnSiteConfigParam(Composite parent, int style) {
		createManualJackpotScreen3AuthComposite(parent, style,
				getSiteConfigurationParameters());
		TopMiddleController.setCurrentComposite(manualJpScreen3AuthComp);
		log.debug("*************" + TopMiddleController.getCurrentComposite());
	}

	/**
	 * Method to create the ManualJPmanualJpScreen3AuthComp
	 * 
	 * @param p
	 * @param s
	 */
	public void createManualJackpotScreen3AuthComposite(Composite p, int s,
			String[] siteConfigParam) {
		manualJpScreen3AuthComp = new ManualJPAuthMachineAmtTaxComposite(p, s,
				siteConfigParam);
	}

	/**
	 * Method to get the siteconfig parameters.
	 * 
	 * @return
	 */
	public String[] getSiteConfigurationParameters() {

		String[] siteConfigParam = new String[6];
		if ((new Double(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())) 
		>= new Long(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS))
		&& new Long(MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) >=1)
				|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq()) {

			siteConfigParam[0] = "yes";
			if (new Long(
					MainMenuController.jackpotSiteConfigParams
					.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2) {
				siteConfigParam[1] = "yes";
			} else {
				siteConfigParam[1] = "no";
			}
		} else {
			siteConfigParam[0] = "no";
			siteConfigParam[1] = "no";
		}

		/*if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS)
				.equalsIgnoreCase("yes")) {
			siteConfigParam[2] = "yes";
		} else {
			siteConfigParam[2] = "no";
		}*/
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase(
				"yes")
				&& (new Double(MainMenuController.jackpotSiteConfigParams
						.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0 || new Double(
								MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) != 0 || new Double(
										MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) != 0)) {
			siteConfigParam[2] = "yes";
			MainMenuController.jackpotForm.setTaxType(LabelKeyConstants.NO_TAX_LABEL);
		} else {
			siteConfigParam[2] = "no";
			MainMenuController.jackpotForm.setTaxType(LabelKeyConstants.NO_TAX_LABEL);
		}
		/*if (new Long(MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) == 0) {
			siteConfigParam[2] = "yes";
		}
		if (new Long(MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) == 0) {
			siteConfigParam[3] = "yes";
		}*/
		return siteConfigParam;
	}

	/**
	 * Method to set the data for the Manual Jackpot static display
	 */
	public void setDataForManualStaticDisplay() {
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID).equalsIgnoreCase("yes")) {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblEmployeeIdInfo().setText(
					"" + MainMenuController.jackpotForm.getEmployeeIdPrintedSlip());
		}
		// manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblShiftInfo()
		//				.setText("" + MainMenuController.jackpotForm.getShift());

		if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Day")) {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Swing")) {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase("Graveyard")) {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
		}else {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblShiftInfo().setText(""+LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}

		manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblSlotInfo().setText(
				"" + StringUtil.trimAcnfNo(MainMenuController.jackpotForm.getSlotNo()));
		if (MainMenuController.jackpotForm.getSlotLocationNo() != null) {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblStandInfo().setText(
					"" + MainMenuController.jackpotForm.getSlotLocationNo().toUpperCase());
		}
		manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblHandPaidAmount().setVisible(true);
		manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblHandPaidAmountInfo().setVisible(true);

		if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
				&& MainMenuController.jackpotForm.getJackpotTypeId() == ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ROUND_PROGRESSIVE_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase(
						"yes")
						&& JackpotUIUtil.isRoundingAllowedForJPAmount(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
								.getHandPaidAmount()))) {
			if(log.isInfoEnabled()) {
				log.info("Rounding is done");
			}
			manualJpScreen3AuthComp
			.getSlipStaticInfoComposite()
			.getLblHandPaidAmountInfo()
			.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
							.centsToDollar(MainMenuController.jackpotForm
									.getRoundedHPJPAmount())));
			if(log.isInfoEnabled()) {
				log.info("The rounded HPJP amount is " + MainMenuController.jackpotForm.getRoundedHPJPAmount());
			}
		} else if (MainMenuController.jackpotForm.getJackpotTypeId() != 0
				&& MainMenuController.jackpotForm.getJackpotTypeId() != ILookupTableConstants.JACKPOT_TYPE_PROGRESSIVE
				&& MainMenuController.jackpotSiteConfigParams.get(
						ISiteConfigConstants.ROUND_JACKPOTS_TO_NEXT_DOLLAR).equalsIgnoreCase("yes")
						&& JackpotUIUtil.isRoundingAllowedForJPAmount(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
								.getHandPaidAmount()))
								&& JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()) != null
								&& MainMenuController.jackpotSiteConfigParams.get(
										JackpotUIUtil.getSiteConfigKeyDenom(MainMenuController.jackpotForm.getDenomination()))
										.equalsIgnoreCase("yes")) {

			manualJpScreen3AuthComp
			.getSlipStaticInfoComposite()
			.getLblHandPaidAmountInfo()
			.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
							.centsToDollar(MainMenuController.jackpotForm
									.getRoundedHPJPAmount())));
		} else {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblHandPaidAmountInfo().setText(
					MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ JackpotUIUtil.getFormattedAmounts(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())));
		}		

		manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblJackpotType().setVisible(true);
		manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblJackpotTypeInfo().setVisible(true);
		manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblJackpotTypeInfo().setText(
				"" + MainMenuController.jackpotForm.getJackpotType());
		// Showing the cardless account number info when Allow auto deposit to
		// eCash account flag is set to YES.
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("Yes")) {
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblAccountNumber().setVisible(true);
			manualJpScreen3AuthComp.getSlipStaticInfoComposite().getLblAccountNumberInfo().setVisible(true);
		}
		/*	if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.TAX_WITHHOLDING_ENABLED).equalsIgnoreCase(
				"yes")
				&& (new Long(MainMenuController.jackpotSiteConfigParams
						.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0 || new Long(
						MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) != 0)) {
			MainMenuController.jackpotForm.setTaxType(LabelLoader
					.getLabelValue(LabelKeyConstants.NO_TAX_LABEL));
		}*/
	}

	/**
	 * nmethod to calculate the tax
	 */
	private void calculateTax() {
		
		
		double taxableAmount = 0;
		double totalDeductions = 0;
		double handPaidAmount = 0;		
		double machinePaidAmount = 0;
		double jackpotNetAmount = 0;
		double stateTaxAmnt = 0;
		double fedTaxAmnt = 0;
		double municipalTaxAmnt = 0;
		double stateTaxRate = 0;
		double fedTaxRate = 0;
		double municipalTaxRate = 0;
		String taxString = "";
		double interceptAmount = 0;

		
	
		if(MainMenuController.jackpotForm.getRoundedHPJPAmount()!=0){
			handPaidAmount = ConversionUtil
			.centsToDollar(MainMenuController.jackpotForm
					.getRoundedHPJPAmount());
		}else{
			handPaidAmount = ConversionUtil
			.centsToDollar(MainMenuController.jackpotForm
					.getHandPaidAmount());
		}	

		// getting the individual tax rates from site configuration		
		if(form.getStateTax()){
			stateTaxRate =  new Double(
					MainMenuController.jackpotSiteConfigParams
					.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT));
		}
		if(form.getFederalTax()){
			fedTaxRate =  new Double(
					MainMenuController.jackpotSiteConfigParams
					.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT));
		}
		if(form.getMunicipalTax()){
			municipalTaxRate =  new Double(
					MainMenuController.jackpotSiteConfigParams
					.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT));
		}
		
		interceptAmount = new Double(form.getInterceptAmount());

		taxableAmount = handPaidAmount + machinePaidAmount;
		
		MainMenuController.jackpotForm.setInterceptAmount(ConversionUtil
				.dollarToCentsRtnLong(String.valueOf(interceptAmount)));


		// code to calculate tax based on the check boxes ticked
		if(form.getStateTax() || form.getFederalTax() || form.getMunicipalTax()){
			if(form.getStateTax()){
				stateTaxAmnt = ConversionUtil.roundHalfUpForTax(taxableAmount, stateTaxRate);
			}
			if(form.getFederalTax()){
				fedTaxAmnt = ConversionUtil.roundHalfUpForTax(taxableAmount, fedTaxRate);
			}
			if(form.getMunicipalTax()){
				municipalTaxAmnt = ConversionUtil.roundHalfUpForTax(taxableAmount, municipalTaxRate);
			}

			totalDeductions = stateTaxAmnt + fedTaxAmnt + municipalTaxAmnt + interceptAmount;
			jackpotNetAmount = taxableAmount - totalDeductions;

			MainMenuController.jackpotForm
			.setJackpotNetAmount(ConversionUtil
					.dollarToCentsRtnLong(String.valueOf(jackpotNetAmount)));
			MainMenuController.jackpotForm
			.setTaxDeductedAmount(ConversionUtil
					.dollarToCentsRtnLong(String.valueOf(totalDeductions)));
		}
		else {
			
			totalDeductions += interceptAmount;
			taxableAmount -= totalDeductions;
			
			MainMenuController.jackpotForm
			.setJackpotNetAmount(ConversionUtil
					.dollarToCentsRtnLong(String.valueOf(taxableAmount)));
			MainMenuController.jackpotForm
			.setTaxDeductedAmount(ConversionUtil
					.dollarToCentsRtnLong(String.valueOf(totalDeductions)));
		}

		// code to set tax type and tax id
		if(!form.getStateTax() && !form.getFederalTax() && !form.getMunicipalTax()){
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_NO);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.NO_TAX_LABEL));
		} else if(form.getStateTax() && !form.getFederalTax() && !form.getMunicipalTax()){
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_STATE);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.STATE_TAX_LABEL));
		} else if(!form.getStateTax() && form.getFederalTax() && !form.getMunicipalTax()) {
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_FEDERAL);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.FEDERAL_TAX_LABEL));
		} else if(!form.getStateTax() && !form.getFederalTax() && form.getMunicipalTax()) {
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_MUNICIPAL);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.MUNICIPAL_TAX_LABEL));
		} else if(form.getStateTax() && form.getFederalTax() && !form.getMunicipalTax()) {
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_FEDERAL_PLUS_STATE);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.STATE_PLUS_FEDERAL_TAX_LABEL));
		} else if(!form.getStateTax() && form.getFederalTax() && form.getMunicipalTax()) {
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_FEDERAL_PLUS_MUNICIPAL);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.FEDERAL_PLUS_MUNICIPAL_TAX_LABEL));
		} else if(form.getStateTax() && !form.getFederalTax() && form.getMunicipalTax()) {
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_STATE_PLUS_MUNICIPAL);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.STATE_PLUS_MUNICIPAL_TAX_LABEL));
		} else if(form.getStateTax() && form.getFederalTax() && form.getMunicipalTax()) {
			MainMenuController.jackpotForm.setTaxID(ILookupTableConstants.TAX_FEDERAL_PLUS_MUNICIPAL_PLUS_STATE);
			MainMenuController.jackpotForm.setTaxType(LabelLoader.getLabelValue(LabelKeyConstants.FEDERAL_PLUS_STATE_PLUS_MUNICIPAL_PLUS_TAX_LABEL));
		}




		// forming the tax string -ST:0.0^0.0|FT:0.0^0.0|MT:0.0^0.0
		taxString = "ST:"+stateTaxRate+"^"+stateTaxAmnt+"|"+"FT:"+fedTaxRate+"^"+fedTaxAmnt+"|"+"MT:"+municipalTaxRate+"^"+municipalTaxAmnt;
		if(log.isInfoEnabled()) {
			log.info("TAX String---------->"+taxString);
		}

		MainMenuController.jackpotForm.setTaxRateAmount(taxString);



		log.info("Intercept Amount " + interceptAmount + "\nTaxable amount " + taxableAmount + "\nstate tax "
				+ stateTaxRate + "\nfederal tax" + fedTaxRate
				+ "\ntotalDeductions " + totalDeductions + "\nNetAmount"
				+ jackpotNetAmount);
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
	 * Method to set the previous selected button in the TAX TYPE group    
	 */
	public void setPrevSelectedBtnGrp(){
			if(manualJpScreen3AuthComp.getTaxComposite() != null  && !manualJpScreen3AuthComp.getTaxComposite().isDisposed()){
				if(form.getStateTax()){
					manualJpScreen3AuthComp.getTaxComposite().getStateTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
				}
				else if(form.getFederalTax()){
					manualJpScreen3AuthComp.getTaxComposite().getFederalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
				}
				else if(form.getMunicipalTax()){
					manualJpScreen3AuthComp.getTaxComposite().getMunicipalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
				}
				else if(form.getIntercept()){
					manualJpScreen3AuthComp.getTaxComposite().getInterceptCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
				}
			}	
	}



	/**
	 * Method to set the SelectedButton
	 * @param selectedButton
	 */
	public void setSelectedButton(SDSTSCheckBox selectedButton) {
		if( !selectedButton.isSelected() ){
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			selectedButton.setSelected(true);	
			if( selectedButton.getName().equalsIgnoreCase(IAppConstants.STATE_TAX_CHECKBOX)) {
				form.setStateTax(true);				
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.FEDERAL_TAX_CHECKBOX)) {
				form.setFederalTax(true);
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.MUNICIPAL_TAX_CHECKBOX)) {
				form.setMunicipalTax(true);
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.INTERCEPT_CHECKBOX)) {
				form.setIntercept(true);
				manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEditable(true);
				manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEnabled(true);
			} 
		} else {			
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			selectedButton.setSelected(false);	
			if( selectedButton.getName().equalsIgnoreCase(IAppConstants.STATE_TAX_CHECKBOX)) {
				form.setStateTax(false);				
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.FEDERAL_TAX_CHECKBOX)) {
				form.setFederalTax(false);
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.MUNICIPAL_TAX_CHECKBOX)) {
				form.setMunicipalTax(false);
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.INTERCEPT_CHECKBOX)) {
				form.setIntercept(false);
				manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEditable(false);
				manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setEnabled(false);
				manualJpScreen3AuthComp.getTaxComposite().getTxtInterceptAmount().setText("");
			}			
		}
	}

	private class ManualJPAuthMachineAmtTaxListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseDown(MouseEvent e) {


			try {
				Control control = (Control) e.getSource();

				if (!(control instanceof TSButtonLabel) && !(control instanceof SDSImageLabel) && !(control instanceof SDSTSCheckBox)) {

					return;
				}
				if (control instanceof SDSImageLabel) {
					if (((SDSImageLabel) control).getName().equalsIgnoreCase("cancel")) {
						boolean response = false;
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader
								.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS),
								manualJpScreen3AuthComp);
						if (response) {
							log.info("Manual jackpot process is aborted");
							JackpotUIUtil.disposeCurrentMiddleComposite();
							CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
							initialScreen.callManualJPFirstScreen();
						}
					} 
					else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {

						log.debug("Previous button action clause");
						if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG) , manualJpScreen3AuthComp)){
							JackpotUIUtil.disposeCurrentMiddleComposite();
							ManualJPHandPaidAmtPromoJackpotTypeForm prevScrForm = new ManualJPHandPaidAmtPromoJackpotTypeForm();					
							ObjectMapping.copyAlikeFields(ManualJPHandPaidAmtPromoJackpotTypeController.form, prevScrForm);					
							new ManualJPHandPaidAmtPromoJackpotTypeController(TopMiddleController.jackpotMiddleComposite, SWT.NONE, 
									prevScrForm, new SDSValidator(getClass(),true));
							return;	
						}								
					} 
					else if (((SDSImageLabel) control).getName().equalsIgnoreCase(
					"next")) {
						populateForm(manualJpScreen3AuthComp);
						ArrayList<String> fieldNames = new ArrayList<String>();
						if ((new Double(ConversionUtil.centsToDollar(MainMenuController.jackpotForm
								.getHandPaidAmount())) >= new Long(MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS))
								&& new Long(MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) >= 1)
										|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq()){//FIX FOR CR 99486 NJ ENHANCEMENT

							fieldNames.add(FormConstants.FORM_AUTH_EMP_ID_ONE);
							fieldNames.add(FormConstants.FORM_AUTH_EMP_PASS_ONE);
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2) {
								fieldNames.add(FormConstants.FORM_AUTH_EMP_ID_TWO);
								fieldNames.add(FormConstants.FORM_AUTH_EMP_PASS_TWO);
							}
						}
						
						fieldNames.add(FormConstants.FORM_INTERCEPT_AMOUNT);
						
						/*if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS)
								.equalsIgnoreCase("yes")) {
							fieldNames.add(FormConstants.FORM_MACHINE_PAID_AMOUNT);
						}*/
						boolean validate = false;

						log.info("form values: " + form.getAuthEmployeeIdOne());
						log.info("form values: " + form.getAuthPasswordOne());
						// log.info("form values: "+
						// form.getMachinePaidAmount());
						log.info("field names size: " + fieldNames.size());
						if (fieldNames != null && fieldNames.size() != 0) {
							validate = validate("ManualJPAuthMachineAmtTaxForm", fieldNames, form,
									manualJpScreen3AuthComp);
							if (!validate) {
								log.info("Validation failed");
								return;
							}
						}

						SessionUtility sessionUtility = new SessionUtility();
						/*if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS).equalsIgnoreCase("yes")) {
							if(!ConversionUtil.isFractionsOfCentsNormal(form.getMachinePaidAmount())){
								log.info("Fractions of a cent is not allowed");
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.FRACTIONS_OF_CENT_NOT_ALLOWED));
								manualJpScreen3AuthComp.getTxtMachinePaidAmount().setFocus();
								return;						
							}
						}*/

						if ((new Double(ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())) >= new Long(MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS))
						&& new Long(MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) >=1)
								|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq()) {//FIX FOR CR 99486 NJ ENHANCEMENT

							/*if (form.getAuthPasswordOne().length() < 5) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
								manualJpScreen3AuthComp.getTxtAuthPasswordOne().forceFocus();
								return;
							}else{*/
							ProgressIndicatorUtil.openInProgressWindow();


							try {
								log.info("Called framework's authenticate user method");
								UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdOne(), form.getAuthPasswordOne(), MainMenuController.jackpotForm.getSiteId());
								if(userDtoEmpPasswordChk!=null){
									if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
													|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
										manualJpScreen3AuthComp.getTxtAuthEmployeeIdOne().forceFocus();
										return;
									}
									else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){

										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										manualJpScreen3AuthComp.getTxtAuthPasswordOne().setText("");
										manualJpScreen3AuthComp.getTxtAuthPasswordOne().forceFocus();
										return;
									}else{	
										if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {
											double empJackpotAmount =	UserFunctionsUtil.getJackpotAmount(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId());
											log.info("The JACKPOT AMOUNT is"+empJackpotAmount);
											if (ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()) > empJackpotAmount) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
												manualJpScreen3AuthComp.getTxtAuthPasswordOne().setText("");
												manualJpScreen3AuthComp.getTxtAuthEmployeeIdOne().forceFocus();
												log.info("Hp Amt exceeds jackpot limit for AuthEmployeeIdOne");
												return;
											}					
										}										
										MainMenuController.jackpotForm.setAuthEmployeeIdOne(form.getAuthEmployeeIdOne());
									}								
								}else{
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
									manualJpScreen3AuthComp.getTxtAuthPasswordOne().setText("");
									manualJpScreen3AuthComp.getTxtAuthPasswordOne().forceFocus();								
									return;
								}
							} catch (Exception e1) {
								JackpotExceptionUtil.getGeneralCtrllerException	(e1);	
								log.error("Exception while checking the employee id with framework", e1);
								return;
							}
							finally{
								ProgressIndicatorUtil.closeInProgressWindow();
							}
							//}
							if (new Long(MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2) {

								/*if (form.getAuthPasswordTwo().length() < 5) {
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
									manualJpScreen3AuthComp.getTxtAuthPasswordTwo()
											.forceFocus();
									return;
								}else{*/
								ProgressIndicatorUtil.openInProgressWindow();							
								try {
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpPasswordChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdTwo(), form.getAuthPasswordTwo(), MainMenuController.jackpotForm.getSiteId());
									if(userDtoEmpPasswordChk!=null){
										if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpPasswordChk.getMessageKey())
														|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpPasswordChk.getMessageKey()))){
											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
											manualJpScreen3AuthComp.getTxtAuthEmployeeIdTwo().forceFocus();
											return;
										}
										else if (userDtoEmpPasswordChk.isErrorPresent() && userDtoEmpPasswordChk.getMessageKey() != null && 
												IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpPasswordChk.getMessageKey())){

											ProgressIndicatorUtil.closeInProgressWindow();
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
											manualJpScreen3AuthComp.getTxtAuthPasswordTwo().setText("");
											manualJpScreen3AuthComp.getTxtAuthPasswordTwo().forceFocus();
											return;
										}else{	
											if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {
												double empJackpotAmount =	UserFunctionsUtil.getJackpotAmount(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId());
												log.info("The JACKPOT AMOUNT is"+empJackpotAmount);
												if (ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()) > empJackpotAmount) {
													MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
													manualJpScreen3AuthComp.getTxtAuthPasswordTwo().setText("");
													manualJpScreen3AuthComp.getTxtAuthEmployeeIdTwo().forceFocus();
													log.info("Hp Amt exceeds jackpot limit for AuthEmployeeIdTwo");
													return;
												}					
											}
											MainMenuController.jackpotForm.setAuthEmployeeIdTwo(form.getAuthEmployeeIdTwo());
										}								
									}else{
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.USER_AUTHENTICATION_FAILED));
										manualJpScreen3AuthComp.getTxtAuthPasswordTwo().setText("");
										manualJpScreen3AuthComp.getTxtAuthPasswordTwo().forceFocus();										
										return;
									}
								} catch (Exception e1) {
									JackpotExceptionUtil.getGeneralCtrllerException	(e1);	
									log.error("Exception while checking the employee id with framework", e1);
									return;
								}
								finally{
									ProgressIndicatorUtil.closeInProgressWindow();
								}
								//}							
							}
							if (MainMenuController.jackpotForm
									.getEmployeeIdPrintedSlip() != null) {

								if (form.getAuthEmployeeIdOne()!=null && MainMenuController.jackpotForm.getEmployeeIdPrintedSlip().equalsIgnoreCase(form.getAuthEmployeeIdOne())){


									MessageDialogUtil.displayTouchScreenErrorMsgDialog(form.getAuthEmployeeIdOne() +" "+LabelLoader
											.getLabelValue(MessageKeyConstants.EMP_ID_ONE_ALREADY_USED_FOR_TRANS));
									manualJpScreen3AuthComp.getTxtAuthPasswordOne()
									.setText("");
									manualJpScreen3AuthComp.getTxtAuthEmployeeIdOne()
									.setFocus();
									return;
								}

								if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.REQUIRE_SUPERVISORY_AUTHORITY)
										.equalsIgnoreCase("yes")) {
									if (UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdOne(),MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")) {		
										log.debug("SUPERVISORY_AUTHORITY_REQUIRED");
										MessageDialogUtil
										.displayTouchScreenInfoDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
										manualJpScreen3AuthComp.getTxtAuthPasswordOne()
										.setText("");
										manualJpScreen3AuthComp.getTxtAuthEmployeeIdOne().setFocus();
										return;
									}
								}
								if(MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.ENABLE_OVERRIDE_AUTH_LEVELS)
										.equalsIgnoreCase("yes")){
									log.info("auth level of Emp id one: "+UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId()));
									log.info("auth level of emp print slip: "+UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), MainMenuController.jackpotForm.getSiteId()));
									if(UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId()) <= UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), MainMenuController.jackpotForm.getSiteId()))
									{	
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
										log.info("LOW_OVERRIDE_AUTH_LEVEL");
										manualJpScreen3AuthComp.getTxtAuthEmployeeIdOne().setFocus();
										manualJpScreen3AuthComp.getTxtAuthPasswordOne().setText("");
										return;
									}
								}				

								if (new Long(MainMenuController.jackpotSiteConfigParams
										.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2) {
									if ((form.getAuthEmployeeIdOne()
											.equalsIgnoreCase(form
													.getAuthEmployeeIdTwo()))) {
										MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(form.getAuthEmployeeIdTwo() +" "+LabelLoader
												.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
										manualJpScreen3AuthComp.getTxtAuthPasswordTwo()
										.setText("");
										manualJpScreen3AuthComp
										.getTxtAuthEmployeeIdTwo().setFocus();
										return;
									}

									if (form.getAuthEmployeeIdTwo()!=null && MainMenuController.jackpotForm.getEmployeeIdPrintedSlip().equalsIgnoreCase(form.getAuthEmployeeIdTwo())){							

										MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(form.getAuthEmployeeIdTwo() +" "+LabelLoader
												.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
										manualJpScreen3AuthComp.getTxtAuthPasswordTwo().setText("");
										manualJpScreen3AuthComp.getTxtAuthEmployeeIdTwo().setFocus();
										return;
									}							
									if (MainMenuController.jackpotSiteConfigParams.get(
											ISiteConfigConstants.REQUIRE_SUPERVISORY_AUTHORITY)
											.equalsIgnoreCase("yes")) {
										if (UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdTwo(),MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")) {		
											log.debug("SUPERVISORY_AUTHORITY_REQUIRED");
											MessageDialogUtil
											.displayTouchScreenInfoDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
											manualJpScreen3AuthComp.getTxtAuthPasswordTwo()
											.setText("");
											manualJpScreen3AuthComp.getTxtAuthEmployeeIdTwo().setFocus();
											return;
										}
									}
									if(MainMenuController.jackpotSiteConfigParams.get(
											ISiteConfigConstants.ENABLE_OVERRIDE_AUTH_LEVELS)
											.equalsIgnoreCase("yes")){
										log.info("auth level of Emp id two: "+UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId()));
										log.info("auth level of emp print slip: "+UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), MainMenuController.jackpotForm.getSiteId()));
										if(UserFunctionsUtil.getOverrideAuthLevel(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId()) <= UserFunctionsUtil.getOverrideAuthLevel(MainMenuController.jackpotForm.getEmployeeIdPrintedSlip(), MainMenuController.jackpotForm.getSiteId()))
										{								
											MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.LOW_OVERRIDE_AUTH_LEVEL));
											log.info("LOW_OVERRIDE_AUTH_LEVEL");
											manualJpScreen3AuthComp.getTxtAuthEmployeeIdTwo().setFocus();
											manualJpScreen3AuthComp.getTxtAuthPasswordTwo().setText("");
											return;
										}
									}
								}
							}
						}
						/*if (MainMenuController.jackpotSiteConfigParams.get(
								ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS)
								.equalsIgnoreCase("yes")) {
							MainMenuController.jackpotForm
									.setMachinePaidAmount(ConversionUtil.dollarToCents(Double
											.parseDouble(form
													.getMachinePaidAmount())));
						}*/

						/** This method is called to calculate the tax amount */
						calculateTax();

						if (MainMenuController.jackpotSiteConfigParams
								.get(
										ISiteConfigConstants.ALLOW_WINNING_COMB_ON_JP_SLIPS)
										.equalsIgnoreCase("yes")
										|| MainMenuController.jackpotSiteConfigParams
										.get(
												ISiteConfigConstants.ALLOW_PAYLINE_ON_JP_SLIPS)
												.equalsIgnoreCase("yes")
												|| MainMenuController.jackpotSiteConfigParams
												.get(
														ISiteConfigConstants.ALLOW_COINS_PLAYED_ON_JP_SLIPS)
														.equalsIgnoreCase("yes")
														|| MainMenuController.jackpotSiteConfigParams
														.get(
																ISiteConfigConstants.ALLOW_WINDOW_NO_ON_JP_SLIPS)
																.equalsIgnoreCase("yes")
																|| MainMenuController.jackpotSiteConfigParams
																.get(
																		ISiteConfigConstants.ALLOW_PLAYER_CARD_ON_JP_SLIPS)
																		.equalsIgnoreCase("yes")
																		|| MainMenuController.jackpotSiteConfigParams
																		.get(
																				ISiteConfigConstants.ALLOW_PLAYER_NAME_ON_JP_SLIPS)
																				.equalsIgnoreCase("yes")) {

							JackpotUIUtil.disposeCurrentMiddleComposite();
							new ManualJPLastScreenController(
									TopMiddleController.jackpotMiddleComposite,
									SWT.NONE, new ManualJPLastScreenForm(),
									new SDSValidator(getClass(),true));
						} else if (MainMenuController.jackpotForm
								.getSelectedJackpotFunction().equalsIgnoreCase(
								"Manual")) {
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
					}
				}else if(control instanceof SDSTSCheckBox){
					if (MainMenuController.jackpotSiteConfigParams.get(
							ISiteConfigConstants.TAX_WITHHOLDING_ENABLED)
							.equalsIgnoreCase("yes")
							&& (new Double(
									MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.STATE_TAX_RATE_FOR_JACKPOT)) != 0 || new Double(
											MainMenuController.jackpotSiteConfigParams
											.get(ISiteConfigConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT)) != 0 || new Double(
													MainMenuController.jackpotSiteConfigParams
													.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT)) != 0)) {
						if (((Control) e.getSource() instanceof SDSTSCheckBox)) {
							SDSTSCheckBox tSChkBox = (SDSTSCheckBox) ((Control)e.getSource());	
							setSelectedButton(tSChkBox);
							populateForm(manualJpScreen3AuthComp);
							//}
						}
					}
				}	
			}catch (Exception ex) {
				log.error(ex.getStackTrace());
				ex.printStackTrace();
			}


		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
