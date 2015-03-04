/*****************************************************************************
 * $Id: JackpotSlipReportForm.java,v 1.0, 2008-04-03 15:53:02Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:53:02 AM$
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

import java.util.Date;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * This class contains the properties coresponding to the controls in the
 * VoidComposite
 * @author dambereen
 * @version $Revision: 1$
 */
public class JackpotSlipReportForm extends SDSForm{

	/**
	 * employeeId form object
	 */
	private String employeeId;
	
	/**
	 * startDate
	 */
	private Date fromDate;
	
	/**
	 * endDate 
	 */
	private Date toDate;

	
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
	 * @return the endDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the endDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the startDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the startDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	
}
