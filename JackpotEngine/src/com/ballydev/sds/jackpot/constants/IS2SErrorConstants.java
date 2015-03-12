/***************************************************************************************************************
 *  $Id :  $
 * $Date : $
 * $Log$
 ***************************************************************************************************************
 *           Copyright (c) 2003 Bally Gaming Inc.  1977 - 2003
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 ****************************************************************************************************************/
package com.ballydev.sds.jackpot.constants;
/**
 *
 * 
 * @author Govindharaju Mohanasundaram email:GMohanasundaram@ballytech.com
 * 
 * @version $Revision: 1$
 */
public interface IS2SErrorConstants {
	
	// Jackpot Error Codes

	String MSG_JPT_REC_NOT_FOUND = "Jackpot Record Not Found";

	String CODE_JPT_REC_NOT_FOUND = "JP01";

	String MSG_JPT_AMNT_NOT_EQL = "Jackpot Amount Does Not Equal Detail";

	String CODE_JPT_AMNT_NOT_EQL = "JP02";

	String MSG_JPT_LOC_NOT_ASSIGNED_TO_CNTRLR = "Location Not Assigned To Controller";

	String CODE_JPT_LOC_NOT_ASSIGNED_TO_CNTRLR = "JP03";

	String MSG_JPT_REDEEMED = "Jackpot Has Been Redeemed";

	String CODE_JPT_REDEEMED = "JP04";

	String MSG_JPT_VOIDED = "Jackpot Has Been Voided";

	String CODE_JPT_VOIDED = "JP05";

	String MSG_JPT_INVALID_STATUS = "Invalid Jackpot Status";

	String CODE_JPT_INVALID_STATUS = "JP06";

	String MSG_JPT_INVALID_TRANSACTION_ACTION_CODE = "Invalid Transaction Action Code";

	String CODE_JPT_INVALID_TRANSACTION_ACTION_CODE = "JP07";
	
	// S2S Error Codes
	
	String MSG_S2S_INVALID_EMP_ID="Invalid Employee Identifier";
	
	String CODE_S2S_INVALID_EMP_ID = "SS03";
	
	String MSG_S2S_INVALID_SLOT="Invalid Slot Machine";
	
	String CODE_S2S_INVALID_SLOT="SS39";
	
	String MSG_S2S_INCOMPLETE_XML="Incomplete/Malformed XML";
	
	String CODE_S2S_INCOMPLETE_XML="MS05";
	
	int CODE_SLOT_INVALID=2;
	
	int CODE_VALID_SLOT=1;
	
	String MSG_SLOT_INVALID="Invalid Slot ";
	
	

}
