package com.ballydev.sds.jackpot.util;

public class JackpotReqDTO {
	
	/**
	 *  The Jackpot Id
	 */
	
	private String jackpotId;
	
	/**
	 *  The  Slot Number
	 */
	private String slotNumber;
	
	
	/**
	 *  The Shift
	 */

	private String shift;
	
	/**
	 *  The From System 
	 */

	private String fromSystem;

	
	/**
	 *  The Authourized Employee ID1
	 */
	
	private String authEmpId1;

	
	/**
	 * The Authourized Employee ID2
	 */
	
	private String authEmpId2;

	/**
	 * The % of Tax 
	 */
	
	private String taxAmount;
	
    /**
     *  The Hand Paid Ampunt
     */
	
	private String handPaidAmount;

	/**
	 *  The Machine Paid Amount
	 */
	
	private String machinePaidAmount;

	/**
	 * The Number Of Coins Played 
	 */
	
	private long coinsPlayed;

	/**
	 *  The Employee ID
	 *  
	 */
			
	private String empID;
	
	
	private String authEmpID;
	
	
	private long originalAmount;


	public String getAuthEmpID() {
		return authEmpID;
	}


	public void setAuthEmpID(String authEmpID) {
		this.authEmpID = authEmpID;
	}


	public String getAuthEmpId1() {
		return authEmpId1;
	}


	public void setAuthEmpId1(String authEmpId1) {
		this.authEmpId1 = authEmpId1;
	}


	public String getAuthEmpId2() {
		return authEmpId2;
	}


	public void setAuthEmpId2(String authEmpId2) {
		this.authEmpId2 = authEmpId2;
	}


	public long getCoinsPlayed() {
		return coinsPlayed;
	}


	public void setCoinsPlayed(long coinsPlayed) {
		this.coinsPlayed = coinsPlayed;
	}


	public String getEmpID() {
		return empID;
	}


	public void setEmpID(String empID) {
		this.empID = empID;
	}


	public String getFromSystem() {
		return fromSystem;
	}


	public void setFromSystem(String fromSystem) {
		this.fromSystem = fromSystem;
	}


	public String getHandPaidAmount() {
		return handPaidAmount;
	}


	public void setHandPaidAmount(String handPaidAmount) {
		this.handPaidAmount = handPaidAmount;
	}


	public String getJackpotId() {
		return jackpotId;
	}


	public void setJackpotId(String jackpotId) {
		this.jackpotId = jackpotId;
	}


	public String getMachinePaidAmount() {
		return machinePaidAmount;
	}


	public void setMachinePaidAmount(String machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}


	public String getShift() {
		return shift;
	}


	public void setShift(String shift) {
		this.shift = shift;
	}


	public String getSlotNumber() {
		return slotNumber;
	}


	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}


	public String getTaxAmount() {
		return taxAmount;
	}


	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}


	public void setCbNumber(String string) {
		// TODO Auto-generated method stub
		
	}


	public long getOriginalAmount() {
		return originalAmount;
	}


	public void setOriginalAmount(long originalAmount) {
		this.originalAmount = originalAmount;
	}


}
