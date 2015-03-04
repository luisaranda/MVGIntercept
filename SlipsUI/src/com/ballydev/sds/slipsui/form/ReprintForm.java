/*****************************************************************************
 * $Id: ReprintForm.java,v 1.1, 2008-05-13 09:07:05Z, Ambereen Drewitt$
 * $Date: 5/13/2008 4:07:05 AM$
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
package com.ballydev.sds.slipsui.form;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.slips.dto.SlipsDTO;

/**
 * This class contains the properties coresponding to the controls in the 
 * ReprintComposite  
 * @author vijayrajm
 * @date 24/04/2007
 */
public class ReprintForm extends SDSForm{

	/**
	 * Employee Id instance
	 */
	private String employeeId;
	/**
	 * Employee Password instance
	 */
	private String empPassword;
	/**
	 * Sequence Number instance
	 */
	private String sequenceNo;
	/**
	 * Reprint JackpotDTO instance
	 */
	private SlipsDTO reprintJackpotSlipDTO;	
	/**
	 * Field that holds the value if a fill slip is selected 
	 */
	private Boolean fill;
	
	/**
	 * Field that holds the value if a bleed slip is selected 
	 */
	private Boolean bleed;
	
	/**
	 * Field that holds the value if a beef slip is selected 
	 */
	private Boolean beef;
	
	/**
	 * @return the beef
	 */
	public Boolean getBeef() {
		return beef;
	}
	/**
	 * @param beef the beef to set
	 */
	public void setBeef(Boolean beef) {
		this.beef = beef;
	}
	/**
	 * @return the bleed
	 */
	public Boolean getBleed() {
		return bleed;
	}
	/**
	 * @param bleed the bleed to set
	 */
	public void setBleed(Boolean bleed) {
		this.bleed = bleed;
	}
	/**
	 * @return the fill
	 */
	public Boolean getFill() {
		return fill;
	}
	/**
	 * @param fill the fill to set
	 */
	public void setFill(Boolean fill) {
		this.fill = fill;
	}
	/**
	 * 
	 * @return employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * Set the employeeId
	 * @param employeeId
	 */
	public void setEmployeeId(String employeeId) {
		String oldValue = this.employeeId;
		String newValue = employeeId;
		this.employeeId = employeeId;
		firePropertyChange("employeeId", oldValue, newValue);		
	}
	/**
	 * 
	 * @return sequenceNo
	 */
	public String getSequenceNo() {
		return sequenceNo;
	}
	/**
	 * Set the sequenceNo
	 * @param sequenceNo
	 */
	public void setSequenceNo(String sequenceNo) {
		String oldValue = this.sequenceNo;
		String newValue = sequenceNo;
		this.sequenceNo = sequenceNo;
		firePropertyChange("sequenceNo", oldValue, newValue);		
	}
	/**
	 * @return the reprintJackpotSlipDTO
	 */
	public SlipsDTO getReprintJackpotSlipDTO() {
		return reprintJackpotSlipDTO;
	}
	/**
	 * @param reprintJackpotSlipDTO the reprintJackpotSlipDTO to set
	 */
	public void setReprintJackpotSlipDTO(SlipsDTO reprintJackpotSlipDTO) {
		SlipsDTO oldValue = this.reprintJackpotSlipDTO;
		SlipsDTO newValue = reprintJackpotSlipDTO;
		this.reprintJackpotSlipDTO = reprintJackpotSlipDTO;
		firePropertyChange("reprintJackpotSlipDTO", oldValue, newValue);
	}
	/**
	 * @return the empPassword
	 */
	public String getEmpPassword() {
		return empPassword;
	}
	/**
	 * @param empPassword the empPassword to set
	 */
	public void setEmpPassword(String empPassword) {
		String oldValue = this.empPassword;
		String newValue = empPassword;
		this.empPassword = empPassword;
		firePropertyChange("employeePwd", oldValue, newValue);	
	}

}
