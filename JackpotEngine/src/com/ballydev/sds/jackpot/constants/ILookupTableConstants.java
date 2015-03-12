/*****************************************************************************
 * $Id: ILookupTableConstants.java,v 1.14.1.0, 2011-05-17 19:01:19Z, SDS12.3.2CheckinUser$
 * $Date: 5/17/2011 2:01:19 PM$
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
 * Intterface that contains the look up table constants
 * 
 * @author dambereen
 * @version $Revision: 16$
 */
public interface ILookupTableConstants {

	/*
	 * STATUS FLAG TABLE
	 */

	/** Pending Slip Status */
	short PENDING_STATUS_ID = 4001;

	/** Processed Slip Status */
	short PROCESSED_STATUS_ID = 4002;

	/** Void Slip Status */
	short VOID_STATUS_ID = 4003;

	/** CreditKeyOff Slip Status */
	short CREDIT_KEY_OFF_STATUS_ID = 4004;

	/** Reprint Slip Status */
	short REPRINT_STATUS_ID = 4005;

	/** Invalid Slip Status */
	short INVALID_STATUS_ID = 4006;

	/** Status Flag - Card In */
	short CARD_IN_STATUS_ID = 4007;

	/** Status Flag - Jackpot Clear */
	short JP_CLEAR_STATUS_ID = 4009;

	/** Status Flag - Mechanics Delta */
	short MECHANICS_DELTA_STATUS_ID = 4010;

	/** Status Flag - Change */
	short CHANGE_STATUS_ID = 4011;

	/** Status Flag - Pouch Pay Attendant */
	short POUCH_PAY_ATTN_STATUS_ID = 4012;

	// Status Flag - Credit Key Off Auth
	short CREDIT_KEY_OFF_AUTH_STATUS_ID = 4013;

	/** Status Flag - Printed status */
	short PRINTED_STATUS_ID = 4014;

	/*
	 * SLIP TYPE TABLE
	 */

	/** Jackpot Slip Type */
	short JACKPOT_SLIP_TYPE_ID = 3001;

	/*
	 * AUDIT CODE TABLE
	 */

	/** Audit Code Id - Cancelled by Unknown Employee */
	short AUDIT_CODE_CANCELLED_BY_UNKNOWN_EMP = 1001;

	/** Audit Code Id - Cancelled by Unauthorized Employee */
	short AUDIT_CODE_CANCELLED_BY_UNAUTHORIZED_EMP = 1002;

	/** Audit Code Id - Slip was printed */
	short AUDIT_CODE_SLIP_WAS_PRINTED = 1003;

	/** Audit Code Id - No matching Jp record */
	short AUDIT_CODE_NO_MATCHING_JP_RECORD = 1004;

	/** Audit Code Id - Cancelled Jp Invalid Jp Record */
	short AUDIT_CODE_INVALID_JP_RECORD = 1005;

	/** Audit Code Id - Cancelled Jp Invalid Jp Record */
	short AUDIT_CODE_GAME_DATE_NOT_MATCHING_JP_REC = 1006;

	/** Audit Code Id - Amount does not match Jp Record */
	short AUDIT_CODE_AMT_NOT_MATCHING = 1007;

	/** Audit Code Id - JPID does not match Jp Record */
	short AUDIT_CODE_JPID_NOT_MATCHING = 1008;

	/** Audit Code Id - Success */
	short AUDIT_CODE_SUCCESS = 1009;

	/*
	 * POST FLAG TABLE
	 */

	/** Posted Flag Id */
	short POSTED_FLAG_ID = 6001;

	/** Not Posted Flag Id */
	short NOT_POSTED_FLAG_ID = 6002;

	/** Auto Authorize Posted Flag Id */
	short AUTO_AUTHORIZE_POSTED = 6003;

	/** Auto Authorize Not Posted Flag Id */
	short AUTO_AUTHORIZE_NOT_POSTED = 6004;

	/*
	 * JACKPOT TYPE TABLE
	 */

