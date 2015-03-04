/*****************************************************************************
 * $Id: JackpotEJBService.java,v 1.31.1.1.1.0, 2011-07-25 16:05:33Z, SDS 12.3.3$
 * $Date: 7/25/2011 11:05:33 AM$
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
package com.ballydev.sds.jackpotui.service;
 
import java.util.ArrayList;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.NameNotFoundException;

import org.apache.log4j.Logger;

import com.ballydev.sds.ecash.dto.account.PlayerSessionDTO;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.service.SDSServiceLocator;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.jackpot.dto.CashlessAccountDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;
import com.ballydev.sds.jackpot.dto.JackpotAssetParamType;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.dto.JackpotEngineAlertObject;
import com.ballydev.sds.jackpot.dto.JackpotReportsDTO;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.filter.JackpotFilter;
import com.ballydev.sds.jackpot.service.IJackpotEngine;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.util.JackpotExceptionUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.barcodelib.barcode.Barcode;

/**
 * @author dambereen
 *
 */
public class JackpotEJBService implements IJackpotService {
	/**
	 * logger instance
	 */
	//private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);	
	
	private IJackpotEngine jackpot;
	
	public JackpotEJBService() throws Exception{
		initialize();
	}
	
	private void initialize()  throws Exception {
		try {
			SDSServiceLocator<IJackpotEngine> serviceFactory = new SDSServiceLocator<IJackpotEngine>();
			jackpot = serviceFactory.fetchEJBService("JackpotEngine");
		} catch (NameNotFoundException e) {
			throw new JackpotEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
		} catch (CommunicationException e) {
			throw new JackpotEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}
	}


	public List<JackpotDTO> getAllPendingJackpotSlipDetails(int siteId) throws JackpotEngineServiceException 
	{
		List<JackpotDTO> jpDTOList = new ArrayList<JackpotDTO>();
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jpDTOList = jackpot.getAllPendingJackpotSlipDetails(siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		}catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpDTOList;
	}

	public List<JackpotDTO> getJackpotDetails(JackpotFilter jpFilter) throws JackpotEngineServiceException 
	{
		List<JackpotDTO> jpDTOList = new ArrayList<JackpotDTO>();
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jpDTOList = jackpot.getJackpotDetails(jpFilter);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpDTOList;
	}
	
