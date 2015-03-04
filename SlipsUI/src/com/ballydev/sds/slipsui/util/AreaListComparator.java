/*****************************************************************************
 * $Id: AreaListComparator.java,v 1.0, 2008-08-14 06:01:39Z, Ambereen Drewitt$
 * $Date: 8/14/2008 1:01:39 AM$
 *****************************************************************************
 *           Copyright  f-(c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.slipsui.util;

import java.util.Comparator;

/**
 * Comparator class to compare two Strings
 * @author dambereen
 * @version $Revision: 1$
 */
public class AreaListComparator implements Comparator{
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object obj1, Object obj2) {
		if(obj1 instanceof String && 
				obj2 instanceof String){
			String object1=(String)obj1;
			String object2=(String)obj2;
			return (object1).compareToIgnoreCase(object2);
		}
		return 0;
	}
}
