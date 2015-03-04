/*****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.controller;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.control.SDSTSCheckBox;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.voucherui.composite.ReportPreferenceComposite;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.form.ReportPreferenceForm;


/**
 * @author Nithya kalyani R
 * @version $Revision: 5$ 
 */
public class ReportPreferenceController  extends SDSBaseController {

	/**
	 * Instance of the ReportPreferenceComposite
	 */
	private ReportPreferenceComposite composite;

	/**
	 * Instance of the ReportPreferenceForm
	 */
	private ReportPreferenceForm form;

	/**
	 * @param parent
	 * @param style
	 * @param form
	 * @param pValidator
	 * @throws Exception
	 */
	public ReportPreferenceController(Composite parent, int style ,ReportPreferenceForm form, SDSValidator pValidator) throws Exception 
	{
		super(form, pValidator);
		composite = new ReportPreferenceComposite(parent,style);
		this.form=form;
		super.registerEvents(composite);
		registerCustomizedListeners(composite);
		form.addPropertyChangeListener(this);		
		setPreferenceValues();	
		setButtonSelection();
		populateScreen(composite);
	}

	/**
	 * This method sets the preference values
	 */
	public void setPreferenceValues(){	

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED))){

			form.setIsCreated(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED));
		}

		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED)))
		{
			form.setIsRedeemed(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED));
		}

		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED)))
		{
			form.setIsVoided(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED));
		}

		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_DISPLAY_ALL_BATCHES)))
		{
			form.setIsDisplayAllBatches(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_DISPLAY_ALL_BATCHES));
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		try {
			populateForm(composite);
			if (((Control) e.getSource() instanceof SDSTSCheckBox)) {
				SDSTSCheckBox tSChkBox = (SDSTSCheckBox) ((Control)e.getSource());	
				setSelectedButton(tSChkBox);
			}
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
	}

	@Override
	public ReportPreferenceComposite getComposite() 
	{
		return composite;
	}

	public ReportPreferenceForm getForm() 
	{
		return form;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		ReportPreferenceComposite reportPreferenceComposite = ((ReportPreferenceComposite)argComposite);
		reportPreferenceComposite.getRadioImgLblCreated().addMouseListener(this);
		reportPreferenceComposite.getRadioImgLblRedeemed().addMouseListener(this);
		reportPreferenceComposite.getRadioImgLblVoided().addMouseListener(this);
	}

	public void setButtonSelection() {
		if(form.getIsCreated()) {			
			composite.getRadioImgLblCreated().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
//			composite.getRadioImgLblCreated().setSelected(true);
		}else{			
			composite.getRadioImgLblCreated().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
//			composite.getRadioImgLblCreated().setSelected(false);
		}
		if(form.getIsRedeemed()) {
			composite.getRadioImgLblRedeemed().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
//			composite.getRadioImgLblRedeemed().setSelected(true);
		}else{
			composite.getRadioImgLblRedeemed().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
//			composite.getRadioImgLblRedeemed().setSelected(false);
		}
		if(form.getIsVoided()) {
			composite.getRadioImgLblVoided().setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
//			composite.getRadioImgLblVoided().setSelected(true);
		}else {
			composite.getRadioImgLblVoided().setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
//			composite.getRadioImgLblVoided().setSelected(false);
		}
	}

	/**
	 * Method to set the SelectedButton
	 * @param selectedButton
	 */
	public void setSelectedButton(SDSTSCheckBox selectedButton) {
		if( !selectedButton.isSelected() ){
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_CHECKED)));
			selectedButton.setSelected(true);	
			if( selectedButton.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_CREATED)) {
				form.setIsCreated(true);				
			} else if(selectedButton.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_REDEEMED)) {
				form.setIsRedeemed(true);
			} else if(selectedButton.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_VOIDED)) {
				form.setIsVoided(true);
			}
		} else {			
			selectedButton.setImage(new Image(Display.getCurrent(),getClass().getResourceAsStream(IImageConstants.IMAGE_TOUCH_SCREEN_CHECKBOX_UNCHECKED)));
			selectedButton.setSelected(false);	
			if( selectedButton.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_CREATED)) {
				form.setIsCreated(false);
			} else if(selectedButton.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_REDEEMED)) {
				form.setIsRedeemed(false);
			} else if(selectedButton.getName().equalsIgnoreCase(IVoucherConstants.PREFERENCE_REPORT_FRM_BTN_VOIDED)) {
				form.setIsVoided(false);
			}
		}
	}
}
