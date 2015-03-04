/*****************************************************************************
 * $Id: RedeemVoucherForm.java,v 1.2.3.0, 2013-10-28 18:29:05Z, Sornam, Ramanathan$
 * $Date: 10/28/2013 12:29:05 PM$
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

import com.ballydev.sds.framework.LabelLoader;

public class RedeemVoucherForm extends UIBaseForm
{
	private String barCode;
	private String amount;
	private String effectiveDate;
	private String expireDate;
	private String state;
	private String location;
	private String playerId;
	private String ticketSeed;
	private boolean isPlayerCardRequired;
	private String createdPlayerCardId;
	private boolean isVoid;
	
	
	public String getCreatedPlayerCardId() {
		return createdPlayerCardId;
	}

	public void setCreatedPlayerCardId(String createdPlayerCardId) {
		this.createdPlayerCardId = createdPlayerCardId;
	}

	public boolean isPlayerCardRequired() {
		return isPlayerCardRequired;
	}

	public void setPlayerCardRequired(boolean isPlayerCardRequired) {
		this.isPlayerCardRequired = isPlayerCardRequired;
	}

	public String getTicketSeed() {
		return ticketSeed;
	}

	public void setTicketSeed(String ticketSeed) {
		this.ticketSeed = ticketSeed;
	}

	public String getAmount() 
	{
		return amount;
	}
	
	public void setAmount(String amount) 
	{
		String oldValue = this.amount;
		String newValue = amount;
		this.amount = amount;
		firePropertyChange("amount", oldValue, newValue);
	}
	
	public String getBarCode() 
	{
		return barCode;
	}
	
	@SuppressWarnings("unused")
	public void setBarCode(String barCode) 
	{
		String oldValue = this.barCode;
		String newValue = barCode;
		this.barCode = barCode;
		//firePropertyChange("barCode", oldValue, newValue);
	}
	
	public String getEffectiveDate() 
	{
		return effectiveDate;
	}
	
	public void setEffectiveDate(String effectiveDate) 
	{
		String oldValue = this.effectiveDate;
		String newValue = effectiveDate;
		this.effectiveDate = effectiveDate;
		firePropertyChange("effectiveDate", oldValue, newValue);
	}
	
	public String getExpireDate() 
	{
		return expireDate;
	}
	
	public void setExpireDate(String expireDate) 
	{
		String oldValue = this.expireDate;
		String newValue = expireDate;
		this.expireDate = expireDate;
		firePropertyChange("expireDate", oldValue, newValue);
	}
	
	public String getLocation() 
	{
		return location;
	}
	
	public void setLocation(String location) 
	{
		String oldValue = this.location;
		String newValue = location;
		this.location = location;
		firePropertyChange("location", oldValue, newValue);
	}
	
	public String getPlayerId() 
	{
		return playerId;
	}
	
	@SuppressWarnings("unused")
	public void setPlayerId(String playerId) 
	{
		String oldValue = this.playerId;
		String newValue = playerId;
		this.playerId = playerId;
		//firePropertyChange("playerId", oldValue, newValue);
	}
	
	public String getState() 
	{
		return state;
	}
	
	public void setState(String state) 
	{
		String oldValue = this.state;
		String newValue = LabelLoader.getLabelValue(state);
		this.state = LabelLoader.getLabelValue(state);
		firePropertyChange("state", oldValue, newValue);
	}
	
	//since multi redeem composite is part of this main composite ... it needs these methos to set
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

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}
}
