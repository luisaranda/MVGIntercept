/*****************************************************************************
 * $Id: DialogShellForPrint.java,v 1.0, 2009-01-20 07:57:08Z, Nithyakalyani, Raman$
 * $Date: 1/20/2009 1:57:08 AM$
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
import com.ballydev.sds.voucherui.controller.OverridePrintController;
import com.ballydev.sds.voucherui.controller.PrintVoucherController;
import com.ballydev.sds.voucherui.form.OverridePrintForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

public class DialogShellForPrint {
	
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * Shell Instance
	 */
	private Shell dialogShell = null;  //  @jve:decl-index=0:	

	private PrintVoucherController printVoucherController;
	
	private OverridePrintController overridePrintController;

	public DialogShellForPrint(Shell parent,PrintVoucherController printVoucherController)	{
		this.printVoucherController = printVoucherController;
		createParentShell(parent);
	}

	/**
	 * This method initializes sShell
	 */
	private void createParentShell(Shell parent)	{
		
		dialogShell = new Shell(parent, SWT.APPLICATION_MODAL);


		try {
			overridePrintController = new OverridePrintController(dialogShell,SWT.NONE, new OverridePrintForm(), new SDSValidator(getClass(),true),printVoucherController);			

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

	public OverridePrintController getAuthController()
	{
		return overridePrintController;
	}
}
