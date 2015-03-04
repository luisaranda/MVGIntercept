/*****************************************************************************
 * $Id: PrintBatchController.java,v 1.16, 2010-04-23 08:00:57Z, Lokesh, Krishna Sinha$
 * $Date: 4/23/2010 3:00:57 AM$
 *****************************************************************************
 *	Copyright (c) 2006 Bally Technology  1977 - 2008
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.PrintBatchComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.form.BatchSummaryDTO;
import com.ballydev.sds.voucherui.form.PrintBatchForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.BatchDetailsPrinter;
import com.ballydev.sds.voucherui.service.IVoucherService;
import com.ballydev.sds.voucherui.service.VoucherServiceLocator;
import com.ballydev.sds.voucherui.util.DateHelper;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;

/**
 * @author Ganesh Amirtharaj
 */
public class PrintBatchController extends SDSBaseController{

	private Shell dialogShell = null;

	private PrintBatchComposite printBatchComposite = null;

	private PrintBatchForm printBatchForm = null;

	private final int length = 18;

	private final int bufferLength = 2;

	private final int lengthShort = 8;

	private final int lengthMed = 12;

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	private BatchSummaryDTO selectedBatchSummaryDTO = null;

	private List<BatchSummaryDTO> listBatchSummaryDTO = null;

	private Timestamp slipDropTime = null;

