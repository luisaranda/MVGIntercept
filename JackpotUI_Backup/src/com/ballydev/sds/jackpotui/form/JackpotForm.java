/*****************************************************************************
 * $Id: JackpotForm.java,v 1.26.1.2.3.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 4:01:12 AM$
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
package com.ballydev.sds.jackpotui.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This form holds changed adjusted slip record details inorder to process the
 * jackpot
 * 
 * @author anantharajr
 * @version $Revision: 31$
 */
public class JackpotForm {
	
	/**
	 * Field that would contain the filter field value that is set the JackpotFilter
	 * in order to display the pending jp slips based on the slot / slot loc/ seq/ minutes
	 */
	private String pendingDisplayFilter;
	
	/**
	 * Boolean Flag used to show the Pouch Pay Slot Attendant id text  
	 */
	private boolean showSlotAttnId;
	
	/**
	 * Seal Number / Asset Regulatory Id
	 */
	private String sealNumber;
		
	/**
	 * Falg to check if the barcode should be printed or not
	 */
	private boolean isPrintBarcode = false;
	
	/**
	 * Field that holds Y if a progressive slot is being used else N
	 */
	private String progressiveSlot;
	
	/**
	 * List of slot nos
	 */
	private List<String> lstSlotNo;
	
	/**
	 * Flag to insert the pouch pay attendant if its a non carded jackpot
	 */
	private boolean insertPouchPayAttn=false;
	
	/**
	 * Slot Attentant Last Name Instance
	 */
	private String slotAttentantLastName;
	
	/**
	 * Slot Attentant First Name Instance
	 */
	private String slotAttentantFirstName;
	
	/**
	 * Currency Symbol for Amount fields
	 */
	private String siteCurrencySymbol = "$";
	
	/**
	 * Area short name
	 */
	private String area;
	
	/**
	 * The denomination of the game played.
	 */
	private long gmuDenom;
	
	/**
	 * Field contains the jackpot process flag id
	 */
	private short processFlagId;
	
	/**
	 * Field contains the jackpot type id
	 */
	private short jackpotTypeId;
	/**
	 * Field contains the Host Name of the touch screen terminal used
	 */
	private String assetConfigNumberUsed; 
	
	/**
	 * Actor login id
	 */
	private String actorLogin;
	
	/**
	 * void employee id
	 */
	private String voidEmployeeId;
	
	
	/**
	 * Field holds the employee cardId 
	 */
	private String cardId;
	
	/**
	 * Field that holds the Site's Long Name
	 */
	private String siteLongName;	
	
	/**
	 * Field that holds the Site No
	 */
	private String siteNo;	
	
	/**
	 * Field that holds the Site id
	 */
	private int siteId;
	/**
	 * boolean field that holds a flag if the either the pending or manual
	 * jackpot process hass started- used for switching from a either processes
	 */
	private boolean processStartedFlag;

	/**
	 * Flag that holds the value if it is a promotional jackpot or not (Used for the manual jp process)
	 */
	private boolean isPromotionalJackpot = false;
	/**
	 * Field holds the revenue flag id value
	 */
	private short revenueFlagId;
	/**
	 * Type of jackpot tax(No Tax, State tax,  Federal tax or State + Federal Tax)
	 */
	private String taxType;
	/**
	 * Type of jackpot (Promotional, Normal,  Progressive or Cancelled credits)
	 */
	private String jackpotType;
	/**
	 * Sequence No or All - Used for displaying all sequence nos
	 */
	private String sequenceNoOrAll;
	/**
	 * Transaction Date / Event Date
	 */
	private long transactionDate;
	/**
	 * Slot Denomination
	 */
	private String denomination;
	/**
	 * Pay Table Id
	 */
	private int payTableId;
	/**
	 * Theme name for a Slot Machine
	 */
	private String themeName;
	/**
	 * This field holds the selected sequence number to process the jackpot
	 */
	private long sequenceNumber;
	/**
	 * This field holds the adjusted hand paid amount
	 */
	private long handPaidAmount;
	/**
	 * This field holds the adjusted machine paid amount
	 */
	//private long machinePaidAmount;		
	/**
	 * This field holds the Original jackpot Amount
	 */
	private long originalAmount;	
	/**
	 * This field holds the employee Id Printed Slip
	 */
	private  String employeeIdPrintedSlip;
	/**
	 * This field holds the user entered winning combination
	 */
	private String winningCombination;
	/**
	 * This field holds the entered coins played count
	 */
	private int coinsPlayed;
	/**
	 * This field stores entered payline
	 */
	private String payline;
	/**
	 * This field holds the entered windown number
	 */
	private String windowNumber;
	/**
	 * This field holds the selected shift
	 */
	private String shift;
	
