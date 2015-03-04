/*****************************************************************************
 * $Id: CbctlUtil.java,v 1.1, 2010-02-16 08:13:08Z, Anbuselvi, Balasubramanian$
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
package com.ballydev.sds.jackpotui.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;

/**
 * Util class for Jackpot to create the composite with mandatory label
 * @author dambereen
 * @version $Revision: 2$
 */
public class CbctlUtil {
	
	/**
	 * Mandatory Label instance
	 */
	private static CbctlLabel mandatoryLabel = null;
	
	/**
	 * This method returns the composite with mandatory label.
	 * @param parent
	 * @return
	 */
	public static Composite getSDSMandatoryComposite(Composite parent){
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight=0;
		gridLayout.marginWidth=0;
		gridLayout.verticalSpacing=0;
		gridLayout.horizontalSpacing=0;		
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.END;
		mandatoryLabel = new CbctlLabel(parent, SWT.NONE);
		mandatoryLabel.setText(" * " + LabelLoader.getLabelValue(LabelKeyConstants.MANDATORY_LABEL)+"   ");
		mandatoryLabel.setForeground(SDSControlFactory.getMandatoryLabelColor());
		mandatoryLabel.setLayoutData(gridData);
		parent.setBackground(SDSControlFactory.getLabelBackGroundColor());
		parent.setLayout(gridLayout);

		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.verticalAlignment = GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		composite.setLayout(new GridLayout());
		composite.setBackground(SDSControlFactory.getDefaultBackGround());
		composite.setLayoutData(gridData1);
		
		return composite;
	}
	
	/**
	 * Method to display the Mandatory Label
	 * @param display
	 */
	public  static void displayMandatoryLabel(boolean display){
		if (mandatoryLabel!=null && !(mandatoryLabel.isDisposed())) {
			mandatoryLabel.setVisible(display);
		}		
	}
	
	
	/**
	 * Method to disable the Mandatory Label
	 */
	public static void setMandatoryLabelOff(){
		CbctlUtil.displayMandatoryLabel(false);
	}
}
