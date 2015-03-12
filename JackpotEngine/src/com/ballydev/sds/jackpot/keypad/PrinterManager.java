package com.ballydev.sds.jackpot.keypad;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;



/**
 * Utility to manage/access printers available.
 * 
 * @author ranjithkumarm
 *
 */
public class PrinterManager {

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);

	private PrintService[] printServiceAvailable=null;


	public PrinterManager(){
		PrintService printer1, printer2 = null;
		try{
			printServiceAvailable=PrintServiceLookup.lookupPrintServices(null, null);		
	// Code added by Viswa Kolli to Sort the printer list. This is as per Seminoles Requirement.        
	        for (int i= 0;i<printServiceAvailable.length;i++){
	        	printer1 = printServiceAvailable[i];        	
	        	for(int j=i+1;j<printServiceAvailable.length;j++){        		
	        		printer2 = printServiceAvailable[j];
	        		if((int)printer1.getName().charAt(0)>(int)printer2.getName().charAt(0)){
	        			printServiceAvailable[i]=printer2;
	        			printServiceAvailable[j]=printer1;
	        			printer1=printServiceAvailable[i];	        	        
	        		}	        			
	        	}
	        }
		}catch (Exception e) {
			log.error("Error in PrinterManager ",e);
		}
	}


	public PrintService[] getPrinters(){		
		return printServiceAvailable;
	}

	public String getPrinterNameForIndex(int index){
		String printerNameToSend=null;
		if(index >0 && printServiceAvailable!=null && printServiceAvailable.length!=0 && 
				index <= printServiceAvailable.length){			
			printerNameToSend=printServiceAvailable[(index-1)].getName();	
		}
		return printerNameToSend;
	}

	/**
	 * Note: Start index position from 1 and not 0.
	 * @return
	 */
	public int[] getAllAvailablePrinterIndex(){
		int[] printerIndexes=null;
		try{
			if(printServiceAvailable!=null && printServiceAvailable.length!=0){
				printerIndexes=new int[((printServiceAvailable.length)+1)];
				for(int index=0;index<printServiceAvailable.length;index++){
					log.info("Printer :"+(index+1)+" Name :"+printServiceAvailable[index].getName());
					printerIndexes[(index+1)]=(index+1);
				}
			}			
		}catch (Exception e) {
			log.error("Exception in getAllAvailablePrinterIndex method of PrinterManager ",e);
		}
		return printerIndexes;
	}


	public PrintService getPrintServiceFromName(String printerName){
		PrintService printServiceToSend=null;
		if(printerName!=null && !((printerName.trim()+" ").equalsIgnoreCase(" ")) 
				&& printServiceAvailable!=null && printServiceAvailable.length!=0){
			for(int i=0;i<printServiceAvailable.length;i++){
				if(printServiceAvailable[i].getName().equalsIgnoreCase(printerName)){
					printServiceToSend=printServiceAvailable[i];
					break;
				}
			}
		}
		return printServiceToSend;
	}

	public int getPrinterCount(){
		int printerCountToSend=0;
		if(printServiceAvailable!=null && printServiceAvailable.length!=0){
			printerCountToSend=printServiceAvailable.length;
		}
		return printerCountToSend;
	}	


	/*public static void main(String[] args) {
		PrinterManager printerManager=new PrinterManager();
		PrintService[] ss= printerManager.getPrinters();
		for(int i=0;i<ss.length;i++){
			System.out.println(ss[i].getName());
		}
	}*/
	
//	public static void main(String[] args) {
//		new PrinterManager().getAllAvailablePrinterIndex();
//	}


}
