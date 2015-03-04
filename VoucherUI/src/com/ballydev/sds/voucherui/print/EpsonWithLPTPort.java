package com.ballydev.sds.voucherui.print;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.ITicketConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.util.Text;
import com.ballydev.sds.voucherui.util.TicketDetails;

public class EpsonWithLPTPort {

	private Logger logger = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	public synchronized void printVoidReceipt_LPT(String printerType) throws PrinterException {
		StringBuffer image = new StringBuffer("");
		String barcode = "000000000000000000";
		try {

			image.append("\n\n\nVOID "+LabelLoader.getLabelValue(IDBPreferenceLabelConstants.CASHIER_RECEIPT));
			image.append(IVoucherConstants.NEWLINE);
			image.append("---------------");
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VALIDATION_NO));
			image.append(IVoucherConstants.NEWLINE);
			image.append("    " + Text.expandBarcode(barcode));
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.MAC_NO)+ barcode.substring(4, 9));
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OPERATOR_NO) +" 0000");
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.ISSUE_DATE));
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PAID_DATE) );
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PAID_TIME) );
			image.append(IVoucherConstants.NEWLINE);
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_AMOUNT));
			image.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.INVALID_TKT_AMT));
			
			image.append("                       ");
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);
			image.append(IVoucherConstants.NEWLINE);

		} catch (Exception error) {
			throw new PrinterException(error.getMessage());
		}
		printContent(image.toString(), printerType);
	}

	public synchronized void printCustomerReceipt(TicketDetails td, String employee, String printerType)   throws PrinterException {   	
		java.text.DateFormat dfdate = new java.text.SimpleDateFormat("MM/dd/yyyy");
		java.text.DateFormat dftime = new java.text.SimpleDateFormat("HH:mm:ss");

		StringBuilder sb = new StringBuilder();

		String barcode     = td.getBarcode();
		String issueDate = dfdate.format(td.getEffective());

		Date paidCalendar = com.ballydev.sds.framework.util.DateUtil.getCurrentServerDate();
		
		String paidDate = dfdate.format(paidCalendar.getTime());
		String paidTime = dftime.format(paidCalendar.getTime());

		sb.append("\n\n\n"+LabelLoader.getLabelValue(IDBPreferenceLabelConstants.CASHIER_RECEIPT));
		sb.append(IVoucherConstants.NEWLINE);
		sb.append("---------------");
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VALIDATION_NO));
		sb.append("\t" + Text.expandBarcode(barcode));
		sb.append(IVoucherConstants.NEWLINE);

		if( barcode.startsWith("5") || barcode.startsWith("6") )
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.MAC_NO)+ td.getLocation());
		
		else if( barcode.startsWith("7") && barcode.charAt(4) == '9' )
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LOCATION_ID )+ td.getLocation());
		
		else 
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.SYS_GEN_VOU));
		
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OPERATOR_NO) + employee);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.ISSUE_DATE) + issueDate);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PAID_DATE) + paidDate);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PAID_TIME) + paidTime);
		sb.append(IVoucherConstants.NEWLINE);
		
		try {
			System.out.println(Text.convertDollar((long)(td.getAmount())));
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_AMOUNT) + "           : " + Text.convertDollar((long)(td.getAmount())));
		} catch (Exception e) {
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_AMOUNT) + "           : " + LabelLoader.getLabelValue(ITicketConstants.INVALID_AMT));
		}

		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		
		printContent(sb.toString(), printerType);
	}

	/**
	 * This method will print the report for voucher override functionality. 
	 * @param tdList
	 * @param employee
	 * @param printerType
	 * @param cashierName
	 * @throws PrinterException
	 */
	public synchronized void printCustomerReceipt(List<TicketDetails> tdList, String employee, String printerType, String cashierName, boolean defaultPrinter ) throws PrinterException {   	
		
		java.text.DateFormat dfdate = new java.text.SimpleDateFormat("MM/dd/yyyy");
		java.text.DateFormat dftime = new java.text.SimpleDateFormat("HH:mm:ss");

		Date authorizedCalendar = com.ballydev.sds.framework.util.DateUtil.getCurrentServerDate();
		
		String authorizedUser  	= "";
		String authorizedDate   = dfdate.format(authorizedCalendar.getTime());
		String authorizedTime   = dftime.format(authorizedCalendar.getTime());

		String playerId = "";
		
		double totalAmount = 0.00;
		double exTotalAmount = 0.00;

		StringBuilder sb = new StringBuilder();
		
		sb.append("\n\n\n"+LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_CASHIER_RECEIPT));
		sb.append(IVoucherConstants.NEWLINE);
		sb.append("----------------------");
		sb.append(IVoucherConstants.NEWLINE);
		sb.append("\"" + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_TICKET_NOT_FOUND) + "\"");
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_CASHIER_NAME) + cashierName);
		sb.append(IVoucherConstants.NEWLINE);
		for( TicketDetails td1 : tdList ) {
			authorizedUser = td1.getOverrideUserName();
		}
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_APPROVER_NAME) + authorizedUser);
		sb.append(IVoucherConstants.NEWLINE);

		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_APPROVAL_DATE) + authorizedDate);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_APPROVAL_TIME) + authorizedTime);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		
		String barcode    = "";

		sb.append(IVoucherConstants.NEWLINE);
		for( TicketDetails td : tdList ) {
			
//			System.out.println("Barcode in Print Object: " + td.getBarcode() 
//					+ "\n" + "Override User Name in Print Object: " + td.getOverrideUserName() 
//					+ "\n" + "Amount in Print Object: " + td.getAmount()
//					+ "\n" + "Status in Print Object: " + td.getStatus());
			
			barcode  = td.getBarcode();
			if( td.getStatus().equals(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_REDEEMED_SUCCESSFULLY)) ) {
				totalAmount = totalAmount + td.getAmount();
			} else {
				exTotalAmount = exTotalAmount + td.getAmount();
			}

			try {
				sb.append(Text.expandBarcode(barcode));
				sb.append(paddingSpace(Text.expandBarcode(barcode), Text.convertDollar((long)(td.getAmount()))));
				sb.append(Text.convertDollar((long)(td.getAmount())));
				sb.append(IVoucherConstants.NEWLINE);
				
				sb.append(td.getStatus());
				sb.append(IVoucherConstants.NEWLINE);
				if( td.getPlayerID().equals(IVoucherConstants.PLAYER_CARD_ZERO) )
					playerId = "";
				else
					playerId = td.getPlayerID();
					
				sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_TICKET_PLAYER_ID) + ":  " + playerId);
				sb.append(IVoucherConstants.NEWLINE);
				
			} catch (Exception e) {
				sb.append(Text.expandBarcode(barcode));
				sb.append(paddingSpace(Text.expandBarcode(barcode), LabelLoader.getLabelValue(ITicketConstants.INVALID_AMT)));
				sb.append(LabelLoader.getLabelValue(ITicketConstants.INVALID_AMT));
				sb.append(IVoucherConstants.NEWLINE);
			}
		}

		sb.append(IVoucherConstants.NEWLINE);

		try {
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_PAY_THIS_AMOUNT));
			sb.append(paddingSpace(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_PAY_THIS_AMOUNT), Text.convertDollar((long)(totalAmount))));
			sb.append(Text.convertDollar((long)(totalAmount)));
		} catch (Exception e) {
			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_PAY_THIS_AMOUNT) + LabelLoader.getLabelValue(ITicketConstants.INVALID_AMT));
		}
		if( exTotalAmount > 0.00 ) {
			try {
				sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_EXCLUDE_THIS_AMOUNT));
				sb.append(paddingSpace(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_EXCLUDE_THIS_AMOUNT), Text.convertDollar((long)(exTotalAmount))));
				sb.append(Text.convertDollar((long)(exTotalAmount)));
			} catch (Exception e) {
				sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_EXCLUDE_THIS_AMOUNT) + LabelLoader.getLabelValue(ITicketConstants.INVALID_AMT));
			}
		}
		
