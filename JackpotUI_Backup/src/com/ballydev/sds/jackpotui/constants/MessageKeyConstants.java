/*****************************************************************************
 * $Id: MessageKeyConstants.java,v 1.39.1.1, 2011-04-27 07:31:08Z, SDS12.3.2CheckinUser$
 * $Date: 4/27/2011 2:31:08 AM$
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
package com.ballydev.sds.jackpotui.constants;

/** 
 * Interface that has all the messages database keys
 *  @author dambereen
 *  @version $Revision: 42$
 */
public interface MessageKeyConstants {

	String JACKPOT_OVERRIDE_AUTHORITY_REQUIRED="JACKPOT.OVERRIDE.AUTHORITY.REQUIRED";
	
	String OVERRIDE_PERSON_MUST_BE_DIFFERENT="JACKPOT.OVERRIDE.PERSON.MUST.BE.DIFFERENT";
	
	 String PLZ_SELECT_A_SEQ_NO = "JACKPOT.PLZ.SELECT.SEQ.NO";

	 String JACKPOT_PROCESS_FAILED = "JACKPOT.PROCESS.FAILED";

	 String JACKPOT_PROCESS_SUCCESS = "JACKPOT.PROCESS.SUCCESS";

	 String SLIP_VOID_SUCCESS = "JACKPOT.VOID.SUCCESS";

	 String SLIP_VOID_FAILED = "JACKPOT.VOID.FAILED";

	 String SLIP_ALREADY_VOIDED = "JACKPOT.SLIP.ALREADY.VOIDED";
	 
	 String JP_SLIP_ALREADY_VOIDED = "JP.SLIP.ALREADY.VOIDED";
	
	 String SLIP_ALREADY_PRINTED = "JACKPOT.SLIP.ALREADY.PRINTED";
	 
	 String SLIP_ALREADY_PROCESSED = "JP.SLIP.ALREADY.PROCESSED"; // TO BE ADDED
	 
	 String SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED = "JACKPOT.SLIP.REPRINT.IS.ALREADY.PRINTED.CASH.DESK.DISABLED"; //slip is already printed and cannot be reprinted as cash desk is disabled.

	 String SLIP_NOT_PRINTED = "JACKPOT.SLIP.NOT.PRINTED";

	 String SLIP_REPRINT_SUCCESS = "JACKPOT.SLIP.REPRINT.SUCCESS";

	 String SLIP_REPRINT_FAILED = "JACKPOT.SLIP.REPRINT.FAILED";
	
	String EXPRESS_JP_GAME_NOT_CARDED ="JACKPOT.GAME.CARDED.BY.EMP.EXP";
	
	 String SLIP_NOT_FOUND = "JACKPOT.SLIP.NOT.FOUND";
	 
	 String JP_SLIP_MECH_DELTA = "JP.SLIP.MECHANICS.DELTA";
	 
	 String JP_SLIP_INVALID = "JP.SLIP.INVALID";
	
	 String HP_AMT_EXCEEDS_JP_LIMIT = "JACKPOT.HP.AMT.EXCEED.LIMIT"; 

	 String EMP_ID_ONE_ALREADY_USED_FOR_TRANS = 	"JACKPOT.EMPLOYEE.ID.ONE.ALREADY.USED.FOR.TRANSACTION"; 

	 String EMP_ID_TWO_ALREADY_USED_FOR_TRANS = "JACKPOT.EMPLOYEE.ID.TWO.ALREADY.USED.FOR.TRANSACTION"; 
	
	 String AMT_EXCEEDS_EMP_LIMIT = "JACKPOT.AMOUNT.EXCEEDS.EMPLOYEE.LIMIT"; 
	
	String ALREADY_USED_FOR_THIS_TRANSACTION="JACKPOT.ALREADY.USED.FOR.TRANSACTION";// CHECK IF NECESSARY TO BE ADDED TO DE DATABASE
	
	String SUPERVISORY_AUTHORITY_REQUIRED="JACKPOT.SUPERVISORY.AUTHORITY.REQUIRED";
		
