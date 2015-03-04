/*****************************************************************************
 * $Id: AreaPreferenceItem.java,v 1.5, 2010-03-09 12:43:01Z, Suganthi, Kaliamoorthy$
 * $Date: 3/9/2010 6:43:01 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.slipsui;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.composite.SlipAreaPreferenceComposite;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceItem;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.LabelKeyConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.controller.AreaPreferenceController;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.form.AreaPreferenceForm;
import com.ballydev.sds.slipsui.util.MultiAreaSupportUtil;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * AreaPreferenceItem for the Slip Area Preferences
 * @author dambereen
 * @version $Revision: 6$
 *
 */
public class AreaPreferenceItem extends TouchScreenPreferenceItem{

	/**
	 * SlipAreaPreferenceComposite instance
	 */
	private SlipAreaPreferenceComposite areaComposite = null;
	
	/**
	 * AreaPreferenceController instance
	 */
	private AreaPreferenceController areaPrefController = null;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#checkState()
	 */
	@Override
	public void checkState() {
		
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(Composite parent) {
		ProgressIndicatorUtil.openInProgressWindow();
		try {
			setPreferenceStore(PlatformUI.getPreferenceStore());
			areaPrefController = new AreaPreferenceController(parent,SWT.NONE,new AreaPreferenceForm(),null);
			if (new SessionUtility().getLoggedInUserID()==null) {
				disableAreaPreferenceControls(areaPrefController);
			}
			areaComposite = (SlipAreaPreferenceComposite) areaPrefController.getComposite();
		} catch (Exception e) {
			SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
			handler.handleException(e,LabelLoader.getLabelValue(MessageKeyConstants.AREA_DIALOG_BOX_ERROR_PREF));
			log.error("Exception in createContents of AreaPreferenceItem"+e);
		}
		finally
		{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return areaComposite;
	}

	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#getName()
	 */
	@Override
	public String getName() {		
		return LabelLoader.getLabelValue(LabelKeyConstants.AREA_PREF);
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.preference.TouchScreenPreferenceItem#performApply()
	 */
	@Override
	public void performApply() {
		ProgressIndicatorUtil.openInProgressWindow();
			try {		
				
				log.debug("Inside performApply()");
				if(new SessionUtility().getLoggedInUserID()!=null
						&& MultiAreaSupportUtil.isMultiAreaSupportEnabled()){
					ArrayList<String> lstAllAreasSelected =	areaPrefController.getAreaPreferenceForm().getLstAllJackpotAreaSelected();
					if(lstAllAreasSelected!=null && lstAllAreasSelected.size()!=0){	
						log.debug("lstAllAreasSelected.size(): "+lstAllAreasSelected.size());
						StringBuffer areaAppended=new StringBuffer();
						for(int i=0;i<lstAllAreasSelected.size();i++){
							String slipAreaGot=lstAllAreasSelected.get(i);
							if(slipAreaGot!=null && !((slipAreaGot.trim()+" ").equalsIgnoreCase(" ")) ){
								areaAppended.append(slipAreaGot);
								areaAppended.append("@");
							}
						}
						String areaToPutInStore = areaAppended.toString();
						log.debug("areaToPutInStore: "+areaToPutInStore);
						if(areaToPutInStore!=null && !((areaToPutInStore.trim()+" ").equalsIgnoreCase(" ")) ){
							log.debug("Setting SDSPreferenceStore.setStoreValue(");
							SDSPreferenceStore.setStoreValue(IAppConstants.SLIP_AREA_PREFERENCE_KEY, areaToPutInStore);
						}
					}else{
						log.debug("Setting SDSPreferenceStore.setStoreValue( in else");
						SDSPreferenceStore.setStoreValue(IAppConstants.SLIP_AREA_PREFERENCE_KEY, " ");
					}
				}
				log.info("Pref Store: "+SDSPreferenceStore.getStringStoreValue(IAppConstants.SLIP_AREA_PREFERENCE_KEY));
			} catch (Exception e) {		
				SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
				handler.handleException(e,LabelLoader.getLabelValue(MessageKeyConstants.AREA_DIALOG_BOX_ERROR_PREF));
				log.error("Exception in performApply() of AreaPreferenceItem"+e);
				return;
			}
			finally
			{
				ProgressIndicatorUtil.closeInProgressWindow();
			}
	}
	
	
	/** 
	 * Method to enable / disable the controls after logging in
	 */
	@Override
	public boolean isEnabled() {
		log.info("new SessionUtility().getLoggedInUserID() in isEnabled: "+new SessionUtility().getLoggedInUserID());
		if(!(Util.isEmpty(SDSApplication.getLoggedInUserID()))
				&& MultiAreaSupportUtil.isMultiAreaSupportEnabled()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Method to disableAreaPreferenceControls
	 * @param areaPrefController
	 */
	public static void disableAreaPreferenceControls(AreaPreferenceController areaPrefController){
		
		areaPrefController.getAreaPreferenceComposite().getBtnPrefLeft().setImage(((SlipAreaPreferenceComposite)areaPrefController.getComposite()).getDisablePrevImage());
		areaPrefController.getAreaPreferenceComposite().getBtnPrefRight().setImage(((SlipAreaPreferenceComposite)areaPrefController.getComposite()).getDisableNextImage());
		areaPrefController.getAreaPreferenceComposite().getLstJackpotAreaAvailable().setEnabled(false);	
		areaPrefController.getAreaPreferenceComposite().getLstJackpotAreaAvailable().setBackground(SDSControlFactory.getDisabledBackGround());
		areaPrefController.getAreaPreferenceComposite().getLstJackpotAreaSelected().setEnabled(false);
		areaPrefController.getAreaPreferenceComposite().getLstJackpotAreaSelected().setBackground(SDSControlFactory.getDisabledBackGround());
		areaPrefController.getAreaPreferenceForm().setLstAllJackpotAreaAvailable(new ArrayList<String>());
		areaPrefController.getAreaPreferenceForm().setLstAllJackpotAreaSelected(new ArrayList<String>());
		areaPrefController.getAreaPreferenceForm().setLstSelJackpotAreaAvailable(new ArrayList<String>());
		areaPrefController.getAreaPreferenceForm().setLstSelJackpotAreaSelected(new ArrayList<String>());	
		
	}
	
	
}
