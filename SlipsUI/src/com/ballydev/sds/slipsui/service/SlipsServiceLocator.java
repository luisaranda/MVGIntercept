/**
 * 
 */
package com.ballydev.sds.slipsui.service;

import com.ballydev.sds.slips.exception.SlipsEngineServiceException;

/**
 * @author gsrinivasulu
 *
 */
public class SlipsServiceLocator {
	
	private static ISlipsService slipsService=null;
	
	private static SlipsServiceLocator slipsServiceLocator=null;
	
	private SlipsServiceLocator() {
		
	}
	
	public static ISlipsService getService() throws Exception {
		try {
			if(slipsServiceLocator == null) {				
//				boolean isWebService = ServerProperties.getInstance().isWebService();
//				if(isWebService) {
//					slipsService = new SlipsServiceWSImpl();
//				} else {
					slipsService = new SlipsServiceEJBImpl();
//				}
				slipsServiceLocator = new SlipsServiceLocator();
			}
		} catch (Exception e) {
			slipsServiceLocator = null;
			e.printStackTrace();
			throw new SlipsEngineServiceException(e);
		}
		return slipsService;
	}
	
	/**
	 * Nullifies Slips Service Objects
	 */
	public static void destroySlipService() {
		
		if(slipsServiceLocator != null) {
			slipsServiceLocator=null;
		}
		if(slipsService!=null) {
			slipsService=null;
		}
	}

}
