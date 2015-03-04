/*****************************************************************************
 * $Id: RedeemVoucherComposite.java,v 1.36.1.0, 2011-05-25 11:43:54Z, SDS12.3.2CheckinUser$
 * $Date: 5/25/2011 6:43:54 AM$
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This class creates the redeem voucher composite
 * @author Nithya kalyani R
 * @version $Revision: 38$ 
 */
public class RedeemVoucherComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {

	/**
	 * Instance of the sub composite
	 */
	private Composite subComposite = null;

	/**
	 * Instance of the barcode label
	 */
	private CbctlMandatoryLabel lblBarCode = null;

	/**
	 * Instance of the barcode text
	 */
	private SDSTSText txtBarCode = null;

	/**
	 * Instance of the amount label
	 */
	private CbctlLabel lblAmount = null;

	/**
	 * Instance of the amount text
	 */
	private SDSTSText txtAmount = null;

	/**
	 * Instance of the state label
	 */
	private CbctlLabel lblState = null;

	/**
	 * Instance of the state text
	 */
	private SDSTSText txtState = null;

	/**
	 * Instance of the effective date label
	 */
	private CbctlLabel lblEffectiveDate = null;

	/**
	 * Instance of the effective date text
	 */
	private SDSTSText txtEffectiveDate = null;

	/**
	 * Instance of the expire date label
	 */
	private CbctlLabel lblExpireDate = null;

	/**
	 * Instance of the expire date text
	 */
	private SDSTSText txtExpireDate = null;

	/**
	 * Instance of the location label
	 */
	private CbctlLabel lblLocation = null;

	/**
	 * Instance of the location text
	 */
	private SDSTSText txtLocation = null;

	/**
	 * Instance of the player id label
	 */
	private CbctlLabel lblPlayerId = null;

	/**
	 * Instance of the playerid text
	 */
	private SDSTSText txtPlayerId = null;

	/**
	 * Instance of the button composite
	 */
	private Composite btnComposite = null;

	/**
	 * Instance of the multi redeem composite
	 */
	private Composite multiRedeemComposite = null;

	/**
	 * Instance to track whether it is a void call
	 */
	private boolean isVoid = false;

	/**
	 * Instance to track whether it is a override call
	 */
	private boolean isOverride = false;

	private Image buttonImage;
	
	private Image buttonDisableImage;
	
	private SDSImageLabel lblSubmitBarcode;
	private SDSImageLabel lblRedeem;
	private SDSImageLabel lblMultiRedeem;
	private SDSImageLabel lblOverride;
	private SDSImageLabel lblCancelProcess;
	private SDSImageLabel lblCancel;
	private SDSImageLabel lblVoid;

	/**
	 * Constructor of the class
	 */
	public RedeemVoucherComposite(Composite parent, int style,boolean isOverride,boolean isVoid) {
		super(parent, style);
		this.isVoid = isVoid;
		this.isOverride = isOverride;
		initialize();
		this.setBounds(0,0,1000,800);
		this.setBackground(SDSControlFactory.getTSBodyColor());
		layout();
	}

	private void initialize() {
		createImages();
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.marginHeight = 1;
		grlPage.marginWidth = 1;
		if(Util.isSmallerResolution()){
			grlPage.verticalSpacing = 0;
		} else {
			grlPage.verticalSpacing = 10;
		}
		grlPage.horizontalSpacing = 1;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		if(isVoid){
			new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(LBL_STC_VOID_TICKET_SCREEN)+AppContextValues.getInstance().getTicketText());
		}
		else{
			new HeaderComposite(this,SWT.NONE,AppContextValues.getInstance().getTicketText() + " " + LabelLoader.getLabelValue(LBL_REDEEM_VOUCHER_HEADER));
		}

		createSubComposite();
		createDetailsGroup();
		if(isVoid) {
			createVoidBtnComposite();
		} else {
			createBtnComposite();
		}

