/*****************************************************************************
 * $Id: TopMiddleController.java,v 1.4, 2010-01-28 09:56:36Z, Ambereen Drewitt$
 * $Date: 1/28/2010 3:56:36 AM$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.slipsui.composite.TopMiddleComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.UserFunctionsConstants;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Controller class for the TopMiddleComposite
 * @author dambereen
 *
 */
public class TopMiddleController extends SDSBaseController {

	/**
	 * TopMiddleComposite static Instance
	 */
	public static TopMiddleComposite slipMiddleComposite;
	
	/**
	 * currentTopMiddleComposite Instance
	 */
	public static Composite currentComposite;
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public TopMiddleController(Composite parent,int style) throws Exception {
		super(new SDSForm(), null);
		createSlipMiddleComposite(parent,style);
		//parent.layout();
		super.registerEvents(slipMiddleComposite);		
		CallInitialScreen firstScreen = new CallInitialScreen();
		if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SHOULD_BEEF_BE_ALLOWED).equalsIgnoreCase("yes")) {
			firstScreen.callBeefScreen();
		} else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_BE_ALLOWED).equalsIgnoreCase("yes")) {
			firstScreen.callVoidScreen();
		} else if(MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_BE_ALLOWED).equalsIgnoreCase("yes")) {
			firstScreen.callReprintScreen();
		}  else if(UserFunctionsConstants.REPORT_ALLOWED) {
			firstScreen.callReportScreen();
		}		
	}

	@Override
	public Composite getComposite() {
		
		return null;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		

	}

	private void createSlipMiddleComposite(Composite parent, int style) {
		slipMiddleComposite = new TopMiddleComposite(parent,style);
	}

	/**
	 * @return the currentComposite
	 */
	public static Composite getCurrentComposite() {
		return currentComposite;
	}

	/**
	 * @param currentComposite the currentComposite to set
	 */
	public static void setCurrentComposite(Composite currentComposite) {
		TopMiddleController.currentComposite = currentComposite;
	}
	
	
}
