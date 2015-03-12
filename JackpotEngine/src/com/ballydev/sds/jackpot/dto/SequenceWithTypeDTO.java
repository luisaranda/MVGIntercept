package com.ballydev.sds.jackpot.dto;

import java.io.Serializable;

/**
 * New Wave Request for sequence with type.
 * @author ranjithkumarm
 *
 */
public class SequenceWithTypeDTO implements Serializable{

	
	private static final long serialVersionUID = 7352416127990402676L;
	
	private String jackpotSequence=null;
	
	private String documentType = null;

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getJackpotSequence() {
		return jackpotSequence;
	}

	public void setJackpotSequence(String jackpotSequence) {
		this.jackpotSequence = jackpotSequence;
	}
	

}
