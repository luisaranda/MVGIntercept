/*****************************************************************************
 * $Id: BarcodeScannerPreferenceComposite.java,v 1.21, 2010-04-08 13:45:07Z, Verma, Nitin Kumar$
 * $Date: 4/8/2010 8:45:07 AM$
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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
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
 * This creates the barcode scanner preference page
 * @author Nithya kalyani R
 * @version $Revision: 22$ 
 */
public class BarcodeScannerPreferenceComposite extends Composite {

	private SDSTSLabel lblTestStatus;

	private SDSTSLabel lblScannerPort;

	private SDSTSLabel lblScannerType;

	private SDSTSLabel lblManufacturer;

	private SDSTSLabel lblModel;

	private SDSTSLabel lblDriver;

	private SDSTSText txtTestStatus;

	private SDSTSText txtManufacturer;

	private SDSTSText txtModel;

	private SDSTSText txtDriver;

	private Composite detailsCompoiste;

	private SDSTSCheckBox chkBoxBarcodeScanner;

	private SDSImageLabel btnTicketScannerTest;

	private SDSTSCombo<ComboLabelValuePair> comboScannerPort ;

	private SDSTSCombo<ComboLabelValuePair> comboScannerType ;
	
	private Image buttonImage;

	private Image buttonDisableImage;

	private Image noteImage;
	
	private Image checkImage;
	
	private SDSTSLabel lblBarcodeScanner = null;
	
	Composite noteComposite = null;

	/**
	 * Instance of the log out msg label
	 */
	private SDSTSLabel lblLogOutMsg = null;

	public BarcodeScannerPreferenceComposite(Composite parent, int style) 
	{
		super(parent, style);
		noteImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.NOTE_IMAGE));
		initialize();
		this.setBounds(0,0, 500,200);
	}

	private void initialize() {
		createImages();
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 0;
		grlPage.marginHeight = 1;
		grlPage.marginWidth = 5;
		grlPage.horizontalSpacing = 5;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.verticalAlignment = GridData.BEGINNING;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;

		createDetailsComposite();

		setControlProperties();
		this.setLayoutData(gdPage);
		this.setLayout(grlPage);
		layout();
	}

	private void createDetailsComposite(){

		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData containerData = new GridData();
		containerData.widthHint = 655;
		
		if( Util.isSmallerResolution() )
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
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_BARCODE_SCANNER_TITLE));
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		GridData grdDetailsComposite = new GridData();
		grdDetailsComposite.grabExcessHorizontalSpace = true;
		grdDetailsComposite.verticalAlignment = GridData.CENTER;
		grdDetailsComposite.grabExcessVerticalSpace = false;
		grdDetailsComposite.horizontalAlignment = GridData.CENTER;		

		GridLayout grlDetailsComposite = new GridLayout();
		grlDetailsComposite.numColumns = 2;
		grlDetailsComposite.verticalSpacing = 4;
		grlDetailsComposite.horizontalSpacing = 10;
		
		if( Util.isSmallerResolution() ) {
			grlDetailsComposite.verticalSpacing = 2;
			grlDetailsComposite.horizontalSpacing = 0;
		}
		
//		setCustDispChkBtn(container);
		
		
		GridData gdButtonComposite = new GridData();
		gdButtonComposite.grabExcessHorizontalSpace = true;
		gdButtonComposite.verticalAlignment = GridData.BEGINNING;
		gdButtonComposite.grabExcessVerticalSpace = false;
		gdButtonComposite.horizontalAlignment = GridData.BEGINNING;

		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 2;
//		grlButtonComposite.verticalSpacing = 5;
		if( Util.isSmallerResolution() ) {
			grlButtonComposite.verticalSpacing = 2;
		}
		grlButtonComposite.horizontalSpacing = 0;
		
		Composite btnComposite = new Composite(container, SWT.NONE);
		btnComposite.setBackground(SDSControlFactory.getTSBodyColor());
