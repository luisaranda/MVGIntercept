package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class MultiareaPreferenceForm extends SDSForm 
{
	private List<ComboLabelValuePair> multiareaIdList;
	private List<ComboLabelValuePair> siteNameList;
	private String siteName;
//	private String selectedMultiareaId;
	private String propertyName;
	private String propertyId;
	private Boolean isMultiarea = false;
	private String multiareaId;
	
		
	public String getPropertyName() 
	{
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) 
	{
		String oldValue = this.propertyName;
		String newValue = propertyName;
		this.propertyName = propertyName;
		firePropertyChange("propertyName", oldValue, newValue);
	}
	
	public String getPropertyId() 
	{
		return propertyId;
	}
	
	public void setPropertyId(String propertyId) 
	{
		String oldValue = this.propertyId;
		String newValue = propertyId;
		this.propertyId = propertyId;
		firePropertyChange("propertyId", oldValue, newValue);
	}
	
	public Boolean getIsMultiarea() 
	{
		return isMultiarea;
	}
	
	public void setIsMultiarea(Boolean isMultiarea) 
	{
		this.isMultiarea = isMultiarea;
	}


	public List<ComboLabelValuePair> getMultiareaIdList() 
	{
		return multiareaIdList;
	}
	
	@SuppressWarnings("unchecked")
	public void setMultiareaIdList(List<ComboLabelValuePair> multiareaIdList) 
	{
		List oldValue = this.multiareaIdList;
		this.multiareaIdList = multiareaIdList;
		firePropertyChange("multiareaIdList", oldValue, multiareaIdList);
	}
	
//	public String getSelectedMultiareaId() 
//	{
//		return selectedMultiareaId;
//	}
//	
//	public void setSelectedMultiareaId(String selectedMultiareaId) 
//	{
//		this.selectedMultiareaId = selectedMultiareaId;
//	}

	public String getMultiareaId() {
		return multiareaId;
	}

	public void setMultiareaId(String multiareaId) {
		this.multiareaId = multiareaId;
	}

	/**
	 * @return the siteNameIdList
	 */
	public List<ComboLabelValuePair> getSiteNameList() {
		return siteNameList;
	}

	/**
	 * @param siteNameIdList the siteNameIdList to set
	 */
	@SuppressWarnings("unchecked")
	public void setSiteNameList(List<ComboLabelValuePair> siteNameList) {
		List oldValue = this.siteNameList;
		this.siteNameList = siteNameList;
		firePropertyChange("siteNameIdList", oldValue, siteNameList);
	}

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

}
