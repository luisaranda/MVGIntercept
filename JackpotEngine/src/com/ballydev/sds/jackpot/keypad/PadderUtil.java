package com.ballydev.sds.jackpot.keypad;



public class PadderUtil {
	
	/**
	 *	Right Pads the string with given character till specified length 
	 * 
	 * @param String - string to pad 
	 * @param String - padLength, length upto which char is to be padded
	 * @param char - pad Character
	 * @return paddedString  
	 */
	public static String rightPad(String str, int padLength, char padChar) {
		StringBuffer sb = new StringBuffer(str);	
		int strLength = str.length();
		if (strLength < padLength){
			for (int i=0; i<padLength-strLength; i++){
				sb.append(padChar);
			}
		}
		return sb.toString();
	}
	
	/**
	 *	Left Pads the string with given character till specified length 
	 * 
	 * @param String - string to pad 
	 * @param String - padLength, length upto which char is to be padded
	 * @param char - pad Character
	 * @return paddedString  
	 */
	public static String leftPad(String str, int padLength, char padChar) {
		StringBuffer sb = new StringBuffer(str);	
		int strLength = str.length();
		if (strLength < padLength){
			for (int i=0; i<padLength-strLength; i++){
				sb.insert(0, padChar);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method to left pad the value with zeros
	 * @param str
	 * @param length
	 * @return String
	 */
	public static String lPad(String str, int length) {
		 str = (str == null)? "" : str;
		if(str.length()<1 || str.length() >= length) {
			return str;
		}
		StringBuilder sb = new StringBuilder(length);
		for(int i = str.length(); i < length; ++i) {
			sb.append("0");
		}
		sb.append(str);
		return sb.toString();
	}
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		//String plyCharacter = "1"; 
		//log.info(PadderUtil.rightPad(plyCharacter,5,'0'));
		//log.info(PadderUtil.leftPad(plyCharacter,5,'0'));
	}
}
