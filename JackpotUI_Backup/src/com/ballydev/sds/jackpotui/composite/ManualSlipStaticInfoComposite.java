/*****************************************************************************
 * $Id: ManualSlipStaticInfoComposite.java,v 1.17, 2010-09-22 11:48:45Z, Subha Viswanathan$
 * $Date: 9/22/2010 6:48:45 AM$
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenMiddleComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;


/**
 * @author dambereen
 * @version $Revision: 18$
 */
public class ManualSlipStaticInfoComposite extends Composite {

	/**
	 * Composite heading label instance
	 */
	private CbctlLabel lblHeading = null;

	/**
	 * Slot no label instance
	 */
	private CbctlLabel lblSlotNumber = null;

	/**
	 * Slot no info label instance
	 */
	private CbctlLabel lblSlotInfo = null;

	/**
	 * Stand no label instance
	 */
	private CbctlLabel lblStandNumber = null;

	/**
	 * Stand no info label instance
	 */
	private CbctlLabel lblStandInfo = null;

	/**
	 * Hand paid amt label instance
	 */
	private CbctlLabel lblHandPaidAmount = null;

	/**
	 * Hand paid amt info label instance
	 */
	private CbctlLabel lblHandPaidAmountInfo = null;

	/**
	 * Jackpot type label instance
	 */
	private CbctlLabel lblJackpotType = null;

	/**
	 * Jackpot type info label instance
	 */
	private CbctlLabel lblJackpotTypeInfo = null;

	/**
	 * Employee id label instance
	 */
	private CbctlLabel lblEmployeeId = null;

	/**
	 * Employee id info label instance
	 */
	private CbctlLabel lblEmployeeIdInfo = null;

	/**
	 * Shift label instance
	 */
	private CbctlLabel lblShift = null;

	/**
	 * Shift label info instance
	 */
	private CbctlLabel lblShiftInfo = null;
	
	/**
	 * Cardless Account number instance
	 */
	private CbctlLabel lblAccountNumber = null;
	/**
	 * Cardless Account number info text instance
	 */
	private CbctlLabel lblAccountNumberInfo = null;

	private TouchScreenMiddleComposite infoComposite = null;
	
	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;

