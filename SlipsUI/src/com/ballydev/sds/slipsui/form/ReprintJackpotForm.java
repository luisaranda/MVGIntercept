/*****************************************************************************
 * $Id: ReprintJackpotForm.java,v 1.0, 2008-04-21 05:45:32Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:45:32 AM$
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
package com.ballydev.sds.slipsui.form;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * This class contains the properties coresponding to the controls in the 
 * ReprintJackpotComposite  
 * @author vijayrajm
 * @date 18/05/2007
 */
public class ReprintJackpotForm extends SDSForm{

	/**
	 * Transaction Date instance
	 */
	private String transDate;
	/**
	 * Sequence Number instance
	 */
	private String seqNumber;
	/**
	 * Slot Number instance
	 */
	private String slotNumber;
	/**
	 * Jackpot Amount instance
	 */
	private String jackpotAmt;
	/**
	 * Employee Name instance
	 */
	private String empName;
	/**
	 * @return the assetId
	 */
	
	
	public String getSlotNumber() {
		return slotNumber;
	}
	/**
	 * @param assetId the assetId to set
	 */
	public void setSlotNumber(String slotNumber) {
		String oldValue = this.slotNumber;
		String newValue = slotNumber;
		this.slotNumber = slotNumber;
		firePropertyChange("slotNumber", oldValue, newValue);
	}
	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}
	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		String oldValue = this.empName;
		String newValue = empName;
		this.empName = empName;
		firePropertyChange("empName", oldValue, newValue);
	}
	/**
	 * @return the jackpotAmt
	 */
	public String getJackpotAmt() {
		return jackpotAmt;
	}
	/**
	 * @param jackpotAmt the jackpotAmt to set
	 */
	public void setJackpotAmt(String jackpotAmt) {
		String oldValue = this.jackpotAmt;
		String newValue = jackpotAmt;
		this.jackpotAmt = jackpotAmt;
		firePropertyChange("jackpotAmt", oldValue, newValue);
	}
	/**
	 * @return the seqNumber
	 */
	public String getSeqNumber() {
		return seqNumber;
	}
	/**
	 * @param seqNumber the seqNumber to set
	 */
	public void setSeqNumber(String seqNumber) {
		String oldValue = this.seqNumber;
		String newValue = seqNumber;
		this.seqNumber = seqNumber;
		firePropertyChange("seqNumber", oldValue, newValue);
	}
	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}
	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(String transDate) {
		String oldValue = this.transDate;
		String newValue = transDate;
		this.transDate = transDate;
		firePropertyChange("transDate", oldValue, newValue);
	}
	
	
	
	
	
}
