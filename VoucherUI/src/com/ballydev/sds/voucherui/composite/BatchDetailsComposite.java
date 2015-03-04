/*****************************************************************************
 * $Id: BatchDetailsComposite.java,v 1.43, 2011-01-05 08:00:14Z, Bhandari, Vineet$
 * $Date: 1/5/2011 2:00:14 AM$
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.TableColumnDescriptor;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.constant.ITSConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSTableViewer;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucher.dto.ReclBatchDetailsInfoDTO;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.AmountComparator;
import com.ballydev.sds.voucherui.util.BarcodeComparator;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.LocationNoComparator;
import com.ballydev.sds.voucherui.util.TimestampComparator;

/**
 * This class creates the batch details composite(Inner most)
 * @author Nithya kalyani R
 * @version $Revision: 44$ 
 */
public class BatchDetailsComposite extends Composite {

	/**
	 * mainGroup Instance
	 */
	private Composite mainComposite = null;

	/**
	 * Instance of the total page count label
	 */
	private SDSTSLabel lblTotalPageCount = null;

	/**
	 * Display Table Viewer instance
	 */
	private SDSTSTableViewer dispBatchDetailsTableViewer = null;  

	/**
	 * Display Table Composite instance
	 */
	private Composite dispTableComposite = null;

	/**
	 * Instance of Display First Page Records Image Label.
	 */
	private SDSImageLabel iLblFirstPageRecords = null;

	/**
	 * Instance of display Previous Records Image Label.
	 */
	private SDSImageLabel iLblPreviousRecords = null;

	/**
	 * Instance of display Next Records Image Label.
	 */
	private SDSImageLabel iLblNextRecords = null;

	/**
	 * Instance of display Last Page Records Image Label.
	 */
	private SDSImageLabel iLblLastPageRecords = null;

	/**
	 * Table Index instance
	 */
	private int tabindex;

	/**
	 * Instance of Reload Image Label.
	 */
	private SDSImageLabel iLblReloadBatchDtls = null;

	/**
	 * Instance of Get Questionable vouchers Image Label.
	 */
	private SDSImageLabel iLblGetQuestionables = null;

	/**
	 * Instance of Print Batch Details Image Label.
	 */
	private SDSImageLabel iLblPrintBatchDetails = null;

	/**
	 * Instance of Delete Batch ButtonImage Label.
	 */
	private SDSImageLabel iLblDeleteBatch = null;

	/**
	 * Instance of Change Employee Image Label.
	 */
	private SDSImageLabel iLblChangeEmployee = null;

	/**
	 * Instance of Delete Ticket Image Label.
	 */
	private SDSImageLabel iLblDeleteTkt = null;

	/**
	 * Instance of Add Ticket Image Label.
	 */
	private SDSImageLabel iLblAddTkt = null;

	/**
	 * Instance of Edit Ticket Image Label.
	 */
	private SDSImageLabel iLblModifyTkt = null;

	/**
	 * Instance of Cancel Image Label.
	 */
	private SDSImageLabel iLblCancel = null;

	/**
	 * Instance of the batch number label
	 */
	private SDSTSLabel lblBatchNbr = null;

	/**
	 * Instance of the batch number text
	 */
	private SDSTSText txtBatchNbr = null;

	/**
	 * Instance of the employee label
	 */
	private SDSTSLabel lblEmployee = null;

	/**
	 * Instance of the employee text
	 */
	private SDSTSText  txtEmployee = null;

	/**
	 * Instance of the voucher count label
	 */
	private SDSTSLabel lblVoucherCount = null;

	/**
	 * Instance of the voucher count text
	 */
	private SDSTSText  txtVoucherCount = null;

	/**
	 * Instance of the voucher amount label
	 */
	private SDSTSLabel lblVoucherAmt = null;

	/**
	 * Instance of the voucher amt text
	 */
	private SDSTSText  txtVoucherAmt = null;

	/**
	 * Instance of the coupon count label
	 */
	private SDSTSLabel lblCouponCount = null;

	/**
	 * Instance of the coupon count text
	 */
	private SDSTSText  txtCouponCount = null;

	/**
	 * Instance of the coupon amount label
	 */
	private SDSTSLabel lblCouponAmt = null;

	/**
	 * Instance of the coupon amount text
	 */
	private SDSTSText  txtCouponAmt= null;

	private TimestampComparator timeStampComparator = null;

	private Image buttonImage;

	private Image buttonDisableImage;
	
	/**
	 * Constructor of the class
	 */
	public BatchDetailsComposite(Composite parent, int style) {
		super(parent, style);
		this.setBackground(SDSControlFactory.getTSBodyColor());
		createImages();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		createMainGroup();
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 5;
		grlPage.marginWidth = 0;
		grlPage.marginHeight = 0;
		grlPage.makeColumnsEqualWidth = true;
		grlPage.horizontalSpacing = 0;

		GridData gdPage = new GridData();
		gdPage.grabExcessVerticalSpace = true;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.verticalAlignment = GridData.FILL;		

		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		this.setSize(new Point(800, 637));

	}

