package com.ballydev.sds.jackpot.keypad;





/**
 * Object Class to be sent to printing.
 * 
 * @author ranjithkumarm
 *
 */
public class PrintDTO {	
		
	/**
	 * Cashier
	 */
	private String cashier;
	
	/**
	 * ActualAmount HPJP amount without rounding
	 */
	private String actualAmount;
	
	/**
	 * Emp NAME who processes the slip
	 */
	private String processEmpName;
	
	/**
	 * Emp ID who processes the slip
	 */
	private String processEmpId;
	
	/**
	 * Description of the type of Jackpot
	 */
	private String jpDescription;
	
	/**
	 * Slip Barcode
	 */
	private String barcode;
	
	/**
	 * From slot asset file - some gaming jurisdictions place an adhesive paper seal with a
	 * serial number on it over the GMU chip to ensure the chip is not changed without proper authorization
	 */
	private String seal;
	
	/**
	 * Terminal where the slip was printed
	 */
	private String pKeyboard;
	
	/**
	 * Second Authourization Emp Name
	 */
	private String authEmpNameTwo;	
	
	/**
	 * First Authourization Emp Name
	 */
	private String authEmpNameOne;	
	
	/**
	 * Second Authourization Emp ID
	 */
	private String authEmpIdTwo;	
	
	/**
	 * First Authourization Emp ID
	 */
	private String authEmpIdOne;	
	
	/**
	 * Area of Slot
	 */
	private String area;
	
	/**
	 * Accounting Period / Gaming Day
	 */
	private String accountPeriod;
	
	/**
	 * The Kiosk / Terminal that was used to process the Jackpot Slip
	 */
	private String keyboard;
	
	/**
	 * Jackpot tax amount used in the Jackpot Slip
	 */
	private String jackpotTaxAmount;
	
	/**
	 * Slot Attendant Name
	 */
	private String slotAttendantName;
	
	/**
	 * Jackpot Type
	 */
	private String jackpotType;
	
	/**
	 * Casino name
	 */
	private String casinoName;
	
	/**
	 * Player name field
	 */
	private String playerName;
	/**
	 * Jackpot Net Amount
	 */
	private String jackpotNetAmount;
	/**
	 * Flag that used to check if the data was posted successfully
	 */
	private String printerSchema;	
	/**
	 * Field that holds the slip schema
	 */
	private String slipSchema;	
	
	/**
	 * Pay Table Id instance
	 */
	
	private String slotAttendantId = null;
	/**
	 * Card In Date instance
	 */
	private String cardInDate;

	/**
	 * Player Card instance
	 */
	private String playerCard = null;
	
	/**
	 * cashless account number instance
	 */
	private String accountNumber = null;
	/**
	 * Original Amount instance
	 */
	private String originalAmount;
	/**
	 * MachinePaid Amount instance
	 */
	//private String machinePaidAmount;
	/**
	 * HandPaid Amount instance
	 */
	private String hpjpAmount;
	/**
	 * Pay Line instance
	 */
	private String payline = null;
	/**
	 * Coins Played instance
	 */
	private Integer coinsPlayed;
	
	/**
	 * Winning Combination instance 
	 */
	private String winningComb = null;
	
	/**
	 * Void Employee Login instance
	 */
	private String voidEmployeeLogin = null;
	
	
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
	private String window = null;
	
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
	private Integer siteId;
	/**
	 * Sequence Number instance
	 */
	private String sequenceNumber;

	/**
	 * Asset Configuration instance
	 */
	private String assetConfigLocation;
	
	
	
	//Additional Fields Added here.
	
	private String langSelected=null;

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
	 * @return the assetConfNumberUsed
	 */

	/**
	 * @return the associatedPlayerCard
	 */

	/**
	 * @return the authEmployeeId1
	 */

	/**
	 * @return the authEmployeeId2
	 */

	/**
	 * @return the cardInDate
	 */
	public String getCardInDate() {
		return cardInDate;
	}
	/**
	 * @param cardInDate the cardInDate to set
	 */
	public void setCardInDate(String cardInDate) {
		this.cardInDate = cardInDate;
	}
	/**
	 * @return the coinsPlayed
	 */
	public Integer getCoinsPlayed() {
		return coinsPlayed;
	}
	/**
	 * @param coinsPlayed the coinsPlayed to set
	 */
	public void setCoinsPlayed(Integer coinsPlayed) {
		this.coinsPlayed = coinsPlayed;
	}
	
