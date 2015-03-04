/*****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.form;

import java.util.Date;
import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * This is the form class of the
 * reports composite
 * @author Nithya kalyani R
 * @version $Revision: 4$ 
 */
public class ReportsForm extends SDSForm {

	/**
	 * Instance of the the reportsList 
	 */
	@SuppressWarnings("unchecked")
	private List reportsList;

	/**
	 * Instance of the selected report
	 */
	private String selectedReport;

	/**
	 * Instance of the start time
	 */
	private Date startTime;

	/**
	 * Instance of the end time
	 */
	private Date endTime;

	/**
	 * Instance of the cashier id
	 */
	private String txtEmployeeId;

	/**
	 * Instance of the property id
	 */
	private String txtPropertyId;


	private Boolean employeeAllToggleBtn;

	private Boolean isVoucher;

	private Boolean isCashable;

	private Boolean isNonCashable;


	private Boolean detailBandToggleBtn = true;

	private Boolean summBandToggleBtn;



	/**
	 * @return the reportsList
	 */
	@SuppressWarnings("unchecked")
	public List getReportsList() {
		return reportsList;
	}
	/**
	 * @param reportsList the reportsList to set
	 */
	@SuppressWarnings("unchecked")
	public void setReportsList(List reportsList) {
		List oldValue = this.reportsList;
		this.reportsList = reportsList;
		firePropertyChange("reportsList", oldValue, reportsList);
	}
	/**
	 * @return the selectedReport
	 */
	public String getSelectedReport() {
		return selectedReport;
	}
	/**
	 * @param selectedReport the selectedReport to set
	 */
	public void setSelectedReport(String selectedReport) {
		String oldValue = this.selectedReport;
		String newValue = selectedReport;
		this.selectedReport = selectedReport;
		firePropertyChange("selectedReport", oldValue, newValue);
	}

	public String getTxtEmployeeId() {
		return txtEmployeeId;
	}

	public void setTxtEmployeeId(String employeeAssetId) {
		String oldValue = this.txtEmployeeId;
		String newValue = employeeAssetId;
		this.txtEmployeeId = employeeAssetId;
		firePropertyChange("employeeAssetId", oldValue, newValue);
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the txtPropertyId
	 */
	public String getTxtPropertyId() {
		return txtPropertyId;
	}
	/**
	 * @param txtPropertyId the txtPropertyId to set
	 */
	public void setTxtPropertyId(String txtPropertyId) {
		String oldValue = this.txtPropertyId;
		String newValue = txtPropertyId;
		this.txtPropertyId = txtPropertyId;
		firePropertyChange("txtPropertyId", oldValue, newValue);
	}
	/**
	 * @return the employeeAssetToggleBtn
	 */
	public Boolean getEmployeeAllToggleBtn() {
		return employeeAllToggleBtn;
	}
	/**
	 * @param employeeAssetToggleBtn the employeeAssetToggleBtn to set
	 */
	public void setEmployeeAllToggleBtn(Boolean employeeAllToggleBtn) {
		this.employeeAllToggleBtn = employeeAllToggleBtn;
	}
	/**
	 * @return the isVoucher
	 */
	public Boolean getIsVoucher() {
		return isVoucher;
	}
	/**
	 * @param isVoucher the isVoucher to set
	 */
	public void setIsVoucher(Boolean isVoucher) {
		this.isVoucher = isVoucher;
	}
	/**
	 * @return the isCashable
	 */
	public Boolean getIsCashable() {
		return isCashable;
	}
	/**
	 * @param isCashable the isCashable to set
	 */
	public void setIsCashable(Boolean isCashable) {
		this.isCashable = isCashable;
	}
	/**
	 * @return the isNonCashable
	 */
	public Boolean getIsNonCashable() {
		return isNonCashable;
	}
	/**
	 * @param isNonCashable the isNonCashable to set
	 */
	public void setIsNonCashable(Boolean isNonCashable) {
		this.isNonCashable = isNonCashable;
	}
	/**
	 * @return the detailBandToggleBtn
	 */
	public Boolean getDetailBandToggleBtn() {
		return detailBandToggleBtn;
	}
	/**
	 * @param detailBandToggleBtn the detailBandToggleBtn to set
	 */
	public void setDetailBandToggleBtn(Boolean detailBandToggleBtn) {	
		this.detailBandToggleBtn = detailBandToggleBtn;
	}
	/**
	 * @return the summBandToggleBtn
	 */
	public Boolean getSummBandToggleBtn() {
		return summBandToggleBtn;
	}
	/**
	 * @param summBandToggleBtn the summBandToggleBtn to set
	 */
	public void setSummBandToggleBtn(Boolean summBandToggleBtn) {
		this.summBandToggleBtn = summBandToggleBtn;
	}
}
