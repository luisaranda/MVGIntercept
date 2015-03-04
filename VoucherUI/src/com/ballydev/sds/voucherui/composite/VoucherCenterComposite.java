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

package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSLabel;

/**
 *
 * This class creates the center composite in the
 * voucher display
 *  
 * @author Nithya kalyani
 * @version $Revision: 4$
 */

public class VoucherCenterComposite extends Composite {


	/**
	 * Instance of composite
	 */
	private Composite middleRightComposite = null;


	/**
	 * Constructor of the class
	 */
	public VoucherCenterComposite(Composite parent, int style,String text) {
		super(parent, style);
		initialize(text);
		this.setBounds(0,0, 800,700);
		this.setBackground(SDSControlFactory.getTouchScreenHomeBackgroundColor());	

	}

	/**
	 * This method sets the grid properties for the
	 * composite
	 */
	private void initialize(String text) {
		GridLayout grlPage = new GridLayout();
		grlPage.horizontalSpacing = 0;
		grlPage.marginWidth = 0;
		grlPage.marginHeight = 0;
		grlPage.verticalSpacing = 0;
		setLayout(grlPage);	


		GridData gdPage = new GridData();
		gdPage.grabExcessHorizontalSpace = true;
		gdPage.grabExcessVerticalSpace = true;
		gdPage.horizontalAlignment = SWT.FILL;
		gdPage.verticalAlignment = SWT.FILL;
		setLayoutData(gdPage);

		createHomeMiddleComposite(text);
	}	

	/**
	 * This method initializes homeMiddleComposite	
	 *
	 */
	private void createHomeMiddleComposite(String text) {
		try {	

			middleRightComposite = new Composite(this, SWT.NONE);			
			middleRightComposite.setLayout(new GridLayout());			
			GridData gdMiddleComp = new GridData();
			gdMiddleComp.grabExcessVerticalSpace = true;
			gdMiddleComp.grabExcessHorizontalSpace = true;
			gdMiddleComp.horizontalAlignment = GridData.FILL;
			gdMiddleComp.verticalAlignment = GridData.FILL;
			gdMiddleComp.widthHint = 850;
			middleRightComposite.setLayoutData(gdMiddleComp);
			middleRightComposite.setBackground(SDSControlFactory.getDefaultBackGround());			

			Composite welcomeComposite = new Composite(middleRightComposite, SWT.NONE);						
			welcomeComposite.setLayout(new GridLayout());
			GridData gdWelcomeComp = new GridData();
			gdWelcomeComp.grabExcessHorizontalSpace = true;
			gdWelcomeComp.grabExcessVerticalSpace = true;
			gdWelcomeComp.horizontalAlignment = GridData.FILL;
			gdWelcomeComp.verticalAlignment = GridData.FILL;
			welcomeComposite.setLayoutData(gdWelcomeComp);

			SDSLabel lblWelcome = new SDSLabel(welcomeComposite,SWT.WRAP | SWT.CENTER);			
			lblWelcome.setText(text);			
			GridData gdLblWelcome = new GridData();
			gdLblWelcome.grabExcessHorizontalSpace = true;
			gdLblWelcome.grabExcessVerticalSpace = true;
			gdLblWelcome.horizontalAlignment = GridData.CENTER;
			gdLblWelcome.verticalAlignment = GridData.BEGINNING;
			gdLblWelcome.verticalIndent = 200;
			gdLblWelcome.horizontalIndent = 25;
			lblWelcome.setLayoutData(gdLblWelcome);
			SDSControlFactory.setTouchScreenHomeWelcomeLabelProperties(lblWelcome);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
