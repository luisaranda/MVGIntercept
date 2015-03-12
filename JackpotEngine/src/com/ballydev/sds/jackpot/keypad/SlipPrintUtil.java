package com.ballydev.sds.jackpot.keypad;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;




/**
 * Slip Printing Util.
 * 
 * @author ranjithkumarm
 *
 */
public class SlipPrintUtil {

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * default constructor
	 */
	public SlipPrintUtil() {
	}

	/**
	 * method to send the text to printer
	 * @param ps
	 */
	public void printSlip(String ps, String printerName) throws Exception{
		InputStream is=null;
		try {		
			if(ps!=null && !((ps.trim()+" ").equalsIgnoreCase(" ")) &&
					printerName!=null && !((printerName.trim()+" ").equalsIgnoreCase(" ")) ){
				File file = new File("slip_prn");
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
				PrinterManager printerManager=new PrinterManager();	
				service=printerManager.getPrintServiceFromName(printerName);				
				/** Create the print job*/
				if(service!=null){
					DocPrintJob job = service.createPrintJob();
					Doc doc = new SimpleDoc(is, flavor, null);
					/** Print it*/
					job.print(doc, null);					
				}				
				log.info("Slip Printed Successfully...");
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}finally{
			if(is!=null){
				is.close();
			}
		}
	}


}
