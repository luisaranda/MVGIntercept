/*****************************************************************************
 * $Id: ReprintComposite.java,v 1.13, 2011-02-15 05:28:58Z, Ambereen Drewitt$
 * $Date: 2/14/2011 11:28:58 PM$
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
import com.ballydev.sds.slipsui.constants.FormConstants;
import com.ballydev.sds.slipsui.constants.IFieldTextLimits;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;


/**
 * This composite is used to reprint a dispute slip
 * 
 * @author dambereen
 * 
 */
public class ReprintComposite extends TouchScreenBaseComposite {

	
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
	 * ReprintComposite constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public ReprintComposite(Composite parent, int style,boolean[] flags) {
		super(parent, style,LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_SLIPS_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.REPRINT_HEADING));
		this.flags = flags;
		initialize("RP");
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
					.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID), FormConstants.FORM_EMPLOYEE_ID);
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
					LabelLoader.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD));
			lblEmployeePwd.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtEmployeePwd = new SDSTSText(
					getTopComposite(),
					SWT.PASSWORD,
					LabelLoader.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD),
					FormConstants.FORM_EMP_PASSWORD);
			txtEmployeePwd.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmployeePwd.setLayoutData(getTopComposite().getGDForText());
			++rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}

		lblSequenceNo = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
				LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_SEQUENCE_NUMBER));
		lblSequenceNo.setLayoutData(getTopComposite().getGDForExtraLabel());
		txtSequenceNo = new SDSTSText(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER),
								FormConstants.FORM_SEQUENCE_NO);
		txtSequenceNo.setTextLimit(IFieldTextLimits.SEQUENCE_NO_TEXT_LIMIT);
		txtSequenceNo.setNumeric(true);
		txtSequenceNo.setLayoutData(getTopComposite().getGDForText());
		++rowCount;
		
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
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

	public SDSTSText getTxtEmployeeId() {
		return txtEmployeeId;
	}

	public void setTxtEmployeeId(SDSTSText txtEmployeeId) {
		this.txtEmployeeId = txtEmployeeId;
	}

	public SDSTSText getTxtEmployeePwd() {
		return txtEmployeePwd;
	}

	public void setTxtEmployeePwd(SDSTSText txtEmployeePwd) {
		this.txtEmployeePwd = txtEmployeePwd;
	}

	public SDSTSText getTxtSequenceNo() {
		return txtSequenceNo;
	}

	public void setTxtSequenceNo(SDSTSText txtSequenceNo) {
		this.txtSequenceNo = txtSequenceNo;
	}

} // @jve:decl-index=0:visual-constraint="215,84"
