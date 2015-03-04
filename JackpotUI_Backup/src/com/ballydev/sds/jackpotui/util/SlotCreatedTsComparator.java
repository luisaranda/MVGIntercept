package com.ballydev.sds.jackpotui.util;

import com.ballydev.sds.framework.SDSComparator;
import com.ballydev.sds.jackpot.dto.JackpotAssetInfoDTO;

/**
 * Class to compare two JackpotAssetInfoDTO dto's based on the slot's createdTs
 * @author dambereen
 *
 */
public class SlotCreatedTsComparator extends SDSComparator<JackpotAssetInfoDTO> {

	public int compare(JackpotAssetInfoDTO o1, JackpotAssetInfoDTO o2) {

			int sortValue = 0;	
			sortValue = 1;			
			if(o1.getCreatedTs() > o2.getCreatedTs()){
				sortValue = -1;
			}
			else if(o1.getCreatedTs() < o2.getCreatedTs()){
				sortValue = 1;
			}
			else{
				sortValue = 0;
			}	
		return sortValue;
	}

}
