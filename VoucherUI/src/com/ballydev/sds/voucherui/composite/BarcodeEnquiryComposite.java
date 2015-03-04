/*****************************************************************************
 *	 Copyright (c) 2006 Bally Technology  1977 - 2007
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

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.TableColumnDescriptor;
import com.ballydev.sds.framework.TableDescriptor;
import com.ballydev.sds.framework.ThemeManager;
import com.ballydev.sds.framework.constant.IConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.SDSTableViewer;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucher.dto.TransactionListDTO;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.ControlConstants;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.DateHelper;
import com.ballydev.sds.voucherui.util.EnquireLocationCodeComparator;
import com.ballydev.sds.voucherui.util.EnquirePlayerIdComparator;
import com.ballydev.sds.voucherui.util.EnquireTimestampComparator;

/**
 * This class creates the redeem voucher composite
 * @author Nithya kalyani R
 * @version $Revision: 57$
 */
public class BarcodeEnquiryComposite extends Composite implements IDBLabelKeyConstants,IVoucherConstants {

	/**
	 * Instance of the sub composite
	 */
	private Composite subComposite = null;

	/**
	 * Instance of the barcode label
	 */
	private CbctlMandatoryLabel lblBarCode = null;

	/**
	 * Instance of the barcode text
	 */
	private SDSTSText txtBarcode = null;

	/**
	 * Instance of the amount label
	 */
	private CbctlLabel lblAmount = null;

	/**
	 * Instance of the amount text
	 */
	private SDSTSText txtAmount = null;

	/**
	 * Instance of the amount label
	 */
	private CbctlLabel lblEAmount = null;

	/**
	 * Instance of the amount text
	 */
	private SDSTSText txtEAmount = null;

	/**
	 * Instance of the state label
	 */
	private CbctlLabel lblState = null;

	/**
	 * Instance of the state text
	 */
	private SDSTSText txtState = null;

	/**
	 * Instance of the effective date label
	 */
	private CbctlLabel lblEffectiveDate = null;

	/**
	 * Instance of the effective date text
	 */
	private SDSTSText txtEffectiveDate = null;

	/**
	 * Instance of the expire date label
	 */
	private CbctlLabel lblExpireDate = null;

	/**
	 * Instance of the expire date text
	 */
	private SDSTSText txtExpireDate = null;

	/**
	 * Instance of the location label
	 */
	private CbctlLabel lblOffline = null;

	/**
	 * Instance of the location text
	 */
	private SDSTSText txtOffline = null;

	/**
	 * Instance of the player id label
	 */
	private CbctlLabel lblTktType = null;

	/**
	 * Instance of the playerid text
	 */
	private SDSTSText txtTktType = null;

	/**
	 * Instance of the button composite
	 */
	private Composite btnComposite = null;

	/**
	 * Instance of the cancel button
	 */
	private SDSImageLabel iLblCancel = null;

	/**
	 * Instance of the submit barcode
	 */
	private SDSImageLabel iLblSubmitBarcode = null;

	/**
	 * Instance of the the table viewer
	 */
	private SDSTableViewer tableViewer = null;

	/**
	 * Instance of the table descriptor
	 */
	private TableDescriptor tblTransactionDetail = null;

	/**
	 * Instance of the display table composite
	 */
	private Composite dispTableComposite = null;

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
	 * Table Index instance
	 */
	private int tabindex;

	/**
	 * Instance of the total page count label
	 */
	private SDSTSLabel lblTotalPageCount = null;

	private Image buttonBG;