	/**
	 * This field holds the selected middlecontrol
	 */
	private String middleControl;
	
	/**
	 * This field holds the blind attempts for an express jackpot
	 */
	private short blindAttempts;	
	/**
	 * This field holds the slot no entered
	 */
	private String slotNo;
	/**
	 * This field holds the slot location no entered
	 */
	private String slotLocationNo;
	/**
	 * This field holds the jackpot id
	 */
	private String jackpotID;
	/**
	 * This field holds the tax id
	 */
	private short taxID;
	/**
	 * Slot Attentant Name Instance
	 */
	private String slotAttentantName;
	/**
	 * Slot Attentant Id Instance
	 */
	private String slotAttentantId;
	
	private String amountAuthEmpId;
	/**
	 * associatedPlayerCard Instance
	 */
	private String associatedPlayerCard;
	/**
	 * player Card Instance
	 */
	private String playerCard;
	/**
	 * Player Name Instance
	 */
	private String playerName;
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
	
	/**
	 * authrization required flag set in runtime.
	 */
	private Boolean authorizationRequired;

	/**
	 *  process as EXP JP flag set in runtime. 
	 */
	private boolean processAsExpress;

	/**
	 *  express JP success or failure flag set in runtime.
	 */
	private boolean expressSuccess = false;

	/**
	 * Pouch Pay flag set in runtime. 
	 */
	private boolean pouchPay;
	
	/**
	 * no of minutes for the last pending slips. 
	 */
	private int numOfMinutes;
	
	/**
	 * Tax deducted Amount.
	 */
	private long taxDeductedAmount;
	
	/**
	 * Tax String for holding individual tax rates and tax amounts
	 */
	private String taxRateAmount;
	
	
	/**
	 * JP Net Amount , Amount after tax deductions.
	 */
	private long jackpotNetAmount;
	
	/**
	 *  The rounded JP adjusted amount if rounding is on.
	 */
	private long roundedHPJPAmount;
		
	/**
	 * messageId field
	 */
	private long messageId;
	
	
	private Long sdsCalculatedAmount = 0L;
	
	/**
	 * Cashless Account Number
	 */
	private String accountNumber;	
	
	/**
	 * Cashless Account type
	 */
	private String accountType;
	
	/**
	 * Jackpot check expiry date
	 */
	private Date expiryDate;
	
	private ArrayList<Integer> lstProgressiveLevel;
	
	private String progressiveLevel;
	
	/**
	 * Field that determines whether to show the PENDING JP HPJP AMT AS MASKED OR NOT
	 */
	private boolean isDisplayMaskAmt=true;
	
	/**
	 * Field to determine if the Authorizer should be asked for a Pending Jp
	 * If the Process Employee exceedes the Emp Job Limit
	 */
	private boolean isEmpJobJpLimitExceededAuthReq=false;
	
	/**
	 * Flag to check if the jackpot was generated externally by the progressive controller
	 */
	private boolean isJpProgControllerGenerated = false;

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
	
	/**
	 * @return
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	 * @return the coinsPlayed
	 */
	public int getCoinsPlayed() {
		return coinsPlayed;
	}

