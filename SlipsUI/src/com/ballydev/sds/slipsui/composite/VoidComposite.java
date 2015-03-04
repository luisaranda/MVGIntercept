/*****************************************************************************
 * $Id: VoidComposite.java,v 1.11, 2010-04-11 16:03:31Z, Suganthi, Kaliamoorthy$
 * $Date: 4/11/2010 11:03:31 AM$
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
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.slipsui.constants.IFieldTextLimits;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;

/**
 * This composite is used to void a dispute slip
 * @author dambereen
 *
 */
public class VoidComposite extends TouchScreenBaseComposite {

	/**
	 * EmployeeId Label instance
	 */
	private CbctlMandatoryLabel lblEmployeeId = null;

	/**
	 * EmployeeId Text instance
	 */
	private SDSTSText txtEmployeeId = null;

	/**
	 * EmployeePwd Label instance
	 */
	private CbctlMandatoryLabel lblEmployeePwd = null;

	/**
	 * EmployeePwd Text instance
	 */
	private SDSTSText txtEmployeePwd = null;

	/**
	 * SequenceNumber Label instance
	 */
	private CbctlMandatoryLabel lblSequenceNo = null;

	/**
	 * SequenceNumber Text instance
	 */
	private SDSTSText txtSequenceNo = null;

	/**
	 * Array of boolean flags for configuration parameters
	 */
	private boolean[] flags;
	
	/**
	 * VoidComposite constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public VoidComposite(Composite parent, int style, boolean[] flags) {
		super(parent, style,  LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_SLIPS_MODULE_NAME)
				+ " | "
				+ LabelLoader
				.getLabelValue(LabelKeyConstants.VOID_HEADING));
		this.flags = flags;
		initialize("V");
		layout();
	}

	/** 
	 * Method to draw the Top Composite for the Text boxes
	 */
	public void drawTopComposite() {				
		//Variable used for calculating the composite height based on the no of controls in the 
		// Top Composite
		int rowCount = 1;
		
		if (flags[0]) {
			lblEmployeeId = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
			lblEmployeeId.setLayoutData(getTopComposite().getGDForLabel());
			txtEmployeeId = new SDSTSText(getTopComposite(), SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.EMPLOYEE_NAME), "employeeId");
			txtEmployeeId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtEmployeeId.setLayoutData(getTopComposite().getGDForText());
			++rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}

		if (flags[1]) {
			lblEmployeePwd = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD));
			lblEmployeePwd.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtEmployeePwd = new SDSTSText(
					getTopComposite(),
					SWT.PASSWORD,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD),
					"employeePwd");
			txtEmployeePwd.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmployeePwd.setLayoutData(getTopComposite().getGDForText());
			++rowCount;
			
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}

		lblSequenceNo = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
				LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER));
		lblSequenceNo.setLayoutData(getTopComposite().getGDForLabel());
		txtSequenceNo = new SDSTSText(getTopComposite(), SWT.NONE, LabelLoader
				.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER), "sequenceNo");
		txtSequenceNo.setTextLimit(IFieldTextLimits.SEQUENCE_NO_TEXT_LIMIT);
		txtSequenceNo.setLayoutData(getTopComposite().getGDForText());
		++rowCount;
		
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
	}
	
	/**
	 * @return the txtEmployeeId
	 */
	public SDSTSText getTxtEmployeeId() {
		return txtEmployeeId;
	}

	/**
	 * @param txtEmployeeId
	 *            the txtEmployeeId to set
	 */
	public void setTxtEmployeeId(SDSTSText txtEmployeeId) {
		this.txtEmployeeId = txtEmployeeId;
	}

	/**
	 * @return the txtEmployeePwd
	 */
	public SDSTSText getTxtEmployeePwd() {
		return txtEmployeePwd;
	}

	/**
	 * @param txtEmployeePwd
	 *            the txtEmployeePwd to set
	 */
	public void setTxtEmployeePwd(SDSTSText txtEmployeePwd) {
		this.txtEmployeePwd = txtEmployeePwd;
	}

	/**
	 * @return the txtSequenceNo
	 */
	public SDSTSText getTxtSequenceNo() {
		return txtSequenceNo;
	}

	/**
	 * @param txtSequenceNo
	 *            the txtSequenceNo to set
	 */
	public void setTxtSequenceNo(SDSTSText txtSequenceNo) {
		this.txtSequenceNo = txtSequenceNo;
	}

	/**
	 * @return the lblEmployeeId
	 */
	public CbctlMandatoryLabel getLblEmployeeId() {
		return lblEmployeeId;
	}

	/**
	 * @param lblEmployeeId
	 *            the lblEmployeeId to set
	 */
	public void setLblEmployeeId(CbctlMandatoryLabel lblEmployeeId) {
		this.lblEmployeeId = lblEmployeeId;
	}

	/**
	 * @return the lblEmployeePwd
	 */
	public CbctlMandatoryLabel getLblEmployeePwd() {
		return lblEmployeePwd;
	}

	/**
	 * @param lblEmployeePwd
	 *            the lblEmployeePwd to set
	 */
	public void setLblEmployeePwd(CbctlMandatoryLabel lblEmployeePwd) {
		this.lblEmployeePwd = lblEmployeePwd;
	}

	/**
	 * @return the lblSequenceNo
	 */
	public CbctlMandatoryLabel getLblSequenceNo() {
		return lblSequenceNo;
	}

	/**
	 * @param lblSequenceNo
	 *            the lblSequenceNo to set
	 */
	public void setLblSequenceNo(CbctlMandatoryLabel lblSequenceNo) {
		this.lblSequenceNo = lblSequenceNo;
	}

	/**
	 * @return the flags
	 */
	public boolean[] getFlags() {
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(boolean[] flags) {
		this.flags = flags;
	}

} // @jve:decl-index=0:visual-constraint="215,84"
