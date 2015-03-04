/*****************************************************************************
 * $Id: ManualJPEmployeeSlotStandShiftComposite.java,v 1.21, 2010-06-08 13:31:41Z, Subha Viswanathan$
 * $Date: 6/8/2010 8:31:41 AM$
 * $Log$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TouchScreenShiftControl;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * This composite has the details regarding the employee's id, password and
 * SlotLocation Number for a Manual Jackpot
 * 
 * @author dambereen
 * @version $Revision: 22$
 */
public class ManualJPEmployeeSlotStandShiftComposite extends TouchScreenBaseComposite {
	
	/**
	 * EmployeeId Label Instance
	 */
	public CbctlMandatoryLabel lblEmployeeId = null;

	/**
	 * EmployeeId Text Instance
	 */
	public SDSTSText txtEmployeeId = null;

	/**
	 * EmpPassword Label Instance
	 */
	public CbctlMandatoryLabel lblEmpPassword = null;

	/**
	 * EmpPassword Text Instance
	 */
	public SDSTSText txtEmpPassword = null;

	/**
	 * Slot Seq or Slot Location No Label Instance
	 */
	public CbctlMandatoryLabel lblSlotSeqLocationNo = null;

	/**
	 * Slot Seq or Slot Location No Text Instance
	 */
	public SDSTSText txtSlotSeqLocationNo = null;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * String array having the site configuration values for the parameters
	 * given below:
	 *  SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID
	 * PENDING_JACKPOT_PASSWORD_ENABLED PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION
	 * PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT_PRECEDENCE
	 */
	private String[] siteConfigParamFlag;

	private TouchScreenShiftControl shiftControl = null;
	
	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;

	public RadioButtonControl getRadioButtonControl() {
		return radioButtonControl;
	}

	public void setRadioButtonControl(RadioButtonControl radioButtonControl) {
		this.radioButtonControl = radioButtonControl;
	}

	/**
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public ManualJPEmployeeSlotStandShiftComposite(Composite parent, int style, String[] siteConfigParamFlag) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.MANUAL_JP_HEADING));
		
		
		this.siteConfigParamFlag = siteConfigParamFlag;
		initialize("N");
		
		layout();
	}


	public void drawTopComposite() {
		System.out.println("Inside ManualJPEmployeeSlotStandShiftComposite:Initialise");
		
		int rowCount = 1;
		
		if (siteConfigParamFlag[0].equalsIgnoreCase("yes")) {
			++ rowCount;
			lblEmployeeId = new CbctlMandatoryLabel(getTopComposite(), SWT.WRAP,
					LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
			lblEmployeeId.setLayoutData(getTopComposite().getGDForLabel());

			txtEmployeeId = new SDSTSText(getTopComposite(), SWT.NONE, LabelLoader
					.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID), FormConstants.FORM_EMPLOYEE_ID);
			txtEmployeeId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtEmployeeId.setLayoutData(getTopComposite().getGDForText());
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForSmallDummyLabel());
			}
		}
		
		

		if (siteConfigParamFlag[1].equalsIgnoreCase("yes")) {
			++ rowCount;
			lblEmpPassword = new CbctlMandatoryLabel(getTopComposite(), SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD));
			lblEmpPassword.setLayoutData(getTopComposite().getGDForMiddleLabel());

			txtEmpPassword = new SDSTSText(getTopComposite(), SWT.PASSWORD, LabelLoader
					.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD),
					FormConstants.FORM_EMP_PASSWORD);
			txtEmpPassword.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmpPassword.setLayoutData(getTopComposite().getGDForText());
			
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
			
		}
		
		if (siteConfigParamFlag[2].equalsIgnoreCase("1")) {
			++ rowCount;
			lblSlotSeqLocationNo = new CbctlMandatoryLabel(getTopComposite(), SWT.WRAP,
					LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
			lblSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtSlotSeqLocationNo = new SDSTSText(
					getTopComposite(),
					SWT.NONE|SWT.LEFT,
					LabelLoader
							.getLabelValue(LabelKeyConstants.SLOT_NUMBER),
							FormConstants.FORM_SLOT_NO);
			txtSlotSeqLocationNo.setTextLimit(IFieldTextLimits.SLOT_NO_TEXT_LIMIT);
			txtSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForText());
			log.debug("............ slot No form prop");
			
		}else if (siteConfigParamFlag[2].equalsIgnoreCase("2")){
			++ rowCount;
			lblSlotSeqLocationNo = new CbctlMandatoryLabel(
					getTopComposite(),
					SWT.WRAP,
					LabelLoader
							.getLabelValue(LabelKeyConstants.STAND_NUMBER));
			lblSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtSlotSeqLocationNo = new SDSTSText(
					getTopComposite(),
					SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.STAND_NUMBER),
							FormConstants.FORM_SLOT_LOCATION_NO);
			txtSlotSeqLocationNo.setTextLimit(IFieldTextLimits.SLOT_LOCATION_TEXT_LIMIT);
			txtSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForText());
		}
		
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
	}

	public void drawBottomComposite() {
		
		if(siteConfigParamFlag[3].equalsIgnoreCase("yes"))
		{
			this.shiftControl = new TouchScreenShiftControl()
					.drawBottomControls(getBottomComposite(), SWT.NONE);
			
		}
	}

	/**
	 * @return the lblEmployeeId
	 */
	public CbctlMandatoryLabel getLblEmployeeId() {
		return lblEmployeeId;
	}

