/*****************************************************************************
 * $Id: MainMenuController.java,v 1.8, 2009-11-16 10:41:15Z, Ambereen Drewitt$
 * $Date: 11/16/2009 4:41:15 AM$
 * $Log$
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
package com.ballydev.sds.slipsui.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.commons.util.InetUtil;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.slipsui.composite.MainMenuComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.exception.SlipUIExceptionHandler;
import com.ballydev.sds.slipsui.form.SlipInfoForm;
import com.ballydev.sds.slipsui.print.SlipUtil;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Controller class of the Parent Composite
 * 
 * @author dambereen
 * 
 */
public class MainMenuController extends SDSBaseController {
	
	/**
	 * Slip form instance that will contain all the values entered for each process that is used
	 */
	public static SlipInfoForm slipForm = new SlipInfoForm();
	
	/**
	 * slipsSiteConfigParams hashmap instance
	 */
	//public static Map<String ,String> slipsSiteConfigParams = new HashMap<String, String>();
	
	
	/**
	 * Hash map that first gets the site param keys from SDSFramework
	 */
	public static Map<String, String> initSlipsSiteConfigParams = new HashMap<String, String>();
	
	/**
	 * Hash map that gets the site param keys that are trimed from initSlipsSiteConfigParams map
	 */
	public static Map<String, String> slipsSiteConfigParams = new HashMap<String, String>();
	
	/**
	 * HashMap that contains the user functions
	 */
	public static HashMap<String, String> slipsUserFunctions = new HashMap<String, String>();
	
	/**
	 * ParentComposite Instance
	 */
	private MainMenuComposite mainMenuComposite;

	/**
	 * ParentController Instance
	 */
	private static MainMenuController parentController;

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * BaseController constructor
	 * 
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public MainMenuController(Composite header, Composite middleComposite, int style) throws Exception {
		super(new SDSForm(), null);		
		MainMenuController.slipForm.setActorLogin(SDSApplication.getLoggedInUserID());
		log.info("Actor login: "+SDSApplication.getLoggedInUserID());
		// reset the site id / no /long name and then set the new values
		MainMenuController.slipForm.setSiteId(0);
		MainMenuController.slipForm.setSiteNo("");
		MainMenuController.slipForm.setSiteLongName("");
		getSiteId();
		SlipUtil.getSlipSchema();
		log.info("****************************************");
		log.info("TOUCH SCREEN TERMINAL: "+InetUtil.getHostName());
		MainMenuController.slipForm.setAssetConfigNumberUsed(InetUtil.getHostName());
		log.info("****************************************");
		boolean success = loadSiteConfigParameters();
		for(String key : initSlipsSiteConfigParams.keySet()) {
			 slipsSiteConfigParams.put(key.trim(), initSlipsSiteConfigParams.get(key));
			 log.info("Key is : "+key+" and the value is : "+slipsSiteConfigParams.get(key));
		 }
		System.out.println("Site Param Map size: "+slipsSiteConfigParams.size());
		if(success)
		{
			/*createParentComposite(parent, style);
			 new TopController(mainMenuComposite,style);
			 super.registerEvents(mainMenuComposite);
			 mainMenuComposite.layout();*/
			try {

				new TopHeaderController(header, style);
				header.layout();
				new TopMiddleController(middleComposite, style);
				middleComposite.layout();
				header.getParent().layout();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		else
		{
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.ERROR_GETTING_SITE_PARAMETERS));
/*			if(parent.getShell() !=null && !parent.getShell().isDisposed())
			{
				parent.getShell().dispose();
			}*/
		}		
	}
	
	/**
	 * Method to get the Inastance of this class
	 * 
	 * @param parent
	 * @param style
	 * @return
	 * @throws Exception
	 */
	public static MainMenuController getInstance(Composite header,Composite middleComposite, int style)
			throws Exception {
		if (parentController == null) {
			parentController = new MainMenuController(header,middleComposite, style);
		}
		return parentController;
	}

	/**
	 * Method to create the ParentComposite
	 * 
	 * @param parent
	 * @param style
	 */
/*	private void createParentComposite(Composite parent, int style) {
		mainMenuComposite = new MainMenuComposite(parent, SWT.NONE);
		mainMenuComposite.layout(true);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		

	}

	/**
	 * ********************************* Try to use this
	 * 
	 * @param s
	 */
	public void callTopController(int s) {
		try {
			new TopController(mainMenuComposite, s);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
		
	/**
	 * Method to load the Site Parameters
	 * @return
	 */
	private boolean loadSiteConfigParameters()
	{
		SessionUtility sessionUtility = new SessionUtility();		
		try {
			initSlipsSiteConfigParams = sessionUtility.getSiteConfigurationValuesMap();
			 if(initSlipsSiteConfigParams.size()==0)
			 {
				 MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.ERROR_GETTING_SITE_PARAMETERS));
				 return false;
			 }
			 log.info("Site Currency Symbol: " +sessionUtility.getSiteConfigurationValue(ISiteConfigurationConstants.CURRENCY_SYMBOL));
			MainMenuController.slipForm.setSiteCurrencySymbol(sessionUtility.getSiteConfigurationValue(ISiteConfigurationConstants.CURRENCY_SYMBOL));
			 /*for(String key : slipsSiteConfigParams.keySet()) {
				 System.out.println("Key is : "+key+" and the value is : "+slipsSiteConfigParams.get(key));
			 }
			 System.out.println("slipsSiteConfigParams.size() : "+slipsSiteConfigParams.size());*/
			 return true;
		} catch (RuntimeException e) {
			
				return false;
		}	
	}
	
	/**
	 * Method to get site id from the Framework's Session 
	 */
	private void getSiteId()
	{
		try {
			SessionUtility sessionUtility = new SessionUtility();
			SiteDTO siteDTO = sessionUtility.getSiteDetails();
			if(siteDTO!=null)
			{
				log.info(" Site Id : "+siteDTO.getId());
				log.info(" Site Number: "+siteDTO.getNumber());
				MainMenuController.slipForm.setSiteId(siteDTO.getId());
				MainMenuController.slipForm.setSiteNo(siteDTO.getNumber());
				MainMenuController.slipForm.setSiteLongName(siteDTO.getLongName());
				log.info("********************************************");
				log.info("Site Short Name: "+siteDTO.getShortName());
				log.info("********************************************");			
			}
			else
			{
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK));
				log.error("Exception getting the site id from framework" );
			}
		} catch (Exception e) {
			if(e instanceof RuntimeException && e.getMessage()!=null && ((e.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_UNREACH_SERVICE_MSG)) ||
                    (e.getMessage().startsWith(IAppConstants.RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG)))){
				
				SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
				handler.handleException(e, ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
			}else{
				SlipUIExceptionHandler handler = new SlipUIExceptionHandler();
				handler.handleException(e, MessageKeyConstants.EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK);
			}
			log.error("Exception getting the site id from framework" );
			return;
		}
	}

}
