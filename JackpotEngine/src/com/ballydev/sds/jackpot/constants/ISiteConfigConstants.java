/*****************************************************************************
 * $Id: ISiteConfigConstants.java,v 1.14.1.0, 2012-05-01 13:12:48Z, SDS12.3.3 Checkin User$
 * $Date: 5/1/2012 8:12:48 AM$
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
package com.ballydev.sds.jackpot.constants;

/**
 * Interface that contains the Site Configuration Parameter Keys for Jackpot
 * @author dambereen
 * @version $Revision: 16$ 
 * 
 */
public interface ISiteConfigConstants {

	 String SYSTEM_CLEARS_JACKPOT_WHEN_SLIP_PRINTS = "SLIP.CLRS.JPTS.WHEN.SLIP.PRINTS";
	 
	 String CURRENCY_SYMBOL = "CURRENCY.SYMBOL";
	 
	 String SEND_MSG_PENDING_SLIP_UNDER_X_MIN_OLD = "SND.MSG.SLIPS.UNDR.X.MINS";
	 
	 String SEND_MSG_PENDING_SLIP_ATLEAST_Y_MIN_OLD = "SND.MSG.SLIPS.ATLST.Y.MINS";
	 
	 String JACKPOT_LICENSE_ENABLED = "MODULE.8.LIC.SITE.KEY";	 
	 
	 String USE_SDS_AMT_OR_PMU_AMT = "OVERWRITE.JKPT.SLIP.VAL.WITH.PMU";
	 
	 // SITE CONFIG PARAM FOR CASHIER DESK
	 String IS_CASHIER_DESK_ENABLED = "IS.CASHIER.DESK.ENABLED"; 
	 // SITE CONFIG PARAM FOR AUTO DEPOSIT TO ECASH ACCOUNT
	 String ALLOW_AUTO_DEPOSIT_TO_ECASH_ACCOUNT = "ALLOW.AUTO.DEPOSIT.TO.ECASH.ACCOUNT";
	 // SITE CONFIG PARAM FOR HPJP AMOUNT SHOULD BE VALID FOR EMPLOYEE
	 String JP_REQUIRES_VALID_HPJP_AMOUNT="HPJP.REQUIRES.VALID.HPJP.AMOUNT";
	 // SITE CONFIG PARAM FOR ENABLING PRINTING JACKPOT SLIP WITH CHECK
	 String ENABLE_CHECK_PRINT_FOR_JACKPOT_SLIP = "ENABLE.CHECK.PRINT.FOR.JACKPOT.SLIP";
	 // SITE CONFIG PARAM FOR GENERATING RANDOM SEQUENCE NUMBER
	 String GENERATE_RANDOM_SEQUENCE_NUMBER = "GENERATE.RANDOM.SEQUENCE.NUMBER";
	 
	 // Player Card Length
	 String PLAYER_CARD_NO_LENGTH = "PLAYER.CARD.NO.LENGTH";
	 
	// VOID VOUCHER OF FE JACKPOT
	 String IS_ALLOW_VOID_VOUCHER_ENABLED = "ALLOW.VOID.VOUCHER.OF.FE.JACKPOT"; 
	 
	 //PEERMONT REQUIREMENT TO AUTO VOID REPRINTED JACKPOT SLIP
	 String ALLOW_AUTO_VOID_ON_REPRINTED_JP = "JP.ALLOW.AUTO.VOID.ON.REPRINTED.JP";
	 String JACKPOT_AMOUNT_OVERIDE_AUTH_ENABLED = "JACKPOT.AMOUNT.OVERIDE.AUTH.ENABLED";
	 String JACKPOT_AMOUNT_OVERIDE_AUTH_LIMIT = "JACKPOT.AMOUNT.OVERIDE.AUTH.LIMIT";
	 String JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS = "JPT.DOLL.VAR.FOR.ADDITIONAL.AUTH";
	 String JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS = "JPT.PER.VAR.FOR.ADDITIONAL.AUTH";
}
