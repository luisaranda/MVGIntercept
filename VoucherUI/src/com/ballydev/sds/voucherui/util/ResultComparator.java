/*****************************************************************************
 * $Id: ResultComparator.java,v 1.0, 2010-06-18 14:23:47Z, Verma, Nitin Kumar$
 * $Date: 6/18/2010 9:23:47 AM$
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
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

/**
 * @author VNitinkumar
 */
@SuppressWarnings("unchecked")
public class ResultComparator extends SDSComparator {

	@Override
	public int compare(Object arg0, Object arg1) {

		int sortValue = 0;
		
		TicketInfoDTO ticketInfoDTO = (TicketInfoDTO)arg0;
		TicketInfoDTO ticketInfoDTO1 = (TicketInfoDTO)arg1;
		
		if( ticketInfoDTO.getErrorCodeId() < ticketInfoDTO1.getErrorCodeId() ) {
			sortValue = 1;
		}
		else if(ticketInfoDTO.getErrorCodeId() > ticketInfoDTO1.getErrorCodeId()) {
			sortValue = -1;
		}
		else {
			sortValue = 0;
		}		
		if( getDirection() == SWT.DOWN ) {
			sortValue = -1 * sortValue;
		}
	
		return sortValue;
	}
}