	public PrintBatchController(SDSForm sdsForm, SDSValidator pValidator, BatchSummaryDTO selectedBatchSummaryDTO, List<BatchSummaryDTO> listBatchSummaryDTO) throws Exception {
		super(sdsForm, pValidator);
		dialogShell = new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL);
		printBatchComposite = new PrintBatchComposite(dialogShell, SWT.APPLICATION_MODAL | SWT.TITLE | SWT.CLOSE); 
		printBatchForm = (PrintBatchForm) sdsForm;
		this.selectedBatchSummaryDTO = selectedBatchSummaryDTO;
		this.listBatchSummaryDTO = listBatchSummaryDTO;
		dialogShell.setSize(500,300);
		Util.initializeShellBounds(dialogShell);
		dialogShell.layout();
		dialogShell.setBackground(new Color(Display.getCurrent(),255,255,255));
		dialogShell.setForeground(new Color(Display.getCurrent(),200,200,255));
		dialogShell.open();
		registerEvents(printBatchComposite);
		this.registerCustomizedListeners(printBatchComposite);
		printBatchComposite.getLblBatchNumber().setText(selectedBatchSummaryDTO.getBatchNumber().toString() + " (" +selectedBatchSummaryDTO.getEmpId() +")");
		printBatchForm.setPrintSelected(true);
		printBatchComposite.getRadioButtonControl().setSelectedButton(printBatchComposite.getRBtnPrintSelected());
		printBatchComposite.getTxtBatchNumber().setEnabled(false);
		populateScreen(printBatchComposite);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		try {
			populateForm(printBatchComposite);
			if( e.getSource() instanceof SDSImageLabel ) {
				SDSImageLabel button = (SDSImageLabel) e.getSource();
				if( PrintBatchComposite.BTN_CANCEL.equals(button.getName()) ) {
					dialogShell.dispose();
					KeyBoardUtil.closeKeyBoard();
				} else if( PrintBatchComposite.BTN_PRINT.equals(button.getName()) ) {
					print();
				}
			
			} else if( e.getSource() instanceof TSButtonLabel ) {
				TSButtonLabel tSRadioButton = (TSButtonLabel) e.getSource();
				tSRadioButton.setSelected(true);
//				tSRadioButton.refreshForm(printBatchForm);
				printBatchComposite.getRadioButtonControl().setSelectedButton(tSRadioButton);
				System.out.println("sdf" + tSRadioButton.getName());
				if( PrintBatchComposite.BTN_PRINT_ENTERED.equals(tSRadioButton.getName()) ) {
					printBatchComposite.getTxtBatchNumber().setEnabled(true);
					printBatchComposite.getTxtBatchNumber().setFocus();
					printBatchForm.setPrintEntered(true);
					printBatchForm.setPrintAll(false);
					printBatchForm.setPrintSelected(false);
					KeyBoardUtil.closeKeyBoard();
//					KeyBoardUtil.createKeyBoard();
//					KeyBoardUtil.setCurrentTextInFocus(printBatchComposite.getTxtBatchNumber());
//					KeyBoardUtil.disableExitButton();
				} else if( PrintBatchComposite.BTN_PRINT_SELECTED.equals(tSRadioButton.getName()) ) {
					printBatchForm.setPrintSelected(true);
				} else if( PrintBatchComposite.BTN_PRINT_ALL.equals(tSRadioButton.getName()) ) {
					printBatchForm.setPrintAll(true);
					printBatchForm.setPrintSelected(false);
					printBatchForm.setPrintEntered(false);
					printBatchForm.setBatchNumber("");
					printBatchComposite.getTxtBatchNumber().refreshControl(printBatchForm);
					printBatchComposite.getTxtBatchNumber().setEnabled(false);
					KeyBoardUtil.closeKeyBoard();
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public Composite getComposite() {
		return printBatchComposite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		PrintBatchComposite printBatchComposite = ((PrintBatchComposite)argComposite);
		printBatchComposite.getILblPrint().addMouseListener(this);
		printBatchComposite.getILblCancel().addMouseListener(this);
		printBatchComposite.getRBtnPrintAll().addMouseListener(this);
		printBatchComposite.getRBtnPrintEnteredBatch().addMouseListener(this);
		printBatchComposite.getRBtnPrintSelected().addMouseListener(this);
	}

	/**
	 * This method prints the contents of batch summary report
	 */
	private void print(){
		long time = 0;
		try {
			IVoucherService service = VoucherServiceLocator.getService();
			SessionUtility sessionUtility = new SessionUtility();
			time = service.getSlipDropTime(sessionUtility.getSiteDetails().getId());
		} catch (Throwable e) {
			log.error("Error while fetching Slip Drop Time. ",e);
		}
		if(time!=0){
			slipDropTime = new Timestamp(time); 
		}

		StringBuilder builder = new StringBuilder();
		if(printBatchForm.getPrintSelected()){
			printBatchSummaryDTO(builder, selectedBatchSummaryDTO);
		}else if(printBatchForm.getPrintEntered()){
			Long batchNumber = null;
			String batchNo = printBatchForm.getBatchNumber().trim();
			if("".equals(batchNo)){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_ERROR_NUMBER));
				printBatchComposite.getTxtBatchNumber().setFocus();
				return;
			}else{
				try {
					batchNumber = Long.parseLong(batchNo);
				} catch (NumberFormatException e) {}
			}

			BatchSummaryDTO batchSummaryDTO = getBatchSummaryDTOFromList(batchNumber);
			if(batchNumber == null || batchSummaryDTO == null){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_BATCH_ERROR_NOT_EXIST, new Object[]{batchNo}));
				printBatchComposite.getTxtBatchNumber().setFocus();
				return;
			}else{
				printBatchSummaryDTO(builder, batchSummaryDTO);
			}
		}else if(printBatchForm.getPrintAll()){
			for (BatchSummaryDTO batchSummaryDTO : listBatchSummaryDTO) {
				printBatchSummaryDTO(builder, batchSummaryDTO);
			}
		}

