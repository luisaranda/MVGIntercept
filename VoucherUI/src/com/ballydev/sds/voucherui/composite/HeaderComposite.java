/*****************************************************************************
 * $Id: HeaderComposite.java,v 1.7, 2010-03-01 15:24:09Z, Verma, Nitin Kumar$
 * $Date: 3/1/2010 9:24:09 AM$
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ILabelConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.SDSControlFactory;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.control.SDSTSLabelStyle;
import com.ballydev.sds.framework.util.Util;

/**
 * This method creates the header composite
 * @author Nithya kalyani R
 * @version $Revision: 8$ 
 */
public class HeaderComposite extends Composite {

	/**
	 * Instance of the header label
	 */
	private SDSTSLabel lblHeader = null;

	/**
	 * Instance of the mandatory label
	 */
	private SDSTSLabel lblMandatory = null;

	/**
	 * Constructor of the class
	 */
	public HeaderComposite(Composite parent, int style)	{
		this(parent, style,"");
	}

	/**
	 * Constructor of the class
	 */
	public HeaderComposite(Composite parent, int style, String text){
		super(parent, style);
		try {
			initialize(text);
			this.setBackground(SDSControlFactory.getTSHeaderColor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param text
	 * @throws Exception
	 */
	private void initialize(String text) throws Exception{

		GridLayout grlPage = new GridLayout();
		grlPage.numColumns 		  = 2;		
		grlPage.marginHeight      = 0;
		grlPage.verticalSpacing   = 0;
		grlPage.horizontalSpacing = 5;

		GridData headerCompositeGD = new GridData();
		headerCompositeGD.grabExcessHorizontalSpace = true;
		headerCompositeGD.grabExcessVerticalSpace = false;
		headerCompositeGD.horizontalAlignment = GridData.FILL;
		headerCompositeGD.heightHint = ITSControlSizeConstants.HEADER_HEIGHT;

		lblHeader = new SDSTSLabel(this, SWT.CENTER | SWT.WRAP, text);
		lblHeader.setText(text);
		if( Util.isSmallerResolution() ) {
			lblHeader.setFont(SDSControlFactory.getSmallDefaultBoldFont());
		} else {
			lblHeader.setFont(SDSControlFactory.getHeadingLabelFont());
		}
		lblHeader.setStyle(SDSTSLabelStyle.HEADING);

		lblMandatory = new SDSTSLabel(this, SWT.NONE, "* " + LabelLoader.getLabelValue(ILabelConstants.FRAMEWORK_MANDATORY_LABEL));
		setControlProperties();
		this.setLayoutData(headerCompositeGD);
		this.setLayout(grlPage);	
		this.setSize(this.getParent().getSize());
	}

	/**
	 * This method sets the control default grid properties
	 */
	private void setControlProperties(){
		try {			
			SDSControlFactory.setTouchScreenLabelProperties(lblHeader);
			SDSControlFactory.setTouchScreenLabelProperties(lblMandatory);
			GridData gdHeaderLabel = (GridData)lblHeader.getLayoutData();
			gdHeaderLabel.horizontalAlignment = GridData.BEGINNING;
			gdHeaderLabel.verticalAlignment = GridData.CENTER;
			gdHeaderLabel.grabExcessHorizontalSpace = true;
			gdHeaderLabel.grabExcessVerticalSpace = true;
			lblHeader.setLayoutData(gdHeaderLabel);

			/**SETS THE TITLE FONT AND COLOR**/
			lblHeader.setFont(SDSControlFactory.getHeadingLabelFont());
			if( Util.isSmallerResolution() ) {
				lblHeader.setFont(SDSControlFactory.getSmallHeadingLabelFont());
			}
			lblHeader.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			lblMandatory.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

			GridData gdReqdLabel = (GridData)lblMandatory.getLayoutData();
			gdReqdLabel.grabExcessHorizontalSpace = true;
			gdReqdLabel.horizontalAlignment = GridData.END;
			gdReqdLabel.verticalAlignment = GridData.CENTER;
			lblMandatory.setLayoutData(gdReqdLabel);

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	/**
	 * @return the headerLabel
	 */
	public SDSTSLabel getLblHeader() {
		return lblHeader;
	}

	/**
	 * @param headerLabel the headerLabel to set
	 */
	public void setLblHeader(SDSTSLabel headerLabel) {
		this.lblHeader = headerLabel;
	}

	/**
	 * @return the mandatoryLabel
	 */
	public SDSTSLabel getLblMandatory() {
		return lblMandatory;
	}

	/**
	 * @param mandatoryLabel the mandatoryLabel to set
	 */
	public void setLblMandatory(SDSTSLabel mandatoryLabel) {
		this.lblMandatory = mandatoryLabel;
	}

	/**
	 * This method displays the mandatory label if the
	 * boolean display is on
	 * @param display
	 */
	public  void displayMandatoryLabel(boolean display){
		if (lblMandatory!=null && !(lblMandatory.isDisposed())) {
			lblMandatory.setVisible(display);
		}	
	}
} 