	private void createMainGroup() {
		GridLayout grlMainComposite = new GridLayout();
		grlMainComposite.numColumns = 1;
		grlMainComposite.verticalSpacing = 10;
		grlMainComposite.makeColumnsEqualWidth = false;
		grlMainComposite.horizontalSpacing = 5;

		GridData gdMainComposite = new GridData();
		gdMainComposite.grabExcessHorizontalSpace = true;
		gdMainComposite.grabExcessVerticalSpace = true;
		gdMainComposite.horizontalAlignment = GridData.CENTER;
		gdMainComposite.verticalAlignment = GridData.FILL;
		gdMainComposite.heightHint = -1;
		gdMainComposite.verticalSpan = 2;
		gdMainComposite.widthHint = -1;

		pageHeading();

		mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setBackground(SDSControlFactory.getTSBodyColor());
//		mainComposite.setBackground(new Color(Display.getCurrent(), ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_R, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_G, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_B));
		mainComposite.setLayout(grlMainComposite);
		mainComposite.setLayoutData(gdMainComposite);

		GridLayout grlHeaderRow = new GridLayout();
		grlHeaderRow.numColumns = 6;
		grlHeaderRow.verticalSpacing = 5;
		grlHeaderRow.marginWidth = 4;
		grlHeaderRow.marginHeight = 0;
		grlHeaderRow.makeColumnsEqualWidth = false;
		grlHeaderRow.horizontalSpacing =4;

		GridData gdHeaderRow = new GridData();
		gdHeaderRow.grabExcessHorizontalSpace = true;
		gdHeaderRow.heightHint = -1;
		gdHeaderRow.horizontalAlignment = GridData.CENTER;
		gdHeaderRow.verticalAlignment = GridData.CENTER;
		gdHeaderRow.grabExcessVerticalSpace = false;

		Composite batchHdrRow2Composite = new Composite(mainComposite, SWT.CENTER);
		batchHdrRow2Composite.setBackground(SDSControlFactory.getTSBodyColor());
//		batchHdrRow2Composite.setBackground(new Color(Display.getCurrent(), ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_R, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_G, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_B));
		batchHdrRow2Composite.setLayout(grlHeaderRow);
		batchHdrRow2Composite.setLayoutData(gdHeaderRow);

		lblBatchNbr = new SDSTSLabel(batchHdrRow2Composite, SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_BATCH_NO));			

		txtBatchNbr = new SDSTSText(batchHdrRow2Composite, IVoucherConstants.RECON_BATCH_SUMM_CTRL_TXT_BATCH_NBR, IVoucherConstants.RECON_BATCH_SUMM_FRM_TXT_BATCH_NBR);
		txtBatchNbr.setEnabled(false);

