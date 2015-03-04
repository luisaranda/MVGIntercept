/**
 * 
 */
package com.ballydev.sds.slipsui.form;

import java.util.Date;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * @author gsrinivasulu
 * @version $Revision :1$
 */
public class ReportForm extends SDSForm {
	
	/**
	 * Employee Id instance
	 */
	private String employeeId;
	
	/**
	 * From Date Instance
	 */
	private Date fromDate;
	
	/**
	 * To Date Instance
	 */
	private Date toDate;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
