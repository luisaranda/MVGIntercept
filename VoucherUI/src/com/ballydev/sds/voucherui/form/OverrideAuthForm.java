/*****************************************************************************
 * $Id: OverrideAuthForm.java,v 1.3, 2010-06-16 14:57:33Z, Verma, Nitin Kumar$
 * $Date: 6/16/2010 9:57:33 AM$
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

import java.util.List;

import com.ballydev.sds.voucher.dto.TransactionReasonInfoDTO;


public class OverrideAuthForm extends UIBaseForm
{
	private String userId;
	
	private String password;
	private List<TransactionReasonInfoDTO> reasonList;
	private String selectedReasonValue;
	private String reasonForOverride;
	
	
	@SuppressWarnings("unchecked")
	public List getReasonList() 
	{
		return reasonList;
	}

	@SuppressWarnings("unchecked")
	public void setReasonList(List reasonList) 
	{
		List oldValue = this.reasonList;
		this.reasonList = reasonList;
		firePropertyChange("reasonList", oldValue, reasonList);
	}
	
	public String getSelectedReasonValue(){
		return selectedReasonValue;
	}

	public void setSelectedReasonValue(String selectedReasonValue){
		
		this.selectedReasonValue = selectedReasonValue;
	}
	
	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		String oldValue = this.password;
		String newValue = password;
		this.password = password;
		firePropertyChange("password", oldValue, newValue);
	}

	public String getUserId() 
	{
		return userId;
	}

	public void setUserId(String userId) 
	{
		String oldValue = this.userId;
		String newValue = userId;
		this.userId = userId;
		firePropertyChange("userId", oldValue, newValue);
	}

	/**
	 * @return the reasonForOverride
	 */
	public String getReasonForOverride() {
		return reasonForOverride;
	}

	/**
	 * @param reasonForOverride the reasonForOverride to set
	 */
	public void setReasonForOverride(String reasonForOverride) {
		this.reasonForOverride = reasonForOverride;
	}
}
