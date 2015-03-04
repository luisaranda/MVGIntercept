/*****************************************************************************
 * $Id: BaseReconciliationComposite.java,v 1.40.5.0, 2013-10-25 16:52:01Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 11:52:01 AM$
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

import com.ballydev.sds.cdatepicker.ACW;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSDatePicker;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSLabelText;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This method creates the base reconciliation composite
 * 
 * @author kLokesh
 * @version $Revision: 42$
 */
public class BaseReconciliationComposite extends Composite {

	/**
	 * Instance of the start time label
	 */
	private CbctlLabel lblStartTime = null;

	/**
	 * Instance of the end time label
	 */
	private CbctlLabel lblEndTime = null;

	/**
	 * Instance of the start time date picker
	 */
	private SDSDatePicker startTime = null;

	/**
	 * Instance of the end time date picker
	 */
	private SDSDatePicker endTime = null;

	/**
	 * Instance of the employee or asset label
	 */
	private CbctlMandatoryLabel employeeOrAssetLbl = null;

	/**
	 * Instance of the employee or asset text
	 */
	private SDSTSText txtEmployeeOrAsset = null;

	/**
	 * Instance of the employee or asset label
	 */
	private CbctlLabel lblBarcode = null;

	/**
	 * Instance of the employee or asset text
	 */
	private SDSTSText txtBarcode = null;

	/**
	 * Instance of the barcode label text
	 */
	// private SDSTSLabelText lblTxtBarcode = null;
	/**
	 * Instance of the accepted label text
	 */
	private SDSTSLabelText lblTxtAccepted;

	/**
	 * Instance of the no read label text
	 */
	private SDSTSLabelText lblTxtNotRead;

	/**
	 * Instance of the total label text
	 */
	private SDSTSLabelText lblTxtTotal;

	/**
	 * Instance of the uncommitted label text
	 */
	private SDSTSLabelText lblTxtUncommitted;

	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;
	
	/**
	 * Instance of the detail touch screen radio image
	 */	
	private TSButtonLabel tBtnCashier = null;
	
	/**
	 * Instance of the summary touch screen radio image
	 */	
	private TSButtonLabel tBtnKiosk = null;

	/**
	 * Boolean to track whether the cashier radio button is selected
	 */
	private boolean isCashier = true;

	/**
	 * Instance of the add to batch button
	 */
	private SDSImageLabel btnAddToBatch;

	/**
	 * Instance of the submit button
	 */
	private SDSImageLabel btnSubmit;

	/**
	 * Instance of the current batch button
	 */
	private SDSImageLabel btnCurrentBatch = null;

	/**
	 * Instance of the questinable button
	 */
	private SDSImageLabel btnQuestionable = null;

	/**
	 * Instance of the batch summary button
	 */
	private SDSImageLabel btnBatchSummary = null;

	/**
	 * Instance of the cashier kiosk group
	 */
	private Group grpCashierKiosk;

	/**
	 * Instance of the button composite
	 */
	private Composite btnComposite;

	/**
	 * Instance of the cancel button
	 */
	private SDSImageLabel btnCancel = null;

	/**
	 * Instance of the batch time composite
	 */
	private Composite batchTimeComposite = null;

	/**
	 * Instance of the cashier label
	 */
	private CbctlLabel lblCashier = null;

	/**
	 * Instance of the kiosk label
	 */
	private CbctlLabel lblKiosk = null;

	/**
	 * Instance of the toggle btn composite
	 */
	private Composite toggleBtnComposite = null;

	/**
	 * Instance of current scan details group
	 */
	private Group grpCurrentScnDtl = null;

	/**
	 * Instance of the dummy label
	 */
	private CbctlLabel lblBatchNo = null;

	/**
	 * Instance of the dummy label
	 */
	private SDSTSLabel lblBatchValue = null;

	private Image buttonBG;

	
	/**
	 * Instance of the Xml File label text
	 */
	private SDSTSLabelText lblTxtXmlFile;
	
	/**
	 * Instance of the submit button
	 */
	private SDSImageLabel btnBrowse;
	
	/**
	 * Instance of the submit button
	 */
	private SDSImageLabel btnXMLSubmit;
	
	/**
	 * @returns whether cashier is selected
	 */
	public boolean isCashier() {
		return isCashier;
	}

	/**
	 * This method enables the cashier or kiosk button according to the
	 * selection
	 * 
	 * @param isCashier
	 */
	public void setCashier(boolean isCashier) {
		this.isCashier = isCashier;
	}

	/**
	 * This method toggles the selection between the cashier and kiosk button
	 * 
	 * @param isCashier
	 */
	public void setKioskCashierToggle(boolean isCashier) {
		this.isCashier = isCashier;
//		if (isCashier) {
//			tBtnCashier.setSelection(true);
//			tBtnKiosk.setSelection(false);
//		} else {
//			tBtnKiosk.setSelection(true);
//			tBtnCashier.setSelection(false);
//		}
	}

