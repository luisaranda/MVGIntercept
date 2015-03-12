/*****************************************************************************
 * $Id: IAppConstants.java,v 1.46.1.1.2.1, 2013-08-16 11:00:32Z, Anbuselvi, Balasubramanian$
 * $Date: 8/16/2013 6:00:32 AM$
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
 * General constants used for the Jackpot Engine 
 * @author dambereen
 * @version $Revision: 51$
 */
public interface IAppConstants {
	
 	 String MODULE_NAME="Jackpot";
 	 
 	 String ENGINE_ID="JACKPOT_ENGINE"; 
 	 
 	 String PROG_ENGINE_ID="PROG_ENGINE";
 	
 	 boolean IS_LOCAL_LOOKUP = true;
 	 
 	 //JP Application property keys 	 
 	//String JP_TIMER_DELAY_KEY="jp.timer.delay";
 	//String JP_TIMER_FREQUENCY_KEY="jp.timer.frequency";
 	 
 	 // pending Jackpot cache
 	 String JACKPOT_CACHE_NAME="JACKPOT_CACHE_NAME";
 	 
 	String ALL_PENDING_JACKPOTS="ALL_PENDING_JACKPOTS";
 	String ALL_PENDING_JACKPOTS_KEY="ALL_PENDING_JACKPOTS_KEY";

	// String ENGINE_PROPERTIES_PATH="/jackpotengine/ApplicationResources.properties";
	 
	 //String HIBERNATE_CONFIG_PATH = ApplicationPropertiesReader.getInstance().getValue("database.conf.dir")+"/hibernate.cfg.xml";		
	 
	 String SERVICE_XML_PATH="/commons/services.xml";
	 
	 String LOGGER_PROPERTIES_PATH="/jackpotengine/log4sds.properties";
	 
	 String PRINTER_CMD_XML_FILE_PATH="/sds/props/jackpotengine/printercmd.xml";		 
	 String SLIP_SCHEMA_XML_FILE_PATH="/sds/props/jackpotengine/slipschema.xml";
	 
	//String HIBERNATE_UTIL_CLASS_PATH= "com.ballydev.sds.jackpotengine.db.util.HibernateUtil";	
	 	 
	 String JACKPOT_ALERTS_ENGINE_ID="JACKPOT_ENGINE";	 	
	 
	 String SESS_JP_SEQUENCE_NO = "jpSequenceNo";
	 String SESS_ASSET_LOC = "assetLoc";
	 String SESS_JP_POST_SUCCESS = "jpPostSuccess";
	 String SESS_SLOT_STATUS = "slotStatus";
	 String SESS_UNKNOWN_SLOT = "unknownSlot";
	 String SESS_SLOT_DOOR_OPEN = "slotDoorOpen";
	
	// Single jackpot timer changes	
	String SEQUENCE_NO = "SEQUENCE_NO";
	String SITE_ID = "SITE_ID";
	String MESSAGE_ID = "MESSAGE_ID";
	String ASSET_CONFIG_NO = "ASSET_CONFIG_NO";
	String JACKPOT_ID = "JACKPOT_ID";
	String ASSET_CONFIG_LOCATION = "ASSET_CONFIG_LOCATION";
	String ORIGINAL_AMOUNT = "ORIGINAL_AMOUNT";
	String AREA_SHORT_NAME = "AREA_SHORT_NAME";
	String TRANSACTION_DATE = "TRANSACTION_DATE";
	String LAST_ALERT_SENT_TIME = "LAST_ALERT_SENT_TIME";
	String TIMER_DELAY = "TIMER_DELAY";
	String TIMER_FREQUENCY = "TIMER_FREQUENCY";
	// String SESS_JP_AUDIT_CODE_SUCCESS = "auditCodeSuccess";
	 
	 String LOGGER_BO_STATISTICS ="JackpotBOStatistics";
	 
