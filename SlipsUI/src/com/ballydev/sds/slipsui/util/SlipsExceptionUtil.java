/*****************************************************************************
 * $Id: SlipsExceptionUtil.java,v 1.0, 2008-09-26 10:25:56Z, Ambereen Drewitt$
 * $Date: 9/26/2008 5:25:56 AM$
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
package com.ballydev.sds.slipsui.util;

import org.jboss.remoting.CannotConnectException;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;

/**
 * Util class that handles the general exceptions
 * @author dambereen
 * @version $Revision: 1$
 */
public class SlipsExceptionUtil {

	/**
	 * Method to handle the general exceptions that occur within tge EJB service class and throws SlipsEngineServiceException 
	 * @param e
	 * @throws SlipsEngineServiceException
	 */
	public static void getGeneralException(Exception e) throws SlipsEngineServiceException{
		
		if (e instanceof CannotConnectException) {
			throw new SlipsEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR), e);
		}
		else if(e instanceof RuntimeException && e.getMessage()!=null && ((e.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_UNREACH_SERVICE_MSG)) ||
                (e.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG)))){
			
			throw new SlipsEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR), e);
		}else{
			throw new SlipsEngineServiceException(e);
		}
	}
	
	/**
	 * Method to handle the general exceptions that occur within a Controller class
	 * and is handled by the SlipsEngineServiceException
	 * @param e
	 * @throws SlipsEngineServiceException
	 */
	public static void getGeneralCtrllerException(Exception e) throws SlipsEngineServiceException{
		
		if (e instanceof CannotConnectException) {
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
		}
		else if(e instanceof RuntimeException && e.getMessage()!=null && ((e.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_UNREACH_SERVICE_MSG)) ||
                (e.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG)))){
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
		}else{
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e,  e.getMessage());
		}
	}
	
	
}
