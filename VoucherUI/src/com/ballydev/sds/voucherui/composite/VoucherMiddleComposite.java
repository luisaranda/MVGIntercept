package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
/*****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.control.SDSTSLabel;

/**
 * This is the middle composite for voucher UI
 * @author Nithya kalyani R
 * @version $Revision: 2$ 
 */
public class VoucherMiddleComposite extends Composite {

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
	private SDSTSLabel lblHeading = null;

	/**
	 * Class constructor
	 * 
	 * @param parent
	 * @param style
	 */
	public VoucherMiddleComposite(Composite parent, int style) 
	{
		super(parent, style);
		initialize();
		this.getParent().layout();		
	}

	/**
	 * initialize method
	 */
	private void initialize(){

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
	public static void setCurrentComposite(Composite currComposite) {
		currentComposite = currComposite;
	}	

	/**
	 * @return the lblHeading
	 */
	public SDSTSLabel getLblHeading() {
		return lblHeading;
	}

	/**
	 * @param lblHeading the lblHeading to set
	 */
	public void setLblHeading(SDSTSLabel lblHeading) {
		this.lblHeading = lblHeading;
	}


	public Composite getHeadingComposite() {
		return headingComposite;
	}

	public void setHeadingComposite(Composite headingComposite) {
		this.headingComposite = headingComposite;
	}

}  //  @jve:decl-index=0:visual-constraint="47,4"

