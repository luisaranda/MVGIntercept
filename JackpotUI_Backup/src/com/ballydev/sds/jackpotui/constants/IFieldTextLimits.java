/*****************************************************************************
 * $Id: IFieldTextLimits.java,v 1.11, 2011-02-14 15:04:18Z, Subha Viswanathan$
 * $Date: 2/14/2011 9:04:18 AM$
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
 * Interface that contains the text limits used for the UI text fields
 * @author dambereen
 * @version $Revision: 12$
 */
public interface IFieldTextLimits {
	
	int EMPLOYEE_ID_TEXT_LIMIT = 5;
	
	int EMPLOYEE_PASSWORD_TEXT_LIMIT = 20;
	
	int SLOT_LOCATION_TEXT_LIMIT = 8;
	
	int SLOT_NO_TEXT_LIMIT = 5;
	
	int SEQUENCE_NO_TEXT_LIMIT = 18;
	
	int MINUTES_TEXT_LIMIT = 5;
	
	int HPJP_AMOUNT_TEXT_LIMIT = 25;
	
	int WIN_COMB_TEXT_LIMIT = 15;
	
	int PAYLINE_TEXT_LIMIT = 10;
	
	int COINS_PLAYED_TEXT_LIMIT = 10;
	
	int WINDOW_TEXT_LIMIT = 3;
	
	int ACCOUNT_NUMBER_LIMIT = 40;
	
	int PLAYER_CARD_LIMIT = 64;
	
	int PROGRESSIVE_LEVEL_LIMIT = 50;
	
	int PLAYER_NAME_LIMIT = 64;
}