		createMultiRedeemComposite();
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		this.setTabList(new Control[]{subComposite,btnComposite});
//		layout();
	}

	private void createImages() {
		buttonImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
	}

	public void createMultiRedeemComposite() {
		GridData gdMultiRedeemComp = new GridData();
		gdMultiRedeemComp.grabExcessHorizontalSpace = true;
		gdMultiRedeemComp.verticalAlignment = GridData.CENTER;
		gdMultiRedeemComp.widthHint = 915;
		gdMultiRedeemComp.heightHint = 150;
		gdMultiRedeemComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlMultiRedeemComp = new GridLayout();
		grlMultiRedeemComp.numColumns = 3;
		grlMultiRedeemComp.marginWidth = 4;
		grlMultiRedeemComp.marginHeight = 0;
		grlMultiRedeemComp.verticalSpacing = 0;
		grlMultiRedeemComp.horizontalSpacing = 5;

		multiRedeemComposite = new Composite(this,SWT.NONE);
		multiRedeemComposite.setLayout(grlMultiRedeemComp);
		multiRedeemComposite.setLayoutData(gdMultiRedeemComp);	
	}

	/**
	 * This method initializes subComposite	
	 */
	private void createSubComposite() {		
		GridData gdSubComp = new GridData();
		gdSubComp.grabExcessHorizontalSpace = true;
		gdSubComp.horizontalAlignment = GridData.CENTER;
		gdSubComp.verticalAlignment = GridData.CENTER;
		gdSubComp.grabExcessVerticalSpace = false;
		
		if( Util.isSmallerResolution() ) {
			gdSubComp.horizontalIndent = 0;
		} else {
			gdSubComp.horizontalIndent = 0;
		}
		
		gdSubComp.widthHint = 900;

		GridLayout grlSubComp = new GridLayout();
		grlSubComp.numColumns = 3;
		grlSubComp.verticalSpacing = 10;
		if( Util.isSmallerResolution() ) {
			grlSubComp.horizontalSpacing = 152;
		} else if( Util.is1024X786Resolution() ) {
			grlSubComp.horizontalSpacing = 172;
		} else
			grlSubComp.horizontalSpacing = 185;

		subComposite = new Composite(this, SWT.NONE);
//		subComposite.setBackground(SDSControlFactory.getTSHeaderColor());
		subComposite.setLayoutData(gdSubComp);
		subComposite.setLayout(grlSubComp);
		
		lblBarCode = new CbctlMandatoryLabel(subComposite,SWT.INHERIT_DEFAULT,LabelLoader.getLabelValue(LBL_BAR_CODE));
		lblBarCode.setBackground(SDSControlFactory.getTSBodyColor());
		
		txtBarCode = new SDSTSText(subComposite, SWT.BORDER,REDEEMVOUCHER_CTRL_TXT_BARCODE,REDEEMVOUCHER_FRM_TXT_BARCODE,true);
		txtBarCode.setTextLimit(18);
		txtBarCode.setTextLimitChkEnabled(true);

		lblSubmitBarcode = new SDSImageLabel(subComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(LBL_SUBMIT_BARCODE), REDEEMVOUCHER_CTRL_BTN_SUBMIT_BARCODE);
		lblSubmitBarcode.getTextLabel().setData("lblLogin");
		lblSubmitBarcode.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		lblSubmitBarcode.setFont(SDSControlFactory.getDefaultBoldFont());
		lblSubmitBarcode.setBackgroundImage(buttonImage);
		lblSubmitBarcode.setBounds(buttonImage.getBounds());
		
		lblSubmitBarcode.setTabList(new Control[]{lblSubmitBarcode.getTextLabel()});
		subComposite.setTabList(new Control[]{txtBarCode,lblSubmitBarcode});
		
	}

	/**
	 * This method initializes detailsGroup	
	 */
	private void createDetailsGroup() {

		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		gdDetailsGrp.grabExcessHorizontalSpace = false;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 769;
		}
		
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
		if(Util.isSmallerResolution()) {
			headerData.widthHint = 865;
		} else if(Util.is1024X786Resolution()) {
			headerData.widthHint = 835;
		} else 
			headerData.widthHint = 910;
		headerData.heightHint = 30;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE | SWT.COLOR_WHITE, AppContextValues.getInstance().getTicketText() +LabelLoader.getLabelValue(LBL_VOUCHER_COUPON_DETAILS));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = false;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		if(Util.isSmallerResolution()) {
			lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		}else {
			lblMessage.setFont(SDSControlFactory.getDefaultBoldFont());
		}
		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		Composite readOnlyComp = new Composite(container, SWT.NONE);
		//readOnlyComp.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdReadOnly = new GridData();
		gdReadOnly.grabExcessHorizontalSpace = false;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 769;
		}
		gdReadOnly.horizontalAlignment = GridData.CENTER;
		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns 		  = 4;
		grlReadOnly.verticalSpacing   = 3;
		grlReadOnly.horizontalSpacing = 50;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		lblAmount = new CbctlLabel(readOnlyComp, SWT.NONE,AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(LBL_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		txtAmount = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT,REDEEMVOUCHER_CTRL_TXT_AMOUNT,REDEEMVOUCHER_FRM_TXT_AMOUNT,true);
		txtAmount.setAmountField(true);
		txtAmount.setEnabled(false);
		
		lblState = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_STATE));
		txtState = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_STATE,REDEEMVOUCHER_FRM_TXT_STATE);
		txtState.setEnabled(false);
		
		lblEffectiveDate = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_EFFECTIVE_DATE));
		txtEffectiveDate = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_EFFECTIVEDATE,REDEEMVOUCHER_FRM_TXT_EFFECTIVEDATE);
		txtEffectiveDate.setEnabled(false);
		
		lblExpireDate = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_EXPIRE_DATE));
		txtExpireDate = new SDSTSText(readOnlyComp, SWT.BORDER, REDEEMVOUCHER_CTRL_TXT_EXPIREDATE,REDEEMVOUCHER_FRM_TXT_EXPIREDATE);
		txtExpireDate.setEnabled(false);
		
		lblLocation = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_LOCATION));
		txtLocation = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_LOCATION,REDEEMVOUCHER_FRM_TXT_LOCATION);
		txtLocation.setEnabled(false);
		
		lblPlayerId = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_PLAYER_ID));
		txtPlayerId = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_PLAYERID,REDEEMVOUCHER_FRM_TXT_PLAYERID,true);
		txtPlayerId.setEnabled(false);
	}

	/**
	 * This method initializes btnComposite	
	 *
	 */
	private void createVoidBtnComposite() {
		GridData gdBtnComp = new GridData();
		gdBtnComp.grabExcessHorizontalSpace = true;
		gdBtnComp.verticalAlignment = GridData.CENTER;
		gdBtnComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlBtnComp = new GridLayout();
		grlBtnComp.numColumns = 2;
		grlBtnComp.marginWidth = 4;
		grlBtnComp.marginHeight = 15;
		grlBtnComp.verticalSpacing = 5;
		grlBtnComp.horizontalSpacing = 25;

		btnComposite = new Composite(this, SWT.NONE);
		btnComposite.setLayout(grlBtnComp);
		btnComposite.setLayoutData(gdBtnComp);

		GridData gridData4 = new GridData();
		gridData4.heightHint = buttonImage.getBounds().height;
		gridData4.widthHint = buttonImage.getBounds().width;
		gridData4.horizontalAlignment = GridData.END;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalIndent = 20;

		lblVoid = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(LBL_VOID), REDEEMVOUCHER_CTRL_BTN_VOID);
		lblVoid.getTextLabel().setData("btnVoid");
		lblVoid.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblVoid.setLayoutData(gridData4);
		}
		lblVoid.setFont(SDSControlFactory.getDefaultBoldFont());
		lblVoid.setBackgroundImage(buttonImage);
		lblVoid.setBounds(buttonImage.getBounds());
		
		lblCancel = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(LBL_CANCEL), VOIDVOUCHER_CTRL_BTN_CANCEL);
		lblCancel.getTextLabel().setData("btnVoidCancel");
		lblCancel.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblCancel.setLayoutData(gridData4);
		}
		lblCancel.setFont(SDSControlFactory.getDefaultBoldFont());
		lblCancel.setBackgroundImage(buttonImage);
		lblCancel.setBounds(buttonImage.getBounds());
		
		btnComposite.setTabList(new Control[]{lblVoid,lblCancel});
		
	}

	/**
	 * This method initializes btnComposite	
	 */
	private void createBtnComposite() 
	{
		GridData gdBtnComp = new GridData();
		gdBtnComp.grabExcessHorizontalSpace = true;
		gdBtnComp.verticalAlignment = GridData.CENTER;
		gdBtnComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlBtnComp = new GridLayout();
		grlBtnComp.numColumns = 6;
		grlBtnComp.marginWidth = 5;
		grlBtnComp.marginHeight = 5;
		grlBtnComp.verticalSpacing = 5;
		grlBtnComp.horizontalSpacing = 16;

		btnComposite = new Composite(this, SWT.NONE);
		btnComposite.setLayout(grlBtnComp);
		btnComposite.setLayoutData(gdBtnComp);

		GridData gridData6 = new GridData();
		gridData6.heightHint = buttonDisableImage.getBounds().height;
		gridData6.widthHint = buttonDisableImage.getBounds().width;
		gridData6.horizontalAlignment = GridData.END;
		gridData6.verticalAlignment = GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalIndent = 20;
		gridData6.widthHint	= ControlConstants.BUTTON_WIDTH + 21;

		lblRedeem = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(LBL_REDEEM_VOUCHER), REDEEMVOUCHER_CTRL_LBL_REDEEM);
		lblRedeem.getTextLabel().setData(REDEEMVOUCHER_CTRL_LBL_REDEEM);
		lblRedeem.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblRedeem.setLayoutData(gridData6);
		}
		lblRedeem.setFont(SDSControlFactory.getDefaultBoldFont());
		lblRedeem.setBackgroundImage(buttonImage);
		lblRedeem.setBounds(buttonImage.getBounds());

		lblMultiRedeem = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(LBL_MULTI_REDEEM), REDEEMVOUCHER_CTRL_LBL_MULTIREDEEM);
		lblMultiRedeem.getTextLabel().setData(REDEEMVOUCHER_CTRL_LBL_MULTIREDEEM);
		lblMultiRedeem.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblMultiRedeem.setLayoutData(gridData6);
		}
		lblMultiRedeem.setFont(SDSControlFactory.getDefaultBoldFont());
		lblMultiRedeem.setBackgroundImage(buttonImage);
		lblMultiRedeem.setBounds(buttonImage.getBounds());

		lblOverride = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(LBL_OVERRIDE), REDEEMVOUCHER_CTRL_LBL_OVERRIDE);
		lblOverride.getTextLabel().setData(REDEEMVOUCHER_CTRL_LBL_OVERRIDE);
		lblOverride.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblOverride.setLayoutData(gridData6);
		}
		lblOverride.setFont(SDSControlFactory.getDefaultBoldFont());
		lblOverride.setBackgroundImage(buttonImage);
		lblOverride.setBounds(buttonImage.getBounds());
		
		lblCancel = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CANCEL_REDEEM), REDEEMVOUCHER_CTRL_LBL_CANCEL);
		lblCancel.getTextLabel().setData(REDEEMVOUCHER_CTRL_LBL_CANCEL);
		lblCancel.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblCancel.setLayoutData(gridData6);
		}
		lblCancel.setFont(SDSControlFactory.getDefaultBoldFont());
		lblCancel.setBackgroundImage(buttonImage);
		lblCancel.setBounds(buttonImage.getBounds());
		
		lblCancelProcess = new SDSImageLabel(btnComposite, SWT.CENTER, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CANCEL), REDEEMVOUCHER_CTRL_LBL_CANCELPROCESS);
		lblCancelProcess.getTextLabel().setData(REDEEMVOUCHER_CTRL_LBL_CANCELPROCESS);
		lblCancelProcess.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblCancelProcess.setLayoutData(gridData6);
		}
		lblCancelProcess.setFont(SDSControlFactory.getDefaultBoldFont());
		lblCancelProcess.setBackgroundImage(buttonImage);
		lblCancelProcess.setBounds(buttonImage.getBounds());
		
		lblRedeem.setTabList(new Control[]{lblRedeem.getTextLabel()});
		lblMultiRedeem.setTabList(new Control[]{lblMultiRedeem.getTextLabel()});
		lblOverride.setTabList(new Control[]{lblOverride.getTextLabel()});
		lblCancel.setTabList(new Control[]{lblCancel.getTextLabel()});
		lblCancelProcess.setTabList(new Control[]{lblCancelProcess.getTextLabel()});
		btnComposite.setTabList(new Control[]{lblRedeem, lblMultiRedeem, lblOverride, lblCancel, lblCancelProcess});
	}

	/**
	 * This method set the default control properties
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblAmount);
			SDSControlFactory.setTouchScreenLabelProperties(lblEffectiveDate);
			SDSControlFactory.setTouchScreenLabelProperties(lblExpireDate);
			SDSControlFactory.setTouchScreenLabelProperties(lblLocation);
			SDSControlFactory.setTouchScreenLabelProperties(lblPlayerId);
			SDSControlFactory.setTouchScreenLabelProperties(lblState);
			SDSControlFactory.setTouchScreenLabelProperties(lblBarCode.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtAmount);
			SDSControlFactory.setTouchScreenTextProperties(txtBarCode);
			SDSControlFactory.setTouchScreenTextProperties(txtEffectiveDate);
			SDSControlFactory.setTouchScreenTextProperties(txtExpireDate);
			SDSControlFactory.setTouchScreenTextProperties(txtLocation);
			SDSControlFactory.setTouchScreenTextProperties(txtPlayerId);
			SDSControlFactory.setTouchScreenTextProperties(txtState);
//			SDSControlFactory.setGroupHeadingFont(grpDetails);
			
			int width  = 0;
			int height = 0;
			if( Util.isSmallerResolution() ) {
				width = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 8;
				height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 14;
			} else {
				width 	= ControlConstants.BUTTON_WIDTH - 10;
				height 	= ControlConstants.BUTTON_HEIGHT;
			}
//			
			if(isVoid && Util.isSmallerResolution()){
				SDSControlFactory.setTouchScreenImageLabelProperties(lblVoid);
				GridData gdBtnVoid = (GridData) lblVoid.getLayoutData();
				gdBtnVoid.widthHint  = width;
				gdBtnVoid.heightHint = height;
				lblVoid.setLayoutData(gdBtnVoid);
				SDSControlFactory.setTouchScreenImageLabelProperties(lblCancel);
				GridData gdBtnCancel = (GridData) lblCancel.getLayoutData();
				gdBtnCancel.widthHint  = width;
				gdBtnCancel.heightHint = height;
				lblCancel.setLayoutData(gdBtnCancel);
			}
			else if( Util.isSmallerResolution() ) {
				SDSControlFactory.setTouchScreenImageLabelProperties(lblMultiRedeem);
				GridData gdBtnMultiRedeem = (GridData) lblMultiRedeem.getLayoutData();
				gdBtnMultiRedeem.widthHint  = width;
				gdBtnMultiRedeem.heightHint = height;
				lblMultiRedeem.setLayoutData(gdBtnMultiRedeem);
				SDSControlFactory.setTouchScreenImageLabelProperties(lblOverride);
				
				GridData gdBtnOverride = (GridData) lblOverride.getLayoutData();			
				gdBtnOverride.widthHint  = width;
				gdBtnOverride.heightHint = height;
				lblOverride.setLayoutData(gdBtnOverride);
				SDSControlFactory.setTouchScreenImageLabelProperties(lblRedeem);
				
				GridData gdBtnRedeem = (GridData) lblRedeem.getLayoutData();			
				gdBtnRedeem.widthHint  = width;
				gdBtnRedeem.heightHint = height;
				lblRedeem.setLayoutData(gdBtnRedeem);
				SDSControlFactory.setTouchScreenImageLabelProperties(lblCancelProcess);
				
				GridData gdBtnCancelProcess = (GridData) lblCancelProcess.getLayoutData();			
				gdBtnCancelProcess.widthHint  = width;
				gdBtnCancelProcess.heightHint = height;
				lblCancelProcess.setLayoutData(gdBtnCancelProcess);	
				SDSControlFactory.setTouchScreenImageLabelProperties(lblCancel);
				
				GridData gdBtnCancel = (GridData) lblCancel.getLayoutData();			
				gdBtnCancel.widthHint  = width;
				gdBtnCancel.heightHint = height;
				lblCancel.setLayoutData(gdBtnCancel);
			}
			
			if( Util.isSmallerResolution() ) {
				GridData gdText = (GridData) txtBarCode.getLayoutData();
				gdText.widthHint  = 160;
				gdText.heightHint = 22;
				txtBarCode.setLayoutData(gdText);

				GridData gdTextAmt = (GridData) txtAmount.getLayoutData();
				gdTextAmt.widthHint  = 160;
				gdTextAmt.heightHint = 22;
				txtAmount.setLayoutData(gdTextAmt);

				GridData gdState = (GridData) txtState.getLayoutData();
				gdState.widthHint  = 160;
				gdState.heightHint = 22;
				txtState.setLayoutData(gdState);

				GridData gdEffDate = (GridData) txtEffectiveDate.getLayoutData();
				gdEffDate.widthHint  = 160;
				gdEffDate.heightHint = 22;
				txtEffectiveDate.setLayoutData(gdEffDate);
				
				GridData gdExpDate = (GridData) txtExpireDate.getLayoutData();
				gdExpDate.widthHint  = 160;
				gdExpDate.heightHint = 22;
				txtExpireDate.setLayoutData(gdExpDate);
				
				GridData gdLocation = (GridData) txtLocation.getLayoutData();
				gdLocation.widthHint  = 160;
				gdLocation.heightHint = 22;
				txtLocation.setLayoutData(gdLocation);
				
				GridData gdPlayerId = (GridData) txtPlayerId.getLayoutData();
				gdPlayerId.widthHint  = 160;
				gdPlayerId.heightHint = 22;
				txtPlayerId.setLayoutData(gdPlayerId);
			}

			GridData gdTxtBarcode = (GridData)txtBarCode.getLayoutData();
			if( Util.isSmallerResolution() ) {
				gdTxtBarcode.horizontalIndent = 30;
			} else
				gdTxtBarcode.horizontalIndent = 100;
			txtBarCode.setLayoutData(gdTxtBarcode);
			GridData gdlblBarcode = (GridData)(lblBarCode.getLabel()).getLayoutData();
			gdlblBarcode.horizontalIndent = 0;
			
			(lblBarCode.getLabel()).setLayoutData(gdlblBarcode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the txtBarCode
	 */
	public SDSTSText getTxtBarCode() {
		return txtBarCode;
	}

	/**
	 * @param txtBarCode the txtBarCode to set
	 */
	public void setTxtBarCode(SDSTSText txtBarCode) {
		this.txtBarCode = txtBarCode;
	}

	/**
	 * @return the txtAmount
	 */
	public SDSTSText getTxtAmount() {
		return txtAmount;
	}

	/**
	 * @param txtAmount the txtAmount to set
	 */
	public void setTxtAmount(SDSTSText txtAmount) {
		this.txtAmount = txtAmount;
	}

	/**
	 * @return the txtState
	 */
	public SDSTSText getTxtState() {
		return txtState;
	}

	/**
	 * @param txtState the txtState to set
	 */
	public void setTxtState(SDSTSText txtState) {
		this.txtState = txtState;
	}

	/**
	 * @return the txtEffectiveDate
	 */
	public SDSTSText getTxtEffectiveDate() {
		return txtEffectiveDate;
	}

	/**
	 * @param txtEffectiveDate the txtEffectiveDate to set
	 */
	public void setTxtEffectiveDate(SDSTSText txtEffectiveDate) {
		this.txtEffectiveDate = txtEffectiveDate;
	}

	/**
	 * @return the txtExpireDate
	 */
	public SDSTSText getTxtExpireDate() {
		return txtExpireDate;
	}

	/**
	 * @param txtExpireDate the txtExpireDate to set
	 */
	public void setTxtExpireDate(SDSTSText txtExpireDate) {
		this.txtExpireDate = txtExpireDate;
	}

	/**
	 * @return the txtLocation
	 */
	public SDSTSText getTxtLocation() {
		return txtLocation;
	}

	/**
	 * @param txtLocation the txtLocation to set
	 */
	public void setTxtLocation(SDSTSText txtLocation) {
		this.txtLocation = txtLocation;
	}

	/**
	 * @return the txtPlayerId
	 */
	public SDSTSText getTxtPlayerId() {
		return txtPlayerId;
	}

	/**
	 * @param txtPlayerId the txtPlayerId to set
	 */
	public void setTxtPlayerId(SDSTSText txtPlayerId) {
		this.txtPlayerId = txtPlayerId;
	}

	/**
	 * @return the btnComposite
	 */
	public Composite getBtnComposite() {
		return btnComposite;
	}

	/**
	 * @param btnComposite the btnComposite to set
	 */
	public void setBtnComposite(Composite btnComposite) {
		this.btnComposite = btnComposite;
	}

	public SDSImageLabel getLblSubmitBarcode() {
		return lblSubmitBarcode;
	}

	public void setLblSubmitBarcode(SDSImageLabel lblSubmitBarcode) {
		this.lblSubmitBarcode = lblSubmitBarcode;
	}

	public SDSImageLabel getLblRedeem() {
		return lblRedeem;
	}

	public void setLblRedeem(SDSImageLabel lblRedeem) {
		this.lblRedeem = lblRedeem;
	}

	public SDSImageLabel getLblMultiRedeem() {
		return lblMultiRedeem;
	}

	public void setLblMultiRedeem(SDSImageLabel lblMultiRedeem) {
		this.lblMultiRedeem = lblMultiRedeem;
	}

	public SDSImageLabel getLblOverride() {
		return lblOverride;
	}

	public void setLblOverride(SDSImageLabel lblOverride) {
		this.lblOverride = lblOverride;
	}

	public SDSImageLabel getLblCancelProcess() {
		return lblCancelProcess;
	}

	public void setLblCancelProcess(SDSImageLabel lblCancelProcess) {
		this.lblCancelProcess = lblCancelProcess;
	}

	public SDSImageLabel getLblCancel() {
		return lblCancel;
	}

	public void setLblCancel(SDSImageLabel lblCancel) {
		this.lblCancel = lblCancel;
	}

	public SDSImageLabel getLblVoid() {
		return lblVoid;
	}

	public void setLblVoid(SDSImageLabel lblVoid) {
		this.lblVoid = lblVoid;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the isOverride
	 */
	public boolean isOverride() {
		return isOverride;
	}

	/**
	 * @param isOverride the isOverride to set
	 */
	public void setOverride(boolean isOverride) {
		this.isOverride = isOverride;
	}

	/**
	 * @return the multiRedeemComposite
	 */
	public Composite getMultiRedeemComposite() {
		return multiRedeemComposite;
	}

	/**
	 * @param multiRedeemComposite the multiRedeemComposite to set
	 */
	public void setMultiRedeemComposite(Composite multiRedeemComposite) {
		this.multiRedeemComposite = multiRedeemComposite;
	}

	/**
	 * @return the buttonImage
	 */
	public Image getButtonImage() {
		return buttonImage;
	}

	/**
	 * @param buttonImage the buttonImage to set
	 */
	public void setButtonImage(Image buttonImage) {
		this.buttonImage = buttonImage;
	}

	/**
	 * @return the buttonDisableImage
	 */
	public Image getButtonDisableImage() {
		return buttonDisableImage;
	}

	/**
	 * @param buttonDisableImage the buttonDisableImage to set
	 */
	public void setButtonDisableImage(Image buttonDisableImage) {
		this.buttonDisableImage = buttonDisableImage;
	}
	
}
