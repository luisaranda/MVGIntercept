
package com.ballydev.sds.jackpot.keypad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;




/**
 * This class contains XML File Utils.
 * 
 * @author ranjithkumarm
 *
 */
public class XMLClientUtil {

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * method creates xml file from strings
	 * @param str
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static File stringToFile(String str) throws IOException 
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
