/*****************************************************************************
 * $Id: ILookupTableConstants.java,v 1.6, 2010-08-25 13:23:22Z, Ambereen Drewitt$
 * $Date: 8/25/2010 8:23:22 AM$
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
 * Intterface that contains the look up table constants
 * @author dambereen
 * @version $Revision: 7$
 */
public interface ILookupTableConstants {

	/*
	 *  STATUS FLAG TABLE 
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
	short CARD_IN_STATUS_ID=4007;
	
	/** Status Flag - Jackpot Clear */
	short JP_CLEAR_STATUS_ID=4009;
	
	/** Status Flag - Mechanics Delta */
	short MECHANICS_DELTA_STATUS_ID=4010;
	
	/** Status Flag - Change */
	short CHANGE_STATUS_ID=4011;
	
	/** Status Flag - Printed */
	short PRINTED_STATUS_ID=4014;
	
	/* 
	 * SLIP TYPE TABLE
	 */

	/** Jackpot Slip Type */
	short BEEF_SLIP_TYPE_ID = 3004;
	
	
	/*
	 * POST FLAG TABLE
	 */
	
	/** Posted Flag Id */
	short POSTED_FLAG_ID = 6001;
	
	/** Not Posted Flag Id */
	short NOT_POSTED_FLAG_ID = 6002;
	
	/** Auto Authorize Posted Flag Id */
	short AUTO_AUTHORIZE_POSTED=6003;
	
	/** Auto Authorize Not Posted Flag Id */
	short AUTO_AUTHORIZE_NOT_POSTED=6004;
	
	
	/*
	 * PROCESS_FLAG TABLE
	 */
	short NORMAL_PROCESS_FLAG = 2001;	
	short POUCH_PAY_PROCESS_FLAG = 2002;	
	short EXPRESS_PROCESS_FLAG = 2003;
	short MANUAL_PROCESS_FLAG = 2005;
	
		
	/** STATUS FLAG DESCRIPTIONS*/
	
	String PENDING_ST_DESC = "PENDING";
	String PROCESSED_ST_DESC = "PROCESSED";
	String CHANGE_ST_DESC = "CHANGE";	
	String REPRINT_ST_DESC = "REPRINT";	
	String VOID_ST_DESC = "VOID";
	String CREDIT_KEY_OFF_ST_DESC = "CREDIT";
	
	// POST TO ACCOUNTING
	String POSTED_TO_ACCOUNTING = "Y";
	String NOT_POSTED_TO_ACCOUNTING = "N";
	
	/*
	 * ECASH ACCOUNT TYPE
	 */
	 String ACCOUNT_TYPE_CASHLESS = "AXCS.TYPE.CARDLESS"; // VALUE = 1;
	 short ACCOUNT_TYPE_CASHLESS_VAL = 1; 
	 String ACCOUNT_TYPE_SMART_CARD = "AXCS.TYPE.SMARTCARD"; // VALUE = 2;
	 short ACCOUNT_TYPE_SMART_CARD_VAL = 2; 
	 String ACCOUNT_TYPE_MAGSTRIPECARD = "AXCS.TYPE.MAGSTRIPECARD"; // VALUE = 3;
	 short ACCOUNT_TYPE_MAGSTRIPECARD_VAL = 3; // VALUE = 3;
	 
	 // ACCOUNT STATE ID
	 short ACCOUNT_ACTIVE_STATE_ID = 2;
}
