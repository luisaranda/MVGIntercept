/*****************************************************************************
 * $Id: JackpotDetailsDTO.java,v 1.5, 2011-02-15 07:29:52Z, Subha Viswanathan$
 * $Date: 2/15/2011 1:29:52 AM$
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class which contains getters and setters for all the jackpot specific fields  
 * @author dambereen
 * @version $Revision: 6$
 */
public class JackpotDetailsDTO extends JackpotBaseDTO{	
	/* From Slip.Jackpot Table */
	
	private static final long serialVersionUID = 7368188508745928937L;

	/**
	 * Area Short Name instance
	 */
	private String areaShortName;
	
	/**
	 * Trans Status Flag Id instance
	 */
	private short transStatusFlagId;
	
	/**
	 * Slot Door Open
	 */
	private boolean slotDoorOpen;
	
	/**
	 * Status Flag Desc
	 */
	private String  statusFlagDesc;
	
	/**
	 * Employee Card No
	 */
	private String employeeCard;
	
	/**
	 * Slip Printer used - Holds the printer used for 
	 */
	private String printerUsed;
	
	/**
	 * Slot Description
	 */
	private String slotDescription;
	
	/**
	 * MessageId from the Transaction table
	 */
	private long transMessageId;
	
	/**
	 * Boolean flag to send an alert for Jackpot - XC 10
	 */
	private boolean sendAlert;
	
	/**
	 * Field that is set to denote a duplicate event
	 */
	private boolean isDuplicateMsg;
	
	/**
	 * Player name
	 */
	private String playerName; 
	
	/**
	 * Event response code
	 */
	private String responseCode;
	
	/**
	 * Game denom of slot
	 */
	private long gmuDenom;	
	
	/**
	 * Jackpot Type ID
	 */
	private short jackpotTypeId;	
	
	/**
	 * SlotAttendantFirstName
	 */
	private String slotAttendantFirstName;	
	
	/**
	 * slotAttendantLastName
	 */
	private String slotAttendantLastName;
	/**
	 * Rounded Hand Paid Amount
	 */
	private long hpjpAmountRounded;
	/**
	 * Jackpot tax amount
	 */
	private long taxAmount;
	/**
	 * Jackpot Net Amount
	 */
	private long jackpotNetAmount;
	/**
	 * Flag that used to check if the data was posted successfully
	 */
	private boolean postedSuccessfully;	
	/**
	 * Field that holds the printer schema
	 */
	private String printerSchema;	
	/**
	 * Field that holds the slip schema
	 */
	private String slipSchema;	
    /**
     * Tax Type Id instance
     */
	private short taxTypeId;
	/**
	 * Jackpot Id instance
	 */
	private String jackpotId = null;
	/**
	 * Slot Attendant Id instance
	 */
	private String slotAttendantId = null;
	/**
	 * Associated Player Card instance
	 */
	private String associatedPlayerCard = null;
	/**
	 * Player Card instance
	 */
	private String playerCard = null;
	/**
	 * Original Amount instance
	 */
	private long originalAmount;
	/**
	 * MachinePaid Amount instance
	 */
	private long machinePaidAmount;
	/**
	 * HandPaid Amount instance
	 */
	private long hpjpAmount;
	/**
	 * Pay Line instance
	 */
	private String payline = null;
	/**
	 * Coins Played instance
	 */
	private int coinsPlayed;
	/**
	 * Blind Attempt instance
	 */
	private Short blindAttempt = null;
	/**
	 * Winning Combination instance 
	 */
	private String winningComb = null;
	/**
	 * Is Slot Online
	 */
	private boolean isSlotOnline = false;
	
	
	/* From Slip.Slip_Data Table */	
	
	/**
	 * Field that holds the Process Flag ID
	 */
	private short processFlagId;	
	
	/**
	 * Actor Login instance
	 */
	private String actorLogin = null;
	/**
	 * assetConfNumberUsed instance
	 */
	private String assetConfNumberUsed=null;
	
	/**
	 * Process Employee Login instance
	 */
	private String processEmployeeId = null;
	
