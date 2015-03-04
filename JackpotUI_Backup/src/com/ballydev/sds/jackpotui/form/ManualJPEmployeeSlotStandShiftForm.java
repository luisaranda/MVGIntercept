package com.ballydev.sds.jackpotui.form;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * ManualJPEmployeeSlotStandShiftForm
 * @author anantharajr
 * @version $Revision: 2$
 */
public class ManualJPEmployeeSlotStandShiftForm extends SDSForm {

	/**
	 * String to hold the employee id
	 */
	private String employeeId;
	
	/**
	 * String to hold the employee password
	 */
	private String empPassword;
	
	/**
	 * String to hold the slot no
	 */
	private String slotNo;
	
	/**
	 * String to hold the slot location no
	 */
	private String slotLocationNo;
	
	/**
	 * This field holds true if user selected the day field
	 */
	private Boolean dayYes = true;
	
	/**
	 * This field holds true if user selected the swing field
	 */
	private Boolean swingYes = false;
	
	/**
	 * This field holds true if user selected the graveyard field
	 */
	private Boolean graveyardYes = false;	
	
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
		this.slotNo = slotNo;
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
}
