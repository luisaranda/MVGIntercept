package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * This Class contains the fields and getters/setters of the session maintained
 * data for the keypad process session.
 * 
 * @author ranjithkumarm
 *
 */
public class SessionObject implements Serializable{
	
	private static final long serialVersionUID = 2175569360273314654L;
	
	private String assetNumber=null;
	
	private int currentStatus=0;
	
	private InitiationRequestObject initiationRequestObject=null;
	
	private InitiationResponseObject initiationResponseObject=null;
	
	private PromptDetailsRequestObject promptDetailsRequestObject=null;
	
	private PrinterDetailsObject printerDetailsObject=null;
	
	private PrinterSelectionRequestObject printerSelectionRequestObject=null;
	
	private FinalizationMessageObject finalizationMessageObject=null;

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	public FinalizationMessageObject getFinalizationMessageObject() {
		return finalizationMessageObject;
	}

	public void setFinalizationMessageObject(
			FinalizationMessageObject finalizationMessageObject) {
		this.finalizationMessageObject = finalizationMessageObject;
	}

	public InitiationRequestObject getInitiationRequestObject() {
		return initiationRequestObject;
	}

	public void setInitiationRequestObject(
			InitiationRequestObject initiationRequestObject) {
		this.initiationRequestObject = initiationRequestObject;
	}

	public InitiationResponseObject getInitiationResponseObject() {
		return initiationResponseObject;
	}

	public void setInitiationResponseObject(
			InitiationResponseObject initiationResponseObject) {
		this.initiationResponseObject = initiationResponseObject;
	}

	public PrinterDetailsObject getPrinterDetailsObject() {
		return printerDetailsObject;
	}

	public void setPrinterDetailsObject(PrinterDetailsObject printerDetailsObject) {
		this.printerDetailsObject = printerDetailsObject;
	}

	public PrinterSelectionRequestObject getPrinterSelectionRequestObject() {
		return printerSelectionRequestObject;
	}

	public void setPrinterSelectionRequestObject(
			PrinterSelectionRequestObject printerSelectionRequestObject) {
		this.printerSelectionRequestObject = printerSelectionRequestObject;
	}

	public PromptDetailsRequestObject getPromptDetailsRequestObject() {
		return promptDetailsRequestObject;
	}

	public void setPromptDetailsRequestObject(
			PromptDetailsRequestObject promptDetailsRequestObject) {
		this.promptDetailsRequestObject = promptDetailsRequestObject;
	}
	
	
	
	
	
	

}
