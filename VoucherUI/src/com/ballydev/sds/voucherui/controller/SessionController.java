package com.ballydev.sds.voucherui.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.util.CurrencyUtil;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.voucher.dto.CashierSessionReportDTO;
import com.ballydev.sds.voucher.dto.TicketDTO;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.print.CashierSessionPrintReports;
import com.ballydev.sds.voucherui.print.VoucherPrintReports;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * This class acts as a controller for all the report print
 * actions in the session dialog
 * @author bdevi
 *
 */
public class SessionController {

	private CashierSessionReportDTO  cashierSessRptDTO = null;
	
	private Logger logger = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	public void printSessionDetails(String eventName) {

		if( cashierSessRptDTO == null || cashierSessRptDTO.getSessionStartDate() == null ) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_DATA)));
			return;
		}
		StringBuilder sb = new StringBuilder();
		String header = null;
		if( eventName.equalsIgnoreCase(IVoucherConstants.VOU_CURR_SESS_DET) ) {
			header = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CURNT_SESS_DET);
		}
		else {
			header = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PREV_SESS_DET);
		}

		sb.append(IVoucherConstants.NEWLINE);
		int headerLength = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CURNT_SESS_SUMM).length();
		int headerSpaces = (IVoucherConstants.MAX_LENGTH_PRINT - headerLength) / 2; 
		for( int i = 0; i < headerSpaces; i++ ) {
			sb.append(" ");
		}

		// header
		sb.append(header);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);

		String gamingDate = com.ballydev.sds.voucherui.util.DateUtil.getFormattedDateWithoutTime(cashierSessRptDTO.getGamingDate()).toString();
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_GAMING_DATE));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_GAMING_DATE),gamingDate));
		sb.append(gamingDate);
		sb.append(IVoucherConstants.NEWLINE);

		sb.append(cashierSessRptDTO.getCashierName());
		sb.append(paddingSpace(cashierSessRptDTO.getCashierName(),VoucherUtil.removeZeros(cashierSessRptDTO.getLoginID())));
		sb.append(VoucherUtil.removeZeros(cashierSessRptDTO.getLoginID()));
		sb.append(IVoucherConstants.NEWLINE);


		String startDate = DateUtil.getFormattedDate(cashierSessRptDTO.getSessionStartDate()).toString();
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_START));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_START),startDate));
		sb.append(startDate);
		sb.append(IVoucherConstants.NEWLINE);

		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_END));
		if(cashierSessRptDTO.getSessionEndDate()!=null) {
			String sessionEnd = DateUtil.getFormattedDate(cashierSessRptDTO.getSessionEndDate()).toString();				
			sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_END),sessionEnd));
			sb.append(sessionEnd);
		}
		sb.append(IVoucherConstants.NEWLINE);


		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_COUNT));
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_COUNT),String.valueOf(cashierSessRptDTO.getNoOfTickets())));
		sb.append(String.valueOf(cashierSessRptDTO.getNoOfTickets()));
		sb.append(IVoucherConstants.NEWLINE);

		String amt = ConversionUtil.centsToDollar(cashierSessRptDTO.getTotalValue());
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_VALUE));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_VALUE),CurrencyUtil.getCurrencySymbol()+ CurrencyUtil.getCurrencyFormat(amt)));
		sb.append(CurrencyUtil.getCurrencySymbol() + CurrencyUtil.getCurrencyFormat(amt));
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);

		if (cashierSessRptDTO.getList() != null && cashierSessRptDTO.getList().size() > 0) {
			List<TicketDTO> tktDTOList = cashierSessRptDTO.getList();
			if(tktDTOList != null && tktDTOList.size() > 0) {
				for(TicketDTO tktDTO : tktDTOList) {
					sb.append(String.valueOf(tktDTO.getTktBarcode()));
					String barcodeAmt = ConversionUtil.centsToDollar(tktDTO.getTktAmount());
					sb.append(paddingSpace(CurrencyUtil.getCurrencyFormat(barcodeAmt), String.valueOf(tktDTO.getTktBarcode())));
					sb.append(CurrencyUtil.getCurrencyFormat(barcodeAmt));
					sb.append(IVoucherConstants.NEWLINE);
				}

			}
		}
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_COUNT));
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_COUNT),String.valueOf(cashierSessRptDTO.getNoOfTickets())));
		sb.append(String.valueOf(cashierSessRptDTO.getNoOfTickets()));
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_VALUE));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_VALUE),CurrencyUtil.getCurrencySymbol() + CurrencyUtil.getCurrencyFormat(amt)));
		sb.append(CurrencyUtil.getCurrencySymbol() + CurrencyUtil.getCurrencyFormat(amt));
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);
		printContent(sb.toString());

	}


	public void printSessionSummary(String eventName) {

		if (cashierSessRptDTO == null || cashierSessRptDTO.getSessionStartDate() == null) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_DATA)));
			return;
		}
		StringBuilder sb = new StringBuilder();
		String header = null;
		if(eventName.equalsIgnoreCase(IVoucherConstants.VOU_CURNT_SESS_SUMM)) {
			header = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CURNT_SESS_SUMM);
		}
		else {
			header = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_PREV_SESS_SUMM);
		}

		sb.append(IVoucherConstants.NEWLINE);
		int headerLength = LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_CURNT_SESS_SUMM).length();
		int headerSpaces = (IVoucherConstants.MAX_LENGTH_PRINT - headerLength) / 2; 
		for (int i = 0; i < headerSpaces; i++) {
			sb.append(" ");
		}

		// header
		sb.append(header);
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);

		String gamingDate = com.ballydev.sds.voucherui.util.DateUtil.getFormattedDateWithoutTime(cashierSessRptDTO.getGamingDate()).toString();
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_GAMING_DATE));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_GAMING_DATE),gamingDate));
		sb.append(gamingDate);
		sb.append(IVoucherConstants.NEWLINE);

		sb.append(cashierSessRptDTO.getCashierName());		
		sb.append(paddingSpace(cashierSessRptDTO.getCashierName(),VoucherUtil.removeZeros(cashierSessRptDTO.getLoginID())));
		sb.append(VoucherUtil.removeZeros(cashierSessRptDTO.getLoginID()));
		sb.append(IVoucherConstants.NEWLINE);


		String startDate = DateUtil.getFormattedDate(cashierSessRptDTO.getSessionStartDate()).toString();
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_START));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_START),startDate));
		sb.append(startDate);
		sb.append(IVoucherConstants.NEWLINE);

		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_END));
		if(cashierSessRptDTO.getSessionEndDate()!=null) {
			String sessionEnd = DateUtil.getFormattedDate(cashierSessRptDTO.getSessionEndDate()).toString();				
			sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SESS_END),sessionEnd));
			sb.append(sessionEnd);
		}
		sb.append(IVoucherConstants.NEWLINE);

		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_COUNT));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_TKT_COUNT),String.valueOf(cashierSessRptDTO.getNoOfTickets())));
		sb.append(String.valueOf(cashierSessRptDTO.getNoOfTickets()));
		sb.append(IVoucherConstants.NEWLINE);

		String amt = ConversionUtil.centsToDollar(cashierSessRptDTO.getTotalValue());
		sb.append(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_VALUE));		
		sb.append(paddingSpace(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_VALUE),CurrencyUtil.getCurrencySymbol() + CurrencyUtil.getCurrencyFormat(amt)));
		sb.append(CurrencyUtil.getCurrencySymbol() + CurrencyUtil.getCurrencyFormat(amt));
		sb.append(IVoucherConstants.NEWLINE);
		sb.append(IVoucherConstants.NEWLINE);

		printContent(sb.toString());
	}

	public void printContent(String content) {
		try {
			boolean printed = false;			
			if( content != null && content.toString().length() == 0 ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog((LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_DATA)));
				return;
			} else {
				/* Retrofit SP9 for PCI */
				ProgressIndicatorUtil.openInProgressWindow();
                VoucherPrintReports printingService = new VoucherPrintReports();        
                printed = printingService.printSessionDetails(content.toUpperCase());
                if(!printed){
                        ProgressIndicatorUtil.closeInProgressWindow();
                        MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_ERR_PRINT));                              
                }
				
				/** 
				 * Fixed CR# 73317
				 * Code change to support unicode Character for Detail/Summary session Reports 
				 */
				//CashierSessionPrintReports cSPrintReports = new CashierSessionPrintReports();
				//cSPrintReports.print(content.toUpperCase());
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
//		String currContent12 = convertAscciiToUni(currContent);
		int availSpace = IVoucherConstants.MAX_LENGTH_PRINT - (prevcontent.length() + currContent.length());
		for( int i = 0 ; i < availSpace; i++ ) {
			buf.append(" ");
		}

//		Pattern p = Pattern.compile("    ");
//		Matcher m = p.matcher(buf);
//		
//		boolean result = m.find();
//		while(result) {
//			m.appendReplacement(buf, "\u3000");
//			result = m.find();
//		}
//		
//		m.appendTail(buf);
				
		return buf;

	}

	public CashierSessionReportDTO getCashierSessRptDTO() {
		return cashierSessRptDTO;
	}

	public void setCashierSessRptDTO(CashierSessionReportDTO cashierSessRptDTO) {
		this.cashierSessRptDTO = cashierSessRptDTO;
	}


}
