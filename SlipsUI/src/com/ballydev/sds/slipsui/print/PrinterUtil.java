/**
 * 
 */
package com.ballydev.sds.slipsui.print;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 * @author gsrinivasulu
 * @version $Revision: 1$
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
