/*
 * $Id: PrinterEventListener.java,v 1.0, 2008-03-27 12:19:29Z, Nithyakalyani, Raman$
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

import java.util.EventListener;

public interface PrinterEventListener extends EventListener {
    public void printerEvent(PrinterEvent pe);
}
