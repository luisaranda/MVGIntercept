package com.ballydev.sds.slipsui.form;

import java.util.List;

import com.ballydev.sds.framework.form.SDSForm;

/**
 * @author Srinivasulu Genji
 * @version $Revision: 1$	
 */
public class PrinterPreferenceForm extends SDSForm{

	/**
	 * lstPrinter
	 */
	private List lstPrinter;
	
	/**
	 * selectedPrinter
	 */
	private String selectedPrinter;

	/**
	 * @return the lstPrinter
	 */
	public List getLstPrinter() {
		return lstPrinter;
	}

	/**
	 * @param lstPrinter the lstPrinter to set
	 */
	public void setLstPrinter(List lstPrinter) {
		this.lstPrinter = lstPrinter;
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

