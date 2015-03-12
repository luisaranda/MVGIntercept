/*****************************************************************************
 * $Id: JackpotEngineLogger.java,v 1.1, 2008-05-21 10:35:19Z, Ambereen Drewitt$
 * $Date: 5/21/2008 5:35:19 AM$
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
package com.ballydev.sds.jackpot.log;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;

/**
 * Logger class for the Jackpot Engine
 * @author anantharajr
 * @version $Revision: 2$
 */
public class JackpotEngineLogger {	
	
	/**
	 * Logger Instance
	 */
	private static Logger logger= null;
	
	/**
	 * Process Logger Instance
	 */
	private static Logger processLogger= null;
	
	/**
	 * Bo process Logger Instance
	 */
	private static Logger boLogger= null;
	
	
	/**
	 * Logger for normal logs
	 * @param moduleName
	 * @return
	 */
	public static Logger getLogger(String moduleName){
		  if(logger==null){
			  logger = Logger.getLogger(IAppConstants.MODULE_NAME);
		  }
	      return logger;
	 }
	
	/**
	 * Logger for Process Statistics
	 * @param moduleName
	 * @return
	 */
	public static Logger getProcessLogger(String moduleName){
		  if(processLogger==null){
			  processLogger = Logger.getLogger(moduleName);
		  }
	      return processLogger;
	}
	
	/**
	 * Logger for BO Statistics
	 * @param moduleName
	 * @return
	 */
	public static Logger getBOLogger(String moduleName){
		  if(boLogger==null){
			  boLogger = Logger.getLogger(moduleName);
		  }
	      return boLogger;
	}
	  
}
