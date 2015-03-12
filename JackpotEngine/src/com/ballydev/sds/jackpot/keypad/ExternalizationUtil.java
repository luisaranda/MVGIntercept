package com.ballydev.sds.jackpot.keypad;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ballydev.sds.jackpot.constants.IAppConstants;
import com.ballydev.sds.jackpot.log.JackpotEngineLogger;


/**
 * This class is to get and store the externalization values for the keypad process and slip printing.
 * 
 * @author ranjithkumarm
 *
 */
public class ExternalizationUtil {

	private static final Logger log = JackpotEngineLogger.getLogger(IAppConstants.MODULE_NAME);
	
	private static ExternalizationUtil externalizationUtil=null;
	
	private static Map<String, HashMap<String, String>> externalizationMap = null;
	
	private ExternalizationUtil(){
		externalizationMap=new HashMap<String, HashMap<String,String>>();
	}
	
	public static ExternalizationUtil getInstance(){
		if(externalizationUtil==null){
			externalizationUtil=new ExternalizationUtil();
		}
		return externalizationUtil;
	}
	
	
	public Map<String, String> getExternalizationMap(String language, String slipType){
		Map<String, String> externalizedMap=null;
		try{
			if(language!=null && !((language.trim()+" ").equalsIgnoreCase(" "))){
				if(externalizationMap.get(language)!=null && externalizationMap.get(language).size()!=0){
					externalizedMap=externalizationMap.get(language);
					return new HashMap<String, String>(externalizedMap);
				}else{
					//load the language keys.
					HashMap<String, String> mapKeyValue=getSlipKeyValues(slipType, language);
					if(mapKeyValue!=null && mapKeyValue.size()!=0){
						externalizationMap.put(language, mapKeyValue);
						externalizedMap=externalizationMap.get(language);					
						return new HashMap<String, String>(externalizedMap);
					}					
				}
			}			
		}catch (Exception e) {
			
		}
		return externalizedMap;
	}
	
	
	
	public String getExternalizationValue(String key, String language){
		String value=null;
		try{
			if(key!=null && !((key.trim()+" ").equalsIgnoreCase(" ")) &&
					language!=null && !((language.trim()+" ").equalsIgnoreCase(" ")) ){
				if(externalizationMap.get(language)!=null && externalizationMap.get(language).size()!=0){
					value=externalizationMap.get(language).get(key);
					return value;
				}else{
					//load the language keys.
					HashMap<String, String> mapKeyValue=getSlipKeyValues(IKeypadProcessConstants.JACKPOT_PRINT_SLIP_TYPE, language);
					if(mapKeyValue!=null && mapKeyValue.size()!=0){
						externalizationMap.put(language, mapKeyValue);
						value=externalizationMap.get(language).get(key);
						return value;
					}
					
					
					
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	
	
	
	private HashMap<String, String> getSlipKeyValues(String slipType,String language) {
		HashMap<String, String> slipKeyMap = new HashMap<String, String>();
		List<String> keyList = null;
		try {
			keyList = getSlipKeys(slipType);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		for (int i = 0; i < keyList.size(); i++) {
			slipKeyMap.put(keyList.get(i), KeypadUtil.getLabelValue(keyList.get(i),language));			
		}
		return slipKeyMap;
	}
	
	
	private List<String> getSlipKeys(String slipType) throws Exception {

		List<String> keyList = new ArrayList<String>();
		Element fieldElement = null;
		InputStream inStream = 
				ExternalizationUtil.class.getResourceAsStream(IAppConstants.SLIP_SCHEMA_XML_FILE_PATH);

		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(inStream);
		Element slips = doc.getRootElement();
		List slipLst = slips.getChildren();
		Element e = null;
		for (int ii = 0; ii < slipLst.size(); ii++) {
			e = (Element) slipLst.get(ii);

			if ((e.getAttributeValue("name")).equalsIgnoreCase(slipType)) {
				log.info("slip Type :" + e.getAttributeValue("name"));
				break;
			}
		}
		List fieldList = e.getChildren();
		for (int i = 0; i < fieldList.size(); i++) {

			fieldElement = (Element) fieldList.get(i);
			keyList.add(fieldElement.getAttributeValue("key"));
		}

		return keyList;

	}
	
	
	
}
