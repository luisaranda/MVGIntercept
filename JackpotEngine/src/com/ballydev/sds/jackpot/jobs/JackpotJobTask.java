/*****************************************************************************
 * $Id: JackpotJobTask.java,v 1.17, 2011-02-22 12:21:05Z, Anbuselvi, Balasubramanian$
 * $Date: 2/22/2011 6:21:05 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2008
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.jobs;

import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_ALERTS_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_ASSET_BO;
import static com.ballydev.sds.lookup.LookupConstants.LOOKUP_SDS_SITE_BO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ballydev.sds.alertsengine.bo.IAlertsBO;
import com.ballydev.sds.alertsengine.exception.AlertsEngineBOException;
import com.ballydev.sds.asset.bo.IAssetBO;
import com.ballydev.sds.asset.dto.AssetInfoDTO;
import com.ballydev.sds.common.message.JackpotAlertObject;
import com.ballydev.sds.framework.util.CustomTimerTask;
import com.ballydev.sds.jackpot.constants.IAlertMessageConstants;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.JackpotUtil;
import com.ballydev.sds.lookup.ServiceLocator;
import com.ballydev.sds.siteconfig.bo.ISiteConfigBO;
import com.ballydev.sds.utils.cluster.ClusterUtil;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;
import com.ballydev.sds.utils.treecache.SDSTreeCache;

/**
 * This timer thread check the pending JP list in cache and send alerts based on site config params.
 * @author dambereen
 * @version $Revsion: 1$
 */
public class JackpotJobTask extends CustomTimerTask{

	/**
	 * Instance of the Logger 
	 */
	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * Instance of Alerts BO
	 */
	private static IAlertsBO alertsBO;


	/**
	 * Instance of SiteConfig BO
	 */
	private static ISiteConfigBO siteConfigBO;

	/**
	 * Instance of Asset BO
	 */
	private static IAssetBO assetBO;


