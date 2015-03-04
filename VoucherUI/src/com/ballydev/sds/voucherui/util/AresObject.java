/*****************************************************************************
 * Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.util;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.ares.Ares;
import com.ballydev.sds.framework.ares.AresNoSuchDriverException;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * Singleton object to set Ares value globally.
 * @author vnitinkumar
 */
public class AresObject {

	static String manufacturer;
	static String readerModel;
	
	/** Instance of Ares */
	private static Ares ares;
	
	/** Logger Instance */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	public static synchronized Ares getAres(){
		
		if(ares == null){
			manufacturer = SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerManft();
			readerModel  = SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerModel();
			log.debug("Reader Manufacturer: " + manufacturer);
			log.debug("Reader Model: " + readerModel);
			try {
				ares = Ares.getBarcodeReader(manufacturer, readerModel);
			} catch (AresNoSuchDriverException e) {
				e.printStackTrace();
			}		
		}		
		return ares;
	}
	
	public static synchronized Ares getAres(String manufacturer, String readerModel){
		
		if( ares == null ){
			log.debug("Reader Manufacturer: " + manufacturer);
			log.debug("Reader Model: " + readerModel);
			try {
				ares = Ares.getBarcodeReader(manufacturer, readerModel);
			} catch (AresNoSuchDriverException e) {
				e.printStackTrace();
			}		
		}		
		return ares;
	}
}