//		btnComposite.setBackground(new Color (Display.getCurrent(), 211,111,112));
		btnComposite.setLayoutData(gdButtonComposite);
		btnComposite.setLayout(grlButtonComposite);
		
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalIndent = 10;
		
		chkBoxBarcodeScanner = new SDSTSCheckBox(btnComposite, SWT.NONE, "", LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_BARCODE_SCANNER),false);
		chkBoxBarcodeScanner.setName(IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_BTN_BARCODE_SCANNER);
		chkBoxBarcodeScanner.setImage(checkImage);
		
		lblBarcodeScanner = new SDSTSLabel(btnComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_BARCODE_SCANNER));
		lblBarcodeScanner.setLayoutData(gd);
		
		@SuppressWarnings("unused")
		SDSTSLabel tsLbl = new SDSTSLabel(container, SWT.NONE,"");
		
		
		detailsCompoiste = new Composite(container, SWT.NONE);
		detailsCompoiste.setLayoutData(grdDetailsComposite);
		detailsCompoiste.setLayout(grlDetailsComposite);

		lblScannerPort = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_SCANNER_PORT) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboScannerPort = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SELECTED_SCANNER_PORT, "label", "value", IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SCANNER_PORT);
			comboScannerPort.getLeft().setLayoutData(gd1);
			comboScannerPort.getRight().setLayoutData(gd1);
		} else {
			comboScannerPort = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SELECTED_SCANNER_PORT, "label", "value", IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SCANNER_PORT);
		}

		lblScannerType = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_SCANNER_TYPE)+ LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboScannerType = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SELECTED_SCANNER_TYPE, "label", "value",IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SCANNER_TYPE);
			comboScannerType.getLeft().setLayoutData(gd1);
			comboScannerType.getRight().setLayoutData(gd1);
		} else {
			comboScannerType = new SDSTSCombo<ComboLabelValuePair>(detailsCompoiste,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SELECTED_SCANNER_TYPE, "label", "value",IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_CMB_SCANNER_TYPE);
		}
		lblManufacturer = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MANUFACTURER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtManufacturer = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_TXT_MANUFACTURER,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_TXT_MANUFACTURER);
		txtManufacturer.setEditable(false);

		lblModel = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MODEL) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtModel = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_TXT_MODEL,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_TXT_MODEL);
		txtModel.setEditable(false);

		lblDriver = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_DRIVER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));		
		txtDriver = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_TXT_DRIVER,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_TXT_DRIVER);
		txtDriver.setEditable(false);

		lblTestStatus = new SDSTSLabel(detailsCompoiste, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_TEST_STATUS)+ LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		txtTestStatus = new SDSTSText(detailsCompoiste,SWT.BORDER,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_TXT_TEST_STATUS,IVoucherConstants.PREFERENCE_BARCODE_SCANNER_FRM_TXT_TEST_STATUS);
		txtTestStatus.setEditable(false);

		GridData gridData5 = new GridData();
		gridData5.heightHint = buttonImage.getBounds().height;
		gridData5.widthHint = buttonImage.getBounds().width;
		gridData5.horizontalAlignment = GridData.END;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.grabExcessHorizontalSpace = false;
		
		btnTicketScannerTest = new SDSImageLabel(detailsCompoiste, SWT.NONE, buttonImage, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_TICKET_SCANNER_TEST),IVoucherConstants.PREFERENCE_BARCODE_SCANNER_CTRL_BTN_TICKET_SCANNER_TEST);
		btnTicketScannerTest.setLayoutData(gridData5);
		
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

	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblDriver);
			SDSControlFactory.setTouchScreenLabelProperties(lblManufacturer);
			SDSControlFactory.setTouchScreenLabelProperties(lblModel);
			SDSControlFactory.setTouchScreenLabelProperties(lblScannerPort);
			SDSControlFactory.setTouchScreenLabelProperties(lblScannerType);
			SDSControlFactory.setTouchScreenLabelProperties(lblTestStatus);
			SDSControlFactory.setTouchScreenTextProperties(txtDriver);
			SDSControlFactory.setTouchScreenTextProperties(txtManufacturer);
			SDSControlFactory.setTouchScreenTextProperties(txtModel);
			SDSControlFactory.setTouchScreenTextProperties(txtTestStatus);
//			SDSControlFactory.setTouchScreenButtonProperties(chkBoxBarcodeScanner);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnTicketScannerTest);
			SDSControlFactory.setTouchScreenLabelProperties(lblLogOutMsg);

			GridData gdLblLogOut = (GridData)lblLogOutMsg.getLayoutData();
			gdLblLogOut.horizontalSpan = 2;
			lblLogOutMsg.setLayoutData(gdLblLogOut);

			GridData gdTktPrintTest = (GridData)btnTicketScannerTest.getLayoutData();			
			gdTktPrintTest.horizontalAlignment = SWT.CENTER;
			gdTktPrintTest.horizontalSpan = 2;
			if( Util.isSmallerResolution() ) {
				gdTktPrintTest.widthHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				gdTktPrintTest.heightHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 18;
			}
			btnTicketScannerTest.setLayoutData(gdTktPrintTest);

