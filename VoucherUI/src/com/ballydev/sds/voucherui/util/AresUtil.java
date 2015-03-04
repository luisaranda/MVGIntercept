/*****************************************************************************
 * $Id: AresUtil.java,v 1.3, 2011-03-16 14:35:54Z, Verma, Nitin Kumar$
 * $Date: 3/16/2011 8:35:54 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
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
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * This class registers the ares event for barcode scanning
 * @author Nithya kalyani R
 * @version $Revision: 4$ 
 */
public class AresUtil {


	/** Logger Instance */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * This method registers the ares barcode scanner
	 * @param ares
	 */
	public static Ares registerAresEvent(Ares ares){
		Boolean tktScanner  = SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerEnabled();
//		String manufacturer = SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerManft();
//		String readerModel  = SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerModel();
		String port 		= SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerPort().trim();
		try {
			if(tktScanner) {
//		 		ares = AresObject.getAres();
				if(SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerManft().equalsIgnoreCase("Duplo")){
					stopScanner(ares);
					closeScannerPort(ares);
				} else if(SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerManft().equalsIgnoreCase("Cummins")) {
					closeScannerPort(ares);
				} else if(SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerManft().equalsIgnoreCase("MultiScan")) {
					closeScannerPort(ares);
				} else if(SDSApplication.getApplicationSessionData().getScannerPreferenceUtil().getTktScannerManft().equalsIgnoreCase("Metrologic")) {
					stopScanner(ares);
					resetScanner(ares);
					closeScannerPort(ares);
				}
				log.debug("Ares before open: " + ares);
				ares.open(port);
				log.debug("Ares after open: " + ares);
			} else {
				log.error("Scanner connection not set");
			}
		} catch (Exception e) {
			log.error("Exception occured in scanner connection:  " + e.getMessage());
			if(ares != null) {
				stopScanner(ares);
				resetScanner(ares);
				closeScannerPort(ares);
			}
			ares = null;
		}
		return ares;
	}


	/**
	 * This method resets the scanner
	 */
	public static void resetScanner(Ares ares) {
		try {
			ares.reset();
		}catch (Exception e) {
			log.error("ares.reset exception :" + e);
		}
	}

	/**
	 * This method stops the scanner
	 */
	public static void stopScanner(Ares ares) {
		try {
			ares.stop();
		}catch (Exception e) {
			log.error("ares.stop exception :" + e);
		}
	}

	/**
	 * This method closes the scanner port
	 */
	public static void closeScannerPort(Ares ares) {
		try{
			ares.close();
		} catch(Exception e) {
			log.error("ares.close exception :" + e);
		}
	}

}
