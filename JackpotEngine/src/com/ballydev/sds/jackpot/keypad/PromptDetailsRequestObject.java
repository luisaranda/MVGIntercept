package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * Object Class for the prompted details entered by user from GMU.
 * 
 * @author ranjithkumarm
 *
 */
public class PromptDetailsRequestObject implements ICustomRequest,Serializable{

	private static final long serialVersionUID = -5165246129917717156L;
	
	
	private String assetNumber=null;
	
	private Integer siteId=0;
	
	private String requestFlagFromSession=null;
	
	private String employeeCrdNumberProcessing=null;
	
	private String playerNumber=null;
	
	private long jackpotAmountOccurred=0;
	
	private int payLine=0;
	
	private int winningCombination=0;
	
	private int coinsPlayed=0;
	
	private int shiftSelected=0;	
	
	private boolean pouchPayEnabled=false;
	
	private String employeeCrdNumberAuthorizing=null;

	public int getCoinsPlayed() {
		return coinsPlayed;
	}

	public void setCoinsPlayed(int coinsPlayed) {
		this.coinsPlayed = coinsPlayed;
	}

	
	public String getEmployeeCrdNumberAuthorizing() {
		return employeeCrdNumberAuthorizing;
	}

	public void setEmployeeCrdNumberAuthorizing(String employeeCrdNumberAuthorizing) {
		this.employeeCrdNumberAuthorizing = employeeCrdNumberAuthorizing;
	}

	public String getEmployeeCrdNumberProcessing() {
		return employeeCrdNumberProcessing;
	}

	public void setEmployeeCrdNumberProcessing(String employeeCrdNumberProcessing) {
		this.employeeCrdNumberProcessing = employeeCrdNumberProcessing;
	}

	public long getJackpotAmountOccurred() {
		return jackpotAmountOccurred;
	}

	public void setJackpotAmountOccurred(long jackpotAmountOccurred) {
		this.jackpotAmountOccurred = jackpotAmountOccurred;
	}

	public int getPayLine() {
		return payLine;
	}

	public void setPayLine(int payLine) {
		this.payLine = payLine;
	}

	public String getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(String playerNumber) {
		this.playerNumber = playerNumber;
	}

	

	public int getShiftSelected() {
		return shiftSelected;
	}

	public void setShiftSelected(int shiftSelected) {
		this.shiftSelected = shiftSelected;
	}

	public int getWinningCombination() {
		return winningCombination;
	}

	public void setWinningCombination(int winningCombination) {
		this.winningCombination = winningCombination;
	}

	public boolean isPouchPayEnabled() {
		return pouchPayEnabled;
	}

	public void setPouchPayEnabled(boolean pouchPayEnabled) {
		this.pouchPayEnabled = pouchPayEnabled;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getRequestFlagFromSession() {
		return requestFlagFromSession;
	}

	public void setRequestFlagFromSession(String requestFlagFromSession) {
		this.requestFlagFromSession = requestFlagFromSession;
	}

}
