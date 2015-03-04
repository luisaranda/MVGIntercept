/*****************************************************************************
 * $Id: FocusUtility.java,v 1.2, 2010-02-28 06:35:08Z, Suganthi, Kaliamoorthy$
 * $Date:
 ******************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.slipsui.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.control.SDSTSText;

/**
 * Focus set utility class.
 * @author anantharajr
 * @version $Revision: 3$
 */
public class FocusUtility {
	
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
				dummy.forceFocus();				
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
	}
}
