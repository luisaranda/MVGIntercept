/*****************************************************************************
 * $Id: UserFunctionsConstants.java,v 1.5, 2009-11-17 11:29:46Z, Ambereen Drewitt$
 * $Date: 11/17/2009 5:29:46 AM$
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
 * This class holds the User funtions parameter for Booth Cashier Employee.
 * 
 * @author anantharajr
 * 
 */
public interface UserFunctionsConstants {
	
	String PROCESS_BEEF = "Slips - Process Dispute";
	
	String VOID_SLIP = "Slips - Void Slip";
	
	String REPRINT_SLIP = "Slips - Reprint Slip";
		
	String SUPERVISORY_AUTHORITY_FUNCTION = "Slips - Supervisory Authority";

	Boolean REPORT_ALLOWED = true;//MainMenuController.slipsUserFunctions.get(UserFunctionsConstants.PROCESS_REPORT)!=  null ? true : false ;
	
	String AUTH_LEVEL_USER_PARAMETER = "AUTHORIZATION LEVEL";
}
