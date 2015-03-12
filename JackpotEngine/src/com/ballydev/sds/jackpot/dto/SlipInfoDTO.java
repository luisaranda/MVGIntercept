/*****************************************************************************
 * $Id: SlipInfoDTO.java,v 1.13, 2009-02-11 17:54:01Z, Ambereen Drewitt$
 * $Date: 2/11/2009 11:54:01 AM$
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

import java.util.Date;

/**
 * DTO object that will contain the jackpot fields for accounting
 * @author gsrinivasulu
 * @version $Revision: 14$
 */
public class SlipInfoDTO extends JackpotBaseDTO{
			
	
	private static final long serialVersionUID = 2873349552159729650L;

	/**
	 * Fill denomination for the slot
	 */
	private Long fillDenomination = 0l;
	
	/**
	 * Site No 
	 */
	private String siteNo;
	
	/**
	 * Site Id 
	 */
	private Integer siteId;
	
	private Short slipTypeId;
	
	private String slipTypeDesc;
	
	private Short auditCodeId = 0;
	
	private String auditCodeDesc;
	
	private Short jackpotTypeId;
	
	private String jackpotTypeDesc;
	
	/**
	 * Jackpot Process Flag Id
	 */
	private Short processFlagId;
	
	/**
	 * Jackpot Process Flag Desc
	 */
	private String processFlagDesc;
	
	/**
	 * Slip status
	 */
	private Short statusFlagId;
	
	private String statusFlagDesc;
	
	/**
	 * Date and time the Jackpot was hit at the slot
	 */
	private Date jackpotHitDate;
	
	private Long beefAmount = 0l;
	
	private Long bleedAmount = 0l;
	
	private Long fillAmount = 0l;
	
	/**
	 * SLIP_REFERENCE table primary key
	 */
	private String extTransId;
	
	private Long sequenceNumber;
	
	private Long originalAmount = 0l;
	
	private Long machinePaidAmount= 0l;
	
	private Long handPaidAmount = 0l;
	
	private Long handPaidAmountRounded = 0l;
	
	private Long jackpotNetAmount = 0l;
	
	/**
	 * Jackpot Process Flag Id
	 */
	private Short slipRevenueType;
	
	private String assetNumber;
	
	private Long gmuDenomination = 0l;
	
	private String authorizingEmployeeId;
	
	private String loggedInEmployeeId;
	
	private String firstAuthorizingEmployeeId;
	
	private String secondAuthorizingEmployeeId;
	
	private String slipTransEmployeeId;	
		
	private String transEmployeeId;
	
	/**
	 * Date and time the slip was printed
	 */
	private Date transDate;		
	
	private String slipId;
	
