/*****************************************************************************
 * $Id: FillUtil.java,v 1.0, 2008-04-03 15:52:45Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:52:45 AM$
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
package com.ballydev.sds.jackpotui.util;

/**
 * This class is used to pad spaces
 * @author vijayrajm
 * @version $Revision: 1$
 */
public class FillUtil {

	/**
	 *  fills the empty space with '-'
	 * @param value
	 * @param length
	 * @param fill
	 * @return
	 * @throws Exception
	 */
	public static String fillBoth(
	        String value, int length, char fill)
	        throws Exception {
	        
			if(value==null)
			{
				value="";
			}
			if (value.length() > length) {
	            throw new Exception("Invalid length");
	        }
   
	        StringBuffer sb = new StringBuffer();

	        for (int i = (length - value.length())/2 ; i > 0; i--) {
	            sb.append(fill);
	        }
	        
	        sb.append(value);
	        
	        for (int i = (length - value.length())/2; i > 0; i--) {
	            sb.append(fill);
	        }
	       
	        return sb.toString();
	    }
	/**
	 * fills the empty space with '-'
	 * @param value
	 * @param length
	 * @param fill
	 * @param start
	 * @return
	 * @throws Exception
	 */
	public static String fill(
			String value, int length, char fill,boolean start)
    		throws Exception {
			if(value==null)
			{
				value="";
			}
		 
			if (value.length() > length) {
	            throw new Exception("Invalid length");
	        }

			StringBuffer sb = new StringBuffer();
			
			if(start){
				sb.append(value);
			}
	        

	        for (int i = length - value.length() ; i > 0; i--) {
	            sb.append(fill);
	        }
	        
	        if(!start){
				sb.append(value);
			}
	        
	        
	        return sb.toString();
	}
}
