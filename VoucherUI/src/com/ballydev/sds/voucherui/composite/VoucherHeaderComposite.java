/*****************************************************************************
 * $Id: VoucherHeaderComposite.java,v 1.22, 2010-06-16 14:57:33Z, Verma, Nitin Kumar$
 * $Date: 6/16/2010 9:57:33 AM$
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

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.ISDSThemeConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSThemeControlFactory;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This is the composite having the voucher's functionality buttons
 * @author VNitinkumar
 */

public class VoucherHeaderComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {

	/**
	 * MiddleComposite Instance
	 */
	private Composite rightComposite = null;

	/**
	 * Instance of redeem voucher icon label
	 */
	private SDSTSLabel lblRedeemVoucherIcon = null;

	/**
	 * Instance of print voucher icon label
	 */
	private SDSTSLabel lblPrintVoucherIcon = null;

	/**
	 * Instance of void voucher icon label
	 */
	private SDSTSLabel lblVoidVoucherIcon = null;

	/**
	 * Instance of reconciliation icon label
	 */
	private SDSTSLabel lblReconciliationIcon = null;

	/**
	 * Instance of reprint voucher icon label
	 */
	//private SDSTSLabel lblReprintIcon = null;

	/**
	 * Instance of reprint voucher icon label
	 */
	private SDSTSLabel lblEnquireIcon = null;

	/**
	 * Instance of redeem voucher icon label
	 */
	private SDSTSLabel lblReportsIcon = null;

	/**
	 * Instance of redeem voucher label
	 */
	private SDSTSLabel lblRedeemVoucher = null;

	/**
	 * Instance of print voucher label
	 */
	private SDSTSLabel lblPrintVoucher = null;

	/**
	 * Instance of void voucher label
	 */
	private SDSTSLabel lblVoidVoucher = null;

	/**
	 * Instance of reconciliation label
	 */
	private SDSTSLabel lblReconciliation = null;

	/**
	 * Instance of reprint voucher label
	 */
	//private SDSTSLabel lblReprint = null;

	/**
	 * Instance of reprint voucher label
	 */
	private SDSTSLabel lblReports = null;
	/**
	 * Instance of reprint voucher label
	 */
	private SDSTSLabel lblEnquire = null;

	/**
	 * Instance of redeem voucher icon label
	 */
	private SDSTSLabel lblOverrideVoucherIcon = null;

	/**
	 * Instance of redeem voucher label
	 */
	private SDSTSLabel lblOverrideVoucher = null;

	/**
	 * Constructor of the class
	 */
	public VoucherHeaderComposite(Composite parent, int style) {
		super(parent, style);
		initialize();		
	}

	/**
	 * initialize method
	 */
	private void initialize() 	{
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 0;
		grlPage.marginWidth = 20;
		grlPage.marginHeight = 0;
		grlPage.horizontalSpacing = 0;

		GridData gdPage = new GridData();
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = false;
		gdPage.horizontalAlignment = GridData.END;
		gdPage.verticalAlignment = GridData.BEGINNING;
		if(Util.isSmallerResolution()) {
			gdPage.heightHint = 90;
		} else {
			gdPage.heightHint = 90;
		}

		createLeftComposite();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		this.setSize(this.getParent().getSize());		
		layout();
	}

	/**
	 * This method initializes HeaderComposite
	 * 
	 */
	private void createLeftComposite() {
		GridLayout grlRightComposite = new GridLayout();
		if(Util.isSmallerResolution())
			grlRightComposite.horizontalSpacing = 10;
		else 
			grlRightComposite.horizontalSpacing = 25;
		
		grlRightComposite.verticalSpacing = 0;
		grlRightComposite.marginHeight = 0;
		grlRightComposite.numColumns = 7;
		grlRightComposite.marginWidth = 10;

		GridData gdRightComposite = new GridData();
		gdRightComposite.grabExcessHorizontalSpace = false;
		gdRightComposite.verticalAlignment = GridData.BEGINNING;
		if(Util.isSmallerResolution())
			gdRightComposite.heightHint = 80;
		else 
			gdRightComposite.heightHint = 90;
		gdRightComposite.grabExcessVerticalSpace = true;
		gdRightComposite.horizontalAlignment = GridData.END;
		if(Util.isSmallerResolution())
			gdRightComposite.verticalIndent = 0;
		else 
			gdRightComposite.verticalIndent = 5;
		rightComposite = new Composite(this, SWT.NONE);
		rightComposite.setLayout(grlRightComposite);
		rightComposite.setLayoutData(gdRightComposite);

		GridData gdLblPrintIcon = new GridData();
		gdLblPrintIcon.grabExcessVerticalSpace = true;
		gdLblPrintIcon.grabExcessHorizontalSpace = true;
		gdLblPrintIcon.horizontalAlignment = GridData.CENTER;
		gdLblPrintIcon.verticalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdLblPrintIcon.widthHint = 50;
		} else { 
			gdLblPrintIcon.widthHint = 60;
		}
		
