/*****************************************************************************
 * $Id: ReportsComposite.java,v 1.42, 2010-11-19 11:43:57Z, Verma, Nitin Kumar$
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
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSCombo;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;
import com.ballydev.sds.voucherui.util.ControlsUtil;

/**
 * @author Nithya kalyani R
 * @version $Revision: 43$ 
 */
public class ReportsComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants{

	/**
	 * Instance of the reports label
	 */
	private CbctlLabel lblReports = null;

	/**
	 * Instance of the reports list combo 
	 */
	private SDSTSCombo<ComboLabelValuePair> reportsList ;

	/**
	 * Instance of the main composite
	 */
	private Composite mainComposite = null;

	/**
	 * Instance of the main composite
	 */
	private Composite detailsComposite = null;

	/**
	 * Instance of the main composite
	 */
	private Composite reportTypeComposite = null;

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
	 * Instance of the Submit button
	 */
	private SDSImageLabel btnSubmit = null;

	/**
	 * Instance of the reports display text area
	 */
	//private SDSTSText txtReportsDisplay = null;

	/**
	 * Instance of the start time label
	 */
	private CbctlMandatoryLabel lblCashier = null;

	/**
	 * Instance of the employee or asset text
	 */
	private SDSTSText txtEmployeeId = null;

	/**
	 * Instance of the emp all select button
	 */
	private SDSTSCheckBox tChkEmpAll = null;

	/**
	 * Instance of the label that shows to select all
	 */
	private CbctlLabel lblSelectAll= null;

	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;
	
	/**
	 * Instance of the detail touch screen radio image
	 */	
	private TSButtonLabel detailRadioImage = null;
	
	/**
	 * Instance of the summary touch screen radio image
	 */	
	private TSButtonLabel summaryRadioImage = null;

	/**
	 * Instance of the cashier kiosk group
	 */
	private Group grpDetailorSumm;

	/**
	 * Boolean to track whether the cashier radio button is selected
	 */
	private boolean isDetail = true;

	/**
	 * Instance of the toggle btn composite
	 */
	private Composite toggleBtnComposite =null;

	/**
	 * Instance of the cashier label
	 */
	private CbctlLabel lblDetail = null;

	/**
	 * Instance of the kiosk label
	 */
	private CbctlLabel lblSumm = null;

	/**
	 * Instance of the redeemed label
	 */
	private CbctlLabel lblNcsh = null;

	/**
	 *Instance of the created label 
	 */
	private CbctlLabel lblTicket = null;

	/**
	 *Instance of the voided label 
	 */
	private CbctlLabel lblCsh = null;

	/**
	 * Instance of the Non Cash Check box button
	 */
	private SDSTSCheckBox tChkNcsh = null;
	
	/**
	 * Instance of the Voucher Check box button
	 */
	private SDSTSCheckBox tChkVoucher = null;
	
	/**
	 * Instance of the Cash Check box button
	 */
	private SDSTSCheckBox tChkCsh = null;
	
	private Composite compInnerReportSetting;
	
	private Image buttonUnChk;

	private Image buttonBG;
	