	String LOW_OVERRIDE_AUTH_LEVEL="JACKPOT.LOW.OVERRIDE.AUTHORIZATION.LEVEL";
	
	String ADJ_AMT_GRT_VAR_LIMIT_REQ_AUTH="JACKPOT.ADJUSTED.AMT.GREATER.VARIANCE.LIMIT.REQ.AUTH";
	
	 String EXP_JP_BLIND_ATTEMPTS_EXCEEDED = "JACKPOT.EXPRESS.JP.BLIND.ATTEMPTS.EXCEEDED";
	
	 String INVALID_AMOUNT = "JACKPOT.INVALID.AMOUNT";
	
	 String NO_RECORDS_FOR_THE_SLOT_SEQUENCE_STAND_NO = "JACKPOT.NO.RECORDS.FOR.THE.SLOT.SEQUENCE.STAND.NUMBER";
	
	 String PLEASE_ENTER_ONE_TEXTBOX = "JACKPOT.PLEASE.ENTER.ONE.TEXTBOX";
	
	 String PLZ_ENTER_SLOT_OR_SEQUENCE_NO = "JACKPOT.PLZ.ENTER.SLOT.OR.SEQUENCE.NO";
	
	 String PLZ_ENTER_SLOT_OR_SEQUENCE_NO_OR_MINUTES = "JACKPOT.PLZ.ENTER.SLOT.OR.SEQUENCE.NO.OR.MINUTES";
	
	 String PLZ_ENTER_SLOT_OR_MINUTES = "JACKPOT.PLZ.ENTER.SLOT.OR.MINUTES";
	
	 String PLZ_ENTER_STAND_OR_SEQUENCE_NO = "JACKPOT.PLZ.ENTER.STAND.OR.SEQUENCE.NO";
	
	 String PLZ_ENTER_STAND_OR_SEQUENCE_NO_OR_MINUTES = "JACKPOT.PLZ.ENTER.STAND.OR.SEQUENCE.NO.OR.MINUTES";
	
	 String PLZ_ENTER_STAND_OR_MINUTES = "JACKPOT.PLZ.ENTER.STAND.OR.MINUTES";
	
	 String PLZ_ENTER_SEQUENCE_NO_OR_MINUTES = "JACKPOT.PLZ.ENTER.SEQUENCE.NO.OR.MINUTES";	
	
	// String PASSWORD_MINIMUM_LENGTH = "JACKPOT.PASSWORD.MINIMUM.LENGTH";	
	
	 String ABORT_JACKPOT_PROCESS ="JACKPOT.ABORT.PROCESS"; 
	
	 String INVALID_SLOT_NO = "JACKPOT.INVALID.SLOT.NUMBER"; 
	
	 String INVALID_SLOT_LOCATION_NO = "JACKPOT.INVALID.SLOT.LOCATION.NUMBER";
	
	 String WITH_SEQUENCE_NUMBER = "JACKPOT.WITH.SEQUENCE.NUMBER";//with the sequence number
	
	String IS_THIS_A_PROMOTIONAL_JACKPOT = "JACKPOT.IS.THIS.PROMOTIONAL.JACKPOT";//Is this a promotional jackpot
	
	 String ABORT_AND_SWITCH_OVER_PROCESS = "JACKPOT.ABORT.AND.SWITCH.PROCESS";//Do you want to abort and switch over to another process
		
	 String EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK = "JACKPOT.EXCEPTION.GETTING.SITE.ID";//Please relogin as an exception occured while getting the site id
	
	 String INVALID_EMPLOYEE_ID = "JACKPOT.INVALID.EMPLOYEE.ID";//Invalid Employee Id
	
	 String INVALID_EMPLOYEE_PASSWORD = "JACKPOT.INVALID.EMPLOYEE.PASSWORD";//Invalid Employee Password
	
	 String ERROR_GETTING_SITE_PARAMETERS = "JACKPOT.ERROR.GETTING.SITE.PARAMETERS";//Error while getting the site configuration parameters
	
	 String ERROR_FETCHING_ASSET_SERVICE = "JACKPOT.ERROR.FETCHING.ASSET.SERVICE";//Could not fetch the asset service
	