//			GridData gdBarcodeScanner = (GridData)chkBoxBarcodeScanner.getLayoutData();
//			if (Util.isSmallerResolution()) {
//				gdBarcodeScanner.widthHint = chkBoxBarcodeScanner.getText().length() * 16 + 40;
//			} else {
//				// gdBarcodeScanner.widthHint = ControlConstants.EX_BUTTON_WIDTH + 100;
//				gdBarcodeScanner.widthHint = chkBoxBarcodeScanner.getText().length() * 8 + 10;				
//			}
//			gdBarcodeScanner.heightHint = 20;
//			gdBarcodeScanner.horizontalSpan = 2;
//			if( Util.isSmallerResolution() ) {
//				gdBarcodeScanner.heightHint = 15;
//			}
//			chkBoxBarcodeScanner.setLayoutData(gdBarcodeScanner);

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
			
			GridData gdTxtTestStatus = (GridData)txtTestStatus.getLayoutData();
			gdTxtTestStatus.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtTestStatus.horizontalIndent = 6;
				gdTxtTestStatus.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			} else {
				gdTxtTestStatus.horizontalIndent = 15;
				gdTxtTestStatus.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtTestStatus.setLayoutData(gdTxtTestStatus);
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public void disableControls(){
//		lblTestStatus.setEnabled(false);
//		lblScannerPort.setEnabled(false);
//		lblScannerType.setEnabled(false);
//		lblManufacturer.setEnabled(false);
//		lblModel.setEnabled(false);
//		lblDriver.setEnabled(false);
		txtTestStatus.setEnabled(false);
		txtManufacturer.setEnabled(false);
		txtModel.setEnabled(false);
		txtDriver.setEnabled(false);
//		btnBarcodeScanner.setEnabled(false);
		btnTicketScannerTest.setEnabled(false);
		btnTicketScannerTest.setImage(buttonDisableImage);
		btnTicketScannerTest.getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		comboScannerPort.getText().setEnabled(false);
		comboScannerType.getText().setEnabled(false);
		comboScannerType.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
		comboScannerType.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
		comboScannerPort.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
		comboScannerPort.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
		comboScannerPort .setEnabled(false);
		comboScannerType .setEnabled(false);
		noteComposite.setBackgroundImage(noteImage);
	}

	class CmbContentProvider implements IStructuredContentProvider
	{

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return ((java.util.List<String>) inputElement).toArray();
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	public SDSTSCheckBox getChkBoxBarcodeScanner() 
	{
		return chkBoxBarcodeScanner;
	}

	public void setChkBoxBarcodeScanner(SDSTSCheckBox chkBoxBarcodeScanner) 
	{
		this.chkBoxBarcodeScanner = chkBoxBarcodeScanner;
	}

	public SDSImageLabel getBtnTicketScannerTest() 
	{
		return btnTicketScannerTest;
	}

	public void setBtnTicketScannerTest(SDSImageLabel btnTicketScannerTest) 
	{
		this.btnTicketScannerTest = btnTicketScannerTest;
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

	public SDSTSText getTxtTestStatus() 
	{
		return txtTestStatus;
	}

	public void setTxtTestStatus(SDSTSText txtTestStatus) 
	{
		this.txtTestStatus = txtTestStatus;
	}

	public SDSTSCombo<ComboLabelValuePair> getComboScannerPort() {
		return comboScannerPort;
	}

	public void setComboScannerPort(SDSTSCombo<ComboLabelValuePair> comboScannerPort) {
		this.comboScannerPort = comboScannerPort;
	}

	public SDSTSCombo<ComboLabelValuePair> getComboScannerType() {
		return comboScannerType;
	}

	public void setComboScannerType(SDSTSCombo<ComboLabelValuePair> comboScannerType) {
		this.comboScannerType = comboScannerType;
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

	private void createImages() {
		buttonImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
		checkImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED));
	}
}