	/**
	 * Constructor
	 * @param String dropType
	 * @param Integer siteId
	 * @param Boolean isDailyTrigger
	 * @param String dropEndTime
	 * @param String employeeId
	 * @param String employeeName
	 * @return 
	 * @throws 
	 */
	public JackpotJobTask(){	

		assetBO = (IAssetBO)ServiceLocator.fetchService(LOOKUP_SDS_ASSET_BO, IAppConstants.IS_LOCAL_LOOKUP);
		alertsBO = (IAlertsBO)ServiceLocator.fetchService(LOOKUP_SDS_ALERTS_BO, IAppConstants.IS_LOCAL_LOOKUP);		
		siteConfigBO = (ISiteConfigBO)ServiceLocator.fetchService(LOOKUP_SDS_SITE_BO, IAppConstants.IS_LOCAL_LOOKUP);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.util.CustomTimerTask#run()
	 */
	@Override
	public void run() {
		if(ClusterUtil.isPrimaryNode()){		
			
			String sendPendingJPAlert = AppPropertiesReader.getInstance().getValue(PropertyKeyConstant.PROPS_JP_SEND_PENDING_JP_ALERT);
			if(sendPendingJPAlert != null && sendPendingJPAlert.equalsIgnoreCase("true")){
			
				/**Get all the pending jackpot from tree cache and send alert**/		
				String currentKey = null;
				Map jackpotMap=null;
				List<String> removedKeyList= new ArrayList<String>();
				Set<String> keyMap = (Set<String>)JackpotUtil.getAllPendingJackpotCacheKeys(IAppConstants.ALL_PENDING_JACKPOTS_KEY);
				
				Map<Integer,String> licenseParamMap=new HashMap<Integer, String>();
				try{
					if(keyMap != null && !(keyMap.isEmpty())){
						for(String key : keyMap){
	
							currentKey = key;
							jackpotMap =(HashMap) JackpotUtil.getJackpotDTOFromClusterCache(IAppConstants.ALL_PENDING_JACKPOTS,key);
							if(jackpotMap == null) {
								removedKeyList.add(currentKey);					
								if(log.isDebugEnabled()){
									log.debug("Pending JP Info is not available. Hence removed."+currentKey);
								}					
								continue;
							}
	
							Integer siteId=(Integer) jackpotMap.get(IAppConstants.SITE_ID);
							String assetConfigNo=(String)jackpotMap.get(IAppConstants.ASSET_CONFIG_NO);				
							String jpLicenseEnabled = null;
							if(licenseParamMap.containsKey(siteId)){
								jpLicenseEnabled = licenseParamMap.get(siteId);
							}else{
									jpLicenseEnabled = JackpotUtil.getSiteParamValue(ISiteConfigConstants.JACKPOT_LICENSE_ENABLED,
											siteId);
										licenseParamMap.put(siteId, jpLicenseEnabled);
							}
	
							if(jpLicenseEnabled!=null && jpLicenseEnabled.equalsIgnoreCase("yes")) {
	
								/* make a call to asset to validate if the slip slot no is valid*/						
								AssetInfoDTO assetDTO = assetBO.getAssetInfoForSlotNumber(assetConfigNo, siteId);
	
								if(assetDTO!=null && assetDTO.getErrorCode()==0) {// asset error code that slot is valid
	
									long delay=(Long)jackpotMap.get(IAppConstants.TIMER_DELAY);
									long frequency=(Long)jackpotMap.get(IAppConstants.TIMER_FREQUENCY);
									long actualFrequency=0;
									Date date = new Date();
									if(log.isDebugEnabled()) {
										log.debug("date: "+date);
									}								
									Long actualTimeToCheckInMilliSec=date.getTime();	
									if(log.isDebugEnabled()) {
										log.debug("actualTimeToCheckInMilliSec: "+actualTimeToCheckInMilliSec);
										log.debug("TRANSACTION_DATE: "+jackpotMap.get(IAppConstants.TRANSACTION_DATE));
									}														
									Long jackpotCreatedTimeInMilliSec=((Date)jackpotMap.get(IAppConstants.TRANSACTION_DATE)).getTime();
									if(log.isDebugEnabled()) {
										log.debug("jackpotCreatedTimeInMilliSec: "+jackpotCreatedTimeInMilliSec);
									}								
									Long currentTimeInMilliSec=actualTimeToCheckInMilliSec;													
									Long lastAlertSentTime=(Long)JackpotUtil.getJackpotDTOFromClusterCache(IAppConstants.ALL_PENDING_JACKPOTS_KEY,currentKey);
									
									if(lastAlertSentTime==null){
										actualTimeToCheckInMilliSec=jackpotCreatedTimeInMilliSec;
										actualFrequency=delay*60*1000;
									}else{
										actualTimeToCheckInMilliSec=lastAlertSentTime;
										actualFrequency=frequency*60*1000;
									}
									if(log.isDebugEnabled()) {
										log.debug("currentTimeInMilliSec: "+currentTimeInMilliSec);
										log.debug("actualTimeToCheckInMilliSec: "+actualTimeToCheckInMilliSec);
										log.debug("(currentTimeInMilliSec-actualTimeToCheckInMilliSec): "+(currentTimeInMilliSec-actualTimeToCheckInMilliSec));
										log.debug("actualFrequency: "+actualFrequency);
									}
	
									/*check initial delay for the first time and check frequency for next time*/
									if((currentTimeInMilliSec-actualTimeToCheckInMilliSec)>=actualFrequency){
										char exceptionCode = 10;
	
										/** 
										 * PENDING JP SLIP FOR SLOT XXXX (92) to be sent to the AlertsEngine
										 */
										JackpotAlertObject jpAlertsPendingSlip = new JackpotAlertObject();
	
										jpAlertsPendingSlip.setTerminalMessageNumber(IAlertMessageConstants.PENDING_JACKPOT_SLIP_FOR_SLOT);
										jpAlertsPendingSlip.setTerminalMessage("PENDING JP SLIP FOR SLOT XXXX");
										jpAlertsPendingSlip.setJackpotId((char)((Integer.parseInt( (String)jackpotMap.get(IAppConstants.JACKPOT_ID), 16))));
										jpAlertsPendingSlip.setAssetConfigNumber(assetConfigNo);
										jpAlertsPendingSlip.setStandNumber((String)jackpotMap.get(IAppConstants.ASSET_CONFIG_LOCATION));
										jpAlertsPendingSlip.setEngineId(IAppConstants.JACKPOT_ALERTS_ENGINE_ID);
										jpAlertsPendingSlip.setJackpotAmount((Long)jackpotMap.get(IAppConstants.ORIGINAL_AMOUNT));			
										jpAlertsPendingSlip.setExceptionCode(exceptionCode);
										jpAlertsPendingSlip.setMessageId((Long)jackpotMap.get(IAppConstants.MESSAGE_ID));
										jpAlertsPendingSlip.setSendingEngineName(IAppConstants.MODULE_NAME);
										jpAlertsPendingSlip.setSiteId(siteId);
										jpAlertsPendingSlip.setSlotAreaId((String)jackpotMap.get(IAppConstants.AREA_SHORT_NAME));
	
										Calendar calc = Calendar.getInstance();
										calc.setTimeInMillis(((Date)jackpotMap.get(IAppConstants.TRANSACTION_DATE)).getTime());
										jpAlertsPendingSlip.setDateTime(calc);
	
										if(log.isDebugEnabled()){
											log.debug("JP Details :"+currentKey+", Site config Delay:"+delay+", Site config Frequency:"+frequency);
											log.debug("Sending Alert Msg for 92 - PENDING JP FOR SLOT:"+jpAlertsPendingSlip.getTerminalMessage());
										}
										/** SENDING MSG TO ALERTS ENGINE */			
										try {
											alertsBO.sendAlert(jpAlertsPendingSlip);
											JackpotUtil.addJackpotDTOToClusterCache(IAppConstants.ALL_PENDING_JACKPOTS_KEY,currentKey, new Date().getTime());
										} catch (AlertsEngineBOException e) {
											log.error("Exception while sending the pending jp alert msg");
										}		
	
									}
	
								}else {
									/* remove pending JP for invalid slot*/
									JackpotUtil.removePendingJPFromCache(currentKey);
									removedKeyList.add(currentKey);	
									if(log.isInfoEnabled()){							
										log.info("Pending JP is removed from cache due to invalid slot: "+currentKey);
									}
	
								}			
							}else {
								if(log.isInfoEnabled()){
									log.info("Site Parameter for Jackpot Licensing is disabled");
								}
							}
	
						}
						/* All the pending JPs in list are removed from key cache */
						for(String removedKey:removedKeyList){
							SDSTreeCache.getInstance().removeValue(IAppConstants.ALL_PENDING_JACKPOTS_KEY, removedKey);						
							if(log.isDebugEnabled()){
								log.debug("Pending JP key is successfully removed from Cache"+removedKey);
							}											
						}
					}
				}catch(Exception e){
					log.error("Exception in JP timer.",e);				
				}
			}
		}
	}

}
