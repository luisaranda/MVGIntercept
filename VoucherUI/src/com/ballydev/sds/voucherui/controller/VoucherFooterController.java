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
package com.ballydev.sds.voucherui.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.TouchScreenUtility;
import com.ballydev.sds.framework.action.SignOutAction;
import com.ballydev.sds.framework.action.TouchScreenPreferenceAction;
import com.ballydev.sds.framework.control.SDSTSLabel;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.voucherui.composite.RedeemVoucherComposite;
import com.ballydev.sds.voucherui.composite.VoucherFooterComposite;
import com.ballydev.sds.voucherui.composite.VoucherMiddleComposite;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * Controller class for the VoucherFooterComposite
 * @author 
 * @version $Revision: 4$
 */
public class VoucherFooterController extends SDSBaseController 
{
	private VoucherFooterComposite VoucherFooterComposite;

	/**
	 * Logger Instance
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	private VoucherHomeController voucherHomeController;

	/**
	 * TopFooterController constructor
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public VoucherFooterController(VoucherHomeController voucherHomeController,Composite parent, int style) throws Exception 
	{
		super(new SDSForm(), null);
		this.voucherHomeController = voucherHomeController;
		createFooterComposite(parent, style);
		log.debug("inside the constructor of top foot controller");
		parent.layout();
		super.registerEvents(VoucherFooterComposite);
		registerCustomizedListeners(null);
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) 
	{
		Control control = (Control) e.getSource();
		if (!((Control) e.getSource() instanceof SDSTSLabel)) 
		{
			return;
		} 
		else{			
			if (((SDSTSLabel) control).getName().equalsIgnoreCase("home")){
				try 
				{
					//VoucherFooterComposite.getParent().dispose();
					RedeemNavCheck();
					TouchScreenUtility touchScreenUtility = new TouchScreenUtility();
					touchScreenUtility.displayTouchScreenHomePage();
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
			else if (((SDSTSLabel) control).getName().equalsIgnoreCase("logout"))
			{
				//VoucherFooterComposite.getParent().dispose();
				RedeemNavCheck();
				SignOutAction signOutAction = new SignOutAction();
				signOutAction.performAction();
			}
			else if (((SDSTSLabel) control).getName().equalsIgnoreCase("preference"))
			{

				TouchScreenPreferenceAction action = new TouchScreenPreferenceAction();
				action.performAction();
			}
			else if (((SDSTSLabel) control).getName().equalsIgnoreCase("keyboard"))
			{
				try {
					Shell keyBoardShell = new Shell();
					Rectangle screenBounds;
					Rectangle keyboardBounds;
					keyBoardShell = KeyBoardUtil.createKeyBoard();					
					screenBounds = voucherHomeController.getMiddleComposite().getBounds();
					keyboardBounds = keyBoardShell.getBounds();
					keyBoardShell.setLocation(keyboardBounds.x, screenBounds.height
							- keyboardBounds.height + 113);
					/*try {
						FocusUtility.setTextFocus(VoucherFooterComposite.getParent());
					} catch (RuntimeException e1) {
					}*/

				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}

			}
			else if(((SDSTSLabel) control).getName().equalsIgnoreCase("lblExit")) 
			{
				RedeemNavCheck();
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().close();
			}
		}

	}

	@Override
	public Composite getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerCustomizedListeners(Composite argComposite) 
	{
		VoucherFooterComposite.getLblHomeIcon().addMouseListener(this);
		VoucherFooterComposite.getLblLogoutIcon().addMouseListener(this);
		VoucherFooterComposite.getLblKeyBoardIcon().addMouseListener(this);
		VoucherFooterComposite.getLblPreferenceIcon().addMouseListener(this);
		VoucherFooterComposite.getLblExitIcon().addMouseListener(this);
	}

	/**
	 * Method to create the TopFooterComposite
	 * 
	 * @param parent
	 * @param style
	 */
	private void createFooterComposite(Composite parent, int style) 
	{
		VoucherFooterComposite = new VoucherFooterComposite(parent, style);

	}

	public static void RedeemNavCheck() {
		if (VoucherMiddleComposite.getCurrentComposite()  instanceof RedeemVoucherComposite) {
			RedeemVoucherComposite comp = (RedeemVoucherComposite)VoucherMiddleComposite.getCurrentComposite();
//			comp.getController().navCheck();
//			comp.getController().setNavCheckRequired(false);
			if(!comp.isDisposed())
				comp.dispose();
		}
	}
}
