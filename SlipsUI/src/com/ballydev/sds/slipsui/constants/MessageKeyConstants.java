/*****************************************************************************
 * $Id: MessageKeyConstants.java,v 1.22, 2010-09-02 07:19:00Z, Ambereen Drewitt$
 * $Date: 9/2/2010 2:19:00 AM$
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
package com.ballydev.sds.slipsui.constants;

/** 
 * Interface that has all the messages database keys
 *  @author dambereen
 *  @version $Revision: 23$
 */
public interface MessageKeyConstants {

	String PLZ_SELECT_A_SEQ_NO = "SLIP.PLZ.SELECT.SEQ.NO";//Please select a sequence no

	String SLIP_PROCESS_FAILED = "SLIP.PROCESS.FAILED";//Slip process has failed

	String SLIP_PROCESS_SUCCESS = "SLIP.PROCESS.SUCCESS";//Slip has been processed successfully

	String SLIP_VOID_SUCCESS = "SLIP.VOID.SUCCESS";//Slip has been voided successfully

	String SLIP_VOID_FAILED = "SLIP.VOID.FAILED";//The void process has failed

	String SLIP_ALREADY_VOIDED = "SLIP.SLIP.ALREADY.VOIDED";//Slip is already voided
	
	String SLIP_ALREADY_PRINTED = "SLIP.ALREADY.PRINTED";//Slip is already printed

	String SLIP_NOT_PRINTED = "SLIP.NOT.PRINTED";//Slip is not printed yet

	String SLIP_REPRINT_SUCCESS = "SLIP.REPRINT.SUCCESS";//Slip has been reprinted successfully

	String SLIP_REPRINT_FAILED = "SLIP.REPRINT.FAILED";//Slip reprint failed
		
	String SLIP_NOT_FOUND = "SLIP.NOT.FOUND";//Slip not found
	
	String SLIP_REPORT_SUCCESS = "SLIP.REPORT.SUCCESS"; //Slip Report has been printed
	
	String SLIP_REPORT_FAILED ="SLIP.REPORT.FAILED"; //Failed to print slip Report 
	
	String EMP_ID_ONE_ALREADY_USED_FOR_TRANS = 	"Employee id one already used for this transaction";//"SLIP.EMPLOYEE.ID.ONE.ALREADY.USED.FOR.TRANSACTION"; //The first authorization employee id has been already used for this transaction

	String EMP_ID_TWO_ALREADY_USED_FOR_TRANS = "Employee id two already used for this transaction";//"SLIP.EMPLOYEE.ID.TWO.ALREADY.USED.FOR.TRANSACTION"; //The second authorization employee id has been already used for this transaction
	
	String AMT_EXCEEDS_EMP_LIMIT = "SLIP.AMOUNT.EXCEEDS.EMPLOYEE.LIMIT";//Slip amount entered exceeds employee limit 
	
	String SUPERVISORY_AUTHORITY_REQUIRED="SLIP.SUPERVISORY.AUTHORITY.REQUIRED";//Supervisory authority is required
		
	String LOW_OVERRIDE_AUTH_LEVEL="SLIP.LOW.OVERRIDE.AUTHORIZATION.LEVEL";//Low override authorization level
	
	String INVALID_AMOUNT = "SLIP.INVALID.AMOUNT";//Invalid slip amount
	
	String NO_RECORDS_FOR_THE_SLOT_SEQUENCE_STAND_NO = "SLIP.NO.RECORDS.FOR.THE.SLOT.SEQUENCE.STAND.NUMBER";//There are no records for the Slot / Slot Location / Sequence no entered
		
	String PLZ_ENTER_SLOT_OR_SEQUENCE_NO = "SLIP.PLZ.ENTER.SLOT.OR.SEQUENCE.NO";//Please enter either the slot / sequence number
	
	String PLZ_ENTER_STAND_OR_SEQUENCE_NO = "SLIP.PLZ.ENTER.STAND.OR.SEQUENCE.NO";//Please enter either the slot location / sequence number
	
	//String PASSWORD_MINIMUM_LENGTH = "SLIP.PASSWORD.MINIMUM.LENGTH";//The password has a minimum length of five.
	
	String ABORT_SLIP_PROCESS = "SLIP.ABORT.PROCESS";//Are you sure you want to abort the current slip process?
	
	String INVALID_SLOT_NO = "SLIP.INVALID.SLOT.NUMBER"; //Invalid slot number
	
	String INVALID_SLOT_LOCATION_NO = "SLIP.INVALID.SLOT.LOCATION.NUMBER";//Invalid slot location number
	
	String WITH_THE_SEQUENCE ="SLIP.WITH.SEQUENCE.NUMBER";//with the sequence
	
	String FOR_THE_SEQUENCE ="SLIP.FOR.SEQUENCE.NUMBER";//for the sequence
	
	String INVALID_EMPLOYEE_ID_OR_PASSWORD ="SLIP.INVALID.EMPLOYEE.ID.OR.PASSWORD";//Invalid employee id or password	
	
