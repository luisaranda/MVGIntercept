/*****************************************************************************
 * $Id: PrintWithGraphics.java,v 1.13.2.5, 2013-10-15 13:23:05Z, SDS12.3.3 Checkin User$
 * $Date: 10/15/2013 8:23:05 AM$
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.StringTokenizer;

import javax.print.PrintService;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.utils.common.props.PropertyKeyConstant;

/**
 * Class to creater the barcode and print the slip
 * @author dambereen
 * @version $Revision: 20$ 
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
	 * Barcode Image
	 */
	private Image barcodeImgToPrint = null;

	/**
	 * JPG File that contains the Barcode image
	 */
	private File barcodeFile = null;

	private boolean setVoidRptPrintFlag = false;

	private boolean isCheckPrintEnabled = false;
	
	private String numericPattern = "[0-9]+";

	private static int PORTRAIT_PAGE_WIDTH_MIN = 77;

	private static int PORTRAIT_PAGE_WIDTH_MIN_DOUBLE = 130;

	private static int PORTRAIT_PAGE_WIDTH_MIN_TRIPLE = 180;
	

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * PrintWithGraphics constructor
	 */
	public PrintWithGraphics(){
		String slipPageWidth = SessionUtility.getServerProperty(PropertyKeyConstant.PROPS_JP_SLIP_PAGE_WIDTH);
		if(slipPageWidth != null && slipPageWidth.matches(numericPattern)) {
			PORTRAIT_PAGE_WIDTH_MIN = Integer.parseInt(slipPageWidth.trim());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */

	public int print (Graphics graphicsIn, PageFormat pageFormat, int pageIndex)throws PrinterException{

		if (log.isInfoEnabled()) {
			log.info("Calling graphics print() method");
		}
		try {
			Graphics2D graphics = (Graphics2D)graphicsIn;
			graphics.setColor(Color.black);
			int iCurrentCount = 0;
			int iLeftMargin = 76;
			
			String slipPageHeight = SessionUtility.getServerProperty(PropertyKeyConstant.PROPS_JP_SLIP_PAGE_HEIGHT);
			if(slipPageHeight != null && slipPageHeight.matches(numericPattern)) {
				iPageHeight = Integer.parseInt(slipPageHeight.trim());
			} else {
				iPageHeight = 90;
			}
			
			if(setVoidRptPrintFlag) {
				iPageHeight = 65;
			}
			int iPrintRow = pageIndex * iPageHeight;
			
			int lastPage = (iBatchRow) / iPageHeight;
			boolean isLastPage = false;
			if (iBatchRow > (lastPage * iPageHeight)) {
				lastPage++;
			}

			if (pageIndex >= lastPage) {
				if (log.isDebugEnabled()) {
					log.debug("LAST PAGE IS REACHED");
				}
				return Printable.NO_SUCH_PAGE;
			}

			int numOfRowsToPrint = iPageHeight;
			if ((lastPage - pageIndex) == 1) {
				numOfRowsToPrint = iBatchRow - ((lastPage - 1) * iPageHeight);
				isLastPage = true;
			}

			String[] strToken = textToPrint.split("\r\n");
			if (log.isDebugEnabled()) {
				log.debug("pageIndex: " + pageIndex);
				log.debug("iPrintRow: " + iPrintRow);
				log.debug("iBatchRow: " + iBatchRow);
				log.debug("lastPage: " + lastPage);
				log.debug("numOfRowsToPrint: " + numOfRowsToPrint);
			}
			if (isCheckPrintEnabled && !setVoidRptPrintFlag) {
				// PRINTING SLIP FOR SMART CARD
				int headerRow = 80;
				if (log.isDebugEnabled()) {
					log.debug("iPageHeight: " + iPageHeight);
				}

				for (int i = 0; i < (numOfRowsToPrint); i++) {
					String rowDataString = strToken[iPrintRow];
					// pageFormat.getOrientation()==1 FOR PORTRAIT
					// IF CONDITION CHKS TO WRAP THE DATA UPTO FOUR LINES
					// 15 IS THE MULTIPLE USED FOR THE SPACING BETWEEN THE ROWS FOR CHECK(SMART CARD) SLIP
					if (rowDataString.length() > PORTRAIT_PAGE_WIDTH_MIN && pageFormat.getOrientation() == 1) {
						String str1 = rowDataString.substring(0, PORTRAIT_PAGE_WIDTH_MIN);
						String strInBold = str1.substring(0, str1.indexOf(':'));
						graphics.setFont(Font.decode(IAppConstants.SLIP_PRINT_FONT_BOLD));
						graphics.drawString(strInBold, iLeftMargin, headerRow + (15 * iCurrentCount));
						graphics.setFont(Font.decode(IAppConstants.SLIP_PRINT_FONT_PLAIN));
						String stringInPlain = str1.substring(str1.indexOf(':'));
						graphics.drawString(stringInPlain, iLeftMargin + 150, headerRow
								+ (15 * iCurrentCount));
						iCurrentCount++;
						if (rowDataString.length() > PORTRAIT_PAGE_WIDTH_MIN_DOUBLE) {
							String str2 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN,
									PORTRAIT_PAGE_WIDTH_MIN_DOUBLE);
							graphics.drawString(str2, iLeftMargin, headerRow + (15 * iCurrentCount));
							iCurrentCount++;
							if (rowDataString.length() > PORTRAIT_PAGE_WIDTH_MIN_TRIPLE) {
								String str3 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN_DOUBLE,
										PORTRAIT_PAGE_WIDTH_MIN_TRIPLE);
								graphics.drawString(str3, iLeftMargin, headerRow + (15 * iCurrentCount));
								iCurrentCount++;
								String str4 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN_TRIPLE);
								graphics.drawString(str4, iLeftMargin, headerRow + (15 * iCurrentCount));
							} else {
								String str3 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN_DOUBLE);
								graphics.drawString(str3, iLeftMargin, headerRow + (15 * iCurrentCount));
							}

						} else {
							// ELSE CONDITION WHEN THE DATA DOES NOT NEED TO
							// BE WRAPPED AND IF IT A LANDSCAPE PAGE ORIENTATION
							String str2 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN);
							graphics.drawString(str2, iLeftMargin, headerRow + (15 * iCurrentCount));
						}

					} else {
						if (rowDataString.indexOf(':') > 0) {
							String strInBold = rowDataString.substring(0, rowDataString.indexOf(':'));
							graphics.setFont(Font.decode(IAppConstants.SLIP_PRINT_FONT_BOLD));
							graphics.drawString(strInBold, iLeftMargin, headerRow + (15 * iCurrentCount));
							graphics.setFont(Font.decode(IAppConstants.SLIP_PRINT_FONT_PLAIN));
							String stringInPlain = rowDataString.substring(rowDataString.indexOf(':'));
							graphics.drawString(stringInPlain, iLeftMargin + 150, headerRow
									+ (15 * iCurrentCount));
						} else if (rowDataString.equalsIgnoreCase(LabelLoader
								.getLabelValue(LabelKeyConstants.CHECK_PRINT_JACKPOT_VALID_DATE_STATIC))
								|| (rowDataString.length() > 24 && rowDataString
										.substring(0, 25).equalsIgnoreCase(
												LabelLoader.getLabelValue(LabelKeyConstants.CHECK_PRINT_JACKPOT_VALID_DATE)))) {
							graphics.setFont(Font.decode(IAppConstants.SLIP_PRINT_FONT_BOLD));
							graphics.drawString(rowDataString, iLeftMargin, headerRow + (15 * iCurrentCount));
						} else {
							graphics.setFont(Font.decode(IAppConstants.SLIP_PRINT_FONT_PLAIN));
							graphics.drawString(rowDataString, iLeftMargin, headerRow + (15 * iCurrentCount));
						}
					}
					iPrintRow++;
					iCurrentCount++;
				}
				if (log.isDebugEnabled()) {
					log.debug("iCurrentCount 1: " + iCurrentCount);
				}
				iCurrentCount = headerRow + (15 * iCurrentCount);
				if (log.isDebugEnabled()) {
					log.debug("iCurrentCount 2: " + iCurrentCount);
				}
				graphics.drawImage(barcodeImgToPrint, iLeftMargin, iCurrentCount, 200, 75, null);
				if (barcodeFile != null && barcodeFile.exists()) {
					barcodeFile.delete();
				}
			} else {
				// PRINTING SLIP FOR LEGACY JACKPOT SLIPS
				int headerRow = 88; 
				String fontType = "courier-BOLD-10";

				String slipHeaderRow = SessionUtility.getServerProperty(PropertyKeyConstant.PROPS_JP_SLIP_HEADER_ROW);
				if(slipHeaderRow != null && slipHeaderRow.matches(numericPattern)) {
					headerRow = Integer.parseInt(slipHeaderRow.trim());
				}
				
				String slipFontType = SessionUtility.getServerProperty(PropertyKeyConstant.PROPS_JP_SLIP_FONT_TYPE);
				if(slipFontType != null) {
					fontType = slipFontType.trim();
				}
				graphics.setFont(Font.decode(fontType));
				
				if (setVoidRptPrintFlag) {
					// SETTING THE PAGE HEIGHT TO 40 WHEN IT IS A PRINT FOR A VOID REPORT
					// AS THIS REPORT SHOULD BE PRINTED IN LANDSCAPE MODE
					iPageHeight = 40;
				}
				if (log.isDebugEnabled()) {
					log.debug("iPageHeight: " + iPageHeight);
				}
					for (int i = 0; i < (numOfRowsToPrint); i++) {
						if(iPrintRow >= strToken.length){
							break;
						}
						//index = iPrintRow + xtraRowCount;
						String rowDataString = strToken[iPrintRow];
						// pageFormat.getOrientation()==1 FOR PORTRAIT
						// IF CONDITION CHKS TO WRAP THE DATA UPTO FOUR LINES
						// 11 IS THE MULTIPLE USED FOR THE SPACING BETWEEN THE ROWS
						int rowspacing = 7;
						String slipRowSpacing = SessionUtility.getServerProperty(PropertyKeyConstant.PROPS_JP_SLIP_MULTIPLE_FIELD_COUNT);
						if(slipRowSpacing != null && slipRowSpacing.matches(numericPattern)) {
							rowspacing = Integer.parseInt(slipRowSpacing.trim());
						}
						
						if (rowDataString.length() > PORTRAIT_PAGE_WIDTH_MIN && pageFormat.getOrientation() == 1) {
							String str1 = rowDataString.substring(0, PORTRAIT_PAGE_WIDTH_MIN);
							graphics.drawString(str1, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
							iCurrentCount++;
							if (rowDataString.length() > PORTRAIT_PAGE_WIDTH_MIN_DOUBLE) {
								String str2 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN,
										PORTRAIT_PAGE_WIDTH_MIN_DOUBLE);
								graphics.drawString(str2, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
								iCurrentCount++;
								if (rowDataString.length() > PORTRAIT_PAGE_WIDTH_MIN_TRIPLE) {
									String str3 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN_DOUBLE,
											PORTRAIT_PAGE_WIDTH_MIN_TRIPLE);
									graphics.drawString(str3, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
									iCurrentCount++;
									String str4 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN_TRIPLE);
									graphics.drawString(str4, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
								} else {
									String str3 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN_DOUBLE);
									graphics.drawString(str3, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
								}

							} else {
								// ELSE CONDITION WHEN THE DATA DOES NOT NEED TO
								// BE WRAPPED AND IF IT A LANDSCAPE PAGE ORIENTATION
								String str2 = rowDataString.substring(PORTRAIT_PAGE_WIDTH_MIN);
								graphics.drawString(str2, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
							}
						} else {
							graphics.drawString(rowDataString, iLeftMargin, headerRow + (rowspacing * iCurrentCount));
						}
						iPrintRow++;
						iCurrentCount++;
					}
					if (log.isDebugEnabled()) {
						log.debug("iCurrentCount 1: " + iCurrentCount);
					}
					iCurrentCount = headerRow + (11 * iCurrentCount);
					if (log.isDebugEnabled()) {
						log.debug("iCurrentCount 2: " + iCurrentCount);
					}
					if (isLastPage) {
						graphics.drawImage(barcodeImgToPrint, iLeftMargin, iCurrentCount, 200, 75, null);
					}
					if (barcodeFile != null && barcodeFile.exists()) {
						barcodeFile.delete();
					}
					
			}
		} catch (Exception e) {
			log.error("Exception While printing the jackpot slip -->", e);
		}
		return Printable.PAGE_EXISTS;
	}
	/**
	 * Method to print the slip text and barcode and to invoke the Printable Interface's print method 
	 * @param textToPrint
	 * @param image
	 * @throws Exception
	 */
	public void printTextImage(String textToPrint, Image image, String slipPrintType) throws Exception{	

		try {
			setVoidRptPrintFlag = false;
			if (slipPrintType != null && slipPrintType.equals(IAppConstants.CHECK_PRINT_SLIP_TYPE)) {
				isCheckPrintEnabled = true;
			}
			this.textToPrint = textToPrint;
			this.barcodeImgToPrint = image;
			StringTokenizer tokenizer = new StringTokenizer(textToPrint, "\r\n");
			iBatchRow = tokenizer.countTokens();
			//Biloxi  Slip Format 
			if(!isCheckPrintEnabled && !setVoidRptPrintFlag) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < (iBatchRow); i++) {
					int start = 0;
					int end = PORTRAIT_PAGE_WIDTH_MIN;
					String str = tokenizer.nextToken();
					if(str.length() < PORTRAIT_PAGE_WIDTH_MIN){
						sb.append(str);
						sb.append("\r\n");
					}else  {
						int len = str.length();
						while (len > PORTRAIT_PAGE_WIDTH_MIN) {
							String dummy = str.substring(start, end);
							start = end;
							end += PORTRAIT_PAGE_WIDTH_MIN;
							sb.append(dummy); 
							sb.append("\r\n");
							len =str.substring(start, str.length()).length();
						}
						sb.append(str.substring(start, str.length()));
						sb.append("\r\n");
					}
				}
				iBatchRow = sb.toString().split("\r\n").length;
				this.textToPrint = sb.toString();
			}
			// Find a factory that can do the conversion
			PrintService service = null;
			PrinterUtil printerUtil = new PrinterUtil();

			service = printerUtil.getPrintService(SDSPreferenceStore
					.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY));

			// Create the print job
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
			job.setPrintable(this, pf);
			job.setPrintService(service);
			// Print it
			job.print();
		} catch (Exception e2) {
			e2.printStackTrace();
			log.error("excep: "+e2);
			throw e2;
		}
	}	

	/**
	 * Method to print the report and to invoke the Printable Interface's print method  
	 * and prints using LANDSCAPE paper orientation mode
	 * @param textToPrint
	 * @param image
	 * @throws Exception
	 */
	public void printReport(String textToPrint) throws Exception{	

		try {
			setVoidRptPrintFlag = true;
			this.textToPrint = textToPrint;
			StringTokenizer tokenizer = new StringTokenizer(textToPrint, "\n");
			iBatchRow = tokenizer.countTokens();

			// Find a factory that can do the conversion			
			PrintService service=null;				
			PrinterUtil printerUtil = new PrinterUtil();

			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY));

			// Create the print job			
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();

			pf.setOrientation(PageFormat.LANDSCAPE);
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
		//		PrintWithGraphics printWithGraphics = new PrintWithGraphics();
		//		try {			
		//			StringBuilder stringBuilder = new StringBuilder();
		//			for(int i=0; i<90; i++) {
		//				stringBuilder.append("Four score and seven years ago our fathers brought forth on this : "+i);
		//				stringBuilder.append("\r\n");	
		//			}
		//						
		//			//Image image = printWithGraphics.createBarcode();			
		//			printWithGraphics.printTextImage(stringBuilder.toString(), null, null);
		//			printWithGraphics.printReport(stringBuilder.toString());
		//			printWithGraphics.printTextImage(stringBuilder.toString(), null, null);
		//			
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}


		//		System.out.println(IAppConstants.JP_CHECK_LINE_ONE_DYNAMIC + ":" + IAppConstants.JP_CHECK_LINE_ONE_DYNAMIC.length());
		//		
		//		String rowDataString = "JACKPOT SLIP (VALID TILL 10-Nov-2010)";
		//		System.out.println(rowDataString.substring(0, 25)+"|");
		//		System.out.println(rowDataString.substring(0, 25)
		//		.equalsIgnoreCase(IAppConstants.JP_CHECK_LINE_ONE_DYNAMIC));
	}
}
