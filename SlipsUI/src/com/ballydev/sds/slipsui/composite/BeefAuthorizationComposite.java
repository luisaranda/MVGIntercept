/*****************************************************************************
 * $Id: BeefAuthorizationComposite.java,v 1.8, 2010-02-28 06:35:08Z, Suganthi, Kaliamoorthy$
 * $Date: 2/28/2010 12:35:08 AM$
 * $Log$
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
package com.ballydev.sds.slipsui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.composite.TouchScreenMiddleHeaderComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.FormConstants;
import com.ballydev.sds.slipsui.constants.IFieldTextLimits;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;

/**
 * This Class contains the authorizing employees' name and passwords for a beef process
 * 
 * @author anantharajr
 * @version $Revision: 9$
 *
 */
public class BeefAuthorizationComposite extends TouchScreenBaseComposite {

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
	 * SlipStaticInfoComposite instance
	 */
	private SlipStaticInfoComposite slipStaticInfoComposite = null;
	
	private Composite beefAuthComposite = null;
	
	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;

	/**
	 * AuthorizationComposite Constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public BeefAuthorizationComposite(Composite parent, int style, boolean[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_SLIPS_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.BEEF_HEADING));
		this.siteParamterFlag = flags;
		if(Util.isSmallerResolution()){
			middleBodyWidth = ITSControlSizeConstants.S_MIDDLE_WIDTH;
			middleBodyHeight = ITSControlSizeConstants.S_MIDDLE_BODY_HEIGHT;
		}
		initialize("N,C");
		layout();
	}

	/** 
	 * Method to draw the Middle Composite for the Text boxes and Static Info Composite
	 */
	public void drawMiddleComposite() {
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.ENTER_DETAILS));		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.STATIC_INFO_SLIP_DETAILS));
				
		createHandPaidAmountChangeGroup(getMiddleComposite());		
		slipStaticInfoComposite = new SlipStaticInfoComposite(getMiddleComposite(), SWT.NONE);		
	}
		
	/**
	 * This method initializes AuthorizationComposite mainGroup
	 * 
	 */
	private void createHandPaidAmountChangeGroup(Composite middleComposite) {
		
		GridData gdEnterDetails = new GridData();
		gdEnterDetails.grabExcessVerticalSpace = false;
		gdEnterDetails.grabExcessHorizontalSpace = true;
		gdEnterDetails.verticalAlignment = GridData.FILL;
		gdEnterDetails.horizontalSpan = 2;
		gdEnterDetails.widthHint = middleBodyWidth;
		gdEnterDetails.heightHint = middleBodyHeight;
		gdEnterDetails.horizontalAlignment = GridData.BEGINNING;
			
		GridLayout glWinComp = new GridLayout();
		glWinComp.numColumns = 2;
		glWinComp.verticalSpacing = 10;
		glWinComp.marginWidth = 5;
		glWinComp.marginHeight = 5;
		glWinComp.horizontalSpacing = 5;		
		
		GridData gdLabel = new GridData();
		gdLabel.grabExcessVerticalSpace = false;
		gdLabel.verticalAlignment = GridData.CENTER;
		gdLabel.horizontalAlignment = GridData.BEGINNING;
		gdLabel.grabExcessHorizontalSpace = true;
		gdLabel.widthHint = 450;
		gdLabel.heightHint = 50;

		beefAuthComposite = new Composite(middleComposite, SWT.NONE);
		beefAuthComposite.setBackground(new Color(Display.getCurrent(), 221, 235, 244));
		beefAuthComposite.setLayoutData(gdEnterDetails);
		beefAuthComposite.setLayout(glWinComp);		
		
		if(siteParamterFlag[0] || siteParamterFlag[1])
		{
			lblAuthEmployeeIdOne = new CbctlMandatoryLabel(
					beefAuthComposite,
					SWT.WRAP ,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_ONE));
			
			txtAuthEmployeeIdOne = new SDSTSText(
					beefAuthComposite,
					SWT.BORDER,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_ONE),
							FormConstants.FORM_AUTH_EMP_ID_ONE);
			txtAuthEmployeeIdOne.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtAuthEmployeeIdOne.setLayoutData(getMiddleComposite().getGDForBodyText());

			lblAuthPasswordOne = new CbctlMandatoryLabel(
					beefAuthComposite,
					SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_ONE));
			
			txtAuthPasswordOne = new SDSTSText(
					beefAuthComposite,
					SWT.PASSWORD,
					LabelLoader
							.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_ONE),
							FormConstants.FORM_AUTH_EMP_PASS_ONE);
			txtAuthPasswordOne.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtAuthPasswordOne.setLayoutData(getMiddleComposite().getGDForBodyText());

			if(Util.isSmallerResolution()) {
				lblAuthEmployeeIdOne.setLayoutData(gdLabel);
				lblAuthPasswordOne.setLayoutData(gdLabel);
			}else {				
				lblAuthEmployeeIdOne.setLayoutData(getMiddleComposite().getGDForBodyLabel());
				lblAuthPasswordOne.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			}
			
			if (siteParamterFlag[1]) {
				lblAuthEmployeeIdTwo = new CbctlMandatoryLabel(
						beefAuthComposite,
						SWT.WRAP,
						LabelLoader
								.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_TWO));

				txtAuthEmployeeIdTwo = new SDSTSText(
						beefAuthComposite,
						SWT.NONE,
						LabelLoader
								.getLabelValue(LabelKeyConstants.AUTHORIZATION_EMPLOYEE_ID_TWO),
								FormConstants.FORM_AUTH_EMP_ID_TWO);
				txtAuthEmployeeIdTwo.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
				txtAuthEmployeeIdTwo.setLayoutData(getMiddleComposite().getGDForBodyText());

				lblAuthPasswordTwo = new CbctlMandatoryLabel(
						beefAuthComposite,
						SWT.WRAP ,
						LabelLoader
								.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_TWO));

				txtAuthPasswordTwo = new SDSTSText(
						beefAuthComposite,
						SWT.PASSWORD,
						LabelLoader
								.getLabelValue(LabelKeyConstants.AUTHORIZATION_PASSWORD_TWO),
								FormConstants.FORM_AUTH_EMP_PASS_TWO);
				txtAuthPasswordTwo.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
				txtAuthPasswordTwo.setLayoutData(getMiddleComposite().getGDForBodyText());
				
				if(Util.isSmallerResolution()) {					
					lblAuthEmployeeIdTwo.setLayoutData(gdLabel);
					lblAuthPasswordTwo.setLayoutData(gdLabel);
				}else {				
					lblAuthEmployeeIdTwo.setLayoutData(getMiddleComposite().getGDForBodyLabel());
					lblAuthPasswordTwo.setLayoutData(getMiddleComposite().getGDForBodyLabel());
				}				
			}			
		}			
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
	 * @return the beefAuthComposite
	 */
	public Composite getBeefAuthComposite() {
		return beefAuthComposite;
	}

	/**
	 * @param beefAuthComposite the beefAuthComposite to set
	 */
	public void setBeefAuthComposite(Composite beefAuthComposite) {
		this.beefAuthComposite = beefAuthComposite;
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

}  //  @jve:decl-index=0:visual-constraint="140,96"
