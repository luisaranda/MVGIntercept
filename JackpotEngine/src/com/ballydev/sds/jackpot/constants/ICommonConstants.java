/*****************************************************************************
 * $Id: ICommonConstants.java,v 1.7, 2010-12-15 09:59:27Z, Subha Viswanathan$
 * $Date: 12/15/2010 3:59:27 AM$
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
 * Common Constannts interface
 * @author anantharajr
 * @version $Revision: 8$
 */
public interface ICommonConstants {
	String GAME_THEME="Blazing Seven";
	String GAME_DENOMINATION_IN_CENTS="00001";
	
	int JACKPOT_RESPONSE_DATA_LENGTH = 2; // Denotes a byte array of size 2 for the Jackpot response data
	
	String HAND_PAID_JP_EXCEPTION = "XC10";
	
	String JP_TO_CREDIT_METER_EXCEPTION = "XC30";	
	
	char JACKPOT_POSTED_SYS_EXCEPTION = 100;	
	
	char JACKPOT_PRINTED_SYS_EXCEPTION = 121; 
	
	String SLOT_DOOR_OPEN = "DOOR.OPEN";
	
	String SLOT_DOOR_CLOSED = "DOOR.CLOSED";
	
	// EXCEPTION CODE CONSTANTS	
	char JKPT_VOID_SYS_EXCEPTION 		= 100;	
	char JKPT_CREDIT_MTR_SYS_EXCEPTION 	= 30;	
	char JKPT_HAND_PAID_EXCEPTION 		= 10;
}
