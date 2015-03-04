package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class BarcodeScannerPreferenceForm extends SDSForm 
{
	private String testStatus;
	private List<ComboLabelValuePair> scannerPortList;
	private String selectedScannerPort;
	private List<ComboLabelValuePair> scannerTypeList;
	private String selectedScannerType;
	private String manufacturer;
	private String model;
	private String driver;
	private Boolean isBarcodeScanner = false;
	private String scannerPort;
	private String scannerType;
	
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
	
	public Boolean getIsBarcodeScanner() 
	{
		return isBarcodeScanner;
	}
	
	public void setIsBarcodeScanner(Boolean isBarcodeScanner) 
	{
		this.isBarcodeScanner = isBarcodeScanner;
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
	
	@SuppressWarnings("unchecked")
	public List getScannerPortList() 
	{
		return scannerPortList;
	}
	
	@SuppressWarnings("unchecked")
	public void setScannerPortList(List<ComboLabelValuePair> scannerPortList) 
	{
		List oldValue = this.scannerPortList;
		this.scannerPortList = scannerPortList;
		firePropertyChange("scannerPortList", oldValue, scannerPortList);
	}
	
	@SuppressWarnings("unchecked")
	public List getScannerTypeList() 
	{
		return scannerTypeList;
	}
	
	@SuppressWarnings("unchecked")
	public void setScannerTypeList(List<ComboLabelValuePair> scannerTypeList) 
	{
		List oldValue = this.scannerTypeList;
		this.scannerTypeList = scannerTypeList;
		firePropertyChange("scannerTypeList", oldValue, scannerTypeList);
	}
	
	public String getSelectedScannerPort() 
	{
		return selectedScannerPort;
	}
	
	public void setSelectedScannerPort(String selectedScannerPort) 
	{
		this.selectedScannerPort = selectedScannerPort;
	}
	
	public String getSelectedScannerType() 
	{
		return selectedScannerType;
	}
	
	public void setSelectedScannerType(String selectedScannerType) 
	{
		this.selectedScannerType = selectedScannerType;
	}
	
	public String getTestStatus() 
	{
		return testStatus;
	}
	
	public void setTestStatus(String testStatus) 
	{
		String oldValue = this.testStatus;
		String newValue = testStatus;
		this.testStatus = testStatus;
		firePropertyChange("testStatus", oldValue, newValue);
	}

	public String getScannerPort() {
		return scannerPort;
	}

	public void setScannerPort(String scannerPort) {
		this.scannerPort = scannerPort;
	}

	public String getScannerType() {
		return scannerType;
	}

	public void setScannerType(String scannerType) {
		this.scannerType = scannerType;
	}
}