	/**
	 * @param lblEmployeeId the lblEmployeeId to set
	 */
	public void setLblEmployeeId(CbctlMandatoryLabel lblEmployeeId) {
		this.lblEmployeeId = lblEmployeeId;
	}

	/**
	 * @return the lblEmpPassword
	 */
	public CbctlMandatoryLabel getLblEmpPassword() {
		return lblEmpPassword;
	}

	/**
	 * @param lblEmpPassword the lblEmpPassword to set
	 */
	public void setLblEmpPassword(CbctlMandatoryLabel lblEmpPassword) {
		this.lblEmpPassword = lblEmpPassword;
	}

	/**
	 * @return the lblSlotSeqLocationNo
	 */
	public CbctlMandatoryLabel getLblSlotSeqLocationNo() {
		return lblSlotSeqLocationNo;
	}

	/**
	 * @param lblSlotSeqLocationNo the lblSlotSeqLocationNo to set
	 */
	public void setLblSlotSeqLocationNo(CbctlMandatoryLabel lblSlotSeqLocationNo) {
		this.lblSlotSeqLocationNo = lblSlotSeqLocationNo;
	}

	/**
	 * @return the siteConfigParamFlag
	 */
	public String[] getSiteConfigParamFlag() {
		return siteConfigParamFlag;
	}

	/**
	 * @param siteConfigParamFlag the siteConfigParamFlag to set
	 */
	public void setSiteConfigParamFlag(String[] siteConfigParamFlag) {
		this.siteConfigParamFlag = siteConfigParamFlag;
	}

	/**
	 * @return the txtEmployeeId
	 */
	public SDSTSText getTxtEmployeeId() {
		return txtEmployeeId;
	}

	/**
	 * @param txtEmployeeId the txtEmployeeId to set
	 */
	public void setTxtEmployeeId(SDSTSText txtEmployeeId) {
		this.txtEmployeeId = txtEmployeeId;
	}

	/**
	 * @return the txtEmpPassword
	 */
	public SDSTSText getTxtEmpPassword() {
		return txtEmpPassword;
	}

	/**
	 * @param txtEmpPassword the txtEmpPassword to set
	 */
	public void setTxtEmpPassword(SDSTSText txtEmpPassword) {
		this.txtEmpPassword = txtEmpPassword;
	}

	/**
	 * @return the txtSlotSeqLocationNo
	 */
	public SDSTSText getTxtSlotSeqLocationNo() {
		return txtSlotSeqLocationNo;
	}

	/**
	 * @param txtSlotSeqLocationNo the txtSlotSeqLocationNo to set
	 */
	public void setTxtSlotSeqLocationNo(SDSTSText txtSlotSeqLocationNo) {
		this.txtSlotSeqLocationNo = txtSlotSeqLocationNo;
	}

	public TouchScreenShiftControl getShiftControl() {
		return shiftControl;
	}

	public void setShiftControl(TouchScreenShiftControl shiftControl) {
		this.shiftControl = shiftControl;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
