/*****************************************************************************
 * $Id: PrinterUtil.java,v 1.1, 2008-12-16 14:14:40Z, Ambereen Drewitt$
 * $Date: 12/16/2008 8:14:40 AM$
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

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 * This class holds the utility methods for getting the printers.
 * @author anantharajr
 * @version $Revision: 2$ 
 */
public class PrinterUtil {

	/**
	 * gets all the available printers
	 * @return
	 */
	public PrintService[] getPrinters()
	{
		  PrintService[] service = PrintServiceLookup.lookupPrintServices(null, null);
		  return service;
	}
	/**
	 * gets the printer name
	 * @param index
	 * @return
	 */
	public String getPrinterName(int index)
	{
		if(index >=0)
		{
			//log.info(getPrinters()[index].getName());
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
	public PrintService getPrintService(String printerName)
	{
		int index=0;
		PrintService[] services= getPrinters();
		for(int i=0;i<services.length;i++)
		{
			if(services[i].getName().equalsIgnoreCase(printerName))
			{
					index=i;
					break;
			}
		}
		return services[index];
	}
	
	/**
	 * get the printer count
	 * @return
	 */
	public int getPrinterCount()
	{
		return getPrinters().length;
	}
	/**
	 * gets the printer names
	 */
	public void getPrinterNames()
	{
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
		for(int i=0;i<printService.length;i++)
		{
			//log.info(printService[i].getName());
		}
			
	}
	
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String args[])
	{
		PrinterUtil availablePrinters= new PrinterUtil();
		availablePrinters.getPrinterName(0);
		
		availablePrinters.getPrinterNames();
		//log.info(availablePrinters.getPrinterIndex(availablePrinters.getPrinterName(0)));
		//log.info(availablePrinters.getPrinterIndex("Slip Printer"));
		//log.info("The Total number of printers are"+availablePrinters.getPrinterCount());
		//availablePrinters.registerPrinter(0);
		//log.info("The registered printer is "+availablePrinters.registerPrinter(3));
	
	}
}
