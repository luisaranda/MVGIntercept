/*****************************************************************************
 * $Id: VerifyOTForm.java,v 1.0, 2008-03-27 12:19:01Z, Nithyakalyani, Raman$Date: 
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

import com.ballydev.sds.framework.form.SDSForm;

public class VerifyOTForm extends SDSForm 
{

	private String ticketBarcodeValue;
	
	private String ticketAmountValue;

	public String getTicketBarcodeValue() 
	{
		
		return ticketBarcodeValue;
	}

	public void setTicketBarcodeValue(String ticketBarcodeValue) 
	{
		String oldValue = this.ticketBarcodeValue;
		String newValue = ticketBarcodeValue;
		this.ticketBarcodeValue = ticketBarcodeValue;
		firePropertyChange("ticketBarcodeValue", oldValue, newValue);
		
	}

	public String getTicketAmountValue() 
	{
		return ticketAmountValue;
	}

	public void setTicketAmountValue(String ticketAmountValue) 
	{
		String oldValue = this.ticketAmountValue;
		String newValue = ticketAmountValue;
		this.ticketAmountValue = ticketAmountValue;
		firePropertyChange("ticketAmountValue", oldValue, newValue);
	}
}
