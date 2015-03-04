/*****************************************************************************
 * $Id: MultiRedeemComposite.java,v 1.14.1.1.1.0, 2013-10-25 17:50:06Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 12:50:06 PM$
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This class creates the multi redeem composite
 * @author Nithya kalyani R
 * @version $Revision: 18$ 
 */
public class MultiRedeemComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants{

	/**
	 * Instance of voucher coupon label
	 */
	private SDSTSLabel lblVoucherCoupon;

	/**
	 * Instance of amount total label
	 */
	private SDSTSLabel lblAmountTotal;

	/**
	 * Instance of voucher coupon text
	 */
	private SDSTSText txtVoucherCoupon;

	/**
	 * Instance of amount total text
	 */
	private SDSTSText txtAmountTotal;

	/**
	 * Instance of redeem group
	 */
	private Group grpRedeem;

	/**
	 * Instance of button composite
	 */
	private Composite compButton ;

	/**
	 * Instance of reset total button
	 */
	private SDSImageLabel  btnResetTotal;

	/**
	 * Instance of cancel button
	 */
	private SDSImageLabel  btnCancel;

	private Image buttonImage;
	
	/**
	 * Constructor of the class
	 */
	public MultiRedeemComposite(Composite parent, int style)
	{
		super(parent, style);
		initialize();
		if( Util.isSmallerResolution() ) {
			setSize(700,123);
		} else {
			setSize(parent.getSize());
		}
	}

	private void initialize()	{
		createImages();
		createMRedeemComposite();		
		GridLayout grlPage = new GridLayout();
		grlPage.verticalSpacing = 0;
		grlPage.horizontalSpacing = 0;
		GridData gdPage = new GridData();
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.verticalAlignment = GridData.CENTER;
		gdPage.horizontalAlignment = GridData.CENTER;
		setControlProperties();
		setLayout(grlPage);
		setLayoutData(gdPage);
	}

	public void createMRedeemComposite()	{		
		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		gdDetailsGrp.grabExcessHorizontalSpace = true;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;		

		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 2;
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;
		
		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);
		
		Composite header = new Composite(container, SWT.NONE);
		
