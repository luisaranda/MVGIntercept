package com.ballydev.sds.jackpotui.util;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import com.ballydev.sds.framework.SDSComparator;
import com.ballydev.sds.jackpot.dto.JackpotDTO;
import com.ballydev.sds.jackpotui.constants.IAppConstants;

/**
 * Class to compare the values in the Pending Jackpots Display table
 * @author dambereen
 * @version $Revision: 2$
 */
public class DataFieldComparator extends SDSComparator{

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger.getLogger(IAppConstants.MODULE_NAME);
	
	/**
	 * Local DataFieldType instance
	 */
	private DataFieldType localDataFieldType;
	
	/**
	 * Class constructor
	 * @param dataFieldType
	 */
	public DataFieldComparator(DataFieldType dataFieldType){
		this.localDataFieldType = dataFieldType;
	}
		
	@Override
	public int compare(Object arg0, Object arg1) {	
		
		return compare(arg0, arg1, localDataFieldType);
	}
		
	/**
	 * Method to compare the two values
	 * @param obj1
	 * @param obj2
	 * @param fieldType
	 * @return
	 */
	public int compare(Object obj1, Object obj2, DataFieldType fieldType) {
		
		JackpotDTO jackpotDTO1 = (JackpotDTO)obj1;
		JackpotDTO jackpotDTO2 = (JackpotDTO)obj2;	
		
		int sortValue = 0;
		
		if(fieldType.equals(DataFieldType.DATE)){
			sortValue = 1;
			
			if(jackpotDTO1.getTransactionDate().getTime() > jackpotDTO2.getTransactionDate().getTime()){
				sortValue = 1;
			}
			else if(jackpotDTO1.getTransactionDate().getTime() < jackpotDTO2.getTransactionDate().getTime()){
				sortValue = -1;
			}
			else{
				sortValue = 0;
			}
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}
		else if(fieldType.equals(DataFieldType.SEQUENCE_NO)){
			if(jackpotDTO1.getSequenceNumber() > jackpotDTO2.getSequenceNumber()){
				sortValue = 1;
			}
			else if(jackpotDTO1.getSequenceNumber() < jackpotDTO2.getSequenceNumber()){
				sortValue = -1;
			}
			else{
				sortValue = 0;
			}
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}else if(fieldType.equals(DataFieldType.SLOT_NO)){
			/** COMPARES THE TWO STRINGS */
			String firstEmpName=jackpotDTO1.getAssetConfigNumber();
			
			String secondEmpName=jackpotDTO2.getAssetConfigNumber();
			int intComparedVal=firstEmpName.compareToIgnoreCase(secondEmpName) ;
			log.debug("intComparedVal: " +intComparedVal);
			/** CONDITION TO CHECK IF THE FIRST VALUE IS GREATER THAN THE NEXT*/
			if(intComparedVal < 0){
				sortValue = 1;			
			}
			/** CONDITION TO CHECK IF THE FIRST VALUE IS LESSER THAN THE NEXT*/
			else if(intComparedVal > 0){
				sortValue = -1;
			}
			/** RETURNS 0 IF BOTH THE STRINGS ARE EQUAL */
			else{
				sortValue = 0;
			}
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}else if(fieldType.equals(DataFieldType.STAND_NO)){
			String firstEmpName=jackpotDTO1.getAssetConfigLocation();
			
			String secondEmpName=jackpotDTO2.getAssetConfigLocation();	
			
			/** COMPARES THE TWO STRINGS */
			int intComparedVal=firstEmpName.compareToIgnoreCase(secondEmpName) ;
			log.debug("intComparedVal: " +intComparedVal);
			/** CONDITION TO CHECK IF THE FIRST VALUE IS GREATER THAN THE NEXT*/
			if(intComparedVal < 0){
				sortValue = 1;			
			}
			/** CONDITION TO CHECK IF THE FIRST VALUE IS LESSER THAN THE NEXT*/
			else if(intComparedVal > 0){
				sortValue = -1;
			}
			/** RETURNS 0 IF BOTH THE STRINGS ARE EQUAL */
			else{
				sortValue = 0;
			}
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}else if(fieldType.equals(DataFieldType.JACKPOT_AMOUNT)){
			if(jackpotDTO1.getOriginalAmount() > jackpotDTO2.getOriginalAmount()){
				sortValue = 1;
			}
			else if(jackpotDTO1.getOriginalAmount() < jackpotDTO2.getOriginalAmount()){
				sortValue = -1;
			}
			else{
				sortValue = 0;
			}
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}else if(fieldType.equals(DataFieldType.EMPLOYEE_NAME)){
			String firstEmpName=jackpotDTO1.getEmployeeName();
			
			String secondEmpName=jackpotDTO2.getEmployeeName();	
			
			/** COMPARES THE TWO STRINGS */
			int intComparedVal=firstEmpName.compareToIgnoreCase(secondEmpName) ;
			log.debug("intComparedVal: " +intComparedVal);
			/** CONDITION TO CHECK IF THE FIRST VALUE IS GREATER THAN THE NEXT*/
			if(intComparedVal < 0){
				sortValue = 1;			
			}
			/** CONDITION TO CHECK IF THE FIRST VALUE IS LESSER THAN THE NEXT*/
			else if(intComparedVal > 0){
				sortValue = -1;
			}
			/** RETURNS 0 IF BOTH THE STRINGS ARE EQUAL */
			else{
				sortValue = 0;
			}
			if(getDirection() == SWT.DOWN){
				sortValue = -1 * sortValue;
			}
		}
		return sortValue;
	}
}
