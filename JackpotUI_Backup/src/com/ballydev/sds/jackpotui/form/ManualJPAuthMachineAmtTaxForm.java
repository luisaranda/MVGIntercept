package com.ballydev.sds.jackpotui.form;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * @author anantharajr
 *	@version $Revision: 4$
 */
public class ManualJPAuthMachineAmtTaxForm extends SDSForm {

	/**
	 * String that holds the first Authorization employee id
	 */
	private String authEmployeeIdOne;
	
	/**
	 * String that holds the first Authorization employee pwd
	 */
	private String authPasswordOne;
	
	/**
	 * String that holds the second Authorization employee id
	 */
	private String authEmployeeIdTwo;
	
	/**
	 * String that holds the second Authorization employee pwd
	 */
	private String authPasswordTwo;
	

	
	/**
	 * This field holds true if user selected state tax
	 */
	private Boolean stateTax = false;
	
	/**
	 * This field holds true if user selected federal tax
	 */
	private Boolean federalTax = false;
	
	/**
	 * This field holds true if user selected state plus federal tax
	 */
	private Boolean municipalTax = false;

	/**
	 * This field holds the machine paid amount
	 */
	private String machinePaidAmount;
	
	public Boolean getMunicipalTax() {
		return municipalTax;
	}

	public void setMunicipalTax(Boolean municipalTax) {
		this.municipalTax = municipalTax;
	}

	/**
	 * @return the authEmployeeIdOne
	 */
	public String getAuthEmployeeIdOne() {
		return authEmployeeIdOne;
	}

	/**
	 * @param authEmployeeIdOne the authEmployeeIdOne to set
	 */
	public void setAuthEmployeeIdOne(String authEmployeeIdOne) {
		String oldValue = this.authEmployeeIdOne;
		String newValue = authEmployeeIdOne;
		this.authEmployeeIdOne = authEmployeeIdOne;
		firePropertyChange("authEmployeeIdOne", oldValue, newValue);
	}

	/**
	 * @return the authEmployeeIdTwo
	 */
	public String getAuthEmployeeIdTwo() {
		return authEmployeeIdTwo;
	}

	/**
	 * @param authEmployeeIdTwo the authEmployeeIdTwo to set
	 */
	public void setAuthEmployeeIdTwo(String authEmployeeIdTwo) {
		String oldValue = this.authEmployeeIdTwo;
		String newValue = authEmployeeIdTwo;
		this.authEmployeeIdTwo = authEmployeeIdTwo;
		firePropertyChange("authEmployeeIdTwo", oldValue, newValue);
	}

	/**
	 * @return the authPasswordOne
	 */
	public String getAuthPasswordOne() {
		return authPasswordOne;
	}

	/**
	 * @param authPasswordOne the authPasswordOne to set
	 */
	public void setAuthPasswordOne(String authPasswordOne) {
		String oldValue = this.authPasswordOne;
		String newValue = authPasswordOne;
		this.authPasswordOne = authPasswordOne;
		firePropertyChange("authPasswordOne", oldValue, newValue);
	}

	/**
	 * @return the authPasswordTwo
	 */
	public String getAuthPasswordTwo() {
		return authPasswordTwo;
	}

	/**
	 * @param authPasswordTwo the authPasswordTwo to set
	 */
	public void setAuthPasswordTwo(String authPasswordTwo) {
		String oldValue = this.authPasswordTwo;
		String newValue = authPasswordTwo;
		this.authPasswordTwo = authPasswordTwo;
		firePropertyChange("authPasswordTwo", oldValue, newValue);
	}

	/**
	 * @return the federalTax
	 */
	public Boolean getFederalTax() {
		return federalTax;
	}

	/**
	 * @param federalTax the federalTax to set
	 */
	public void setFederalTax(Boolean federalTax) {
		this.federalTax = federalTax;
	}

	
	/**
	 * @return the stateTax
	 */
	public Boolean getStateTax() {
		return stateTax;
	}

	/**
	 * @param stateTax the stateTax to set
	 */
	public void setStateTax(Boolean stateTax) {
		this.stateTax = stateTax;
	}


	/**
	 * @return the machinePaidAmount
	 */
	/*public String getMachinePaidAmount() {
		return machinePaidAmount;
	}

	*//**
	 * @param machinePaidAmount the machinePaidAmount to set
	 *//*
	public void setMachinePaidAmount(String machinePaidAmount) {
		String oldValue = this.machinePaidAmount;
		String newValue = machinePaidAmount;
		this.machinePaidAmount = machinePaidAmount;
		firePropertyChange("machinePaidAmount", oldValue, newValue);
	}*/
	
}
