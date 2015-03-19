/*****************************************************************************
 * $Id: ManualJPAuthMachineAmtTaxComposite.java,v 1.20.1.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 4:01:12 AM$
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
package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.composite.TouchScreenMiddleHeaderComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.controls.TouchScreenRadioButton;

/**
 * Class contains the authorizing employees' name and passwords, and the tax type
 * 
 * @author dambereen
 * @version $Revision: 22$
 */
public class ManualJPAuthMachineAmtTaxComposite extends TouchScreenBaseComposite {
	
	/**
	 * Authorization EmployeeIdOne Label
	 */
	private CbctlMandatoryLabel lblAuthEmployeeIdOne = null;

	/**
	 * Authorization EmployeeIdOne Text
	 */
	private SDSTSText txtAuthEmployeeIdOne = null;

	/**
	 * Authorization PasswordOne Label
	 */
	private CbctlMandatoryLabel lblAuthPasswordOne = null;

	/**
	 * Authorization PasswordOne text
	 */
	private SDSTSText txtAuthPasswordOne = null;

	/**
	 * Authorization EmployeeIdTwo label
	 */
	private CbctlMandatoryLabel lblAuthEmployeeIdTwo = null;

	/**
	 * Authorization EmployeeIdTwo Button
	 */
	private SDSTSText txtAuthEmployeeIdTwo = null;

	/**
	 * Authorization PasswordTwo Label
	 */
	private CbctlMandatoryLabel lblAuthPasswordTwo = null;

	/**
	 * Authorization PasswordTwo Button
	 */
	private SDSTSText txtAuthPasswordTwo = null;

	private TouchScreenTaxComposite taxComposite = null;
	
	/**
	* MVG Custom Fields Intercept
	*/	
	private TouchScreenRadioButton btnIntercept = null;
	
	private SDSTSText txtInterceptAmount = null;
	
	

	/**
	 * SlipStaticInfoComposite instance
	 */
	private ManualSlipStaticInfoComposite slipStaticInfoComposite = null;  
	/**
	 * Boolean array containing the site Parameters
	 * NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES
	 * TAX_WITHHOLDING_ENABLED_PRECEDENCE
	 * STATE_TAX_RATE_FOR_JACKPOT_PRECEDENCE
	 * FEDERAL_TAX_RATE_FOR_JACKPOT_PRECEDENCE
	 */
	private String[] siteParameterFlag;
	
	
	