	/**
	 * @return the machinePaidAmount
	 *//*
	public double getMachinePaidAmount() {
		return machinePaidAmount;
	}
	*//**
	 * @param machinePaidAmount the machinePaidAmount to set
	 *//*
	public void setMachinePaidAmount(double machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}*/
	
	/*
	 * JACKPOT CHECK SCHEMA DETAILS FOR SOUTH AFRICA
	 */
	/**
	 * JACKPOT SLIP NUMBER
	 * @author vsubha
	 */
	private String jackpotSlipNumber;
	
	/**
	 * JACKPOT SLIP PRINTED DATE
	 * @author vsubha
	 */
	private String date;
	
	/**
	 * PLAYER NAME
	 * @author vsubha
	 */
	private String pay;
	
	/**
	 * JACKPOT SLIP AMOUNT
	 * @author vsubha
	 */
	private String amount;
	
	/**
	 * JACKPOT HIT SLOT MACHINE NO
	 * @author vsubha
	 */
	private String machineNo;
	
	/**
	 * JACKPOT HIT SLOT STAND NO
	 * @author vsubha
	 */
	private String standNo;
	
	/**
	 * JACKPOT PAYOUT TYPE
	 * @author vsubha
	 */
	private String payoutType;
	
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
	 * @return the playerFirstName
	 */

	/**
	 * @return the playerLastName
	 */

	/**
	 * @return the printDate
	 */
	public String getPrintDate() {
		return printDate;
	}
	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	/**
	 * @return the printEmployeeLogin
	 */
	
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
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = IKeypadProcessConstants.JACKPOT_SLIP_SEQUENCE_NO_PREFIX + sequenceNumber;
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
	public Integer getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
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
	 * @return the taxAmount
	 */
	
