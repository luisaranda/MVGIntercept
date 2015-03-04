/*****************************************************************************
 * $Id: AmountSlotAttendantIdForm.java,v 1.3.1.0, 2011-05-17 20:01:53Z, SDS12.3.2CheckinUser$
 * $Date: 5/17/2011 3:01:53 PM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpotui.form;

import java.util.Date;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.jackpotui.constants.FormConstants;

/**
 * This form object stores the adjusted hand paid jackpot amount.
 * 
 * @author anantharajr
 * @version $Revision: 5$
 */
public class AmountSlotAttendantIdForm extends SDSForm {
	/**
	 * this field holds the adjusted hand paid jackpot amount.
	 */
	private String handPaidAmount;

	/**
	 * slotAttendantId
	 */
	private String slotAttendantId;
	
	private String sdsCalculatedAmount = null;
	
	private String amountAuthEmpId = null;
	
	private String amountAuthPassword = null;
	/**
	 * expiryDate
	 */
	private Date expiryDate;
	
	/**
	 * Cashless Account Number
	 */
	private String accountNumber;
	
	/**
	 * Confirm Cardless Account Number
	 */
	private String confirmAccountNumber;
	
	
	/**
	 * machinePaidAmount
	 */
	//private String machinePaidAmount;

	

	/**
	 * @return the slotAttendantId
	 */
	public String getSlotAttendantId() {
		return slotAttendantId;
	}

	/**
	 * @param slotAttendantId the slotAttendantId to set
	 */
	public void setSlotAttendantId(String slotAttendantId) {
		this.slotAttendantId = slotAttendantId;
	}

	/**
	 * @return the handPaidAmount
	 */
	public String getHandPaidAmount() {
		return handPaidAmount;
	}

	/**
	 * @param handPaidAmount
	 *            the handPaidAmount to set
	 */
	public void setHandPaidAmount(String handPaidAmount) {
		this.handPaidAmount = handPaidAmount;
	}

	public String getSdsCalculatedAmount() {
		return sdsCalculatedAmount;
	}

	public void setSdsCalculatedAmount(String sdsCalculatedAmount) {
		this.sdsCalculatedAmount = sdsCalculatedAmount;
	}

	public String getAmountAuthEmpId() {
		return amountAuthEmpId;
	}

	public void setAmountAuthEmpId(String amountAuthEmpId) {
		this.amountAuthEmpId = amountAuthEmpId;
	}

	public String getAmountAuthPassword() {
		return amountAuthPassword;
	}

	public void setAmountAuthPassword(String amountAuthPassword) {
		this.amountAuthPassword = amountAuthPassword;
	}

	

	/**
	 * @return the machinePaidAmount
	 */
	/*public String getMachinePaidAmount() {
		return machinePaidAmount;
	}

	*//**
	 * @param machinePaidAmount the machinePaidAmount to set
	 *//*
	public void setMachinePaidAmount(String machinePaidAmount) {
		this.machinePaidAmount = machinePaidAmount;
	}*/
	/**
	 * Gets the Jackpot check expiry date
	 * @return
	 * @author vsubha
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}
	/**
	 * Sets the jackpot check expiry date
	 * @param expiryDate
	 * @author vsubha
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	/**
	 * @return
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 */
	public void setAccountNumber(String accountNumber) {
		String oldValue = this.accountNumber;
		String newValue = accountNumber;
		this.accountNumber = accountNumber;
		firePropertyChange(FormConstants.FORM_ACCOUNT_NUMBER, oldValue, newValue);
	}

	/**
	 * @return
	 */
	public String getConfirmAccountNumber() {
		return confirmAccountNumber;
	}

	/**
	 * @param confirmAccountNumber
	 */
	public void setConfirmAccountNumber(String confirmAccountNumber) {
		String oldValue = this.confirmAccountNumber;
		String newValue = confirmAccountNumber;
		this.confirmAccountNumber = confirmAccountNumber;
		firePropertyChange(FormConstants.FORM_CONFIRM_ACCOUNT_NUMBER, oldValue, newValue);
	}
}