	/**
	 * AuthorizationComposite Constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public ManualJPAuthMachineAmtTaxComposite(Composite parent, int style, String[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.MANUAL_JP_HEADING));
		this.siteParameterFlag = flags;
		initialize("P,N,C");
		layout();
	}

	public void drawTopComposite() {
		
		System.out.println("Inside ManualJPAuthMachineAmtTaxComposite:Initialise");
		
		int rowCount = 1;
		
		if(siteParameterFlag[0].equalsIgnoreCase("yes"))
		{
			rowCount = rowCount + 2;
			lblAuthEmployeeIdOne = new CbctlMandatoryLabel(
					getTopComposite(),
					SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelLoader
									.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_ONE)));
			lblAuthEmployeeIdOne.setLayoutData(getTopComposite().getGDForLabel());
			
			txtAuthEmployeeIdOne = new SDSTSText(
					getTopComposite(),
					SWT.BORDER,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_ONE),
					"authEmployeeIdOne");
			txtAuthEmployeeIdOne.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtAuthEmployeeIdOne.setLayoutData(getTopComposite().getGDForText());
			
			CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
			dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());

			lblAuthPasswordOne = new CbctlMandatoryLabel(
					getTopComposite(),
					SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelLoader
									.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_ONE)));
			lblAuthPasswordOne.setLayoutData(getTopComposite().getGDForExtraLabel());
			
			txtAuthPasswordOne = new SDSTSText(
					getTopComposite(),
					SWT.PASSWORD,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_ONE),
					"authPasswordOne");
			txtAuthPasswordOne.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtAuthPasswordOne.setLayoutData(getTopComposite().getGDForText());

		}
		
		if(siteParameterFlag[1].equalsIgnoreCase("yes"))
		{
			++ rowCount;
			lblAuthEmployeeIdTwo = new CbctlMandatoryLabel(
					getTopComposite(),
					SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelLoader
									.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_TWO)));
			lblAuthEmployeeIdTwo.setLayoutData(getTopComposite().getGDForLabel());

			txtAuthEmployeeIdTwo = new SDSTSText(
					getTopComposite(),
					SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_TWO),
					"authEmployeeIdTwo");
			txtAuthEmployeeIdTwo.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtAuthEmployeeIdTwo.setLayoutData(getTopComposite().getGDForText());
			
			CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
			dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());

			lblAuthPasswordTwo = new CbctlMandatoryLabel(
					getTopComposite(),
					SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelLoader
									.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_TWO)));
			
			lblAuthPasswordTwo.setLayoutData(getTopComposite().getGDForExtraLabel());

			txtAuthPasswordTwo = new SDSTSText(
					getTopComposite(),
					SWT.PASSWORD,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_TWO),
					"authPasswordTwo");
			txtAuthPasswordTwo.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtAuthPasswordTwo.setLayoutData(getTopComposite().getGDForText());
		}
		
		txtInterceptAmount = new SDSTSText(
				getTopComposite(),
				SWT.NONE,
				LabelLoader.getLabelValue(LabelKeyConstants.INTERCEPT_AMOUNT),"interceptAmount");

		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
		
	}
	
	/**
	 * This method initializes handPaidAmountChangeGroup
	 * 
	 */
	public void drawMiddleComposite() {
		
		boolean enableStateTax = true;
		boolean enableFederalTax = true;
		boolean enableMunicipalTax = true;
		if(new Double(MainMenuController.jackpotSiteConfigParams.get(ILabelConstants.STATE_TAX_RATE_FOR_JACKPOT))==0)
		{
			enableStateTax = false;
		}
		if(new Double(MainMenuController.jackpotSiteConfigParams.get(ILabelConstants.FEDERAL_TAX_RATE_FOR_JAKCPOT))==0)
		{
			enableFederalTax = false;
		}
		if(new Double(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MUNICIPAL_TAX_RATE_FOR_JAKCPOT))==0)
		{
			enableMunicipalTax = false;
		}
		
