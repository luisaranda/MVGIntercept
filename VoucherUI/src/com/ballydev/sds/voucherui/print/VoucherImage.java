/*****************************************************************************
 * $Id: VoucherImage.java,v 1.10, 2010-06-22 16:01:18Z, Verma, Nitin Kumar$
 * $Date: 6/22/2010 11:01:18 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.print;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.DateUtil;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.enumconstants.ErrorCodeEnum;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.util.ConversionUtil;
import com.ballydev.sds.voucherui.util.TicketDetails;
import com.ballydev.sds.voucherui.util.WordDecimalFormatUtil;

/**
 * The class that contains the logic to generate an image/format for voucher
 * related printing tasks.
 * 
 * @author skarthik
 */
public class VoucherImage {

	public TicketDetails copyObj(TicketInfoDTO ticketInfoDTO){
		@SuppressWarnings("unused")
		WordDecimalFormatUtil decimalFmtUtil = new WordDecimalFormatUtil();
		String strFormattedAmount = ConversionUtil.centsToDollar(ticketInfoDTO.getAmount());
		@SuppressWarnings("unused")
		String[] arr = strFormattedAmount.split("\\.");
		//ConversionUtil.centsToDollar will always return a string of format XX.XX
		//So we can assume there will always be two elemnts in the array.
//		String amount = decimalFmtUtil.convert(Integer.parseInt(arr[0]))
//		+ " Dollars And "
//		+ decimalFmtUtil.convert(Integer.parseInt(arr[1]))
//		+ " Cents";	
				
		TicketDetails ticketDetails = new TicketDetails(null,null,ticketInfoDTO.getBarcode(),ticketInfoDTO.getTransAssetNumber(),
				ticketInfoDTO.getAmount(),ticketInfoDTO.getAmountType().toString(),ticketInfoDTO.getStatus().toString(),
				ticketInfoDTO.getPlayerId(),
				DateUtil.getLocalTimeFromUTC(ticketInfoDTO.getEffectiveDate().getTime()).getTime(),DateUtil.getLocalTimeFromUTC(ticketInfoDTO.getExpireDate().getTime()).getTime(),
				ticketInfoDTO.getEmployeeId(),(DateUtil.getCurrentServerDate()).toString(),ticketInfoDTO.getPlayerCardReqd());	
		return ticketDetails;
	}
	
	public List<TicketDetails> copyObj(List<TicketInfoDTO> tfList){
		List<TicketDetails> ticketDetailsList = new ArrayList<TicketDetails>();
		for(int i =0;i<tfList.size();i++){
			TicketInfoDTO ticketInfoDTO = tfList.get(i);
			TicketDetails ticketDetails = new TicketDetails(null,null,ticketInfoDTO.getBarcode(),ticketInfoDTO.getTransAssetNumber(),
					ticketInfoDTO.getAmount(),ticketInfoDTO.getAmountType().toString(),ticketInfoDTO.getStatus().toString(),
					ticketInfoDTO.getPlayerId(),
					DateUtil.getLocalTimeFromUTC(ticketInfoDTO.getEffectiveDate().getTime()).getTime(),DateUtil.getLocalTimeFromUTC(ticketInfoDTO.getExpireDate().getTime()).getTime(),
					ticketInfoDTO.getEmployeeId(),(DateUtil.getCurrentServerDate()).toString(),ticketInfoDTO.getPlayerCardReqd());
			ticketDetailsList.add(ticketDetails);
		}		
		return ticketDetailsList;
	}

	//Method added to support voucher override functionality.
	public List<TicketDetails> copyObject(List<TicketInfoDTO> tfList, String cashierName){
		List<TicketDetails> ticketDetailsList = new ArrayList<TicketDetails>();
		
		for( int i = 0; i < tfList.size(); i++ ) {
			TicketInfoDTO ticketInfoDTO = tfList.get(i);
			
			String ticketStatus = "";
			if( ticketInfoDTO.getErrorCodeId() == ErrorCodeEnum.SUCCESS.getErrorCode() ) {
				ticketStatus = LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_REDEEMED_SUCCESSFULLY);
			} else {
				ticketStatus = LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OVERRIDE_REDEEMED_FAILURE);
			} 
			
			TicketDetails ticketDetails = new TicketDetails(null, null, ticketInfoDTO.getBarcode(), ticketInfoDTO.getTransAssetNumber(),
					ticketInfoDTO.getAmount(), ticketStatus, ticketInfoDTO.getEmployeeId(), cashierName, 
					ticketInfoDTO.getOverrideUsername(), ticketInfoDTO.getPlayerId());
			ticketDetailsList.add(ticketDetails);
		}		
		return ticketDetailsList;
	}

	
	public static void main(String[] args) {

		try {

			@SuppressWarnings("unused")
			VoucherImage img = new VoucherImage();
			TicketInfoDTO ticketForm;
			ticketForm = new TicketInfoDTO();

			ticketForm.setBarcode("700012345678912345");
			ticketForm.setAmount(125);
			ticketForm.setEffectiveDate(new Date());
			ticketForm.setExpireDate(new Date());

			//String image  = img.getLaserPrinterImage(ticketForm);
			@SuppressWarnings("unused")
			VoucherPrintingService  print = new VoucherPrintingService();
//			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//			aset.add(OrientationRequested.LANDSCAPE);
			//print.printVoucher(image, null);

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
