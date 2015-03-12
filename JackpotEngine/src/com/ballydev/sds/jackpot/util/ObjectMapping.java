/*****************************************************************************
 * $Id: ObjectMapping.java,v 1.0, 2008-04-02 13:30:02Z, Ambereen Drewitt$
 * $Date: 4/2/2008 7:30:02 AM$
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Object Mapper Class
 * @author anantharajr
 * @version $Revision: 1$
 */
public class ObjectMapping {

/**
 * This method is used to copy alike fields from hibernate POJO objects to DTO objects.
 * @param fromObject
 * @param toObject
 */
public static void copyAlikeFields( Object fromObject, Object toObject){
		
		Class fromObjectClass = fromObject.getClass();
		while(fromObjectClass!=Object.class){
		Field[] fieldArr = fromObjectClass.getDeclaredFields();
			for(int i=0;i<fieldArr.length;i++){
				String fieldName = fieldArr[i].getName();
				try {
					String getterName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
					Class[] classArr1 = {};
					try {
						Method m1 = fromObject.getClass().getMethod(getterName, classArr1);
						Object[] objectArr1 = {};
						Object value = m1.invoke(fromObject, objectArr1);
						String setterName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
						Method m2;
						try {
							m2 = toObject.getClass().getMethod(setterName, fieldArr[i].getType());
							Object[] objectArr2 = {value};
							m2.invoke(toObject, objectArr2);
						} catch (NoSuchMethodException e) {
							continue;
						}
					} catch (NoSuchMethodException e) {
						continue;
					}catch (Exception e) {
						continue;
					}
				} catch (Exception e) {
						continue;
				} 
			}
			fromObjectClass = fromObjectClass.getSuperclass();
		}
	}
}
