/*****************************************************************************
 * $Id: VoidComposite.java,v 1.21, 2010-06-08 13:33:37Z, Subha Viswanathan$
 * $Date: 6/8/2010 8:33:37 AM$
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.composite.TouchScreenMiddleSingleHeaderComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;


/**
 * This composite is used to void a jackpot
 * 
 * @author vijayrajm
 * @version $Revision: 22$
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
	 * Slot / Slot Location label instance
	 */
	private CbctlMandatoryLabel lblSlotOrLocNo = null;

	/**
	 * Slot / Slot Location text instance
	 */
	private SDSTSText txtSlotOrLocNo = null;

	/**
	 * Boolean array to hold the site param to check of Slot / Slot location no is to be displayed
	 */
	private boolean[] siteParam;
	
	
	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;
	
	/**
	 * Dummy Label
	 */
	private CbctlLabel dummyLabel = null;
	
	private TSButtonLabel standSlotRadioImage = null;
	
	private TSButtonLabel seqRadioImage = null;

	/**
	 * VoidComposite constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public VoidComposite(Composite parent, int style, boolean[] flags) {
		
		
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.VOID_JP_HEADING));
		
		
		this.flags = flags;
		initialize("V");
		layout();
	}

	public void drawTopComposite() {
		System.out.println("Inside VoidComposite:Initialise");
		
		int rowCount = 1;
			
			
		if (flags[0]) {
			++ rowCount;
			lblEmployeeId = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
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
		
		

		if (flags[1]) {
			++ rowCount;
			lblEmployeePwd = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD));
			lblEmployeePwd.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtEmployeePwd = new SDSTSText(
					getTopComposite(),
					SWT.PASSWORD,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD),
					FormConstants.FORM_EMP_PASSWORD);
			txtEmployeePwd.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmployeePwd.setLayoutData(getTopComposite().getGDForText());
		}
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
		
	}
	
	public void drawMiddleComposite() {
		
		getMiddleComposite().setGdMiddleComp(getMiddleComposite().getGridDataForSingleMiddleCmp());
		getMiddleComposite().setGlMiddleCom(getMiddleComposite().getLayoutForSingleMiddleCmp());
		getMiddleComposite().setLayoutDataForMiddle();
		getMiddleComposite().setBackground(SDSControlFactory.getTSMiddleBodyColor());

		
		
		new TouchScreenMiddleSingleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEARCH_BY_ONE));
		
		
		radioButtonControl = new RadioButtonControl(LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEARCH_BY_ONE));
		GridData gdDummyLbl = new GridData();
		gdDummyLbl.horizontalAlignment = GridData.BEGINNING;
		
		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.BEGINNING;
		gdRadio.horizontalIndent = 10;
		
		GridData gdLabel = getMiddleComposite().getGDForExtraLabel();
		
		dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.WRAP);
		dummyLabel.setLayoutData(gdDummyLbl);
		lblSequenceNo = new CbctlMandatoryLabel(getMiddleComposite(), SWT.WRAP ,
				LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER_FOR_PROCESSED_SLIPS));
		lblSequenceNo.setLayoutData(gdLabel);
		
		dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.WRAP);
		dummyLabel.setLayoutData(gdDummyLbl);
		lblSlotOrLocNo = new CbctlMandatoryLabel(getMiddleComposite(),  SWT.WRAP , 
				LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER_FOR_PENDING_SLIPS));	
		lblSlotOrLocNo.setLayoutData(getMiddleComposite().getGDForExtraLargeLabel());
		
		dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.WRAP);
		dummyLabel.setLayoutData(gdDummyLbl);
		
		dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.WRAP);
		dummyLabel.setLayoutData(gdDummyLbl);
		
		seqRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE, "seqradio");
		seqRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		seqRadioImage.setLayoutData(gdRadio);
		radioButtonControl.add(seqRadioImage);
		
		txtSequenceNo = new SDSTSText(getMiddleComposite(), SWT.WRAP|SWT.LEFT, LabelLoader
				.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER), FormConstants.FORM_SEQUENCE_NO);
		txtSequenceNo.setTextLimit(IFieldTextLimits.SEQUENCE_NO_TEXT_LIMIT);
		txtSequenceNo.setLayoutData(getMiddleComposite().getGDForText());
		
		
		standSlotRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE, "slotstandradio");
		standSlotRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		standSlotRadioImage.setLayoutData(gdRadio);
		radioButtonControl.add(standSlotRadioImage);
		
		txtSlotOrLocNo = new SDSTSText(getMiddleComposite(), SWT.WRAP|SWT.LEFT, "", FormConstants.FORM_SLOT_NO);
		txtSlotOrLocNo.setTextLimit(IFieldTextLimits.SLOT_NO_TEXT_LIMIT);
		txtSlotOrLocNo.setLayoutData(getMiddleComposite().getGDForText());
		txtSlotOrLocNo.setEnabled(false);
		
		/*dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.WRAP|SWT.BORDER);
		dummyLabel.setLayoutData(gdDummyLbl);*/
		
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

	
	/**
	 * @return the lblSlotOrLocNo
	 */
	public CbctlMandatoryLabel getLblSlotOrLocNo() {
		return lblSlotOrLocNo;
	}

	/**
	 * @param lblSlotOrLocNo the lblSlotOrLocNo to set
	 */
	public void setLblSlotOrLocNo(CbctlMandatoryLabel lblSlotOrLocNo) {
		this.lblSlotOrLocNo = lblSlotOrLocNo;
	}

	/**
	 * @return the siteParam
	 */
	public boolean[] getSiteParam() {
		return siteParam;
	}

	/**
	 * @param siteParam the siteParam to set
	 */
	public void setSiteParam(boolean[] siteParam) {
		this.siteParam = siteParam;
	}

	/**
	 * @return the txtSlotOrLocNo
	 */
	public SDSTSText getTxtSlotOrLocNo() {
		return txtSlotOrLocNo;
	}

	/**
	 * @param txtSlotOrLocNo the txtSlotOrLocNo to set
	 */
	public void setTxtSlotOrLocNo(SDSTSText txtSlotOrLocNo) {
		this.txtSlotOrLocNo = txtSlotOrLocNo;
	}
	
	public RadioButtonControl getRadioButtonControl() {
		return radioButtonControl;
	}

	public void setRadioButtonControl(RadioButtonControl middleButtonGroup) {
		this.radioButtonControl = middleButtonGroup;
	}

	public TSButtonLabel getStandSlotRadioImage() {
		return standSlotRadioImage;
	}

	public void setStandSlotRadioImage(TSButtonLabel standSlotRadioImage) {
		this.standSlotRadioImage = standSlotRadioImage;
	}

	public TSButtonLabel getSeqRadioImage() {
		return seqRadioImage;
	}

	public void setSeqRadioImage(TSButtonLabel seqRadioImage) {
		this.seqRadioImage = seqRadioImage;
	}

	/**
	 * @return the btnPrintReport
	 *//*
	public CbctlButton getBtnPrintReport() {
		return btnPrintReport;
	}

	*//**
	 * @param btnPrintReport the btnPrintReport to set
	 *//*
	public void setBtnPrintReport(CbctlButton btnPrintReport) {
		this.btnPrintReport = btnPrintReport;
	}
*/
} // @jve:decl-index=0:visual-constraint="215,84"
