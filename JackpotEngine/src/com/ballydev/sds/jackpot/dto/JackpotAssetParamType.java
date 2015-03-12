/*****************************************************************************
 * $Id: JackpotAssetParamType.java,v 1.0, 2008-04-02 13:29:29Z, Ambereen Drewitt$
 * $Date: 4/2/2008 7:29:29 AM$
 * $Log:
 *  1    SDS11     1.0         9/11/2007 4:23:27 PM   Ambereen Drewitt Initial
 *       revision
 * $
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

package com.ballydev.sds.jackpot.dto;

/**
 * This enumeration value is used by the other engines to decribe the 
 * type of parameter passed in the web method getJackpotAssetInfo()
 * @author dambereen 
 * @version $Revision: 1$
 */
public enum JackpotAssetParamType {
	ASSET_CONFIG_ID,
	ASSET_CONFIG_NUMBER,
	ASSET_CONFIG_LOCATION
}