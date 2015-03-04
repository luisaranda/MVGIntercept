/*****************************************************************************
 * $Id: WordDecimalFormatUtil.java,v 1.0, 2008-03-27 12:19:17Z, Nithyakalyani, Raman$
 * $Date: 3/27/2008 6:19:17 AM$
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
package com.ballydev.sds.voucherui.util;

/**
 * A Utility class to convert numbers into words.
 * 
 */
public class WordDecimalFormatUtil {
	private static final String[] majorNames = { "", " Thousand", " Million",
			" Billion", " Trillion", " Quadrillion", " Quintillion" };

	private static final String[] tensNames = { "", " Ten", " Twenty",
			" Thirty", " Fourty", " Fifty", " Sixty", " Seventy", " Eighty",
			" Ninety" };

	private static final String[] numNames = { "", " One", " Two", " Three",
			" Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten",
			" Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen",
			" Sixteen", " Seventeen", " Eighteen", " nineteen" };

	private String convertLessThanOneThousand(int number) {
		String soFar;

		if (number % 100 < 20) {
			soFar = numNames[number % 100];
			number /= 100;
		} else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		return numNames[number] + " Hundred" + soFar;
	}

	public String convert(int number) {
		/* special case */
		if (number == 0) {
			return "Zero";
		}

		String prefix = "";

		if (number < 0) {
			number = -number;
			prefix = "Negative";
		}

		String soFar = "";
		int place = 0;

		do {
			int n = number % 1000;
			if (n != 0) {
				String s = convertLessThanOneThousand(n);
				soFar = s + majorNames[place] + soFar;
			}
			place++;
			number /= 1000;
		} while (number > 0);

		return (prefix + soFar).trim();
	}

	public static void main(String[] args) {
		WordDecimalFormatUtil f = new WordDecimalFormatUtil();
		System.out.println("*** " + f.convert(0));
		System.out.println("*** " + f.convert(1));
		System.out.println("*** " + f.convert(16));
		System.out.println("*** " + f.convert(100));
		System.out.println("*** " + f.convert(118));
		System.out.println("*** " + f.convert(200));
		System.out.println("*** " + f.convert(219));
		System.out.println("*** " + f.convert(800));
		System.out.println("*** " + f.convert(801));
		System.out.println("*** " + f.convert(1316));
		System.out.println("*** " + f.convert(1000000));
		System.out.println("*** " + f.convert(2000000));
		System.out.println("*** " + f.convert(3000200));
		System.out.println("*** " + f.convert(700000));
		System.out.println("*** " + f.convert(9000000));
		System.out.println("*** " + f.convert(123456789));
		System.out.println("*** " + f.convert(-45));
		/*
		 *** zero
		 *** one
		 *** sixteen
		 *** one hundred
		 *** one hundred eighteen
		 *** two hundred
		 *** two hundred nineteen
		 *** eight hundred
		 *** eight hundred one
		 *** one thousand three hundred sixteen
		 *** one million
		 *** two million
		 *** three million two hundred
		 *** seven hundred thousand
		 *** nine million
		 *** one hundred twenty three million four hundred 
		 **                fifty six thousand seven hundred eighty nine
		 *** negative fourty five
		 */
	}
}
