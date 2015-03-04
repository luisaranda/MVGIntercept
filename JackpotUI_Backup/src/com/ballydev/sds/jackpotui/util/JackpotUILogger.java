/*****************************************************************************
 * $Id: JackpotUILogger.java,v 1.0, 2008-04-03 15:53:22Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:53:22 AM$
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

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.SDSUILogger;

/**
 * Logger class for jackpot
 * @author anantharajr
 * @version $Revision: 1$
 */
public class JackpotUILogger {
	/**
	 * gets the SDSUILogger.
	 * @param moduleName
	 * @return
	 */
	public static Logger getLogger(String moduleName) {
		Logger logger = SDSUILogger.getLogger(moduleName);
		return logger;

	}
}
