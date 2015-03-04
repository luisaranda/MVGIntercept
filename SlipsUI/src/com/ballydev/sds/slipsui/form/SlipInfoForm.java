/*****************************************************************************
 * $Id: SlipInfoForm.java,v 1.8, 2011-01-24 09:17:14Z, Ambereen Drewitt$
 * $Date: 1/24/2011 3:17:14 AM$
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
package com.ballydev.sds.slipsui.form;

/**
 * This form holds adjusted slip record details inorder to process the
 * slip
 * 
 * @author anantharajr
 * @version $Revision: 9$
 */
public class SlipInfoForm {
		
	/**
	 * Field that holds the reason for printing the slip
	 */
	private String reason = null;
	
//	/**
//	 * Flag to determine if it is a Cashable / Non Cashable Dispute
//	 */
//	private boolean isCashableAmount = true;
//	
//	/**
//	 * Cardless Account Type
//	 */
//	private String cashlessAccountType = null;
	
	/**
	 * Cardless Account Number
	 */
	private String cashlessAccountNumber;
	
	/**
	 * Currency Symbol for Amount fields
	 */
	private String siteCurrencySymbol = "$";
	
	/**
	 * Field contains the Host Name of the touch screen terminal used
	 */
	private String assetConfigNumberUsed; 
	/**
	 * Actor Login 
	 */
	private String actorLogin;
	/**
	 * Area short name; 
	 */
	private String area;	
	/**
	 * boolean field that holds a flag if the either the pending or manual
	 * fill process has started - used for switching from either processes
	 */
	private boolean processStartedFlag;
	
	/**
	 * Field that holds the Site's Long Name
	 */
	private String siteLongName;	
	
	/**
	 * Site No
	 */
	private String siteNo; 
	
	/**
	 * Site id
	 */
	private int siteId; 
	/**
	 * Sequence No or All - Used for displaying all sequence nos
	 */
	private String sequenceNoOrAll;
	/**
	 * Asset Configuration Id instance
	 */
	private byte[] assetConfigurationId; 
	/**
	 * Transaction Date / Event Date
	 */
	private long transactionDate;
	/**
	 * The denomination of the game played.
	 */
	private double denomination;
	/**
	 * This field holds the selected sequence number to process the jackpot
	 */
	private long sequenceNumber;	
	/**
	 * This field holds the Original jackpot Amount
	 */
	private long slipAmount;	
	/**
	 * This field holds the employee Id Printed Slip
	 */
	private  String employeeIdPrintedSlip;		
	/**
	 * This field holds the entered windown number
	 */
	private String windowNumber;
	/**
	 * This field holds the selected shift
	 */
	private String shift;	
	/**
	 * This field holds the slot no entered
	 */
	private String slotNo;
	/**
	 * This field holds the slot location no entered
	 */
	private String slotLocationNo;
	/**
	 * Slot Attentant Name Instance
	 */
	private String slotAttentantName;
	/**
	 * Slot Attentant Id Instance
	 */
	private String slotAttentantId;
	/**
	 * First authEmployeeId Instance
	 */
	private String authEmployeeIdOne;
	/**
	 * Second authEmployeeId Instance
	 */
	private String authEmployeeIdTwo;	
	/**
	 * The Selected Jackpot Function Instance, can be Pending / Manual
	 */
	private String selectedJackpotFunction;
	

	private int NumOfMinutes;
	
	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the windowNumber
	 */
	public String getWindowNumber() {
		return windowNumber;
	}

	/**
	 * @param windowNumber
	 *            the windowNumber to set
	 */
	public void setWindowNumber(String windowNumber) {
		this.windowNumber = windowNumber;
	}

	/**
	 * @return the shift
	 */
	public String getShift() {
		return shift;
	}

	/**
	 * @param shift
	 *            the shift to set
	 */
	public void setShift(String shift) {
		this.shift = shift;
	}

	/**
	 * @return the slotLocationNo
	 */
	public String getSlotLocationNo() {
		return slotLocationNo;
	}

	/**
	 * @param slotLocationNo the slotLocationNo to set
	 */
	public void setSlotLocationNo(String slotLocationNo) {
		this.slotLocationNo = slotLocationNo;
	}

	/**
	 * @return the slotNo
	 */
	public String getSlotNo() {
		return slotNo;
	}

	/**
	 * @param slotNo the slotNo to set
	 */
	public void setSlotNo(String slotNo) {
		this.slotNo = slotNo;
	}

	/**
	 * @return the employeeIdPrintedSlip
	 */
	public String getEmployeeIdPrintedSlip() {
		return employeeIdPrintedSlip;
	}

	/**
	 * @param employeeIdPrintedSlip the employeeIdPrintedSlip to set
	 */
	public void setEmployeeIdPrintedSlip(String employeeIdPrintedSlip) {
		this.employeeIdPrintedSlip = employeeIdPrintedSlip;
	}
	
	/**
	 * @return the slotAttentantName
	 */
	public String getSlotAttentantName() {
		return slotAttentantName;
	}

	/**
	 * @param slotAttentantName the slotAttentantName to set
	 */
	public void setSlotAttentantName(String slotAttentantName) {
		this.slotAttentantName = slotAttentantName;
	}

	/**
	 * @return the slotAttentantId
	 */
	public String getSlotAttentantId() {
		return slotAttentantId;
	}

	/**
	 * @param slotAttentantId the slotAttentantId to set
	 */
	public void setSlotAttentantId(String slotAttentantId) {
		this.slotAttentantId = slotAttentantId;
	}

