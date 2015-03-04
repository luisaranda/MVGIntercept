/*****************************************************************************
 * $Id: BaseReconciliationForm.java,v 1.2, 2008-05-29 13:46:30Z, Nithyakalyani, Raman$
 * $Date: 5/29/2008 8:46:30 AM$
 *****************************************************************************
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

public class BaseReconciliationForm extends UIBaseForm
{
	private Date startTime;

	private Date endTime;

	private String barcode;

	private String accepted;

	private String notRead;

	private String total;

	private String uncommitted;

	private String scannedDetailsList;

	private String employeeAssetId;

	private boolean isCashier=true;





	public boolean isCashier() {
		return isCashier;
	}

	public void setCashier(boolean isCashier) {
		this.isCashier = isCashier;
	}

	public String getEmployeeAssetId() {
		return employeeAssetId;
	}

	public void setEmployeeAssetId(String employeeAssetId) {
		this.employeeAssetId = employeeAssetId;
	}

	public String getAccepted() {
		return accepted;
	}

	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getNotRead() {
		return notRead;
	}

	public void setNotRead(String notRead) {
		this.notRead = notRead;
	}

	public String getScannedDetailsList() {
		return scannedDetailsList;
	}

	public void setScannedDetailsList(String scannedDetailsList) {
		this.scannedDetailsList = scannedDetailsList;
	}


	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getUncommitted() {
		return uncommitted;
	}

	public void setUncommitted(String uncommitted) {
		this.uncommitted = uncommitted;
	}






}
