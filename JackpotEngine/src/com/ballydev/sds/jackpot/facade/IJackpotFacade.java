/*****************************************************************************
 * $Id: IJackpotFacade.java,v 1.49.1.2.1.0.4.0, 2012-05-22 06:06:29Z, Thilak Raj  Chandramohan$
 * $Date: 5/22/2012 1:06:29 AM$
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
package com.ballydev.sds.jackpot.facade; 

import java.util.Date;
import java.util.List;

import com.ballydev.sds.common.message.SDSMessage;
import com.ballydev.sds.ecash.dto.account.PlayerSessionDTO;
import com.ballydev.sds.facade.IMessagingFacade;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JPCashlessProcessInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotEngineAlertObject;
import com.ballydev.sds.jackpot.dto.JackpotLiabilityDetailsDTO;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.dto.JackpotResetDTO;
import com.ballydev.sds.jackpot.dto.NewWaveRequestDTO;
import com.ballydev.sds.jackpot.dto.NewWaveResponseDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.barcodelib.barcode.Barcode;

/**
 * Entry Point for UI to Engine Communication, 
 * Only Remote communication will happen between UI and Engine.
 * UI has an option to have Webservice Communication with Engine.
 * @author dambereen
 */
public interface IJackpotFacade extends IMessagingFacade{
	
	/**
	 * Method to get all the pending jp slips for the site id passed
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId)	throws JackpotEngineServiceException;	

	/**
	 * Method to get the pending jp slips for the site id passed based one the Filter - Type (SlotNo/SlotLoc/SeqNo/Minutes)
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> getJackpotDetails(JackpotFilter filter) throws JackpotEngineServiceException;
	
	/**
	 * Method to get the reprint jp details for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber,	int siteId) throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Void Jp details for the Seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Express Jp Blind Attempts for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public short getExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to post the Express Jp Blind Attempts for a NonCarded Jp for the seq no passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public boolean postNonCardedExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to post the Express Jp Blind Attempts for the Seq No passed
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public boolean postExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to update the blind attempt as -1 for a unsuccessful exp jp processing that is aborted
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNumber, int siteId) throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Jp Slip status
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public short getJackpotStatus(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to get details whether the JP is posted to accounting or not 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getJackpotPostToAccountDetail(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
		
	/**
	 * Method to get the Jackpot slip status for a void method that will return the Transaction date based one the status and the current day
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO getJackpotStatusForVoid(long sequenceNumber, int siteId) throws JackpotEngineServiceException ;
	
	/**
	 * Method to get the Jp Slip details that was last processed
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	@Deprecated
	public JackpotDTO getReprintPriorSlipDetails(int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Jp details to print the report based on the Date and Site id
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(int siteId,String fromDate, String toDate) throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Jp details to print the report based on the Date,Employee and Site id
	 * @param siteId
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(int siteId, String employeeId, String fromDate, String toDate)throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Printer and Slip Schema xml files
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public String[] getJackpotXMLInfo() throws JackpotEngineServiceException;
	
	/**
	 * Method to process a pending jackpot slip to printed
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotDTO processPrintJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException; 
	
	/**
	 * Method to process a printed jackpot slip
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO processJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException;
	
	/**
	 * Method to process a manual jackpot slip
	 * @param jackpotDTO
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO processManualJackpot(JackpotDTO jackpotDTO, JackpotFilter filter) throws JackpotEngineServiceException;
	
	/**
	 * Method to update the jackpot status on a void
	 * @param jackpotDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO postJackpotVoidStatus(JackpotDTO jackpotDTO)throws JackpotEngineServiceException;
	
	/**
	 * Method to update the jackpot status on a reprint
	 * @param jackptoDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO postJackpotReprint(JackpotDTO jackptoDTO)throws JackpotEngineServiceException;
	
	/**
	 * Method to void all the pending jackpot slips based on the site id
	 * @param siteId
	 * @param voidEmpId
	 * @param actorLogin
	 * @param printerUsed
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> voidAllPendingJackpotSlips(int siteId,String voidEmpId,String printerUsed, String actorLogin, String kioskProcessed)throws JackpotEngineServiceException;
	
	/**
	 * Method to void all the pending jackpot slips based on the slot and site id
	 * @param slotNo
	 * @param siteId
	 * @param voidEmpId
	 * @param printerUsed
	 * @param actorLogin
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> voidPendingJackpotSlipsForSlotNo(String slotNo,int siteId,String voidEmpId, String printerUsed, String actorLogin, String kioskProcessed)throws JackpotEngineServiceException;
	
	/**
	 * Method to validate the Cashless Account Number
	 * 
	 * @param siteNo
	 * @param accountNumber
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public CashlessAccountDTO validateCashlessAccountNo(int siteNo, String accountNumber) throws JackpotEngineServiceException ;
	
	/**
	 * Method to check if Jackpot Process or Void can be allowed for the Player Cashless Account.
	 * 
	 * @param accessID
	 * @param siteID
	 * @param operationType
	 * @return CashlessAccountDTO
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public CashlessAccountDTO isJackpotOperationAllowed(String accessID, int siteID, short operationType) throws JackpotEngineServiceException;
	
	/**
	 * Method to check if the player card number entered is valid or not.
	 * 
	 * @param playerCardNumber
	 * @param siteID
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public long getPlayerCardId(String playerCardNumber, int siteID) throws JackpotEngineServiceException;
	
	/**
	 * Method to get the player name for the player card number.
	 * 
	 * @param playerCardNumber
	 * @param siteID
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getPlayerCardName(String playerCardNumber, int siteID) throws JackpotEngineServiceException;
	
	/* METHOD IS CALLED TO GET THE ASSET DETAILS FROM THE UI */
	
