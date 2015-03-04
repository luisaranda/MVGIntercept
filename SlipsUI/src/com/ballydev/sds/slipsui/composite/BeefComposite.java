/*****************************************************************************
 * $Id: BeefComposite.java,v 1.25, 2011-03-01 14:47:47Z, Ambereen Drewitt$
 * $Date: 3/1/2011 8:47:47 AM$
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
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBaseComposite;
import com.ballydev.sds.framework.constant.ICommonLabels;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.CbctlMandatoryLabel;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TouchScreenShiftControl;
import com.ballydev.sds.slipsui.constants.FormConstants;
import com.ballydev.sds.slipsui.constants.IFieldTextLimits;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;

/**
 * This composite gets the details of all the beef inputs.
 * 
 * @author anantharajr
 * 
 */
public class BeefComposite extends TouchScreenBaseComposite {

	/**
	 * mainGroup Instance
	 */
	private Group mainGroup = null;

	/**
	 * EmployeeId Label Instance
	 */
	private CbctlMandatoryLabel lblEmployeeId = null;

	/**
	 * EmployeeId Text Instance
	 */
	private SDSTSText txtEmployeeId = null;

	/**
	 * EmpPassword Label Instance
	 */
	private CbctlMandatoryLabel lblEmpPassword = null;

	/**
	 * EmpPassword Text Instance
	 */
	private SDSTSText txtEmpPassword = null;

	/**
	 * Slot Seq or Slot Location No Label Instance
	 */
	public CbctlMandatoryLabel lblSlotSeqLocationNo = null;

	/**
	 * Slot Seq or Slot Location No Text Instance
	 */
	private SDSTSText txtSlotSeqLocationNo = null;

	/**
	 * Boolean array containing the values of the site configuration parameters,
	 * which will determine whether a particular control of this page needs to
	 * be shown.
	 */
	private String[] flags;

	/**
	 * Window Number lable instance
	 */
	private CbctlLabel lblWindowNo = null;

	/**
	 * Window Number Text instance.
	 */
	private SDSTSText txtWindowNo = null;
	
	/**
	 * Day label instance
	 */
	private CbctlLabel lblDay = null;

	/**
	 *  Swing label instance
	 */
	private CbctlLabel lblSwing = null;

	/**
	 *  Graveyard label instance
	 */
	private CbctlLabel lblGraveyard = null;

	/**
	 * Amount label instance
	 */
	private CbctlMandatoryLabel lblAmount = null;

	/**
	 *  Amount text instance
	 */
	private SDSTSText txtAmount = null;
	
	/**
	 * Cashless Account No Label instance
	 */
	private CbctlMandatoryLabel lblCashlessAccountNo = null;

	/**
	 *  Cashless Account No text instance
	 */
	private SDSTSText txtCashlessAccountNo = null;
	
	/**
	 * Cashless Account No Confirm Label instance
	 */
	private CbctlMandatoryLabel lblCashlessAccountNoConfirm = null;

	/**
	 *  Cashless Account No Confirm text instance
	 */
	private SDSTSText txtCashlessAccountNoConfirm = null;
	
	/**
	 * Slip Reason label instance
	 */
	private CbctlLabel lblReason = null;

	/**
	 * Slip Reason Text instance.
	 */
	private SDSTSText txtReason = null;
	
	/**
	 * TouchScreenShiftControl shift control instance
	 */
	private TouchScreenShiftControl shiftControl = null;
	
/*	*//**
	 * TouchScreenAmountTypeControl amountType control instance
	 *//*
	private TouchScreenAmountTypeControl amountTypeControl = null;*/

