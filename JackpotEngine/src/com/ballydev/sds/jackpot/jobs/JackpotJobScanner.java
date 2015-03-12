/*****************************************************************************
 * $Id: JackpotJobScanner.java,v 1.3, 2010-09-01 16:12:32Z, Anbuselvi, Balasubramanian$
 * $Date: 9/1/2010 11:12:32 AM$
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


/**
 * This class scans the time to end a drop
 * @author dambereen
 * @version $Revsion: 1$
 */
public class JackpotJobScanner {
		
//	/**
//	 * Instance of the Logger 
//	 */
//	private static Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
//	
//	/**
//	 * Variable that holds the site param value for 
//	 * SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD
//	 */
//	private long sendMsgUnderXMinsOld = 0;
//	
//	/**
//	 * Variable that holds the site param value for 
//	 * SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD
//	 */
//	private long sendMsgAtleastYMinsOld = 0;
//	
//	/**
//	 * Constructor
//	 *
//	 */
//	public JackpotJobScanner(){	
//		
//	}
//
//	/**
//	 * Method thats called by the Jackpot Facade to initiate the timer
//	 * @param jackpotDTO
//	 * @throws Exception
//	 */
//	public void scanForPendingJackpotAlert(JackpotDTO jackpotDTO) throws Exception{
//		log.info("Inside scanForPendingJackpotAlert method");
//		
//		List<SiteConfigurationDTO> siteDTOLst = null;				
//		/* GET SITE PARAMETER VALUES */		
//		List<String> siteParamLst = new ArrayList<String>();		
//		siteParamLst.add(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD);
//		siteParamLst.add(ISiteConfigConstants.SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD);
//		ISiteConfigBO siteConfigBO = (ISiteConfigBO)ServiceLocator.fetchService(IAppConstants.SITE_CONFIG_JNDI_NAME);
//		
//		try {
//			siteDTOLst = siteConfigBO.getSiteConfigurationValues(siteParamLst, jackpotDTO.getSiteId());
//			if(siteDTOLst!=null && siteDTOLst.size()>0) {
//				sendMsgUnderXMinsOld = siteDTOLst.get(0)!=null ? Integer.parseInt(siteDTOLst.get(0).getSiteConfigurationValue()) : 0;				
//				sendMsgAtleastYMinsOld = siteDTOLst.get(1)!=null ? Integer.parseInt(siteDTOLst.get(1).getSiteConfigurationValue()) : 0;
//				log.info("sendMsgUnderXMinsOld: "+sendMsgUnderXMinsOld);
//				log.info("sendMsgAtleastYMinsOld: "+sendMsgAtleastYMinsOld);				
//			}else{
//				log.error("Site param values returned are null");
//				throw new Exception("Site param values returned are null");
//			}
//		} catch (SiteConfigBOException e) {
//			log.error("Exception while getting site params: "+e);
//			throw new Exception(e);
//		}
//		
//		/* CHECK FOR TIME LIMIT */
//		
//		if(jackpotDTO!=null && sendMsgUnderXMinsOld>0 && sendMsgAtleastYMinsOld>0) {			
//			
//			log.info("Calling the startPendingJackpotAlertProcess of JackpotJobTimer");
//			JackpotJobTimer.startPendingJackpotAlertProcessTimer(jackpotDTO, sendMsgUnderXMinsOld, sendMsgAtleastYMinsOld);
//		}
//	}
	
	
}
