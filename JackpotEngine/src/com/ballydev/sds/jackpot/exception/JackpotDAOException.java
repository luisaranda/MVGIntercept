/*****************************************************************************
 * $Id: JackpotDAOException.java,v 1.1, 2008-04-11 13:20:24Z, Ambereen Drewitt$
 * $Date: 4/11/2008 8:20:24 AM$
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

import javax.ejb.ApplicationException;

/**
 * A class to handle jackpot engine dao exceptions
 * @author dambereen
 * @version $Revision: 2$
 */
@ApplicationException(rollback=true)
public class JackpotDAOException extends JackpotEngineServiceException {
	
	public JackpotDAOException (Throwable cause){
		super(cause);
	}
}
