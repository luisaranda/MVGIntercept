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
package com.ballydev.sds.jackpotui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.control.SDSControlFactory;

/**
 * TopMainComposite composite class 
 * @author anantharajr
 * @version $Revision: 3$
 */
public class TopMainComposite extends Composite {

	private Composite leftBorederComposite = null;
	private Composite centerComposite = null;
	private Composite rightBorderComposite = null;
	public TopMainComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	/**
	 * initialize method
	 */
	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		createLeftBorederComposite();
		this.setLayout(gridLayout);
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.horizontalAlignment = GridData.FILL;
		this.setLayoutData(gridData5);
		setSize(new Point(887, 482));
		createCenterComposite();
		createRightBorderComposite();
	}

	/**
	 * This method initializes leftBorederComposite	
	 *
	 */
	private void createLeftBorederComposite() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		gridData1.widthHint = 200;
		gridData1.horizontalAlignment = GridData.BEGINNING;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.marginWidth = 0;
		leftBorederComposite = new Composite(this, SWT.NONE);
		leftBorederComposite.setBackground(SDSControlFactory.getTouchScreenHomeBackgroundColor());
		leftBorederComposite.setLayout(gridLayout1);
		leftBorederComposite.setLayoutData(gridData1);
	}

	/**
	 * This method initializes centerComposite	
	 *
	 */
	private void createCenterComposite() {
		GridData gridData11 = new GridData();
		gridData11.grabExcessVerticalSpace = true;
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.verticalAlignment = GridData.CENTER;
		gridData11.grabExcessHorizontalSpace = true;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 0;
		gridLayout3.marginWidth = 0;
		gridLayout3.marginHeight = 0;
		gridLayout3.verticalSpacing = 0;
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		centerComposite = new Composite(this, SWT.NONE);
		centerComposite.setBackground(SDSControlFactory.getDefaultBackGround());
		centerComposite.setLayoutData(gridData);
		centerComposite.setLayout(gridLayout3);
	}

	/**
	 * This method initializes rightBorderComposite	
	 *
	 */
	private void createRightBorderComposite() {
		GridData gridData2 = new GridData();
		gridData2.widthHint = 7;
		gridData2.horizontalAlignment = GridData.BEGINNING;
		gridData2.verticalAlignment = GridData.FILL;
		gridData2.grabExcessVerticalSpace = false;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.marginHeight = 0;
		gridLayout2.verticalSpacing = 0;
		rightBorderComposite = new Composite(this, SWT.NONE);
		rightBorderComposite.setBackground(SDSControlFactory.getTouchScreenHomeBackgroundColor());
		rightBorderComposite.setLayout(gridLayout2);
		rightBorderComposite.setLayoutData(gridData2);
	}

}  //  @jve:decl-index=0:visual-constraint="44,36"