	 String ERROR_FETCHING_MARKETING_SERVICE = "JACKPOT.ERROR.FETCHING.MARKETING.SERVICE";//Could not fetch the marketing service
	
	 String ERROR_GETTING_ASSET_DETAILS = "JACKPOT.ERROR.GETTING.SLOT.STAND.ASSET.DETAILS";//Exception getting the slot / slot location asset details
	
	 String ERROR_POSTING_JACKPOT_ADJUSTMENT_PLAYER_DETAILS_TO_MARKETING = "JACKPOT.POSTING.ADJUSTMENT.PLAYER.DETAILS.TO.MARKETING.FAILED";//Posting the jackpot adjustment and player details to marketing failed
	
	 String SUCCESS_POSTING_JACKPOT_ADJUSTMENT_PLAYER_DETAILS_TO_MARKETING = "JACKPOT.DETAILS.POSTED.TO.MESSAGING";//The jackpot adjustment and player details have been posted to Marketing
	
	 String EXCEPTION_WHILE_CHECKING_SLOT_NO_WITH_ASSET = "JACKPOT.EXCEPTION.WHILE.CHECKING.SLOT.NO.WITH.ASSET";
	
	 String EXCEPTION_WHILE_CHECKING_STAND_NO_WITH_ASSET = "EXCEPTION.WHILE.CHECKING.STAND.NO.WITH.ASSET";
	
	 String USER_SHOULD_REENTER_HPJP_AMOUNT = "JACKPOT.USER.SHOULD.REENTER.HPJP.AMOUNT";

	String  ADJUSTED_JACKPOT_AMOUNT_VARIES = "JACKPOT.ADJUSTED.JACKPOT.AMOUNT.VARIES";

	String PERCENT_OF_ORIG_AMOUNT = "JACKPOT.PERCENT.OF.ORIG.AMOUNT";
	
	String IS_THIS_A_CREDIT_METER_HANDPAY="JACKPOT.IS.THIS.A.CREDIT.METER.HANDPAY";
	
	String WAS_THIS_A_POUCH_PAY="JACKPOT.WAS.THIS.A.POUCH.PAY";

	String USER_AUTHENTICATION_FAILED = "JACKPOT.USER.AUTHENTICATION.FAILED";

	String USER_AUTHENTICATION_FAILED_CHK_USERNAME = "JACKPOT.USER.AUTHENTICATION.FAILED.CHK.USERNAME";//"User authentication failed. Please check username

	String USER_AUTHENTICATION_FAILED_CHK_PASSWORD = "JACKPOT.USER.AUTHENTICATION.FAILED.CHK.PASSWORD";//"User athentication failed check password

	String USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD = "JACKPOT.USER.AUTHENTICATION.FAILED.CHK.USERNAME.PASSWORD";//"User authentication failed. Please check username and password
	
	 String SLOT_DOES_NOT_BELONG_TO_THIS_SITE = "JACKPOT.SLOT.DOES.NOT.BELONG.TO.THIS.SITE";//Slot number entered does not belong to this site, please re-enter the slot number
	
	 String SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE = "JACKPOT.SLOT.LOCATION.DOES.NOT.BELONG.TO.THIS.SITE";//Slot location number entered does not belong to this site, please re-enter the slot location number
	
	 String CURRENT_PROCESS_IS_NOT_ALLOWED_FOR_OFFLINE_SLOTS = "JACKPOT.CURRENT.PROCESS.IS.NOT.ALLOWED.FOR.OFFLINE.SLOTS";//"The current jackpot process is not allowed for offline slots. Please re-enter the slot / slot location number"
	
	 String EXCEPTION_WHILE_GETTING_THE_KIOSK_USED_INFO = "JACKPOT.EXCEPTION.GETTING.KIOSK.USED.INFO";//Please relogin as an exception has occured while getting the kiosk / touch screen information
	
	 String FRACTIONS_OF_CENT_NOT_ALLOWED = "JACKPOT.FRACTIONS.OF.CENT.NOT.ALLOWED";//Fractions of a cent is not allowed, please re-enter the amount
	
