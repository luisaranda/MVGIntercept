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

/**
 * @author Nithya kalyani R
 * @version $Revision: 29$ 
 */
public interface IVoucherService {

	/**
	 * This is the overloaded method that calls the create voucher 
	 * method multiple times
	 * @param ticketFormDTO
	 * @param noOfTickets
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public List<TicketInfoDTO> createVoucherList(TicketInfoDTO ticketFormDTO,int noOfTickets) throws VoucherEngineServiceException;

	/**
	 * This method checks if the passed slot number is a vaild slot or not
	 * @param assetLocation
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public boolean isSlotNumberAvailable(String assetLocation,int siteId) throws VoucherEngineServiceException;

	/**
	 * This method voids the passed ticket
	 * @param ticketFormDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO voidVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method returns the ticket details for the passed barcode
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO inquireVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method reprints the voucher whose status is ACTIVE
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO reprintVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;


	/**
	 * Saves a new Reconciled Voucher record when a ticket is scanned during reconciliation.
	 * Also creates the corresponding recl vouchers.
	 * @param reconciliationInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public ReconciliationInfoDTO scanReclVouchers(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException;

	/**
	 * Fetches the batch details with the given criteria
	 * @param batchInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BatchSummaryDTO showBatch(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException;

	/**
	 * Returns a list of Batch details for a given batch Id.
	 * @param reconciliationInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public ReconciliationBatchInfoDTO getVouchersForBatch(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException;
	/**
	 * This method processes the redeem request of a voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemRequestVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method redeems the voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method redeems the voucher regardless of the status
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemVoucherWithoutStatusCheck(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;


	/**
	 * This method processes the redeem request of a voucher regardless of the status
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO redeemRequestVoucherWithoutStatusCheck(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method overrides the redeem voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO overrideRedeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method cancels the redeem voucher
	 * @param ticketInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO cancelRedeemVoucher(TicketInfoDTO ticketInfoDTO) throws VoucherEngineServiceException;
	/**
	 * This takes care of running the rules for the Questionable tickets and
	 * stamping the ticket with the corresponding Auditcodes
	 * @param BatchInfoDTO
	 * @return
	 */
	public QuestionableReportInfoDTO updateQuestionableVoucher(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException;

	/**
	 *  Retrives the list of Transaction Reasons
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TxnReasonsInfoDTO getTransactionReasons()  throws VoucherEngineServiceException;

	/**
	 * Verify the tickets by calling the crc barcode helper
	 * @param infoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public OfflineTicketInfoDTO validateOfflineTicket(TicketInfoDTO infoDTO) throws VoucherEngineServiceException;
	/**
	 * This method changes the location/employee for the passed batch
	 * @param baseDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BaseDTO changeLocation(BatchInfoDTO batchInfoDTO)  throws VoucherEngineServiceException;
	/**
	 * This method cancels the particular batch
	 * @param batchInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BaseDTO cancelBatch(BatchInfoDTO batchInfoDTO) throws VoucherEngineServiceException;

	/**
	 * This method deletes the voucher from the batch
	 * @param batchInfoDTO
	 * @param barCode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO deleteTicketFromBatch(BatchInfoDTO batchInfoDTO, String barcode) throws VoucherEngineServiceException;

	/**
	 * This method adds reconcile vouchers by batch
	 * @param batchInfoDTO
	 * @param barCode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO addReclVoucherByBatchId(BatchInfoDTO batchInfoDTO, String barcode) throws VoucherEngineServiceException;

	/**
	 * This method updates the voucher in batch
	 * @param batchInfoDTO
	 * @param barCode
	 * @param modifiedBarCode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public TicketInfoDTO updateTicketInBatch(BatchInfoDTO batchInfoDTO, String barcode, String modifiedBarcode) throws VoucherEngineServiceException;

	/**
	 * This method checks whether the passed asset number is a valid asset or not
	 * @param assetNumber
	 * @return
	 */	
	public VoucherAssetTypeEnum isAssetAvailable(String assetNumber,int siteId) throws VoucherEngineServiceException;

	/**
	 * This method gets the list of batch details available
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public BatchSummaryDTO getBatchSummary(int siteId) throws VoucherEngineServiceException ;
	/** This method inserts the reconciliation details to the reconciliation table
	 * @param reconciliationDTO
	 * @return ReconciliationDTO
	 */	
	public ReconciliationInfoDTO insertReconciliation(ReconciliationInfoDTO reconciliationInfoDTO) throws VoucherEngineServiceException;	 
	/**
	 * This method returns the min and max value of time stamp 
	 * user passed
	 * @param user
	 * @return
	 * @throws VoucherDAOException
	 */
	public List<String> getTimeDetails(String user,int siteId) throws VoucherEngineServiceException;

	/**
	 * This method inserts the player card id
	 * for the given barcode
	 * @param transDTO
	 * @param transPlayerId
	 * @return
	 * @throws VoucherDAOException
	 */
	public void insertPlayerCardId(String barcode,String transPlayerId,int siteId) throws VoucherEngineServiceException;

	/**
	 * This method voids the passed ticket
	 * @param ticketFormDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public void voidVoucherList(List<TicketInfoDTO> ticketInfoDTOList) throws VoucherEngineServiceException;

	/**
	 * This method gets the list of transactions associated for the barcode and amount passed
	 * @param barcode
	 * @param amount
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public EnquiryInfoDTO getVoucherDetails(String barcode,String amount,int siteId)  throws VoucherEngineServiceException;

	/**
	 * This method sends the update redeem to EVS
	 * @param ticketInfoDTO
	 */
	public void sendUpdateRedeemRequest(TicketInfoDTO ticketInfoDTO) throws Exception;

	/**
	 * This method sends the void request handled to EVS
	 * @param ticketInfoDTO
	 * @throws Exception
	 */
	public void sendVoidEntryForPromo(TicketInfoDTO ticketInfoDTO) throws Exception;

	/**
	 * This method checks whether the total emp amt is 
	 * in limit with the site config value set for the user
	 * @param userName
	 * @param amt
	 * @param siteId
	 * @throws VoucherEngineServiceException
	 */
	public void chkEmpTotalAmt(String userName , String amt,int siteId) throws VoucherEngineServiceException;

	/**
	 * This method checks if the given workstation is present in the hashmap
	 * and puts the value in the amp if it is not present already
	 * @param workStation
	 * @return
	 */
	public boolean chkWorkStationSession(UserInfoDTO infoDTO) throws Exception;

	/**
	 * This method removes the workstation value
	 * from the hash map
	 * @param workStation
	 */
	public void removeWorkStationSession(UserInfoDTO infoDTO) throws Exception;

	public void insertTransWithErrorCode(TicketInfoDTO ticketInfoDTO, TransactionDetailsDTO transactionDetailsDTO) throws VoucherEngineServiceException;

	public long getSlipDropTime(int siteNumber);

	/**
	 * This method returns the amount type id for the barcode passed
	 * @param barcode
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public int getAmountType(String barcode,int siteId) throws VoucherEngineServiceException;

	/**
	 * Method validate user name passed
	 * @param userName 
	 * @return boolean
	 * @throws VoucherEngineServiceException
	 */
	public boolean isValidEmployee(String userName, int siteId) throws VoucherEngineServiceException;
	
	/**
	 * This method inserts a record for the new session created
	 * @param userInfoDTO 
	 */
	public void createSession(UserInfoDTO userInfoDTO) throws VoucherEngineServiceException;
	
	/**
	 * This method inserts a record for the new session created and update the previous paused session.
	 * @param userInfoDTO 
	 */
	public void createSessionWhenPaused(UserInfoDTO userInfoDTO) throws VoucherEngineServiceException;
	
	/**
	 * This method sign on in the existing session for the emp
	 * @param userInfoDTO 
	 */
	public void signOnSession(UserInfoDTO userInfoDTO)	throws VoucherEngineServiceException;
	
	/**
	 * This method sign off in the existing session for the emp
	 * @param userInfoDTO 
	 */
	public void signOffSession(UserInfoDTO userInfoDTO)	throws VoucherEngineServiceException;
	
	/**
 	 * This method returns the session status
	 * @param employeeId
	 * @param location
	 * @param siteId
	 * @return SessionInfoDTO
	 * @throws VoucherEngineServiceException
	 */
	public SessionInfoDTO isWorkStationLoggedIn(String employeeId, String location, int siteId) throws VoucherEngineServiceException;
	
	/**
	 * This method updates the session status to 1 if the
	 * session is paused in the client side
	 * @param userInfoDTO
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public void updateSessionForceStatus(UserInfoDTO userInfoDTO) throws VoucherEngineServiceException;

	/**
	 * Returns the cashier session object for a particular cashier, cashier workstation
	 * and siteID 
	 * @param cashierID
	 * @param workstationNo
	 * @param isCurrentSession
	 * @return CashierSessionReportDTO
	 * @throws VoucherEngineServiceException
	 */
	
	public CashierSessionReportDTO fetchSummaryReport(int siteID, String cashierID,	String workstationNo, boolean isCurrentSession) throws VoucherEngineServiceException; 

	/**
	 * Returns the cashier session object including barcode details redeemed by a particular cashier in a cashier workstation
	 * @param cashierID
	 * @param workstationNo
	 * @param isCurrentSession
	 * @return CashierSessionReportDTO
	 * @throws VoucherEngineServiceException
	 */

	public CashierSessionReportDTO fetchDetailReport(int siteID, String cashierID,String workstationNo, boolean isCurrentSession) throws VoucherEngineServiceException;

	/**
	 * Returns the Employee Id for the locked workstation.
	 * @param workstationNo
	 * @param siteId
	 * @throws VoucherEngineServiceException
	 */	
	public String getEmpIdForLockedWorkstation(String workStation, int siteId) throws VoucherEngineServiceException;

	public int getStatID(String barcode, int siteId)throws VoucherEngineServiceException;

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
	public TicketInfoDTO verifyVoucherForOverride(TicketInfoDTO ticket,	int siteId) throws VoucherEngineServiceException;

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

  	 * @param List<TicketInfoDTO> tickets
	 * @param int siteId
	 * @return List<TicketInfoDTO> tickets with processing status
	 * @throws VoucherEngineServiceException 
	 */
	public List<TicketInfoDTO> overrideVouchers(List<TicketInfoDTO> tickets, int siteId) throws VoucherEngineServiceException;

	/**
	 * This API will provide you Current SessionId for the entered employee Id.
	 * @param empLoginId
	 * @param siteId
	 * @return getCurrentSessionId
	 * @throws VoucherEngineServiceException
	 */
	public long getCurrentSessionId( String empLoginId, int siteId ) throws VoucherEngineServiceException;

}
