/*****************************************************************************
 * $Id: MultiRedeemForm.java,v 1.1, 2010-01-12 08:43:47Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:43:47 AM$
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


public class MultiRedeemForm extends UIBaseForm
{
	private String voucherCoupon;
	
	private String amountTotal;

	public String getAmountTotal()
	{
		return amountTotal;
	}

	public void setAmountTotal(String amountTotal)
	{
		String oldValue = this.amountTotal;
		String newValue = amountTotal;
		this.amountTotal = amountTotal;
		firePropertyChange("amountTotal", oldValue, newValue);
	}

	public String getVoucherCoupon()
	{
		return voucherCoupon;
	}

	public void setVoucherCoupon(String voucherCoupon)
	{
		String oldValue = this.voucherCoupon;
		String newValue = voucherCoupon;
		this.voucherCoupon = voucherCoupon;
		firePropertyChange("voucherCoupon", oldValue, newValue);
	}
}
