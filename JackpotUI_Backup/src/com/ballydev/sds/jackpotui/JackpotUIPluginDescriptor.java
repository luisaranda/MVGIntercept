/*****************************************************************************
 * $Id: JackpotUIPluginDescriptor.java,v 1.12, 2009-11-24 10:41:31Z, Ambereen Drewitt$
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
package com.ballydev.sds.jackpotui;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceGroup;
import com.ballydev.sds.framework.siteconfig.SiteConfigurationKeySet;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.action.GeneralAction;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.service.JackpotServiceLocator;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;


/**
 * Plugin Descriptor class to create the tree nodes, menu items and tool bar
 * action icons
 * 
 * @author dambereen
 * @version $Revision: 13$
 */
public class JackpotUIPluginDescriptor extends PluginDescriptor implements ITSPluginDescriptor {

	/**
	 * SDSAction instance
	 */
	public static SDSAction outer = null;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.PluginDescriptor#createTree()
	 */
	@Override
	public SDSTreeNode createTree() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.PluginDescriptor#createViewDescriptors()
	 */
	@Override
	public List<ViewDescriptor> createViewDescriptors() {
		List<ViewDescriptor> jackpotViews = new ArrayList<ViewDescriptor>();

		/*
		 * ViewDescriptor mainView1 = new
		 * ViewDescriptor(JackpotView.VIEW_ID,ILayoutConstants.LAYOUT_ID_CENTER);
		 * jackpotViews.add(mainView1);
		 * 
		 * ViewDescriptor mainView2 = new
		 * ViewDescriptor(HelpView.VIEW_ID,ILayoutConstants.LAYOUT_ID_LEFTBOTTOM);
		 * jackpotViews.add(mainView2);
		 * 
		 * ViewDescriptor mainView3 = new
		 * ViewDescriptor(MenuView.VIEW_ID,ILayoutConstants.LAYOUT_ID_LEFTTOP);
		 * jackpotViews.add(mainView3);
		 */

		return jackpotViews;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.PluginDescriptor#createToolBarActions()
	 *      Method to create the toolbar icon for the Plugin
	 */
	@Override
	public List<SDSAction> createToolBarActions() {

		/*outer = new SDSAction("Jackpot", ImageDescriptor.createFromFile(
				JackpotUIPluginDescriptor.class,
				ImageConstants.JACKPOT_UI_TOOLBAR_ICON), new GeneralAction());

		List<SDSAction> outerActions = new ArrayList<SDSAction>();
		outerActions.add(outer);

		return outerActions;*/
		return null;
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
		JackpotUIPluginDescriptor.outer = outer;
	}

	public SDSAction getTouchScreenRoot() {
		String imgJackpotBtn = null;
		if(Util.isSmallerResolution()) {
			imgJackpotBtn = ImageConstants.S_JACKPOT_UI_BUTTON_IMG;
		}
		else {
			imgJackpotBtn = ImageConstants.JACKPOT_UI_BUTTON_IMG;
		}
		SDSAction touchScreenRootAction = new SDSAction(IAppConstants.MODULE_NAME, ImageDescriptor.createFromFile(
				JackpotUIPluginDescriptor.class,
				imgJackpotBtn), new GeneralAction());
		
		return touchScreenRootAction;
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createSignOutAction()
	 */
	@Override
	public ISelectionAction createSignOutAction() {
		super.createSignOutAction();
		return new ISelectionAction(){
			public void performAction() {
				JackpotServiceLocator.nullJackpotService();				
			}			
		};
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#createPreferencePages(java.util.List)
	 */
	public TouchScreenPreferenceGroup createPreferencePages(List<com.ballydev.sds.framework.dto.FunctionDTO> functionsList) {
		TouchScreenPreferenceGroup group = new TouchScreenPreferenceGroup(LabelLoader.getLabelValue(ILabelConstants.FRAMEWORK_JACKPOT_MODULE_NAME));
		List listOfItems = new ArrayList();
		listOfItems.add(new PrinterPreferenceItem());
		listOfItems.add(new AreaPreferenceItem());
		group.setPreferencePageList(listOfItems);
		return group;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#createPluginControls(org.eclipse.swt.widgets.Composite)
	 */
	/*public Control createPluginControls(Composite parent) {
		
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
		set.add(IAppConstants.JACKPOT_CATAGORY_NAME);
		SiteConfigurationKeySet siteConfigurationKeySet = new SiteConfigurationKeySet();
		siteConfigurationKeySet.setCategoryKeySet(set);
		return siteConfigurationKeySet;
	}

	public Control createPluginControls(Composite parentHeaderComposite, Composite parentCenterComposite) {
		MainMenuController controller = null;
		
		try {			
			log.info("*** createPluginControls is called");
			MainMenuController.jackptotUserFunctions = new HashMap<String, String>();
			controller = new MainMenuController(parentHeaderComposite,parentCenterComposite, SWT.NONE);
			log.info("Selected printer: "+SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY));
			log.info("Selected AREAS: "+SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_AREA_PREFERENCE_KEY));
						
		}catch (Exception e) {			
			log.error(e);
		}
		return controller.getComposite();
	}

	public Control createPluginControls(Composite parent) {
		
		return null;
	}
	
	@Override
	public void nullServiceObject() {
		JackpotServiceLocator.nullJackpotService();
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
			if(SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY)!=null && SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_PRINTER_PREFERENCE_KEY).equalsIgnoreCase("")){
				MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(MessageKeyConstants.JP_PRINTER_PREF_NOT_SET_LOG_OUT));
				returnValue = false;
			}
		}
		return returnValue;
	}

	
	
}
