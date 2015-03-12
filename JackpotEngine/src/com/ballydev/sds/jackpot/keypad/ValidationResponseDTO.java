package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * DTO to store/access validation status for requests. 
 * @author ranjithkumarm
 *
 */
public class ValidationResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 8220771533717670413L;
	
	private boolean validationSuccess=false;
	
	private String errorMessage=null;
	
	private int validationEvent=-1;
	
	//Initiation Request Validation success Fields
	
	private String employeeCardNo = null;
	
	private Integer employeeId = null;
	
	private String employeeFirstName = null;
	
	private String employeeLastName = null;
	
	private String employeeAuthorizationLevel=null;
	
	private String employeeAuthAmount=null;
	
	
	
	// Prompt Details Request fields.
	
    private String employeeCardNoAuth = null;
	
	private Integer employeeIdAuth = null;
	
	private String employeeFirstNameAuth = null;
	
	private String employeeLastNameAuth = null;
	
	

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getValidationEvent() {
		return validationEvent;
	}

	public void setValidationEvent(int validationEvent) {
		this.validationEvent = validationEvent;
	}

	public boolean isValidationSuccess() {
		return validationSuccess;
	}

	public void setValidationSuccess(boolean validationSuccess) {
		this.validationSuccess = validationSuccess;
	}

	public String getEmployeeCardNo() {
		return employeeCardNo;
	}

	public void setEmployeeCardNo(String employeeCardNo) {
		this.employeeCardNo = employeeCardNo;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getEmployeeCardNoAuth() {
		return employeeCardNoAuth;
	}

	public void setEmployeeCardNoAuth(String employeeCardNoAuth) {
		this.employeeCardNoAuth = employeeCardNoAuth;
	}

	public String getEmployeeFirstNameAuth() {
		return employeeFirstNameAuth;
	}

	public void setEmployeeFirstNameAuth(String employeeFirstNameAuth) {
		this.employeeFirstNameAuth = employeeFirstNameAuth;
	}

	public Integer getEmployeeIdAuth() {
		return employeeIdAuth;
	}

	public void setEmployeeIdAuth(Integer employeeIdAuth) {
		this.employeeIdAuth = employeeIdAuth;
	}

	public String getEmployeeLastNameAuth() {
		return employeeLastNameAuth;
	}

	public void setEmployeeLastNameAuth(String employeeLastNameAuth) {
		this.employeeLastNameAuth = employeeLastNameAuth;
	}

	public String getEmployeeAuthorizationLevel() {
		return employeeAuthorizationLevel;
	}

	public void setEmployeeAuthorizationLevel(String employeeAuthorizationLevel) {
		this.employeeAuthorizationLevel = employeeAuthorizationLevel;
	}

	public String getEmployeeAuthAmount() {
		return employeeAuthAmount;
	}

	public void setEmployeeAuthAmount(String employeeAuthAmount) {
		this.employeeAuthAmount = employeeAuthAmount;
	}
	
	

}
