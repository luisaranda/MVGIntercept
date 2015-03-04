/*****************************************************************************
 *  Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * @author Nithya kalyani
 * @version $Revision: 3$	
 */
public class PrinterPreferenceForm extends SDSForm{


	/**
	 * lstPrinter
	 */
	@SuppressWarnings("unchecked")
	private List lstPrinter;
	
	/**
	 * selectedPrinter
	 */
	private String selectedPrinter;

	/**
	 * @return the lstPrinter
	 */
	@SuppressWarnings("unchecked")
	public List getLstPrinter() {
		return lstPrinter;
	}

	/**
	 * @param lstPrinter the lstPrinter to set
	 */
	@SuppressWarnings("unchecked")
	public void setLstPrinter(List lstPrinter) {
		List oldList = this.lstPrinter;
		this.lstPrinter = lstPrinter;
		firePropertyChange("lstPrinter", oldList, lstPrinter);
	}

	/**
	 * @return the selectedPrinter
	 */
	public String getSelectedPrinter() {
		return selectedPrinter;
	}

	/**
	 * @param selectedPrinter the selectedPrinter to set
	 */
	public void setSelectedPrinter(String selectedPrinter) {
		this.selectedPrinter = selectedPrinter;
	}
}
