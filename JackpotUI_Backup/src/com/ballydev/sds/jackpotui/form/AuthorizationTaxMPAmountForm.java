/*****************************************************************************
 * $Id: AuthorizationTaxMPAmountForm.java,v 1.2.1.0, 2013-10-12 09:01:12Z, SDS12.3.3 Checkin User$
 * $Date: 10/12/2013 4:01:12 AM$
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
 * Form for the AuthorizationComposite
 * @author dambereen
 * @version $Revision: 4$
 */
public class AuthorizationTaxMPAmountForm extends SDSForm{

	/**
	 * Authorization EmployeeIdOne Form object
	 */
	private String authEmployeeIdOne=null;
	/**
	 * Authorization password One Form object
	 */
	private String authPasswordOne=null;	
	/**
	 * Authorization EmployeeId Two Form object
	 */
	private String authEmployeeIdTwo=null;
	/**
	 *  Authorization password Two Form object
	 */
	private String authPasswordTwo=null;
	
		
	/**
	 * stateTax
	 */
	private Boolean stateTax = false;
	
	/**
	 * federalTax
	 */
	private Boolean federalTax = false; 
	
	/**
	 * statePlusFedTax
	 */
	private Boolean municipalTax = false;
	
	
	/**
	 * MVG Custom Fields
	 */
	private Boolean intercept = false;
	
	public Boolean getIntercept() {
		return intercept;
	}
	/**
	 * @param federalTax the federalTax to set
	 */
	public void setIntercept(Boolean intercept) {
		this.intercept = intercept;
	}	
	
	private String interceptAmount;
	
	
	public String getInterceptAmount(){
		return interceptAmount;		
	}
	
	public void setInterceptAmount(String interceptAmount){
		this.interceptAmount = interceptAmount;
	}	
	/**
	 * End MVG Custom Fields
	 */
	
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

	public Boolean getMunicipalTax() {
		return municipalTax;
	}
	public void setMunicipalTax(Boolean municipalTax) {
		this.municipalTax = municipalTax;
	}
	/**
	 * @return the statePlusFedTax
	 */
	public Boolean getStatePlusFedTax() {
		return municipalTax;
	}
	/**
	 * @param statePlusFedTax the statePlusFedTax to set
	 */
	public void setStatePlusFedTax(Boolean statePlusFedTax) {
		this.municipalTax = statePlusFedTax;
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
}
