/*****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.util;

import java.util.Set;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.jobs.JackpotJobTimer;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.utils.common.AppPropertiesReader;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;
/**
 * 
 * @author awilliamfranklin
 *
 * This thread loads all the pending JP from DB to cache during server startup.
 */
public class PendingJPCacheLoader implements Runnable {
	/**
	 * Logger instance
	 */
	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	@Override
	public void run() {
		/* Separate thread loads pending Jackpot from DB if not available from the cache.*/
		Set pendingJPFromCache=(Set)JackpotUtil.getAllPendingJackpotCacheKeys(IAppConstants.ALL_PENDING_JACKPOTS);
		if(pendingJPFromCache==null || pendingJPFromCache.size()==0){
			log.info("Start Loading Pending JP from DB to cache ...");		
			try{
				JackpotUtil.loadAllPendingJPFromDBToCache();
				log.info("All Pending JP from DB loaded successfully.");
			}catch(Exception e){
				log.error("Error in Loading Pending JP from DB to cache.", e);
			}		
			
		}else{
			log.info("There is no Pending JP from DB.");
		}
		
		/* reading JP timer property from application property file */
		AppPropertiesReader propertyReader=AppPropertiesReader.getInstance();
		String delayString=propertyReader.getValue(PropertyKeyConstant.PROPS_JP_TIMER_DELAY);
		delayString=delayString.trim();
		String frequencyString=propertyReader.getValue(PropertyKeyConstant.PROPS_JP_TIMER_FREQUENCY);
		frequencyString=frequencyString.trim();
		
		long delay=0;
		long frequency=1;
		try{
			delay=(delayString==null ||delayString.equals("")?0:Long.parseLong(delayString));
		}catch(NumberFormatException ne){
			delay=0;
		}
		try{
			frequency=(frequencyString==null ||frequencyString.equals("")?0:Long.parseLong(frequencyString));
		}catch(NumberFormatException ne){
			frequency=1;
		}
		
		try {	
		JackpotJobTimer.startPendingJackpotAlertProcessTimer(delay,frequency);			
		} catch (Exception e) {
			log.error("Exception @ JackpotEngine's PendingJPCacheLoader Thread.", e);
		} catch (Throwable e) {
			log.error("Throwable @ JackpotEngine's PendingJPCacheLoader Thread.", e);
		}
	}
}
