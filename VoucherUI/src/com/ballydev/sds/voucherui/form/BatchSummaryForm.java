/*****************************************************************************
 * Id: 
 * Date: Jun 27, 2007 
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

package com.ballydev.sds.voucherui.form;

import java.util.ArrayList;
import java.util.List;


/**
 * @author selvaraj
 * 
 */
public class BatchSummaryForm extends BaseReconciliationForm {
	List<BatchSummaryDTO> batchSummaries = new ArrayList<BatchSummaryDTO>();
	
	public List<BatchSummaryDTO> getBatchSummaries() {		
		return batchSummaries;
	}

	public void setBatchSummaries(List<BatchSummaryDTO> batchSummaries) {
		this.batchSummaries = batchSummaries;
	}
}
