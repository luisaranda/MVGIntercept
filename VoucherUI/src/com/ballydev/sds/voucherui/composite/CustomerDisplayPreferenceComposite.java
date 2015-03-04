/*****************************************************************************
 * $Id: CustomerDisplayPreferenceComposite.java,v 1.29, 2010-04-08 13:45:07Z, Verma, Nitin Kumar$
 * $Date: 4/8/2010 8:45:07 AM$
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
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSCombo;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

/**
 * This class creates the customer display preference composite
 * @author Nithya kalyani R
 * @version $Revision: 30$ 
 */
public class CustomerDisplayPreferenceComposite extends Composite {

	/**
	 * Instance of the display type label
	 */
	private SDSTSLabel lblDisplayType;

	/**
	 * Instance of the port label
	 */
	private SDSTSLabel lblPort;

	/**
	 * Instance of the manufacturer label
	 */
	private SDSTSLabel lblManufacturer;

	/**
	 * Instance of the model label
	 */
	private SDSTSLabel lblModel;

	/**
	 * Instance of the driver label
	 */
	private SDSTSLabel lblDriver;

	/**
	 * Instance of the test status label
	 */
	private SDSTSLabel lblTestStatus;

	/**
	 * Instance of the welcome msg label
	 */
	private SDSTSLabel lblWelcomeMessage;		

	/**
	 * Instance of the display type combo 
	 */
	private SDSTSCombo<ComboLabelValuePair> comboDisplayType ;

	/**
	 * Instance of the display port combo
	 */
	private SDSTSCombo<ComboLabelValuePair> comboDisplayPort ;

	/**
	 * Instance of the manufacturer text
	 */
	private SDSTSText txtManufacturer;

	/**
	 * Instance of the model text
	 */
	private SDSTSText txtModel;

	/**
	 * Instance of the driver text
	 */
	private SDSTSText txtDriver;

	/**
	 * Instance of the test status text
	 */
	private SDSTSText txtTestStatus;

	/**
	 * Instance of the welcome msg text
	 */
	private SDSTSText txtWelcomeMessage;

	/**
	 * Instance of the details composite
	 */
	private Composite detailsComposite;

	/**
	 * Instance of the details composite
	 */
	private Composite buttonComposite;

	/**
	 * Instance of the customer display button
	 */
	private SDSTSCheckBox chkBoxCustomerDisplay;

	/**
	 * Instance of the customer display test button
	 */
	private SDSImageLabel btnCustomerDisplayTest;

	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;

	/**
	 * Instance of the display scroll Radio Image
	 */
	private TSButtonLabel radioImageDispScroll;

	/**
	 * Instance of the display center Radio Image
	 */
	private TSButtonLabel radioImageDispCenter;

	/**
	 * Instance of the toggle button composite
	 */
	private Composite toggleBtnComposite =null;

	/**
	 * Instance to check whether the center display button is selected or not
	 */
	private boolean isCenterDisplay = true;

	/**
	 * Instance of the cashier label
	 */
	private SDSTSLabel lblCenterDisp = null;

	/**
	 * Instance of the auditor label
	 */
	private SDSTSLabel lblScrollDisp = null;

	/**
	 * Instance of the log out msg label
	 */
	private SDSTSLabel lblLogOutMsg = null;

	private Composite container = null;

	private Image buttonImage;

	private Image buttonDisableImage;
	
	private Image noteImage;
	
	private Image checkImage;
	
	Composite noteComposite = null;
	
	SDSTSLabel lblCustDisp = null;
	
	/**
	 * Constructor of the class
	 */
	public CustomerDisplayPreferenceComposite(Composite parent, int style){
		super(parent, style);
		initialize();
		this.setBounds(0,0, 800,700);
	}

	/**
	 * This method initializes the page grid properties
	 */
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
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		createMainComposite();
		
