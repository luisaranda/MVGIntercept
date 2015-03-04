/*****************************************************************************
 * $Id: BatchSummaryComposite.java,v 1.41, 2010-11-19 11:43:57Z, Verma, Nitin Kumar$
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
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
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ICompositeStyleConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSTableViewer;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.form.BatchSummaryDTO;
import com.ballydev.sds.voucherui.util.BatchNoComparator;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.CouponAmountComparator;
import com.ballydev.sds.voucherui.util.CouponCountComparator;
import com.ballydev.sds.voucherui.util.EmployeeNoComparator;
import com.ballydev.sds.voucherui.util.LocationCodeComparator;
import com.ballydev.sds.voucherui.util.VoucherAmountComparator;
import com.ballydev.sds.voucherui.util.VoucherCountComparator;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * @author Nithya kalyani R
 * @version $Revision: 42$ 
 */
public class BatchSummaryComposite extends Composite {

	/**
	 * Instance of the Load selected batch image label.
	 */
	private SDSImageLabel iLblLoadSelectedBatch = null;

	/**
	 * Instance of the selected batch image label.
	 */
	private SDSImageLabel iLblDeleteSelectedBatch = null; 

	/**
	 * Instance of the change emp image label.
	 */
	private SDSImageLabel iLblChangeEmployeeOrKiosk = null;

	/**
	 * Instance of Print Batch Details image label.
	 */
	private SDSImageLabel iLblPrintBatchDetails = null;

	/**
	 * Instance of the cancel image label.
	 */
	private SDSImageLabel iLblCancel = null; 

	/**
	 * Instance of the the table viewer
	 */
	private SDSTSTableViewer tableViewer = null;

	/**
	 * Instance of the table descriptor
	 */
	private TableDescriptor tblBatchSummaryDtl = null;

	/**
	 * Instance of the display table composite
	 */
	private Composite dispTableComposite = null;

	/**
	 * Instance to check whether the cashier button is checked
	 */
	private boolean isCashier ;

	/**
	 * Instance of the main composite
	 */
	private Composite mainComposite = null;

	/**
	 * Display First Page Records Button instance
	 */
	private SDSImageLabel iLblFirstPageRecords = null;

	/**
	 * Display Previous Records Button instance
	 */
	private SDSImageLabel iLblPreviousRecords = null;

	/**
	 * Display Next Records Button instance
	 */
	private SDSImageLabel iLblNextRecords = null;

	/**
	 * Display Last Page Records Button instance
	 */
	private SDSImageLabel iLblLastPageRecords = null;

	/**
	 * Table Index instance
	 */
	private int tabindex;

	/**
	 * Instance of the total page count label
	 */
	private SDSTSLabel lblTotalPageCount = null;

	private BatchNoComparator batchNoComparator = null;

	private Image buttonImage;

	private Image buttonDisableImage;
	
	/**
	 * Constructor of the class
	 */
	public BatchSummaryComposite(Composite parent, int style) {
		super(parent, style);
		createImages();
		initialize();
		//setSize(parent.getSize());
	}

	/**
	 * Constructor of the class
	 */
	public BatchSummaryComposite(Composite parent, int style,boolean isCashier) {
		super(parent, style);
		this.isCashier = isCashier;
		this.setBackground(SDSControlFactory.getTSBodyColor());
		createImages();
		initialize();
		//setSize(parent.getSize());
	}

