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
import com.ballydev.sds.voucherui.composite.TicketPrinterPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.TicketPrinterPreferenceController;
import com.ballydev.sds.voucherui.form.TicketPrinterPreferenceForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class TicketPrinterPreference extends TouchScreenPreferenceItem {

	private TicketPrinterPreferenceController controller;
	private TicketPrinterPreferenceComposite composite;
	public TicketPrinterPreference() {
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_TICKET_PRINTER_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_TICKET_PRINTER_TITLE));
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
			controller = new TicketPrinterPreferenceController(parent,SWT.NONE,new TicketPrinterPreferenceForm(),null);
			composite = controller.getComposite();
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
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_TICKET_PRINTER_TITLE);
	}


	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			super.performDefaults();
			controller.getForm().setSelectedPrinterPort(((ComboLabelValuePair)controller.getForm().getPrinterPortList().get(0)).getLabel());
			controller.setSelectedPrinterPortValue(((ComboLabelValuePair)controller.getForm().getPrinterPortList().get(0)).getLabel());
			controller.getForm().setSelectedPrinterType(((ComboLabelValuePair)controller.getForm().getPrinterTypeList().get(0)).getLabel());
			controller.setSelectedPrinterTypeValue(((ComboLabelValuePair)controller.getForm().getPrinterTypeList().get(0)).getLabel());
			controller.getForm().setDriver(IVoucherConstants.TKTSCANNER_ITHACA_DRIVER);
			controller.getForm().setManufacturer(IVoucherConstants.TKTSCANNER_ITHACA_MANFT);
			controller.getForm().setModel(IVoucherConstants.TKTSCANNER_ITHACA_750_MODEL);
			controller.getComposite().getRadioButtonControl().setSelectedButton(controller.getComposite().getRadioImageNormalSize());
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
			try {
				setPrinterSelection();
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_FAILED));
			}
			
		}
	}

	public void setPrinterSelection() throws Exception{
		if(!Util.isEmpty(SDSApplication.getLoggedInUserID())){
			return;
		}
		String selectedPrinterPort = "";
		String selectedPrinterType = "";
		String manufacturer = "";
		String model = "";
		String driver = "";
		boolean isSmallSize = false;
		boolean isNormalSize = false;
		controller.populateForm(composite);
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
		if(!Util.isEmpty(controller.getForm().getIsSmallSize())){
			isSmallSize = controller.getForm().getIsSmallSize();
		}
		if(!Util.isEmpty(controller.getForm().getIsNormalSize())){
			isNormalSize = controller.getForm().getIsNormalSize();
		}
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_PORT, selectedPrinterPort);
		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_TYPE, selectedPrinterType);
		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MANUFACTURER, manufacturer);
		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_MODEL, model);
		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_DRIVER, driver);
		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_SMALL_SIZE, isSmallSize);
		preferenceStore.setValue(IVoucherPreferenceConstants.TICKET_PRINTER_NORMAL_SIZE, isNormalSize);
	}
}
