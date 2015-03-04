/*****************************************************************************
 * $Id: TopFooterController.java,v 1.2, 2010-01-31 12:40:04Z, Suganthi, Kaliamoorthy$
 * $Date: 1/31/2010 6:40:04 AM$
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.ballydev.sds.framework.TouchScreenUtility;
import com.ballydev.sds.framework.action.SignOutAction;
import com.ballydev.sds.framework.action.TouchScreenPreferenceAction;
import com.ballydev.sds.framework.control.TSButtonLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.jackpotui.composite.JackpotFooterComposite;
import com.ballydev.sds.jackpotui.composite.MainMenuComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlButton;
import com.ballydev.sds.jackpotui.util.FocusUtility;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * Controller class for the TopFooterComposite
 * @author dambereen
 * @version $Revision: 3$
 */
public class TopFooterController extends SDSBaseController {

	
	/**
	 * JackpotFooterComposite instance
	 */
	private JackpotFooterComposite jackpotFooterComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * TopFooterController constructor
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public TopFooterController(Composite parent, int style) throws Exception {
		super(new SDSForm(), null);
		createFooterComposite(parent, style);
		log.debug("inside the constructor of top foot controller");
		//jackpotFooterComposite.getBtnPrinter().setVisible(false);
		//jackpotFooterComposite.getLblPrinter().setVisible(false);
		parent.layout();
		super.registerEvents(jackpotFooterComposite);
		registerCustomizedListeners(null);
		// TODO Auto-generated constructor stub
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
			if (((TSButtonLabel) control).getName().equalsIgnoreCase("home"))
			{

				try {

		
					//PlatformUI.getWorkbench().getActiveWorkbenchWindow().close();
					jackpotFooterComposite.getParent().getParent().dispose();
					TouchScreenUtility touchScreenUtility = new TouchScreenUtility();
					touchScreenUtility.displayTouchScreenHomePage();
					
					//JackpotUIPluginDescriptor.getOuter().setEnabled(true);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
			else if (((TSButtonLabel) control).getName().equalsIgnoreCase("logout"))
			{
				
				jackpotFooterComposite.getParent().getParent().dispose();
							
				SignOutAction signOutAction = new SignOutAction();
				signOutAction.performAction();
			
			}
			else if (((TSButtonLabel) control).getName().equalsIgnoreCase("preference"))
			{
				
				TouchScreenPreferenceAction action = new TouchScreenPreferenceAction();
				action.performAction();
			}
			else if (((TSButtonLabel) control).getName().equalsIgnoreCase("keyboard"))
			{

				try {
					Shell keyBoardShell = new Shell();
					Rectangle screenBounds;
					Rectangle keyboardBounds;
					keyBoardShell = KeyBoardUtil.createKeyBoard();
					screenBounds = MainMenuComposite.getCurrentComposite()
							.getBounds();
					keyboardBounds = keyBoardShell.getBounds();
					keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height
							- keyboardBounds.height + 113);
					try {
						FocusUtility.setTextFocus(jackpotFooterComposite.getParent());
						FocusUtility.focus = false;
					} catch (RuntimeException e1) {
					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
			
			
			
		}
		
		if (!((Control) e.getSource() instanceof CbctlButton)) {
			return;
		}
		if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.LOG_OUT_BUTTON)) {
				
			jackpotFooterComposite.getParent().getParent().getShell().dispose();
	
			SignOutAction signOutAction = new SignOutAction();
			signOutAction.performAction();
			
		} else if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.EXIT_BUTTON)) {
			try {/*

				//PlatformUI.getWorkbench().getActiveWorkbenchWindow().close();
				
				MainMenuComposite.getCurrentComposite().getParent().getParent()
						.dispose();
				TouchScreenUtility touchScreenUtility = new TouchScreenUtility();
				touchScreenUtility.displayTouchScreenHomePage();
				
				//JackpotUIPluginDescriptor.getOuter().setEnabled(true);

			*/} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.KEYBOARD_BUTTON)) {

			try {
				Shell keyBoardShell = new Shell();
				Rectangle screenBounds;
				Rectangle keyboardBounds;
				keyBoardShell = KeyBoardUtil.createKeyBoard();
				screenBounds = MainMenuComposite.getCurrentComposite()
						.getBounds();
				keyboardBounds = keyBoardShell.getBounds();
				// log.info("Keyboard Bounds:x-"+keyboardBounds.x+"
				// y-"+keyboardBounds.y+" width-"+keyboardBounds.width+"
				// height-"+keyboardBounds.height);
				// log.info("Screen Bounds:x-"+screenBounds.x+"
				// y-"+screenBounds.y+" width-"+screenBounds.width+"
				// height-"+screenBounds.height);
				keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height
						- keyboardBounds.height - 10);
				
				try {
					FocusUtility.setTextFocus(jackpotFooterComposite.getParent());
				} catch (RuntimeException e1) {
					// TODO Auto-generated catch block
				}
								
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public Composite getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		jackpotFooterComposite.getLblHomeIcon().addMouseListener(this);
		jackpotFooterComposite.getLblLogoutIcon().addMouseListener(this);
		jackpotFooterComposite.getLblKeyBoardIcon().addMouseListener(this);
		jackpotFooterComposite.getLblPreferenceIcon().addMouseListener(this);
		
	}

	/**
	 * Method to create the TopFooterComposite
	 * 
	 * @param parent
	 * @param style
	 */
	private void createFooterComposite(Composite parent, int style) {
		/*
		 * GridData gridData4 = new GridData();
		 * gridData4.grabExcessVerticalSpace = true;
		 * gridData4.grabExcessHorizontalSpace = true;
		 * gridData4.horizontalAlignment = GridData.FILL;
		 * gridData4.verticalAlignment = GridData.END;
		 */
		jackpotFooterComposite = new JackpotFooterComposite(parent, style);
		// jackpotFooterComposite.setLayoutData(gridData4);

	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#keyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	@Override
	public void keyTraversed(TraverseEvent e) {

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
