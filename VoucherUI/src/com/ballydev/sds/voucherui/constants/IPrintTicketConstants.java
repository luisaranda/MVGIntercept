/*****************************************************************************
 *  Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/

package com.ballydev.sds.voucherui.constants;

import com.ballydev.sds.framework.LabelLoader;

/**
 * This contains all the texts that are required
 * for printing the tickets
 * @author Nithya kalyani
 * @version $Revision: 2$ 
 */
public interface IPrintTicketConstants {
	
	String ADDRESS = LabelLoader.getLabelValue("VOU.TKT.PRINT.ADD");
	String CASINO_NAME = LabelLoader.getLabelValue("VOU.TKT.PRINT.CASINO");
	String TICKET_TYPE =LabelLoader.getLabelValue("VOU.TKT.PRINT.TKT.TYPE");
	String VALIDATION_TXT = LabelLoader.getLabelValue("VOU.TKT.PRINT.VALID.TXT");
	String DATE_TXT =LabelLoader.getLabelValue("VOU.TKT.PRINT.DATE.TXT");
	String VOUCHER_TXT =LabelLoader.getLabelValue("VOU.TKT.PRINT.VOU.TXT");
	String AMT_TXT =LabelLoader.getLabelValue("VOU.TKT.PRINT.AMT.TXT");
	String EXPIRE_TXT =LabelLoader.getLabelValue("VOU.TKT.PRINT.EXP.TXT");
	String EMP_TXT =LabelLoader.getLabelValue("VOU.TKT.PRINT.EMP.TXT");
	String CITY="Chennai";
	String COUNTRY="India";

}
