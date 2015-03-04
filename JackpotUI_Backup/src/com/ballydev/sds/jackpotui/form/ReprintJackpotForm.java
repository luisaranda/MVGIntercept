/*****************************************************************************
 * $Id: ReprintJackpotForm.java,v 1.0, 2008-04-03 15:52:56Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:52:56 AM$
 * $Log:
 *  7    SDS11     1.6         11/17/2007 7:10:33 PM  Rajesh Anantharaj updated
 *        javadoc
 *  6    SDS11     1.5         10/4/2007 11:00:53 AM  Ambereen Drewitt Added
 *       slot location field
 *  5    SDS11     1.4         9/7/2007 11:12:47 AM   Rajesh Anantharaj changed
 *        the package name to framework standards
 *  4    SDS11     1.3         7/2/2007 2:10:43 PM    Ambereen Drewitt Added
 *       copyright text.
 *  3    SDS11     1.2         6/29/2007 4:12:00 PM   Ambereen Drewitt Changed
 *       asset id  to slot number field.
 *  2    SDS11     1.1         5/18/2007 11:25:50 AM  Vijayraj Magenthrarajan
 *       Added properties. Vijayraj.
 *  1    SDS11     1.0         5/17/2007 6:54:17 PM   Vijayraj Magenthrarajan
 *       Added new ReprintJackpotForm is added. vijyraj
 * $
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

import com.ballydev.sds.framework.form.SDSForm;

/**
 * This class contains the properties coresponding to the controls in the 
 * ReprintJackpotComposite  
 * @author vijayrajm
 * @version $Revision: 1$
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
	 * Slot Location No
	 */
	private String slotLocationNo;
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
	/**
	 * @return the slotLocationNo
	 */
	public String getSlotLocationNo() {
		return slotLocationNo;
	}
	/**
	 * @param slotLocationNo the slotLocationNo to set
	 */
	public void setSlotLocationNo(String slotLocationNo) {
		String oldValue = this.slotLocationNo;
		String newValue = slotLocationNo;
		this.slotLocationNo = slotLocationNo;
		firePropertyChange("slotLocationNo", oldValue, newValue);
	}
	
	
	
	
	
}
