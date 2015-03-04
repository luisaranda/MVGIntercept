/*****************************************************************************
 * $Id: CallInitialScreen.java,v 1.2, 2010-01-28 09:56:36Z, Ambereen Drewitt$
 * $Date: 1/28/2010 3:56:36 AM$
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
package com.ballydev.sds.slipsui.util;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import com.ballydev.sds.framework.validator.SDSValidator;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.controller.BeefController;
import com.ballydev.sds.slipsui.controller.MainMenuController;
import com.ballydev.sds.slipsui.controller.ReportController;
import com.ballydev.sds.slipsui.controller.ReprintController;
import com.ballydev.sds.slipsui.controller.TopMiddleController;
import com.ballydev.sds.slipsui.controller.VoidController;
import com.ballydev.sds.slipsui.form.BeefForm;
import com.ballydev.sds.slipsui.form.ReportForm;
import com.ballydev.sds.slipsui.form.ReprintForm;
import com.ballydev.sds.slipsui.form.VoidForm;

/**
 * @author gsrinivasulu
 *
 */
public class CallInitialScreen {

	private static final Logger logger = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	private void initialize(String functionName) {
		ClearSlipsFormFields fields = new ClearSlipsFormFields();
		fields.clearSlipFormFilelds();
		MainMenuController.slipForm.setSelectedSlipFunction(functionName);

		if(TopMiddleController.getCurrentComposite() != null &&
				!(TopMiddleController.getCurrentComposite().isDisposed())) {
			TopMiddleController.getCurrentComposite().dispose();
		}
	}
	
	public void callBeefScreen() {
		logger.info("Calling the Beef`s Initial Screen");
		try {
			initialize(IAppConstants.BEEF_FUNCTION_NAME);
			new BeefController(TopMiddleController.slipMiddleComposite, SWT.NONE,
					new BeefForm(), new SDSValidator(getClass(),true));

		} catch (Throwable e) {
			logger.error("Failed to set the Beef`s Initial Screen ",e);
			e.printStackTrace();
		}
	}
	 

	public void callVoidScreen() {
		logger.info("Calling the Void`s Initial Screen");
		try {
			initialize(IAppConstants.VOID_FUNCTION_NAME);
			new VoidController(TopMiddleController.slipMiddleComposite,
					SWT.NONE, new VoidForm(), new SDSValidator(getClass(),true));
			
		} catch (Exception e) {
			logger.error("Failed to set the Reprint`s Initial Screen ",e);
		}
		
	}
	
	public void callReprintScreen() {
		logger.info("Calling the Reprint`s Initial Screen");
		try {
			initialize(IAppConstants.REPRINT_FUNCTION_NAME);
			new ReprintController(
					TopMiddleController.slipMiddleComposite, SWT.NONE,
					new ReprintForm(), new SDSValidator(getClass(),true));

		} catch (Exception e) {
			logger.error("Failed to set the Reprint`s Initial Screen ",e);
		}
	}
	
	public void callReportScreen() {
		logger.info("Calling the Report`s Initial Screen");
		try {
			initialize(IAppConstants.REPORT_FUNCTION_NAME);	
			new ReportController(
					TopMiddleController.slipMiddleComposite, SWT.NONE,
					new ReportForm(), new SDSValidator(getClass(),true));

		} catch (Exception e) {
			logger.error("Failed to set the Display`s Initial Screen ",e);
		}
	}

}
