package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class TicketPrinterPreferenceForm extends SDSForm 
{
	private String maxLimit;
	private List<ComboLabelValuePair> printerPortList;
	private String selectedPrinterPort;
	private List<ComboLabelValuePair> printerTypeList;
	private String selectedPrinterType;
	private String manufacturer;
	private String model;
	private String driver;
	private Boolean isSmallSize = false;
	private Boolean isNormalSize = false;
	private Boolean isTicketPrinting = false;
	private Boolean isMultipleTickets = false;
	private Boolean isSecurePrint = false;
	private String printerPort;
	private String testStatus;
	private String printerType;
	
	public String getDriver() 
	{
		return driver;
	}
	
	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		String oldValue = this.testStatus;
		String newValue = testStatus;
		this.testStatus = testStatus;
		firePropertyChange("testStatus", oldValue, newValue);
	}

	public void setDriver(String driver) 
	{
		String oldValue = this.driver;
		String newValue = driver;
		this.driver = driver;
		firePropertyChange("driver", oldValue, newValue);
	}
	
	public Boolean getIsMultipleTickets() 
	{
		return isMultipleTickets;
	}
	
	public void setIsMultipleTickets(Boolean isMultipleTickets) 
	{
		this.isMultipleTickets = isMultipleTickets;
	}
	
	public Boolean getIsSecurePrint() 
	{
		return isSecurePrint;
	}
	
	public void setIsSecurePrint(Boolean isSecurePrint)
	{
		this.isSecurePrint = isSecurePrint;
	}
	
	public Boolean getIsSmallSize() {
		return isSmallSize;
	}

	public void setIsSmallSize(Boolean isSmallSize) {
		this.isSmallSize = isSmallSize;
	}

	public Boolean getIsNormalSize() {
		return isNormalSize;
	}

	public void setIsNormalSize(Boolean isNormalSize) {
		this.isNormalSize = isNormalSize;
	}

	public Boolean getIsTicketPrinting() 
	{
		return isTicketPrinting;
	}
	
	public void setIsTicketPrinting(Boolean isTicketPrinting) 
	{
		this.isTicketPrinting = isTicketPrinting;
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
	
	public String getMaxLimit() 
	{
		return maxLimit;
	}
	
	public void setMaxLimit(String maxLimit) 
	{
		String oldValue = this.maxLimit;
		String newValue = maxLimit;
		this.maxLimit = maxLimit;
		firePropertyChange("maxLimit", oldValue, newValue);
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
