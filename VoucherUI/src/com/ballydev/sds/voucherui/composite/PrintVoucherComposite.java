/*****************************************************************************
 * $Id: PrintVoucherComposite.java,v 1.29, 2010-11-19 11:43:57Z, Verma, Nitin Kumar$
 * $Date: 11/19/2010 5:43:57 AM$
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
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This is the class that creates the composites for Print voucher.
 * @author VNitinkumar
 */
public class PrintVoucherComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {

	/**
	 * Instance of the ticket amount label
	 */
	private CbctlMandatoryLabel lblTicketAmount = null;

	/**
	 * Instance of the total amount label
	 */
	private CbctlLabel lblTotalAmount = null;

	/**
	 * Instance of the total count label
	 */
	private CbctlLabel lblTotalCount = null;

	/**
	 * Instance of the ticket amount text
	 */
	private SDSTSText txtTicketAmount= null;

	/**
	 * Instance of the total amount text
	 */
	private SDSTSText txtTotalAmount= null;

	/**
	 * Instance of the total count text
	 */
	private SDSTSText txtTotalCount= null;

	/**
	 * Instance of the main composite
	 */
	private Composite mainComposite = null;

	/**
	 * Instance of the button composite
	 */
	private Composite btnComposite = null;

	/**
	 * Instance of the no of tickets label
	 */
	private CbctlMandatoryLabel lblNoOftkts = null;

	/**
	 * Instance of the no of tickets text
	 */
	private SDSTSText txtNoOfTkts= null;

	/**
	 * Instance of the print Image
	 */
	private SDSImageLabel lblBtnPrint;
	
	/**
	 * Instance of the cancel Image
	 */
	private SDSImageLabel lblBtnCancel;
	
	private Image buttonBG;
	
	/**
	 * Constructor of the class
	 */
	public PrintVoucherComposite(Composite parent, int style){
		super(parent, style);
		initialize();
		this.setBounds(0,0, 1000,400);
		this.setBackground(SDSControlFactory.getTSBodyColor());
		layout();
	}

