/*****************************************************************************
 * $Id: VoidForm.java,v 1.0, 2008-04-21 05:46:32Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:46:32 AM$
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

/**
 * This class contains the properties coresponding to the controls in the
 * VoidComposite
 * 
 * 
 */
public class VoidForm extends SDSForm{

	/**
	 * Employee Id instance
	 */
	private String employeeId;
	/**
	 * Employee Password instance
	 */
	private String employeePwd;
	/**
	 * Sequence Number instance
	 */
	private String sequenceNo;

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
	 * @return employeePwd
	 */
	public String getEmployeePwd() {
		return employeePwd;
	}
	/**
	 * Set the employeePwd
	 * @param employeePwd
	 */
	public void setEmployeePwd(String employeePwd) {
		String oldValue = this.employeePwd;
		String newValue = employeePwd;
		this.employeePwd = employeePwd;
		firePropertyChange("employeePwd", oldValue, newValue);	
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


}
