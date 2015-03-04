/*****************************************************************************
 * $Id: BarcodeScannerPreference.java,v 1.10, 2010-12-31 08:47:17Z, Bhandari, Vineet$
 * $Date: 12/31/2010 2:47:17 AM$
 *****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.preference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceItem;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.BarcodeScannerPreferenceController;
import com.ballydev.sds.voucherui.form.BarcodeScannerPreferenceForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class BarcodeScannerPreference extends TouchScreenPreferenceItem {

	private BarcodeScannerPreferenceController controller;

	public BarcodeScannerPreference(){
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_BARCODE_SCANNER_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_BARCODE_SCANNER_TITLE));
	}

	@Override
	public Control createContents(Composite parent)	{
		try {
			controller = new BarcodeScannerPreferenceController(parent,SWT.NONE,new BarcodeScannerPreferenceForm(),null);
			setValues();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controller.getComposite();
	}

	@Override
	public boolean performCancel() 
	{
		return super.performCancel();
	}

	@Override
	public boolean isEnabled() {		
		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean performOk()	{

		return super.performOk();
	}

	public void init(IWorkbench workbench) {

	}

	@Override
	public void checkState() {

	}

	@Override
	public String getName() {
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_BARCODE_SCANNER_TITLE);
	}

	@Override
	public void performApply() {
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			setValues();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
		}
	}

	private void setValues(){
		if(!Util.isEmpty(SDSApplication.getLoggedInUserID())){
			return;
		}
		@SuppressWarnings("unused")
		String testStatus = "";
		String selectedScannerPort = "";
		String selectedScannerType ="";
		String manufacturer = "";
		String model = "";
		String driver = "";
		Boolean isBarcodeScanner = false;


		if(!Util.isEmpty(controller.getForm().getIsBarcodeScanner()))
		{
			isBarcodeScanner = controller.getForm().getIsBarcodeScanner();
		}

		if(!Util.isEmpty(controller.getForm().getTestStatus()))
		{
			testStatus = controller.getForm().getTestStatus();
		}

		if(!Util.isEmpty(controller.getForm().getSelectedScannerPort()))
		{
			selectedScannerPort = controller.getForm().getSelectedScannerPort();
		}

		if(!Util.isEmpty(controller.getForm().getSelectedScannerType()))
		{
			selectedScannerType = controller.getForm().getSelectedScannerType();
		}

		if(!Util.isEmpty(controller.getForm().getManufacturer()))
		{
			manufacturer = controller.getForm().getManufacturer();
		}

		if(!Util.isEmpty(controller.getForm().getModel()))
		{
			model = controller.getForm().getModel();
		}

		if(!Util.isEmpty(controller.getForm().getDriver()))
		{
			driver = controller.getForm().getDriver();
		}

		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.IS_BARCODE_SCANNER, isBarcodeScanner);		
		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.SCANNER_PORT, selectedScannerPort);
		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.SCANNER_TYPE, selectedScannerType);
		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.SCANNER_MANUFACTURER, manufacturer);
		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.SCANNER_MODEL, model);

		
		//TEMP FIX UNTIL THE FRAMEWORK PROVIDES THE SCANNER SUPPORT
		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.SCANNER_DRIVER, driver);
//		SDSPreferenceStore.setStoreValue(IVoucherPreferenceConstants.SCANNER_DRIVER, driver);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			super.performDefaults();
			controller.getForm().setSelectedScannerPort(((ComboLabelValuePair)controller.getForm().getScannerPortList().get(0)).getLabel());
			controller.setSelectedScannerPortValue(((ComboLabelValuePair)controller.getForm().getScannerPortList().get(0)).getLabel());
			controller.getForm().setSelectedScannerType(((ComboLabelValuePair)controller.getForm().getScannerTypeList().get(0)).getLabel());
			controller.setSelectedScannerTypeValue(((ComboLabelValuePair)controller.getForm().getScannerTypeList().get(0)).getLabel());
			controller.getForm().setDriver(IVoucherConstants.TKTSCANNER_DUPLO_DRIVER);
			controller.getForm().setManufacturer(IVoucherConstants.TKTSCANNER_DUPLO_MANFT);
			controller.getForm().setModel(IVoucherConstants.TKTSCANNER_DUPLO_MODEL);	
			try {
				controller.populateScreen(controller.getComposite());
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}
	}
}
