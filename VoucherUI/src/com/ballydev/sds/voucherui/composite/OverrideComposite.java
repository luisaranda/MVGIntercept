/*****************************************************************************
 * $Id: OverrideComposite.java,v 1.10, 2010-11-19 11:43:57Z, Verma, Nitin Kumar$
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.ballydev.sds.cdatepicker.ACW;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.TableColumnDescriptor;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.ThemeManager;
import com.ballydev.sds.framework.constant.IConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSDatePicker;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSTableViewer;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.ErrorCodeEnum;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.DateHelper;
import com.ballydev.sds.voucherui.util.ResultComparator;
import com.ballydev.sds.voucherui.util.Text;
import com.ballydev.sds.voucherui.util.TicketTimestampComparator;

/**
 * @author VNitinkumar
 */
public class OverrideComposite extends Composite {

	/**
	 * Instance of the main composite
	 */
	private Composite mainComposite = null;

	/**
	 * Instance of the button composite
	 */
	private Composite btnFirstComposite = null;

	/**
	 * Instance of the button composite
	 */
	private Composite btnSecondComposite = null;

	/**
	 * Instance of the display table composite
	 */
	private Composite dispTableComposite = null;

	/**
	 * Instance of the barcode text
	 */
	private SDSTSText txtBarcode = null;

	/**
	 * Instance of the barcode label
	 */
	private CbctlMandatoryLabel lblBarcode = null;

	/**
	 * Instance of the amount text
	 */
	private SDSTSText txtAmount = null;

	/**
	 * Instance of the amount label
	 */
	private CbctlMandatoryLabel lblAmount = null;

	/**
	 * Instance of the asset text
	 */
	private SDSTSText txtAsset = null;

	/**
	 * Instance of the asset label
	 */
	private CbctlMandatoryLabel lblAsset = null;

	/**
	 * Instance of the playerId text
	 */
	private SDSTSText txtPlayerId = null;

	/**
	 * Instance of the playerId label
	 */
	private CbctlLabel lblPlayerId = null;

	/**
	 * Instance of the date time text
	 */
	private SDSDatePicker ticketCreatedTime = null;

	/**
	 * Instance of the date time label
	 */
	private CbctlMandatoryLabel lblTktCreatedTime = null;

	/**
	 * Instance of the table descriptor
	 */
	private TableDescriptor tblTicketsDetail = null;

	/**
	 * Instance of the the table viewer
	 */
	private SDSTSTableViewer tableViewer = null;

	/**
	 * Display First Page Records Image Label instance
	 */
	private SDSImageLabel iLblFirstPageRecords = null;

	/**
	 * Display Previous Records Image Label instance
	 */
	private SDSImageLabel iLblPreviousRecords = null;

	/**
	 * Display Next Records Image Label instance
	 */
	private SDSImageLabel iLblNextRecords = null;

	/**
	 * Display Last Page Records Image Label instance
	 */
	private SDSImageLabel iLblLastPageRecords = null;

	/**
	 * Instance of the total page count label
	 */
	private SDSTSLabel lblTotalPageCount = null;

	/**
	 * Instance of the Submit Image Label
	 */
	private SDSImageLabel imgLblSubmit;
	
	/**
	 * Instance of the Delete Image Label
	 */
	private SDSImageLabel imgLblDelete;
	

	/**
	 * Instance of the total page count label
	 */
	private SDSTSLabel lblTotalTktAmount = null;

	/**
	 * Instance of the Authorize Image Label
	 */
	private SDSImageLabel imgLblAuthorise;
	
	private Image buttonBG;
	
	private Image buttonImage;

	private Image buttonDisableImage;