	/**
	 * This method sets the grid properties for the
	 * composite
	 */
	private void initialize() {			

		// Setting Page Layout
		GridLayout grlPage = new GridLayout();
		grlPage.verticalSpacing = 1;
		grlPage.marginHeight = 0;
		grlPage.marginWidth = 0;
		grlPage.horizontalSpacing = 5;
		grlPage.makeColumnsEqualWidth = true;


		//Grid Data for the Page
		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		pageHeading();

		GridLayout grlMainComposite = new GridLayout();
		grlMainComposite.numColumns = 1;
		grlMainComposite.verticalSpacing = 10;
		grlMainComposite.makeColumnsEqualWidth = false;
		grlMainComposite.horizontalSpacing = 1;

		GridData gdMainComposite = new GridData();
		gdMainComposite.grabExcessHorizontalSpace = true;
		gdMainComposite.grabExcessVerticalSpace = true;
		gdMainComposite.horizontalAlignment = GridData.CENTER;
		gdMainComposite.verticalAlignment = GridData.FILL;
		gdMainComposite.heightHint = -1;
		gdMainComposite.verticalSpan = 2;
		gdMainComposite.widthHint = -1;

		mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setBackground(SDSControlFactory.getTSBodyColor());
//		mainComposite.setBackground(new Color(Display.getCurrent(), ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_R, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_G, ITSConstants.SDSTS_LBL_HEADING_DEFAULT_FONT_B));
		mainComposite.setLayout(grlMainComposite);
		mainComposite.setLayoutData(gdMainComposite);
		createTable();
		submitProcess();
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
	}

