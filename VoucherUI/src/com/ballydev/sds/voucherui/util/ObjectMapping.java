/*****************************************************************************
 * $Id: ObjectMapping.java,v 1.5, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:46:20 AM$
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ballydev.sds.voucher.dto.TicketInfoDTO;

/**
 * 
 * Class containing utility methods to map one object to another with same variable names,
 * getters, setters. 
 * 
 * @author Dinesh R
 * @version $Revision: 6$
 *
 */

public class ObjectMapping {

	public ObjectMapping(){

	}
	
	public static TicketInfoDTO copyTktInfoDTOForRedeem(TicketInfoDTO ticketInfoDTO) {
		TicketInfoDTO ticketInfoDTO2 = new TicketInfoDTO();
		ticketInfoDTO2.setEmployeeId(ticketInfoDTO.getEmployeeId());
		ticketInfoDTO2.setAmountType(ticketInfoDTO.getAmountType());
		ticketInfoDTO2.setStatus(ticketInfoDTO.getStatus());
		ticketInfoDTO2.setEffectiveDate(ticketInfoDTO.getEffectiveDate());
		ticketInfoDTO2.setExpireDate(ticketInfoDTO.getExpireDate());
		ticketInfoDTO2.setTransAssetNumber(ticketInfoDTO.getTransAssetNumber());
		ticketInfoDTO2.setCreatedEmpId(ticketInfoDTO.getCreatedEmpId());
		return ticketInfoDTO2;
	}

	/**
	 * Copies the values of member variables of one object to another. Only those variables which have 
	 * getters and setters in the both the classes with same names will be copied.
	 *  
	 * @param fromObject
	 * @param toObject
	 */
	@SuppressWarnings("unchecked")
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
							if(value != null && value instanceof java.util.Date) {
								m2 = toObject.getClass().getMethod(setterName, java.util.Date.class);
							}
							else {
								m2 = toObject.getClass().getMethod(setterName, fieldArr[i].getType());
							}
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

	/**
	 * Copies the list of objects passed, to a list of objects whose class Object is passed
	 * 
	 * @param fromList list of objects to be copied
	 * @param toObjectClass Class object whose objects are created 
	 *        and the values are copied from fromList objects 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static List copyArrayOfObjectstToList(List fromList, Class toObjectClass){
		List toList = null;
		if (fromList != null && fromList.size()>0){
			for(int i=0;i<fromList.size();i++){
				Object fromObject = fromList.get(i);
				if(fromObject != null){
					try{
						Constructor toObjectConstructor = toObjectClass.getConstructor((Class[])null);
						Object toObject = toObjectConstructor.newInstance((Object[])null);
						if (toList == null){
							toList = new ArrayList();
						}
						copyAlikeFields(fromObject, toObject);
						toList.add(toObject);
					}
					catch(NoSuchMethodException nsmex){
						return null;
					}
					catch(IllegalAccessException iaex){
						return null;
					}
					catch(InstantiationException iex){
						return null;
					}
					catch(InvocationTargetException iex){
						return null;
					}
					catch(Exception ex){
						continue;
					}
				}
			}
		}
		return toList;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List copyArrayOfObjectst(List fromList, Class toObjectClass){
		List toList = null;
		if (fromList != null && fromList.size()>0){
			for(int i=0;i<fromList.size();i++){
				Object fromObject = fromList.get(i);
				if(fromObject != null){
					try{
						Constructor toObjectConstructor = toObjectClass.getConstructor((Class[])null);
						Object toObject = toObjectConstructor.newInstance((Object[])null);
						if (toList == null){
							toList = new ArrayList();
						}
						copyAlikeFields(fromObject, toObject);
						toList.add(toObject);
					}
					catch(NoSuchMethodException nsmex){
						return null;
					}
					catch(IllegalAccessException iaex){
						return null;
					}
					catch(InstantiationException iex){
						return null;
					}
					catch(InvocationTargetException iex){
						return null;
					}
					catch(Exception ex){
						continue;
					}
				}
			}
		}
		return toList;
	}
}