	String INVALID_EMPLOYEE_ID ="SLIP.INVALID.EMPLOYEE.ID";//Invalid Employee Id
	
	String INVALID_EMPLOYEE_PASSWORD ="SLIP.INVALID.EMPLOYEE.PASSWORD";//Invalid Employee Password
	
	String ERROR_GETTING_SITE_PARAMETERS = "SLIP.ERROR.GETTING.SITE.PARAMETERS"; //Error while getting the site configuration parameters
	
	String NO_RECORDS_FOR_THIS_AREA = "SLIP.NO.RECORDS.FOR.THIS.AREA";//No Records found for this area
			
	String NO_RECORDS_FOR_THIS_AREA_AND_DENOMINATION = "SLIP.NO.RECORDS.FOR.THIS.AREA.SLOT.DENOMINATION";//No Records found for this area and slot denomination
	
	String NO_RECORDS_FOR_THIS_DENOMINATION = "SLIP.NO.RECORDS.FOR.THIS.SLOT.DENOMINATION";//No Records found for this slot denomination
	
	String SLOT_DENOMINATION_REQUIRES_A_DOUBLE_VALUE = "SLIP.SLOT.DENOMINATION.REQUIRES.DOUBLE.VALUE";//Slot denomination field requires a double value

	String EXCEPTION_WHILE_GETTING_SITE_ID_FROM_FRAMEWORK = "SLIP.EXCEPTION.GETTING.SITE.ID";//Please relogin as an exception occured while getting the site id from framework 
	
	String EXCEPTION_WHILE_CHECKING_SLOT_NO_WITH_ASSET = "SLIP.EXCEPTION.WHILE.CHECKING.SLOT.NO.WITH.ASSET";//Exception has occured while checking with Asset if the slot no entered is valid or not 
	
	String EXCEPTION_WHILE_CHECKING_STAND_NO_WITH_ASSET = "SLIP.EXCEPTION.WHILE.CHECKING.STAND.NO.WITH.ASSET";//Exception has occured while checking with Asset if the stand no entered is valid or not

	String LOGGED_IN_ACTOR_CANNOT_AUTHORIZE_A_SLIP = "SLIP.LOGGED.IN.ACTOR.CANNOT.AUTHORIZE.A.SLIP";//The logged in actor cannot authorize a slip
	
	String USER_AUTHENTICATION_FAILED_CHK_USERNAME_PASSWORD= "SLIP.USER.AUTHENTICATION.FAILED.PLZ.CHECK.USERNAME.PASSWORD";//User authentication failed, please check the username and password
	
	String USER_AUTHENTICATION_FAILED_CHK_USERNAME= "SLIP.USER.AUTHENTICATION.FAILED.PLZ.CHECK.USERNAME";//User authentication failed, please check the username
	
	String USER_AUTHENTICATION_FAILED_CHK_PASSWORD= "SLIP.USER.AUTHENTICATION.FAILED.PLZ.CHECK.PASSWORD";//User authentication failed, please check the password
	
	String ENTER_SLOT_DENOMINATION_VALUE = "SLIP.ENTER.SLOT.DENOMINATION.VALUE";//Please enter a slot denomination value
	
	String FRACTIONS_OF_A_CENT_TOO_LOW_SLIP_AMOUNT = "SLIP.AMOUNT.FRACTIONS.OF.CENT.TOO.LOW";//Fractions of a cent are too low, please re-enter the slip amount
	
	String BLEED_NO_HOPPER_IN_SLOT = "SLIP.BLEED.NO.HOPPER.IN.SLOT";//Slot has no hopper therefore the bleed slip cannot be printed for this slot, please re-enter the slot number
	
	String MANUAL_FILL_NO_HOPPER_IN_SLOT = "SLIP.MANUAL.FILL.NO.HOPPER.IN.SLOT";//Slot has no hopper therefore the manual fill slip cannot be printed for this slot, please re-enter the slot number
	
	String REQUESTOR_NOT_ALLOWED_TO_PROCESS_SLIPS = "SLIP.REQUESTOR.NOT.ALLOWED.TO.PROCESS.SLIPS";//"The DMK Fill employee is not allowed to process this slip
	
	String SLOT_DOES_NOT_BELONG_TO_THIS_SITE = "SLIP.SLOT.DOES.NOT.BELONG.TO.THIS.SITE";//Slot number entered does not belong to this site, please re-enter the slot number
	
	String SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_SITE = "SLIP.SLOT.LOCATION.DOES.NOT.BELONG.TO.THIS.SITE";//Slot location number entered does not belong to this site, please re-enter the slot location number
	
	String CURRENT_SLIP_PROCESS_IS_NOT_ALLOWED_FOR_OFFLINE_SLOTS = "The current slip process is not allowed for offline slots. Please re-enter the slot / slot location number";//SLIP.CURRENT.PROCESS.IS.NOT.ALLOWED.FOR.OFFLINE.SLOTS// TO BE ADDED TO THE DB

