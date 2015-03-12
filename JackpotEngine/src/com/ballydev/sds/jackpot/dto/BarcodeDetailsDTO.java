package com.ballydev.sds.jackpot.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Barcode segregator DTO.
 * @author ranjithkumarm
 *
 */
public class BarcodeDetailsDTO implements Serializable{

	
	private static final long serialVersionUID = -4381551377541739624L;
	
	private String siteNumber = null;
	
	private String netAmount = null;
	
	private String sequence = null;

	public String getNetAmount() {
		return netAmount;
	}
	
	
	public BarcodeDetailsDTO(String barcodeToProcess){
		setSiteNumber(barcodeToProcess);
		setNetAmount(barcodeToProcess);
		setSequence(barcodeToProcess);
	}

	private void setNetAmount(String netAmountFromBarcode) {
		if(netAmountFromBarcode!=null){
			try{
				this.netAmount=removePrependZerosForBarcode(netAmountFromBarcode.substring(4, 21));
			}catch (Exception e) {
				this.netAmount = null;
				e.printStackTrace();
			}
		}else{
			this.netAmount = netAmountFromBarcode;
		}
		
	}

	public String getSequence() {
		return sequence;
	}

	private void setSequence(String sequenceFromBarcode) {
		if(sequenceFromBarcode!=null){
			try{
				this.sequence = removePrependZerosForBarcode(sequenceFromBarcode.substring(22));
			}catch (Exception e) {
				this.sequence = null;
				e.printStackTrace();
			}
		}else{
			this.sequence = sequenceFromBarcode;
		}
		
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	private void setSiteNumber(String siteNumberFromBarcode) {
		if(siteNumberFromBarcode!=null){
			try{
				this.siteNumber = removePrependZerosForBarcode(siteNumberFromBarcode.substring(0, 4));
			}catch (Exception e) {
				this.siteNumber = null;
				e.printStackTrace();
			}
		}else{
			this.siteNumber = siteNumberFromBarcode;
		}
		
	}
	
	private static String removePrependZerosForBarcode(String strToProcess){
		String processedString = null;
		try{
			if(strToProcess!=null && !((strToProcess.trim()+" ").equalsIgnoreCase(" ")) ){
				if(strToProcess.contains(".")){
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					decimalFormat.applyLocalizedPattern("0.00");
					decimalFormat.setParseBigDecimal(true);
					processedString = decimalFormat.format(new BigDecimal(strToProcess));
				}else{
					processedString=String.valueOf(Long.parseLong(strToProcess));
				}
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return processedString;
		
		
		
		
	}
	
	
	
	
	
	
	
	
	

}
