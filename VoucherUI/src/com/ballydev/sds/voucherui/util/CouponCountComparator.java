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
import com.ballydev.sds.voucherui.form.BatchSummaryDTO;

/**
 * This class is a comparator class for amount
 * @author Nithya Kalyani
 * @version $Revision: 2$
 * 
 */
@SuppressWarnings("unchecked")
public class CouponCountComparator extends SDSComparator {

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.SDSComparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object obj1, Object obj2) {
		BatchSummaryDTO batchSummaryDTO = (BatchSummaryDTO)obj1;
		BatchSummaryDTO batchSummaryDTO1 = (BatchSummaryDTO)obj2;
		
		int sortValue = 0;

		if(batchSummaryDTO.getCouponCount() > batchSummaryDTO1.getCouponCount()){
			sortValue = 1;
		}
		else if(batchSummaryDTO.getCouponCount() < batchSummaryDTO1.getCouponCount()){
			sortValue = -1;
		}
		else{
			sortValue = 0;
		}

		if(getDirection() == SWT.DOWN){
			sortValue = -1 * sortValue;
		}

		return sortValue;
	}

}
