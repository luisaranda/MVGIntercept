/*****************************************************************************
 * $Id: VoucherHomeComposite.java,v 1.4, 2010-01-12 08:43:47Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:43:47 AM$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.constant.ITSConstants;
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSLabelStyle;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This is the composite having the voucher's functionality buttons
 */

public class VoucherHomeComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {
	/**
	 * HeaderComposite Instance
	 */
	private Composite middlerComposite = null;

	/**
	 * MiddleComposite Instance
	 */
	private Composite headerComposite = null;

	/**
	 * FooterComposite Instance
	 */
	private Composite footerComposite = null;

	/**
	 * Logger Instance
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Title1 Label instance.
	 */
	private SDSTSLabel lblTitle1 = null;

	/**
	 * MainMenuComposite Constructor
	 * 
	 * @param parent
	 * @param style
	 */

	private SDSTSButton btnRedeemVoucher = null;
	private SDSTSButton btnPrintVoucher = null;
	private SDSTSButton btnVoidVoucher = null;
	private SDSTSButton btnReconciliation = null;
	private SDSTSButton btnAdministration = null;
	private SDSTSButton btnLogOut = null;
	private SDSTSButton btnVerifyOT = null;


	public VoucherHomeComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		log.debug("------ VoucherHomeComposite created");
		setSize(new Point(800, 600));
	}

	/**
	 * initialize method
	 */
	private void initialize() 
	{
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.verticalSpacing = 0;

		createHeaderComposite();
		createMiddleComposite();
		createFooterComposite();

		this.setBackground(VoucherUtil.getHPDefaultBackGroudColor());
		this.setLayout(gridLayout1);
		layout();
	}

	/**
	 * This method initializes HeaderComposite
	 * 
	 */
	private void createHeaderComposite() 
	{
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 20;
		gridLayout3.numColumns = 4;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.heightHint = 150;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.horizontalAlignment = GridData.FILL;

		headerComposite = new Composite(this, SWT.NONE);
		headerComposite.setBackground(VoucherUtil.getHPDefaultBackGroudColor());
		headerComposite.setLayout(gridLayout3);
		headerComposite.setLayoutData(gridData5);

		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = GridData.CENTER;
		gridDataLbl.verticalAlignment = GridData.BEGINNING;
		gridDataLbl.widthHint = ITSConstants.SDSTS_LBL_DEFAULT_WIDTH;


		GridData gridDataBtn = new GridData();
		gridDataBtn.grabExcessVerticalSpace = true;
		gridDataBtn.horizontalAlignment = GridData.CENTER;
		gridDataBtn.verticalAlignment = GridData.CENTER;
		gridDataBtn.heightHint = ICompositeStyleConstants.TOUCHSCREEN_BUTTON_HEIGHT;//100;
		gridDataBtn.widthHint = ICompositeStyleConstants.TOUCHSCREEN_BUTTON_WIDTH;//100;
		gridDataBtn.grabExcessHorizontalSpace = true;

		btnRedeemVoucher = new SDSTSButton(headerComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_REDEEM_VOUCHER), VOUCHERHOME_CTRL_BTN_REDEEMVOUCHER);
		//btnRedeemVoucher.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REDEEM_VOUCHER_BUTTON_IMG)));
		btnRedeemVoucher.setLayoutData(gridDataBtn);

		btnPrintVoucher = new SDSTSButton(headerComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_PRINT_VOUCHER)+AppContextValues.getInstance().getTicketText() , VOUCHERHOME_CTRL_BTN_PRINTVOUCHER);
		//btnPrintVoucher.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.PRINT_VOUCHER_BUTTON_IMG)));
		btnPrintVoucher.setLayoutData(gridDataBtn);

		btnVoidVoucher = new SDSTSButton(headerComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_VOID_VOUCHER), VOUCHERHOME_CTRL_BTN_VOIDVOUCHER);
		//btnVoidVoucher.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.VOID_VOUCHER_BUTTON_IMG)));
		btnVoidVoucher.setLayoutData(gridDataBtn);

		btnVerifyOT = new SDSTSButton(headerComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_VERIFY_OFFLINE), VOUCHERHOME_CTRL_BTN_VERIFY_OT);
		btnVerifyOT.setLayoutData(gridDataBtn);

		/*btnReports = new SDSTSButton(headerComposite, SWT.NONE, LBL_REPORTS, VOUCHERHOME_CTRL_BTN_REPORTS);
        //btnReports.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.REPORT_BUTTON_IMG)));
        btnReports.setLayoutData(gridDataBtn);

        lblRedeemVoucher = new SDSTSLabel(headerComposite, SWT.CENTER,LBL_REDEEM_VOUCHER);
        lblRedeemVoucher.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblRedeemVoucher.setLayoutData(gridDataLbl);

        lblPrintVoucher = new SDSTSLabel(headerComposite, SWT.CENTER,LBL_PRINT_VOUCHER);
        lblPrintVoucher.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblPrintVoucher.setLayoutData(gridDataLbl);

        lblVoidVoucher = new SDSTSLabel(headerComposite, SWT.CENTER,LBL_VOID_VOUCHER);
        lblVoidVoucher.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblVoidVoucher.setLayoutData(gridDataLbl);

        lblReports = new SDSTSLabel(headerComposite, SWT.CENTER,LBL_REPORTS);
        lblReports.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblReports.setLayoutData(gridDataLbl);*/
	}

	/**
	 * This method initializes MiddleComposite
	 * 
	 */
	private void createMiddleComposite() 
	{

		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = GridData.CENTER;
		gridData7.verticalAlignment = GridData.CENTER;
		gridData7.grabExcessVerticalSpace = true;

		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.heightHint = 400;
		gridData21.verticalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 50;
		gridLayout.marginWidth = 20;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 35;
		middlerComposite = new Composite(this, SWT.NONE);
		middlerComposite.setBackground(VoucherUtil.getDefaultBackGroudColor());
		middlerComposite.setLayoutData(gridData21);
		middlerComposite.setLayout(gridLayout);

		lblTitle1 = new SDSTSLabel(middlerComposite, SWT.CENTER);
		lblTitle1.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_HOME_PAGE_NOTE)+AppContextValues.getInstance().getTicketText());
		lblTitle1.setStyle(SDSTSLabelStyle.HEADING);
		lblTitle1.setLayoutData(gridData7);

	}

	/**
	 * This method initializes FooterComposite
	 * 
	 */
	private void createFooterComposite()
	{
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 30;
		gridLayout2.marginHeight = 20;
		gridLayout2.marginWidth = 20;
		gridLayout2.numColumns = 3;

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 150;
		gridData.horizontalAlignment = GridData.FILL;
		footerComposite = new Composite(this, SWT.NONE);
		footerComposite.setBackground(VoucherUtil.getHPDefaultBackGroudColor());
		footerComposite.setLayout(gridLayout2);
		footerComposite.setLayoutData(gridData);

		GridData gridDataLbl = new GridData();
		gridDataLbl.horizontalAlignment = GridData.CENTER;
		gridDataLbl.verticalAlignment = GridData.BEGINNING;
		gridDataLbl.widthHint = 150;

		GridData gridDataBtn = new GridData();
		gridDataBtn.grabExcessVerticalSpace = true;
		gridDataBtn.horizontalAlignment = GridData.CENTER;
		gridDataBtn.verticalAlignment = GridData.CENTER;
		gridDataBtn.heightHint = ICompositeStyleConstants.TOUCHSCREEN_BUTTON_HEIGHT;//100;
		gridDataBtn.widthHint = ICompositeStyleConstants.TOUCHSCREEN_BUTTON_WIDTH;//100;
		gridDataBtn.grabExcessHorizontalSpace = true;

		btnReconciliation = new SDSTSButton(footerComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_RECONCILIATION), VOUCHERHOME_CTRL_BTN_RECONCILIATION);
		//btnReconciliation.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.RECONCILIATION_VOUCHER_BUTTON_IMG)));
		btnReconciliation.setLayoutData(gridDataBtn);

		btnAdministration = new SDSTSButton(footerComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_ADMINISTRATION), VOUCHERHOME_CTRL_BTN_ADMINISTRATION);
		//btnAdministration.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.ADMINISTRATOR_BUTTON_IMG)));
		btnAdministration.setLayoutData(gridDataBtn);

		btnLogOut = new SDSTSButton(footerComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_LOG_OUT), VOUCHERHOME_CTRL_BTN_LOGOUT);
		//btnLogOut.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.LOGOUT_BUTTON_IMG)));
		btnLogOut.setLayoutData(gridDataBtn);

		btnAdministration.setEnabled(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_ADMINISTRATOR));
		btnReconciliation.setEnabled(VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_TKT_RECONCILATION));

		/*lblReconciliation = new SDSTSLabel(footerComposite, SWT.CENTER,LBL_RECONCILIATION);
        lblReconciliation.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblReconciliation.setLayoutData(gridDataLbl);

        lblAdministration = new SDSTSLabel(footerComposite, SWT.CENTER,LBL_ADMINISTRATION);
        lblAdministration.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblAdministration.setLayoutData(gridDataLbl);

        lblLogOut = new SDSTSLabel(footerComposite, SWT.CENTER,LBL_LOG_OUT);
        lblLogOut.setStyle(SDSTSLabelStyle.SUBHEADING);
        lblLogOut.setLayoutData(gridDataLbl);	*/
	}
}
