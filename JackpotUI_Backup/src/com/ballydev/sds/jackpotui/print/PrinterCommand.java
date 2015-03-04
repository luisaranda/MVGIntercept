/*****************************************************************************
 * $Id: PrinterCommand.java,v 1.0, 2008-04-03 15:53:52Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:53:52 AM$
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
package com.ballydev.sds.jackpotui.print;

import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import org.jdom.input.SAXBuilder;


import com.ballydev.sds.jackpotui.util.XMLClientUtil;

/**
 * This class parses printer command schema file
 * 
 * @author Govindharaju Mohanasundaram email:GMohanasundaram@ballytech.com
 * @version $Revision: 1$
 */

public class PrinterCommand {

	/**
	 * HashMap Object which contains printer command list
	 * 
	 **/
	private HashMap<String, String> commandList = new HashMap<String, String>();
	
	
	

	/**
	 * This method parses printer command  schema file 
	 
	  * @param printerName  Manufacturer of the printer
	 
	 * @return returns list of printer command
	 
	 * @throws JackpotEngineServiceException if there is an issue while parsing printer schema file
	 
	 */
	public HashMap<String, String> getCommandList(String printerName,String printerSchema) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try {
			File printercmd=XMLClientUtil.stringToFile(printerSchema,"printerschema");
			InputStream inStream=new FileInputStream(printercmd);
			
			doc = builder.build(inStream);
			Element root = doc.getRootElement();
			List lstPrinter = root.getChildren();
			Element e = null;
			for (int i = 0; i < lstPrinter.size(); i++) {
				e = (Element) lstPrinter.get(i);
				if (e.getAttributeValue("name").equalsIgnoreCase(printerName))
					break;
			}

			List lst = e.getChildren();
			for (int i = 0; i < lst.size(); i++) {

				Element element = (Element) lst.get(i);
				if (e.getText() != null) {
					commandList.put(element.getName(), element.getText());

				}

			}

			printercmd.delete();
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}

		return commandList;
	}

}