	 String PLEASE_REENTER_THE_HP_AMOUNT = "JACKPOT.RENTER.HANDPAID.AMOUNT";//Please re-enter the hand paid amount field
	
	 String PLEASE_REENTER_THE_MACHINE_PAID_AMOUNT = "JACKPOT.RENTER.MACHINE.PAID.AMOUNT";//Please re-enter the machine paid amount field
	
	 String 	PENDING_SLIPS_HAVE_BEEN_VOIDED_SUCCESSFULLY_PRINT_REPORT = "JACKPOT.PENDING.SLIPS.VOIDED.SUCCESSFULLY";//The pending slips have been voieded successfully 
	
	 String 	PENDING_SLIPS_VOID_FAILED = "JACKPOT.PENDING.SLIPS.VOID.FAILED";//The voiding of pending slips failed 
	
	 String 	VOID_REPORT_PRINT_SUCCESS = "JACKPOT.VOID.REPORT.PRINT.SUCCESS";// The voided slip report has been printed successfully 
	
	 String 	JACKPOT_REPORT_PRINT_SUCCESS = "JACKPOT.REPORT.PRINT.SUCCESS";
	
	 String 	JACKPOT_REPORT_PRINT_FAILED = "JACKPOT.REPORT.PRINT.FAILED";
	
	 String 	NO_RECORDS_TO_PRINT_FOR_DATE = "JACKPOT.NO.RECORDS.TO.PRINT.FOR.DATE";
	
	 String 	SERVICE_DOWN = "JACKPOT.SERVICE.DOWN";
	
	 String 	NO_PRIOR_SLIPS_TO_REPRINT = "JACKPOT.NO.PRIOR.SLIPS.TO.REPRINT";//"There are no prior slips to reprint"
	
	String UNABLE_TO_FETCH_ASSET_DETAILS = "JACKPOT.UNABLE.TO.FETCH.ASSET.DETAILS"; //Unable to fetch the asset details for the slot information entered";
	
	String JACKPOT_FUNC_REQ_SIGNED_ON_USER = "JACKPOT.FUNC.REQ.SIGNED.ON.USER"; //"Jackpot functions require a signed on user"; 
		
	String EXCEPTION_OCCURED_DURING_SLIP_PRINT = "JACKPOT.EXCEPTION.DURING.SLIP.PRINT"; //Exception occured while printing the slip 
			
	String WAS_PRINTED_ON = "JACKPOT.WAS.PRINTED.ON";//was printed on
	
	String SLIP_CANNOT_BE_VOIDED_TODAY = "JACKPOT.SLIP.CANNOT.BE.VOIDED.TODAY";//hence the slip cannot be voided today
	
	String JACKPOT_ONE_PERSON_AUTHORITY_REQUIRED = "JACKPOT.ONE.PERSON.AUTHORITY.REQUIRED"; //One person authority is required
	
	String JACKPOT_INITIATOR_NOT_ALLOWED_TO_AUTHORIZE_THE_SLIP = "JACKPOT.INITIATOR.NOT.ALLOWED.TO.AUTHORIZE.SLIP";//The jackpot initiator is not allowed to authorize the slip
		
	String SLOT_IS_NOT_ONLINE = "JACKPOT.SLOT.NOT.ONLINE";// Slot is not online
	
	String FOR_THE_SEQUENCE = "JACKPOT.FOR.SEQUENCE";// for the sequence
	
	String SLIP_CREDIT_KEYED_OFF_CANT_VOID = "JACKPOT.CREDIT.KEYED.CANT.VOID";// Slip is credit keyed off hence the slip cannot be voided
	
	String SLIP_CREDIT_KEYED_OFF_CANT_PRINT = "JACKPOT.CREDIT.KEYED.CANT.PRINT";// Slip is credit keyed off hence the slip cannot be printed
	
	String SLIP_CREDIT_KEYED_OFF = "JACKPOT.CREDIT.KEYED";// Slip is credit keyed off
	
	String ORIGINAL_AMOUNT = "JACKPOT.ORIGINAL.AMT";// Original amount: 
	
