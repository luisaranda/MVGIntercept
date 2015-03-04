/*****************************************************************************
 * $Id: ConversionUtil.java,v 1.4, 2009-09-15 05:45:54Z, Arun, Balasubramaniam$
 * $Date: 9/15/2009 12:45:54 AM$
 *****************************************************************************
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * A utility class to perform conversions like - <BR>
 * Currency conversions from cents to dollars and vice-versa 
 * @author skarthik
  */
public class ConversionUtil {

	public static String centsToDollar(String strAmount){
		try{
			long longAmount = Long.parseLong(strAmount);
			return centsToDollar(longAmount);
		}catch (NumberFormatException e) {
			return centsToDollar(0);
		}
	}
	
	public static String centsToDollar(long longAmount){
		double dblAmt = (double)longAmount/100;		
		return getDecimalFormat().format(dblAmt);
	}
	
	public static String centsToDollar(double doubleAmount){
		double dblAmt = (double)doubleAmount/100;		
		return getDecimalFormat().format(dblAmt);
	}
	
	public static long dollarToCents(double dollarAmount){
		return (long)(Math.round(dollarAmount*100));
	}

	public static long dollarToCents(String dollarAmount){
		try{
			Double amount = Double.parseDouble(dollarAmount);
			return dollarToCents(amount.doubleValue());
		}catch (NumberFormatException e) {
			return dollarToCents(0d);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(ConversionUtil.centsToDollar(10));
		System.out.println(ConversionUtil.centsToDollar(0));
		System.out.println(ConversionUtil.centsToDollar("10"));
		System.out.println(ConversionUtil.centsToDollar("A Non-Number"));
		System.out.println(new Double(ConversionUtil.centsToDollar(1012)).toString());
		System.out.println(dollarToCents(1233.1334d));
		System.out.println(dollarToCents("1233.1334"));
	}
	
	public static String voucherAmountFormat(String strVoucherAmount){
		try
		{
			double voucherAmount = Double.parseDouble(strVoucherAmount);
			return voucherAmountFormat(voucherAmount);
		}
		catch (NumberFormatException e) 
		{
			return voucherAmountFormat(0d);
		}
	}
	
	public static String voucherAmountFormat(double voucherAmount){		
		return getDecimalFormat().format(voucherAmount);
	}
	
	private static DecimalFormat getDecimalFormat() {
		java.text.DecimalFormat fmt = new DecimalFormat();
		fmt.setMaximumFractionDigits(2);
		fmt.setMinimumFractionDigits(2);
		fmt.setGroupingSize(0);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		fmt.setDecimalFormatSymbols(dfs);
		return fmt;
	}
}