	/**
	 * Process Employee Login instance
	 */
	private String processEmployeeName = null;
	
	/**
	 * Reprint Employee Login instance
	 */
	private String reprintEmployeeId = null;
	
	/**
	 * Reprint Employee Login instance
	 */
	private String reprintEmployeeName = null;
	
	/**
	 * Void Employee Login instance
	 */
	private String voidEmployeeId = null;
	
	/**
	 * Void Employee Login instance
	 */
	private String voidEmployeeName = null;
	
	/**
	 * Authorization Employee Id1 instance
	 */
	private String authEmployeeId1 = null;
	/**
	 * Authorization Employee Id2 instance
	 */
	private String authEmployeeId2 = null;
	/**
	 * Transaction Date instance
	 */
	private long transactionDate;
	/**
	 * Print Date instance
	 */
	private long printDate;
	/**
	 * Shift instance
	 */
	private String shift = null;
	/**
	 * Window Number instance 
	 */
	private String windowNumber = null;
	/**
	 * Seal Number instance
	 */
	private String sealNumber = null;
	
	
	/* From Slip.Slip_Reference Table */
	
	/**
	 * Post Flag Id
	 */
	private short postFlagId;
	/**
	 * Denomination for the slot
	 */
	private long slotDenomination;
	/**
	 * Audit Code Id instance
	 */
	private short auditCodeId;
	/**
	 * Status Flag Id instance
	 */
	private short statusFlagId;
	/**
	 * Slip Type Id instance
	 */
	private short slipTypeId;
	/**
	 * Area Long Name instance
	 */
	private String areaLongName;
	/**
	 * assetConfigNumber instance
	 */
	private String assetConfigNumber;
	/**
	 * Site Id instance
	 */
	private int siteId;
	/**
	 * Denomination Id instance
	 */
	private Short denominationId;	
	/**
	 * Sequence Number instance
	 */
	private long sequenceNumber;
	/**
	 * Message Id instance
	 */
	private long messageId;
	/**
	 * Collection Id instance
	 */
	private int collectionId;
	
	/* From other engines */
	/**
	 * Player First Name Instance
	 */
	private String playerFirstName;
	/**
	 * Player Last Name Instance
	 */
	private String playerLastName;
	/**
	 * Asset Configuration instance
	 */
	private String assetConfigLocation;
	
	/**
	 * @return the associatedPlayerCard
	 */
	public String getAssociatedPlayerCard() {
		return associatedPlayerCard;
	}
	/**
	 * @param associatedPlayerCard the associatedPlayerCard to set
	 */
	public void setAssociatedPlayerCard(String associatedPlayerCard) {
		this.associatedPlayerCard = associatedPlayerCard;
	}
	/**
	 * @return the auditCodeId
	 */
	public short getAuditCodeId() {
		return auditCodeId;
	}
	/**
	 * @param auditCodeId the auditCodeId to set
	 */
	public void setAuditCodeId(short auditCodeId) {
		this.auditCodeId = auditCodeId;
	}
	/**
	 * @return the authEmployeeId1
	 */
	public String getAuthEmployeeId1() {
		return authEmployeeId1;
	}
	/**
	 * @param authEmployeeId1 the authEmployeeId1 to set
	 */
	public void setAuthEmployeeId1(String authEmployeeId1) {
		this.authEmployeeId1 = authEmployeeId1;
	}
	/**
	 * @return the authEmployeeId2
	 */
	public String getAuthEmployeeId2() {
		return authEmployeeId2;
	}
	/**
	 * @param authEmployeeId2 the authEmployeeId2 to set
	 */
	public void setAuthEmployeeId2(String authEmployeeId2) {
		this.authEmployeeId2 = authEmployeeId2;
	}
	/**
	 * @return the blindAttempt
	 */
	public Short getBlindAttempt() {
		return blindAttempt;
	}
	/**
	 * @param blindAttempt the blindAttempt to set
	 */
	public void setBlindAttempt(Short blindAttempt) {
		this.blindAttempt = blindAttempt;
	}
	
