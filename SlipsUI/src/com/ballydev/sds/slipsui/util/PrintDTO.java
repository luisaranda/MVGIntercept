/*****************************************************************************
 * $Id: PrintDTO.java,v 1.9, 2011-01-24 09:17:14Z, Ambereen Drewitt$
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
package com.ballydev.sds.slipsui.util;

/**
 * This class contains the datas that need to be send to printer.
 * 
 * @author anantharajr
 * @version $Revision: 10$
 */
public class PrintDTO {

	/**
	 * Field that holds the reason for printing the slip
	 */
	private String reason = null;
	
	/**
	 * Dispute Amount Type - Cashable / Non Cashable
	 */
	private String amountType;
	
//	/**
//	 * Account Type - Cashless/Smartcard
//	 */
//	private String AccountType;
	
	/**
	 * Cashless/Smart Card Account No
	 */
	private String AccountNo;
	
	/**
	 * Seal Number / Asset Regulatory Id
	 */
	private String seal;
		
	/**
	 * Casino name
	 */
	private String casinoName;
		
	/**
	 * Denomination.
	 */
	private String denomination;

	/**
	 * Rounded Hand Paid Amount
	 */
	private double hpjpAmountRounded;

	/**
	 * Jackpot tax amount
	 */
	private double taxAmount;

	/**
	 * Jackpot Net Amount
	 */
	private double jackpotNetAmount;

	/**
	 * Flag that used to check if the data was posted successfully
	 */
	private String printerSchema;

	/**
	 * Field that holds the slip schema
	 */
	private String slipSchema;

	/**
	 * Theme Name instance
	 */
	private String themeName = null;

	/**
	 * Pay Table Id instance
	 */

	private String slotAttendantId = null;

	/**
	 * Card In Date instance
	 */
	private String cardInDate;

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
	private String originalAmount;

	/**
	 * MachinePaid Amount instance
	 */
	private double machinePaidAmount;

	/**
	 * HandPaid Amount instance
	 */
	private double hpjpAmount;

	/**
	 * Pay Line instance
	 */
	private String payline = null;

	/**
	 * Coins Played instance
	 */
	private int coinsPlayed;

	/**
	 * Winning Combination instance
	 */
	private String winningComb = null;

	/**
	 * Actor Login instance
	 */
	private String empID = null;

	/**
	 * assetConfNumberUsed instance
	 */
	private String assetConfNumberUsed = null;

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
	private String authEmpIdOne = null;

	/**
	 * First Authorization Employee Name instance
	 */
	private String authEmpNameOne = null;
	
	/**
	 * Authorization Employee Id2 instance
	 */
	private String AuthEmpIdTwo = null;
	
	/**
	 * Second Authorization Employee Name instance
	 */
	private String authEmpNameTwo = null;
	
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
	 * Denomination for the slot
	 */
	private Double slotDenomination;

	/**
	 * assetConfigNumber instance
	 */
	private String assetConfigNumber;

	/**
	 * Site Id instance
	 */
	private int siteId;

	/**
	 * Sequence Number instance
	 */
	private String sequenceNumber;

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
	private String empName;

	/**
	 * Asset Configuration instance
	 */
	private String assetConfigLocation;

	/**
	 * @return the actorLogin
	 *//*
	public String getActorLogin() {
		return actorLogin;
	}

	*//**
	 * @param actorLogin
	 *            the actorLogin to set
	 *//*
	public void setActorLogin(String actorLogin) {
		this.actorLogin = actorLogin;
	}*/

	/**
	 * @return the assetConfigLocation
	 */
	public String getAssetConfigLocation() {
		return assetConfigLocation;
	}

	/**
	 * @param assetConfigLocation
	 *            the assetConfigLocation to set
	 */
	public void setAssetConfigLocation(String assetConfigLocation) {
		this.assetConfigLocation = assetConfigLocation;
	}

	/**
	 * @return the assetConfigNumber
	 */
	public String getAssetConfigNumber() {
		return assetConfigNumber;
	}

	/**
	 * @param assetConfigNumber
	 *            the assetConfigNumber to set
	 */
	public void setAssetConfigNumber(String assetConfigNumber) {
		this.assetConfigNumber = assetConfigNumber;
	}

	/**
	 * @return the assetConfNumberUsed
	 */
	public String getAssetConfNumberUsed() {
		return assetConfNumberUsed;
	}

