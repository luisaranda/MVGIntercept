/*****************************************************************************
 * $Id: EmployeeShiftSlotDetailsCompoisite.java,v 1.24, 2010-04-11 16:02:34Z, Suganthi, Kaliamoorthy$
 * $Date: 4/11/2010 11:02:34 AM$
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
import com.ballydev.sds.framework.control.TouchScreenShiftControl;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * This composite has the details regarding the employee's id, password and
 * Slot/Sequence/SlotLocation Number
 * 
 * @author anantharajr
 * @version $Revision: 25$
 * 
 */
public class EmployeeShiftSlotDetailsCompoisite extends TouchScreenBaseComposite {

	
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
	public CbctlLabel lblSlotSeqLocationNo = null;

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
	 * given below SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID
	 * PENDING_JACKPOT_PASSWORD_ENABLED PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION
	 */
	private String[] siteConfigParamFlag;

	/**
	 *  Sequence no label instance
	 */
	private CbctlLabel lblSequenceNumber = null;

	/**
	 *  Sequence no text instance
	 */
	private SDSTSText txtSequenceNumber = null;
	
	/**
	 * NumOfMins label instance
	 */
	private CbctlLabel lblNumOfMins = null;

	/**
	 * NumOfMins text instance
	 */
	private SDSTSText txtNumOfMins = null;

	private TSButtonLabel standSlotRadioImage = null;
	
	private TSButtonLabel seqRadioImage = null;
	
	private TSButtonLabel minRadioImage = null;
	
	private TouchScreenShiftControl shiftControl = null;
	
	
	/**
	 * Middle button group instance
	 */
	private RadioButtonControl radioButtonControl;

	/**
	 * Dummy Label
	 */
	private CbctlLabel dummyLabel = null;
	
	/**
	 * PendingJackpotComposite constructor, the boolean flag parameter is used
	 * to create the controls based on the Site Configuartion Parameters
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public EmployeeShiftSlotDetailsCompoisite(Composite parent, int style,
			String[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.PENDING_JP_HEADING));
		this.siteConfigParamFlag = flags;
		initialize("N");
		layout();
		
	}

	public void drawBottomComposite() {
		
		if(siteConfigParamFlag[3].equalsIgnoreCase("P"))
		{
			this.shiftControl = new TouchScreenShiftControl()
					.drawBottomControls(getBottomComposite(), SWT.NONE);
			
		}
	}

	/**
	 * This method initializes mainGroup
	 * 
	 */
	
