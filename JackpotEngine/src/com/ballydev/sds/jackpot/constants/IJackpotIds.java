/*****************************************************************************
 * $Id: IJackpotIds.java,v 1.3.1.0, 2011-05-17 19:01:19Z, SDS12.3.2CheckinUser$
 * $Date: 5/17/2011 2:01:19 PM$$
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
 * Interface that holds all the jackpot ids 
 * @author dambereen
 * @version $Revision: 5$
 */
public interface IJackpotIds {
	int JACKPOT_ID_250_FA 				 = 250; // promo	
	int JACKPOT_ID_251_FB 				 = 251; // MYSTERY
	int JACKPOT_ID_252_FC 				 = 252; // normal
	int JACKPOT_ID_253_FD 				 = 253;
	int JACKPOT_ID_254_FE 				 = 254; // cancel credit
	int JACKPOT_ID_PROG_START_112 		 = 112;
	int JACKPOT_ID_PROG_END_159 		 = 159;
	int JACKPOT_ID_INVALID_PROG_149 	 = 149;
	int JACKPOT_ID_INVALID_PROG_150 	 = 150;
	int JACKPOT_ID_INVALID_186_BA 		 = 186;
	int JACKPOT_ID_INVALID_0 			 = 0;
	int JACKPOT_ID_INVALID_1_FORTUNE 	 = 1;
	
	String JACKPOT_ID_250_FA_STR		 = "FA";
	String JACKPOT_ID_251_FB_STR		 = "FB";
	String JACKPOT_ID_252_FC_STR		 = "FC";
	String JACKPOT_ID_253_FD_STR		 = "FD";
	String JACKPOT_ID_254_FE_STR		 = "FE";
	String JACKPOT_ID_INVALID_186_BA_STR = "BA";
}