/*****************************************************************************
 * $Id: JackpotDTO.java,v 1.46.1.0.4.0, 2013-10-12 08:39:47Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 3:39:47 AM$
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
import java.util.ArrayList;
import java.util.Date;

import com.ballydev.sds.framework.util.DateEngineUtil;

/**
 * A class which contains getters and setters for jackpot specific fields  
 * @author dambereen
 * @version $Revision: 49$
 */
public class JackpotDTO extends JackpotBaseDTO{	
	/* From Slip.Jackpot Table */

	
	private static final long serialVersionUID = -5430614715657883314L;

	/**
	 * Flag to know if the void transaction was a pending jackpot void from S2S
	 */
	private boolean isS2SPendingVoid = false;
	
	/**
	 * Flag to know if the Jackpot process transaction is a pending jackpot from S2S
	 */
	private boolean isS2SJackpotProcess = false;
	
	/**
	 * Barcode Encode Format
	 */
	private String barEncodeFormat;
	
	/**
	 * This field contains the Asset Line Address
	 */
	private String assetlineAddr;
	
	/**
	 * This field contains the Jackpot / Slip table's primary key (id) 
	 */
	private String slipId;
	
	/**
	 * Falg ro check if the pending jackpot is valid or not
	 */
	private boolean isInvalidPendingJP=false;
	
	/**
	 * Area Short Name instance
	 */
	private String areaShortName;
	
	/**
	 * Flag to insert the pouch pay attendant if its a non carded jackpot
	 */
	private boolean insertPouchPayAttn=false;
	
	/**
	 * Generated Primary Key 
	 */
	private long slipPrimaryKey;
	
	/**
	 * Slip Reference Id instance
	 */
	private long slipReferenceId;
	
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
	 * Currentt Date and Time
	 */
	private String currentDateTime;
	
	/**
	 * Employee LastName
	 */
	private String employeeLastName;
	
	/**
	 * Employee FirstName
	 */
	private String employeeFirstName;
	
	/**
	 * Employee Card No
	 */
	private String employeeCard;
	
	/**
	 * Slip Printer used
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
	 * GMU denom of slot
	 */
	private long gmuDenom;	
	
	/**
	 * SlipReference Updated Ts (currently used for processJackpot and postJackpotVoidStatus Facade methods)
	 */
	private String slipRefUpdatedTs;
	
	/**
	 * Flag to check if jackpot is posted to accounting.
	 */
	private String postToAccounting;
	
	/**
	 * Jackpot Type Description
	 */
	private String jackpotTypeDesc;	
	
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
	
	/*
	 * Jackpot Tax String
	 */
	private String taxRateAmount;
	
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
	 * JP Id instance
	 */
	private char jpId;
	/**
	 * Is Slot Online
	 */
	private boolean isSlotOnline = false;
	
	public char getJpId() {
		return jpId;
	}
	public void setJpId(char jpId) {
		this.jpId = jpId;
	}
	/**
	 * Slot Attendant Id instance
	 */
	private String slotAttendantId = null;
	
	private String amountAuthEmpId = null;
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
	 * Cardless Account Type
	 */
	private String accountType = null;
	
	/**
	 * Cardless Account Number
	 */
	private String accountNumber;
	
	
	/* From Slip.Slip_Data Table */	
	
	/**
	 * Field that holds the Process Flag ID
	 */
	private short processFlagId;	
	
	/**
	 * Field that holds the Process Flag Desc
	 */
	private String processFlagDesc;
	
	/**
	 * Actor Login instance
	 */
	private String actorLogin = null;
	/**
	 * assetConfNumberUsed instance
	 */
	private String assetConfNumberUsed=null;
	/**
	 * Void Employee Login instance
	 */
	private String voidEmployeeLogin = null;
	/**
	 * Print Employee Login instance
	 */
	private String printEmployeeLogin = null;
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
	private String transactionDate;
	/**
	 * Print Date instance
	 */
	private String printDate;
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
	 * Audit Code Description instance
	 */
	private String auditCodeDesc;
	
