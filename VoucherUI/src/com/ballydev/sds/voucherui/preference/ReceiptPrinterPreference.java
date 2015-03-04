 /*****************************************************************************
 * $Id: ReceiptPrinterPreference.java,v 1.9, 2010-12-31 08:47:17Z, Bhandari, Vineet$
 * $Date: 12/31/2010 2:47:17 AM$
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

package com.ballydev.sds.voucherui.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceItem;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.ReceiptPrinterPreferenceController;
import com.ballydev.sds.voucherui.form.ReceiptPrinterPreferenceForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class ReceiptPrinterPreference extends TouchScreenPreferenceItem {

	private ReceiptPrinterPreferenceController controller;

	public ReceiptPrinterPreference() {
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_RECEIPT_PRINTER_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_RECEIPT_PRINTER_TITLE));
	}

	@Override
	public boolean performOk(){		

		return super.performOk();
	}

	@Override
	public boolean isEnabled() {		
		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean performCancel() 
	{
		return super.performCancel();
	}

	@Override
	public Control createContents(Composite parent) 
	{
		try {			
			controller = new ReceiptPrinterPreferenceController(parent,SWT.NONE,new ReceiptPrinterPreferenceForm(),null);
			setPrinterSelection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controller.getComposite();
	}

	public void init(IWorkbench workbench) {

	}


	@Override
	public void checkState() {

	}


	@Override
	public String getName() {
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_RECEIPT_PRINTER_TITLE);
	}


	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			super.performDefaults();
			Boolean isReceiptPrinting = false;	
			controller.getForm().setSelectedPrinterPort(((ComboLabelValuePair)controller.getForm().getPrinterPortList().get(0)).getLabel());
			controller.setSelectedPrinterPortValue(((ComboLabelValuePair)controller.getForm().getPrinterPortList().get(0)).getLabel());
			controller.getForm().setSelectedPrinterType(((ComboLabelValuePair)controller.getForm().getPrinterTypeList().get(0)).getLabel());
			controller.setSelectedPrinterTypeValue(((ComboLabelValuePair)controller.getForm().getPrinterTypeList().get(0)).getLabel());
			controller.getForm().setDriver(IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
			controller.getForm().setManufacturer(IVoucherConstants.TKTSCANNER_ITHACA_MANFT);
			controller.getForm().setModel(IVoucherConstants.TKTSCANNER_ITHACA_MODEL);	
			controller.getForm().setIsReceiptPrinting(isReceiptPrinting);
			try {
				controller.populateScreen(controller.getComposite());
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}
	}

	@Override
	public void performApply() 	{
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			setPrinterSelection();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
		}
	}

	public void setPrinterSelection(){
		if(!Util.isEmpty(SDSApplication.getLoggedInUserID())){
			return;
		}
		String selectedPrinterPort = "";
		String selectedPrinterType = "";
		String manufacturer = "";
		String model = "";
		String driver = "";	
		Boolean isReceiptPrinting = false;	

		if(!Util.isEmpty(controller.getForm().getIsReceiptPrinting())){
			isReceiptPrinting = controller.getForm().getIsReceiptPrinting();
		}

		if(!Util.isEmpty(controller.getForm().getSelectedPrinterPort())){
			selectedPrinterPort = controller.getForm().getSelectedPrinterPort();			
		}
		if(!Util.isEmpty(controller.getForm().getSelectedPrinterType())){
			selectedPrinterType = controller.getForm().getSelectedPrinterType();
		}
		if(!Util.isEmpty(controller.getForm().getManufacturer())){
			manufacturer = controller.getForm().getManufacturer();
		}
		if(!Util.isEmpty(controller.getForm().getModel())){
			model = controller.getForm().getModel();
		}
		if(!Util.isEmpty(controller.getForm().getDriver())){
			driver = controller.getForm().getDriver();
		}		
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		preferenceStore.setValue(IVoucherPreferenceConstants.IS_RECEIPT_PRINTING, isReceiptPrinting);
		preferenceStore.setValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_PORT, selectedPrinterPort);
		preferenceStore.setValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_TYPE, selectedPrinterType);
		preferenceStore.setValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_MANUFACTURER, manufacturer);
		preferenceStore.setValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_MODEL, model);
		preferenceStore.setValue(IVoucherPreferenceConstants.RECEIPT_PRINTER_DRIVER, driver);	
	}
}