	/**
	 * Currency symbol
	 */
	private String currency;

	
	@Override
	public String toString() {
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("*************************************************************************");
		strBuild.append("\n");
		strBuild.append("ExtTransID is : ");
		strBuild.append(this.extTransId);
		strBuild.append("\n");
		strBuild.append("Sequence Number is : ");
		strBuild.append(this.sequenceNumber);
		strBuild.append("\n");		
		strBuild.append("Original Amount is : ");
		strBuild.append(this.originalAmount);
		strBuild.append("\n");
		strBuild.append("Beef Amount is : ");
		strBuild.append(this.beefAmount);
		strBuild.append("\n");
		strBuild.append("Fill Amount is : ");
		strBuild.append(this.fillAmount);
		strBuild.append("\n");
		strBuild.append("Bleed Amount is : ");
		strBuild.append(this.bleedAmount);
		strBuild.append("\n");
		strBuild.append("Machine Paid Amount is : ");
		strBuild.append(this.machinePaidAmount);
		strBuild.append("\n");
		strBuild.append("Hand Paid Amount is : ");
		strBuild.append(this.handPaidAmount);
		strBuild.append("\n");
		strBuild.append("Hand Paid Amount Rounded is : ");
		strBuild.append(this.handPaidAmountRounded);
		strBuild.append("\n");
		strBuild.append("Jackpot Net Amount is : ");
		strBuild.append(this.jackpotNetAmount);
		strBuild.append("\n");
		strBuild.append("Audit Code is : ");
		strBuild.append(this.auditCodeId);
		strBuild.append("\n");
		strBuild.append("Slip Type Id is : ");
		strBuild.append(this.slipTypeId);
		strBuild.append("\n");
		strBuild.append("Slip Type Desc is : ");
		strBuild.append(this.slipTypeDesc);
		strBuild.append("\n");
		strBuild.append("Slip Revenue Type is : ");
		strBuild.append(this.slipRevenueType);
		strBuild.append("\n");
		strBuild.append("Asset Number is : ");
		strBuild.append(this.assetNumber);
		strBuild.append("\n");
		strBuild.append("GMU Denomination is : ");
		strBuild.append(this.gmuDenomination);
		strBuild.append("\n");
		strBuild.append("FILL Denomination is : ");
		strBuild.append(this.fillDenomination);
		strBuild.append("\n");		
		strBuild.append("Authorizing Employee Id is : ");
		strBuild.append(this.authorizingEmployeeId);
		strBuild.append("\n");
		strBuild.append("Logged In Employee is : ");
		strBuild.append(this.loggedInEmployeeId);
		strBuild.append("\n");
		strBuild.append("First Authorizing Employee is : ");
		strBuild.append(this.firstAuthorizingEmployeeId);
		strBuild.append("\n");
		strBuild.append("Second Authorizing Employee is : ");
		strBuild.append(this.secondAuthorizingEmployeeId);
		strBuild.append("\n");
		strBuild.append("Slip Trans Employee is : ");
		strBuild.append(this.slipTransEmployeeId);
		strBuild.append("\n");
		strBuild.append("Status Flag Id is : ");
		strBuild.append(this.statusFlagId);
		strBuild.append("\n");
		strBuild.append("Status Flag Desc is : ");
		strBuild.append(this.statusFlagDesc);
		strBuild.append("\n");
		strBuild.append("Trans Employee is : ");
		strBuild.append(this.transEmployeeId);
		strBuild.append("\n");
		strBuild.append("Jackpto Hit Date is : ");
		strBuild.append(this.jackpotHitDate);
		strBuild.append("\n");
		strBuild.append("Trans Date is : ");
		strBuild.append(this.transDate);
		strBuild.append("\n");
		strBuild.append("Jackpot Type Id is : ");
		strBuild.append(this.jackpotTypeId);
		strBuild.append("\n");
		strBuild.append("Jackpot Type Desc is : ");
		strBuild.append(this.jackpotTypeDesc);
		strBuild.append("\n");
		strBuild.append("Slip Id is : ");
		strBuild.append(this.slipId);
		strBuild.append("\n");
		strBuild.append("Process Flag Id is : ");
		strBuild.append(this.processFlagId);
		strBuild.append("\n");
		strBuild.append("Process Flag Desc is : ");
		strBuild.append(this.processFlagDesc);
		strBuild.append("\n");
		strBuild.append("Site Id is : ");
		strBuild.append(this.siteId);
		strBuild.append("\n");
		strBuild.append("Site No is : ");
		strBuild.append(this.siteNo);
		strBuild.append("\n");
		strBuild.append("Currency Symbol is : ");
		strBuild.append(this.currency);
		strBuild.append("*************************************************************************");
		strBuild.append("\n");		
		return strBuild.toString();
	}


	/**
	 * @return the assetNumber
	 */
	public String getAssetNumber() {
		return assetNumber;
	}


	/**
	 * @param assetNumber the assetNumber to set
	 */
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
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
	 * @return the auditCodeId
	 */
	public Short getAuditCodeId() {
		return auditCodeId;
	}


	/**
	 * @param auditCodeId the auditCodeId to set
	 */
	public void setAuditCodeId(Short auditCodeId) {
		this.auditCodeId = auditCodeId;
	}


