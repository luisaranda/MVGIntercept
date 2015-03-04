/*****************************************************************************
 * $Id: DialogShell.java,v 1.2.3.0, 2013-10-25 17:50:06Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 12:50:06 PM$
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
import com.ballydev.sds.voucherui.controller.OverrideAuthController;
import com.ballydev.sds.voucherui.controller.RedeemVoucherController;
import com.ballydev.sds.voucherui.form.OverrideAuthForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

public class DialogShell
{
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Shell Instance
	 */
	private Shell dialogShell = null;  //  @jve:decl-index=0:	
	private RedeemVoucherController redeemVoucherController;
	private boolean Multiredeem=false;
	OverrideAuthController overrideAuthController;

	public DialogShell(Shell parent,RedeemVoucherController redeemVoucherController) 
	{
		this.redeemVoucherController = redeemVoucherController;
		createParentShell(parent);
	}

	public DialogShell(Shell parent,RedeemVoucherController redeemVoucherController,boolean multiredeem) 
	{
		this.redeemVoucherController = redeemVoucherController;
		this.Multiredeem=multiredeem;
		createParentShell(parent);
	}
	/**
	 * This method initializes sShell
	 */
	private void createParentShell(Shell parent) 
	{
		dialogShell = new Shell(parent, SWT.APPLICATION_MODAL);

		//dialogShell.setText("Shell");
		try {
			overrideAuthController = new OverrideAuthController(dialogShell,SWT.NONE, new OverrideAuthForm(), new SDSValidator(getClass(),true),redeemVoucherController,this.Multiredeem);			

		} 
		catch (Exception e1) 
		{	
			log.error("Exception while creating OverrideAuthController", e1);
		}	

		dialogShell.setSize(500,300);
		Util.initializeShellBounds(dialogShell);
		dialogShell.layout();
		dialogShell.setBackground(new Color(Display.getCurrent(),255,255,255));
		dialogShell.setForeground(new Color(Display.getCurrent(),200,200,255));
		dialogShell.open();

	}

	public Shell getShell()
	{
		return dialogShell;
	}

	public OverrideAuthController getAuthController()
	{
		return overrideAuthController;
	}
}