	/**
	 * @param parent
	 * @param style
	 */
	public OverrideComposite(Composite parent, int style) {
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

		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_OVERRIDE_VOUCHER_HEADER));
		createOverrideComposite();
		createBtnComposite();
		createTable();
		tableNavigationComposite();
		createSecondBtnComposite();
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		this.setTabList(new Control[]{mainComposite});
	}

	/** Main Composite */
	private void createOverrideComposite() {
		GridData gdMainComposite = new GridData();
		gdMainComposite.grabExcessHorizontalSpace = true;
		gdMainComposite.verticalAlignment = GridData.CENTER;
		gdMainComposite.horizontalAlignment = GridData.CENTER;
		
		if(Util.isSmallerResolution()) {
			gdMainComposite.verticalIndent = 0;
			gdMainComposite.horizontalIndent = 0;
		}
		else {
			gdMainComposite.verticalIndent = 5;
			gdMainComposite.horizontalIndent = 50;
		}

		GridLayout grlMainComposite = new GridLayout();
		grlMainComposite.numColumns = 4;
		
		if( Util.isSmallerResolution() ) {
			grlMainComposite.horizontalSpacing = 40;
			grlMainComposite.verticalSpacing = 2;			
		}
		else {
			grlMainComposite.horizontalSpacing = 80;
			grlMainComposite.verticalSpacing = 5;			
		}

		mainComposite = new Composite(this,SWT.NONE);
		mainComposite.setLayout(grlMainComposite);
		mainComposite.setLayoutData(gdMainComposite);

		lblBarcode = new CbctlMandatoryLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_BARCODE));
		lblBarcode.setBackground(SDSControlFactory.getTSBodyColor());
		lblBarcode.getLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtBarcode = new SDSTSText(mainComposite, SWT.NONE | SWT.RIGHT, IVoucherConstants.OVERRIDEVOUCHER_CTRL_TXT_TICKETBARCODE, IVoucherConstants.OVERRIDEVOUCHER_FRM_TXT_TICKETBARCODE, false);
		txtBarcode.setNumeric(true);
		txtBarcode.setTextLimit(18);

		lblAmount = new CbctlMandatoryLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_AMOUNT));
		lblAmount.setBackground(SDSControlFactory.getTSBodyColor());
		lblAmount.getLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtAmount = new SDSTSText(mainComposite, SWT.NONE | SWT.RIGHT, IVoucherConstants.OVERRIDEVOUCHER_CTRL_TXT_TICKET_AMOUNT, IVoucherConstants.OVERRIDEVOUCHER_FRM_TXT_TICKET_AMOUNT, false);
		txtAmount.setTextLimit(10);
		txtAmount.setNumeric(true);
		txtAmount.setAmountField(true);

		lblAsset = new CbctlMandatoryLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ASSET_NUMBER));
		lblAsset.setBackground(SDSControlFactory.getTSBodyColor());
		lblAsset.getLabel().setBackground(SDSControlFactory.getTSBodyColor());
		
		txtAsset = new SDSTSText(mainComposite, SWT.NONE | SWT.RIGHT, IVoucherConstants.OVERRIDEVOUCHER_CTRL_TXT_TICKET_CREATED_ASSET, IVoucherConstants.OVERRIDEVOUCHER_FRM_TXT_TICKET_CREATED_ASSET, false);
		txtAsset.setNumeric(true);
		txtAsset.setTextLimit(5);

		lblPlayerId = new CbctlLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_PLAYER_ID));
		lblPlayerId.setBackground(SDSControlFactory.getTSBodyColor());
		
		txtPlayerId = new SDSTSText(mainComposite, SWT.NONE | SWT.RIGHT, IVoucherConstants.OVERRIDEVOUCHER_CTRL_TXT_TICKET_PLAYER_ID, IVoucherConstants.OVERRIDEVOUCHER_FRM_TXT_TICKET_PLAYER_ID, false);
		txtPlayerId.setTextLimit(10);
		txtPlayerId.setNumeric(true);
		txtPlayerId.setTextLimitChkEnabled(true);

		lblTktCreatedTime = new CbctlMandatoryLabel(mainComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_CREATED_TIME));
	
		createDateFromTime();
		
