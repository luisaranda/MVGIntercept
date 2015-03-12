package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * Printer Details Response Object for Jackpot process through Keypad.
 * 
 * @author ranjithkumarm
 *
 */
public class PrinterDetailsObject implements ICustomResponse,Serializable{

	
	private static final long serialVersionUID = -6846744646387310212L;	
	
	private boolean errorPresent=false;
	
	private String errorText=null;
	
	private int printerLocationCount=0;
	
	private int[] printerLocations=null;

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}	

	public int getPrinterLocationCount() {
		return printerLocationCount;
	}

	public void setPrinterLocationCount(int printerLocationCount) {
		this.printerLocationCount = printerLocationCount;
	}

	public int[] getPrinterLocations() {
		return printerLocations;
	}

	public void setPrinterLocations(int[] printerLocations) {
		this.printerLocations = printerLocations;
	}

	public boolean isErrorPresent() {
		return errorPresent;
	}

	public void setErrorPresent(boolean errorPresent) {
		this.errorPresent = errorPresent;
	} 

}
