/*****************************************************************************
 * $Id: AmountSlotAttendantIdComposite.java,v 1.23.1.1, 2011-05-17 20:01:53Z, SDS12.3.2CheckinUser$
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

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.cdatepicker.ACW;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.composite.TouchScreenMiddleHeaderComposite;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSDatePicker;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;


/**
 * This Class Processes the Hand Paid Amount entered
 * 
 * @author anantharajr
 * @version $Revision: 26$
 */
public class AmountSlotAttendantIdComposite extends TouchScreenBaseComposite {

	/**
	 * HandPaidAmountChangeGroup instance.
	 */
	private Composite handPaidAmountChangeComposite = null;

	/**
	 * Cancel button instance.02
	 */
	private SDSImageLabel btnCancel = null;

	/**
	 * Next button instance.
	 */
	private SDSImageLabel btnNext = null;

	/**
	 * Previous Button instance.
	 */
	private SDSImageLabel btnPrevious = null;

	/**
	 * HPJP amount label instance
	 */
	private CbctlMandatoryLabel lblHandPaidAmount = null;

	/**
	 * HPJP amount text instance
	 */
	private SDSTSText txtHandPaidAmount = null;

	/**
	 * SlipStaticInfoComposite instance
	 */
	private SlipStaticInfoComposite slipStaticInfoComposite = null;

	/**
	 * SlotAttendantId label instance
	 */
	private CbctlMandatoryLabel lblSlotAttendantId = null;

	/**
	 * SlotAttendantId text instance
	 */
	private SDSTSText txtSlotAttendantId = null;
	
	/**
	 * Overide auth employee Id label instance
	 */
	private CbctlLabel lblAmountAuthEmpId = null;
	
	/**
	 * Overide auth emp Id text instance
	 */
	
	private SDSTSText txtAmountAuthEmpId = null;
	
	/**
	 * Overide auth emp password label instance
	 */
	private CbctlLabel lblAmountAuthPassword = null;
	
	/**
	 * Overide auth emp password text instance
	 */
	private SDSTSText txtAmountAuthPassword = null;

	/**
	 * siteParamterFlag boolean array that holds all the site parameters that are passed from the controller 
	 */
	private boolean[] siteParamterFlag = {false, false, false};

	/**
	 * SDS Calculated amount label.
	 */
	private CbctlLabel lblCalcSDSAmount = null;

	/**
	 * SDS Calculated amount text.
	 */
	private SDSTSText txtCalcSDSAmount = null;
	
	private int middleBodyWidth = ITSControlSizeConstants.MIDDLE_WIDTH;
	
	private int middleBodyHeight = ITSControlSizeConstants.MIDDLE_BODY_HEIGHT;
	/**
	 * Jackpot Check Expiry date label instance
	 */
	private CbctlMandatoryLabel lblExpiryDate = null;

	/**
	 * Jackpot Check Expiry DatePicker instance
	 */
	private SDSDatePicker dtPickerExpiryDate = null;
	
	/**
	 * Cardless Account Number Label
	 */
	private CbctlLabel lblAccountNumber = null;
	
	/**
	 * Cardless Account Number Text
	 */
	private SDSTSText txtAccountNumber = null;
	
	/**
	 * Confirm Cardless Account Number Label
	 */
	private CbctlLabel lblConfirmAccountNumber = null;
	/**
	 * Confirm Cardless Account Number Text
	 */
	private SDSTSText txtConfirmAccountNumber = null;


	/**
	 * @param parent
	 * @param style
	 */
	public AmountSlotAttendantIdComposite(Composite parent, int style,
			boolean[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.PENDING_JP_HEADING));
		this.siteParamterFlag = flags;
		if(Util.isSmallerResolution()){
			middleBodyWidth = ITSControlSizeConstants.S_MIDDLE_WIDTH;
			middleBodyHeight = ITSControlSizeConstants.S_MIDDLE_BODY_HEIGHT;
		}
		initialize("P,N,C");
		
