/*****************************************************************************
 *  Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.util;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_JACKPOT_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_PROGRSSIVE_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_TRANSACTION_BO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.bo.IJackpotBO;
import com.ballydev.sds.jackpot.constants.IAlertMessageConstants;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.IJackpotIds;
import com.ballydev.sds.jackpot.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpot.dao.JackpotCommonDAO;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.exception.JackpotDAOException;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.lookup.ServiceLocator;
import com.ballydev.sds.progressive.bo.IProgressiveBO;
import com.ballydev.sds.siteconfig.bo.exception.SiteConfigBOException;
import com.ballydev.sds.siteconfig.dto.SiteConfigurationDTO;
import com.ballydev.sds.siteconfig.util.SiteMasterUtil;
import com.ballydev.sds.transactionengine.bo.ITransactionBO;
import com.ballydev.sds.utils.common.props.IPrimaryKeyConstants;
import com.ballydev.sds.utils.treecache.SDSTreeCache;
import com.barcodelib.barcode.Barcode;

/**
 * This is a caching utility class for Jackpot engine. 
 * @author awilliamfranklin
 */
public class JackpotUtil implements IAppConstants{
	
	
	/**
	 * Logger instance
	 */
	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * This form contains the some values that 
	 * needs to preserved for future use
	 */
	private static HashMap<String,Object> pendingJackpotMap = new HashMap<String,Object>();

	/**
	 * Getter method to get pendingJackpotMap in cache
	 * @return pendingJackpotMap
	 */
	public static HashMap<String, Object> getPendingJackpotMap() {
		return pendingJackpotMap;
	}

	/**
	 * Setter method to get pendingJackpotMap in cache
	 * @param pendingJackpotMap
	 */
	public static void setPendingJackpotMap(
			HashMap<String, Object> pendingJackpotMap) {
		JackpotUtil.pendingJackpotMap = pendingJackpotMap;
	}

	
	/**
	 * Add jackpot details in sds tree cache with given key
	 * @param key
	 * @param jackpotDTO
	 */
	public static void addJackpotDTOToClusterCache(String cacheName, String key, Object jackpotDTO) {
		SDSTreeCache.getInstance ().putValue (cacheName, key, jackpotDTO);
		
	}
	
	/**
	 * This method is used to retrieve jackpot details from sds tree cache based on key
	 * @param key
	 * @return
	 */
	public static Object getJackpotDTOFromClusterCache(String cacheName , String key) {
	    return SDSTreeCache.getInstance ().getValue (cacheName, key);
	}
	
	public static Set<String> getAllPendingJackpotCacheKeys(String cacheName) {
	    return (Set<String>)SDSTreeCache.getInstance ().keySet(cacheName);
	}
		