	/**
	 * @return the coinsPlayed
	 */
	public int getCoinsPlayed() {
		return coinsPlayed;
	}
	/**
	 * @param coinsPlayed the coinsPlayed to set
	 */
	public void setCoinsPlayed(int coinsPlayed) {
		this.coinsPlayed = coinsPlayed;
	}
	/**
	 * @return the denominationId
	 */
	public Short getDenominationId() {
		return denominationId;
	}
	/**
	 * @param denominationId the denominationId to set
	 */
	public void setDenominationId(Short denominationId) {
		this.denominationId = denominationId;
	}
	/**
	 * @return the hpjpAmount
	 */
	public long getHpjpAmount() {
		return hpjpAmount;
	}
	/**
	 * @param hpjpAmount the hpjpAmount to set
	 */
	public void setHpjpAmount(long hpjpAmount) {
		this.hpjpAmount = hpjpAmount;
	}
	/**
	 * @return the jackpotId
	 */
	public String getJackpotId() {
		return jackpotId;
	}
	/**
	 * @param jackpotId the jackpotId to set
	 */
	public void setJackpotId(String jackpotId) {
		this.jackpotId = jackpotId;
	}
	/**
	 * @return the machinePaidAmount
	 */
	public long getMachinePaidAmount() {
		return machinePaidAmount;
	}
	/**
	 * @param machinePaidAmount the machinePaidAmount to set
	 */
	public void setMachinePaidAmount(long machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}
	/**
	 * @return the messageId
	 */
	public long getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the originalAmount
	 */
	public long getOriginalAmount() {
		return originalAmount;
	}
	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(long originalAmount) {
		this.originalAmount = originalAmount;
	}
	/**
	 * @return the payline
	 */
	public String getPayline() {
		return payline;
	}
	/**
	 * @param payline the payline to set
	 */
	public void setPayline(String payline) {
		this.payline = payline;
	}	
	/**
	 * @return the playerCard
	 */
	public String getPlayerCard() {
		return playerCard;
	}
	/**
	 * @param playerCard the playerCard to set
	 */
	public void setPlayerCard(String playerCard) {
		this.playerCard = playerCard;
	}
	/**
	 * @return the printDate
	 */
	public long getPrintDate() {
		return printDate;
	}
	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(long printDate) {
		this.printDate = printDate;
	}
	
