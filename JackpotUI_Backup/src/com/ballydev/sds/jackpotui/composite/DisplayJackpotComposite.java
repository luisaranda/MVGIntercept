/*****************************************************************************
 * $Id: DisplayJackpotComposite.java,v 1.21, 2010-06-08 13:31:41Z, Subha Viswanathan$
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
 * This composite is used as the login screen for displaying pending jackpot
 * slips
 * 
 * @author vijayrajm
 * @version $Revision: 22$
 */
public class DisplayJackpotComposite extends TouchScreenBaseComposite {

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
	 * SlotNumber Label instance
	 */
	private CbctlLabel lblSlotNumber = null;

	/**
	 * SlotNumber Text instance
	 */
	private SDSTSText txtSlotNumber = null;

	/**
	 * A String array flags for the configuration parameters
	 */
	private String[] flags;
	
	
	/**
	 * Sequence no label instance
	 */
	private CbctlLabel lblSequenceNumber = null;

	/**
	 *  Sequence no text instance
	 */
	private SDSTSText txtSequenceNumber = null;

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
	 * DisplayJackpotComposite constructor
	 * 
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public DisplayJackpotComposite(Composite parent, int style, String[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.DISPLAY_JP_HEADING));
		
		this.flags = flags;
		initialize("D");
		
		layout();
	}


	public void drawTopComposite() {
		System.out.println("Inside DisplayJackpotComposite:Initialise");
		
		int rowCount = 1;
		
		if (flags[0].equalsIgnoreCase("yes")) {
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
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}
		
		

		if (flags[1].equalsIgnoreCase("yes")) {
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
		
		if (flags[2].equalsIgnoreCase("1")) {
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
			lblSlotNumber = new CbctlLabel(getMiddleComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
			lblSlotNumber.setLayoutData(getMiddleComposite().getGDForLabel());
			
			}else if (flags[2].equalsIgnoreCase("2")) {
				dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
				dummyLabel.setLayoutData(gdDummyLbl);
				lblSlotNumber = new CbctlLabel(getMiddleComposite(), SWT.NONE,
						LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER));
				lblSlotNumber.setLayoutData(getMiddleComposite().getGDForLabel());
				
			}
			
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
			
			lblSequenceNumber =  new CbctlLabel(getMiddleComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER_OR_ALL));
			lblSequenceNumber.setLayoutData(getMiddleComposite().getGDForExtraLabel());
			
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
			
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
			
			
			
			
			if (flags[2].equalsIgnoreCase("1")) {
				standSlotRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE, "slotstandradio");
				standSlotRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
				standSlotRadioImage.setLayoutData(gdRadio);
				radioButtonControl.add(standSlotRadioImage);
				
				txtSlotNumber = new SDSTSText(getMiddleComposite(), SWT.NONE, LabelLoader
						.getLabelValue(LabelKeyConstants.SLOT_NUMBER), FormConstants.FORM_SLOT_NO);
				txtSlotNumber.setTextLimit(IFieldTextLimits.SLOT_NO_TEXT_LIMIT);
				txtSlotNumber.setLayoutData(getMiddleComposite().getGDForText());
			}else if (flags[2].equalsIgnoreCase("2")) {
				
				standSlotRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE, "slotstandradio");
				standSlotRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
				standSlotRadioImage.setLayoutData(gdRadio);
				radioButtonControl.add(standSlotRadioImage);
				
				txtSlotNumber = new SDSTSText(getMiddleComposite(), SWT.NONE, LabelLoader
						.getLabelValue(LabelKeyConstants.SLOT_NUMBER), FormConstants.FORM_SLOT_LOCATION_NO);
				txtSlotNumber.setTextLimit(IFieldTextLimits.SLOT_LOCATION_TEXT_LIMIT);
				txtSlotNumber.setLayoutData(getMiddleComposite().getGDForText());
			}
			seqRadioImage = new TSButtonLabel(getMiddleComposite(), SWT.NONE, "seqradio");
			seqRadioImage.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(ImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
			seqRadioImage.setLayoutData(gdRadio);
			radioButtonControl.add(seqRadioImage);
		
			txtSequenceNumber = new SDSTSText(getMiddleComposite(), SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER_OR_ALL),
					FormConstants.FORM_SEQUENCE_NO);
			txtSequenceNumber.setTextLimit(IFieldTextLimits.SEQUENCE_NO_TEXT_LIMIT);
			txtSequenceNumber.setLayoutData(getMiddleComposite().getGDForText());
			txtSequenceNumber.setEnabled(false);
			dummyLabel = new CbctlLabel(getMiddleComposite(), SWT.NONE);
			dummyLabel.setLayoutData(gdDummyLbl);
		
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
	public SDSTSText getTxtSlotNumber() {
		return txtSlotNumber;
	}

	/**
	 * @param txtSequenceNo
	 *            the txtSequenceNo to set
	 */
	public void setTxtSlotNumber(SDSTSText txtSequenceNo) {
		this.txtSlotNumber = txtSequenceNo;
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
	public CbctlLabel getLblSlotNumber() {
		return lblSlotNumber;
	}

	/**
	 * @param lblSequenceNo
	 *            the lblSequenceNo to set
	 */
	public void setLblSlotNumber(CbctlLabel lblSequenceNo) {
		this.lblSlotNumber = lblSequenceNo;
	}

	/**
	 * @return the txtSequenceNumber
	 */
	public SDSTSText getTxtSequenceNumber() {
		return txtSequenceNumber;
	}

	/**
	 * @param txtSequenceNumber the txtSequenceNumber to set
	 */
	public void setTxtSequenceNumber(SDSTSText txtSequenceNumber) {
		this.txtSequenceNumber = txtSequenceNumber;
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

} 
