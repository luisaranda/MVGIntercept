/*****************************************************************************
 * $Id: VoidForm.java,v 1.0, 2008-04-03 15:54:06Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:54:06 AM$
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
 * This class contains the properties coresponding to the controls in the
 * VoidComposite
 * @author vijayrajm
 * @version $Revision: 1$
 */
public class VoidForm extends SDSForm{

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
	 * Slot Number instance
	 */
	private String slotNo;
	
	/**
	 * slotLocationNo form object
	 */
	private String slotLocationNo;
	
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
	

}