	String ABORT_AND_SWITCH_OVER_PROCESS = "Abort and Switch over process";
	
	String SERVICE_DOWN = "SLIP.SERVICE.DOWN";
	
	String NO_PRIOR_SLIPS_TO_REPRINT = "NO.PRIOR.SLIPS.REPRINT"; 
	
	String SLIPS_FUNC_REQ_SIGNED_ON_USER = "SLIP.FUNC.REQ.SIGNED.ON.USER";
	
	String SLOT_IS_NOT_ONLINE = "SLIP.SLOT.NOT.ONLINE";// Slot is not online
	
	String PRIOR_SLIP_VOIDED_ENTER_MANUALLY="SLIP.PRIOR.VOIDED.ENTER.MANUALLY";

	String NO_RECORDS_TO_PRINT_FOR_DATE = "SLIP.NO.RECORDS.TO.PRINT.FOR.DATE";//There are no records to print in the slip report for the date choosen
	
	String SLIP_PRINTER_PREF_NOT_SET_LOG_OUT = "SLIP.PRINTER.PREF.NOT.SET.LOG.OUT";//"Please log out and set the printer preference for Slips";
	
	String BEEF_AMT_EXCEEDS_SITE_MAXIMUM_ALLOWED_LIMIT = "SLIP.BEEF.AMT.EXCEEDS.SITE.MAXIMUM.ALLOWED.LIMIT";//Amount exceeds maximum dispute allowed limit	
	
	String UNABLE_TO_FETCH_ASSET_DETAILS = "SLIP.UNABLE.TO.FETCH.ASSET.DETAILS"; //Unable to fetch the asset details for the slot information entered";
	
	String SLOT_DOES_NOT_BELONG_TO_THIS_AREA = "SLIP.SLOT.DOES.NOT.BELONG.TO.THIS.AREA";//Slot number entered does not belong to this area, please re-enter the slot number
	
	String SLOT_LOCATION_DOES_NOT_BELONG_TO_THIS_AREA = "SLIP.SLOT.LOCATION.DOES.NOT.BELONG.TO.THIS.AREA";//Slot location number entered does not belong to this area, please re-enter the slot location number
	
	String AREA_DIALOG_BOX_ERROR_PREF = "SLIP.PREF.AREA.ERROR";//Unable to get the area preferences for slips
	
	String PRINTER_NOT_ACCEPTING_JOB = "SLIP.PRINT.NOT.ACCEPTING.JOB";//Printer is not accepting the job
	
	String BEEF_PROCESS_FUNC_REQUIRED = "SLIP.PROCESS.DISPUTE.REQ"; //"User should contain the Slips - Process Dispute function";
	String BEEF_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "SLIP.PROCESS.DISPUTE.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Slips - Process Dispute function";
	
	String VOID_PROCESS_FUNC_REQUIRED = "SLIP.PROCESS.VOID.REQ"; //"User should contain the Slips - Void Slip function";
	String VOID_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "SLIP.PROCESS.VOID.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Slips - Void Slip function";
	
	String REPRINT_PROCESS_FUNC_REQUIRED = "SLIP.PROCESS.REPRINT.REQ"; //"User should contain the Slips - Reprint Slip function";
	String REPRINT_PROCESS_FUNC_REQUIRED_FOR_LOGGED_IN_USER = "SLIP.PROCESS.REPRINT.REQ.LOGGED.IN.USER"; //"Logged in user should contain the Slips - Reprint Slip function";
	
	String REPRINT_SLIP_ALREADY_PROCESSED = "SLIP.REPRINT.IS.ALREADY.PROCESSED";//slip is already processed and cannot be reprinted.
	String REPRINT_SLIP_ALREADY_PRINTED_CASH_DESK_DISABLED = "SLIP.REPRINT.IS.ALREADY.PRINTED.CASH.DESK.DISABLED";//slip is already printed and cannot be reprinted as cash desk is disabled.
	
	String ACCOUNT_NOS_DO_NOT_MATCH = "SLIP.ACC.NOS.DO.NOT.MATCH";//Account numbers do not match, please re-enter the account no.
	String ACCOUNT_IS_NOT_CASHLESS_ACC_NO = "SLIP.ACC.NOT.CASHLESS";//This is not a cashless account number, please re-enter the account no.
	
	String INVALID_ACCOUNT_NUMBER = "SLIP.INVALID.ACCOUNT.NUMBER";//Account is not valid. Please enter a valid active account number.
	//String INACTIVE_ACCOUNT_NUMBER = "SLIP.INACTIVE.ACCOUNT.NUMBER";//Account is not active. Please enter a valid active account number.
	String INVALID_ACCOUNT_NUMBER_FOR_VOID = "SLIP.INVALID.ACCOUNT.NO.FOR.VOID";//The Account no {0} associated to the slip is invalid or inactive, hence it cannot be voided.
}
