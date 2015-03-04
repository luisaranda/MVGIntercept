/*****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpotui.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.control.SDSTSText;


/**
 * Focus set utility class.
 * @author anantharajr
 * @version $Revision: 5$
 */
public class FocusUtility {
	
	public static boolean focus = false;
	
	/**
	 * This method will the set the focus to first Text box.
	 * @param composite
	 */
	public static void setTextFocus(Composite composite)
	{
		Control[] controls = composite.getChildren();
		for(Control dummy:controls)
		{
			if(dummy instanceof SDSTSText) {
				if(!focus)
					dummy.forceFocus();	
				focus = true;
				break;			
			}
			else if(dummy instanceof Group || dummy instanceof Composite)
			{
				setTextFocus((Composite)dummy);
			}
			else
			{
				continue;
			}
		}
		if(!focus)
			return;
	}
}
