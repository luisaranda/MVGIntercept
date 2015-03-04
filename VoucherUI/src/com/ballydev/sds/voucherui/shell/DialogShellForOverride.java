/*****************************************************************************
 * $Id: DialogShellForOverride.java,v 1.0, 2010-06-16 14:53:48Z, Verma, Nitin Kumar$
 * $Date: 6/16/2010 9:53:48 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.shell;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.controller.OverrideController;
import com.ballydev.sds.voucherui.controller.OverrideUidPwdController;
import com.ballydev.sds.voucherui.form.OverrideAuthForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * @author VNitinkumar
 */
public class DialogShellForOverride {

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Shell Instance
	 */
	private Shell dialogShell = null;

	OverrideUidPwdController overrideUidPwdController;

	public DialogShellForOverride(Shell parent, OverrideController controller) {
		createParentShell(parent, controller);
	}

	/**
	 * This method initializes sShell
	 */
	private void createParentShell(Shell parent, OverrideController controller) {
		dialogShell = new Shell(parent, SWT.APPLICATION_MODAL);

		//dialogShell.setText("Shell");
		try {
			overrideUidPwdController = new OverrideUidPwdController(dialogShell, SWT.NONE, new OverrideAuthForm(), new SDSValidator(getClass(),true), controller);

		} 
		catch (Exception e1) {	
			log.error("Exception while creating OverrideAuthController", e1);
		}	

		dialogShell.setSize(500,300);
		Util.initializeShellBounds(dialogShell);
		dialogShell.layout();
		dialogShell.setBackground(new Color(Display.getCurrent(),255,255,255));
		dialogShell.setForeground(new Color(Display.getCurrent(),200,200,255));
		dialogShell.open();

	}

	public Shell getShell()	{
		return dialogShell;
	}

	public OverrideUidPwdController getOverrideUidPwdController()	{
		return overrideUidPwdController;
	}

}
