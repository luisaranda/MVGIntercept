/*****************************************************************************
 * $Id: SlipStaticInfoComposite.java,v 1.31.1.1, 2011-05-17 20:01:53Z, SDS12.3.2CheckinUser$
 * $Date: 5/17/2011 3:01:53 PM$
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
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.util.ConversionUtil;
import com.ballydev.sds.jackpotui.util.DateUtil;

/**
 * Composite that will contain all the static jackpot slip details
 * @author anantharajr
 * @version $Revision: 34$
 */
public class SlipStaticInfoComposite extends Composite{
	
	
	/**
	 * Group instance
	 */
	private TouchScreenMiddleComposite infoComposite = null;
	/**
	 * Slot no label instance
	 */
	private CbctlLabel lblSlotNumber = null;
	/**
	 * Sequence no label instance
	 */
	private CbctlLabel lblSequenceNumber = null;
	/**
	 * Stand no label instance
	 */
	private CbctlLabel lblStandNumber = null;
	/**
	 * Original amt label instance
	 */
	private CbctlLabel lblOriginalAmount = null;
	/**
	 * Date label instance
	 */
	private CbctlLabel lblTimeStamp = null;
	/**
	 * Slot info label instance
	 */
	private CbctlLabel lblSlotInfo = null;
	/**
	 * Stand no info label instance
	 */
	private CbctlLabel lblStandInfo = null;
	/**
	 * Sequence no info label instance
	 */
	private CbctlLabel lblSequenceInfo = null;
	/**
	 * Original amt info label instance
	 */
	private CbctlLabel lblOriginalAmountInfo = null;
	/**
	 * Date info text instance
	 */
	private CbctlLabel lblTimeStampInfo = null;
	/**
	 * Adjusted amount label instance
	 */
	private CbctlLabel lblAdjustedAmountLbl = null;
	/**
	 *  Adjusted amount info label instance
	 */
	private CbctlLabel lblAdjustedAmount = null;
	/**
	 * Machine amt label instance
	 */
	/*private CbctlLabel lblMachinePaidAmount = null;
	*//**
	 * Machine amt info label instance
	 *//*
	private CbctlLabel lblMachinePaidAmountInfo = null;*/
	/**
	 * Shift label instance
	 */
	private CbctlLabel lblShift = null;
	/**
	 * Shift info label instance
	 */
	private CbctlLabel lblShiftInfo = null;	
	/**
	 * Associated PlayerCard instance
	 */
	private CbctlLabel lblAssPlyCard = null;
	/**
	 * Associated PlayerCard info text instance
	 */
	private CbctlLabel lblAssPlyCardInfo = null;	
	/**
	 * Cardless Account number instance
	 */
	private CbctlLabel lblAccountNumber = null;
	/**
	 * Cardless Account number info text instance
	 */
	private CbctlLabel lblAccountNumberInfo = null;	
	/**
	 * Screen title heading label instance
	 */
	private CbctlLabel lblHeading = null;
	
	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;
	
	/**
	 * Class constructor
	 * @param middle
	 * @param style
	 */
	public SlipStaticInfoComposite(TouchScreenMiddleComposite middleComposite, int style) {
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
	 * Initialize method
	 */
	private void initialize() {
		
		lblSlotNumber = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
		lblSlotNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		
		lblSlotInfo = new CbctlLabel(this, SWT.WRAP);
		lblSlotInfo.setText(StringUtil.trimAcnfNo(MainMenuController.jackpotForm.getSlotNo()));
		lblSlotInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		
		
		lblStandNumber = new CbctlLabel(this, SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER));
		lblStandNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		
		lblStandInfo = new CbctlLabel(this, SWT.WRAP);
		if (MainMenuController.jackpotForm.getSlotLocationNo() != null) {
			lblStandInfo.setText(MainMenuController.jackpotForm.getSlotLocationNo().toUpperCase());
		}
		lblStandInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		
		lblSequenceNumber = new CbctlLabel(this, SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.SEQUENCE_NUMBER));
		lblSequenceNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		
		lblSequenceInfo = new CbctlLabel(this, SWT.WRAP);
		lblSequenceInfo.setText(""+MainMenuController.jackpotForm.getSequenceNumber());
		lblSequenceInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		
		
