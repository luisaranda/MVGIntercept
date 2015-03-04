/*****************************************************************************
 * $Id: EmployeeShiftSlotDetailsForm.java,v 1.2, 2010-01-18 10:22:32Z, Suganthi, Kaliamoorthy$
 * $Date: 1/18/2010 4:22:32 AM$
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

/**
 * Form for the Pending Jackpot Composite
 * @author dambereen
 * @version $Revision: 3$
 */
public class EmployeeShiftSlotDetailsForm extends SDSForm{	
	/**
	 * numOfMinutes form object
	 */
	private String numOfMinutes;
	/**
	 * employeeId form object
	 */
	private String employeeId;
	/**
	 * empPassword form object
	 */
	private String empPassword;		
	/**
	 * slotNo form object
	 */
	private String slotNo;
	/**
	 * slotLocationNo form object
	 */
	private String slotLocationNo;
	
	/**
	 * sequenceNo form object. 
	 */
	private String sequenceNumber;
	
	/**
	 *  day shift form object
	 */
	private Boolean dayYes = true;
	
	/** 
	 *  swing shift form object
	 */
	private Boolean swingYes = false;
	
	/**
	 * graveyard shift form object
	 */
	private Boolean graveyardYes = false;
	
	
	/**
	 *  day shift form object
	 */
	private Boolean slotStandYes = true;
	
	/** 
	 *  swing shift form object
	 */
	private Boolean sequenceYes = false;
	
	/**
	 * graveyard shift form object
	 */
	private Boolean minutesYes = false;
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
	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		String oldValue = this.employeeId;
		String newValue = employeeId;
		this.employeeId = employeeId;
		firePropertyChange("employeeId", oldValue, newValue);	
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
	 * @return the slotLocationNo
	 */
	public String getSlotLocationNo() {
		return slotLocationNo;
	}
	/**
	 * @param slotLocationNo the slotLocationNo to set
	 */
	public void setSlotLocationNo(String slotLocationNo) {
		String oldValue = this.slotLocationNo;
		String newValue = slotLocationNo;
		this.slotLocationNo = slotLocationNo;
		firePropertyChange("slotLocationNo", oldValue, newValue);
	}
	/**
	 * @return the slotNo
	 */
	public String getSlotNo() {
		return slotNo;
	}
	/**
	 * @param slotNo the slotNo to set
	 */
	public void setSlotNo(String slotNo) {
		String oldValue = this.slotNo;
		String newValue = slotNo;
		this.slotNo = slotNo;
		firePropertyChange("slotNo", oldValue, newValue);
	}
	/**
	 * @return the numOfMinutes
	 */
	public String getNumOfMinutes() {
		return numOfMinutes;
	}
	/**
	 * @param numOfMinutes the numOfMinutes to set
	 */
	public void setNumOfMinutes(String numOfMinutes) {
		String oldValue = this.numOfMinutes;
		String newValue = numOfMinutes;
		this.numOfMinutes = numOfMinutes;
		firePropertyChange("numOfMinutes", oldValue, newValue);
	}
	/**
	 * @return the dayYes
	 */
	public Boolean getDayYes() {
		return dayYes;
	}
	/**
	 * @param dayYes the dayYes to set
	 */
	public void setDayYes(Boolean dayYes) {
		this.dayYes = dayYes;
	}
	/**
	 * @return the graveyardYes
	 */
	public Boolean getGraveyardYes() {
		return graveyardYes;
	}
	/**
	 * @param graveyardYes the graveyardYes to set
	 */
	public void setGraveyardYes(Boolean graveyardYes) {
		this.graveyardYes = graveyardYes;
	}
	/**
	 * @return the swingYes
	 */
	public Boolean getSwingYes() {
		return swingYes;
	}
	/**
	 * @param swingYes the swingYes to set
	 */
	public void setSwingYes(Boolean swingYes) {
		this.swingYes = swingYes;
	}
	public Boolean getSlotStandYes() {
		return slotStandYes;
	}
	public void setSlotStandYes(Boolean slotStandYes) {
		this.slotStandYes = slotStandYes;
	}
	public Boolean getSequenceYes() {
		return sequenceYes;
	}
	public void setSequenceYes(Boolean sequenceYes) {
		this.sequenceYes = sequenceYes;
	}
	public Boolean getMinutesYes() {
		return minutesYes;
	}
	public void setMinutesYes(Boolean minutesYes) {
		this.minutesYes = minutesYes;
	}
	
}
