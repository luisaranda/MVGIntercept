/*****************************************************************************
 * $Id: CbctlButton.java,v 1.1, 2009-07-22 14:49:10Z, Radhika, Kameswaran$
 * $Date: 7/22/2009 9:49:10 AM$
 * $Log$
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
package com.ballydev.sds.slipsui.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.control.SDSButton;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSButton;

/**
 * CBCTL Custom Button Class
 * @version $Revision: 2$
 */
public class CbctlButton extends SDSTSButton {
	

	/**
	 * final DEFAULT_FONT
	 */
	private final static String DEFAULT_FONT = "Arial";
	/**
	 * final DEFAULT_FONT_HEIGHT
	 */
	private final static int DEFAULT_FONT_HEIGHT = 10;
	/**
	 * final DEFAULT_FONT_STYLE
	 */
	private final static int DEFAULT_FONT_STYLE = SWT.BOLD;
	
	
	/**
	 * Constructor for button
	 * @param parent
	 * @param style
	 * @param text
	 * @param formProperty
	 */
	public CbctlButton(Composite parent, int style, String text, String name) {
		super(parent, style, text, name);	
		//this.setFont(new Font(Display.getDefault(), DEFAULT_FONT, DEFAULT_FONT_HEIGHT, DEFAULT_FONT_STYLE));
		try {
			SDSControlFactory.setButtonProperties(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param parent
	 * @param style
	 * @param text
	 * @param name
	 * @param formPropery
	 * @param checkValue
	 */
	public CbctlButton(Composite parent, int style, String text, String name,String formPropery,boolean checkValue) {
		super(parent, style, text,name,formPropery,checkValue);	
		//this.setFont(new Font(Display.getDefault(), DEFAULT_FONT, DEFAULT_FONT_HEIGHT, DEFAULT_FONT_STYLE));
		try {
			SDSControlFactory.setButtonProperties(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
