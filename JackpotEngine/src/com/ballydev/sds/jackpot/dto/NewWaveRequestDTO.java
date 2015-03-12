package com.ballydev.sds.jackpot.dto;

import java.io.Serializable;
import java.util.List;

/**
 * New Wave Request DTO.
 * @author ranjithkumarm
 *
 */
public class NewWaveRequestDTO implements Serializable{

	
	private static final long serialVersionUID = 4387583763590641697L;
	
	private String slipLookUp=null;
	
	private String trackingSequence = null;
	
	private List<String> barcodeList = null;
	
	private List<SequenceWithTypeDTO> sequenceWithTypes = null;

	public List<String> getBarcodeList() {
		return barcodeList;
	}

	public void setBarcodeList(List<String> barcodeList) {
		this.barcodeList = barcodeList;
	}

	public List<SequenceWithTypeDTO> getSequenceWithTypes() {
		return sequenceWithTypes;
	}

	public void setSequenceWithTypes(List<SequenceWithTypeDTO> sequenceWithTypes) {
		this.sequenceWithTypes = sequenceWithTypes;
	}

	public String getSlipLookUp() {
		return slipLookUp;
	}

	public void setSlipLookUp(String slipLookUp) {
		this.slipLookUp = slipLookUp;
	}

	public String getTrackingSequence() {
		return trackingSequence;
	}

	public void setTrackingSequence(String trackingSequence) {
		this.trackingSequence = trackingSequence;
	}
	

	

}
