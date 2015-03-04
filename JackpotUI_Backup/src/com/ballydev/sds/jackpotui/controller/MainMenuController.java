/*****************************************************************************
 * $Id: MainMenuController.java,v 1.12, 2009-12-09 09:16:30Z, Ambereen Drewitt$
 * $Date: 12/9/2009 3:16:30 AM$
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
package com.ballydev.sds.jackpotui.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.commons.util.InetUtil;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.FunctionDTO;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.exception.JackpotUiExceptionHandler;
import com.ballydev.sds.jackpotui.form.JackpotForm;
import com.ballydev.sds.jackpotui.print.SlipUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Controller class of the Parent Composite
 * 
 * @author dambereen
 * @version $Revision: 13$
 */
public class MainMenuController extends SDSBaseController {

	/**
	 * ParentController Instance
	 */
	private static MainMenuController parentController;

	public static JackpotForm jackpotForm = new JackpotForm();

	public static Map<String ,String> jackpotSiteConfigParams = new HashMap<String, String>();

	public static HashMap<String ,String> jackptotUserFunctions = new HashMap<String, String>();

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
	.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * BaseController constructor
	 * 
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public MainMenuController(Composite parent,Composite parentCenterComposite, int style) throws Exception {
		super(new SDSForm(), null);
		/** CALL TO GET THE SITE ID INFO*/
		MainMenuController.jackpotForm.setSiteId(0);
		MainMenuController.jackpotForm.setSiteNo("");
		MainMenuController.jackpotForm.setSiteLongName("");
		getSiteId();
		/**CALL TO GET THE SLIP SCHEMA INFO*/
		SlipUtil.getSlipSchema();
		log.info("****************************************");
		log.info("TOUCH SCREEN TERMINAL: "+InetUtil.getHostName());
		MainMenuController.jackpotForm.setAssetConfigNumberUsed(InetUtil.getHostName());
		log.info("****************************************");

		
		/**CALL TO GET THE SITE CONFIG PARAMETERS INFO*/
		boolean success = loadSiteConfigParamters();
		if(success)
		{
			/**CALL TO GET THE USER FUNCTIONS INFO*/
			getUserFunctions();
			try {
				new TopHeaderController(parent,SWT.NONE);	
				parent.layout();
				new TopMiddleController(parentCenterComposite,SWT.NONE);
				parentCenterComposite.layout();
				parent.getParent().layout();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.ERROR_GETTING_SITE_PARAMETERS));
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
	public static MainMenuController getInstance(Composite parent,Composite parentCenterComposite, int style)
	throws Exception {
		if (parentController == null) {
			parentController = new MainMenuController(parent,parentCenterComposite, style);
		}
		return parentController;
	}

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
	/*public void callTopController(int s) {
		try {
			new TopController(mainMenuComposite, s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	/**
	 * Method to loadSiteConfigParamters
	 * @return boolean
	 */
	private boolean loadSiteConfigParamters()
	{
		SessionUtility sessionUtility = new SessionUtility();
		try {			
			jackpotSiteConfigParams = sessionUtility.getSiteConfigurationValuesMap();
			if(jackpotSiteConfigParams.size()==0)
			{
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.ERROR_GETTING_SITE_PARAMETERS));
				return false;
			}
			log.info("Site Currency Symbol: " +sessionUtility.getSiteConfigurationValue(ISiteConfigConstants.CURRENCY_SYMBOL));
			MainMenuController.jackpotForm.setSiteCurrencySymbol(sessionUtility.getSiteConfigurationValue(ISiteConfigConstants.CURRENCY_SYMBOL));
			return true;
		} catch (Exception e2) {
			JackpotUiExceptionHandler handler = new JackpotUiExceptionHandler();
			handler.handleException(e2,ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR);
			log.error("SERVICE_DOWN",e2);
			e2.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to get the Site id from Framework
	 */
	private void getSiteId()
	{
		try {
			MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(SDSApplication.getLoggedInUserID());
			MainMenuController.jackpotForm.setVoidEmployeeId(SDSApplication.getLoggedInUserID());
			MainMenuController.jackpotForm.setActorLogin(SDSApplication.getLoggedInUserID());
			SessionUtility sessionUtility = new SessionUtility();
			SiteDTO siteDTO = sessionUtility.getSiteDetails();
			if(siteDTO!=null)
			{
				log.info("********************************************");
				log.info(" Site Id : "+siteDTO.getId());
				log.info(" Site Number: "+siteDTO.getNumber());				
				MainMenuController.jackpotForm.setSiteId(siteDTO.getId());
				MainMenuController.jackpotForm.setSiteNo(siteDTO.getNumber());
				MainMenuController.jackpotForm.setSiteLongName(siteDTO.getLongName());
				log.info("Site Short Name: "+siteDTO.getShortName());
				log.info("********************************************");
			}
			else
			{
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK));
			}
		} catch (Exception e) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(MessageKeyConstants.EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK));
			log.error("Exception while getting the Site Id from Framework", e);
			return;
		}
	}


	/**
	 * Method to get the user functions of a logged on user.
	 */
	private void getUserFunctions()
	{
		try {
			SessionUtility sessionUtility = new SessionUtility();

			List<FunctionDTO> functionList = sessionUtility.getUserDetails().getFunctionList();
			MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(sessionUtility.getUserDetails().getUserName());
			MainMenuController.jackpotForm.setVoidEmployeeId(sessionUtility.getUserDetails().getUserName());
			for(FunctionDTO functionList1:functionList) {
				jackptotUserFunctions.put(functionList1.getFunctionName(),functionList1.getFunctionName());
				log.debug("Func Name: "+functionList1.getFunctionName());
			}
			if(sessionUtility.getUserDetails().getParameterList()!=null && sessionUtility.getUserDetails().getParameterList().size()>0){
				for(int i=0;i<sessionUtility.getUserDetails().getParameterList().size();i++)
				{
					log.debug("Param name "+sessionUtility.getUserDetails().getParameterList().get(i).getParameterName());
					log.debug("Param value "+sessionUtility.getUserDetails().getParameterList().get(i).getParameterValue());
				}
			}
		} catch (Exception e) {
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(MessageKeyConstants.ERROR_GETTING_SITE_PARAMETERS);			
			log.error(e);
		}
	}
}