		setControlProperties();

		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		if(Util.isSmallerResolution()) {
			setSmallerFont();
		}
	}
	
	private void createMainComposite() {
		container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		GridData containerData = new GridData();
		containerData.widthHint 		= 655;
		
		if(Util.isSmallerResolution())
			containerData.heightHint 	= 530;
		else 
			containerData.heightHint 	= 498;
		
		containerData.horizontalIndent  = 0;
		containerData.verticalIndent 	= 0;
		container.setLayoutData(containerData);
		
		GridLayout containerLayout   = new GridLayout(2, true);
		containerLayout.numColumns   = 1;
		containerLayout.marginTop    = 0;
		containerLayout.marginLeft   = 0;
		containerLayout.marginRight  = 0;
		containerLayout.marginBottom = 0;
		containerLayout.marginHeight = 0;
		containerLayout.marginWidth  = 0;
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
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_CUSTOMER_DISPLAY_TITLE));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		setCustDispChkBtn(container);
	
//		btnCustomerDisplay = new SDSTSButton(container, SWT.CHECK, 
//		LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CUSTOMER_DISPLAY),IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY, 
//		IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_BTN_CUSTOMER_DISPLAY, false);
//		btnCustomerDisplay.setLayoutData(gd);
		
		createButtonComposite();
		createDetailsComposite();
		
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
	
	private void createButtonComposite() {
		GridData gdButtonComposite = new GridData();
		gdButtonComposite.grabExcessHorizontalSpace = true;
		gdButtonComposite.verticalAlignment = GridData.BEGINNING;
		gdButtonComposite.grabExcessVerticalSpace = false;
		gdButtonComposite.horizontalAlignment = GridData.CENTER;

		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 6;
		grlButtonComposite.horizontalSpacing = 0;
//		grlButtonComposite.verticalSpacing = 5;
		if( Util.isSmallerResolution() ) {
			gdButtonComposite.heightHint = 90;
			grlButtonComposite.verticalSpacing = 2;
		} else {
			gdButtonComposite.heightHint = 82;
		}
		
		buttonComposite = new Composite(container, SWT.NONE);
		buttonComposite.setBackground(SDSControlFactory.getTSBodyColor());
//		buttonComposite.setBackground(new Color (Display.getCurrent(), 211,111,112));
		buttonComposite.setLayoutData(gdButtonComposite);
		buttonComposite.setLayout(grlButtonComposite);
		
		createBtnGroup(buttonComposite);
		
		GridData gridData5 = new GridData();
		gridData5.heightHint = buttonImage.getBounds().height;
		gridData5.widthHint = buttonImage.getBounds().width;
		gridData5.horizontalAlignment = GridData.END;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.grabExcessHorizontalSpace = false;
		
		btnCustomerDisplayTest = new SDSImageLabel(buttonComposite, SWT.NONE, buttonImage, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CUSTOMER_DISPLAY_TEST), IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY_TEST);
		btnCustomerDisplayTest.setLayoutData(gridData5);
		
	}

	private void setCustDispChkBtn(Composite composite) {
		
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
//		grlButtonComposite.verticalSpacing = 5;
		if( Util.isSmallerResolution() ) {
			grlButtonComposite.verticalSpacing = 2;
		}
		grlButtonComposite.horizontalSpacing = 0;
		
		Composite btnComposite = new Composite(composite, SWT.NONE);
		btnComposite.setBackground(SDSControlFactory.getTSBodyColor());
//		btnComposite.setBackground(new Color (Display.getCurrent(), 211,111,112));
		btnComposite.setLayoutData(gdButtonComposite);
		btnComposite.setLayout(grlButtonComposite);
		
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalIndent = 10;
		
		chkBoxCustomerDisplay = new SDSTSCheckBox(btnComposite, SWT.NONE, "", LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CUSTOMER_DISPLAY), false);
		chkBoxCustomerDisplay.setName(IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY);
		chkBoxCustomerDisplay.setImage(checkImage);
		
		lblCustDisp = new SDSTSLabel(btnComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CUSTOMER_DISPLAY));
		lblCustDisp.setLayoutData(gd);
	}

	private void createDetailsComposite(){

		GridData gdDetailsComposite = new GridData();
		gdDetailsComposite.grabExcessHorizontalSpace = true;
		gdDetailsComposite.verticalAlignment = GridData.CENTER;
		gdDetailsComposite.grabExcessVerticalSpace = false;
		gdDetailsComposite.horizontalAlignment = GridData.CENTER;
		if( Util.isSmallerResolution() ) {
			gdDetailsComposite.heightHint = 275;
		}
		GridLayout grlDetailsComposite = new GridLayout();
		grlDetailsComposite.numColumns = 2;
		grlDetailsComposite.verticalSpacing = 0;
		grlDetailsComposite.horizontalSpacing = 10;

		detailsComposite = new Composite(container, SWT.NONE);
		detailsComposite.setBackground(SDSControlFactory.getTSBodyColor());
		detailsComposite.setLayoutData(gdDetailsComposite);
		detailsComposite.setLayout(grlDetailsComposite);

		lblPort = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PORT) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));	

		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboDisplayPort = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_SELECTED_PORT, "label", "value", IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_PORT);
			comboDisplayPort.getLeft().setLayoutData(gd1);
			comboDisplayPort.getRight().setLayoutData(gd1);
		} else {
			comboDisplayPort = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_SELECTED_PORT, "label", "value", IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_PORT);
		}
		
		lblDisplayType = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_DISPLAY_TYPE) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboDisplayType = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_SELECTED_DISPLAY_TYPE, "label", "value", IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_DISPLAY_TYPE);
			comboDisplayType.getLeft().setLayoutData(gd1);
			comboDisplayType.getRight().setLayoutData(gd1);
		} else {
			comboDisplayType = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_SELECTED_DISPLAY_TYPE, "label", "value", IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_CMB_DISPLAY_TYPE);
		}
		
		lblManufacturer = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MANUFACTURER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtManufacturer = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_MANUFACTURER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_TXT_MANUFACTURER);
		txtManufacturer.setEditable(false);

		lblModel = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MODEL) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtModel = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_MODEL,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_TXT_MODEL);
		txtModel.setEditable(false);

		lblDriver = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_DRIVER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtDriver = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_CTRL_TXT_DRIVER,IVoucherConstants.PREFERENCE_RECEIPT_PRINTER_FRM_TXT_DRIVER);
		txtDriver.setEditable(false);

		lblTestStatus = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_CUST_TEST_MSG) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtTestStatus = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_TEST_STATUS,IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_TEST_STATUS);
		txtTestStatus.setTextLimit(50);

		lblWelcomeMessage = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_WELCOME_MESSAGE) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtWelcomeMessage = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_TXT_WELCOME_MESSAGE,IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_FRM_TXT_WELCOME_MESSAGE);
		txtWelcomeMessage.setTextLimit(50);
