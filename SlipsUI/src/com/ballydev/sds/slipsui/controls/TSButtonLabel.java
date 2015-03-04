/*****************************************************************************
 * $Id: TSButtonLabel.java,v 1.1, 2009-07-22 14:49:10Z, Radhika, Kameswaran$
 * $Date: 7/22/2009 9:49:10 AM$
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.control.SDSTSLabel;

/**
 * CBCTL Custom Button Class
 *
 */
public class TSButtonLabel extends SDSTSLabel {
	/**
	 * final DEFAULT_STYLE
	 */
	private final static int DEFAULT_STYLE = SWT.NONE;
	/**
	 * final DEFAULT_FONT = "Arial"
	 */
	protected final static String CBCTL_DEFAULT_FONT = "Arial";
	/**
	 * final DEFAULT_FONT_HEIGHT = 10
	 */
	protected final static int CBCTL_DEFAULT_FONT_HEIGHT = 10;
	
	protected final static int CBCTL_HEADING_FONT=14;
	/**
	 * final DEFAULT_FONT_STYLE = SWT.NORMAL
	 */
	protected final static int CBCTL_DEFAULT_FONT_STYLE = SWT.BOLD;
	
	/**
	 * overloaded constructor.
	 * @param parent
	 * @param style
	 */
	public TSButtonLabel(Composite parent, int style,String name) {
		super(parent, style);
		setName(name);
		setLabelDetails();
	}
	
	
	/**
	 * overloaded constructor.
	 * @param parent
	 * @param style
	 */
	public TSButtonLabel(Composite parent, int style) {
		super(parent, style);
		setLabelDetails();
	}
	
	/**
	 * overloaded constructor.
	 * @param parent
	 * @param style
	 * @param text
	 */
	public TSButtonLabel(Composite parent, int style, String text,String name) {
		super(parent, style, text);
		setName(name);
		setLabelDetails();
	
	}
	
	/**
	 * Constructor for a heading label
	 * @param parent
	 * @param style
	 * @param head
	 */
	public TSButtonLabel(Composite parent, int style,boolean head) {
		super(parent, style);
		setHeadingLabelDetails();
	}

	
	
	/**
	 * overloaded constructor.
	 * @param parent
	 * @param style
	 * @param text
	 * @param image
	 * @param name
	 */
	public TSButtonLabel(Composite parent, int style, String text, Image image,String name) {
		super(parent, style, text, image);
		setName(name);
		setLabelDetails();
		
	}
	
	/**
	 * overloaded constructor.
	 * @param parent
	 * @param style
	 * @param text
	 * @param image
	 */
	public TSButtonLabel(Composite parent, int style, String text, Image image) {
		super(parent, style, text, image);
		setLabelDetails();
	}
	

	/**
	 *	Sets the default label details 
	 */
	public void setLabelDetails(){
		this.setFont(new Font(Display.getDefault(), CBCTL_DEFAULT_FONT, CBCTL_DEFAULT_FONT_HEIGHT, CBCTL_DEFAULT_FONT_STYLE));
		this.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
	}
	
	/**
	 *	Sets the heading label details 
	 */
	public void setHeadingLabelDetails(){
		try {
			//SDSControlFactory.setHeadingLabelProperties(this);
			this.setFont(new Font(Display.getDefault(), CBCTL_DEFAULT_FONT, CBCTL_HEADING_FONT, CBCTL_DEFAULT_FONT_STYLE));
			this.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
