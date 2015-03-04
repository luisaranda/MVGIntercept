/*****************************************************************************
 * $Id: ReprintForm.java,v 1.0, 2008-04-03 15:53:20Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:53:20 AM$
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
package com.ballydev.sds.jackpotui.form;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.jackpot.dto.JackpotDTO;

/**
 * This class contains the properties coresponding to the controls in the 
 * ReprintComposite  
 * @author vijayrajm
 * @version $Revision: 1$
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
	private String sequenceNumber;
	/**
	 * Reprint JackpotDTO instance
	 */
	private JackpotDTO reprintJackpotSlipDTO;
	
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
	 * @return the reprintJackpotSlipDTO
	 */
	public JackpotDTO getReprintJackpotSlipDTO() {
		return reprintJackpotSlipDTO;
	}
	/**
	 * @param reprintJackpotSlipDTO the reprintJackpotSlipDTO to set
	 */
	public void setReprintJackpotSlipDTO(JackpotDTO reprintJackpotSlipDTO) {
		JackpotDTO oldValue = this.reprintJackpotSlipDTO;
		JackpotDTO newValue = reprintJackpotSlipDTO;
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
		firePropertyChange("empPassword", oldValue, newValue);	
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
		String oldValue = this.sequenceNumber;
		String newValue = sequenceNumber;
		this.sequenceNumber = sequenceNumber;
		firePropertyChange("sequenceNumber", oldValue, newValue);	
	}

}