//		btnCustomerDisplayTest = new SDSTSButton(detailsComposite,SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CUSTOMER_DISPLAY_TEST),IVoucherConstants.PREFERENCE_CUSTOMER_DISPLAY_CTRL_BTN_CUSTOMER_DISPLAY_TEST);
	}

	private void createBtnGroup(Composite buttonComposite)	{

		toggleBtnComposite =  new Composite(buttonComposite,SWT.NONE);
		toggleBtnComposite.setBackground(SDSControlFactory.getTSBodyColor());
//		toggleBtnComposite.setBackground(new Color (Display.getCurrent(), 211,111,12));
		
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 3;
		gridLayout3.verticalSpacing = 1;
		gridLayout3.horizontalSpacing = 10;
		gridLayout3.marginWidth = 5;
		gridLayout3.marginLeft = 5;


		GridData toggleCompositeGridData = new GridData();
		toggleCompositeGridData.horizontalAlignment = SWT.CENTER;
		toggleCompositeGridData.horizontalSpan	= 2;
		toggleCompositeGridData.verticalIndent	= 0;
		if( !Util.isSmallerResolution() ) {
			toggleCompositeGridData.heightHint 		= 81;
		}
		else {
			gridLayout3.marginTop = 0;
			gridLayout3.marginBottom = 0;
			toggleCompositeGridData.heightHint 		= 100;
		}
		
		toggleBtnComposite.setLayout(gridLayout3);
		toggleBtnComposite.setLayoutData(toggleCompositeGridData);

		GridData gdTouchScreenButton = new GridData();
		gdTouchScreenButton.grabExcessVerticalSpace = true;
		gdTouchScreenButton.heightHint = 50;
		gdTouchScreenButton.widthHint = 50;
		gdTouchScreenButton.verticalAlignment = GridData.END;
		if( Util.isSmallerResolution() ) {
			gdTouchScreenButton.heightHint = 43;
			gdTouchScreenButton.widthHint = 43;
			gdTouchScreenButton.verticalAlignment = GridData.BEGINNING;
		}
		gdTouchScreenButton.horizontalAlignment = GridData.CENTER;
		gdTouchScreenButton.grabExcessHorizontalSpace = true;
		gdTouchScreenButton.horizontalIndent = 30;

		@SuppressWarnings("unused")
		SDSTSLabel lblDisplay = new SDSTSLabel(toggleBtnComposite,SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_DISPLAY) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		radioButtonControl = new RadioButtonControl("Search by any one of the following");

		radioImageDispCenter = new TSButtonLabel(toggleBtnComposite, SWT.NONE, "" ,IVoucherConstants.PREFERENCE_LOCATION_FRM_BTN_CENTER_DISP);
		radioImageDispCenter.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
		radioImageDispCenter.setLayoutData(gdTouchScreenButton);	
		radioButtonControl.add(radioImageDispCenter);

		radioImageDispScroll = new TSButtonLabel(toggleBtnComposite, SWT.NONE, "", IVoucherConstants.PREFERENCE_LOCATION_FRM_BTN_SCROLL_DISP);
		radioImageDispScroll.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		radioImageDispScroll.setLayoutData(gdTouchScreenButton);
		radioButtonControl.add(radioImageDispScroll);

		lblCenterDisp = new SDSTSLabel(toggleBtnComposite,SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CENTER_DISP));		
		lblScrollDisp = new SDSTSLabel(toggleBtnComposite,SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_SCROLL_DISP));
	}

	public void setCenterDisplay(boolean isCenterDisplay) {
		this.isCenterDisplay = isCenterDisplay;
		if( isCenterDisplay ){
			lblCenterDisp.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_CENTER_DISP));			
		}else{
			lblScrollDisp.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_SCROLL_DISP));			
		}
	}

	private void setSmallerFont() {
		try {			
			lblLogOutMsg.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblCustDisp.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblScrollDisp.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblCenterDisp.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblPort.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblDisplayType.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblManufacturer.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblModel.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblDriver.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblTestStatus.setFont(SDSControlFactory.getSmallTouchScreenFont());
			lblWelcomeMessage.setFont(SDSControlFactory.getSmallTouchScreenFont());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method sets the default control grid properties
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblPort);
			SDSControlFactory.setTouchScreenLabelProperties(lblDisplayType);
			SDSControlFactory.setTouchScreenLabelProperties(lblManufacturer);
			SDSControlFactory.setTouchScreenLabelProperties(lblModel);
			SDSControlFactory.setTouchScreenLabelProperties(lblDriver);
			SDSControlFactory.setTouchScreenLabelProperties(lblTestStatus);
			SDSControlFactory.setTouchScreenLabelProperties(lblWelcomeMessage);
			SDSControlFactory.setTouchScreenLabelProperties(lblCustDisp);
			SDSControlFactory.setTouchScreenTextProperties(txtManufacturer);
			SDSControlFactory.setTouchScreenTextProperties(txtModel);
			SDSControlFactory.setTouchScreenTextProperties(txtDriver);
			SDSControlFactory.setTouchScreenTextProperties(txtTestStatus);
			SDSControlFactory.setTouchScreenTextProperties(txtWelcomeMessage);			
			SDSControlFactory.setTouchScreenImageLabelProperties(btnCustomerDisplayTest);
//			SDSControlFactory.setTouchScreenButtonProperties(btnCustomerDisplay);
			SDSControlFactory.setTouchScreenLabelProperties(lblCenterDisp);
			SDSControlFactory.setTouchScreenLabelProperties(lblScrollDisp);
			SDSControlFactory.setTouchScreenLabelProperties(lblLogOutMsg);

			GridData gdLabel1 = (GridData)lblManufacturer.getLayoutData();
			gdLabel1.heightHint = 20;
			lblManufacturer.setLayoutData(gdLabel1);

			GridData gdText1 = (GridData)txtManufacturer.getLayoutData();
			gdText1.widthHint  = ControlConstants.EX_TEXT_WIDTH;
			gdText1.heightHint = 21;
			if( Util.isSmallerResolution() ) {
				gdText1.horizontalIndent = 6;
				gdText1.verticalIndent = 1;
				gdText1.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			} else {
				gdText1.verticalIndent = 2;
				gdText1.horizontalIndent = 15;
				gdText1.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtManufacturer.setLayoutData(gdText1);

			GridData gdLabel2 = (GridData)lblModel.getLayoutData();
			gdLabel2.heightHint = 20;
			lblModel.setLayoutData(gdLabel2);

			GridData gdText2 = (GridData)txtModel.getLayoutData();
			gdText2.widthHint  = ControlConstants.EX_TEXT_WIDTH;
			gdText2.heightHint = 21;
			if( Util.isSmallerResolution() ) {
				gdText2.horizontalIndent = 6;
				gdText2.verticalIndent = 1;
				gdText2.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
				gdText2.heightHint = 20;
			} else {
				gdText2.verticalIndent = 2;
				gdText2.horizontalIndent = 15;
				gdText2.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtModel.setLayoutData(gdText2);

			GridData gdLabel3 = (GridData)lblDriver.getLayoutData();
			gdLabel3.heightHint = 20;
			lblDriver.setLayoutData(gdLabel3);

			GridData gdText3 = (GridData)txtDriver.getLayoutData();
			gdText3.widthHint  = ControlConstants.EX_TEXT_WIDTH;
			gdText3.heightHint = 21;
			if( Util.isSmallerResolution() ) {
				gdText3.horizontalIndent = 6;
				gdText3.verticalIndent = 1;
				gdText3.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			} else {
				gdText3.verticalIndent = 2;
				gdText3.horizontalIndent = 15;
				gdText3.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
				gdText3.heightHint = 20;
			}
			txtDriver.setLayoutData(gdText3);

			GridData gdLabel4 = (GridData)lblTestStatus.getLayoutData();
			gdLabel4.heightHint = 20;
			lblTestStatus.setLayoutData(gdLabel4);

			GridData gdText4 = (GridData)txtTestStatus.getLayoutData();
			gdText4.widthHint  = ControlConstants.EX_TEXT_WIDTH;
			gdText4.heightHint = 21;
			if( Util.isSmallerResolution() ) {
				gdText4.horizontalIndent = 6;
				gdText4.verticalIndent = 1;
				gdText4.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
				gdText4.heightHint = 20;
			} else {
				gdText4.verticalIndent = 2;
				gdText4.horizontalIndent = 15;
				gdText4.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtTestStatus.setLayoutData(gdText4);

			GridData gdLabel5 = (GridData)lblWelcomeMessage.getLayoutData();
			gdLabel5.heightHint = 20;
			lblWelcomeMessage.setLayoutData(gdLabel5);

			GridData gdText5 = (GridData)txtWelcomeMessage.getLayoutData();
			gdText5.widthHint  = ControlConstants.EX_TEXT_WIDTH;
			gdText5.heightHint = 21;
			if( Util.isSmallerResolution() ) {
				gdText5.horizontalIndent = 6;
				gdText5.verticalIndent = 1;
				gdText5.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
				gdText5.heightHint = 20;
			} else {
				gdText5.verticalIndent = 2;
				gdText5.horizontalIndent = 15;
				gdText5.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtWelcomeMessage.setLayoutData(gdText5);
			

			if( Util.isSmallerResolution() ) {
				GridData gdDisplayTest = (GridData)btnCustomerDisplayTest.getLayoutData();
				gdDisplayTest.verticalAlignment = SWT.BEGINNING;
				gdDisplayTest.horizontalSpan = 2;
				gdDisplayTest.horizontalAlignment = SWT.CENTER;
				gdDisplayTest.horizontalIndent = 20;
				gdDisplayTest.widthHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				gdDisplayTest.heightHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 18;
				btnCustomerDisplayTest.setLayoutData(gdDisplayTest);
			}
//
//			GridData gdDisplay = (GridData)btnCustomerDisplay.getLayoutData();			
//			gdDisplay.widthHint = ControlConstants.EX_BUTTON_WIDTH;
//			gdDisplay.horizontalSpan = 2;
//			gdDisplay.heightHint = 20;
//			if( Util.isSmallerResolution() ) {
//				gdDisplay.heightHint = 14;
//				gdDisplay.widthHint = 250;
//			}
//			btnCustomerDisplay.setLayoutData(gdDisplay);

			GridData gdLblCenterDisp = (GridData)lblCenterDisp.getLayoutData();
			gdLblCenterDisp.horizontalIndent = 35;
			gdLblCenterDisp.horizontalSpan = 2;
			gdLblCenterDisp.horizontalAlignment = SWT.END;
			lblCenterDisp.setLayoutData(gdLblCenterDisp);

			GridData gdLblScrollDisp = (GridData)lblScrollDisp.getLayoutData();
			gdLblScrollDisp.horizontalIndent = 35;
			lblScrollDisp.setLayoutData(gdLblScrollDisp);

			GridData gdLblLogOut = (GridData)lblLogOutMsg.getLayoutData();
			gdLblLogOut.horizontalSpan = 2;
			lblLogOutMsg.setLayoutData(gdLblLogOut);

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public void disableControls(){
		comboDisplayPort.getText().setEnabled(false);
		comboDisplayType.getText().setEnabled(false);
		comboDisplayType.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
		comboDisplayType.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
		comboDisplayPort.getRight().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)));
		comboDisplayPort.getLeft().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)));
		comboDisplayType .setEnabled(false);
		comboDisplayPort .setEnabled(false);
		txtManufacturer.setEnabled(false);
		txtModel.setEnabled(false);
		txtDriver.setEnabled(false);
		txtTestStatus.setEnabled(false);
		txtWelcomeMessage.setEnabled(false);
		detailsComposite.setEnabled(false);
		btnCustomerDisplayTest.setEnabled(false);
		btnCustomerDisplayTest.setImage(buttonDisableImage);
		btnCustomerDisplayTest.getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		radioImageDispScroll.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_DISABLED_BTHEME)));
		radioImageDispCenter.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_DISABLED_BTHEME)));
		noteComposite.setBackgroundImage(noteImage);
	}

	/**
	 * @return the comboDisplayType
	 */
	public SDSTSCombo<ComboLabelValuePair> getComboDisplayType() {
		return comboDisplayType;
	}

	/**
	 * @param comboDisplayType the comboDisplayType to set
	 */
	public void setComboDisplayType(SDSTSCombo<ComboLabelValuePair> comboDisplayType) {
		this.comboDisplayType = comboDisplayType;
	}

	/**
	 * @return the comboDisplayPort
	 */
	public SDSTSCombo<ComboLabelValuePair> getComboDisplayPort() {
		return comboDisplayPort;
	}

	/**
	 * @param comboDisplayPort the comboDisplayPort to set
	 */
	public void setComboDisplayPort(SDSTSCombo<ComboLabelValuePair> comboDisplayPort) {
		this.comboDisplayPort = comboDisplayPort;
	}

	/**
	 * @return the txtManufacturer
	 */
	public SDSTSText getTxtManufacturer() {
		return txtManufacturer;
	}

	/**
	 * @param txtManufacturer the txtManufacturer to set
	 */
	public void setTxtManufacturer(SDSTSText txtManufacturer) {
		this.txtManufacturer = txtManufacturer;
	}

	/**
	 * @return the txtModel
	 */
	public SDSTSText getTxtModel() {
		return txtModel;
	}

	/**
	 * @param txtModel the txtModel to set
	 */
	public void setTxtModel(SDSTSText txtModel) {
		this.txtModel = txtModel;
	}

	/**
	 * @return the txtDriver
	 */
	public SDSTSText getTxtDriver() {
		return txtDriver;
	}

	/**
	 * @param txtDriver the txtDriver to set
	 */
	public void setTxtDriver(SDSTSText txtDriver) {
		this.txtDriver = txtDriver;
	}

	/**
	 * @return the txtTestStatus
	 */
	public SDSTSText getTxtTestStatus() {
		return txtTestStatus;
	}

	/**
	 * @param txtTestStatus the txtTestStatus to set
	 */
	public void setTxtTestStatus(SDSTSText txtTestStatus) {
		this.txtTestStatus = txtTestStatus;
	}

	/**
	 * @return the txtWelcomeMessage
	 */
	public SDSTSText getTxtWelcomeMessage() {
		return txtWelcomeMessage;
	}

	/**
	 * @param txtWelcomeMessage the txtWelcomeMessage to set
	 */
	public void setTxtWelcomeMessage(SDSTSText txtWelcomeMessage) {
		this.txtWelcomeMessage = txtWelcomeMessage;
	}

	/**
	 * @return the detailsComposite
	 */
	public Composite getDetailsComposite() {
		return detailsComposite;
	}

	/**
	 * @param detailsComposite the detailsComposite to set
	 */
	public void setDetailsComposite(Composite detailsComposite) {
		this.detailsComposite = detailsComposite;
	}

	/**
	 * @return the buttonComposite
	 */
	public Composite getButtonComposite() {
		return buttonComposite;
	}

	/**
	 * @param buttonComposite the buttonComposite to set
	 */
	public void setButtonComposite(Composite buttonComposite) {
		this.buttonComposite = buttonComposite;
	}

	/**
	 * @return the btnCustomerDisplay
	 */
	public SDSTSCheckBox getBtnCustomerDisplay() {
		return chkBoxCustomerDisplay;
	}

	/**
	 * @param btnCustomerDisplay the btnCustomerDisplay to set
	 */
	public void setBtnCustomerDisplay(SDSTSCheckBox btnCustomerDisplay) {
		this.chkBoxCustomerDisplay = btnCustomerDisplay;
	}

	/**
	 * @return the btnCustomerDisplayTest
	 */
	public SDSImageLabel getBtnCustomerDisplayTest() {
		return btnCustomerDisplayTest;
	}

	/**
	 * @param btnCustomerDisplayTest the btnCustomerDisplayTest to set
	 */
	public void setBtnCustomerDisplayTest(SDSImageLabel btnCustomerDisplayTest) {
		this.btnCustomerDisplayTest = btnCustomerDisplayTest;
	}

	/**
	 * @return the radioButtonControl
	 */
	public RadioButtonControl getRadioButtonControl() {
		return radioButtonControl;
	}

	/**
	 * @param radioButtonControl the radioButtonControl to set
	 */
	public void setRadioButtonControl(RadioButtonControl radioButtonControl) {
		this.radioButtonControl = radioButtonControl;
	}

	/**
	 * @return the radioImageDispScroll
	 */
	public TSButtonLabel getRadioImageDispScroll() {
		return radioImageDispScroll;
	}

	/**
	 * @param radioImageDispScroll the radioImageDispScroll to set
	 */
	public void setRadioImageDispScroll(TSButtonLabel radioImageDispScroll) {
		this.radioImageDispScroll = radioImageDispScroll;
	}

	/**
	 * @return the radioImageDispCenter
	 */
	public TSButtonLabel getRadioImageDispCenter() {
		return radioImageDispCenter;
	}

	/**
	 * @param radioImageDispCenter the radioImageDispCenter to set
	 */
	public void setRadioImageDispCenter(TSButtonLabel radioImageDispCenter) {
		this.radioImageDispCenter = radioImageDispCenter;
	}

	/**
	 * @return the toggleBtnComposite
	 */
	public Composite getToggleBtnComposite() {
		return toggleBtnComposite;
	}

	/**
	 * @param toggleBtnComposite the toggleBtnComposite to set
	 */
	public void setToggleBtnComposite(Composite toggleBtnComposite) {
		this.toggleBtnComposite = toggleBtnComposite;
	}

	/**
	 * @return the lblCenterDisp
	 */
	public SDSTSLabel getLblCenterDisp() {
		return lblCenterDisp;
	}

	/**
	 * @param lblCenterDisp the lblCenterDisp to set
	 */
	public void setLblCenterDisp(SDSTSLabel lblCenterDisp) {
		this.lblCenterDisp = lblCenterDisp;
	}

	/**
	 * @return the lblScrollDisp
	 */
	public SDSTSLabel getLblScrollDisp() {
		return lblScrollDisp;
	}

	/**
	 * @param lblScrollDisp the lblScrollDisp to set
	 */
	public void setLblScrollDisp(SDSTSLabel lblScrollDisp) {
		this.lblScrollDisp = lblScrollDisp;
	}

	/**
	 * @return the isCenterDisplay
	 */
	public boolean isCenterDisplay() {
		return isCenterDisplay;
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
		noteImage  = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.NOTE_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
		checkImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED));
	}
}
