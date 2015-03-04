/*****************************************************************************
 * $Id: CashierSessionPrintReports.java,v 1.1.1.0, 2013-08-26 14:04:05Z, Iraj, Satish$
 * $Date: 8/26/2013 9:04:05 AM$
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
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;

/**
 * This class is used to print Cashier session Detail/Summary Reports in Normal and Unicode format.
 * @author VNitinkumar
 *
 */
public class CashierSessionPrintReports implements Printable {

	/**
	 * Default page height
	 */
	private int iPageHeight = 50;

	/**
	 * Cashier session row index
	 */
	private int iCashierSessRow = 0;

	/**
	 * String that needs to be set in the footer
	 */
	private String footerString = "";
	
	/**
	 * String that has the text to print
	 */
	private String textToPrint = "";

	
	public void print(String ps) throws Exception {
		
		try {
	
			this.textToPrint = ps;

			StringTokenizer tokenizer = new StringTokenizer(ps, "\n");
			iCashierSessRow = tokenizer.countTokens();

			PrintService service=null;
			PrinterUtil printerUtil = new PrinterUtil();
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			
			if( service == null ) {
				throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
			}
			
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
			job.setPrintable(this, pf);
			job.setPrintService(service);
			
			job.print();
		} catch (Exception e2) {
			e2.printStackTrace();
			throw e2;
		} finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
	}

	
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		
		try {
			graphics.setColor(Color.black);
			graphics.setFont(Font.decode("courier-Bold-8"));
			int iCurrentCount = 0;
			int iLeftMargin = 85;
			int iTopMargin = 85;
			int iPrintRow = pageIndex * iPageHeight;
			int lastPage = (iCashierSessRow) / iPageHeight;
			if( iCashierSessRow > (lastPage * iPageHeight) ) {
				lastPage++;
			}
			
			if (pageIndex >= lastPage) 
				return Printable.NO_SUCH_PAGE;
			
			int numOfRowsToPrint = iPageHeight;
			if( (lastPage - pageIndex) == 1 ) {
				numOfRowsToPrint = iCashierSessRow - ((lastPage - 1) * iPageHeight);
			}

			String[] strToken = textToPrint.split("\n");
			for( int i = 0; i < numOfRowsToPrint; i++ ) {
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
		finally {
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return Printable.PAGE_EXISTS;
	}
}
