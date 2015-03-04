/*****************************************************************************
 * $Id: DisplayNoSuchDriverException.java,v 1.1, 2010-01-12 08:43:47Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:43:47 AM$
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
package com.ballydev.sds.voucherui.displays;

/**
 * This exception is created when there does not exist a <code>Display</code>
 * class that implements a requested protocol.
 */
public class DisplayNoSuchDriverException extends Exception {

        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		/**
         * Create a new DisplayNoSuchDriverException object.
         */
        public DisplayNoSuchDriverException() {
                super();
        }

        /**
         * Creates a new DisplayNoSuchDriverException object with the
         * exception message set to the string parameter.
         *
         * @param s the message of the exception
         */
        public DisplayNoSuchDriverException(String s) {
                super(s);
        }
}
