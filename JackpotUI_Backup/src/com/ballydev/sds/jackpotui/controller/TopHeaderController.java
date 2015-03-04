/*****************************************************************************
 * $Id: TopHeaderController.java,v 1.6, 2010-06-28 15:59:28Z, Subha Viswanathan$
 * $Date: 6/28/2010 10:59:28 AM$
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
package com.ballydev.sds.jackpotui.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpotui.composite.JackpotHeaderComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;
import com.ballydev.sds.jackpotui.constants.ImageConstants;
import com.ballydev.sds.jackpotui.constants.MessageKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlButton;
import com.ballydev.sds.jackpotui.form.DisplayJackpotForm;
import com.ballydev.sds.jackpotui.form.EmployeeShiftSlotDetailsForm;
import com.ballydev.sds.jackpotui.form.JackpotSlipReportForm;
import com.ballydev.sds.jackpotui.form.ManualJPEmployeeSlotStandShiftForm;
import com.ballydev.sds.jackpotui.form.ReprintForm;
import com.ballydev.sds.jackpotui.form.VoidForm;
import com.ballydev.sds.jackpotui.util.ClearJackpotFormFields;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Controller class for the TopHeaderComposite
 * 
 * @author dambereen
 * @version $Revision: 7$
 */
public class TopHeaderController extends SDSBaseController {

	/**
	 * JackpotHeaderComposite instance
	 */
	private JackpotHeaderComposite jackpotHeaderComposite;
	
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Pending Inactive Image instance
	 */
	private Image pendingInactive;
	
	/**
	 * Manual Inactive Image instance
	 */
	private Image manualInactive;
	
	/**
	 * Void Inactive Image instance
	 */
	private Image voidInactive;
	
	/**
	 * Pending Reprint Image instance
	 */
	private Image reprintInactive;
	
	/**
	 * Display Inactive Image instance
	 */
	private Image displayInactive;
	
	
	/**
	 * Report Inactive Image instance
	 */
	private Image reportInactive;
	
	/**
	 * Boolean instance to check if the pending function should be disabled
	 */
	private boolean isPendingDisabled;
	
	/**
	 * Boolean instance to check if the manual function should be disabled
	 */
	private boolean isManualDisabled;
	
	/**
	 * Boolean instance to check if the void function should be disabled
	 */
	private boolean isVoidDisabled;
	
	/**
	 * Boolean instance to check if the reprint function should be disabled
	 */
	private boolean isReprintDisabled;
	
	/**
	 * Boolean instance to check if the display function should be disabled
	 */
	private boolean isDisplayDisabled;
	
	/**
	 * Boolean instance to check if the report function should be disabled
	 */
	private boolean isReportDisabled;
	
	private String imgPendingActBtn;
	
	private String imgManualActBtn;
	
	private String imgVoidActBtn;
	
	private String imgReprintActBtn;
	
	private String imgDisplayActBtn;
	
	private String imgRptActBtn;
	
	private String imgPendingDisableBtn;
	
	private String imgManualDisableBtn;
	
	private String imgVoidDisableBtn;
	
	private String imgReprintDisableBtn;
	
	private String imgDispDisableBtn;
	
	private String imgRptDisableBtn;
	
	private String imgPendingInactBtn;
	
	private String imgManualInactiveBtn;
	
	private String imgVoidInactiveBtn;
	
	private String imgReprintInactiveBtn;
	
	private String imgDisplayInactiveBtn;
	
	private String imgRptInactiveBtn;
		
