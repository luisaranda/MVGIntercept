/*****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.util;

/**
 * Util class to calculate a unique message id 
 * @author anantharajr
 * @version $Revision: 1$
 */
public class MessageUtil {

	/**
	 * method to get the message id
	 * @return
	 */
	public synchronized static long getMessageId(){
		return System.nanoTime();
	}

}
