package com.ballydev.sds.jackpotui.service;
 
import java.util.ArrayList;
import java.util.List;

import com.ballydev.sds.ecash.dto.account.PlayerSessionDTO;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotEngineAlertObject;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.barcodelib.barcode.Barcode;

public interface IJackpotService {
	
	/**
	 * Method to retrieve all the pending jackpot slip details
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId)	throws JackpotEngineServiceException;	

	/**
	 * Method to retrieve the pending jackpot slip details based on the Filter type (SlotNo/SlotLoc/SeqNo/Minutes)
	 * @param filter
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotDTO> getJackpotDetails(JackpotFilter filter) throws JackpotEngineServiceException;
	
	/**
	 * Method to get the Jackpot slip status for a void method that will return the Transaction date based one the status and the current day
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO getJackpotStatusForVoid(long sequenceNumber, int siteId) throws JackpotEngineServiceException ;
	
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber,	int siteId) throws JackpotEngineServiceException;
	
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	public short getExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	public boolean postNonCardedExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	public boolean postExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	public boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNumber, int siteId) throws JackpotEngineServiceException ;
	
	public short getJackpotStatus(long sequenceNumber, int siteId)throws JackpotEngineServiceException;
	
	/**
	 * Method to get details whether the JP is posted to accounting or not 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getJackpotPostToAccountDetail(long sequenceNumber, int siteId) throws JackpotEngineServiceException;
	
	public JackpotDTO getReprintPriorSlipDetails(int siteId)throws JackpotEngineServiceException;
	
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(int siteId,String fromDate, String toDate) throws JackpotEngineServiceException;
	
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(int siteId, String employeeId, String fromDate, String toDate)throws JackpotEngineServiceException;
	
	public String[] getJackpotXMLInfo() throws JackpotEngineServiceException;
		
	/**
	 * getUserDetails
	 * updateProcessJackpot
	 * sendAlert
	 * sendAlert
	 * getJpSiteConfigurationValue
	 * directedResetHandPaidJackpot
	 * sendSystemGeneratedException
	 * getPrinterAndSchemaXmlFiles
	 */
	public JackpotDTO processJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException;
	
	/**
	 * getJackpotAssetInfo
	 * getUserDetails
	 * updateProcessManualJackpot
	 * sendAlert
	 * sendSystemGeneratedException
	 * getPrinterAndSchemaXmlFiles
	 */
	public JackpotDTO processManualJackpot(JackpotDTO jackpotDTO, JackpotFilter filter) throws JackpotEngineServiceException;
	
	/**
	 * getUserDetails
	 * postUpdateJackpotVoidStatus
	 * sendAlert
	 * sendSystemGeneratedException
	 * getPrinterAndSchemaXmlFiles
	 */
	public JackpotDTO postJackpotVoidStatus(JackpotDTO jackpotDTO)throws JackpotEngineServiceException;
	
	/**
	 * getUserDetails
	 * postUpdateJackpotReprint
	 * getPrinterAndSchemaXmlFiles
	 */
	public JackpotDTO postJackpotReprint(JackpotDTO jackptoDTO)throws JackpotEngineServiceException;
	
	/**
	 * getUserDetails
	 * updateVoidAllPendingJackpotSlips
	 * sendAlert
	 * processBulkVoidJackpots
	 */
	public List<JackpotDTO> voidAllPendingJackpotSlips(int siteId,String voidEmpId, String printerUsed, String actorLogin, String kioskProcessed)throws JackpotEngineServiceException;
	
	/**
	 * getUserDetails
	 * updateVoidPendingJackpotSlipsForSlotNo
	 * processBulkVoidJackpots
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
	 * @param accountNumber
	 * @param siteID
	 * @param operationType
	 * @return CashlessAccountDTO
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public CashlessAccountDTO isJackpotOperationAllowed(String accountNumber, int siteID, short operationType) throws JackpotEngineServiceException;
	
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
	 * Method to validate the progressive level for the slot
	 * @param siteId
	 * @param progressiveLevel
	 * @param slotNumber
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean validateProgressiveLevel(int siteId, ArrayList<Integer> progressiveLevel, String slotNumber) throws JackpotEngineServiceException;
	
	/** METHOD IS CALLED TO GET THE ASSET DETAILS FROM THE UI */
	public JackpotAssetInfoDTO getJackpotAssetInfo(Object object, JackpotAssetParamType jackpotAssetParamType, int siteId)throws JackpotEngineServiceException;
	
	
	/**
	 * Method to get the list of asset details based on the Slot No / Stand No / AcnfId
	 * @param slotLocationNo
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotAssetInfoDTO> getJackpotListAssetInfoForLocation(String slotLocationNo, int siteId) throws JackpotEngineServiceException;
	
	/** METHOD TO SEND JACKPOT ADJUSTEMNT TO MARKETING FROM THE UI SIDE FOR A PENDING JP*/
	public int processJackpotAdjustment(int siteId, String origCardNo, String newCardNo,long oldAmount, long newJackpotAmount, String slotNo, long msgiD)throws JackpotEngineServiceException;
	
	/** METHOD TO SEND JACKPOT ADJUSTEMNT TO MARKETING FROM THE UI SIDE FOR A MANUAL JP*/
	public int postManualJackpot(int siteId, String playerCardNumber,long jackpotAmount, String slotNo, long msgiD, String jackpotId) throws JackpotEngineServiceException;
		
	/** METHOD IS CALLED TO SEND ALERTS FROM THE UI */
	public void sendAlert(JackpotEngineAlertObject jpAlertObj)throws JackpotEngineServiceException;
	
	/** METHOD IS CALLED TO POST SLIP PRINT AMOUNT TO PROG ENGINE */
	/*public void postToProgressiveEngine(String slotNumber, Long msgId, String jackpotId, Long slipAmount, Integer siteNumber, boolean messageStatusFlag) throws JackpotEngineServiceException;*/
	
	/**
	 * Method to post the progressive level for the slot
	 * @param siteId
	 * @param slotNumber
	 * @param progressiveLevel
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	/*public void postManualJPProgressiveLevel(int siteId, String slotNumber, int progressiveLevel, Long slipAmount) throws JackpotEngineServiceException;*/
	
	/**
	 * Method to return the gaming day info for a processed slip seq no and the current gaming day 
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<String> getGamingDayInfoForSlipSeq(int siteNo, long sequenceNo) throws JackpotEngineServiceException;
	
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
		
}
