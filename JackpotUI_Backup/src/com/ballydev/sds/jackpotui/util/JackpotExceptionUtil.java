/*****************************************************************************
 * $Id: JackpotExceptionUtil.java,v 1.0, 2008-09-26 10:26:09Z, Ambereen Drewitt$
 * $Date: 9/26/2008 5:26:09 AM$
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
package com.ballydev.sds.jackpotui.util;

import org.jboss.remoting.CannotConnectException;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.constants.IExceptionConstants;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;

/**
 * Util class that handles the general exceptions
 * @author dambereen
 * @version $Revision: 1$
 */
public class JackpotExceptionUtil {

	/**
	 * Method to handle the general exceptions that occur within tge EJB service class and throws JackpotEngineServiceException 
	 * @param e
	 * @throws JackpotEngineServiceException
	 */
	public static void getGeneralException(Exception e) throws JackpotEngineServiceException{
		
		if (e instanceof CannotConnectException) {
			throw new JackpotEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR), e);
		}
		else if(e instanceof RuntimeException && e.getMessage()!=null && ((e.getMessage().startsWith(IExceptionConstants.RUNTIME_ERROR_UNREACH_SERVICE_MSG)) ||
                (e.getMessage().startsWith(IExceptionConstants.RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG)))){
			
			throw new JackpotEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR), e);
		}else{
			throw new JackpotEngineServiceException(e);
		}
	}
	
	/**
	 * Method to handle the general exceptions that occur within a Controller class
	 * and is handled by the JackpotUiExceptionHandler
	 * @param e
	 * @throws JackpotEngineServiceException
	 */
	public static void getGeneralCtrllerException(Exception e) throws JackpotEngineServiceException{
		
		if (e instanceof CannotConnectException) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
		}
		else if(e instanceof RuntimeException && e.getMessage()!=null && ((e.getMessage().startsWith(IExceptionConstants.RUNTIME_ERROR_UNREACH_SERVICE_MSG)) ||
                (e.getMessage().startsWith(IExceptionConstants.RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG)))){
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
		}else{
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e,  e.getMessage());
		}
	}
	
	
}
