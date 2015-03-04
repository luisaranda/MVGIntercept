/*****************************************************************************
 * $Id: ManualJPHandPaidAmtPromoJackpotTypeComposite.java,v 1.26, 2011-02-16 14:47:27Z, Subha Viswanathan$
 * $Date: 2/16/2011 8:47:27 AM$
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
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.FormConstants;
import com.ballydev.sds.jackpotui.constants.IFieldTextLimits;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controls.JackpotTypeControl;


/**
 * Class for Manual jackpot's second screen , where the user enters the handpaid amount and the type of jackpot
 * 
 * @author anantharajr
 * @version $Revision: 27$
 */
public class ManualJPHandPaidAmtPromoJackpotTypeComposite extends TouchScreenBaseComposite {
	/**
	 * HandPaidAmountChangeGroup instance.
	 */
	private Composite handPaidAmountChangeComposite = null;

	/**
	 * Hand Paid Amount Label
	 */
	private CbctlMandatoryLabel lblHandPaidAmount = null;

	/**
	 * HandPaid Amount Text.
	 */
	private SDSTSText txtHandPaidAmount = null;

	/**
	 * Cardless Account Number Label
	 */
	private CbctlMandatoryLabel lblAccountNumber = null;
	
	/**
	 * Cardless Account Number Text
	 */
	private SDSTSText txtAccountNumber = null;
	/**
	 * Confirm Cardless Account Number Label
	 */
	private CbctlMandatoryLabel lblConfirmAccountNumber = null;
	/**
	 * Confirm Cardless Account Number Text
	 */
	private SDSTSText txtConfirmAccountNumber = null;
	
	/**
	 * Progressive Level Label
	 */
	private CbctlLabel lblProgressiveLevel = null;
	
	/**
	 * Progressive Level Text
	 */
	private SDSTSText txtProgressiveLevel = null;

	/**
	 * SlipStaticInfoComposite instance
	 */
	private ManualSlipStaticInfoComposite slipStaticInfoComposite = null; 	
	
	private String[] siteConfigParam;
	
	private JackpotTypeControl jackpotTypeControl = null;
	
	private boolean isProgressive = false;
	
	/**
	 * This field holds true if user selected to process a normal manual jackpot 
	 */
	private Boolean normalYes = true;

	/**
	 * This field holds true if user selected to process a progressive manual jackpot
	 */
	private Boolean progressiveYes = false;

	/**
	 * This field holds true if user selected to process a canceled credit manual jackpot
	 */
	private Boolean canceledCreditsYes = false;
	
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
	 * default constructor
	 * @param parent
	 * @param style
	 */
	public ManualJPHandPaidAmtPromoJackpotTypeComposite(Composite parent, int style,String[] siteConfigParam, boolean isProgressive) {
		
		super(parent, style, LabelLoader
				.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME)
				+ " | "
				+ LabelLoader
						.getLabelValue(LabelKeyConstants.MANUAL_JP_HEADING));
		this.siteConfigParam= siteConfigParam ;
		this.isProgressive = isProgressive;
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
		
		createManualJPHandPaidAmountComposite(getMiddleComposite());
		
