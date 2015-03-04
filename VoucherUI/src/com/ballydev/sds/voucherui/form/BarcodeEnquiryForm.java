package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucher.dto.TransactionListDTO;

public class BarcodeEnquiryForm  extends SDSForm{

	private String barCode;

	private String amount;

	private String state;

	private String effectiveDate;

	private String txtOffline;

	private String expireDate;

	private String txtTicketType;

	private List<TransactionListDTO> transactionListDTOList;

	private TransactionListDTO transactionListDTOSelectedValue;

	private String eamount;

	/**
	 * @return the eAmount
	 */
	public String getEamount() {
		return eamount;
	}

	/**
	 * @param amount the eAmount to set
	 */
	public void setEamount(String amount) {
		eamount = amount;
	}

	/**
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(String barCode) {
		String oldValue = this.barCode;
		String newValue = barCode;
		this.barCode = barCode;
		firePropertyChange("barCode", oldValue, newValue);
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		String oldValue = this.amount;
		String newValue = amount;
		this.amount = amount;
		firePropertyChange("amount", oldValue, newValue);
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		String oldValue = this.state;
		String newValue = LabelLoader.getLabelValue(state);
		this.state = LabelLoader.getLabelValue(state);
		firePropertyChange("state", oldValue, newValue);
	}

	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		String oldValue = this.effectiveDate;
		String newValue = effectiveDate;
		this.effectiveDate = effectiveDate;
		firePropertyChange("effectiveDate", oldValue, newValue);
	}

	/**
	 * @return the txtOffline
	 */
	public String getTxtOffline() {
		return txtOffline;
	}

	/**
	 * @param txtOffline the txtOffline to set
	 */
	public void setTxtOffline(String txtOffline) {
		String oldValue = this.txtOffline;
		String newValue = txtOffline;
		this.txtOffline = txtOffline;
		firePropertyChange("txtOffline", oldValue, newValue);
	}

	/**
	 * @return the expireDate
	 */
	public String getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(String expireDate) {
		String oldValue = this.expireDate;
		String newValue = expireDate;
		this.expireDate = expireDate;
		firePropertyChange("expireDate", oldValue, newValue);
	}

	/**
	 * @return the txtTicketType
	 */
	public String getTxtTicketType() {
		return txtTicketType;
	}

	/**
	 * @param txtTicketType the txtTicketType to set
	 */
	public void setTxtTicketType(String txtTicketType) {
		String oldValue = this.txtTicketType;
		String newValue = LabelLoader.getLabelValue(txtTicketType);
		this.txtTicketType = LabelLoader.getLabelValue(txtTicketType);
		firePropertyChange("txtTicketType", oldValue, newValue);
	}

	/**
	 * @return the transactionListDTOList
	 */
	public List<TransactionListDTO> getTransactionListDTOList() {
		return transactionListDTOList;
	}

	/**
	 * @param transactionListDTOList the transactionListDTOList to set
	 */
	@SuppressWarnings("unchecked")
	public void setTransactionListDTOList(
			List<TransactionListDTO> transactionListDTOList) {
		List oldValue = this.transactionListDTOList;
		this.transactionListDTOList = transactionListDTOList;
		firePropertyChange("transactionListDTOList", oldValue, transactionListDTOList);
	}

	/**
	 * @return the transactionListDTOSelectedValue
	 */
	public TransactionListDTO getTransactionListDTOSelectedValue() {
		return transactionListDTOSelectedValue;
	}

	/**
	 * @param transactionListDTOSelectedValue the transactionListDTOSelectedValue to set
	 */
	public void setTransactionListDTOSelectedValue(
			TransactionListDTO transactionListDTOSelectedValue) {
		this.transactionListDTOSelectedValue = transactionListDTOSelectedValue;
	}
}