	/**
	 * Audit Code Id instance
	 */
	private short auditCodeId;
	/**
	 * Status Flag Id instance
	 */
	private short statusFlagId;
	
	/**
	 * Slip Type Description instance
	 */
	private String slipTypeDesc;
	
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
	 * Site No instance
	 */
	private String siteNo;
	
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
	 * Employee Name instance
	 */
	private String employeeName;
	/**
	 * Asset Configuration instance
	 */
	private String assetConfigLocation;
	
	
	private long sdsCalculatedAmount = 0;
	
	/**
	 * Allow cashier desk flow related to status change (PROCESS and PRINTED)
	 */
	private String cashierDeskEnabled;	
	
	/**
	 * Enable check printing for jackpot slip
	 */
	private String enableCheckPrint;
	/**
	 * Generate Random Sequence Number
	 */
	private String generateRandSeqNum;
	/**
	 * Cash Desk Location
	 */
	private String cashDeskLocation;
	/**
	 * Jackpot Check expiry date
	 */
	private String expiryDate;
	/**
	 * Integer List of Progressive Level
	 */
	private ArrayList<Integer> lstProgressiveLevel = null;
	/**
	 * Progressive Level values for Manual Progressive Jackpot
	 */
	private String progressiveLevel = null;
	
	private boolean validateEmpSession;
		
	/**
	 * Field that holds the value of the pending jp transaction date 
	 * used for sending alert 92 - for DLS fix
	 */
	private long pendingAlertTxnDate;
	
	/**
	 * Field that holds the value whether to auto void the reprinted jp seq
	 * Peermont Requirement
	 */
	private String autoVoidReprintedJp;
	
	/**
	 * Flag to check if the jackpot was generated externally by the progressive controller
	 */
	private boolean isJpProgControllerGenerated = false;
	
	/**
	 * MVG Intercept
	 */
	
	private long interceptAmount;
	
	
	public long getInterceptAmount(){
		return interceptAmount;
	}
	
	public void setInterceptAmount(long interceptAmount){
		this.interceptAmount = interceptAmount;
	}
	
	/**
	 * End MVG custom code
	 */
	
