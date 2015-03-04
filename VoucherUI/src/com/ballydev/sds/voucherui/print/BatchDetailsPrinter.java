/*****************************************************************************
 * $Id: BatchDetailsPrinter.java,v 1.4, 2010-04-08 13:13:46Z, Lokesh, Krishna Sinha$
 * $Date: 4/8/2010 8:13:46 AM$
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

package com.ballydev.sds.voucherui.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.StringTokenizer;

import javax.print.PrintService;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

public class BatchDetailsPrinter implements Printable {	
	/**
	 * Default page height
	 */
	private int iPageHeight = 25;

	/**
	 * Batch row index
	 */
	private int iBatchRow = 0;

	/**
	 * String that needs to be set in the footer
	 */
	private String footerString = "";

	/**
	 * String that needs to be set in the footer
	 */
	private String headerString = "";
	
	/**
	 * String that has the text to print
	 */
	private String textToPrint = "";
	
	public void print(String ps) throws Exception {
		try {
			this.textToPrint = ps;
			StringTokenizer tokenizer = new StringTokenizer(ps, "\n");
			iBatchRow = tokenizer.countTokens();

			PrintService service=null;
			PrinterUtil printerUtil = new PrinterUtil();
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			if(service==null) {
				throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
			}
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
			pf.setOrientation(PageFormat.LANDSCAPE);
			job.setPrintable(this, pf);
			job.setPrintService(service);
			
			job.print();
		} catch (Exception e2) {
			throw e2;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		
		try {
			graphics.setColor(Color.black);
			graphics.setFont(Font.decode("courier-BOLD-8"));
			int iCurrentCount = 0;
			int iLeftMargin = 76;
			int iTopMargin = 85;
			int iPrintRow = pageIndex * iPageHeight;
			int lastPage = (iBatchRow) / iPageHeight;
			if (iBatchRow > (lastPage * iPageHeight)) {
				lastPage++;
			}
			
			if (pageIndex >= lastPage) 
				return Printable.NO_SUCH_PAGE;

			String pageNoHeader = LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_PAGE_TEXT, new Object[]{(pageIndex + 1), lastPage});
			graphics.drawString(pageNoHeader, 630, 100);
			
			int numOfRowsToPrint = iPageHeight;
			if ((lastPage - pageIndex) == 1) {
				numOfRowsToPrint = iBatchRow - ((lastPage - 1) * iPageHeight);
			}
			
			String[] header = headerString.split("\n");
			for(int i = 0; i < header.length; i++){
				try {
					graphics.drawString(header[i], iLeftMargin, iTopMargin + (11 * iCurrentCount));
				} catch (Exception e) {
					e.printStackTrace();
				}
				iCurrentCount++;
			}
			
			String[] strToken = textToPrint.split("\n");
			for(int i = 0; i < numOfRowsToPrint; i++){
				try {
					graphics.drawString(strToken[iPrintRow], iLeftMargin, iTopMargin + (11 * iCurrentCount));
				} catch (Exception e) {
					e.printStackTrace();
				}
				iPrintRow++;
				iCurrentCount++;
			}
			
			graphics.drawString(footerString, 578, 540);
		} catch (Exception e) {			
			e.printStackTrace();		
		}
		return Printable.PAGE_EXISTS;
	}

	/**
	 * @return the headerString
	 */
	public String getHeaderString() {
		return headerString;
	}

	/**
	 * @param headerString the headerString to set
	 */
	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}

	/**
	 * @return the footerString
	 */
	public String getFooterString() {
		return footerString;
	}

	/**
	 * @param footerString the footerString to set
	 */
	public void setFooterString(String footerString) {
		this.footerString = footerString;
	}
}