package com.ballydev.sds.jackpot.dto;

public class CashlessAccountDTO  extends JackpotBaseDTO {

	private static final long serialVersionUID = -4624189148679767542L;
	
	private String acntNo;
	private Short acntAccessTypeId;
	private String acntAccessType;
	private String playerCardNumber;
	private Short acntStateId;
	
	// error code related fields
	private int errorCode = 0; // by default no error 
	private String errorCodeDesc;
	private boolean success = true;
	
	public String getAcntNo() {
		return acntNo;
	}

	public void setAcntNo(String acntNo) {
		this.acntNo = acntNo;
	}

	public Short getAcntAccessTypeId() {
		return acntAccessTypeId;
	}

	public String getAcntAccessType() {
		return acntAccessType;
	}

	public void setAcntAccessType(String acntAccessType) {
		this.acntAccessType = acntAccessType;
	}
	
	public void setAcntAccessTypeId(Short acntAccessTypeId) {
		this.acntAccessTypeId = acntAccessTypeId;
	}
	
	public String getPlayerCardNumber() {
		return playerCardNumber;
	}

	public void setPlayerCardNumber(String playerCardNumber) {
		this.playerCardNumber = playerCardNumber;
	}
	
	public Short getAcntStateId() {
		return acntStateId;
	}

	public void setAcntStateId(Short acntStateId) {
		this.acntStateId = acntStateId;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCodeDesc() {
		return errorCodeDesc;
	}

	public void setErrorCodeDesc(String errorCodeDesc) {
		this.errorCodeDesc = errorCodeDesc;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("\n*************************************************************************");
		strBuilder.append("\n");
		strBuilder.append("Cashless Account number : ");
		strBuilder.append(this.acntNo);
		strBuilder.append("\n");
		strBuilder.append("Account Access Type ID : ");
		strBuilder.append(this.acntAccessTypeId);
		strBuilder.append("\n");
		strBuilder.append("Account Access Type : ");
		strBuilder.append(this.acntAccessType);
		strBuilder.append("\n");
		strBuilder.append("Player Card Number : ");
		strBuilder.append(this.playerCardNumber);
		strBuilder.append("\n");
		strBuilder.append("Account State ID : ");
		strBuilder.append(this.acntStateId);
		strBuilder.append("\n");
		strBuilder.append("Error Code : ");
		strBuilder.append(this.errorCode);
		strBuilder.append("\n");
		strBuilder.append("Error Code Description : ");
		strBuilder.append(this.errorCodeDesc);
		strBuilder.append("\n");
		strBuilder.append("Sucess : ");
		strBuilder.append(this.success);
		strBuilder.append("\n*************************************************************************");
		strBuilder.append("\n");		
		return strBuilder.toString();
	}

	
}
