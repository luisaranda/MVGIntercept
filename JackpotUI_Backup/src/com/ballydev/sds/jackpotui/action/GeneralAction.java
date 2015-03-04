/*****************************************************************************
 * $Id: GeneralAction.java,v 1.1, 2008-08-08 08:58:14Z, Ambereen Drewitt$
 * $Date: 8/8/2008 3:58:14 AM$
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
package com.ballydev.sds.jackpotui.action;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.ISelectionAction;
import com.ballydev.sds.jackpotui.composite.TopComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.shell.ParentShell;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Action Class called from the Plugin Descriptor class that inturn calls the
 * ParentShell
 * 
 * @author dambereen
 * @version $Revision: 2$
 */
public class GeneralAction implements ISelectionAction {

	/**
	 * TopComposite Instance
	 */
	public TopComposite top = null;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.ISelectionAction#performAction()
	 */
	public void performAction() {
		try {
			log.debug("The parent Shell is called");
			new ParentShell();
			//JackpotUIPluginDescriptor.getOuter().setEnabled(false);

		} catch (Exception e) {
			log.error(e);
		}
	}
}
