/*****************************************************************************
 * $Id: TopComposite.java,v 1.0, 2008-04-03 15:53:17Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:53:17 AM$
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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Composite containing the TopHeader, TopMiddle, and TopFooter Composites
 * 
 * @author dambereen
 * @version $Revision: 1$
 */
public class TopComposite extends Composite {
	/**
	 * TopComposite Constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public TopComposite(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		initialize();
		setSize(new Point(1024, 768));
	}

	/**
	 * Method to create the composite
	 */
	private void initialize() {
		GridData gridData4 = new GridData();
		gridData4.grabExcessVerticalSpace = true;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.FILL;

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.verticalSpacing = 0;
		// this.setBackgroundMode(SWT.INHERIT_NONE);
		this.setLayout(gridLayout1);
		this.setLayoutData(gridData4);
		this.layout();
	}

	/*
	 * TopComposite getInstance(Composite parent, int style) {
	 * if(baseComposite==null || baseComposite.isDisposed()) { baseComposite=new
	 * BaseComposite(parent,style); } return baseComposite; }
	 */

} // @jve:decl-index=0:visual-constraint="36,26"