	/**
	 * Constructor of the class
	 */
	public BaseReconciliationComposite(Composite parent, int style) {
		super(parent, style);
		this.setBackground(SDSControlFactory.getTSBodyColor());
		new HeaderComposite(this, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_RECONCILIATION));
		initialize();
		this.setBounds(0, 0, 800, 500);
	}

	/**
	 * This method contains call to various other methods which will create the
	 * composite
	 */
	private void initialize() {
		createImages();
		pageHeading();
		reconciliationType();
		setBarcodeInformation();
		currentScanDetails();
		submitProcess();
		uploadXMLControls();
		setControlProperties();
		setPageLayout();
	}

	/**
	 * This method set the layout properties
	 */
	private void setPageLayout() {
		// Setting Page Layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		if( Util.isSmallerResolution()) {
			gridLayout.verticalSpacing = 2;
		} else
			gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		gridLayout.horizontalSpacing = 5;

		// Grid Data for the Page
		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		this.setLayout(gridLayout);
		this.setLayoutData(gdPage);
	}

	/**
	 * This method creates the heading composite
	 */
	private void pageHeading() {
		// Setting Heading Layout
		GridData headCompositeGridData = new GridData();
		headCompositeGridData.horizontalAlignment = GridData.FILL;
		headCompositeGridData.grabExcessHorizontalSpace = true;
		headCompositeGridData.grabExcessVerticalSpace = false;
		headCompositeGridData.verticalAlignment = GridData.FILL;
		headCompositeGridData.horizontalSpan = 2;
		// HeaderComposite headerComposite = new
		// HeaderComposite(this,SWT.None,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_RECONCILIATION));
	}

	private void reconciliationType() {

		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 770;
		}
		gdDetailsGrp.grabExcessHorizontalSpace = true;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;		

		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 5;
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;
		
		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);
		
		Composite header = new Composite(container, SWT.NONE);
		
		GridData headerData = new GridData();
		if(Util.isSmallerResolution()) {
			headerData.widthHint = 865;
		} else 
			headerData.widthHint = 910;
		headerData.heightHint = 30;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE | SWT.COLOR_WHITE, LabelLoader.getLabelValue(IDBLabelKeyConstants.GRP_RECON_TYPE_HEADING));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
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
		readOnlyComp.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdReadOnly = new GridData();
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 770;
		} 
		if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 4 && Util.isSmallerResolution() ) {
				gdReadOnly.widthHint = 870;
			}
		}
		gdReadOnly.grabExcessHorizontalSpace = true;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		gdReadOnly.horizontalAlignment = GridData.CENTER;		

		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns = 6;
		grlReadOnly.verticalSpacing = 05;
		grlReadOnly.horizontalSpacing = 1;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		toggleBtnComposite = new Composite(readOnlyComp, SWT.NONE);
		toggleBtnComposite.setBackground(SDSControlFactory.getTSMiddleBodyColor());

		GridLayout grlBtnComposite = new GridLayout();
		grlBtnComposite.numColumns = 6;
		grlBtnComposite.verticalSpacing = 1;
		if (Util.isSmallerResolution()) {
			grlBtnComposite.horizontalSpacing = 15;
		}
		grlBtnComposite.marginHeight = 0;
		grlBtnComposite.marginWidth = 5;
		grlBtnComposite.marginLeft = 5;
		toggleBtnComposite.setLayout(grlBtnComposite);

		GridData toggleCompositeGridData = new GridData();
		toggleCompositeGridData.horizontalAlignment = SWT.CENTER;
		if (Util.isSmallerResolution()) {
			toggleCompositeGridData.widthHint = 910;
		}
		toggleBtnComposite.setLayoutData(toggleCompositeGridData);
