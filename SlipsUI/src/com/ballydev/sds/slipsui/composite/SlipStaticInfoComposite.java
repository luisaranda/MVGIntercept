/*****************************************************************************
 * $Id: SlipStaticInfoComposite.java,v 1.13, 2010-08-20 12:10:15Z, Ambereen Drewitt$
 * $Date: 8/20/2010 7:10:15 AM$
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
package com.ballydev.sds.slipsui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenMiddleComposite;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.util.StringUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.controller.MainMenuController;
import com.ballydev.sds.slipsui.util.ConversionUtil;

/**
 * Composite that will contain all the static slip details
 * @author dambereen
 * @version $Revision: 14$
 */
public class SlipStaticInfoComposite extends Composite {
	/**
	 * Group instance
	 */
	private Group infoGroup = null;
	/**
	 * Slot no label instance
	 */
	private CbctlLabel lblSlotNumber = null;
	/**
	 * Stand no label instance
	 */
	private CbctlLabel lblStandNumber = null;
	/**
	 *  Slip amount label instance
	 */
	private CbctlLabel lblSlipAmount = null;
	/**
	 *  Slot no info label instance
	 */
	private CbctlLabel lblSlotInfo = null;
	/**
	 *  Stand no info label instance
	 */
	private CbctlLabel lblStandInfo = null;
	/**
	 *  Shift info label instance
	 */
	private CbctlLabel lblShiftInfo = null;
	/**
	 *  Slip amount info label instance
	 */
	private CbctlLabel lblSlipAmountInfo = null;
	/**
	 * Shift label instance
	 */
	private CbctlLabel lblShift = null;
	/**
	 * Heading label instance
	 */
	private CbctlLabel lblHeading = null;
	
	/**
	 * Cardless Account number instance
	 */
	private CbctlLabel lblAccountNumber = null;
	/**
	 * Cardless Account number info text instance
	 */
	private CbctlLabel lblAccountNumberInfo = null;
	
	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;
	
