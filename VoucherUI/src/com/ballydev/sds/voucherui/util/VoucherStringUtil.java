/*****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.util;

import com.ballydev.sds.framework.util.StringUtil;

/**
 * 
 * Util class for manipulation of Strings
 * 
 * @author Nithya kalyani
 * @version $Revision: 3$
 *
 */
public class VoucherStringUtil {

	public static String initCap(String str) {
		String modifiedString = null;
		modifiedString = str.toLowerCase();
		modifiedString= StringUtil.initCap(modifiedString);	
		return modifiedString;
	}	

	public static String replaceAmp(String audit_trial_desc){
		audit_trial_desc = audit_trial_desc.replace('&',' ');
		return audit_trial_desc;
	}

	public static String rPad(String str, int length, char padChar) {
		if(str == null || str.length() >= length) {
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		sb.append(str);
		for(int i = str.length(); i < length; ++i) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	public static String rPad(String str, int length) {
		if(str == null || str.length() >= length) {
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		sb.append(str);
		for(int i = str.length(); i < length; ++i) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String lPad(String str, int length) {
		if(str == null || str.length() >= length) {
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		for(int i = str.length(); i < length; ++i) {
			sb.append(" ");
		}
		sb.append(str);
		return sb.toString();
	}

	public static String nCopy(char data, int length) {
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; ++i) {
			sb.append(data);
		}
		return sb.toString();
	}

	public static String lPadWithZero(String str, int length) {
		if(str == null || str.length() >= length) {
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		for(int i = str.length(); i < length; ++i) {
			sb.append("0");
		}
		sb.append(str);
		return sb.toString();
	}
}
