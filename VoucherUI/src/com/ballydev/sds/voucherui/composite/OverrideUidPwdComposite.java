/*****************************************************************************
 * $Id: OverrideUidPwdComposite.java,v 1.2, 2010-06-17 13:06:33Z, Verma, Nitin Kumar$
 * $Date: 6/17/2010 8:06:33 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * @author VNitinkumar
 */
public class OverrideUidPwdComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {

	/**
	 * Instance of the Auth composite
	 */
	private Composite authComposite;

	/**
	 * Instance of the user id label
	 */
	private SDSTSMandatoryLabel lblUserId;

	/**
	 * Instance of the password label
	 */
	private SDSTSMandatoryLabel lblPassword;

	/**
	 * Instance of the reason label
	 */
	private SDSTSMandatoryLabel lblReasonForOverride;

	/**
	 * Instance of the user id text
	 */
	private SDSTSText txtUserId;

	/**
	 * Instance of the password text
	 */
	private SDSTSText txtPassword;

	/**
	 * Instance of the reason text
	 */
	private SDSTSText  txtReasonForOverride;

	/**
	 * Instance of the submit Image Label
	 */
	private SDSImageLabel  iLblSubmit;

	/**
	 * Instance of the cancel Image Label
	 */
	private SDSImageLabel iLblCancel;

	/**
	 * Instance of the Image Label composite
	 */
	private Composite compBtn;

	private Image buttonBG;

	/**
	 * Constructor of the class
	 */
	public OverrideUidPwdComposite(Composite parent, int style){
		super(parent, style);
		createImages();
		initialize();
	}

	private void initialize(){
		GridLayout grlPage = new GridLayout();
		grlPage.horizontalSpacing = 0;
		grlPage.marginWidth = 0;
		grlPage.marginHeight = 0;
		grlPage.verticalSpacing = 0;
		createAuthComposite();
		setControlProperties();
		this.setLayout(grlPage);
		this.setBackground(SDSControlFactory.getTSBodyColor());
		if( !Util.isSmallerResolution() )
			setSize(new Point(500,210));
		else
			setSize(new Point(450,210));
	}