	/**
	 * Constructor of the class
	 */
	public BarcodeEnquiryComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		this.setBounds(0,0, 920,400);
		this.setBackground(SDSControlFactory.getTSBodyColor());
		layout();
	}

	private void initialize() {
		createImages();
		GridLayout grlPage = new GridLayout();
		grlPage.numColumns = 1;
		grlPage.verticalSpacing = 5;
		grlPage.marginHeight = 1;
		grlPage.marginWidth  = 1;
		grlPage.horizontalSpacing = 1;

		GridData gdPage = new GridData();
		gdPage.horizontalAlignment = GridData.FILL;
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;

		new HeaderComposite(this,SWT.NONE,LabelLoader.getLabelValue(LBL_ENQUIRE_VOUCHER)+ AppContextValues.getInstance().getTicketText());

		createSubComposite();
		createDetailsGroup();
		createTable();
		submitProcess();
		setControlProperties();
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
		this.setTabList(new Control[]{subComposite});
	}

	/**
	 * This method initializes subComposite
	 */
	private void createSubComposite() {
		GridData gdSubComp = new GridData();
		if(Util.isSmallerResolution()) {
			gdSubComp.widthHint = 770;
			gdSubComp.grabExcessHorizontalSpace = false;
		}else {
			gdSubComp.widthHint = 910;
			gdSubComp.grabExcessHorizontalSpace = true;
		}
		gdSubComp.verticalAlignment = GridData.CENTER;
		gdSubComp.grabExcessVerticalSpace = false;
		gdSubComp.horizontalAlignment = GridData.CENTER;

		GridLayout grlSubComp = new GridLayout();

		if( Util.isSmallerResolution() ) {
			grlSubComp.numColumns = 8;
		} else {
			grlSubComp.numColumns = 4;
		}
		grlSubComp.verticalSpacing = 10;
		grlSubComp.horizontalSpacing = 5;
		
		subComposite = new Composite(this, SWT.NONE);
		subComposite.setBackground(SDSControlFactory.getTSBodyColor());
		subComposite.setLayoutData(gdSubComp);
		subComposite.setLayout(grlSubComp);
//		subComposite.setBackground(new Color (Display.getCurrent(), 221,211,21));

		GridData gdData = new GridData();
		if(Util.is1024X786Resolution())
			gdData.horizontalIndent = 50;
		else
			gdData.horizontalIndent = 100;

		lblBarCode = new CbctlMandatoryLabel(subComposite,SWT.NONE,LabelLoader.getLabelValue(LBL_BAR_CODE));
		lblBarCode.setBackground(SDSControlFactory.getTSBodyColor());

		txtBarcode = new SDSTSText(subComposite, SWT.BORDER,REDEEMVOUCHER_CTRL_TXT_BARCODE, REDEEMVOUCHER_FRM_TXT_BARCODE);
		txtBarcode.setTextLimit(18);
		txtBarcode.setNumeric(true);
		txtBarcode.setTextLimitChkEnabled(true);
		if( !Util.isSmallerResolution() ) {
			txtBarcode.setLayoutData(gdData);
		}

		lblEAmount = new CbctlLabel(subComposite, SWT.NONE, AppContextValues.getInstance().getTicketText() + LabelLoader.getLabelValue(LBL_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		if( !Util.isSmallerResolution() ) {
			lblEAmount.setLayoutData(gdData);
		}

		txtEAmount = new SDSTSText(subComposite, SWT.BORDER | SWT.RIGHT,ENQURIYVOUCHER_CTRL_TXT_AMOUNT, ENQUIREVOUCHER_FRM_TXT_AMOUNT,true);
		//txtEAmount.setEditable(false);
		txtEAmount.setAmountField(true);
		txtEAmount.setTextLimit(25);
		if( !Util.isSmallerResolution() ) {
			txtEAmount.setLayoutData(gdData);
		}

		GridData gridData1 = new GridData();
		gridData1.heightHint = buttonBG.getBounds().height;
		gridData1.widthHint = buttonBG.getBounds().width;
		if( Util.isSmallerResolution() ) {
			gridData1.horizontalAlignment = GridData.CENTER;
			gridData1.horizontalIndent = 15;
		} else {
			gridData1.horizontalAlignment = GridData.END;
			gridData1.horizontalIndent = 150;
		}
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = false;
//		gridData1.horizontalIndent = 150;

		iLblSubmitBarcode = new SDSImageLabel(subComposite, SWT.NONE, buttonBG, LabelLoader.getLabelValue(LBL_SUBMIT_BARCODE), REDEEMVOUCHER_CTRL_BTN_SUBMIT_BARCODE);
		iLblSubmitBarcode.getTextLabel().setData(MULTIREDEEMVOUCHER_CTRL_BTN_CANCEL);
		iLblSubmitBarcode.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		iLblSubmitBarcode.setFont(SDSControlFactory.getDefaultBoldFont());
		if( !Util.isSmallerResolution() ) {
			iLblSubmitBarcode.setLayoutData(gridData1);
		}
		iLblSubmitBarcode.setBounds(buttonBG.getBounds());

		GridData gridData2 = new GridData();
		gridData2.heightHint = buttonBG.getBounds().height;
		gridData2.widthHint = buttonBG.getBounds().width;

		if( Util.isSmallerResolution() ) {
			gridData2.horizontalAlignment = GridData.CENTER;
			gridData2.horizontalIndent = 15;
		} else {
			gridData2.horizontalAlignment = GridData.END;
			gridData2.horizontalIndent = 90;
		}

		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = false;

		iLblCancel = new SDSImageLabel(subComposite, SWT.NONE, buttonBG, LabelLoader.getLabelValue(LBL_CANCEL), VOIDVOUCHER_CTRL_BTN_CANCEL);
		iLblCancel.getTextLabel().setData(MULTIREDEEMVOUCHER_CTRL_BTN_CANCEL);
		iLblCancel.getTextLabel().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		iLblCancel.setFont(SDSControlFactory.getDefaultBoldFont());
		iLblCancel.setBounds(buttonBG.getBounds());

		if( !Util.isSmallerResolution() ) {
			iLblCancel.setLayoutData(gridData2);
		}

		subComposite.setTabList(new Control[]{txtBarcode,txtEAmount,iLblSubmitBarcode,iLblCancel});
	}

	private void submitProcess(){

		GridLayout grlPageNavComposite = new GridLayout();
		grlPageNavComposite.numColumns = 5;
		grlPageNavComposite.verticalSpacing = 5;
		grlPageNavComposite.marginWidth = 585;
		grlPageNavComposite.marginHeight = 5;
		grlPageNavComposite.makeColumnsEqualWidth = true;
		grlPageNavComposite.horizontalSpacing = 15;

		if( Util.isSmallerResolution() ) {
			grlPageNavComposite.verticalSpacing = 0;
			grlPageNavComposite.marginWidth = 0;
			grlPageNavComposite.marginHeight = 0;
			grlPageNavComposite.makeColumnsEqualWidth = false;
			grlPageNavComposite.horizontalSpacing = 20;
		}
		if( Util.is1024X786Resolution() ) {
			grlPageNavComposite.verticalSpacing = 5;
			grlPageNavComposite.marginWidth = 5;
			grlPageNavComposite.marginHeight = 5;
			grlPageNavComposite.marginWidth = 265;
			grlPageNavComposite.makeColumnsEqualWidth = false;
			grlPageNavComposite.horizontalSpacing = 20;
		}
		GridData gdPageNavComposite = new GridData();
		gdPageNavComposite.grabExcessHorizontalSpace = true;
		gdPageNavComposite.horizontalAlignment = GridData.CENTER;
		gdPageNavComposite.verticalAlignment = GridData.FILL;
		gdPageNavComposite.heightHint = 56;
		gdPageNavComposite.widthHint = -1;
		gdPageNavComposite.grabExcessVerticalSpace = false;
		gdPageNavComposite.horizontalIndent = 55;

		if( Util.isSmallerResolution() ) {
			gdPageNavComposite.heightHint = 38;
			gdPageNavComposite.horizontalIndent = 410;
		}

		if( Util.is1024X786Resolution() ) {
			gdPageNavComposite.heightHint = 55;
			gdPageNavComposite.horizontalIndent = 260;
		}

		GridData gdPageNavButtons = new GridData();
		gdPageNavButtons.grabExcessHorizontalSpace = false;
		gdPageNavButtons.heightHint = -1;
		gdPageNavButtons.widthHint = -1;
		gdPageNavButtons.horizontalAlignment = GridData.CENTER;
		gdPageNavButtons.verticalAlignment = GridData.FILL;
		gdPageNavButtons.grabExcessVerticalSpace = true;
		gdPageNavButtons.horizontalIndent = 5;

		GridData gdPageCount = new GridData();
		gdPageCount.grabExcessHorizontalSpace = true;
		gdPageCount.horizontalAlignment = GridData.CENTER;
		gdPageCount.heightHint = 15;
		gdPageCount.widthHint = 150;
		gdPageCount.verticalAlignment = GridData.CENTER;
		gdPageCount.grabExcessVerticalSpace = true;
		gdPageCount.horizontalIndent = 5;

		Composite pageNavComposite = new Composite(this, SWT.NONE);
//		pageNavComposite.setBackground(new Color(Display.getCurrent(), 137, 126, 213));
		pageNavComposite.setLayout(grlPageNavComposite);
		pageNavComposite.setLayoutData(gdPageNavComposite);

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

	/**
	 * @return
	 */
	private ArrayList<TableColumnDescriptor> addColumns() {
		ArrayList<TableColumnDescriptor> arrayListTblColDesc = new ArrayList<TableColumnDescriptor>();


		TableColumnDescriptor columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .00,	"");
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .160, LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_DATE_TIME), true, SWT.UP);
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new EnquireTimestampComparator());
		
		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .13,	LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_ASSET_NUMBER));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .13,	LabelLoader.getLabelValue(IDBLabelKeyConstants.TKT_LOCATION));
		arrayListTblColDesc.add(columnDescriptor);
		
		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .20,	LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_TRANSACTION));
		arrayListTblColDesc.add(columnDescriptor);

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .13, LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_RESULT));
		arrayListTblColDesc.add(columnDescriptor);


		columnDescriptor = new TableColumnDescriptor(SWT.RIGHT, .10, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PLAYER_ID));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new EnquirePlayerIdComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .075, LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_SOURCE));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new EnquireLocationCodeComparator());

		columnDescriptor = new TableColumnDescriptor(SWT.LEFT, .07, LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_SITEID));
		arrayListTblColDesc.add(columnDescriptor);
		columnDescriptor.setComparator(new EnquireLocationCodeComparator());

		tblTransactionDetail.setTableColsDescriptors(arrayListTblColDesc);
		return arrayListTblColDesc;
	}

	class BatchContentProvider implements IStructuredContentProvider {

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((List) inputElement).toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class BatchDataProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object dto, int columnIndex) {
			TransactionListDTO transactionListDTO = (TransactionListDTO) dto;
			switch (columnIndex) {
			case 1:
				SessionUtility utility = new SessionUtility();
				String datePattern = utility.getApplicationDateFormat();
				//String trDate = DateUtil.getLocalTimeFromUTC(transactionListDTO.getDateTime()).toString();
				String trDate = DateUtil.convertDateToString(transactionListDTO.getDateTime());
				String transactionDate = DateHelper.getFormatedDate(trDate,IConstants.DATE_FORMAT,datePattern);
				return transactionDate;
			case 2:
				String assetNumber = null;
				if(transactionListDTO.getAssetNumber().trim()!=null && transactionListDTO.getAssetNumber().trim().length()>0 ) {
					if(transactionListDTO.getAssetNumber().trim().charAt(0)=='0'){
						assetNumber = StringUtil.trimAcnfNo(transactionListDTO.getAssetNumber().trim());
					}else{
						assetNumber = transactionListDTO.getAssetNumber().trim();
					}					
				}
				return assetNumber;
			case 3:
				String location = null;
				if(transactionListDTO.getLocation().trim()!=null && transactionListDTO.getLocation().trim().length()>0 ) {
					if(transactionListDTO.getLocation().trim().charAt(0)=='0'){
						location = StringUtil.trimAcnfNo(transactionListDTO.getLocation().trim());
					}else{
						location = transactionListDTO.getLocation().trim();
					}					
				}
				return location;
			case 4:
				return LabelLoader.getLabelValue(transactionListDTO.getTransactionReason());
			case 5:
				return LabelLoader.getLabelValue(transactionListDTO.getTransactionResult());
			case 6:
				return transactionListDTO.getPlayerId();
			case 7:
				return transactionListDTO.getSource();
			case 8:
				return Integer.toString(transactionListDTO.getSiteId());
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

	/**
	 * This method creates the table
	 */
	private void createTable() {
		GridData gdDispTableComposite = new GridData();
		gdDispTableComposite.grabExcessHorizontalSpace = true;
		gdDispTableComposite.horizontalSpan = 1;
		gdDispTableComposite.heightHint = -1;
		if(Util.isSmallerResolution()) {
			gdDispTableComposite.widthHint = 770;
		}
		if(Util.is1024X786Resolution()) {
			gdDispTableComposite.widthHint = 850;
		}
		gdDispTableComposite.horizontalAlignment = GridData.CENTER;
		gdDispTableComposite.verticalAlignment = GridData.FILL;
		gdDispTableComposite.grabExcessVerticalSpace = true;

		dispTableComposite = new Composite(this, SWT.BORDER);
		dispTableComposite.setLayout(new FillLayout());
		dispTableComposite.setLayoutData(gdDispTableComposite);
		dispTableComposite.setBackground(SDSControlFactory.getTSBodyColor());
		tblTransactionDetail = new TableDescriptor(dispTableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL, IVoucherConstants.ENQURIY_CTRL_TABLE_BATCH_SUMMARY, IVoucherConstants.ENQURIY_CTRL_TABLE_BATCH_SUMMARY);

		tblTransactionDetail.setSelItemFormProperty(IVoucherConstants.ENQURIY_CTRL_TABLE_BATCH_DETAIL_SEL_ITEM);
		tblTransactionDetail.setHeaderVisible(true);
		tblTransactionDetail.setLinesVisible(true);
//		tblTransactionDetail.setBackground(SDSControlFactory.getTSBodyColor());
		// Table Descriptor for the Table Viewer
		TableDescriptor dispBatchDetailsTblDescriptor = new TableDescriptor(dispTableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL, IVoucherConstants.ENQURIY_CTRL_TABLE_BATCH_DETAIL,"");
		dispBatchDetailsTblDescriptor.setHeaderVisible(true);
		dispBatchDetailsTblDescriptor.setLinesVisible(true);


		ArrayList<TableColumnDescriptor> arrayListTblColDesc = addColumns();

		tblTransactionDetail.setContentProvider(new BatchContentProvider());
		tblTransactionDetail.setLabelProvider(new BatchDataProvider());
		tableViewer = Util.createTable(tblTransactionDetail);
		tblTransactionDetail.getTable().setData("name", IVoucherConstants.ENQURIY_CTRL_TABLE_BATCH_SUMMARY);

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
				tblTransactionDetail.getTableColsDescriptors().get(i).getTableColumn().setResizable(true);
			} else {
				tblTransactionDetail.getTableColsDescriptors().get(i).getTableColumn().setResizable(false);
			}
		}
	}

	/**
	 * This method initializes detailsGroup
	 *
	 */
	private void createDetailsGroup() {

		Composite container = new Composite(this, SWT.NONE);
		container.setBackground(SDSControlFactory.getTSBodyColor());

		GridData gdDetailsGrp = new GridData();
		if(Util.isSmallerResolution()) {
			gdDetailsGrp.widthHint = 769;
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

		SDSTSLabel lblMessage = new SDSTSLabel(header, SWT.NONE, AppContextValues.getInstance().getTicketText() +LabelLoader.getLabelValue(LBL_VOUCHER_COUPON_DETAILS));

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

		Composite readOnlyComp = new Composite(container, SWT.NONE);
		readOnlyComp.setBackground(SDSControlFactory.getTSBodyColor());

		GridData gdReadOnly = new GridData();
		if(Util.isSmallerResolution()) {
			gdReadOnly.widthHint = 769;
		}
		gdReadOnly.grabExcessHorizontalSpace = true;
		gdReadOnly.grabExcessVerticalSpace = false;
		gdReadOnly.verticalAlignment = GridData.CENTER;

		if(Util.isSmallerResolution()) {
			gdReadOnly.horizontalAlignment = GridData.BEGINNING;
			gdReadOnly.horizontalIndent = 20;
		} else {
			gdReadOnly.horizontalAlignment = GridData.CENTER;
		}

		GridLayout grlReadOnly = new GridLayout();
		grlReadOnly.numColumns 		  = 4;
		grlReadOnly.verticalSpacing   = 5;

		if( Util.isSmallerResolution() || Util.is1024X786Resolution() ) {
			grlReadOnly.horizontalSpacing = 55;
		} else
			grlReadOnly.horizontalSpacing = 90;

		readOnlyComp.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		readOnlyComp.setLayout(grlReadOnly);
		readOnlyComp.setLayoutData(gdReadOnly);

		lblAmount = new CbctlLabel(readOnlyComp, SWT.NONE,AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(LBL_AMOUNT)+ " ("+CurrencyUtil.getCurrencySymbol()+")");
		txtAmount = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.RIGHT| SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_AMOUNT,REDEEMVOUCHER_FRM_TXT_AMOUNT,true);
		txtAmount.setAmountField(true);
		txtAmount.setEditable(false);
		txtAmount.setEnabled(false);

		lblState = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_STATE));
		txtState = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_STATE,REDEEMVOUCHER_FRM_TXT_STATE);
		txtState.setEditable(false);
		txtState.setEnabled(false);

		lblOffline = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_OFFLINE));
		txtOffline = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_OFFLINE,REDEEMVOUCHER_FRM_TXT_OFFLINE);
		txtOffline.setEditable(false);
		txtOffline.setEnabled(false);

		lblTktType = new CbctlLabel(readOnlyComp, SWT.NONE,AppContextValues.getInstance().getTicketText() +LabelLoader.getLabelValue(LBL_TKT_TYPE));
		txtTktType = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_TKT_TYPE,REDEEMVOUCHER_FRM_TXT_TKT_TYPE);
		txtTktType.setEditable(false);
		txtTktType.setEnabled(false);

		lblEffectiveDate = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_EFFECTIVE_DATE));
		txtEffectiveDate = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY ,REDEEMVOUCHER_CTRL_TXT_EFFECTIVEDATE,REDEEMVOUCHER_FRM_TXT_EFFECTIVEDATE);
		txtEffectiveDate.setEditable(false);
		txtEffectiveDate.setEnabled(false);

		lblExpireDate = new CbctlLabel(readOnlyComp, SWT.NONE,LabelLoader.getLabelValue(LBL_EXPIRE_DATE));
		txtExpireDate = new SDSTSText(readOnlyComp, SWT.BORDER | SWT.READ_ONLY,REDEEMVOUCHER_CTRL_TXT_EXPIREDATE,REDEEMVOUCHER_FRM_TXT_EXPIREDATE);
		txtExpireDate.setEditable(false);
		txtExpireDate.setEnabled(false);
	}

	/**
	 * This method set the default control properties
	 */
	private void setControlProperties(){
		try {
			SDSControlFactory.setTouchScreenLabelProperties(lblAmount);
			SDSControlFactory.setTouchScreenLabelProperties(lblEAmount);
			SDSControlFactory.setTouchScreenLabelProperties(lblEffectiveDate);
			SDSControlFactory.setTouchScreenLabelProperties(lblExpireDate);
			SDSControlFactory.setTouchScreenLabelProperties(lblOffline);
			SDSControlFactory.setTouchScreenLabelProperties(lblTktType);
			SDSControlFactory.setTouchScreenLabelProperties(lblState);
			SDSControlFactory.setTouchScreenLabelProperties(lblBarCode.getLabel());
			lblBarCode.setBackground(SDSControlFactory.getTSBodyColor());
			lblBarCode.getLabel().setBackground(SDSControlFactory.getTSBodyColor());
			SDSControlFactory.setTouchScreenTextProperties(txtAmount);
			SDSControlFactory.setTouchScreenTextProperties(txtEAmount);
			SDSControlFactory.setTouchScreenTextProperties(txtBarcode);
			SDSControlFactory.setTouchScreenTextProperties(txtEffectiveDate);
			SDSControlFactory.setTouchScreenTextProperties(txtExpireDate);
			SDSControlFactory.setTouchScreenTextProperties(txtOffline);
			SDSControlFactory.setTouchScreenTextProperties(txtTktType);
			SDSControlFactory.setTouchScreenTextProperties(txtState);
//			SDSControlFactory.setGroupHeadingFont(grpDetails);
			SDSControlFactory.setTouchScreenLabelProperties(lblTotalPageCount);

			int arrow_Btn_Width = 0;
			int arrow_Btn_Height = 0;

			if( Util.isSmallerResolution() ) {
				arrow_Btn_Width  = ControlConstants.ARROW_BUTTON_WIDTH;
				arrow_Btn_Height = ControlConstants.BUTTON_HEIGHT + 6;
			}

			GridData gdBtnSubmitBarcode = new GridData();
			if( Util.isSmallerResolution() ) {
				gdBtnSubmitBarcode.horizontalIndent = 40;
			} else {
				gdBtnSubmitBarcode.horizontalIndent = 260;
			}
			if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
				if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 4 && Util.isSmallerResolution() ) {
					gdBtnSubmitBarcode.horizontalIndent = 20;
				}
			}
			
			gdBtnSubmitBarcode.horizontalSpan = 2;
			iLblSubmitBarcode.setLayoutData(gdBtnSubmitBarcode);

			GridData gdBtnCancel = new GridData();
			gdBtnCancel.horizontalSpan = 2;
			if( Util.isSmallerResolution() ) {
				gdBtnCancel.horizontalIndent = 40;
			} else {
				gdBtnCancel.horizontalIndent = 90;
			}
			if(SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() != null) {
				if( SDSApplication.getApplicationSessionData().getCurrentLanguage().getId() == 4 && Util.isSmallerResolution() ) {
					gdBtnCancel.horizontalIndent = 20;
				}
			}
			
			iLblCancel.setLayoutData(gdBtnCancel);

			if( Util.isSmallerResolution() ) {

				GridData gdTextAmt = (GridData) txtAmount.getLayoutData();
				gdTextAmt.widthHint  = 140;
				gdTextAmt.heightHint = 22;
				txtAmount.setLayoutData(gdTextAmt);

				GridData gdState = (GridData) txtState.getLayoutData();
				gdState.widthHint  = 140;
				gdState.heightHint = 22;
				txtState.setLayoutData(gdState);

				GridData gdEffDate = (GridData) txtEffectiveDate.getLayoutData();
				gdEffDate.widthHint  = 140;
				gdEffDate.heightHint = 22;
				txtEffectiveDate.setLayoutData(gdEffDate);

				GridData gdExpDate = (GridData) txtExpireDate.getLayoutData();
				gdExpDate.widthHint  = 140;
				gdExpDate.heightHint = 22;
				txtExpireDate.setLayoutData(gdExpDate);

				GridData gdText = (GridData) txtTktType.getLayoutData();
				gdText.widthHint  = 140;
				gdText.heightHint = 22;
				txtTktType.setLayoutData(gdText);

				GridData gdLocation = (GridData) txtOffline.getLayoutData();
				gdLocation.widthHint  = 140;
				gdLocation.heightHint = 22;
				txtOffline.setLayoutData(gdLocation);

				GridData gdFirstPageRecords 	= (GridData)iLblFirstPageRecords.getLayoutData();
				gdFirstPageRecords.widthHint 	= arrow_Btn_Width;
				gdFirstPageRecords.heightHint 	= arrow_Btn_Height;
				iLblFirstPageRecords.setLayoutData(gdFirstPageRecords);

				GridData gdBtnLastPageRecords 	= (GridData)iLblLastPageRecords.getLayoutData();
				gdBtnLastPageRecords.widthHint 	= arrow_Btn_Width;
				gdBtnLastPageRecords.heightHint = arrow_Btn_Height;
				iLblLastPageRecords.setLayoutData(gdBtnLastPageRecords);

				GridData gdBtnNextRecords 		= (GridData)iLblNextRecords.getLayoutData();
				gdBtnNextRecords.widthHint 		= arrow_Btn_Width;
				gdBtnNextRecords.heightHint		= arrow_Btn_Height;
				iLblNextRecords.setLayoutData(gdBtnNextRecords);

				GridData gdBtnPreviousRecords 	= (GridData)iLblPreviousRecords.getLayoutData();
				gdBtnPreviousRecords.widthHint 	= arrow_Btn_Width;
				gdBtnPreviousRecords.heightHint = arrow_Btn_Height;
				iLblPreviousRecords.setLayoutData(gdBtnPreviousRecords);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the txtBarCode
	 */
	public SDSTSText getTxtBarcode() {
		return txtBarcode;
	}

	/**
	 * @param txtBarCode the txtBarCode to set
	 */
	public void setTxtBarcode(SDSTSText txtBarCode) {
		this.txtBarcode = txtBarCode;
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
	 * @return the txtState
	 */
	public SDSTSText getTxtState() {
		return txtState;
	}

	/**
	 * @param txtState the txtState to set
	 */
	public void setTxtState(SDSTSText txtState) {
		this.txtState = txtState;
	}

	/**
	 * @return the txtEffectiveDate
	 */
	public SDSTSText getTxtEffectiveDate() {
		return txtEffectiveDate;
	}

	/**
	 * @param txtEffectiveDate the txtEffectiveDate to set
	 */
	public void setTxtEffectiveDate(SDSTSText txtEffectiveDate) {
		this.txtEffectiveDate = txtEffectiveDate;
	}

	/**
	 * @return the txtExpireDate
	 */
	public SDSTSText getTxtExpireDate() {
		return txtExpireDate;
	}

	/**
	 * @param txtExpireDate the txtExpireDate to set
	 */
	public void setTxtExpireDate(SDSTSText txtExpireDate) {
		this.txtExpireDate = txtExpireDate;
	}

	/**
	 * @return the txtLocation
	 */
	public SDSTSText getTxtLocation() {
		return txtOffline;
	}

	/**
	 * @param txtLocation the txtLocation to set
	 */
	public void setTxtLocation(SDSTSText txtLocation) {
		this.txtOffline = txtLocation;
	}

	/**
	 * @return the txtPlayerId
	 */
	public SDSTSText getTxtPlayerId() {
		return txtTktType;
	}

	/**
	 * @param txtPlayerId the txtPlayerId to set
	 */
	public void setTxtPlayerId(SDSTSText txtPlayerId) {
		this.txtTktType = txtPlayerId;
	}

	/**
	 * @return the btnComposite
	 */
	public Composite getBtnComposite() {
		return btnComposite;
	}

	/**
	 * @param btnComposite the btnComposite to set
	 */
	public void setBtnComposite(Composite btnComposite) {
		this.btnComposite = btnComposite;
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
	 * @return the iLblSubmitBarcode
	 */
	public SDSImageLabel getILblSubmitBarcode() {
		return iLblSubmitBarcode;
	}

	/**
	 * @param iLblSubmitBarcode the iLblSubmitBarcode to set
	 */
	public void setILblSubmitBarcode(SDSImageLabel iLblSubmitBarcode) {
		this.iLblSubmitBarcode = iLblSubmitBarcode;
	}

	/**
	 * @return the tblTransactionDetail
	 */
	public TableDescriptor getTblTransactionDetail() {
		return tblTransactionDetail;
	}

	/**
	 * @param tblTransactionDetail the tblTransactionDetail to set
	 */
	public void setTblTransactionDetail(TableDescriptor tblTransactionDetail) {
		this.tblTransactionDetail = tblTransactionDetail;
	}

	/**
	 * @return the tableViewer
	 */
	public SDSTableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * @param tableViewer the tableViewer to set
	 */
	public void setTableViewer(SDSTableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	/**
	 * @return the txtEAmount
	 */
	public SDSTSText getTxtEAmount() {
		return txtEAmount;
	}

	/**
	 * @param txtEAmount the txtEAmount to set
	 */
	public void setTxtEAmount(SDSTSText txtEAmount) {
		this.txtEAmount = txtEAmount;
	}

	/**
	 * @return the txtOffline
	 */
	public SDSTSText getTxtOffline() {
		return txtOffline;
	}

	/**
	 * @param txtOffline the txtOffline to set
	 */
	public void setTxtOffline(SDSTSText txtOffline) {
		this.txtOffline = txtOffline;
	}

	/**
	 * @return the txtTktType
	 */
	public SDSTSText getTxtTktType() {
		return txtTktType;
	}

	/**
	 * @param txtTktType the txtTktType to set
	 */
	public void setTxtTktType(SDSTSText txtTktType) {
		this.txtTktType = txtTktType;
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


	private void createImages() {
		buttonBG  = new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_BUTTON_IMAGE));
	}
}

