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
import com.ballydev.sds.voucher.enumconstants.VoucherAssetTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.AppContextValues;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.LocationPreferenceController;
import com.ballydev.sds.voucherui.form.LocationPreferenceForm;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.util.VoucherUIValidator;
import com.ballydev.sds.voucherui.util.VoucherUtil;

public class LocationPreference extends TouchScreenPreferenceItem {

	private LocationPreferenceController controller;


	public LocationPreference() {
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_LOCATION_DESC));
		setTitle(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_LOCATION_TITLE));
	}

	@Override
	public Control createContents(Composite parent){
		try {
			controller = new LocationPreferenceController(parent,SWT.NONE,new LocationPreferenceForm(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controller.getComposite();
	}

	@Override
	public boolean performOk(){
		return super.performOk();
	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	public void checkState() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		return LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PLUGIN_PREFERENCE_LOCATION_TITLE);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performApply()
	 */
	@Override
	public void performApply() {
		if( !AppContextValues.getInstance().isVoucherOpen() && !Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
			setValues();
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performDefaults()
	 */
	@Override
	public void performDefaults() {
		super.performDefaults();
		if( !AppContextValues.getInstance().isVoucherOpen() ) {
			String locationId = "";
			Boolean isCashier = true;
			Boolean isAuditor = false;
			if( Util.isEmpty(SDSApplication.getLoggedInUserID()) ) {
				controller.getForm().setIsAuditor(isAuditor);
				controller.getForm().setIsCashier(isCashier);
				controller.getComposite().setCashier(isCashier);
				controller.getComposite().getRadioButtonControl().setSelectedButton(controller.getComposite().getRadioImgLblCashier());
				controller.getForm().setLocationId(locationId);
				try {
					controller.populateScreen(controller.getComposite());
				} catch (Exception e) {
					e.printStackTrace();
				}
				controller.getComposite().getTxtLocationId().setFocus();
			}
		}
	}

	@Override
	public boolean isEnabled() {
		if( AppContextValues.getInstance().isVoucherOpen() ) {
			return false;
		}
		return true;
	}
//	public boolean isEnabled() {		
//		if (!Util.isEmpty(SDSApplication.getLoggedInUserID())) {
//			return false;
//		}		
//		return true;
//	}
//

	/**
	 * This method sets the values set
	 * in the preference page to the preference store
	 */
	private void setValues(){
		if( !AppContextValues.getInstance().isVoucherOpen() ) {
			try {
				controller.populateForm(controller.getComposite());
			} catch (Exception e1) {
				e1.printStackTrace();
			}


			String locationId = "";
			Boolean isCashier = false;
			Boolean isAuditor = false;	

			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

			if( !Util.isEmpty(controller.getForm().getIsAuditor()) ) {
				isAuditor = controller.getForm().getIsAuditor();
			}

			if( !Util.isEmpty(controller.getForm().getIsCashier()) ) {
				isCashier = controller.getForm().getIsCashier();
			}


			if( SDSApplication.getLoggedInUserID() != null && SDSApplication.getLoggedInUserID().trim().length() > 0) {
				if( !Util.isEmpty(controller.getForm().getLocationId()) ) {
					locationId = controller.getForm().getLocationId().toUpperCase();
					
					if( locationId.trim() != null && locationId.trim().length() != 0 ) {
						try {
							if( !VoucherUIValidator.validateAlphaNumericWithSpaces(locationId) ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.ASSET_FIELD_ONLY_ALPHA_NUMERIC));
								controller.getComposite().getTxtLocationId().selectAll();
								controller.getComposite().getTxtLocationId().setFocus();
								return;
							}
							int siteId = (int)SDSApplication.getUserDetails().getSiteId();
							VoucherAssetTypeEnum assetTypeEnum =  ServiceCall.getInstance().isAssetAvailable(locationId,siteId);

							if( assetTypeEnum == null || assetTypeEnum.equals(VoucherAssetTypeEnum.INVALID) || !(assetTypeEnum.equals(VoucherAssetTypeEnum.CASHIER_WORK_STATION))){
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_INVALID_CASHIER_LOCATION));
								controller.getComposite().getTxtLocationId().selectAll();
								controller.getComposite().getTxtLocationId().setFocus();
							}
							else
								MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_SUCCESSFULLY));
						} catch(VoucherEngineServiceException ex) {
							String exceptionMsg = null;
							exceptionMsg = VoucherUtil.getExMessageForDisplay(ex);
							
							if( exceptionMsg != null && exceptionMsg.trim().length() > 0 ) {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getExMessageForDisplay(ex));
							} else {
								MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_ASSET_EXPTN));
							}
							controller.getComposite().getTxtLocationId().selectAll();
							controller.getComposite().getTxtLocationId().setFocus();
						
						} catch (Exception e) {
							e.printStackTrace();
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_PREFERENCES_UPDATED_FAILED));
						}
					}
				} else {
					System.out.println("Calling the msg.........");
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_CASHIER_LOCATION_NOT_SET));
					controller.getComposite().getTxtLocationId().selectAll();
					controller.getComposite().getTxtLocationId().setFocus();
				}
			}
			preferenceStore.setValue(IVoucherPreferenceConstants.LOCATION_IS_AUDITOR, isAuditor);
			preferenceStore.setValue(IVoucherPreferenceConstants.LOCATION_IS_CASHIER, isCashier);
			preferenceStore.setValue(IVoucherPreferenceConstants.LOCATION_ID, locationId);

		}
	}
}
