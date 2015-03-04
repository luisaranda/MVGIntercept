/*****************************************************************************
 * $Id: ButtonComposite.java,v 1.1, 2008-12-22 10:11:55Z, Nithyakalyani, Raman$
 * $Date: 12/22/2008 4:11:55 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ITSConstants;
import com.ballydev.sds.framework.control.SDSTSButton;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.util.VoucherUtil;


public class ButtonComposite extends Composite 
{
	private Composite buttonComposite = null;
	private SDSTSButton btnExit = null;

	public ButtonComposite(Composite parent, int style) 
	{
		super(parent, style);
		initialize();
		setSize(new Point(800, 100));
	}

	private void initialize() 
	{
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 5;
		gridLayout1.numColumns = 1;
		gridLayout1.verticalSpacing = 0;

		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.verticalAlignment = GridData.FILL;

		createButtonComposite();

		this.setBackground(VoucherUtil.getDefaultBackGroudColor());
		this.setLayout(new GridLayout());
		this.setLayoutData(gridData21);

		layout();
	}

	public void createButtonComposite()
	{
		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.heightHint = 100;
		//gridData21.widthHint = ;
		gridData21.verticalAlignment = GridData.CENTER;

		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 50;
		gridLayout.marginWidth = 20;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;

		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setBackground(VoucherUtil.getDefaultBackGroudColor());
		buttonComposite.setLayoutData(gridData21);
		buttonComposite.setLayout(gridLayout);

		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.END;
		gridData2.heightHint = ITSConstants.SDSTS_BTN_DEFAULT_HEIGHT;
		gridData2.widthHint = ITSConstants.SDSTS_BTN_DEFAULT_WIDTH;
		gridData2.horizontalAlignment = GridData.END;

		btnExit = new SDSTSButton(buttonComposite, SWT.NONE, LabelLoader.getLabelValue(IDBLabelKeyConstants.LBL_MENU),IVoucherConstants.BUTTON_CTRL_BTN_EXIT);
		//btnExit.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.EXIT_BUTTON_IMG)));
		btnExit.setLayoutData(gridData2);
	}

	public Composite getButtonComposite()
	{
		return buttonComposite;
	}
}
