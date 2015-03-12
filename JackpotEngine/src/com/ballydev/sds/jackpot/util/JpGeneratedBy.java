/*****************************************************************************
 *  Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.util;

/**
 * Enum to get the field that generated the jackpot
 * @author dambereen
 */
public enum JpGeneratedBy {

	SLOT("JP.GENERATED.BY.SLOT"),
	SYSTEM("JP.GENERATED.BY.SYSTEM"),
	CONTROLLER("JP.GENERATED.BY.CONTROLLER");
			
	JpGeneratedBy(String value){
		this.value = value;
	}
	
	private String value;
	
	public String getValue(){
		return value;
	}
	
}
