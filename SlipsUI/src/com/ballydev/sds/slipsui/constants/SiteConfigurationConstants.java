/*****************************************************************************
 * $Id: SiteConfigurationConstants.java,v 1.0, 2008-04-21 05:44:34Z, Ambereen Drewitt$
 * $Date: 4/21/2008 12:44:34 AM$
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
package com.ballydev.sds.slipsui.constants;

/**
 * This class maintains the site configuration parameters info.
 * 
 * @author anantharajr
 * 
 */
public class SiteConfigurationConstants {

	public static final String PENDING_JACKPOT_ALLOWED = "yes";

	public static final String MANUAL_JACKPOT_ALLOWED = "no";

	public static final String VOID_ALLOWED = "yes";

	public static final String REPRINT_ALLOWED = "yes";

	public static final String DISPLAY_ALLOWED = "yes";

	public static final String PENDING_JACKPOT_PASSWORD_ENABLED = "yes";

	public static final String MANUAL_JACKPOT_PASSWORD_ENABLED = "yes";

	public static final String VOID_PASSWORD_ENABLED = "yes";

	public static final String REPRINT_PASSWORD_ENABLED = "yes";

	public static final String DISPLAY_PASSWORD_ENABLED = "yes";

	public static final String SLIP_FUNCTIONS_REQUIRES_EMPLOYEE_ID = "yes";

	public static final String SLIP_FUNCTIONS_REQUIRES_SIGNED_ON_CARDS = "yes";

	public static final int PROMPT_FOR_SLOT_NO_OR_SLOT_LOCATION = 1;

	public static final String PROMPT_FOR_SHIFT_OR_SINGLE_SHIFT = "P";

	public static final String SLIP_FUNCTIONS_ALLOWED_FOR_OFFLINE_SLOTS = "yes";

	public static final String TAX_WITHHOLDING_ENABLED = "no";

	public static final int STATE_TAX_RATE_FOR_JACKPOT = 0;

	public static final int FEDERAL_TAX_RATE_FOR_JAKCPOT = 0;

	public static final String EXPRESS_JACKPOT_ENABLED = "yes";

	public static final long EXPRESS_JACKPOT_LIMIT = 201;

	public static final String SYSTEM_CLEARS_JACKPOT_WHEN_SLIP_PRINTS = "yes";

	public static final String PENDING_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS = "yes";

	public static final String MANUAL_JACKPOT_REQUIRES_AUTH_EMPLOYEE_IDS = "yes";

	public static final String PROMPT_FOR_CREDIT_METER_HANDPAY_ON_JP = "yes";

	public static final String SEPARATE_REPORTING_FOR_PROMOTIONAL_JACKPOT = "yes";

	public static final int NUM_AUTH_SIGNATURES_FOR_EXCESSIVE_VARIANCES = 2;

	public static final int JP_DOLLAR_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS = 50;

	public static final int JP_PERCENT_VARIANCE_FOR_ADDITIONAL_AUTHORIZERS = 10;

	public static final String ENABLE_OVERRIDE_AUTH_LEVELS = "yes";
	
	public static final String REQUIRE_SUPERVISORY_AUTHORITY="no";
	
	public static final String JP_REQUIRES_VALID_EMP_ID_HP_JP_AUTH_AMOUNT="yes";
	
	public static final String SLIP_INSTALLED="yes";
	
	public static final String ALLOW_PLAYER_NAME_ON_JP_SLIPS="no";
	
	public static final String ALLOW_WINNING_COMB_ON_JP_SLIPS="yes";
	
	public static final String ALLOW_PAYLINE_ON_JP_SLIPS="yes";
	
	public static final String ALLOW_COINS_PLAYED_ON_JP_SLIPS="yes";
	
	public static final String ALLOW_WINDOW_NO_ON_JP_SLIPS="yes";
	
	public static final String ALLOW_MACHINE_PAY_ON_JP_SLIPS="yes";
	
	public static final String ALLOW_PLAYER_CARD_ON_JP_SLIPS="no";
	
	public static final String POUCH_PAY_JP_ENABLED="yes";

	public static final int MANUAL_JP_AMOUNT_REQUIRES_ADDITIONAL_AUTHORIZER=100;
	
	public static final String BLEED_PASSWORD_ENABLED="yes";//added for slips
	
	public static final String BEEF_PASSWORD_ENABLED="yes";//added for slips
	
	public static final String FILL_PASSWORD_ENABLED = "yes";//added for slips
	
	
}