	String ADJUSTED_AMOUNT = "JACKPOT.ADJUSTED.AMT";// Adjusted amount:
	
	String THIS_SLIP_IS = "JACKPOT.THIS.SLIP.IS";// This slip is greater than or equal to 
	
	String PERCENT_GREATER_ORG_AMT_IS_AMT_CORRECT = "JACKPOT.PERCENT.GREATER.ORG.AMT.IS.AMT.CORRECT";// % of the original amount. Is the new slip amount correct?
	
	String JP_PRINTER_PREF_NOT_SET_LOG_OUT = "JACKPOT.PRINTER.PREF.NOT.SET.LOG.OUT";//"Please log out and set the printer preference for Jackpot";
	
	 String SLOT_DOES_NOT_BELONG_TO_THIS_AREA = "JACKPOT.SLOT.DOES.NOT.BELONG.TO.THIS.AREA";//Slot number entered does not belong to this area, please re-enter the slot number
	
	 String SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA = "JACKPOT.SLOT.LOCATION.DOES.NOT.BELONG.TO.THIS.AREA";//Slot location number entered does not belong to this area, please re-enter the slot location number
	
	 String SEQ_SLOT_DOES_NOT_BELONG_TO_THIS_AREA = "JACKPOT.SEQUENCE.SLOT.DOES.NOT.BELONG.TO.THIS.AREA";//Slot number assigned to this slip does not belong to this area, please use another slip sequence number.
	
	 String AREA_DIALOG_BOX_ERROR_PREF = "JACKPOT.PREF.AREA.ERROR";//Unable to get the area preferences for jackpot
	
	 String FIELD_IS_REQUIRED = "JACKPOT.FIELD.IS.REQUIRED";// field is required
	
	 String CONFIRM_MSG_TO_VOID_PENDING_JPS = "JACKPOT.CONFIRM.MSG.TO.VOID.PENDING";// field is required

	String ADDED_TO_TRANS = "JACKPOT.ADDED.TO.TRANS";//added to this transaction.
	
	String FOR_THE_SLOT ="JACKPOT.FOR.SLOT";//for the slot
	
	String CONFIRM_VOID_SEQ_MSG = "JACKPOT.CONFIRM.VOID.MSG";//Are you sure you want to void the jackpot slip sequence
	
	String PREVIOUS_BUTTON_CONFIRM_MSG = "JACKPOT.PREVIOUS.BUTTON.CONFIRM.MSG";//Changes if made will be lost. Do you still want to continue?
	
	String HPJP_AMOUNT = "JACKPOT.HPJP.AMOUNT";//HPJP Amount:
	
	String NET_AMOUNT = "JACKPOT.NET.AMOUNT";//Jackpot Net Amount:
	
	String BARCODE_CREATION_FAILED = "JACKPOT.BARCODE.CREATION.FAILED";
	
	String PRIOR_SLIP_VOIDED_ENTER_MANUALLY = "JACKPOT.PRIOR.SLIP.VOIDED";//is already voided. Please enter the slip sequence number manually to reprint.
	
	String PENDING_PROCESS_FUNC_REQUIRED = "JACKPOT.PROCESS.PENDING.REQ"; //"User should contain the Jackpot - Process Pending Jackpot function";
	String PENDING_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "JACKPOT.PROCESS.PENDING.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Jackpot - Process Pending Jackpot function";
	
	String MANUAL_PROCESS_FUNC_REQUIRED = "JACKPOT.PROCESS.MANUAL.REQ"; //"User should contain the Jackpot - Process Manual Jackpot function";
	String MANUAL_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "JACKPOT.PROCESS.MANUAL.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Jackpot - Process Manual Jackpot function";
		
	String VOID_PROCESS_FUNC_REQUIRED = "JACKPOT.PROCESS.VOID.REQ"; //"User should contain the Jackpot - Void Jackpot function";
	String VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "JACKPOT.PROCESS.VOID.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Jackpot - Void Jackpot function";
	
