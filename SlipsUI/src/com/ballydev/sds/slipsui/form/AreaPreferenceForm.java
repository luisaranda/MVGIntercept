/*****************************************************************************
 * $Id: AreaPreferenceForm.java,v 1.1, 2010-02-11 12:57:26Z, Ambereen Drewitt$
 * $Date: 2/11/2010 6:57:26 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Gaming Inc.  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.slipsui.form;

import java.util.ArrayList;

import com.ballydev.sds.framework.form.SDSForm;


/**
 * AreaPreference form
 * @author dambereen
 * @version $Revision: 2$
 */
public class AreaPreferenceForm extends SDSForm{/*
	/**
	 * ArrayList of All Available areas
	 */
	private ArrayList<String> lstAllJackpotAreaAvailable;

	/**
	 * ArrayList of Selected areas
	 */
	private ArrayList<String> lstSelJackpotAreaAvailable;

	/**
	 * ArrayList of All Selected areas
	 */
	private ArrayList<String> lstAllJackpotAreaSelected;

	/**
	 * ArrayList of Available areas
	 */
	private ArrayList<String> lstSelJackpotAreaSelected;

	public ArrayList<String> getLstAllJackpotAreaAvailable() {
		return lstAllJackpotAreaAvailable;
	}

	public void setLstAllJackpotAreaAvailable(
			ArrayList<String> lstAllJackpotAreaAvailable) {
		ArrayList<String> oldLstAllJackpotAreaAvailable=this.lstAllJackpotAreaAvailable;
		this.lstAllJackpotAreaAvailable = lstAllJackpotAreaAvailable;
		firePropertyChange("lstAllJackpotAreaAvailable", oldLstAllJackpotAreaAvailable, lstAllJackpotAreaAvailable);
	}

	public ArrayList<String> getLstAllJackpotAreaSelected() {
		return lstAllJackpotAreaSelected;
	}

	public void setLstAllJackpotAreaSelected(
			ArrayList<String> lstAllJackpotAreaSelected) {
		ArrayList<String> oldLstAllJackpotAreaSelected=this.lstAllJackpotAreaSelected;
		this.lstAllJackpotAreaSelected = lstAllJackpotAreaSelected;
		firePropertyChange("lstAllJackpotAreaSelected", oldLstAllJackpotAreaSelected, lstAllJackpotAreaSelected);
	}

	public ArrayList<String> getLstSelJackpotAreaAvailable() {
		return lstSelJackpotAreaAvailable;
	}

	public void setLstSelJackpotAreaAvailable(
			ArrayList<String> lstSelJackpotAreaAvailable) {
		ArrayList<String> oldLstSelJackpotAreaAvailable=this.lstSelJackpotAreaAvailable;
		this.lstSelJackpotAreaAvailable = lstSelJackpotAreaAvailable;
		firePropertyChange("lstSelJackpotAreaAvailable", oldLstSelJackpotAreaAvailable, lstSelJackpotAreaAvailable);
	}

	public ArrayList<String> getLstSelJackpotAreaSelected() {
		return lstSelJackpotAreaSelected;
	}

	public void setLstSelJackpotAreaSelected(
			ArrayList<String> lstSelJackpotAreaSelected) {
		ArrayList<String> oldLstSelJackpotAreaSelected=this.lstSelJackpotAreaSelected;
		this.lstSelJackpotAreaSelected = lstSelJackpotAreaSelected;
		firePropertyChange("lstSelJackpotAreaSelected", oldLstSelJackpotAreaSelected, lstSelJackpotAreaSelected);
	}
}