	/**
	 * @param parent
	 * @param style
	 * @param flags
	 */
	public BeefComposite(Composite parent, int style, String[] flags) {
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_SLIPS_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.BEEF_HEADING));
		this.flags = flags;
		initialize("N");
		//initialize("N", true);
		layout();
	}

	/** 
	 * Method to draw the Bottom Composite for the Shift controls 
	 */
	public void drawBottomComposite() {					
		if (flags[4].equalsIgnoreCase("P")) {
			//getBottomComposite().setWidthHint(800);
			getBottomComposite().setHeightHint(100);
			getBottomComposite().setLayoutForComposite();
			this.shiftControl = new TouchScreenShiftControl()
					.drawBottomControls(getBottomComposite(), SWT.NONE);			
		}
	}
	
	
	/** 
	 * Method to draw the Bottom Composite for the Shift controls 
	 *//*
	public void drawAmountTypeComposite() {		
		if(flags[5].equalsIgnoreCase("YES")) {
			getAmountTypeComposite().setHeightHint(75);
			getAmountTypeComposite().setLayoutForComposite();
			this.amountTypeControl = new TouchScreenAmountTypeControl()
					.drawAmountTypeControls(getAmountTypeComposite(), SWT.NONE);
		}
	}*/
	
	/** 
	 * Method to draw the Top Composite for the Text boxes
	 */
	public void drawTopComposite() {	
		/**
		 * Increment and find the count based on siteconfig - Total count
		 * Total height - 100, Individual height - 30
		 * no of rows = count/2 
		 * height = Individual height * no of rows
		 */
		int rowCount = 1;
		
		if (flags[0].equalsIgnoreCase("yes")) {
			lblEmployeeId = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(ICommonLabels.COMMON_LABEL_EMPLOYEE_ID));
			lblEmployeeId.setLayoutData(getTopComposite().getGDForLabel());

			txtEmployeeId = new SDSTSText(getTopComposite(), SWT.NONE, LabelLoader
					.getLabelValue(LabelKeyConstants.EMPLOYEE_NAME), FormConstants.FORM_EMPLOYEE_ID);
			txtEmployeeId.setTextLimit(IFieldTextLimits.EMPLOYEE_ID_TEXT_LIMIT);
			txtEmployeeId.setLayoutData(getTopComposite().getGDForText());
			
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}
		if (flags[1].equalsIgnoreCase("yes")) {
			lblEmpPassword = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader
							.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD));
			lblEmpPassword.setLayoutData(getTopComposite().getGDForMiddleLabel());

			txtEmpPassword = new SDSTSText(getTopComposite(), SWT.PASSWORD, LabelLoader
					.getLabelValue(LabelKeyConstants.EMPLOYEE_PASSWORD),
					FormConstants.FORM_EMP_PASSWORD);
			txtEmpPassword.setTextLimit(IFieldTextLimits.EMPLOYEE_PASSWORD_TEXT_LIMIT);
			txtEmpPassword.setLayoutData(getTopComposite().getGDForText());
			
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}
		
		if (flags[2].equalsIgnoreCase("1")) {
			lblSlotSeqLocationNo = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER));
			lblSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtSlotSeqLocationNo = new SDSTSText(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.SLOT_NUMBER),
					FormConstants.FORM_SLOT_NO);
			txtSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForText());
			txtSlotSeqLocationNo.setTextLimit(IFieldTextLimits.SLOT_NO_TEXT_LIMIT);
			txtSlotSeqLocationNo.setNumeric(true);
			
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}else if (flags[2].equalsIgnoreCase("2")) {
			lblSlotSeqLocationNo = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER));
			lblSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtSlotSeqLocationNo = new SDSTSText(getTopComposite(), SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.STAND_NUMBER),
					FormConstants.FORM_SLOT_LOCATION_NO);			
			txtSlotSeqLocationNo.setLayoutData(getTopComposite().getGDForText());
			txtSlotSeqLocationNo.setTextLimit(IFieldTextLimits.SLOT_LOCATION_TEXT_LIMIT);
			
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}
		
		lblAmount = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.SLIP_AMOUNT));
		lblAmount.setLayoutData(getTopComposite().getGDForMiddleLabel());
		txtAmount = new SDSTSText(getTopComposite(), SWT.NONE, "", FormConstants.FORM_AMOUNT, true);
		txtAmount.setTextLimit(25);
		txtAmount.setAmountField(true);
		txtAmount.setNumeric(true);
		txtAmount.setLayoutData(getTopComposite().getGDForText());
		++ rowCount;
		
		if(rowCount %  2 == 0){
			CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
			dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
		}		
		
		if(flags[5].equalsIgnoreCase("YES")) {
			lblCashlessAccountNo = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.ACCOUNT_NO));
			lblCashlessAccountNo.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtCashlessAccountNo = new SDSTSText(getTopComposite(), SWT.LEFT, "",
					FormConstants.FORM_ACCOUNT_NO){
						@Override
						public void cut() {} // DISABLED CTRL + X
						@Override
						public void copy() {} // DISABLED CTRL + C
						@Override
						public void paste() {} // DISABLE CTRL + V
			};
			// DISABLING RIGHT CLICK for txtCashlessAccountNo
			/*txtCashlessAccountNo.addMouseListener(new MouseListener() {
					public void mouseDoubleClick(MouseEvent e) {
					
					}
					public void mouseDown(MouseEvent e) {
						if(e.button == 3) {
							try {
								System.out.println("right mouse button pressed");
								txtCashlessAccountNo.setToolTipText("Right click disabled");										
								Menu menu = new Menu (txtCashlessAccountNo.getShell(), SWT.PUSH);
								MenuItem rightClickMenu = new MenuItem (menu, SWT.PUSH);
								rightClickMenu.setText ("Right click disabled");	
						        menu.setEnabled(false);
						        menu.setVisible (true);					        
								return;	
							} catch(Exception e1 ){
								e1.printStackTrace();
							}
						}
					}				
					public void mouseUp(MouseEvent e) {
						
					}
				});*/
			txtCashlessAccountNo.setNumeric(true);
			txtCashlessAccountNo.setTextLimit(IFieldTextLimits.ACCOUNT_NO_LIMIT);
			txtCashlessAccountNo.setLayoutData(getTopComposite().getGDForText());
			
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
			
			lblCashlessAccountNoConfirm = new CbctlMandatoryLabel(getTopComposite(), SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.CONFIRM_ACCOUNT_N0));
			lblCashlessAccountNoConfirm.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtCashlessAccountNoConfirm = new SDSTSText(getTopComposite(), SWT.LEFT, "",
					FormConstants.FORM_CONFIRM_ACCOUNT_NO){
						@Override
						public void cut() {} // DISABLED CTRL + X
						@Override
						public void copy() {} // DISABLED CTRL + C
						@Override
						public void paste() {} // DISABLE CTRL + V	
			};
			// DISABLING RIGHT CLICK for txtCashlessAccountNoConfirm
			/*txtCashlessAccountNoConfirm.addMouseListener(new MouseListener() {
					public void mouseDoubleClick(MouseEvent e) {
					
					}
					public void mouseDown(MouseEvent e) {
						if(e.button == 3) {
							try {
								System.out.println("right mouse button pressed");
								txtCashlessAccountNoConfirm.setToolTipText("Right click disabled");										
								Menu menu = new Menu (txtCashlessAccountNoConfirm.getShell(), SWT.PUSH);
								MenuItem rightClickMenu = new MenuItem (menu, SWT.PUSH);
								rightClickMenu.setText ("Right click disabled");	
						        menu.setEnabled(false);
						        menu.setVisible (true);					        
								return;	
							} catch(Exception e1 ){
								e1.printStackTrace();
							}
						}
					}				
					public void mouseUp(MouseEvent e) {
						
					}
				});*/		
			txtCashlessAccountNoConfirm.setNumeric(true);
			txtCashlessAccountNoConfirm.setTextLimit(IFieldTextLimits.ACCOUNT_NO_LIMIT);
			txtCashlessAccountNoConfirm.setLayoutData(getTopComposite().getGDForText());
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}
		
		if (flags[3].equalsIgnoreCase("yes")) {
			lblWindowNo = new CbctlLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.WINDOW_NUMBER));
			lblWindowNo.setLayoutData(getTopComposite().getGDForMiddleLabel());
			txtWindowNo = new SDSTSText(getTopComposite(), SWT.LEFT, "",
					FormConstants.FORM_WINDOW_NO);
			txtWindowNo.setTextLimit(IFieldTextLimits.WINDOW_TEXT_LIMIT);
			txtWindowNo.setLayoutData(getTopComposite().getGDForText());
	
			++ rowCount;
			if(rowCount %  2 == 0){
				CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
				dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
			}
		}
		
		lblReason = new CbctlLabel(getTopComposite(), SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.REASON));
		lblReason.setLayoutData(getTopComposite().getGDForMiddleLabel());
		txtReason = new SDSTSText(getTopComposite(), SWT.LEFT, "", FormConstants.FORM_REASON);
		txtReason.setTextLimit(IFieldTextLimits.REASON);
		txtReason.setLayoutData(getTopComposite().getGDForText());
		
		++ rowCount;
		if(rowCount %  2 == 0){
			CbctlLabel dummyLabel = new CbctlLabel(getTopComposite(), SWT.NONE);
			dummyLabel.setLayoutData(getTopComposite().getGDForDummyLabel());
		}
		
		getTopComposite().setHeightHint(calculateCompositeHeight(rowCount));
		getTopComposite().setLayoutForComposite();
	}
	
	/**
	 * @return the flags
	 */
	public String[] getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	public void setFlags(String[] flags) {
		this.flags = flags;
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
	 * @return the lblSlotSeqLocationNo
	 */
	public CbctlMandatoryLabel getLblSlotSeqLocationNo() {
		return lblSlotSeqLocationNo;
	}

	/**
	 * @param lblSlotSeqLocationNo
	 *            the lblSlotSeqLocationNo to set
	 */
	public void setLblSlotSeqLocationNo(CbctlMandatoryLabel lblSlotSeqLocationNo) {
		this.lblSlotSeqLocationNo = lblSlotSeqLocationNo;
	}

	/**
	 * @return the lblWindowNo
	 */
	public CbctlLabel getLblWindowNo() {
		return lblWindowNo;
	}

	/**
	 * @param lblWindowNo
	 *            the lblWindowNo to set
	 */
	public void setLblWindowNo(CbctlLabel lblWindowNo) {
		this.lblWindowNo = lblWindowNo;
	}

	/**
	 * @return the mainGroup
	 */
	public Group getMainGroup() {
		return mainGroup;
	}

	/**
	 * @param mainGroup
	 *            the mainGroup to set
	 */
	public void setMainGroup(Group mainGroup) {
		this.mainGroup = mainGroup;
	}


	/**
	 * @return the shiftControl
	 */
	public TouchScreenShiftControl getShiftControl() {
		return shiftControl;
	}

	/**
	 * @param shiftControl the shiftControl to set
	 */
	public void setShiftControl(TouchScreenShiftControl shiftControl) {
		this.shiftControl = shiftControl;
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
	 * @return the txtWindowNo
	 */
	public SDSTSText getTxtWindowNo() {
		return txtWindowNo;
	}

	/**
	 * @param txtWindowNo
	 *            the txtWindowNo to set
	 */
	public void setTxtWindowNo(SDSTSText txtWindowNo) {
		this.txtWindowNo = txtWindowNo;
	}


	/**
	 * @return the lblAmount
	 */
	public CbctlMandatoryLabel getLblAmount() {
		return lblAmount;
	}

	/**
	 * @param lblAmount the lblAmount to set
	 */
	public void setLblAmount(CbctlMandatoryLabel lblAmount) {
		this.lblAmount = lblAmount;
	}

	/**
	 * @return the lblDay
	 */
	public CbctlLabel getLblDay() {
		return lblDay;
	}

	/**
	 * @param lblDay the lblDay to set
	 */
	public void setLblDay(CbctlLabel lblDay) {
		this.lblDay = lblDay;
	}

	/**
	 * @return the lblGraveyard
	 */
	public CbctlLabel getLblGraveyard() {
		return lblGraveyard;
	}

	/**
	 * @param lblGraveyard the lblGraveyard to set
	 */
	public void setLblGraveyard(CbctlLabel lblGraveyard) {
		this.lblGraveyard = lblGraveyard;
	}

	/**
	 * @return the lblSwing
	 */
	public CbctlLabel getLblSwing() {
		return lblSwing;
	}

	/**
	 * @param lblSwing the lblSwing to set
	 */
	public void setLblSwing(CbctlLabel lblSwing) {
		this.lblSwing = lblSwing;
	}

	/**
	 * @return the txtAmount
	 */
	public SDSTSText getTxtAmount() {
		return txtAmount;
	}

	/**
	 * @param txtAmount the txtAmount to set
	 */
	public void setTxtAmount(SDSTSText txtAmount) {
		this.txtAmount = txtAmount;
	}

	/**
	 * @return the lblCashlessAccountNo
	 */
	public CbctlMandatoryLabel getLblCashlessAccountNo() {
		return lblCashlessAccountNo;
	}

	/**
	 * @param lblCashlessAccountNo the lblCashlessAccountNo to set
	 */
	public void setLblCashlessAccountNo(CbctlMandatoryLabel lblCashlessAccountNo) {
		this.lblCashlessAccountNo = lblCashlessAccountNo;
	}

	/**
	 * @return the txtCashlessAccountNo
	 */
	public SDSTSText getTxtCashlessAccountNo() {
		return txtCashlessAccountNo;
	}

	/**
	 * @param txtCashlessAccountNo the txtCashlessAccountNo to set
	 */
	public void setTxtCashlessAccountNo(SDSTSText txtCashlessAccountNo) {
		this.txtCashlessAccountNo = txtCashlessAccountNo;
	}

	/**
	 * @return the lblCashlessAccountNoConfirm
	 */
	public CbctlMandatoryLabel getLblCashlessAccountNoConfirm() {
		return lblCashlessAccountNoConfirm;
	}

	/**
	 * @param lblCashlessAccountNoConfirm the lblCashlessAccountNoConfirm to set
	 */
	public void setLblCashlessAccountNoConfirm(
			CbctlMandatoryLabel lblCashlessAccountNoConfirm) {
		this.lblCashlessAccountNoConfirm = lblCashlessAccountNoConfirm;
	}

	/**
	 * @return the txtCashlessAccountNoConfirm
	 */
	public SDSTSText getTxtCashlessAccountNoConfirm() {
		return txtCashlessAccountNoConfirm;
	}

	/**
	 * @param txtCashlessAccountNoConfirm the txtCashlessAccountNoConfirm to set
	 */
	public void setTxtCashlessAccountNoConfirm(SDSTSText txtCashlessAccountNoConfirm) {
		this.txtCashlessAccountNoConfirm = txtCashlessAccountNoConfirm;
	}

	/**
	 * @return the lblReason
	 */
	public CbctlLabel getLblReason() {
		return lblReason;
	}

	/**
	 * @param lblReason the lblReason to set
	 */
	public void setLblReason(CbctlLabel lblReason) {
		this.lblReason = lblReason;
	}

	/**
	 * @return the txtReason
	 */
	public SDSTSText getTxtReason() {
		return txtReason;
	}

	/**
	 * @param txtReason the txtReason to set
	 */
	public void setTxtReason(SDSTSText txtReason) {
		this.txtReason = txtReason;
	}

	/**
	 * @return the amountTypeControl
	 *//*
	public TouchScreenAmountTypeControl getAmountTypeControl() {
		return amountTypeControl;
	}

	*//**
	 * @param amountTypeControl the amountTypeControl to set
	 *//*
	public void setAmountTypeControl(TouchScreenAmountTypeControl amountTypeControl) {
		this.amountTypeControl = amountTypeControl;
	}*/

} // @jve:decl-index=0:visual-constraint="10,10"
