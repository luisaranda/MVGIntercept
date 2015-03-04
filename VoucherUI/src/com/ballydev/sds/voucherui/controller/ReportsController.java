 /*****************************************************************************
 * $Id: ReportsController.java,v 1.22, 2010-07-07 10:47:27Z, Secure Check-in User$
 * $Date: 7/7/2010 5:47:27 AM$
 ******************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSImageLabel;
import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.reportplugin.util.DateUtil;
import com.ballydev.sds.voucherui.composite.ReportsComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.ReportsForm;
import com.ballydev.sds.voucherui.form.ReportsTextForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.service.VoucherServiceLocator;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;
import com.ballydev.sds.voucherui.util.ReportsUtil;

/**
 * This acts as a controller class for the reports composite
 * @author Nithya kalyani R
 * @version $Revision: 23$ 
 */
public class ReportsController  extends SDSBaseController{

	/**
	 * Instance of ReprintVoucherComposite
	 */
	private ReportsComposite reportsComposite;

	/**
	 * Instance of ReprintForm
	 */
	private ReportsForm reportsForm;
	/**
	 * Instance of Logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Instance of the uerReportsId
	 */
	private int userReportID;

	/**
	 * Instance of the parent composite
	 */
	private Composite parent;

	public ReportsController(Composite parent,int style,ReportsForm reportsForm, SDSValidator pValidator)throws Exception {
		super(reportsForm, pValidator);
		reportsComposite = new ReportsComposite(parent,style);
		this.parent = parent;
		this.reportsForm=reportsForm;
		VoucherMiddleComposite.setCurrentComposite(reportsComposite);
		super.registerEvents(reportsComposite);
		registerCustomizedListeners(reportsComposite);
		reportsForm.addPropertyChangeListener(this);		
		reportsForm.setDetailBandToggleBtn(true);
		List<ComboLabelValuePair> reportsList = new ArrayList<ComboLabelValuePair>();
		reportsList.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBLabelKeyConstants.CASHIER_AND_KIOSK_SESS_REP), IVoucherConstants.CASHIER_AND_KIOSK_SESS_REP));
		reportsList.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBLabelKeyConstants.CASHIER_SESS_REP),IVoucherConstants.CASHIER_SESS_REP));
		reportsList.add(new ComboLabelValuePair(LabelLoader.getLabelValue(IDBLabelKeyConstants.KIOSK_SESS_REP), IVoucherConstants.KIOSK_SESS_REP));

		reportsForm.setReportsList(reportsList);

		reportsComposite.getReportsList().setSelectedIndex(0);		
		reportsForm.setSelectedReport(reportsList.get(0).getLabel());		
		reportsComposite.getTxtEmployeeId().setFocus();
		parent.layout();
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {		
		return reportsComposite;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		((ReportsComposite)argComposite).getTChkEmpAll().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getBtnSubmit().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getDetailRadioImage().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getSummaryRadioImage().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getTChkNcsh().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getTChkVoucher().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getTChkCsh().addMouseListener(new ReportsMouseListener());
		((ReportsComposite)argComposite).getReportsList().addMouseListener(new ReportsMouseListener());
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		super.widgetSelected(e);
		try {
			populateForm(reportsComposite);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(reportsForm.getSelectedReport().equalsIgnoreCase(LabelLoader.getLabelValue(IDBLabelKeyConstants.CASHIER_AND_KIOSK_SESS_REP))){
			userReportID = IVoucherConstants.CASHIER_AND_KIOSK_SESS_REP_ID;
		}else if(reportsForm.getSelectedReport().equalsIgnoreCase( LabelLoader.getLabelValue(IDBLabelKeyConstants.CASHIER_SESS_REP))){
			userReportID = IVoucherConstants.CASHIER_SESS_REP_ID;
		}else if(reportsForm.getSelectedReport().equalsIgnoreCase(LabelLoader.getLabelValue(IDBLabelKeyConstants.KIOSK_SESS_REP))){
			userReportID = IVoucherConstants.KIOSK_SESS_REP_ID;
		}
	}

	public boolean getControlValueForTKtStatusFrmPref() {
		boolean retValue = true;
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		if( (!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED))) &&
				(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED))) && 
				(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED)))){
			if((!preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED)) &&
					(!preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED))&&
					(!preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED))) {
				retValue = false;
			}
		}		
		return retValue;
	}
	
	private String getBandSelectedValue() {
		String retValue = null;
		if(reportsForm.getDetailBandToggleBtn()) {
			retValue = IVoucherConstants.DETAIL_VALUE;
		}else {
			retValue = IVoucherConstants.SUMM_VALUE;
		}		
		return retValue;
	}

	private void setSelectedNcshButton(SDSTSCheckBox selectedButton) {
		if( selectedButton.isSelected() == false) {
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			selectedButton.setSelected(true);
			reportsForm.setIsNonCashable(true);
		} else {
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			selectedButton.setSelected(false);
			reportsForm.setIsNonCashable(false);
		}		
	}

	private void setSelectedTktButton(SDSTSCheckBox selectedButton) {
		if( selectedButton.isSelected() == false) {
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			selectedButton.setSelected(true);
			reportsForm.setIsVoucher(true);
		} else {
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			selectedButton.setSelected(false);
			reportsForm.setIsVoucher(false);
		}		
	}

	private void setSelectedCshButton(SDSTSCheckBox selectedButton) {
		if( selectedButton.isSelected() == false) {
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			selectedButton.setSelected(true);
			reportsForm.setIsCashable(true);
		} else {
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			selectedButton.setSelected(false);
			reportsForm.setIsCashable(false);
		}		
	}

	public String getControlValueForTKtStatus() {
		String retValue = null;
		if( !Util.isEmpty(reportsForm.getIsVoucher()) ) {
			if( reportsForm.getIsVoucher() ) {
				if( retValue == null ) {
					retValue = "'" + IVoucherConstants.CSH_FORM_VALUE + "'";
				} else {
					retValue = retValue + "'" + IVoucherConstants.CSH_FORM_VALUE + "'";
				}
			}
		}
		if( !Util.isEmpty(reportsForm.getIsNonCashable()) ){
			if( reportsForm.getIsNonCashable() ) {
				if( retValue == null ) {
					retValue = "'" + IVoucherConstants.NCSH_PROMO_FORM_VALUE + "'";
				} else {
					if( retValue.trim().length() != 0 ) {
						retValue = retValue + ",'" + IVoucherConstants.NCSH_PROMO_FORM_VALUE + "'";
					}					
				}				
			}
		}
		if( !Util.isEmpty(reportsForm.getIsCashable()) ) {
			if( reportsForm.getIsCashable() ) {
				if( retValue == null ) {
					retValue = "'" + IVoucherConstants.CSH_PROMO_FORM_VALUE + "'";
				}else {
					if( retValue.trim().length() != 0 ) {
						retValue = retValue + ",'" + IVoucherConstants.CSH_PROMO_FORM_VALUE + "'";
					}					
				}	
			}
		}	
		return retValue;
	}

	/**
	 * Method to set the SelectedButton
	 * @param selectedButton
	 */
	public void setSelectedEmpButton(SDSTSCheckBox selectedButton) {	
		if( selectedButton.isSelected() == false) {
			log.debug("Radio button selected");
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			selectedButton.setSelected(true);
			reportsForm.setEmployeeAllToggleBtn(true);
			reportsComposite.getTxtEmployeeId().setEnabled(false);
		}else{
			log.debug("Radio button deselected");
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			selectedButton.setSelected(false);
			reportsForm.setEmployeeAllToggleBtn(false);
			reportsComposite.getTxtEmployeeId().setEnabled(true);
			reportsComposite.getTxtEmployeeId().setFocus();
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e) ;
		//composite.redraw();
		if (e.getSource() instanceof SDSTSText)
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
	}
	
	
	private class ReportsMouseListener implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseDown(MouseEvent e) {
			
			try {
				populateForm(reportsComposite);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			Control control = (Control) e.getSource();
			Control [] ctrl = null;
			
			if(reportsForm.getSelectedReport().equalsIgnoreCase(LabelLoader.getLabelValue(IDBLabelKeyConstants.CASHIER_AND_KIOSK_SESS_REP))){
				userReportID = IVoucherConstants.CASHIER_AND_KIOSK_SESS_REP_ID;
			}else if(reportsForm.getSelectedReport().equalsIgnoreCase( LabelLoader.getLabelValue(IDBLabelKeyConstants.CASHIER_SESS_REP))){
				userReportID = IVoucherConstants.CASHIER_SESS_REP_ID;
			}else if(reportsForm.getSelectedReport().equalsIgnoreCase(LabelLoader.getLabelValue(IDBLabelKeyConstants.KIOSK_SESS_REP))){
				userReportID = IVoucherConstants.KIOSK_SESS_REP_ID;
			}

			if (((Control) e.getSource() instanceof SDSImageLabel)) {
				if (((SDSImageLabel) control).getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_BTN_SUBMIT)) {

					try {
						if(!getControlValueForTKtStatusFrmPref()) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.SELECT_ONE_OPT_REP));
							return;
						}

						populateForm(reportsComposite);
						boolean isValidate = validate(IVoucherConstants.REPORTS_VOUCHER_FORM,reportsForm,reportsComposite);
						if(!isValidate){
							return;
						}
						if(getControlValueForTKtStatus()==null) {
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOUCHER_SELECT_TKT_TYPE));
							return;
						}
						ProgressIndicatorUtil.openInProgressWindow();
						if (reportsComposite.getTxtEmployeeId().isEnabled() && reportsForm.getTxtEmployeeId() != null && reportsForm.getTxtEmployeeId().trim().length() > 0) {
							if (!VoucherServiceLocator.getService().isValidEmployee(reportsForm.getTxtEmployeeId(), (int)SDSApplication.getUserDetails().getSiteId())) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IVoucherConstants.INVALID_USER));
								ProgressIndicatorUtil.closeInProgressWindow();
								return;
							}
						}
						
						ReportsUtil reportsUtil = new ReportsUtil();
						HashMap<Object,Object> userInput = new HashMap<Object,Object>();
						userInput.put(IVoucherConstants.EMP_ID_PARAM_ID, reportsForm.getTxtEmployeeId());
						userInput.put(IVoucherConstants.PROPERTY_ID_PARAM_ID, reportsForm.getTxtPropertyId());
						userInput.put(IVoucherConstants.SITE_ID_PARAM_ID, reportsForm.getTxtPropertyId());
						userInput.put(IVoucherConstants.START_TIME_PARAM_ID, DateUtil.getUTCTimeForServerTime(new Timestamp(reportsForm.getStartTime().getTime())).toString());
						userInput.put(IVoucherConstants.END_TIME_PARAM_ID, DateUtil.getUTCTimeForServerTime(new Timestamp(reportsForm.getEndTime().getTime())).toString());	
						userInput.put(IVoucherConstants.ALL_CONTROL_VALUE,getControlValueForTKtStatus());
						userInput.put(IVoucherConstants.DETAIL_VALUE, getBandSelectedValue());
						log.debug("Select All option value for Employee inside the button click of submit "+ reportsForm.getEmployeeAllToggleBtn());