	/**
	 * @return the authEmployeeIdOne
	 */
	public String getAuthEmployeeIdOne() {
		return authEmployeeIdOne;
	}

	/**
	 * @param authEmployeeIdOne the authEmployeeIdOne to set
	 */
	public void setAuthEmployeeIdOne(String authEmployeeIdOne) {
		this.authEmployeeIdOne = authEmployeeIdOne;
	}

	/**
	 * @return the authEmployeeIdTwo
	 */
	public String getAuthEmployeeIdTwo() {
		return authEmployeeIdTwo;
	}

	/**
	 * @param authEmployeeIdTwo the authEmployeeIdTwo to set
	 */
	public void setAuthEmployeeIdTwo(String authEmployeeIdTwo) {
		this.authEmployeeIdTwo = authEmployeeIdTwo;
	}
	/**
	 * @return the selectedJackpotFunction
	 */
	public String getSelectedJackpotFunction() {
		return selectedJackpotFunction;
	}

	/**
	 * @param selectedJackpotFunction the selectedJackpotFunction to set
	 */
	public void setSelectedSlipFunction(String selectedJackpotFunction) {
		this.selectedJackpotFunction = selectedJackpotFunction;
	}

	/**
	 * @return the denomination
	 */
	public double getDenomination() {
		return denomination;
	}

	/**
	 * @param denomination the denomination to set
	 */
	public void setDenomination(double denomination) {
		this.denomination = denomination;
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
	 * @return the assetConfigurationId
	 */
	public byte[] getAssetConfigurationId() {
		return assetConfigurationId;
	}

	/**
	 * @param assetConfigurationId the assetConfigurationId to set
	 */
	public void setAssetConfigurationId(byte[] assetConfigurationId) {
		this.assetConfigurationId = assetConfigurationId;
	}
	/**
	 * @return the numOfMinutes
	 */
	public int getNumOfMinutes() {
		return NumOfMinutes;
	}

	/**
	 * @param numOfMinutes the numOfMinutes to set
	 */
	public void setNumOfMinutes(int numOfMinutes) {
		NumOfMinutes = numOfMinutes;
	}

	/**
	 * @return the sequenceNoOrAll
	 */
	public String getSequenceNoOrAll() {
		return sequenceNoOrAll;
	}

	/**
	 * @param sequenceNoOrAll the sequenceNoOrAll to set
	 */
	public void setSequenceNoOrAll(String sequenceNoOrAll) {
		this.sequenceNoOrAll = sequenceNoOrAll;
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
	 * @return the processStartedFlag
	 */
	public boolean isProcessStartedFlag() {
		return processStartedFlag;
	}

	/**
	 * @param processStartedFlag the processStartedFlag to set
	 */
	public void setProcessStartedFlag(boolean processStartedFlag) {
		this.processStartedFlag = processStartedFlag;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the slipAmount
	 */
	public long getSlipAmount() {
		return slipAmount;
	}

	/**
	 * @param slipAmount the slipAmount to set
	 */
	public void setSlipAmount(long slipAmount) {
		this.slipAmount = slipAmount;
	}

	/**
	 * @return the actorLogin
	 */
	public String getActorLogin() {
		return actorLogin;
	}

	/**
	 * @param actorLogin the actorLogin to set
	 */
	public void setActorLogin(String actorLogin) {
		this.actorLogin = actorLogin;
	}

	/**
	 * @return the assetConfigNumberUsed
	 */
	public String getAssetConfigNumberUsed() {
		return assetConfigNumberUsed;
	}

	/**
	 * @param assetConfigNumberUsed the assetConfigNumberUsed to set
	 */
	public void setAssetConfigNumberUsed(String assetConfigNumberUsed) {
		this.assetConfigNumberUsed = assetConfigNumberUsed;
	}

	/**
	 * @return the siteCurrencySymbol
	 */
	public String getSiteCurrencySymbol() {
		return siteCurrencySymbol;
	}

	/**
	 * @param siteCurrencySymbol the siteCurrencySymbol to set
	 */
	public void setSiteCurrencySymbol(String siteCurrencySymbol) {
		this.siteCurrencySymbol = siteCurrencySymbol;
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

	/**
	 * @return the siteLongName
	 */
	public String getSiteLongName() {
		return siteLongName;
	}

	/**
	 * @param siteLongName the siteLongName to set
	 */
	public void setSiteLongName(String siteLongName) {
		this.siteLongName = siteLongName;
	}

//	/**
//	 * @return the cashlessAccountType
//	 */
//	public String getCashlessAccountType() {
//		return cashlessAccountType;
//	}
//
//	/**
//	 * @param cashlessAccountType the cashlessAccountType to set
//	 */
//	public void setCashlessAccountType(String cashlessAccountType) {
//		this.cashlessAccountType = cashlessAccountType;
//	}

	/**
	 * @return the cashlessAccountNumber
	 */
	public String getCashlessAccountNumber() {
		return cashlessAccountNumber;
	}

	/**
	 * @param cashlessAccountNumber the cashlessAccountNumber to set
	 */
	public void setCashlessAccountNumber(String cashlessAccountNumber) {
		this.cashlessAccountNumber = cashlessAccountNumber;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

//	/**
//	 * @return the isCashableAmount
//	 */
//	public boolean isCashableAmount() {
//		return isCashableAmount;
//	}
//
//	/**
//	 * @param isCashableAmount the isCashableAmount to set
//	 */
//	public void setCashableAmount(boolean isCashableAmount) {
//		this.isCashableAmount = isCashableAmount;
//	}

}
