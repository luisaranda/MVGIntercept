/*****************************************************************************
 *           Copyright Bally Gaming Inc. 1977-2008 
 *  All programs and files on this medium are operated under U.S. 
 *  patents Nos. 5,429,361 & 5,470,079. 
 *  
 *  All programs and files on this medium are copyrighted works and all rights 
 *  are reserved. Duplication of these in whole or in part is prohibited 
 *  without written permission from Bally Gaming Inc., 
 *  Las Vegas, Nevada, U.S.A
 ****************************************************************************/
package com.ballydev.sds.jackpot.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;
import com.ballydev.sds.jackpot.util.PendingJPCacheLoader;


/**
 * Servlet class which initiates JP timer to send pending JP alerts.
 * @author awilliamfranklin
 * @date 12/05/2010
 */
public class JackpotServlet extends HttpServlet{

	/**
	 * Logger instance
	 */
	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * init method is used to start the scheduler to send alerts for the pending jackpots
	 * @param 
	 * @return void
	 * @throws ServletException
	 */

	public void init() throws ServletException{
		if (log.isInfoEnabled()) {
			log.info("Jackpot Engine InitServlet invoked");
		}		
		new Thread(new PendingJPCacheLoader()).start();
	}

}
