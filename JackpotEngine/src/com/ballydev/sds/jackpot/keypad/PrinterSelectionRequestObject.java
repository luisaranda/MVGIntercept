package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * Object Class representing the printer select request from GMU.
 * 
 * @author ranjithkumarm
 *
 */
public class PrinterSelectionRequestObject implements ICustomRequest,Serializable{

	
	private static final long serialVersionUID = 3991394454964370715L;
	
	private int printerSelected=-1;//default no printer selected.
	
	private String printerNameSelected=null;

	public String getPrinterNameSelected() {
		return printerNameSelected;
	}

	public void setPrinterNameSelected(String printerNameSelected) {
		this.printerNameSelected = printerNameSelected;
	}

	public int getPrinterSelected() {
		return printerSelected;
	}

	public void setPrinterSelected(int printerSelected) {
		this.printerSelected = printerSelected;
	}
	
	

}
