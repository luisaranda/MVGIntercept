package com.ballydev.sds.voucherui.preference;

import java.io.IOException;

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
import com.ballydev.sds.voucherui.composite.CustomerDisplayPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.CustomerDisplayPreferenceController;
import com.ballydev.sds.voucherui.displays.Display;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.form.CustomerDisplayPreferenceForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class CustomerDisplayPreference extends TouchScreenPreferenceItem {
	
	@Override
	public boolean performCancel() 	{
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
			String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
			if(Util.isEmpty(dispType) && Util.isEmpty(dispDriver)){
				dispDriver=IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
				dispType=IVoucherConstants.CUST_DISP_PIONEER_MANFT+" "+IVoucherConstants.CUST_DISP_PIONEER_MODEL;
			}
			Display display = null;
			Class.forName(dispDriver);
			display = Display.getDisplay(dispType);	
			display.blankDisplay();
			display.close();
		} catch (ClassNotFoundException e) {			
			//e.printStackTrace();			
		} catch (DisplayNoSuchDriverException e) {			
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}catch (Exception e) {
			//e.printStackTrace();
		}	
		return super.performCancel();
	}

	@Override
	public boolean performOk() {
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
			String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
			if(Util.isEmpty(dispType) && Util.isEmpty(dispDriver)){
				dispDriver=IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
				dispType=IVoucherConstants.CUST_DISP_PIONEER_MANFT+" "+IVoucherConstants.CUST_DISP_PIONEER_MODEL;
			}
			Display display = null;
			Class.forName(dispDriver);
			display = Display.getDisplay(dispType);	
			display.blankDisplay();
			display.close();
		} catch (ClassNotFoundException e) {			
			//e.printStackTrace();			
		} catch (DisplayNoSuchDriverException e) {			
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}catch (Exception e) {
			//e.printStackTrace();
		}

		return super.performOk();
	}

	private CustomerDisplayPreferenceController controller;

	public CustomerDisplayPreference(){
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_CUSTOMER_DISPLAY_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_CUSTOMER_DISPLAY_TITLE));
	}

	@Override
	public Control createContents(Composite parent)	{
		try 
		{
			controller = new CustomerDisplayPreferenceController(parent,SWT.NONE,new CustomerDisplayPreferenceForm(),null);
			setValues();
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
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_CUSTOMER_DISPLAY_TITLE);
	}

	@Override
	public void performApply(){
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			updateForm();
			setValues();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
		}
	}


	@Override
	public boolean isEnabled() {		
		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {		
		super.performDefaults();
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){	
			Boolean isCustomerDisplay = false;	
			Boolean isDisplayCenter = true;	
			Boolean isDisplayScroll = false;			
			controller.getForm().setSelectedPort(((ComboLabelValuePair)controller.getForm().getPortList().get(0)).getLabel());
			controller.setSelectedDisplayPortValue(((ComboLabelValuePair)controller.getForm().getPortList().get(0)).getLabel());
			controller.getForm().setSelectedDisplayType(((ComboLabelValuePair)controller.getForm().getDisplayTypeList().get(0)).getLabel());
			controller.setSelectedDisplayTypeValue(((ComboLabelValuePair)controller.getForm().getDisplayTypeList().get(0)).getLabel());
			controller.getForm().setDriver(IVoucherConstants.CUST_DISP_PIONEER_DRIVER);
			controller.getForm().setManufacturer(IVoucherConstants.CUST_DISP_PIONEER_MANFT);
			controller.getForm().setModel(IVoucherConstants.CUST_DISP_PIONEER_MODEL);				
			controller.getForm().setIsCenterDisplay(isDisplayCenter);
			controller.getForm().setIsCustomerDisplay(isCustomerDisplay);
			controller.getForm().setIsScrollDisplay(isDisplayScroll);
			controller.getComposite().setCenterDisplay(isDisplayCenter);									
			controller.getComposite().getRadioButtonControl().setSelectedButton(controller.getComposite().getRadioImageDispCenter());	
			try {
				controller.populateScreen(controller.getComposite());
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}
	}

	private void setValues(){
		if(!Util.isEmpty(SDSApplication.getLoggedInUserID())){
			return;
		}
		String testStatus = "";
		String welcomeMessage = "";
		String selectedDisplayType  = "";;
		String selectedPort = "";
		String manufacturer = "";
		String model = "";
		String driver = "";
		Boolean isCustomerDisplay = false;	
		Boolean isDisplayCenter = false;	
		Boolean isDisplayScroll = false;	

		if(!Util.isEmpty(controller.getForm().getIsCustomerDisplay()))
		{
			isCustomerDisplay = controller.getForm().getIsCustomerDisplay();
		}

		if(!Util.isEmpty(controller.getForm().getIsCenterDisplay()))
		{
			isDisplayCenter = controller.getForm().getIsCenterDisplay();			
		}

		if(!Util.isEmpty(controller.getForm().getIsScrollDisplay()))
		{
			isDisplayScroll = controller.getForm().getIsScrollDisplay();			
		}

		if(!Util.isEmpty(controller.getForm().getTestStatus()))
		{
			testStatus = controller.getForm().getTestStatus();
		}

		if(!Util.isEmpty(controller.getForm().getWelcomeMessage()))
		{
			welcomeMessage = controller.getForm().getWelcomeMessage();			
		}

		if(!Util.isEmpty(controller.getForm().getSelectedDisplayType()))
		{
			selectedDisplayType = controller.getForm().getSelectedDisplayType();
		}

		if(!Util.isEmpty(controller.getForm().getSelectedPort()))
		{
			selectedPort = controller.getForm().getSelectedPort();
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

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		preferenceStore.setValue(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY, isCustomerDisplay);
		preferenceStore.setValue(IVoucherPreferenceConstants.IS_DISPLAY_CENTER, isDisplayCenter);
		preferenceStore.setValue(IVoucherPreferenceConstants.IS_DISPLAY_SCROLL, isDisplayScroll);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_TEST_STATUS, testStatus);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_WELCOME_MESSAGE, welcomeMessage);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE, selectedDisplayType);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT, selectedPort);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER, manufacturer);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL, model);
		preferenceStore.setValue(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER, driver);

	}
/**
 * Methood to update form values from the UI
 */
	protected void updateForm() {
		CustomerDisplayPreferenceForm form = controller.getForm(); 
		CustomerDisplayPreferenceComposite composite = controller.getComposite();
		form.setTestStatus(composite.getTxtTestStatus().getText());
		form.setWelcomeMessage(composite.getTxtWelcomeMessage().getText());
		//TODO Add more fields if required
	}


}
