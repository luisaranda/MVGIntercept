/*****************************************************************************
 * $Id: TopHeaderController.java,v 1.8, 2010-01-28 09:56:36Z, Ambereen Drewitt$
 * $Date: 1/28/2010 3:56:36 AM$
 * $Log$
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
package com.ballydev.sds.slipsui.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.slipsui.composite.SlipsHeaderComposite;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.constants.ISiteConfigurationConstants;
import com.ballydev.sds.slipsui.constants.ImageConstants;
import com.ballydev.sds.slipsui.constants.MessageKeyConstants;
import com.ballydev.sds.slipsui.constants.UserFunctionsConstants;
import com.ballydev.sds.slipsui.controls.CbctlButton;
import com.ballydev.sds.slipsui.controls.TSButtonLabel;
import com.ballydev.sds.slipsui.util.CallInitialScreen;
import com.ballydev.sds.slipsui.util.SlipUILogger;

/**
 * Controller class for the TopHeaderComposite
 * 
 * @author anantharajr
 * @version $Revision: 9$
 */
public class TopHeaderController extends SDSBaseController {

	private SlipsHeaderComposite slipTopHeaderComposite;
	
	private Boolean isBeefDisabled = new Boolean(false);
	
	private Boolean isVoidDisabled = new Boolean(false);
	
	private Boolean isReprintDisabled = new Boolean(false);
	
