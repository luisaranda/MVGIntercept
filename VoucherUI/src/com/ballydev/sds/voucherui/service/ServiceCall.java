/*****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.ballydev.sds.employee.biz.dto.SessionInfoDTO;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.voucher.dto.BaseDTO;
import com.ballydev.sds.voucher.dto.BatchInfoDTO;
import com.ballydev.sds.voucher.dto.BatchSummaryDTO;
import com.ballydev.sds.voucher.dto.CashierSessionReportDTO;
import com.ballydev.sds.voucher.dto.EnquiryInfoDTO;
import com.ballydev.sds.voucher.dto.OfflineTicketInfoDTO;
import com.ballydev.sds.voucher.dto.QuestionableReportInfoDTO;
import com.ballydev.sds.voucher.dto.ReconciliationBatchInfoDTO;
import com.ballydev.sds.voucher.dto.ReconciliationInfoDTO;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.dto.TxnReasonsInfoDTO;
import com.ballydev.sds.voucher.dto.UserInfoDTO;
import com.ballydev.sds.voucher.enumconstants.VoucherAssetTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * @author Nithya kalyani R
 * @version $Revision: 35$ 
 */
public class ServiceCall {

	/**
	 * Instance of Service call
	 */
	private static ServiceCall serviceCall = null;

