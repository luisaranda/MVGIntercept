/*****************************************************************************
 * $Id: JackpotServiceLocator.java,v 1.4, 2010-01-29 11:32:04Z, Ambereen Drewitt$
 * $Date: 1/29/2010 5:32:04 AM$
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


import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;


/**
 * Class that will fetch the Jackpot Service
 * @author dambereen
 * @version $Revision: 5$
 */
public class JackpotServiceLocator {

	/**
	 * JackpotServiceLocator instance
	 */
	private static JackpotServiceLocator jackpotServiceLocator;
	
	/**
	 * IJackpotService instance
	 */
	private static IJackpotService jackpotService;
	
	/**
	 * Class constructor
	 */
	private JackpotServiceLocator() {
		
	}
	
	/**
	 * Method to get the Jackpot service
	 * @return
	 * @throws JackpotEngineServiceException
	 */
	public static IJackpotService getService() throws JackpotEngineServiceException {
		try {
			if(jackpotServiceLocator == null) {
				jackpotServiceLocator = new JackpotServiceLocator();				
//				boolean isWebService = ServerProperties.getInstance().isWebService();
//				if(isWebService) {
//					jackpotService = new JackpotWebService();
//				} else {
					jackpotService = new JackpotEJBService();
//				}
			}			
		} catch (Exception e) {
			throw new JackpotEngineServiceException(e);
		}
		return jackpotService;
	}
	
	/**
	 * This method makes the service to null
	 */
	public static void nullJackpotService(){
		jackpotService = null;
		jackpotServiceLocator = null;
	}
}