		GridData gdLblReconciliationIcon = new GridData();
		gdLblReconciliationIcon.grabExcessVerticalSpace = true;
		gdLblReconciliationIcon.grabExcessHorizontalSpace = true;
		gdLblReconciliationIcon.horizontalAlignment = GridData.CENTER;
		gdLblReconciliationIcon.verticalAlignment = GridData.CENTER;
		if(Util.isSmallerResolution()) {
			gdLblReconciliationIcon.widthHint = 50;
		} else {
//			gdLblReconciliationIcon.horizontalIndent = 15;
			gdLblReconciliationIcon.widthHint = 60;
		}
		
		lblRedeemVoucherIcon = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_REDEEM_VOUCHER));
		lblRedeemVoucherIcon.setName(VOUCHERHOME_CTRL_BTN_REDEEMVOUCHER);
		if(Util.isSmallerResolution())
			lblRedeemVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		else 
			lblRedeemVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_INACTIVE_BUTTON_IMG)));
		lblRedeemVoucherIcon.setLayoutData(gdLblPrintIcon);
		lblRedeemVoucherIcon.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));


		lblPrintVoucherIcon = new SDSTSLabel(rightComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_PRINT_VOUCHER)+AppContextValues.getInstance().getTicketText() );
		lblPrintVoucherIcon.setName(VOUCHERHOME_CTRL_BTN_PRINTVOUCHER);
		lblPrintVoucherIcon.setLayoutData(gdLblPrintIcon);
		if(Util.isSmallerResolution())
			lblPrintVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		else 
			lblPrintVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_INACTIVE_BUTTON_IMG)));

		lblPrintVoucherIcon.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));

		
		lblVoidVoucherIcon = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_VOID_VOUCHER));
		lblVoidVoucherIcon.setName(VOUCHERHOME_CTRL_BTN_VOIDVOUCHER);
		if(Util.isSmallerResolution())
			lblVoidVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		else 
			lblVoidVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_INACTIVE_BUTTON_IMG)));
		lblVoidVoucherIcon.setLayoutData(gdLblPrintIcon);
		lblVoidVoucherIcon.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
		
		
		lblReconciliationIcon = new SDSTSLabel(rightComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_RECONCILIATION));
		lblReconciliationIcon.setName(VOUCHERHOME_CTRL_BTN_RECONCILIATION);

		if(Util.isSmallerResolution())
			lblReconciliationIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		else
			lblReconciliationIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_INACTIVE_BUTTON_IMG)));

		lblReconciliationIcon.setLayoutData(gdLblReconciliationIcon);
		lblReconciliationIcon.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));

		lblReportsIcon = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_REPORTS));
		lblReportsIcon.setName(VOUCHERHOME_CTRL_BTN_REPORTS);
		
		if(Util.isSmallerResolution())
			lblReportsIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_INACTIVE_BUTTON_IMG_SMALL)));
		else 
			lblReportsIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_INACTIVE_BUTTON_IMG)));
		
		lblReportsIcon.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
		lblReportsIcon.setLayoutData(gdLblPrintIcon);

		lblEnquireIcon = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_ENQUIRE_VOUCHER)+ AppContextValues.getInstance().getTicketText());
		lblEnquireIcon.setName(VOUCHERHOME_CTRL_BTN_ENQUIREVOUCHER);

		if(Util.isSmallerResolution())
			lblEnquireIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		else
			lblEnquireIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ENQUIRE_VOUCHER_INACTIVE_BUTTON_IMG)));
		
		lblEnquireIcon.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
		lblEnquireIcon.setLayoutData(gdLblPrintIcon);
		
		lblOverrideVoucherIcon = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_OVERRIDE_VOUCHER));
		lblOverrideVoucherIcon.setName(VOUCHERHOME_CTRL_BTN_OVERRIDEVOUCHER);

		if(Util.isSmallerResolution())
			lblOverrideVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG_SMALL)));
		else
			lblOverrideVoucherIcon.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.OVERRIDE_VOUCHER_INACTIVE_BUTTON_IMG)));
		
		lblOverrideVoucherIcon.setForeground(SDSThemeControlFactory.getTouchScreenColor(ISDSThemeConstants.TS_TEXT_COLOR));
		lblOverrideVoucherIcon.setLayoutData(gdLblPrintIcon);

		GridData gridDatalblTxt = new GridData();
		gridDatalblTxt.heightHint = 25;
		gridDatalblTxt.horizontalAlignment = GridData.CENTER;
		gridDatalblTxt.verticalAlignment = GridData.BEGINNING;
		gridDatalblTxt.grabExcessHorizontalSpace = true;

		GridData gridDatalblReconcile = new GridData();        
		gridDatalblReconcile.heightHint = 25;
		gridDatalblReconcile.horizontalAlignment = GridData.BEGINNING;
		gridDatalblReconcile.verticalAlignment = GridData.BEGINNING;
		gridDatalblReconcile.widthHint = 90;
		if(Util.isSmallerResolution()) {
			gridDatalblReconcile.verticalIndent = 3;
		} else {
			gridDatalblReconcile.verticalIndent = 3;
			gridDatalblReconcile.horizontalIndent = 0;
		}
		if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 5 ) {
				gridDatalblReconcile.widthHint = 30;
				gridDatalblReconcile.horizontalAlignment = GridData.CENTER;
			}
		}

		GridData gridData34 = new GridData();
		gridData34.horizontalAlignment = GridData.BEGINNING;
		gridData34.verticalAlignment = GridData.BEGINNING;
		
		GridData gdLblVoid = new GridData();
		gdLblVoid.horizontalAlignment = GridData.BEGINNING;
		gdLblVoid.verticalAlignment = GridData.BEGINNING;
		gdLblVoid.widthHint = 40;
		if(Util.isSmallerResolution()) {
			gdLblVoid.horizontalIndent = 10;
			gdLblVoid.verticalIndent = 3;
		} else {
			gdLblVoid.horizontalIndent = 15;
			gdLblVoid.verticalIndent = 3;
		}
		
		GridData gdLblRedeem = new GridData();
		gdLblRedeem.horizontalAlignment = GridData.BEGINNING;
		gdLblRedeem.verticalAlignment = GridData.BEGINNING;
		gdLblRedeem.widthHint = 55;
		if(Util.isSmallerResolution()) {
			gdLblRedeem.verticalIndent = 3;
			gdLblRedeem.horizontalIndent = 0;
		} else { 
			gdLblRedeem.horizontalIndent = 3;
			gdLblRedeem.verticalIndent = 3;
		}
		if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 5 ) {
				gdLblRedeem.widthHint = 40;
				gdLblRedeem.horizontalAlignment = GridData.CENTER;
			}
		}
		GridData gdLblPrint = new GridData();
		gdLblPrint.horizontalAlignment = GridData.BEGINNING;
		gdLblPrint.verticalAlignment = GridData.BEGINNING;
		gdLblPrint.horizontalIndent = 0;
		if(Util.isSmallerResolution())
			gdLblPrint.verticalIndent = 3;
		else 
			gdLblPrint.verticalIndent = 3;

		GridData gdLblReports = new GridData();
		gdLblReports.horizontalAlignment = GridData.BEGINNING;
		gdLblReports.verticalAlignment = GridData.BEGINNING;
		if(Util.isSmallerResolution()) {
			gdLblReports.horizontalIndent = 0;
			gdLblReports.verticalIndent = 3;
		} else {
			gdLblReports.horizontalIndent = 5;
			gdLblReports.verticalIndent = 3;
		}

		GridData gdLblEnquire = new GridData();
		gdLblEnquire.horizontalAlignment = GridData.BEGINNING;
		gdLblEnquire.verticalAlignment = GridData.BEGINNING;
		if(Util.isSmallerResolution()) {
			gdLblEnquire.horizontalIndent = 2;
			gdLblEnquire.verticalIndent = 3;
		} else {
			gdLblEnquire.horizontalIndent = 5;
			gdLblEnquire.verticalIndent = 3;
		}
		
		GridData gdLblOverride = new GridData();
		gdLblOverride.horizontalAlignment = GridData.BEGINNING;
		gdLblOverride.verticalAlignment = GridData.BEGINNING;
		gdLblOverride.widthHint = 70;
		if(Util.isSmallerResolution()) {
			gdLblOverride.horizontalIndent = 2;
			gdLblOverride.verticalIndent = 3;
		} else {
			gdLblOverride.horizontalIndent = 5;
			gdLblOverride.verticalIndent = 3;
		}
		
		lblRedeemVoucher = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_REDEEM_VOUCHER));
		lblRedeemVoucher.setLayoutData(gdLblRedeem);
		lblRedeemVoucher.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblRedeemVoucher.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
		lblPrintVoucher = new SDSTSLabel(rightComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_PRINT_VOUCHER)+AppContextValues.getInstance().getTicketText() );
		lblPrintVoucher.setLayoutData(gdLblPrint);
		lblPrintVoucher.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblPrintVoucher.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
		lblVoidVoucher = new SDSTSLabel(rightComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_VOID_VOUCHER));
		lblVoidVoucher.setLayoutData(gdLblVoid);
		lblVoidVoucher.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblVoidVoucher.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
		lblReconciliation = new SDSTSLabel(rightComposite, SWT.NONE, 
				LabelLoader.getLabelValue(LBL_RECONCILIATION));
		lblReconciliation.setLayoutData(gridDatalblReconcile);
		lblReconciliation.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblReconciliation.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		/*
		lblVerifyOT = new SDSTSLabel(rightComposite, SWT.NONE, LBL_VERIFY_OFFLINE);
		lblVerifyOT.setLayoutData(gridData34);
		 */

		/*lblReprint = new SDSTSLabel(rightComposite, SWT.NONE, LBL_REPRINT);
		lblReprint.setLayoutData(gdLblReprint);
		 */
		lblReports = new SDSTSLabel(rightComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_REPORTS));
		lblReports.setLayoutData(gdLblReports);
		lblReports.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblReports.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		
		lblEnquire = new SDSTSLabel(rightComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_ENQUIRE_VOUCHER_HEADER));
		lblEnquire.setLayoutData(gdLblEnquire);
		lblEnquire.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblEnquire.setFont(SDSControlFactory.getSmallDefaultBoldFont());
	
		if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 5 ) {
				gdLblEnquire.widthHint = 40;
				gdLblEnquire.horizontalAlignment = GridData.CENTER;
			}
		}
		
		lblOverrideVoucher = new SDSTSLabel(rightComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_OVERRIDE_VOUCHER_HEADER));
		lblOverrideVoucher.setLayoutData(gdLblOverride);
		lblOverrideVoucher.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		if(Util.isSmallerResolution())
			lblOverrideVoucher.setFont(SDSControlFactory.getSmallDefaultBoldFont());
	
	}

	/**
	 * @return the lblRedeemVoucherIcon
	 */
	public SDSTSLabel getLblRedeemVoucherIcon() {
		return lblRedeemVoucherIcon;
	}

	/**
	 * @param lblRedeemVoucherIcon the lblRedeemVoucherIcon to set
	 */
	public void setLblRedeemVoucherIcon(SDSTSLabel lblRedeemVoucherIcon) {
		this.lblRedeemVoucherIcon = lblRedeemVoucherIcon;
	}

	/**
	 * @return the lblPrintVoucherIcon
	 */
	public SDSTSLabel getLblPrintVoucherIcon() {
		return lblPrintVoucherIcon;
	}

	/**
	 * @param lblPrintVoucherIcon the lblPrintVoucherIcon to set
	 */
	public void setLblPrintVoucherIcon(SDSTSLabel lblPrintVoucherIcon) {
		this.lblPrintVoucherIcon = lblPrintVoucherIcon;
	}

	/**
	 * @return the lblVoidVoucherIcon
	 */
	public SDSTSLabel getLblVoidVoucherIcon() {
		return lblVoidVoucherIcon;
	}

	/**
	 * @param lblVoidVoucherIcon the lblVoidVoucherIcon to set
	 */
	public void setLblVoidVoucherIcon(SDSTSLabel lblVoidVoucherIcon) {
		this.lblVoidVoucherIcon = lblVoidVoucherIcon;
	}

	/**
	 * @return the lblReconciliationIcon
	 */
	public SDSTSLabel getLblReconciliationIcon() {
		return lblReconciliationIcon;
	}

	/**
	 * @param lblReconciliationIcon the lblReconciliationIcon to set
	 */
	public void setLblReconciliationIcon(SDSTSLabel lblReconciliationIcon) {
		this.lblReconciliationIcon = lblReconciliationIcon;
	}

	/**
	 * @return the lblRedeemVoucher
	 */
	public SDSTSLabel getLblRedeemVoucher() {
		return lblRedeemVoucher;
	}

	/**
	 * @param lblRedeemVoucher the lblRedeemVoucher to set
	 */
	public void setLblRedeemVoucher(SDSTSLabel lblRedeemVoucher) {
		this.lblRedeemVoucher = lblRedeemVoucher;
	}

	/**
	 * @return the lblPrintVoucher
	 */
	public SDSTSLabel getLblPrintVoucher() {
		return lblPrintVoucher;
	}

	/**
	 * @param lblPrintVoucher the lblPrintVoucher to set
	 */
	public void setLblPrintVoucher(SDSTSLabel lblPrintVoucher) {
		this.lblPrintVoucher = lblPrintVoucher;
	}

	/**
	 * @return the lblVoidVoucher
	 */
	public SDSTSLabel getLblVoidVoucher() {
		return lblVoidVoucher;
	}

	/**
	 * @param lblVoidVoucher the lblVoidVoucher to set
	 */
	public void setLblVoidVoucher(SDSTSLabel lblVoidVoucher) {
		this.lblVoidVoucher = lblVoidVoucher;
	}

	/**
	 * @return the lblReconciliation
	 */
	public SDSTSLabel getLblReconciliation() {
		return lblReconciliation;
	}

	/**
	 * @param lblReconciliation the lblReconciliation to set
	 */
	public void setLblReconciliation(SDSTSLabel lblReconciliation) {
		this.lblReconciliation = lblReconciliation;
	}

	/**
	 * @return the lblReportsIcon
	 */
	public SDSTSLabel getLblReportsIcon() {
		return lblReportsIcon;
	}

	/**
	 * @param lblReportsIcon the lblReportsIcon to set
	 */
	public void setLblReportsIcon(SDSTSLabel lblReportsIcon) {
		this.lblReportsIcon = lblReportsIcon;
	}

	/**
	 * @return the lblReports
	 */
	public SDSTSLabel getLblReports() {
		return lblReports;
	}

	/**
	 * @param lblReports the lblReports to set
	 */
	public void setLblReports(SDSTSLabel lblReports) {
		this.lblReports = lblReports;
	}

	/**
	 * @return the lblEnquireIcon
	 */
	public SDSTSLabel getLblEnquireIcon() {
		return lblEnquireIcon;
	}

	/**
	 * @param lblEnquireIcon the lblEnquireIcon to set
	 */
	public void setLblEnquireIcon(SDSTSLabel lblEnquireIcon) {
		this.lblEnquireIcon = lblEnquireIcon;
	}

	/**
	 * @return the lblEnquire
	 */
	public SDSTSLabel getLblEnquire() {
		return lblEnquire;
	}

	/**
	 * @param lblEnquire the lblEnquire to set
	 */
	public void setLblEnquire(SDSTSLabel lblEnquire) {
		this.lblEnquire = lblEnquire;
	}

	public SDSTSLabel getLblOverrideVoucherIcon() {
		return lblOverrideVoucherIcon;
	}

	public void setLblOverrideVoucherIcon(SDSTSLabel lblOverrideVoucherIcon) {
		this.lblOverrideVoucherIcon = lblOverrideVoucherIcon;
	}

	public SDSTSLabel getLblOverrideVoucher() {
		return lblOverrideVoucher;
	}

	public void setLblOverrideVoucher(SDSTSLabel lblOverrideVoucher) {
		this.lblOverrideVoucher = lblOverrideVoucher;
	}
}
