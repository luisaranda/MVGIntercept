/*****************************************************************************
 * $Id: AuthorizationTaxMPAmountController.java,v 1.41.1.1.4.5, 2013-10-17 13:07:15Z, SDS12.3.3 Checkin User$
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
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.UserDTO;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ObjectMapping;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpot.dto.JackpotEngineAlertObject;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.composite.AuthorizationTaxMPAmountComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ILookupTableConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
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
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.JackpotUIUtil;
import com.ballydev.sds.jackpotui.util.PendingJackpotProcess;
import com.ballydev.sds.jackpotui.util.UserFunctionsUtil;

/**
 * Controller Class for the Authorization Composite
 * 
 * @author anantharajr
 * @version $Revision: 50$
 */
public class AuthorizationTaxMPAmountController extends SDSBaseController {

	/**
	 * AuthorizationForm Instance
	 */
	public static AuthorizationTaxMPAmountForm form;

	/**
	 * AuthorizationComposite Instance
	 */
	private AuthorizationTaxMPAmountComposite authorizationComposite;

	/**
	 * Flag that is set if the alert should be sent for the first auth emp
	 */
	private boolean sendAlertAuthSameEmpOne = false;

	/**
	 * Flag that is set if the alert should be sent for the second auth emp
	 */
	private boolean sendAlertAuthSameEmpTwo = false;

	/**
	 * JackpotEngineAlertObject for First auth emp
	 */
	private JackpotEngineAlertObject jpAlertAuthEmpOne = new JackpotEngineAlertObject();

