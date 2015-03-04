/**
 * 
 */
package com.ballydev.sds.voucherui.util;

import org.eclipse.swt.SWT;

import com.ballydev.sds.framework.SDSComparator;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;

/**
 * @author VNitinkumar
 *
 */
@SuppressWarnings("unchecked")
public class TicketTimestampComparator extends SDSComparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		
		int sortValue = 0;
		if( arg0 instanceof TicketInfoDTO && arg1 instanceof TicketInfoDTO ) {
			TicketInfoDTO ticketInfoDTO = (TicketInfoDTO)arg0;
			TicketInfoDTO ticketInfoDTO1 = (TicketInfoDTO)arg1;
			if(ticketInfoDTO.getEffectiveDate().getTime()<(ticketInfoDTO1.getEffectiveDate().getTime())){
				sortValue = -1;
			}
			else if(ticketInfoDTO.getEffectiveDate().getTime()>(ticketInfoDTO1.getEffectiveDate().getTime())){
				sortValue = 1;
			}
			else{
				sortValue = 0;
			}		
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}
		return sortValue;
	}

}
