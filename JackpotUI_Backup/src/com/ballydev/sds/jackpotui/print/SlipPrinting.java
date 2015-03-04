/*****************************************************************************
 * $Id: SlipPrinting.java,v 1.5, 2008-05-08 16:02:15Z, Ambereen Drewitt$
 * $Date: 5/8/2008 11:02:15 AM$
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
package com.ballydev.sds.jackpotui.print;

 /**
 * @author Kumar Vittoba email:KVittoba@ballytech.com
 * @version $Revision: 6$
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Slips Printing class provides method to send to printers
 * @author anantharajr
 * @version $Revision: 6$
 */
public class SlipPrinting {
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * default constructor
	 */
	public SlipPrinting() {
	}

	/**
	 * method to send the text to printer
	 * @param ps
	 */
	public void printSlip(String ps) {
		try {
			/** delete the temporary file used for printing, if exists */
			//new File("slip_prn").delete();

			File file = new File("slip_prn");
			if(file.exists()){
				file.delete();
			}

			/** Create file if it does not exist*/
			boolean success = file.createNewFile();

			if (success) {
				if (file.canWrite()) {
					BufferedWriter wri = new BufferedWriter(new FileWriter(
							file, true));
					wri.write(ps);
					wri.close();
				}
			}

			InputStream is = new BufferedInputStream(new FileInputStream(file));

			/** Find a factory that can do the conversion*/
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//			PrintService service = PrintServiceLookup
//					.lookupDefaultPrintService();
//			
			PrintService service=null;				
			PrinterUtil printerUtil = new PrinterUtil();			
			log.info("Selected Printer Name: "+SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY));
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY));
			/** Create the print job*/
			DocPrintJob job = service.createPrintJob();
			Doc doc = new SimpleDoc(is, flavor, null);
			/** Print it*/
			job.print(doc, null);
			is.close();
			//file.delete();
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2, MessageKeyConstants.EXCEPTION_OCCURED_DURING_SLIP_PRINT);
			log.error("Exception while printing the slip",e2);
			//return;
		}
	}
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String args[])
	{
		SlipPrinting slipPrinting = new SlipPrinting();
		slipPrinting.printSlip("Hello how r u?:");
	}
	
}
