/*****************************************************************************
 * $Id: JackpotTypeControl.java,v 1.7, 2010-06-30 11:59:51Z, Subha Viswanathan$
 * $Date: 6/30/2010 6:59:51 AM$
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

package com.ballydev.sds.jackpotui.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.composite.TouchScreenBottomComposite;
import com.ballydev.sds.framework.constant.IImageConstants;
import com.ballydev.sds.framework.constant.ITSControlSizeConstants;
import com.ballydev.sds.framework.control.CbctlLabel;
import com.ballydev.sds.framework.control.RadioButtonControl;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;



/**
 * 
 * TouchScreenShiftComposite - Draws the shift composite
 * Day, Swing, Graveyard	
 * 
 * @author ksuganthi
 * 
 */

public class JackpotTypeControl{


	/**
	 * Shift label instance
	 */
	private CbctlLabel lblJPType = null;

	
	/**
	 * Normal jp label instance
	 */
	private CbctlLabel lblNormal = null;

	/**
	 * Progressive jp label instance
	 */
	private CbctlLabel lblProgressive = null;

	/**
	 * Cancelled credit jp label instance
	 */
	private CbctlLabel lblCanceledCredits = null;	
	
	/**
	 * Normal jp button instance
	 */
	private TSButtonLabel btnNormal = null;

	/**
	 * Progressive jp button instance
	 */
	private TSButtonLabel btnProgressive = null;

	/**
	 * Cancelled credit jp button instance
	 */
	private TSButtonLabel btnCanceledCredit = null;
	
	
	private String imgRadioBtn = IImageConstants.RADIO_BUTTON_CHECKED_IMAGE;
	
	
	/**
	 * Shift Composite
	 */
	private RadioButtonControl jpTypeRadioButtonComposite;
	
	private boolean isProgressive = false;
	
	private int lblWidth = ITSControlSizeConstants.BOTTOM_LARGE_LABEL_WIDTH;
	
	
	public JackpotTypeControl drawBottomControls(TouchScreenBottomComposite parent, int style, boolean isProgressive) {
		
		this.isProgressive = isProgressive;
		
		if (Util.isSmallerResolution()) {
			lblWidth = ITSControlSizeConstants.S_BOTTOM_LARGE_LABEL_WIDTH;
			imgRadioBtn = IImageConstants.S_RADIO_BUTTON_CHECKED_IMAGE;
		}
		
		return initialize(parent);
		
	}

	
	
	
	private JackpotTypeControl initialize(TouchScreenBottomComposite parent){
		

		System.out.println("Inside JackpotTypeControl:initialize");
		
		
		GridData gdRadioImage = new GridData();
		gdRadioImage.horizontalAlignment = GridData.BEGINNING;
		gdRadioImage.horizontalIndent = 10;
		
		
		GridData gdShiftLbl = new GridData();
		//gdShiftLbl.heightHint = 25;
		gdShiftLbl.horizontalAlignment = GridData.BEGINNING;
		gdShiftLbl.verticalAlignment = GridData.CENTER;
		gdShiftLbl.widthHint = lblWidth;
		
		jpTypeRadioButtonComposite = new RadioButtonControl("shift");
		
		lblJPType = new CbctlLabel(parent, SWT.NONE, LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_GROUP_HEADING_LABEL));
		lblJPType.setLayoutData(gdShiftLbl);
		
		
		
		btnNormal = new TSButtonLabel(parent, SWT.NONE, "normal");
		btnNormal.setImage(new Image(Display.getCurrent(), getClass()
				.getResourceAsStream(imgRadioBtn)));
		btnNormal.setLayoutData(gdRadioImage);
		jpTypeRadioButtonComposite.add(btnNormal);
		
		lblNormal = new CbctlLabel(parent, SWT.NONE);
		lblNormal.setText(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_NORMAL_LABEL));
		lblNormal.setLayoutData(gdShiftLbl);
			
		if(isProgressive){
			btnProgressive = new TSButtonLabel(parent, SWT.NONE, "progressive");
			btnProgressive.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgRadioBtn)));
			btnProgressive.setLayoutData(gdRadioImage);
			jpTypeRadioButtonComposite.add(btnProgressive);
			
			lblProgressive = new CbctlLabel(parent, SWT.NONE);
			lblProgressive.setText(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_PROGRESSIVE_LABEL));
			lblProgressive.setLayoutData(gdShiftLbl);
		}
		btnCanceledCredit = new TSButtonLabel(parent, SWT.NONE, "canceledCredits");
		btnCanceledCredit.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(imgRadioBtn)));
		btnCanceledCredit.setLayoutData(gdRadioImage);
		jpTypeRadioButtonComposite.add(btnCanceledCredit);
		
		lblCanceledCredits = new CbctlLabel(parent, SWT.NONE);
		lblCanceledCredits.setText(LabelLoader.getLabelValue(LabelKeyConstants.JACKPOT_TYPE_CANCELED_CREDITS_LABEL));
		lblCanceledCredits.setLayoutData(gdShiftLbl);
		
		return this;		
		
	}




	public RadioButtonControl getJpTypeRadioButtonComposite() {
		return jpTypeRadioButtonComposite;
	}




	public void setJpTypeRadioButtonComposite(
			RadioButtonControl jpTypeRadioButtonComposite) {
		this.jpTypeRadioButtonComposite = jpTypeRadioButtonComposite;
	}




	public TSButtonLabel getBtnNormal() {
		return btnNormal;
	}




	public void setBtnNormal(TSButtonLabel btnNormal) {
		this.btnNormal = btnNormal;
	}




	public TSButtonLabel getBtnProgressive() {
		return btnProgressive;
	}




	public void setBtnProgressive(TSButtonLabel btnProgressive) {
		this.btnProgressive = btnProgressive;
	}




	public TSButtonLabel getBtnCanceledCredit() {
		return btnCanceledCredit;
	}




	public void setBtnCanceledCredit(TSButtonLabel btnCanceledCredit) {
		this.btnCanceledCredit = btnCanceledCredit;
	}


		
	

}  