	/**
	 * Constructor of the class
	 */
	public ReportsComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		this.setBounds(0,0, 1000,450);
		this.setBackground(SDSControlFactory.getTSBodyColor());
//		if( Util.isSmallerResolution() ) {
//			setSize(parent.getSize());
//		}
	}

	private void initialize(){
		
		createImages();
		
		GridLayout grlPage 	= ControlsUtil.createGridLayout(1, 5, 5, 1, 1);
		GridData gdPage 	= ControlsUtil.createGridData( GridData.FILL, GridData.FILL, true, true, 0);
		
		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(LBL_REPORTS));
		createMainComposite();	
		submitComposite();
		setDefaultProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		layout();
	}

	private void submitComposite() {
		
		GridData gdImgLblSubmit = new GridData();
		gdImgLblSubmit.grabExcessHorizontalSpace = true;
		gdImgLblSubmit.grabExcessVerticalSpace = true;
		gdImgLblSubmit.verticalAlignment = GridData.BEGINNING;
		gdImgLblSubmit.horizontalAlignment = GridData.CENTER;
		gdImgLblSubmit.verticalIndent = 1;

		GridLayout grlImgLblSubmit = new GridLayout();
		grlImgLblSubmit.numColumns = 1;
		grlImgLblSubmit.verticalSpacing = 1;
		grlImgLblSubmit.horizontalSpacing = 20;

		Composite imgLblComposite = new Composite(this, SWT.NONE);
		imgLblComposite.setLayout(grlImgLblSubmit);
		imgLblComposite.setLayoutData(gdImgLblSubmit);
		
		btnSubmit = new SDSImageLabel(imgLblComposite, SWT.NONE,buttonBG, LabelLoader.getLabelValue(LabelLoader.getLabelValue(LBL_SUBMIT_BARCODE)),REPORTS_CTRL_BTN_SUBMIT);

	}
	/**
	 * This method toggles the selection between the
	 * cashier and kiosk button
	 * @param isCashier
	 */
	public void setBandToggle(boolean isDetail) {
		this.isDetail = isDetail;
//		if( isDetail ){
//			detailRadioImage.setSelected(true);
//			summaryRadioImage.setSelected(false);
//		}else{
//			summaryRadioImage.setSelected(true);
//			detailRadioImage.setSelected(false);
//		}
	}

	/**
	 * This method creates the main composite
	 */
	public void createMainComposite()	{
		GridData gdMainComp = new GridData();
		if( Util.isSmallerResolution()) {
			gdMainComp.widthHint = 750;
		} else if( Util.is1024X786Resolution()) {
			gdMainComp.widthHint = 735;
		}else {
			gdMainComp.widthHint = 935;
		}
		gdMainComp.grabExcessHorizontalSpace = false;
		gdMainComp.verticalAlignment = GridData.CENTER;
		gdMainComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlMainComp = new GridLayout();
		grlMainComp.numColumns = 4;
		grlMainComp.verticalSpacing = 5;
		grlMainComp.horizontalSpacing = 5;

		mainComposite = new Composite(this,SWT.NONE);
//		mainComposite.setBackground(SDSControlFactory.getTSMiddleHeaderColor());
		mainComposite.setLayout(grlMainComp);
		mainComposite.setLayoutData(gdMainComp);	

		GridData gdTouchScreenButton = new GridData();
		gdTouchScreenButton.grabExcessVerticalSpace = true;
		gdTouchScreenButton.heightHint = 50;
		gdTouchScreenButton.widthHint = 50;
		if(Util.isSmallerResolution()) {
			gdTouchScreenButton.heightHint = 49;
			gdTouchScreenButton.widthHint = 49;
		}
		gdTouchScreenButton.horizontalAlignment = GridData.CENTER;
		gdTouchScreenButton.verticalAlignment = GridData.END;
		gdTouchScreenButton.grabExcessHorizontalSpace = true;

		lblSelectAll = new CbctlLabel(mainComposite, SWT.NONE | SWT.WRAP | SWT.CENTER, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_SELECT_ALL) + LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));

		tChkEmpAll = new SDSTSCheckBox(mainComposite, SWT.NONE, "", IVoucherConstants.REPORTS_CTRL_TGL_BTN_EMPLOYEE_ASSET, false);
		tChkEmpAll.setBackground(SDSControlFactory.getTSBodyColor());
		tChkEmpAll.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		tChkEmpAll.setLayoutData(gdTouchScreenButton);

		lblCashier = new CbctlMandatoryLabel(mainComposite,SWT.NONE, LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
		lblCashier.getLabel().setBackground(SDSControlFactory.getTSBodyColor());
		lblCashier.setBackground(SDSControlFactory.getTSBodyColor());
		
		txtEmployeeId =  new SDSTSText(mainComposite,IVoucherConstants.RECON_CTRL_LBL_EMPLOYE_ID, IVoucherConstants.REPORTS_FRM_LBL_EMPLOYE_ID);
		//txtEmployeeId.setFocus();
		txtEmployeeId.setTextLimit(5);
		txtEmployeeId.setTextLimitChkEnabled(true);

		lblStartTime = new CbctlLabel(mainComposite,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_START_TIME));
		createDateFromTime();

		lblEndTime = new CbctlLabel(mainComposite,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_END_TIME));
		createDateToTime();		

		createReportTypeGroup();

		createDetailsComposite();

	}

	private void createReportTypeGroup() {
		
		GridLayout grlMainComp 	= ControlsUtil.createGridLayout(2, 5, 5, 0, 0);

		GridData gdMainComp = new GridData();
		gdMainComp.grabExcessHorizontalSpace = false;
		gdMainComp.verticalAlignment = GridData.CENTER;
		gdMainComp.horizontalAlignment = GridData.CENTER;

		reportTypeComposite = new Composite(this,SWT.NONE);
		reportTypeComposite.setLayout(grlMainComp);
		reportTypeComposite.setLayoutData(gdMainComp);
		reportTypeComposite.setBackground(SDSControlFactory.getTSBodyColor());

		createDetailTypeComposite();
		createReportSettingGroup();
	}

	private void createReportSettingGroup(){

		Composite container = new Composite(reportTypeComposite, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());

		GridData gdDetailsGrp = new GridData();
		gdDetailsGrp.grabExcessHorizontalSpace = true;
		gdDetailsGrp.grabExcessVerticalSpace = true;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;
		
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 550;
		} else if(Util.is1024X786Resolution()) {
			gdDetailsGrp.widthHint = 520;
		} else {
			gdDetailsGrp.widthHint = 580;
		}
		
		gdDetailsGrp.heightHint = 150;

		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		if(Util.isSmallerResolution()) {
			grlDetailsGrp.verticalSpacing = 15;
		} else {
			grlDetailsGrp.verticalSpacing = 17;
		}
		
		grlDetailsGrp.horizontalSpacing = 0;
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;

		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);

		Composite header = new Composite(container, SWT.NONE);

		GridData headerData = new GridData();
		if(Util.isSmallerResolution()) {
			headerData.widthHint = 617;
		} else {
			headerData.widthHint = 580;
		}
		headerData.heightHint = 30;

		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());

		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());

		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TKT_TYPE_SETTINGS));

		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.grabExcessVerticalSpace = false;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.BEGINNING;