	String REPRINT_PROCESS_FUNC_REQUIRED = "JACKPOT.PROCESS.REPRINT.REQ"; //"User should contain the Jackpot - Reprint Jackpot function";
	String REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "JACKPOT.PROCESS.REPRINT.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Jackpot - Reprint Jackpot function";
		
	String DISPLAY_PROCESS_FUNC_REQUIRED = "JACKPOT.PROCESS.DISPLAY.REQ"; //"User should contain the Jackpot - Display Jackpot function";
	String DISPLAY_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "JACKPOT.PROCESS.DISPLAY.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Jackpot - Display Jackpot function";
	
	String HPJP_EXCEED_OVERIDE_AUTH_LIMIT = "HPJP.EXCEED.OVERIDE.AUTH.LIMIT"; // TODO
	
	String JACKPOT_VALIDATION_OVERIDE_EMP_PASSWORD = "JACKPOT.VALIDATION.OVERIDE.EMP.PASSWORD";
	// CASHLESS ACCOUNT NUMBER NOT ACTIVE WITH ECASH
	String JKPT_INVALID_ACCOUNT_NUMBER = "JKPT.INVALID.CARDLESS.ACCOUNT.NUMBER"; // Account is not active. Please enter a valid active account number.
	String JKPT_ACCOUNT_NUMBER = "JKPT.ACCOUNT.NUMBER"; // Player account
	String JKPT_PENDING_INVALID_ACCOUNT_NUMBER = "JKPT.PENDING.INVALID.ACCOUNT.NUMBER"; // is not active. Please select another jackpot.
	
	// WARNING MESSAGE FOR ACTIVE PLAYER SESSION
	String PLAYER_SESSION_NOT_ACTIVE = "JKPT.PLAYER.SESSION.NOT.ACTIVE"; //"The Player is not active any more in the slot " 
		
	String ERROR_PRINTING_SLIP_CHK_PRINTER_LOGS = "JACKPOT.PRINTING.SLIP.CHK.PRINTER.SET.LOGS";//"Error while trying to print the slip. Please check the printer settings or logs for more details."
	
	// ERROR MESSAGE FOR INVALID PLAYER CARD NUMBER
	String JKPT_INVALID_PLAYER_CARD = "JKPT.INVALID.PLAYER.CARD"; // Please enter a valid player card.
	
	String PROG_LEVEL_CANNOT_BE_EMPTY = "JKPT.PROG.LVL.CANNOT.BE.EMPTY"; // Progressive Level cannot be empty.
	String ENTER_VALID_PROG_LEVEL = "JKPT.INVALID.PROG.LVL"; //Please enter valid progressive levels.
	
	//FIX FOR NJ ENHANCEMENT CR 99486
	String HPJP_EXCEED_OVERIDE_AUTH_LIMIT_REQ_AUTH = "HPJP.EXCEED.OVERIDE.AUTH.LIMIT.REQ.AUTH";//Amount exceeds employee limit. Requires authorization. 
	String EMP_JOB_LIMIT_N_DOLLAR_VARIANCE_EXCEEDED_AUTH = "JACKPOT.EMP.JOB.LIMIT.N.DOLLAR.VAR.EXCEED.REQ.AUTH";//Amount exceeds employee limit and the adjusted jackpot amount variance is greater than the variance limit. Requires authorization.//simillar to ADJ_AMT_GRT_VAR_LIMIT_REQ_AUTH
	String EMP_JOB_LIMIT_N_PERC_VARIANCE_EXCEEDED_AUTH = "JACKPOT.EMP.JOB.LIMIT.N.PERC.VAR.EXCEED.REQ.AUTH";//Amount exceeds employee limit and the adjusted jackpot amount varies //simillar to ADJUSTED_JACKPOT_AMOUNT_VARIES
	
	//FIX FOR NJ ENHANCEMENT CR 99606
	String JP_INITIATOR_NOT_ALLOWED_TO_PROCESS_EXP_JP = "JACKPOT.INITIATOR.NOT.ALLOWED.TO.PROC.OWN.EXP.JP";//The jackpot initiator is not allowed to process his/her own express jackpot. 
	
}