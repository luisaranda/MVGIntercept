package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * Final Response Object for Jackpot process through Keypad.
 * 
 * @author ranjithkumarm
 *
 */
public class FinalizationMessageObject implements ICustomResponse,Serializable{
	
	private static final long serialVersionUID = -666504759205446042L;
	
	private boolean clearJackpot = false;
	
	private String textMessage = null;
	
	private boolean errorPresent = false;

	public boolean isErrorPresent() {
		return errorPresent;
	}

	public void setErrorPresent(boolean errorPresent) {
		this.errorPresent = errorPresent;
	}

	public boolean isClearJackpot() {
		return clearJackpot;
	}

	public void setClearJackpot(boolean clearJackpot) {
		this.clearJackpot = clearJackpot;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	
	
	

}
