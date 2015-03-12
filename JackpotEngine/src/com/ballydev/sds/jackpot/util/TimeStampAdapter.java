/*****************************************************************************
 * $Id: TimeStampAdapter.java,v 1.0, 2008-04-02 13:30:03Z, Ambereen Drewitt$
 * $Date: 4/2/2008 7:30:03 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpot.util;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Time Stamp Adapter Class
 * @author anantharajr
 * @version $Revision: 1$
 */
public class TimeStampAdapter extends XmlAdapter<String, Timestamp> {

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Timestamp arg0) throws Exception {
		return arg0.toString();

	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Timestamp unmarshal(String arg0) throws Exception {
		return Timestamp.valueOf(arg0);

	}

}
