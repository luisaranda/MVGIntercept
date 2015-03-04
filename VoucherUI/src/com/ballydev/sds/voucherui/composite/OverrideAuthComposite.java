/*****************************************************************************
 * $Id: OverrideAuthComposite.java,v 1.16, 2010-04-09 07:10:52Z, Lokesh, Krishna Sinha$
 * $Date: 4/9/2010 2:10:52 AM$
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

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
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
import com.ballydev.sds.framework.control.SDSTSCombo;
import com.ballydev.sds.framework.control.SDSTSMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucher.dto.TransactionReasonInfoDTO;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This creates the override auth composite
 * @author Nithya kalyani R
 * @version $Revision: 17$ 
 */
public class OverrideAuthComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {

	/**
	 * Instance of the user id text
	 */
	private SDSTSText txtUserId;

	/**
	 * Instance of the password text
	 */
	private SDSTSText txtPassword;

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
	 * Instance of the Auth composite
	 */
	private Composite authComposite;

	/**
	 * Instance of the keyboard label
	 */
//	private SDSTSLabel  lblKeyBoard;

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

	/**
	 * Instance of the transaction reason combo
	 */
	private SDSTSCombo<TransactionReasonInfoDTO> comboTxnReason ;

	private Image buttonBG;

	/**
	 * Constructor of the class
	 */
	public OverrideAuthComposite(Composite parent, int style){
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
		
		txtUserId = new SDSTSText(authComposite,SWT.NONE,OVERRIDEAUTH_CTRL_TXT_USERID,OVERRIDEAUTH_FRM_TXT_USERID,false);
		txtUserId.setFocus();
		txtUserId.setTextLimit(5);

		lblPassword = new SDSTSMandatoryLabel(authComposite, SWT.END,LabelLoader.getLabelValue(LBL_PASSWORD));
		lblPassword.setBackground(SDSControlFactory.getTSBodyColor());
		lblPassword.getStarLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtPassword = new SDSTSText(authComposite,SWT.NONE | SWT.PASSWORD,OVERRIDEAUTH_CTRL_TXT_PASSWORD,OVERRIDEAUTH_FRM_TXT_PASSWORD,false);
		txtPassword.setTextLimit(20);

		lblReasonForOverride = new SDSTSMandatoryLabel(authComposite, SWT.END,LabelLoader.getLabelValue(LBL_REASON_FOR_OVERRIDE));
		lblReasonForOverride.setBackground(SDSControlFactory.getTSBodyColor());
		lblReasonForOverride.getStarLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		try	{
			if( Util.isSmallerResolution() ) {
				GridData downBtnGD = new GridData();
				downBtnGD.horizontalAlignment = GridData.CENTER;
				downBtnGD.verticalAlignment = GridData.CENTER;
				downBtnGD.widthHint =  33;
				downBtnGD.heightHint = 33;
				comboTxnReason = new SDSTSCombo<TransactionReasonInfoDTO>(authComposite,OVERRIDEAUTH_FRM_CMB_VIWER_SEL_ITEM_SELECTEDREASONVALUE,"description","id",OVERRIDEAUTH_FRM_CMB_VIWER_LIST_REASON);
				comboTxnReason.getLeft().setLayoutData(downBtnGD);
				comboTxnReason.getRight().setLayoutData(downBtnGD);
			} else {
				comboTxnReason = new SDSTSCombo<TransactionReasonInfoDTO>(authComposite,OVERRIDEAUTH_FRM_CMB_VIWER_SEL_ITEM_SELECTEDREASONVALUE,"description","id",OVERRIDEAUTH_FRM_CMB_VIWER_LIST_REASON);
			}
			
			comboTxnReason.setBackground(comboTxnReason.getParent().getBackground());
			comboTxnReason.getLeft().setBackground(comboTxnReason.getParent().getBackground());
			comboTxnReason.getRight().setBackground(comboTxnReason.getParent().getBackground());
		} 
		catch (Exception e)	{
			e.printStackTrace();
		}	

		authComposite.setTabList(new Control[]{txtUserId,txtPassword,comboTxnReason});

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

//		lblKeyBoard = new SDSTSLabel(compBtn, SWT.NONE,IVoucherConstants.OVERRIDEAUTH_CTRL_BTN_KEYBOARD);
//		lblKeyBoard.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.KEYBOARD_BUTTON_IMG)));
//		lblKeyBoard.setLayoutData(gridDatalbl);
//		lblKeyBoard.setName(IVoucherConstants.OVERRIDEAUTH_CTRL_BTN_KEYBOARD);
//		lblKeyBoard.setBackground(compBtn.getBackground());

		iLblSubmit = new SDSImageLabel(compBtn, SWT.NONE, buttonBG, LabelLoader.getLabelValue(LBL_SUBMIT), OVERRIDEAUTH_CTRL_BTN_SUBMIT);
		iLblCancel = new SDSImageLabel(compBtn, SWT.NONE, buttonBG, LabelLoader.getLabelValue(LBL_CANCEL), OVERRIDEAUTH_CTRL_BTN_CANCEL);
	}

