/*****************************************************************************
 * $Id: BatchDetailsForm.java,v 1.1, 2008-06-04 09:47:34Z, Nithyakalyani, Raman$
 * $Date: 6/4/2008 4:47:34 AM$
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

import java.util.ArrayList;

import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.voucher.dto.ReclBatchDetailsInfoDTO;
import com.ballydev.sds.voucherui.util.VoucherUtil;



public class BatchDetailsForm extends SDSForm {
	
	private String batchNbrValue;
	
	private String batchEmployeeValue;
	
	private String batchVoucherCountValue;
	
	private String batchVoucherAmountValue;
	
	private String batchCouponCountValue;
	
	private String batchCouponAmountValue;
	
	private boolean isCashier = true;

	private ArrayList<ReclBatchDetailsInfoDTO> dispBatchDetailsDTO;
	
	private ReclBatchDetailsInfoDTO dispBatchDetailsDTOSelectedValue;

	public ArrayList<ReclBatchDetailsInfoDTO> getDispBatchDetailsDTO() {
		return dispBatchDetailsDTO;
	}

	public void setDispBatchDetailsDTO(ArrayList<ReclBatchDetailsInfoDTO> dispBatchDetailsDTO) {
		ArrayList<ReclBatchDetailsInfoDTO> oldArrayList = this.dispBatchDetailsDTO;
		ArrayList<ReclBatchDetailsInfoDTO> newArrayList = dispBatchDetailsDTO;
		this.dispBatchDetailsDTO = dispBatchDetailsDTO;
		firePropertyChange("dispBatchDetailsDTO", oldArrayList, newArrayList);
	}

	public String getBatchCouponAmountValue() {
		return batchCouponAmountValue;
	}

	public void setBatchCouponAmountValue(String batchCouponAmountValue) {
		this.batchCouponAmountValue = batchCouponAmountValue;
	}

	public String getBatchCouponCountValue() {
		return batchCouponCountValue;
	}

	public void setBatchCouponCountValue(String batchCouponCountValue) {
		this.batchCouponCountValue = batchCouponCountValue;
	}

	public String getBatchEmployeeValue() {
		return batchEmployeeValue;
	}

	public void setBatchEmployeeValue(String batchEmployeeValue) {		
		this.batchEmployeeValue = VoucherUtil.removeZeros(batchEmployeeValue);
	}

	public String getBatchNbrValue() {
		return batchNbrValue;
	}

	public void setBatchNbrValue(String batchNbrValue) {
		this.batchNbrValue = batchNbrValue;
	}

	public String getBatchVoucherAmountValue() {
		return batchVoucherAmountValue;
	}

	public void setBatchVoucherAmountValue(String batchVoucherAmountValue) {
		this.batchVoucherAmountValue = batchVoucherAmountValue;
	}

	public String getBatchVoucherCountValue() {
		return batchVoucherCountValue;
	}

	public void setBatchVoucherCountValue(String batchVoucherCountValue) {
		this.batchVoucherCountValue = batchVoucherCountValue;
	}

	public boolean isCashier() {
		return isCashier;
	}

	public void setCashier(boolean isCashier) {
		this.isCashier = isCashier;
	}

	/**
	 * @return the dispBatchDetailsDTOSelectedValue
	 */
	public ReclBatchDetailsInfoDTO getDispBatchDetailsDTOSelectedValue() {
		return dispBatchDetailsDTOSelectedValue;
	}

	/**
	 * @param dispBatchDetailsDTOSelectedValue the dispBatchDetailsDTOSelectedValue to set
	 */
	public void setDispBatchDetailsDTOSelectedValue(
			ReclBatchDetailsInfoDTO dispBatchDetailsDTOSelectedValue) {
		this.dispBatchDetailsDTOSelectedValue = dispBatchDetailsDTOSelectedValue;
	}
}
