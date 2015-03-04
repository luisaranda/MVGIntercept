/*****************************************************************************
 *  Copyright (c) 2003 Bally Gaming Inc.  1977 - 2003
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.print;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;

/**
 * This class holds the utility methods for getting the printers.
 * @author Nithya kalyani
 * @version $Revision: 5$ 
 */
public class PrinterUtil {

	/**
	 * gets all the available printers
	 * @return
	 */
	public PrintService[] getPrinters() throws Exception{
		  PrintService[] service = PrintServiceLookup.lookupPrintServices(null, null);
		  if(service==null) {
			  System.out.println("Service null");
			  throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
		  }
		  return service;
	}
	/**
	 * gets the printer name
	 * @param index
	 * @return
	 */
	public String getPrinterName(int index) throws Exception{
		if(index >=0){			
			return getPrinters()[index].getName();
			
		}
		else
		{	
			return"";
		}
	}
	/**
	 * gets the print service
	 * @param printerName
	 * @return
	 */
	public PrintService getPrintService(String printerName) throws Exception{
		int index=0;
		PrintService[] services =null;
		try {
			services = getPrinters();			
			for(int i=0;i<services.length;i++){				
				if( services[i].getName().equalsIgnoreCase(printerName) || services[i].getName().contains((printerName)) ){
						index=i;
						break;
				}
			}
		} catch (Exception e) {
			throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
		}
		return (services==null || services.length==0)?null:services[index];
	}
	/**
	 * get the printer count
	 * @return
	 */
	public int getPrinterCount() throws Exception{
		return getPrinters().length;
	}
	/**
	 * gets the printer names
	 */
	public void getPrinterNames(){
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
		for(int i=0;i<printService.length;i++)
		{
			//log.info(printService[i].getName());
		}
			
	}

}