	public boolean isValidateEmpSession() {
		return validateEmpSession;
	}
	public void setValidateEmpSession(boolean validateEmpSession) {
		this.validateEmpSession = validateEmpSession;
	}
	
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
	public Date getPrintDate() {
		return DateEngineUtil.convertStringToDate(printDate);
	}
	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(Date printDate) {
		this.printDate = DateEngineUtil.convertDateToString(printDate);
	}
	/**
	 * @return the printEmployeeLogin
	 */
	public String getPrintEmployeeLogin() {
		return printEmployeeLogin;
	}
	/**
	 * @param printEmployeeLogin the printEmployeeLogin to set
	 */
	public void setPrintEmployeeLogin(String printEmployeeLogin) {
		this.printEmployeeLogin = printEmployeeLogin;
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
	public Date getTransactionDate() {
		return DateEngineUtil.convertStringToDate(transactionDate);
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = DateEngineUtil.convertDateToString(transactionDate);
	}
	/**
	 * @return the voidEmployeeLogin
	 */
	public String getVoidEmployeeLogin() {
		return voidEmployeeLogin;
	}
	/**
	 * @param voidEmployeeLogin the voidEmployeeLogin to set
	 */
	public void setVoidEmployeeLogin(String voidEmployeeLogin) {
		this.voidEmployeeLogin = voidEmployeeLogin;
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
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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
		StringBuilder toStrBuilder = new StringBuilder();
		Method[] methods = this.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get")) {
				try {
					value = methods[i].invoke(this, (Object[]) null);
					if (value != null) {
						toStrBuilder.append("Methods available: " + methods[i].getName() + " = "
								+ value.toString());
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
		return super.toString();
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
	 * @return the employeeFirstName
	 */
	public String getEmployeeFirstName() {
		return employeeFirstName;
	}
	/**
	 * @param employeeFirstName the employeeFirstName to set
	 */
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}
	/**
	 * @return the employeeLastName
	 */
	public String getEmployeeLastName() {
		return employeeLastName;
	}
	/**
	 * @param employeeLastName the employeeLastName to set
	 */
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}
	/**
	 * @return the currentDateTime
	 */
	public Date getCurrentDateTime() {
		return DateEngineUtil.convertStringToDate(currentDateTime);
	}
	/**
	 * @param currentDateTime the currentDateTime to set
	 */
	public void setCurrentDateTime(Date currentDateTime) {
		this.currentDateTime = DateEngineUtil.convertDateToString(currentDateTime);
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
	 * @return the slipReferenceId
	 */
	public long getSlipReferenceId() {
		return slipReferenceId;
	}
	/**
	 * @param slipReferenceId the slipReferenceId to set
	 */
	public void setSlipReferenceId(long slipReferenceId) {
		this.slipReferenceId = slipReferenceId;
	}
	/**
	 * @return the insertPouchPayAttn
	 */
	public boolean isInsertPouchPayAttn() {
		return insertPouchPayAttn;
	}
	/**
	 * @param insertPouchPayAttn the insertPouchPayAttn to set
	 */
	public void setInsertPouchPayAttn(boolean insertPouchPayAttn) {
		this.insertPouchPayAttn = insertPouchPayAttn;
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
	 * @return the isInvalidPendingJP
	 */
	public boolean isInvalidPendingJP() {
		return isInvalidPendingJP;
	}
	/**
	 * @param isInvalidPendingJP the isInvalidPendingJP to set
	 */
	public void setInvalidPendingJP(boolean isInvalidPendingJP) {
		this.isInvalidPendingJP = isInvalidPendingJP;
	}
	/**
	 * @return the auditCodeDesc
	 */
	public String getAuditCodeDesc() {
		return auditCodeDesc;
	}
	/**
	 * @param auditCodeDesc the auditCodeDesc to set
	 */
	public void setAuditCodeDesc(String auditCodeDesc) {
		this.auditCodeDesc = auditCodeDesc;
	}
	/**
	 * @return the jackpotTypeDesc
	 */
	public String getJackpotTypeDesc() {
		return jackpotTypeDesc;
	}
	/**
	 * @param jackpotTypeDesc the jackpotTypeDesc to set
	 */
	public void setJackpotTypeDesc(String jackpotTypeDesc) {
		this.jackpotTypeDesc = jackpotTypeDesc;
	}
	/**
	 * @return the slipTypeDesc
	 */
	public String getSlipTypeDesc() {
		return slipTypeDesc;
	}
	/**
	 * @param slipTypeDesc the slipTypeDesc to set
	 */
	public void setSlipTypeDesc(String slipTypeDesc) {
		this.slipTypeDesc = slipTypeDesc;
	}
	/**
	 * @return the slipId
	 */
	public String getSlipId() {
		return slipId;
	}
	/**
	 * @param slipId the slipId to set
	 */
	public void setSlipId(String slipId) {
		this.slipId = slipId;
	}
	/**
	 * @return the assetlineAddr
	 */
	public String getAssetlineAddr() {
		return assetlineAddr;
	}
	/**
	 * @param assetlineAddr the assetlineAddr to set
	 */
	public void setAssetlineAddr(String assetlineAddr) {
		this.assetlineAddr = assetlineAddr;
	}
	public String getBarEncodeFormat() {
		return barEncodeFormat;
	}
	public void setBarEncodeFormat(String barEncodeFormat) {
		this.barEncodeFormat = barEncodeFormat;
	}
	/**
	 * @return the processFlagDesc
	 */
	public String getProcessFlagDesc() {
		return processFlagDesc;
	}
	/**
	 * @param processFlagDesc the processFlagDesc to set
	 */
	public void setProcessFlagDesc(String processFlagDesc) {
		this.processFlagDesc = processFlagDesc;
	}
	/**
	 * @return the siteNo
	 */
	public String getSiteNo() {
		return siteNo;
	}
	/**
	 * @param siteNo the siteNo to set
	 */
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public long getSdsCalculatedAmount() {
		return sdsCalculatedAmount;
	}
	public void setSdsCalculatedAmount(long sdsCalculatedAmount) {
		this.sdsCalculatedAmount = sdsCalculatedAmount;
	}
	/**
	 * @return the isS2SPendingVoid
	 */
	public boolean isS2SPendingVoid() {
		return isS2SPendingVoid;
	}
	/**
	 * @param isS2SPendingVoid the isS2SPendingVoid to set
	 */
	public void setS2SPendingVoid(boolean isS2SPendingVoid) {
		this.isS2SPendingVoid = isS2SPendingVoid;
	}
	
	/**
	 * @return the isS2SJackpotProcess
	 */
	public boolean isS2SJackpotProcess() {
		return isS2SJackpotProcess;
	}
	/**
	 * @param isS2SJackpotProcess the isS2SJackpotProcess to set
	 */
	public void setS2SJackpotProcess(boolean isS2SJackpotProcess) {
		this.isS2SJackpotProcess = isS2SJackpotProcess;
	}
	
	/**
	 * Gets cashier desk enabled value
	 * 
	 * @return String
	 */
	public String getCashierDeskEnabled() {
		return cashierDeskEnabled;
	}
	
	/**
	 * Sets cashier desk enabled value
	 * 
	 * @param cashierDeskEnabled
	 */
	public void setCashierDeskEnabled(String cashierDeskEnabled) {
		this.cashierDeskEnabled = cashierDeskEnabled;
	}
	
	/**
	 * Gets the cardless account type
	 * 
	 * @return String
	 */
	public String getCashlessAccountType() {
		return accountType;
	}
	
	/**
	 * Sets cardless Account Type
	 * 
	 * @param accountType
	 */
	public void setCashlessAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * Gets cardless account number
	 * 
	 * @return long
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * Sets cardless account number
	 * 
	 * @param accountNumber
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public Date getSlipRefUpdatedTs() {
		return DateEngineUtil.convertStringToDate(slipRefUpdatedTs);
	}	
	
	public void setSlipRefUpdatedTs(Date slipRefUpdatedTs) {
		this.slipRefUpdatedTs = DateEngineUtil.convertDateToString(slipRefUpdatedTs);
	}
	/**
	 * Method to get if jackpot is posted to accounting
	 * @return
	 * @author vsubha
	 */
	public String getPostToAccounting() {
		return postToAccounting;
	}
	/**
	 * Method to set if jackpot is posted to accounting
	 * @param postToAccounting
	 * @author vsubha
	 */
	public void setPostToAccounting(String postToAccounting) {
		this.postToAccounting = postToAccounting;
	}
	
	/**
	 * Method to get the location from where the jackpot is processed
	 * @return
	 * @author vsubha
	 */
	public String getCashDeskLocation() {
		return cashDeskLocation;
	}
	/**
	 * Method to set the location from where the jackpot is processed
	 * @param cashDeskLocation
	 * @author vsubha
	 */
	public void setCashDeskLocation(String cashDeskLocation) {
		this.cashDeskLocation = cashDeskLocation;
	}
	/**
	 * Method to get the authorization employee Id for jackpot 
	 * exceeding the limit specified in the site config
	 * @return
	 * @author vsubha
	 */
	public String getAmountAuthEmpId() {
		return amountAuthEmpId;
	}
	/**
	 * Method to set the authorization employee Id for jackpot 
	 * exceeding the limit specified in the site config 
	 * @param amountAuthEmpId
	 * @author vsubha
	 */
	public void setAmountAuthEmpId(String amountAuthEmpId) {
		this.amountAuthEmpId = amountAuthEmpId;
	}
	/**
	 * Gets the site param value for printing check for jackpot slip
	 * @return
	 * @author vsubha
	 */
	public String getEnableCheckPrint() {
		return enableCheckPrint;
	}
	/**
	 * Sets the site param value for printing check for jackpot slip
	 * @param enableCheckPrint
	 * @author vsubha
	 */
	public void setEnableCheckPrint(String enableCheckPrint) {
		this.enableCheckPrint = enableCheckPrint;
	}
	/**
	 * Gets the jackpot check expiry date
	 * @return
	 * @author vsubha
	 */
	public String getExpiryDate() {
		return expiryDate;
	}
	/**
	 * Sets the jackpot check expiry date
	 * @param expiryDate
	 * @author vsubha
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = DateEngineUtil.convertDateToString(expiryDate);
	}
	/**
	 * Gets the generate random sequence number site config param value
	 * @return
	 * @author vsubha
	 */
	public String getGenerateRandSeqNum() {
		return generateRandSeqNum;
	}
	/**
	 * Sets the generate random sequence number site config param value
	 * @param generateRandSeqNum
	 * @author vsubha
	 */
	public void setGenerateRandSeqNum(String generateRandSeqNum) {
		this.generateRandSeqNum = generateRandSeqNum;
	}
	
	/**
	 * Gets the generated primary key
	 * @return
	 * @author vsubha
	 */
	public long getSlipPrimaryKey() {
		return slipPrimaryKey;
	}
	/**
	 * Sets the generated primary key
	 * @param slipPrimaryKey
	 * @author vsubha
	 */
	public void setSlipPrimaryKey(long slipPrimaryKey) {
		this.slipPrimaryKey = slipPrimaryKey;
	}
	
	/**
	 * Gets the Jp Hit Date that is in millisecs
	 * @return
	 * @author dambereen
	 */
	public long getPendingAlertTxnDate() {
		return pendingAlertTxnDate;
	}
	
	/**
	 * Sets the Jp Hit Date that is in millisecs
	 * @param pendingAlertTxnDate
	 * @author dambereen
	 */
	public void setPendingAlertTxnDate(long pendingAlertTxnDate) {
		this.pendingAlertTxnDate = pendingAlertTxnDate;
	}
	
	/**
	 * Gets the autoVoidReprintJp flag value
	 * @return the autoVoidReprintedJp
	 * @author dambereen
	 */
	public String getAutoVoidReprintedJp() {
		return autoVoidReprintedJp;
	}
	
	/**
	 * Sets the autoVoidReprintJp flag value
	 * @param autoVoidReprintedJp the autoVoidReprintedJp to set
	 * @author dambereen
	 */
	public void setAutoVoidReprintedJp(String autoVoidReprintedJp) {
		this.autoVoidReprintedJp = autoVoidReprintedJp;
	}
	/**
	 * Gets the list values for Progressive Level
	 * @return
	 * @author vsubha
	 */
	public ArrayList<Integer> getLstProgressiveLevel() {
		return lstProgressiveLevel;
	}
	/**
	 * Sets the list values for Progressive Level
	 * @param lstProgressiveLevel
	 * @author vsubha
	 */
	public void setLstProgressiveLevel(ArrayList<Integer> lstProgressiveLevel) {
		this.lstProgressiveLevel = lstProgressiveLevel;
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
	/**
	 * Returns the progressive Level values for the Manual Progressive Jackpot
	 * @return
	 * @author vsubha
	 */
	public String getProgressiveLevel() {
		return progressiveLevel;
	}

	/**
	 * Sets the progressive Level values for the Manual Progressive Jackpot
	 * @param progressiveLevel
	 * @author vsubha
	 */
	public void setProgressiveLevel(String progressiveLevel) {
		this.progressiveLevel = progressiveLevel;
	}
	/**
	 * @return the isJpProgControllerGenerated
	 */
	public boolean isJpProgControllerGenerated() {
		return isJpProgControllerGenerated;
	}
	/**
	 * @param isJpProgControllerGenerated the isJpProgControllerGenerated to set
	 */
	public void setJpProgControllerGenerated(boolean isJpProgControllerGenerated) {
		this.isJpProgControllerGenerated = isJpProgControllerGenerated;
	}
	/**
	 * @return the taxRateAmount
	 */
	public String getTaxRateAmount() {
		return taxRateAmount;
	}
	/**
	 * @param taxRateAmount the taxRateAmount to set
	 */
	public void setTaxRateAmount(String taxRateAmount) {
		this.taxRateAmount = taxRateAmount;
	}
	
	
}
