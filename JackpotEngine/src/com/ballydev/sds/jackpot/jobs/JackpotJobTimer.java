/*****************************************************************************
 * $Id: JackpotJobTimer.java,v 1.4, 2010-06-22 15:49:54Z, Antony Samy , William Franklin$
 * $Date: 6/22/2010 10:49:54 AM$
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

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.util.CustomTimer;
import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.utils.cluster.ClusterUtil;

/**
 * Class to start/ stop the timer
 * @author dambereen	
 * @version $Revision: 5$
 */
public class JackpotJobTimer implements IAppConstants{
	
	/**
	 * Logger instance
	 */
	private static Logger log = JackpotEngineLogger.getLogger(MODULE_NAME);
	
	/**
	 * Instance of JackpotJobTimer
	 */
	private static JackpotJobTimer jackpotTimer;

	/**
	 * Instance of CustomTimer
	 */
	private static CustomTimer customTimer;

	
	/**
	 * Private Constructor
	 */	
	private JackpotJobTimer(){
	}
	
	/**
	 * Static block to create JackpotJobTimer instance
	 */
	static{
		jackpotTimer = new JackpotJobTimer();
	}
	
	/**
	 * Method to get the instance of JackpotJobTimer class
	 * @return
	 * @throws Exception
	 */
	public static JackpotJobTimer getInstance() throws Exception{
		return jackpotTimer;
	}

	
	/**
	 * Method to start the jackpot timer
	 * @param siteXMins
	 * @param siteYMins
	 * @throws Exception
	 */
	public static void startPendingJackpotAlertProcessTimer(long siteXMins, long siteYMins) throws Exception
	{
		if(log.isInfoEnabled()){		
		log.info("JP timer starts with delay "+siteXMins+" and frequency "+siteYMins+" in mins.");
		}
		customTimer = new CustomTimer();
		customTimer.schedule(new JackpotJobTask(), siteXMins * 60 * 1000L, siteYMins * 60 * 1000L);
			
	}		
}