	/**
	 * @param assetConfNumberUsed
	 *            the assetConfNumberUsed to set
	 */
	public void setAssetConfNumberUsed(String assetConfNumberUsed) {
		this.assetConfNumberUsed = assetConfNumberUsed;
	}

	/**
	 * @return the associatedPlayerCard
	 */
	public String getAssociatedPlayerCard() {
		return associatedPlayerCard;
	}

	/**
	 * @param associatedPlayerCard
	 *            the associatedPlayerCard to set
	 */
	public void setAssociatedPlayerCard(String associatedPlayerCard) {
		this.associatedPlayerCard = associatedPlayerCard;
	}

	/**
	 * @return the cardInDate
	 */
	public String getCardInDate() {
		return cardInDate;
	}

	/**
	 * @param cardInDate
	 *            the cardInDate to set
	 */
	public void setCardInDate(String cardInDate) {
		this.cardInDate = cardInDate;
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
	 * @return the employeeName
	 */
	/*public String getEmployeeName() {
		return employeeName;
	}

	*//**
	 * @param employeeName
	 *            the employeeName to set
	 *//*
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}*/

	/**
	 * @return the hpjpAmount
	 */
	public double getHpjpAmount() {
		return hpjpAmount;
	}

	/**
	 * @param hpjpAmount
	 *            the hpjpAmount to set
	 */
	public void setHpjpAmount(double hpjpAmount) {
		this.hpjpAmount = hpjpAmount;
	}

	/**
	 * @return the hpjpAmountRounded
	 */
	public double getHpjpAmountRounded() {
		return hpjpAmountRounded;
	}

	/**
	 * @param hpjpAmountRounded
	 *            the hpjpAmountRounded to set
	 */
	public void setHpjpAmountRounded(double hpjpAmountRounded) {
		this.hpjpAmountRounded = hpjpAmountRounded;
	}

	/**
	 * @return the jackpotNetAmount
	 */
	public double getJackpotNetAmount() {
		return jackpotNetAmount;
	}

	/**
	 * @param jackpotNetAmount
	 *            the jackpotNetAmount to set
	 */
	public void setJackpotNetAmount(double jackpotNetAmount) {
		this.jackpotNetAmount = jackpotNetAmount;
	}

	/**
	 * @return the machinePaidAmount
	 */
	public double getMachinePaidAmount() {
		return machinePaidAmount;
	}

	/**
	 * @param machinePaidAmount
	 *            the machinePaidAmount to set
	 */
	public void setMachinePaidAmount(double machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}

	/**
	 * @return the originalAmount
	 */
	public String getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount
	 *            the originalAmount to set
	 */
	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

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
	 * @return the playerCard
	 */
	public String getPlayerCard() {
		return playerCard;
	}

	/**
	 * @param playerCard
	 *            the playerCard to set
	 */
	public void setPlayerCard(String playerCard) {
		this.playerCard = playerCard;
	}

	/**
	 * @return the playerFirstName
	 */
	public String getPlayerFirstName() {
		return playerFirstName;
	}

	/**
	 * @param playerFirstName
	 *            the playerFirstName to set
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
	 * @param playerLastName
	 *            the playerLastName to set
	 */
	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}

	/**
	 * @return the printDate
	 */
	public String getPrintDate() {
		return printDate;
	}

	/**
	 * @param printDate
	 *            the printDate to set
	 */
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	/**
	 * @return the printEmployeeLogin
	 */
	public String getPrintEmployeeLogin() {
		return printEmployeeLogin;
	}

	/**
	 * @param printEmployeeLogin
	 *            the printEmployeeLogin to set
	 */
	public void setPrintEmployeeLogin(String printEmployeeLogin) {
		this.printEmployeeLogin = printEmployeeLogin;
	}

	/**
	 * @return the printerSchema
	 */
	public String getPrinterSchema() {
		return printerSchema;
	}

	/**
	 * @param printerSchema
	 *            the printerSchema to set
	 */
	public void setPrinterSchema(String printerSchema) {
		this.printerSchema = printerSchema;
	}

	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	 * @return the siteId
	 */
	public int getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the slipSchema
	 */
	public String getSlipSchema() {
		return slipSchema;
	}