//		toggleBtnComposite.setBackground(new Color (Display.getCurrent(), 221,211,21));

		radioButtonControl = new RadioButtonControl("Search by any one of the following");

		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.BEGINNING;
		gdRadio.horizontalIndent = 5;

		tBtnCashier = new TSButtonLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_SET_EMPLOYEE));
		tBtnCashier.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
		tBtnCashier.setLayoutData(gdRadio);
		radioButtonControl.add(tBtnCashier);
	
		tBtnKiosk = new TSButtonLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_SET_KIOSK));
		tBtnKiosk.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		tBtnKiosk.setLayoutData(gdRadio);
		radioButtonControl.add(tBtnKiosk);

		employeeOrAssetLbl = new CbctlMandatoryLabel(toggleBtnComposite,SWT.BOLD, LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
		txtEmployeeOrAsset = new SDSTSText(toggleBtnComposite,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID);
		txtEmployeeOrAsset.setTextLimitChkEnabled(true);

		lblStartTime = new CbctlLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_START_TIME));

		createDateFromTime();

		lblCashier = new CbctlLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_SET_EMPLOYEE).trim());
		lblKiosk   = new CbctlLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_SET_KIOSK).trim());

		lblBatchNo    = new CbctlLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO).trim());
		
		GridData gdBatchValue = new GridData();
		gdBatchValue.heightHint = 18;
		gdBatchValue.grabExcessHorizontalSpace = true;
		gdBatchValue.horizontalAlignment = GridData.CENTER;
		gdBatchValue.verticalAlignment = GridData.CENTER;
		gdBatchValue.widthHint = 150;
		
		lblBatchValue = new SDSTSLabel(toggleBtnComposite,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_VALUE_NOT_SET));
		lblBatchValue.setLayoutData(gdBatchValue);
		
		lblEndTime    = new CbctlLabel(toggleBtnComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_END_TIME));

		createDateToTime();

		txtEmployeeOrAsset.getGridData().horizontalAlignment = SWT.END;
		txtEmployeeOrAsset.setTextLimit(5);
	}

	/**
	 * This method initializes dateStartTime
	 */
	private void createDateFromTime() {
		GridData gdStartTime = new GridData();
		gdStartTime.heightHint = 28;
		gdStartTime.grabExcessHorizontalSpace = true;
		gdStartTime.horizontalAlignment = GridData.CENTER;
		gdStartTime.verticalAlignment = GridData.CENTER;
		if (Util.isSmallerResolution())
			gdStartTime.horizontalIndent = 0;
		else
			gdStartTime.horizontalIndent = 10;
		gdStartTime.widthHint = 230;
		startTime = new SDSDatePicker(toggleBtnComposite, ACW.FOOTER | ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.TIME_CUSTOM | ACW.DIGITAL_CLOCK, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_START_TIME), IVoucherConstants.RECON_CTRL_LBL_START_TIME);
		startTime.setLayoutData(gdStartTime);
	}

	/**
	 * This method initializes dateEndTime
	 */
	private void createDateToTime() {
		GridData gdEndTime = new GridData();
		gdEndTime.heightHint = 28;
		gdEndTime.horizontalAlignment = GridData.CENTER;
		gdEndTime.verticalAlignment = GridData.CENTER;
		gdEndTime.grabExcessHorizontalSpace = true;
		gdEndTime.widthHint = 230;
		if (Util.isSmallerResolution())
			gdEndTime.horizontalIndent = 0;
		else
			gdEndTime.horizontalIndent = 10;
		endTime = new SDSDatePicker(toggleBtnComposite, ACW.FOOTER | ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.TIME_CUSTOM | ACW.DIGITAL_CLOCK, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_END_TIME), IVoucherConstants.RECON_CTRL_LBL_END_TIME);
		endTime.setLayoutData(gdEndTime);
	}

	private void setBarcodeInformation() {

		GridLayout grlBarcodeComposite = new GridLayout();
		grlBarcodeComposite.numColumns = 4;
		grlBarcodeComposite.marginWidth = 4;
		grlBarcodeComposite.marginHeight = 5;
		grlBarcodeComposite.marginLeft = 1;
		grlBarcodeComposite.verticalSpacing = 10;
		grlBarcodeComposite.horizontalSpacing = 20;

		GridData gdBarcodeComposite = new GridData();
		gdBarcodeComposite.horizontalAlignment = GridData.CENTER;
		gdBarcodeComposite.grabExcessHorizontalSpace = false;
		gdBarcodeComposite.widthHint = 700;

		Composite barcodeComposite = new Composite(this, SWT.NONE);
		barcodeComposite.setBackground(SDSControlFactory.getTSBodyColor());
		barcodeComposite.setLayout(grlBarcodeComposite);
		barcodeComposite.setLayoutData(gdBarcodeComposite);

		lblBarcode = new CbctlLabel(barcodeComposite, SWT.BOLD, AppContextValues.getInstance().getTicketText() 
				+ LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BARCODE));
		txtBarcode = new SDSTSText(barcodeComposite, SWT.BEGINNING, IVoucherConstants.RECON_CTRL_TXT_BARCODE, IVoucherConstants.RECON_CTRL_TXT_BARCODE, true);
		txtBarcode.setTextLimitChkEnabled(true);
		txtBarcode.setTextLimit(18);

		btnAddToBatch = new SDSImageLabel(barcodeComposite,SWT.SHADOW_ETCHED_OUT, buttonBG, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_ADD_TO_BATCH),"scanBtn");
	}

	private void currentScanDetails() {
		
		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 770;
		}
		gdDetailsGrp.grabExcessHorizontalSpace = true;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;

		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 0;
		if( Util.isSmallerResolution() ) {
			grlDetailsGrp.verticalSpacing = 0;	
		}
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;
		
		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);
		
		Composite header = new Composite(container, SWT.NONE);
		
		GridData headerData = new GridData();
		if(Util.isSmallerResolution()) {
			headerData.widthHint = 865;
		} else 
			headerData.widthHint = 910;
		headerData.heightHint = 30;
		
		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());
		
		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE | SWT.COLOR_WHITE, LabelLoader.getLabelValue(IDBLabelKeyConstants.GRP_RECN_CURR_SCN_DETAIL_HEADING));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		Composite readOnlyComp = new Composite(container, SWT.NONE);
		readOnlyComp.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdReadOnly = new GridData();
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 770;
		}
		gdReadOnly.grabExcessHorizontalSpace = true;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		gdReadOnly.horizontalAlignment = GridData.CENTER;		

		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns = 2;
		grlReadOnly.verticalSpacing = 3;
		if( Util.isSmallerResolution() ) {
			grlReadOnly.verticalSpacing = 0;	
		}
		grlReadOnly.horizontalSpacing = 1;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		lblTxtAccepted = new SDSTSLabelText(readOnlyComp, IVoucherConstants.RECON_CTRL_LBLTXT_ACCEPTED, 
				IVoucherConstants.RECON_CTRL_LBLTXT_ACCEPTED, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ACCEPTED) + ":", SWT.RIGHT);
		lblTxtAccepted.getSdsText().setEnabled(false);
		lblTxtAccepted.getSdsLabel().setAlignment(SWT.BEGINNING);

		lblTxtNotRead = new SDSTSLabelText(readOnlyComp, IVoucherConstants.RECON_CTRL_LBLTXT_NO_READ, IVoucherConstants.RECON_CTRL_LBLTXT_NO_READ, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_NO_READ) + ":",SWT.RIGHT);
		lblTxtNotRead.getSdsText().setEnabled(false);
		lblTxtNotRead.getSdsLabel().setAlignment(SWT.BEGINNING);

		lblTxtTotal = new SDSTSLabelText(readOnlyComp, IVoucherConstants.RECON_CTRL_LBLTXT_TOTAL, IVoucherConstants.RECON_CTRL_LBLTXT_TOTAL, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TOTAL)+ ":",SWT.RIGHT);
		lblTxtTotal.getSdsText().setEnabled(false);
		lblTxtTotal.getSdsLabel().setAlignment(SWT.BEGINNING);

		lblTxtUncommitted = new SDSTSLabelText(readOnlyComp, IVoucherConstants.RECON_CTRL_LBLTXT_UNCOMMITED, IVoucherConstants.RECON_CTRL_LBLTXT_UNCOMMITED, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_UNCOMMITED)+ ":",SWT.RIGHT);
		lblTxtUncommitted.getSdsText().setEnabled(false);
		lblTxtUncommitted.getSdsLabel().setAlignment(SWT.BEGINNING);
	}

