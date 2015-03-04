/*****************************************************************************
 * $Id: SlipUILogger.java,v 1.0, 2008-04-21 05:42:06Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:42:06 AM$
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

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.SDSUILogger;

public class SlipUILogger {
	public static Logger getLogger(String moduleName) {
		Logger logger = SDSUILogger.getLogger(moduleName);
		return logger;

	}
}