	/**
	 * @return the themeName
	 */
	
	
	/**
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
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
	 * @param voidEmployeeLogin the voidEmployeeLogin to set
	 */
	public void setVoidEmployeeLogin(String voidEmployeeLogin) {
		this.voidEmployeeLogin = voidEmployeeLogin;
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
	 * @return the accountPeriod
	 */
	public String getAccountPeriod() {
		return accountPeriod;
	}
	/**
	 * @param accountPeriod the accountPeriod to set
	 */
	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
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
		return authEmpIdTwo;
	}
	/**
	 * @param authEmpIdTwo the authEmpIdTwo to set
	 */
	public void setAuthEmpIdTwo(String authEmpIdTwo) {
		this.authEmpIdTwo = authEmpIdTwo;
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
	 * @return the barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	 * @return the jpDescription
	 */
	public String getJpDescription() {
		return jpDescription;
	}
	/**
	 * @param jpDescription the jpDescription to set
	 */
	public void setJpDescription(String jpDescription) {
		this.jpDescription = jpDescription;
	}
	/**
	 * @return the keyboard
	 */
	public String getKeyboard() {
		return keyboard;
	}
	/**
	 * @param keyboard the keyboard to set
	 */
	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}
	/**
	 * @return the pKeyboard
	 */
	public String getPKeyboard() {
		return pKeyboard;
	}
	/**
	 * @param keyboard the pKeyboard to set
	 */
	public void setPKeyboard(String keyboard) {
		pKeyboard = keyboard;
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
	/**
	 * @return the slotAttendantName
	 */
	public String getSlotAttendantName() {
		return slotAttendantName;
	}
	/**
	 * @param slotAttendantName the slotAttendantName to set
	 */
	public void setSlotAttendantName(String slotAttendantName) {
		this.slotAttendantName = slotAttendantName;
	}
	/**
	 * @return the window
	 */
	public String getWindow() {
		return window;
	}
	/**
	 * @param window the window to set
	 */
	public void setWindow(String window) {
		this.window = window;
	}
	/**
	 * @return the processEmpId
	 */
	public String getProcessEmpId() {
		return processEmpId;
	}
	/**
	 * @param processEmpId the processEmpId to set
	 */
	public void setProcessEmpId(String processEmpId) {
		this.processEmpId = processEmpId;
	}
	/**
	 * @return the processEmpName
	 */
	public String getProcessEmpName() {
		return processEmpName;
	}
	/**
	 * @param processEmpName the processEmpName to set
	 */
	public void setProcessEmpName(String processEmpName) {
		this.processEmpName = processEmpName;
	}
	/**
	 * @return the slotDenomination
	 */
	public Double getSlotDenomination() {
		return slotDenomination;
	}
	/**
	 * @param slotDenomination the slotDenomination to set
	 */
	public void setSlotDenomination(Double slotDenomination) {
		this.slotDenomination = slotDenomination;
	}
	/**
	 * @return the hpjpAmount
	 */
	public String getHpjpAmount() {
		return hpjpAmount;
	}
	/**
	 * @param hpjpAmount the hpjpAmount to set
	 */
	public void setHpjpAmount(String hpjpAmount) {
		this.hpjpAmount = hpjpAmount;
	}
	/**
	 * @return the jackpotNetAmount
	 */
	public String getJackpotNetAmount() {
		return jackpotNetAmount;
	}
	/**
	 * @param jackpotNetAmount the jackpotNetAmount to set
	 */
	public void setJackpotNetAmount(String jackpotNetAmount) {
		this.jackpotNetAmount = jackpotNetAmount;
	}
	/**
	 * @return the jackpotTaxAmount
	 */
	public String getJackpotTaxAmount() {
		return jackpotTaxAmount;
	}
	/**
	 * @param jackpotTaxAmount the jackpotTaxAmount to set
	 */
	public void setJackpotTaxAmount(String jackpotTaxAmount) {
		this.jackpotTaxAmount = jackpotTaxAmount;
	}
	/**
	 * @return the originalAmount
	 */
	public String getOriginalAmount() {
		return originalAmount;
	}
	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}
	/**
	 * @return the actualAmount
	 */
	public String getActualAmount() {
		return actualAmount;
	}
	/**
	 * @param actualAmount the actualAmount to set
	 */
	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getLangSelected() {
		return langSelected;
	}
	public void setLangSelected(String langSelected) {
		this.langSelected = langSelected;
	}
	/**
	 * @return the cashier
	 */
	public String getCashier() {
		return cashier;
	}
	/**
	 * @param cashier the cashier to set
	 */
	public void setCashier(String cashier) {
		this.cashier = cashier;
	}
	/**
	 * Gets the cardless account number
	 * @return
	 * @author vsubha
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * Sets the cardless account number
	 * @param accountNumber
	 * @author vsubha
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}	
	/*
	 * JACKPOT CHECK SCHEMA DETAILS FOR SOUTH AFRICA
	 * GETTER AND SETTER METHODS
	 */
	/**
	 * Gets jackpot slip number
	 * @return
	 * @author vsubha
	 */
	public String getJackpotSlipNumber() {
		return jackpotSlipNumber;
	}
	/**
	 * Sets jackpot slip number
	 * @param jackpotSlipNumber
	 * @author vsubha
	 */
	public void setJackpotSlipNumber(String jackpotSlipNumber) {
		this.jackpotSlipNumber = jackpotSlipNumber;
	}
	/**
	 * Gets the jackpot slip printed date
	 * @return
	 * @author vsubha
	 */
	public String getDate() {
		return date;
	}
	/**
	 * Sets the jackpot slip printed date
	 * @param date
	 * @author vsubha
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * Gets the player name
	 * @return
	 */
	public String getPay() {
		return pay;
	}
	/**
	 * Sets the player name
	 * @param pay
	 * @author vsubha
	 */
	public void setPay(String pay) {
		this.pay = pay;
	}
	/**
	 * Gets the jackpot amount
	 * @return
	 * @author vsubha
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * Sets the jackpot amount
	 * @param amount
	 * @author vsubha
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * Gets the jackpot hit slot machine number
	 * @return
	 * @author vsubha
	 */
	public String getMachineNo() {
		return machineNo;
	}
	/**
	 * Sets the jackpot hit slot machine number
	 * @param machineNo
	 * @author vsubha
	 */
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	/**
	 * Gets the jackpot hit slot stand number
	 * @return
	 * @author vsubha
	 */
	public String getStandNo() {
		return standNo;
	}
	/**
	 * Sets the jackpot hit slot stand number
	 * @param standNo
	 * @author vsubha
	 */
	public void setStandNo(String standNo) {
		this.standNo = standNo;
	}
	/**
	 * Gets the jackpot payout type
	 * @return
	 * @author vsubha
	 */
	public String getPayoutType() {
		return payoutType;
	}
	/**
	 * Sets the jackpot payout type
	 * @param payoutType
	 * @author vsubha
	 */
	public void setPayoutType(String payoutType) {
		this.payoutType = payoutType;
	}		
}
