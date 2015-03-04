/*****************************************************************************
 * $Id: TSMenuController.java,v 1.0, 2008-04-03 15:53:56Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:53:56 AM$
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

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.form.SDSForm;
import com.ballydev.sds.jackpotui.JackpotUIPluginDescriptor;
import com.ballydev.sds.jackpotui.composite.MainMenuComposite;
import com.ballydev.sds.jackpotui.composite.TSMenuComposite;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controls.CbctlButton;
import com.ballydev.sds.jackpotui.form.JackpotForm;
import com.ballydev.sds.jackpotui.util.JackpotUILogger;

/**
 * The controller class for Main Menu Composite
 * 
 * @author dambereen
 * @version $Revision: 1$
 */
public class TSMenuController extends SDSBaseController {

	/**
	 * TSMenuComposite instance
	 */
	private TSMenuComposite composite = null;

	/**
	 * Static jackpot form instance
	 */
	public static JackpotForm jackpotForm = new JackpotForm();
	
	/**
	 * jackpotSiteConfigParams HashMap instance
	 */
	public static HashMap<String ,String> jackpotSiteConfigParams = new HashMap<String, String>(); 

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * MainMenuController constructor
	 * 
	 * @param parent
	 * @param style
	 * @throws Exception
	 */
	public TSMenuController(Composite parent, int style) throws Exception {
		super(new SDSForm(), null);
		createMainMenuComposite(parent, style);
		MainMenuComposite.setCurrentComposite(composite);
		parent.layout();
		super.registerEvents(composite);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Control control = (Control) e.getSource();

		if (!((Control) e.getSource() instanceof CbctlButton)) {
			return;
		}

		if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.SLIPS_UI_BUTTON)) {
			log.debug("************ inside SLipsUI:**********");

		} else if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.JACKPOT_UI_BUTTON)) {
			log.debug("************ inside jackpot UI:**********");
			
		} else if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.VOUCHER_UI_BUTTON)) {
			log.debug("************ inside VoucherUI:**********");

		} else if (((CbctlButton) control).getName().equalsIgnoreCase(
				LabelKeyConstants.EXIT_BUTTON)) {
			log.debug("************ inside Exit Button:**********");
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().close();
			MainMenuComposite.getCurrentComposite().getParent().getParent()
					.dispose();
			JackpotUIPluginDescriptor.getOuter().setEnabled(true);
		}
	}

	/**
	 * Method to create the MainMenuComposite
	 * 
	 * @param parent
	 * @param style
	 */
	private void createMainMenuComposite(Composite parent, int style) {
		composite = new TSMenuComposite(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#getComposite()
	 */
	@Override
	public Composite getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ballydev.sds.framework.controller.SDSBaseController#registerCustomizedListeners(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void registerCustomizedListeners(Composite argComposite) {
		// TODO Auto-generated method stub

	}

}
