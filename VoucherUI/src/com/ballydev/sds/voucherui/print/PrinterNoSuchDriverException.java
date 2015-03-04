/*
 * $Id: PrinterNoSuchDriverException.java,v 1.1, 2010-01-12 08:43:47Z, Lokesh, Krishna Sinha$
 *
 * Copyright Bally Gaming Inc. 2001-2004
 *
 * All programs and files on this medium are operated under U.S. patents
 * Nos. 5,429,361 & 5,470,079.
 *
 * All programs and files on this medium are copyrighted works and all
 * rights are reserved.
 * 
 * Duplication of these in whole or in part is prohibited without written
 * permission from Bally Gaming Inc., Las Vegas, Nevada, U.S.A.
 *
 */
package com.ballydev.sds.voucherui.print;

public class PrinterNoSuchDriverException extends PrinterException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new PrinterNoSuchDriverException object.
     */
    public PrinterNoSuchDriverException() {
        super();
    }

    public PrinterNoSuchDriverException(String s) {
        super(s);
    }
}
