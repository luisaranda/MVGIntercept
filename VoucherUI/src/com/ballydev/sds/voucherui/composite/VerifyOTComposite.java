/*****************************************************************************
 * $Id: VerifyOTComposite.java,v 1.2, 2008-12-22 10:12:15Z, Nithyakalyani, Raman$Date: 
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ITSConstants;
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.framework.control.SDSTSMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.VoucherUtil;

public class VerifyOTComposite extends Composite {
	private SDSTSMandatoryLabel lblTicketBarcode = null;
	private SDSTSMandatoryLabel lblTicketAmount = null;
	private SDSTSText txtTicketBarcode= null;
	private SDSTSText txtTicketAmount= null;
	private SDSTSButton btnVerify = null;
	private SDSTSButton btnRedeem = null;
	private SDSTSButton btnCancel = null;

	public VerifyOTComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		this.setBounds(0,0, 800,600);
	}

	private void initialize()
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.horizontalSpacing = 5;

		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.verticalAlignment = GridData.FILL;

		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_VERIFY_OFFLINE_TICKET));
		createMainComposite();

		this.setBackground(VoucherUtil.getDefaultBackGroudColor());
		this.setLayout(gridLayout);
		this.setLayoutData(gridData21);
		this.setLayout(gridLayout);
		layout();
	}

	private void createMainComposite() 
	{
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = GridData.CENTER;
		gridData3.horizontalAlignment = GridData.CENTER;

		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gridLayout3.verticalSpacing = 15;
		gridLayout3.horizontalSpacing = 20;

		Composite mainComposite = null;
		mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setBackground(VoucherUtil.getDefaultBackGroudColor());
		mainComposite.setLayout(gridLayout3);
		mainComposite.setLayoutData(gridData3);

		GridData gridDataTxt = new GridData();
		gridDataTxt.horizontalAlignment = GridData.BEGINNING;
		gridDataTxt.grabExcessHorizontalSpace = true;
		gridDataTxt.grabExcessVerticalSpace = true;
		gridDataTxt.verticalAlignment = GridData.CENTER;
		gridDataTxt.heightHint = ITSConstants.SDSTS_TXT_DEFAULT_HEIGHT;
		gridDataTxt.widthHint = ITSConstants.SDSTS_TXT_DEFAULT_WIDTH;


		GridData gridDataLblBarcode = new GridData();
		gridDataLblBarcode.horizontalAlignment = GridData.FILL;
		gridDataLblBarcode.grabExcessHorizontalSpace = true;
		gridDataLblBarcode.grabExcessVerticalSpace = true;
		gridDataLblBarcode.verticalAlignment = GridData.CENTER;

		lblTicketBarcode = new SDSTSMandatoryLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BAR_CODE));
		lblTicketBarcode.setLayoutData(gridDataLblBarcode);

		txtTicketBarcode = new SDSTSText(mainComposite,0,"ticketBarcodeValue", "ticketBarcodeValue", true);
		txtTicketBarcode.setLayoutData(gridDataTxt);


		GridData gridDataTicketAmount = new GridData();
		gridDataTicketAmount.horizontalAlignment = GridData.FILL;
		gridDataTicketAmount.grabExcessHorizontalSpace = true;
		gridDataTicketAmount.grabExcessVerticalSpace = true;
		gridDataTicketAmount.verticalAlignment = GridData.CENTER;

		lblTicketAmount = new SDSTSMandatoryLabel(mainComposite, SWT.NONE, AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		lblTicketAmount.setLayoutData(gridDataTicketAmount);

		txtTicketAmount = new SDSTSText(mainComposite,0,"ticketAmountValue", "ticketAmountValue",true);
		txtTicketAmount.setLayoutData(gridDataTxt);

		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.horizontalSpan = 2;
		gridData4.horizontalAlignment = GridData.CENTER;

		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 3;
		gridLayout4.marginWidth = 4;
		gridLayout4.marginHeight = 15;
		gridLayout4.verticalSpacing = 5;
		gridLayout4.horizontalSpacing = 25;

		Composite btnComposite = null;
		btnComposite = new Composite(this, SWT.NONE);
		btnComposite.setBackground(VoucherUtil.getDefaultBackGroudColor());
		btnComposite.setLayout(gridLayout4);
		btnComposite.setLayoutData(gridData4);

		GridData gridDataBtn = new GridData();
		gridDataBtn.grabExcessVerticalSpace = true;
		gridDataBtn.horizontalAlignment = GridData.CENTER;
		gridDataBtn.verticalAlignment = GridData.END;
		gridDataBtn.heightHint = ITSConstants.SDSTS_BTN_DEFAULT_HEIGHT;//100;
		gridDataBtn.widthHint = ITSConstants.SDSTS_BTN_DEFAULT_WIDTH;//100;
		gridDataBtn.grabExcessHorizontalSpace = true;



		btnVerify = new SDSTSButton(btnComposite, SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VERIFY),IVoucherConstants.VERIFYOT_CTRL_BTN_VERIFY);
		btnVerify.setLayoutData(gridDataBtn);

		btnRedeem = new SDSTSButton(btnComposite, SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CASH_OUT),IVoucherConstants.VERIFYOT_CTRL_BTN_REDEEM);
		btnRedeem.setLayoutData(gridDataBtn);
		btnRedeem.setEnabled(false);//By default it is disabled. It will be enabled upon successful verification.

		btnCancel = new SDSTSButton(btnComposite, SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CANCEL),IVoucherConstants.VERIFYOT_CTRL_BTN_CANCEL);
		btnCancel.setLayoutData(gridDataBtn);
	}

	public SDSTSText getTxtTicketAmount() 
	{
		return txtTicketAmount;
	}

	public void setTxtTicketAmount(SDSTSText txtTicketAmount) 
	{
		this.txtTicketAmount = txtTicketAmount;
	}

	public SDSTSText getTxtTicketBarcode() 
	{
		return txtTicketBarcode;
	}

	public void setTxtTicketBarcode(SDSTSText txtTicketBarcode) 
	{
		this.txtTicketBarcode = txtTicketBarcode;
	}

	public SDSTSButton getBtnCancel() 
	{
		return btnCancel;
	}

	public void setBtnCancel(SDSTSButton btnCancel) 
	{
		this.btnCancel = btnCancel;
	}

	public SDSTSButton getBtnRedeem() 
	{
		return btnRedeem;
	}

	public void setBtnRedeem(SDSTSButton btnRedeem) 
	{
		this.btnRedeem = btnRedeem;
	}

	public SDSTSButton getBtnVerify() 
	{
		return btnVerify;
	}

	public void setBtnVerify(SDSTSButton btnVerify) 
	{
		this.btnVerify = btnVerify;
	}
}

