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

import java.util.List;

import com.ballydev.sds.employee.biz.dto.SessionInfoDTO;
import com.ballydev.sds.framework.service.SDSServiceLocator;
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
import com.ballydev.sds.voucher.dto.TransactionDetailsDTO;
import com.ballydev.sds.voucher.dto.TxnReasonsInfoDTO;
import com.ballydev.sds.voucher.dto.UserInfoDTO;
import com.ballydev.sds.voucher.enumconstants.VoucherAssetTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucher.service.IVoucher;


/**
 * @author Nithya kalyani R
 * @version $Revision: 28$ 
 */
public class VoucherEJBService implements IVoucherService {

	/**
	 * Inatance of IVoucher
	 */
	private IVoucher voucher;

	/**
	 * Constructor of the class
	 */
	public VoucherEJBService() throws VoucherEngineServiceException{
		initialize();
	}

	/**
	 * This method gets the voucher service
	 * @throws VoucherEngineServiceException
	 */
	private void initialize()  throws VoucherEngineServiceException {
		try {
			SDSServiceLocator<IVoucher> serviceFactory = new SDSServiceLocator<IVoucher>();
			voucher = serviceFactory.fetchEJBService("Voucher");
		} catch(VoucherEngineServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#isSlotNumberAvailable(java.lang.String)
	 */
	public boolean isSlotNumberAvailable(String assetLocation,int siteId) throws VoucherEngineServiceException {
		return voucher.isSlotNumberAvailable(assetLocation,siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#createVoucherList(com.ballydev.sds.voucher.dto.TicketInfoDTO, int)
	 */
	public List<TicketInfoDTO> createVoucherList(TicketInfoDTO ticketFormDTO, int noOfTickets) throws VoucherEngineServiceException {
		return voucher.createVoucherList(ticketFormDTO, noOfTickets);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#voidVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO voidVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.voidVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#inquireVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO inquireVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.inquireVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#reprintVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO reprintVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.reprintVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#scanReclVouchers(com.ballydev.sds.voucher.dto.ReconciliationInfoDTO)
	 */
	public ReconciliationInfoDTO scanReclVouchers(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException {
		return voucher.scanReclVouchers(reconciliationInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#showBatch(com.ballydev.sds.voucher.dto.BatchInfoDTO)
	 */
	public BatchSummaryDTO showBatch(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		return voucher.showBatch(batchInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getVouchersForBatch(com.ballydev.sds.voucher.dto.ReconciliationInfoDTO)
	 */
	public ReconciliationBatchInfoDTO getVouchersForBatch(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException {
		return voucher.getVouchersForBatch(reconciliationInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#cancelRedeemVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO cancelRedeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.cancelRedeemVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#overrideRedeemVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO overrideRedeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.overrideRedeemVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#redeemRequestVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO redeemRequestVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.redeemRequestVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#redeemRequestVoucherWithoutStatusCheck(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO redeemRequestVoucherWithoutStatusCheck(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.redeemRequestVoucherWithoutStatusCheck(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#redeemVoucher(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO redeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.redeemVoucher(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#redeemVoucherWithoutStatusCheck(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public TicketInfoDTO redeemVoucherWithoutStatusCheck(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException {
		return voucher.redeemVoucherWithoutStatusCheck(ticketInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#updateQuestionableVoucher(com.ballydev.sds.voucher.dto.BatchInfoDTO)
	 */
	public QuestionableReportInfoDTO updateQuestionableVoucher(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		return voucher.updateQuestionableVoucher(batchInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getTransactionReasons()
	 */
	public TxnReasonsInfoDTO getTransactionReasons() throws VoucherEngineServiceException {
		return voucher.getTransactionReasons();
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#validateOfflineTicket(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public OfflineTicketInfoDTO validateOfflineTicket(TicketInfoDTO infoDTO) throws VoucherEngineServiceException {
		return voucher.validateOfflineTicket(infoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#changeLocation(com.ballydev.sds.voucher.dto.BatchInfoDTO)
	 */
	public BaseDTO changeLocation(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		return voucher.changeLocation(batchInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#addReclVoucherByBatchId(com.ballydev.sds.voucher.dto.BatchInfoDTO, java.lang.String)
	 */
	public TicketInfoDTO addReclVoucherByBatchId(BatchInfoDTO batchInfoDTO, String barcode) throws VoucherEngineServiceException {
		return voucher.addReclVoucherByBatchId(batchInfoDTO, barcode);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#cancelBatch(com.ballydev.sds.voucher.dto.BatchInfoDTO)
	 */
	public BaseDTO cancelBatch(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException {
		return voucher.cancelBatch(batchInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#deleteTicketFromBatch(com.ballydev.sds.voucher.dto.BatchInfoDTO, java.lang.String)
	 */
	public TicketInfoDTO deleteTicketFromBatch(BatchInfoDTO batchInfoDTO, String barcode) throws VoucherEngineServiceException {
		return voucher.deleteTicketFromBatch(batchInfoDTO, barcode);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#updateTicketInBatch(com.ballydev.sds.voucher.dto.BatchInfoDTO, java.lang.String, java.lang.String)
	 */
	public TicketInfoDTO updateTicketInBatch(BatchInfoDTO batchInfoDTO, String barcode, String modifiedBarcode) throws VoucherEngineServiceException {
		return voucher.updateTicketInBatch(batchInfoDTO, barcode, modifiedBarcode);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#isAssetAvailable(java.lang.String)
	 */
	public VoucherAssetTypeEnum isAssetAvailable(String assetNumber,int siteId)throws VoucherEngineServiceException {
		return voucher.isAssetAvailable(assetNumber,siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getBatchSummary()
	 */
	public BatchSummaryDTO getBatchSummary(int siteId)throws VoucherEngineServiceException {
		return voucher.getBatchSummary(siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#insertReconciliation(com.ballydev.sds.voucher.dto.ReconciliationInfoDTO)
	 */
	public ReconciliationInfoDTO insertReconciliation(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException {
		return voucher.insertReconciliation(reconciliationInfoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getTimeDetails(java.lang.String)
	 */
	public List<String> getTimeDetails(String user,int siteId)throws VoucherEngineServiceException {
		return voucher.getTimeDetails(user,siteId);
	}

	public void insertPlayerCardId(String barcode, String transPlayerId,int siteId) throws VoucherEngineServiceException {
		voucher.insertPlayerCardId(barcode, transPlayerId,siteId);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#voidVoucherList(java.util.List)
	 */
	public void voidVoucherList(List<TicketInfoDTO> ticketInfoDTOList) throws VoucherEngineServiceException {
		voucher.voidVoucherList(ticketInfoDTOList);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getVoucherDetails(java.lang.String, java.lang.String)
	 */
	public EnquiryInfoDTO getVoucherDetails(String barcode, String amount,int siteId)throws VoucherEngineServiceException {
		return voucher.getVoucherDetails(barcode, amount,siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#sendUpdateRedeemRequest(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public void sendUpdateRedeemRequest(TicketInfoDTO ticketInfoDTO) throws Exception{
		voucher.sendUpdateRedeemRequest(ticketInfoDTO);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#sendVoidEntryForPromo(com.ballydev.sds.voucher.dto.TicketInfoDTO)
	 */
	public void sendVoidEntryForPromo(TicketInfoDTO ticketInfoDTO)	throws Exception {
		voucher.sendVoidEntryForPromo(ticketInfoDTO);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#chkEmpTotalAmt(java.lang.String, java.lang.String, int)
	 */
	public void chkEmpTotalAmt(String userName, String amt, int siteId)
	throws VoucherEngineServiceException {
		voucher.chkEmpTotalAmt(userName, amt, siteId);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#chkWorkStation(java.lang.String)
	 */
	public boolean chkWorkStationSession(UserInfoDTO infoDTO) throws Exception {
		return voucher.chkWorkStationSession(infoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#removeWorkStation(java.lang.String)
	 */
	public void removeWorkStationSession(UserInfoDTO infoDTO) throws Exception {
		voucher.removeWorkStationSession(infoDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#insertTransWithErrorCode(com.ballydev.sds.voucher.dto.TicketInfoDTO, com.ballydev.sds.voucher.dto.TransactionDetailsDTO)
	 */
	public void insertTransWithErrorCode(TicketInfoDTO ticketInfoDTO, TransactionDetailsDTO transactionDetailsDTO) throws VoucherEngineServiceException{
		voucher.insertTransWithErrorCode(ticketInfoDTO, transactionDetailsDTO);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getSlipDropTime(int)
	 */
	public long getSlipDropTime(int siteNumber){
		return voucher.getSlipDropTime(siteNumber);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getAmountType(java.lang.String, int)
	 */
	public int getAmountType(String barcode,int siteId)throws VoucherEngineServiceException {
		return voucher.getAmountType(barcode,siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#isValidEmployee(java.lang.String, int)
	 */
	public boolean isValidEmployee(String userName, int siteId) throws VoucherEngineServiceException {

		return voucher.isValidEmployee(userName, siteId);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#createSession(com.ballydev.sds.voucher.dto.UserInfoDTO)
	 */
	public void createSession(UserInfoDTO userInfoDTO)	throws VoucherEngineServiceException {
		voucher.createSession(userInfoDTO);
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#createSession(com.ballydev.sds.voucher.dto.UserInfoDTO)
	 */
	public void createSessionWhenPaused(UserInfoDTO userInfoDTO)	throws VoucherEngineServiceException {
		voucher.createSessionWhenPaused(userInfoDTO);
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#createSession(com.ballydev.sds.voucher.dto.UserInfoDTO)
	 */
	public void signOnSession(UserInfoDTO userInfoDTO)	throws VoucherEngineServiceException {
		voucher.signOnSession(userInfoDTO);
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#createSession(com.ballydev.sds.voucher.dto.UserInfoDTO)
	 */
	public void signOffSession(UserInfoDTO userInfoDTO)	throws VoucherEngineServiceException {
		voucher.signOffSession(userInfoDTO);
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#getSessionStatus(com.ballydev.sds.voucher.dto.UserInfoDTO)
	 */
	public SessionInfoDTO isWorkStationLoggedIn(String employeeId, String location, int siteId) throws VoucherEngineServiceException {
		return voucher.isWorkStationLoggedIn(employeeId, location, siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#updateSessionForceStatus(com.ballydev.sds.voucher.dto.UserInfoDTO)
	 */
	public void updateSessionForceStatus(UserInfoDTO userInfoDTO) throws VoucherEngineServiceException {
		voucher.updateSessionForceStatus(userInfoDTO);
		
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#fetchSummaryReport(int, java.lang.String, java.lang.String, boolean)
	 */
	public CashierSessionReportDTO fetchSummaryReport(int siteID, String cashierID,	String workstationNo, boolean isCurrentSession) throws VoucherEngineServiceException {
		return voucher.fetchSummaryReport(siteID, cashierID, workstationNo, isCurrentSession);
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#fetchDetailReport(int, java.lang.String, java.lang.String, boolean)
	 */
	public CashierSessionReportDTO fetchDetailReport(int siteID, String cashierID,String workstationNo, boolean isCurrentSession) throws VoucherEngineServiceException {
		return voucher.fetchDetailReport(siteID, cashierID, workstationNo, isCurrentSession);
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#fetchDetailReport(int, java.lang.String, java.lang.String, boolean)
	 */
	public String getEmpIdForLockedWorkstation(String workstationNo, int siteID) throws VoucherEngineServiceException {
		return voucher.getUserofLockedWorkstation(workstationNo, siteID);
	}
	
	public int getStatID(String barcode, int siteId) throws VoucherEngineServiceException {
	    return voucher.getStatID(barcode, siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#verifyVoucherForOverride(com.ballydev.sds.voucher.dto.TicketInfoDTO, int)
	 */
	public TicketInfoDTO verifyVoucherForOverride(TicketInfoDTO ticket, int siteId) throws VoucherEngineServiceException {
		return voucher.verifyVoucherForOverride(ticket, siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#overrideVouchers(java.util.List, int)
	 */
	public List<TicketInfoDTO> overrideVouchers(List<TicketInfoDTO> tickets, int siteId) throws VoucherEngineServiceException {
		return voucher.overrideVouchers(tickets, siteId);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.service.IVoucherService#overrideVouchers(java.util.List, int)
	 */
	public long getCurrentSessionId( String empLoginId, int siteId ) throws VoucherEngineServiceException {
		return voucher.getCurrentSessionId(empLoginId, siteId);
	}

}