	private void setControlProperties(){
		try {
			/**FOR THE LBL KEYBOARD(lblKeyboard) CONTROL SET THE GRID DATA AT THE
			 * CREATION ITSELF SINCE IT IS A BUTTON WHICH IS GOING TO BE DISPLAYED 
			 * WITH THE IMAGE**/

			SDSControlFactory.setTouchScreenLabelProperties(lblPassword.getLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblReasonForOverride.getLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblUserId.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtPassword);
			SDSControlFactory.setTouchScreenTextProperties(txtUserId);
			
			GridData gdTxtUserId = (GridData)txtUserId.getLayoutData();
			gdTxtUserId.widthHint= ControlConstants.TEXT_WIDTH;
			gdTxtUserId.horizontalIndent = 5;
			txtUserId.setLayoutData(gdTxtUserId);

			GridData gdTxtPassword = (GridData)txtPassword.getLayoutData();
			gdTxtPassword.widthHint= ControlConstants.TEXT_WIDTH;
			gdTxtPassword.horizontalIndent = 5;
			txtPassword.setLayoutData(gdTxtPassword);

//			GridData gdBtnCancel = (GridData)iLblCancel.getLayoutData();
//			gdBtnCancel.heightHint = ControlConstants.BUTTON_HEIGHT;
//			gdBtnCancel.widthHint = ControlConstants.SMALL_BUTTON_WIDTH;
//			iLblCancel.setLayoutData(gdBtnCancel);
//
//			GridData gdBtnSubmit = (GridData)iLblSubmit.getLayoutData();
//			gdBtnSubmit.heightHint = ControlConstants.BUTTON_HEIGHT;
//			gdBtnSubmit.widthHint = ControlConstants.SMALL_BUTTON_WIDTH;
//			iLblSubmit.setLayoutData(gdBtnSubmit);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	class CmbContentProvider implements IStructuredContentProvider{

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((ArrayList)inputElement).toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

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
	 * @return the lblUserId
	 */
	public SDSTSMandatoryLabel getLblUserId() {
		return lblUserId;
	}

	/**
	 * @param lblUserId the lblUserId to set
	 */
	public void setLblUserId(SDSTSMandatoryLabel lblUserId) {
		this.lblUserId = lblUserId;
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
	 * @return the comboTxnReason
	 */
	public SDSTSCombo<TransactionReasonInfoDTO> getComboTxnReason() {
		return comboTxnReason;
	}

	/**
	 * @param comboTxnReason the comboTxnReason to set
	 */
	public void setComboTxnReason(
			SDSTSCombo<TransactionReasonInfoDTO> comboTxnReason) {
		this.comboTxnReason = comboTxnReason;
	}

//	/**
//	 * @return the lblKeyBoard
//	 */
//	public SDSTSLabel getLblKeyBoard() {
//		return lblKeyBoard;
//	}

//	/**
//	 * @param lblKeyBoard the lblKeyBoard to set
//	 */
//	public void setLblKeyBoard(SDSTSLabel lblKeyBoard) {
//		this.lblKeyBoard = lblKeyBoard;
//	}

	private void createImages() {
		buttonBG = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
}
