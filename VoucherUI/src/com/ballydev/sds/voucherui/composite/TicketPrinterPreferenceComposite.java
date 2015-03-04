/*****************************************************************************
 * $Id: TicketPrinterPreferenceComposite.java,v 1.26, 2010-04-08 13:45:07Z, Verma, Nitin Kumar$
 * $Date: 4/8/2010 8:45:07 AM$
 *****************************************************************************
 * Copyright (c) 2006 Bally Technology  1977 - 2007
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSComboViewer;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
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
 * This creates the ticket preference page
 * @author Nithya kalyani R
 * @version $Revision: 27$ 
 */
public class TicketPrinterPreferenceComposite extends Composite {

	/**
	 * Instance of Printer port label
	 */
	private SDSTSLabel lblPrinterPort;

	/**
	 * Instance of  printer type label
	 */
	private SDSTSLabel lblPrinterType;

	/**
	 * Instance of manufacturer label
	 */
	private SDSTSLabel lblManufacturer;

	/**
	 * Instance of model label
	 */
	private SDSTSLabel lblModel;

	/**
	 * Instance of driver label
	 */
	private SDSTSLabel lblDriver;

	/**
	 * Instance of printer port combo
	 */
	private Combo cmbPrinterPort;

	/**
	 * Instance of printer port combo viewer
	 */
	private SDSComboViewer cmbViewerPrinterPort = null; 

	/**
	 * Instance of printer type combo
	 */
	private Combo cmbPrinterType;

	/**
	 * Instance of printer type combo viewer
	 */
	private SDSComboViewer cmbViewerPrinterType = null;

	/**
	 * Instance of printer port combo value pair
	 */
	private SDSTSCombo<ComboLabelValuePair> comboPrinterPort ;

	/**
	 * Instance of printer type combo value pair
	 */
	private SDSTSCombo<ComboLabelValuePair> comboPrinterType ;

	/**
	 * Instance of manufacturer text
	 */
	private SDSTSText txtManufacturer;

	/**
	 *Instance of model text 
	 */
	private SDSTSText txtModel;

	/**
	 * Instance of driver text
	 */
	private SDSTSText txtDriver;

	/**
	 * Instance of the details composite
	 */
	private Composite buttonComposite;

	/**
	 * Instance of the toggle button composite
	 */
	private Composite toggleBtnComposite =null;

	/**
	 * Instance of  printer type label
	 */
	private SDSTSLabel lblSize;

	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;

	/**
	 * Instance of the Small size Radio Image
	 */
	private TSButtonLabel radioImageSmallSize = null;

	/**
	 *Instance of the Normal size Radio Image 
	 */
	private TSButtonLabel radioImageNormalSize = null;
	
	/**
	 * Instance of the small size label
	 */
	private SDSTSLabel lblSmallSize = null;

	/**
	 * Instance of the normal size label
	 */
	private SDSTSLabel lblNormalSize = null;

	/**
	 * Instance of details composite
	 */
	private Composite detailsComposite;

	/**
	 * Instance of ticket printing button
	 */
	private SDSImageLabel btnTicketPrinting;

	/**
	 * Instance of the log out msg label
	 */
	private SDSTSLabel lblLogOutMsg = null;

	/**
	 * Instance to check whether the small size button is selected or not.
	 */
	@SuppressWarnings("unused")
	private boolean isSmallSize = true;

	Composite container = null;

	private Image buttonImage;

	private Image buttonDisableImage;

	private Image noteImage;
	
	Composite noteComposite = null;
	
	/**
	 * Constructor of the class
	 */
	public TicketPrinterPreferenceComposite(Composite parent, int style){
		super(parent, style);
		noteImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.NOTE_IMAGE));
		initialize();
		this.setBounds(0,0, 800,700);
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
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		createMainComposite();
		