	 String LOGGER_PROCESS_STATISTICS ="JackpotProcessStatistics";
	 int ASSET_INVALID_SLOT_ERROR_CODE = 1;
	 
	 int EMPLOYEE_ID_PAD_LENGTH = 5;
	 
	 int ASSET_NO_PAD_LENGTH = 5;
	 
	 int SITE_ID_PAD_LENGTH = 4;
	 
	 int LOCK_EMPLOYEE_ID_PAD_LENGTH = 6;
	 
	 short CHANGE_FLAG_ENABLED = 1;
	 
	 short CHANGE_FLAG_DISABLED = 0;
	 
	 String ASSET_ONLINE_STATUS = "ONLINE";//
	 String ASSET_CET_STATUS = "CASH EXCHANGE TERMINAL"; //CASH EXCHANGE TERMINAL
	 
	 String XC10_POSITIVE_RESPONSE_CODE = "E0";
	 
	 String GEN_POSITIVE_RESPONSE_CODE = "A0";
	 
	 String GEN_NEGATIVE_RESPONSE_CODE = "B0";
	 
	 String TRUE_FLAG = "true";

	 String JACKPOT_SLIP_SEQUENCE_NO_PREFIX = "J";
	
	
//	 BARCODE ENCODING FORMATS
	String CODE39_ENCODE_FORMAT = "CODE39";
	String CODE128_ENCODE_FORMAT = "CODE128";
	String CODE128A_ENCODE_FORMAT = "CODE128A";
	String CODE128B_ENCODE_FORMAT = "CODE128B";
	
	String EPI_JP_AMOUNT_LIMIT = "9999";
	
	// CONSTANTS - CASHIER DESK PROCESSING MESSAGE 
	String JP_SLIP_PROCESSED_SUCCESSFULLY = "JP.SLIP.PROCESSED.SUCCESSFULLY";
	String JP_SLIP_ALREADY_PROCESSED = "JP.SLIP.ALREADY.PROCESSED";
	String JP_SLIP_ALREADY_VOIDED = "JP.SLIP.ALREADY.VOIDED";
	String JP_SLIP_NOT_FOUND = "JP.SLIP.NOT.FOUND";
	String JP_SLIP_VOIDED_SUCCESSFULLY = "JP.SLIP.VOIDED.SUCCESSFULLY";
	String JP_SLIP_PENDING = "JP.SLIP.PENDING";
	String JP_SLIP_MECHANICS_DELTA = "JP.SLIP.MECHANICS.DELTA";
	String JP_SLIP_CREDIT_KEY_OFF = "JP.SLIP.CREDIT.KEY.OFF";
	String JP_SLIP_INVALID = "JP.SLIP.INVALID";
	String JP_SLIP_STATUS_RETURN_SUCCESS = "JP.SLIP.STATUS.RETURN.SUCCESS";
	String JP_SLIP_ALREADY_PROCESSED_CASH_DESK_DISABLED = "JP.SLIP.ALREADY.PROCESSED.CASH.DESK.DISABLED";
	String JP_SLIP_GAMING_DAY_MISMATCH ="JP.SLIP.GAMING.DAY.MISMATCH";
	String JP_SLIP_AMT_EXCEEDS_JP_LIMIT ="JACKPOT.HP.AMT.EXCEED.LIMIT";
	String JP_SLIP_EMP_CD_PROCESS_SLIP_FUNC_NOT_FOUND ="CASH.DESK.SLIP.PROCESS.FUNCTION.NOT.FOUND";
	String JP_SLIPS_PENDING_FOR_ACCOUNT = "JP.SLIPS.PENDING.FOR.ACCOUNT";
	String JP_SLIP_EMP_SESSION_NOT_AVAILABLE = "JP.SLIP.EMP.SESSION.NOT.AVAILABLE";
	
