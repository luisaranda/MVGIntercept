/*****************************************************************************
 * $Id: MainMenuComposite.java,v 1.0, 2008-04-03 15:51:31Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:51:31 AM$
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
package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


/**
 * Parent Composite of the DemoShell
 * @author dambereen 
 * @version $Revision: 1$
 */
public class MainMenuComposite extends Composite {
	
	/**
	 * ParentComposite Instance
	 */
	public static MainMenuComposite parentComposite;
	
	
	/**
	 * CurrentComposite Instance
	 */
	public static Composite currentComposite;
	/**
	 * ParentComposite constructor
	 * @param parent
	 * @param style
	 */
	public MainMenuComposite(Composite parent,Composite parentCenterComposite, int style) {
		super(parent, style);
		initialize();		
		parentComposite=this;
	}

	/**
	 * initialize method of composite
	 */
	private void initialize() {
		
		GridData gridData21 = new GridData();
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.verticalAlignment = GridData.FILL;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 0;
		
		
		this.setLayout(gridLayout);
		this.setLayoutData(gridData21);
		
		
		
		
	}
	
	/**
	 * method to get the instance of the BaseComposite
	 * @param parent
	 * @param style
	 * @return
	 */
	public static MainMenuComposite getInstance(Composite parent,Composite parentCenterComposite, int style)
	{
		if(parentComposite==null || parentComposite.isDisposed())
		{
			parentComposite=new MainMenuComposite(parent,parentCenterComposite, style);
		}
		return parentComposite;
	}
	/**
	 * @return the currentComposite
	 */
	public static Composite getCurrentComposite() {
		return currentComposite;
	}

	/**
	 * @param currentComposite the currentComposite to set
	 */
	public static void setCurrentComposite(Composite currentComposite) {
		MainMenuComposite.currentComposite = currentComposite;
	}
}