	public void drawTopComposite() {
		System.out.println("Inside EmployeeShiftSlotDetailsComposite:Initialise");
		
		int rowCount = 1;
		
		
		if (siteConfigParamFlag[0].equalsIgnoreCase("true")) {
			++ rowCount;
			lblEmployeeId = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
			lblEmployeeId.setLayoutData(getTopComposite().getGDForLabel());

			txtEmployeeId = new SDSTSText(getTopComposite(), SWT.BORDER, LabelLoader
					.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID), FormConstants.FORM_EMPLOYEE_ID);
			txtEmployeeId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtEmployeeId.setLayoutData(getTopComposite().getGDForText());
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForSmallDummyLabel());
			}
		}
		
		
		if (siteConfigParamFlag[1].equalsIgnoreCase("true")) {
			++ rowCount;
			lblEmpPassword = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD));
			lblEmpPassword.setLayoutData(getTopComposite().getGDForMiddleLabel());

			txtEmpPassword = new SDSTSText(getTopComposite(), SWT.PASSWORD|SWT.BORDER, LabelLoader
					.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD),
					FormConstants.FORM_EMP_PASSWORD);
			txtEmpPassword.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmpPassword.setLayoutData(getTopComposite().getGDForText());
			
		}
		
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();

		
		
	}

	/**
	 * This method initializes mainGroup
	 * 
	 */
	
	public void drawMiddleComposite() {
		
		getMiddleComposite().setGdMiddleComp(getMiddleComposite().getGridDataForSingleMiddleCmp());
		getMiddleComposite().setGlMiddleCom(getMiddleComposite().getLayoutForSingleMiddleCmp());
		getMiddleComposite().setLayoutDataForMiddle();
		getMiddleComposite().setBackground(SDSControlFactory.getTSMiddleBodyColor());
				
		new TouchScreenMiddleSingleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEARCH_BY_ONE));
		
		
		radioButtonControl = new RadioButtonControl(LabelLoader.getLabelValue(LabelKeyConstants.LBL_SEARCH_BY_ONE));
		GridData gdDummyLbl = new GridData();
		gdDummyLbl.horizontalIndent = 10;
		gdDummyLbl.horizontalAlignment = GridData.BEGINNING;
		
		GridData gdRadio = new GridData();
		gdRadio.horizontalAlignment = GridData.BEGINNING;
		gdRadio.horizontalIndent = 10;
		
		if (siteConfigParamFlag[2].equalsIgnoreCase("1")) {
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
			
			lblSlotSeqLocationNo = new CbctlLabel(getMiddleComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
			lblSlotSeqLocationNo.setLayoutData(getMiddleComposite().getGDForLabel());
		}else if (siteConfigParamFlag[2].equalsIgnoreCase("2")){
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
			
			lblSlotSeqLocationNo = new CbctlLabel(getMiddleComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER));
			lblSlotSeqLocationNo.setLayoutData(getMiddleComposite().getGDForLabel());
		}
		
		dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
		dummyLabel.setLayoutData(gdDummyLbl);
		lblSequenceNumber = new CbctlLabel(getMiddleComposite(), SWT.NONE,
				LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER));
		lblSequenceNumber.setLayoutData(getMiddleComposite().getGDForLabel());
		
	
		dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
		dummyLabel.setLayoutData(gdDummyLbl);
		lblNumOfMins = new CbctlLabel(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.NUMBER_OF_MINUTES));
		lblNumOfMins.setLayoutData(getMiddleComposite().getGDForLabel());
		

		if (siteConfigParamFlag[2].equalsIgnoreCase("1")) {
			
			standSlotRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE,"slotradio");
			standSlotRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
			standSlotRadioImage.setLayoutData(gdRadio);
			radioButtonControl.add(standSlotRadioImage);
			
			txtSlotSeqLocationNo = new SDSTSText(
					getMiddleComposite(),
					SWT.NONE|SWT.LEFT,
					LabelLoader
							.getLabelValue(LabelKeyConstants.SLOT_NUMBER),
					FormConstants.FORM_SLOT_NO);
			txtSlotSeqLocationNo.setTextLimit(IFieldTextLimits.SLOT_NO_TEXT_LIMIT);
			txtSlotSeqLocationNo.setLayoutData(getMiddleComposite().getGDForText());
			log.debug("............ slot No form prop");
		} else if (siteConfigParamFlag[2].equalsIgnoreCase("2"))
		{
			
			//standSlotRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE,null, "", "standradio", false);
			standSlotRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE,"standradio");
			standSlotRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
			standSlotRadioImage.setLayoutData(gdRadio);
			radioButtonControl.add(standSlotRadioImage);
			
			txtSlotSeqLocationNo = new SDSTSText(
					getMiddleComposite(),
					SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.STAND_NUMBER),
							FormConstants.FORM_SLOT_LOCATION_NO);
			txtSlotSeqLocationNo.setTextLimit(IFieldTextLimits.SLOT_LOCATION_TEXT_LIMIT);
			txtSlotSeqLocationNo.setLayoutData(getMiddleComposite().getGDForText());
			log.debug("............ slot location no form prop");
		}		

		
		seqRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE,"seqradio");
		seqRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		seqRadioImage.setLayoutData(gdRadio);
		radioButtonControl.add(seqRadioImage);
		
		txtSequenceNumber = new SDSTSText(getMiddleComposite(), SWT.NONE|SWT.LEFT, LabelLoader
				.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER),
				FormConstants.FORM_SEQUENCE_NO);
		txtSequenceNumber.setTextLimit(IFieldTextLimits.SEQUENCE_NO_TEXT_LIMIT);
		txtSequenceNumber.setEnabled(false);
		txtSequenceNumber.setLayoutData(getMiddleComposite().getGDForText());
	
		minRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE,"minradio");
		minRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
		minRadioImage.setLayoutData(gdRadio);
		radioButtonControl.add(minRadioImage);
		
		txtNumOfMins = new SDSTSText(getMiddleComposite(), SWT.BORDER|SWT.LEFT, "", FormConstants.FORM_NUMBER_OF_MINUTES);
		txtNumOfMins.setTextLimit(IFieldTextLimits.MINUTES_TEXT_LIMIT);
		txtNumOfMins.setEnabled(false);
		txtNumOfMins.setLayoutData(getMiddleComposite().getGDForText());
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
	 * @return the lblEmpPassword
	 */
	public CbctlMandatoryLabel getLblEmpPassword() {
		return lblEmpPassword;
	}

	/**
	 * @param lblEmpPassword
	 *            the lblEmpPassword to set
	 */
	public void setLblEmpPassword(CbctlMandatoryLabel lblEmpPassword) {
		this.lblEmpPassword = lblEmpPassword;
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
	 * @return the txtEmpPassword
	 */
	public SDSTSText getTxtEmpPassword() {
		return txtEmpPassword;
	}

	/**
	 * @param txtEmpPassword
	 *            the txtEmpPassword to set
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
	 * @param txtSlotSeqLocationNo
	 *            the txtSlotSeqLocationNo to set
	 */
	public void setTxtSlotSeqLocationNo(SDSTSText txtSlotSeqLocationNo) {
		this.txtSlotSeqLocationNo = txtSlotSeqLocationNo;
	}

	/**
	 * @return the siteConfigParamFlag
	 */
	public String[] getSiteConfigParamFlag() {
		return siteConfigParamFlag;
	}

	/**
	 * @param siteConfigParamFlag
	 *            the siteConfigParamFlag to set
	 */
	public void setSiteConfigParamFlag(String[] siteConfigParamFlag) {
		this.siteConfigParamFlag = siteConfigParamFlag;
	}


	/**
	 * @return the txtSequenceNumber
	 */
	public SDSTSText getTxtSequenceNumber() {
		return txtSequenceNumber;
	}

	/**
	 * @param txtSequenceNumber
	 *            the txtSequenceNumber to set
	 */
	public void setTxtSequenceNumber(SDSTSText txtSequenceNumber) {
		this.txtSequenceNumber = txtSequenceNumber;
	}

	/**
	 * @return the txtNumOfMins
	 */
	public SDSTSText getTxtNumOfMins() {
		return txtNumOfMins;
	}

	/**
	 * @param txtNumOfMins the txtNumOfMins to set
	 */
	public void setTxtNumOfMins(SDSTSText txtNumOfMins) {
		this.txtNumOfMins = txtNumOfMins;
	}

	/**
	 * @return the lblNumOfMins
	 */
	public CbctlLabel getLblNumOfMins() {
		return lblNumOfMins;
	}

	/**
	 * @param lblNumOfMins the lblNumOfMins to set
	 */
	public void setLblNumOfMins(CbctlLabel lblNumOfMins) {
		this.lblNumOfMins = lblNumOfMins;
	}

	/**
	 * @return the lblSequenceNumber
	 */
	public CbctlLabel getLblSequenceNumber() {
		return lblSequenceNumber;
	}

	/**
	 * @param lblSequenceNumber the lblSequenceNumber to set
	 */
	public void setLblSequenceNumber(CbctlLabel lblSequenceNumber) {
		this.lblSequenceNumber = lblSequenceNumber;
	}

	/**
	 * @return the lblSlotSeqLocationNo
	 */
	public CbctlLabel getLblSlotSeqLocationNo() {
		return lblSlotSeqLocationNo;
	}

	/**
	 * @param lblSlotSeqLocationNo the lblSlotSeqLocationNo to set
	 */
	public void setLblSlotSeqLocationNo(CbctlLabel lblSlotSeqLocationNo) {
		this.lblSlotSeqLocationNo = lblSlotSeqLocationNo;
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

	public TSButtonLabel getMinRadioImage() {
		return minRadioImage;
	}

	public void setMinRadioImage(TSButtonLabel minRadioImage) {
		this.minRadioImage = minRadioImage;
	}

	public TouchScreenShiftControl getShiftControl() {
		return shiftControl;
	}

	public void setShiftControl(TouchScreenShiftControl shiftControl) {
		this.shiftControl = shiftControl;
	}





	/**
	 * @return the txtSequenceNumber
	 */

} // @jve:decl-index=0:visual-constraint="5,16"
