/*****************************************************************************
 * $Id: PrintVoucherForm.java,v 1.0, 2008-03-27 12:19:55Z, Nithyakalyani, Raman$
 * $Date: 3/27/2008 6:19:55 AM$
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


public class PrintVoucherForm extends UIBaseForm
{
	private String ticketAmount;
	
	private String count;
	
	private String totalAmount;
	
	private String totalCount;
	
	private String totalTktsToPrint;

	public String getCount() {
		return count;
	}

	public void setCount(String count) 
	{
		String oldValue = this.count;
		String newValue = count;
		this.count = count;
		firePropertyChange("count", oldValue, newValue);
	}

	public String getTicketAmount() {
		return ticketAmount;
	}

	public void setTicketAmount(String ticketAmount) 
	{
		String oldValue = this.ticketAmount;
		String newValue = ticketAmount;
		this.ticketAmount = ticketAmount;
		firePropertyChange("ticketAmount", oldValue, newValue);
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		String oldValue = this.totalAmount;
		String newValue = totalAmount;
		this.totalAmount = totalAmount;
		firePropertyChange("totalAmount", oldValue, newValue);
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		String oldValue = this.totalCount;
		String newValue = totalCount;
		this.totalCount = totalCount;
		firePropertyChange("totalCount", oldValue, newValue);
	}

	/**
	 * @return the totalTktsToPrint
	 */
	public String getTotalTktsToPrint() {
		return totalTktsToPrint;
	}

	/**
	 * @param totalTktsToPrint the totalTktsToPrint to set
	 */
	public void setTotalTktsToPrint(String totalTktsToPrint) {
		String oldValue = this.totalTktsToPrint;
		String newValue = totalTktsToPrint;
		this.totalTktsToPrint = totalTktsToPrint;
		firePropertyChange("totalTktsToPrint", oldValue, newValue);

	}

}
