/*****************************************************************************
 * $Id: CallInitialScreenUtil.java,v 1.4, 2010-06-28 15:59:28Z, Subha Viswanathan$
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
package com.ballydev.sds.jackpotui.util;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.controller.DisplayJackpotController;
import com.ballydev.sds.jackpotui.controller.EmployeeShiftSlotDetailsController;
import com.ballydev.sds.jackpotui.controller.JackpotSlipReportController;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.controller.ManualJPEmployeeSlotStandShiftController;
import com.ballydev.sds.jackpotui.controller.ReprintController;
import com.ballydev.sds.jackpotui.controller.TopMiddleController;
import com.ballydev.sds.jackpotui.controller.VoidController;
import com.ballydev.sds.jackpotui.form.DisplayJackpotForm;
import com.ballydev.sds.jackpotui.form.EmployeeShiftSlotDetailsForm;
import com.ballydev.sds.jackpotui.form.JackpotSlipReportForm;
import com.ballydev.sds.jackpotui.form.ManualJPEmployeeSlotStandShiftForm;
import com.ballydev.sds.jackpotui.form.ReprintForm;
import com.ballydev.sds.jackpotui.form.VoidForm;

/**
 * Class to call the initial screen when commencing any jackpot process
 * @author anantharajr
 * @version $Revision: 5$
 */
public class CallInitialScreenUtil {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * To call PendingJPFirstScreen
	 */
	public void callPendingJPFirstScreen()
	{
		try {
			log.info("selected process: "+MainMenuController.jackpotForm.isProcessStartedFlag());
			/** To clear the Jackpot Form fields before any Jackpot Process is called */
			ClearJackpotFormFields fields = new ClearJackpotFormFields();
			fields.clearJackpotFormFilelds();
			/** Highlighting the process that is selected */
						
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
		} catch (Exception e1) {
			log.error(e1);
		}		
	}
	
	/**
	 *  To call ManualJPFirstScreen
	 */
	public void callManualJPFirstScreen()
	{
		 log.info("inside call first screen");
		
		try {
			log.info("selected process: "+MainMenuController.jackpotForm.isProcessStartedFlag());
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

		} catch (Exception e1) {
			log.error(e1);
		}
	}
	
	/**
	 *  To call VoidJPFirstScreen
	 */
	public void callVoidJPFirstScreen()
	{

		try {
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
		} catch (Exception e1) {
			log.error(e1);
		}
	
		
	}
	/**
	 *  To call ReprintJPFirstScreen
	 */
	public void callReprintJPFirstScreen()
	{
		try {
			log.info("selected process: "+MainMenuController.jackpotForm.isProcessStartedFlag());
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
		} catch (Exception e1) {
			log.error(e1);
		}
	
		
	}
	/**
	 *  To call DisplayJPFirstScreen
	 */
	public void  callDisplayJPFirstScreen()
	{
		
		log.info("selected process: "+MainMenuController.jackpotForm.isProcessStartedFlag());
		/** To clear the Jackpot Form fields before any Jackpot Process is called */
		ClearJackpotFormFields fields = new ClearJackpotFormFields();
		fields.clearJackpotFormFilelds();
		MainMenuController.jackpotForm
				.setSelectedJackpotFunction("Display");
		try {
			if (TopMiddleController.getCurrentComposite() != null
					&& !(TopMiddleController.getCurrentComposite()
							.isDisposed())) {
				TopMiddleController.getCurrentComposite().dispose();
			}
			new DisplayJackpotController(
					TopMiddleController.jackpotMiddleComposite, SWT.NONE,
					new DisplayJackpotForm(), new SDSValidator(getClass(),true));
		} catch (Exception e1) {
			log.error(e1);
		}
	}
	
	/**
	 * To call ReportJPFirstScreen
	 */
	public void callReportJPFirstScreen()
	{
		try {
			log.info("selected process: "+MainMenuController.jackpotForm.isProcessStartedFlag());
			/** To clear the Jackpot Form fields before any Jackpot Process is called */
			ClearJackpotFormFields fields = new ClearJackpotFormFields();
			fields.clearJackpotFormFilelds();
			/** Highlighting the process that is selected */
						
			MainMenuController.jackpotForm
					.setSelectedJackpotFunction("Report");
			if (TopMiddleController.getCurrentComposite() != null
					&& !(TopMiddleController.getCurrentComposite()
							.isDisposed())) {
				TopMiddleController.getCurrentComposite().dispose();
			}
			
			new JackpotSlipReportController(
					TopMiddleController.jackpotMiddleComposite, SWT.NONE,
					new JackpotSlipReportForm(), new SDSValidator(getClass(),true));
		} catch (Exception e1) {
			log.error(e1);
		}
	}
	
	
}