//		gridDataPrefHeader.verticalIndent = 1;
		lblMessage.setLayoutData(gridDataPrefHeader);

		if(Util.isSmallerResolution()) {
			lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		}else {
			lblMessage.setFont(SDSControlFactory.getDefaultBoldFont());
		}

		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		GridData gdTouchScreenButton = new GridData();
		gdTouchScreenButton.grabExcessHorizontalSpace = true;
		gdTouchScreenButton.grabExcessVerticalSpace = true;
		gdTouchScreenButton.horizontalAlignment = GridData.BEGINNING;
		gdTouchScreenButton.verticalAlignment = GridData.BEGINNING;
		gdTouchScreenButton.horizontalIndent = 30;
		gdTouchScreenButton.heightHint = 50;
		gdTouchScreenButton.widthHint = 50;
		if(Util.isSmallerResolution()) {
			gdTouchScreenButton.heightHint = 45;
			gdTouchScreenButton.widthHint = 60;
		}
		if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null ) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 4 && Util.isSmallerResolution() ) {
				gdTouchScreenButton.horizontalIndent = 10;
			}
		}
		
		GridData gdTouchScreenButton1 = new GridData();
		gdTouchScreenButton1.grabExcessHorizontalSpace = true;
		gdTouchScreenButton1.grabExcessVerticalSpace = true;
		gdTouchScreenButton1.horizontalAlignment = GridData.BEGINNING;
		gdTouchScreenButton1.verticalAlignment = GridData.BEGINNING;
		gdTouchScreenButton1.horizontalIndent = 5;
		gdTouchScreenButton1.heightHint = 50;
		gdTouchScreenButton1.widthHint = 50;
		if(Util.isSmallerResolution()) {
			gdTouchScreenButton1.heightHint = 45;
			gdTouchScreenButton1.widthHint = 60;
		}
		GridData gdTouchScreenButton2 = new GridData();
		gdTouchScreenButton2.grabExcessHorizontalSpace = true;
		gdTouchScreenButton2.grabExcessVerticalSpace = true;
		gdTouchScreenButton2.horizontalAlignment = GridData.BEGINNING;
		gdTouchScreenButton2.verticalAlignment = GridData.BEGINNING;
		gdTouchScreenButton2.horizontalIndent = 20;
		gdTouchScreenButton2.heightHint = 50;
		gdTouchScreenButton2.widthHint = 50;
		if(Util.isSmallerResolution()) {
			gdTouchScreenButton2.heightHint = 45;
			gdTouchScreenButton2.widthHint = 60;
		}
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.heightHint = 30;
		
		GridLayout grlComp 	= ControlsUtil.createGridLayout(3, 20, 10, 0, 0);
		GridData grdComp 	= ControlsUtil.createGridData( 480, 120, 0, GridData.CENTER, GridData.CENTER, true, true, 0);
		compInnerReportSetting = new Composite(container, SWT.NONE);
		compInnerReportSetting.setLayout(grlComp);
		compInnerReportSetting.setLayoutData(grdComp);

		tChkNcsh = new SDSTSCheckBox(compInnerReportSetting, SWT.NONE, buttonUnChk, "", IVoucherConstants.REPORT_FRM_BTN_NCSH, false);
		tChkNcsh.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		tChkNcsh.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		tChkNcsh.setLayoutData(gdTouchScreenButton);
		
		tChkVoucher = new SDSTSCheckBox(compInnerReportSetting, SWT.NONE, buttonUnChk, "", IVoucherConstants.REPORT_FRM_BTN_TICKET, false);
		tChkVoucher.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		tChkVoucher.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		tChkVoucher.setLayoutData(gdTouchScreenButton1);
		
		tChkCsh = new SDSTSCheckBox(compInnerReportSetting, SWT.NONE, buttonUnChk, "", IVoucherConstants.REPORT_FRM_BTN_CSH, false);
		tChkCsh.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		tChkCsh.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
		tChkCsh.setLayoutData(gdTouchScreenButton2);
		
		GridData gdLbl = new GridData();
		gdLbl.grabExcessHorizontalSpace = true;
		gdLbl.grabExcessVerticalSpace = true;
		gdLbl.horizontalAlignment = GridData.BEGINNING;
		gdLbl.verticalAlignment = GridData.BEGINNING;
		gdLbl.heightHint = -1;
		gdLbl.widthHint = -1;

		GridData gdLbl1 = new GridData();
		gdLbl1.grabExcessHorizontalSpace = true;
		gdLbl1.grabExcessVerticalSpace = true;
		gdLbl1.horizontalAlignment = GridData.BEGINNING;
		gdLbl1.verticalAlignment = GridData.BEGINNING;
		gdLbl1.heightHint = -1;
		gdLbl1.widthHint = -1;
		
		GridData gdLbl2 = new GridData();
		gdLbl2.grabExcessHorizontalSpace = true;
		gdLbl2.grabExcessVerticalSpace = true;
		gdLbl2.horizontalAlignment = GridData.BEGINNING;
		gdLbl2.verticalAlignment = GridData.BEGINNING;
		gdLbl2.heightHint = -1;
		gdLbl2.widthHint = -1;
		
		if(Util.isSmallerResolution()) {
			gdLbl1.widthHint = 124;
			gdLbl2.widthHint = 124;
		}
		if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null ) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 5 && Util.isSmallerResolution() ) {
				gdLbl.widthHint = 100;
			}
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 4 && Util.isSmallerResolution() ) {
				gdLbl.widthHint = 80;
				gdLbl1.widthHint = 80;
				gdLbl2.widthHint = 164;
			}
		}
		lblNcsh = new CbctlLabel(compInnerReportSetting, SWT.NONE | SWT.WRAP, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BTN_NCSH));
		lblNcsh.setLayoutData(gdLbl1);
		lblCsh = new CbctlLabel(compInnerReportSetting, SWT.NONE | SWT.WRAP, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BTN_CSH));
		lblCsh.setLayoutData(gdLbl);
		lblTicket = new CbctlLabel(compInnerReportSetting, SWT.NONE | SWT.WRAP, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BTN_CSH_PROMO));
		lblTicket.setLayoutData(gdLbl2);
		
	}
	
	private void createDetailTypeComposite() {
		
		Composite container = new Composite(reportTypeComposite, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());

		GridData gdDetailsGrp = new GridData();
		gdDetailsGrp.grabExcessHorizontalSpace = true;
		gdDetailsGrp.grabExcessVerticalSpace = true;
		gdDetailsGrp.verticalAlignment = GridData.CENTER;
		gdDetailsGrp.horizontalAlignment = GridData.CENTER;
		
		if( Util.isSmallerResolution() ) {
			gdDetailsGrp.widthHint = 280;
		} else if( Util.is1024X786Resolution() ) {
			gdDetailsGrp.widthHint = 310;
		} else {
			gdDetailsGrp.widthHint = 350;
		}
		
		gdDetailsGrp.heightHint = 150;

		GridLayout grlDetailsGrp = new GridLayout();
		grlDetailsGrp.numColumns = 1;
		grlDetailsGrp.verticalSpacing = 10;
		grlDetailsGrp.horizontalSpacing = 0;
		grlDetailsGrp.marginHeight = 0;
		grlDetailsGrp.marginWidth = 0;

		container.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		container.setLayout(grlDetailsGrp);
		container.setLayoutData(gdDetailsGrp);

		Composite header = new Composite(container, SWT.NONE);

		GridData headerData = new GridData();
		if( Util.isSmallerResolution() ) {
			headerData.widthHint = 315;
		} else {
			headerData.widthHint = 350;
		}
		headerData.heightHint = 30;

		header.setLayoutData(headerData);
		header.setLayout(new GridLayout());

		header.setBackground(SDSControlFactory.getTSMiddleHeaderColor());

		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TKT_TYPE_SETTINGS));

		GridData gridDataPrefHeader = new GridData();
		gridDataPrefHeader.grabExcessHorizontalSpace = true;
		gridDataPrefHeader.grabExcessVerticalSpace = false;
		gridDataPrefHeader.horizontalAlignment = SWT.BEGINNING;
		gridDataPrefHeader.verticalAlignment = SWT.BEGINNING;
