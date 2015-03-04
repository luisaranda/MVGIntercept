/*****************************************************************************
 * $Id: IAppConstants.java,v 1.13, 2011-03-05 10:37:03Z, Ambereen Drewitt$
 * $Date: 3/5/2011 4:37:03 AM$
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
 * This class contains all the generic constants used by the plugin.
 * @author anantharajr
 *	@version $Revision: 14$
 */
public interface IAppConstants {
	String MODULE_NAME = "SlipsEngine";
	String SLIPS_SERVICE_NAME = "SlipsEngine";
	String SLIPS_CATEGORY_NAME = "SITE.SLIP.DMK.FILL.PARAMS";
	String SLIP_SCHEMA_FILE_KEY = "slipschema";
	String SLIP_SCHEMA_FILE = "slipschema.xml";
	String PRINT_SLIP_TYPE = "SLIP"; 
	short MANUAL_PROCESS_FLAG = 2005;
	short PENDING_PROCESS_FLAG = 2004;
	short BEEF_PROCESS_FLAG = 2001;
	short BLEED_PROCESS_FLAG = 2001;
	short FILL_SLIP_TYPE_ID = 3002;
	short BEEF_SLIP_TYPE_ID = 3004;
	short BLEED_SLIP_TYPE_ID =3003;
	String BEEF_PRINT_SLIP_TYPE = "BEEF";	
	String VOID_PRINT_SLIP_TYPE = "VOID";	
	String REPORT_PRINT_SLIP_TYPE = "REPORT";
	String BLEED_FUNCTION_NAME = "Bleed";
	String BEEF_FUNCTION_NAME = "Beef";
	String VOID_FUNCTION_NAME = "Void";
	String DISPLAY_FUNCTION_NAME = "Display";
	String REPRINT_FUNCTION_NAME = "Reprint";
	String REPORT_FUNCTION_NAME = "Report";
	String FILL_FUNCTION_NAME = "Manual";
	int SLOT_TEXT_LIMIT = 5;
	int SLOT_LOCATION_TEXT_LIMIT = 10;
	//String PRIOR_SLIP_REPRINT = "P";	
	int WINDOW_NO_TEXT_LIMIT = 3;
	
	String ANA_MESSAGES_INVALID_PASSWORD_KEY 		= "ANA.MESSAGES.INVALID.PASSWORD";	
	String ANA_MESSAGES_INVALID_USERNAME_KEY 		= "ANA.MESSAGES.INVALID.USER.NAME";
	String ANA_MESSAGES_INVALID_USERNAME_FOR_SITE 	= "ANA.MESSAGES.ROLE.NOT.ACTIVE.IN.SITE";
	String ANA_MESSAGES_USER_NOT_PRESENT 			= "ANA.MESSAGES.USER.NOT.PRESENT";
	
	int ASSET_INVALID_SLOT_ERROR_CODE = 1;
	
	int EMPLOYEE_ID_PAD_LENGTH = 5;
	
	String SITE_VALUE_SHIFT_PROMPT_FOR_ALL = "Prompt for All";
	
	String SITE_VALUE_SHIFT_DAY = "Day";
	
	String SITE_VALUE_SHIFT_SWING = "Swing";
	
	String SITE_VALUE_SHIFT_GRAVEYARD = "Graveyard";
	
	String SLIP_AREA_PREFERENCE_KEY = "SLIP_SELECTED_AREAS";
	
	String RUNTIME_ERROR_UNREACH_SERVICE_MSG = "Unreachable?: Service unavailable";
	
	String RUNTIME_ERROR_CLUSTER_INNVOCATION_FAIL_MSG = "cluster invocation failed,";
	
	short CASHABLE_AMT_FLAG = 1;
	short NONCASHABLE_AMT_FLAG = 0;
	
	//POST TO ACCOUNTING FLAGS
	String POST_TO_ACC_FLG_YES = "Y";
	String POST_TO_ACC_FLG_NO = "N";
	
	String ZERO = "0";
	
}
