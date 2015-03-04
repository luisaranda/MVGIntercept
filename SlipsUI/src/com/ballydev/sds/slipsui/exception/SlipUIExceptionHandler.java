/*****************************************************************************
 * $Id: SlipUIExceptionHandler.java,v 1.2, 2008-08-08 14:23:29Z, Ambereen Drewitt$
 * $Date: 8/8/2008 9:23:29 AM$
 * $Log$
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
package com.ballydev.sds.slipsui.exception;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Class to handle exceptions and displays an error message dialog box.
 * @author dambereen
 * @version $Revision: 3$
 */
public class SlipUIExceptionHandler{

	/**
	 * Instance of SlipsEngineServiceException
	 */
	private SlipsEngineServiceException slipsEngineServiceException = new SlipsEngineServiceException();
	
	/**
	 *Logger Instance 
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * SlipUIExceptionHandler Constructor
	 */
	public SlipUIExceptionHandler(){
		
	}
	
	/**
	 * SlipUIExceptionHandler Constructor
	 * @param SlipsEngineServiceException se
	 */
	public SlipUIExceptionHandler(SlipsEngineServiceException origException){
		this.slipsEngineServiceException = origException;
	}
	
	/**
	 * This method returns the String as User message
	 */
	public String getUserMessage(){
		/** CAN BE CUSTOMIZED AS PER PRESENTATION **/
		return LabelLoader.getLabelValue(slipsEngineServiceException.getMessage());
	}

	/**
	 * This method returns the String as detailed message
	 */
	public String getDetailedMessage(){
		/** PRINTS THE DETAILED ERROR FOR LOGIN PURPOSES	**/
		StringBuffer detailedMessage = new StringBuffer();
		StackTraceElement[] elements = slipsEngineServiceException.getStackTrace();
		detailedMessage.append(slipsEngineServiceException.getMessage());
		detailedMessage.append("\n");
		for(int i=0;i<elements.length;i++){
			detailedMessage.append(elements[i].toString());
			detailedMessage.append("\n");
		}
		return detailedMessage.toString();
	}
	
	/**
	 * This method displayes the message dialog when the 
	 * exception is thrown in the specified composite
	 * @param composite
	 * @param e
	 * @param user message
	 */
/*	public void handleClientException(SlipsEngineServiceException e){
		SlipUIExceptionHandler handler = new SlipUIExceptionHandler(e);
		MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(handler.getUserMessage()));
		log.error(handler.getDetailedMessage());
		e.printStackTrace();
	}*/
	
	/**
	 * This method displayes the message dialog when the 
	 * exception is thrown in the specified composite
	 * @param composite
	 * @param e
	 * @param user message
	 */
	public void handleException(Exception e, String message){
		//Handled for the first time the service is got but Jboss is down
		if(message.indexOf(": ") != -1){
			message = LabelLoader.getLabelValue(message.substring(message.indexOf(": ")+2, message.length()));
		}
		MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(message));
		log.error(e.getMessage());
	}

}
