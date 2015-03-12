package com.ballydev.sds.jackpot.keypad;

import java.io.Serializable;

/**
 * Object Class for the InitiationRequest.
 * 
 * @author ranjithkumarm
 *
 */
public class InitiationRequestObject implements ICustomRequest,Serializable{

	
	private static final long serialVersionUID = -4275651872020255082L;
	
	private String employeeIdCardInserted=null;

	public String getEmployeeIdCardInserted() {
		return employeeIdCardInserted;
	}

	public void setEmployeeIdCardInserted(String employeeIdCardInserted) {
		this.employeeIdCardInserted = employeeIdCardInserted;
	}

}