		lblVoucherCount = new SDSTSLabel(batchHdrRow2Composite, SWT.NONE);
		lblVoucherCount.setText(AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOUCHER_COUNT));

		txtVoucherCount = new SDSTSText(batchHdrRow2Composite,SWT.RIGHT,IVoucherConstants.RECON_BATCH_SUMM_CTRL_TXT_VOU_CNT, IVoucherConstants.RECON_BATCH_SUMM_FRM_TXT_VOU_CNT);
		txtVoucherCount.setEnabled(false);

		lblVoucherAmt = new SDSTSLabel(batchHdrRow2Composite, SWT.NONE);
		lblVoucherAmt.setText(AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOUCHER_$)+ " ("+CurrencyUtil.getCurrencySymbol()+")");

		txtVoucherAmt = new SDSTSText(batchHdrRow2Composite,SWT.RIGHT,IVoucherConstants.RECON_BATCH_SUMM_CTRL_TXT_VOU_AMT, IVoucherConstants.RECON_BATCH_SUMM_FRM_TXT_VOU_AMT);
		txtVoucherAmt.setEnabled(false);
		txtVoucherAmt.setAmountField(true);

		lblEmployee = new SDSTSLabel(batchHdrRow2Composite, SWT.NONE);
		lblEmployee.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_EMPLOYEE_ID));

		txtEmployee = new SDSTSText(batchHdrRow2Composite,IVoucherConstants.RECON_BATCH_SUMM_CTRL_TXT_EMP , IVoucherConstants.RECON_BATCH_SUMM_FRM_TXT_EMP);
		txtEmployee.setEnabled(false);

		lblCouponCount = new SDSTSLabel(batchHdrRow2Composite, SWT.NONE);
		lblCouponCount.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_COUPAN_COUNT));

		txtCouponCount = new SDSTSText(batchHdrRow2Composite,SWT.RIGHT, IVoucherConstants.RECON_BATCH_SUMM_CTRL_TXT_COU_CNT, IVoucherConstants.RECON_BATCH_SUMM_FRM_TXT_COU_CNT);
		txtCouponCount.setEnabled(false);

		lblCouponAmt = new SDSTSLabel(batchHdrRow2Composite, SWT.NONE);
		lblCouponAmt.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_COUPAN_$)+ " ("+CurrencyUtil.getCurrencySymbol()+")");

		txtCouponAmt = new SDSTSText(batchHdrRow2Composite,SWT.RIGHT, IVoucherConstants.RECON_BATCH_SUMM_CTRL_TXT_COU_AMT, IVoucherConstants.RECON_BATCH_SUMM_FRM_TXT_COU_AMT);
		txtCouponAmt.setEnabled(false);
		txtCouponAmt.setAmountField(true);

		createDispTableComposite();

		GridLayout grlPageNavComposite = new GridLayout();
		grlPageNavComposite.numColumns = 5;
		grlPageNavComposite.verticalSpacing = 5;
		grlPageNavComposite.marginHeight = 5;
		grlPageNavComposite.marginWidth = 45;
		grlPageNavComposite.makeColumnsEqualWidth = true;
		grlPageNavComposite.horizontalSpacing = 25;
		
		if( Util.isSmallerResolution() ) {
			grlPageNavComposite.verticalSpacing = 0;
			grlPageNavComposite.marginWidth = 0;
			grlPageNavComposite.marginHeight = 0;
			grlPageNavComposite.makeColumnsEqualWidth = false;
			grlPageNavComposite.horizontalSpacing = 5;
		}

		if( Util.is1024X786Resolution()) {
			grlPageNavComposite.verticalSpacing = 0;
			grlPageNavComposite.marginWidth = 40;
			grlPageNavComposite.marginHeight = 0;
			grlPageNavComposite.makeColumnsEqualWidth = false;
			grlPageNavComposite.horizontalSpacing = 25;
		}
		GridData gdPageNavComposite = new GridData();
		gdPageNavComposite.verticalAlignment = GridData.FILL;
		gdPageNavComposite.horizontalAlignment = GridData.FILL;
		gdPageNavComposite.grabExcessVerticalSpace = false;
		gdPageNavComposite.horizontalIndent = 600;
		
		if( Util.isSmallerResolution() ) {
			gdPageNavComposite.horizontalIndent = 375;
		}

		if( Util.is1024X786Resolution() ) {
			gdPageNavComposite.horizontalIndent = 465;
		}

		Composite pageNavComposite = new Composite(mainComposite, SWT.CENTER);
		pageNavComposite.setBackground(SDSControlFactory.getTSBodyColor());
		pageNavComposite.setLayout(grlPageNavComposite);
		pageNavComposite.setLayoutData(gdPageNavComposite);

		GridData gdPageNavButtons = new GridData();
		
		gdPageNavButtons.grabExcessHorizontalSpace = false;
		gdPageNavButtons.horizontalAlignment = GridData.CENTER;
		gdPageNavButtons.verticalAlignment = GridData.CENTER;
		gdPageNavButtons.grabExcessVerticalSpace = true;
		gdPageNavButtons.horizontalIndent = 5;

		iLblFirstPageRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(), 
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_FIRST_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_FIRST_ARROW);
		iLblFirstPageRecords.setLayoutData(gdPageNavButtons);

		iLblPreviousRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(), 
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_PREVIOUS_ARROW);
		iLblPreviousRecords.setLayoutData(gdPageNavButtons);

		lblTotalPageCount = new SDSTSLabel(pageNavComposite, SWT.CENTER);
		lblTotalPageCount.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT));
		lblTotalPageCount.setLayoutData(gdPageNavButtons);

		iLblNextRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(), 
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_NEXTARROW_ARROW);
		iLblNextRecords.setLayoutData(gdPageNavButtons);

		iLblLastPageRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(), 
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_LAST_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_LAST_ARROW);
		iLblLastPageRecords.setLayoutData(gdPageNavButtons);

		GridLayout grlActionBtnsComposite = new GridLayout();
		grlActionBtnsComposite.numColumns =5;
		grlActionBtnsComposite.verticalSpacing = 5;
		grlActionBtnsComposite.marginWidth = 5;
		grlActionBtnsComposite.marginHeight = 5;
		grlActionBtnsComposite.horizontalSpacing =10;
		if( Util.isSmallerResolution() ) {
			grlActionBtnsComposite.marginWidth = 0;
			grlActionBtnsComposite.makeColumnsEqualWidth = false;
			grlActionBtnsComposite.horizontalSpacing =0;
		} else {
			grlActionBtnsComposite.makeColumnsEqualWidth = true;
		}
		GridData gdActionBtnsComposite = new GridData();
		gdActionBtnsComposite.grabExcessHorizontalSpace = true;
		gdActionBtnsComposite.horizontalAlignment = GridData.CENTER;
		gdActionBtnsComposite.verticalAlignment = GridData.FILL;
		gdActionBtnsComposite.grabExcessVerticalSpace = false;
		gdActionBtnsComposite.horizontalIndent = 20;
		gdActionBtnsComposite.heightHint = -1;
		gdActionBtnsComposite.widthHint = -1;

		Composite actionBtnsComposite = new Composite(mainComposite, SWT.CENTER);
		batchHdrRow2Composite.setBackground(SDSControlFactory.getTSBodyColor());