//						System.out.println("Selected Employee status: " + reportsComposite.getTChkEmpAll().isSelected());
						String reportsOutput= reportsUtil.getParameterTO(userReportID,userInput, reportsComposite.getTChkEmpAll().isSelected());
						reportsComposite.dispose();
						ReportsTextForm reportsTextForm = new ReportsTextForm();
						reportsTextForm.setTxtReportsDisplay(reportsOutput);
						ReportsTextController textController = new ReportsTextController(parent,SWT.NONE,reportsTextForm,new SDSValidator((getClass()),true));
						textController.populateScreen(textController.getComposite());
					} catch (Exception e1) {
						log.debug("Exception occured while fetching the reports");
						e1.printStackTrace();
						return;
					} finally {
						ProgressIndicatorUtil.closeInProgressWindow();
					}
				}
			}
			
			if (((Control) e.getSource() instanceof SDSTSCheckBox)) {
				SDSTSCheckBox tsCheckBox = (SDSTSCheckBox) ((Control)e.getSource());
				if(tsCheckBox.getName().equalsIgnoreCase(IVoucherConstants.REPORTS_CTRL_TGL_BTN_EMPLOYEE_ASSET)) {
					setSelectedEmpButton(tsCheckBox);
				}else if(tsCheckBox.getName().equalsIgnoreCase(IVoucherConstants.REPORT_FRM_BTN_NCSH)) {
					setSelectedNcshButton(tsCheckBox);
				}else if(tsCheckBox.getName().equalsIgnoreCase(IVoucherConstants.REPORT_FRM_BTN_TICKET)) {
					setSelectedTktButton(tsCheckBox);
				}else if(tsCheckBox.getName().equalsIgnoreCase(IVoucherConstants.REPORT_FRM_BTN_CSH)) {
					setSelectedCshButton(tsCheckBox);
				}
			}
			
			if (((Control) e.getSource() instanceof TSButtonLabel)) {
				if( reportsComposite != null && !reportsComposite.isDisposed() )
					ctrl = reportsComposite.getToggleBtnComposite().getChildren();
				
				doCheck(ctrl, e);
			}
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}
	
	// Checking the toggling status changing the selection.
	private void doCheck(Control[] ctrl, MouseEvent e) {
		TSButtonLabel selectedRadioBtn = (TSButtonLabel) ((Control)e.getSource());
		for( int i = 0; i < ctrl.length; i++ ) {
			if( ctrl[i] instanceof TSButtonLabel ) {
				TSButtonLabel btn = (TSButtonLabel) ctrl[i];
				if( selectedRadioBtn.getName().equalsIgnoreCase(btn.getName().toString()) ) {
					btn.setEnabled(true);
					btn.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_ACTIVE_BTHEME)));
				}
				else {
					btn.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_RADIO_BUTTON_INACTIVE_BTHEME)));
					btn.setEnabled(true);
				}
			}
		}
		if( selectedRadioBtn.getName().equalsIgnoreCase("summBandToggleBtn") ) {
			reportsForm.setSummBandToggleBtn(true);
			reportsForm.setDetailBandToggleBtn(false);
			
		} else if( selectedRadioBtn.getName().equalsIgnoreCase("detailBandToggleBtn") ) {
			reportsForm.setDetailBandToggleBtn(true);
			reportsForm.setSummBandToggleBtn(false);
		}
	}
}