	public void createAuthComposite() {

		GridLayout grlAuthComposite = new GridLayout();
		grlAuthComposite.numColumns = 2;
		grlAuthComposite.verticalSpacing = 5;
		grlAuthComposite.marginWidth = 15;
		grlAuthComposite.marginHeight = 10;
		grlAuthComposite.horizontalSpacing = 25;

		GridData gdAuthComposite = new GridData();
		gdAuthComposite.grabExcessHorizontalSpace = true;
		gdAuthComposite.verticalAlignment = GridData.CENTER;
		gdAuthComposite.grabExcessVerticalSpace = true;
		gdAuthComposite.horizontalAlignment = GridData.CENTER;
		if( !Util.isSmallerResolution() )
			gdAuthComposite.widthHint = 500;
		else
			gdAuthComposite.widthHint = 450;
		
		authComposite = new Composite(this, SWT.NONE);
		authComposite.setLayoutData(gdAuthComposite);
		authComposite.setLayout(grlAuthComposite);
		authComposite.setBackground(SDSControlFactory.getTSBodyColor());

		lblUserId = new SDSTSMandatoryLabel(authComposite, SWT.END, LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
		lblUserId.setBackground(SDSControlFactory.getTSBodyColor());
		lblUserId.getStarLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtUserId = new SDSTSText(authComposite, SWT.NONE, OVERRIDEAUTH_CTRL_TXT_USERID,OVERRIDEAUTH_FRM_TXT_USERID, false);
		txtUserId.setFocus();
		txtUserId.setTextLimit(5);

		lblPassword = new SDSTSMandatoryLabel(authComposite, SWT.END, LabelLoader.getLabelValue(LBL_PASSWORD));
		lblPassword.setBackground(SDSControlFactory.getTSBodyColor());
		lblPassword.getStarLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtPassword = new SDSTSText(authComposite, SWT.NONE | SWT.PASSWORD, OVERRIDEAUTH_CTRL_TXT_PASSWORD,OVERRIDEAUTH_FRM_TXT_PASSWORD, false);
		txtPassword.setTextLimit(20);

		lblReasonForOverride = new SDSTSMandatoryLabel(authComposite, SWT.END,LabelLoader.getLabelValue(LBL_REASON_FOR_OVERRIDE));
		lblReasonForOverride.setBackground(SDSControlFactory.getTSBodyColor());
		lblReasonForOverride.getStarLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtReasonForOverride = new SDSTSText(authComposite, SWT.NONE, OVERRIDEAUTH_CTRL_TXT_REASON_FOR_OVERRIDE, null, false);
		txtReasonForOverride.setEditable(false);
		
		authComposite.setTabList(new Control[]{txtUserId,txtPassword,txtReasonForOverride});

		GridData gdCompButton = new GridData();
		gdCompButton.verticalAlignment = GridData.CENTER;
		gdCompButton.horizontalAlignment = GridData.CENTER;
		gdCompButton.horizontalIndent = 20;

		GridLayout grlCompButton = new GridLayout();
		grlCompButton.numColumns = 2;
		grlCompButton.marginWidth = 5;
		grlCompButton.marginHeight = 5;
		grlCompButton.verticalSpacing = 10;
		grlCompButton.horizontalSpacing = 50;

		GridData gridDatalbl = new GridData();
		gridDatalbl.grabExcessVerticalSpace = true;
		gridDatalbl.horizontalAlignment = GridData.CENTER;
		gridDatalbl.verticalAlignment = GridData.CENTER;
		gridDatalbl.grabExcessHorizontalSpace = false;

		compBtn = new Composite(this,SWT.NONE);
		compBtn.setLayout(grlCompButton);
		compBtn.setLayoutData(gdCompButton);
		compBtn.setBackground(SDSControlFactory.getTSBodyColor());

		iLblSubmit = new SDSImageLabel(compBtn, SWT.NONE, buttonBG, LabelLoader.getLabelValue(LBL_SUBMIT_BARCODE), OVERRIDEAUTH_CTRL_BTN_SUBMIT);
		iLblCancel = new SDSImageLabel(compBtn, SWT.NONE, buttonBG, LabelLoader.getLabelValue(LBL_CANCEL), OVERRIDEAUTH_CTRL_BTN_CANCEL);
	}

	private void setControlProperties(){
		try {
			/**FOR THE LBL KEYBOARD(lblKeyboard) CONTROL SET THE GRID DATA AT THE
			 * CREATION ITSELF SINCE IT IS A BUTTON WHICH IS GOING TO BE DISPLAYED 
			 * WITH THE IMAGE**/

			SDSControlFactory.setTouchScreenLabelProperties(lblPassword.getLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblUserId.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtPassword);
			SDSControlFactory.setTouchScreenTextProperties(txtUserId);
			SDSControlFactory.setTouchScreenTextProperties(txtReasonForOverride);
			
			GridData gdTxtUserId = (GridData)txtUserId.getLayoutData();
			gdTxtUserId.widthHint= ControlConstants.TEXT_WIDTH;
			gdTxtUserId.horizontalIndent = 5;
			txtUserId.setLayoutData(gdTxtUserId);

			GridData gdTxtPassword = (GridData)txtPassword.getLayoutData();
			gdTxtPassword.widthHint= ControlConstants.TEXT_WIDTH;
			gdTxtPassword.horizontalIndent = 5;
			txtPassword.setLayoutData(gdTxtPassword);
			
			GridData gdTxtReason = (GridData)txtReasonForOverride.getLayoutData();
			gdTxtReason.widthHint= ControlConstants.TEXT_WIDTH;
			gdTxtReason.horizontalIndent = 5;
			txtReasonForOverride.setLayoutData(gdTxtReason);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the authComposite
	 */
	public Composite getAuthComposite() {
		return authComposite;
	}

	/**
	 * @param authComposite the authComposite to set
	 */
	public void setAuthComposite(Composite authComposite) {
		this.authComposite = authComposite;
	}

	/**
	 * @return the compBtn
	 */
	public Composite getCompBtn() {
		return compBtn;
	}

	/**
	 * @param compBtn the compBtn to set
	 */
	public void setCompBtn(Composite compBtn) {
		this.compBtn = compBtn;
	}

	/**
	 * @return the iLblSubmit
	 */
	public SDSImageLabel getILblSubmit() {
		return iLblSubmit;
	}

	/**
	 * @param iLblSubmit the iLblSubmit to set
	 */
	public void setILblSubmit(SDSImageLabel iLblSubmit) {
		this.iLblSubmit = iLblSubmit;
	}

	/**
	 * @return the iLblCancel
	 */
	public SDSImageLabel getILblCancel() {
		return iLblCancel;
	}

	/**
	 * @param iLblCancel the iLblCancel to set
	 */
	public void setILblCancel(SDSImageLabel iLblCancel) {
		this.iLblCancel = iLblCancel;
	}

	/**
	 * @return the txtUserId
	 */
	public SDSTSText getTxtUserId() {
		return txtUserId;
	}

	/**
	 * @param txtUserId the txtUserId to set
	 */
	public void setTxtUserId(SDSTSText txtUserId) {
		this.txtUserId = txtUserId;
	}

	/**
	 * @return the txtPassword
	 */
	public SDSTSText getTxtPassword() {
		return txtPassword;
	}

	/**
	 * @param txtPassword the txtPassword to set
	 */
	public void setTxtPassword(SDSTSText txtPassword) {
		this.txtPassword = txtPassword;
	}

	/**
	 * @return the txtReasonForOverride
	 */
	public SDSTSText getTxtReasonForOverride() {
		return txtReasonForOverride;
	}

	/**
	 * @param txtReasonForOverride the txtReasonForOverride to set
	 */
	public void setTxtReasonForOverride(SDSTSText txtReasonForOverride) {
		this.txtReasonForOverride = txtReasonForOverride;
	}

	private void createImages() {
		buttonBG = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}

}