	/**
	 * TopHeaderController instance
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public TopHeaderController(Composite parent, int style) throws Exception {
		super(new SDSForm(), null);
		if (Util.isSmallerResolution()) {
			 imgPendingActBtn = ImageConstants.S_PENDING_ACTIVE_BUTTON_IMG;
			 imgManualActBtn = ImageConstants.S_MANUAL_ACTIVE_BUTTON_IMG;
			 imgVoidActBtn = ImageConstants.S_VOID_ACTIVE_BUTTON_IMG;
			 imgReprintActBtn = ImageConstants.S_REPRINT_ACTIVE_BUTTON_IMG;
			 imgDisplayActBtn = ImageConstants.S_DISPLAY_ACTIVE_BUTTON_IMG;
			 imgRptActBtn = ImageConstants.S_REPORT_ACTIVE_BUTTON_IMG;
			 imgPendingDisableBtn = ImageConstants.S_PENDING_DISABLE_BUTTON_IMG;
			 imgManualDisableBtn = ImageConstants.S_MANUAL_DISABLE_BUTTON_IMG;
			 imgVoidDisableBtn = ImageConstants.S_VOID_DISABLE_BUTTON_IMG;
			 imgReprintDisableBtn = ImageConstants.S_REPRINT_DISABLE_BUTTON_IMG;
			 imgDispDisableBtn = ImageConstants.S_DISPLAY_DISABLE_BUTTON_IMG;
			 imgRptDisableBtn = ImageConstants.S_REPORT_DISABLE_BUTTON_IMG;
			 imgPendingInactBtn = ImageConstants.S_PENDING_INACTIVE_BUTTON_IMG;
			 imgManualInactiveBtn = ImageConstants.S_MANUAL_INACTIVE_BUTTON_IMG;
			 imgVoidInactiveBtn = ImageConstants.S_VOID_INACTIVE_BUTTON_IMG;
			 imgReprintInactiveBtn = ImageConstants.S_REPRINT_INACTIVE_BUTTON_IMG;
			 imgDisplayInactiveBtn = ImageConstants.S_DISPLAY_INACTIVE_BUTTON_IMG;
			 imgRptInactiveBtn = ImageConstants.S_REPORT_INACTIVE_BUTTON_IMG;
		} else {
			imgPendingActBtn = ImageConstants.PENDING_ACTIVE_BUTTON_IMG;
			 imgManualActBtn = ImageConstants.MANUAL_ACTIVE_BUTTON_IMG;
			 imgVoidActBtn = ImageConstants.VOID_ACTIVE_BUTTON_IMG;
			 imgReprintActBtn = ImageConstants.REPRINT_ACTIVE_BUTTON_IMG;
			 imgDisplayActBtn = ImageConstants.DISPLAY_ACTIVE_BUTTON_IMG;
			 imgRptActBtn = ImageConstants.REPORT_ACTIVE_BUTTON_IMG;
			 imgPendingDisableBtn = ImageConstants.PENDING_DISABLE_BUTTON_IMG;
			 imgManualDisableBtn = ImageConstants.MANUAL_DISABLE_BUTTON_IMG;
			 imgVoidDisableBtn = ImageConstants.VOID_DISABLE_BUTTON_IMG;
			 imgReprintDisableBtn = ImageConstants.REPRINT_DISABLE_BUTTON_IMG;
			 imgDispDisableBtn = ImageConstants.DISPLAY_DISABLE_BUTTON_IMG;
			 imgRptDisableBtn = ImageConstants.REPORT_DISABLE_BUTTON_IMG;
			imgPendingInactBtn = ImageConstants.PENDING_INACTIVE_BUTTON_IMG;
			imgManualInactiveBtn = ImageConstants.MANUAL_INACTIVE_BUTTON_IMG;
			imgVoidInactiveBtn = ImageConstants.VOID_INACTIVE_BUTTON_IMG;
			imgReprintInactiveBtn = ImageConstants.REPRINT_INACTIVE_BUTTON_IMG;
			imgDisplayInactiveBtn = ImageConstants.DISPLAY_INACTIVE_BUTTON_IMG;
			imgRptInactiveBtn = ImageConstants.REPORT_INACTIVE_BUTTON_IMG;
		}
				
		System.out.println("B4 creating the createTopHeaderComposite");
		createTopHeaderComposite(parent, style);
		parent.layout();
		System.out.println("AFTER  creating the createTopHeaderComposite");
		/** This if conditions is to load the active image when the default jackpot function called.*/
		if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PENDING_JACKPOT_ALLOWED).equalsIgnoreCase("yes"))
		{
			jackpotHeaderComposite.getLblPendingImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgPendingActBtn)));
			
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_ALLOWED).equalsIgnoreCase("yes"))
		{
			jackpotHeaderComposite.getLblManualImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgManualActBtn)));
			
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_ALLOWED).equalsIgnoreCase("yes"))
		{
			jackpotHeaderComposite.getLblVoidImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgVoidActBtn)));
			
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.REPRINT_ALLOWED).equalsIgnoreCase("yes"))
		{
			jackpotHeaderComposite.getLblReprintImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgReprintActBtn)));
			
		}
		else if(MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.DISPLAY_ALLOWED).equalsIgnoreCase("yes"))
		{
			jackpotHeaderComposite.getLblDisplayImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgDisplayActBtn)));
			
		}
		else 
		{
			jackpotHeaderComposite.getLblReportImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgRptActBtn)));			
		}	

		/**** This if conditions is to access the jackpot functions */
				
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.PENDING_JACKPOT_ALLOWED).equalsIgnoreCase("no")) {
				jackpotHeaderComposite.getLblPendingImage().setImage(createPendingDisabledImage());
				isPendingDisabled=true;
				
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.MANUAL_JACKPOT_ALLOWED).equalsIgnoreCase("no")) {
				jackpotHeaderComposite.getLblManualImage().setImage(createManualDisabledImage());
				isManualDisabled=true;
				
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.VOID_ALLOWED).equalsIgnoreCase("no")) {
			jackpotHeaderComposite.getLblVoidImage().setImage(createVoidDisabledImage());
			isVoidDisabled=true;
			
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.REPRINT_ALLOWED).equalsIgnoreCase("no")) {
			jackpotHeaderComposite.getLblReprintImage().setImage(createReprintDisabledImage());
			isReprintDisabled=true;
			
		}
		if (MainMenuController.jackpotSiteConfigParams.get(ISiteConfigConstants.DISPLAY_ALLOWED).equalsIgnoreCase("no")) {
			jackpotHeaderComposite.getLblDisplayImage().setImage(createDisplayDisabledImage());
			isDisplayDisabled=true;
			
		}
		super.registerEvents(jackpotHeaderComposite);
		registerCustomizedListeners(jackpotHeaderComposite);
	}

	/**
	 * Method to create the TopHeaderComposite
	 * 
	 * @param parent
	 * @param style
	 */
	private void createTopHeaderComposite(Composite parent, int style) {
		jackpotHeaderComposite = new JackpotHeaderComposite(parent, style);
	}

	@Override
	public Composite getComposite() {		
		return jackpotHeaderComposite;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		jackpotHeaderComposite.getLblPendingImage().addMouseListener(this);
		jackpotHeaderComposite.getLblManualImage().addMouseListener(this);
		jackpotHeaderComposite.getLblVoidImage().addMouseListener(this);
		jackpotHeaderComposite.getLblReprintImage().addMouseListener(this);
		jackpotHeaderComposite.getLblDisplayImage().addMouseListener(this);
		jackpotHeaderComposite.getLblReportImage().addMouseListener(this);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {		
		
		Control control = (Control) e.getSource();
		if (!((Control) e.getSource() instanceof TSButtonLabel)) {
			return;
		} else {
			if (((TSButtonLabel) control).getName().equalsIgnoreCase("pending"))
			{
				if(isPendingDisabled)
				{
					return;
				}
				try {
					
					if(MainMenuController.jackpotForm.isProcessStartedFlag())
					{
						boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS), jackpotHeaderComposite);
						log.info("Switch over from the current pending process: "+response);
						if(response){						
							if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
								TopMiddleController.getCurrentComposite().dispose();
							}
						}
						else{
							return;
						}
					}
					/** To clear the Jackpot Form fields before any Jackpot Process is called */
					ClearJackpotFormFields fields = new ClearJackpotFormFields();
					fields.clearJackpotFormFilelds();
					/** Highlighting the process that is selected */
					
					//new TopMiddleController((Composite)TopMainController.topMainComposite.getChildren()[1],SWT.NONE);
			
					
					MainMenuController.jackpotForm
							.setSelectedJackpotFunction("Pending");
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					
					new EmployeeShiftSlotDetailsController(
							TopMiddleController.jackpotMiddleComposite, SWT.NONE,
							new EmployeeShiftSlotDetailsForm(), new SDSValidator(getClass(),true));
					refreshIconImages();
					jackpotHeaderComposite.getLblPendingImage().setImage(new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(imgPendingActBtn)));
					//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.PENDING_JP_HEADING));

				} catch (Exception e1) {
					log.error(e1);
				}
			}
			else if(((TSButtonLabel) control).getName().equalsIgnoreCase("manual"))
			{
				if(isManualDisabled)
				{
					return;
				}
				try {
					
					if(MainMenuController.jackpotForm.isProcessStartedFlag())
					{
						boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS), jackpotHeaderComposite);
						log.info("Switch over from the current manual process: "+response);
						if(response){						
							if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
								TopMiddleController.getCurrentComposite().dispose();
							}
						}
						else{
							return;
						}
					}
					/** To clear the Jackpot Form fields before any Jackpot Process is called */
					ClearJackpotFormFields fields = new ClearJackpotFormFields();
					fields.clearJackpotFormFilelds();
				
					
					MainMenuController.jackpotForm
							.setSelectedJackpotFunction("Manual");
					
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					new ManualJPEmployeeSlotStandShiftController(TopMiddleController.jackpotMiddleComposite, SWT.NONE,
							new ManualJPEmployeeSlotStandShiftForm(), new SDSValidator(getClass(),true));
					refreshIconImages();
					jackpotHeaderComposite.getLblManualImage().setImage(new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(imgManualActBtn)));
					//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.MANUAL_JP_HEADING));

				} catch (Exception e1) {
					log.error(e1);
				}
			
				
			}
			else if(((TSButtonLabel) control).getName().equalsIgnoreCase("void"))
			{

				if(isVoidDisabled)
				{
					return;
				}
				try {
					
					if(MainMenuController.jackpotForm.isProcessStartedFlag())
					{
						boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS), jackpotHeaderComposite);
						log.info("Switch over from the current manual process: "+response);
						if(response){						
							if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
								TopMiddleController.getCurrentComposite().dispose();
							}
						}
						else{
							return;
						}
					}
					/** To clear the Jackpot Form fields before any Jackpot Process is called */
					ClearJackpotFormFields fields = new ClearJackpotFormFields();
					fields.clearJackpotFormFilelds();
				
					
					MainMenuController.jackpotForm
							.setSelectedJackpotFunction("Void");
					
				
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					new VoidController(TopMiddleController.jackpotMiddleComposite,
							SWT.NONE, new VoidForm(), new SDSValidator(getClass(),true));
					refreshIconImages();
					jackpotHeaderComposite.getLblVoidImage().setImage(new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(imgVoidActBtn)));
					//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.VOID_JP_HEADING));
				} catch (Exception e1) {
					log.error(e1);
				}
			}
			else if(((TSButtonLabel) control).getName().equalsIgnoreCase("reprint"))
			{
				if(isReprintDisabled)
				{
					return;
				}
				try {
					
					if(MainMenuController.jackpotForm.isProcessStartedFlag())
					{
						boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS), jackpotHeaderComposite);
						log.info("Switch over from the current manual process: "+response);
						if(response){						
							if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
								TopMiddleController.getCurrentComposite().dispose();
							}
						}
						else{
							return;
						}
					}
					/** To clear the Jackpot Form fields before any Jackpot Process is called */
					ClearJackpotFormFields fields = new ClearJackpotFormFields();
					fields.clearJackpotFormFilelds();
				
					MainMenuController.jackpotForm
							.setSelectedJackpotFunction("Reprint");
					
				
					
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					new ReprintController(
							TopMiddleController.jackpotMiddleComposite, SWT.NONE,
							new ReprintForm(), new SDSValidator(getClass(),true));
					refreshIconImages();
					jackpotHeaderComposite.getLblReprintImage().setImage(new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(imgReprintActBtn)));
					//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.REPRINT_JP_HEADING));
				} catch (Exception e1) {
					log.error(e1);
				}
			}
			else if(((TSButtonLabel) control).getName().equalsIgnoreCase("display"))
			{
				if(isDisplayDisabled)
				{
					return;
				}
				if(MainMenuController.jackpotForm.isProcessStartedFlag())
				{
					boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS), jackpotHeaderComposite);
					log.info("Switch over from the current manual process: "+response);
					if(response){						
						if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
							TopMiddleController.getCurrentComposite().dispose();
						}
					}
					else{
						return;
					}
				}
				/** To clear the Jackpot Form fields before any Jackpot Process is called */
				ClearJackpotFormFields fields = new ClearJackpotFormFields();
				fields.clearJackpotFormFilelds();
				MainMenuController.jackpotForm.setSelectedJackpotFunction("Display");
				try {
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					new DisplayJackpotController(
							TopMiddleController.jackpotMiddleComposite, SWT.NONE,
							new DisplayJackpotForm(), new SDSValidator(getClass(),true));
					refreshIconImages();
					jackpotHeaderComposite.getLblDisplayImage().setImage(new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(imgDisplayActBtn)));
					//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.DISPLAY_JP_HEADING));
				} catch (Exception e1) {
					log.error(e1);
				}
							
			}
			else if(((TSButtonLabel) control).getName().equalsIgnoreCase("report"))
			{
				if(isReportDisabled)
				{
					return;
				}
				if(MainMenuController.jackpotForm.isProcessStartedFlag())
				{
					boolean response = MessageDialogUtil.displayTouchScreenQuestionDialog(LabelLoader.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS), jackpotHeaderComposite);
					log.info("Switch over from the current manual process: "+response);
					if(response){						
						if(TopMiddleController.getCurrentComposite()!=null && !(TopMiddleController.getCurrentComposite().isDisposed())){
							TopMiddleController.getCurrentComposite().dispose();
						}
					}
					else{
						return;
					}
				}
				/** To clear the Jackpot Form fields before any Jackpot Process is called */
				ClearJackpotFormFields fields = new ClearJackpotFormFields();
				fields.clearJackpotFormFilelds();
				MainMenuController.jackpotForm.setSelectedJackpotFunction("Report");
				try {
					if (TopMiddleController.getCurrentComposite() != null
							&& !(TopMiddleController.getCurrentComposite()
									.isDisposed())) {
						TopMiddleController.getCurrentComposite().dispose();
					}
					new JackpotSlipReportController(
							TopMiddleController.jackpotMiddleComposite, SWT.NONE,
							new JackpotSlipReportForm(), new SDSValidator(getClass(),true));
					refreshIconImages();
					jackpotHeaderComposite.getLblReportImage().setImage(new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(imgRptActBtn)));
					//TopMiddleController.jackpotMiddleComposite.getLblHeading().setText(LabelLoader.getLabelValue(LabelKeyConstants.REPORT_JP_HEADING));
				} catch (Exception e1) {
					log.error(e1);
				}
							
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#keyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	@Override
	public void keyTraversed(TraverseEvent e) {
		
		if (!((Control) e.getSource() instanceof CbctlButton)) {
			return;
		} else {
			if (TopMiddleController.getCurrentComposite() != null
					&& !(TopMiddleController.getCurrentComposite().isDisposed())) {
				Control[] c = TopMiddleController.getCurrentComposite()
						.getChildren();
				for (int i = 0; i < c.length; i++) {
					if (c[i] instanceof Group) {
						c[i].redraw();
					}
				}
			}
		}
	}
	
	
	/**
	 * Method to refresh the icon images
	 */
	public void refreshIconImages()
	{
		if(isPendingDisabled)
		{
			jackpotHeaderComposite.getLblPendingImage().setImage(createPendingDisabledImage());
		}else {
			jackpotHeaderComposite.getLblPendingImage().setImage(new Image(Display.getCurrent(), getClass()
						.getResourceAsStream(imgPendingInactBtn)));
		}
		if(isManualDisabled)
		{
			jackpotHeaderComposite.getLblManualImage().setImage(createManualDisabledImage());
		}else {
			jackpotHeaderComposite.getLblManualImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgManualInactiveBtn)));
		}
		if(isVoidDisabled)
		{
			jackpotHeaderComposite.getLblVoidImage().setImage(createVoidDisabledImage());
		}else {
			jackpotHeaderComposite.getLblVoidImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgVoidInactiveBtn)));
		}
		if(isReprintDisabled)
		{
			jackpotHeaderComposite.getLblReprintImage().setImage(createReprintDisabledImage());
		}else {
			jackpotHeaderComposite.getLblReprintImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgReprintInactiveBtn)));
		}
		if(isDisplayDisabled)
		{
			jackpotHeaderComposite.getLblDisplayImage().setImage(createDisplayDisabledImage());
		}else {
			jackpotHeaderComposite.getLblDisplayImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgDisplayInactiveBtn)));
		}
		if(isReportDisabled)
		{
			jackpotHeaderComposite.getLblReportImage().setImage(createReportDisabledImage());
		}else {
			jackpotHeaderComposite.getLblReportImage().setImage(new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(imgRptInactiveBtn)));
		}
	
		
	}
	public Image createPendingDisabledImage(){
		pendingInactive = new Image(Display.getCurrent(),getClass().getResourceAsStream(imgPendingDisableBtn));
		return pendingInactive;
	}
	public Image createManualDisabledImage(){
		manualInactive = new Image(Display.getCurrent(),getClass().getResourceAsStream(imgManualDisableBtn));
		return manualInactive;
	}
	public Image createVoidDisabledImage(){
		voidInactive = new Image(Display.getCurrent(),getClass().getResourceAsStream(imgVoidDisableBtn));
		return voidInactive;
	}
	public Image createReprintDisabledImage(){
		reprintInactive = new Image(Display.getCurrent(),getClass().getResourceAsStream(imgReprintDisableBtn));
		return reprintInactive;
	}
	public Image createDisplayDisabledImage(){
		displayInactive = new Image(Display.getCurrent(),getClass().getResourceAsStream(imgDispDisableBtn));
		return displayInactive;
	}
	public Image createReportDisabledImage(){
		reportInactive = new Image(Display.getCurrent(),getClass().getResourceAsStream(imgRptDisableBtn));
		return reportInactive;
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		
		super.focusGained(e);
		//jackpotHeaderComposite.redraw();
	}
	
}
