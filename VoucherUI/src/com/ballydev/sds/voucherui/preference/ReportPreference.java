/*****************************************************************************
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
import com.ballydev.sds.framework.preference.TouchScreenPreferenceItem;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.ReportPreferenceController;
import com.ballydev.sds.voucherui.form.ReportPreferenceForm;

/**
 * @author Nithya kalyani R
 * @version $Revision: 6$ 
 */
public class ReportPreference extends TouchScreenPreferenceItem
{
	/**
	 * Instance of the ReportPreferenceController
	 */
	private ReportPreferenceController controller;

	/**
	 * Constructor
	 */
	public ReportPreference() 	{
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_REPORT_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_REPORT_TITLE));
	}


	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performOk()
	 */
	@Override
	public boolean performOk()	{		
		return super.performOk();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performCancel()
	 */
	@Override
	public boolean performCancel()	{
		return super.performCancel();
	}

	@Override
	public Control createContents(Composite parent) {
		try {
			controller = new ReportPreferenceController(parent,SWT.NONE,new ReportPreferenceForm(),null);
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
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_REPORT_TITLE);
	}


	@Override
	public void performApply() 	{
		try {
			controller.populateForm(controller.getComposite());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProgressIndicatorUtil.openInProgressWindow();
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		if(!Util.isEmpty(controller.getForm().getIsCreated()))	{			
			preferenceStore.setValue(IVoucherPreferenceConstants.REPORT_IS_CREATED, controller.getForm().getIsCreated());
		}

		if(!Util.isEmpty(controller.getForm().getIsRedeemed()))	{
			preferenceStore.setValue(IVoucherPreferenceConstants.REPORT_IS_REDEEMED, controller.getForm().getIsRedeemed());
		}

		if(!Util.isEmpty(controller.getForm().getIsVoided())) {
			preferenceStore.setValue(IVoucherPreferenceConstants.REPORT_IS_VOIDED, controller.getForm().getIsVoided());
		}

		if(!Util.isEmpty(controller.getForm().getIsDisplayAllBatches())) {
			preferenceStore.setValue(IVoucherPreferenceConstants.REPORT_IS_DISPLAY_ALL_BATCHES, controller.getForm().getIsDisplayAllBatches());
		}
		ProgressIndicatorUtil.closeInProgressWindow();
		MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
		
	}
}
