/*****************************************************************************
 * $Id: ReportComposite.java,v 1.11, 2010-03-03 06:28:49Z, Suganthi, Kaliamoorthy$
 * $Date: 3/3/2010 12:28:49 AM$
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
package com.ballydev.sds.slipsui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.cdatepicker.ACW;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSDatePicker;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.IFieldTextLimits;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;


/**
 * @author gsrinivasulu
 *
 */
public class ReportComposite extends TouchScreenBaseComposite {

	/**
	 * From date label instance
	 */
	private CbctlMandatoryLabel lblFromDate = null;

	/**
	 * To date  DatePicker instance
	 */
	private SDSDatePicker toDate = null;
	
	/**
	 * From date  DatePicker instance
	 */
	private SDSDatePicker fromDate = null;

	/**
	 * To date  label instance
	 */
	private CbctlMandatoryLabel lblToDate = null;

	/**
	 * Employee id label instance
	 */
	private CbctlLabel lblEmpId = null;

	/**
	 * Employee Id text instance
	 */
	private SDSTSText txtEmpId = null;
	
	
	/**
	 * ReportComposite constructor
	 * @param parent
	 * @param style
	 */
	public ReportComposite(Composite parent, int style) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_SLIPS_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.REPORT_HEADING));
		initialize("S");
		layout();
	}

	/** 
	 * Method to draw the Top Composite for the Text boxes
	 */
	public void drawTopComposite() {
		System.out.println("Inside EmployeeShiftSlotDetailsComposite:Initialise");
		//Variable used for calculating the composite height based on the no of controls in the 
		// Top Composite
		int rowCount = 1;
		
		lblFromDate = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(ILabelConstants.FROM_DATE_TIME));
		lblFromDate.setLayoutData(getTopComposite().getGDForLabel());
		createDateFromTime();
		++rowCount;
		CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
		dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
		
		lblToDate = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(ILabelConstants.TO_DATE_TIME));
		lblToDate.setLayoutData(getTopComposite().getGDForLabel());
		createDateToTime();
		++rowCount;
		
		lblEmpId = new CbctlLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
		lblEmpId.setLayoutData(getTopComposite().getGDForLabel());
		
		txtEmpId = new SDSTSText(getTopComposite(), SWT.NONE,"", ILabelConstants.FORM_EMPLOYEE_ID);
		txtEmpId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
		txtEmpId.setLayoutData(getTopComposite().getGDForText());
		++rowCount;
		
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
	}
	
	
	/**
	 * This method initializes dateStartTime	
	 *
	 */
	private void createDateFromTime() {		
		GridData gridData12 = new GridData();
		gridData12.heightHint = 25;
		gridData12.grabExcessHorizontalSpace = true;
		gridData12.horizontalAlignment = GridData.CENTER;
		gridData12.verticalAlignment = GridData.CENTER;
		gridData12.horizontalIndent = 0;
		gridData12.widthHint = 210;
		GridData gdForDate = getTopComposite().getGDForText();
		gdForDate.widthHint = 160;		
		fromDate = new SDSDatePicker(getTopComposite(), ACW.FOOTER|ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.DIGITAL_CLOCK | ACW.TIME_CUSTOM, "fromDate", "fromDate");
		if (Util.isSmallerResolution()) {
			fromDate.setLayoutData(gridData12);
		}else{
			fromDate.setLayoutData(getTopComposite().getGDForText());
		}
	}
	
	/**
	 * This method initializes dateEndTime	
	 *
	 */
	private void createDateToTime() {
		GridData gridData12 = new GridData();
		gridData12.heightHint = 25;
		gridData12.grabExcessHorizontalSpace = true;
		gridData12.horizontalAlignment = GridData.CENTER;
		gridData12.verticalAlignment = GridData.CENTER;
		gridData12.horizontalIndent = 0;
		gridData12.widthHint = 210;
		GridData gdForDate = getTopComposite().getGDForText();
		gdForDate.widthHint = 160;		
		toDate = new SDSDatePicker(getTopComposite(), ACW.FOOTER|ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.DIGITAL_CLOCK | ACW.TIME_CUSTOM, "toDate", "toDate");
		if (Util.isSmallerResolution()) {
			toDate.setLayoutData(gridData12);
		}else{
			toDate.setLayoutData(getTopComposite().getGDForText());
		}
	}

	public SDSTSText getTxtEmpId() {
		return txtEmpId;
	}

	public void setTxtEmpId(SDSTSText txtEmpId) {
		this.txtEmpId = txtEmpId;
	}

	public SDSDatePicker getFromDate() {
		return fromDate;
	}

	public void setFromDate(SDSDatePicker fromDate) {
		this.fromDate = fromDate;
	}

	public SDSDatePicker getToDate() {
		return toDate;
	}

	public void setToDate(SDSDatePicker toDate) {
		this.toDate = toDate;
	}

	public CbctlLabel getLblEmpId() {
		return lblEmpId;
	}

	public void setLblEmpId(CbctlLabel lblEmpId) {
		this.lblEmpId = lblEmpId;
	}

	public CbctlMandatoryLabel getLblFromDate() {
		return lblFromDate;
	}

	public void setLblFromDate(CbctlMandatoryLabel lblFromDate) {
		this.lblFromDate = lblFromDate;
	}

	public CbctlMandatoryLabel getLblToDate() {
		return lblToDate;
	}

	public void setLblToDate(CbctlMandatoryLabel lblToDate) {
		this.lblToDate = lblToDate;
	}

}  //  @jve:decl-index=0:visual-constraint="150,57"
