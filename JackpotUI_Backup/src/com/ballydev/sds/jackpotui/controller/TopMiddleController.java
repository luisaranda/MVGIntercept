/*****************************************************************************
 * $Id: TopMiddleController.java,v 1.3, 2010-01-18 13:00:27Z, Suganthi, Kaliamoorthy$
 * $Date: 1/18/2010 7:00:27 AM$
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

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.jackpotui.composite.TopMiddleComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlUtil;
import com.ballydev.sds.jackpotui.util.CallInitialScreenUtil;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Controller class for the TopMiddleComposite
 * @author dambereen
 * @version $Revision: 4$
 */
public class TopMiddleController extends SDSBaseController {

	/**
	 * TopMiddleComposite static Instance
	 */
	public static TopMiddleComposite jackpotMiddleComposite;
	
	/**
	 * currentTopMiddleComposite Instance
	 */
	public static Composite currentComposite;
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);


	/**
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public TopMiddleController(Composite parent,int style) throws Exception {
		super(new SDSForm(), null);
		createJackpotMiddleComposite(parent,style);
		parent.layout();
		super.registerEvents(jackpotMiddleComposite);	
		
		CallInitialScreenUtil callInitialScreenUtil = new CallInitialScreenUtil();
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PENDING_JACKPOT_ALLOWED).equalsIgnoreCase("yes"))
		{
			callInitialScreenUtil.callPendingJPFirstScreen();
			System.out.println("after EmployeeShiftSlotDetailsController");
			MainMenuController.jackpotForm.setSelectedJackpotFunction("pending");
			//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.PENDING_JP_HEADING));
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_ALLOWED).equalsIgnoreCase("yes"))
		{
			System.out.println("Calling callManualJPFirstScreen");
			callInitialScreenUtil.callManualJPFirstScreen();
			MainMenuController.jackpotForm.setSelectedJackpotFunction("manual");
			//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.MANUAL_JP_HEADING));
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_ALLOWED).equalsIgnoreCase("yes"))
		{
			callInitialScreenUtil.callVoidJPFirstScreen();
			MainMenuController.jackpotForm.setSelectedJackpotFunction("void");
			//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.VOID_JP_HEADING));
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.REPRINT_ALLOWED).equalsIgnoreCase("yes"))
		{
			callInitialScreenUtil.callReprintJPFirstScreen();
			MainMenuController.jackpotForm.setSelectedJackpotFunction("reprint");
			//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_JP_HEADING));
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.DISPLAY_ALLOWED).equalsIgnoreCase("yes"))
		{
			callInitialScreenUtil.callDisplayJPFirstScreen();
			MainMenuController.jackpotForm.setSelectedJackpotFunction("display");
			//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_JP_HEADING));
		}else{
			callInitialScreenUtil.callReportJPFirstScreen();
			MainMenuController.jackpotForm.setSelectedJackpotFunction("report");
			//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.REPORT_JP_HEADING));
		}	
	}

	@Override
	public Composite getComposite() {
		
		return null;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		

	}

	/**
	 * Method to create the Jp Middle Composite
	 * @param parent
	 * @param style
	 */
	private void createJackpotMiddleComposite(Composite parent, int style) {		
		jackpotMiddleComposite = new TopMiddleComposite(parent,style);
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
	
	@Override
	public void focusGained(FocusEvent e) {
		super.focusGained(e);
		jackpotMiddleComposite.redraw();
		
	}
	
}