//		actionBtnsComposite.setBackground(new Color(Display.getCurrent(), ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_R, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_G, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_B));
		actionBtnsComposite.setLayout(grlActionBtnsComposite);
		actionBtnsComposite.setLayoutData(gdActionBtnsComposite);

		iLblReloadBatchDtls  = new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.RELOAD_BATCH_DETAILS), IVoucherConstants.RECON_CTRL_BTN_RELOAD_BATCH );
		iLblGetQuestionables = new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.QUESTIONABLE_VOUCHERS_COUPONS), IVoucherConstants.RECON_CTRL_BTN_QUESTIONABLE_VOUCHER);
		iLblPrintBatchDetails= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_DETAILS),IVoucherConstants.RECON_CTRL_BTN_PRINT_BATCH_DTL );
		iLblChangeEmployee 	= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.CHANGE_LOCATION), IVoucherConstants.RECON_CTRL_BTN_CHANGE_LOCATION);
		iLblCancel 			= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.CANCEL_BUTTON), IVoucherConstants.RECON_CANCEL);
		iLblAddTkt 			= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.ADD_TKT_IN_A_BATCH) + AppContextValues.getInstance().getTicketText() ,IVoucherConstants.RECON_CTRL_BTN_ADD_TKT_BATCH );
		iLblModifyTkt 		= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.UPDATE_TKT) + AppContextValues.getInstance().getTicketText(), IVoucherConstants.RECON_CTRL_BTN_MODIY_TKT);
		iLblDeleteTkt 		= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.DELETE_TKT_IN_A_BATCH) + AppContextValues.getInstance().getTicketText(),IVoucherConstants.RECON_CTRL_BTN_DELETE_TKT_BATCH );
		iLblDeleteBatch 	= new SDSImageLabel(actionBtnsComposite, SWT.NONE, buttonImage,
				LabelLoader.getLabelValue(IDBLabelKeyConstants.DELETE_CURRENT_BATCH),IVoucherConstants.RECON_CTRL_BTN_DELETE_CURRENT_BATCH );
	}

	private void pageHeading() {
		HeaderComposite headerComposite = new HeaderComposite(this,SWT.None,LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_BATCH_DETAILS_TITLE));
		headerComposite.displayMandatoryLabel(false);
	}

	private void createDispTableComposite() {

		GridData gdDispTableComposite = new GridData();
		gdDispTableComposite.grabExcessHorizontalSpace = true;
		gdDispTableComposite.horizontalSpan = 1;
		gdDispTableComposite.heightHint = -1;
//		gdDispTableComposite.widthHint = 960;
		gdDispTableComposite.horizontalAlignment = GridData.CENTER;
		gdDispTableComposite.verticalAlignment = GridData.FILL;
		gdDispTableComposite.horizontalIndent = 15;
		gdDispTableComposite.grabExcessVerticalSpace = true;
		if(Util.is1024X786Resolution()) {
			gdDispTableComposite.horizontalIndent = 0;
		}

		dispTableComposite = new Composite(mainComposite, SWT.BORDER);
		dispTableComposite.setBackground(new Color(Display.getCurrent(), ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_R, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_G, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_B));
		dispTableComposite.setLayout(new FillLayout());
		dispTableComposite.setLayoutData(gdDispTableComposite);

		// Table Descriptor for the Table Viewer
		TableDescriptor dispBatchDetailsTblDescriptor = new TableDescriptor(dispTableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL, IVoucherConstants.RECON_CTRL_TABLE_BATCH_DETAIL,"dispBatchDetailsDTO");

		dispBatchDetailsTblDescriptor.setSelItemFormProperty(IVoucherConstants.RECON_CTRL_TABLE_BATCH_DETAIL_SEL_ITEM);
		dispBatchDetailsTblDescriptor.setHeaderVisible(true);
		dispBatchDetailsTblDescriptor.setLinesVisible(true);

		ArrayList<TableColumnDescriptor> arrayListTblColDesc = new ArrayList<TableColumnDescriptor>();

		TableColumnDescriptor columnDescriptor;
		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .10, LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_LOCATION));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new LocationNoComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .17,LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_BARCODE));		
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new BarcodeComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .25,LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .17,LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new AmountComparator());


		columnDescriptor = new TableColumnDescriptor(SWT.LEFT,	.24,LabelLoader.getLabelValue(IDBLabelKeyConstants.REASON));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.CENTER, .000001,"",true,SWT.UP);		
		arrayListTblColDesc.add(columnDescriptor);
		timeStampComparator = new TimestampComparator();
		columnDescriptor.setComparator(timeStampComparator);

		dispBatchDetailsTblDescriptor.setTableColsDescriptors(arrayListTblColDesc);

		dispBatchDetailsTblDescriptor.setContentProvider(new DispBatchDetailsContentProvider());
		dispBatchDetailsTblDescriptor.setLabelProvider(new DispBatchDetailsLabelProvider());

		dispBatchDetailsTableViewer = Util.createTSTable(dispBatchDetailsTblDescriptor);
		dispBatchDetailsTblDescriptor.getTable().setData("name", IVoucherConstants.RECON_CTRL_TABLE_BATCH_DETAIL);
		dispBatchDetailsTableViewer.getControl().addListener(SWT.MeasureItem,new Listener() {
			public void handleEvent(Event event) {
				int clientWidth = dispBatchDetailsTableViewer
				.getTable().getClientArea().width;
				event.height = event.gc.getFontMetrics().getHeight() * 2;
				event.width = clientWidth * 2;
			}

		});
		/* Setting the column width as not resizable */
		for (int i = 0; i < arrayListTblColDesc.size(); i++) {
			if(Util.isSmallerResolution()) {
				dispBatchDetailsTblDescriptor.getTableColsDescriptors().get(i)
				.getTableColumn().setResizable(true);
			} else{
				dispBatchDetailsTblDescriptor.getTableColsDescriptors().get(i)
				.getTableColumn().setResizable(false);
			}
		}
	}

	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblTotalPageCount);
			SDSControlFactory.setTouchScreenLabelProperties(lblBatchNbr);
			SDSControlFactory.setTouchScreenLabelProperties(lblEmployee);
			SDSControlFactory.setTouchScreenLabelProperties(lblVoucherCount);
			SDSControlFactory.setTouchScreenLabelProperties(lblVoucherAmt);
			SDSControlFactory.setTouchScreenLabelProperties(lblCouponCount);
			SDSControlFactory.setTouchScreenLabelProperties(lblCouponAmt);
			SDSControlFactory.setTouchScreenTextProperties(txtBatchNbr);
			SDSControlFactory.setTouchScreenTextProperties(txtCouponAmt);
			SDSControlFactory.setTouchScreenTextProperties(txtCouponCount);
			SDSControlFactory.setTouchScreenTextProperties(txtEmployee);
			SDSControlFactory.setTouchScreenTextProperties(txtVoucherAmt);
			SDSControlFactory.setTouchScreenTextProperties(txtVoucherCount);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblAddTkt);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblCancel);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblChangeEmployee);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblDeleteBatch);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblDeleteTkt);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblFirstPageRecords);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblGetQuestionables);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblLastPageRecords);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblModifyTkt);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblNextRecords);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblPreviousRecords);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblPrintBatchDetails);
			SDSControlFactory.setTouchScreenBigImageLabelProperties(iLblReloadBatchDtls);

			int width   = 0;
			int height  = 0;
			int arrow_Btn_Hieght = 0;
			int arrow_Btn_Width  = 0;
			if( Util.isSmallerResolution() ) {
				width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				height 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 17;
				arrow_Btn_Hieght = ControlConstants.BUTTON_HEIGHT + 5;
				arrow_Btn_Width = ControlConstants.ARROW_BUTTON_WIDTH - 30;
				
				GridData gdBtnReloadBatchDetails   = (GridData) iLblReloadBatchDtls.getLayoutData();			
				gdBtnReloadBatchDetails.widthHint  = width;
				gdBtnReloadBatchDetails.heightHint = height;
				iLblReloadBatchDtls.setLayoutData(gdBtnReloadBatchDetails);
				GridData gdBtnGetQuestionables   = (GridData) iLblGetQuestionables.getLayoutData();			
				gdBtnGetQuestionables.widthHint  = width;
				gdBtnGetQuestionables.heightHint = height;
				iLblGetQuestionables.setLayoutData(gdBtnGetQuestionables);
				GridData gdBtnPrintBatchDetails   = (GridData) iLblPrintBatchDetails.getLayoutData();			
				gdBtnPrintBatchDetails.widthHint  = width;
				gdBtnPrintBatchDetails.heightHint = height;
				iLblPrintBatchDetails.setLayoutData(gdBtnPrintBatchDetails);
				GridData gdBtnChangeEmployee   = (GridData) iLblChangeEmployee.getLayoutData();			
				gdBtnChangeEmployee.widthHint  = width;
				gdBtnChangeEmployee.heightHint = height;
				iLblChangeEmployee.setLayoutData(gdBtnChangeEmployee);
				GridData gdBtnCancel   = (GridData) iLblCancel.getLayoutData();			
				gdBtnCancel.widthHint  = width;
				gdBtnCancel.heightHint = height;
				iLblCancel.setLayoutData(gdBtnCancel);
				GridData gdBtnAddTkt = (GridData) iLblAddTkt.getLayoutData();			
				gdBtnAddTkt.widthHint = width;
				gdBtnAddTkt.heightHint = height;
				iLblAddTkt.setLayoutData(gdBtnAddTkt);
				GridData gdBtnModifyTkt   = (GridData) iLblModifyTkt.getLayoutData();			
				gdBtnModifyTkt.widthHint  = width;
				gdBtnModifyTkt.heightHint = height;
				iLblModifyTkt.setLayoutData(gdBtnModifyTkt);
				GridData gdBtnDeleteTkt   = (GridData) iLblDeleteTkt.getLayoutData();			
				gdBtnDeleteTkt.widthHint  = width;
				gdBtnDeleteTkt.heightHint = height;
				iLblDeleteTkt.setLayoutData(gdBtnDeleteTkt);
				GridData gdBtnDeleteBatch 	= (GridData) iLblDeleteBatch.getLayoutData();			
				gdBtnDeleteBatch.widthHint  = width;
				gdBtnDeleteBatch.heightHint = height;
				iLblDeleteBatch.setLayoutData(gdBtnDeleteBatch);
				
				GridData gdLblCount = (GridData) lblTotalPageCount.getLayoutData();			
				gdLblCount.widthHint = arrow_Btn_Width;
				
				lblTotalPageCount.setLayoutData(gdLblCount);
				
				GridData gdFirstPageRecords  = (GridData)iLblFirstPageRecords.getLayoutData();				
				gdFirstPageRecords.heightHint= arrow_Btn_Hieght;	
				gdFirstPageRecords.widthHint = arrow_Btn_Width;
				iLblFirstPageRecords.setLayoutData(gdFirstPageRecords);
				GridData gdBtnLastPageRecords  = (GridData)iLblLastPageRecords.getLayoutData();				
				gdBtnLastPageRecords.heightHint= arrow_Btn_Hieght;
				gdBtnLastPageRecords.widthHint = arrow_Btn_Width;
				iLblLastPageRecords.setLayoutData(gdBtnLastPageRecords);
				GridData gdBtnNextRecords  = (GridData)iLblNextRecords.getLayoutData();				
				gdBtnNextRecords.heightHint= arrow_Btn_Hieght;
				gdBtnNextRecords.widthHint = arrow_Btn_Width;
				iLblNextRecords.setLayoutData(gdBtnNextRecords);
				GridData gdBtnPreviousRecords  = (GridData)iLblPreviousRecords.getLayoutData();				
				gdBtnPreviousRecords.heightHint= arrow_Btn_Hieght;
				gdBtnPreviousRecords.widthHint = arrow_Btn_Width;
				iLblPreviousRecords.setLayoutData(gdBtnPreviousRecords);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class DispBatchDetailsContentProvider implements
	IStructuredContentProvider {

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			tabindex = 0;
			return ((List) inputElement).toArray();
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	private class DispBatchDetailsLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ReclBatchDetailsInfoDTO batchDtlDTO = (ReclBatchDetailsInfoDTO) element;

			switch (columnIndex) 
			{
			case 0:
				String assetId = batchDtlDTO.getAssetConfNbr().trim();
				if(assetId!=null)
					return assetId;
				else
					return "";

			case 1:
				String vouBrcd = batchDtlDTO.getReclVouBrcd();
				if(vouBrcd!=null)
					return vouBrcd;
				else
					return "";			
			case 2:
				Long tktTypeId = batchDtlDTO.getTktTypeId();
				String tktType = "";
				if( tktTypeId != null ) {
					if( tktTypeId == 7 || tktTypeId == 1 || tktTypeId == 2 || tktTypeId == 3 || tktTypeId == 4 || tktTypeId == 9 ) {
						tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_NON_SLOT);
					
					} else if(tktTypeId == 5 || tktTypeId == 6 || tktTypeId == 90 || tktTypeId == 91) {//Added 90,91 as Enhanced Validation and Standard Validation Ticket Type is 90,91
						tktType = LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_TYPE_SLOT);
					}
					return tktType;
				}
				else
					return "";					
			case 3:
				Long amt = batchDtlDTO.getTktAmount();
				if(amt!=null){
					return CurrencyUtil.getCurrencyFormat(ConversionUtil.centsToDollar(amt.longValue()));
				}
				else
					return "";					
			case 4:
				Long auditCode = batchDtlDTO.getAuditId();
				if(auditCode!=null){
					System.out.println("batchDtlDTO.getAuditCodeDesc(): "+batchDtlDTO.getAuditCodeDesc());
					String reason  = LabelLoader.getLabelValue(batchDtlDTO.getAuditCodeDesc());
					return reason;
				}		
				else
					return "";
			case 5:
				String createdTs = Long.toString(batchDtlDTO.getCreatedTs());				
				return createdTs;
			default:
				return "";
			}

		}

		public void addListener(ILabelProviderListener listener) {

		}

		public void dispose() {

		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}
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
	 * @return the dispBatchDetailsTableViewer
	 */
	public SDSTSTableViewer getDispBatchDetailsTableViewer() {
		return dispBatchDetailsTableViewer;
	}

	/**
	 * @param dispBatchDetailsTableViewer the dispBatchDetailsTableViewer to set
	 */
	public void setDispBatchDetailsTableViewer(
			SDSTSTableViewer dispBatchDetailsTableViewer) {
		this.dispBatchDetailsTableViewer = dispBatchDetailsTableViewer;
	}

	/**
	 * @return the dispTableComposite
	 */
	public Composite getDispTableComposite() {
		return dispTableComposite;
	}

	/**
	 * @param dispTableComposite the dispTableComposite to set
	 */
	public void setDispTableComposite(Composite dispTableComposite) {
		this.dispTableComposite = dispTableComposite;
	}

	/**
	 * @return the iLblFirstPageRecords
	 */
	public SDSImageLabel getILblFirstPageRecords() {
		return iLblFirstPageRecords;
	}

	/**
	 * @param iLblFirstPageRecords the iLblFirstPageRecords to set
	 */
	public void setILblFirstPageRecords(SDSImageLabel iLblFirstPageRecords) {
		this.iLblFirstPageRecords = iLblFirstPageRecords;
	}

	/**
	 * @return the iLblPreviousRecords
	 */
	public SDSImageLabel getILblPreviousRecords() {
		return iLblPreviousRecords;
	}

	/**
	 * @param iLblPreviousRecords the iLblPreviousRecords to set
	 */
	public void setILblPreviousRecords(SDSImageLabel iLblPreviousRecords) {
		this.iLblPreviousRecords = iLblPreviousRecords;
	}

	/**
	 * @return the iLblNextRecords
	 */
	public SDSImageLabel getILblNextRecords() {
		return iLblNextRecords;
	}

	/**
	 * @param iLblNextRecords the iLblNextRecords to set
	 */
	public void setILblNextRecords(SDSImageLabel iLblNextRecords) {
		this.iLblNextRecords = iLblNextRecords;
	}

	/**
	 * @return the iLblLastPageRecords
	 */
	public SDSImageLabel getILblLastPageRecords() {
		return iLblLastPageRecords;
	}

	/**
	 * @param iLblLastPageRecords the iLblLastPageRecords to set
	 */
	public void setILblLastPageRecords(SDSImageLabel iLblLastPageRecords) {
		this.iLblLastPageRecords = iLblLastPageRecords;
	}

	/**
	 * @return the tabindex
	 */
	public int getTabindex() {
		return tabindex;
	}

	/**
	 * @param tabindex the tabindex to set
	 */
	public void setTabindex(int tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * @return the iLblReloadBatchDtls
	 */
	public SDSImageLabel getILblReloadBatchDtls() {
		return iLblReloadBatchDtls;
	}

	/**
	 * @param iLblReloadBatchDtls the iLblReloadBatchDtls to set
	 */
	public void setILblReloadBatchDtls(SDSImageLabel lblReloadBatchDtls) {
		iLblReloadBatchDtls = lblReloadBatchDtls;
	}

	/**
	 * @return the iLblGetQuestionables
	 */
	public SDSImageLabel getILblGetQuestionables() {
		return iLblGetQuestionables;
	}

	/**
	 * @param iLblGetQuestionables the iLblGetQuestionables to set
	 */
	public void setILblGetQuestionables(SDSImageLabel lblGetQuestionables) {
		iLblGetQuestionables = lblGetQuestionables;
	}

	/**
	 * @return the iLblPrintBatchDetails
	 */
	public SDSImageLabel getILblPrintBatchDetails() {
		return iLblPrintBatchDetails;
	}

	/**
	 * @param iLblPrintBatchDetails the iLblPrintBatchDetails to set
	 */
	public void setILblPrintBatchDetails(SDSImageLabel lblPrintBatchDetails) {
		iLblPrintBatchDetails = lblPrintBatchDetails;
	}

	/**
	 * @return the iLblDeleteBatch
	 */
	public SDSImageLabel getILblDeleteBatch() {
		return iLblDeleteBatch;
	}

	/**
	 * @param iLblDeleteBatch the iLblDeleteBatch to set
	 */
	public void setILblDeleteBatch(SDSImageLabel lblDeleteBatch) {
		iLblDeleteBatch = lblDeleteBatch;
	}

	/**
	 * @return the iLblChangeEmployee
	 */
	public SDSImageLabel getILblChangeEmployee() {
		return iLblChangeEmployee;
	}

	/**
	 * @param iLblChangeEmployee the iLblChangeEmployee to set
	 */
	public void setILblChangeEmployee(SDSImageLabel lblChangeEmployee) {
		iLblChangeEmployee = lblChangeEmployee;
	}

	/**
	 * @return the iLblDeleteTkt
	 */
	public SDSImageLabel getILblDeleteTkt() {
		return iLblDeleteTkt;
	}

	/**
	 * @param iLblDeleteTkt the iLblDeleteTkt to set
	 */
	public void setILblDeleteTkt(SDSImageLabel lblDeleteTkt) {
		iLblDeleteTkt = lblDeleteTkt;
	}

	/**
	 * @return the iLblAddTkt
	 */
	public SDSImageLabel getILblAddTkt() {
		return iLblAddTkt;
	}

	/**
	 * @param iLblAddTkt the iLblAddTkt to set
	 */
	public void setILblAddTkt(SDSImageLabel lblAddTkt) {
		iLblAddTkt = lblAddTkt;
	}

	/**
	 * @return the iLblModifyTkt
	 */
	public SDSImageLabel getILblModifyTkt() {
		return iLblModifyTkt;
	}

	/**
	 * @param iLblModifyTkt the iLblModifyTkt to set
	 */
	public void setILblModifyTkt(SDSImageLabel lblModifyTkt) {
		iLblModifyTkt = lblModifyTkt;
	}

	/**
	 * @return the iLblCancel
	 */
	public SDSImageLabel getILblCancel() {
		return iLblCancel;
	}

	/**
	 * @param iLblCancel the iLblCancel to set
	 */
	public void setILblCancel(SDSImageLabel lblCancel) {
		iLblCancel = lblCancel;
	}

	/**
	 * @return the txtBatchNbr
	 */
	public SDSTSText getTxtBatchNbr() {
		return txtBatchNbr;
	}

	/**
	 * @param txtBatchNbr the txtBatchNbr to set
	 */
	public void setTxtBatchNbr(SDSTSText txtBatchNbr) {
		this.txtBatchNbr = txtBatchNbr;
	}

	/**
	 * @return the txtEmployee
	 */
	public SDSTSText getTxtEmployee() {
		return txtEmployee;
	}

	/**
	 * @param txtEmployee the txtEmployee to set
	 */
	public void setTxtEmployee(SDSTSText txtEmployee) {
		this.txtEmployee = txtEmployee;
	}

	/**
	 * @return the txtVoucherCount
	 */
	public SDSTSText getTxtVoucherCount() {
		return txtVoucherCount;
	}

	/**
	 * @param txtVoucherCount the txtVoucherCount to set
	 */
	public void setTxtVoucherCount(SDSTSText txtVoucherCount) {
		this.txtVoucherCount = txtVoucherCount;
	}

	/**
	 * @return the txtVoucherAmt
	 */
	public SDSTSText getTxtVoucherAmt() {
		return txtVoucherAmt;
	}

	/**
	 * @param txtVoucherAmt the txtVoucherAmt to set
	 */
	public void setTxtVoucherAmt(SDSTSText txtVoucherAmt) {
		this.txtVoucherAmt = txtVoucherAmt;
	}

	/**
	 * @return the txtCouponCount
	 */
	public SDSTSText getTxtCouponCount() {
		return txtCouponCount;
	}

	/**
	 * @param txtCouponCount the txtCouponCount to set
	 */
	public void setTxtCouponCount(SDSTSText txtCouponCount) {
		this.txtCouponCount = txtCouponCount;
	}

	/**
	 * @return the txtCouponAmt
	 */
	public SDSTSText getTxtCouponAmt() {
		return txtCouponAmt;
	}

	/**
	 * @param txtCouponAmt the txtCouponAmt to set
	 */
	public void setTxtCouponAmt(SDSTSText txtCouponAmt) {
		this.txtCouponAmt = txtCouponAmt;
	}

	/**
	 * @return the lblEmployee
	 */
	public SDSTSLabel getLblEmployee() {
		return lblEmployee;
	}

	/**
	 * @param lblEmployee the lblEmployee to set
	 */
	public void setLblEmployee(SDSTSLabel lblEmployee) {
		this.lblEmployee = lblEmployee;
	}

	/**
	 * @return the lblTotalPageCount
	 */
	public SDSTSLabel getLblTotalPageCount() {
		return lblTotalPageCount;
	}

	/**
	 * @param lblTotalPageCount the lblTotalPageCount to set
	 */
	public void setLblTotalPageCount(SDSTSLabel lblTotalPageCount) {
		this.lblTotalPageCount = lblTotalPageCount;
	}

	/**
	 * @return the timeStampComparator
	 */
	public TimestampComparator getTimeStampComparator() {
		return timeStampComparator;
	}

	private void createImages() {
		buttonImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
	}

	/**
	 * @return the buttonImage
	 */
	public Image getButtonImage() {
		return buttonImage;
	}

	/**
	 * @return the buttonDisableImage
	 */
	public Image getButtonDisableImage() {
		return buttonDisableImage;
	}
}