	/**
	 * Method to get the asset details based on the Slot No / Stand No / AcnfId
	 * @param object
	 * @param jackpotAssetParamType
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotAssetInfoDTO getJackpotAssetInfo(Object object, JackpotAssetParamType jackpotAssetParamType, int siteid)throws JackpotEngineServiceException;
	
	/* METHOD TO SEND JACKPOT ADJUSTEMNT TO MARKETING FROM THE UI SIDE FOR A PENDING JP*/
	
	/**
	 * Method to send the jackpot adjustemnt to Marketing for a processed pending jp slip 
	 * @param siteId
	 * @param origCardNo
	 * @param newCardNo
	 * @param oldAmount
	 * @param newJackpotAmount
	 * @param slotNo
	 * @param msgiD
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public int processJackpotAdjustment(int siteId, String origCardNo, String newCardNo,long oldAmount, long newJackpotAmount, String slotNo, long msgiD)throws JackpotEngineServiceException;
	
	/* METHOD TO SEND JACKPOT ADJUSTEMNT TO MARKETING FROM THE UI SIDE FOR A MANUAL JP*/
	
	/**
	 * Method to send the jackpot adjustemnt to Marketing for a processed manual jp slip 
	 * @param siteId
	 * @param playerCardNumber
	 * @param jackpotAmount
	 * @param slotNo
	 * @param msgiD
	 * @param jackpotId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public int postManualJackpot(int siteId, String playerCardNumber,long jackpotAmount, String slotNo, long msgiD, String jackpotId) throws JackpotEngineServiceException;
	
	/* METHOD IS CALLED TO SEND ALERTS FROM THE UI */
	
	/**
	 * Method to send an alert to the Alerts Engine
	 * @param jpAlertObj
	 * @throws JackpotEngineServiceException
	 */
	public void sendAlert(JackpotEngineAlertObject jpAlertObj)throws JackpotEngineServiceException;	
	
	/* METHOD IS CALLED BY THE AUDIT ENGINE */
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param startTime
	 * @param endTime
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> voidPendingJackpotSlipsForAuditProcess(int siteId, long startTime, long endTime, String employeeId, String kioskProcessed) throws JackpotEngineServiceException;
	
	/**
	 * Method to void all the pending jp slips for AUDIT based on the site id and gaming day
	 * @param siteId
	 * @param employeeId
	 * @param kioskProcessed
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> voidAllPendingJackpotSlipsForAuditProcess(int siteId, String employeeId, String kioskProcessed) throws JackpotEngineServiceException;
	
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
	
	
	/*************************** METHODS EXPOSED TO OTHER ENGINES ********************/
	
	/* WEB METHODS TO ACCOUNTING */
	
