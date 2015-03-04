/*****************************************************************************
 * $Id: XMLClientUtil.java,v 1.0, 2008-04-03 15:54:16Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:54:16 AM$
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpotui.constants.IAppConstants;

/**
 * Class user for creating the xml file from strings
 * @author anantharajr
 * @version $Revision: 1$
 */
public class XMLClientUtil {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * method creates xml file from strings
	 * @param str
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static File stringToFile(String str,String name) throws IOException 
	{
		File infile = new File("slipschema.xml");
		log.info(infile.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(infile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(str);
		bw.close();
		osw.close();
		fos.close();
		return infile;
	}

	
}