	/**
	 * @return the sealNumber
	 */
	public String getSealNumber() {
		return sealNumber;
	}
	/**
	 * @param sealNumber the sealNumber to set
	 */
	public void setSealNumber(String sealNumber) {
		this.sealNumber = sealNumber;
	}
	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	/**
	 * @return the shift
	 */
	public String getShift() {
		return shift;
	}
	/**
	 * @param shift the shift to set
	 */
	public void setShift(String shift) {
		this.shift = shift;
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
	/**
	 * @return the slotAttendantId
	 */
	public String getSlotAttendantId() {
		return slotAttendantId;
	}
	/**
	 * @param slotAttendantId the slotAttendantId to set
	 */
	public void setSlotAttendantId(String slotAttendantId) {
		this.slotAttendantId = slotAttendantId;
	}
	/**
	 * @return the statusFlagId
	 */
	public short getStatusFlagId() {
		return statusFlagId;
	}
	/**
	 * @param statusFlagId the statusFlagId to set
	 */
	public void setStatusFlagId(short statusFlagId) {
		this.statusFlagId = statusFlagId;
	}
	/**
	 * @return the taxTypeId
	 */
	public short getTaxTypeId() {
		return taxTypeId;
	}
	/**
	 * @param taxTypeId the taxTypeId to set
	 */
	public void setTaxTypeId(short taxTypeId) {
		this.taxTypeId = taxTypeId;
	}
	/**
	 * @return the transactionDate
	 */
	public long getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return the windowNumber
	 */
	public String getWindowNumber() {
		return windowNumber;
	}
	/**
	 * @param windowNumber the windowNumber to set
	 */
	public void setWindowNumber(String windowNumber) {
		this.windowNumber = windowNumber;
	}
	/**
	 * @return the slipTypeId
	 */
	public short getSlipTypeId() {
		return slipTypeId;
	}
	/**
	 * 
	 * @param slipTypeId the slipTypeId to set
	 */
	public void setSlipTypeId(short slipTypeId) {
		this.slipTypeId = slipTypeId;
	}
	/**
	 * 
	 * @return actorLogin
	 */
	public String getActorLogin() {
		return actorLogin;
	}
	/**
	 * 
	 * @param actorLogin the actorLogin to set
	 */
	public void setActorLogin(String actorLogin) {
		this.actorLogin = actorLogin;
	}
	/**
	 * @return the collectionId
	 */
	public int getCollectionId() {
		return collectionId;
	}
	/**
	 * @param collectionId the collectionId to set
	 */
	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}
	/**
	 * @return the winningComb
	 */
	public String getWinningComb() {
		return winningComb;
	}
	/**
	 * @param winningComb the winningComb to set
	 */
	public void setWinningComb(String winningComb) {
		this.winningComb = winningComb;
	}
	/**
	 * @return the playerFirstName
	 */
	public String getPlayerFirstName() {
		return playerFirstName;
	}
	/**
	 * @param playerFirstName the playerFirstName to set
	 */
	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}
	/**
	 * @return the playerLastName
	 */
	public String getPlayerLastName() {
		return playerLastName;
	}
	/**
	 * @param playerLastName the playerLastName to set
	 */
	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}
	/**
	 * @return the assetConfNumberUsed
	 */
	public String getAssetConfNumberUsed() {
		return assetConfNumberUsed;
	}
	/**
	 * @param assetConfNumberUsed the assetConfNumberUsed to set
	 */
	public void setAssetConfNumberUsed(String assetConfNumberUsed) {
		this.assetConfNumberUsed = assetConfNumberUsed;
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
	
	/**
	 * @return the assetConfigLocation
	 */
	public String getAssetConfigLocation() {
		return assetConfigLocation;
	}
	/**
	 * @param assetConfigLocation the assetConfigLocation to set
	 */
	public void setAssetConfigLocation(String assetConfigLocation) {
		this.assetConfigLocation = assetConfigLocation;
	}
	/**
	 * @return the printerSchema
	 */
	public String getPrinterSchema() {
		return printerSchema;
	}
	/**
	 * @param printerSchema the printerSchema to set
	 */
	public void setPrinterSchema(String printerSchema) {
		this.printerSchema = printerSchema;
	}
	/**
	 * @return the slipSchema
	 */
	public String getSlipSchema() {
		return slipSchema;
	}
	/**
	 * @param slipSchema the slipSchema to set
	 */
	public void setSlipSchema(String slipSchema) {
		this.slipSchema = slipSchema;
	}
	/**
	 * @return the postedSuccessfully
	 */
	public boolean isPostedSuccessfully() {
		return postedSuccessfully;
	}
	/**
	 * @param postedSuccessfully the postedSuccessfully to set
	 */
	public void setPostedSuccessfully(boolean postedSuccessfully) {
		this.postedSuccessfully = postedSuccessfully;
	}
	/**
	 * @return the areaLongName
	 */
	public String getAreaLongName() {
		return areaLongName;
	}
	/**
	 * @param areaLongName the areaLongName to set
	 */
	public void setAreaLongName(String areaLongName) {
		this.areaLongName = areaLongName;
	}
	/**
	 * @return the slotDenomination
	 */
	public long getSlotDenomination() {
		return slotDenomination;
	}
	/**
	 * @param slotDenomination the slotDenomination to set
	 */
	public void setSlotDenomination(long slotDenomination) {
		this.slotDenomination = slotDenomination;
	}
	/**
	 * @return the jackpotNetAmount
	 */
	public long getJackpotNetAmount() {
		return jackpotNetAmount;
	}
	/**
	 * @param jackpotNetAmount the jackpotNetAmount to set
	 */
	public void setJackpotNetAmount(long jackpotNetAmount) {
		this.jackpotNetAmount = jackpotNetAmount;
	}
	/**
	 * @return the taxAmount
	 */
	public long getTaxAmount() {
		return taxAmount;
	}
	/**
	 * @param taxAmount the taxAmount to set
	 */
	public void setTaxAmount(long taxAmount) {
		this.taxAmount = taxAmount;
	}
	/**
	 * @return the hpjpAmountRounded
	 */
	public long getHpjpAmountRounded() {
		return hpjpAmountRounded;
	}
	/**
	 * @param hpjpAmountRounded the hpjpAmountRounded to set
	 */
	public void setHpjpAmountRounded(long hpjpAmountRounded) {
		this.hpjpAmountRounded = hpjpAmountRounded;
	}
	/**
	 * @return the slotAttendantFirstName
	 */
	public String getSlotAttendantFirstName() {
		return slotAttendantFirstName;
	}
	/**
	 * @param slotAttendantFirstName the slotAttendantFirstName to set
	 */
	public void setSlotAttendantFirstName(String slotAttendantFirstName) {
		this.slotAttendantFirstName = slotAttendantFirstName;
	}
	/**
	 * @return the slotAttendantLastName
	 */
	public String getSlotAttendantLastName() {
		return slotAttendantLastName;
	}
	/**
	 * @param slotAttendantLastName the slotAttendantLastName to set
	 */
	public void setSlotAttendantLastName(String slotAttendantLastName) {
		this.slotAttendantLastName = slotAttendantLastName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Object value = "";
		StringBuilder strBuilder = new StringBuilder();
		Method[] methods = this.getClass().getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].getName().startsWith("get")){
				try {
					value = methods[i].invoke(this, (Object[])null);
					if(value != null){
						strBuilder.append("Methods available: " + methods[i].getName() + " = " + value.toString());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}		
		return strBuilder.toString();
	}
	/**
	 * @return the postFlagId
	 */
	public short getPostFlagId() {
		return postFlagId;
	}
	/**
	 * @param postFlagId the postFlagId to set
	 */
	public void setPostFlagId(short postFlagId) {
		this.postFlagId = postFlagId;
	}
	/**
	 * @return the processFlagId
	 */
	public short getProcessFlagId() {
		return processFlagId;
	}
	/**
	 * @param processFlagId the processFlagId to set
	 */
	public void setProcessFlagId(short processFlagId) {
		this.processFlagId = processFlagId;
	}
	/**
	 * @return the jackpotTypeId
	 */
	public short getJackpotTypeId() {
		return jackpotTypeId;
	}
	/**
	 * @param jackpotTypeId the jackpotTypeId to set
	 */
	public void setJackpotTypeId(short jackpotTypeId) {
		this.jackpotTypeId = jackpotTypeId;
	}
	
	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}
	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	/**
	 * @return the isDuplicate
	 */
	public boolean isDuplicateMsg() {
		return isDuplicateMsg;
	}
	/**
	 * @param isDuplicate the isDuplicate to set
	 */
	public void setDuplicateMsg(boolean isDuplicateMsg) {
		this.isDuplicateMsg = isDuplicateMsg;
	}
	/**
	 * @return the gmuDenom
	 */
	public long getGmuDenom() {
		return gmuDenom;
	}
	/**
	 * @param gmuDenom the gmuDenom to set
	 */
	public void setGmuDenom(long gmuDenom) {
		this.gmuDenom = gmuDenom;
	}
	/**
	 * @return the sendAlert
	 */
	public boolean isSendAlert() {
		return sendAlert;
	}
	/**
	 * @param sendAlert the sendAlert to set
	 */
	public void setSendAlert(boolean sendAlert) {
		this.sendAlert = sendAlert;
	}
	/**
	 * @return the transMessageId
	 */
	public long getTransMessageId() {
		return transMessageId;
	}
	/**
	 * @param transMessageId the transMessageId to set
	 */
	public void setTransMessageId(long transMessageId) {
		this.transMessageId = transMessageId;
	}
	/**
	 * @return the slotDescription
	 */
	public String getSlotDescription() {
		return slotDescription;
	}
	/**
	 * @param slotDescription the slotDescription to set
	 */
	public void setSlotDescription(String slotDescription) {
		this.slotDescription = slotDescription;
	}
	/**
	 * @return the printerUsed
	 */
	public String getPrinterUsed() {
		return printerUsed;
	}
	/**
	 * @param printerUsed the printerUsed to set
	 */
	public void setPrinterUsed(String printerUsed) {
		this.printerUsed = printerUsed;
	}
	/**
	 * @return the employeeCard
	 */
	public String getEmployeeCard() {
		return employeeCard;
	}
	/**
	 * @param employeeCard the employeeCard to set
	 */
	public void setEmployeeCard(String employeeCard) {
		this.employeeCard = employeeCard;
	}
	
	/**
	 * @return the statusFlagDesc
	 */
	public String getStatusFlagDesc() {
		return statusFlagDesc;
	}
	/**
	 * @param statusFlagDesc the statusFlagDesc to set
	 */
	public void setStatusFlagDesc(String statusFlagDesc) {
		this.statusFlagDesc = statusFlagDesc;
	}
	/**
	 * @return the slotDoorOpen
	 */
	public boolean isSlotDoorOpen() {
		return slotDoorOpen;
	}
	/**
	 * @param slotDoorOpen the slotDoorOpen to set
	 */
	public void setSlotDoorOpen(boolean slotDoorOpen) {
		this.slotDoorOpen = slotDoorOpen;
	}
	/**
	 * @return the transStatusFlagId
	 */
	public short getTransStatusFlagId() {
		return transStatusFlagId;
	}
	/**
	 * @param transStatusFlagId the transStatusFlagId to set
	 */
	public void setTransStatusFlagId(short transStatusFlagId) {
		this.transStatusFlagId = transStatusFlagId;
	}
	/**
	 * @return the processEmployeeId
	 */
	public String getProcessEmployeeId() {
		return processEmployeeId;
	}
	/**
	 * @param processEmployeeId the processEmployeeId to set
	 */
	public void setProcessEmployeeId(String processEmployeeId) {
		this.processEmployeeId = processEmployeeId;
	}
	/**
	 * @return the processEmployeeName
	 */
	public String getProcessEmployeeName() {
		return processEmployeeName;
	}
	/**
	 * @param processEmployeeName the processEmployeeName to set
	 */
	public void setProcessEmployeeName(String processEmployeeName) {
		this.processEmployeeName = processEmployeeName;
	}
	/**
	 * @return the reprintEmployeeId
	 */
	public String getReprintEmployeeId() {
		return reprintEmployeeId;
	}
	/**
	 * @param reprintEmployeeId the reprintEmployeeId to set
	 */
	public void setReprintEmployeeId(String reprintEmployeeId) {
		this.reprintEmployeeId = reprintEmployeeId;
	}
	/**
	 * @return the reprintEmployeeName
	 */
	public String getReprintEmployeeName() {
		return reprintEmployeeName;
	}
	/**
	 * @param reprintEmployeeName the reprintEmployeeName to set
	 */
	public void setReprintEmployeeName(String reprintEmployeeName) {
		this.reprintEmployeeName = reprintEmployeeName;
	}
	/**
	 * @return the voidEmployeeId
	 */
	public String getVoidEmployeeId() {
		return voidEmployeeId;
	}
	/**
	 * @param voidEmployeeId the voidEmployeeId to set
	 */
	public void setVoidEmployeeId(String voidEmployeeId) {
		this.voidEmployeeId = voidEmployeeId;
	}
	/**
	 * @return the voidEmployeeName
	 */
	public String getVoidEmployeeName() {
		return voidEmployeeName;
	}
	/**
	 * @param voidEmployeeName the voidEmployeeName to set
	 */
	public void setVoidEmployeeName(String voidEmployeeName) {
		this.voidEmployeeName = voidEmployeeName;
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
	 * Method to get if slot is online when JP is hit
	 * @return
	 * @author vsubha
	 */
	public boolean isSlotOnline() {
		return this.isSlotOnline;
	}
	
	/**
	 * Method to set if slot is online when JP is hit
	 * @param isSlotOnline
	 * @author vsubha
	 */
	public void setSlotOnline(boolean isSlotOnline) {
		this.isSlotOnline = isSlotOnline;
	}
	
}