	/**
	 * Method to get the slip transfer table's slip count 
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public Long getSlipsByCount() throws JackpotEngineServiceException;
	
	/* METHODS TO CAGE */
	/**
	 * Method to get the slip status and HPJP amount for the given sequence number and Site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JPCashlessProcessInfoDTO getJackpotStatusAmount(Long sequenceNumber, Long siteId) throws JackpotEngineServiceException;
	
	/**
	 * Method to process and pay the jackpot for the given sequence number and Site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @param employeeId
	 * @param employeeFirstName
	 * @param employeeLastName
	 * @param cashDeskLocation
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JPCashlessProcessInfoDTO payJackpot(Long sequenceNumber, Long siteId, String employeeId, String employeeFirstName, String employeeLastName, String cashDeskLocation, boolean validateEmpSession) throws JackpotEngineServiceException;
		
	/**
	 * Method to void a jackpot for the given sequence number and Site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @param employeeId
	 * @param employeeFirstName
	 * @param employeeLastName
	 * @param cashDeskLocation
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	@Deprecated
	public JPCashlessProcessInfoDTO voidPaidJackpot(Long sequenceNumber, Long siteId, String employeeId, String employeeFirstName, String employeeLastName, String cashDeskLocation) throws JackpotEngineServiceException ;
	
	/**
	 * Method to show if all jackpots are processed or not 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean isAllJackpotProcessed(String accountNumber, int siteId) throws JackpotEngineServiceException;
	
	/**
	 * Method to return the list of all jackpots that are not processed 
	 * 
	 * @param accountNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public List<JPCashlessProcessInfoDTO> getAllUnProcessedJackpot(String accountNumber, int siteId) throws JackpotEngineServiceException;
			
	//************************** METHODS FOR FLOOR EVENTS ****************************/
	
	/**
	 * Method to that is called by the Messaging Server when a JP Exception code arrives
	 * @param sdsMessage
	 * @return SDSMessage
	 * @throws Exception
	 */
	public SDSMessage preProcessMessage(SDSMessage sdsMessage)throws Exception;
	
	/**
	 * Method to that is called by the Messaging Server after the JP Exception code's response is sent back
	 * @param sdsMessage
	 * @throws Exception
	 */
	public void postProcessMessage(SDSMessage sdsMessage)throws Exception;
		
	/**
	 * Method to get the list of asset details based on the Slot No / Stand No / AcnfId
	 * @param slotLocationNo
	 * @param jackpotAssetParamType
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotAssetInfoDTO> getJackpotListAssetInfoForLocation(String slotLocationNo, int siteId) throws JackpotEngineServiceException; 
	
	/**
	 * Method to validate the progressive level for the slot
	 * @param siteId
	 * @param lstLevelNo
	 * @param slotNumber
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean validateProgressiveLevel(int siteId, List<Integer> lstLevelNo, String slotNumber) throws JackpotEngineServiceException;
	
	/**
	 * Method to fetch the jackpot details for New Wave.
	 * @param newWaveRequestDTO
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<NewWaveResponseDTO> getNewWaveJackpotDetails(NewWaveRequestDTO newWaveRequestDTO)throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Pending Jackpots that were not reset
	 * @param siteId
	 * @return
	 * @throws JackpotDAOException
	 */
	public List<JackpotResetDTO> getUnResetPendingJackpots(int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to return the gaming day info for a processed slip seq no and the current gaming day 
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<String> getGamingDayInfoForSlipSeq(int siteNo, long sequenceNo) throws JackpotEngineServiceException;
	
	/**
	 * This API calls the Ecash engine to get active player session at the slot
	 * @param accessID
	 * @param assetNumber
	 * @param siteID
	 * @return void
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */   
	public PlayerSessionDTO getActivePlayerSession(String accessID, String assetNumber, int siteID) throws JackpotEngineServiceException;
	
	/**
	 * Method to create the jp barcode
	 * @param siteid
	 * @param jpNetAmt
	 * @param sequenceNo
	 * @param encodeFormat
	 * @return barcodeImage
	 * @author vsubha
	 */
	public Barcode createBarcode(int siteid, long jpNetAmt, long sequenceNo, String encodeFormat) throws JackpotEngineServiceException;
	
	/**
	 * Method to get Jackpot Liability details for DEG
	 * @param siteId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotLiabilityDetailsDTO getJackpotLiabilityDetailsForDEG(int siteId, String fromDate,
			String toDate) throws JackpotEngineServiceException;
	
	/**
	 * Method exposed to the Progressive Engine to create a jackpot based on the Controller's notification
	 * @param siteId
	 * @param slotNo
	 * @param jackpotId
	 * @param jpAmount
	 * @param jpHitDate
	 * @throws JackpotEngineServiceException
	 * @return long
	 * @author dambereen
	 */
	public long createJackpotFromController(int siteId, String slotNo, String jackpotId, long jpAmount, Date jpHitDate) throws Exception;
	
	/**
	 * Method to retrieve Slip details By Sequence number and site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public JackpotDTO retrieveSlipBySequenceNumber(long sequenceNumber, int siteId ) throws JackpotEngineServiceException;
	
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
