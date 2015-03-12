package com.ballydev.sds.jackpot.keypad;

/**
 * Look Up Constants.
 * 
 * @author ranjithkumarm
 *
 */
public interface KeypadLookUpConstants {

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
	
	/* 
	 * SLIP TYPE TABLE
	 */

	/** Jackpot Slip Type */
	short JACKPOT_SLIP_TYPE_ID = 3001;
	
	
	/*
	 *  AUDIT CODE TABLE 
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
	short AUTO_AUTHORIZE_POSTED=6003;
	
	/** Auto Authorize Not Posted Flag Id */
	short AUTO_AUTHORIZE_NOT_POSTED=6004;
	
	
	/*
	 * JACKPOT TYPE TABLE
	 */		

	/** Jackpot Type - Regular Jackpot */
	short JACKPOT_TYPE_REGULAR=7001;
	
	/** Jackpot Type - Promotional Jackpot */
	short JACKPOT_TYPE_PROMOTIONAL= 7002;
	
	/** Jackpot Type - Progressive Jackpot */
	short JACKPOT_TYPE_PROGRESSIVE= 7003;
	
	/** Jackpot Type - Linked Progressive Jackpot */
	short JACKPOT_TYPE_lINKED_PROGRESSIVE= 7004;
	
	/** Jackpot Type - System Game Jackpot */
	short JACKPOT_TYPE_SYSTEM_GAME= 7005;
	
	/** Jackpot Type - Cancel Credit Jackpot */
	short JACKPOT_TYPE_CANCEL_CREDIT= 7006;
	
	/** Jackpot Type - Mystery Jackpot */
	short JACKPOT_TYPE_MYSTERY = 7007;
	
	/*
	 * PROCESS_FLAG TABLE
	 */
	short NORMAL_PROCESS_FLAG = 2001;	
	short POUCH_PAY_PROCESS_FLAG = 2002;	
	short EXPRESS_PROCESS_FLAG = 2003;
	short MANUAL_PROCESS_FLAG = 2005;
	
	
	/*
	 * TAX_TYPE TABLE 
	 */
	short TAX_NO = 5001;
	short TAX_FEDERAL = 5002;
	short TAX_STATE = 5003;
	short TAX_FEDERAL_PLUS_STATE = 5004;
	
		
	/** STATUS FLAG DESCRIPTIONS*/
	
	String PENDING_ST_DESC = "PENDING";
	String PROCESSED_ST_DESC = "PROCESSED";
	String CHANGE_ST_DESC = "CHANGE";	
	String REPRINT_ST_DESC = "REPRINT";	
	String VOID_ST_DESC = "VOID";
	String CREDIT_KEY_OFF_ST_DESC = "CREDIT";
	
	
     long TRILLIONS = 100000000000000L ;
	
     long BILLIONS = 100000000000L;
     
	/** Number of pennies in a million dollars */
	 long MILLIONS = 100000000L;

	/** Number of pennies in a thousand dollars */
	 int THOUSANDS = 100000;

	/** Number of ones in a hundred */
	 int HUNDREDS = 100;

	/** Number of pennies in a one dollar bill ME! */
	 int ONES = 100;

	
	
	
}
