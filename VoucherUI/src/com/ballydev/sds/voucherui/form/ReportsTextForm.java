/*****************************************************************************
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

import com.ballydev.sds.framework.form.SDSForm;

/**
 * This is the form class of the
 * reports text composite
 * @author Nithya kalyani R
 * @version $Revision: 1$ 
 */
public class ReportsTextForm extends SDSForm {

	/**
	 * Reports Display
	 */
	private String txtReportsDisplay;	

	/**
	 * @return the txtReportsDisplay
	 */
	public String getTxtReportsDisplay() {
		return txtReportsDisplay;
	}

	/**
	 * @param txtReportsDisplay the txtReportsDisplay to set
	 */
	public void setTxtReportsDisplay(String txtReportsDisplay) {
		String oldValue = this.txtReportsDisplay;
		String newValue = txtReportsDisplay;
		this.txtReportsDisplay = txtReportsDisplay;
		firePropertyChange("txtReportsDisplay", oldValue, newValue);
	}

}