		if(!(siteParameterFlag[2].equalsIgnoreCase("yes")) || ( siteParameterFlag[2].equalsIgnoreCase("yes") && !enableStateTax && !enableFederalTax && !enableMunicipalTax)){
			getMiddleComposite().getGlMiddleCom().numColumns = 2;
			getMiddleComposite().setLayoutDataForMiddle();
		}
		if(siteParameterFlag[2].equalsIgnoreCase("yes") && (enableStateTax || enableFederalTax || enableMunicipalTax)){
			new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_JACKPOT_SELECT_TAX));
		}
		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_JACKPOT_DETAILS));
		if(siteParameterFlag[2].equalsIgnoreCase("yes") && (enableStateTax || enableFederalTax || enableMunicipalTax))
		{
			taxComposite = new TouchScreenTaxComposite(getMiddleComposite(), SWT.NONE);
		}

		slipStaticInfoComposite = new ManualSlipStaticInfoComposite(getMiddleComposite(), SWT.NONE);
	}

	/**
	 * @return the lblAuthEmployeeIdOne
	 */
	public CbctlMandatoryLabel getLblAuthEmployeeIdOne() {
		return lblAuthEmployeeIdOne;
	}

	/**
	 * @param lblAuthEmployeeIdOne the lblAuthEmployeeIdOne to set
	 */
	public void setLblAuthEmployeeIdOne(CbctlMandatoryLabel lblAuthEmployeeIdOne) {
		this.lblAuthEmployeeIdOne = lblAuthEmployeeIdOne;
	}

	/**
	 * @return the lblAuthEmployeeIdTwo
	 */
	public CbctlMandatoryLabel getLblAuthEmployeeIdTwo() {
		return lblAuthEmployeeIdTwo;
	}

	/**
	 * @param lblAuthEmployeeIdTwo the lblAuthEmployeeIdTwo to set
	 */
	public void setLblAuthEmployeeIdTwo(CbctlMandatoryLabel lblAuthEmployeeIdTwo) {
		this.lblAuthEmployeeIdTwo = lblAuthEmployeeIdTwo;
	}

	/**
	 * @return the lblAuthPasswordOne
	 */
	public CbctlMandatoryLabel getLblAuthPasswordOne() {
		return lblAuthPasswordOne;
	}

	/**
	 * @param lblAuthPasswordOne the lblAuthPasswordOne to set
	 */
	public void setLblAuthPasswordOne(CbctlMandatoryLabel lblAuthPasswordOne) {
		this.lblAuthPasswordOne = lblAuthPasswordOne;
	}

	/**
	 * @return the lblAuthPasswordTwo
	 */
	public CbctlMandatoryLabel getLblAuthPasswordTwo() {
		return lblAuthPasswordTwo;
	}

	/**
	 * @param lblAuthPasswordTwo the lblAuthPasswordTwo to set
	 */
	public void setLblAuthPasswordTwo(CbctlMandatoryLabel lblAuthPasswordTwo) {
		this.lblAuthPasswordTwo = lblAuthPasswordTwo;
	}

	/**
	 * @return the siteParameterFlag
	 */
	public String[] getSiteParameterFlag() {
		return siteParameterFlag;
	}

	/**
	 * @param siteParameterFlag the siteParameterFlag to set
	 */
	public void setSiteParameterFlag(String[] siteParameterFlag) {
		this.siteParameterFlag = siteParameterFlag;
	}

	/**
	 * @return the slipStaticInfoComposite
	 */
	public ManualSlipStaticInfoComposite getSlipStaticInfoComposite() {
		return slipStaticInfoComposite;
	}

	/**
	 * @param slipStaticInfoComposite the slipStaticInfoComposite to set
	 */
	public void setSlipStaticInfoComposite(
			ManualSlipStaticInfoComposite slipStaticInfoComposite) {
		this.slipStaticInfoComposite = slipStaticInfoComposite;
	}

	/**
	 * @return the txtAuthEmployeeIdOne
	 */
	public SDSTSText getTxtAuthEmployeeIdOne() {
		return txtAuthEmployeeIdOne;
	}

	/**
	 * @param txtAuthEmployeeIdOne the txtAuthEmployeeIdOne to set
	 */
	public void setTxtAuthEmployeeIdOne(SDSTSText txtAuthEmployeeIdOne) {
		this.txtAuthEmployeeIdOne = txtAuthEmployeeIdOne;
	}

	/**
	 * @return the txtAuthEmployeeIdTwo
	 */
	public SDSTSText getTxtAuthEmployeeIdTwo() {
		return txtAuthEmployeeIdTwo;
	}

	/**
	 * @param txtAuthEmployeeIdTwo the txtAuthEmployeeIdTwo to set
	 */
	public void setTxtAuthEmployeeIdTwo(SDSTSText txtAuthEmployeeIdTwo) {
		this.txtAuthEmployeeIdTwo = txtAuthEmployeeIdTwo;
	}

	/**
	 * @return the txtAuthPasswordOne
	 */
	public SDSTSText getTxtAuthPasswordOne() {
		return txtAuthPasswordOne;
	}

	/**
	 * @param txtAuthPasswordOne the txtAuthPasswordOne to set
	 */
	public void setTxtAuthPasswordOne(SDSTSText txtAuthPasswordOne) {
		this.txtAuthPasswordOne = txtAuthPasswordOne;
	}

	/**
	 * @return the txtAuthPasswordTwo
	 */
	public SDSTSText getTxtAuthPasswordTwo() {
		return txtAuthPasswordTwo;
	}

	/**
	 * @param txtAuthPasswordTwo the txtAuthPasswordTwo to set
	 */
	public void setTxtAuthPasswordTwo(SDSTSText txtAuthPasswordTwo) {
		this.txtAuthPasswordTwo = txtAuthPasswordTwo;
	}

	public TouchScreenTaxComposite getTaxComposite() {
		return taxComposite;
	}

	public void setTaxComposite(TouchScreenTaxComposite taxComposite) {
		this.taxComposite = taxComposite;
	}
	
	/**
	* MVG Custom Fields Intercept
	*/	
	public void setBtnIntercept(TouchScreenRadioButton btnIntercept) {
		this.btnIntercept = btnIntercept;
	}
	
	public TouchScreenRadioButton getIntercept() {
		return btnIntercept;
	}
	
	public void setTxtInterceptAmount(SDSTSText txtInterceptAmount) {
		this.txtInterceptAmount = txtInterceptAmount;
	}
	
	public SDSTSText getTxtInterceptAmount() {
		return txtInterceptAmount;
	}

}  //  @jve:decl-index=0:visual-constraint="135,0"