	/**
	 * This creates the header composite
	 */
	private void pageHeading() {
		HeaderComposite headerComposite = new HeaderComposite(this, SWT.None, LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_BATCH_DETAILS_TITLE));
		headerComposite.displayMandatoryLabel(false);
	}

	private void submitProcess(){

		GridLayout grlPageNavComposite = new GridLayout();
		grlPageNavComposite.numColumns = 5;
		grlPageNavComposite.verticalSpacing = 5;
		grlPageNavComposite.marginHeight = 5;
		grlPageNavComposite.marginWidth = 45;
		grlPageNavComposite.makeColumnsEqualWidth = true;

		if( Util.isSmallerResolution() ) {
			grlPageNavComposite.verticalSpacing = 0;
			grlPageNavComposite.marginWidth = 0;
			grlPageNavComposite.marginHeight = 0;
			grlPageNavComposite.makeColumnsEqualWidth = false;
			grlPageNavComposite.horizontalSpacing = 0;
		}
		
		if( Util.is1024X786Resolution() ) {
			grlPageNavComposite.verticalSpacing = 0;
			grlPageNavComposite.marginWidth = 20;
			grlPageNavComposite.marginHeight = 0;
			grlPageNavComposite.makeColumnsEqualWidth = true;
			grlPageNavComposite.horizontalSpacing = 5;
		}
		
		GridData gdPageNavComposite = new GridData();
		gdPageNavComposite.verticalAlignment = GridData.FILL;
		gdPageNavComposite.horizontalAlignment = GridData.FILL;
		gdPageNavComposite.grabExcessVerticalSpace = false;
		gdPageNavComposite.horizontalIndent = 585;
		
		if( Util.isSmallerResolution() ) {
			gdPageNavComposite.horizontalIndent = 375;
		}

		if( Util.is1024X786Resolution() ) {
			gdPageNavComposite.horizontalIndent = 450;
		}

		Composite pageNavComposite = new Composite(mainComposite, SWT.CENTER);
		pageNavComposite.setBackground(SDSControlFactory.getTSBodyColor());
		pageNavComposite.setLayout(grlPageNavComposite);
		pageNavComposite.setLayoutData(gdPageNavComposite);

		GridData gdPageNavButtons = new GridData();
		
		gdPageNavButtons.grabExcessHorizontalSpace = false;
		gdPageNavButtons.heightHint = -1;
		gdPageNavButtons.widthHint = -1;
		gdPageNavButtons.horizontalAlignment = GridData.CENTER;
		gdPageNavButtons.verticalAlignment = GridData.CENTER;
		gdPageNavButtons.grabExcessVerticalSpace = true;
		gdPageNavButtons.horizontalIndent = 5;
		
//		gdPageNavButtons.grabExcessHorizontalSpace = false;
//		gdPageNavButtons.horizontalAlignment = GridData.CENTER;
//		gdPageNavButtons.verticalAlignment = GridData.CENTER;
//		gdPageNavButtons.grabExcessVerticalSpace = true;
		
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
		
		GridData gridDataBtn = new GridData();
		gridDataBtn.grabExcessVerticalSpace = true;
		gridDataBtn.horizontalAlignment = GridData.CENTER;
		gridDataBtn.verticalAlignment = GridData.CENTER;
		gridDataBtn.heightHint = 40;
		gridDataBtn.widthHint = 150;
		gridDataBtn.grabExcessHorizontalSpace = false;

		GridData gdBtnComposite = new GridData();
		gdBtnComposite.grabExcessHorizontalSpace = true;
		gdBtnComposite.verticalAlignment = GridData.FILL;
		gdBtnComposite.verticalIndent = 70;
		gdBtnComposite.horizontalAlignment = GridData.CENTER;

		GridLayout grlBtnComposite = new GridLayout();
		grlBtnComposite.numColumns = 5;
		grlBtnComposite.verticalSpacing = 30;
		grlBtnComposite.horizontalSpacing = 10;
		if( Util.isSmallerResolution() ) {
			grlBtnComposite.horizontalSpacing = 5;
		}

		Composite iLblComposite  = new Composite(mainComposite, SWT.NONE);
		iLblComposite.setLayout(grlBtnComposite);
		iLblComposite.setLayoutData(gdBtnComposite);

		iLblLoadSelectedBatch = new SDSImageLabel(iLblComposite, SWT.BUTTON1, buttonImage, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_LOAD_SELECTED_BATCH),
				IVoucherConstants.RECON_LOAD_SELECTED_BATCH);

		iLblDeleteSelectedBatch = new SDSImageLabel(iLblComposite, SWT.BUTTON1, buttonImage, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_DELETE_SELECTED_BATCH),
				IVoucherConstants.RECON_DELETE_SELECTED_BATCH);

		iLblChangeEmployeeOrKiosk = new SDSImageLabel(iLblComposite, SWT.BUTTON1, buttonImage, LabelLoader.getLabelValue(IDBLabelKeyConstants.BTN_CHANGE_EMPLOYEE),
				IVoucherConstants.RECON_CHANGE_EMPLOYEE);

		iLblPrintBatchDetails = new SDSImageLabel(iLblComposite, SWT.NONE, buttonImage, LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_DETAILS),
				IVoucherConstants.RECON_CTRL_BTN_PRINT_BATCH_DTL );

		iLblCancel = new SDSImageLabel(iLblComposite, SWT.NONE, buttonImage, LabelLoader.getLabelValue(IDBLabelKeyConstants.CANCEL_BUTTON), 
				IVoucherConstants.RECON_CANCEL);

	}

	/**
	 * This method set the grid properties for all the controls.
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblCancel);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblChangeEmployeeOrKiosk);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblDeleteSelectedBatch);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblLoadSelectedBatch);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblPrintBatchDetails);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblFirstPageRecords);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblLastPageRecords);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblNextRecords);
			SDSControlFactory.setTouchScreenImageLabelProperties(iLblPreviousRecords);
			SDSControlFactory.setTouchScreenLabelProperties(lblTotalPageCount);
			
			int width  = 0;
			int hieght = 0;
			int arrow_Btn_Width = 0;
			int arrow_Btn_Hieght = 0;
			if( Util.isSmallerResolution() ) {
				width 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_WIDTH + 21;
				hieght 	= ICompositeStyleConstants.TOUCHSCREEN_S_BUTTON_HEIGHT + 17;
				arrow_Btn_Width = ControlConstants.ARROW_BUTTON_WIDTH-29;
				arrow_Btn_Hieght = ControlConstants.BUTTON_HEIGHT + 6;
			
				GridData gdLblCount = (GridData) lblTotalPageCount.getLayoutData();			
				gdLblCount.widthHint = arrow_Btn_Width;
				lblTotalPageCount.setLayoutData(gdLblCount);
	
				GridData gdFirstPageRecords = (GridData)iLblFirstPageRecords.getLayoutData();				
				gdFirstPageRecords.heightHint= arrow_Btn_Hieght;
				gdFirstPageRecords.widthHint = arrow_Btn_Width;
				iLblFirstPageRecords.setLayoutData(gdFirstPageRecords);
				
				GridData gdBtnLastPageRecords = (GridData)iLblLastPageRecords.getLayoutData();				
				gdBtnLastPageRecords.heightHint= arrow_Btn_Hieght;
				gdBtnLastPageRecords.widthHint = arrow_Btn_Width;
				iLblLastPageRecords.setLayoutData(gdBtnLastPageRecords);
				
				GridData gdBtnNextRecords = (GridData)iLblNextRecords.getLayoutData();				
				gdBtnNextRecords.heightHint= arrow_Btn_Hieght;
				gdBtnNextRecords.widthHint = arrow_Btn_Width;
				iLblNextRecords.setLayoutData(gdBtnNextRecords);
				
				GridData gdBtnPreviousRecords = (GridData)iLblPreviousRecords.getLayoutData();				
				gdBtnPreviousRecords.heightHint= arrow_Btn_Hieght;
				gdBtnPreviousRecords.widthHint = arrow_Btn_Width;
				iLblPreviousRecords.setLayoutData(gdBtnPreviousRecords);

				GridData gdBtnChangeEmp = (GridData) iLblChangeEmployeeOrKiosk.getLayoutData();			
				gdBtnChangeEmp.widthHint = width;
				gdBtnChangeEmp.heightHint = hieght;
				iLblChangeEmployeeOrKiosk.setLayoutData(gdBtnChangeEmp);
				
				GridData gdBtnDeleteBatch = (GridData) iLblDeleteSelectedBatch.getLayoutData();			
				gdBtnDeleteBatch.widthHint = width;
				gdBtnDeleteBatch.heightHint = hieght;
				iLblDeleteSelectedBatch.setLayoutData(gdBtnDeleteBatch);
				
				GridData gdBtnLoadSelectedBatch = (GridData) iLblLoadSelectedBatch.getLayoutData();			
				gdBtnLoadSelectedBatch.widthHint = width;
				gdBtnLoadSelectedBatch.heightHint = hieght;
				iLblLoadSelectedBatch.setLayoutData(gdBtnLoadSelectedBatch);
				
				GridData gdBtnPrintBatchDetails = (GridData) iLblPrintBatchDetails.getLayoutData();			
				gdBtnPrintBatchDetails.widthHint = width;
				gdBtnPrintBatchDetails.heightHint = hieght;
				iLblPrintBatchDetails.setLayoutData(gdBtnPrintBatchDetails);
				
				GridData gdBtnCancel = (GridData) iLblCancel.getLayoutData();			
				gdBtnCancel.widthHint = width;
				gdBtnCancel.heightHint = hieght;
				iLblCancel.setLayoutData(gdBtnCancel);
	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * This method creates the table
	 */
	private void createTable() {
		GridData gdDispTableComposite = new GridData();
		gdDispTableComposite.grabExcessHorizontalSpace = true;
		gdDispTableComposite.horizontalSpan = 1;
		gdDispTableComposite.heightHint = -1;
//		gdDispTableComposite.widthHint = 970;
		gdDispTableComposite.horizontalAlignment = GridData.CENTER;
		gdDispTableComposite.verticalAlignment = GridData.FILL;
		gdDispTableComposite.grabExcessVerticalSpace = true;


		Composite dispTableComposite = new Composite(mainComposite, SWT.BORDER);
		dispTableComposite.setBackground(VoucherUtil.getDefaultBackGroudColor());
//		dispTableComposite.setBackground(new Color(Display.getCurrent(), 212,22,21));
		dispTableComposite.setLayout(new FillLayout());
		dispTableComposite.setLayoutData(gdDispTableComposite);

		tblBatchSummaryDtl = new TableDescriptor(dispTableComposite,SWT.FULL_SELECTION | SWT.H_SCROLL, IVoucherConstants.RECON_CTRL_TABLE_BATCH_SUMMARY,IVoucherConstants.RECON_CTRL_TABLE_BATCH_SUMMARY);

		tblBatchSummaryDtl.setSelItemFormProperty(IVoucherConstants.RECON_CTRL_TABLE_BATCH_SUMM_SEL_ITEM);
		tblBatchSummaryDtl.setHeaderVisible(true);
		tblBatchSummaryDtl.setLinesVisible(true);

		// Table Descriptor for the Table Viewer
		TableDescriptor dispBatchDetailsTblDescriptor = new TableDescriptor(dispTableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL, IVoucherConstants.RECON_CTRL_TABLE_BATCH_DETAIL,"");
		dispBatchDetailsTblDescriptor.setHeaderVisible(true);
		dispBatchDetailsTblDescriptor.setLinesVisible(true);


		ArrayList<TableColumnDescriptor> arrayListTblColDesc = addColumns();

		tblBatchSummaryDtl.setContentProvider(new BatchContentProvider());
		tblBatchSummaryDtl.setLabelProvider(new BatchDataProvider());
		tableViewer = Util.createTSTable(tblBatchSummaryDtl);
		tblBatchSummaryDtl.getTable().setData("name", IVoucherConstants.RECON_CTRL_TABLE_BATCH_SUMMARY);

		tableViewer.getControl().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				int clientWidth = tableViewer.getTable().getClientArea().width;
				event.height = event.gc.getFontMetrics().getHeight() * 2;
				event.width = clientWidth * 2;
			}

		});
		/* Setting the column width as not resizable */
		for (int i = 0; i < arrayListTblColDesc.size(); i++) {
			if(Util.isSmallerResolution()) {
				tblBatchSummaryDtl.getTableColsDescriptors().get(i)
				.getTableColumn().setResizable(true);
			} else{
				tblBatchSummaryDtl.getTableColsDescriptors().get(i)
				.getTableColumn().setResizable(false);
			}
		}
	}

	/**
	 * @return
	 */
	private ArrayList<TableColumnDescriptor> addColumns() {
		ArrayList<TableColumnDescriptor> arrayListTblColDesc = new ArrayList<TableColumnDescriptor>();


		TableColumnDescriptor columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .00,	"");
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .15,	LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_BATCH_NO));
		arrayListTblColDesc.add(columnDescriptor);
		batchNoComparator = new BatchNoComparator();
		columnDescriptor.setComparator(new BatchNoComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .10,	AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_VOUCHER_COUNT));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new VoucherCountComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .16,	AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_VOUCHER_$)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new VoucherAmountComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .14,LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_COUPAN_COUNT));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new CouponCountComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .18, LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_COUPAN_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new CouponAmountComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .10, LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new EmployeeNoComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .14, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_LOC_CODE));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new LocationCodeComparator());



		tblBatchSummaryDtl.setTableColsDescriptors(arrayListTblColDesc);
		return arrayListTblColDesc;
	}

	class BatchContentProvider implements IStructuredContentProvider {

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((List) inputElement).toArray();
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	class BatchDataProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		public String getColumnText(Object dto, int columnIndex) {
			BatchSummaryDTO batchSummaryDTO = (BatchSummaryDTO) dto;
			switch (columnIndex) {
			case 1:
				return batchSummaryDTO.getBatchNumber().toString();
			case 2:
				return batchSummaryDTO.getVoucherCount().toString();
			case 3:
				return CurrencyUtil.getCurrencyFormat(ConversionUtil.voucherAmountFormat(batchSummaryDTO.getVoucherAmount()));
			case 4:
				return batchSummaryDTO.getCouponCount().toString();
			case 5:
				return CurrencyUtil.getCurrencyFormat(ConversionUtil.voucherAmountFormat(batchSummaryDTO.getCouponAmount()));
			case 6:
				return batchSummaryDTO.getEmpId();
			case 7:
				return batchSummaryDTO.getLocCode();
			default:
				break;
			}

			return null;
		}

		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}
	}

	/**
	 * @return the iLblLoadSelectedBatch
	 */
	public SDSImageLabel getILblLoadSelectedBatch() {
		return iLblLoadSelectedBatch;
	}

	/**
	 * @param iLblLoadSelectedBatch the iLblLoadSelectedBatch to set
	 */
	public void setILblLoadSelectedBatch(SDSImageLabel iLblLoadSelectedBatch) {
		this.iLblLoadSelectedBatch = iLblLoadSelectedBatch;
	}

	/**
	 * @return the deleteSelectedBatch
	 */
	public SDSImageLabel getILblDeleteSelectedBatch() {
		return iLblDeleteSelectedBatch;
	}

	/**
	 * @param deleteSelectedBatch the deleteSelectedBatch to set
	 */
	public void setILblDeleteSelectedBatch(SDSImageLabel deleteSelectedBatch) {
		this.iLblDeleteSelectedBatch = deleteSelectedBatch;
	}

	/**
	 * @return the iLblChangeEmployeeOrKiosk
	 */
	public SDSImageLabel getILblChangeEmployeeOrKiosk() {
		return iLblChangeEmployeeOrKiosk;
	}

	/**
	 * @param iLblChangeEmployeeOrKiosk the iLblChangeEmployeeOrKiosk to set
	 */
	public void setILblChangeEmployeeOrKiosk(SDSImageLabel iLblChangeEmployeeOrKiosk) {
		this.iLblChangeEmployeeOrKiosk = iLblChangeEmployeeOrKiosk;
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
	public void setILblCancel(SDSImageLabel iLblCancel) {
		this.iLblCancel = iLblCancel;
	}

	/**
	 * @return the iLblPrintBatchDetails
	 */
	public SDSImageLabel getILblPrintBatchDetails() {
		return iLblPrintBatchDetails;
	}

	/**
	 * @return the tableViewer
	 */
	public SDSTSTableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * @param tableViewer the tableViewer to set
	 */
	public void setTableViewer(SDSTSTableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	/**
	 * @return the tblBatchSummaryDtl
	 */
	public TableDescriptor getTblBatchSummaryDtl() {
		return tblBatchSummaryDtl;
	}

	/**
	 * @param tblBatchSummaryDtl the tblBatchSummaryDtl to set
	 */
	public void setTblBatchSummaryDtl(TableDescriptor tblBatchSummaryDtl) {
		this.tblBatchSummaryDtl = tblBatchSummaryDtl;
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
	 * @return the isCashier
	 */
	public boolean isCashier() {
		return isCashier;
	}

	/**
	 * @param isCashier the isCashier to set
	 */
	public void setCashier(boolean isCashier) {
		this.isCashier = isCashier;
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
	 * @return the iLblFirstPageRecords
	 */
	public SDSImageLabel getILblFirstPageRecords() {
		return iLblFirstPageRecords;
	}

	/**
	 * @param btnFirstPageRecords the btnFirstPageRecords to set
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
	 * @param iLblPreviousRecords the btnPreviousRecords to set
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
	 * @return the btnLastPageRecords
	 */
	public SDSImageLabel getILblLastPageRecords() {
		return iLblLastPageRecords;
	}

	/**
	 * @param iLblLastPageRecords the btnLastPageRecords to set
	 */
	public void setBtnLastPageRecords(SDSImageLabel iLblLastPageRecords) {
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
	 * @return the batchNoComparator
	 */
	public BatchNoComparator getBatchNoComparator() {
		return batchNoComparator;
	}

	/**
	 * @param batchNoComparator the batchNoComparator to set
	 */
	public void setBatchNoComparator(BatchNoComparator batchNoComparator) {
		this.batchNoComparator = batchNoComparator;
	}

	private void createImages() {
		buttonImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
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
