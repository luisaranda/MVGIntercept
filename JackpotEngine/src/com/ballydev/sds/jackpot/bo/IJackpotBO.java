/*****************************************************************************
 * $Id: IJackpotBO.java,v 1.40.1.2.1.0.2.0, 2012-05-22 06:05:00Z, Thilak Raj  Chandramohan$
 * $Date: 5/22/2012 1:05:00 AM$
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
package com.ballydev.sds.jackpot.bo;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.ballydev.sds.common.jackpot.ICreditKeyOffBlock;
import com.ballydev.sds.common.jackpot.IHandPaidJackpot;
import com.ballydev.sds.common.jackpot.IJackpotClear;
import com.ballydev.sds.common.jackpot.IJackpotToCreditMeter;
import com.ballydev.sds.jackpot.dto.JPCashlessProcessInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotEmployeeInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotLiabilityDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.dto.JackpotResetDTO;
import com.ballydev.sds.jackpot.dto.NewWaveRequestDTO;
import com.ballydev.sds.jackpot.dto.NewWaveResponseDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpot.util.JpGeneratedBy;

/**
 * This Business Object Interface is also an EJB 
 * which is used for InterEngine Communication. 
 * @author dambereen
 * @version $Revision: 46$
 */
public interface IJackpotBO {
	
	/* METHODS FOR SLOT FLOOR EVENTS THAT ARE IN THE JACKPOT EVENTS DAO CLASS*/
	
