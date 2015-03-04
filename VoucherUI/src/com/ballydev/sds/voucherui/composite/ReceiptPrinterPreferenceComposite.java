/*****************************************************************************
 * $Id: ReceiptPrinterPreferenceComposite.java,v 1.22, 2011-01-04 06:02:33Z, Bhandari, Vineet$
 * $Date: 1/4/2011 12:02:33 AM$
 *****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
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
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSCombo;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

/**
 * This creates the receipt preference page
 * @author Nithya kalyani R
 * @version $Revision: 23$ 
 */
public class ReceiptPrinterPreferenceComposite extends Composite {

	private SDSTSLabel lblPrinterPort;

	private SDSTSLabel lblPrinterType;

	private SDSTSLabel lblManufacturer;

	private SDSTSLabel lblModel;

	private SDSTSLabel lblDriver;

	private SDSTSCombo<ComboLabelValuePair> comboPrinterPort ;

	private SDSTSCombo<ComboLabelValuePair> comboPrinterType ;

	private SDSTSText txtManufacturer;

	private SDSTSText txtModel;

	private SDSTSText txtDriver;

	private Composite detailsCompoiste;

	private SDSTSCheckBox chkReceiptPrinting;

	private SDSImageLabel btnReceiptPrinterTest;

	private Image buttonImage;

	private Image buttonDisableImage;

	private Image noteImage;
	
	private Image checkImage;
	
	private SDSTSLabel lblReceiptPrinting = null;
	
	Composite noteComposite = null;

	/**
	 * Instance of the log out msg label
	 */
	private SDSTSLabel lblLogOutMsg = null;

