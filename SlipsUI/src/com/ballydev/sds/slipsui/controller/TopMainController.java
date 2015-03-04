/*****************************************************************************
 * $Id: TopMainController.java,v 1.1, 2008-05-21 14:32:27Z, Ambereen Drewitt$
 * $Date: 5/21/2008 9:32:27 AM$
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.slipsui.composite.MainMenuComposite;
import com.ballydev.sds.slipsui.composite.TopMainComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.util.SlipUILogger;


/**
 * Controller class for the TopComposite
 * @author anantharajr
 *
 */
public class TopMainController extends SDSBaseController {
	
	public static TopMainComposite topMainComposite;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);

	
	public TopMainController(Composite parent, int style) throws Exception {
		super(new SDSForm(), null);		
		createTopMainComposite(parent,style);
		MainMenuComposite.setCurrentComposite(topMainComposite);
		new TopMiddleController((Composite)topMainComposite.getChildren()[1],SWT.NONE);
		//MainMenuComposite.parentComposite.layout();
		super.registerEvents(topMainComposite);
	}	

	/**
	 * Method to create the TopComposite
	 * @param parent
	 * @param style
	 */
	private void createTopMainComposite(Composite parent, int style) {
		topMainComposite = new TopMainComposite(parent,style);
	}
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		
		
	}
}
