/*****************************************************************************
 * $Id: ClearJackpotFormFields.java,v 1.25.1.3.1.0, 2013-10-14 16:34:33Z, SDS12.3.3 Checkin User$
 * $Date: 10/14/2013 11:34:33 AM$
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

import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.LabelKeyConstants;
import com.ballydev.sds.jackpotui.controller.AmountSlotAttendantIdController;
import com.ballydev.sds.jackpotui.controller.AuthorizationTaxMPAmountController;
import com.ballydev.sds.jackpotui.controller.EmployeeShiftSlotDetailsController;
import com.ballydev.sds.jackpotui.controller.MainMenuController;
import com.ballydev.sds.jackpotui.controller.ManualJPAuthMachineAmtTaxController;
import com.ballydev.sds.jackpotui.controller.ManualJPEmployeeSlotStandShiftController;
import com.ballydev.sds.jackpotui.controller.ManualJPHandPaidAmtPromoJackpotTypeController;
import com.ballydev.sds.jackpotui.controller.PendingJackpotDisplayController;

/**
 * Class to clear the Jackpot Form fields before any jackpot process is started
 * 
 * @author dambereen
 * @version $Revision: 31$
 */
public class ClearJackpotFormFields {
	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);

	/**
	 * Method that clears Jackpot Form Filelds
	 */
	public void clearJackpotFormFilelds() {
		MainMenuController.jackpotForm.setAuthEmployeeIdOne("");
		MainMenuController.jackpotForm.setAuthEmployeeIdTwo("");
		MainMenuController.jackpotForm.setBlindAttempts((short) 0);
		MainMenuController.jackpotForm.setCoinsPlayed(0);
		MainMenuController.jackpotForm.setEmployeeIdPrintedSlip(getLoggedInUser());
		MainMenuController.jackpotForm.setVoidEmployeeId(getLoggedInUser());
		MainMenuController.jackpotForm.setHandPaidAmount(0);
		MainMenuController.jackpotForm.setJackpotID("");
		// MainMenuController.jackpotForm.setMachinePaidAmount(0);
		MainMenuController.jackpotForm.setOriginalAmount(0);
		MainMenuController.jackpotForm.setPayline("");
		MainMenuController.jackpotForm.setAssociatedPlayerCard(IAppConstants.DEFAULT_PLAYER_CARD);
		// MainMenuController.jackpotForm.setPlayerCard(IAppConstants.PLAYER_CARD_VALUE);
		MainMenuController.jackpotForm.setPlayerCard(null);
		MainMenuController.jackpotForm.setPlayerName("");
		MainMenuController.jackpotForm.setSelectedJackpotFunction("");
		MainMenuController.jackpotForm.setSequenceNumber(0);
		MainMenuController.jackpotForm.setShift("");
		MainMenuController.jackpotForm.setSlotAttentantId("");
		MainMenuController.jackpotForm.setSlotAttentantName("");
		MainMenuController.jackpotForm.setSlotLocationNo("");
		MainMenuController.jackpotForm.setSlotNo("");
		MainMenuController.jackpotForm.setTaxID((short) 5001);
		MainMenuController.jackpotForm.setWindowNumber("");
		MainMenuController.jackpotForm.setWinningCombination("");
		MainMenuController.jackpotForm.setDenomination("");
		MainMenuController.jackpotForm.setTransactionDate(0);
		MainMenuController.jackpotForm.setPayTableId(0);
		MainMenuController.jackpotForm.setThemeName("");
		MainMenuController.jackpotForm.setSequenceNoOrAll("");
		MainMenuController.jackpotForm.setTaxType(LabelKeyConstants.NO_TAX_LABEL);
		MainMenuController.jackpotForm.setTaxRateAmount(IAppConstants.JKPT_TAX_RATE_AMNT_CLEAR_VALUE);
		MainMenuController.jackpotForm.setTaxDeductedAmount(0);
		MainMenuController.jackpotForm.setJackpotNetAmount(0);
		MainMenuController.jackpotForm.setExpressSuccess(false);
		MainMenuController.jackpotForm.setPouchPay(false);
		MainMenuController.jackpotForm.setAuthorizationRequired(false);
		MainMenuController.jackpotForm.setProcessAsExpress(false);
		MainMenuController.jackpotForm.setProcessStartedFlag(false);
		MainMenuController.jackpotForm.setPromotionalJackpot(false);
		MainMenuController.jackpotForm.setJackpotTypeId((short) 7001);
		MainMenuController.jackpotForm.setGmuDenom(0);
		MainMenuController.jackpotForm.setProcessFlagId((short) 2001);
		MainMenuController.jackpotForm.setRoundedHPJPAmount(0);

		MainMenuController.jackpotForm.setSlotAttentantFirstName("");
		MainMenuController.jackpotForm.setSlotAttentantLastName("");
		MainMenuController.jackpotForm.setSlotAttentantName("");
		
		MainMenuController.jackpotForm.setInsertPouchPayAttn(false);
		MainMenuController.jackpotForm.setPrintBarcode(false);
		MainMenuController.jackpotForm.setSealNumber("");

		MainMenuController.jackpotForm.setPendingDisplayFilter("");

		MainMenuController.jackpotForm.setShowSlotAttnId(false);
		MainMenuController.jackpotForm.setMessageId(0);
		// RESETTING CASHLESS ACCOUNT NUMBER AND ACCOUNT TYPE
		MainMenuController.jackpotForm.setAccountNumber(null);
		MainMenuController.jackpotForm.setAccountType("");
		// RESETTING THE EXPIRY DATE
		MainMenuController.jackpotForm.setExpiryDate(null);
		// RESETTING THE PROGRESSIVE LEVEL
		MainMenuController.jackpotForm.setLstProgressiveLevel(null);
		// RESETTING THE DISPLAY PENDING HPJP AMT FIELD AS MASKED TO FALSE BY DEF - NJ ENHANCE CHGS
		MainMenuController.jackpotForm.setDisplayMaskAmt(false);
		// RESETTING THE EXP OVERRIDE AUTH REQ FIELD TO False BY DEF - NJ ENHANCE CHGS
		MainMenuController.jackpotForm.setEmpJobJpLimitExceededAuthReq(false);
		//FOR PROG CONTROLLER INITIATED JPS- AS ACC NO INFO IS NOT KNOWN TO THE CONTROLLER, IF REQUIRED USER HAS TO ENTER
		MainMenuController.jackpotForm.setJpProgControllerGenerated(false);
		
		// SETTING THE PENDING JACKPOT FORMS TO NULL ON A NEW PROCESS (done to
		// help in the previous btn action)
		EmployeeShiftSlotDetailsController.form = null;
		PendingJackpotDisplayController.form = null;
		AmountSlotAttendantIdController.form = null;
		AuthorizationTaxMPAmountController.form = null;

		// SETTING THE PENDING JACKPOT FORMS TO NULL ON A NEW PROCESS (done to
		// help in the previous btn action)
		ManualJPEmployeeSlotStandShiftController.form = null;
		ManualJPHandPaidAmtPromoJackpotTypeController.form = null;
		ManualJPAuthMachineAmtTaxController.form = null;
		if (log.isInfoEnabled()) {
			log.info("Jackpot form fields are cleared");
		}
	}

	/**
	 * Method to get the logged in user
	 * 
	 * @return
	 */
	private String getLoggedInUser() {
		SessionUtility sessionUtility = new SessionUtility();
		String userName = sessionUtility.getUserDetails().getUserName();
		return userName;
	}

}
