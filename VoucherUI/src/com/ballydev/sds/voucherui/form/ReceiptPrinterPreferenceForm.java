/*****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class ReceiptPrinterPreferenceForm extends SDSForm {
	private List<ComboLabelValuePair> printerPortList;
	private String selectedPrinterPort;
	private List<ComboLabelValuePair> printerTypeList;
	private String selectedPrinterType;
	private String manufacturer;
	private String model;
	private String driver;
	private Boolean isReceiptPrinting = false;
	private String printerPort;
	private String printerType;
	private String testStatus;

	/**
	 * @return the testStatus
	 */
	public String getTestStatus() {
		return testStatus;
	}

	/**
	 * @param testStatus the testStatus to set
	 */
	public void setTestStatus(String testStatus) {
		String oldValue = this.testStatus;
		String newValue = testStatus;
		this.testStatus = testStatus;
		firePropertyChange("testStatus", oldValue, newValue);
	}

	public String getDriver() 
	{
		return driver;
	}

	public void setDriver(String driver) 
	{
		String oldValue = this.driver;
		String newValue = driver;
		this.driver = driver;
		firePropertyChange("driver", oldValue, newValue);
	}



	public String getManufacturer() 
	{
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) 
	{
		String oldValue = this.manufacturer;
		String newValue = manufacturer;
		this.manufacturer = manufacturer;
		firePropertyChange("manufacturer", oldValue, newValue);
	}

	public String getModel() 
	{
		return model;
	}

	public void setModel(String model) 
	{
		String oldValue = this.model;
		String newValue = model;
		this.model = model;
		firePropertyChange("model", oldValue, newValue);
	}

	public Boolean getIsReceiptPrinting() 
	{
		return isReceiptPrinting;
	}

	public void setIsReceiptPrinting(Boolean isReceiptPrinting) 
	{
		this.isReceiptPrinting = isReceiptPrinting;
	}

	@SuppressWarnings("unchecked")
	public List getPrinterPortList() 
	{
		return printerPortList;
	}

	@SuppressWarnings("unchecked")
	public void setPrinterPortList(List<ComboLabelValuePair> printerPortList) 
	{
		List oldValue = this.printerPortList;
		this.printerPortList = printerPortList;
		firePropertyChange("printerPortList", oldValue, printerPortList);
	}

	@SuppressWarnings("unchecked")
	public List getPrinterTypeList() 
	{
		return printerTypeList;
	}

	@SuppressWarnings("unchecked")
	public void setPrinterTypeList(List<ComboLabelValuePair> printerTypeList) 
	{
		List oldValue = this.printerTypeList;
		this.printerTypeList = printerTypeList;
		firePropertyChange("printerTypeList", oldValue, printerTypeList);
	}

	public String getSelectedPrinterPort() 
	{
		return selectedPrinterPort;
	}

	public void setSelectedPrinterPort(String selectedPrinterPort) 
	{
		this.selectedPrinterPort = selectedPrinterPort;
	}

	public String getSelectedPrinterType() 
	{
		return selectedPrinterType;
	}

	public void setSelectedPrinterType(String selectedPrinterType) 
	{
		this.selectedPrinterType = selectedPrinterType;
	}

	public String getPrinterPort() {
		return printerPort;
	}

	public void setPrinterPort(String printerPort) {
		this.printerPort = printerPort;
	}

	public String getPrinterType() {
		return printerType;
	}

	public void setPrinterType(String printerType) {
		this.printerType = printerType;
	}
}
