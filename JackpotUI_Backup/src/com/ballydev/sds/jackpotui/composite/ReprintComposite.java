/*****************************************************************************
 * $Id: ReprintComposite.java,v 1.17, 2011-02-15 10:12:04Z, Subha Viswanathan$
 * $Date: 2/15/2011 4:12:04 AM$
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
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;

/**
 * This composite is used to reprint a jackpot
 * 
 * @author vijayrajm
 * @version $Revision: 18$
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
	public ReprintComposite(Composite parent, int style, boolean[] flags) {
		
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.REPRINT_JP_HEADING));
		
		this.flags = flags;
		initialize("RP");
		layout();
		
	}

	public void drawTopComposite() {
		System.out.println("Inside ReprintComposite:Initialise");
		int rowCount = 1;
		
		
		if (flags[0]) {
			++ rowCount;
			lblEmployeeId = new CbctlMandatoryLabel(getTopComposite(),
					SWT.WRAP, LabelLoader
							.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
			lblEmployeeId.setLayoutData(getTopComposite().getGDForLabel());
			
			txtEmployeeId = new SDSTSText(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID),
					ILabelConstants.FORM_EMPLOYEE_ID);
			txtEmployeeId.setTextLimit(ILabelConstants.EMPLOYEE_ID_TEXT_LIMIT);
			txtEmployeeId.setLayoutData(getTopComposite().getGDForText());

			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForSmallDummyLabel());
			}
	
		}
		
		if (flags[1]) {
			++ rowCount;
			lblEmployeePwd = new CbctlMandatoryLabel(getTopComposite(),
					SWT.WRAP, LabelLoader
							.getLabelValue(ILabelConstants.EMPLOYEE_PASSWORD));
			lblEmployeePwd.setLayoutData(getTopComposite().getGDForMiddleLabel());
			
			txtEmployeePwd = new SDSTSText(
					getTopComposite(),
					SWT.PASSWORD,
					LabelLoader
							.getLabelValue(ILabelConstants.EMPLOYEE_PASSWORD),
							ILabelConstants.FORM_EMP_PASSWORD);
			txtEmployeePwd.setTextLimit(ILabelConstants.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmployeePwd.setLayoutData(getTopComposite().getGDForText());
			
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}

		}
		
		++ rowCount;

		lblSequenceNo = new CbctlMandatoryLabel(getTopComposite(), SWT.WRAP,
				LabelLoader.getLabelValue(ILabelConstants.SEQUENCE_NUMBER));
		lblSequenceNo.setLayoutData(getTopComposite().getGDForExtraLabel());
		txtSequenceNo = new SDSTSText(getTopComposite(), SWT.NONE|SWT.LEFT, LabelLoader
				.getLabelValue(ILabelConstants.SEQUENCE_NUMBER), ILabelConstants.FORM_SEQUENCE_NO);
		txtSequenceNo.setTextLimit(ILabelConstants.SEQUENCE_NO_TEXT_LIMIT);
		txtSequenceNo.setNumeric(true);
		txtSequenceNo.setLayoutData(getTopComposite().getGDForText());
		
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

} // @jve:decl-index=0:visual-constraint="226,152"

