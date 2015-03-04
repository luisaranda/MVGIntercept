/*****************************************************************************
 * $Id: PrintWithGraphics.java,v 1.2, 2009-03-28 14:55:56Z, Ambereen Drewitt$
 * $Date: 3/28/2009 8:55:56 AM$
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
package com.ballydev.sds.slipsui.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.StringTokenizer;

import javax.print.PrintService;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Class to create print the slip using Printable interface
 * @author dambereen
 * @version $Revision: 3$ 
 */
public class PrintWithGraphics implements Printable{
	
	/**
	 * Default page height
	 */
	private int iPageHeight = 58;	

	/**
	 * Batch row index
	 */
	private int iBatchRow = 0;

	/**
	 * String that has the text to print
	 */
	private String textToPrint = "";
		
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
			.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * PrintWithGraphics constructor
	 */
	public PrintWithGraphics(){
		
	}
		

	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException 
	{		
		log.info("Calling graphics print() method");
		int headerRow = 85;
		try {
			
			graphics.setColor(Color.black);
			graphics.setFont(Font.decode("courier-BOLD-12"));
			
			int iCurrentCount = 0;
			int iLeftMargin = 76;
						
			log.debug("iPageHeight: "+iPageHeight);
			
			int iPrintRow = pageIndex * iPageHeight;
			
			log.debug("pageIndex: "+pageIndex);
			log.debug("iPrintRow: "+iPrintRow);
			
			int lastPage = (iBatchRow) / iPageHeight;
			
			log.debug("iBatchRow: "+iBatchRow);
			
			if (iBatchRow > (lastPage * iPageHeight)) {
				lastPage++;
			}			
			log.debug("lastPage: "+lastPage);
			
			if (pageIndex >= lastPage) {
				log.debug("LAST PAGE IS REACHED");
				return Printable.NO_SUCH_PAGE;
			}				
			
			int numOfRowsToPrint = iPageHeight;
			if ((lastPage - pageIndex) == 1) {
				numOfRowsToPrint = iBatchRow - ((lastPage - 1) * iPageHeight);
			}
			log.debug("numOfRowsToPrint: "+numOfRowsToPrint);
			
			String[] strToken = textToPrint.split("\r\n");
			
			for(int i = 0; i < (numOfRowsToPrint); i++){
				try {
					graphics.drawString(strToken[iPrintRow], iLeftMargin, headerRow + (11 * iCurrentCount));
				} catch (Exception e) {
					log.error("Error in printing", e);
				}
				iPrintRow++;
				iCurrentCount++;
			}	
			log.debug("iCurrentCount 1: "+iCurrentCount);
			iCurrentCount = headerRow + (11 * iCurrentCount);
			log.debug("iCurrentCount 2: "+iCurrentCount);
			
			
		} catch (Exception e) {		
			log.error(e);
		}
		return Printable.PAGE_EXISTS;
	}
	
	/**
	 * Method to print the slip textand to invoke the Printable Interface's print method 
	 * @param textToPrint
	 * @param image
	 * @throws Exception
	 */
	public void printTextImage(String textToPrint) throws Exception{	

		try {
			this.textToPrint = textToPrint;
			StringTokenizer tokenizer = new StringTokenizer(textToPrint, "\n");
			iBatchRow = tokenizer.countTokens();

			// Find a factory that can do the conversion			
			PrintService service=null;				
			PrinterUtil printerUtil = new PrinterUtil();
						
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER));
						
			// Create the print job			
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
						
			job.setPrintable(this, pf);
			job.setPrintService(service);			
			
			// Print it
			job.print();
		} catch (Exception e2) {	
			log.error("excep: "+e2);
			throw e2;
		}
	}	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		PrintWithGraphics printWithGraphics = new PrintWithGraphics();
		try {			
			StringBuilder stringBuilder = new StringBuilder();
			for(int i=0; i<90; i++) {
			stringBuilder.append("test string: i: "+i);
			stringBuilder.append("\r\n");		
			}
			//Image image = printWithGraphics.createBarcode();			
			printWithGraphics.printTextImage(stringBuilder.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
