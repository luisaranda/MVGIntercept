package com.ballydev.sds.jackpot.util;

import java.util.Comparator;

import com.ballydev.sds.jackpot.dto.JackpotResetDTO;

public class JpResetDTOComparator implements Comparator<JackpotResetDTO> {

	private String inputType = null;
	
	public JpResetDTOComparator(String inputType) {
		this.inputType = inputType;
	}
	
	public int compare(JackpotResetDTO obj1, JackpotResetDTO obj2){
		return this.compare(obj1, obj2, inputType);
	}
	
	public int compare(JackpotResetDTO obj1, JackpotResetDTO obj2, String inputType) {
	
		
		if(inputType.equalsIgnoreCase("Slot")) {
			// asc order
			if(Integer.parseInt(obj1.getSlotNo()) > Integer.parseInt(obj2.getSlotNo())) {
				return 1;
			}else if(Integer.parseInt(obj1.getSlotNo()) < Integer.parseInt(obj2.getSlotNo())) {
				return -1;
			}else {
				return 0;
			}
		}else {
			// desc order
			if(obj1.getTransactionDate().getTime() > obj2.getTransactionDate().getTime()) {
				return -1;
			}else if(obj1.getTransactionDate().getTime() < obj2.getTransactionDate().getTime()) {
				return 1;
			}else {
				return 0;
			}
		}
		
		
	}

	
}