	/**
	 * @param slipSchema
	 *            the slipSchema to set
	 */
	public void setSlipSchema(String slipSchema) {
		this.slipSchema = slipSchema;
	}

	/**
	 * @return the slotAttendantId
	 */
	public String getSlotAttendantId() {
		return slotAttendantId;
	}

	/**
	 * @param slotAttendantId
	 *            the slotAttendantId to set
	 */
	public void setSlotAttendantId(String slotAttendantId) {
		this.slotAttendantId = slotAttendantId;
	}

	/**
	 * @return the slotDenomination
	 */
	public Double getSlotDenomination() {
		return slotDenomination;
	}

	/**
	 * @param slotDenomination
	 *            the slotDenomination to set
	 */
	public void setSlotDenomination(Double slotDenomination) {
		this.slotDenomination = slotDenomination;
	}

	/**
	 * @return the taxAmount
	 */
	public double getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount
	 *            the taxAmount to set
	 */
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	/**
	 * @return the themeName
	 */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * @param themeName
	 *            the themeName to set
	 */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the voidEmployeeLogin
	 */
	public String getVoidEmployeeLogin() {
		return voidEmployeeLogin;
	}

	/**
	 * @param voidEmployeeLogin
	 *            the voidEmployeeLogin to set
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
	 * @param windowNumber
	 *            the windowNumber to set
	 */
	public void setWindowNumber(String windowNumber) {
		this.windowNumber = windowNumber;
	}

	/**
	 * @return the winningComb
	 */
	public String getWinningComb() {
		return winningComb;
	}

	/**
	 * @param winningComb
	 *            the winningComb to set
	 */
	public void setWinningComb(String winningComb) {
		this.winningComb = winningComb;
	}

	/**
	 * @return the denomination
	 */
	public String getDenomination() {
		return denomination;
	}

	/**
	 * @param denomination
	 *            the denomination to set
	 */
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	/**
	 * @return the casinoName
	 */
	public String getCasinoName() {
		return casinoName;
	}

	/**
	 * @param casinoName the casinoName to set
	 */
	public void setCasinoName(String casinoName) {
		this.casinoName = casinoName;
	}

	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * @return the authEmpIdOne
	 */
	public String getAuthEmpIdOne() {
		return authEmpIdOne;
	}

	/**
	 * @param authEmpIdOne the authEmpIdOne to set
	 */
	public void setAuthEmpIdOne(String authEmpIdOne) {
		this.authEmpIdOne = authEmpIdOne;
	}

	/**
	 * @return the authEmpIdTwo
	 */
	public String getAuthEmpIdTwo() {
		return AuthEmpIdTwo;
	}

	/**
	 * @param authEmpIdTwo the authEmpIdTwo to set
	 */
	public void setAuthEmpIdTwo(String authEmpIdTwo) {
		AuthEmpIdTwo = authEmpIdTwo;
	}

	/**
	 * @return the authEmpNameOne
	 */
	public String getAuthEmpNameOne() {
		return authEmpNameOne;
	}

	/**
	 * @param authEmpNameOne the authEmpNameOne to set
	 */
	public void setAuthEmpNameOne(String authEmpNameOne) {
		this.authEmpNameOne = authEmpNameOne;
	}

	/**
	 * @return the authEmpNameTwo
	 */
	public String getAuthEmpNameTwo() {
		return authEmpNameTwo;
	}

	/**
	 * @param authEmpNameTwo the authEmpNameTwo to set
	 */
	public void setAuthEmpNameTwo(String authEmpNameTwo) {
		this.authEmpNameTwo = authEmpNameTwo;
	}

	/**
	 * @return the seal
	 */
	public String getSeal() {
		return seal;
	}

	/**
	 * @param seal the seal to set
	 */
	public void setSeal(String seal) {
		this.seal = seal;
	}

//	/**
//	 * @return the accountType
//	 */
//	public String getAccountType() {
//		return AccountType;
//	}
//
//	/**
//	 * @param accountType the accountType to set
//	 */
//	public void setAccountType(String accountType) {
//		AccountType = accountType;
//	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return AccountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}

	/**
	 * @return the amountType
	 */
	public String getAmountType() {
		return amountType;
	}

	/**
	 * @param amountType the amountType to set
	 */
	public void setAmountType(String amountType) {
		this.amountType = amountType;
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


}
