/*****************************************************************************
 * $Id: ClearSlipsFormFields.java,v 1.4, 2011-01-24 09:17:14Z, Ambereen Drewitt$
 * $Date: 1/24/2011 3:17:14 AM$
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
package com.ballydev.sds.slipsui.util;

import org.apache.log4j.Logger;

import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.controller.MainMenuController;

/**
 * Class to clear the Slip's Form fields before any slip process is started
 * 
 * @author dambereen
 * @version $Revision: 5$
 */
public class ClearSlipsFormFields {
	/**
	 * Logger Instance
	 */
	private static final Logger log = SlipUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * Method that clears Jackpot Form Filelds
	 */
	public void clearSlipFormFilelds() {
		MainMenuController.slipForm.setAuthEmployeeIdOne("");
		MainMenuController.slipForm.setAuthEmployeeIdTwo("");
		MainMenuController.slipForm.setSelectedSlipFunction("");
		MainMenuController.slipForm.setSequenceNumber(0);
		MainMenuController.slipForm.setShift("");
		MainMenuController.slipForm.setSlotAttentantId("");
		MainMenuController.slipForm.setSlotAttentantName("");
		MainMenuController.slipForm.setSlotLocationNo("");
		MainMenuController.slipForm.setSlotNo("");
		MainMenuController.slipForm.setWindowNumber("");
		MainMenuController.slipForm.setDenomination(0);
		MainMenuController.slipForm.setAssetConfigurationId(new byte[0]);
		MainMenuController.slipForm.setTransactionDate(0);
		MainMenuController.slipForm.setSlipAmount(0);
		MainMenuController.slipForm.setProcessStartedFlag(false);		
		MainMenuController.slipForm.setCashlessAccountNumber(null);
//		MainMenuController.slipForm.setCashlessAccountType(null);
//		MainMenuController.slipForm.setCashableAmount(true);
		MainMenuController.slipForm.setReason(null);
		log.info("Jackpot form fields are cleared");
	}

}
