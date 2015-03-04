/*****************************************************************************
 * $Id: JackpotUiExceptionHandler.java,v 1.4, 2008-08-08 08:58:40Z, Ambereen Drewitt$
 * $Date: 8/8/2008 3:58:40 AM$
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
package com.ballydev.sds.jackpotui.exception;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.jackpot.exception.JackpotEngineServiceException;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Class to handle exceptions and displays an error box.
 * @author dambereen
 * @version $Revision: 5$
 */
public class JackpotUiExceptionHandler{

	/**
	 * Instance of JackpotEngineServiceException
	 */
	private JackpotEngineServiceException jackpotEngineServiceException = new JackpotEngineServiceException();
	
	/**
	 *Logger Instance 
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * JackpotUiExceptionHandler Constructor
	 */
	public JackpotUiExceptionHandler(){
		
	}
	
	/**
	 * This method returns the String as User message
	 */
	public String getUserMessage(){
		/** CAN BE CUSTOMIZED AS PER PRESENTATION **/
		return LabelLoader.getLabelValue(jackpotEngineServiceException.getMessage());
	}
	
	/**
	 * This method displayes the message dialog when the 
	 * exception is thrown in the specified composite
	 * @param composite
	 * @param excep
	 * @param user message
	 */
	public void handleException(Exception excep, String message){
		//Handled for the first time the service is got but Jboss is down
		if(message.indexOf(": ") != -1){
			message = LabelLoader.getLabelValue(message.substring(message.indexOf(": ")+2, message.length()));
		}
		MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(message));
		log.error(excep.getMessage());
	}

}
