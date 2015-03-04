package com.ballydev.sds.voucherui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.control.SDSButton;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.MultiareaPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.MultiareaPreferenceForm;
import com.ballydev.sds.voucherui.util.ComboLabelValuePair;

public class MultiareaPreferenceController  extends SDSBaseController {

	private MultiareaPreferenceComposite composite;

	private MultiareaPreferenceForm form;

	private String propertyName = "";

	private HashMap<String, String> siteDTOMap = new HashMap<String, String>();

	public MultiareaPreferenceController(Composite parent, int style ,MultiareaPreferenceForm form, SDSValidator pValidator) throws Exception {

		super(form, pValidator);
		composite = new MultiareaPreferenceComposite(parent,style);
		this.form=form;
		super.registerEvents(composite);
		form.addPropertyChangeListener(this);

		if(SDSApplication.getApplicationSessionData()!=null ){
			if( SDSApplication.getApplicationSessionData().getSiteList()!=null){
				int siteListSize = SDSApplication.getApplicationSessionData().getSiteList().size();
				List<SiteDTO> siteDTO = SDSApplication.getApplicationSessionData().getSiteList();

				List<ComboLabelValuePair> lstMultiAreaId = new ArrayList<ComboLabelValuePair>();
				for(int i=0;i<siteListSize; i++){			
					lstMultiAreaId.add(new ComboLabelValuePair((((SiteDTO)siteDTO.get(i)).getId()).toString(),Integer.toString(i)));
					siteDTOMap.put((((SiteDTO)siteDTO.get(i)).getId()).toString(), (((SiteDTO)siteDTO.get(i)).getLongName()));
				}

				List<ComboLabelValuePair> lstSiteName= new ArrayList<ComboLabelValuePair>();
				for(int i=0;i<siteListSize; i++){			
					lstSiteName.add(new ComboLabelValuePair((((SiteDTO)siteDTO.get(i)).getLongName()).toString(),Integer.toString(i)));					
				}

				if(siteDTO!=null && siteDTO.size()>0){
					propertyName = (((SiteDTO)siteDTO.get(0)).getLongName()).toString();
				}

				form.setMultiareaIdList(lstMultiAreaId);	
				composite.getComboMultiareaId().setSelectedIndex(0);
				form.setPropertyName(propertyName);
				form.setSiteNameList(lstSiteName);	
				composite.getComboSiteName().setSelectedIndex(0);

				//form.setIsMultiarea(composite.getBtnMultiarea().getSelection());

				setPreferenceValues();
				populateScreen(composite);
			}
		}

		if(!Util.isEmpty(SDSApplication.getLoggedInUserID())){
			composite.getLblLogOutMsg().setText(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_MULTI_AREA_PREF_VALUE_TO_SET));
			composite.disableControls();
			return;
		}
	}

	public void setSelectedAreaId(String selectedAreaId){
		if(!Util.isEmpty(selectedAreaId)){
			List<ComboLabelValuePair> lstType = form.getMultiareaIdList();

			if(lstType != null && lstType.size() >0)
			{
				for(int i=0; i< lstType.size(); i++)
				{
					ComboLabelValuePair comboLabelValuePair = lstType.get(i);
					if(!Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(selectedAreaId))
					{
						form.setMultiareaId(selectedAreaId);
						composite.getComboMultiareaId().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	public void setSiteName(String siteName){
		if(!Util.isEmpty(siteName)){
			List<ComboLabelValuePair> lstType = form.getSiteNameList();

			if(lstType != null && lstType.size() >0)
			{
				for(int i=0; i< lstType.size(); i++)
				{
					ComboLabelValuePair comboLabelValuePair = lstType.get(i);
					if(!Util.isEmpty(comboLabelValuePair.getValue()) && comboLabelValuePair.getLabel().equalsIgnoreCase(siteName))
					{
						form.setSiteName(siteName);
						composite.getComboSiteName().setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	public void setPreferenceValues(){

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_MULTIAREA))){
			form.setIsMultiarea(preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_MULTIAREA));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.MULTIAREA_ID))){
			form.setMultiareaId(preferenceStore.getString(IVoucherPreferenceConstants.MULTIAREA_ID));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.PROPERTY_ID))){
			form.setPropertyId(preferenceStore.getString(IVoucherPreferenceConstants.PROPERTY_ID));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.PROPERTY_NAME))){
			form.setPropertyName(preferenceStore.getString(IVoucherPreferenceConstants.PROPERTY_NAME));
		}

		if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.SITE_NAME))){
			form.setSiteName(preferenceStore.getString(IVoucherPreferenceConstants.SITE_NAME));
		}

		setSelectedAreaId(preferenceStore.getString(IVoucherPreferenceConstants.MULTIAREA_ID));
		setSiteName(preferenceStore.getString(IVoucherPreferenceConstants.SITE_NAME));
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			populateForm(composite);
		} 
		catch (Exception e1){
			e1.printStackTrace();
		}

		if (e.getSource() instanceof SDSButton) {
			SDSButton button = (SDSButton) e.getSource();
			try {
				populateForm(composite);
			} catch (Exception e1) {					
				e1.printStackTrace();
			}	

			if(button.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_SITENAME)){
				System.out.println("Btn clicked**********" +form.getSiteName());

			}
			else if(button.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_MULTIAREA_CTRL_CMB_MULTIAREA_ID)){
				propertyName = siteDTOMap.get(form.getMultiareaId());
				form.setPropertyName(propertyName);
			}
		}
		try {
			populateScreen(composite);
		} catch (Exception e2) {				
			e2.printStackTrace();
		}
	}

	@Override
	public MultiareaPreferenceComposite getComposite() 
	{
		return composite;
	}

	public MultiareaPreferenceForm getForm() 
	{
		return form;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) 
	{

	}
}