	private TouchScreenMiddleComposite middleComposite;
	/**
	 * This composite's constructor
	 * @param parent
	 * @param style
	 */
	public SlipStaticInfoComposite(TouchScreenMiddleComposite middleComposite, int style) {
		super(middleComposite, style);
		this.middleComposite = middleComposite;
		if(Util.isSmallerResolution()){
			middleBodyWidth = ITSControlSizeConstants.S_MIDDLE_WIDTH;
			middleBodyHeight = ITSControlSizeConstants.S_MIDDLE_BODY_HEIGHT;
		}
		initialize();
		this.setLayoutData();
	}
	/**
	 * Method to create the group and its contents
	 */
	private void initialize() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.makeColumnsEqualWidth = false;
		gridLayout1.marginWidth = 0;
		this.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		this.setLayout(gridLayout1);
		createInfoGroup();
		//setSize(new Point(532, 443));
		//setSize(new Point(532, 460));
	}

	/**
	 * This method initializes infoGroup	
	 *
	 */
	private void createInfoGroup() {
		/*GridData gridData16 = new GridData();
		gridData16.horizontalAlignment = GridData.CENTER;
		gridData16.horizontalSpan = 2;
		gridData16.verticalAlignment = GridData.CENTER;
		gridData16.grabExcessHorizontalSpace = true;
		
		GridData gridData15 = new GridData();
		gridData15.grabExcessVerticalSpace = true;
		gridData15.heightHint = 20;
		gridData15.widthHint = 150;
		gridData15.grabExcessHorizontalSpace = true;
		GridData gridData13 = new GridData();
		gridData13.grabExcessVerticalSpace = true;
		gridData13.horizontalIndent = 25;
		gridData13.grabExcessHorizontalSpace = true;
		GridData gridData12 = new GridData();
		gridData12.grabExcessVerticalSpace = true;
		gridData12.horizontalIndent = 25;
		gridData12.grabExcessHorizontalSpace = true;
		GridData gridData111 = new GridData();
		gridData111.grabExcessVerticalSpace = true;
		gridData111.heightHint = 20;
		gridData111.widthHint = 150;
		gridData111.grabExcessHorizontalSpace = true;
		GridData gridData10 = new GridData();
		gridData10.horizontalIndent = 25;
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.grabExcessVerticalSpace = true;
		GridData gridData9 = new GridData();
		gridData9.heightHint = 20;
		gridData9.grabExcessVerticalSpace = true;
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.widthHint = 150;
		GridData gridData81 = new GridData();
		gridData81.heightHint = 20;
		gridData81.widthHint = 150;
		GridData gridData71 = new GridData();
		gridData71.heightHint = -1;
		gridData71.grabExcessVerticalSpace = true;
		gridData71.grabExcessHorizontalSpace = true;
		gridData71.widthHint = -1;
		GridData gridData61 = new GridData();
		gridData61.heightHint = -1;
		gridData61.widthHint = -1;
		gridData61.grabExcessHorizontalSpace = true;
		GridData gridData51 = new GridData();
		gridData51.heightHint = -1;
		gridData51.widthHint = -1;
		gridData51.grabExcessHorizontalSpace = true;
		GridData gridData41 = new GridData();
		gridData41.heightHint = -1;
		gridData41.widthHint = -1;
		gridData41.grabExcessHorizontalSpace = true;
		GridData gridData31 = new GridData();
		gridData31.horizontalIndent = 25;
		gridData31.grabExcessHorizontalSpace = true;
		gridData31.grabExcessVerticalSpace = true;
		GridData gridData11 = new GridData();
		gridData11.horizontalIndent = 25;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.grabExcessVerticalSpace = true;
		GridData gridData8 = new GridData();
		gridData8.grabExcessVerticalSpace = true;
		gridData8.heightHint = 25;
		gridData8.widthHint = 180;
		gridData8.grabExcessHorizontalSpace = true;
		GridData gridData7 = new GridData();
		gridData7.grabExcessVerticalSpace = true;
		gridData7.horizontalIndent = 25;
		gridData7.grabExcessHorizontalSpace = true;
		GridData gridData6 = new GridData();
		gridData6.grabExcessVerticalSpace = true;
		gridData6.heightHint = 25;
		gridData6.widthHint = 180;
		gridData6.grabExcessHorizontalSpace = true;
		GridData gridData5 = new GridData();
		gridData5.grabExcessVerticalSpace = true;
		gridData5.horizontalIndent = 25;
		gridData5.grabExcessHorizontalSpace = true;
		GridData gridData4 = new GridData();
		gridData4.grabExcessVerticalSpace = true;
		gridData4.heightHint = 25;
		gridData4.widthHint = 180;
		gridData4.grabExcessHorizontalSpace = true;
		GridData gridData3 = new GridData();
		gridData3.grabExcessVerticalSpace = true;
		gridData3.horizontalIndent = 25;
		gridData3.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.grabExcessVerticalSpace = true;
		gridData2.heightHint = 25;
		gridData2.widthHint = 180;
		gridData2.grabExcessHorizontalSpace = true;
		GridData gridData1 = new GridData();
		gridData1.grabExcessVerticalSpace = true;
		gridData1.horizontalIndent = 25;
		gridData1.grabExcessHorizontalSpace = true;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.verticalSpacing = 10;
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 10;
		gridLayout.horizontalSpacing = 10;
		
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.widthHint = 310;
		gridData.grabExcessHorizontalSpace = false;
		
		*//**
		 * Resizing the composite for 800*600 resolution
		 * 
		 *//* 
		
		if(Util.isSmallerResolution()) {
			
			gridLayout.makeColumnsEqualWidth = false;
			gridLayout.marginWidth = 0;
			gridLayout.horizontalSpacing = 0;
			
			gridData1.horizontalAlignment = GridData.BEGINNING;
			gridData1.horizontalIndent = 2;
			gridData1.grabExcessHorizontalSpace= true;
			
			gridData41.horizontalAlignment = GridData.BEGINNING;
			gridData41.grabExcessHorizontalSpace = true;
			//gridData41.horizontalIndent = -5;
			
			gridData3.horizontalAlignment = GridData.BEGINNING;
			gridData3.horizontalIndent = 5;
			
			gridData51.horizontalAlignment = GridData.BEGINNING;
			//gridData51.horizontalIndent = -5;	
			
			gridData7.horizontalAlignment = GridData.BEGINNING;
			gridData7.horizontalIndent = 5;
						
			gridData71.horizontalAlignment = GridData.BEGINNING;
			//gridData71.horizontalIndent = -5;
			
			gridData12.horizontalAlignment = GridData.BEGINNING;
			gridData12.horizontalIndent = 5;
			
			gridData61.horizontalAlignment = GridData.BEGINNING;
			//gridData61.horizontalIndent = -5;
			
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalAlignment = GridData.BEGINNING;
			//gridData.horizontalIndent = -50;
			gridData.verticalAlignment = GridData.BEGINNING;
			gridData.widthHint = 200;
			gridData.heightHint = 200;
		}
		
		infoGroup = new Group(this, SWT.WRAP);
		infoGroup.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		infoGroup.setLayoutData(gridData);
		infoGroup.setLayout(gridLayout);
		
		lblHeading = new CbctlLabel(infoGroup, SWT.WRAP, true);
		lblHeading.setText(LabelLoader.getLabelValue(LabelKeyConstants.STATIC_INFO_SLIP_DETAILS));
		lblHeading.setEnabled(false);
		lblHeading.setLayoutData(gridData16);
		
		lblSlotNumber = new CbctlLabel(infoGroup, SWT.WRAP);
		lblSlotNumber.setText(LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
		lblSlotNumber.setEnabled(false);
		lblSlotNumber.setLayoutData(gridData1);
		
		lblSlotInfo = new CbctlLabel(infoGroup, SWT.WRAP);
		lblSlotInfo.setText(StringUtil.trimAcnfNo(MainMenuController.slipForm.getSlotNo()));
//		lblSlotInfo.setBackground(new Color(Display.getCurrent(), 246, 246, 246));
		lblSlotInfo.setLayoutData(gridData41);
		lblSlotInfo.setEnabled(false);
		
		lblStandNumber = new CbctlLabel(infoGroup, SWT.WRAP);
		lblStandNumber.setText(LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER));
		lblStandNumber.setEnabled(false);
		lblStandNumber.setLayoutData(gridData3);
		
		lblStandInfo = new CbctlLabel(infoGroup, SWT.WRAP);
		lblStandInfo.setText(MainMenuController.slipForm.getSlotLocationNo());
		lblStandInfo.setText(MainMenuController.slipForm.getSlotLocationNo());
		lblStandInfo.setEnabled(false);
//		lblStandInfo.setBackground(new Color(Display.getCurrent(), 246, 246, 246));
		lblStandInfo.setLayoutData(gridData51);
		
		lblSlipAmount = new CbctlLabel(infoGroup, SWT.WRAP);
		lblSlipAmountInfo = new CbctlLabel(infoGroup, SWT.WRAP );
		lblSlipAmount.setText(LabelLoader.getLabelValue(LabelKeyConstants.SLIP_AMOUNT));
		lblSlipAmount.setEnabled(false);
		lblSlipAmount.setLayoutData(gridData7);
		
		lblShift = new CbctlLabel(infoGroup, SWT.WRAP);
		lblShift.setText(LabelLoader.getLabelValue(LabelKeyConstants.SHIFT_GROUP_HEADING_LABEL));
		lblShift.setEnabled(false);
		lblShift.setLayoutData(gridData12);
		
		lblShiftInfo = new CbctlLabel(infoGroup, SWT.WRAP );		
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)){
			
			if(MainMenuController.slipForm.getShift().equalsIgnoreCase("day")) {
				lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));		
			}else if(MainMenuController.slipForm.getShift().equalsIgnoreCase("swing")) {
				lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
			}else {
				lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
			}				
		}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY)){
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_SWING)){
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
		}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)){
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
		}
		lblShiftInfo.setEnabled(false);
		lblShiftInfo.setLayoutData(gridData61);
		
		lblSlipAmountInfo.setText(MainMenuController.slipForm.getSiteCurrencySymbol() + ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(MainMenuController.slipForm.getSlipAmount())));
		lblSlipAmountInfo.setEnabled(false);
		lblSlipAmountInfo.setLayoutData(gridData71);*/
		
		/*GridData gdLeftLabel = new GridData();
		gdLeftLabel.grabExcessVerticalSpace = true;
		gdLeftLabel.verticalAlignment = GridData.BEGINNING;
		gdLeftLabel.horizontalIndent = 10;
		gdLeftLabel.widthHint = 140;
		gdLeftLabel.grabExcessHorizontalSpace = true;
				
		GridData gdRightLabel = new GridData();
		gdRightLabel.grabExcessVerticalSpace = true;
		gdRightLabel.heightHint = 25;
		gdRightLabel.widthHint = 140;
		gdRightLabel.verticalAlignment = GridData.BEGINNING;
		gdRightLabel.grabExcessHorizontalSpace = true;*/
		
/*		lblHeading = new CbctlLabel(this, SWT.NONE, true);
		lblHeading.setText(LabelLoader.getLabelValue(LabelKeyConstants.STATIC_INFO_SLIP_DETAILS));
		lblHeading.setEnabled(false);
		lblHeading.setLayoutData(gdLeftLabel);*/
		
		lblSlotNumber = new CbctlLabel(this, SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
		//lblSlotNumber.setEnabled(false);
		lblSlotNumber.setLayoutData(middleComposite.getGDForLargeBodyLabel());
		
		String slotNumber = "";
		
		if (!Util.isEmpty(MainMenuController.slipForm.getSlotNo())) {
			slotNumber = StringUtil.trimAcnfNo(MainMenuController.slipForm.getSlotNo().trim());
		}

		String standNumber = "";
		if (!Util.isEmpty(MainMenuController.slipForm.getSlotLocationNo())) {
			standNumber = MainMenuController.slipForm.getSlotLocationNo().trim();
		}
		
		String seqNumber = "";
		if (MainMenuController.slipForm.getSequenceNumber() != 0) {
			seqNumber = ((Long)MainMenuController.slipForm.getSequenceNumber()).toString();
		}
		
		lblSlotInfo = new CbctlLabel(this, SWT.WRAP);
		lblSlotInfo.setText(slotNumber);
		lblSlotInfo.setLayoutData(middleComposite.getGDForBodyLabel());
		//lblSlotInfo.setEnabled(false);
		
		lblStandNumber = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER));
		//lblStandNumber.setEnabled(false);
		lblStandNumber.setLayoutData(middleComposite.getGDForLargeBodyLabel());
		
		lblStandInfo = new CbctlLabel(this, SWT.WRAP);
		lblStandInfo.setText(standNumber);
		//lblStandInfo.setEnabled(false);
		lblStandInfo.setLayoutData(middleComposite.getGDForBodyLabel());
		
		lblSlipAmount = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.SLIP_AMOUNT));
		lblSlipAmountInfo = new CbctlLabel(this, SWT.WRAP );
		//lblSlipAmount.setEnabled(false);
		lblSlipAmount.setLayoutData(middleComposite.getGDForLargeBodyLabel());
		
		lblShift = new CbctlLabel(this, SWT.WRAP, LabelLoader.getLabelValue(LabelKeyConstants.SHIFT_GROUP_HEADING_LABEL));
		//lblShift.setEnabled(false);
		lblShift.setLayoutData(middleComposite.getGDForLargeBodyLabel());
		
		lblShiftInfo = new CbctlLabel(this, SWT.WRAP );		
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_PROMPT_FOR_ALL)){
			lblShiftInfo.setText(MainMenuController.slipForm.getShift());
		}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_DAY)){
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.DAY_SHIFT_LABEL));
		}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_SWING)){
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.SWING_SHIFT_LABEL));
		}else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.PROMPT_FOR_SINGLE_SHIFT_OR_DEFAULT).equalsIgnoreCase(IAppConstants.SITE_VALUE_SHIFT_GRAVEYARD)){
			lblShiftInfo.setText(LabelLoader.getLabelValue(LabelKeyConstants.GRAVEYARD_SHIFT_LABEL));
		}
		//lblShiftInfo.setEnabled(false);
		lblShiftInfo.setLayoutData(middleComposite.getGDForBodyLabel());
		
		lblSlipAmountInfo.setText(MainMenuController.slipForm.getSiteCurrencySymbol() +  ConversionUtil.getCurrencyFormat(ConversionUtil.centsToDollar(MainMenuController.slipForm.getSlipAmount())));
		//lblSlipAmountInfo.setEnabled(false);
		lblSlipAmountInfo.setLayoutData(middleComposite.getGDForBodyLabel());
		
		if(MainMenuController.slipForm.getCashlessAccountNumber()!=null) {
			lblAccountNumber = new CbctlLabel(this, SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.ACCOUNT_NO));			
			lblAccountNumber.setLayoutData(middleComposite.getGDForBodyLabel());		
			
			lblAccountNumberInfo = new CbctlLabel(this, SWT.WRAP);
			lblAccountNumberInfo.setText(MainMenuController.slipForm.getCashlessAccountNumber());
			lblAccountNumberInfo.setLayoutData(middleComposite.getGDForBodyLabel());			
		}
		
	}
	
	/*private void setLayoutData() {
		GridData gridDataforComposite = new GridData();
		gridDataforComposite.grabExcessVerticalSpace = true;
		gridDataforComposite.grabExcessHorizontalSpace = true;
		gridDataforComposite.horizontalAlignment = GridData.FILL;
		gridDataforComposite.verticalAlignment = GridData.FILL;
		this.setLayoutData(gridDataforComposite);
	}*/
	
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
		this.setBackground(new Color(Display.getCurrent(), 112, 151, 188));
	}
	
	/**
	 * @return the infoGroup
	 */
	public Group getInfoGroup() {
		return infoGroup;
	}
	/**
	 * @param infoGroup the infoGroup to set
	 */
	public void setInfoGroup(Group infoGroup) {
		this.infoGroup = infoGroup;
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
	 * @return the lblShift
	 */
	public CbctlLabel getLblShift() {
		return lblShift;
	}
	/**
	 * @param lblShift the lblShift to set
	 */
	public void setLblShift(CbctlLabel lblShift) {
		this.lblShift = lblShift;
	}
	/**
	 * @return the lblShiftInfo
	 */
	public CbctlLabel getLblShiftInfo() {
		return lblShiftInfo;
	}
	/**
	 * @param lblShiftInfo the lblShiftInfo to set
	 */
	public void setLblShiftInfo(CbctlLabel lblShiftInfo) {
		this.lblShiftInfo = lblShiftInfo;
	}
	/**
	 * @return the lblSlipAmount
	 */
	public CbctlLabel getLblSlipAmount() {
		return lblSlipAmount;
	}
	/**
	 * @param lblSlipAmount the lblSlipAmount to set
	 */
	public void setLblSlipAmount(CbctlLabel lblSlipAmount) {
		this.lblSlipAmount = lblSlipAmount;
	}
	/**
	 * @return the lblSlipAmountInfo
	 */
	public CbctlLabel getLblSlipAmountInfo() {
		return lblSlipAmountInfo;
	}
	/**
	 * @param lblSlipAmountInfo the lblSlipAmountInfo to set
	 */
	public void setLblSlipAmountInfo(CbctlLabel lblSlipAmountInfo) {
		this.lblSlipAmountInfo = lblSlipAmountInfo;
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
	 * @param lblStandInfo the lblStandInfo to set
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
	 * @param lblStandNumber the lblStandNumber to set
	 */
	public void setLblStandNumber(CbctlLabel lblStandNumber) {
		this.lblStandNumber = lblStandNumber;
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
	
}  //  @jve:decl-index=0:visual-constraint="141,9"