		layout();
	}

	/**
	 * This method initializes handPaidAmountChangeGroup
	 * 
	 */
	public void drawMiddleComposite() {		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_ENTER_DETAILS));		
		new TouchScreenMiddleHeaderComposite(getMiddleComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_JACKPOT_DETAILS));		
		createHandPaidAmountComposite(getMiddleComposite());		
		slipStaticInfoComposite = new SlipStaticInfoComposite(getMiddleComposite(), SWT.NONE);		
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.composite.TouchScreenBaseComposite#drawTopComposite()
	 */
	public void drawTopComposite() {
		if(siteParamterFlag[2]) {
			int rowCount = 1;
			rowCount += 2;
			lblExpiryDate = new CbctlMandatoryLabel(getTopComposite(), 
					SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_CHECK_EXPIRY_DATE_TIME));
			lblExpiryDate.setLayoutData(getTopComposite().getGDForLabel());
			createExpiryDateTime();
			getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
			getTopComposite().setLayoutForComposite();
		}		
	}
	
	private void createHandPaidAmountComposite(Composite middleComposite) {
		
		GridData gdHPComp = new GridData();
		gdHPComp.grabExcessVerticalSpace = false;
		gdHPComp.verticalAlignment = GridData.FILL;
		gdHPComp.horizontalSpan = 2;
		gdHPComp.widthHint = middleBodyWidth;
		gdHPComp.heightHint = middleBodyHeight;
		gdHPComp.horizontalAlignment = GridData.BEGINNING;
			
		GridLayout glHPComp = new GridLayout();
		glHPComp.numColumns = 2;
		glHPComp.verticalSpacing = 10;
		glHPComp.marginWidth = 5;
		glHPComp.marginHeight = 5;
		glHPComp.horizontalSpacing = 5;
		
		handPaidAmountChangeComposite = new Composite(middleComposite, SWT.NONE); 
		handPaidAmountChangeComposite.setLayoutData(gdHPComp);
		handPaidAmountChangeComposite.setLayout(glHPComp);
		handPaidAmountChangeComposite.setBackground(SDSControlFactory.getTSMiddleBodyColor());
		
		lblHandPaidAmount = new CbctlMandatoryLabel(handPaidAmountChangeComposite,	
				SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_AMOUNT_LABEL));
		lblHandPaidAmount.setLayoutData(getMiddleComposite().getGDForBodyLabel());
		txtHandPaidAmount = new SDSTSText(handPaidAmountChangeComposite,
				SWT.BORDER, "", FormConstants.FORM_HAND_PAID_AMOUNT,true);
		
		txtHandPaidAmount.setAmountField(true);
		txtHandPaidAmount.setNumeric(true);
		
		txtHandPaidAmount.setTextLimit(IFieldTextLimits.HPJP_AMOUNT_TEXT_LIMIT);
		txtHandPaidAmount.setLayoutData(getMiddleComposite().getGDForBodyText());
		
		if(MainMenuController.jackpotForm!=null &&  MainMenuController.jackpotForm.getSdsCalculatedAmount()!=null &&
		MainMenuController.jackpotForm.getSdsCalculatedAmount().longValue()!=0  && !(MainMenuController.jackpotForm.isProcessAsExpress())){
			lblCalcSDSAmount = new CbctlLabel(handPaidAmountChangeComposite,	
					SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.SDS_CALC_AMOUNT_LABEL));
			lblCalcSDSAmount.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			txtCalcSDSAmount = new SDSTSText(handPaidAmountChangeComposite,
					SWT.BORDER, "", FormConstants.FORM_SDS_CALCULATED_AMT);
			txtCalcSDSAmount.setAmountField(true);
			txtCalcSDSAmount.setLayoutData(getMiddleComposite().getGDForBodyText());
		}		
		if (siteParamterFlag[1]) {
			lblSlotAttendantId = new CbctlMandatoryLabel(handPaidAmountChangeComposite,
					SWT.WRAP,LabelLoader.getLabelValue(LabelKeyConstants.SLOT_ATTENDANT_ID));
			lblSlotAttendantId.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			
			txtSlotAttendantId = new SDSTSText(handPaidAmountChangeComposite,
					SWT.NONE, "", FormConstants.FORM_SLOT_ATTN_ID);
			txtSlotAttendantId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtSlotAttendantId.setLayoutData(getMiddleComposite().getGDForBodyText());	
		}	
		
		if (MainMenuController.jackpotSiteConfigParams.get(
				ISiteConfigConstants.JACKPOT_AMOUNT_OVERIDE_AUTH_ENABLED).equalsIgnoreCase("yes")) {
			// JACKPOT AMOUNT LIMIT OVERRIDE AUTH EMPLOYEE ID
			lblAmountAuthEmpId = new CbctlLabel(handPaidAmountChangeComposite, SWT.WRAP, 
					LabelLoader.getLabelValue(LabelKeyConstants.AMOUNT_AUTHORIZATION_EMPLOYEE_ID));
			lblAmountAuthEmpId.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			
			txtAmountAuthEmpId = new SDSTSText(handPaidAmountChangeComposite, SWT.BORDER, "", FormConstants.FORM_AMOUNT_AUTH_EMP_ID);
			txtAmountAuthEmpId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtAmountAuthEmpId.setLayoutData(getMiddleComposite().getGDForBodyText());
			
			// JACKPOT AMOUNT OVERRIDE AUTH PASSWORD
			lblAmountAuthPassword = new CbctlLabel(handPaidAmountChangeComposite, SWT.NONE, 
					LabelLoader.getLabelValue(LabelKeyConstants.AMOUNT_AUTHORIZATION_PASSWORD));
			lblAmountAuthPassword.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			
			txtAmountAuthPassword = new SDSTSText(handPaidAmountChangeComposite, SWT.PASSWORD, "", FormConstants.FORM_AMOUNT_AUTH_PASSWORD);
			txtAmountAuthPassword.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtAmountAuthPassword.setLayoutData(getMiddleComposite().getGDForBodyText());
		}
		if (siteParamterFlag[3]) {

			// Cardless Account Number		
			lblAccountNumber =  new CbctlLabel(handPaidAmountChangeComposite, 
					SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.LBL_ACCOUNT_NUMBER));
			lblAccountNumber.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			
			txtAccountNumber = new SDSTSText(handPaidAmountChangeComposite,
					SWT.BORDER , "", FormConstants.FORM_ACCOUNT_NUMBER, true) {
				@Override
				public void cut() {} // DISABLED CTRL + X
				@Override
				public void copy() {} // DISABLED CTRL + C
				@Override
				public void paste() {} // DISABLE CTRL + V
			};
			txtAccountNumber.setTextLimit(IFieldTextLimits.ACCOUNT_NUMBER_LIMIT);
			txtAccountNumber.setLayoutData(getMiddleComposite().getGDForBodyText());			
			
			// Confirm Cardless Account Number
			lblConfirmAccountNumber =  new CbctlLabel(handPaidAmountChangeComposite, 
					SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.LBL_CONFIRM_ACCOUNT_NUMBER));
			lblConfirmAccountNumber.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			
			txtConfirmAccountNumber = new SDSTSText(handPaidAmountChangeComposite,
					SWT.BORDER , "", FormConstants.FORM_CONFIRM_ACCOUNT_NUMBER, true) {				
					@Override
					public void cut() {} // DISABLED CTRL + X
					@Override
					public void copy() {} // DISABLED CTRL + C
					@Override
					public void paste() {} // DISABLE CTRL + V				
			};
			/********************************************************************************************************* 
			// DISABLING RIGHT CLICK
//			txtConfirmCardlessAccountNumber.addMouseListener(new MouseListener() {
//				public void mouseDoubleClick(MouseEvent e) {
//					// TODO Auto-generated method stub
//					
//				}
//				public void mouseDown(MouseEvent e) {
//					if(e.button == 3) {
//						try {
//							System.out.println("right mouse button pressed - Subha suganthi immac");
//							txtConfirmCardlessAccountNumber.setToolTipText("Right click disabled");										
//							Menu menu = new Menu (txtConfirmCardlessAccountNumber.getShell(), SWT.PUSH);
//							MenuItem rightClickMenu = new MenuItem (menu, SWT.PUSH);
//							rightClickMenu.setText ("Right click disabled");	
//					        menu.setEnabled(false);
//					        menu.setVisible (true);					        
//							return;	
//						} catch(Exception e1 ){
//							e1.printStackTrace();
//						}
//					}
//				}				
//				public void mouseUp(MouseEvent e) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
 * **********************************************************************************************************/
			txtConfirmAccountNumber.setTextLimit(IFieldTextLimits.ACCOUNT_NUMBER_LIMIT);
			txtConfirmAccountNumber.setLayoutData(getMiddleComposite().getGDForBodyText());
		
		}
		
		
	}
	
	/**
	 * This method initializes expiryDateTime	
	 *
	 */
	private void createExpiryDateTime() {
		GridData gridData14 = new GridData();
		gridData14.heightHint = 25;
		gridData14.horizontalAlignment = GridData.CENTER;
		gridData14.verticalAlignment = GridData.CENTER;
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.widthHint = 210;
		// Get a Calendar for current locale and time zone
		Calendar cal = Calendar.getInstance();  
		// Get a Date object that represents 30 days from now
		Date today = new Date();                   // Current date
		cal.setTime(today);                        // Set it in the Calendar object
		cal.add(Calendar.DATE, 30);                // Add 30 days
		Date defaultExpiryDate = cal.getTime();    // Retrieve the resulting date
		
		dtPickerExpiryDate = new SDSDatePicker(getTopComposite(), ACW.FOOTER | ACW.DROP_DOWN | ACW.BORDER
				| ACW.DATE_CUSTOM | ACW.DIGITAL_CLOCK | ACW.TIME_CUSTOM, defaultExpiryDate, "expiryDate",
				"expiryDate");
		if (Util.isSmallerResolution()) {
			dtPickerExpiryDate.setLayoutData(gridData14);
		} else {
			dtPickerExpiryDate.setLayoutData(getTopComposite().getGDForText());
		}
	}	
	
	/**
	 * @return the btnNext
	 */
	public SDSImageLabel getBtnNext() {
		return btnNext;
	}

	/**
	 * @return the slipStaticInfoComposite
	 */
	public SlipStaticInfoComposite getSlipStaticInfoComposite() {
		return slipStaticInfoComposite;
	}

	/**
	 * @param slipStaticInfoComposite
	 *            the slipStaticInfoComposite to set
	 */
	public void setSlipStaticInfoComposite(
			SlipStaticInfoComposite slipStaticInfoComposite) {
		this.slipStaticInfoComposite = slipStaticInfoComposite;
	}

	/**
	 * @return the txtHandPaidAmount
	 */
	public SDSTSText getTxtHandPaidAmount() {
		return txtHandPaidAmount;
	}

	/**
	 * @param txtHandPaidAmount
	 *            the txtHandPaidAmount to set
	 */
	public void setTxtHandPaidAmount(SDSTSText txtHandPaidAmount) {
		this.txtHandPaidAmount = txtHandPaidAmount;
	}

	/**
	 * @return the txtSlotAttendantId
	 */
	public SDSTSText getTxtSlotAttendantId() {
		return txtSlotAttendantId;
	}

	/**
	 * @param txtSlotAttendantId
	 *            the txtSlotAttendantId to set
	 */
	public void setTxtSlotAttendantId(SDSTSText txtSlotAttendantId) {
		this.txtSlotAttendantId = txtSlotAttendantId;
	}

	/**
	 * @return the lblHandPaidAmount
	 */
	public CbctlMandatoryLabel getLblHandPaidAmount() {
		return lblHandPaidAmount;
	}

	/**
	 * @param lblHandPaidAmount the lblHandPaidAmount to set
	 */
	public void setLblHandPaidAmount(CbctlMandatoryLabel lblHandPaidAmount) {
		this.lblHandPaidAmount = lblHandPaidAmount;
	}

	/**
	 * @return the lblSlotAttendantId
	 */
	public CbctlMandatoryLabel getLblSlotAttendantId() {
		return lblSlotAttendantId;
	}

	/**
	 * @param lblSlotAttendantId the lblSlotAttendantId to set
	 */
	public void setLblSlotAttendantId(CbctlMandatoryLabel lblSlotAttendantId) {
		this.lblSlotAttendantId = lblSlotAttendantId;
	}

	public SDSTSText getTxtCalcSDSAmount() {
		return txtCalcSDSAmount;
	}

	public SDSImageLabel getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(SDSImageLabel btnCancel) {
		this.btnCancel = btnCancel;
	}

	public SDSImageLabel getBtnPrevious() {
		return btnPrevious;
	}

	public void setBtnPrevious(SDSImageLabel btnPrevious) {
		this.btnPrevious = btnPrevious;
	}

	public void setBtnNext(SDSImageLabel btnNext) {
		this.btnNext = btnNext;
	}
	
	public CbctlLabel getLblAmountAuthEmpId() {
		return lblAmountAuthEmpId;
	}

	public void setLblAmountAuthEmpId(CbctlLabel lblAmountAuthEmpId) {
		this.lblAmountAuthEmpId = lblAmountAuthEmpId;
	}

	public SDSTSText getTxtAmountAuthEmpId() {
		return txtAmountAuthEmpId;
	}

	public void setTxtAmountAuthEmpId(SDSTSText txtAmountAuthEmpId) {
		this.txtAmountAuthEmpId = txtAmountAuthEmpId;
	}

	public CbctlLabel getLblAmountAuthPassword() {
		return lblAmountAuthPassword;
	}

	public void setLblAmountAuthPassword(CbctlLabel lblAmountAuthPassword) {
		this.lblAmountAuthPassword = lblAmountAuthPassword;
	}

	public SDSTSText getTxtAmountAuthPassword() {
		return txtAmountAuthPassword;
	}

	public void setTxtAmountAuthPassword(SDSTSText txtAmountAuthPassword) {
		this.txtAmountAuthPassword = txtAmountAuthPassword;
	}
	/**
	 * Gets the jackpot check expiry date
	 * @return
	 * @author vsubha
	 */
	public SDSDatePicker getDtPickerExpiryDate() {
		return dtPickerExpiryDate;
	}

	/**
	 * Sets the jackpot check expiry date
	 * @param expiryDate
	 * @author vsubha
	 */
	public void setDtPickerExpiryDate(SDSDatePicker expiryDate) {
		this.dtPickerExpiryDate = expiryDate;
	}

	/**
	 * @return the txtAccountNumber
	 */
	public SDSTSText getTxtAccountNumber() {
		return txtAccountNumber;
	}

	/**
	 * @param txtAccountNumber the txtAccountNumber to set
	 */
	public void setTxtAccountNumber(SDSTSText txtAccountNumber) {
		this.txtAccountNumber = txtAccountNumber;
	}

	/**
	 * @return the txtConfirmAccountNumber
	 */
	public SDSTSText getTxtConfirmAccountNumber() {
		return txtConfirmAccountNumber;
	}

	/**
	 * @param txtConfirmAccountNumber the txtConfirmAccountNumber to set
	 */
	public void setTxtConfirmAccountNumber(SDSTSText txtConfirmAccountNumber) {
		this.txtConfirmAccountNumber = txtConfirmAccountNumber;
	}

}  //  @jve:decl-index=0:visual-constraint="121,-1"
