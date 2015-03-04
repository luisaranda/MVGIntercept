/*****************************************************************************
 * $Id: Barcode.java,v 1.0, 2008-03-27 12:19:47Z, Nithyakalyani, Raman$
 * $Date: 3/27/2008 6:19:47 AM$
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


public class Barcode
{
	public static final String  BARCODE_ELEMENT_TICKET_TYPE= "TICKET_TYPE";
	public static final String BARCODE_ELEMENT_PROPERTY_IDENTIFIER = "PROPERTY_IDENTIFIER";
	public static final String BARCODE_ELEMENT_TICKET_NUMBER = "TICKET_NUMBER";
	public static final String BARCODE_ELEMENT_TICKET_IDENTIFIER ="TICKET_IDENTIFIER";
	public static final String BARCODE_ELEMENT_SLOT_OR_CASHIER_STATION_NUMBER = "SLOT_OR_CASHIER_STATION_NUMBER";
	
	
	private static String[] BARCODE_ELEMENTS={
		BARCODE_ELEMENT_TICKET_TYPE,
		BARCODE_ELEMENT_PROPERTY_IDENTIFIER,
		BARCODE_ELEMENT_SLOT_OR_CASHIER_STATION_NUMBER,
		BARCODE_ELEMENT_TICKET_NUMBER,
		BARCODE_ELEMENT_TICKET_IDENTIFIER
	};
	
	private static int[] BARCODE_ELEMENTS_LENGTH={1,3,5,5,4};

	private String barcode;
	
	public Barcode(String barcode){
		this.barcode = barcode;
	}

	public String getBarcodeProperty(String barcodeElement)
	{
		int endIndex = 0;
		if (barcode != null && barcode.length() == 18)
		{
			for (int i = 0; i < BARCODE_ELEMENTS.length; i++)
			{
				if (BARCODE_ELEMENTS[i].equals(barcodeElement))
				{
					return barcode.substring(endIndex, endIndex
							+ BARCODE_ELEMENTS_LENGTH[i]);
				}
				endIndex += BARCODE_ELEMENTS_LENGTH[i];
			}
		}
		return "";

	}
	
	public String getTicketType()
	{

		String ticketType = getBarcodeProperty(Barcode.BARCODE_ELEMENT_TICKET_TYPE);
		String generatedLocation = getBarcodeProperty(Barcode.BARCODE_ELEMENT_SLOT_OR_CASHIER_STATION_NUMBER);
		if ("5".equals(ticketType) || "6".equals(ticketType))
		{
			return "Voucher";
		} else if ("7".equals(ticketType))
		{
			if ("9".equals(generatedLocation))
			{
				return "Voucher";
			} else
			{
				return "Coupon";
			}
		}

		return "";
	}
	public static void main(String[] args)
	{
		
		System.out.println("BARCODE_ELEMENT_TICKET_TYPE = "+
				new Barcode("567832767781292653").getBarcodeProperty(BARCODE_ELEMENT_TICKET_TYPE));
		System.out.println("BARCODE_ELEMENT_PROPERTY_IDENTIFIER = "+
				new Barcode("567832767781292653").getBarcodeProperty(BARCODE_ELEMENT_PROPERTY_IDENTIFIER));

		System.out.println("BARCODE_ELEMENT_SLOT_OR_CASHIER_STATION_NUMBER = "+
				new Barcode("567832767781292653").getBarcodeProperty(BARCODE_ELEMENT_SLOT_OR_CASHIER_STATION_NUMBER));

		System.out.println("BARCODE_ELEMENT_TICKET_NUMBER = "+
				new Barcode("567832767781292653").getBarcodeProperty(BARCODE_ELEMENT_TICKET_NUMBER));

		System.out.println("BARCODE_ELEMENT_TICKET_IDENTIFIER = "+
				new Barcode("567832767781292653").getBarcodeProperty(BARCODE_ELEMENT_TICKET_IDENTIFIER));

	}
	
	
	
}