	/**
	 * @param coinsPlayed
	 *            the coinsPlayed to set
	 */
	public void setCoinsPlayed(int coinsPlayed) {
		this.coinsPlayed = coinsPlayed;
	}

	/**
	 * @return the handPaidAmount
	 */
	public long getHandPaidAmount() {
		return handPaidAmount;
	}

	/**
	 * @param handPaidAmount
	 *            the handPaidAmount to set
	 */
	public void setHandPaidAmount(long handPaidAmount) {
		this.handPaidAmount = handPaidAmount;
	}

	/**
	 * @return the machinePaidAmount
	 */
	/*public long getMachinePaidAmount() {
		return machinePaidAmount;
	}

	*//**
	 * @param machinePaidAmount
	 *            the machinePaidAmount to set
	 *//*
	public void setMachinePaidAmount(long machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}*/

	/**
	 * @return the payline
	 */
	public String getPayline() {
		return payline;
	}

	/**
	 * @param payline
	 *            the payline to set
	 */
	public void setPayline(String payline) {
		this.payline = payline;
	}

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
	 * @return the winningCombination
	 */
	public String getWinningCombination() {
		return winningCombination;
	}

	/**
	 * @param winningCombination
	 *            the winningCombination to set
	 */
	public void setWinningCombination(String winningCombination) {
		this.winningCombination = winningCombination;
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
	 * @return the blindAttempts
	 */
	public short getBlindAttempts() {
		return blindAttempts;
	}

	/**
	 * @param blindAttempts the blindAttempts to set
	 */
	public void setBlindAttempts(short blindAttempts) {
		this.blindAttempts = blindAttempts;
	}

	/**
	 * @return the originalAmount
	 */
	public long getOriginalAmount() {
		return originalAmount;
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
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(long originalAmount) {
		this.originalAmount = originalAmount;
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
	 * @return the jackpotID
	 */
	public String getJackpotID() {
		return jackpotID;
	}

	/**
	 * @param jackpotID the jackpotID to set
	 */
	public void setJackpotID(String jackpotID) {
		this.jackpotID = jackpotID;
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
	 * @return the taxID
	 */
	public short getTaxID() {
		return taxID;
	}

	/**
	 * @param taxID the taxID to set
	 */
	public void setTaxID(short taxID) {
		this.taxID = taxID;
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
	public void setSelectedJackpotFunction(String selectedJackpotFunction) {
		this.selectedJackpotFunction = selectedJackpotFunction;
	}

	/**
	 * @return the themename
	 */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * @param themename the themename to set
	 */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * @return the paytableId
	 */
	public int getPayTableId() {
		return payTableId;
	}

	/**
	 * @param paytableId the paytableId to set
	 */
	public void setPayTableId(int payTableId) {
		this.payTableId = payTableId;
	}

	/**
	 * @return the denomination
	 */
	public String getDenomination() {
		return denomination;
	}

	/**
	 * @param denomination the denomination to set
	 */
	public void setDenomination(String denomination) {
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
	 * @return the processStartedFlag
	 */
	public boolean isProcessStartedFlag() {
		return processStartedFlag;
	}

	/**
	 * @param processStartedFlag
	 *            the processStartedFlag to set
	 */
	public void setProcessStartedFlag(boolean processStartedFlag) {
		this.processStartedFlag = processStartedFlag;
	}
	/**
	 * @return the jackpotType
	 */
	public String getJackpotType() {
		return jackpotType;
	}

	/**
	 * @param jackpotType the jackpotType to set
	 */
	public void setJackpotType(String jackpotType) {
		this.jackpotType = jackpotType;
	}

	/**
	 * @return the taxType
	 */
	public String getTaxType() {
		return taxType;
	}

	/**
	 * @param taxType the taxType to set
	 */
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	/**
	 * @return the revenueFlagId
	 */
	public short getRevenueFlagId() {
		return revenueFlagId;
	}

	/**
	 * @param revenueFlagId the revenueFlagId to set
	 */
	public void setRevenueFlagId(short revenueFlagId) {
		this.revenueFlagId = revenueFlagId;
	}

	/**
	 * @return the isPromotionalJackpot
	 */
	public boolean isPromotionalJackpot() {
		return isPromotionalJackpot;
	}

	/**
	 * @param isPromotionalJackpot the isPromotionalJackpot to set
	 */
	public void setPromotionalJackpot(boolean isPromotionalJackpot) {
		this.isPromotionalJackpot = isPromotionalJackpot;
	}
	/**
	 * @return the authorizationRequired
	 */
	public Boolean getAuthorizationRequired() {
		return authorizationRequired;
	}

	/**
	 * @param authorizationRequired
	 *            the authorizationRequired to set
	 */
	public void setAuthorizationRequired(Boolean authorizationRequired) {
		this.authorizationRequired = authorizationRequired;
	}

	/**
	 * @return the expressSuccess
	 */
	public boolean isExpressSuccess() {
		return expressSuccess;
	}

	/**
	 * @param expressSuccess
	 *            the expressSuccess to set
	 */
	public void setExpressSuccess(boolean expressSuccess) {
		this.expressSuccess = expressSuccess;
	}

	/**
	 * @return the pouchPay
	 */
	public boolean isPouchPay() {
		return pouchPay;
	}
	
	/**
	 * @param pouchPay
	 *            the pouchPay to set
	 */
	public void setPouchPay(boolean pouchPay) {
		this.pouchPay = pouchPay;
	}

	/**
	 * @return the processAsExpress
	 */
	public boolean isProcessAsExpress() {
		return processAsExpress;
	}

	/**
	 * @param processAsExpress
	 *            the processAsExpress to set
	 */
	public void setProcessAsExpress(boolean processAsExpress) {
		this.processAsExpress = processAsExpress;
	}

	/**
	 * @return the numOfMinutes
	 */
	public int getNumOfMinutes() {
		return numOfMinutes;
	}

	/**
	 * @param numOfMinutes the numOfMinutes to set
	 */
	public void setNumOfMinutes(int numOfMinutes) {
		this.numOfMinutes = numOfMinutes;
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
	 * @return the taxDeductedAmount
	 */
	public long getTaxDeductedAmount() {
		return taxDeductedAmount;
	}

	/**
	 * @param taxDeductedAmount the taxDeductedAmount to set
	 */
	public void setTaxDeductedAmount(long taxDeductedAmount) {
		this.taxDeductedAmount = taxDeductedAmount;
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
	 * @return the roundedHPJPAmount
	 */
	public long getRoundedHPJPAmount() {
		return roundedHPJPAmount;
	}

	/**
	 * @param roundedHPJPAmount the roundedHPJPAmount to set
	 */
	public void setRoundedHPJPAmount(long roundedHPJPAmount) {
		this.roundedHPJPAmount = roundedHPJPAmount;
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
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
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
	 * @return the slotAttentantFirstName
	 */
	public String getSlotAttentantFirstName() {
		return slotAttentantFirstName;
	}

	/**
	 * @param slotAttentantFirstName the slotAttentantFirstName to set
	 */
	public void setSlotAttentantFirstName(String slotAttentantFirstName) {
		this.slotAttentantFirstName = slotAttentantFirstName;
	}

	/**
	 * @return the slotAttentantLastName
	 */
	public String getSlotAttentantLastName() {
		return slotAttentantLastName;
	}

	/**
	 * @param slotAttentantLastName the slotAttentantLastName to set
	 */
	public void setSlotAttentantLastName(String slotAttentantLastName) {
		this.slotAttentantLastName = slotAttentantLastName;
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
	 * @return the lstSlotNo
	 */
	public List<String> getLstSlotNo() {
		return lstSlotNo;
	}

	/**
	 * @param lstSlotNo the lstSlotNo to set
	 */
	public void setLstSlotNo(List<String> lstSlotNo) {
		this.lstSlotNo = lstSlotNo;
	}

	/**
	 * @return the progressiveSlot
	 */
	public String getProgressiveSlot() {
		return progressiveSlot;
	}

	/**
	 * @param progressiveSlot the progressiveSlot to set
	 */
	public void setProgressiveSlot(String progressiveSlot) {
		this.progressiveSlot = progressiveSlot;
	}

	public boolean isPrintBarcode() {
		return isPrintBarcode;
	}

	public void setPrintBarcode(boolean isPrintBarcode) {
		this.isPrintBarcode = isPrintBarcode;
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

	public Long getSdsCalculatedAmount() {
		return sdsCalculatedAmount;
	}

	public void setSdsCalculatedAmount(Long sdsCalculatedAmount) {
		this.sdsCalculatedAmount = sdsCalculatedAmount;
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
	 * @return the showSlotAttnId
	 */
	public boolean isShowSlotAttnId() {
		return showSlotAttnId;
	}

	/**
	 * @param showSlotAttnId the showSlotAttnId to set
	 */
	public void setShowSlotAttnId(boolean showSlotAttnId) {
		this.showSlotAttnId = showSlotAttnId;
	}

	/**
	 * @return the pendingDisplayFilter
	 */
	public String getPendingDisplayFilter() {
		return pendingDisplayFilter;
	}

	/**
	 * @param pendingDisplayFilter the pendingDisplayFilter to set
	 */
	public void setPendingDisplayFilter(String pendingDisplayFilter) {
		this.pendingDisplayFilter = pendingDisplayFilter;
	}

	public String getMiddleControl() {
		return middleControl;
	}

	public void setMiddleControl(String middleControl) {
		this.middleControl = middleControl;
	}

	/**
	 * Gets the override amount authoriser Id
	 * @return
	 * @author vsubha
	 */
	public String getAmountAuthEmpId() {
		return amountAuthEmpId;
	}

	/**
	 * Sets the override amount authoriser Id
	 * @param amountAuthEmpId
	 * @author vsubha
	 */
	public void setAmountAuthEmpId(String amountAuthEmpId) {
		this.amountAuthEmpId = amountAuthEmpId;
	}

	/**
	 * Gets the cahsless account type
	 * @return
	 * @author vsubha
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * Sets the cashless account type
	 * @param accountType
	 * @author vsubha
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * Gets the jackpot check expiry date
	 * @return
	 * @author vsubha
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the jackpot check epiry date
	 * @param expiryDate
	 * @author vsubha
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public ArrayList<Integer> getLstProgressiveLevel() {
		return lstProgressiveLevel;
	}

	public void setLstProgressiveLevel(ArrayList<Integer> lstProgressiveLevel) {
		this.lstProgressiveLevel = lstProgressiveLevel;
	}

	public String getProgressiveLevel() {
		return progressiveLevel;
	}

	public void setProgressiveLevel(String progressiveLevel) {
		this.progressiveLevel = progressiveLevel;
	}

	public boolean isDisplayMaskAmt() {
		return isDisplayMaskAmt;
	}

	public void setDisplayMaskAmt(boolean isDisplayStaticHPJPAmt) {
		this.isDisplayMaskAmt = isDisplayStaticHPJPAmt;
	}

	/**
	 * @return the isEmpJobJpLimitExceededAuthReq
	 */
	public boolean isEmpJobJpLimitExceededAuthReq() {
		return isEmpJobJpLimitExceededAuthReq;
	}

	/**
	 * @param isEmpJobJpLimitExceededAuthReq the isEmpJpJobOverrideAuthReq to set
	 */
	public void setEmpJobJpLimitExceededAuthReq(boolean isEmpJobJpLimitExceededAuthReq) {
		this.isEmpJobJpLimitExceededAuthReq = isEmpJobJpLimitExceededAuthReq;
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

	public String getTaxRateAmount() {
		return taxRateAmount;
	}

	public void setTaxRateAmount(String taxRateAmount) {
		this.taxRateAmount = taxRateAmount;
	}

}