package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;

public class CustomerDisplayPreferenceForm extends SDSForm 
{
	private String testStatus;
	private String welcomeMessage;
	@SuppressWarnings("unchecked")
	private List displayTypeList;
	private String selectedDisplayType;
	@SuppressWarnings("unchecked")
	private List portList;
	private String selectedPort;
	private String manufacturer;
	private String model;
	private String driver;
	private Boolean isCustomerDisplay = false;
	private String port;
	private String displayType;
	private Boolean isCenterDisplay = false;
	private Boolean isScrollDisplay = false;

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

	@SuppressWarnings("unchecked")
	public List getDisplayTypeList() 
	{
		return displayTypeList;
	}

	@SuppressWarnings("unchecked")
	public void setDisplayTypeList(List displayTypeList) 
	{
		List oldValue = this.displayTypeList;
		this.displayTypeList = displayTypeList;
		firePropertyChange("displayTypeList", oldValue, displayTypeList);
	}

	public Boolean getIsCustomerDisplay() 
	{
		return isCustomerDisplay;
	}

	public void setIsCustomerDisplay(Boolean isCustomerDisplay) 
	{
		this.isCustomerDisplay = isCustomerDisplay;
	}

	@SuppressWarnings("unchecked")
	public List getPortList() 
	{
		return portList;
	}

	@SuppressWarnings("unchecked")
	public void setPortList(List portList) 
	{
		List oldValue = this.portList;
		this.portList = portList;
		firePropertyChange("portList", oldValue, portList);
	}

	public String getSelectedDisplayType() 
	{
		return selectedDisplayType;
	}

	public void setSelectedDisplayType(String selectedDisplayType) 
	{
		this.selectedDisplayType = selectedDisplayType;
	}

	public String getSelectedPort() 
	{
		return selectedPort;
	}

	public void setSelectedPort(String selectedPort) 
	{
		this.selectedPort = selectedPort;
	}

	public String getWelcomeMessage() 
	{
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) 
	{
		String oldValue = this.welcomeMessage;
		String newValue = welcomeMessage;
		this.welcomeMessage = welcomeMessage;
		firePropertyChange("welcomeMessage", oldValue, newValue);
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the isCenterDisplay
	 */
	public Boolean getIsCenterDisplay() {
		return isCenterDisplay;
	}

	/**
	 * @param isCenterDisplay the isCenterDisplay to set
	 */
	public void setIsCenterDisplay(Boolean isCenterDisplay) {
		this.isCenterDisplay = isCenterDisplay;
	}

	/**
	 * @return the isScrollDisplay
	 */
	public Boolean getIsScrollDisplay() {
		return isScrollDisplay;
	}

	/**
	 * @param isScrollDisplay the isScrollDisplay to set
	 */
	public void setIsScrollDisplay(Boolean isScrollDisplay) {
		this.isScrollDisplay = isScrollDisplay;
	}
}
