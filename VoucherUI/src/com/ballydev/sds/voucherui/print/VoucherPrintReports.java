/**
 * $Id: VoucherPrintReports.java,v 1.10.2.0, 2012-09-27 10:46:50Z, SDS12.3.3 Checkin User$
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.Paper;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.log.VoucherUILogger;


/**
 * This class acts as a utility for printing
 * the plain text.
 * @author Nithya kalyani R
 * @version $Revision: 12$ 
 */
public class VoucherPrintReports implements Printable{
	/**
	 * Default page height
	 */
	private int maxPageHeight = 30;

	/**
	 * String that needs to be set in the footer
	 */
	private String strFooter = "";

	/**
	 * String that has the text to print
	 */
	private String textToPrint = "";
	
	/**
	 * Left margin value
	 */
	private int iLeftMargin = 59;
	
	private String fontType = "Courier New-PLAIN-5";
	
	private String page_break = "."+(char)12;
	
	private String[] pages = null;
	
	private int[] linesPerPage = null;

	private String line_break = "\r\n";
	
	private static final Logger logger = VoucherUILogger.getLogger(IDBLabelKeyConstants.MODULE_NAME);

	public VoucherPrintReports() {
	}

	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		int iPageHeight = getPageHeight(pageIndex);
		int headerRow = 85;
		try {
			graphics.setColor(Color.black);
			graphics.setFont(Font.decode(this.fontType));
			int iCurrentCount = 0;

			int iPrintRow = 0;

			if(linesPerPage.length > (pageIndex + 1)) {
				iPrintRow = linesPerPage[pageIndex];
			} else {
				return Printable.NO_SUCH_PAGE;
			}
			
			if(iPageHeight == 0) {
				return Printable.PAGE_EXISTS;
			}

			int numOfRowsToPrint = iPageHeight;

			String[] strToken = textToPrint.replaceAll(page_break, "").split("\r\n");
			for(int i = 0; i < numOfRowsToPrint; i++){
				try {
					graphics.drawString(strToken[iPrintRow], iLeftMargin, headerRow + (11 * iCurrentCount));
				} catch (Exception e) {					
					e.printStackTrace();
				}
				iPrintRow++;
				iCurrentCount++;
			}

			graphics.drawString(strFooter, 578, 540);
		} catch (Exception e) {		
				
		}
		return Printable.PAGE_EXISTS;
	
	}
	

	private int getPageHeight(int pageIndex) {
		String[] tokenizer = null;
		try {
			tokenizer = pages[pageIndex].split("\r\n");
		} catch (Exception e) {
			return maxPageHeight;
		}
		
		int height = tokenizer.length;

		return height;
	}

	/**
	 * This method accepts the string that needs to be printed
	 * and sets that string to the text that will be printed
	 * from the paint method
	 * @param textToPrint
	 */
	public void printText(String textToPrint) throws Exception {	
		try {
			this.textToPrint = textToPrint;
			this.iLeftMargin = 0;
			this.fontType = "courier-Bold-7";
			pages = textToPrint.split(page_break);
			linesPerPage = new int[pages.length];
			int noOfLines =0;
			for(int i =0; i< pages.length; i++){
				linesPerPage[i] = noOfLines;
				noOfLines += pages[i].split(line_break ).length;
			}
			
			// Find a factory that can do the conversion			
			PrintService service=null;
			PrinterUtil printerUtil = new PrinterUtil();
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			if( service == null ) {
				throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
			}
			// Create the print job
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
			
			Paper paper = new Paper();
		    double margin = 0;
		    
		    paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight()
		        - margin * 2);
		    pf.setPaper(paper);
		    
			pf.setOrientation(PageFormat.LANDSCAPE);
			job.setPrintable(this, pf);
			job.setPrintService(service);			
			
			// Print it
			job.print();
		} catch (Exception e2) {			
			throw e2;
		}
	}	


	/**
	 * This method accepts the string that needs to be printed 
	 * and sets that string to the text that will be printed
	 * from the paint method
	 * @param textToPrint
	 */
	public boolean printText(String textToPrint, String printerType) throws Exception{
		boolean printed = false;
		File file = new File("voucher_report_prn");
		InputStream inputstream = null;
		this.textToPrint = textToPrint;
		try {
			
			/** delete the temporary file used for printing, if exists */
			if(file.exists()){
				file.delete();
			}

			/** Create file if it does not exist*/
			boolean success = file.createNewFile();

			if (success) {
				if (file.canWrite()) {
					BufferedWriter wri = new BufferedWriter(new FileWriter(file, true));
					wri.write(textToPrint);
					wri.close();
				}
			}

			inputstream = new BufferedInputStream(new FileInputStream(file));
			
			/** Find a factory that can do the conversion*/
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			PrintService service = null;
			PrinterUtil printerUtil = new PrinterUtil();
			logger.info("Selected Printer Name: " + SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			if( !SDSPreferenceStore.getStringStoreValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE).equals(printerType) ) {
				service = printerUtil.getPrintService(printerType);
			} else {
//			    service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
				service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE));
			}

			if( service == null ) {
				ProgressIndicatorUtil.closeInProgressWindow();
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
				return true;
			}
			//IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY
			if( printerType.contains(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREF_TKT_PRINT_TYPE3)) ) {

				/** Create the print job*/
				DocPrintJob job = service.createPrintJob();
				Doc doc = new SimpleDoc(inputstream, flavor, null);
				
				/** Print it **/
				job.print(doc, null);
			} else {
				/** Create the print job*/
				PrinterJob job = PrinterJob.getPrinterJob();
				PageFormat pf = job.defaultPage();
				pf.setOrientation(PageFormat.LANDSCAPE);
				job.setPrintable(this, pf);
				job.setPrintService(service);			
				
				/** Print it **/
//				job.print();

			}
			
			printed = true;
			ProgressIndicatorUtil.closeInProgressWindow();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_SUCCESS));
			//file.delete();
		} catch (Exception e2) {
			VoucherUIExceptionHandler.handleException(e2);
			logger.error("Exception while printing the session details",e2);
			//return;
		} finally {
			if( inputstream != null ) {
				try {
					inputstream.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		}
		return printed;
	}	

	
	public void printTicket(ByteArrayOutputStream outputStream, PrintRequestAttributeSet attrSet) {
		try {

//			File file = new File("voucher_prn");
			//byte [] buffer=);
			// Create file if it does not exist
			ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
//			boolean success = file.createNewFile();

			/*if (success) {
				if (file.canWrite()) {
					BufferedWriter wri = new BufferedWriter(new FileWriter(
							file, true));
					wri.write();
					wri.close();
				}
			}*/

			//InputStream is = new BufferedInputStream(new FileInputStream(file));

			//OutputStream out = outputStream;
			/*	ByteArrayOutputStream  b=new ByteArrayOutputStream();
			b.write(outputStream);
			b.close();*/

//			Find a factory that can do the conversion
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

			PrintService service=null;				
			PrinterUtil printerUtil = new PrinterUtil();			
			//System.out.println("Selected Printer Name: "+SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			//service = printerUtil.getPrintService("\\\\TEST-SDS\\Ithaca Generic / Text Only");
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			//service = null;
			if(service==null) {
				throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
			}
			//service.		

			// Create the print job
			DocPrintJob job = service.createPrintJob();
			Doc doc = new SimpleDoc(arrayInputStream, flavor, null);

			// Print it
			job.print(doc, attrSet);
			job.getAttributes();

			/*is.close();
			file.delete();
			 */
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}
	
	public boolean printSessionDetails(String ps) {
		boolean printed = false;
		File file = new File("slip_prn");
		InputStream is = null;
		try {
			/** delete the temporary file used for printing, if exists */
			//new File("slip_prn").delete();			
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

			is = new BufferedInputStream(new FileInputStream(file));

			/** Find a factory that can do the conversion*/
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;		
			PrintService service=null;				
			PrinterUtil printerUtil = new PrinterUtil();			
			logger.info("Selected Printer Name: "+SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			if(service==null) {
				ProgressIndicatorUtil.closeInProgressWindow();
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
				return true;
			}
			/** Create the print job*/
			DocPrintJob job = service.createPrintJob();
			Doc doc = new SimpleDoc(is, flavor, null);
			/** Print it*/
			job.print(doc, null);			
			printed = true;
			ProgressIndicatorUtil.closeInProgressWindow();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_PRINT_SUCCESS));
			//file.delete();
		} catch (Exception e2) {
			VoucherUIExceptionHandler.handleException(e2);
			logger.error("Exception while printing the session details",e2);
			//return;
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		}
		return printed;
	}

	public static void main(String[] args) {
		VoucherPrintReports printingService = new VoucherPrintReports();
		StringBuffer buffer = new StringBuffer();
		buffer.append("Batch # 	1	Voucher Count	2	Voucher Amount ($)	24.00");
		buffer.append("Employee	10006	Coupon Count	0	Coupon Amount  ($)	0.00");
		buffer.append("\r\n");
		buffer.append("\r\n");
		buffer.append("Location	Barcode			  Type							Amount ($)		Reason");
		buffer.append("Amount Type: CASHABLE");
		buffer.append("\r\n");
		buffer.append("\r\n");
		buffer.append("C02000	700190006000039522	Printed at a Non-slot Location		12.00		Wrong Status");
		buffer.append("C02000	700190006000039522	Printed at a Non-slot Location		12.00		Wrong Status");
		buffer.append("Amount Type: NON_CASHABLE_PROMO");

		String inputValue = buffer.toString();
		try {

			printingService.printText(inputValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
