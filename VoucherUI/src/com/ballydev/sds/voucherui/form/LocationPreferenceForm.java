package com.ballydev.sds.voucherui.form;

import com.ballydev.sds.framework.form.SDSForm;

public class LocationPreferenceForm extends SDSForm 
{
	private String locationId;
	private String locationTimeOut;
	private Boolean isCashier = false;
	private Boolean isAuditor = false;
	private Boolean isAdmin = false;
	
	public Boolean getIsAdmin() 
	{
		return isAdmin;
	}
	
	public void setIsAdmin(Boolean isAdmin) 
	{
		this.isAdmin = isAdmin;
	}
	
	public Boolean getIsAuditor() 
	{
		return isAuditor;
	}
	
	public void setIsAuditor(Boolean isAuditor) 
	{
		this.isAuditor = isAuditor;
	}
	
	public Boolean getIsCashier() 
	{
		return isCashier;
	}
	
	public void setIsCashier(Boolean isCashier) 
	{
		this.isCashier = isCashier;
	}
	
	public String getLocationId() 
	{
		return locationId;
	}
	
	public void setLocationId(String locationId) 
	{
		String oldValue = this.locationId;
		String newValue = locationId;
		this.locationId = locationId;
		firePropertyChange("locationId", oldValue, newValue);
	}
	
	public String getLocationTimeOut() 
	{
		return locationTimeOut;
	}
	
	public void setLocationTimeOut(String locationTimeOut) 
	{
		String oldValue = this.locationTimeOut;
		String newValue = locationTimeOut;
		this.locationTimeOut = locationTimeOut;
		firePropertyChange("locationTimeOut", oldValue, newValue);
	}
}
