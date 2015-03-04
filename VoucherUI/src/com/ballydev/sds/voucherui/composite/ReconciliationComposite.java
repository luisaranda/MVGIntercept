/*****************************************************************************
 * $Id: ReconciliationComposite.java,v 1.1, 2008-04-30 14:23:08Z, Nithyakalyani, Raman$
 * $Date: 4/30/2008 9:23:08 AM$
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

package com.ballydev.sds.voucherui.composite;

import org.eclipse.swt.widgets.Composite;

/**
 * This creates the reconciliation composite
 * @author Nithya kalyani R
 * @version $Revision: 2$ 
 */
public class ReconciliationComposite extends BaseReconciliationComposite
{

	/**
	 * Constructor of the class
	 */
	public ReconciliationComposite(Composite parent, int style) {
		super(parent, style);
		setSize(parent.getSize());
	}

	/**
	 * Constructor of the class
	 */
	public ReconciliationComposite(Composite parent, int style,boolean isQuestionableVisible) {
		super(parent, style);
		setSize(parent.getSize());
	}

}
