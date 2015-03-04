/*****************************************************************************
 * $Id: AuthorizationTaxMPAmountComposite.java,v 1.21.1.0.1.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
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
 * This Class contains the authorizing employees' name and passwords
 * 
 * @author anantharajr
 * @version $Revision: 24$
 */
public class AuthorizationTaxMPAmountComposite extends TouchScreenBaseComposite {
	
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

	/**
	 * Boolean array containing the site Parameter
	 * NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES
	 */
	private boolean[] siteParamterFlag;

	/**
	 * MachinePaidAmount text instance
	 */
	private SDSTSText txtMachinePaidAmount = null;

	/**
	 * No Tax TSRadio instance
	 */
	private TouchScreenRadioButton btnNoTax = null;

	/**
	 * State tax radio instance
	 */
	private TouchScreenRadioButton btnStateTax = null;

	/**
	 * Federal Tax TSradio instance
	 */
	private TouchScreenRadioButton btnFederalTax = null;

	/**
	 * State + Federal Tax TSradio instance
	 */
	private TouchScreenRadioButton btnStatePlusFederalTax = null;

	/**
	 * SlipStaticInfoComposite instance
	 */
	private SlipStaticInfoComposite slipStaticInfoComposite = null;
	
	private TouchScreenTaxComposite taxComposite = null;
	
	/**
	 * AuthorizationComposite Constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public AuthorizationTaxMPAmountComposite(Composite parent, int style,
			boolean[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.PENDING_JP_HEADING));
		
		this.siteParamterFlag = flags;
		initialize("P,N,C");
		layout();
	}

	public void drawTopComposite() {
		int rowCount = 1;
		
		if (siteParamterFlag[0]) {
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
			
		

		if (siteParamterFlag[1]) {
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

			CbctlLabel dummyLabel1 = new CbctlLabel(getTopComposite(), SWT.NONE);
			dummyLabel1.setLayoutData(getTopComposite().getGDForDummyLabel());
			
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
		if(!siteParamterFlag[2] || ( siteParamterFlag[2] && !enableStateTax && !enableFederalTax && !enableMunicipalTax)){
			getMiddleComposite().getGlMiddleCom().numColumns = 2;
			getMiddleComposite().setLayoutDataForMiddle();
		}
		if (siteParamterFlag[2] && (enableStateTax || enableFederalTax || enableMunicipalTax)) {
		  new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_JACKPOT_SELECT_TAX));
		}
		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_JACKPOT_DETAILS));
		if (siteParamterFlag[2] && (enableStateTax || enableFederalTax || enableMunicipalTax)) {
			taxComposite = new TouchScreenTaxComposite(getMiddleComposite(), SWT.NONE);
		}

		slipStaticInfoComposite = new SlipStaticInfoComposite(getMiddleComposite(), SWT.NONE);
		
		
		
	}

	/**
	 * @return the slipStaticInfoComposite
	 */
	public SlipStaticInfoComposite getSlipStaticInfoComposite() {
		return slipStaticInfoComposite;
	}

	/**
	 * @param slipStaticInfoComposite the slipStaticInfoComposite to set
	 */
	public void setSlipStaticInfoComposite(
			SlipStaticInfoComposite slipStaticInfoComposite) {
		this.slipStaticInfoComposite = slipStaticInfoComposite;
	}

	/**
	 * @return the txtMachinePaidAmount
	 */
	public SDSTSText getTxtMachinePaidAmount() {
		return txtMachinePaidAmount;
	}

	/**
	 * @param txtMachinePaidAmount the txtMachinePaidAmount to set
	 */
	public void setTxtMachinePaidAmount(SDSTSText txtMachinePaidAmount) {
		this.txtMachinePaidAmount = txtMachinePaidAmount;
	}

	/**
	 * @param btnFederalTax the btnFederalTax to set
	 */
	public void setBtnFederalTax(TouchScreenRadioButton btnFederalTax) {
		this.btnFederalTax = btnFederalTax;
	}

	/**
	 * @param btnNoTax the btnNoTax to set
	 */
	public void setBtnNoTax(TouchScreenRadioButton btnNoTax) {
		this.btnNoTax = btnNoTax;
	}

	/**
	 * @param btnStatePlusFederalTax the btnStatePlusFederalTax to set
	 */
	public void setBtnStatePlusFederalTax(
			TouchScreenRadioButton btnStatePlusFederalTax) {
		this.btnStatePlusFederalTax = btnStatePlusFederalTax;
	}

	/**
	 * @param btnStateTax the btnStateTax to set
	 */
	public void setBtnStateTax(TouchScreenRadioButton btnStateTax) {
		this.btnStateTax = btnStateTax;
	}

	
	/**
	 * @return the btnFederalTax
	 */
	public TouchScreenRadioButton getBtnFederalTax() {
		return btnFederalTax;
	}

	/**
	 * @return the btnNoTax
	 */
	public TouchScreenRadioButton getBtnNoTax() {
		return btnNoTax;
	}

	/**
	 * @return the btnStatePlusFederalTax
	 */
	public TouchScreenRadioButton getBtnStatePlusFederalTax() {
		return btnStatePlusFederalTax;
	}

	/**
	 * @return the btnStateTax
	 */
	public TouchScreenRadioButton getBtnStateTax() {
		return btnStateTax;
	}



	
	/**
	 * @return the lblAuthEmployeeIdOne
	 */
	public CbctlMandatoryLabel getLblAuthEmployeeIdOne() {
		return lblAuthEmployeeIdOne;
	}

	/**
	 * @param lblAuthEmployeeIdOne
	 *            the lblAuthEmployeeIdOne to set
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
	 * @param lblAuthEmployeeIdTwo
	 *            the lblAuthEmployeeIdTwo to set
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
	 * @param lblAuthPasswordOne
	 *            the lblAuthPasswordOne to set
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
	 * @param lblAuthPasswordTwo
	 *            the lblAuthPasswordTwo to set
	 */
	public void setLblAuthPasswordTwo(CbctlMandatoryLabel lblAuthPasswordTwo) {
		this.lblAuthPasswordTwo = lblAuthPasswordTwo;
	}

	/**
	 * @return the siteParamterFlag
	 */
	public boolean[] getSiteParamterFlag() {
		return siteParamterFlag;
	}

	/**
	 * @param siteParamterFlag
	 *            the siteParamterFlag to set
	 */
	public void setSiteParamterFlag(boolean[] siteParamterFlag) {
		this.siteParamterFlag = siteParamterFlag;
	}

	/**
	 * @return the txtAuthEmployeeIdOne
	 */
	public SDSTSText getTxtAuthEmployeeIdOne() {
		return txtAuthEmployeeIdOne;
	}

	/**
	 * @param txtAuthEmployeeIdOne
	 *            the txtAuthEmployeeIdOne to set
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
	 * @param txtAuthEmployeeIdTwo
	 *            the txtAuthEmployeeIdTwo to set
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
	 * @param txtAuthPasswordOne
	 *            the txtAuthPasswordOne to set
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
	 * @param txtAuthPasswordTwo
	 *            the txtAuthPasswordTwo to set
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

}  //  @jve:decl-index=0:visual-constraint="25,0"
