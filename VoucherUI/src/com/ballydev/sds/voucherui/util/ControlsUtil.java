/*****************************************************************************
 * $Id: ControlsUtil.java,v 1.0, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:46:20 AM$
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
package com.ballydev.sds.voucherui.util;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.control.SDSButton;
import com.ballydev.sds.framework.control.SDSCombo;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * Class to support UI controls. 
 * @author vnitinkumar
 *
 */
public class ControlsUtil {

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	public static GridData createGridData(int wHint, int hHint, int hIndent, int hAlignment, int vAlignment, boolean gHSpace, boolean gVSpave, int vSpan)
	{
		GridData gridData = new GridData(hAlignment,vAlignment, gHSpace, gVSpave, 1, vSpan);
		gridData.heightHint = hHint;
		gridData.widthHint = wHint;
		gridData.horizontalIndent = hIndent;
		log.debug(gridData.toString());
		return gridData;
	}
	
	public static GridData createGridData(int hAlignment, int vAlignment, boolean gHSpace, boolean gVSpave, int vSpan)
	{
		GridData gridData = new GridData(hAlignment,vAlignment, gHSpace, gVSpave, 1, vSpan);
		log.debug(gridData.toString());
		return gridData;
	}
	
	public static GridLayout createGridLayout(int numCol, int hSpace, int vSpace, int mWidth, int mHeight)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numCol;
		gridLayout.horizontalSpacing = hSpace;
		gridLayout.verticalSpacing = vSpace;
		gridLayout.marginWidth = mWidth;
		gridLayout.marginHeight = mHeight;
		log.debug(gridLayout.toString());
		return gridLayout;
	}

	public static Group createGroup(Composite composite, String label)
	{
		Group group = new Group(composite, SWT.NONE);
		group.setText(label);
		group.setFont(SDSControlFactory.getGroupHeadingFont());
		group.setLayoutData(createGridData(-1, -1, 0, GridData.FILL, GridData.FILL, true, true, 1));
		log.debug(group.toString());
		return group;
	}
	
	public static Group createGroup(Composite composite, GridData gridData, String labelKey)
	{
		Group group = new Group(composite, SWT.NONE);
		group.setText(labelKey);
		group.setFont(SDSControlFactory.getGroupHeadingFont());
		group.setLayoutData(gridData);
		log.debug(group.toString());
		return group;
	}
	
	public static SDSButton createButton(Composite parent, int style, String text, GridData gridData, String toolTip, String name)
	{
		SDSButton button = new SDSButton(parent, style, text, name);
		button.setFont(SDSControlFactory.getButtonLabelFont());
		button.setLayoutData(gridData);
		button.setToolTipText(toolTip);
		log.debug(button.toString());
		return button;
	}
	
	public static Composite makeComposite(Composite parComposite, GridData gridData, GridLayout gridLayout)
	{
		Composite composite = new Composite(parComposite, SWT.NONE);
		composite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		composite.setLayoutData(gridData);
		composite.setLayout(gridLayout);
		composite.layout();
		log.debug(composite.toString());
		return composite;
	}
	
	public static SDSCombo createCombo(Group group, String helpContent, String toolTip, GridData gridData, String formListProperty, String formItemProperty)
	{
		
		SDSCombo savedFormCombo = new SDSCombo(group, SWT.BORDER, toolTip, formListProperty, formItemProperty);
		savedFormCombo.setToolTipText(toolTip);
		savedFormCombo.setLayoutData(gridData);
		log.debug(savedFormCombo.toString());
		return savedFormCombo;
	}
	
}
