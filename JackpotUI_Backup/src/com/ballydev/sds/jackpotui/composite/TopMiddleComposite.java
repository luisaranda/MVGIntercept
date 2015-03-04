/*****************************************************************************
 * $Id: TopMiddleComposite.java,v 1.5, 2010-02-16 08:13:08Z, Anbuselvi, Balasubramanian$
 * $Date: 2/16/2010 2:13:08 AM$
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

import com.ballydev.sds.framework.control.CbctlLabel;

/**
 * The middle composite for Top Composite
 * 
 * @author dambereen
 * @version $Revision: 6$
 */
public class TopMiddleComposite extends Composite {
	
	/**
	 * The Current Composite Instance of this middle composite
	 */
	public static Composite currentComposite;
	/**
	 * headingComposite instance
	 */
	private Composite headingComposite = null;

	/**
	 * lblHeading instance
	 */
	private CbctlLabel lblHeading = null;

	/**
	 * Class constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public TopMiddleComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
		this.getParent().layout();	
		
	}

	/**
	 * initialize method
	 */
	private void initialize() {
		GridLayout grlPage = new GridLayout();
		grlPage.horizontalSpacing = 0;
		grlPage.marginWidth = 0;
		grlPage.marginHeight = 0;
		grlPage.verticalSpacing = 0;

		GridData gdPage = new GridData();
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.verticalAlignment = GridData.FILL;
		gdPage.horizontalAlignment = GridData.FILL;
		this.setLayout(grlPage);
		this.setLayoutData(gdPage);
	}

	/**
	 * @return the currentComposite
	 */
	public static Composite getCurrentComposite() {
		return currentComposite;
	}

	/**
	 * @param currentComposite
	 *            the currentComposite to set
	 */
	public static void setCurrentComposite(Composite currentComposite) {
		MainMenuComposite.currentComposite = currentComposite;
	}

	/**
	 * @return the lblHeading
	 */
	public CbctlLabel getLblHeading() {
		return lblHeading;
	}

	/**
	 * @param lblHeading the lblHeading to set
	 */
	public void setLblHeading(CbctlLabel lblHeading) {
		this.lblHeading = lblHeading;
	}

}  //  @jve:decl-index=0:visual-constraint="47,4"
