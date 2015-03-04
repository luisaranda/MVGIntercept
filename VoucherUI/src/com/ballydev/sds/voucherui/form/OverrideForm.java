/*****************************************************************************
 * $Id: OverrideForm.java,v 1.0, 2010-06-16 14:53:48Z, Verma, Nitin Kumar$
 * $Date: 6/16/2010 9:53:48 AM$
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

import java.util.Date;
import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;

/**
 * @author VNitinkumar
 */
public class OverrideForm extends SDSForm {

	private String barCode;

	private String amount;

	private String assetNumber;
	
	private Date cretaedDateTime;
	
	private String playerId;

	private List<TicketInfoDTO> lstTktInfoDTO;
	
	private TicketInfoDTO ticketListDTOSelectedValue;

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
	 * @return the assetNumber
	 */
	public String getAssetNumber() {
		return assetNumber;
	}
	
	/**
	 * @param assetNumber the assetNumber to set
	 */
	public void setAssetNumber(String assetNumber) {
		String oldValue = this.assetNumber;
		String newValue = assetNumber;
		this.assetNumber = assetNumber;
		firePropertyChange("assetNumber", oldValue, newValue);
	}
	
	/**
	 * @return the cretaedDateTime
	 */
	public Date getCretaedDateTime() {
		return cretaedDateTime;
	}

	/**
	 * @param cretaedDateTime the cretaedDateTime to set
	 */
	public void setCretaedDateTime(Date cretaedDateTime) {
		Date oldValue = this.cretaedDateTime;
		Date newValue = cretaedDateTime;
		this.cretaedDateTime = cretaedDateTime;
		firePropertyChange("cretaedDateTime", oldValue, newValue);
	}

	/**
	 * @return the playerId
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(String playerId) {
		String oldValue = this.playerId;
		String newValue = playerId;
		this.playerId = playerId;
		firePropertyChange("playerId", oldValue, newValue);
	}

	/**
	 * @return the lstTktInfoDTO
	 */
	public List<TicketInfoDTO> getLstTktInfoDTO() {
		return lstTktInfoDTO;
	}

	/**
	 * @param lstTktInfoDTO the lstTktInfoDTO to set
	 */
	public void setLstTktInfoDTO(List<TicketInfoDTO> lstTktInfoDTO) {
		List<TicketInfoDTO> oldValue = this.lstTktInfoDTO;
		this.lstTktInfoDTO = lstTktInfoDTO;
		firePropertyChange("lstTktInfoDTO", oldValue, lstTktInfoDTO);
	}

	/**
	 * @return the ticketListDTOSelectedValue
	 */
	public TicketInfoDTO getTicketListDTOSelectedValue() {
		return ticketListDTOSelectedValue;
	}

	/**
	 * @param ticketListDTOSelectedValue the ticketListDTOSelectedValue to set
	 */
	public void setTicketListDTOSelectedValue(
			TicketInfoDTO ticketListDTOSelectedValue) {
		this.ticketListDTOSelectedValue = ticketListDTOSelectedValue;
	}

}
