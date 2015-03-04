/*****************************************************************************
 * $Id: TopMiddleComposite.java,v 1.1, 2010-01-28 09:56:36Z, Ambereen Drewitt$
 * $Date: 1/28/2010 3:56:36 AM$
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
package com.ballydev.sds.slipsui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.control.CbctlLabel;

/**
 * The middle composite for Top Composite
 * 
 * @author dambereen
 * 
 */
public class TopMiddleComposite extends Composite {
	/**
	 * JackpotMiddleComposite Instance
	 */
//	public static TopMiddleComposite slipMiddleComposite = null;

	/**
	 * The Current Composite Instance of this middle composite
	 */
	public static Composite currentComposite;

	private Composite headerComposite = null;

	private CbctlLabel lblHeading = null;

	/**
	 * Class constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public TopMiddleComposite(Composite parent, int style) {
		super(parent, style);
		
		//slipMiddleComposite = this;
		
		initialize();
		this.getParent().layout();
		
		//this.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		// setSize(new Point(1152,100));
	}

	/**
	 * initialize method
	 */
	private void initialize() {
		/*createHeaderComposite();
		this.setSize(new Point(863, 517));
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 0;
		gridLayout3.marginWidth = 0;
		gridLayout3.marginHeight = 0;
		gridLayout3.verticalSpacing = 0;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		// gridData5.heightHint = 100;
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.horizontalAlignment = GridData.FILL;
		this.setLayout(gridLayout3);
		this.setLayoutData(gridData5);*/
		
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
	 * This method initializes headerComposite	
	 *
	 */
	private void createHeaderComposite() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.heightHint = -1;
		gridData1.widthHint = 600;
		gridData1.verticalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.heightHint = 30;
		gridData.widthHint = -1;
		headerComposite = new Composite(this, SWT.NONE);
		headerComposite.setLayoutData(gridData);
		headerComposite.setLayout(gridLayout);
		lblHeading = new CbctlLabel(headerComposite, SWT.CENTER, true);
		lblHeading.setText("");
		lblHeading.setLayoutData(gridData1);
	}

	public CbctlLabel getLblHeading() {
		return lblHeading;
	}

	public void setLblHeading(CbctlLabel lblHeading) {
		this.lblHeading = lblHeading;
	}

	/*public static TopMiddleComposite getSlipMiddleComposite() {
		return slipMiddleComposite;
	}

	public static void setSlipMiddleComposite(TopMiddleComposite slipMiddleComposite) {
		TopMiddleComposite.slipMiddleComposite = slipMiddleComposite;
	}*/

	public Composite getHeaderComposite() {
		return headerComposite;
	}

	public void setHeaderComposite(Composite headerComposite) {
		this.headerComposite = headerComposite;
	}

}  //  @jve:decl-index=0:visual-constraint="226,95"