		slipStaticInfoComposite = new ManualSlipStaticInfoComposite(getMiddleComposite(), SWT.NONE);
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.composite.TouchScreenBaseComposite#drawTopComposite()
	 */
	public void drawTopComposite() {
		System.out.println("Inside ManualJPHandPaidAmtPromoJackpotTypeComposite:drawTopComposite()");
		if(siteConfigParam[2].equalsIgnoreCase("Yes")) {
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
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.composite.TouchScreenBaseComposite#drawBottomComposite()
	 */
	public void drawBottomComposite() {
		System.out.println("Inside ManualJPHandPaidAmtPromoJackpotTypeComposite:drawBottomComposite()");
		if(siteConfigParam[0].equalsIgnoreCase("yes")) {
			this.jackpotTypeControl = new JackpotTypeControl().drawBottomControls(getBottomComposite(), SWT.NONE, isProgressive);
		}
	}
	
	/**
	 * This method initializes manualJPHandPaidAmountGroup
	 * 
	 */
	private void createManualJPHandPaidAmountComposite(Composite middleComposite) {		
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
		
		lblHandPaidAmount = new CbctlMandatoryLabel(handPaidAmountChangeComposite,	SWT.NONE,LabelLoader.getLabelValue(LabelKeyConstants.HAND_PAID_AMOUNT_LABEL));
		lblHandPaidAmount.setLayoutData(getMiddleComposite().getGDForBodyLabel());
		txtHandPaidAmount = new SDSTSText(handPaidAmountChangeComposite,
				SWT.BORDER, "", FormConstants.FORM_HAND_PAID_AMOUNT,true);
		
		txtHandPaidAmount.setAmountField(true);
		txtHandPaidAmount.setNumeric(true);
		txtHandPaidAmount.setTextLimit(IFieldTextLimits.HPJP_AMOUNT_TEXT_LIMIT);
		txtHandPaidAmount.setLayoutData(getMiddleComposite().getGDForBodyText());
		
		if (isProgressive) {
			lblProgressiveLevel = new CbctlLabel(handPaidAmountChangeComposite, SWT.NONE,
					LabelLoader.getLabelValue(LabelKeyConstants.LBL_PROGRESSIVE_LEVEL));
			lblProgressiveLevel.setLayoutData(getMiddleComposite().getGDForBodyLabel());
			txtProgressiveLevel = new SDSTSText(handPaidAmountChangeComposite, SWT.BORDER, "",
					FormConstants.FORM_PROGRESSIVE_LEVEL, true);

			txtProgressiveLevel.setNumeric(true);
			txtProgressiveLevel.setTextLimit(IFieldTextLimits.PROGRESSIVE_LEVEL_LIMIT);
			txtProgressiveLevel.setLayoutData(getMiddleComposite().getGDForBodyText());
			txtProgressiveLevel.setEnabled(false);
		}

		// Show Cashless Account number text boxes iff 
		// Allow Automatic Deposit to ecash Account is YES 
		if(siteConfigParam[1].equalsIgnoreCase("Yes")) {
			// Cardless Account Number		
			lblAccountNumber =  new CbctlMandatoryLabel(handPaidAmountChangeComposite, 
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
			lblConfirmAccountNumber =  new CbctlMandatoryLabel(handPaidAmountChangeComposite, 
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
	 * @return the slipStaticInfoComposite
	 */
	public ManualSlipStaticInfoComposite getSlipStaticInfoComposite() {
		return slipStaticInfoComposite;
	}

	/**
	 * @param slipStaticInfoComposite the slipStaticInfoComposite to set
	 */
	public void setSlipStaticInfoComposite(
			ManualSlipStaticInfoComposite slipStaticInfoComposite) {
		this.slipStaticInfoComposite = slipStaticInfoComposite;
	}

	/**
	 * @return the txtHandPaidAmount
	 */
	public SDSTSText getTxtHandPaidAmount() {
		return txtHandPaidAmount;
	}

	/**
	 * @param txtHandPaidAmount the txtHandPaidAmount to set
	 */
	public void setTxtHandPaidAmount(SDSTSText txtHandPaidAmount) {
		this.txtHandPaidAmount = txtHandPaidAmount;
	}

	public JackpotTypeControl getJackpotTypeControl() {
		return jackpotTypeControl;
	}

	public void setJackpotTypeControl(JackpotTypeControl jackpotTypeControl) {
		this.jackpotTypeControl = jackpotTypeControl;
	}
	
	/**
	 * @return the canceledCreditsYes
	 */
	public Boolean getCanceledCreditsYes() {
		return canceledCreditsYes;
	}

	/**
	 * @param canceledCreditsYes
	 *            the canceledCreditsYes to set
	 */
	public void setCanceledCreditsYes(Boolean canceledCreditsYes) {
		this.canceledCreditsYes = canceledCreditsYes;
	}

	/**
	 * @return the normalYes
	 */
	public Boolean getNormalYes() {
		return normalYes;
	}

	/**
	 * @param normalYes
	 *            the normalYes to set
	 */
	public void setNormalYes(Boolean normalYes) {
		this.normalYes = normalYes;
	}

	/**
	 * @return the progressiveYes
	 */
	public Boolean getProgressiveYes() {
		return progressiveYes;
	}

	/**
	 * @param progressiveYes
	 *            the progressiveYes to set
	 */
	public void setProgressiveYes(Boolean progressiveYes) {
		this.progressiveYes = progressiveYes;
	}

    /**
	 * @return
	 */
	public CbctlMandatoryLabel getLblAccountNumber() {
		return lblAccountNumber;
	}

	/**
	 * @param lblAccountNumber
	 */
	public void setLblAccountNumber(
			CbctlMandatoryLabel lblAccountNumber) {
		this.lblAccountNumber = lblAccountNumber;
	}

	/**
	 * @return
	 */
	public SDSTSText getTxtAccountNumber() {
		return txtAccountNumber;
	}

	/**
	 * @param txtAccountNumber
	 */
	public void setTxtAccountNumber(SDSTSText txtAccountNumber) {
		this.txtAccountNumber = txtAccountNumber;
	}

	/**
	 * @return
	 */
	public CbctlMandatoryLabel getLblConfirmAccountNumber() {
		return lblConfirmAccountNumber;
	}

	/**
	 * @param lblConfirmAccountNumber
	 */
	public void setLblConfirmAccountNumber(
			CbctlMandatoryLabel lblConfirmAccountNumber) {
		this.lblConfirmAccountNumber = lblConfirmAccountNumber;
	}

	/**
	 * @return
	 */
	public SDSTSText getTxtConfirmAccountNumber() {
		return txtConfirmAccountNumber;
	}

	/**
	 * @param txtConfirmAccountNumber
	 */
	public void setTxtConfirmAccountNumber(
			SDSTSText txtConfirmAccountNumber) {
		this.txtConfirmAccountNumber = txtConfirmAccountNumber;
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

	public SDSTSText getTxtProgressiveLevel() {
		return txtProgressiveLevel;
	}

	public void setTxtProgressiveLevel(SDSTSText txtProgressiveLevel) {
		this.txtProgressiveLevel = txtProgressiveLevel;
	}
}  //  @jve:decl-index=0:visual-constraint="82,50"
