package com.ballydev.sds.voucherui.util;


public class VoucherPrintUtil {/*

	public static File stringToXMLFile(String str,String name) throws IOException {
		File infile = new File(name+".xml");
		FileOutputStream fos = new FileOutputStream(infile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(str);
		bw.close();
		osw.close();
		fos.close();
		return infile;
	}

	public static HashMap<String, String> getCommandList(String printerName,String printerSchema) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		HashMap<String, String> commandList = new HashMap<String, String>();
		try {
			File printercmd=VoucherPrintUtil.stringToXMLFile(printerSchema,"printerschema");
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
			throw e;
		}

		return commandList;
	}
	
*/}
