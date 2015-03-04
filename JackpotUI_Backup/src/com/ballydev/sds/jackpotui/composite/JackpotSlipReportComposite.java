/*****************************************************************************
 * $Id: JackpotSlipReportComposite.java,v 1.21, 2010-06-08 13:31:41Z, Subha Viswanathan$
 * $Date: 6/8/2010 8:31:41 AM$
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
package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.cdatepicker.ACW;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSDatePicker;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;


/**
 * This composite is used to void a jackpot
 * 
 * @author dambereen
 * @version $Revision: 22$
 */
public class JackpotSlipReportComposite extends TouchScreenBaseComposite {

	
	/**
	 * From date label instance
	 */
	private CbctlMandatoryLabel lblFromDate = null;

	/**
	 * To date  DatePicker instance
	 */
	private SDSDatePicker dateToTime = null;
	
	/**
	 * From date  DatePicker instance
	 */
	private SDSDatePicker dateFromTime = null;

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
	private SDSTSText txtEmployeeId = null;
	
	
	
	/**
	 * VoidComposite constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public JackpotSlipReportComposite(Composite parent, int style) {
		
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.REPORT_JP_HEADING));
		initialize("S");
		layout();
		
	}

	public void drawTopComposite() {
		System.out.println("Inside JackpotSlipReportComposite:Initialise");
		int rowCount = 1;
		rowCount = rowCount + 3;
		
		lblFromDate = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(ILabelConstants.FROM_DATE_TIME));
		lblFromDate.setLayoutData(getTopComposite().getGDForLabel());
		createDateFromTime();
		
		CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
		dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
		
		lblToDate = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(ILabelConstants.TO_DATE_TIME));
		lblToDate.setLayoutData(getTopComposite().getGDForLabel());
		createDateToTime();
		
		lblEmpId = new CbctlLabel(getTopComposite(), SWT.NONE,LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
		lblEmpId.setLayoutData(getTopComposite().getGDForLabel());
		
		txtEmployeeId = new SDSTSText(getTopComposite(), SWT.NONE,"", ILabelConstants.FORM_EMPLOYEE_ID);
		txtEmployeeId.setTextLimit(5);
		txtEmployeeId.setLayoutData(getTopComposite().getGDForText());
			
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
		dateFromTime = new SDSDatePicker(getTopComposite(), ACW.FOOTER|ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.DIGITAL_CLOCK | ACW.TIME_CUSTOM, "fromDate", "fromDate");
		if (Util.isSmallerResolution()) {
			dateFromTime.setLayoutData(gridData12);
		}else{
			dateFromTime.setLayoutData(getTopComposite().getGDForText());
		}
		
	}
	
	/**
	 * This method initializes dateEndTime	
	 *
	 */
	private void createDateToTime() {
		GridData gridData14 = new GridData();
		gridData14.heightHint = 25;
		gridData14.horizontalAlignment = GridData.CENTER;
		gridData14.verticalAlignment = GridData.CENTER;
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.widthHint = 210;
		dateToTime = new SDSDatePicker(getTopComposite(), ACW.FOOTER|ACW.DROP_DOWN | ACW.BORDER | ACW.DATE_CUSTOM | ACW.DIGITAL_CLOCK | ACW.TIME_CUSTOM, "toDate", "toDate");
		if (Util.isSmallerResolution()) {
			dateToTime.setLayoutData(gridData14);
		}else{
			dateToTime.setLayoutData(getTopComposite().getGDForText());
		}
	}
	
	/**
	 * @return the lblEndDate
	 */
	public CbctlMandatoryLabel getLblToDate() {
		return lblToDate;
	}

	/**
	 * @param lblToDate the lblEndDate to set
	 */
	public void setLblToDate(CbctlMandatoryLabel lblToDate) {
		this.lblToDate = lblToDate;
	}

	/**
	 * @return the lblStartDate
	 */
	public CbctlMandatoryLabel getLblFromDate() {
		return lblFromDate;
	}

	/**
	 * @param lblFromDate the lblStartDate to set
	 */
	public void setLblFromDate(CbctlMandatoryLabel lblFromDate) {
		this.lblFromDate = lblFromDate;
	}

	public CbctlLabel getLblEmpId() {
		return lblEmpId;
	}

	public void setLblEmpId(CbctlLabel lblEmpId) {
		this.lblEmpId = lblEmpId;
	}

	public SDSDatePicker getDateToTime() {
		return dateToTime;
	}

	public void setDateToTime(SDSDatePicker dateToTime) {
		this.dateToTime = dateToTime;
	}

	public SDSDatePicker getDateFromTime() {
		return dateFromTime;
	}

	public void setDateFromTime(SDSDatePicker dateFromTime) {
		this.dateFromTime = dateFromTime;
	}

	public SDSTSText getTxtEmployeeId() {
		return txtEmployeeId;
	}

	public void setTxtEmployeeId(SDSTSText txtEmployeeId) {
		this.txtEmployeeId = txtEmployeeId;
	}

} // @jve:decl-index=0:visual-constraint="215,84"