//		gridDataPrefHeader.verticalIndent = 1;
		lblMessage.setLayoutData(gridDataPrefHeader);

		if(Util.isSmallerResolution()) {
			lblMessage.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		}else {
			lblMessage.setFont(SDSControlFactory.getDefaultBoldFont());
		}

		lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		toggleBtnComposite =  new Composite(container, SWT.NONE);

		GridLayout grlBtnComposite = new GridLayout();
		grlBtnComposite.numColumns = 2;
		grlBtnComposite.verticalSpacing = 13;
		grlBtnComposite.horizontalSpacing = 50;

		toggleBtnComposite.setLayout(grlBtnComposite);

		GridData toggleCompositeGridData = new GridData();
		toggleCompositeGridData.horizontalAlignment = SWT.CENTER;

		toggleBtnComposite.setLayoutData(toggleCompositeGridData);

		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.BEGINNING;
		gdRadio.horizontalIndent = 10;
		if( Util.is1024X786Resolution() ) {
			gdRadio.horizontalIndent = 0;
		}
		
		radioButtonControl = new RadioButtonControl("Search by any one of the following");
		
		detailRadioImage = new TSButtonLabel(toggleBtnComposite, SWT.NONE, IVoucherConstants.REP_CTRL_TGL_BTN_BAND);
		detailRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
		detailRadioImage.setLayoutData(gdRadio);
		radioButtonControl.add(detailRadioImage);
	
		summaryRadioImage = new TSButtonLabel(toggleBtnComposite, SWT.NONE, IVoucherConstants.REP_CTRL_TGL_BTN_SUMM);
		summaryRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		summaryRadioImage.setLayoutData(gdRadio);
		radioButtonControl.add(summaryRadioImage);

		GridData gdLbl = new GridData();
		gdLbl.widthHint = 65;
	
		if( Util.isSmallerResolution() ) {
			gdLbl.verticalIndent = 3;
		} else {
			gdLbl.verticalIndent = 10;
		}
		
		if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null ) {
			if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 5 && Util.isSmallerResolution() ) {
				gdLbl.horizontalIndent = 15;
			}
		}
		lblDetail = new CbctlLabel(toggleBtnComposite,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_SET_DETAIL));
		lblDetail.setLayoutData(gdLbl);
		
		lblSumm = new CbctlLabel(toggleBtnComposite,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_SET_SUMM));
		lblSumm.setLayoutData(gdLbl);
	}

	private void createDetailsComposite(){

		GridLayout grlMainComp 	= ControlsUtil.createGridLayout(2, 5, 5, 5, 5);
		GridData gdMainComp 	= ControlsUtil.createGridData( GridData.CENTER, GridData.BEGINNING, false, true, 0);

		detailsComposite = new Composite(this, SWT.NONE);
		detailsComposite.setLayout(grlMainComp);
		detailsComposite.setLayoutData(gdMainComp);

		lblReports = new CbctlLabel(detailsComposite, SWT.NONE, LabelLoader.getLabelValue(LBL_REPORTS));

		if( Util.isSmallerResolution() ) {
			GridData downBtnGD = new GridData();
			downBtnGD.horizontalAlignment = GridData.CENTER;
			downBtnGD.verticalAlignment = GridData.CENTER;
			downBtnGD.widthHint =  33;
			downBtnGD.heightHint = 33;
			reportsList = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, REPORTS_FRM_CMB_SELECTED_REPORT, "label", "value", REPORTS_FRM_CMB_REPORT_LIST);
			reportsList.getLeft().setLayoutData(downBtnGD);
			reportsList.getRight().setLayoutData(downBtnGD);
		} else {
			reportsList = new SDSTSCombo<ComboLabelValuePair>(detailsComposite, REPORTS_FRM_CMB_SELECTED_REPORT, "label", "value", REPORTS_FRM_CMB_REPORT_LIST);
		}
	}

	/**
	 * This method initializes dateStartTime	
	 */
	private void createDateFromTime() {
		GridData gdStartTime = new GridData();
		gdStartTime.heightHint = 25;
		gdStartTime.grabExcessHorizontalSpace = false;
		gdStartTime.horizontalAlignment = GridData.CENTER;
		gdStartTime.verticalAlignment = GridData.CENTER;
		gdStartTime.widthHint = 180;		
		startTime = new SDSDatePicker(mainComposite,ACW.FOOTER|ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.TIME_CUSTOM| ACW.DIGITAL_CLOCK,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_START_TIME),IVoucherConstants.RECON_CTRL_LBL_START_TIME);
		startTime.setLayoutData(gdStartTime);
	}

	/**
	 * This method initializes dateEndTime	
	 */
	private void createDateToTime() {
		GridData gdEndTime = new GridData();
		gdEndTime.heightHint = 25;
		gdEndTime.horizontalAlignment = GridData.CENTER;
		gdEndTime.verticalAlignment = GridData.CENTER;
		gdEndTime.grabExcessHorizontalSpace = false;
		gdEndTime.widthHint = 180;
		endTime = new SDSDatePicker(mainComposite,ACW.FOOTER|ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM| ACW.TIME_CUSTOM|ACW.DIGITAL_CLOCK,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_END_TIME),IVoucherConstants.RECON_CTRL_LBL_END_TIME);
		endTime.setLayoutData(gdEndTime);
	}

	/**
	 * This method sets the default properties for all the 
	 * controls
	 */
	public void setDefaultProperties(){

		try	{			
			SDSControlFactory.setTouchScreenLabelProperties(lblReports);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(btnSubmit);
			SDSControlFactory.setTouchScreenLabelProperties(lblEndTime);
			SDSControlFactory.setTouchScreenLabelProperties(lblStartTime);
			SDSControlFactory.setTouchScreenTextProperties(txtEmployeeId);
			SDSControlFactory.setTouchScreenLabelProperties(lblCashier.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(reportsList.getText());
			SDSControlFactory.setTouchScreenLabelProperties(lblSelectAll);
//			SDSControlFactory.setTouchScreenLabelProperties(lblDetailOrSumm);
			SDSControlFactory.setTouchScreenLabelProperties(lblCsh);
			SDSControlFactory.setTouchScreenLabelProperties(lblDetail);
			SDSControlFactory.setTouchScreenLabelProperties(lblNcsh);
			SDSControlFactory.setTouchScreenLabelProperties(lblSumm);
			SDSControlFactory.setTouchScreenLabelProperties(lblTicket);
//			SDSControlFactory.setGroupHeadingProperties(grpDetailorSumm);

			int width  = 0;
			int height = 0;
			if( Util.isSmallerResolution() ) {
				width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 8;
				height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 14;
			
				GridData gdBtnPrint = (GridData)btnSubmit.getLayoutData();
				gdBtnPrint.widthHint  = width;	
				gdBtnPrint.heightHint = height;
				gdBtnPrint.horizontalAlignment = SWT.CENTER;
				btnSubmit.setLayoutData(gdBtnPrint);

			}
			
			GridData gdTxtEmpId = (GridData)txtEmployeeId.getLayoutData();
			gdTxtEmpId.widthHint =  170;
			if( Util.isSmallerResolution() ) {
				gdTxtEmpId.widthHint =  160;
				gdTxtEmpId.heightHint = 22;
			}
			txtEmployeeId.setLayoutData(gdTxtEmpId);

			GridData gdBtnPrint = (GridData)btnSubmit.getLayoutData();
			gdBtnPrint.horizontalAlignment = SWT.CENTER;
			btnSubmit.setLayoutData(gdBtnPrint);

			GridData gdComboTxt = (GridData)reportsList.getText().getLayoutData();
			gdComboTxt.widthHint = ControlConstants.EX_TEXT_WIDTH;
			if( Util.isSmallerResolution() ) {
				gdComboTxt.widthHint = ControlConstants.EX_TEXT_WIDTH + 50;
			}
			reportsList.getText().setLayoutData(gdComboTxt);
		} 
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return the reportsList
	 */
	public SDSTSCombo<ComboLabelValuePair> getReportsList() {
		return reportsList;
	}

	/**
	 * @param reportsList the reportsList to set
	 */
	public void setReportsList(SDSTSCombo<ComboLabelValuePair> reportsList) {
		this.reportsList = reportsList;
	}

	/**
	 * @return the mainComposite
	 */
	public Composite getMainComposite() {
		return mainComposite;
	}

	/**
	 * @param mainComposite the mainComposite to set
	 */
	public void setMainComposite(Composite mainComposite) {
		this.mainComposite = mainComposite;
	}


	/**
	 * @return the btnSubmit
	 */
	public SDSImageLabel getBtnSubmit() {
		return btnSubmit;
	}

	/**
	 * @param btnSubmit the btnSubmit to set
	 */
	public void setBtnSubmit(SDSImageLabel btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	/**
	 * @return the startTime
	 */
	public SDSDatePicker getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
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
	 * @param endTime the endTime to set
	 */
	public void setEndTime(SDSDatePicker endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the txtEmployeeOrAsset
	 */
	public SDSTSText getTxtEmployeeId() {
		return txtEmployeeId;
	}

	/**
	 * @param txtEmployeeOrAsset the txtEmployeeOrAsset to set
	 */
	public void setTxtEmployeeId(SDSTSText txtEmployeeOrAsset) {
		this.txtEmployeeId = txtEmployeeOrAsset;
	}

	/**
	 * @return the tBtnEmpAll
	 */
	public SDSTSCheckBox getTChkEmpAll() {
		return tChkEmpAll;
	}

	/**
	 * @param btnEmpAll the tBtnEmpAll to set
	 */
	public void setTChkEmpAll(SDSTSCheckBox btnEmpAll) {
		tChkEmpAll = btnEmpAll;
	}

	/**
	 * @return the lblCashier
	 */
	public CbctlMandatoryLabel getLblCashier() {
		return lblCashier;
	}

	/**
	 * @param lblCashier the lblCashier to set
	 */
	public void setLblCashier(CbctlMandatoryLabel lblCashier) {
		this.lblCashier = lblCashier;
	}

	/**
	 * @return the detailRadioImage
	 */
	public TSButtonLabel getDetailRadioImage() {
		return detailRadioImage;
	}

	/**
	 * @param detailRadioImage the detailRadioImage to set
	 */
	public void setDetailRadioImage(TSButtonLabel detailRadioImage) {
		this.detailRadioImage = detailRadioImage;
	}

	/**
	 * @return the summaryRadioImage
	 */
	public TSButtonLabel getSummaryRadioImage() {
		return summaryRadioImage;
	}

	/**
	 * @param summaryRadioImage the summaryRadioImage to set
	 */
	public void setSummaryRadioImage(TSButtonLabel summaryRadioImage) {
		this.summaryRadioImage = summaryRadioImage;
	}

	/**
	 * @return the grpDetailorSumm
	 */
	public Group getGrpDetailorSumm() {
		return grpDetailorSumm;
	}

	/**
	 * @param grpDetailorSumm the grpDetailorSumm to set
	 */
	public void setGrpDetailorSumm(Group grpDetailorSumm) {
		this.grpDetailorSumm = grpDetailorSumm;
	}

	/**
	 * @return the isDetail
	 */
	public boolean isDetail() {
		return isDetail;
	}

	/**
	 * @param isDetail the isDetail to set
	 */
	public void setDetail(boolean isDetail) {
		this.isDetail = isDetail;
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
	 * @return the tChkNcsh
	 */
	public SDSTSCheckBox getTChkNcsh() {
		return tChkNcsh;
	}

	/**
	 * @param chkNcsh the tChkNcsh to set
	 */
	public void setTChkNcsh(SDSTSCheckBox chkNcsh) {
		tChkNcsh = chkNcsh;
	}

	/**
	 * @return the tChkVoucher
	 */
	public SDSTSCheckBox getTChkVoucher() {
		return tChkVoucher;
	}

	/**
	 * @param chkVoucher the tChkVoucher to set
	 */
	public void setTChkVoucher(SDSTSCheckBox chkVoucher) {
		tChkVoucher = chkVoucher;
	}

	/**
	 * @return the tChkCsh
	 */
	public SDSTSCheckBox getTChkCsh() {
		return tChkCsh;
	}

	/**
	 * @param chkCsh the tChkCsh to set
	 */
	public void setTChkCsh(SDSTSCheckBox chkCsh) {
		tChkCsh = chkCsh;
	}

	//	/**
//	 * @return the tbtnNcsh
//	 */
//	public TouchScreenRadioButton getTbtnNcsh() {
//		return tbtnNcsh;
//	}
//
//	/**
//	 * @param tbtnNcsh the tbtnNcsh to set
//	 */
//	public void setTbtnNcsh(TouchScreenRadioButton tbtnNcsh) {
//		this.tbtnNcsh = tbtnNcsh;
//	}
//
//	/**
//	 * @return the tbtnVoucher
//	 */
//	public TouchScreenRadioButton getTbtnVoucher() {
//		return tbtnVoucher;
//	}
//
//	/**
//	 * @param tbtnVoucher the tbtnVoucher to set
//	 */
//	public void setTbtnVoucher(TouchScreenRadioButton tbtnVoucher) {
//		this.tbtnVoucher = tbtnVoucher;
//	}
//
//	/**
//	 * @return the tbtnCsh
//	 */
//	public TouchScreenRadioButton getTbtnCsh() {
//		return tbtnCsh;
//	}
//
//	/**
//	 * @param tbtnCsh the tbtnCsh to set
//	 */
//	public void setTbtnCsh(TouchScreenRadioButton tbtnCsh) {
//		this.tbtnCsh = tbtnCsh;
//	}
	
	private void createImages() {
//		buttonBG_E = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_BG_E));
		buttonUnChk= new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED));
		buttonBG  = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}

}
