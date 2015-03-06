/*****************************************************************************
 * $Id: IAppConstants.java,v 1.31.1.0.2.4, 2013-10-15 15:13:16Z, SDS12.3.3 Checkin User$
 * $Date: 10/15/2013 10:13:16 AM$
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
package com.ballydev.sds.jackpotui.constants;

/**
 * This class contains all the generic constants used by the plugin.
 * @author anantharajr
 * @version $Revision: 38$
 */
public interface IAppConstants {
	String MODULE_NAME = "Jackpot";
	String JACKPOT_SERVICE_NAME = "JackpotEngine";
	String MARKETING_SERVICE_NAME = "MarketingEngine";
	String ASSET_SERVICE_NAME ="Asset"; 
	
	String JACKPOT_CATAGORY_NAME="SITE.JACKPOT.PARAMETERS";
	String SLIP_SCHEMA_FILE ="slipschema.xml";
	String PRINTER_SCHEMA_FILE="printcmd.xml";
	
	String JACKPOT_PRINT_SLIP_TYPE = "JACKPOT";	
	String VOID_PRINT_SLIP_TYPE = "VOID";	
	String REPORT_PRINT_SLIP_TYPE = "REPORT";	
	String CHECK_PRINT_SLIP_TYPE = "CHECK";
	
	String SLIP_DATE_FORMAT = "dd-MMM-yy HH:mm";
	
	String SLIP_SCHEMA_FILE_KEY = "slipschema";
	
	String JACKPOT_SLIP_SEQUENCE_NO_PREFIX = "J";
	
	String JACKPOT_PRINTER_PREFERENCE_KEY = "Preference.jakcpotPrinterName";
	
	String JACKPOT_AREA_PREFERENCE_KEY = "JACKPOT_SELECTED_AREAS";
	
	int NO_OF_RECORDS_TO_DISPLAY_IN_TABLE = 5;
	
	String EPSON_PRINTER_SUBSTRING	="EPSON";
	
	String LASER_JET_PRINTER_SUBSTRING	="LaserJet";
	
	String ANA_MESSAGES_INVALID_PASSWORD_KEY = "ANA.MESSAGES.INVALID.PASSWORD";
	
	String ANA_MESSAGES_INVALID_USERNAME_KEY = "ANA.MESSAGES.INVALID.USER.NAME";
	
	String ANA_MESSAGES_INVALID_USERNAME_FOR_SITE = "ANA.MESSAGES.ROLE.NOT.ACTIVE.IN.SITE";
	
	String ANA_MESSAGES_USER_NOT_PRESENT = "ANA.MESSAGES.USER.NOT.PRESENT";
	
	int ASSET_INVALID_SLOT_ERROR_CODE = 1;
	String ASSET_ONLINE_STATUS = "ONLINE";//
	String ASSET_CET_STATUS = "CASH EXCHANGE TERMINAL"; //CASH EXCHANGE TERMINAL
	
	int ALERT_TERMINAL_NO_AUTHORIZED_BY_SAME_EMPLOYEE = 301;
	
	int EMPLOYEE_ID_PAD_LENGTH = 5;
	
	String SITE_VALUE_SHIFT_PROMPT_FOR_ALL = "Prompt for All";
	
	String SITE_VALUE_SHIFT_DAY = "Day";
	
	String SITE_VALUE_SHIFT_SWING = "Swing";
	
	String SITE_VALUE_SHIFT_GRAVEYARD = "Graveyard";
	
	String SITE_VALUE_SHIFT_DAY_CODE = "D";
	
	String SITE_VALUE_SHIFT_SWING_CODE = "S";
	
	String SITE_VALUE_SHIFT_GRAVEYARD_CODE = "G";
	
	// CARDLESS ACCOUNT TYPE
	String ACCOUNT_TYPE_CASHLESS = "ACCNT.ECASH.TYPE.CASHLESS"; // VALUE = 1;

	String ACCOUNT_TYPE_SMART_CARD = "ACCNT.ECASH.TYPE.SMARTCARD"; // VALUE = 2;
	
	int JACKPOT_ID_PROG_START_112 	= 112;
	
	int JACKPOT_ID_PROG_END_159 	= 159;
	
	int JACKPOT_ID_254_FE 	    	= 254; // cancel credit
		
	int HEXA_DECIMAL_RADIX = 16;
	
	String ALL_CONSTANT = "a";
	
	String Y_CONSTANT = "Y";
	
	String N_CONSTANT = "N";
	
	// BARCODE ENCODING FORMATS
	String CODE39_ENCODE_FORMAT = "CODE39";
	String CODE128_ENCODE_FORMAT = "CODE128";
	String CODE128A_ENCODE_FORMAT = "CODE128A";
	String CODE128B_ENCODE_FORMAT = "CODE128B";
	
	int TEXT_WIDTH = 150;
	int SMALL_RESOLUTION_BTN_HEIGHT = 42;
	int SMALL_RESOLUTION_BTN_WIDTH = 42;
		
	// NEW LINE FEED
	String NEW_LINE_FEED = " \r\n";
	// NEW TAB FEED
	String NEW_TAB_FEED = " \t";
	// UNDERLINE CHARACTER STRING
	String UNDERLINE_CHAR = "_";
	String SIGLE_SPACE  							= " ";
	String EMPTY_STRING 							= "";
	
	// SLIP PRINT FONT TYPE
	String SLIP_PRINT_FONT_BOLD = "courier-BOLD-12";
	String SLIP_PRINT_FONT_PLAIN = "courier-PLAIN-12";
	
	// SITE VALUE - PROMPT FOR SLOT NO OR SLOT LOCATION
	long PROMPT_SLOT_NO = 1;
	long PROMPT_SLOT_LOCATION = 2;
	
	// DOLLAR VALUE
	double HALF_DOLLAR = 0.5;
	double ONE_DOLLAR = 1;
	
	// DEFAULT PLAYER CARD LENGTH IF SITE PARAM VALUE IS NOT AVAILABLE
	String DEFAULT_PALYER_CARD_LENGTH = "10";
	String DEFAULT_PLAYER_CARD = "0000000000";
	
	//CONSTANTS FOR EXP JP - NJ ENHANCEMENT CHANGES
	int 	MAX_BLIND_ATTEMPTS 					= 2;
	short 	UNSUCCESSFUL_BLIND_ATTEMPTS_ABORT 	= -1;	
	String 	MASKED_AMOUNT_STRING 				= "******" ;	
	String STATE_TAX_CHECKBOX = "stateTax";
	String FEDERAL_TAX_CHECKBOX = "federalTax";
	String MUNICIPAL_TAX_CHECKBOX = "municipalTax";
	
	String JKPT_TAX_RATE_AMNT_CLEAR_VALUE = "ST:0.0^0.0|FT:0.0^0.0|MT:0.0^0.0";
	
	String INTERCEPT_CHECKBOX = "intercept";
	
	
}
