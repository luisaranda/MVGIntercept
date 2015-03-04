package com.ballydev.sds.jackpotui.form;

import java.util.Date;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * @author anantharajr
 * @version $Revision: 9$
 */
public class ManualJPHandPaidAmtPromoJackpotTypeForm extends SDSForm {

	/**
	 * String that holds the hand paid amount 
	 */
	private String handPaidAmount;
	
	/**
	 * Cashless Account Number
	 */
	private String accountNumber;
	
	/**
	 * Confirm Cardless Account Number
	 */
	private String confirmAccountNumber;
	
	/**
	 * This field holds true if user selected promotional jackpot
	 */
	private Boolean promoYes;
	
	/**
	 *  This field holds true if user selected non promotional jackpot
	 */
	private Boolean promoNo;

	/**
	 * This field holds true if user selected to process a normal manual jackpot 
	 */
	private Boolean normalYes = true;

	/**
	 * This field holds true if user selected to process a progressive manual jackpot
	 */
	private Boolean progressiveYes = false;

	/**
	 * This field holds true if user selected to process a canceled credit manual jackpot
	 */
	private Boolean canceledCreditsYes = false;
	
	/**
	 * expiryDate
	 */
	private Date expiryDate;
	
	/**
	 * Progressive Level
	 */
	private String progressiveLevel;

	/**
	 * @return the handPaidAmount
	 */
	public String getHandPaidAmount() {
		return handPaidAmount;
	}

	/**
	 * @param handPaidAmount the handPaidAmount to set
	 */
	public void setHandPaidAmount(String handPaidAmount) {
		String oldValue = this.handPaidAmount;
		String newValue = handPaidAmount;
		this.handPaidAmount = handPaidAmount;
		firePropertyChange("handPaidAmount", oldValue, newValue);
	}
	

	/**
	 * @return the canceledCreditsYes
	 */
	public Boolean getCanceledCreditsYes() {
		return canceledCreditsYes;
	}

	/**
	 * @param canceledCreditsYes
	 *            the canceledCreditsYes to set
	 */
	public void setCanceledCreditsYes(Boolean canceledCreditsYes) {
		this.canceledCreditsYes = canceledCreditsYes;
	}

	/**
	 * @return the normalYes
	 */
	public Boolean getNormalYes() {
		return normalYes;
	}

	/**
	 * @param normalYes
	 *            the normalYes to set
	 */
	public void setNormalYes(Boolean normalYes) {
		this.normalYes = normalYes;
	}

	/**
	 * @return the progressiveYes
	 */
	public Boolean getProgressiveYes() {
		return progressiveYes;
	}

	/**
	 * @param progressiveYes
	 *            the progressiveYes to set
	 */
	public void setProgressiveYes(Boolean progressiveYes) {
		this.progressiveYes = progressiveYes;
	}

	
	/**
	 * @return the promoNo
	 */
	public Boolean getPromoNo() {
		return promoNo;
	}

	/**
	 * @param promoNo the promoNo to set
	 */
	public void setPromoNo(Boolean promoNo) {
		this.promoNo = promoNo;
	}

	/**
	 * @return the promoYes
	 */
	public Boolean getPromoYes() {
		return promoYes;
	}

	/**
	 * @param promoYes the promoYes to set
	 */
	public void setPromoYes(Boolean promoYes) {
		this.promoYes = promoYes;
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
		this.accountNumber = accountNumber;
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
		if(confirmAccountNumber != null && !confirmAccountNumber.equalsIgnoreCase("")) {
			this.confirmAccountNumber = confirmAccountNumber;
		}
	}
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
	 * Gets the progressive level
	 * @return
	 * @author vsubha
	 */
	public String getProgressiveLevel() {
		return progressiveLevel;
	}

	/**
	 * Sets the progressive level
	 * @param progressiveLevel
	 * @author vsubha
	 */
	public void setProgressiveLevel(String progressiveLevel) {
		this.progressiveLevel = progressiveLevel;
	}
}