	// CONSTANTS - CASHIER DESK PROCESSING MESSAGE CODE
	short JP_SLIP_PROCESSED_SUCCESSFULLY_CODE = 0;
	short JP_SLIP_STATUS_RETURN_SUCCESS_CODE = 0;
	short JP_SLIP_VOIDED_SUCCESSFULLY_CODE = 0;
	short JP_SLIP_ALREADY_PROCESSED_CODE = 8801;
	short JP_SLIP_ALREADY_VOIDED_CODE = 8802;
	short JP_SLIP_NOT_FOUND_CODE = 8803;
	short JP_SLIP_PENDING_CODE = 8804;
	short JP_SLIP_MECHANICS_DELTA_CODE = 8805;
	short JP_SLIP_CREDIT_KEY_OFF_CODE = 8806;
	short JP_SLIP_INVALID_CODE = 8807;
	short JP_SLIP_ALREADY_PROCESSED_CASH_DESK_DISABLED_CODE = 8808;
	short JP_SLIP_GAMING_DAY_MISMATCH_CODE = 8810;
	short JP_SLIP_EMP_JP_LIMIT_EXCEEDED_CODE = 8811;
	short JP_SLIP_EMP_CD_PROCESS_SLIP_FUNC_NOT_FOUND_CODE = 8812;
	short JP_SLIPS_PENDING_FOR_ACCOUNT_CODE = 8813;
	short JP_SLIP_EMP_SESSION_NOT_AVAILABLE_CODE = 8814;
	
	
	//CREDIT KEY OF AUTH BLOCK RESP and RESPONSE MSGS
	int XC_30_AUTH_BLOCK_POSITIVE_RESP = 1;
	int XC_30_AUTH_BLOCK_NEGATIVE_RESP = 0;
		
	String SUCCESS = "Success";
	String SLIP_IS_A_MECHANICS_DELTA = "JP Mechanics Delta";
	String SLIP_IS_INVALID = "JP Invalid";
	String SLIP_IS_ALREADY_PRINTED = "Jp Printed";
	String SLIP_IS_ALREADY_VOIDED = "Jp Voided";
	String SLIP_IS_ALREADY_CREDIT_KEYED_OFF = "Jp Credit Key Off";
	String SLIP_NO_JACKPOT_SLIP = "No Slip Found";
	String SLIP_PROBLEM_OCCURED_DURING_CREDIT_KEY_OFF_AUTH = "Jp Problem Occured";

	//EMP JOB ROLE PARAMETER - JACKPOT AMOUNT
	String JACKPOT_AMOUNT_USER_PARAMETER = "JACKPOT AMOUNT";
	
	//EMP JOB FUNCTION NAME FOR CASH DESK
	String EMP_FUNC_CASH_DESK_PROC_SLIPS = "Utility - Cash Desk Process Slips";
	
	// NUMBER OF DIGITS FOR RANDOM NUMBER FOR JACKPOT SEQUENCE
	int RANDOM_NUMBER_LENGTH = 4;
			
	// EXPIRY DATE FORMAT FROM THE JACKPOT UI
	String EXPIRY_DATE_FORMAT = "yyyyMMddHHmmssSSS";
	
	String EMPTY_STRING = "";
	
	String DEFAULT_PLAYER_CARD = "0000000000";
	
	String NORMAL_SLOT = "ASSET.OPTION.ENUM.NORMAL";
	
	int 	MAX_BLIND_ATTEMPTS 					= 2;
	short 	UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT 	= -1;
	
	int HEXA_RADIX = 16;
	
	String HANDPAID_JP_XC_CODE = "10";
	String S2S_OVERRIDE_LIMIT_EXCEEDED  	= "OVERRIDE_LIMIT_EXCEEDED";
	String S2S_DOLLAR_VARIANCE_EXCEEDED  	= "DOLLAR_VARIANCE_EXCEEDED";
	String S2S_PERCENT_VARIANCE_EXCEEDED  	= "PERCENT_VARIANCE_EXCEEDED";
	
	String JACKPOT_SUPERVISOR_EMP_FUNCTION = "Jackpot - Supervisory Authority";
}