	public ReceiptPrinterPreferenceComposite(Composite parent, int style){
		super(parent, style);
		noteImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.NOTE_IMAGE));
		initialize();
		this.setBounds(0,0, 800,700);
	}

	private void initialize() {
		createImages();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 5;
		gridLayout.horizontalSpacing = 5;

		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.verticalAlignment = GridData.FILL;

		this.setLayout(gridLayout);
		this.setLayoutData(gridData21);
		createDetailsComposite();
		setControlProperties();
		layout();
	}

	private void createDetailsComposite(){

		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData containerData = new GridData();
		containerData.widthHint = 655;
		
		if(Util.isSmallerResolution())
			containerData.heightHint = 530;
		else 
			containerData.heightHint = 498;
		
		containerData.horizontalIndent = 0;
		containerData.verticalIndent = 0;
		container.setLayoutData(containerData);
		
		GridLayout containerLayout = new GridLayout(2, true);
		containerLayout.marginTop = 0;
		containerLayout.marginLeft = 0;
		containerLayout.marginRight = 0;
		containerLayout.marginBottom = 0;
		containerLayout.marginHeight = 0;
		containerLayout.marginWidth = 0;
		container.setLayout(containerLayout);
		
		Composite header = new Composite(container, SWT.NONE);
		GridData headerData = new GridData();
		headerData.widthHint = 653;
		if(Util.isSmallerResolution())
			headerData.heightHint = 30;
		else 
			headerData.heightHint = 30;
		headerData.horizontalIndent = 1;
		headerData.horizontalSpan = 2;
		headerData.verticalIndent = 1;
		headerData.horizontalAlignment = GridData.BEGINNING;
		headerData.verticalAlignment = GridData.BEGINNING;
		headerData.grabExcessHorizontalSpace = false;
		headerData.grabExcessVerticalSpace = false;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getButtonForegroundColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_RECEIPT_PRINTER_TITLE));
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		chkRecieptPrinter(container);
		
		@SuppressWarnings("unused")
		SDSTSLabel tsLbl = new SDSTSLabel(container, SWT.NONE,"");

		GridData grdDetailsComposite = new GridData();
		grdDetailsComposite.grabExcessHorizontalSpace = true;
		grdDetailsComposite.verticalAlignment = GridData.CENTER;
		grdDetailsComposite.grabExcessVerticalSpace = false;
		grdDetailsComposite.horizontalAlignment = GridData.CENTER;

		GridLayout grlDetailsComposite = new GridLayout();
		grlDetailsComposite.numColumns = 2;
		grlDetailsComposite.verticalSpacing = 10;
		if( Util.isSmallerResolution() ) {
			grlDetailsComposite.verticalSpacing = 5;
		}
		grlDetailsComposite.horizontalSpacing = 10;

		detailsCompoiste = new Composite(container, SWT.NONE);
		detailsCompoiste.setLayoutData(grdDetailsComposite);
		detailsCompoiste.setLayout(grlDetailsComposite);

		lblPrinterPort = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PRINTER_PORT) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboPrinterPort = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste, IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_SELECTED_PRINTER_PORT, "label", "value", IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_PRINTER_PORT);
			comboPrinterPort.getLeft().setLayoutData(gd1);
			comboPrinterPort.getRight().setLayoutData(gd1);
		} else {
			comboPrinterPort = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste, IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_SELECTED_PRINTER_PORT, "label", "value", IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_PRINTER_PORT);
		}

		lblPrinterType = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PRINTER_TYPE) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
	
		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboPrinterType = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_SELECTED_PRINTER_TYPE, "label", "value", IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_PRINTER_TYPE);
			comboPrinterType.getLeft().setLayoutData(gd1);
			comboPrinterType.getRight().setLayoutData(gd1);
		} else {
			comboPrinterType = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_SELECTED_PRINTER_TYPE, "label", "value", IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_CMB_PRINTER_TYPE);
		}

		lblManufacturer = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MANUFACTURER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		txtManufacturer = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_MANUFACTURER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_TXT_MANUFACTURER);
		txtManufacturer.setEditable(false);

		lblModel = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MODEL) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		txtModel = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_MODEL,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_TXT_MODEL);
		txtModel.setEditable(false);

		lblDriver = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_DRIVER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		txtDriver = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_DRIVER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_TXT_DRIVER);
		txtDriver.setEditable(false);

		GridData gridData5 = new GridData();
		gridData5.heightHint = buttonImage.getBounds().height;
		gridData5.widthHint = buttonImage.getBounds().width;
		gridData5.horizontalAlignment = GridData.END;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.grabExcessHorizontalSpace = false;
		
		btnReceiptPrinterTest = new SDSImageLabel(detailsCompoiste, SWT.NONE, buttonImage, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_RECEIPT_PRINTING_TEST),IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_BTN_RECEIPT_PRINTING_TEST);		
		btnReceiptPrinterTest.setLayoutData(gridData5);
		
		noteComposite = new Composite(container, SWT.NONE);
		noteComposite.setBackground(SDSControlFactory.getTSBodyColor());
		GridData gdData = new GridData();
		gdData.horizontalAlignment = GridData.BEGINNING;
		gdData.horizontalSpan = 3;
		gdData.horizontalIndent = 30;
		
		GridData lblGridData = new GridData();
		lblGridData.horizontalAlignment = GridData.CENTER;
		lblGridData.verticalAlignment = GridData.CENTER;
		
		if( Util.isSmallerResolution() ) {
			gdData.widthHint = 588;
			gdData.heightHint = 37;
			lblGridData.verticalIndent = 2;
		} else {
			gdData.widthHint = noteImage.getBounds().width;
			gdData.heightHint = noteImage.getBounds().height;
			lblGridData.verticalIndent = 5;
		}
		
		GridLayout glLayout = new GridLayout();
		glLayout.marginWidth = 2;
		
		noteComposite.setLayoutData(gdData);
		noteComposite.setLayout(glLayout);
		
		lblGridData.widthHint = noteImage.getBounds().width; 
		
		lblLogOutMsg = new SDSTSLabel(noteComposite, SWT.CENTER, "");
		lblLogOutMsg.setLayoutData(lblGridData);
	
	}

	private void chkRecieptPrinter(Composite composite) {
//		GridData gd = new GridData();
//		gd.verticalAlignment = GridData.BEGINNING;
//		gd.horizontalIndent = 20;
//		
//		btnReceiptPrinting.setLayoutData(gd);
		
		
		GridData gdButtonComposite = new GridData();
		gdButtonComposite.grabExcessHorizontalSpace = true;
		gdButtonComposite.verticalAlignment = GridData.BEGINNING;
		gdButtonComposite.grabExcessVerticalSpace = false;
		gdButtonComposite.horizontalAlignment = GridData.BEGINNING;
		if( Util.isSmallerResolution() ) {
			gdButtonComposite.heightHint = 60;
		} else {
			gdButtonComposite.widthHint = 180;
		}
		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 2;
//			grlButtonComposite.verticalSpacing = 5;
		if( Util.isSmallerResolution() ) {
			grlButtonComposite.verticalSpacing = 2;
		}
		grlButtonComposite.horizontalSpacing = 0;
		
		Composite btnComposite = new Composite(composite, SWT.NONE);
		btnComposite.setBackground(SDSControlFactory.getTSBodyColor());
//			btnComposite.setBackground(new Color (Display.getCurrent(), 211,111,112));
		btnComposite.setLayoutData(gdButtonComposite);
		btnComposite.setLayout(grlButtonComposite);
		
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalIndent = 10;
		
		chkReceiptPrinting = new SDSTSCheckBox(btnComposite, SWT.NONE, "", LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_RECEIPT_PRINTING), false);
		chkReceiptPrinting.setName(IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_BTN_RECEIPT_PRINTING);
		chkReceiptPrinting.setImage(checkImage);
		
		lblReceiptPrinting = new SDSTSLabel(btnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_RECEIPT_PRINTING));
		lblReceiptPrinting.setLayoutData(gd);
	}

	/**
	 * This method sets the controls grid properties
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblDriver);
			SDSControlFactory.setTouchScreenLabelProperties(lblManufacturer);
			SDSControlFactory.setTouchScreenLabelProperties(lblModel);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrinterPort);
			SDSControlFactory.setTouchScreenLabelProperties(lblPrinterType);
			//SDSControlFactory.setTouchScreenLabelProperties(lblTestStatus);
			SDSControlFactory.setTouchScreenTextProperties(txtDriver);
			SDSControlFactory.setTouchScreenTextProperties(txtManufacturer);
			SDSControlFactory.setTouchScreenTextProperties(txtModel);
			//SDSControlFactory.setTouchScreenTextProperties(txtTestStatus);
//			SDSControlFactory.setTouchScreenButtonProperties(chkReceiptPrinting);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnReceiptPrinterTest);
			SDSControlFactory.setTouchScreenLabelProperties(lblLogOutMsg);

			GridData gdLblLogOut = (GridData)lblLogOutMsg.getLayoutData();
			gdLblLogOut.horizontalSpan=2;
			lblLogOutMsg.setLayoutData(gdLblLogOut);

			GridData gdTktPrintTest = (GridData)btnReceiptPrinterTest.getLayoutData();			
			gdTktPrintTest.horizontalAlignment = SWT.CENTER;
			gdTktPrintTest.horizontalSpan = 2;
			if( Util.isSmallerResolution() ) {
				gdTktPrintTest.widthHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 22;
				gdTktPrintTest.heightHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 18;
			}
			btnReceiptPrinterTest.setLayoutData(gdTktPrintTest);

//			GridData gdReceiptPrint = (GridData)chkReceiptPrinting.getLayoutData();			
//			gdReceiptPrint.widthHint = ControlConstants.EX_BUTTON_WIDTH;
//			gdReceiptPrint.horizontalSpan = 2;
//			gdReceiptPrint.heightHint = 20;
//			if( Util.isSmallerResolution() ) {
//				gdReceiptPrint.heightHint = 15;
//			}
//			chkReceiptPrinting.setLayoutData(gdReceiptPrint);

			GridData gdTxtManufacturer = (GridData)txtManufacturer.getLayoutData();
			gdTxtManufacturer.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtManufacturer.horizontalIndent = 6;
				gdTxtManufacturer.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			} else {
				gdTxtManufacturer.horizontalIndent = 15;
				gdTxtManufacturer.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtManufacturer.setLayoutData(gdTxtManufacturer);
			GridData gdTxtModel = (GridData)txtModel.getLayoutData();
			gdTxtModel.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtModel.horizontalIndent = 6;
				gdTxtModel.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			} else {
				gdTxtModel.horizontalIndent = 15;
				gdTxtModel.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtModel.setLayoutData(gdTxtModel);
			GridData gdTxtPort = (GridData)txtDriver.getLayoutData();
			gdTxtPort.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtPort.horizontalIndent = 6;
				gdTxtPort.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			} else {
				gdTxtPort.horizontalIndent = 15;
				gdTxtPort.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtDriver.setLayoutData(gdTxtPort);
			/*GridData gdTxtTestStatus = (GridData)txtTestStatus.getLayoutData();
			gdTxtTestStatus.widthHint = ControlConstants.EX_TEXT_WIDTH;
			txtTestStatus.setLayoutData(gdTxtTestStatus);*/			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disableControls(){
		comboPrinterPort.getText().setEnabled(false);
		comboPrinterType.getText().setEnabled(false);
		comboPrinterType.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
		comboPrinterType.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
		comboPrinterPort.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
		comboPrinterPort.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
		comboPrinterType .setEnabled(false);
		comboPrinterPort .setEnabled(false);
		txtManufacturer.setEnabled(false);
		txtModel.setEnabled(false);
		txtDriver.setEnabled(false);
		btnReceiptPrinterTest.setEnabled(false);
		btnReceiptPrinterTest.setImage(buttonDisableImage);
		btnReceiptPrinterTest.getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		noteComposite.setBackgroundImage(noteImage);
	}

	public void enableControls(){
		comboPrinterPort.getText().setEnabled(true);
		comboPrinterType.getText().setEnabled(true);
		comboPrinterType.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISPLAY_NEXT_PAGE)));
		comboPrinterType.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISPLAY_PREVIOUS_PAGE)));
		comboPrinterPort.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISPLAY_NEXT_PAGE)));
		comboPrinterPort.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISPLAY_PREVIOUS_PAGE)));
		comboPrinterType .setEnabled(true);
		comboPrinterPort .setEnabled(true);
		txtManufacturer.setEnabled(true);
		txtModel.setEnabled(true);
		txtDriver.setEnabled(true);
		btnReceiptPrinterTest.setEnabled(true);
		btnReceiptPrinterTest.setImage(buttonImage);
		btnReceiptPrinterTest.getTextLabel().setForeground(SDSControlFactory.getEnableButtonTextColor());
		noteComposite.setBackgroundImage(noteImage);
	}

	
	/**
	 * @return the chkReceiptPrinting
	 */
	public SDSTSCheckBox getChkReceiptPrinting() {
		return chkReceiptPrinting;
	}

	/**
	 * @param chkReceiptPrinting the chkReceiptPrinting to set
	 */
	public void setChkReceiptPrinting(SDSTSCheckBox chkReceiptPrinting) {
		this.chkReceiptPrinting = chkReceiptPrinting;
	}

	/**
	 * @return the btnReceiptPrinterTest
	 */
	public SDSImageLabel getBtnReceiptPrinterTest() {
		return btnReceiptPrinterTest;
	}

	/**
	 * @param btnReceiptPrinterTest the btnReceiptPrinterTest to set
	 */
	public void setBtnReceiptPrinterTest(SDSImageLabel btnReceiptPrinterTest) {
		this.btnReceiptPrinterTest = btnReceiptPrinterTest;
	}

	public SDSTSText getTxtDriver() 
	{
		return txtDriver;
	}

	public void setTxtDriver(SDSTSText txtDriver) 
	{
		this.txtDriver = txtDriver;
	}

	public SDSTSText getTxtManufacturer() 
	{
		return txtManufacturer;
	}

	public void setTxtManufacturer(SDSTSText txtManufacturer) 
	{
		this.txtManufacturer = txtManufacturer;
	}

	public SDSTSText getTxtModel() 
	{
		return txtModel;
	}

	public void setTxtModel(SDSTSText txtModel) 
	{
		this.txtModel = txtModel;
	}

	public SDSTSCombo<ComboLabelValuePair> getComboPrinterPort() {
		return comboPrinterPort;
	}

	public void setComboPrinterPort(SDSTSCombo<ComboLabelValuePair> comboPrinterPort) {
		this.comboPrinterPort = comboPrinterPort;
	}

	public SDSTSCombo<ComboLabelValuePair> getComboPrinterType() {
		return comboPrinterType;
	}

	public void setComboPrinterType(SDSTSCombo<ComboLabelValuePair> comboPrinterType) {
		this.comboPrinterType = comboPrinterType;
	}

	/**
	 * @return the lblLogOutMsg
	 */
	public SDSTSLabel getLblLogOutMsg() {
		return lblLogOutMsg;
	}

	/**
	 * @param lblLogOutMsg the lblLogOutMsg to set
	 */
	public void setLblLogOutMsg(SDSTSLabel lblLogOutMsg) {
		this.lblLogOutMsg = lblLogOutMsg;
	}

	/**
	 * @return the detailsCompoiste
	 */
	public Composite getDetailsCompoiste() {
		return detailsCompoiste;
	}

	/**
	 * @param detailsCompoiste the detailsCompoiste to set
	 */
	public void setDetailsCompoiste(Composite detailsCompoiste) {
		this.detailsCompoiste = detailsCompoiste;
	}

	private void createImages() {
		buttonImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
		checkImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED));
	}
}

