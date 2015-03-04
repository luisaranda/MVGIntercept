/*****************************************************************************
 * $Id: GeneralAction.java,v 1.0, 2008-04-21 05:45:53Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:45:53 AM$
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
package com.ballydev.sds.slipsui.action;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.ISelectionAction;
import com.ballydev.sds.slipsui.SlipsUIPluginDescriptor;
import com.ballydev.sds.slipsui.composite.TopComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.shell.ParentShell;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Action Class called from the Plugin Descriptor class that inturn calls the
 * ParentShell
 * 
 * @author dambereen
 * 
 */
public class GeneralAction implements ISelectionAction {

	/**
	 * TopComposite Instance
	 */
	public TopComposite top = null;

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.ISelectionAction#performAction()
	 */
	public void performAction() {
		try {
			log.debug("The parent Shell is called");
			new ParentShell();
			//SlipUIPluginDescriptor.getOuter().setEnabled(false);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
