/*****************************************************************************
 * $Id: AreaPreferenceController.java,v 1.5, 2010-03-03 16:59:55Z, Suganthi, Kaliamoorthy$
 * $Date: 3/3/2010 10:59:55 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Gaming Inc.  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpotui.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.composite.SlipAreaPreferenceComposite;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpotui.AreaPreferenceItem;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.AreaPreferenceForm;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;
import com.ballydev.sds.jackpotui.util.MultiAreaSupportUtil;

/**
 * This is the Area Preference Controller. 
 * 
 * @author dambereen
 * @version $Revision: 6$
 */
public class AreaPreferenceController extends SDSBaseController{

	/**
	 * AreaPreferenceComposite instance
	 */
	private SlipAreaPreferenceComposite areaPreferenceComposite;

	/**
	 * AreaPreferenceForm instance
	 */
	private AreaPreferenceForm areaPreferenceForm;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * AreaPreferenceController constructor
	 * @param parent
	 * @param style
	 * @param alertsPreferenceForm
	 * @param pValidator
	 * @throws Exception
	 */
	public AreaPreferenceController(Composite parent, int style, AreaPreferenceForm alertsPreferenceForm, SDSValidator pValidator) throws Exception {
		super(alertsPreferenceForm, pValidator);
		this.areaPreferenceForm=alertsPreferenceForm;
		try{
			areaPreferenceComposite=new SlipAreaPreferenceComposite(parent,style);
			super.registerEvents(areaPreferenceComposite);
			this.areaPreferenceForm.addPropertyChangeListener(this);
			resetButtons();
					
		if(new SessionUtility().getLoggedInUserID()!=null
					&& MultiAreaSupportUtil.isMultiAreaSupportEnabled()){
				try{
					ArrayList<String> lstAreaAvaiToForm=new ArrayList<String>();
					ArrayList<String> lstAreaSelToForm=new ArrayList<String>();
					List<String> lstAreasGot= MultiAreaSupportUtil.getAreaDetails();
					if(lstAreasGot!=null && lstAreasGot.size()!=0){
						lstAreaAvaiToForm.addAll(new ArrayList<String>(lstAreasGot));
						String alreadySelectedAreas= SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_AREA_PREFERENCE_KEY);
						if(alreadySelectedAreas!=null && !((alreadySelectedAreas.trim()+" ").equalsIgnoreCase(" ")) ){
							//go for check.
							String[] selectedAreasGot=alreadySelectedAreas.split("@");
							if(selectedAreasGot!=null && selectedAreasGot.length!=0){
								for(int i=0;i<lstAreasGot.size();i++){
									String areaFromFramework=lstAreasGot.get(i);
									if(areaFromFramework!=null && !((areaFromFramework.trim()+" ").equalsIgnoreCase(" ")) ){
										boolean isAvailable=false;
										for(int j=0;j<selectedAreasGot.length;j++){
											String areaFromStore = selectedAreasGot[j];
											if(areaFromStore!=null && !((areaFromStore.trim()+" ").equalsIgnoreCase(" ")) ){
												if(areaFromFramework.trim().equalsIgnoreCase(areaFromStore.trim())){
													isAvailable=true;
													break;
												}
											}
										}										
										if(isAvailable){
											lstAreaSelToForm.add(areaFromFramework);
											lstAreaAvaiToForm.remove(areaFromFramework);											
										}										
									}
								}
								this.areaPreferenceForm.setLstAllJackpotAreaAvailable(lstAreaAvaiToForm);
								this.areaPreferenceForm.setLstAllJackpotAreaSelected(lstAreaSelToForm);
								
							}else{
								this.areaPreferenceForm.setLstAllJackpotAreaAvailable(new ArrayList<String>(lstAreasGot));
							}							
						}else{
							//no need check show as it is.
							this.areaPreferenceForm.setLstAllJackpotAreaAvailable(new ArrayList<String>(lstAreasGot));
						}
					}				
					
				}catch (Exception e) {
					e.printStackTrace();
					log.error(e);
				}
			}else{
				AreaPreferenceItem.disableAreaPreferenceControls(this);
			}
			populateScreen(areaPreferenceComposite);
			registerCustomizedListeners(areaPreferenceComposite);
		}catch (Exception e) {		
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e,LabelLoader.getLabelValue(MessageKeyConstants.AREA_DIALOG_BOX_ERROR_PREF));
			log.error("Unable to get the area list"+e);
		}
	}

	@Override
	public Composite getComposite() {		
		return areaPreferenceComposite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		AreaPreferenceMouseListener listener = new AreaPreferenceMouseListener();
		areaPreferenceComposite.getBtnPrefLeft().addMouseListener(listener);
		areaPreferenceComposite.getBtnPrefLeft().addTraverseListener(this);
		areaPreferenceComposite.getBtnPrefRight().addMouseListener(listener);
		areaPreferenceComposite.getBtnPrefRight().addTraverseListener(this);
	}

	/**
	 * @param ctrl
	 */
	public void updateArrowKeysAdvanced(Control ctrl){
		try{
			if(areaPreferenceComposite!=null){
				if(ctrl!=null && ctrl instanceof org.eclipse.swt.widgets.List){
					org.eclipse.swt.widgets.List listGot=(org.eclipse.swt.widgets.List)ctrl;
					if(listGot.getData().equals("lstVwrSelectedAreasName")){
						int[] indices=areaPreferenceComposite.getLstJackpotAreaSelected().getSelectionIndices();
						if(indices.length!=0){
							areaPreferenceComposite.getBtnPrefLeft().setImage(areaPreferenceComposite.getPrevImage());
						}
					}else if(listGot.getData().equals("lstVwrAvailableAreasName")){
						int[] indices=areaPreferenceComposite.getLstJackpotAreaAvailable().getSelectionIndices();
						if(indices.length!=0){
							areaPreferenceComposite.getBtnPrefRight().setImage(areaPreferenceComposite.getNextImage());
						}
					}
				}
			}
		}catch (Exception e) {
			log.error(e);
		}
	}
	
	
	/**
	 * Method to enable / disable the list arrow buttons
	 */
	public void resetButtons(){
		try{
			if(areaPreferenceComposite!=null){
				areaPreferenceComposite.getBtnPrefLeft().setImage(areaPreferenceComposite.getDisablePrevImage());
				areaPreferenceComposite.getBtnPrefRight().setImage(areaPreferenceComposite.getDisableNextImage());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		try{
			if(areaPreferenceForm!=null){		
				Control ctrl = ((Control)e.getSource());
				if((ctrl instanceof CCombo)){					
					populateForm(areaPreferenceComposite);
				}else if(ctrl instanceof org.eclipse.swt.widgets.List){
					if (!Util.isEmpty(SDSApplication.getLoggedInUserID())){
						updateArrowKeysAdvanced(ctrl);
					}
					
				}
			}
		}catch (Exception ex) {
			log.error(ex);
		}
	}


	/**
	 * @return the areaPreferenceComposite
	 */
	public SlipAreaPreferenceComposite getAreaPreferenceComposite() {
		return areaPreferenceComposite;
	}


	/**
	 * @param areaPreferenceComposite the areaPreferenceComposite to set
	 */
	public void setAreaPreferenceComposite(
			SlipAreaPreferenceComposite areaPreferenceComposite) {
		this.areaPreferenceComposite = areaPreferenceComposite;
	}


	/**
	 * @return the areaPreferenceForm
	 */
	public AreaPreferenceForm getAreaPreferenceForm() {
		return areaPreferenceForm;
	}


	/**
	 * @param areaPreferenceForm the areaPreferenceForm to set
	 */
	public void setAreaPreferenceForm(AreaPreferenceForm areaPreferenceForm) {
		this.areaPreferenceForm = areaPreferenceForm;
	}
	
	private class AreaPreferenceMouseListener implements MouseListener{

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDown(MouseEvent e) {
			
			try{
				if(areaPreferenceForm!=null){				
					Control ctrl = ((Control)e.getSource());
					if((ctrl instanceof TSButtonLabel)){
						populateForm(areaPreferenceComposite);
						if (!Util.isEmpty(SDSApplication.getLoggedInUserID())){
							TSButtonLabel button = (TSButtonLabel)ctrl;	
							if((button.getName().trim()).equalsIgnoreCase("BTN_ALERTS_PREF_RIGHT")){
								if(areaPreferenceForm.getLstSelJackpotAreaAvailable()!=null && areaPreferenceForm.getLstSelJackpotAreaAvailable().size()!=0){
									if(areaPreferenceForm.getLstAllJackpotAreaSelected()==null){
										areaPreferenceForm.setLstAllJackpotAreaSelected(new ArrayList<String>());
									}
									areaPreferenceForm.getLstAllJackpotAreaSelected().addAll(areaPreferenceForm.getLstSelJackpotAreaAvailable());
									areaPreferenceForm.getLstAllJackpotAreaAvailable().removeAll(areaPreferenceForm.getLstSelJackpotAreaAvailable());
									populateScreen(areaPreferenceComposite);
									resetButtons();
								}
							}else if((button.getName().trim()).equalsIgnoreCase("BTN_ALERTS_PREF_LEFT")){
								if(areaPreferenceForm.getLstSelJackpotAreaSelected()!=null && areaPreferenceForm.getLstSelJackpotAreaSelected().size()!=0){
									if(areaPreferenceForm.getLstAllJackpotAreaAvailable()==null){
										areaPreferenceForm.setLstAllJackpotAreaAvailable(new ArrayList<String>());
									}
									areaPreferenceForm.getLstAllJackpotAreaAvailable().addAll(areaPreferenceForm.getLstSelJackpotAreaSelected());
									areaPreferenceForm.getLstAllJackpotAreaSelected().removeAll(areaPreferenceForm.getLstSelJackpotAreaSelected());
									populateScreen(areaPreferenceComposite);
									resetButtons();
								}
							}
						}
					}else if((ctrl instanceof CCombo)){					
						populateForm(areaPreferenceComposite);
					}else if(ctrl instanceof org.eclipse.swt.widgets.List){
						updateArrowKeysAdvanced(ctrl);
					}
				}
			}catch (Exception ex) {
				log.error(ex);
			}
		
			
		}

		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
