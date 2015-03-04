/*****************************************************************************
 * $Id: VoidReportDTO.java,v 1.0, 2008-04-03 15:54:20Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:54:20 AM$
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
package com.ballydev.sds.jackpotui.util;

/**
 * DTO class for printing the report of voided pending slips
 * @author Govindharaju Mohanasundaram
 * @version $Revision: 1$
 */
public class VoidReportDTO {

	/**
	 * Sequenece no
	 */
	private String seq;

	/**
	 * Date field
	 */
	private String date;

	/**
	 * Time field
	 */
	private String time;

	/**
	 * Slot no 
	 */
	private String slotNo;

	/**
	 * Stand no
	 */
	private String standNo;

	/**
	 * Denomination 
	 */
	private String denom;

	/**
	 * Jp Original amt
	 */
	private String amount;

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
		this.amount = amount;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the denom
	 */
	public String getDenom() {
		return denom;
	}

	/**
	 * @param denom the denom to set
	 */
	public void setDenom(String denom) {
		this.denom = denom;
	}

	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * @return the slotNo
	 */
	public String getSlotNo() {
		return slotNo;
	}

	/**
	 * @param slotNo the slotNo to set
	 */
	public void setSlotNo(String slotNo) {
		this.slotNo = slotNo;
	}

	/**
	 * @return the standNo
	 */
	public String getStandNo() {
		return standNo;
	}

	/**
	 * @param standNo the standNo to set
	 */
	public void setStandNo(String standNo) {
		this.standNo = standNo;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}


}