//		createBtnComposite(mainComposite);
	}

	/**
	 * This method initializes Ticket Created DateTime	
	 */
	private void createDateFromTime() {
		GridData gdStartTime = new GridData();
		gdStartTime.heightHint = 25;
		gdStartTime.grabExcessHorizontalSpace = false;
		gdStartTime.horizontalAlignment = GridData.CENTER;
		gdStartTime.verticalAlignment = GridData.CENTER;
		gdStartTime.widthHint = 160;
		ticketCreatedTime = new SDSDatePicker(mainComposite, ACW.FOOTER | ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.TIME_CUSTOM | ACW.DIGITAL_CLOCK,
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_CREATED_TIME),IVoucherConstants.OVERRIDEVOUCHER_FRM_TXT_TICKET_CREATED_TIME);
		ticketCreatedTime.setLayoutData(gdStartTime);
	}

	private void createBtnComposite() {
		
		GridData gdBtnComposite = new GridData();
		gdBtnComposite.grabExcessHorizontalSpace = true;
		gdBtnComposite.grabExcessVerticalSpace = true;
		gdBtnComposite.verticalAlignment = GridData.CENTER;
		gdBtnComposite.horizontalAlignment = GridData.CENTER;

		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 1;
		grlButtonComposite.marginWidth = 4;
		grlButtonComposite.horizontalSpacing = 5;
		
		if( Util.isSmallerResolution() ) {
			grlButtonComposite.marginHeight = 1;
			grlButtonComposite.verticalSpacing = 1;
		}
		else {
			grlButtonComposite.marginHeight = 15;
			grlButtonComposite.verticalSpacing = 5;	
		}


		btnFirstComposite = new Composite(this, SWT.NONE);
		btnFirstComposite.setLayout(grlButtonComposite);
		btnFirstComposite.setLayoutData(gdBtnComposite);
//		btnFirstComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		GridData gridData4 = new GridData();
		gridData4.heightHint = buttonBG.getBounds().height;
		gridData4.widthHint = buttonBG.getBounds().width;
		gridData4.horizontalAlignment = GridData.END;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;

		imgLblSubmit = new SDSImageLabel(btnFirstComposite, SWT.CENTER, buttonBG, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_ACCEPT), IVoucherConstants.OVERRIDEVOUCHER_CTRL_BTN_SUBMIT);
		imgLblSubmit.getTextLabel().setData("btnSubmit");
		imgLblSubmit.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			imgLblSubmit.setLayoutData(gridData4);
		}
		imgLblSubmit.setFont(SDSControlFactory.getDefaultBoldFont());
		imgLblSubmit.setBackgroundImage(buttonBG);
		imgLblSubmit.setBounds(buttonBG.getBounds());
	}

	/**
	 * This method creates the table
	 */
	private void createTable() {
		GridData gdDispTableComposite = new GridData();
		gdDispTableComposite.grabExcessHorizontalSpace = true;
		gdDispTableComposite.horizontalSpan = 1;
		gdDispTableComposite.horizontalAlignment = GridData.CENTER;
		gdDispTableComposite.verticalAlignment = GridData.FILL;
		gdDispTableComposite.grabExcessVerticalSpace = true;

		if( Util.isSmallerResolution() ) {
			gdDispTableComposite.heightHint = 170;
		} else if( Util.is1024X786Resolution() ) {
			gdDispTableComposite.heightHint = 200;
		} else {
			gdDispTableComposite.heightHint = 400;
			gdDispTableComposite.widthHint = 950;
		}

		dispTableComposite = new Composite(this, SWT.BORDER);
		dispTableComposite.setLayout(new FillLayout());
		dispTableComposite.setLayoutData(gdDispTableComposite);
		dispTableComposite.setBackground(SDSControlFactory.getTSBodyColor());
		tblTicketsDetail = new TableDescriptor(dispTableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL, 
				IVoucherConstants.OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SUMMARY, IVoucherConstants.OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SUMMARY);

		tblTicketsDetail.setSelItemFormProperty(IVoucherConstants.OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SEL_ITEM);
		tblTicketsDetail.setHeaderVisible(true);
		tblTicketsDetail.setLinesVisible(true);

		ArrayList<TableColumnDescriptor> arrayListTblColDesc = addColumns();

		tblTicketsDetail.setContentProvider(new TicketContentProvider());
		tblTicketsDetail.setLabelProvider(new TicketDataProvider());
		tableViewer = Util.createTSTable(tblTicketsDetail);
		tblTicketsDetail.getTable().setData("name", IVoucherConstants.OVERRIDEVOUCHER_CTRL_TABLE_TICKET_SUMMARY);

		if( Util.isSmallerResolution() ) {
			tableViewer.getTable().setFont(ThemeManager.getTableSmallHeaderFont());
		}
		tableViewer.getControl().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				int clientWidth = tableViewer.getTable().getClientArea().width;
				event.height = event.gc.getFontMetrics().getHeight() * 2;
				event.width = clientWidth * 2;
			}
		});

		for (int i = 0; i < arrayListTblColDesc.size(); i++) {

			if( Util.isSmallerResolution() ) {
				tblTicketsDetail.getTableColsDescriptors().get(i).getTableColumn().setResizable(true);
			} else {
				tblTicketsDetail.getTableColsDescriptors().get(i).getTableColumn().setResizable(false);
			}
		}
	}
	
	private ArrayList<TableColumnDescriptor> addColumns() {
		ArrayList<TableColumnDescriptor> arrayListTblColDesc = new ArrayList<TableColumnDescriptor>();


		TableColumnDescriptor columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .00,	"");
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .160, LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_BARCODE));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new TicketTimestampComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .100, LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_AMOUNT));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .120, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ASSET_NUMBER));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .180, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_CREATED_TIME));
		arrayListTblColDesc.add(columnDescriptor);
		
		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .180, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_RESULT),true, SWT.DOWN);
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new ResultComparator());
		
		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .100, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_PLAYER_ID));
		arrayListTblColDesc.add(columnDescriptor);
		
		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .160, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_STATUS));
		arrayListTblColDesc.add(columnDescriptor);
		
		tblTicketsDetail.setTableColsDescriptors(arrayListTblColDesc);
		return arrayListTblColDesc;
	}

	class TicketContentProvider implements IStructuredContentProvider {

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((List) inputElement).toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class TicketDataProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object dto, int columnIndex) {
			TicketInfoDTO ticketInfoDTO = (TicketInfoDTO) dto;
			switch (columnIndex) {
			case 1:
				return ticketInfoDTO.getBarcode();
			case 2:
				String amount = "INVALID";
				try {
					amount = Text.convertDollar((long)(ticketInfoDTO.getAmount()));
				} catch (Throwable t) {
					// eat up
				}
				return amount;
			case 3:
				return ticketInfoDTO.getAcnfNumber();
			case 4:
				SessionUtility utility = new SessionUtility();
				String datePattern = utility.getApplicationDateFormat();
				String trDate = DateUtil.convertDateToString(ticketInfoDTO.getEffectiveDate());
				String createdDate = DateHelper.getFormatedDate(trDate, IConstants.DATE_FORMAT, datePattern);
				return createdDate;
			case 5:
				return getResultOfTicket(ticketInfoDTO);
			case 6:
				return getPlayerId(ticketInfoDTO);
			case 7:
				return LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_OVERRIDE_REASON);
			default:
				break;
			}
			return null;
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
	
	private void tableNavigationComposite(){

		GridLayout grlPageNavComposite = new GridLayout();
		grlPageNavComposite.numColumns = 6;
		grlPageNavComposite.verticalSpacing = 5;
		grlPageNavComposite.marginWidth = 0;
		grlPageNavComposite.marginHeight = 5;
		grlPageNavComposite.makeColumnsEqualWidth = false;
		grlPageNavComposite.horizontalSpacing = 15;

		if( Util.isSmallerResolution() ) {
			grlPageNavComposite.verticalSpacing = 0;
			grlPageNavComposite.marginWidth = 0;
			grlPageNavComposite.marginHeight = 0;
			grlPageNavComposite.makeColumnsEqualWidth = false;
			grlPageNavComposite.horizontalSpacing = 20;
		}

		GridData gdPageNavComposite = new GridData();
		gdPageNavComposite.grabExcessHorizontalSpace = true;
		gdPageNavComposite.horizontalAlignment = GridData.CENTER;
		gdPageNavComposite.verticalAlignment = GridData.FILL;
		gdPageNavComposite.heightHint = 56;
		
		gdPageNavComposite.grabExcessVerticalSpace = false;

		if( Util.isSmallerResolution() ) {
			gdPageNavComposite.widthHint = -1;
			gdPageNavComposite.heightHint = 36;
			gdPageNavComposite.horizontalIndent = 0;
		} else 
			gdPageNavComposite.widthHint = 955;

		GridData gdLbl = new GridData();
		gdLbl.grabExcessHorizontalSpace = true;
		gdLbl.verticalAlignment = GridData.CENTER;
		gdLbl.horizontalAlignment = GridData.BEGINNING;
		

		if( Util.isSmallerResolution() ) {
			gdLbl.widthHint = 400;
			gdLbl.horizontalIndent = 25;
		}
		else {
			gdLbl.horizontalIndent = 60;
			gdLbl.widthHint = 420;
		}
		
		GridData gdPageNavButtons = new GridData();
		gdPageNavButtons.grabExcessHorizontalSpace = false;
		gdPageNavButtons.heightHint = -1;
		gdPageNavButtons.widthHint = -1;
		gdPageNavButtons.horizontalAlignment = GridData.CENTER;
		gdPageNavButtons.verticalAlignment = GridData.FILL;
		gdPageNavButtons.grabExcessVerticalSpace = true;
		gdPageNavButtons.horizontalIndent = 5;
		gdPageNavButtons.verticalIndent = 0;

		GridData gdPageCount = new GridData();
		gdPageCount.grabExcessHorizontalSpace = true;
		gdPageCount.horizontalAlignment = GridData.CENTER;
		gdPageCount.heightHint = 15;
		gdPageCount.widthHint = 30;
		gdPageCount.verticalAlignment = GridData.CENTER;
		gdPageCount.grabExcessVerticalSpace = true;
		gdPageCount.horizontalIndent = 5;

		Composite pageNavComposite = new Composite(this, SWT.NONE);
//		pageNavComposite.setBackground(new Color(Display.getCurrent(), 137, 126, 213));
		pageNavComposite.setLayout(grlPageNavComposite);
		pageNavComposite.setLayoutData(gdPageNavComposite);

		lblTotalTktAmount = new CbctlLabel(pageNavComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKETS_TOTAL_AMOUNT));
		lblTotalTktAmount.setBackground(SDSControlFactory.getTSBodyColor());
		lblTotalTktAmount.setLayoutData(gdLbl);

		iLblFirstPageRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(),
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_FIRST_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_FIRST_ARROW);
		iLblFirstPageRecords.setLayoutData(gdPageNavButtons);

		iLblPreviousRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(),
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_PREVIOUS_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_PREVIOUS_ARROW);
		iLblPreviousRecords.setLayoutData(gdPageNavButtons);

		lblTotalPageCount = new SDSTSLabel(pageNavComposite, SWT.CENTER);
		lblTotalPageCount.setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.DISPLAY_TOTAL_PAGE_COUNT));
		lblTotalPageCount.setLayoutData(gdPageCount);

		iLblNextRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(),
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_NEXT_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_NEXTARROW_ARROW);
		iLblNextRecords.setLayoutData(gdPageNavButtons);

		iLblLastPageRecords = new SDSImageLabel(pageNavComposite, SWT.NONE, new Image(Display.getCurrent(),
				getClass().getResourceAsStream(IImageConstants.DISABLED_DISPLAY_LAST_PAGE)), "", IVoucherConstants.RECON_CTRL_BTN_LAST_ARROW);
		iLblLastPageRecords.setLayoutData(gdPageNavButtons);

	}

	private void createSecondBtnComposite() {
		
		GridData gdBtnComposite = new GridData();
		gdBtnComposite.grabExcessHorizontalSpace = true;
		gdBtnComposite.grabExcessVerticalSpace = true;
		gdBtnComposite.verticalAlignment = GridData.CENTER;
		gdBtnComposite.horizontalAlignment = GridData.CENTER;

		GridLayout grlButtonComposite = new GridLayout();
		grlButtonComposite.numColumns = 3;
		grlButtonComposite.marginWidth = 4;
		grlButtonComposite.horizontalSpacing = 5;
		
		if( Util.isSmallerResolution()) {
			grlButtonComposite.marginHeight = 0;
			grlButtonComposite.verticalSpacing = 0;
		} else {
			grlButtonComposite.marginHeight = 15;
			grlButtonComposite.verticalSpacing = 5;
		}

		btnSecondComposite = new Composite(this, SWT.NONE);
		btnSecondComposite.setLayout(grlButtonComposite);
		btnSecondComposite.setLayoutData(gdBtnComposite);
//		btnSecondComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		GridData gridData4 = new GridData();
		gridData4.heightHint = buttonBG.getBounds().height;
		gridData4.widthHint = buttonBG.getBounds().width;
		gridData4.horizontalAlignment = GridData.END;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;

		imgLblDelete = new SDSImageLabel(btnSecondComposite, SWT.CENTER, buttonBG, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_REMOVE), IVoucherConstants.OVERRIDEVOUCHER_CTRL_BTN_DELETE);
		imgLblDelete.getTextLabel().setData("btnDelete");
		imgLblDelete.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			imgLblDelete.setLayoutData(gridData4);
		}
		imgLblDelete.setFont(SDSControlFactory.getDefaultBoldFont());
		imgLblDelete.setBackgroundImage(buttonBG);
		imgLblDelete.setBounds(buttonBG.getBounds());

		imgLblAuthorise = new SDSImageLabel(btnSecondComposite, SWT.CENTER, buttonBG, 
				LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_AUTHORISE), IVoucherConstants.OVERRIDEVOUCHER_CTRL_BTN_AUTHORISE);
		imgLblAuthorise.getTextLabel().setData("btnAuthorise");
		imgLblAuthorise.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if( !Util.isSmallerResolution() ) {
			imgLblAuthorise.setLayoutData(gridData4);
		}
		imgLblAuthorise.setFont(SDSControlFactory.getDefaultBoldFont());
		imgLblAuthorise.setBackgroundImage(buttonBG);
		imgLblAuthorise.setBounds(buttonBG.getBounds());
		
	}

	/**
	 * This method sets the default control grid properties
	 */
	private void setControlProperties(){
		try	{
			SDSControlFactory.setTouchScreenLabelProperties(lblBarcode.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtBarcode);
			SDSControlFactory.setTouchScreenLabelProperties(lblAmount.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtAmount);
			SDSControlFactory.setTouchScreenLabelProperties(lblAsset.getLabel());
			SDSControlFactory.setTouchScreenTextProperties(txtAsset);
			SDSControlFactory.setTouchScreenLabelProperties(lblTktCreatedTime.getLabel());
			SDSControlFactory.setTouchScreenLabelProperties(lblTotalTktAmount);
			SDSControlFactory.setTouchScreenTextProperties(txtPlayerId);

			if( Util.isSmallerResolution() ) {

				GridData gdBarcode = (GridData) txtBarcode.getLayoutData();
				gdBarcode.widthHint  = 140;
				gdBarcode.heightHint = 19;
				txtBarcode.setLayoutData(gdBarcode);
				
				GridData gdTextAmt = (GridData) txtAmount.getLayoutData();
				gdTextAmt.widthHint  = 140;
				gdTextAmt.heightHint = 19;
				txtAmount.setLayoutData(gdTextAmt);

				GridData gdAsset = (GridData) txtAsset.getLayoutData();
				gdAsset.widthHint  = 140;
				gdAsset.heightHint = 19;
				txtAsset.setLayoutData(gdAsset);
				
				GridData gdPlayerId = (GridData) txtPlayerId.getLayoutData();
				gdPlayerId.widthHint  = 140;
				gdPlayerId.heightHint = 19;
				txtPlayerId.setLayoutData(gdPlayerId);
				
				GridData gdCreatedDate = (GridData) ticketCreatedTime.getLayoutData();
				gdCreatedDate.widthHint  = 170;
				gdCreatedDate.heightHint = 22;
				ticketCreatedTime.setLayoutData(gdCreatedDate);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createImages() {
		buttonBG = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonImage = new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
		buttonDisableImage = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE_DISABLED));
	}

	/**
	 * @return the txtBarcode
	 */
	public SDSTSText getTxtBarcode() {
		return txtBarcode;
	}

	/**
	 * @param txtBarcode the txtBarcode to set
	 */
	public void setTxtBarcode(SDSTSText txtBarcode) {
		this.txtBarcode = txtBarcode;
	}

	/**
	 * @return the lblBarcode
	 */
	public CbctlMandatoryLabel getLblBarcode() {
		return lblBarcode;
	}

	/**
	 * @param lblBarcode the lblBarcode to set
	 */
	public void setLblBarcode(CbctlMandatoryLabel lblBarcode) {
		this.lblBarcode = lblBarcode;
	}

	/**
	 * @return the txtAmount
	 */
	public SDSTSText getTxtAmount() {
		return txtAmount;
	}

	/**
	 * @param txtAmount the txtAmount to set
	 */
	public void setTxtAmount(SDSTSText txtAmount) {
		this.txtAmount = txtAmount;
	}

	/**
	 * @return the lblAmount
	 */
	public CbctlMandatoryLabel getLblAmount() {
		return lblAmount;
	}

	/**
	 * @param lblAmount the lblAmount to set
	 */
	public void setLblAmount(CbctlMandatoryLabel lblAmount) {
		this.lblAmount = lblAmount;
	}

	/**
	 * @return the txtAsset
	 */
	public SDSTSText getTxtAsset() {
		return txtAsset;
	}

	/**
	 * @param txtAsset the txtAsset to set
	 */
	public void setTxtAsset(SDSTSText txtAsset) {
		this.txtAsset = txtAsset;
	}

	/**
	 * @return the lblAsset
	 */
	public CbctlMandatoryLabel getLblAsset() {
		return lblAsset;
	}

	/**
	 * @param lblAsset the lblAsset to set
	 */
	public void setLblAsset(CbctlMandatoryLabel lblAsset) {
		this.lblAsset = lblAsset;
	}

	/**
	 * @return the ticketCreatedTime
	 */
	public SDSDatePicker getTicketCreatedTime() {
		return ticketCreatedTime;
	}

	/**
	 * @param ticketCreatedTime the ticketCreatedTime to set
	 */
	public void setTicketCreatedTime(SDSDatePicker ticketCreatedTime) {
		this.ticketCreatedTime = ticketCreatedTime;
	}

	/**
	 * @return the lblTktCreatedTime
	 */
	public CbctlMandatoryLabel getLblTktCreatedTime() {
		return lblTktCreatedTime;
	}

	/**
	 * @param lblTktCreatedTime the lblTktCreatedTime to set
	 */
	public void setLblTktCreatedTime(CbctlMandatoryLabel lblTktCreatedTime) {
		this.lblTktCreatedTime = lblTktCreatedTime;
	}

	/**
	 * @return the txtPlayerId
	 */
	public SDSTSText getTxtPlayerId() {
		return txtPlayerId;
	}

	/**
	 * @param txtPlayerId the txtPlayerId to set
	 */
	public void setTxtPlayerId(SDSTSText txtPlayerId) {
		this.txtPlayerId = txtPlayerId;
	}

	/**
	 * @return the imgLblSubmit
	 */
	public SDSImageLabel getImgLblSubmit() {
		return imgLblSubmit;
	}

	/**
	 * @param imgLblSubmit the imgLblSubmit to set
	 */
	public void setImgLblSubmit(SDSImageLabel imgLblSubmit) {
		this.imgLblSubmit = imgLblSubmit;
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
	 * @return the iLblFirstPageRecords
	 */
	public SDSImageLabel getILblFirstPageRecords() {
		return iLblFirstPageRecords;
	}

	/**
	 * @param lblFirstPageRecords the iLblFirstPageRecords to set
	 */
	public void setILblFirstPageRecords(SDSImageLabel lblFirstPageRecords) {
		iLblFirstPageRecords = lblFirstPageRecords;
	}

	/**
	 * @return the iLblPreviousRecords
	 */
	public SDSImageLabel getILblPreviousRecords() {
		return iLblPreviousRecords;
	}

	/**
	 * @param lblPreviousRecords the iLblPreviousRecords to set
	 */
	public void setILblPreviousRecords(SDSImageLabel lblPreviousRecords) {
		iLblPreviousRecords = lblPreviousRecords;
	}

	/**
	 * @return the iLblNextRecords
	 */
	public SDSImageLabel getILblNextRecords() {
		return iLblNextRecords;
	}

	/**
	 * @param lblNextRecords the iLblNextRecords to set
	 */
	public void setILblNextRecords(SDSImageLabel lblNextRecords) {
		iLblNextRecords = lblNextRecords;
	}

	/**
	 * @return the iLblLastPageRecords
	 */
	public SDSImageLabel getILblLastPageRecords() {
		return iLblLastPageRecords;
	}

	/**
	 * @param lblLastPageRecords the iLblLastPageRecords to set
	 */
	public void setILblLastPageRecords(SDSImageLabel lblLastPageRecords) {
		iLblLastPageRecords = lblLastPageRecords;
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
	 * @return the imgLblDelete
	 */
	public SDSImageLabel getImgLblDelete() {
		return imgLblDelete;
	}

	/**
	 * @param imgLblDelete the imgLblDelete to set
	 */
	public void setImgLblDelete(SDSImageLabel imgLblDelete) {
		this.imgLblDelete = imgLblDelete;
	}

	/**
	 * @return the imgLblAuthorise
	 */
	public SDSImageLabel getImgLblAuthorise() {
		return imgLblAuthorise;
	}

	/**
	 * @param imgLblAuthorise the imgLblAuthorise to set
	 */
	public void setImgLblAuthorise(SDSImageLabel imgLblAuthorise) {
		this.imgLblAuthorise = imgLblAuthorise;
	}

	/**
	 * @return the tblTicketsDetail
	 */
	public TableDescriptor getTblTicketsDetail() {
		return tblTicketsDetail;
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

	/**
	 * @return the lblTotalTktAmount
	 */
	public SDSTSLabel getLblTotalTktAmount() {
		return lblTotalTktAmount;
	}

	private String getResultOfTicket(TicketInfoDTO tktInfoDTO) {
		String result = "";
		
		//Success
		if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.SUCCESS.getErrorCode() ) {
			result = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_SUCCESS);
		//Errors
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_INVALID_LOCATION.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_INVALID_LOCATION);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_CRC_CHECK_FAILED.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_CRC_CHECK_FAILED);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_RESTRICTED_LOCATION.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_RESTRICTED_LOCATION);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_MAX_REDEEMED_AMOUNT_EXCEEDED.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_MAX_REDEEMED_AMOUNT_EXCEEDED);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.UNKNOWN_APPLICATION_ERROR.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_UNKNOWN_APPLICATION_ERROR);

		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_ALREADY_REDEEMED.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_TICKET_ALREADY_REDEEMED);
		//Warnings
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_UNKNOWN_ERROR.getErrorCode() ) {
			result = "* " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_UNKNOWN_APPLICATION_ERROR);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_PREVIOUS_TICKET_OUT_OF_SEQUENCE.getErrorCode() ) {
			result = "! " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_PREV_TICKET_OUT_OF_SEQUENCE);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_NEXT_TICKET_OUT_OF_SEQUENCE.getErrorCode() ) {
			result = "! " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_NEXT_TICKET_OUT_OF_SEQUENCE);
		
		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_PLAYER_ID_IN_MULTIPLE_OVERRIDES.getErrorCode() ) {
			result = "! " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_PLAYER_ID_IN_MULTIPLE_OVERRIDES);

		} else if( tktInfoDTO.getErrorCodeId() == ErrorCodeEnum.OVERRIDE_ALREADY_EXISTS.getErrorCode()) {
			result = "! " + LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_VOU_TICKET_ALREADY_EXISTS);
		}
		return result;
	}
	
	private String getPlayerId( TicketInfoDTO ticketInfoDTO) {
		String playerId = IVoucherConstants.DEFAULT_PLAYER_ID;
		if( ticketInfoDTO.getPlayerId() == null || ticketInfoDTO.getPlayerId().equals("") ) {
			playerId = IVoucherConstants.DEFAULT_PLAYER_ID;
		} else {
			playerId = ticketInfoDTO.getPlayerId();
		}
		return playerId;
	}
}
