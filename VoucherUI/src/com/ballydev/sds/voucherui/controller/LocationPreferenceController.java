package com.ballydev.sds.voucherui.controller;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSTSLabelText;
import com.ballydev.sds.framework.control.SDSTSText;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.composite.LocationPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.LocationPreferenceForm;

public class LocationPreferenceController  extends SDSBaseController {
	
	private LocationPreferenceComposite locationPreferenceComposite;
	private LocationPreferenceForm locationPreferenceForm;

	public LocationPreferenceController(Composite parent, int style ,LocationPreferenceForm form, SDSValidator pValidator) throws Exception 
	{
		super(form, pValidator);
		locationPreferenceComposite = new LocationPreferenceComposite(parent,style);
		this.locationPreferenceForm = form;
		super.registerEvents(locationPreferenceComposite);
		registerCustomizedListeners(locationPreferenceComposite);
		form.addPropertyChangeListener(this);
		getLocationPreferences();
		populateScreen(locationPreferenceComposite);
		locationPreferenceComposite.layout();
		parent.layout();
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			locationPreferenceComposite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_LOC_VALUE_SET_LOGIN));
			locationPreferenceComposite.disableControls();
			setSelectedOption();
			return ;
		} else if(AppContextValues.getInstance().isVoucherOpen()){
			locationPreferenceComposite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_LOC_VALUE_SET_CLOSE));
			locationPreferenceComposite.disableControls();
			setSelectedOption();
			return ;
		} else {
			cashierOrAuditor();
		}
		locationPreferenceComposite.getTxtLocationId().setFocus();
	}

	private void cashierOrAuditor() {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();	
		Boolean isCashier = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_CASHIER);
		Boolean isAuditor = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_AUDITOR);
		if(isCashier){
			boolean toggleStatus = true;
			locationPreferenceComposite.setCashier(toggleStatus);
			locationPreferenceComposite.getRadioButtonControl().setSelectedButton(locationPreferenceComposite.getRadioImgLblCashier());
		}
		else if(isAuditor){
			boolean toggleStatus = true;
			locationPreferenceComposite.setCashier(toggleStatus);
			locationPreferenceComposite.getRadioButtonControl().setSelectedButton(locationPreferenceComposite.getRadioImgLblAuditor());
		}		
	}

	/**
	 * This method sets the selection of the
	 * touch screen radio button acc to the
	 * value stored in the preference page 
	 */
	private void setSelectedOption(){
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();	
		Boolean isCashier = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_CASHIER);
		Boolean isAuditor = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_AUDITOR);
		if(isCashier){
			boolean toggleStatus = true;
			locationPreferenceComposite.setCashier(toggleStatus);
//			locationPreferenceComposite.getRadioButtonControl().setSelectedButton(locationPreferenceComposite.getRadioImgLblCashier());
		}
		else if(isAuditor){
			boolean toggleStatus = true;
			locationPreferenceComposite.setCashier(toggleStatus);
//			locationPreferenceComposite.getRadioButtonControl().setSelectedButton(locationPreferenceComposite.getRadioImgLblAuditor());
		}	
	}

	private void getLocationPreferences() {		
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();	
		Boolean isCashier = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_CASHIER);
		Boolean isAuditor = preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_AUDITOR);

		if(!Util.isEmpty(isAuditor)){
			locationPreferenceForm.setIsAuditor(preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_AUDITOR));
		}

		if(!Util.isEmpty(isCashier)){
			locationPreferenceForm.setIsCashier(preferenceStore.getBoolean(IVoucherPreferenceConstants.LOCATION_IS_CASHIER));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID))){
			locationPreferenceForm.setLocationId(preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID));
		}

		if((!Util.isEmpty(isCashier) && !isCashier) && (!Util.isEmpty(isAuditor) && !isAuditor)){
			locationPreferenceForm.setIsCashier(true);
		}

		if(isCashier){
			boolean toggleStatus = true;
			locationPreferenceComposite.setCashier(toggleStatus);
//			locationPreferenceComposite.getRadioButtonControl().setSelectedButton(locationPreferenceComposite.getRadioImgLblCashier());
		}
		else if(isAuditor){
			boolean toggleStatus = true;
			locationPreferenceComposite.setCashier(toggleStatus);
//			locationPreferenceComposite.getRadioButtonControl().setSelectedButton(locationPreferenceComposite.getRadioImgLblAuditor());
		}		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		try {
			populateForm(locationPreferenceComposite);			
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}

		Control ctrl = ((Control)e.getSource());
		
		if( ((Control) e.getSource() instanceof TSButtonLabel) ) {

			if( Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
				return ;
			}

			if( ctrl.equals(locationPreferenceComposite.getRadioImgLblCashier()) ) {				
				boolean toggleStatus = ctrl.equals(locationPreferenceComposite.getRadioImgLblCashier());
				locationPreferenceComposite.setCashier(toggleStatus);
				locationPreferenceForm.setIsCashier(toggleStatus);
				locationPreferenceForm.setIsAuditor(false);
				TSButtonLabel tSRadioButtonImage = (TSButtonLabel) (ctrl);
				locationPreferenceComposite.getRadioButtonControl().setSelectedButton(tSRadioButtonImage);
				locationPreferenceComposite.getTxtLocationId().setFocus();

			} else if( ctrl.equals(locationPreferenceComposite.getRadioImgLblAuditor())){				
				boolean toggleStatus = ctrl.equals(locationPreferenceComposite.getRadioImgLblAuditor());
				locationPreferenceComposite.setCashier(toggleStatus);
				locationPreferenceForm.setIsAuditor(toggleStatus);
				locationPreferenceForm.setIsCashier(false);
				TSButtonLabel tSRadioButtonImage = (TSButtonLabel) (ctrl);
				locationPreferenceComposite.getRadioButtonControl().setSelectedButton(tSRadioButtonImage);
				locationPreferenceComposite.getTxtLocationId().setFocus();

			}
			try {
				populateScreen(locationPreferenceComposite);
			} catch (Exception e1) {		
				e1.printStackTrace();
			}
		}		
	}

	@Override
	public LocationPreferenceComposite getComposite() 
	{
		return locationPreferenceComposite;
	}

	public LocationPreferenceForm getForm() 
	{
		return locationPreferenceForm;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		LocationPreferenceComposite locPreferenceComposite = ((LocationPreferenceComposite)argComposite);
		locPreferenceComposite.getRadioImgLblAuditor().addMouseListener(this);
		locPreferenceComposite.getRadioImgLblCashier().addMouseListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		//locationPreferenceComposite.redraw();		
		if (e.getSource() instanceof SDSTSText){			
			KeyBoardUtil.setCurrentTextInFocus((SDSTSText) e.getSource());
		}
		if (e.getSource() instanceof SDSTSLabelText){
			KeyBoardUtil.setCurrentTextInFocus(((SDSTSLabelText) e.getSource()).getSdsText());
		}
	}
}