	/**
	 * JackpotEngineAlertObject for Second auth emp
	 */
	private JackpotEngineAlertObject jpAlertAuthEmpTwo = new JackpotEngineAlertObject();

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
	.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * AuthorizationController constructor
	 * 
	 * @param parent
	 * @param style
	 * @param form
	 * @param validator
	 * @throws Exception
	 */
	public AuthorizationTaxMPAmountController(Composite parent, int style,
			AuthorizationTaxMPAmountForm form, SDSValidator validator)
	throws Exception {
		super(form, validator);		
		this.form = form;
		CbctlUtil.displayMandatoryLabel(true);	
		createCompositeOnSiteConfigParam(parent, style);

		if(!MainMenuController.jackpotForm.isDisplayMaskAmt()){
			if (MainMenuController.jackpotForm.getRoundedHPJPAmount() != 0) {
				authorizationComposite
				.getSlipStaticInfoComposite()
				.getLblAdjustedAmountLbl()
				.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
						+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
								.centsToDollar(MainMenuController.jackpotForm
										.getRoundedHPJPAmount())));
			} else {
				authorizationComposite
				.getSlipStaticInfoComposite()
				.getLblAdjustedAmountLbl()
				.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
						+ JackpotUIUtil.getFormattedAmounts(ConversionUtil
								.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount())));
			}
		}					
		//parent.layout();		
		super.registerEvents(authorizationComposite);
		form.addPropertyChangeListener(this);
		setButtonSelection();
		populateScreen(authorizationComposite);
		setPrevSelectedBtnGrp();		
		FocusUtility.setTextFocus(authorizationComposite);
		FocusUtility.focus = false;
		registerCustomizedListeners(authorizationComposite);
	}

	/*
	 * Method to change images of check boxes
	 */
	public void setButtonSelection() {
		if(authorizationComposite.getTaxComposite() != null){	
			if(authorizationComposite.getTaxComposite().getStateTaxCheckBox() != null){
				if(form.getStateTax()) {			
					authorizationComposite.getTaxComposite().getStateTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					authorizationComposite.getTaxComposite().getStateTaxCheckBox().setSelected(true);
				}else{			
					authorizationComposite.getTaxComposite().getStateTaxCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					authorizationComposite.getTaxComposite().getStateTaxCheckBox().setSelected(false);
				}
			}
			if(authorizationComposite.getTaxComposite().getFederalTaxCheckBox() != null){
				if(form.getFederalTax()) {
					authorizationComposite.getTaxComposite().getFederalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					authorizationComposite.getTaxComposite().getFederalTaxCheckBox().setSelected(true);
				}else{
					authorizationComposite.getTaxComposite().getFederalTaxCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					authorizationComposite.getTaxComposite().getFederalTaxCheckBox().setSelected(false);
				}
			}
			if(authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox() != null) {
				if(form.getMunicipalTax()) {
					authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().setSelected(true);
				}else {
					authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().setSelected(false);
				}
			}
			if(authorizationComposite.getTaxComposite().getInterceptCheckBox() != null){
				if(form.getIntercept()) {			
					authorizationComposite.getTaxComposite().getInterceptCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
					authorizationComposite.getTaxComposite().getInterceptCheckBox().setSelected(true);	
					authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEditable(true);
					authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEnabled(true);
				}else{			
					authorizationComposite.getTaxComposite().getInterceptCheckBox().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
					authorizationComposite.getTaxComposite().getInterceptCheckBox().setSelected(false);
					authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEditable(false);
					authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEnabled(false);
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
		AuthorizationTaxMPMouseListener listener = new AuthorizationTaxMPMouseListener();
		authorizationComposite.getButtonComposite().getBtnNext().addMouseListener(listener);
		authorizationComposite.getButtonComposite().getBtnNext().getTextLabel().addTraverseListener(this);
		authorizationComposite.getButtonComposite().getBtnPrevious().addMouseListener(listener);
		authorizationComposite.getButtonComposite().getBtnPrevious().getTextLabel().addTraverseListener(this);
		authorizationComposite.getButtonComposite().getBtnCancel().addMouseListener(listener);
		authorizationComposite.getButtonComposite().getBtnCancel().getTextLabel().addTraverseListener(this);
		
		if(authorizationComposite.getTaxComposite() != null){
			if(authorizationComposite.getTaxComposite().getStateTaxCheckBox() != null){
				authorizationComposite.getTaxComposite().getStateTaxCheckBox().addMouseListener(listener);
				authorizationComposite.getTaxComposite().getStateTaxCheckBox().addTraverseListener(this);
			}
			if(authorizationComposite.getTaxComposite().getFederalTaxCheckBox() != null){
				authorizationComposite.getTaxComposite().getFederalTaxCheckBox().addMouseListener(listener);
				authorizationComposite.getTaxComposite().getFederalTaxCheckBox().addTraverseListener(this);
			}
			if(authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox() != null){
				authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().addMouseListener(listener);
				authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().addTraverseListener(this);
			}
			if(authorizationComposite.getTaxComposite().getInterceptCheckBox() != null){
				authorizationComposite.getTaxComposite().getInterceptCheckBox().addMouseListener(listener);
				authorizationComposite.getTaxComposite().getInterceptCheckBox().addTraverseListener(this);
			}
		}
	}

	@Override
	public Composite getComposite() {

		return null;
	}

	/**
	 * @param parent
	 * @param style
	 */
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
		authorizationComposite = new AuthorizationTaxMPAmountComposite(p, s,
				paramFlags);
	}

	/**
	 * @return
	 */
	public boolean[] getSiteConfigurationParameters() {

		boolean[] paramFlags = new boolean[3];

		/*if (MainMenuController.jackpotSiteConfigParams
				.get(
						ISiteConfigConstants.PENDING_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS)
				.equalsIgnoreCase("yes")
				&& new Long(
						MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 1
				&& MainMenuController.jackpotForm.getAuthorizationRequired()
				&& !MainMenuController.jackpotForm.isExpressSuccess()) {
			paramFlags[0] = true;
		} else */

		if (MainMenuController.jackpotForm.getAuthorizationRequired()
				&& (!MainMenuController.jackpotForm.isExpressSuccess()
						|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq())) {//FIX FOR CR 99486
			paramFlags[0] = true;
		}
		if (new Long(MainMenuController.jackpotSiteConfigParams
				.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2
				&& MainMenuController.jackpotForm.getAuthorizationRequired()
				&& (!MainMenuController.jackpotForm.isExpressSuccess()
						|| MainMenuController.jackpotForm.isEmpJobJpLimitExceededAuthReq())) {

			paramFlags[1] = true;
		}
		/*if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.ALLOW_MACHINE_PAY_ON_JP_SLIPS)
				.equalsIgnoreCase("yes")) {
			paramFlags[2] = true;
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
			MainMenuController.jackpotForm.setTaxType(LabelKeyConstants.NO_TAX_LABEL);
			paramFlags[2] = true;
		}
		return paramFlags;
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
	 * method to calculate the tax
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




		// forming the tax string 
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

	/**
	 * Method to set the previous selected button in the TAX TYPE group    
	 */
	public void setPrevSelectedBtnGrp(){

		if(authorizationComposite.getTaxComposite() != null  && !authorizationComposite.getTaxComposite().isDisposed()){
			if(form.getStateTax()){
				authorizationComposite.getTaxComposite().getStateTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			}
			if(form.getFederalTax()){
				authorizationComposite.getTaxComposite().getFederalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			}
			if(form.getMunicipalTax()){
				authorizationComposite.getTaxComposite().getMunicipalTaxCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			}
			if(form.getIntercept()){
				authorizationComposite.getTaxComposite().getInterceptCheckBox().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(ImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
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
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.MUNICIPAL_TAX_CHECKBOX)) {
				form.setMunicipalTax(true);
			} else if(selectedButton.getName().equalsIgnoreCase(IAppConstants.INTERCEPT_CHECKBOX)) {
				form.setIntercept(true);
				authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEditable(true);
				authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEnabled(true);
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
				authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEditable(false);
				authorizationComposite.getTaxComposite().getTxtInterceptAmount().setEnabled(false);
				authorizationComposite.getTaxComposite().getTxtInterceptAmount().setText("");
			}
		}
	}



	private class AuthorizationTaxMPMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {


		}

		public void mouseDown(MouseEvent e) {
			try {
				Control control = (Control) e.getSource();

				if (!(control instanceof SDSTSCheckBox) && !(control instanceof SDSImageLabel)){

					return;
				}

				if (control instanceof SDSImageLabel) {

					if (((SDSImageLabel) control).getName().equalsIgnoreCase(
							"cancel")) {
						boolean response= false;				
						response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_JACKPOT_PROCESS), authorizationComposite);
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
						
						log.info("************ After next button is clicked:**********");
						populateForm(authorizationComposite);
						boolean validate = validate("AuthorizationTaxMPAmountForm",
								form, authorizationComposite);
						log.debug("---------Validate Result: " + validate);
						if (!validate) {
							log
							.debug("Validation of authorizationComposite failed");
							return;
						}
						SessionUtility sessionUtility = new SessionUtility();

						if (new Long(
								MainMenuController.jackpotSiteConfigParams
								.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) > 0
								&& MainMenuController.jackpotForm.getAuthorizationRequired()) {

							/*if (form.getAuthPasswordOne().length() < 5) {
								MessageDialogUtil
										.displayTouchScreenErrorMsgDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
								return;
							} else {*/
							ProgressIndicatorUtil.openInProgressWindow();

							try {
								log.info("Called framework's authenticate user method");
								UserDTO userDtoEmpIdChk = sessionUtility.authenticateUser(form
										.getAuthEmployeeIdOne(), form
										.getAuthPasswordOne(),
										MainMenuController.jackpotForm
										.getSiteId());
								if (userDtoEmpIdChk.isErrorPresent() && userDtoEmpIdChk.getMessageKey() != null && 
										(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpIdChk.getMessageKey()))
										|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpIdChk.getMessageKey())){
									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
									authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
									return;
								}
								else if (userDtoEmpIdChk.isErrorPresent() && userDtoEmpIdChk.getMessageKey() != null && 
										IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpIdChk.getMessageKey())){

									ProgressIndicatorUtil.closeInProgressWindow();
									MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
									authorizationComposite.getTxtAuthPasswordOne().setText("");
									authorizationComposite.getTxtAuthPasswordOne().forceFocus();
									return;
								}								
								else {

									if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("yes") && 
											UserFunctionsUtil.isJackpotOverrideAuthority(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")){
										MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_OVERRIDE_AUTHORITY_REQUIRED));
										authorizationComposite.getTxtAuthPasswordOne().setText("");
										authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
										log.info("JackpotOverrideAuthority for AuthEmployeeIdOne is required");
										return;
									}
									//if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("no")){

									if(!MainMenuController.jackpotForm.isProcessAsExpress()){

										if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_THE_JACKPOT_INITIATOR_ALLOWED_TO_AUTHORIZE_A_SLIP).equalsIgnoreCase("yes")
												&& MainMenuController.jackpotForm.getSlotAttentantId()!=null
												&& MainMenuController.jackpotForm.getSlotAttentantId().equalsIgnoreCase(StringUtil.trimAcnfNo(form.getAuthEmployeeIdOne()))
												&& UserFunctionsUtil.isOnePersonAllowedToAuthorize(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")){
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_ONE_PERSON_AUTHORITY_REQUIRED));
											authorizationComposite.getTxtAuthPasswordOne().setText("");
											authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
											log.info("JACKPOT_ONE_PERSON_AUTHORITY_REQUIRED for AuthEmployeeIdOne is required");
											return;

										}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_THE_JACKPOT_INITIATOR_ALLOWED_TO_AUTHORIZE_A_SLIP).equalsIgnoreCase("yes")
												&& MainMenuController.jackpotForm.getSlotAttentantId()!=null
												&& MainMenuController.jackpotForm.getSlotAttentantId().equalsIgnoreCase(StringUtil.trimAcnfNo(form.getAuthEmployeeIdOne()))
												&& UserFunctionsUtil.isOnePersonAllowedToAuthorize(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("yes")){

											/** 
											 * Send a message to the ALERTS ENGINE "Authorised by the same employee" 
											 * 
											 * AUTHORIZED BY THE SAME EMPLOYEE (301) 
											 * 
											 */
											log.info("Authorised by the same employee for AuthEmpOne");
											char exceptionCode = 100;
											jpAlertAuthEmpOne.setEmployeeId(form.getAuthEmployeeIdOne());

											jpAlertAuthEmpOne.setAssetConfigNumber(MainMenuController.jackpotForm.getSlotNo());
											jpAlertAuthEmpOne.setStandNumber(MainMenuController.jackpotForm.getSlotLocationNo());
											jpAlertAuthEmpOne.setSendingEngineName(IAppConstants.MODULE_NAME);
											jpAlertAuthEmpOne.setTerminalMessageNumber(IAppConstants.ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE);
											jpAlertAuthEmpOne.setTerminalMessage("AUTHORIZED BY THE SAME EMPLOYEE");
											jpAlertAuthEmpOne.setExceptionCode(exceptionCode);
											jpAlertAuthEmpOne.setJackpotAmount(MainMenuController.jackpotForm.getHandPaidAmount());
											jpAlertAuthEmpOne.setSiteId(MainMenuController.jackpotForm.getSiteId());
											sendAlertAuthSameEmpOne = true;
										}
										else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_THE_JACKPOT_INITIATOR_ALLOWED_TO_AUTHORIZE_A_SLIP).equalsIgnoreCase("no")
												&& MainMenuController.jackpotForm.getSlotAttentantId()!=null
												&& MainMenuController.jackpotForm.getSlotAttentantId().equalsIgnoreCase(StringUtil.trimAcnfNo(form.getAuthEmployeeIdOne()))){

											log.info("-------------- OVERRIDE_PERSON_MUST_BE_DIFFERENT auth emp one");
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.OVERRIDE_PERSON_MUST_BE_DIFFERENT));
											authorizationComposite.getTxtAuthPasswordOne().setText("");
											authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
											log.info("OVERRIDE_PERSON_MUST_BE_DIFFERENT for AuthEmployeeIdOne is required");
											return;
										}
									}
									if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {
										double empJackpotAmount =	UserFunctionsUtil.getJackpotAmount(form.getAuthEmployeeIdOne(), MainMenuController.jackpotForm.getSiteId());
										log.info("The JACKPOT AMOUNT is"+empJackpotAmount);
										if (ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()) > empJackpotAmount) {
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
											authorizationComposite.getTxtAuthPasswordOne().setText("");
											authorizationComposite.getTxtAuthEmployeeIdOne().forceFocus();
											log.info("Hp Amt exceeds jackpot limit for AuthEmployeeIdOne");
											return;
										}					
									}									
									MainMenuController.jackpotForm.setAuthEmployeeIdOne(form.getAuthEmployeeIdOne());
								}
							} catch (Exception e1) {
								JackpotExceptionUtil.getGeneralCtrllerException	(e1);	
								log.error(e1);
								return;
							}
							finally{
								ProgressIndicatorUtil.closeInProgressWindow();
							}
							//}
							if (new Long(
									MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2) {
								/*	if (form.getAuthPasswordTwo().length() < 5) {
									MessageDialogUtil
											.displayTouchScreenErrorMsgDialog(LabelLoader
													.getLabelValue(MessageKeyConstants.PASSWORD_MINIMUM_LENGTH));
									authorizationComposite.getTxtAuthPasswordTwo()
											.forceFocus();
									return;
								} else {*/
								ProgressIndicatorUtil.openInProgressWindow();

								try {
									log.info("Called framework's authenticate user method");
									UserDTO userDtoEmpIdChk = sessionUtility.authenticateUser(form.getAuthEmployeeIdTwo(), form.getAuthPasswordTwo(),
											MainMenuController.jackpotForm.getSiteId());									
									if (userDtoEmpIdChk.isErrorPresent() && userDtoEmpIdChk.getMessageKey() != null && 
											(IAppConstants.ANA_MESSAGES_INVALID_USERNAME_KEY.equals(userDtoEmpIdChk.getMessageKey())
													|| IAppConstants.ANA_MESSAGES_INVALID_USERNAME_FOR_SITE.equals(userDtoEmpIdChk.getMessageKey()))){
										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_ID));
										authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
										return;
									}
									else if (userDtoEmpIdChk.isErrorPresent() && userDtoEmpIdChk.getMessageKey() != null && 
											IAppConstants.ANA_MESSAGES_INVALID_PASSWORD_KEY.equals(userDtoEmpIdChk.getMessageKey())){

										ProgressIndicatorUtil.closeInProgressWindow();
										MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.INVALID_EMPLOYEE_PASSWORD));
										authorizationComposite.getTxtAuthPasswordTwo().setText("");
										authorizationComposite.getTxtAuthPasswordTwo().forceFocus();
										return;
									}
									else {
										if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("yes") && 
												UserFunctionsUtil.isJackpotOverrideAuthority(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")){
											MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_OVERRIDE_AUTHORITY_REQUIRED));
											authorizationComposite.getTxtAuthPasswordTwo().setText("");
											authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
											log.info("JackpotOverrideAuthority for AuthEmployeeIdTwo is required");
											return;
										}
										//if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED).equalsIgnoreCase("no")){
										if(!MainMenuController.jackpotForm.isProcessAsExpress()){

											log.info("!MainMenuController.jackpotForm.isProcessAsExpress() for Auth 2");

											if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_THE_JACKPOT_INITIATOR_ALLOWED_TO_AUTHORIZE_A_SLIP).equalsIgnoreCase("yes")
													&& MainMenuController.jackpotForm.getSlotAttentantId()!=null
													&& MainMenuController.jackpotForm.getSlotAttentantId().equalsIgnoreCase(StringUtil.trimAcnfNo(form.getAuthEmployeeIdTwo()))
													&& UserFunctionsUtil.isOnePersonAllowedToAuthorize(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")){
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JACKPOT_ONE_PERSON_AUTHORITY_REQUIRED));
												authorizationComposite.getTxtAuthPasswordTwo().setText("");
												authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
												log.info("JACKPOT_ONE_PERSON_AUTHORITY_REQUIRED for AuthEmployeeIdTwo is required");
												return;
											}else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_THE_JACKPOT_INITIATOR_ALLOWED_TO_AUTHORIZE_A_SLIP).equalsIgnoreCase("yes")
													&& MainMenuController.jackpotForm.getSlotAttentantId()!=null
													&& MainMenuController.jackpotForm.getSlotAttentantId().equalsIgnoreCase(StringUtil.trimAcnfNo(form.getAuthEmployeeIdTwo()))
													&& UserFunctionsUtil.isOnePersonAllowedToAuthorize(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("yes")){

												/** 
												 * Send a message to the ALERTS ENGINE "Authorised by the same employee" 
												 * 
												 * AUTHORIZED BY THE SAME EMPLOYEE (301) 
												 * 
												 */
												log.info("Authorised by the same employee for AuthEmpTwo");
												char exceptionCode = 100;
												jpAlertAuthEmpTwo.setEmployeeId(form.getAuthEmployeeIdTwo());

												jpAlertAuthEmpTwo.setAssetConfigNumber(MainMenuController.jackpotForm.getSlotNo());
												jpAlertAuthEmpTwo.setStandNumber(MainMenuController.jackpotForm.getSlotLocationNo());
												jpAlertAuthEmpTwo.setSendingEngineName(IAppConstants.MODULE_NAME);
												jpAlertAuthEmpTwo.setTerminalMessageNumber(IAppConstants.ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE);
												jpAlertAuthEmpTwo.setTerminalMessage("AUTHORIZED BY THE SAME EMPLOYEE");
												jpAlertAuthEmpTwo.setExceptionCode(exceptionCode);
												jpAlertAuthEmpTwo.setJackpotAmount(MainMenuController.jackpotForm.getHandPaidAmount());
												jpAlertAuthEmpTwo.setSiteId(MainMenuController.jackpotForm.getSiteId());
												sendAlertAuthSameEmpTwo = true;														
											}
											else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.IS_THE_JACKPOT_INITIATOR_ALLOWED_TO_AUTHORIZE_A_SLIP).equalsIgnoreCase("no")
													&& MainMenuController.jackpotForm.getSlotAttentantId()!=null
													&& MainMenuController.jackpotForm.getSlotAttentantId().equalsIgnoreCase(StringUtil.trimAcnfNo(form.getAuthEmployeeIdTwo()))){
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.OVERRIDE_PERSON_MUST_BE_DIFFERENT));
												authorizationComposite.getTxtAuthPasswordTwo().setText("");
												authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
												log.info("OVERRIDE_PERSON_MUST_BE_DIFFERENT for AuthEmployeeIdOne is required");
												return;
											}
										}
										if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.JP_REQUIRES_VALID_HPJP_AMOUNT).equalsIgnoreCase("yes")) {
											double empJackpotAmount =	UserFunctionsUtil.getJackpotAmount(form.getAuthEmployeeIdTwo(), MainMenuController.jackpotForm.getSiteId());
											log.info("The JACKPOT AMOUNT is"+empJackpotAmount);
											if (ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()) > empJackpotAmount) {
												MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.HP_AMT_EXCEEDS_JP_LIMIT));
												authorizationComposite.getTxtAuthPasswordTwo().setText("");
												authorizationComposite.getTxtAuthEmployeeIdTwo().forceFocus();
												log.info("Hp Amt exceeds jackpot limit for AuthEmployeeIdTwo");
												return;
											}					
										}
										MainMenuController.jackpotForm.setAuthEmployeeIdTwo(form.getAuthEmployeeIdTwo());
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
								if (form.getAuthEmployeeIdOne() != null
										&& MainMenuController.jackpotForm.getEmployeeIdPrintedSlip().equalsIgnoreCase(form.getAuthEmployeeIdOne())){							

									MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(form.getAuthEmployeeIdOne()+ " "+LabelLoader
											.getLabelValue(MessageKeyConstants.EMP_ID_ONE_ALREADY_USED_FOR_TRANS));
									authorizationComposite.getTxtAuthPasswordOne().setText("");
									authorizationComposite.getTxtAuthEmployeeIdOne().setFocus();
									return;
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
										authorizationComposite.getTxtAuthEmployeeIdOne().setFocus();
										authorizationComposite.getTxtAuthPasswordOne().setText("");
										return;							
									}
								}
								if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.REQUIRE_SUPERVISORY_AUTHORITY)
										.equalsIgnoreCase("yes")) {
									if (UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdOne(),MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")) {		
										log.debug("SUPERVISORY_AUTHORITY_REQUIRED");
										MessageDialogUtil
										.displayTouchScreenInfoDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
										authorizationComposite.getTxtAuthEmployeeIdOne().setFocus();
										return;
									}
								}
							}

							if (new Long(
									MainMenuController.jackpotSiteConfigParams
									.get(ISiteConfigConstants.NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES)) == 2) {

								if (form.getAuthEmployeeIdTwo() != null
										&& MainMenuController.jackpotForm.getEmployeeIdPrintedSlip().equalsIgnoreCase(form.getAuthEmployeeIdTwo())){									
									MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(form.getAuthEmployeeIdTwo() +" "+LabelLoader
											.getLabelValue(MessageKeyConstants.EMP_ID_TWO_ALREADY_USED_FOR_TRANS));
									authorizationComposite.getTxtAuthPasswordTwo().setText("");
									authorizationComposite.getTxtAuthEmployeeIdTwo().setFocus();
									return;
								}
								if (form.getAuthEmployeeIdOne() != null
										&& form.getAuthEmployeeIdTwo() != null
										&& form.getAuthEmployeeIdOne().equalsIgnoreCase(form.getAuthEmployeeIdTwo())) {

									MessageDialogUtil
									.displayTouchScreenErrorMsgDialog(form.getAuthEmployeeIdTwo()+ " "+LabelLoader
											.getLabelValue(MessageKeyConstants.ALREADY_USED_FOR_THIS_TRANSACTION));
									authorizationComposite.getTxtAuthPasswordTwo()
									.setText("");
									authorizationComposite
									.getTxtAuthEmployeeIdTwo().setFocus();
									return;
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
										authorizationComposite.getTxtAuthEmployeeIdTwo().setFocus();
										authorizationComposite.getTxtAuthPasswordTwo().setText("");
										return;
									}
								}

								if (MainMenuController.jackpotSiteConfigParams.get(
										ISiteConfigConstants.REQUIRE_SUPERVISORY_AUTHORITY)
										.equalsIgnoreCase("yes")) {
									if (UserFunctionsUtil.isSupervisoryAuthority(form.getAuthEmployeeIdTwo(),MainMenuController.jackpotForm.getSiteId()).equalsIgnoreCase("no")) {		
										log.debug("SUPERVISORY_AUTHORITY_REQUIRED");
										MessageDialogUtil
										.displayTouchScreenInfoDialog(LabelLoader
												.getLabelValue(MessageKeyConstants.SUPERVISORY_AUTHORITY_REQUIRED));
										authorizationComposite.getTxtAuthEmployeeIdTwo().setFocus();
										return;
									}
								}

							}
						}
						MainMenuController.jackpotForm.setAuthEmployeeIdOne(form
								.getAuthEmployeeIdOne());
						MainMenuController.jackpotForm.setAuthEmployeeIdTwo(form
								.getAuthEmployeeIdTwo());

						/** METHOD TO CALCULATE TAX */

						calculateTax();
						try{
							log.info("send alerts for auths");
							if(sendAlertAuthSameEmpOne){
								log.info("Before calling sendAlert method for terminal msg no: "+IAppConstants.ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE+ " for auth emp one");
								ProgressIndicatorUtil.openInProgressWindow();
								JackpotServiceLocator.getService().sendAlert(jpAlertAuthEmpOne);
								ProgressIndicatorUtil.closeInProgressWindow();									
								log.info("After calling sendAlert method for terminal msg no: "+IAppConstants.ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE);
							}else if(sendAlertAuthSameEmpTwo){
								log.info("Before calling sendAlert method for terminal msg no: "+IAppConstants.ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE+ " for auth emp one");
								ProgressIndicatorUtil.openInProgressWindow();
								JackpotServiceLocator.getService().sendAlert(jpAlertAuthEmpTwo);
								ProgressIndicatorUtil.closeInProgressWindow();
								log.info("After calling sendAlert method for terminal msg no: "+IAppConstants.ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE+ " for auth emp two");
							}
						}catch (JackpotEngineServiceException e1) {
							ProgressIndicatorUtil.closeInProgressWindow();
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e1, e1.getMessage());
							log.error("Exception in sendAlert web method call",e1);
							return;
						}catch (Exception e2) {
							ProgressIndicatorUtil.closeInProgressWindow();
							JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
							handler.handleException(e2,MessageKeyConstants.SERVICE_DOWN);
							log.error("SERVICE_DOWN",e2);
							return;
						}

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
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite()
											.isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
							}
							new PendingJackpotDetailsController(
									TopMiddleController.jackpotMiddleComposite,
									SWT.NONE, new PendingJackpotDetailsForm(),
									new SDSValidator(getClass(), true));
						} else if (MainMenuController.jackpotForm
								.getSelectedJackpotFunction().equalsIgnoreCase(
								"Pending")) {
							/**
							 * If this is the last screen and it is for a Pending
							 * Jackpot
							 */
							PendingJackpotProcess processPendingObj = new PendingJackpotProcess();
							processPendingObj.processPending();
							if (TopMiddleController.getCurrentComposite() != null
									&& !(TopMiddleController.getCurrentComposite()
											.isDisposed())) {
								TopMiddleController.getCurrentComposite().dispose();
								CallInitialScreenUtil initialScreen = new CallInitialScreenUtil();
								initialScreen.callPendingJPFirstScreen();
							}
						}
					}else if (((SDSImageLabel) control).getName().equalsIgnoreCase("previous")) {

						log.debug("Previous button action clause");				
						if(MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.PREVIOUS_BUTTON_CONFIRM_MSG) , authorizationComposite)){
							JackpotUIUtil.disposeCurrentMiddleComposite();	
							AmountSlotAttendantIdForm prevScrForm = new AmountSlotAttendantIdForm();
							ObjectMapping objMapping = new ObjectMapping();
							objMapping.copyAlikeFields(AmountSlotAttendantIdController.form, prevScrForm);

							new AmountSlotAttendantIdController(TopMiddleController.jackpotMiddleComposite, SWT.NONE,
									prevScrForm, new SDSValidator(getClass(),true), true);

							return;				
						}
					}
				} else if( (SDSTSCheckBox) control instanceof SDSTSCheckBox){
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
							populateForm(authorizationComposite);
							//}
						}
					}
				}
			} catch (Exception ex) {
				log.error(ex);
			}

		}

		public void mouseUp(MouseEvent e) {


		}

	}


}
