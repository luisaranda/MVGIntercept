package com.ballydev.sds.jackpot.keypad;



import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import org.jdom.input.SAXBuilder;





/**
 * This Class Actually acts as an parser for the Epson printer as of now, consuming the printercmd.xml doc as input.
 * 
 * @author ranjithkumarm
 *
 */
public class PrinterCommandParser {

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
	public HashMap<String, String> getCommandList(String printerName) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		InputStream inStream=null;
		try {
			//File printercmd=XMLClientUtil.stringToFile(printerSchema);
			//InputStream inStream=new FileInputStream(printercmd);	
			inStream=PrinterCommandParser.class.getResourceAsStream(IKeypadProcessConstants.PRINTER_CMD_XML_FILE_PATH_KEYPAD);
			doc = builder.build(inStream);			
			Element root = doc.getRootElement();
			List lstPrinter = root.getChildren();
			Element e = null;
			for (int i = 0; i < lstPrinter.size(); i++) {
				e = (Element) lstPrinter.get(i);
				if (e.getAttributeValue("name").equalsIgnoreCase(printerName)){
					break;
				}
			}

			List lst = e.getChildren();
			for (int i = 0; i < lst.size(); i++) {

				Element element = (Element) lst.get(i);
				if (e.getText() != null) {
					commandList.put(element.getName(), element.getText());
				}

			}
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}finally{
			if(inStream!=null){
				inStream.close();
			}
		}

		return commandList;
	}

}
