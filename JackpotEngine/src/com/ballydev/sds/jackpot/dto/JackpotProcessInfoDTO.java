/*****************************************************************************
 * $Id: JackpotProcessInfoDTO.java,v 1.10, 2010-10-27 11:02:12Z, Subha Viswanathan$
 * $Date: 10/27/2010 6:02:12 AM$
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



/**
 * JackpotProcessInfoDTO
 * 
 * @author vsubha
 * @version $Revision: 11$
 */
public class JackpotProcessInfoDTO extends JackpotBaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7206034734650441359L;

	private String siteNo;
	
	private Integer siteId;
	/**
	 * assetConfigNumber instance
	 */
	private String assetConfigNumber;
	
	private Long sequenceNumber;
	
	private Short jackpotTypeId;
	
	private int slipReferenceID;
	
	private Short processFlagId;
	
	private Short statusFlagId;
	
	private Long hpjpAmount = 0l;
	
	private Long jackpotNetAmount = 0l;
	
	private short errCode;
	
	private String errMessage;
	
	private boolean isProcessSuccessful = false;

	/**
	 * Gets Site Number
	 * @return
	 */
	public String getSiteNo() {
		return this.siteNo;
	}

	/**
	 * Sets Site Number
	 * @param siteNo
	 */
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	/**
	 * Gets Sequence Number
	 * @return
	 */
	public Long getSequenceNumber() {
		return this.sequenceNumber;
	}

	/**
	 * Sets sequence number
	 * @param sequenceNumber
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Gets jackpot type id
	 * @return
	 */
	public Short getJackpotTypeId() {
		return this.jackpotTypeId;
	}

	/**
	 * Sets jackpot type id
	 * @param jackpotTypeId
	 */
	public void setJackpotTypeId(Short jackpotTypeId) {
		this.jackpotTypeId = jackpotTypeId;
	}

	/**
	 * Gets process flag id
	 * @return
	 */
	public Short getProcessFlagId() {
		return this.processFlagId;
	}

	/**
	 * Sets process flag id
	 * @param processFlagId
	 */
	public void setProcessFlagId(Short processFlagId) {
		this.processFlagId = processFlagId;
	}

	/**
	 * Gets status flag id
	 * @return
	 */
	public Short getStatusFlagId() {
		return this.statusFlagId;
	}

	/**
	 * Sets status flag id.
	 * @param statusFlagId
	 */
	public void setStatusFlagId(Short statusFlagId) {
		this.statusFlagId = statusFlagId;
	}

	/**
	 * Gets HPJP amount.
	 * @return
	 */
	public Long getHpjpAmount() {
		return this.hpjpAmount;
	}

	/**
	 * Sets HPJP amount.
	 * @param hpjpAmount
	 */
	public void setHpjpAmount(Long hpjpAmount) {
		this.hpjpAmount = hpjpAmount;
	}

	/**
	 * Gets Jackpot net amount.
	 * @return
	 */
	public Long getJackpotNetAmount() {
		return this.jackpotNetAmount;
	}

	/**
	 * Sets Jackpot net amount.
	 * @param jackpotNetAmount
	 */
	public void setJackpotNetAmount(Long jackpotNetAmount) {
		this.jackpotNetAmount = jackpotNetAmount;
	}

	/**
	 * Gets slip reference id.
	 * @return
	 */
	public int getSlipReferenceID() {
		return this.slipReferenceID;
	}

	/**
	 * Sets slip reference id.
	 * @param slipReferenceID
	 */
	public void setSlipReferenceID(int slipReferenceID) {
		this.slipReferenceID = slipReferenceID;
	}
	
	/**
	 * Gets error code.
	 * @return
	 */
	public short getErrCode() {
		return this.errCode;
	}

	/**
	 * Sets error code.
	 * @param errCode
	 */
	public void setErrCode(short errCode) {
		this.errCode = errCode;
	}

	/**
	 * Gets error message.
	 * @return
	 */
	public String getErrMessage() {
		return this.errMessage;
	}

	/**
	 * Sets error message.
	 * @param errMessage
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	/**
	 * Gets process successful flag.
	 * @return
	 */
	public boolean isProcessSuccessful() {
		return this.isProcessSuccessful;
	}

	/**
	 * Sets process successful flag.
	 * @param isProcessSuccessful
	 */
	public void setProcessSuccessful(boolean isProcessSuccessful) {
		this.isProcessSuccessful = isProcessSuccessful;
	}

	/**
	 * Gets site id.
	 * @return
	 */
	public Integer getSiteId() {
		return this.siteId;
	}

	/**
	 * Sets site id.
	 * @param siteId
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	/**
	 * Gets Slot Number
	 * @return
	 */
	public String getAssetConfigNumber() {
		return this.assetConfigNumber;
	}

	/**
	 * Sets Slot Number
	 * @param assetConfigNumber
	 */
	public void setAssetConfigNumber(String assetConfigNumber) {
		this.assetConfigNumber = assetConfigNumber;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("\n*************************************************************************");
		strBuilder.append("\n");
		strBuilder.append("Site number is : ");
		strBuilder.append(this.siteNo);
		strBuilder.append("\n");
		strBuilder.append("Site Id is : ");
		strBuilder.append(this.siteId);
		strBuilder.append("\n");		
		strBuilder.append("Sequence number is : ");
		strBuilder.append(this.sequenceNumber);
		strBuilder.append("\n");
		strBuilder.append("Jackpot Type Id is : ");
		strBuilder.append(this.jackpotTypeId);
		strBuilder.append("\n");
		strBuilder.append("Slip reference Id is : ");
		strBuilder.append(this.slipReferenceID);
		strBuilder.append("\n");
		strBuilder.append("Slot Number is : " );
		strBuilder.append(this.assetConfigNumber);
		strBuilder.append("\n");
		strBuilder.append("Process flag Id is : ");
		strBuilder.append(this.processFlagId);
		strBuilder.append("\n");
		strBuilder.append("Status flag Id is : ");
		strBuilder.append(this.statusFlagId);
		strBuilder.append("\n");
		strBuilder.append("HPJP amount is : ");
		strBuilder.append(this.hpjpAmount);
		strBuilder.append("\n");
		strBuilder.append("Jackpot net amount is : ");
		strBuilder.append(this.jackpotNetAmount);
		strBuilder.append("\n");
		strBuilder.append("Error code is : ");
		strBuilder.append(this.errCode);
		strBuilder.append("\n");
		strBuilder.append("Error message is : ");
		strBuilder.append(this.errMessage);
		strBuilder.append("\n");
		strBuilder.append("Is process successful : ");
		strBuilder.append(this.isProcessSuccessful);
		strBuilder.append("\n*************************************************************************");
		strBuilder.append("\n");		
		return strBuilder.toString();
	}	
}