	/**
	 * Load all the pending jackpot during startup
	 * @param
	 * @return
	 */	
	public static void loadAllPendingJPFromDBToCache() {
		
		IJackpotBO jackpotBO  = (IJackpotBO)ServiceLocator.fetchService(LOOKUP_SDS_JACKPOT_BO, IS_LOCAL_LOOKUP);
		List<JackpotDTO> allPendingJPList=null;
		Map<String,Object> jackpotMap=null;
		String key="";
		Map<Integer,Map<String,Long>>  siteConfigParamMap=new HashMap<Integer, Map<String,Long>>();
		if(jackpotBO!=null){
			try{
				
				/*Load the Pending Jackpot from DB to cache during initial startup If the cache is empty.
				* SiteId -1 for retriveving all pending JP from DB.
				**/
				
				allPendingJPList=jackpotBO.getAllPendingJackpotForCache();				
				
				/* GET SITE CONFIG PARAMS */
				long sendMsgUnderXMinsOld=0;
				long sendMsgAtleastYMinsOld=0;
				int siteId=-1;
				Map<String,Long> jackpotSiteConfigParams=null;
				for(JackpotDTO jackpotDTO:allPendingJPList){
					siteId=jackpotDTO.getSiteId();
					try{
						jackpotSiteConfigParams=getJPSiteConfigParams(jackpotDTO);
						
						if(jackpotSiteConfigParams!=null){
							
							if(siteConfigParamMap.containsKey(siteId)){
								Map<String,Long> paramMap=siteConfigParamMap.get(siteId);
								sendMsgUnderXMinsOld=paramMap.get(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD);
								sendMsgAtleastYMinsOld=paramMap.get(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD);
								
							}else{
								sendMsgUnderXMinsOld=jackpotSiteConfigParams.get(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD);
								sendMsgAtleastYMinsOld=jackpotSiteConfigParams.get(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD);
								Map<String,Long> paramMap=new HashMap<String, Long>();
								paramMap.put(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD, sendMsgUnderXMinsOld);
								paramMap.put(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD, sendMsgAtleastYMinsOld);
								siteConfigParamMap.put(siteId, paramMap);
								
							}
														
							key=jackpotDTO.getSiteId()+"_"+jackpotDTO.getSequenceNumber();			
							jackpotMap=new LinkedHashMap<String,Object>();
							jackpotMap.put(IAppConstants.SEQUENCE_NO, jackpotDTO.getSequenceNumber());
							jackpotMap.put(IAppConstants.SITE_ID, siteId);
							jackpotMap.put(IAppConstants.JACKPOT_ID, jackpotDTO.getJackpotId());
							jackpotMap.put(IAppConstants.MESSAGE_ID, jackpotDTO.getMessageId());
							jackpotMap.put(IAppConstants.ASSET_CONFIG_NO, jackpotDTO.getAssetConfigNumber());
							jackpotMap.put(IAppConstants.ASSET_CONFIG_LOCATION, jackpotDTO.getAssetConfigLocation());
							jackpotMap.put(IAppConstants.TRANSACTION_DATE, new Date(jackpotDTO.getPendingAlertTxnDate()));
							jackpotMap.put(IAppConstants.ORIGINAL_AMOUNT, jackpotDTO.getOriginalAmount());			
							jackpotMap.put(IAppConstants.AREA_SHORT_NAME, jackpotDTO.getAreaShortName());
							jackpotMap.put(IAppConstants.TIMER_DELAY, sendMsgUnderXMinsOld);
							jackpotMap.put(IAppConstants.TIMER_FREQUENCY, sendMsgAtleastYMinsOld);
							JackpotUtil.addJackpotDTOToClusterCache(IAppConstants.ALL_PENDING_JACKPOTS, key, jackpotMap);
							JackpotUtil.addJackpotDTOToClusterCache(IAppConstants.ALL_PENDING_JACKPOTS_KEY, key,null);
						}else{
							log.error("Error in getting site config params for jackpot.");
						}
						
					}catch(Exception e){						
						log.error("Error in getting site config params for jackpot", e);
					}					
					
				}			
			}catch(JackpotDAOException je){
				log.error("Error in getting all pending jackpots",je);
			}
		}
		
		
	}
	
	/**
	 * This method is used to get site config param
	 * @param jackpotDTO
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Long> getJPSiteConfigParams(JackpotDTO jackpotDTO) throws Exception{
		Map<String,Long> jackpotSiteConfigParams=new HashMap<String,Long>();
		/*
		 * Variable that holds the site param value for
		 * SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD
		 */
		long sendMsgUnderXMinsOld = 0;

		/*
		 * Variable that holds the site param value for
		 * SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD
		 */
		long sendMsgAtleastYMinsOld = 0;

		// List<SiteConfigurationDTO> siteDTOLst = null;
		/* GET SITE PARAMETER VALUES */		
		List<String> siteParamLst = new ArrayList<String>();		
		siteParamLst.add(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD);
		siteParamLst.add(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD);
		// ISiteConfigBO siteConfigBO =
		// (ISiteConfigBO)ServiceLocator.fetchService(
		// LOOKUP_SDS_SITE_BO, IS_LOCAL_LOOKUP);
		// try {
		// siteDTOLst = siteConfigBO.getSiteConfigurationValues(siteParamLst,
		// jackpotDTO.getSiteId());
		// if(siteDTOLst!=null && siteDTOLst.size()>0) {
		sendMsgUnderXMinsOld = Integer.parseInt(JackpotUtil.getSiteParamValue(
				ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD, jackpotDTO.getSiteId()));
		jackpotSiteConfigParams.put(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD,
				sendMsgUnderXMinsOld);