		GridData headerData = new GridData();
		headerData.widthHint = 910;
		headerData.heightHint = 30;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE | SWT.COLOR_WHITE, AppContextValues.getInstance().getTicketText() +LabelLoader.getLabelValue(LBL_VOUCHER_COUPON_DETAILS));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		
		if( Util.isSmallerResolution() ) {
			lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		} else {
			lblMessage.setFont(SDSControlFactory.getDefaultBoldFont());
		}
		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Composite readOnlyComp = new Composite(container, SWT.NONE);
		readOnlyComp.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdReadOnly = new GridData();
		gdReadOnly.grabExcessHorizontalSpace = true;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		gdReadOnly.horizontalAlignment = GridData.CENTER;

		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns = 4;
		grlReadOnly.verticalSpacing = 3;
		grlReadOnly.horizontalSpacing = 35;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		lblVoucherCoupon = new SDSTSLabel(readOnlyComp, SWT.END,LabelLoader.getLabelValue(LBL_OF_VOUCHERS_COUPONS,new String[] {AppContextValues.getInstance().getTicketText()}));
		txtVoucherCoupon = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT, MULTIREDEEM_CTRL_TXT_VOUCHERCOUPON, MULTIREDEEM_FRM_TXT_VOUCHERCOUPON);
		txtVoucherCoupon.setEditable(false);
		lblAmountTotal   = new SDSTSLabel(readOnlyComp, SWT.END,LabelLoader.getLabelValue(LBL_TOTAL_AMOUNT) + " ("+CurrencyUtil.getCurrencySymbol()+")");
		txtAmountTotal   = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT, MULTIREDEEM_CTRL_TXT_AMOUNTTOTAL, MULTIREDEEM_FRM_TXT_AMOUNTTOTAL);
		txtAmountTotal.setEditable(false);
		txtAmountTotal.setVisible(false);
		lblAmountTotal.setVisible(false);
		txtAmountTotal.setAmountField(true);

		GridData gdBtnComposite = new GridData();
		gdBtnComposite.grabExcessHorizontalSpace = true;
		gdBtnComposite.verticalAlignment = GridData.CENTER;
		gdBtnComposite.horizontalAlignment = GridData.CENTER;
		gdBtnComposite.horizontalSpan = 4;

		GridLayout grlBtnComposite = new GridLayout();
		grlBtnComposite.numColumns = 3;
		grlBtnComposite.marginWidth = 4;
		grlBtnComposite.marginHeight = 5;
		grlBtnComposite.verticalSpacing = 5;
		grlBtnComposite.horizontalSpacing = 25;

		compButton = new Composite(readOnlyComp, SWT.NONE);
		compButton.setLayout(grlBtnComposite);
		compButton.setLayoutData(gdBtnComposite);
			
		btnResetTotal = new SDSImageLabel(compButton, SWT.NONE, buttonImage,LabelLoader.getLabelValue(LBL_RESET_TOTAL), MULTIREDEEM_CTRL_BTN_RESETTOTAL);
		btnResetTotal.getTextLabel().setData(MULTIREDEEM_CTRL_BTN_RESETTOTAL);
		btnResetTotal.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		btnResetTotal.setFont(SDSControlFactory.getDefaultBoldFont());
		
		btnCancel = new SDSImageLabel(compButton, SWT.NONE, buttonImage, LabelLoader.getLabelValue(LBL_CANCEL),MULTIREDEEMVOUCHER_CTRL_BTN_CANCEL);
		btnCancel.getTextLabel().setData(MULTIREDEEMVOUCHER_CTRL_BTN_CANCEL);
		btnCancel.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		btnCancel.setFont(SDSControlFactory.getDefaultBoldFont());
		
	}

	public SDSImageLabel getBtnResetTotal() {
		return btnResetTotal;
	}

	public SDSImageLabel getBtnCancel() {
		return btnCancel;
	}

	/**
	 * This method sets the default grid properties for all the
	 * controls
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblAmountTotal);
			SDSControlFactory.setTouchScreenLabelProperties(lblVoucherCoupon);
			SDSControlFactory.setTouchScreenTextProperties(txtAmountTotal);
			SDSControlFactory.setTouchScreenTextProperties(txtVoucherCoupon);
//			SDSControlFactory.setTouchScreenButtonProperties(btnCancel);
//			SDSControlFactory.setTouchScreenButtonProperties(btnResetTotal);
//			SDSControlFactory.setGroupHeadingFont(grpRedeem);

//			int width  = 0;
//			int height = 0;
//			if( Util.isSmallerResolution() ) {
//				width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 35;
//				height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 12;
//			} else {
//				width 	= ControlConstants.BUTTON_WIDTH;
//				height 	= ControlConstants.BUTTON_HEIGHT;
//			}
			
//			GridData gdBtnCancel = (GridData) btnCancel.getLayoutData();
//			gdBtnCancel.widthHint  = width;
//			gdBtnCancel.heightHint = height;
//			btnCancel.setLayoutData(gdBtnCancel);
//
//			GridData gdBtnReset = (GridData) btnResetTotal.getLayoutData();			
//			gdBtnReset.widthHint  = width;
//			gdBtnReset.heightHint = height;
//			btnResetTotal.setLayoutData(gdBtnReset);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the txtVoucherCoupon
	 */
	public SDSTSText getTxtVoucherCoupon() {
		return txtVoucherCoupon;
	}

	/**
	 * @param txtVoucherCoupon the txtVoucherCoupon to set
	 */
	public void setTxtVoucherCoupon(SDSTSText txtVoucherCoupon) {
		this.txtVoucherCoupon = txtVoucherCoupon;
	}

	/**
	 * @return the txtAmountTotal
	 */
	public SDSTSText getTxtAmountTotal() {
		return txtAmountTotal;
	}

	/**
	 * @param txtAmountTotal the txtAmountTotal to set
	 */
	public void setTxtAmountTotal(SDSTSText txtAmountTotal) {
		this.txtAmountTotal = txtAmountTotal;
	}
	
	private void createImages() {
		buttonImage  = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}

}
