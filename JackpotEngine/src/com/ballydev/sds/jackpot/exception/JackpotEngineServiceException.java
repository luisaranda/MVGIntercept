/*****************************************************************************
 * $Id: JackpotEngineServiceException.java,v 1.3, 2008-04-11 13:20:19Z, Ambereen Drewitt$
 * $Date: 4/11/2008 8:20:19 AM$
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
package com.ballydev.sds.jackpot.exception;

/**
 * A class to handle jackpot engine service exceptions
 * @author dambereen
 * @version $Revision: 4$
 */
public class JackpotEngineServiceException extends Exception {
	
	/**
	 * Message Strign instance
	 */
	private String msg;

	/**
	 * JackpotEngineServiceException default constructor
	 */
	public JackpotEngineServiceException(){
		super();
	}
	
	/**
	 * JackpotEngineServiceException constructor
	 * @param message
	 */
	public JackpotEngineServiceException(String message) {
		super(message);
	}
	
	/**
	 * JackpotEngineServiceException constructor
	 * @param cause
	 */
	public JackpotEngineServiceException(Throwable cause){
		super(cause);
	}

	/**
	 * JackpotEngineServiceException constructor
	 * @param message
	 * @param cause
	 */
	public JackpotEngineServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
