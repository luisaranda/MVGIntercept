/*****************************************************************************
 * $Id: SlipsUIPluginDescriptor.java,v 1.12, 2009-11-24 10:41:31Z, Ambereen Drewitt$
 * $Date: 11/24/2009 4:41:31 AM$
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.ActionType;
import com.ballydev.sds.framework.ISelectionAction;
import com.ballydev.sds.framework.ITSPluginDescriptor;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.PluginDescriptor;
import com.ballydev.sds.framework.SDSTreeNode;
import com.ballydev.sds.framework.ViewDescriptor;
import com.ballydev.sds.framework.action.SDSAction;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.dto.FunctionDTO;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceGroup;
import com.ballydev.sds.framework.siteconfig.SiteConfigurationKeySet;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.slipsui.action.GeneralAction;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ImageConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.PrinterConstants;
import com.ballydev.sds.slipsui.controller.MainMenuController;
import com.ballydev.sds.slipsui.service.SlipsServiceLocator;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Plugin Descriptor class to create the tree nodes, menu items and tool bar
 * action icons
 * 
 * @author dambereen
 * @version $Revision: 13$
 */
public class SlipsUIPluginDescriptor extends PluginDescriptor implements ITSPluginDescriptor {

	/**
	 * SDSAction instance
	 */
	public static SDSAction outer = null;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#getTouchScreenRoot()
	 */
	public SDSAction getTouchScreenRoot() {
		SDSAction touchScreenRootAction = new SDSAction(IAppConstants.MODULE_NAME, ImageDescriptor.createFromFile(
				SlipsUIPluginDescriptor.class,
				ImageConstants.SLIPS_UI_BUTTON_IMG), new GeneralAction());
	
		return touchScreenRootAction;
	}

	/**
	 * @return the outer Gets the SDSAction outer instance
	 */
	public static SDSAction getOuter() {
		return outer;
	}

	/**
	 * @param outer
	 *            Sets the SDSAction outer instance
	 */
	public static void setOuter(SDSAction outer) {
		SlipsUIPluginDescriptor.outer = outer;
	}
	
	@Override
	public List<SDSAction> createToolBarActions() {
		
		return null;
	}

	@Override
	public SDSTreeNode createTree() {
		
		return null;
	}

	@Override
	public List<ViewDescriptor> createViewDescriptors() {
		
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createSignOutAction()
	 */
	@Override
	public ISelectionAction createSignOutAction() {
		
		 super.createSignOutAction();
		 
		 return new ISelectionAction(){
				public void performAction() {
					SlipsServiceLocator.destroySlipService();				
				}			
			};
		
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#createPreferencePages(java.util.List)
	 */
	public TouchScreenPreferenceGroup createPreferencePages(List<FunctionDTO> functionsList) {
		TouchScreenPreferenceGroup group = new TouchScreenPreferenceGroup(LabelLoader.getLabelValue(ILabelConstants.FRAMEWORK_SLIPS_MODULE_NAME));
		List listOfItems = new ArrayList();
		listOfItems.add(new SlipPrinterPreferenceItem());
		listOfItems.add(new AreaPreferenceItem());
		group.setPreferencePageList(listOfItems);
		return group;
	}

/*	public Control createPluginControls(Composite parent) {
		
		MainMenuController controller;
		try {
			controller = new MainMenuController(parent,SWT.NONE);
			return controller.getComposite();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;

	}*/

	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createSiteConfigurationKeys()
	 */
	@Override
	public SiteConfigurationKeySet createSiteConfigurationKeys() {

		Set<String> set = new HashSet<String>();
		set.add(IAppConstants.SLIPS_CATEGORY_NAME);
		SiteConfigurationKeySet siteConfigurationKeySet = new SiteConfigurationKeySet();
		siteConfigurationKeySet.setCategoryKeySet(set);
		return siteConfigurationKeySet;
	}

	public Control createPluginControls(Composite parentHeaderComposite, Composite parentCenterComposite) {
		MainMenuController controller = null;
		try {
			controller = new MainMenuController(parentHeaderComposite,parentCenterComposite,SWT.NONE);
			log.info("Selected printer: "+SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER));
			log.info("Selected AREAS: "+SDSPreferenceStore.getStringStoreValue(IAppConstants.SLIP_AREA_PREFERENCE_KEY));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return controller.getComposite();
	}
	
	@Override
	public void nullServiceObject() {
		SlipsServiceLocator.destroySlipService();		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#isPluginReadyToOpen()
	 */
	public boolean isPluginReadyToOpen() {		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#checkStateBeforeAction(com.ballydev.sds.framework.ActionType)
	 */
	public boolean checkStateBeforeAction(ActionType type) {
		boolean returnValue = true;
		if(type.equals(ActionType.PLUGIN_SELECT)){
			if(SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER)!=null && SDSPreferenceStore.getStringStoreValue(PrinterConstants.PREFERENCE_PRINTER).equalsIgnoreCase("")){
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.SLIP_PRINTER_PREF_NOT_SET_LOG_OUT));
				returnValue = false;
			}
		}
		return returnValue;
	}
	
}