	/** Jackpot Type - Regular Jackpot */
	short JACKPOT_TYPE_REGULAR = 7001;

	/** Jackpot Type - Promotional Jackpot */
	short JACKPOT_TYPE_PROMOTIONAL = 7002;

	/** Jackpot Type - Progressive Jackpot */
	short JACKPOT_TYPE_PROGRESSIVE = 7003;

	/** Jackpot Type - Linked Progressive Jackpot */
	short JACKPOT_TYPE_lINKED_PROGRESSIVE = 7004;

	/** Jackpot Type - System Game Jackpot */
	short JACKPOT_TYPE_SYSTEM_GAME = 7005;

	/** Jackpot Type - Cancel Credit Jackpot */
	short JACKPOT_TYPE_CANCEL_CREDIT = 7006;

	/** Jackpot Type - Mystery Jackpot */
	short JACKPOT_TYPE_MYSTERY = 7007;

	/*
	 * PROCESS_FLAG TABLE
	 */
	short MANUAL_PROCESS_FLAG = 2005;

	short POUCH_PAY_PROCESS_FLAG = 2002;

	short EXPRESS_PROCESS_FLAG = 2003;

	/*
	 * EXCEPTION_CODE TABLE
	 */
	/** Exception Code - Hand Paid Jackpot */
	short EXCEPTION_HAND_PAID_JP = 8001;

	/** Exception Code - Jackpot To Credit Meter */
	short EXCEPTION_JP_TO_CREDIT_MTR = 8002;

	/** Exception Code -Employee Card In */
	short EXCEPTION_EMPLOYEE_CARD_IN = 8003;

	/** Exception Code - Jackpot Clear */
	short EXCEPTION_JP_CLEAR = 8004;

	/** Exception Code - Jackpot Slip Posted */
	short EXCEPTION_JP_SLIP_POSTED = 8005;

	/** Exception Code - Credit Key Off Auth */
	short EXCEPTION_JP_CREDIT_KEY_OFF_AUTH = 8009;

	/** Exception Code - Jackpot Slip Printed */
	short EXCEPTION_JP_SLIP_PRINTED = 8010;
	
	/** Exception Code - HPJP From External System */
	short EXCEPTION_JP_HANDPAID_FROM_EXT_SYS = 8014;

	/*
	 * ECASH ACCOUNT TYPE
	 */
	int ACCOUNT_TYPE_CASHLESS_ID = 1;

	String ACCOUNT_TYPE_CARDLESS = "AXCS.TYPE.CARDLESS"; // "Cardless"; VALUE =
															// 1;

	int ACCOUNT_TYPE_SMART_CARD_ID = 2;

	String ACCOUNT_TYPE_SMART_CARD = "AXCS.TYPE.SMARTCARD"; // "Smart Card";
															// VALUE = 2;

	int ACCOUNT_TYPE_MAGSTRIPE_CARD_ID = 3;

	String ACCOUNT_TYPE_MAGSTRIPE_CARD = "AXCS.TYPE.MAGSTRIPECARD";// "Magstrip Card";
																	// VALUE = 3

	// String ACCOUNT_TYPE_ECASH = "ACCNT.ECASH.TYPE.ECASH";

	/** STATUS FLAG DESCRIPTIONS */

	String PENDING_ST_DESC = "PENDING";

	String PROCESSED_ST_DESC = "PROCESSED";

	String PRINTED_ST_DESC = "PRINTED";

	String REPRINT_ST_DESC = "REPRINT";

	String VOID_ST_DESC = "VOID";

	String CREDIT_KEY_OFF_ST_DESC = "CREDIT";

	String CHANGE_ST_DESC = "CHANGE";

	String EXTERNAL_CONTROLLER_TYPE = "E";

	String INTERNAL_CONTROLLER_TYPE = "I";

	// POST TO ACCOUNTING
	String POSTED_TO_ACCOUNTING = "Y";

	String NOT_POSTED_TO_ACCOUNTING = "N";
}
