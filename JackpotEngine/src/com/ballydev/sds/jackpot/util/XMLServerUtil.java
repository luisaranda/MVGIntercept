/*****************************************************************************
 * $Id: XMLServerUtil.java,v 1.0, 2008-04-02 13:29:21Z, Ambereen Drewitt$
 * $Date: 4/2/2008 7:29:21 AM$
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
package com.ballydev.sds.jackpot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This utility class is to convert an xml file into a String object.
 * 
 * @author anantharajr
 * @version $Revision: 1$
 */
public class XMLServerUtil {

	/**
	 * This method converts an xml file into a String Object.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getFileAsString(String file) throws IOException {

		InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream(file));
		BufferedReader br = new BufferedReader(isr);

		String retString = "";
		StringBuffer sb = new StringBuffer();
		while ((retString = br.readLine()) != null) {
				sb.append(retString);
		}
		br.close();
		isr.close();
		retString = sb.toString();
		return retString;
	}

	public static void main(String args[]) throws IOException {
		//XMLServerUtil util = new XMLServerUtil();
	}

}