	/**
	 * Method to create a pending jp slip when an XC -10 event occurs
	 * @param handPaidJackpot
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO createPendingJackpotSlips(IHandPaidJackpot handPaidJackpot, JackpotDTO jackpotDTO, boolean slotDoorOpenStatus)throws JackpotDAOException;
	
	/**
	 * Method to Credit Key Off a pending slip when a XC-30 event occurs
	 * @param jackpotToCreditMeter
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO createJackpotToCreditMeterSlips(IJackpotToCreditMeter jackpotToCreditMeter, JackpotDTO jackpotDTO) throws JackpotDAOException;
	
	/**
	 * Method to log the Jp Clear event when a XC-52 event occurs
	 * @param jackpotClear
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO jackpotClearEvent(IJackpotClear jackpotClear, long slipPrimaryKey)	throws JackpotDAOException;
	
	/**
	 * Method to check the duplicate message id for the event that occured
	 * @param messageId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO eventDuplicateMsgCheckGetResponse(long messageId)throws JackpotDAOException;	
	
	/**
	 * Method to process the Credit key Off Auth Block
	 * @param creditKeyOffBlk
	 * @param slipPrimaryKey
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<String> creditKeyOffAuthBlockProcessing(ICreditKeyOffBlock creditKeyOffBlk, long slipPrimaryKey)throws JackpotDAOException;
	
	/**
	 * Method to lock the Jackpot Slot table on a particular slot no
	 * @param acnfNo
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean lockJackpotSlotTable(String acnfNo, int siteId) throws JackpotDAOException;
	
	/**
	 * Method to get all the pending jackport data to cache for sending alerts
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getAllPendingJackpotForCache() throws JackpotDAOException;
	
	/* METHODS IN THE JACKPOT DAO CLASS */	
	/**
	 * Method to get all the pending jp slips for the site id passed
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId)	throws JackpotDAOException;	

	/**
	 * Method to get the pending jp slips for the site id passed based one the Filter - Type (SlotNo/SlotLoc/SeqNo/Minutes)
	 * @param filter
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getJackpotDetails(JackpotFilter filter) throws JackpotDAOException;
	
	/**
	 * Method to get the reprint jp details for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @param cashierDeskEnabled
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber,	int siteId, String cashierDeskEnabled) throws JackpotDAOException;
	
	/**
	 * Method to get the Void Jp details for the Seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId)throws JackpotDAOException;
	
	/**
	 * Method to get the Express Jp Blind Attempts for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public short getExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotDAOException;
	
	/**
	 * Method to post the Express Jp Blind Attempts for a NonCarded Jp for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean postNonCardedExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotDAOException;
	
	/**
	 * Method to post the Express Jp Blind Attempts for the Seq No passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean postExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotDAOException;
	
	/**
	 * Method to update the blind attempt as -1 for a unsuccessful exp jp processing that is aborted
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 * @author dambereen
	 */
	public boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNumber, int siteId) throws JackpotDAOException;
	
	/**
	 * Method to get the Jp Slip status
	 * @param sequenceNumber
	 * @param siteId
	 * @return 
	 * @throws JackpotDAOException
	 */
	public short getJackpotStatus(long sequenceNumber, int siteId)throws JackpotDAOException;
	
	/**
	 * Method to get details whether the JP is posted to accounting or not 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getJackpotPostToAccountDetail(long sequenceNumber, int siteId)throws JackpotDAOException;
		
	/**
	 * Method to get the Jackpot slip status for a void method that will return the Transaction date based one the status and the current day
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO getJackpotStatusForVoid(long sequenceNumber, int siteId)throws JackpotDAOException ;
	
	/**
	 * Method to get the Jp Slip details that was last processed
	 * @param siteId
	 * @param cashierDeskEnabled
	 * @return
	 * @throws JackpotDAOException
	 */
	@Deprecated
	public JackpotDTO getReprintPriorSlipDetails(int siteId, String cashierDeskEnabled)throws JackpotDAOException;
	
	/**
	 * Method to get the Jp details to print the report based on the Date and Site id
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @param cashierDeskEnabled
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(int siteId,String fromDate, String toDate) throws JackpotDAOException;
	
	/**
	 * Method to get the Jp details to print the report based on the Date,Employee and Site id
	 * @param siteId
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(int siteId, String employeeId, String fromDate, String toDate)throws JackpotDAOException;
	
	/**
	 * Method to process a ready to pay jackpot slip to Processed JP slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO updateProcessJackpot(JackpotDTO jackpotDTO) throws JackpotDAOException;
	
	/**
	 * Method to process a printed jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 * @author vsubha
	 */
	public JackpotDTO updatePrintJackpot(JackpotDTO jackpotDTO) throws JackpotDAOException;
	
	/**
	 * Method to process all the jackpots before closing the cashless account from cage.
	 * 
	 * @param accountNumber
	 * @param siteId
	 * @param employeeId
	 * @param employeeFirstName
	 * @param employeeLastName
	 * @param cashDeskLocation
	 * @param validateEmpSession
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updatePrintJackpots(String accountNumber, Integer siteId, String employeeId, String employeeFirstName, String employeeLastName, String cashDeskLocation, boolean validateEmpSession) throws JackpotDAOException;
	
	/**
	 * Method to process a manual jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO updateProcessManualJackpot(JackpotDTO jackpotDTO)throws JackpotDAOException;
	
	/**
	 * Method to update the jackpot status on a void
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO postUpdateJackpotVoidStatus(JackpotDTO jackpotDTO)throws JackpotDAOException;
	
	/**
	 * Method to update the jackpot status on a reprint
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO postUpdateJackpotReprint(JackpotDTO jackpotDTO)throws JackpotDAOException;
	
	/**
	 * Method to void all the pending jackpot slips based on the site id
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidAllPendingJackpotSlips(JackpotDTO jackpotDTO)throws JackpotDAOException;		
	
	/**
	 * Method to void all the pending jackpot slips based on the slot and site id
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> updateVoidPendingJackpotSlipsForSlotNo(JackpotDTO jackpotDTO)throws JackpotDAOException;
	
	//******************** METHODS EXPOSED TO OTHER ENGINES ******************/
	
	/* METHOD IS CALLED BY THE AUDIT ENGINE */
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> voidPendingJackpotSlipsForAuditProcess(int siteId, long startTime, long endTime, String employeeId, String kioskProcessed) throws JackpotDAOException;
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id, gaming day and slot no
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param slotNo
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> voidPendingJackpotSlipsWithSlotForAuditProcess(int siteId, long startTime, long endTime, String slotNo, String employeeId, String kioskProcessed) throws JackpotDAOException;
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> voidAllPendingJackpotSlipsForAuditProcess(int siteId, String employeeId, String kioskProcessed) throws JackpotDAOException;
		
	/* WEB METHOD TO SECURITY ENGINE */
	/**
	 * Method to insert a record on an employee card on when a jp is hit in the slot machine
	 * @param employeeInfoDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean jackpotCardedEmployeeInfo(JackpotEmployeeInfoDTO employeeInfoDTO)throws JackpotDAOException;
	
	/* WEB METHODS TO ACCOUNTING */
	/**
	 * Method to get the slip transfer table's slip count 
	 * @return
	 * @throws JackpotDAOException
	 */
	public Long getSlipsByCount() throws JackpotDAOException;	
	
	/**
	 * Method to get the slip status and HPJP amount for the given sequence number and Site ID
	 * 
	 * @param sequenceNumber
	 * @return statusDesc
	 * @throws HibernateException
	 * @throws Exception
	 *             Throws Exception if jackpot amount retrieval failed
	 * @author vsubha
	 */
	public JPCashlessProcessInfoDTO getJackpotStatusAmount(long sequenceNumber, int siteId) throws JackpotDAOException;
	
	/**
	 * Method to retrieve Slip details By Sequence number and site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotDTO retrieveSlipBySequenceNumber(long sequenceNumber, int siteId ) throws JackpotEngineServiceException;
	
	/* WEB METHODS TO PROGRESSIVE */
	/**
	 * Method that returns the Processed Progressive Jackpot Info
	 * @param siteId
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDetailsDTO> getProgressiveJackpotInfo(int siteId, long fromTime, long toTime) throws JackpotDAOException;
	
	/* WEB METHODS TO JACKPOT S2S */
	
	/**
	 * Method to get the Jackpot Details for S2S
	 * @param sequenceNo
	 * @param siteId
	 * @return JackpotDetailsDTO
	 * @throws JackpotDAOException
	 */
	public JackpotDetailsDTO getJackpotDetailsForS2S(long sequenceNo, int siteId) throws JackpotDAOException;
	
	/**
	 * Method to insert the pouch pay attendant who did the Pouch Pay
	 * @param slipReference
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean insertPouchPayAttendant(JackpotDTO jackpotDTO)throws JackpotDAOException ;
	
	/* WEB METHODS TO DASHBOARD */
	
	/**
	 * Method to provide the total count of the jackpots that have been processed and reprinted
	 * @param fromDate
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public int getNumOfJackpots(Date fromDate, int siteId) throws JackpotDAOException;
	
	/**
	 * Method to provide the total count of the jackpots that have been processed and reprinted
	 * @param fromDate
	 * @param toDate
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public int getNumOfJackpots(Date fromDate,Date toDate, int siteId) throws JackpotDAOException;
	
	/* WEB METHODS TO CAGE */
	/**
	 * Method to show if all jackpots are processed or not 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean isAllJackpotProcessed(String accountNumber, int siteId) throws JackpotDAOException;
	
	/**
	 * Method to show if all jackpots are processed or not 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public List<JPCashlessProcessInfoDTO> getAllUnProcessedJackpot(String accountNumber, int siteId) throws JackpotDAOException; 
	
	
	/**
	 * Method used to post the latest progressive jackpot amount from the progressive engine.
	 * @param acnf_number
	 * @param msgId
	 * @param jackpotId
	 * @param sdsAmount
	 * @param pmuAmount
	 */
	public void postProgressiveJackpotAmount(String acnf_number, Long msgId, Long sdsAmount, Long pmuAmount, int siteId)throws JackpotDAOException;
	
	
	/**
	 * This Method is exposed for the progressive jackpot engine for it to check whether the last hit jackpot for this slot is progressive or not.
	 * @param siteNumber
	 * @param slotNumber
	 * @return
	 * @throws JackpotDAOException
	 */
	public boolean isLastHitProgressive (int siteNumber, String slotNumber)throws JackpotDAOException;
	
	
	
	/**
	 * Method to fetch the jackpot details for New Wave.
	 * @param newWaveRequestDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<NewWaveResponseDTO> getNewWaveJackpotDetails(NewWaveRequestDTO newWaveRequestDTO)throws JackpotDAOException;
	
	/**
	 * Method to get the Pending Jackpots that were not reset
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotResetDTO> getUnResetPendingJackpots(int siteId)throws JackpotDAOException;
	
	/**
	 * Method to return the slip reference id for a particular slip seq no and site no
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotDAOException
	 */
	public long getSlipReferenceIdForSlipSeq(int siteNo, long sequenceNo)throws JackpotDAOException;	
	
	/**
	 * Method to auto void reprinted sequence and create a new slip seq in Slip Ref table
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public JackpotDTO postUpdateJpAutoVoidReprint(JackpotDTO jackpotDTO) throws JackpotDAOException;
	
	/**
	 * Method to get Jackpot Liability details for DEG
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotDAOException
	 * @author vsubha
	 */
	public JackpotLiabilityDetailsDTO getJackpotLiabilityDetailsForDEG(int siteId, String fromDate,
			String toDate) throws JackpotDAOException;
	
	/**
	 * Method to create a jackpot slip in the db
	 * @param jackpotDTO
	 * @param transExcepCode
	 * @param jpGeneratedBy
	 * @return JackpotDTO
	 * @throws JackpotDAOException
	 * @author dambereen
	 */
	public JackpotDTO createJackpotFromExternalSystem(JackpotDTO jackpotDTO, short transExcepCode, JpGeneratedBy jpGeneratedBy) throws JackpotDAOException;
	
	/**
	 * @param empId
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> getAllActiveJackpotsForEmpId(String empId, int siteId) throws JackpotEngineServiceException;
	
	/**
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotDTO> getJackpotDetails(JackpotDTO jackpotDTO) throws JackpotDAOException;
}
