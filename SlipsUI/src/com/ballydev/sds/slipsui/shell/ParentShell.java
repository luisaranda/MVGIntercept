/*****************************************************************************
 * $Id: ParentShell.java,v 1.0, 2008-04-21 05:44:28Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:44:28 AM$
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
package com.ballydev.sds.slipsui.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.slipsui.controller.MainMenuController;

/**
 * @author dambereen
 *
 */
public class ParentShell {	
	/**
	 * Shell Instance
	 */
	private Shell parentShellObj = null; //  @jve:decl-index=0:			
	/**
	 * ParentShell Constructor
	 */
	public ParentShell() {
		createParentShell();
	}		
	/**
	 * This method initializes sShell
	 */
	private void createParentShell() {		
		parentShellObj = new Shell(SWT.NONE);
		parentShellObj.setText("Shell");
		try {
			//new MainMenuController(parentShellObj,SWT.NONE);			
			
		} catch (Exception e1) {			
			e1.printStackTrace();
		}			
		parentShellObj.setLayout(new FillLayout());
		parentShellObj.setMaximized(true);
		parentShellObj.layout();
		parentShellObj.open();
	}
}
