package com.ballydev.sds.voucherui.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintRequestAttributeSet;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;


/**
 * This class acts as a utility for printing
 * the plain text
 * @author Nithya kalyani R
 * @version $Revision: 11$ 
 */
public class VoucherPrintingService implements Printable{
	/**
	 * Default page height
	 */
	private int iPageHeight = 36;	

	/**
	 * Batch row index
	 */
	private int iBatchRow = 0;

	/**
	 * String that needs to be set in the footer
	 */
	private String strFooter = "";

	/**
	 * String that has the text to print
	 */
	private String textToPrint = "";

	public VoucherPrintingService() {
	}

	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

		int headerRow = 85;
		try {
			graphics.setColor(Color.black);
			graphics.setFont(Font.decode("courier-BOLD-9"));
			int iCurrentCount = 0;
			int iLeftMargin = 76;
			int iPrintRow = pageIndex * iPageHeight;
			int lastPage = (iBatchRow) / iPageHeight;
			if (iBatchRow > (lastPage * iPageHeight)) {
				lastPage++;
			}
			
			if (pageIndex >= lastPage) 
				return Printable.NO_SUCH_PAGE;

			
			graphics.drawString(LabelLoader.getLabelValue(IDBLabelKeyConstants.PRINT_PAGE_TEXT, new Object[] {(pageIndex + 1),lastPage}), 630, 100);
			//graphics.drawString("Page " + (pageIndex + 1) + " of " + lastPage, 630, 90);		

			int numOfRowsToPrint = iPageHeight;
			if ((lastPage - pageIndex) == 1) {
				numOfRowsToPrint = iBatchRow - ((lastPage - 1) * iPageHeight);
			}
			String[] strToken = textToPrint.split("\r\n");
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


	/**
	 * This method accepts the string that needs to be printed 
	 * and sets that string to the text that will be printed
	 * from the paint method
	 * @param textToPrint
	 */
	public void printText(String textToPrint) throws Exception{	

		try {
			this.textToPrint = textToPrint;
			String[] tokenizer = textToPrint.split("\r\n");
			iBatchRow = tokenizer.length;

			// Find a factory that can do the conversion			
			PrintService service=null;				
			PrinterUtil printerUtil = new PrinterUtil();
			service = printerUtil.getPrintService(SDSPreferenceStore.getStringStoreValue(IVoucherConstants.VOUCHER_PRINTER_PREFERENCE_KEY));
			if(service==null) {
				throw new Exception(LabelLoader.getLabelValue(IDBLabelKeyConstants.NO_PRINTER_CONNECTED));
			}
			// Create the print job			
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf = job.defaultPage();
			pf.setOrientation(PageFormat.LANDSCAPE);
			job.setPrintable(this, pf);
			job.setPrintService(service);			
			
			// Print it
			job.print();
		} catch (Exception e2) {			
			throw e2;
		}
	
	

	}	

	public void printTicket(ByteArrayOutputStream outputStream, PrintRequestAttributeSet attrSet) {
		try {

			File file = new File("voucher_prn");
			//byte [] buffer=);
			// Create file if it does not exist
			ByteArrayInputStream arrayInputStream=new ByteArrayInputStream(outputStream.toByteArray());
			@SuppressWarnings("unused")
			boolean success = file.createNewFile();

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

	public static void main(String[] args) {
		VoucherPrintingService printingService = new VoucherPrintingService();
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
