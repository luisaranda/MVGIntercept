/*****************************************************************************
 * $Id: TopController.java,v 1.0, 2008-04-03 15:54:19Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:54:19 AM$
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.controller.TouchScreenFooterController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.jackpotui.composite.MainMenuComposite;
import com.ballydev.sds.jackpotui.composite.TopComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;


/**
 * Controller class for the TopComposite
 * @author dambereen
 * @version $Revision: 1$
 */
public class TopController extends SDSBaseController {
	
	/**
	 * Static TopComposite instance
	 */
	public static TopComposite topComposite;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	
	/**
	 * TopController constructor 
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public TopController(Composite parent, int style) throws Exception {
		
		super(new SDSForm(), null);		
		createTopComposite(parent,style);
		MainMenuComposite.setCurrentComposite(topComposite);
		super.registerEvents(topComposite);		
		new TopHeaderController(topComposite,SWT.NONE);
		//new TopMiddleController(topComposite,SWT.NONE);
		new TopMainController(topComposite,SWT.NONE);		
		//new TouchScreenFooterController(topComposite, SWT.NONE, new SDSForm(), null, false);
		
		log.info("inside top controller");
		MainMenuComposite.parentComposite.layout();
	}	

	/**
	 * Method to create the TopComposite
	 * @param parent
	 * @param style
	 */
	private void createTopComposite(Composite parent, int style) {
		topComposite = new TopComposite(parent,style);
	}
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		// TODO Auto-generated method stub
		
	}
}