//		lblLogOutMsg = new SDSTSLabel(this, SWT.NONE,"");
		setControlProperties();

		this.setLayout(grlPage);
		this.setLayoutData(gdPage);

	}

	private void createMainComposite() {
		container = new Composite(this, SWT.NONE);
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
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_TICKET_PRINTER_TITLE));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
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
		gdButtonComposite.horizontalAlignment = GridData.BEGINNING;
		gdButtonComposite.horizontalIndent = 60;
		
		if( Util.isSmallerResolution() ) {
			gdButtonComposite.heightHint = 75;
		}
		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 4;
		grlButtonComposite.verticalSpacing = 5;
		if( Util.isSmallerResolution() ) {
			grlButtonComposite.verticalSpacing = 2;
		}
		grlButtonComposite.horizontalSpacing = 0;
		
		buttonComposite = new Composite(container, SWT.NONE);
		buttonComposite.setLayoutData(gdButtonComposite);
		buttonComposite.setLayout(grlButtonComposite);
		buttonComposite.setBackground(SDSControlFactory.getTSBodyColor());
		
		createBtnGroup(buttonComposite);
	}

	private void createDetailsComposite(){

		GridData gdDetailsComp = new GridData();
		gdDetailsComp.grabExcessHorizontalSpace = true;
		gdDetailsComp.verticalAlignment = GridData.CENTER;
		gdDetailsComp.grabExcessVerticalSpace = false;
		gdDetailsComp.horizontalAlignment = GridData.BEGINNING;
		gdDetailsComp.horizontalIndent = 50;
		if( Util.isSmallerResolution() ) {
			gdDetailsComp.heightHint = 285;
		}
		
		GridLayout grlDetailsComp = new GridLayout();
		grlDetailsComp.numColumns = 2;
		grlDetailsComp.verticalSpacing = 5;
		if( Util.isSmallerResolution() ) {
			grlDetailsComp.verticalSpacing = 3;
		}
		grlDetailsComp.horizontalSpacing = 60;

		detailsComposite = new Composite(container, SWT.NONE);
		detailsComposite.setLayoutData(gdDetailsComp);
		detailsComposite.setLayout(grlDetailsComp);

		lblPrinterPort = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PRINTER_PORT) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		if( Util.isSmallerResolution() ) {
			GridData gd1 = new GridData();
			gd1.horizontalAlignment = GridData.CENTER;
			gd1.verticalAlignment = GridData.CENTER;
			gd1.widthHint =  33;
			gd1.heightHint = 33;		
			comboPrinterPort = new SDSTSCombo<ComboLabelValuePair>(detailsComposite,IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_SELECTED_PRINTER_PORT, "label", "value", IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_PRINTER_PORT);
			comboPrinterPort.getLeft().setLayoutData(gd1);
			comboPrinterPort.getRight().setLayoutData(gd1);
		} else {
			comboPrinterPort = new SDSTSCombo<ComboLabelValuePair>(detailsComposite,IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_SELECTED_PRINTER_PORT, "label", "value", IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_PRINTER_PORT);
		}

		lblPrinterType = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_PRINTER_TYPE) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		if( Util.isSmallerResolution() ) {
			GridData gd2 = new GridData();
			gd2.horizontalAlignment = GridData.CENTER;
			gd2.verticalAlignment = GridData.CENTER;
			gd2.widthHint =  33;
			gd2.heightHint = 33;		
			comboPrinterType = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_SELECTED_PRINTER_TYPE, "label", "value",IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_PRINTER_TYPE);
			comboPrinterType.getLeft().setLayoutData(gd2);
			comboPrinterType.getRight().setLayoutData(gd2);
		} else {
			comboPrinterType = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_SELECTED_PRINTER_TYPE, "label", "value",IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_CMB_PRINTER_TYPE);
		}

		lblManufacturer = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MANUFACTURER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));

		txtManufacturer = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_TICKET_PRINTER_CTRL_TXT_MANUFACTURER,IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_TXT_MANUFACTURER);
		txtManufacturer.setEditable(false);
		

		lblModel = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MODEL) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		txtModel = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_TICKET_PRINTER_CTRL_TXT_MODEL,IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_TXT_MODEL);
		txtModel.setEditable(false);

		lblDriver = new SDSTSLabel(detailsComposite, SWT.NONE,LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_DRIVER) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		txtDriver = new SDSTSText(detailsComposite,SWT.BORDER,IVoucherConstants.PREFERENCE_TICKET_PRINTER_CTRL_TXT_DRIVER,IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_TXT_DRIVER);
		txtDriver.setEditable(false);

		GridData gridData5 = new GridData();
		gridData5.heightHint = buttonImage.getBounds().height;
		gridData5.widthHint = buttonImage.getBounds().width;
		gridData5.horizontalAlignment = GridData.END;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.grabExcessHorizontalSpace = false;
		
		btnTicketPrinting = new SDSImageLabel(detailsComposite, SWT.NONE, buttonImage, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_TKT_PRINT_TEST_TKT_PRINTER),IVoucherConstants.PREFERENCE_TICKET_PRINTER_CTRL_BTN_TICKET_PRINTING);
		btnTicketPrinting.setLayoutData(gridData5);
	}

	private void createBtnGroup(Composite detailsComposite)	{

		GridData gdBtnGroup = new GridData();
		gdBtnGroup.grabExcessHorizontalSpace = true;
		gdBtnGroup.verticalAlignment = GridData.CENTER;
		gdBtnGroup.grabExcessVerticalSpace = false;
		gdBtnGroup.horizontalAlignment = GridData.FILL;
		gdBtnGroup.heightHint = 50;
		if( Util.isSmallerResolution() ) {
			gdBtnGroup.heightHint = 50;
		}
		gdBtnGroup.horizontalSpan = 3;

		toggleBtnComposite =  new Composite(detailsComposite,SWT.NONE);
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 3;
		gridLayout3.verticalSpacing = 1;
		gridLayout3.horizontalSpacing = 10;
		gridLayout3.marginHeight = 5;
		if( Util.isSmallerResolution() ) {
			gridLayout3.marginHeight = 0;
		}
		gridLayout3.marginWidth = 5;
		gridLayout3.marginLeft = 5;
		toggleBtnComposite.setLayout(gridLayout3);
		toggleBtnComposite.setBackground(SDSControlFactory.getTSBodyColor());

		GridData toggleCompositeGridData = new GridData();
		toggleCompositeGridData.horizontalAlignment = SWT.CENTER;
		toggleCompositeGridData.horizontalSpan=2;

		toggleBtnComposite.setLayoutData(toggleCompositeGridData);

		GridData gdTouchScreenButton = new GridData();
		gdTouchScreenButton.grabExcessVerticalSpace = true;
		gdTouchScreenButton.heightHint = 50;
		gdTouchScreenButton.widthHint = 50;
		if( Util.isSmallerResolution() ) {
			gdTouchScreenButton.heightHint = 43;
			gdTouchScreenButton.widthHint = 43;
		}
		gdTouchScreenButton.horizontalAlignment = GridData.CENTER;
		gdTouchScreenButton.verticalAlignment = GridData.END;
		gdTouchScreenButton.grabExcessHorizontalSpace = true;
		gdTouchScreenButton.horizontalIndent=30;
		
		lblSize = new SDSTSLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_SIZE) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOUCHER_COLON));
		
		radioButtonControl = new RadioButtonControl("Search by any one of the following");

		radioImageNormalSize = new TSButtonLabel(toggleBtnComposite, SWT.NONE, "", IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_NORMAL_SIZE, true);
		radioImageNormalSize.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
		radioImageNormalSize.setLayoutData(gdTouchScreenButton);
		radioButtonControl.add(radioImageNormalSize);
		
		radioImageSmallSize = new TSButtonLabel(toggleBtnComposite, SWT.NONE, "", IVoucherConstants.PREFERENCE_TICKET_PRINTER_FRM_SMALL_SIZE, false);
		radioImageSmallSize.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		radioImageSmallSize.setLayoutData(gdTouchScreenButton);
		radioImageSmallSize.isSelected();
		radioButtonControl.add(radioImageSmallSize);

		SDSTSLabel dummy = new SDSTSLabel(toggleBtnComposite, SWT.NONE, "");
		dummy.setVisible(false);
		
		lblNormalSize = new SDSTSLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_NORMAL_SIZE));
		lblSmallSize  = new SDSTSLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_SMALL_SIZE));
	}
	
	public void setPrintSize(boolean isSmallSize) {
		this.isSmallSize = isSmallSize;
		if( isSmallSize ){
			lblSmallSize.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_SMALL_SIZE));
		}else{
			lblNormalSize.setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_BTN_NORMAL_SIZE));
		}
	}

	public void setScrollToggleStatus() {
//		if( radioImageSmallSize.getSelection() == true ) {
//			radioImageNormalSize.setSelection(false);
//		} else if( radioImageNormalSize.getSelection() == true ) {
//			radioImageSmallSize.setSelection(false);
//		}
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
			SDSControlFactory.setTouchScreenLabelProperties(lblSize);
			SDSControlFactory.setTouchScreenLabelProperties(lblSmallSize);
			SDSControlFactory.setTouchScreenLabelProperties(lblNormalSize);
			//SDSControlFactory.setTouchScreenLabelProperties(lblTestStatus);
			SDSControlFactory.setTouchScreenTextProperties(txtDriver);
			SDSControlFactory.setTouchScreenTextProperties(txtManufacturer);
			SDSControlFactory.setTouchScreenTextProperties(txtModel);
			//SDSControlFactory.setTouchScreenTextProperties(txtTestStatus);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnTicketPrinting);
			SDSControlFactory.setTouchScreenLabelProperties(lblLogOutMsg);

			GridData gdLblLogOut = (GridData)lblLogOutMsg.getLayoutData();
			gdLblLogOut.horizontalSpan = 2;
			lblLogOutMsg.setLayoutData(gdLblLogOut);

			GridData gdTktPrintTest = (GridData)btnTicketPrinting.getLayoutData();			
			gdTktPrintTest.horizontalAlignment = SWT.CENTER;
			gdTktPrintTest.horizontalSpan = 2;
			if( Util.isSmallerResolution() ) {
				gdTktPrintTest.widthHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				gdTktPrintTest.heightHint = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 18;
			} else
				gdTktPrintTest.horizontalIndent = 100;

			btnTicketPrinting.setLayoutData(gdTktPrintTest);

			GridData gdTxtManufacturer = (GridData)txtManufacturer.getLayoutData();
			gdTxtManufacturer.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtManufacturer.heightHint = 22;
				gdTxtManufacturer.widthHint = 275;
			} else {
				gdTxtManufacturer.horizontalIndent = 15;
				gdTxtManufacturer.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;

			}
			txtManufacturer.setLayoutData(gdTxtManufacturer);
			GridData gdTxtModel = (GridData)txtModel.getLayoutData();
			gdTxtModel.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtModel.heightHint = 22;
				gdTxtModel.widthHint = 275;
			} else {
				gdTxtModel.horizontalIndent = 15;
				gdTxtModel.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtModel.setLayoutData(gdTxtModel);
			GridData gdTxtDriver = (GridData)txtDriver.getLayoutData();
			gdTxtDriver.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdTxtDriver.heightHint = 22;
				gdTxtDriver.widthHint = 275;
			} else {
				gdTxtDriver.horizontalIndent = 15;
				gdTxtDriver.widthHint  = ControlConstants.EX_TEXT_WIDTH + 26;
			}
			txtDriver.setLayoutData(gdTxtDriver);

			GridData gdLbl1 = (GridData)lblNormalSize.getLayoutData();
			gdLbl1.horizontalIndent = 60;
			gdLbl1.horizontalSpan=1;
			lblNormalSize.setLayoutData(gdLbl1);

			GridData gdLbl2 = (GridData)lblSmallSize.getLayoutData();
			gdLbl2.horizontalIndent = 25;
			lblSmallSize.setLayoutData(gdLbl2);
			
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
		radioImageSmallSize.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_DISABLED_BTHEME)));
		radioImageNormalSize.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_DISABLED_BTHEME)));
		//txtTestStatus.setEnabled(false);
		btnTicketPrinting.setEnabled(false);
		btnTicketPrinting.setImage(buttonDisableImage);
		btnTicketPrinting.getTextLabel().setForeground(SDSControlFactory.getDisableButtonTextColor());
		noteComposite.setBackgroundImage(noteImage);
	}


	class CmbContentProvider implements IStructuredContentProvider
	{
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((java.util.List<String>) inputElement).toArray();
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
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

	public Combo getCmbPrinterPort() {
		return cmbPrinterPort;
	}

	public void setCmbPrinterPort(Combo cmbPrinterPort) {
		this.cmbPrinterPort = cmbPrinterPort;
	}

	public SDSComboViewer getCmbViewerPrinterPort() {
		return cmbViewerPrinterPort;
	}

	public void setCmbViewerPrinterPort(SDSComboViewer cmbViewerPrinterPort) {
		this.cmbViewerPrinterPort = cmbViewerPrinterPort;
	}


	public SDSTSText getTxtDriver() {
		return txtDriver;
	}

	public void setTxtDriver(SDSTSText txtDriver) {
		this.txtDriver = txtDriver;
	}

	public TSButtonLabel getRadioImageSmallSize() {
		return radioImageSmallSize;
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

	public void setRadioImageSmallSize(TSButtonLabel radioImageSmallSize) {
		this.radioImageSmallSize = radioImageSmallSize;
	}

	public TSButtonLabel getRadioImageNormalSize() {
		return radioImageNormalSize;
	}

	public void setRadioImageNormalSize(TSButtonLabel radioImageNormalSize) {
		this.radioImageNormalSize = radioImageNormalSize;
	}

	public SDSImageLabel getBtnTicketPrinting() {
		return btnTicketPrinting;
	}

	public void setBtnTicketPrinting(SDSImageLabel btnTicketPrinting) {
		this.btnTicketPrinting = btnTicketPrinting;
	}

	public Combo getCmbPrinterType() {
		return cmbPrinterType;
	}

	public void setCmbPrinterType(Combo cmbPrinterType) {
		this.cmbPrinterType = cmbPrinterType;
	}

	public SDSComboViewer getCmbViewerPrinterType() {
		return cmbViewerPrinterType;
	}

	public void setCmbViewerPrinterType(SDSComboViewer cmbViewerPrinterType) {
		this.cmbViewerPrinterType = cmbViewerPrinterType;
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

	private void createImages() {
		buttonImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
		noteImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.NOTE_IMAGE));
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
