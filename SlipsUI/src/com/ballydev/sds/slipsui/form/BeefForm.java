/*****************************************************************************
 * $Id: BeefForm.java,v 1.3, 2011-01-24 09:17:14Z, Ambereen Drewitt$
 * $Date: 1/24/2011 3:17:14 AM$
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
 * Form for the Fill Composite
 * @author anantharajr
 *
 */
public class BeefForm extends SDSForm{

	/**
	 * employeeId form field
	 */
	private String employeeId;
	/**
	 * empPassword form field
	 */
	private String empPassword;
	/**
	 * Slot Location No form field
	 */
	private String slotLocationNo;
	/**
	 * Slot form field
	 */
	private String slotNo;
	
	private String windowNo;
	
	private Boolean dayYes;
	
	private Boolean swingYes;
	
	private Boolean graveyardYes;
	
	private String amount;
	
	private String accountNo;
	
	private String confirmAccountNo;
	
	private Boolean cashAmtYes;
	
	private Boolean nonCashAmtYes;
	
	/**
	 * Field that holds the reason for printing the slip
	 */
	private String reason = null;
	
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
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

	/**
	 * @return the windowNo
	 */
	public String getWindowNo() {
		return windowNo;
	}
	/**
	 * @param windowNo the windowNo to set
	 */
	public void setWindowNo(String windowNo) {
		this.windowNo = windowNo;
	}
	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		String oldValue = this.accountNo;
		String newValue = accountNo;
		this.accountNo = accountNo;
		firePropertyChange("accountNo", oldValue, newValue);	
	}
	/**
	 * @return the confirmAccountNo
	 */
	public String getConfirmAccountNo() {
		return confirmAccountNo;
	}
	/**
	 * @param confirmAccountNo the confirmAccountNo to set
	 */
	public void setConfirmAccountNo(String confirmAccountNo) {
		String oldValue = this.confirmAccountNo;
		String newValue = confirmAccountNo;
		this.confirmAccountNo = confirmAccountNo;
		firePropertyChange("confirmAccountNo", oldValue, newValue);	
	}
	/**
	 * @return the cashAmtYes
	 */
	public Boolean getCashAmtYes() {
		return cashAmtYes;
	}
	/**
	 * @param cashAmtYes the cashAmtYes to set
	 */
	public void setCashAmtYes(Boolean cashAmtYes) {
		this.cashAmtYes = cashAmtYes;
	}
	/**
	 * @return the nonCashAmtYes
	 */
	public Boolean getNonCashAmtYes() {
		return nonCashAmtYes;
	}
	/**
	 * @param nonCashAmtYes the nonCashAmtYes to set
	 */
	public void setNonCashAmtYes(Boolean nonCashAmtYes) {
		this.nonCashAmtYes = nonCashAmtYes;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		String oldValue = this.reason;
		String newValue = reason;
		this.reason = reason;
		firePropertyChange("reason", oldValue, newValue);	
	}	
	
	
}