	private void initialize() {
		createImages();
		GridLayout grlPage 		= new GridLayout();
		grlPage.numColumns 		= 1;
		grlPage.verticalSpacing = 5;
		grlPage.marginHeight 	= 1;
		grlPage.marginWidth 	= 1;
		grlPage.horizontalSpacing = 5;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment 			= GridData.FILL;
		gdPage.grabExcessHorizontalSpace 	= true;
		gdPage.grabExcessVerticalSpace 		= true;
		gdPage.verticalAlignment 			= GridData.FILL;
		gdPage.heightHint 					= 300;

		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(LBL_PRINT_VOUCHER_HEADER)+ AppContextValues.getInstance().getTicketText());
		createMainRedeemComposite();
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
	}

	private void createImages() {
		buttonBG = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
	
	public void createMainRedeemComposite()
	{
		GridData gdMainComposite = new GridData();
		gdMainComposite.grabExcessHorizontalSpace = true;
		gdMainComposite.verticalAlignment = GridData.CENTER;
		gdMainComposite.horizontalAlignment = GridData.BEGINNING;
		gdMainComposite.verticalIndent = 15;
		gdMainComposite.heightHint = 300;
		if(Util.isSmallerResolution() || Util.is1024X786Resolution())
			gdMainComposite.horizontalIndent = 0;
		else
			gdMainComposite.horizontalIndent = 50;

		GridLayout grlMainComposite = new GridLayout();
		grlMainComposite.numColumns = 4;
		grlMainComposite.verticalSpacing = 15;
		if( Util.isSmallerResolution() ) {
			grlMainComposite.horizontalSpacing = 40;
		} else if( Util.is1024X786Resolution() ) {
			grlMainComposite.horizontalSpacing = 60;
		} else
			grlMainComposite.horizontalSpacing = 80;
		
		if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 4 && Util.isSmallerResolution() ) {
				grlMainComposite.horizontalSpacing = 15;
			}
		}
		mainComposite = new Composite(this,SWT.NONE);
		mainComposite.setLayout(grlMainComposite);
		mainComposite.setLayoutData(gdMainComposite);


		lblTicketAmount = new CbctlMandatoryLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_ENTER_TICKET_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		lblTicketAmount.setBackground(SDSControlFactory.getTSBodyColor());
		lblTicketAmount.getLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtTicketAmount = new SDSTSText(mainComposite, SWT.NONE | SWT.RIGHT, PRINTVOUCHER_CTRL_TXT_TICKETAMOUNT, PRINTVOUCHER_FRM_TXT_TICKETAMOUNT, true);
		txtTicketAmount.setTextLimit(10);
		txtTicketAmount.setAmountField(true);

		lblNoOftkts = new CbctlMandatoryLabel(mainComposite, SWT.NONE,LabelLoader.getLabelValue(LBL_TOTAL_TKTS_TO_PRINT, new String[] {AppContextValues.getInstance().getTicketText()}));
		lblNoOftkts.setBackground(SDSControlFactory.getTSBodyColor());
		lblNoOftkts.getLabel().setBackground(SDSControlFactory.getTSBodyColor());

		txtNoOfTkts = new SDSTSText(mainComposite, SWT.BORDER | SWT.RIGHT, PRINTVOUCHER_CTRL_TXT_NO_TKTS, PRINTVOUCHER_FRM_TXT_NO_TKTS, true);
		txtNoOfTkts.setTextLimit(2);
		txtNoOfTkts.setTextLimitChkEnabled(true);
		
		createPrintDetailsGroup();
		SDSImageLabel dummy1 = new SDSImageLabel(mainComposite, SWT.CENTER, buttonBG, "dummy1", "dummy1");
		dummy1.setVisible(false);
		createBtnComposite(mainComposite);		
		SDSImageLabel dummy2 = new SDSImageLabel(mainComposite, SWT.CENTER, buttonBG, "dummy2", "dummy2");
		dummy2.setVisible(false);

		lblTotalAmount = new CbctlLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_TOTAL_AMOUNT) + " ("+CurrencyUtil.getCurrencySymbol()+")");
		
		txtTotalAmount = new SDSTSText(mainComposite, SWT.BORDER | SWT.RIGHT, PRINTVOUCHER_CTRL_TXT_TOTAL_AMOUNT, PRINTVOUCHER_FRM_TXT_TOTAL_AMOUNT);
		txtTotalAmount.setEnabled(false);
		txtTotalAmount.setAmountField(true);

		lblTotalCount = new CbctlLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_TOTAL_COUNT));

		txtTotalCount = new SDSTSText(mainComposite, SWT.BORDER | SWT.RIGHT, PRINTVOUCHER_CTRL_TXT_TOTAL_COUNT, PRINTVOUCHER_FRM_TXT_TOTAL_COUNT);
		txtTotalCount.setEnabled(false);
		mainComposite.setTabList(new Control[]{txtTicketAmount, txtNoOfTkts, txtTotalAmount, txtTotalCount});
	}

	private void createPrintDetailsGroup(){		
	}

	/**
	 * This method initializes btnComposite	
	 */
	private void createBtnComposite(Composite mainComposite) 
	{
		GridData gdBtnComposite = new GridData();
		gdBtnComposite.grabExcessHorizontalSpace = true;
		gdBtnComposite.verticalAlignment = GridData.CENTER;
		gdBtnComposite.horizontalSpan = 2;	
		gdBtnComposite.horizontalAlignment = GridData.BEGINNING;
		gdBtnComposite.horizontalIndent = 20;

		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 2;
		grlButtonComposite.marginWidth = 4;
		grlButtonComposite.marginHeight = 15;
		grlButtonComposite.verticalSpacing = 5;
		grlButtonComposite.horizontalSpacing = 5;

		btnComposite = new Composite(mainComposite, SWT.NONE);
		btnComposite.setLayout(grlButtonComposite);
		btnComposite.setLayoutData(gdBtnComposite);
		
		GridData gridData4 = new GridData();
		gridData4.heightHint = buttonBG.getBounds().height;
		gridData4.widthHint = buttonBG.getBounds().width;
		gridData4.horizontalAlignment = GridData.END;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalIndent = 20;

		lblBtnPrint = new SDSImageLabel(btnComposite, SWT.CENTER, buttonBG, 
				LabelLoader.getLabelValue(LBL_PRINT), PRINT_VOUCHER_CTRL_BTN_PRINT);
		lblBtnPrint.getTextLabel().setData("btnSubmit");
		lblBtnPrint.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblBtnPrint.setLayoutData(gridData4);
		}
		lblBtnPrint.setFont(SDSControlFactory.getDefaultBoldFont());
		lblBtnPrint.setBackgroundImage(buttonBG);
		lblBtnPrint.setBounds(buttonBG.getBounds());

		lblBtnCancel = new SDSImageLabel(btnComposite, SWT.CENTER, buttonBG, 
				LabelLoader.getLabelValue(LBL_CANCEL), PRINT_VOUCHER_CTRL_BTN_CANCEL);
		lblBtnCancel.getTextLabel().setData("btnCancel");
		lblBtnCancel.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			lblBtnCancel.setLayoutData(gridData4);
		}
		lblBtnCancel.setFont(SDSControlFactory.getDefaultBoldFont());
		lblBtnCancel.setBackgroundImage(buttonBG);
		lblBtnCancel.setBounds(buttonBG.getBounds());
		
	}

	/**
	 * This method sets the default control grid properties
	 */
	private void setControlProperties(){
		try	{
			SDSControlFactory.setTouchScreenLabelProperties(lblTotalAmount);
			SDSControlFactory.setTouchScreenLabelProperties(lblTotalCount);
			SDSControlFactory.setTouchScreenLabelProperties(lblTicketAmount.getLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblNoOftkts.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtTicketAmount);
			SDSControlFactory.setTouchScreenTextProperties(txtTotalAmount);
			SDSControlFactory.setTouchScreenTextProperties(txtTotalCount);
			SDSControlFactory.setTouchScreenTextProperties(txtNoOfTkts);

			int width  = 0;
			int height = 0;
			if( Util.isSmallerResolution() ) {
				width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 19;
			} else {
				width 	= ControlConstants.BUTTON_WIDTH;
				height 	= ControlConstants.BUTTON_HEIGHT;
			}
			if( Util.isSmallerResolution() ) {
				SDSControlFactory.setTouchScreenImageLabelProperties(lblBtnCancel);
				GridData gdBtnCancel = (GridData)lblBtnCancel.getLayoutData();
				gdBtnCancel.heightHint = height;
				gdBtnCancel.widthHint  = width;
				lblBtnCancel.setLayoutData(gdBtnCancel);
				
				SDSControlFactory.setTouchScreenImageLabelProperties(lblBtnPrint);
				GridData gdBtnPrint = (GridData)lblBtnPrint.getLayoutData();
				gdBtnPrint.heightHint = height;
				gdBtnPrint.widthHint  = width;
				lblBtnPrint.setLayoutData(gdBtnPrint);
				
				GridData gdText = (GridData) txtTotalAmount.getLayoutData();
				gdText.widthHint  = 160;
				gdText.heightHint = 22;
				txtTotalAmount.setLayoutData(gdText);
				
				GridData gdTextAmt = (GridData) txtTicketAmount.getLayoutData();
				gdTextAmt.widthHint  = 160;
				gdTextAmt.heightHint = 22;
				txtTicketAmount.setLayoutData(gdTextAmt);
				
				GridData gdState = (GridData) txtTotalCount.getLayoutData();
				gdState.widthHint  = 160;
				gdState.heightHint = 22;
				txtTotalCount.setLayoutData(gdState);
				
				GridData gdPlayerId = (GridData) txtNoOfTkts.getLayoutData();
				gdPlayerId.widthHint  = 160;
				gdPlayerId.heightHint = 22;
				txtNoOfTkts.setLayoutData(gdPlayerId);
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the txtTicketAmount
	 */
	public SDSTSText getTxtTicketAmount() {
		return txtTicketAmount;
	}

	/**
	 * @param txtTicketAmount the txtTicketAmount to set
	 */
	public void setTxtTicketAmount(SDSTSText txtTicketAmount) {
		this.txtTicketAmount = txtTicketAmount;
	}

	/**
	 * @return the txtTotalAmount
	 */
	public SDSTSText getTxtTotalAmount() {
		return txtTotalAmount;
	}

	/**
	 * @param txtTotalAmount the txtTotalAmount to set
	 */
	public void setTxtTotalAmount(SDSTSText txtTotalAmount) {
		this.txtTotalAmount = txtTotalAmount;
	}

	/**
	 * @return the txtTotalCount
	 */
	public SDSTSText getTxtTotalCount() {
		return txtTotalCount;
	}

	/**
	 * @param txtTotalCount the txtTotalCount to set
	 */
	public void setTxtTotalCount(SDSTSText txtTotalCount) {
		this.txtTotalCount = txtTotalCount;
	}

	/**
	 * @return the txtNoOfTkts
	 */
	public SDSTSText getTxtNoOfTkts() {
		return txtNoOfTkts;
	}

	/**
	 * @param txtNoOfTkts the txtNoOfTkts to set
	 */
	public void setTxtNoOfTkts(SDSTSText txtNoOfTkts) {
		this.txtNoOfTkts = txtNoOfTkts;
	}
	
	/**
	 * @return the lblBtnPrint
	 */
	public SDSImageLabel getLblBtnPrint() {
		return lblBtnPrint;
	}

	/**
	 * @param lblBtnPrint the lblBtnPrint to set
	 */
	public void setLblBtnPrint(SDSImageLabel lblBtnPrint) {
		this.lblBtnPrint = lblBtnPrint;
	}

	/**
	 * @return the lblBtnCancel
	 */
	public SDSImageLabel getLblBtnCancel() {
		return lblBtnCancel;
	}

	/**
	 * @param lblBtnCancel the lblBtnCancel to set
	 */
	public void setLblBtnCancel(SDSImageLabel lblBtnCancel) {
		this.lblBtnCancel = lblBtnCancel;
	}
}