	/**	 
	 * @return
	 * @throws Exception
	 */
	public static ServiceCall getInstance(){
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			if(serviceCall == null){			
				serviceCall = new ServiceCall();				
			}
		}catch (Exception e) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SERVER_ERROR));
			return null;
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return serviceCall;
	}


	/**
	 * This method adds reconcile vouchers by batch
	 * @param batchInfoDTO
	 * @param barcode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO addReclVoucherByBatchId(BatchInfoDTO batchInfoDTO, String barcode) throws VoucherEngineServiceException {
		TicketInfoDTO ticketInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			ticketInfoDTO = VoucherServiceLocator.getService().addReclVoucherByBatchId(batchInfoDTO, barcode);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return ticketInfoDTO;
	}

	/**
	 * This method cancels the particular batch
	 * @param batchInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BaseDTO cancelBatch(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		BaseDTO baseDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			baseDTO = VoucherServiceLocator.getService().cancelBatch(batchInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}		
		return baseDTO;
	}

	/**
	 * This method cancels the redeem voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO cancelRedeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().cancelRedeemVoucher(ticketInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	

		return infoDTO;
	}

	/**
	 * This method changes the location/employee for the passed batch
	 * @param batchInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BaseDTO changeLocation(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		BaseDTO baseDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			baseDTO = VoucherServiceLocator.getService().changeLocation(batchInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return baseDTO;
	}

	/**
	 * This is the overloaded method that calls the create voucher 
	 * method multiple times
	 * @param ticketFormDTO
	 * @param noOfTickets
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public List<TicketInfoDTO> createVoucherList(TicketInfoDTO ticketFormDTO, int noOfTickets) throws VoucherEngineServiceException {
		List<TicketInfoDTO> ticketInfoDTOList = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			ticketInfoDTOList = VoucherServiceLocator.getService().createVoucherList(ticketFormDTO, noOfTickets);
		} catch (RuntimeException e) {			
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} catch (VoucherEngineServiceException e) {			
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	

		return ticketInfoDTOList;
	}

	/**
	 * This method deletes the voucher from the batch
	 * @param batchInfoDTO
	 * @param barcode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO deleteTicketFromBatch(BatchInfoDTO batchInfoDTO, String barcode) throws VoucherEngineServiceException {
		TicketInfoDTO ticketInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			ticketInfoDTO = VoucherServiceLocator.getService().deleteTicketFromBatch(batchInfoDTO, barcode);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return ticketInfoDTO;
	}

	/**
	 *  Retrives the list of Transaction Reasons
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TxnReasonsInfoDTO getTransactionReasons() throws VoucherEngineServiceException {
		TxnReasonsInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().getTransactionReasons();
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return infoDTO;
	}

	/**
	 *  Returns a list of Batch details for a given batch Id.
	 * @param reconciliationInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public ReconciliationBatchInfoDTO getVouchersForBatch(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException {
		ReconciliationBatchInfoDTO batchInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			batchInfoDTO = VoucherServiceLocator.getService().getVouchersForBatch(reconciliationInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return batchInfoDTO;

	}

	/**
	 * This method returns the ticket details for the passed barcode
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO inquireVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().inquireVoucher(ticketInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return infoDTO;
	}

	/**
	 * This method checks if the passed slot number is a vaild slot or not
	 * @param assetLocation
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public boolean isSlotNumberAvailable(String assetLocation,int siteId) throws VoucherEngineServiceException {
		boolean isSlotNumberAvailable = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			isSlotNumberAvailable = VoucherServiceLocator.getService().isSlotNumberAvailable(assetLocation,siteId);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return isSlotNumberAvailable;

	}

	/**
	 * @param assetNumber
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public VoucherAssetTypeEnum isAssetAvailable(String assetNumber,int siteId)throws VoucherEngineServiceException,Exception {
		VoucherAssetTypeEnum voucherAssetTypeEnum = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			voucherAssetTypeEnum = VoucherServiceLocator.getService().isAssetAvailable(assetNumber,siteId);
		}catch (RuntimeException e) {
			if(e.getMessage() != null && e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2) || e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG3)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} 
		catch (VoucherEngineServiceException e) {
			if(e.getMessage() != null && e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2) || e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG3)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
				throw new Exception(e);
			}
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return voucherAssetTypeEnum;
	}
	/**
	 * This method overrides the redeem voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO overrideRedeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;		
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().overrideRedeemVoucher(ticketInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return infoDTO;

	}

	/**
	 * This method processes the redeem request of a voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemRequestVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;		
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().redeemRequestVoucher(ticketInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}		
		return infoDTO;
	}

	/**
	 * This method processes the redeem request of a voucher regardless of the status
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemRequestVoucherWithoutStatusCheck(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;	
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().redeemRequestVoucherWithoutStatusCheck(ticketInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}		
		return infoDTO;
	}

	/**
	 * This method redeems the voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO =VoucherServiceLocator.getService().redeemVoucher(ticketInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return infoDTO;
	}

	/**
	 * This method redeems the voucher regardless of the status
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemVoucherWithoutStatusCheck(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().redeemVoucherWithoutStatusCheck(ticketInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return infoDTO;
	}

	/**
	 * This method reprints the voucher whose status is ACTIVE
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO reprintVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().reprintVoucher(ticketInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return infoDTO;
	}

	/**
	 *  Saves a new Reconciled Voucher record when a ticket is scanned during reconciliation.
	 * Also creates the corresponding recl vouchers.
	 * @param reconciliationInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public ReconciliationInfoDTO scanReclVouchers(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException {
		ReconciliationInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().scanReclVouchers(reconciliationInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return infoDTO;
	}

	/**
	 *  Fetches the batch details with the given criteria
	 * @param batchInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BatchSummaryDTO showBatch(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		BatchSummaryDTO batchSummaryDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			batchSummaryDTO = VoucherServiceLocator.getService().showBatch(batchInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return batchSummaryDTO;
	}

	/**
	 * This takes care of running the rules for the Questionable tickets and
	 * stamping the ticket with the corresponding Auditcodes
	 * @param batchInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public QuestionableReportInfoDTO updateQuestionableVoucher(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		QuestionableReportInfoDTO reportInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			reportInfoDTO = VoucherServiceLocator.getService().updateQuestionableVoucher(batchInfoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return reportInfoDTO;
	}

	/**
	 * This method updates the voucher in batch
	 * @param batchInfoDTO
	 * @param barcode
	 * @param modifiedBarcode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO updateTicketInBatch(BatchInfoDTO batchInfoDTO, String barcode, String modifiedBarcode) throws VoucherEngineServiceException {
		TicketInfoDTO ticketInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			ticketInfoDTO = VoucherServiceLocator.getService().updateTicketInBatch(batchInfoDTO, barcode, modifiedBarcode);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return ticketInfoDTO;
	}

	/**
	 * Verify the tickets by calling the crc barcode helper
	 * @param infoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public OfflineTicketInfoDTO validateOfflineTicket(TicketInfoDTO infoDTO) throws VoucherEngineServiceException {
		OfflineTicketInfoDTO offlineTicketInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			offlineTicketInfoDTO = VoucherServiceLocator.getService().validateOfflineTicket(infoDTO);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return offlineTicketInfoDTO;
	}

	/**
	 *  This method voids the passed ticket
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO voidVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().voidVoucher(ticketInfoDTO);			
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return infoDTO;
	}

	/**
	 * This method returns all the available batch information
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BatchSummaryDTO getBatchSummary(int siteId)throws VoucherEngineServiceException {		
		BatchSummaryDTO batchSummaryDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			batchSummaryDTO = VoucherServiceLocator.getService().getBatchSummary(siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return batchSummaryDTO;
	}

	/**
	 *  Saves a new Reconciled Voucher record when a ticket is scanned during reconciliation.
	 * Also creates the corresponding recl vouchers.
	 * @param reconciliationInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public ReconciliationInfoDTO insertReconciliation(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException {
		ReconciliationInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().insertReconciliation(reconciliationInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return infoDTO;
	}

	/**
	 * This method returns the min and max value of time stamp 
	 * user passed
	 * @param user
	 * @return
	 * @throws VoucherDAOException
	 */
	public List<String> getTimeDetails(String user,int siteId)throws VoucherEngineServiceException {
		List<String> timeDetails = new ArrayList<String>();
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			timeDetails = VoucherServiceLocator.getService().getTimeDetails(user,siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return timeDetails;
	}

	/**
	 * This method inserts the player card id
	 * for the given barcode
	 * @param transDTO
	 * @param transPlayerId
	 * @return
	 * @throws VoucherDAOException
	 */
	public void insertPlayerCardId(String barcode, String transPlayerId,int siteId) throws VoucherEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			VoucherServiceLocator.getService().insertPlayerCardId(barcode, transPlayerId,siteId);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	

	}

	/**
	 * This method voids the passed ticket
	 * @param ticketFormDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public void voidVoucherList(List<TicketInfoDTO> ticketInfoDTOList) throws VoucherEngineServiceException{
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			VoucherServiceLocator.getService().voidVoucherList(ticketInfoDTOList);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	

	}

	/**
	 * This method gets the list of transactions associated for the barcode and amount passed
	 * @param barcode
	 * @param amount
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public EnquiryInfoDTO getVoucherDetails(String barcode, String amount,int siteId)throws VoucherEngineServiceException {
		EnquiryInfoDTO enquiryInfoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			enquiryInfoDTO = VoucherServiceLocator.getService().getVoucherDetails(barcode, amount,siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return enquiryInfoDTO;
	}

	/**
	 * @param ticketInfoDTO
	 * @throws VoucherEngineServiceException
	 */
	public void sendUpdateRedeemRequest(TicketInfoDTO ticketInfoDTO) throws Exception {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			VoucherServiceLocator.getService().sendUpdateRedeemRequest(ticketInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (Exception e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			

	}

	/**
	 * @param ticketInfoDTO
	 * @throws VoucherEngineServiceException
	 */
	public void sendVoidEntryForPromo(TicketInfoDTO ticketInfoDTO) throws Exception {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			VoucherServiceLocator.getService().sendVoidEntryForPromo(ticketInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (Exception e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			

	}


	/**
	 * This method checks whether the total emp amt is 
	 * in limit with the site config value set for the user
	 * @param userName
	 * @param amt
	 * @param siteId
	 * @throws VoucherEngineServiceException
	 */
	public void chkEmpTotalAmt(String userName , String amt,int siteId) throws VoucherEngineServiceException{	
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			VoucherServiceLocator.getService().chkEmpTotalAmt(userName, amt, siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}


	/**
	 * This method checks if the given workstation is present in the hashmap
	 * and puts the value in the amp if it is not present already
	 * @param workStation
	 * @return
	 */
	public boolean chkWorkStation(String workStation) throws VoucherEngineServiceException{
		boolean retValue = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			retValue = VoucherServiceLocator.getService().chkWorkStationSession(getUserInfoDTO(workStation));
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}			
		return retValue;
	}
	
	private UserInfoDTO getUserInfoDTO(String workStation) throws Exception {
		UserInfoDTO userInfoDTO =null;
		try {
			userInfoDTO = VoucherUtil.convertUserDTOToUserForm(SDSApplication.getUserDetails());
			workStation = workStation.toUpperCase();
			userInfoDTO.setWorkStation(workStation);
			workStation = workStation.concat(String.valueOf(SDSApplication.getSiteDetails().getId()));
			/*workStation = workStation.concat(SDSApplication.getLoggedInUserID());	
			workStation = workStation.concat(InetAddress.getLocalHost().getHostAddress());	
			*/		
			userInfoDTO.setWorkStationKey(workStation);			
			userInfoDTO.setUserRoleTypeId(VoucherUtil.getUserRole());		 
			userInfoDTO.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			throw e;
		}
		return userInfoDTO;
	
	}

	/**
	 * This method removes the workstaion value
	 * from the hash map
	 * @param workStation
	 */
	public void removeWorkStation(String workStation) throws VoucherEngineServiceException{		
		ProgressIndicatorUtil.openInProgressWindow();
		try {
		    VoucherServiceLocator.getService().removeWorkStationSession(getUserInfoDTO(workStation));
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}
	
	/**
	 * This method creates the new session for the emp
	 * @param workStation
	 */
	public void createSession(String workStation) throws VoucherEngineServiceException{		
		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			VoucherServiceLocator.getService().createSession(getUserInfoDTO(workStation));
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}

	/**
	 * This method inserts a record for the new session created and update the previous paused session.
	 * @param userInfoDTO 
	 */
	public void createSessionWhenPaused(String workStation) throws VoucherEngineServiceException {
	

		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			VoucherServiceLocator.getService().createSessionWhenPaused(getUserInfoDTO(workStation));
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}

	/**
	 * This method sign on in the existing session for the emp
	 * @param workStation
	 */
	public void signOnSession(String workStation) throws VoucherEngineServiceException{		
		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			VoucherServiceLocator.getService().signOnSession(getUserInfoDTO(workStation));
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}

	/**
	 * This method sign off in the existing session for the emp
	 * @param workStation
	 */
	public void signOffSession(String workStation) throws VoucherEngineServiceException{		
		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			VoucherServiceLocator.getService().signOffSession(getUserInfoDTO(workStation));
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}

	public int getAmountType(String barcode,int siteId)throws VoucherEngineServiceException{
		ProgressIndicatorUtil.openInProgressWindow();
		int amtType =0;
		try {
			amtType = VoucherServiceLocator.getService().getAmountType(barcode,siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return amtType;
	}
	
	/**
	 * This method returns the session status
	 * @param userInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public SessionInfoDTO getSessionStatus(String employeeId, String workStation, int siteId) throws VoucherEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		SessionInfoDTO sessionInfoDTO = null;
		try {
			sessionInfoDTO = VoucherServiceLocator.getService().isWorkStationLoggedIn(employeeId, workStation, siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return sessionInfoDTO;
	}
	
	/**
	 * This method updates the session status to 1 if the
	 * session is paused in the client side
	 * @param sessionStatus 
	 * @param userInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public void updateSessionForceStatus(String workStation, int sessionStatus) throws VoucherEngineServiceException{		
		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			UserInfoDTO userInfoDTO = getUserInfoDTO(workStation);
			userInfoDTO.setPaused(sessionStatus);
			VoucherServiceLocator.getService().updateSessionForceStatus(userInfoDTO);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}
	
	/**
	 * Returns the cashier session object for a particular cashier, cashier workstation
	 * and siteID 
	 * @param cashierID
	 * @param workstationNo
	 * @param isCurrentSession
	 * @return CashierSessionReportDTO
	 * @throws VoucherEngineServiceException
	 */
	public CashierSessionReportDTO fetchSummaryReport(int siteID, String cashierID,	String workstationNo, boolean isCurrentSession) throws VoucherEngineServiceException{
		ProgressIndicatorUtil.openInProgressWindow();
		CashierSessionReportDTO cashierSessionReportDTO = null;
		try {			
			cashierSessionReportDTO = VoucherServiceLocator.getService().fetchSummaryReport(siteID, cashierID, workstationNo, isCurrentSession);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return cashierSessionReportDTO;
	}
	

	/**
	 * Returns the cashier session object including barcode details redeemed by a particular cashier in a cashier workstation
	 * @param cashierID
	 * @param workstationNo
	 * @param isCurrentSession
	 * @return CashierSessionReportDTO
	 * @throws VoucherEngineServiceException
	 */
	public CashierSessionReportDTO fetchDetailReport(int siteID, String cashierID,	String workstationNo, boolean isCurrentSession) throws VoucherEngineServiceException{
		ProgressIndicatorUtil.openInProgressWindow();
		CashierSessionReportDTO cashierSessionReportDTO = null;
		try {			
			cashierSessionReportDTO = VoucherServiceLocator.getService().fetchDetailReport(siteID, cashierID, workstationNo, isCurrentSession);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return cashierSessionReportDTO;
	}
	
	/**
	 * This method gets the EmpId logged on for a given workStation on a particular site.
	 * 
	 * @param siteId 
	 * @param workStation
	 *
	 */
	public String getEmpIdForLockedWorkstation (String workStation, int siteId) throws VoucherEngineServiceException{		

		try {			
			return VoucherServiceLocator.getService().getEmpIdForLockedWorkstation(workStation, siteId);
		} catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
	}

	public int getStatID(String barcode, int siteId) throws VoucherEngineServiceException{
	    
	    try {			
		return VoucherServiceLocator.getService().getStatID( barcode, siteId);
        	} catch (RuntimeException e) {
        		if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
        				e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
        			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
        		}
        		throw e;
        	} catch(VoucherEngineServiceException e) {
        		throw e;
        	} catch(Exception e) {
        		throw new VoucherEngineServiceException(e);
        	} finally {
        		ProgressIndicatorUtil.closeInProgressWindow();
        	}
	}

	/**
	 * This method checks for the following:
	 * 	a. if the passed ticket bar-code passes the basic CRC verification
	 * 	b. if the passed ticket asset is valid and available
	 * 	c. if the passed ticket sequence is reasonable as per ticket sequence. 
	 * 		A Reasonability check on the ticket sequence includes:
	 * 		  Printed Date/Time compared to the previous ticket generated by the machine (using sequence number).  
	 * 			This ticket should not precede the date/time of the previous ticket.
	 * 		(ii) Printed Date/Time compared to the following ticket generated by the machine (using sequence number).  
	 * 			This ticket should not exceed the date/time of the following ticket.

  	 * @param TicketInfoDTO ticket
	 * @param int siteId
	 * @return TicketInfoDTO
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO verifyVoucherForOverride(TicketInfoDTO ticketInfoDTO,	int siteId) throws VoucherEngineServiceException {
		TicketInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			infoDTO = VoucherServiceLocator.getService().verifyVoucherForOverride(ticketInfoDTO, siteId);
		}catch (RuntimeException e) {
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		}catch (VoucherEngineServiceException e) {
			throw e;
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}	

		return infoDTO;
	}

	/**
	 * The method takes care of the following:
	 *	(a) Insert new ticket records:
	 *		  The issue date/time will be the current system time.
	 *		(ii) The ticket type will be cashable.
	 *		(iii) The stand will be the current Location defined for the Asset.
	 *		(iv) The ticket status will be redeemed.
	 *		(v) The redemption date/time will be the current system time.
	 *		(vi) The supervisor login will be stored as the override authorization.
	 *	  When the application is processing the tickets, it may find an existing record in the SDS active ticket table 
	 *		(in the case that the ticket was delayed in communication from the floor).
	 *		The existing record should be updated as redeemed by the current cashier login and location.

	 * @param ticketInfoList
	 * @param siteId
	 * @return List<TicketInfoDTO> tickets with processing status
	 * @throws VoucherEngineServiceException
	 */
	public List<TicketInfoDTO> overrideVouchers(List<TicketInfoDTO> ticketInfoList, int siteId) throws VoucherEngineServiceException {
		List<TicketInfoDTO> ticketInfoDTOList = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			ticketInfoDTOList = VoucherServiceLocator.getService().overrideVouchers(ticketInfoList, siteId);
		} catch (RuntimeException e) {			
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
		} catch (VoucherEngineServiceException e) {			
			throw e;
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}	

		return ticketInfoDTOList;
	}

	/**
	 * This API will provide you Current SessionId for the entered employee Id.
	 * @param empLoginId
	 * @param siteId
	 * @return getCurrentSessionId
	 * @throws VoucherEngineServiceException
	 */
	public long getCurrentSessionId( String empLoginId, int siteId ) throws VoucherEngineServiceException {
		long currentSessionId = 0l;
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			currentSessionId = VoucherServiceLocator.getService().getCurrentSessionId(empLoginId, siteId);
		} catch (RuntimeException e) {			
			if(e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG1) ||
					e.getMessage().contains(IVoucherConstants.SERVICE_UNAVAILABLE_MSG2)) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
			}
			throw e;
			
		} catch (VoucherEngineServiceException e) {			
			throw e;
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}	
		return currentSessionId;
	}

}