private void uploadXMLControls() {
		
		Composite containerXML = new Composite(this, SWT.NONE);
		containerXML.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdDetailsGrp = new GridData();
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 770;
		}
		gdDetailsGrp.grabExcessHorizontalSpace = true;
		gdDetailsGrp.grabExcessVerticalSpace = false;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;

		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 3;
		if( Util.isSmallerResolution() ) {
			grlDetailsGrp.verticalSpacing = 0;	
		}
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;
		
		containerXML.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		containerXML.setLayout(grlDetailsGrp);
		containerXML.setLayoutData(gdDetailsGrp);
		
		Composite headerXML = new Composite(containerXML, SWT.NONE);
		
		GridData headerData = new GridData();
		if(Util.isSmallerResolution()) {
			headerData.widthHint = 865;
		} else 
			headerData.widthHint = 910;
		headerData.heightHint = 30;
		
		headerXML.setLayoutData(headerData);
		headerXML.setLayout(new GridLayout());
		
		headerXML.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		
		SDSTSLabel lblMessage = new SDSTSLabel(headerXML, SWT.NONE | SWT.COLOR_WHITE, LabelLoader.getLabelValue(IDBLabelKeyConstants.GRP_RECN_XML_UPLOAD_HEADING));
		
		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.CENTER;
		lblMessage.setLayoutData(gridDataPrefHeader);
		lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//		SDSControlFactory.setHeadingLabelPropertiesWithoutBG(lblMessage);
		
		Composite readOnlyComp = new Composite(containerXML, SWT.NONE);
		readOnlyComp.setBackground(SDSControlFactory.getTSBodyColor());
		
		GridData gdReadOnly = new GridData();
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 770;
		}
		gdReadOnly.grabExcessHorizontalSpace = true;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;
		gdReadOnly.horizontalAlignment = GridData.CENTER;		

		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns = 3;
		grlReadOnly.verticalSpacing = 3;
		if( Util.isSmallerResolution() ) {
			grlReadOnly.verticalSpacing = 0;	
		}
		grlReadOnly.horizontalSpacing = 1;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		GridData gridData5 = new GridData();
		gridData5.heightHint = buttonBG.getBounds().height;
		gridData5.widthHint = buttonBG.getBounds().width;
		gridData5.horizontalAlignment = GridData.BEGINNING;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.horizontalIndent = 20;
		if (Util.isSmallerResolution()) {
			gridData5.horizontalIndent = 0;
		}

		GridData gridData6 = new GridData();
		gridData6.heightHint = buttonBG.getBounds().height;
		gridData6.widthHint = buttonBG.getBounds().width;
		gridData6.horizontalAlignment = GridData.BEGINNING;
		gridData6.verticalAlignment = GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalIndent = 20;
		if (Util.isSmallerResolution()) {
			gridData6.horizontalIndent = 0;
		}

		lblTxtXmlFile = new SDSTSLabelText(readOnlyComp, IVoucherConstants.UPLOAD_FRM_TXT_UPLOAD_XML_FILE, 
				IVoucherConstants.UPLOAD_FRM_TXT_UPLOAD_XML_FILE, " "+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_UPLOAD_XML) + ":", SWT.LEFT);
		
		lblTxtXmlFile.getSdsLabel().setAlignment(SWT.BEGINNING);
				

		btnBrowse = new SDSImageLabel(readOnlyComp, SWT.BUTTON3, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BRWOSE_XML),
				IVoucherConstants.RECON_CTRL_BTN_BROWSEXML);
		if( !Util.isSmallerResolution() ) {
			btnBrowse.setLayoutData(gridData6);
		}
		btnXMLSubmit = new SDSImageLabel(readOnlyComp, SWT.BUTTON1, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_UPLOAD_FILE),
				IVoucherConstants.RECON_CTRL_BTN_SUMBITXML);
		if( !Util.isSmallerResolution() ) {
			btnXMLSubmit.setLayoutData(gridData5);
		}
	}


	private void submitProcess() {

		GridData gdBtnComposite = new GridData();
		gdBtnComposite.grabExcessHorizontalSpace = true;
		gdBtnComposite.horizontalAlignment = GridData.CENTER;
		gdBtnComposite.verticalAlignment = GridData.CENTER;
		gdBtnComposite.widthHint = 720;
		gdBtnComposite.horizontalIndent = 0;

		if (Util.isSmallerResolution()) {
			gdBtnComposite.horizontalIndent = 11;
			
		}
		GridLayout grlBtnComposite = new GridLayout();
		grlBtnComposite.numColumns = 8;
		grlBtnComposite.marginWidth = 4;
		grlBtnComposite.marginHeight = 5;
		grlBtnComposite.marginLeft = 0;
		grlBtnComposite.verticalSpacing = 10;
		grlBtnComposite.horizontalSpacing = 10;
		if (Util.isSmallerResolution()) {
			grlBtnComposite.marginLeft = 0;
			grlBtnComposite.horizontalSpacing = 0;
		}
		
		GridData gridData5 = new GridData();
		gridData5.heightHint = buttonBG.getBounds().height;
		gridData5.widthHint = buttonBG.getBounds().width;
		gridData5.horizontalAlignment = GridData.BEGINNING;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.horizontalIndent = 20;
		if (Util.isSmallerResolution()) {
			gridData5.horizontalIndent = 0;
		}

		GridData gridData6 = new GridData();
		gridData6.heightHint = buttonBG.getBounds().height;
		gridData6.widthHint = buttonBG.getBounds().width;
		gridData6.horizontalAlignment = GridData.BEGINNING;
		gridData6.verticalAlignment = GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalIndent = 20;
		if (Util.isSmallerResolution()) {
			gridData6.horizontalIndent = 0;
		}

		btnComposite = new Composite(this, SWT.NONE);
		btnComposite.setLayout(grlBtnComposite);
		btnComposite.setLayoutData(gdBtnComposite);
		btnSubmit = new SDSImageLabel(btnComposite, SWT.BUTTON3, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_SUBMIT_BARCODE),
				IVoucherConstants.RECON_CTRL_BTN_SUBMIT);
		if( !Util.isSmallerResolution() ) {
			btnSubmit.setLayoutData(gridData6);
		}
		btnCancel = new SDSImageLabel(btnComposite, SWT.BUTTON1, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CANCEL),
				IVoucherConstants.RECON_CTRL_BTN_CANCEL);
		if( !Util.isSmallerResolution() ) {
			btnCancel.setLayoutData(gridData6);
		}
		btnQuestionable = new SDSImageLabel(btnComposite, SWT.BUTTON1, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.QUESTIONABLE_VOUCHERS_COUPONS),
				IVoucherConstants.RECON_CTRL_BTN_GETQUESTIONABLE);
		if( !Util.isSmallerResolution() ) {
			btnQuestionable.setLayoutData(gridData5);
		}
		btnCurrentBatch = new SDSImageLabel(btnComposite, SWT.BUTTON1, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_GET_CURRENT_BATCH),
				IVoucherConstants.RECON_CTRL_BTN_GET_CURRENT_BATCH);
		if( !Util.isSmallerResolution() ) {
			btnCurrentBatch.setLayoutData(gridData5);
		}
		btnBatchSummary = new SDSImageLabel(btnComposite, SWT.BUTTON1, buttonBG, LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_BATCH_DETAILS_TITLE),
				IVoucherConstants.RECON_CTRL_BTN_GET_BATCH_SUMM);
		if( !Util.isSmallerResolution() ) {
			btnBatchSummary.setLayoutData(gridData5);
		}
	}

	/**
	 * This method sets the default grid properties for all the controls
	 */
	private void setControlProperties() {
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblCashier);
			SDSControlFactory.setTouchScreenLabelProperties(lblEndTime);
			SDSControlFactory.setTouchScreenLabelProperties(lblBatchNo);
