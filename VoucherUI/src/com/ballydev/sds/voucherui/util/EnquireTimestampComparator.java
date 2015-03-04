/*****************************************************************************
 *  Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.util;

import org.eclipse.swt.SWT;

import com.ballydev.sds.framework.SDSComparator;
import com.ballydev.sds.voucher.dto.TicketInfoDTO;
import com.ballydev.sds.voucher.dto.TransactionListDTO;
/**
 * This class is a comparator class for created time 
 * @author Nithya Kalyani
 * @version $Revision: 4$
 * 
 */
@SuppressWarnings("unchecked")
public class EnquireTimestampComparator extends SDSComparator {

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.SDSComparator#compare(java.lang.Object, java.lang.Object)
	 */
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
			
		} else {
			TransactionListDTO transactionListDTO = (TransactionListDTO)arg0;
			TransactionListDTO transactionListDTO1 = (TransactionListDTO)arg1;

			if(transactionListDTO.getDateTime().getTime()<(transactionListDTO1.getDateTime().getTime())){
				sortValue = -1;
			}
			else if(transactionListDTO.getDateTime().getTime()>(transactionListDTO1.getDateTime().getTime())){
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
