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
 * This class is a comparator class for employee no
 * @author Nithya Kalyani
 * @version $Revision: 2$
 * 
 */
@SuppressWarnings("unchecked")
public class EmployeeNoComparator extends SDSComparator {

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.SDSComparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		BatchSummaryDTO batchSummaryDTO = (BatchSummaryDTO)arg0;
		BatchSummaryDTO batchSummaryDTO1 = (BatchSummaryDTO)arg1;
		
		int sortValue = 0;

		if(batchSummaryDTO.getEmpId().compareToIgnoreCase(batchSummaryDTO1.getEmpId())< 0){
			sortValue = 1;
		}
		else if(batchSummaryDTO.getEmpId().compareToIgnoreCase(batchSummaryDTO1.getEmpId())> 0){
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