//			SDSControlFactory.setTouchScreenLabelProperties(lblBatchValue);
			SDSControlFactory.setTouchScreenLabelProperties(lblKiosk);
			SDSControlFactory.setTouchScreenLabelProperties(lblStartTime);
			SDSControlFactory.setTouchScreenTextProperties(txtEmployeeOrAsset);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnAddToBatch);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnSubmit);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnCurrentBatch);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnQuestionable);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnBatchSummary);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnCancel);
			SDSControlFactory.setTouchScreenLabelProperties(lblTxtAccepted.getSdsLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblBarcode);
			SDSControlFactory.setTouchScreenLabelProperties(lblTxtNotRead.getSdsLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblTxtTotal.getSdsLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblTxtUncommitted.getSdsLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtBarcode);
			SDSControlFactory.setTouchScreenTextProperties(lblTxtAccepted.getSdsText());
			SDSControlFactory.setTouchScreenTextProperties(lblTxtNotRead.getSdsText());
			SDSControlFactory.setTouchScreenTextProperties(lblTxtTotal.getSdsText());
			SDSControlFactory.setTouchScreenTextProperties(lblTxtUncommitted.getSdsText());
			SDSControlFactory.setTouchScreenLabelProperties(employeeOrAssetLbl.getLabel());

			SDSControlFactory.setTouchScreenImageLabelProperties(btnXMLSubmit);
			SDSControlFactory.setTouchScreenImageLabelProperties(btnBrowse);
			SDSControlFactory.setTouchScreenLabelProperties(lblTxtXmlFile.getSdsLabel());
			SDSControlFactory.setTouchScreenTextProperties(lblTxtXmlFile.getSdsText());
			
			
			int width = 0;
			int height = 0;
			if (Util.isSmallerResolution()) {
				width = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 8;
				height = ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 14;
				
				GridData gdBarcode = (GridData) txtBarcode.getLayoutData();
				gdBarcode.widthHint = 160;
				txtBarcode.setLayoutData(gdBarcode);

			} else {
				width = ControlConstants.BUTTON_WIDTH;
				height = ControlConstants.BUTTON_HEIGHT;
			}

			if (Util.isSmallerResolution()) {
				
				GridData gdEmpOrAsset = (GridData) txtEmployeeOrAsset.getLayoutData();
				gdEmpOrAsset.widthHint  = 150;
				gdEmpOrAsset.heightHint = 22;
				txtEmployeeOrAsset.setLayoutData(gdEmpOrAsset);
				
				GridData gdBtnAddToBatch = (GridData) btnAddToBatch.getLayoutData();
				gdBtnAddToBatch.widthHint = width;
				gdBtnAddToBatch.heightHint = height;
				btnAddToBatch.setLayoutData(gdBtnAddToBatch);
				
				GridData gdBtnSubmit = (GridData) btnSubmit.getLayoutData();
				gdBtnSubmit.widthHint = width;
				gdBtnSubmit.heightHint = height;
				btnSubmit.setLayoutData(gdBtnSubmit);

				GridData gdXMLBtnSubmit = (GridData) btnXMLSubmit.getLayoutData();
				gdXMLBtnSubmit.widthHint = width;
				gdXMLBtnSubmit.heightHint = height;
				btnXMLSubmit.setLayoutData(gdXMLBtnSubmit);
				
				GridData gdXMLBrowse = (GridData) btnBrowse.getLayoutData();
				gdXMLBrowse.widthHint = width;
				gdXMLBrowse.heightHint = height;
				btnBrowse.setLayoutData(gdXMLBrowse);
				
				
				GridData gdBtnCurrentBatch = (GridData) btnCurrentBatch.getLayoutData();
				gdBtnCurrentBatch.widthHint = width;
				gdBtnCurrentBatch.heightHint = height;
				btnCurrentBatch.setLayoutData(gdBtnCurrentBatch);
				
				GridData gdBtnQuestionable = (GridData) btnQuestionable.getLayoutData();
				gdBtnQuestionable.widthHint = width;
				gdBtnQuestionable.heightHint = height;
				btnQuestionable.setLayoutData(gdBtnQuestionable);
				
				GridData gdBtnCancel = (GridData) btnCancel.getLayoutData();
				gdBtnCancel.widthHint = width;
				gdBtnCancel.heightHint = height;
				btnCancel.setLayoutData(gdBtnCancel);
	
				GridData gdBtnBatchSumm = (GridData) btnBatchSummary.getLayoutData();
				gdBtnBatchSumm.widthHint = width;
				gdBtnBatchSumm.heightHint = height;
				btnBatchSummary.setLayoutData(gdBtnBatchSumm);
			}

			GridData gdLabel1 = (GridData) lblCashier.getLayoutData();
			gdLabel1.horizontalIndent = 8;
			lblCashier.setLayoutData(gdLabel1);
			
			GridData gdLabel2 = (GridData) lblKiosk.getLayoutData();
			gdLabel2.horizontalIndent = 8;
			lblKiosk.setLayoutData(gdLabel2);
			
			
			
			GridData gdText = (GridData) lblTxtAccepted.getSdsText().getLayoutData();
			gdText.widthHint  = 160;
			gdText.heightHint = 22;
			lblTxtAccepted.getSdsText().setLayoutData(gdText);
			
			GridData gdTextAmt = (GridData) lblTxtNotRead.getSdsText().getLayoutData();
			gdTextAmt.widthHint  = 160;
			gdTextAmt.heightHint = 22;
			lblTxtNotRead.getSdsText().setLayoutData(gdTextAmt);
			
			GridData gdState = (GridData) lblTxtTotal.getSdsText().getLayoutData();
			gdState.widthHint  = 160;
			gdState.heightHint = 22;
			lblTxtTotal.getSdsText().setLayoutData(gdState);
			
			GridData gdPlayerId = (GridData) lblTxtUncommitted.getSdsText().getLayoutData();
			gdPlayerId.widthHint  = 160;
			gdPlayerId.heightHint = 22;
			lblTxtUncommitted.getSdsText().setLayoutData(gdPlayerId);

			GridData gdXmlFilePath = (GridData) lblTxtXmlFile.getSdsText().getLayoutData();
			gdXmlFilePath.widthHint  = 150;
			gdXmlFilePath.heightHint = 22;
			lblTxtXmlFile.getSdsText().setLayoutData(gdXmlFilePath);
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the batchTimeComposite
	 */
	public Composite getBatchTimeComposite() {
		return batchTimeComposite;
	}

	/**
	 * @param batchTimeComposite
	 *            the batchTimeComposite to set
	 */
	public void setBatchTimeComposite(Composite batchTimeComposite) {
		this.batchTimeComposite = batchTimeComposite;
	}

	/**
	 * @return the btnComposite
	 */
	public Composite getBtnComposite() {
		return btnComposite;
	}

	/**
	 * @param btnComposite
	 *            the btnComposite to set
	 */
	public void setBtnComposite(Composite btnComposite) {
		this.btnComposite = btnComposite;
	}

	/**
	 * @return the startTime
	 */
	public SDSDatePicker getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(SDSDatePicker startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public SDSDatePicker getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(SDSDatePicker endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the employeeOrAssetLbl
	 */
	public CbctlMandatoryLabel getEmployeeOrAssetLbl() {
		return employeeOrAssetLbl;
	}

	/**
	 * @param employeeOrAssetLbl
	 *            the employeeOrAssetLbl to set
	 */
	public void setEmployeeOrAssetLbl(CbctlMandatoryLabel employeeOrAssetLbl) {
		this.employeeOrAssetLbl = employeeOrAssetLbl;
	}

	/**
	 * @return the txtEmployeeOrAsset
	 */
	public SDSTSText getTxtEmployeeOrAsset() {
		return txtEmployeeOrAsset;
	}

	/**
	 * @param txtEmployeeOrAsset
	 *            the txtEmployeeOrAsset to set
	 */
	public void setTxtEmployeeOrAsset(SDSTSText txtEmployeeOrAsset) {
		this.txtEmployeeOrAsset = txtEmployeeOrAsset;
	}

	/**
	 * @return the lblBarcode
	 */
	public SDSTSLabel getLblBarcode() {
		return lblBarcode;
	}

	/**
	 * @param lblBarcode
	 *            the lblBarcode to set
	 */
	public void setLblBarcode(CbctlLabel lblBarcode) {
		this.lblBarcode = lblBarcode;
	}

	/**
	 * @return the txtBarcode
	 */
	public SDSTSText getTxtBarcode() {
		return txtBarcode;
	}

	/**
	 * @param txtBarcode
	 *            the txtBarcode to set
	 */
	public void setTxtBarcode(SDSTSText txtBarcode) {
		this.txtBarcode = txtBarcode;
	}

	/**
	 * @return the lblTxtAccepted
	 */
	public SDSTSLabelText getLblTxtAccepted() {
		return lblTxtAccepted;
	}

	/**
	 * @param lblTxtAccepted
	 *            the lblTxtAccepted to set
	 */
	public void setLblTxtAccepted(SDSTSLabelText lblTxtAccepted) {
		this.lblTxtAccepted = lblTxtAccepted;
	}

	/**
	 * @return the lblTxtNotRead
	 */
	public SDSTSLabelText getLblTxtNotRead() {
		return lblTxtNotRead;
	}

	/**
	 * @param lblTxtNotRead
	 *            the lblTxtNotRead to set
	 */
	public void setLblTxtNotRead(SDSTSLabelText lblTxtNotRead) {
		this.lblTxtNotRead = lblTxtNotRead;
	}

	/**
	 * @return the lblTxtTotal
	 */
	public SDSTSLabelText getLblTxtTotal() {
		return lblTxtTotal;
	}

	/**
	 * @param lblTxtTotal
	 *            the lblTxtTotal to set
	 */
	public void setLblTxtTotal(SDSTSLabelText lblTxtTotal) {
		this.lblTxtTotal = lblTxtTotal;
	}

	/**
	 * @return the lblTxtUncommitted
	 */
	public SDSTSLabelText getLblTxtUncommitted() {
		return lblTxtUncommitted;
	}

	/**
	 * @param lblTxtUncommitted
	 *            the lblTxtUncommitted to set
	 */
	public void setLblTxtUncommitted(SDSTSLabelText lblTxtUncommitted) {
		this.lblTxtUncommitted = lblTxtUncommitted;
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
	 * @return the tBtnCashier
	 */
	public TSButtonLabel getTBtnCashier() {
		return tBtnCashier;
	}

	/**
	 * @param btnCashier
	 *            the tBtnCashier to set
	 */
	public void setTBtnCashier(TSButtonLabel btnCashier) {
		tBtnCashier = btnCashier;
	}

	/**
	 * @return the tBtnKiosk
	 */
	public TSButtonLabel getTBtnKiosk() {
		return tBtnKiosk;
	}

	/**
	 * @param btnKiosk
	 *            the tBtnKiosk to set
	 */
	public void setTBtnKiosk(TSButtonLabel btnKiosk) {
		tBtnKiosk = btnKiosk;
	}

	/**
	 * @return the btnAddToBatch
	 */
	public SDSImageLabel getBtnAddToBatch() {
		return btnAddToBatch;
	}

	/**
	 * @param btnAddToBatch
	 *            the btnAddToBatch to set
	 */
	public void setBtnAddToBatch(SDSImageLabel btnAddToBatch) {
		this.btnAddToBatch = btnAddToBatch;
	}

	/**
	 * @return the btnSubmit
	 */
	public SDSImageLabel getBtnSubmit() {
		return btnSubmit;
	}

	/**
	 * @param btnSubmit
	 *            the btnSubmit to set
	 */
	public void setBtnSubmit(SDSImageLabel btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	/**
	 * @return the btnCurrentBatch
	 */
	public SDSImageLabel getBtnCurrentBatch() {
		return btnCurrentBatch;
	}

	/**
	 * @param btnCurrentBatch
	 *            the btnCurrentBatch to set
	 */
	public void setBtnCurrentBatch(SDSImageLabel btnCurrentBatch) {
		this.btnCurrentBatch = btnCurrentBatch;
	}

	/**
	 * @return the btnQuestionable
	 */
	public SDSImageLabel getBtnQuestionable() {
		return btnQuestionable;
	}

	/**
	 * @param btnQuestionable
	 *            the btnQuestionable to set
	 */
	public void setBtnQuestionable(SDSImageLabel btnQuestionable) {
		this.btnQuestionable = btnQuestionable;
	}

	/**
	 * @return the grpCashierKiosk
	 */
	public Group getGrpCashierKiosk() {
		return grpCashierKiosk;
	}

	/**
	 * @param grpCashierKiosk
	 *            the grpCashierKiosk to set
	 */
	public void setGrpCashierKiosk(Group grpCashierKiosk) {
		this.grpCashierKiosk = grpCashierKiosk;
	}

	/**
	 * @return the grpCurrentScnDtl
	 */
	public Group getGrpCurrentScnDtl() {
		return grpCurrentScnDtl;
	}

	/**
	 * @param grpCurrentScnDtl
	 *            the grpCurrentScnDtl to set
	 */
	public void setGrpCurrentScnDtl(Group grpCurrentScnDtl) {
		this.grpCurrentScnDtl = grpCurrentScnDtl;
	}

	/**
	 * @return the btnCancel
	 */
	public SDSImageLabel getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @param btnCancel
	 *            the btnCancel to set
	 */
	public void setBtnCancel(SDSImageLabel btnCancel) {
		this.btnCancel = btnCancel;
	}

		/**
	 * @return the lblCashier
	 */
	public SDSTSLabel getLblCashier() {
		return lblCashier;
	}

	/**
	 * @param lblCashier
	 *            the lblCashier to set
	 */
	public void setLblCashier(CbctlLabel lblCashier) {
		this.lblCashier = lblCashier;
	}

	/**
	 * @return the toggleBtnComposite
	 */
	public Composite getToggleBtnComposite() {
		return toggleBtnComposite;
	}

	/**
	 * @param toggleBtnComposite
	 *            the toggleBtnComposite to set
	 */
	public void setToggleBtnComposite(Composite toggleBtnComposite) {
		this.toggleBtnComposite = toggleBtnComposite;
	}

	/**
	 * @return the btnBatchSummary
	 */
	public SDSImageLabel getBtnBatchSummary() {
		return btnBatchSummary;
	}

	/**
	 * @param btnBatchSummary
	 *            the btnBatchSummary to set
	 */
	public void setBtnBatchSummary(SDSImageLabel btnBatchSummary) {
		this.btnBatchSummary = btnBatchSummary;
	}

	/**
	 * @return the lblBatchNo
	 */
	public SDSTSLabel getLblBatchNo() {
		return lblBatchNo;
	}

	/**
	 * @param lblBatchNo
	 *            the lblBatchNo to set
	 */
	public void setLblBatchNo(CbctlLabel lblBatchNo) {
		this.lblBatchNo = lblBatchNo;
	}

	/**
	 * @return the lblBatchValue
	 */
	public SDSTSLabel getLblBatchValue() {
		return lblBatchValue;
	}

	/**
	 * @param lblBatchValue
	 *            the lblBatchValue to set
	 */
	public void setLblBatchValue(CbctlLabel lblBatchValue) {
		this.lblBatchValue = lblBatchValue;
	}

	private void createImages() {
		buttonBG   = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}

	public SDSImageLabel getBtnBrowse() {
		return btnBrowse;
	}

	public void setBtnBrowse(SDSImageLabel btnBrowse) {
		this.btnBrowse = btnBrowse;
	}

	public SDSImageLabel getBtnXMLSubmit() {
		return btnXMLSubmit;
	}

	public void setBtnXMLSubmit(SDSImageLabel btnXMLSubmit) {
		this.btnXMLSubmit = btnXMLSubmit;
	}

	public SDSTSLabelText getLblTxtXmlFile() {
		return lblTxtXmlFile;
	}

	public void setLblTxtXmlFile(SDSTSLabelText lblTxtXmlFile) {
		this.lblTxtXmlFile = lblTxtXmlFile;
	}
}
