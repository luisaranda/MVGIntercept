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
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.MultiareaPreferenceController;
import com.ballydev.sds.voucherui.form.MultiareaPreferenceForm;

public class MultiareaPreference extends TouchScreenPreferenceItem {

	private MultiareaPreferenceController  multiareaPreferenceController;

	public MultiareaPreference() 
	{
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_MULTIAREA_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_MULTIAREA_TITLE));
	}

	@Override
	public Control createContents(Composite parent) 
	{
		try 
		{
			multiareaPreferenceController = new MultiareaPreferenceController(parent,SWT.NONE,new MultiareaPreferenceForm(),null);
		}catch (Exception e) {			
			e.printStackTrace();
		}
		return multiareaPreferenceController.getComposite();
	}

	@Override
	public boolean performOk()	{

		return super.performOk();
	}

	@Override
	public boolean isEnabled() {		
		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
			return false;
		}
		return true;
	}

	public void init(IWorkbench workbench) 
	{
	}

	@Override
	public boolean performCancel() 
	{
		return super.performCancel();
	}

	@Override
	public void checkState() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_MULTIAREA_TITLE);
	}

	@Override
	public void performApply()	{
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			setValues();
			MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
		}

	}

	public void setValues(){
		String propertyName = "";
		String propertyId = "";
		Boolean isMultiarea = false;
		String multiareaId = "";
		String siteName ="";

		if(!Util.isEmpty(multiareaPreferenceController.getForm().getIsMultiarea()))
		{
			isMultiarea = multiareaPreferenceController.getForm().getIsMultiarea();
		}

		if(!Util.isEmpty(multiareaPreferenceController.getForm().getSiteName()))
		{
			siteName = multiareaPreferenceController.getForm().getSiteName();
		}

		if(!Util.isEmpty(multiareaPreferenceController.getForm().getMultiareaId()))
		{
			multiareaId = multiareaPreferenceController.getForm().getMultiareaId();
		}

		if(!Util.isEmpty(multiareaPreferenceController.getForm().getPropertyId()))
		{
			propertyId = multiareaPreferenceController.getForm().getPropertyId();
		}

		if(!Util.isEmpty(multiareaPreferenceController.getForm().getPropertyName()))
		{
			propertyName = multiareaPreferenceController.getForm().getPropertyName();
		}

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		preferenceStore.setValue(IVoucherPreferenceConstants.IS_MULTIAREA, isMultiarea);
		preferenceStore.setValue(IVoucherPreferenceConstants.MULTIAREA_ID, multiareaId);
		preferenceStore.setValue(IVoucherPreferenceConstants.PROPERTY_ID, propertyId);
		preferenceStore.setValue(IVoucherPreferenceConstants.PROPERTY_NAME, propertyName);
		preferenceStore.setValue(IVoucherPreferenceConstants.SITE_NAME, siteName);		

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {		
		super.performDefaults();
		if(Util.isEmpty(SDSApplication.getLoggedInUserID())){
			multiareaPreferenceController.getComposite().getComboMultiareaId().setSelectedIndex(0);
			multiareaPreferenceController.getForm().setMultiareaId(multiareaPreferenceController.getForm().getMultiareaIdList().get(0).getLabel());
			try {
				multiareaPreferenceController.populateScreen(multiareaPreferenceController.getComposite());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