		sendMsgAtleastYMinsOld = Integer.parseInt(JackpotUtil.getSiteParamValue(
				ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD, jackpotDTO.getSiteId()));
		jackpotSiteConfigParams.put(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD,
				sendMsgAtleastYMinsOld);
		/*
		 * }else{ log.error("Site param values returned are null"); throw new
		 * Exception("Site param values returned are null"); }
		 */
		/*
		 * } catch (SiteConfigBOException e) {
		 * log.error("Exception while getting site params: "+e); throw new
		 * Exception(e); }
		 */
		return jackpotSiteConfigParams;
	}

	/**
	 * Remove pending JP from cluster cache.
	 * @param key
	 */
	public static void removePendingJPFromCache(String key) {
		/* remove the processed pending jp from cache*/
		
			SDSTreeCache.getInstance().removeValue(IAppConstants.ALL_PENDING_JACKPOTS, key);
			
			if(log.isInfoEnabled()){
				log.info("Stop sending alerts 92 - PENDING JP FOR SLOT::"+key);
			}
	}
	
	
	/**
	 * Method is called by the Jackpot Facade to Cache
	 * @param jackpotDTO
	 * @throws Exception
	 */
	public static void addNewPendingJackpotInCache(JackpotDTO jackpotDTO) throws Exception {		
		if (log.isDebugEnabled()) {
			log.debug("Inside addNewPendingJackpotInCache method.");
		}
		/* GET SITE CONFIG PARAMS */
		Map<String,Long> jackpotSiteConfigParams=null;
		long sendMsgUnderXMinsOld=0;
		long sendMsgAtleastYMinsOld=0;
		try {
			jackpotSiteConfigParams = JackpotUtil.getJPSiteConfigParams(jackpotDTO);
			if (jackpotSiteConfigParams != null) {	
				sendMsgUnderXMinsOld=jackpotSiteConfigParams.get(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD);
				sendMsgAtleastYMinsOld=jackpotSiteConfigParams.get(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD);
				
				/* CHECK FOR TIME LIMIT */		
				if (jackpotDTO != null && sendMsgUnderXMinsOld > 0 && sendMsgAtleastYMinsOld > 0) {					
					HashMap<String, Object> jackpotMap = new HashMap<String, Object>();
					jackpotMap.put(IAppConstants.SEQUENCE_NO, jackpotDTO.getSequenceNumber());
					jackpotMap.put(IAppConstants.SITE_ID, jackpotDTO.getSiteId());
					jackpotMap.put(IAppConstants.JACKPOT_ID, jackpotDTO.getJackpotId());
					jackpotMap.put(IAppConstants.MESSAGE_ID, jackpotDTO.getMessageId());
					jackpotMap.put(IAppConstants.ASSET_CONFIG_NO, jackpotDTO.getAssetConfigNumber());
					jackpotMap.put(IAppConstants.ASSET_CONFIG_LOCATION, jackpotDTO.getAssetConfigLocation());
					log.info("TransactionDate bef updating the map: "+ new Date(jackpotDTO.getPendingAlertTxnDate()));
					jackpotMap.put(IAppConstants.TRANSACTION_DATE, new Date(jackpotDTO.getPendingAlertTxnDate()));
					log.info("TransactionDate from map: "+jackpotMap.get(IAppConstants.TRANSACTION_DATE));
					jackpotMap.put(IAppConstants.ORIGINAL_AMOUNT, jackpotDTO.getOriginalAmount());			
					jackpotMap.put(IAppConstants.AREA_SHORT_NAME, jackpotDTO.getAreaShortName());
					jackpotMap.put(IAppConstants.TIMER_DELAY, sendMsgUnderXMinsOld);
					jackpotMap.put(IAppConstants.TIMER_FREQUENCY, sendMsgAtleastYMinsOld);
					
					String key=jackpotDTO.getSiteId()+"_"+jackpotDTO.getSequenceNumber();
	
					JackpotUtil.addJackpotDTOToClusterCache(IAppConstants.ALL_PENDING_JACKPOTS,key, jackpotMap);
					

					JackpotUtil.addJackpotDTOToClusterCache(IAppConstants.ALL_PENDING_JACKPOTS_KEY,key,null);

					if (log.isInfoEnabled()) {
						log.info("Start sending alerts 92 - PENDING JP FOR SLOT::"+key);
					}						
				}
			} else {
				log.error("Error in getting site config params for jackpot.");
			}		
		} catch (Exception e) {
			log.error("Error in getting site config params for jackpot", e);
		}
		if(log.isInfoEnabled()){
			log.info("Jackpot DTO successfully added to the cluster cache");
		}
	}
	
	
	/**
	 * Method to generate Slip Primary Key for UI operations
	 * 
	 * @param slotNumber
	 * @param siteNumber
	 * @param numberOfIncrements
	 * @return
	 * @throws Exception
	 * @author vsubha
	 */
	public static long getSlipPrimaryKey(String slotNumber, int siteNumber, int numberOfIncrements)
			throws Exception {
		/**
		 * Transaction BO instance
		 */
		ITransactionBO transactionBO = (ITransactionBO) ServiceLocator.fetchService(
				LOOKUP_SDS_TRANSACTION_BO, IS_LOCAL_LOOKUP);
		if (numberOfIncrements > 0) {
			return transactionBO.getSDSPrimaryKey(slotNumber, siteNumber, numberOfIncrements).getKey(
					IPrimaryKeyConstants.SLIP_SLIP_REFERENCE);
		} else {
			return transactionBO.getSDSPrimaryKey(slotNumber, siteNumber).getKey(
					IPrimaryKeyConstants.SLIP_SLIP_REFERENCE);
		}
	}
	
	/**
	 * Method to get the site parameter value for the given site
	 * 
	 * @param siteConfigParam
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public static String getSiteParamValue(String siteConfigParam, Integer siteId)
			throws JackpotEngineServiceException {
		/** GET SITE CONFIG PARAMETER VALUE */
		String siteConfigValue = null;
		try {
			SiteConfigurationDTO siteDTO = SiteMasterUtil.getSiteConfigurationValue(siteConfigParam, siteId);
			siteConfigValue = (siteDTO != null && siteDTO.getSiteConfigurationValue() != null) ? siteDTO
					.getSiteConfigurationValue() : "";
			if (log.isDebugEnabled()) {
				log.debug(siteConfigParam + " : " + siteConfigValue);
			}
		} catch (SiteConfigBOException e) {
			log.error("Exception while getting the " + siteConfigParam + " site param");
			throw new JackpotEngineServiceException(e);
		} catch (Exception e) {
			log.error("Exception while getting the " + siteConfigParam + " site param");
			throw new JackpotEngineServiceException(e);
		}
		return siteConfigValue;
	}
	
	/**
	 * Method to get the site parameter value for the given site
	 * 
	 * @param siteConfigParam
	 * @param siteId
	 * @return
	 * @throws JackpotEngineServiceException
	 * @author vsubha
	 */
	public static String[] getSiteParamValueList(List<String> siteConfigParams, Integer siteId)
			throws JackpotEngineServiceException {
		/** GET SITE CONFIG PARAMETER VALUE */
		String[] siteConfigValue = null;
		try {
			List<SiteConfigurationDTO> siteDTOList = SiteMasterUtil.getSiteConfigurationValues(siteConfigParams, siteId);
			if (siteDTOList != null && siteDTOList.size() > 0) {
				int siteDTOSize = siteDTOList.size();
				int i = 0;
				siteConfigValue = new String[siteDTOSize];
				while(i < siteDTOSize) {
					siteConfigValue[i] = siteDTOList.get(i).getSiteConfigurationValue();
					if (log.isDebugEnabled()) {
						log.debug(siteConfigParams.get(i) + " : " + siteConfigValue[i]);
					}
					i++;
				}
			} else if (log.isDebugEnabled()) {
				log.debug("No site param values returned");				
			}			
		} catch (SiteConfigBOException e) {
			log.error("Exception while getting the siteConfigParam values ");
			throw new JackpotEngineServiceException(e);
		} catch (Exception e) {
			log.error("Exception while getting the siteConfigParam values");
			throw new JackpotEngineServiceException(e);
		}
		return siteConfigValue;
	}
	
	/**
	 * Method to create the jp barcode
	 * @param siteid
	 * @param jpNetAmt
	 * @param sequenceNo
	 * @param encodeFormat
	 * @return
	 */
	public static Barcode createBarcode(int siteid, long jpNetAmt, long sequenceNo, String encodeFormat)
			throws Exception {
		Barcode barcode = new Barcode();
		if (log.isInfoEnabled()) {
			log.info("Inside createBarcode util method");
		}
		try {
			String jpNetAmtStr = new DecimalFormat("0.00").format(new Double(JackpotCommonDAO
					.centsToDollarForNewWave(jpNetAmt)));
			jpNetAmtStr = JackpotCommonDAO.padLeftZerosForBarcode(jpNetAmtStr, 17);
			String sitIdStr = JackpotCommonDAO.padLeftZerosForBarcode(Integer.toString(siteid), 4);

			barcode.setData(sitIdStr + jpNetAmtStr + IAppConstants.JACKPOT_SLIP_SEQUENCE_NO_PREFIX
					+ sequenceNo);

			if (log.isDebugEnabled()) {
				log.debug("barcode.getData(): " + barcode.getData());
				log.debug("encodeFormat: " + encodeFormat);
			}

			if (encodeFormat.equalsIgnoreCase(IAppConstants.CODE39_ENCODE_FORMAT)) {
				barcode.setType(Barcode.CODE39);
			} else if (encodeFormat.equalsIgnoreCase(IAppConstants.CODE128_ENCODE_FORMAT)) {
				barcode.setType(Barcode.CODE128);
			} else if (encodeFormat.equalsIgnoreCase(IAppConstants.CODE128A_ENCODE_FORMAT)) {
				barcode.setType(Barcode.CODE128A);
			} else if (encodeFormat.equalsIgnoreCase(IAppConstants.CODE128B_ENCODE_FORMAT)) {
				barcode.setType(Barcode.CODE128B);
			}
			barcode.setBarWidth(1);
			barcode.setBarHeight(25);
			barcode.setShowText(false);

			/*String initialBarFile = encodeFormat + ".jpg";

			barcode.createBarcodeImage(initialBarFile);

			File barcodeFile = new File(initialBarFile);
			image = ImageIO.read(barcodeFile);*/

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in createBarcode of PrintWithGraphicsForKeypad ", e);
			throw e;
		}
		return barcode;
	}
	
	/**
	 * Method to get the list of progressive level values
	 * @param progressiveLevels
	 * @return
	 * @author vsubha
	 */
	public static ArrayList<Integer> getProgressiveLevelValues(String progressiveLevels) {
		ArrayList<Integer> lstProgLevel = new ArrayList<Integer>();
		
		String[] stringArray = progressiveLevels.split(",");
		for (int i = 0; i < stringArray.length; i++) {
			char[] chars = stringArray[i].toCharArray();
			int index = 0;
			for (; index < stringArray[i].length(); index++) {
				if (chars[index] != '0') {
					break;
				}
			}
			lstProgLevel.add(new Integer((index == 0) ? stringArray[i] : stringArray[i].substring(index)));
		}
		return lstProgLevel;
	}
	
	/**
	 * Method to check if PROG CONTROLLER NOTIFICATION IS ENABLED FOR CREATING THE PROG JP	
	 * This is applicable for XC10, XC30, XC52 and JpKeypadProcessing
	 * If Prog Notification is enabled - Return FALSE - Ignore the current floor event
	 * Else return TRUE - Handle the current floor event
	 * @param slotNo
	 * @param siteId
	 * @param intJackpotId
	 * @return boolean
	 * @throws Exception
	 * @author dambereen
	 */
	public static boolean isJackpotSlotFloorEventHandlingRequired(String slotNo, int siteId, int intJackpotId)throws Exception{
		if(((intJackpotId >= IJackpotIds.JACKPOT_ID_PROG_START_112 
				&& intJackpotId <= IJackpotIds.JACKPOT_ID_PROG_END_159)				
				|| intJackpotId == IJackpotIds.JACKPOT_ID_251_FB)
				&& intJackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_149
				&& intJackpotId != IJackpotIds.JACKPOT_ID_INVALID_PROG_150
				&& JackpotUtil.isCreateSlipUsingControllerSignal(slotNo, siteId)){
			
			return false;			
		}else{
			return true;
		}
	}
	
	
	/**
	 * Method that invokes the Progressive Engine to check whether Jackpot notification 
	 * from Controller is enabled 
	 * If the method returns TRUE - Ignore Slip creation by XC10
	 * Else if FALSE - Create slip from slot
	 * This is applicable for XC10, XC30, XC52 and JpKeypadProcessing
	 * @param slotNo
	 * @param siteId
	 * @return boolean
	 * @throws Exception
	 * @author dambereen
	 */
	private static boolean isCreateSlipUsingControllerSignal(String slotNo, int siteId)throws Exception{
		IProgressiveBO progressiveBO = (IProgressiveBO) ServiceLocator.fetchService(LOOKUP_SDS_PROGRSSIVE_BO, IS_LOCAL_LOOKUP);
		if(progressiveBO!=null){
			return progressiveBO.isCreateSlipUsingControllerSignal(slotNo, siteId);
		}else{
			return false;
		}		
	}
	
	/**
	 * Mathod to get the site keys into an arraylist
	 * 
	 * @param siteKeys
	 * @return
	 * @author vsubha
	 */
	public static List<String> getSiteKeyList(String[] siteKeys) {
		ArrayList<String> siteKeyList = new ArrayList<String>();
		if(siteKeys != null && siteKeys.length > 0) {
			int siteKeyLength = siteKeys.length;
			for(int i = 0; i < siteKeyLength; i++) {
				siteKeyList.add(siteKeys[i]);
			}			
		}
		return siteKeyList;
	}
	
	public static boolean isS2SErrorPresent (String responseCode){
		boolean isS2SErrorPresent = false;
		List<String> lstS2SResponse = new ArrayList<String>();
		lstS2SResponse.add(IAppConstants.S2S_OVERRIDE_LIMIT_EXCEEDED);
		lstS2SResponse.add(IAppConstants.S2S_DOLLAR_VARIANCE_EXCEEDED);
		lstS2SResponse.add(IAppConstants.S2S_PERCENT_VARIANCE_EXCEEDED);
		if(responseCode!= null && lstS2SResponse.contains(responseCode)){
			isS2SErrorPresent = true;
		}
		return isS2SErrorPresent;
		
	}
	/**
	 * Method to validate the S2SJackpot
	 * J.P. Override Amount Limit
	 * J.P. Dollar Variance for Additional Authorizers
	 * J.P. % Variance for Additional Authorizers	
	 * @param jackpotDTO3
	 * @throws Exception
	 */
	public static String validateS2SJackpot(int siteNo,long hpjpAmount, long orgAmount) throws Exception{
		String[] s2sSiteParamValues  = JackpotUtil.getSiteParamValueList(getSiteKeyList(new String[] {
				ISiteConfigConstants.JACKPOT_AMOUNT_OVERIDE_AUTH_ENABLED,
				ISiteConfigConstants.JACKPOT_AMOUNT_OVERIDE_AUTH_LIMIT,
				ISiteConfigConstants.JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS,
				ISiteConfigConstants.JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS}), siteNo);
		String jpOverrideAuthEnabled = s2sSiteParamValues[0];
		double hpjpAuthAmount = (double)Double.parseDouble(s2sSiteParamValues[1]);
		double siteJPDollarVarValue = new Double(s2sSiteParamValues[2]);
		long siteJPPercentVarValue = new Long(s2sSiteParamValues[3]);
		double hpjpAmt = ConversionUtil.centsToDollar(hpjpAmount);
		double originalAmount = ConversionUtil.centsToDollar(orgAmount);
		if (log.isInfoEnabled()) {
			log.info("jpOverrideAuthEnabled  " + jpOverrideAuthEnabled);
			log.info("HpjpAuthAmount  " + hpjpAuthAmount);
			log.info("siteJPDollarVarValue: " + siteJPDollarVarValue);
			log.info("siteJPPercentVarValue: " + siteJPPercentVarValue);
			log.info("hpjpAmt  " + hpjpAmt);
		}
		if (jpOverrideAuthEnabled.equalsIgnoreCase("yes")) {
			if (hpjpAmt > hpjpAuthAmount) {
				if(log.isInfoEnabled()) {
					log.info("OVERRIDE_LIMIT_EXCEEDED");
				}
				return IAppConstants.S2S_OVERRIDE_LIMIT_EXCEEDED;
			} 
		}
		
		double variance = ConversionUtil.roundHalfUp((originalAmount - hpjpAmt));
		if (variance < 0)
			variance = (variance * -1);
		if(log.isInfoEnabled()) {
			log.info("Original amount is " + originalAmount);
			log.info("Variance is " + variance);
		}
		double originalAmtPercentVar =0;
		double finalPercentVar = 0;
		if (originalAmount != 0) {
			originalAmtPercentVar = ConversionUtil.roundHalfUp(((hpjpAmt * 100) / originalAmount));
			if (originalAmtPercentVar > 100) {
				finalPercentVar = ConversionUtil.roundHalfUp(originalAmtPercentVar - 100);
			} else {
				finalPercentVar = ConversionUtil.roundHalfUp(100 - originalAmtPercentVar);
			}
		}
		if(log.isInfoEnabled()) {
			log.info("Org Amt Percentage var: " + originalAmtPercentVar);
			log.info("Final percentage Variance is " + finalPercentVar);
			log.info("Original Amt: " + originalAmount);
		}		
		//JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS
		if (siteJPDollarVarValue!=0					
				&& variance >= siteJPDollarVarValue) {
			if(log.isInfoEnabled()) {
				log.info("JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS--------Yes");
			}
			return IAppConstants.S2S_DOLLAR_VARIANCE_EXCEEDED;
		}//JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS
		else if (siteJPPercentVarValue != 0					
				&& finalPercentVar >= siteJPPercentVarValue) {
			if(log.isInfoEnabled()) {
				log.info("JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS--------Yes");
			}
			return IAppConstants.S2S_PERCENT_VARIANCE_EXCEEDED;
		}
		return null;
	}
}
