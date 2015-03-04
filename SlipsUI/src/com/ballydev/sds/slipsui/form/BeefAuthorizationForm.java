/*****************************************************************************
 * $Id: BeefAuthorizationForm.java,v 1.0, 2008-04-21 05:43:37Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:43:37 AM$
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
 * Form for the BeefAuthorizationForm
 * @author anantharajr
 * @version $Revision : $
 */
public class BeefAuthorizationForm extends SDSForm{
	/**
	 * Authorization EmployeeIdOne Form object
	 */
	private String authEmployeeIdOne;
	/**
	 * Authorization password One Form object
	 */
	private String authPasswordOne;	
	/**
	 * Authorization EmployeeId Two Form object
	 */
	private String authEmployeeIdTwo;
	/**
	 *  Authorization password Two Form object
	 */
	private String authPasswordTwo;
		
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
}