	/**
	 * ManualSlipStaticInfoComposite Class's constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public ManualSlipStaticInfoComposite(TouchScreenMiddleComposite middleComposite, int style) {
		
		super(middleComposite, style);
		this.infoComposite = middleComposite;
		if(Util.isSmallerResolution()){
			middleBodyWidth = ITSControlSizeConstants.S_MIDDLE_WIDTH;
			middleBodyHeight = ITSControlSizeConstants.S_MIDDLE_BODY_HEIGHT;
		}
		initialize();
		setLayoutData();
	}

	/**
	 * Method to create the group
	 */
	private void initialize() {
		
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID)
				.equalsIgnoreCase("yes")) {
			lblEmployeeId = new CbctlLabel(this, SWT.WRAP, LabelLoader
					.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
			lblEmployeeId.setLayoutData(infoComposite.getGDForLargeBodyLabel());

			lblEmployeeIdInfo = new CbctlLabel(this, SWT.WRAP);
			lblEmployeeIdInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
			
		}
		lblSlotNumber = new CbctlLabel(this, SWT.WRAP, LabelLoader
				.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
		lblSlotNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());

		lblSlotInfo = new CbctlLabel(this, SWT.WRAP);
		lblSlotInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());

		lblStandNumber = new CbctlLabel(this, SWT.WRAP, LabelLoader
				.getLabelValue(LabelKeyConstants.STAND_NUMBER));
		lblStandNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());

		lblStandInfo = new CbctlLabel(this, SWT.WRAP);
		lblStandInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());

		lblShift = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.SHIFT));
		lblShift.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		lblShiftInfo = new CbctlLabel(this, SWT.WRAP);
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
			
			lblShiftInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
			
		}else if (!MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)) {
			if(MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
				.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY)){
				lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
			}else if(MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
					.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_SWING)){
					lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
			}else if(MainMenuController.jackpotSiteConfigParams.get(
					ISiteConfigConstants.PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT)
					.equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)){
					lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
			}
			lblShiftInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
			
		}

		lblHandPaidAmount = new CbctlLabel(this, SWT.WRAP, LabelLoader
				.getLabelValue(LabelKeyConstants.HAND_PAID_AMOUNT_LABEL));
		lblHandPaidAmount.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		lblHandPaidAmount.setVisible(false);

		lblHandPaidAmountInfo = new CbctlLabel(this, SWT.WRAP);
		lblHandPaidAmountInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		lblHandPaidAmountInfo.setVisible(false);

		lblJackpotType = new CbctlLabel(this, SWT.WRAP,LabelLoader
				.getLabelValue(LabelKeyConstants.JACKPOT_TYPE));
		
		lblJackpotType.setLayoutData(infoComposite.getGDForLargeBodyLabel());

		lblJackpotType.setVisible(false);
		
		lblJackpotTypeInfo = new CbctlLabel(this, SWT.WRAP);
		lblJackpotTypeInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());

		lblJackpotTypeInfo.setVisible(false);
		
		lblAccountNumber = new CbctlLabel(this, SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.CASHLESS_ACCOUNT_NUMER));			
		lblAccountNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());		
		lblAccountNumber.setVisible(false);
		lblAccountNumberInfo = new CbctlLabel(this, SWT.WRAP);
		lblAccountNumberInfo.setText(""+MainMenuController.jackpotForm.getAccountNumber());
		lblAccountNumberInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());	
		lblAccountNumberInfo.setVisible(false);
		
	}

	/**
	 * This method initializes infoGroup
	 * 
	 */
	
	private void setLayoutData() {
		
		GridData gdSlipComp = new GridData();
		gdSlipComp.grabExcessVerticalSpace = false;
		gdSlipComp.verticalAlignment = GridData.FILL;
		gdSlipComp.horizontalSpan = 2;
		
		gdSlipComp.widthHint = middleBodyWidth;
		gdSlipComp.heightHint = middleBodyHeight;
		gdSlipComp.horizontalAlignment = GridData.BEGINNING;
		
		GridLayout glSlipComp = new GridLayout();
		glSlipComp.numColumns = 2;
		glSlipComp.verticalSpacing = 10;
		glSlipComp.marginWidth = 5;
		glSlipComp.marginHeight = 5;
		glSlipComp.makeColumnsEqualWidth = false;
		glSlipComp.horizontalSpacing = 5;
		this.setLayoutData(gdSlipComp);
		this.setLayout(glSlipComp);
		this.setBackground(SDSControlFactory.getTSMiddleBodyShadedColor());
	}

	/**
	 * @return the lblHandPaidAmount
	 */
	public CbctlLabel getLblHandPaidAmount() {
		return lblHandPaidAmount;
	}

	/**
	 * @param lblHandPaidAmount
	 *            the lblHandPaidAmount to set
	 */
	public void setLblHandPaidAmount(CbctlLabel lblHandPaidAmount) {
		this.lblHandPaidAmount = lblHandPaidAmount;
	}

	/**
	 * @return the lblHandPaidAmountInfo
	 */
	public CbctlLabel getLblHandPaidAmountInfo() {
		return lblHandPaidAmountInfo;
	}

	/**
	 * @param lblHandPaidAmountInfo
	 *            the lblHandPaidAmountInfo to set
	 */
	public void setLblHandPaidAmountInfo(CbctlLabel lblHandPaidAmountInfo) {
		this.lblHandPaidAmountInfo = lblHandPaidAmountInfo;
	}

	/**
	 * @return the lblHeading
	 */
	public CbctlLabel getLblHeading() {
		return lblHeading;
	}

	/**
	 * @param lblHeading
	 *            the lblHeading to set
	 */
	public void setLblHeading(CbctlLabel lblHeading) {
		this.lblHeading = lblHeading;
	}

	/**
	 * @return the lblMachinePaidAmount
	 */
	/*public CbctlLabel getLblMachinePaidAmount() {
		return lblMachinePaidAmount;
	}

	*//**
	 * @param lblMachinePaidAmount
	 *            the lblMachinePaidAmount to set
	 *//*
	public void setLblMachinePaidAmount(CbctlLabel lblMachinePaidAmount) {
		this.lblMachinePaidAmount = lblMachinePaidAmount;
	}

	*//**
	 * @return the lblMachinePaidAmountInfo
	 *//*
	public CbctlLabel getLblMachinePaidAmountInfo() {
		return lblMachinePaidAmountInfo;
	}

	*//**
	 * @param lblMachinePaidAmountInfo
	 *            the lblMachinePaidAmountInfo to set
	 *//*
	public void setLblMachinePaidAmountInfo(CbctlLabel lblMachinePaidAmountInfo) {
		this.lblMachinePaidAmountInfo = lblMachinePaidAmountInfo;
	}*/

	/**
	 * @return the lblSlotInfo
	 */
	public CbctlLabel getLblSlotInfo() {
		return lblSlotInfo;
	}

	/**
	 * @return the lblSlotNumber
	 */
	public CbctlLabel getLblSlotNumber() {
		return lblSlotNumber;
	}

	/**
	 * @return the lblStandInfo
	 */
	public CbctlLabel getLblStandInfo() {
		return lblStandInfo;
	}

	/**
	 * @param lblStandInfo
	 *            the lblStandInfo to set
	 */
	public void setLblStandInfo(CbctlLabel lblStandInfo) {
		this.lblStandInfo = lblStandInfo;
	}

	/**
	 * @return the lblStandNumber
	 */
	public CbctlLabel getLblStandNumber() {
		return lblStandNumber;
	}

	/**
	 * @param lblStandNumber
	 *            the lblStandNumber to set
	 */
	public void setLblStandNumber(CbctlLabel lblStandNumber) {
		this.lblStandNumber = lblStandNumber;
	}

	/**
	 * @return the lblEmployeeId
	 */
	public CbctlLabel getLblEmployeeId() {
		return lblEmployeeId;
	}

	/**
	 * @param lblEmployeeId
	 *            the lblEmployeeId to set
	 */
	public void setLblEmployeeId(CbctlLabel lblEmployeeId) {
		this.lblEmployeeId = lblEmployeeId;
	}

	/**
	 * @return the lblEmployeeIdInfo
	 */
	public CbctlLabel getLblEmployeeIdInfo() {
		return lblEmployeeIdInfo;
	}

	/**
	 * @param lblEmployeeIdInfo
	 *            the lblEmployeeIdInfo to set
	 */
	public void setLblEmployeeIdInfo(CbctlLabel lblEmployeeIdInfo) {
		this.lblEmployeeIdInfo = lblEmployeeIdInfo;
	}

	/**
	 * @return the lblJackpotType
	 */
	public CbctlLabel getLblJackpotType() {
		return lblJackpotType;
	}

	/**
	 * @return the lblJackpotTypeInfo
	 */
	public CbctlLabel getLblJackpotTypeInfo() {
		return lblJackpotTypeInfo;
	}

	/**
	 * @param lblJackpotTypeInfo
	 *            the lblJackpotTypeInfo to set
	 */

	/**
	 * @return the lblShift
	 */
	public CbctlLabel getLblShift() {
		return lblShift;
	}

	/**
	 * @return the lblShiftInfo
	 */
	public CbctlLabel getLblShiftInfo() {
		return lblShiftInfo;
	}

	public TouchScreenMiddleComposite getInfoComposite() {
		return infoComposite;
	}

	public void setInfoComposite(TouchScreenMiddleComposite infoComposite) {
		this.infoComposite = infoComposite;
	}

	
	/**
	 * Gets the Label for displaying account number
	 * @return
	 */
	public CbctlLabel getLblAccountNumber() {
		return lblAccountNumber;
	}

	/**
	 * Sets the Label for displaying account number
	 * @param lblAccountNumber
	 */
	public void setLblAccountNumber(CbctlLabel lblAccountNumber) {
		this.lblAccountNumber = lblAccountNumber;
	}

	/**
	 * Gets the Label for displaying account number info
	 * @return
	 */
	public CbctlLabel getLblAccountNumberInfo() {
		return lblAccountNumberInfo;
	}

	/**
	 * Sets the Label for displaying account number info
	 * @param lblAccountNumberInfo
	 */
	public void setLblAccountNumberInfo(CbctlLabel lblAccountNumberInfo) {
		this.lblAccountNumberInfo = lblAccountNumberInfo;
	}


	/**
	 * @return the lblTaxType
	 *//*
	public CbctlLabel getLblTaxType() {
		return lblTaxType;
	}


	*//**
	 * @return the lblTaxTypeInfo
	 *//*
	public CbctlLabel getLblTaxTypeInfo() {
		return lblTaxTypeInfo;
	}*/
}  //  @jve:decl-index=0:visual-constraint="8,3"
