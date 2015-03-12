package com.ballydev.sds.jackpot.dto;

import java.io.Serializable;

/**
 * New Wave Response DTO.
 * @author ranjithkumarm
 *
 */
public class NewWaveResponseDTO implements Serializable{

	
	private static final long serialVersionUID = 561879199226167563L;
	
    private String slipLookUp=null;
	
	private String trackingSequence = null;
	
	private String barcode = null;
	
	private String slipSequence = null;
	
	private String documentType = null;
	
	private String siteNumber = null;
	
	private Long jackpotAmount = null;
	
	private Long denomination = null;
	
	private String transactionDateTime = null;
	
	private String voidedDateTime = null;
	
	private Boolean isManual=null;
	
	private String processedBoothId = null;
	
	private String processedSlotId = null;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Long getDenomination() {
		return denomination;
	}

	public void setDenomination(Long denomination) {
		this.denomination = denomination;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Boolean getIsManual() {
		return isManual;
	}

	public void setIsManual(Boolean isManual) {
		this.isManual = isManual;
	}

	public Long getJackpotAmount() {
		return jackpotAmount;
	}

	public void setJackpotAmount(Long jackpotAmount) {
		this.jackpotAmount = jackpotAmount;
	}

	public String getProcessedBoothId() {
		return processedBoothId;
	}

	public void setProcessedBoothId(String processedBoothId) {
		this.processedBoothId = processedBoothId;
	}

	public String getProcessedSlotId() {
		return processedSlotId;
	}

	public void setProcessedSlotId(String processedSlotId) {
		this.processedSlotId = processedSlotId;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public String getSlipLookUp() {
		return slipLookUp;
	}

	public void setSlipLookUp(String slipLookUp) {
		this.slipLookUp = slipLookUp;
	}

	public String getSlipSequence() {
		return slipSequence;
	}

	public void setSlipSequence(String slipSequence) {
		this.slipSequence = slipSequence;
	}

	public String getTrackingSequence() {
		return trackingSequence;
	}

	public void setTrackingSequence(String trackingSequence) {
		this.trackingSequence = trackingSequence;
	}

	public String getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(String transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public String getVoidedDateTime() {
		return voidedDateTime;
	}

	public void setVoidedDateTime(String voidedDateTime) {
		this.voidedDateTime = voidedDateTime;
	}

	

}
