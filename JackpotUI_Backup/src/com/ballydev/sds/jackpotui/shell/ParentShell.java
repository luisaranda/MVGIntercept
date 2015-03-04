/*****************************************************************************
 * $Id: ParentShell.java,v 1.0, 2008-04-03 15:52:14Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:52:14 AM$
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
package com.ballydev.sds.jackpotui.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.jackpotui.controller.MainMenuController;

/**
 * Parent shell for the Jackpot Module
 * @author dambereen
 * @version $Revision: 1$
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