	/**
	 * @return the authorizingEmployeeId
	 */
	public String getAuthorizingEmployeeId() {
		return authorizingEmployeeId;
	}


	/**
	 * @param authorizingEmployeeId the authorizingEmployeeId to set
	 */
	public void setAuthorizingEmployeeId(String authorizingEmployeeId) {
		this.authorizingEmployeeId = authorizingEmployeeId;
	}


	/**
	 * @return the beefAmount
	 */
	public Long getBeefAmount() {
		return beefAmount;
	}


	/**
	 * @param beefAmount the beefAmount to set
	 */
	public void setBeefAmount(Long beefAmount) {
		this.beefAmount = beefAmount;
	}


	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}


	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}


	/**
	 * @return the extTransId
	 */
	public String getExtTransId() {
		return extTransId;
	}


	/**
	 * @param extTransId the extTransId to set
	 */
	public void setExtTransId(String extTransId) {
		this.extTransId = extTransId;
	}


	/**
	 * @return the firstAuthorizingEmployeeId
	 */
	public String getFirstAuthorizingEmployeeId() {
		return firstAuthorizingEmployeeId;
	}


	/**
	 * @param firstAuthorizingEmployeeId the firstAuthorizingEmployeeId to set
	 */
	public void setFirstAuthorizingEmployeeId(String firstAuthorizingEmployeeId) {
		this.firstAuthorizingEmployeeId = firstAuthorizingEmployeeId;
	}


	/**
	 * @return the gmuDenomination
	 */
	public Long getGmuDenomination() {
		return gmuDenomination;
	}


	/**
	 * @param gmuDenomination the gmuDenomination to set
	 */
	public void setGmuDenomination(Long gmuDenomination) {
		this.gmuDenomination = gmuDenomination;
	}


	/**
	 * @return the handPaidAmount
	 */
	public Long getHandPaidAmount() {
		return handPaidAmount;
	}


	/**
	 * @param handPaidAmount the handPaidAmount to set
	 */
	public void setHandPaidAmount(Long handPaidAmount) {
		this.handPaidAmount = handPaidAmount;
	}


	/**
	 * @return the handPaidAmountRounded
	 */
	public Long getHandPaidAmountRounded() {
		return handPaidAmountRounded;
	}


	/**
	 * @param handPaidAmountRounded the handPaidAmountRounded to set
	 */
	public void setHandPaidAmountRounded(Long handPaidAmountRounded) {
		this.handPaidAmountRounded = handPaidAmountRounded;
	}


	/**
	 * @return the jackpotHitDate
	 */
	public Date getJackpotHitDate() {
		return jackpotHitDate;
	}


	/**
	 * @param jackpotHitDate the jackpotHitDate to set
	 */
	public void setJackpotHitDate(Date jackpotHitDate) {
		this.jackpotHitDate = jackpotHitDate;
	}


	/**
	 * @return the jackpotNetAmount
	 */
	public Long getJackpotNetAmount() {
		return jackpotNetAmount;
	}


	/**
	 * @param jackpotNetAmount the jackpotNetAmount to set
	 */
	public void setJackpotNetAmount(Long jackpotNetAmount) {
		this.jackpotNetAmount = jackpotNetAmount;
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
	 * @return the jackpotTypeId
	 */
	public Short getJackpotTypeId() {
		return jackpotTypeId;
	}


	/**
	 * @param jackpotTypeId the jackpotTypeId to set
	 */
	public void setJackpotTypeId(Short jackpotTypeId) {
		this.jackpotTypeId = jackpotTypeId;
	}


	/**
	 * @return the loggedInEmployeeId
	 */
	public String getLoggedInEmployeeId() {
		return loggedInEmployeeId;
	}


	/**
	 * @param loggedInEmployeeId the loggedInEmployeeId to set
	 */
	public void setLoggedInEmployeeId(String loggedInEmployeeId) {
		this.loggedInEmployeeId = loggedInEmployeeId;
	}


	/**
	 * @return the machinePaidAmount
	 */
	public Long getMachinePaidAmount() {
		return machinePaidAmount;
	}


	/**
	 * @param machinePaidAmount the machinePaidAmount to set
	 */
	public void setMachinePaidAmount(Long machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}


	/**
	 * @return the originalAmount
	 */
	public Long getOriginalAmount() {
		return originalAmount;
	}


	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(Long originalAmount) {
		this.originalAmount = originalAmount;
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
	 * @return the processFlagId
	 */
	public Short getProcessFlagId() {
		return processFlagId;
	}


	/**
	 * @param processFlagId the processFlagId to set
	 */
	public void setProcessFlagId(Short processFlagId) {
		this.processFlagId = processFlagId;
	}


	/**
	 * @return the secondAuthorizingEmployeeId
	 */
	public String getSecondAuthorizingEmployeeId() {
		return secondAuthorizingEmployeeId;
	}


	/**
	 * @param secondAuthorizingEmployeeId the secondAuthorizingEmployeeId to set
	 */
	public void setSecondAuthorizingEmployeeId(String secondAuthorizingEmployeeId) {
		this.secondAuthorizingEmployeeId = secondAuthorizingEmployeeId;
	}


	/**
	 * @return the sequenceNumber
	 */
	public Long getSequenceNumber() {
		return sequenceNumber;
	}


	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	 * @return the slipRevenueType
	 */
	public Short getSlipRevenueType() {
		return slipRevenueType;
	}


	/**
	 * @param slipRevenueType the slipRevenueType to set
	 */
	public void setSlipRevenueType(Short slipRevenueType) {
		this.slipRevenueType = slipRevenueType;
	}


	/**
	 * @return the slipTransEmployeeId
	 */
	public String getSlipTransEmployeeId() {
		return slipTransEmployeeId;
	}


	/**
	 * @param slipTransEmployeeId the slipTransEmployeeId to set
	 */
	public void setSlipTransEmployeeId(String slipTransEmployeeId) {
		this.slipTransEmployeeId = slipTransEmployeeId;
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
	 * @return the slipTypeId
	 */
	public Short getSlipTypeId() {
		return slipTypeId;
	}


	/**
	 * @param slipTypeId the slipTypeId to set
	 */
	public void setSlipTypeId(Short slipTypeId) {
		this.slipTypeId = slipTypeId;
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
	 * @return the statusFlagId
	 */
	public Short getStatusFlagId() {
		return statusFlagId;
	}


	/**
	 * @param statusFlagId the statusFlagId to set
	 */
	public void setStatusFlagId(Short statusFlagId) {
		this.statusFlagId = statusFlagId;
	}


	/**
	 * @return the transDate
	 */
	public Date getTransDate() {
		return transDate;
	}


	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}


	/**
	 * @return the transEmployeeId
	 */
	public String getTransEmployeeId() {
		return transEmployeeId;
	}


	/**
	 * @param transEmployeeId the transEmployeeId to set
	 */
	public void setTransEmployeeId(String transEmployeeId) {
		this.transEmployeeId = transEmployeeId;
	}


	/**
	 * @return the bleedAmount
	 */
	public Long getBleedAmount() {
		return bleedAmount;
	}


	/**
	 * @param bleedAmount the bleedAmount to set
	 */
	public void setBleedAmount(Long bleedAmount) {
		this.bleedAmount = bleedAmount;
	}


	/**
	 * @return the fillAmount
	 */
	public Long getFillAmount() {
		return fillAmount;
	}


	/**
	 * @param fillAmount the fillAmount to set
	 */
	public void setFillAmount(Long fillAmount) {
		this.fillAmount = fillAmount;
	}


	/**
	 * @return the fillDenomination
	 */
	public Long getFillDenomination() {
		return fillDenomination;
	}


	/**
	 * @param fillDenomination the fillDenomination to set
	 */
	public void setFillDenomination(Long fillDenomination) {
		this.fillDenomination = fillDenomination;
	}


}