//		if( barcode.startsWith("5") || barcode.startsWith("6") )
//			sb.append(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_LOG_MAC_NO) + location);
		
		sb.append(IVoucherConstants.NEWLINE);
		
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		
		if( !defaultPrinter ) {
			printContent(sb.toString(), printerType);
		} else
			printContentOnDefault(sb.toString());
	}

	@SuppressWarnings("null")
	public void printContent(String content, String printerType) {
		try {
			boolean printed = false;
			if( content == null && content.toString().length() == 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_DATA)));
				return;
			} else {
				ProgressIndicatorUtil.openInProgressWindow();
				VoucherPrintReports printingService = new VoucherPrintReports();
				printed = printingService.printText(content, printerType);
				if( !printed ) {
					ProgressIndicatorUtil.closeInProgressWindow();
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_ERR_PRINT));
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while printing the details",e);
			if( e.getMessage() != null && e.getMessage().trim().length() > 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(e.getMessage());
			} else {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_DETAILS_ERROR));
			}
		}
	}
	
	public void printContentOnDefault(String content) {
		try {
			if( content != null && content.toString().length() == 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_DATA)));
				return;
			} else {
				ProgressIndicatorUtil.openInProgressWindow();
				
				CashierSessionPrintReports cSPrintReports = new CashierSessionPrintReports();
				cSPrintReports.print(content.toUpperCase());
			}
		} catch (Exception e) {
			logger.error("Exception occured while printing the details",e);
			
			if( e.getMessage() != null && e.getMessage().trim().length() > 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(e.getMessage());
			} else {			
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_DETAILS_ERROR));
			}
		}		
	}
	
	public StringBuffer paddingSpace(String prevcontent, String currContent) {
		StringBuffer buf = new StringBuffer();
		int availSpace = IVoucherConstants.MAX_LENGTH_PRINT - (prevcontent.length() + currContent.length());
		for( int i = 0 ; i < availSpace; i++ ) {
			buf.append(" ");
		}
		return buf;
	}
}
