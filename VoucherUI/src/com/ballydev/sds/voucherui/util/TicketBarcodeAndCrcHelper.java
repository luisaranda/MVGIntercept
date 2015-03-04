/*****************************************************************************
 * $Id: TicketBarcodeAndCrcHelper.java,v 1.1, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:46:20 AM$
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


public class TicketBarcodeAndCrcHelper
{
	/**
	 * @param mbarcode 18 digit barcode
	 * @param mamount amount in pennies
	 * @param mseed seed value from when ticket was created
	 * Set a ticket value. For reuse of ticket object 
	 */
	public void set ( String barcode, long amount, byte[] seed ) { 
			if (barcode.startsWith("6")) {
				String first = barcode.substring(0, 9);
				String second = barcode.substring(9, 13);
				String third = barcode.substring(13);
				barcode = first + third + second;
			}
			if( seed == null ) {
				seed = new byte[16];
			}
	}

	/**
	 * Used to verify that a ticket is valid 
	 * @returns true if the barcode is valid; or false if invalid.
	 */
	public static boolean verifyBarcodeCheckDigit(String barcode) throws Exception {
		return verifyCheckDigit(barcode);
	}
	
	/**
	 * Used to verify that a ticket is valid 
	 * @returns true if the barcode is valid; or false if invalid.
	 */
	public static boolean verifyBarcodeCrc(String barcode,long amount,byte[] seed) throws Exception {
		return  verifyTicketCRC(barcode,amount,seed);
	}
	
		
	/**
	 * Part of the ticket verifcation process.
	 * used to verify that all digits in the barcode match and
	 * were not transposed.
	 */
	@SuppressWarnings("unused")
	private static byte createCheckDigit(String partBarcode) throws Exception
	{
		String scr;
		int length;
		byte chkDigit;
		byte digit;
		int	i, j=0, chk;

		String text = partBarcode; 
		length = text.length();

		chk = (text.charAt(j++) - 0x30) * 3;
		for (i = 0; i < (length / 2); i++)
		{
			chk += (text.charAt(j++) - 0x30) * 1;
			chk += (text.charAt(j++) - 0x30) * 3;
		}

		scr = Integer.toString( chk );
		digit = (byte) scr.charAt( scr.length()-1 );
		digit -= 0x30;
		chkDigit = 0;

		while (digit != 0x0A)
		{
			digit++;
			chkDigit++;
		}

		if (chkDigit == 0x0A)
			chkDigit = 0;

		/* Convert it to ASCII before returning it.*/
		//chkDigit += 0x30;

		return chkDigit; 
	}

	/**
	/**
	 * Part of the ticket verifcation process.
	 * used to verify that all digits in the barcode match and
	 * were not transposed.
	 */
	private static boolean verifyCheckDigit (String barcode) throws Exception
	{
		@SuppressWarnings("unused")
		String scr1;
		String scr;
		int length;
		byte chkDigit;
		byte digit;
		int	i, j=0, chk;

		String text = barcode.substring(0,17);
		length = text.length();

		chk = (text.charAt(j++) - 0x30) * 3;
		for (i = 0; i < (length / 2); i++)
		{
			chk += (text.charAt(j++) - 0x30) * 1;
			chk += (text.charAt(j++) - 0x30) * 3;
		}

		scr = Integer.toString( chk );
		digit = (byte) scr.charAt( scr.length()-1 );
		digit -= 0x30;
		chkDigit = 0;

		while (digit != 0x0A)
		{
			digit++;
			chkDigit++;
		}

		if (chkDigit == 0x0A)
			chkDigit = 0;

		/* Convert it to ASCII before returning it.*/
		chkDigit += 0x30;

		if( chkDigit == barcode.charAt(17) ) 
		 	return true;	

		return false; 
	}

	/**
	 * Part of the ticket verifcation process.
	 * Used to verify that the amount, seed, and barcode 
	 * all match. Used to prevent fraudulent tickets. 
	 */
	private static int TicketCRC  ( String data, int seed ) throws Exception
	{
		int   i;	
		int crc = seed;

		int[] table =
		{
        0x0000, 0xa184, 0x4314, 0x344c, 0x4244, 0x46cb, 0x2432, 0x64ce,
        0x8148, 0x4bc1, 0xce4c, 0xceb3, 0xcc2c, 0xbce4, 0xe40e, 0xe8e6,
        0x1081, 0x0108, 0x3343, 0x441c, 0x4284, 0x464c, 0x64c6, 0x243e,
        0x47c4, 0x8b40, 0xcebc, 0xce44, 0xbceb, 0xcc24, 0xe40e, 0xe862,
        0x4104, 0x308c, 0x0410, 0x1344, 0x2642, 0x62ce, 0x4434, 0x44cb,
        0xcb4c, 0xcbc3, 0x8ea8, 0x4eb1, 0xec2e, 0xece6, 0xc80c, 0xb4e4,
        0x3183, 0x400c, 0x1441, 0x0318, 0x66c6, 0x224e, 0x44c4, 0x443c,
        0xcbcc, 0xcc44, 0x4eb4, 0x8e40, 0xecee, 0xec22, 0xb89b, 0xc464,
        0x4204, 0x438b, 0x2112, 0x604e, 0x0440, 0x14c4, 0x4634, 0x32cc,
        0xc74c, 0xbec4, 0xeb4e, 0xecb6, 0x8828, 0x44e1, 0xcc6c, 0xcce3,
        0x4484, 0x430c, 0x6146, 0x201e, 0x14c1, 0x0448, 0x3683, 0x423c,
        0xbecb, 0xce44, 0xebbe, 0xec42, 0x48e4, 0x8420, 0xccec, 0xcc64,
        0x2302, 0x648e, 0x4014, 0x414b, 0x4444, 0x34cc, 0x0210, 0x16c4,
        0xee4e, 0xeec6, 0xcc4c, 0xbbb4, 0xc42c, 0xc8e3, 0x8c68, 0x4ce1,
        0x6386, 0x240e, 0x4044, 0x411c, 0x34c3, 0x444c, 0x12c1, 0x0638,
        0xeece, 0xee42, 0xbabb, 0xcb44, 0xc4ec, 0xc824, 0x4c34, 0x8c60,
        0x8408, 0x4481, 0xc61c, 0xc243, 0xc44c, 0xb3c4, 0xe13e, 0xe0c6,
        0x0840, 0x14c4, 0x4c44, 0x3cbc, 0x4e54, 0x4eeb, 0x2b62, 0x6cee,
        0x4484, 0x8400, 0xc64c, 0xc214, 0xb4cb, 0xc344, 0xe17e, 0xe032,
        0x18c1, 0x0448, 0x3cb3, 0x4c4c, 0x4ee4, 0x4e2c, 0x6be6, 0x2c6e,
        0xc40c, 0xc483, 0x8a18, 0x4641, 0xe34e, 0xe4c6, 0xc03c, 0xb1c4,
        0x4444, 0x38cc, 0x0c40, 0x1cb4, 0x2e22, 0x6eee, 0x4c64, 0x4beb,
        0xc48c, 0xc504, 0x4244, 0x8610, 0xe3ce, 0xe442, 0xb0eb, 0xc134,
        0x34c3, 0x484c, 0x1cb1, 0x0c48, 0x6ee6, 0x2e2e, 0x4ce4, 0x4b6c,
        0xc20c, 0xb684, 0xe91e, 0xe446, 0x8048, 0x41c1, 0xc33c, 0xc4c3,
        0x4c44, 0x4ccb, 0x2442, 0x68be, 0x0c20, 0x1be4, 0x4e64, 0x3eec,
        0xb28b, 0xc604, 0xe44e, 0xe412, 0x4064, 0x8140, 0xc3dc, 0xc434,
        0x49c4, 0x4c4c, 0x64b6, 0x284e, 0x1ce1, 0x0b28, 0x3ee3, 0x4e6c,
        0xe60e, 0xe286, 0xc81c, 0xb444, 0xc14c, 0xc0c3, 0x8438, 0x43c1,
        0x2c42, 0x65ce, 0x4544, 0x44bb, 0x4bc4, 0x3cec, 0x0e60, 0x1ee4,
        0xe68e, 0xe202, 0xb44b, 0xc414, 0xc1cc, 0xc044, 0x44c4, 0x8330,
        0x67c6, 0x254e, 0x48b4, 0x444c, 0x3be3, 0x4c2c, 0x1ee1, 0x0e68
    	};

		int j=0;
		for (i=0; i<data.length(); i++) {
			crc = (crc >> 8) ^ table[data.charAt(j++) ^ (crc & 0xff)];
		}
		return crc;
	}

	/**
	 * Part of the ticket verifcation process.
	 * Used to verify that the amount, seed, and barcode 
	 * all match. Used to prevent fraudulent tickets. 
	 */
	private	static boolean verifyTicketCRC(String barcode,long amount,byte[] seed) throws Exception
	{
		int crc;

		int bar = Integer.parseInt(barcode.substring(14,17));
		String partbar = barcode.substring(0,14);

		crc = CreateCRC ( partbar, amount, seed );
		
		if( crc == bar ) 
			return true;

		return false;
	}

	/**
	 * Part of the ticket verifcation process.
	 * @param barcode 18 digit barcode
	 * @param amount amount of ticket in pennies 
	 * @param key seed or key value for this ticket 
	 */
	private static int CreateCRC  (
			String barcode,
			long amount,
			byte[] key ) throws Exception
	{
		int	crc;
		String str;
		ByteArrayOutputStream bytebuffer = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream( bytebuffer ); 	
		stream.write(key, 0, key.length) ;
		@SuppressWarnings("unused")
		byte[] tmp = null;
		tmp = bytebuffer.toByteArray();
		stream.writeInt((int)amount);
		stream.writeBytes(barcode) ;
		str = bytebuffer.toString();
		crc = TicketCRC( str, 0 );
		crc = crc % 1000 ;
		return crc;
	}

}
