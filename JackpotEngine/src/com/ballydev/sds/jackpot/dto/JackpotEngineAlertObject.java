/*****************************************************************************
 * $Id: JackpotEngineAlertObject.java,v 1.4, 2009-04-27 14:24:49Z, Ambereen Drewitt$
 * $Date: 4/27/2009 9:24:49 AM$
 * $Log$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.dto;

import java.util.Calendar;

import com.ballydev.sds.common.PublicCloneable;

/**
 * Class the has all the getters and setters for the JackpotAlertObject in the Messages Project
 * @author dambereen
 * @version $Revision: 5$
 * 
 */
public class JackpotEngineAlertObject extends JackpotBaseDTO{
	
	
	private static final long serialVersionUID = -8274632059010688026L;

	/**
	 * Site Id
	 */
	private int siteId;
	
	/**
	 * Area Short Name.
	 */
	private String areaShortName=null;
	
	/**
	 * terminal message number.
	 */
	private int terminalMessageNumber=0;	
	
	/**
	 * Terminal Message.
	 */
	private String terminalMessage=null;	
	
	/**
	 * Sending Engine Name.
	 */
	private String sendingEngineName=null;	
	
	/**
	 * message priority
	 */
	private int messagePriority=0;	
	
	/**
	 * line address
	 */
	private String lineAddress=null;	
	
	/**
	 * exception code.
	 */
	private char exceptionCode='0';	
	
	/**
	 * Card Id for the employee.
	 */
	private String employeeCardNumber=null;	
	
	/**
	 * slot number
	 */
	private String assetConfigNumber;	
	
	/**
	 * SMI Code.
	 */
	private String smiCode=null;	
	
	/**
	 * message id.
	 */
	private long messageId=0;	
	
	/**
	 * Jackpot Amount.
	 */
	private long jackpotAmount=0;	
	
	/**
	 * Jackpot Id.
	 */
	private char jackpotId='0';
	
	
	private String employeeName="";
	
	private String standNumber="";
	
	private String employeeId=null;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	/**
	 * @return the assetConfigNumber
	 */
	public String getAssetConfigNumber() {
		return assetConfigNumber;
	}

	/**
	 * @param assetConfigNumber the assetConfigNumber to set
	 */
	public void setAssetConfigNumber(String assetConfigNumber) {
		this.assetConfigNumber = assetConfigNumber;
	}

	public String getEmployeeCardNumber() {
		return employeeCardNumber;
	}

	public void setEmployeeCardNumber(String employeeCardNumber) {
		this.employeeCardNumber = employeeCardNumber;
	}

	public char getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(char exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public long getJackpotAmount() {
		return jackpotAmount;
	}

	public void setJackpotAmount(long jackpotAmount) {
		this.jackpotAmount = jackpotAmount;
	}

	public char getJackpotId() {
		return jackpotId;
	}

	public void setJackpotId(char jackpotId) {
		this.jackpotId = jackpotId;
	}

	public String getLineAddress() {
		return lineAddress;
	}

	public void setLineAddress(String lineAddress) {
		this.lineAddress = lineAddress;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public int getMessagePriority() {
		return messagePriority;
	}

	public void setMessagePriority(int messagePriority) {
		this.messagePriority = messagePriority;
	}

	public String getSendingEngineName() {
		return sendingEngineName;
	}

	public void setSendingEngineName(String sendingEngineName) {
		this.sendingEngineName = sendingEngineName;
	}

	public String getSmiCode() {
		return smiCode;
	}

	public void setSmiCode(String smiCode) {
		this.smiCode = smiCode;
	}

	public String getTerminalMessage() {
		return terminalMessage;
	}

	public void setTerminalMessage(String terminalMessage) {
		this.terminalMessage = terminalMessage;
	}

	public int getTerminalMessageNumber() {
		return terminalMessageNumber;
	}

	public void setTerminalMessageNumber(int terminalMessageNumber) {
		this.terminalMessageNumber = terminalMessageNumber;
	}
	
	
	public String getEmployeeName() {
		
		return employeeName;
	}

	public String getStandNumber() {
		
		return standNumber;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
		
	}

	public void setStandNumber(String standNumber) {
		
		this.standNumber = standNumber;
	}	

	/**
	 * @return the areaShortName
	 */
	public String getAreaShortName() {
		return areaShortName;
	}

	/**
	 * @param areaShortName the areaShortName to set
	 */
	public void setAreaShortName(String areaShortName) {
		this.areaShortName = areaShortName;
	}

	/**
	 * @return the siteId
	 */
	public int getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	
	

	
}
