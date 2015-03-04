package com.ballydev.sds.voucherui.form;

import com.ballydev.sds.framework.form.SDSForm;

public class ReportPreferenceForm extends SDSForm 
{
	private Boolean isRedeemed = false;
	private Boolean isCreated = false;
	private Boolean isVoided = false;
	private Boolean isDisplayAllBatches = false;
	
	public Boolean getIsCreated() 
	{
		return isCreated;
	}
	
	public void setIsCreated(Boolean isCreated) 
	{
		this.isCreated = isCreated;
	}
	
	public Boolean getIsDisplayAllBatches() 
	{
		return isDisplayAllBatches;
	}
	
	public void setIsDisplayAllBatches(Boolean isDisplayAllBatches) 
	{
		this.isDisplayAllBatches = isDisplayAllBatches;
	}
	
	public Boolean getIsRedeemed() 
	{
		return isRedeemed;
	}
	
	public void setIsRedeemed(Boolean isRedeemed) 
	{
		this.isRedeemed = isRedeemed;
	}
	
	public Boolean getIsVoided() 
	{
		return isVoided;
	}
	
	public void setIsVoided(Boolean isVoided) 
	{
		this.isVoided = isVoided;
	}
	
}
