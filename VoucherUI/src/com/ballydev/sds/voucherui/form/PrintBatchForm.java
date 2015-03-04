/*****************************************************************************
 *	Copyright (c) 2006 Bally Technology  1977 - 2008
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.form;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * @author Ganesh Amirtharaj
 */
public class PrintBatchForm extends SDSForm{

	private Boolean  printSelected;
	private Boolean  printEntered; 
	private Boolean  printAll; 
	private String  batchNumber;
	
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public Boolean getPrintAll() {
		return printAll;
	}
	public void setPrintAll(Boolean printAll) {
		this.printAll = printAll;
	}
	public Boolean getPrintEntered() {
		return printEntered;
	}
	public void setPrintEntered(Boolean printEntered) {
		this.printEntered = printEntered;
	}
	public Boolean getPrintSelected() {
		return printSelected;
	}
	public void setPrintSelected(Boolean printSelected) {
		this.printSelected = printSelected;
	} 
	
}
