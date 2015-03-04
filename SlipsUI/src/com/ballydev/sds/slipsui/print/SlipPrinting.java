package com.ballydev.sds.slipsui.print;

/**
 * This class is used to Print Slips.
 * <p>
 * <b>REFERENCE SECTION</b> <br />
 * None.
 * </p>
 *
 * @author Kumar Vittoba email:KVittoba@ballytech.com
 * @version $Revision: 2$
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

// import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;

import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.print.PrinterUtil;
import com.ballydev.sds.slipsui.print.SlipPrinting;

public class SlipPrinting {
	public SlipPrinting() {
	}

	public void printSlip(String ps) {
		try {
			// delete the temporary file used for printing, if exists
			//new File("slip_prn").delete();

			File file = new File("slip_prn");

			if(file.exists()) {
				file.delete();
			}
			// Create file if it does not exist
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

			// Find a factory that can do the conversion
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			/*PrintService service = PrintServiceLookup
					.lookupDefaultPrintService();
*/
			PrintService service=null;
			
			PrinterUtil printerUtil = new PrinterUtil();
			//String servi = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);
			//System.out.println(servi);
			String servi = SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER);//"Epson Generic / Text Only";
			service = printerUtil.getPrintService(servi);
			// Create the print job
			DocPrintJob job = service.createPrintJob();
			Doc doc = new SimpleDoc(is, flavor, null);

			// Print it
			job.print(doc, null);

			is.close();
			file.delete();

		} catch (Exception e2) {
			e2.printStackTrace();
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
