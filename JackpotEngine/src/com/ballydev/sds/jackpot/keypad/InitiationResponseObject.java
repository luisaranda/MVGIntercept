package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * Initiation Response Object for Jackpot process through Keypad.
 * 
 * @author ranjithkumarm
 *
 */
public class InitiationResponseObject implements ICustomResponse,Serializable{

	
	private static final long serialVersionUID = 1465933314130765276L;
	
	private boolean processFlag=false;
	
	private boolean promptJackpotAmount=false;
	
	private boolean promptPayLine=false;
	
	private boolean promptWinningCombination=false;
	
	private boolean promptForCoinsPlayed=false;
	
	private boolean promptShift=false;
	
	private boolean promptCMHP=false;
	
	private boolean promptPouchPay=false;
	
	private boolean promptEmployeeAuth=false;
	
	
	//additional Fields for validation
	
	private String authLevel=null;
	
	private String authAmount=null;
	

	public String getAuthAmount() {
		return authAmount;
	}

	public void setAuthAmount(String authAmount) {
		this.authAmount = authAmount;
	}

	public String getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(String authLevel) {
		this.authLevel = authLevel;
	}

	public boolean isProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(boolean processFlag) {
		this.processFlag = processFlag;
	}

	public boolean isPromptCMHP() {
		return promptCMHP;
	}

	public void setPromptCMHP(boolean promptCMHP) {
		this.promptCMHP = promptCMHP;
	}

	public boolean isPromptEmployeeAuth() {
		return promptEmployeeAuth;
	}

	public void setPromptEmployeeAuth(boolean promptEmployeeAuth) {
		this.promptEmployeeAuth = promptEmployeeAuth;
	}

	public boolean isPromptForCoinsPlayed() {
		return promptForCoinsPlayed;
	}

	public void setPromptForCoinsPlayed(boolean promptForCoinsPlayed) {
		this.promptForCoinsPlayed = promptForCoinsPlayed;
	}

	public boolean isPromptJackpotAmount() {
		return promptJackpotAmount;
	}

	public void setPromptJackpotAmount(boolean promptJackpotAmount) {
		this.promptJackpotAmount = promptJackpotAmount;
	}

	public boolean isPromptPayLine() {
		return promptPayLine;
	}

	public void setPromptPayLine(boolean promptPayLine) {
		this.promptPayLine = promptPayLine;
	}

	public boolean isPromptPouchPay() {
		return promptPouchPay;
	}

	public void setPromptPouchPay(boolean promptPouchPay) {
		this.promptPouchPay = promptPouchPay;
	}

	public boolean isPromptShift() {
		return promptShift;
	}

	public void setPromptShift(boolean promptShift) {
		this.promptShift = promptShift;
	}

	public boolean isPromptWinningCombination() {
		return promptWinningCombination;
	}

	public void setPromptWinningCombination(boolean promptWinningCombination) {
		this.promptWinningCombination = promptWinningCombination;
	}
	
	

}