	private Boolean isReportDisabled = new Boolean(false);

	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	public TopHeaderController(Composite parent, int style) throws Exception {
		super(new SDSForm(), null);
		createTopHeaderComposite(parent, style);
		
		/**
		 * This block of If - else condition will enables the image
		 * of the functionality that is allowed
		 */
		if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SHOULD_BEEF_BE_ALLOWED).equalsIgnoreCase("yes")) {
			if(Util.isSmallerResolution()) {
				slipTopHeaderComposite.getLblBeefImage().setImage(
						new Image(Display.getCurrent(), getClass()
								.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_BEEF_ACTIVE)));
			}
			else {
				slipTopHeaderComposite.getLblBeefImage().setImage(
					new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(ImageConstants.BEEF_ACTIVE)));
			
		}
		}else if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_BE_ALLOWED).equalsIgnoreCase("yes")) {
			if(Util.isSmallerResolution()) {
				slipTopHeaderComposite.getLblVoidImage().setImage(
						new Image(Display.getCurrent(), getClass()
								.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_VOID_ACTIVE)));
			}
			else {
				slipTopHeaderComposite.getLblVoidImage().setImage(
					new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(ImageConstants.VOID_ACTIVE)));
			}
		} else if (MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_BE_ALLOWED).equalsIgnoreCase("yes")) {
			if(Util.isSmallerResolution()) {
				slipTopHeaderComposite.getLblReprintImage().setImage(
						new Image(Display.getCurrent(), getClass()
								.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_REPRINT_ACTIVE)));
			}
			else {
				slipTopHeaderComposite.getLblReprintImage().setImage(
					new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(ImageConstants.REPRINT_ACTIVE)));
				
			}	
		} else if (UserFunctionsConstants.REPORT_ALLOWED) {
			
			if(Util.isSmallerResolution()) {
				slipTopHeaderComposite.getLblReportImage().setImage(
						new Image(Display.getCurrent(), getClass()
								.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_REPORT_ACTIVE)));
			}
			else {
				slipTopHeaderComposite.getLblReportImage().setImage(
							new Image(Display.getCurrent(), getClass()
											.getResourceAsStream(ImageConstants.REPORT_ACTIVE)));
				
				
			}
		}


		if (!MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.SHOULD_BEEF_BE_ALLOWED).equalsIgnoreCase("yes")) {
			isBeefDisabled = true;
			setDisabledBeefImage();
		}
		if (!MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.VOID_BE_ALLOWED).equalsIgnoreCase("yes")) {
			isVoidDisabled = true;
			setDisabledVoidImage();
		} 
		if (!MainMenuController.slipsSiteConfigParams.get(ISiteConfigurationConstants.REPRINT_BE_ALLOWED).equalsIgnoreCase("yes")) {
			isReprintDisabled = true;
			setDisabledReprintImage();
		} 

		if (!UserFunctionsConstants.REPORT_ALLOWED) {
			isReportDisabled = true;
			setDisabledReportImage();
		}
		
		log.info("Calling the framework's method to get the site id");
		try {

			SessionUtility sessionUtility = new SessionUtility();
			SiteDTO siteDTO = sessionUtility.getSiteDetails();
			if (siteDTO != null) {
				System.out.println(" Site Id : " + siteDTO.getId());
				MainMenuController.slipForm.setSiteId(siteDTO.getId());
				System.out
						.println("********************************************");
				System.out.println(" Site Id : " + siteDTO.getId());
				System.out
						.println("Site Short Name: " + siteDTO.getShortName());
				System.out
						.println("********************************************");
			} else {
				MessageDialogUtil
						.displayTouchScreenErrorMsgDialog(MessageKeyConstants.EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK);
			}
		} catch (Exception e) {
			MessageDialogUtil
					.displayTouchScreenErrorMsgDialog(MessageKeyConstants.EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK);
			log.error(e);
			return;
		}
		super.registerEvents(slipTopHeaderComposite);
		registerCustomizedListeners(null);
	}

	/**
	 * Code which runs for every click on the buttons in the Header
	 *
	 */
	private void init() {
		if (MainMenuController.slipForm.isProcessStartedFlag()) {
			boolean response = MessageDialogUtil
					.displayTouchScreenQuestionDialog(
							LabelLoader
									.getLabelValue(MessageKeyConstants.ABORT_AND_SWITCH_OVER_PROCESS),
							slipTopHeaderComposite);
			log.info("Switch over from the current manual process: "
					+ response);
			if (response) {
				if (TopMiddleController.getCurrentComposite() != null
						&& !(TopMiddleController.getCurrentComposite()
								.isDisposed())) {
					TopMiddleController.getCurrentComposite().dispose();
				}
			} else {
				return;
			}
		}

	}
	
	@Override
	public void mouseDown(MouseEvent e) {

		Control control = (Control) e.getSource();
		if (!((Control) e.getSource() instanceof TSButtonLabel)) {
			return;
		} else {
			CallInitialScreen initScreen = new CallInitialScreen();
			if (((TSButtonLabel) control).getName().equalsIgnoreCase(
					"beef")) {
				if(isBeefDisabled) {
					return ;
				}
				try {
					init();
					initScreen.callBeefScreen();
					refreshIconImages();
					if(Util.isSmallerResolution()) {
						slipTopHeaderComposite.getLblBeefImage().setImage(
								new Image(Display.getCurrent(),
										getClass().getResourceAsStream(
												ImageConstants.SMALL_RESOLUTION_BEEF_ACTIVE)));
					}
					else {
					slipTopHeaderComposite.getLblBeefImage().setImage(
							new Image(Display.getCurrent(),
									getClass().getResourceAsStream(
											ImageConstants.BEEF_ACTIVE)));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			} else if (((TSButtonLabel) control).getName().equalsIgnoreCase(
					"void")) {
				if(isVoidDisabled) {
					return;
				}
				try {
					init();
					/**
					 * To clear the Jackpot Form fields before any Jackpot
					 * Process is called
					 */
					initScreen.callVoidScreen();
					refreshIconImages();
					if(Util.isSmallerResolution()) {
						slipTopHeaderComposite.getLblVoidImage().setImage(
								new Image(Display.getCurrent(),
										getClass().getResourceAsStream(
												ImageConstants.SMALL_RESOLUTION_VOID_ACTIVE)));
					}
					else {
					slipTopHeaderComposite.getLblVoidImage().setImage(
							new Image(Display.getCurrent(), getClass()
									.getResourceAsStream(
											ImageConstants.VOID_ACTIVE)));
					}
				} catch (Exception e1) {
					
					e1.printStackTrace();
				}
			} else if (((TSButtonLabel) control).getName().equalsIgnoreCase(
					"reprint")) {
				if(isReprintDisabled) {
					return;
				}
				try {

					init();
					/**
					 * To clear the Jackpot Form fields before any Jackpot
					 * Process is called
					 */
					initScreen.callReprintScreen();
					refreshIconImages();
					if(Util.isSmallerResolution()) {
						slipTopHeaderComposite.getLblReprintImage().setImage(
								new Image(Display.getCurrent(),
										getClass().getResourceAsStream(
												ImageConstants.SMALL_RESOLUTION_REPRINT_ACTIVE)));
					}
					else {
					slipTopHeaderComposite.getLblReprintImage().setImage(
							new Image(Display.getCurrent(), getClass()
									.getResourceAsStream(
											ImageConstants.REPRINT_ACTIVE)));
					}
				} catch (Exception e1) {
					
					e1.printStackTrace();
				}
			} else if (((TSButtonLabel) control).getName().equalsIgnoreCase(
					"report")) {
				if(isReportDisabled) {
					return;				
				}
				try {
					init();
					/**
					 * To clear the Jackpot Form fields before any Jackpot Process
					 * is called
					 */
					initScreen.callReportScreen();
					refreshIconImages();
					if(Util.isSmallerResolution()) {
					slipTopHeaderComposite.getLblReportImage().setImage(
							new Image(Display.getCurrent(), getClass()
									.getResourceAsStream(
											ImageConstants.SMALL_RESOLUTION_REPORT_ACTIVE)));
					}else {
						slipTopHeaderComposite.getLblReportImage().setImage(
								new Image(Display.getCurrent(), getClass()
										.getResourceAsStream(
												ImageConstants.REPORT_ACTIVE)));
					}
				} catch (Exception e1) {
					log.error(e1);
				}
			}
		}
	}

	/**
	 * Method to create the TopHeaderComposite
	 * 
	 * @param parent
	 * @param style
	 */
	private void createTopHeaderComposite(Composite parent, int style) {
		slipTopHeaderComposite = new SlipsHeaderComposite(parent, style);
	}

	@Override
	public Composite getComposite() {
		
		return null;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {		
		slipTopHeaderComposite.getLblBeefImage().addMouseListener(this);		
		slipTopHeaderComposite.getLblVoidImage().addMouseListener(this);
		slipTopHeaderComposite.getLblReprintImage().addMouseListener(this);
		slipTopHeaderComposite.getLblReportImage().addMouseListener(this);
	}

	@Override
	public void keyTraversed(TraverseEvent e) {
		Control control = (Control) e.getSource();

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

	public void refreshIconImages() {
		if(isBeefDisabled) {
			setDisabledBeefImage();
		} else {
			if(Util.isSmallerResolution()){
				slipTopHeaderComposite.getLblBeefImage().setImage(
						new Image(Display.getCurrent(), getClass().getResourceAsStream(
								ImageConstants.SMALL_RESOLUTION_BEEF_INACTIVE)));
			}
			
			else {	
			slipTopHeaderComposite.getLblBeefImage().setImage(
					new Image(Display.getCurrent(), getClass().getResourceAsStream(
							ImageConstants.BEEF_INACTIVE_BUTTON_IMG)));
			}
			
		}
		if(isVoidDisabled) {
			setDisabledVoidImage();
		} else {
			if(Util.isSmallerResolution()){
				slipTopHeaderComposite.getLblVoidImage().setImage(
						new Image(Display.getCurrent(), getClass().getResourceAsStream(
								ImageConstants.SMALL_RESOLUTION_VOID_INACTIVE_BUTTON_IMG)));
			}
			else {
				slipTopHeaderComposite.getLblVoidImage().setImage(
					new Image(Display.getCurrent(), getClass().getResourceAsStream(
							ImageConstants.VOID_INACTIVE_BUTTON_IMG)));
			}
		}
		if(isReprintDisabled) {
			setDisabledReprintImage();
		} else {
			if(Util.isSmallerResolution()){
				slipTopHeaderComposite.getLblReprintImage().setImage(
						new Image(Display.getCurrent(), getClass().getResourceAsStream(
								ImageConstants.SMALL_RESOLUTION_REPRINT_INACTIVE_BUTTON_IMG)));
			}
			else {
			slipTopHeaderComposite.getLblReprintImage().setImage(
					new Image(Display.getCurrent(), getClass().getResourceAsStream(
							ImageConstants.REPRINT_INACTIVE_BUTTON_IMG)));
			}	
		}

		if(isReportDisabled) {
			setDisabledReportImage();
		} else {
			if(Util.isSmallerResolution()){
				slipTopHeaderComposite.getLblReportImage().setImage(
						new Image(Display.getCurrent(), getClass().getResourceAsStream(
								ImageConstants.SMALL_RESOLUTION_REPORT_INACTIVE)));
			}
			else {
			slipTopHeaderComposite.getLblReportImage().setImage(
					new Image(Display.getCurrent(), getClass().getResourceAsStream(
							ImageConstants.REPORT_INACTIVE_BUTTON_IMG)));
			}
		}		
	}
	
	private void setDisabledBeefImage() {
		if(Util.isSmallerResolution()) {
		slipTopHeaderComposite.getLblBeefImage().setImage(
					new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_BEEF_DISABLED)));
			
		}	
		else {
		slipTopHeaderComposite.getLblBeefImage().setImage(
				new Image(Display.getCurrent(), getClass()
						.getResourceAsStream(ImageConstants.BEEF_DISABLED))); 
		
		}
	}
	
	private void setDisabledVoidImage() {
		if(Util.isSmallerResolution()){
				slipTopHeaderComposite.getLblVoidImage().setImage(
				new Image(Display.getCurrent(), getClass()
						.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_VOID_DISABLED)));
		}
		else {
			slipTopHeaderComposite.getLblVoidImage().setImage(
					new Image(Display.getCurrent(), getClass()
							.getResourceAsStream(ImageConstants.VOID_DISABLED)));
		}
	}
	
	private void setDisabledReprintImage(){
		if(Util.isSmallerResolution()){
			slipTopHeaderComposite.getLblReprintImage().setImage(
			new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_REPRINT_DISABLED)));
		}
		else {
		slipTopHeaderComposite.getLblReprintImage().setImage(
				new Image(Display.getCurrent(), getClass()
						.getResourceAsStream(
								ImageConstants.REPRINT_DISABLED)));
		}
	}		
		
	private void setDisabledReportImage() {
		if(Util.isSmallerResolution()){
			slipTopHeaderComposite.getLblReportImage().setImage(
			new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(ImageConstants.SMALL_RESOLUTION_REPORT_DISABLED)));
		}
		else {
		
		slipTopHeaderComposite.getLblReportImage().setImage(
				new Image(Display.getCurrent(), getClass()
						.getResourceAsStream(
								ImageConstants.REPORT_DISABLED)));
			}
	}
}
