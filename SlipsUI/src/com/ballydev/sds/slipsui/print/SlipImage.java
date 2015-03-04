/*****************************************************************************
 * $Id: SlipImage.java,v 1.7.5.1, 2014-05-19 14:21:22Z, Bhandari, Vineet$
 * $Date: 5/19/2014 9:21:22 AM$
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

package com.ballydev.sds.slipsui.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.util.SlipUILogger;
import com.ballydev.sds.slipsui.util.XMLClientUtil;

/**
 * This class builds slip image whith printer commands
 * 
 * @author Govindharaju Mohanasundaram email:GMohanasundaram@ballytech.com
 * @date
 */

public class SlipImage {

	private static final Logger log = SlipUILogger.getLogger(IAppConstants.MODULE_NAME);
	/**
	 * Builds slip image
	 * 
	 * @param dto
	 *            The dto to be used to build slip image
	 * 
	 * @param slipType
	 *            Type of the slip image to be built
	 * 
	 * @return slip image
	 * 
	 * @throws JackpotEngineServiceException
	 *             if there is an issue
	 * 
	 */
	public SlipImage() {

	}

	/**
	 * Method to build the Slip image that will be printed on the slip for an EPSON 40 column printer
	 * @param dto
	 * @param slipType
	 * @param slipI18NMap
	 * @return String
	 * @throws Exception
	 */
	public String buildSlipImage(Object dto, String slipType,HashMap<String,String> slipI18NMap) throws Exception 
	{
		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");
		String printerName = "EPSON"; /** PRINTER NAME - the commands below can be used only by EPSON 40 column printers */
		String retImage = "";

		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Method getDTOMethod;
		Object fieldValue = null;

		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});
			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			log.info(schemaContents[i]);
		}		
		PrinterCommand pc = new PrinterCommand();
		HashMap<String, String> printerCmd = pc.getCommandList(printerName,	schemaContents[0]);

		try {
			File slipschema = XMLClientUtil.stringToFile(schemaContents[1],
					IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(inStream);
			Element slips = doc.getRootElement();
			List slipLst = slips.getChildren();
			Element e = null;
			for (int ii = 0; ii < slipLst.size(); ii++) {
				e = (Element) slipLst.get(ii);

				if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {

					break;
				}
			}

			List fieldList = e.getChildren();

			image = image.append(getCommand(printerCmd.get("PRINT_INIT")));

			for (int k = 0; k < 2; k++) {

				Element fieldElement = (Element) fieldList.get(k);

				if (fieldElement.getAttributeValue("display").equalsIgnoreCase(
				"TRUE")) {

					method = "get" + fieldElement.getName();
					System.out.println("method: "+method);

					image = image.append(getCommand(printerCmd.get(fieldElement
							.getAttributeValue("align")))
							+ getCommand(printerCmd.get(fieldElement
									.getAttributeValue("font")))
									+ getCommand(printerCmd.get(fieldElement
											.getAttributeValue("color"))));

					try {
						getDTOMethod = dtoClass.getMethod(method,
								new Class[] {});
						System.out.println("////: "+getDTOMethod.invoke(dto).toString());

						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}

					if (fieldValue != null){					
						value = fieldValue.toString();
					}/*else{
						value = "_______________";
					}*/						

					/*	if (k == 1){
						value = slipType;
						slipFieldValue="";	
					}else{*/


					if(k==0){ // For Casino Name
						slipFieldValue="";
					}else if(k==1){ // For Slip type
						value = "";
						slipFieldValue=(slipI18NMap.get(fieldElement.getAttributeValue("key"))!=null)?slipI18NMap.get(fieldElement.getAttributeValue("key")):fieldElement.getAttributeValue("key");
					}
					else{
						slipFieldValue=(slipI18NMap.get(fieldElement.getAttributeValue("key"))!=null)?slipI18NMap.get(fieldElement.getAttributeValue("key")):fieldElement.getAttributeValue("key");
					}

					//}

					image = image.append(slipFieldValue
							+ value
							+ getCommand(printerCmd.get(fieldElement
									.getAttributeValue("linefeed"))));

					System.out.println("--------for casino name: "+image);

				}
				fieldValue=null;
			}

			for (int k = 2; k < fieldList.size(); k++) {

				Element fieldElement = (Element) fieldList.get(k);

				if (fieldElement.getAttributeValue("display").equalsIgnoreCase(
				"TRUE")) {

					method = "get" + fieldElement.getName();
					image = image.append( getCommand(printerCmd.get(fieldElement
							.getAttributeValue("align")))
							+ getCommand(printerCmd.get(fieldElement
									.getAttributeValue("font")))
									+ getCommand(printerCmd.get(fieldElement
											.getAttributeValue("color"))));

					try {
						getDTOMethod = dtoClass.getMethod(method,
								new Class[] {});
						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}

					if (fieldValue != null && !fieldValue.equals("")){					
						value = fieldValue.toString();
					}else{
						value = "_______________";
					}

					slipFieldValue=(slipI18NMap.get(fieldElement.getAttributeValue("key"))!=null)?slipI18NMap.get(fieldElement.getAttributeValue("key")):fieldElement.getAttributeValue("key");
					image = image.append(slipFieldValue
							+ ": "
							+ value
							+ getCommand(printerCmd.get(fieldElement
									.getAttributeValue("linefeed"))));

				}
				fieldValue=null;
			}
			image = image.append(getCommand(printerCmd.get("LINE_FEED_4"))
					+ getCommand(printerCmd.get("COLOR_DEFAULT"))
					+ getCommand(printerCmd.get("CENTER")) + "*****"
					+ getCommand(printerCmd.get("LINE_FEED_4")));
			//log.info("image :" + image);
			slipschema.delete();

		} catch (Exception e) {

			e.printStackTrace();

		}
		retImage=image.toString();
		return retImage;

	}


	/**
	 * Method to build the Slip image that will be printed on the slip for a Laser Jet printer
	 * @param dto
	 * @param slipType
	 * @param slipI18NMap
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String getLaserPrinterImage(Object dto, String slipType, 
			HashMap<String, String> slipI18NMap) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		String schemaMethods[] = { "getPrinterSchema", "getSlipSchema" };
		String schemaContents[] = new String[2];
		String method = "";
		String value = "";
		StringBuffer image = new StringBuffer("");
		image.append("\r\n");
		//image.append("\t\t\t");

		String retImage = "";

		Class dtoClass = null;
		String slipFieldValue = "";
		try {
			dtoClass = dto.getClass();

		} catch (Exception e) {

			e.printStackTrace();

		}
		Method getDTOMethod;
		Object fieldValue = null;

		for (int i = 0; i < schemaMethods.length; i++) {
			getDTOMethod = dtoClass.getMethod(schemaMethods[i], new Class[] {});

			schemaContents[i] = (getDTOMethod.invoke(dto)).toString();
			log.info(schemaContents[i]);

		}

		try {

			File slipschema = XMLClientUtil.stringToFile(schemaContents[1],
					IAppConstants.SLIP_SCHEMA_FILE_KEY);
			InputStream inStream = new FileInputStream(slipschema);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(inStream);
			Element slips = doc.getRootElement();
			List slipLst = slips.getChildren();
			Element e = null;
			for (int ii = 0; ii < slipLst.size(); ii++) {
				e = (Element) slipLst.get(ii);

				if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {

					break;
				}
			}

			List fieldList = e.getChildren();
			boolean keyOnly;
			long beefAmount = 0;
			for (int k = 0; k < fieldList.size(); k++) {
				keyOnly = false;
				Element fieldElement = (Element) fieldList.get(k);

				if (fieldElement.getAttributeValue("display").equalsIgnoreCase("TRUE")) {
					method = "get" + fieldElement.getName();

					if(fieldElement.getName().equalsIgnoreCase("KeyAlone"))
						keyOnly = true;
					try {
						getDTOMethod = dtoClass.getMethod(method,
								new Class[] {});
						fieldValue = getDTOMethod.invoke(dto);
					} catch (Exception e1) {

					}
					//Original Amount is there, when we get it save it for future 
					if(fieldElement.getName().equalsIgnoreCase("OriginalAmount")){
						beefAmount = Long.parseLong(fieldValue.toString().replaceAll("\\$", "").replaceAll("\\.", "").replaceAll(",", ""));
					}
					// Now use the previously save amount to get words value
					if(fieldElement.getName().equalsIgnoreCase("AmountInWriting")){
						fieldValue = SlipUtil.getAmountInWords(beefAmount);
						try{
							fieldValue = getStringWithNewLine(fieldValue+"", 60);
						}
						catch(Exception e1){
							//for safety
							fieldValue  = fieldValue+"".substring(0,60)+"\r\n"+
							fieldValue+"".substring(60);
						}
					}

					int numberOfSpaces = 0; String alignment = "";//variable for same line feed
					int numberOfNewLine = 0; //variable for line feed
					if(keyOnly){//This section prints exactly what we given the value for Key
						slipFieldValue = fieldElement.getAttributeValue("key");
						if(!fieldElement.getAttributeValue("linefeed").contains("sameline")){
							image = image.append(SlipUtil.formatField(slipFieldValue, 0));
							numberOfNewLine =  Integer.parseInt(fieldElement.getAttributeValue("linefeed").substring(10));
							for (int j = 0; j < numberOfNewLine; j++) {//When its not same line we will put the specified number of new lines in feed
								image.append("\r\n");
							}
						}
						else{
							//here we will parse the line feed input. it should be sameline_left_10 of this format where "_" used to seprate the alignment alloted width
							// digit in end specify the width of the fields in characters if the final value is of 8 character length and we specified 10 
							//then 2 character will be grabbed by spaces on opposite side of the specified alignment.
							numberOfSpaces = Integer.parseInt(fieldElement.getAttributeValue("linefeed").substring(fieldElement.getAttributeValue("linefeed").lastIndexOf("_")+1));
							alignment = fieldElement.getAttributeValue("linefeed").substring(9,fieldElement.getAttributeValue("linefeed").lastIndexOf("_"));
							numberOfSpaces = numberOfSpaces - slipFieldValue.length();
							for (int i = 0; i < numberOfSpaces ; i++) {
								if(alignment.equalsIgnoreCase("right"))
									slipFieldValue = " "+slipFieldValue;
								else
									slipFieldValue = slipFieldValue+" ";
							}
							image = image.append(SlipUtil.formatField(slipFieldValue, 0));

						}
					}
					else{
						if (fieldValue != null && !fieldValue.equals(""))
							value = fieldValue.toString();
						else
							value = "_______________";

						if(fieldElement.getName().equalsIgnoreCase("Reason")){
							
							try{
								value = getStringWithNewLine(value+"", 50);
							}
							catch(Exception e1){
								//for safety
								value = value.substring(0,50)+"\r\n"+
								value.substring(50);
							}
						}
						System.out.println(value);
						//When we specify the key that means it has to be taken as it is else from map with :
						if(slipI18NMap.get(fieldElement.getAttributeValue("key")) != null && !slipI18NMap.get(fieldElement.getAttributeValue("key")).equals("")){
							slipFieldValue = slipI18NMap.get(fieldElement.getAttributeValue("key"));
							slipFieldValue = SlipUtil.formatField(slipFieldValue, 0) + ": "+value;
						}
						else{
							slipFieldValue = fieldElement.getAttributeValue("key");
							slipFieldValue = SlipUtil.formatField(slipFieldValue, 0) + value;

						}

						if(!fieldElement.getAttributeValue("linefeed").contains("sameline")){
							image = image.append(slipFieldValue);
							numberOfNewLine =  Integer.parseInt(fieldElement.getAttributeValue("linefeed").substring(10));
							for (int j = 0; j < numberOfNewLine; j++) {
								image.append("\r\n");
							}

						}

						else{
							numberOfSpaces = Integer.parseInt(fieldElement.getAttributeValue("linefeed").substring(fieldElement.getAttributeValue("linefeed").lastIndexOf("_")+1));
							alignment = fieldElement.getAttributeValue("linefeed").substring(9,fieldElement.getAttributeValue("linefeed").lastIndexOf("_"));
							numberOfSpaces = numberOfSpaces - slipFieldValue.length();
							for (int i = 0; i < numberOfSpaces ; i++) {
								if(alignment.equalsIgnoreCase("right"))
									slipFieldValue = " "+slipFieldValue;
								else
									slipFieldValue = slipFieldValue+" ";
							}
							image = image.append(slipFieldValue);
						}
					}

				}
				fieldValue = null;
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		retImage = image.toString();
		log.info("SLIP Image: "+retImage);
		return retImage;

	}

	private String getStringWithNewLine(String value, int columnsToNewLine) {
		int originalNewLineindex = columnsToNewLine;
		if(value.length() <= columnsToNewLine)
			return value;
		boolean spaceFound = false;
		char[] chArr = value.toCharArray();
		for (int i = 0; i < chArr.length; i++) {
			char c = chArr[i];
			// finding space in characters 10 before the next line to be entered
			if(i  > (columnsToNewLine - 10) && i <= columnsToNewLine && Character.toString(c).trim().equals("")){
				columnsToNewLine = i+1;
				spaceFound = true;
				break;
			}
		}


		if(!spaceFound){
			for (int i = 0; i < chArr.length; i++) {
				char c = chArr[i];
				// finding 20 characters backwards
				if(i > (columnsToNewLine - 20) && i <= (columnsToNewLine - 10) && Character.toString(c).trim().equals("")){
					columnsToNewLine = i+1;
					break;
				}
			}
		}

		String line1 = value.substring(0,columnsToNewLine)+"\r\n";
		String line2 = value.substring(columnsToNewLine);
		
		if(line2.length() > originalNewLineindex)
			line2 = getStringWithNewLine(line2, originalNewLineindex);
		
		return line1 + line2;
	}

	public String getCommand(String commandStr) throws Exception {

		String retString = "";
		if (commandStr != null) {
			String sa[] = {};
			sa = commandStr.split(",");

			int ia[] = new int[sa.length];

			for (int i = 0; i < sa.length; i++) {

				ia[i] = Integer.parseInt(sa[i].trim());
				retString = retString + (char) ia[i];
			}
		}
		return retString;

	}
}