		lblTimeStamp = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.DATE_OR_TIME));
		lblTimeStamp.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		
		java.sql.Timestamp timeStamp = DateUtil.getTime(MainMenuController.jackpotForm.getTransactionDate());
		
		lblTimeStampInfo = new CbctlLabel(this, SWT.WRAP);
		lblTimeStampInfo.setText(com.ballydev.sds.framework.util.DateUtil.getFormattedDate(timeStamp));
		lblTimeStampInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		
		
		lblShift = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.SHIFT));
		lblShift.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		
		lblShiftInfo = new CbctlLabel(this, SWT.WRAP);
		if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_DAY)) {
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_SWING)) {
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
		}else if(MainMenuController.jackpotForm.getShift().equalsIgnoreCase(ILabelConstants.SITE_VALUE_SHIFT_GRAVEYARD)) {
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
		}else {
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}
		lblShiftInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		
		lblAdjustedAmountLbl = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_AMOUNT_LABEL));
		
		lblAdjustedAmount = new CbctlLabel(this, SWT.WRAP);
		
		long expressJPlimit=0;
		expressJPlimit=(long)Double.parseDouble(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.EXPRESS_JACKPOT_LIMIT));
		expressJPlimit=ConversionUtil.dollarToCentsRtnLong(String.valueOf(expressJPlimit));
				
		if(MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.EXPRESS_JACKPOT_ENABLED)
				.equalsIgnoreCase("Yes")
				&& MainMenuController.jackpotForm.getOriginalAmount() < expressJPlimit) {

			lblAdjustedAmount.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()+ ConversionUtil.maskAmount());
			
		} else if(MainMenuController.jackpotForm.getHandPaidAmount()==0){
			lblAdjustedAmount.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getOriginalAmount()));
		} else {
			lblAdjustedAmount.setText(MainMenuController.jackpotForm.getSiteCurrencySymbol()
					+ ConversionUtil.centsToDollar(MainMenuController.jackpotForm.getHandPaidAmount()));
		}
		lblAdjustedAmount.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT).equalsIgnoreCase("Yes")
				&& !MainMenuController.jackpotForm.isJpProgControllerGenerated()
				&& MainMenuController.jackpotForm.getAccountNumber() != null 
				&& !MainMenuController.jackpotForm.getAccountNumber().trim().isEmpty()
				&& Long.parseLong(MainMenuController.jackpotForm.getAccountNumber()) > 0){
						
			lblAccountNumber = new CbctlLabel(this, SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.CASHLESS_ACCOUNT_NUMER));
			
			lblAccountNumber.setLayoutData(infoComposite.getGDForLargeBodyLabel());
			
			lblAccountNumberInfo = new CbctlLabel(this, SWT.WRAP);
			if(MainMenuController.jackpotForm.getAccountNumber() != null) {
				lblAccountNumberInfo.setText(MainMenuController.jackpotForm.getAccountNumber().toString());
			} 
			lblAccountNumberInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
		} 
		lblAssPlyCard = new CbctlLabel(this, SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.ASSOCIATED_PLAYER_CARD));
		
		lblAssPlyCard.setLayoutData(infoComposite.getGDForLargeBodyLabel());
		
		lblAssPlyCardInfo = new CbctlLabel(this, SWT.WRAP);
		lblAssPlyCardInfo.setText(""+MainMenuController.jackpotForm.getAssociatedPlayerCard());
		lblAssPlyCardInfo.setLayoutData(infoComposite.getGDForInfoBodyLabel());
	}

	/**
	 * Method to set the layout data
	 */
	private void setLayoutData() {
		
		GridData gdSlipComp = new GridData();
		gdSlipComp.grabExcessVerticalSpace = false;
		gdSlipComp.verticalAlignment = GridData.FILL;
		//gdSlipComp.horizontalSpan = 2;
		
		gdSlipComp.widthHint = middleBodyWidth;
		gdSlipComp.heightHint = middleBodyHeight;
		gdSlipComp.horizontalAlignment = GridData.BEGINNING;
		
		GridLayout glSlipComp = new GridLayout();
		glSlipComp.numColumns = 2;
		glSlipComp.verticalSpacing = 10;
		glSlipComp.marginWidth = 5;
		glSlipComp.marginHeight = 5;
		//glSlipComp.makeColumnsEqualWidth = false;
		glSlipComp.horizontalSpacing = 5;
		this.setLayoutData(gdSlipComp);
		this.setLayout(glSlipComp);
		this.setBackground(SDSControlFactory.getTSMiddleBodyShadedColor());
	}

	/**
	 * @return the infoGroup
	 */
	public Composite getInfoComposite() {
		return infoComposite;
	}

	/**
	 * @return the lblAdjustedAmount
	 */
	public CbctlLabel getLblAdjustedAmount() {
		return lblAdjustedAmountLbl;
	}

	/**
	 * @return the lblAdjustedAmountLbl
	 */
	public CbctlLabel getLblAdjustedAmountLbl() {
		return lblAdjustedAmount;
	}

	/**
	 * @return the lblHeading
	 */
	public CbctlLabel getLblHeading() {
		return lblHeading;
	}

	/**
	 * @param lblHeading the lblHeading to set
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
	 * @param lblMachinePaidAmount the lblMachinePaidAmount to set
	 *//*
	public void setLblMachinePaidAmount(CbctlLabel lblMachinePaidAmount) {
		this.lblMachinePaidAmount = lblMachinePaidAmount;
	}*/

	/**
	 * @return the lblMachinePaidAmountInfo
	 */
	/*public CbctlLabel getLblMachinePaidAmountInfo() {
		return lblMachinePaidAmountInfo;
	}

	*//**
	 * @param lblMachinePaidAmountInfo the lblMachinePaidAmountInfo to set
	 *//*
	public void setLblMachinePaidAmountInfo(CbctlLabel lblMachinePaidAmountInfo) {
		this.lblMachinePaidAmountInfo = lblMachinePaidAmountInfo;
	}*/

	/**
	 * @return the lblOriginalAmount
	 */
	public CbctlLabel getLblOriginalAmount() {
		return lblOriginalAmount;
	}

	/**
	 * @return the lblOriginalAmountInfo
	 */
	public CbctlLabel getLblOriginalAmountInfo() {
		return lblOriginalAmountInfo;
	}

	/**
	 * @return the lblSequenceInfo
	 */
	public CbctlLabel getLblSequenceInfo() {
		return lblSequenceInfo;
	}

	/**
	 * @param lblSequenceInfo the lblSequenceInfo to set
	 */
	public void setLblSequenceInfo(CbctlLabel lblSequenceInfo) {
		this.lblSequenceInfo = lblSequenceInfo;
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

	/**
	 * @return the lblSlotInfo
	 */
	public CbctlLabel getLblSlotInfo() {
		return lblSlotInfo;
	}

	/**
	 * @param lblSlotInfo the lblSlotInfo to set
	 */
	public void setLblSlotInfo(CbctlLabel lblSlotInfo) {
		this.lblSlotInfo = lblSlotInfo;
	}

	/**
	 * @return the lblSlotNumber
	 */
	public CbctlLabel getLblSlotNumber() {
		return lblSlotNumber;
	}

	/**
	 * @param lblSlotNumber the lblSlotNumber to set
	 */
	public void setLblSlotNumber(CbctlLabel lblSlotNumber) {
		this.lblSlotNumber = lblSlotNumber;
	}

	/**
	 * @return the lblStandInfo
	 */
	public CbctlLabel getLblStandInfo() {
		return lblStandInfo;
	}

	/**
	 * @return the lblStandNumber
	 */
	public CbctlLabel getLblStandNumber() {
		return lblStandNumber;
	}

	/**
	 * @return the lblTax
	 *//*
	public CbctlLabel getLblTax() {
		return lblTax;
	}


	*//**
	 * @return the lblTaxInfo
	 *//*
	public CbctlLabel getLblTaxInfo() {
		return lblTaxInfo;
	}*/

	/**
	 * @return the lblTimeStamp
	 */
	public CbctlLabel getLblTimeStamp() {
		return lblTimeStamp;
	}

	/**
	 * @return the lblTimeStampInfo
	 */
	public CbctlLabel getLblTimeStampInfo() {
		return lblTimeStampInfo;
	}

	/**
	 * @param lblShift the lblShift to set
	 */
	public void setLblShift(CbctlLabel lblShift) {
		this.lblShift = lblShift;
	}

	/**
	 * @param lblShiftInfo the lblShiftInfo to set
	 */
	public void setLblShiftInfo(CbctlLabel lblShiftInfo) {
		this.lblShiftInfo = lblShiftInfo;
	}

	/**
	 * @return the lblAssPlyCard
	 */
	public CbctlLabel getLblAssPlyCard() {
		return lblAssPlyCard;
	}

	/**
	 * @param lblAssPlyCard the lblAssPlyCard to set
	 */
	public void setLblAssPlyCard(CbctlLabel lblAssPlyCard) {
		this.lblAssPlyCard = lblAssPlyCard;
	}

	/**
	 * @return the lblAssPlyCardInfo
	 */
	public CbctlLabel getLblAssPlyCardInfo() {
		return lblAssPlyCardInfo;
	}

	/**
	 * @param lblAssPlyCardInfo the lblAssPlyCardInfo to set
	 */
	public void setLblAssPlyCardInfo(CbctlLabel lblAssPlyCardInfo) {
		this.lblAssPlyCardInfo = lblAssPlyCardInfo;
	}

	/**
	 * @param lblAdjustedAmount the lblAdjustedAmount to set
	 */
	public void setLblAdjustedAmount(CbctlLabel lblAdjustedAmount) {
		this.lblAdjustedAmountLbl = lblAdjustedAmount;
	}

	/**
	 * @param lblAdjustedAmountLbl the lblAdjustedAmountLbl to set
	 */
	public void setLblAdjustedAmountLbl(CbctlLabel lblAdjustedAmountLbl) {
		this.lblAdjustedAmount = lblAdjustedAmountLbl;
	}

	/**
	 * @param lblOriginalAmount the lblOriginalAmount to set
	 */
	public void setLblOriginalAmount(CbctlLabel lblOriginalAmount) {
		this.lblOriginalAmount = lblOriginalAmount;
	}

	/**
	 * @param lblOriginalAmountInfo the lblOriginalAmountInfo to set
	 */
	public void setLblOriginalAmountInfo(CbctlLabel lblOriginalAmountInfo) {
		this.lblOriginalAmountInfo = lblOriginalAmountInfo;
	}

	/**
	 * @param lblStandInfo the lblStandInfo to set
	 */
	public void setLblStandInfo(CbctlLabel lblStandInfo) {
		this.lblStandInfo = lblStandInfo;
	}

	/**
	 * @param lblStandNumber the lblStandNumber to set
	 */
	public void setLblStandNumber(CbctlLabel lblStandNumber) {
		this.lblStandNumber = lblStandNumber;
	}

	/**
	 * @param lblTimeStamp the lblTimeStamp to set
	 */
	public void setLblTimeStamp(CbctlLabel lblTimeStamp) {
		this.lblTimeStamp = lblTimeStamp;
	}

	/**
	 * @param lblTimeStampInfo the lblTimeStampInfo to set
	 */
	public void setLblTimeStampInfo(CbctlLabel lblTimeStampInfo) {
		this.lblTimeStampInfo = lblTimeStampInfo;
	}

	
	/**
	 * Gets the lable for showing account number
	 * @return
	 * @author vsubha
	 */
	public CbctlLabel getLblAccountNumber() {
		return lblAccountNumber;
	}
	

	/**
	 * Sets the lable for showing account number
	 * @param lblAccountNumber
	 * @author vsubha
	 */
	public void setLblAccountNumber(CbctlLabel lblAccountNumber) {
		this.lblAccountNumber = lblAccountNumber;
	}
	

	/**
	 * Gets the lable for showing account number Info
	 * @return
	 * @author vsubha
	 */
	public CbctlLabel getLblAccountNumberInfo() {
		return lblAccountNumberInfo;
	}
	

	/**
	 * Sets the lable for showing account number Info
	 * @param lblAccountNumberInfo
	 * @author vsubha
	 */
	public void setLblAccountNumberInfo(CbctlLabel lblAccountNumberInfo) {
		this.lblAccountNumberInfo = lblAccountNumberInfo;
	}

}  //  @jve:decl-index=0:visual-constraint="50,18"