	public JackpotDTO getReprintJackpotSlipDetails(long sequenceNumber,	int siteId) throws JackpotEngineServiceException
	{
//		com.ballydev.sds.jackpot.client.JackpotDTO jackpotDTOClient = null;
		JackpotDTO jackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jackpotDTO = jackpot.getReprintJackpotSlipDetails(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jackpotDTO;
	}
	
	public JackpotDTO getVoidJackpotSlipDetails(long sequenceNumber, int siteId)throws JackpotEngineServiceException
	{
		JackpotDTO jackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jackpotDTO = jackpot.getVoidJackpotSlipDetails(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jackpotDTO;
	}
	
	public short getExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException
	{
		short respBlindAtt=0;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			respBlindAtt = jackpot.getExpressJackpotBlindAttempts(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return respBlindAtt;
	}
	
	public boolean postNonCardedExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException
	{
		boolean respBlindAtt = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			respBlindAtt = jackpot.postNonCardedExpressJackpotBlindAttempts(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return respBlindAtt;
	}
	
	public boolean postExpressJackpotBlindAttempts(long sequenceNumber, int siteId)throws JackpotEngineServiceException
	{
		boolean respBlindAtt = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			respBlindAtt = jackpot.postExpressJackpotBlindAttempts(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return respBlindAtt;
	}
	
	/**
	 * Method to update the blind attempt as -1 for a unsuccessful exp jp processing that is aborted
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author dambereen
	 */
	@Override
	public boolean postUnsuccessfulExpJpBlindAttemptsAbort(long sequenceNumber,
			int siteId) throws JackpotEngineServiceException {
		boolean isBlindAttUpdated = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			isBlindAttUpdated = jackpot.postUnsuccessfulExpJpBlindAttemptsAbort(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return isBlindAttUpdated;
	}
	
	public short getJackpotStatus(long sequenceNumber, int siteId)throws JackpotEngineServiceException
	{
		short respStatusId = 0;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			respStatusId = jackpot.getJackpotStatus(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return respStatusId;
	}
	/**
	 * Method to get details whether the JP is posted to accounting or not 
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getJackpotPostToAccountDetail(long sequenceNumber, int siteId) throws JackpotEngineServiceException {
		String postToAccounting = "N";
		try {
			postToAccounting = jackpot.getJackpotPostToAccountDetail(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw(e);
		} catch(Exception e) {
			JackpotExceptionUtil.getGeneralException(e);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return postToAccounting;
	}
	
	/**
	 * Method to get the Jackpot slip status for a void method that will return the Transaction date based one the status and the current day
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public JackpotDTO getJackpotStatusForVoid(long sequenceNumber, int siteId) throws JackpotEngineServiceException 
	{
		JackpotDTO jackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jackpotDTO = jackpot.getJackpotStatusForVoid(sequenceNumber, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jackpotDTO;
	}
	
	public JackpotDTO getReprintPriorSlipDetails(int siteId)throws JackpotEngineServiceException
	{
		JackpotDTO jackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jackpotDTO = jackpot.getReprintPriorSlipDetails(siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jackpotDTO;
	}
	
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDate(int siteId,String fromDate, String toDate) throws JackpotEngineServiceException
	{
		List<JackpotReportsDTO> jpReportsDTOList =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jpReportsDTOList = jackpot.getDetailsToPrintJpSlipReportForDate(siteId, fromDate, toDate);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpReportsDTOList;
	}
	
	public List<JackpotReportsDTO> getDetailsToPrintJpSlipReportForDateEmployee(int siteId, String employeeId, String fromDate, String toDate)throws JackpotEngineServiceException
	{
		List<JackpotReportsDTO> jpReportsDTOList =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jpReportsDTOList = jackpot.getDetailsToPrintJpSlipReportForDateEmployee(siteId, employeeId, fromDate, toDate);
		} catch(JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpReportsDTOList;
	}
	
	public String[] getJackpotXMLInfo() throws JackpotEngineServiceException
	{
		ProgressIndicatorUtil.openInProgressWindow();
		String[] lisOfXMLFilesArray = null;
		try{
			lisOfXMLFilesArray = jackpot.getJackpotXMLInfo();		
			//lisOfXMLFilesArray = (String[]) listOfXMLFiles.toArray(new String[listOfXMLFiles.size()]);			
		}
		catch(JackpotEngineServiceException exception){
			throw (exception);
		}
		catch (Exception exception) {			
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return lisOfXMLFilesArray;
	}
		
	
	public JackpotDTO processJackpot(JackpotDTO jackpotDTO) throws JackpotEngineServiceException
	{
		JackpotDTO returnedJackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {									
			returnedJackpotDTO = jackpot.processJackpot(jackpotDTO);	
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return returnedJackpotDTO;
	}
	
	
	public JackpotDTO processManualJackpot(JackpotDTO jackpotDTO, JackpotFilter jpFilter) throws JackpotEngineServiceException
	{
		JackpotDTO returnedJackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {								
			returnedJackpotDTO = jackpot.processManualJackpot(jackpotDTO, jpFilter);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return returnedJackpotDTO;
	}
	
	
	public JackpotDTO postJackpotVoidStatus(JackpotDTO jackpotDTO)throws JackpotEngineServiceException
	{
		JackpotDTO returnedJackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {		
			returnedJackpotDTO = jackpot.postJackpotVoidStatus(jackpotDTO);	
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return returnedJackpotDTO;
	}
	
	
	public JackpotDTO postJackpotReprint(JackpotDTO jackpotDTO)throws JackpotEngineServiceException
	{
		JackpotDTO returnedJackpotDTO =null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {			
			returnedJackpotDTO = jackpot.postJackpotReprint(jackpotDTO);	
		} catch(JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return returnedJackpotDTO;
	}
	
	
	public List<JackpotDTO> voidAllPendingJackpotSlips(int siteId,String voidEmpId, String printerUsed,String actorLogin, String kioskProcessed)throws JackpotEngineServiceException
	{
		List<JackpotDTO> jpDTOList = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jpDTOList = jackpot.voidAllPendingJackpotSlips(siteId, voidEmpId, printerUsed,actorLogin, kioskProcessed);	
		} catch(JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpDTOList;
	}
	
	
	public List<JackpotDTO> voidPendingJackpotSlipsForSlotNo(String slotNo,int siteId,String voidEmpId, String printerUsed,String actorLogin, String kioskProcessed)throws JackpotEngineServiceException
	{
		List<JackpotDTO> jpDTOList = new ArrayList<JackpotDTO>();
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			jpDTOList = jackpot.voidPendingJackpotSlipsForSlotNo(slotNo, siteId, voidEmpId, printerUsed,actorLogin, kioskProcessed);	
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpDTOList;
	}
	
	/**
	 * Method to validate the Cashless Account Number
	 * 
	 * @param siteNo
	 * @param accountNumber
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public CashlessAccountDTO validateCashlessAccountNo(int siteNo, String accountNumber) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			return jackpot.validateCashlessAccountNo(siteNo, accountNumber);
		} catch(JackpotEngineServiceException e) {
			throw(e);
		} catch(Exception e) {
			JackpotExceptionUtil.getGeneralException(e);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return null;
	}
	
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
	public CashlessAccountDTO isJackpotOperationAllowed(String accessID, int siteID, short operationType) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			return jackpot.isJackpotOperationAllowed(accessID, siteID, operationType);
		} catch(JackpotEngineServiceException e) {
			throw(e);
		} catch(Exception e) {
			JackpotExceptionUtil.getGeneralException(e);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return null;
	}
	
	/**
	 * Method to check if the player card number entered is valid or not.
	 * 
	 * @param playerCardNumber
	 * @param siteID
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public long getPlayerCardId(String playerCardNumber, int siteID) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			return jackpot.getPlayerCardId(playerCardNumber, siteID);
		} catch(JackpotEngineServiceException e) {
			throw(e);
		} catch(Exception e) {
			JackpotExceptionUtil.getGeneralException(e);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return 0;
	}
	
	/**
	 * Method to get the player name for the player card number.
	 * 
	 * @param playerCardNumber
	 * @param siteID
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public String getPlayerCardName(String playerCardNumber, int siteID) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			return jackpot.getPlayerCardName(playerCardNumber, siteID);
		} catch(JackpotEngineServiceException e) {
			throw(e);
		} catch(Exception e) {
			JackpotExceptionUtil.getGeneralException(e);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return null;
	}
	
	/**
	 * This API calls the Ecash engine to get active player session at the slot
	 * @param accessID
	 * @param assetNumber
	 * @param siteID
	 * @return void
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */   
	public PlayerSessionDTO getActivePlayerSession(String accessID, String assetNumber, int siteID) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			return jackpot.getActivePlayerSession(accessID, assetNumber, siteID);
		} catch(JackpotEngineServiceException e) {
			throw(e);
		} catch(Exception e) {
			JackpotExceptionUtil.getGeneralException(e);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return null;
	}
	
	/** METHOD IS CALLED TO GET THE ASSET DETAILS FROM THE UI */
	public JackpotAssetInfoDTO getJackpotAssetInfo(Object object, JackpotAssetParamType jackpotAssetParamType, int siteId)throws JackpotEngineServiceException
	{
		JackpotAssetInfoDTO jpAssetInfoDTO = new JackpotAssetInfoDTO();
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
				jpAssetInfoDTO = jackpot.getJackpotAssetInfo(object, jackpotAssetParamType, siteId);			
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpAssetInfoDTO;
	}
		
	/**
	 * Method to get the list of asset details based on the Slot No / Stand No / AcnfId
	 * @param slotLocationNo
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<JackpotAssetInfoDTO> getJackpotListAssetInfoForLocation(String slotLocationNo, int siteId) throws JackpotEngineServiceException 
	{
		List<JackpotAssetInfoDTO> jpAssInfoDTOLst = new ArrayList<JackpotAssetInfoDTO>();
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
				jpAssInfoDTOLst = jackpot.getJackpotListAssetInfoForLocation(slotLocationNo, siteId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return jpAssInfoDTOLst;
	}
	
	
	/** METHOD TO SEND JACKPOT ADJUSTEMNT TO MARKETING FROM THE UI SIDE FOR A PENDING JP*/
	public int processJackpotAdjustment(int siteId, String origCardNo, String newCardNo,long oldAmount, long newJackpotAmount, String slotNo, long msgiD)throws JackpotEngineServiceException
	{
		int respJpAdj = 0;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			respJpAdj = jackpot.processJackpotAdjustment(siteId, origCardNo, newCardNo, oldAmount, newJackpotAmount, slotNo, msgiD);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return respJpAdj;	
	}
	
	/** METHOD TO SEND JACKPOT ADJUSTEMNT TO MARKETING FROM THE UI SIDE FOR A MANUAL JP*/
	public int postManualJackpot(int siteId, String playerCardNumber,long jackpotAmount, String slotNo, long msgiD, String jackpotId) throws JackpotEngineServiceException
	{
		int respJpAdj = 0;
		ProgressIndicatorUtil.openInProgressWindow();
		try {				
			respJpAdj = jackpot.postManualJackpot(siteId, playerCardNumber, jackpotAmount, slotNo, msgiD, jackpotId);
		} catch(JackpotEngineServiceException e) {
			throw (e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);				
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return respJpAdj;	
	}
		
	/** METHOD IS CALLED TO SEND ALERTS FROM THE UI */
	public void sendAlert(JackpotEngineAlertObject jpAlertObj)throws JackpotEngineServiceException
	{
		ProgressIndicatorUtil.openInProgressWindow();
		try {	
			jackpot.sendAlert(jpAlertObj);
		} catch(JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}

	/** METHOD IS CALLED TO POST SLIP PRINT AMOUNT TO PROG ENGINE */
	/*public void postToProgressiveEngine(String slotNumber, Long msgId, String jackpotId, Long slipAmount,
			Integer siteNumber, boolean messageStatusFlag) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			jackpot.postToProgressiveEngine(slotNumber, msgId, jackpotId, slipAmount, siteNumber,
					messageStatusFlag);
		} catch (JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {
			JackpotExceptionUtil.getGeneralException(exception);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}
	
	*//**
	 * Method to post the progressive level for the slot
	 * @param siteId
	 * @param slotNumber
	 * @param progressiveLevel
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 *//*
	public void postManualJPProgressiveLevel(int siteId, String slotNumber, int progressiveLevel, Long slipAmount) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			jackpot.postManualJPProgressiveLevel(siteId, slotNumber, progressiveLevel, slipAmount);
		} catch (JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {
			JackpotExceptionUtil.getGeneralException(exception);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}*/
	
	/**
	 * Method to validate the progressive level for the slot
	 * @param siteId
	 * @param progressiveLevel
	 * @param slotNumber
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public boolean validateProgressiveLevel(int siteId, ArrayList<Integer> progressiveLevel, String slotNumber) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			return jackpot.validateProgressiveLevel(siteId, progressiveLevel, slotNumber);
		} catch (JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {
			JackpotExceptionUtil.getGeneralException(exception);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return false;
	}
	
	/**
	 * Method to return the gaming day info for a processed slip seq no and the current gaming day 
	 * @param siteNo
	 * @param sequenceNo
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public List<String> getGamingDayInfoForSlipSeq(int siteNo, long sequenceNo) throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		List<String> rtnStrDateLst = null;
		try {	
			rtnStrDateLst = jackpot.getGamingDayInfoForSlipSeq(siteNo, sequenceNo);
		} catch(JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {	
			JackpotExceptionUtil.getGeneralException(exception);			
		}finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return rtnStrDateLst;
	}
	
	/**
	 * Method to create the jp barcode
	 * @param siteid
	 * @param jpNetAmt
	 * @param sequenceNo
	 * @param encodeFormat
	 * @return barcodeImage
	 * @author vsubha
	 */
	public Barcode createBarcode(int siteid, long jpNetAmt, long sequenceNo, String encodeFormat)
			throws JackpotEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		Barcode barcode = null;
		try {
			barcode = jackpot.createBarcode(siteid, jpNetAmt, sequenceNo, encodeFormat);
		} catch (JackpotEngineServiceException e) {
			throw new JackpotEngineServiceException(e);
		} catch (Exception exception) {
			JackpotExceptionUtil.getGeneralException(exception);
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return barcode;
	}

	
}