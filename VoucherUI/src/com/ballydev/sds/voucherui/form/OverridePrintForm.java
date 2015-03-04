/*****************************************************************************
 * $Id: OverridePrintForm.java,v 1.0, 2009-01-20 07:57:07Z, Nithyakalyani, Raman$
 * $Date: 1/20/2009 1:57:07 AM$
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



public class OverridePrintForm extends UIBaseForm {
	
	private String userId;
	
	private String password;	

	
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
}
