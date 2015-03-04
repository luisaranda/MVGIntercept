/*****************************************************************************
 * $Id: EmptyCheckUtil.java,v 1.0, 2008-04-03 15:52:28Z, Ambereen Drewitt$
 * $Date: 4/3/2008 9:52:28 AM$
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
package com.ballydev.sds.jackpotui.util;
import java.util.Collection;
import java.util.Map;

/**
 * This class is an utility clss to check whether the object is empty or not.
 * @author anantharajr
 * @version $Revision: 1$
 */
public class EmptyCheckUtil {
	
	
    /**
     * This method check the object as empty or not.
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof String) {
            String strObj = (String)object;
            if ("".equals(strObj.trim())) {
                return true;
            }
        } else if (object instanceof Collection) {
            Collection colObj = (Collection)object;
            if (colObj.isEmpty()) {
                return true;
            }
        } else if (object instanceof Map) {
            Map colObj = (Map)object;
            if (colObj.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
