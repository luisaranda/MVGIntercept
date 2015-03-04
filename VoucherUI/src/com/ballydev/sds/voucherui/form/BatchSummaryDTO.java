/*****************************************************************************
 * Id: 
 * Date: Jun 27, 2007 
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

/**
 * @author selvaraj
 * 
 */
public class BatchSummaryDTO {
	
	private Long batchNumber;

	private Long voucherCount;

	private Double voucherAmount;

	private Long couponCount;

	private Double couponAmount;
	
	private String cashierOrKiosk;
	
	private String empId;
	
	private String assetNbr;
	
	private String locCode;
	
	private Date batchScanDate;
	
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public BatchSummaryDTO() {
		
	}

	public BatchSummaryDTO(Long batchNumer,Long voucherCount,Double voucherAmount,Long couponCount,Double couponAmount,String cashierOrKiosk) {
		this.batchNumber= batchNumer;
		this.voucherCount = voucherCount;
		this.voucherAmount = voucherAmount;
		this.couponCount  = couponCount;
		this.couponAmount = couponAmount;
		this.cashierOrKiosk = cashierOrKiosk;
	}

	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Long getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Long couponCount) {
		this.couponCount = couponCount;
	}

	public Double getVoucherAmount() {
		return voucherAmount;
	}

	public void setVoucherAmount(Double voucherAmount) {
		this.voucherAmount = voucherAmount;
	}

	public Long getVoucherCount() {
		return voucherCount;
	}

	public void setVoucherCount(Long voucherCount) {
		this.voucherCount = voucherCount;
	}

	public String getCashierOrKiosk() {
		return cashierOrKiosk;
	}

	public void setCashierOrKiosk(String cashierOrKiosk) {
		this.cashierOrKiosk = cashierOrKiosk;
	}

	/**
	 * @return the assetNbr
	 */
	public String getAssetNbr() {
		return assetNbr;
	}

	/**
	 * @param assetNbr the assetNbr to set
	 */
	public void setAssetNbr(String assetNbr) {
		this.assetNbr = assetNbr;
	}

	/**
	 * @return the locCode
	 */
	public String getLocCode() {
		return locCode;
	}

	/**
	 * @param locCode the locCode to set
	 */
	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

	public Date getBatchScanDate() {
		return batchScanDate;
	}

	public void setBatchScanDate(Date batchScanDate) {
		this.batchScanDate = batchScanDate;
	}
}