		try {	
			ProgressIndicatorUtil.openInProgressWindow();
			BatchDetailsPrinter batchDetailsPrinter = new BatchDetailsPrinter();
			StringBuilder header = new StringBuilder();
			printHeader(header);
			batchDetailsPrinter.setHeaderString(header.toString());
			batchDetailsPrinter.print(builder.toString());
			ProgressIndicatorUtil.closeInProgressWindow();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_SUCCESS));
		} catch (Exception e) {
			ProgressIndicatorUtil.closeInProgressWindow();
			log.error("Exception occured while printing the details",e);
			if(e.getMessage()!=null && e.getMessage().trim().length()>0){
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(e.getMessage());
			}else{			
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_DETAILS_ERROR));
			}
		}

		dialogShell.dispose();
		KeyBoardUtil.closeKeyBoard();

	}
	/**
	 * This method prints the details of the table column selected
	 */
	private void printBatchSummaryDTO(StringBuilder builder, BatchSummaryDTO batchSummaryDTO) {	
		String accountingDate= "N/A";
		String batchScanDate = "N/A";
		String datePattern = DateUtil.getCurrentDateFormat();

		if(batchSummaryDTO.getBatchScanDate()!=null){
			
			String batchScanDateS = DateUtil.getLocalTimeFromUTC(batchSummaryDTO.getBatchScanDate().getTime()).toString();
			batchScanDate = DateHelper.getFormatedDate(batchScanDateS,IVoucherConstants.DATE_FORMAT,datePattern);
			
			//batchScanDate = new SimpleDateFormat(DateUtil.getCurrentDateFormat()).format(batchSummaryDTO.getBatchScanDate());
			Date date = getAccountingDate(batchSummaryDTO.getBatchScanDate());
			if(date!=null){
				String accountingDateS = DateUtil.getLocalTimeFromUTC(date.getTime()).toString();
				accountingDate = DateHelper.getFormatedDate(accountingDateS,IVoucherConstants.DATE_FORMAT,datePattern);
			
				//accountingDate = new SimpleDateFormat(DateUtil.getCurrentDateFormat()).format(date);
			}
		}
		builder.append(VoucherStringUtil.rPad(Long.toString(batchSummaryDTO.getBatchNumber()), lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(Long.toString(batchSummaryDTO.getVoucherCount()), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(CurrencyUtil.getCurrencyFormat(Double.toString(batchSummaryDTO.getVoucherAmount())), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(Long.toString(batchSummaryDTO.getCouponCount()), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(CurrencyUtil.getCurrencyFormat(Double.toString(batchSummaryDTO.getCouponAmount())), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(batchSummaryDTO.getEmpId(), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(batchSummaryDTO.getLocCode(), lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(accountingDate, lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.rPad(batchScanDate, lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(IVoucherConstants.LINE_SEPERATOR);
	}

	/**
	 * The accounting date is calculated as follows:
	 * 
	 * if batchScanDate < slipDropTime
	 * then accountingDate = batchScanDate - 1
	 * else accountingDate = batchScanDate
	 * 
	 * @param batchScanDate
	 * @return Timestamp
	 */
	@SuppressWarnings("unused")
	private Timestamp getAccountingDate(Timestamp batchScanDate){
		Timestamp accountingDate = null;
		if(slipDropTime!=null){
			if(batchScanDate.before(slipDropTime)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(batchScanDate);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				accountingDate = new Timestamp(calendar.getTimeInMillis());
			}else{
				accountingDate = batchScanDate;
			}
		}
		return accountingDate;
	}
	
	/**
	 * The accounting date is calculated as follows:
	 * 
	 * if batchScanDate < slipDropTime
	 * then accountingDate = batchScanDate - 1
	 * else accountingDate = batchScanDate
	 * 
	 * @param batchScanDate
	 * @return Timestamp
	 */
	private Date getAccountingDate(Date batchScanDate){
		Date accountingDate = null;
		if(slipDropTime!=null){
			if(batchScanDate.before(slipDropTime)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(batchScanDate);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				accountingDate = new Timestamp(calendar.getTimeInMillis());
			}else{
				accountingDate = batchScanDate;
			}
		}
		return accountingDate;
	}

	/**
	 * This method prints the header of the table column
	 * @param builder
	 * @return
	 */
	private void printHeader(StringBuilder builder) {

		String casinoName="";
		SiteDTO  siteDTO = SDSApplication.getSiteDetails();
		if(siteDTO!=null){
			casinoName = siteDTO.getLongName();			
		}else{
			casinoName = LabelLoader.getLabelValue(IDBLabelKeyConstants.CASINO_NAME);			
		}
		builder.append(VoucherStringUtil.lPad(DateUtil.getFormattedDate(DateUtil.getCurrentServerDate().getTime()), 115));	
		builder.append(IVoucherConstants.LINE_SEPERATOR);
		builder.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_SDS_VERSION),length)+VoucherStringUtil.rPad(Util.getSDSVersion(), length));
		builder.append(IVoucherConstants.LINE_SEPERATOR);
		builder.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_RPT_CASINO_NAME), length)+VoucherStringUtil.rPad(casinoName,length));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(IVoucherConstants.LINE_SEPERATOR);
		builder.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_RPT_NAME),length)+VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.RECON_BATCH_DETAILS_RPT),length));
		builder.append(IVoucherConstants.LINE_SEPERATOR);

		builder.append(VoucherStringUtil.nCopy('-', 116));		

		builder.append(IVoucherConstants.LINE_SEPERATOR);		
		builder.append(VoucherStringUtil.rPad(LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_BATCH_NO), lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		String str = AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_VOUCHER_COUNT);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str).substring(0,(str).indexOf(" ")), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = AppContextValues.getInstance().getTicketText()+LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_VOUCHER_$)+ "("+CurrencyUtil.getCurrencySymbol()+")";
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str).substring(0,(str).indexOf(" ")), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_COUPAN_COUNT);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(str.substring(0,str.indexOf(" ")), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_COUPAN_AMOUNT)+ "("+CurrencyUtil.getCurrencySymbol()+")";
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(str.substring(0,str.indexOf(" ")), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(str.substring(0,str.indexOf(" ")), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_LOC_CODE);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str.substring(0,str.indexOf(" "))), lengthShort));
		else
			builder.append(VoucherStringUtil.rPad(str, lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));

		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_ACCOUNTING_DATE);
		if(str.contains(" "))			
			builder.append(VoucherStringUtil.rPad((str.substring(0,str.indexOf(" "))), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_BATCH_SCAN_DATE);
		if(str.contains(" "))			
			builder.append(VoucherStringUtil.rPad((str.substring(0,str.indexOf(" "))), lengthMed));
		else 
			builder.append(VoucherStringUtil.rPad(str, lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));

		builder.append(IVoucherConstants.LINE_SEPERATOR);
		builder.append(VoucherStringUtil.rPad(" " ,lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_VOUCHER_COUNT);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str.substring(str.indexOf(" "))).trim(), lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_VOUCHER_$)+ "("+CurrencyUtil.getCurrencySymbol()+")";
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str.substring(str.indexOf(" "))).trim(), lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));

		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_COUPAN_COUNT);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str.substring(str.indexOf(" "))).trim(), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));		

		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_COUPAN_AMOUNT)+ "("+CurrencyUtil.getCurrencySymbol()+")";

		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad((str.substring(str.indexOf(" "))).trim(), lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(((str.substring(str.indexOf(" ")))).trim(), lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		
		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_LOC_CODE);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(((str.substring(str.indexOf(" ")))).trim(), lengthShort));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));

		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_ACCOUNTING_DATE);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(((str.substring(str.indexOf(" ")))).trim(), lengthMed));
		
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));

		str = LabelLoader.getLabelValue(IDBLabelKeyConstants.TBL_LBL_BATCH_SCAN_DATE);
		if(str.contains(" "))
			builder.append(VoucherStringUtil.rPad(((str.substring(str.indexOf(" ")))).trim(), lengthMed));

		builder.append(VoucherStringUtil.rPad(" ", bufferLength));

		builder.append(IVoucherConstants.LINE_SEPERATOR);
		builder.append(VoucherStringUtil.nCopy('-', lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));		
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));
		builder.append(VoucherStringUtil.nCopy('-', lengthShort));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));		
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));	
		builder.append(VoucherStringUtil.nCopy('-', lengthMed));
		builder.append(VoucherStringUtil.rPad(" ", bufferLength));	
		builder.append(IVoucherConstants.LINE_SEPERATOR);
	}

	/**
	 * This method get the BatchSummaryDTO from the List of BatchSummaryDTO for the given batch Number
	 * @param batchNumber
	 * @return BatchSummaryDTO
	 */
	private BatchSummaryDTO getBatchSummaryDTOFromList(Long batchNumber){
		BatchSummaryDTO batchSummaryDTO = null;
		if(batchNumber!=null){
			for (BatchSummaryDTO summaryDTO : listBatchSummaryDTO) {
				if(batchNumber.equals(summaryDTO.getBatchNumber())){
					batchSummaryDTO = summaryDTO;
					break;
				}
			}
		}
		return batchSummaryDTO;
	}
}
