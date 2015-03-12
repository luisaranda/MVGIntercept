/*****************************************************************************
 * $Id: IAlertMessageConstants.java,v 1.2, 2010-12-15 09:59:27Z, Subha Viswanathan$
 * $Date: 12/15/2010 3:59:27 AM$
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
package com.ballydev.sds.jackpot.constants;

/**
 * Interface that contains the Alert Message Numbers that are handlde by the Jackpot Engine
 * @author dambereen
 * @version $Revision: 3$
 */
public interface IAlertMessageConstants {

	/** CB MESSAGES FOR THE EXCEPTION CODE 10 */	
	int JACKPOT 						= 1;	
	int PROGRESSIVE_JACKPOT 			= 2;	
	int INVALID_JACKPOT 				= 3;	
	int JACKPOT_SLOT_STATUS_INVALID 	= 4;	
	int JACKPOT_UNKNOWN_SLOT 			= 5;	
	int FORTUNE_JACKPOT 				= 49;	
	int PENDING_JACKPOT_SLIP_FOR_SLOT 	= 92;	
	int PROGRESSIVE_JP_UNKNOWN_AMT 		= 101;	
	int JP_INVALID_AMT_ZERO 			= 161;	
	int JP_INVALID_COIN_IN 				= 162;	
	int JP_INVALID_JP_ID 				= 163;
	
	/** CB MESSAGES FOR THE EXCEPTION CODE 30 */	
	int JP_TO_CREDIT_METER = 204;
	//int NO_SLIP_FOR_FE_JP_UNDER_SLOTX = 207; // NOT SURE HOW IT SHD BE HANDLED
	
	/** CB MESSAGES FOR THE EXCEPTION CODE 100 */	
	int SLIP_VOIDED 			= 19;	
	int JACKPOT_PRINTED 		= 103;
	int JP_AMT_ADJUSTED_NEW_AMT = 104;	
	int MANUAL_JP_ENTERED_FOR 	= 105;
	int PROMOTIONAL_JP_FOR 		= 186;
	
	String JP_AMT_ADJUSTED_NEW_AMT_MESG 	= "JACKPOT AMOUNT ADJUSTED, NEW AMOUNT";
	String JACKPOT_PRINTED_MSG 				= "JACKPOT PRINTED";
	String JP_TO_CREDIT_METER_MSG 			= "JACKPOT TO CREDIT METER";
	String JACKPOT_SLOT_STATUS_INVALID_MSG 	= "JACKPOT_SLOT_STATUS_INVALID";
	String MANUAL_JP_ENTERED_FOR_MSG 		= "MANUAL JACKPOT ENTERED FOR:";
	String SLIP_VOIDED_MSG 					= "SLIP VOIDED";
}
