/*****************************************************************************
 * $Id: ReportsUtil.java,v 1.19, 2010-12-29 09:27:38Z, Thilagavathi, Veerasamy$
 *
 * Copyright Bally Gaming Inc. 2001-2004
 *
 * All programs and files on this medium are operated under U.S. patents
 * Nos. 5,429,361 & 5,470,079.
 *
 * All programs and files on this medium are copyrighted works and all
 * rights are reserved.
 * 
 * Duplication of these in whole or in part is prohibited without written
 * permission from Bally Gaming Inc., Las Vegas, Nevada, U.S.A.
 *
 *****************************************************************************/
package com.ballydev.sds.voucherui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.reportplugin.services.impl.UIService;
import com.ballydev.sds.reportplugin.util.ReportPluginUtil;
import com.ballydev.sds.reportsengine.dto.db.ParameterTO;
import com.ballydev.sds.reportsengine.report.IFormatType;
import com.ballydev.sds.reportsengine.report.model.impl.ControlValue;
import com.ballydev.sds.voucher.enumconstants.VoucherStatusEnum;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;


/**
 * This class communicates with the
 * reports plugin to fetch the report details 
 * @author Nithya kalyani R
 * @version $Revision: 20$ 
 */
public class ReportsUtil {

	/**
	 * Instance of Logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * This method gets the report for the
	 * user report id and the values passed
	 * @param userReportID
	 * @param formatType
	 * @param grpOrder
	 * @param sortOrder
	 * @param controlValues
	 * @return
	 */
	public String showReport(int userReportID, int formatType, String [] grpOrder, String [] sortOrder, List<ControlValue> controlValues) throws Exception{
		String requestOutput = null;
		try {

			String[] output = UIService.generateReport(userReportID, formatType, grpOrder, sortOrder, controlValues);			
			requestOutput = getFileInDirectory(output[0]);
			log.debug("Out put file: "+requestOutput);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return requestOutput;
	}

	/**
	 * This method sets the corresponding user entered
	 * control values in the control value object that is needed
	 * for the call of the reports
	 * @param userReportId
	 * @param userInput
	 * @param allSelected
	 * @return
	 */
	public String getParameterTO(int userReportId, HashMap<Object, Object> userInput,boolean allSelected){	
		List<ControlValue> controlValues = new ArrayList<ControlValue>();
		String reportsOutput = null;
		try {
			List<ParameterTO> parameterTOList = UIService.getUIControlsList(userReportId);
			int i=0;
			for(ParameterTO pto : parameterTOList){
				if( pto.getParamId() != 100 ) {
					log.debug(pto.getParamId()+" "+ pto.getControltype() +" "+ pto.getDatatype()+" "+pto.getParamname()+" "+pto.getParamtype()+ " Operator " + pto.getOperator());
					ControlValue controlValue = new ControlValue();
					//List<String> operatorList = getOperator(pto.getOperator());
					String operator = null;
					if( pto.getParamId() == (IVoucherConstants.EMP_ID_PARAM_ID) || pto.getParamId() == (IVoucherConstants.EMP_ID_PARAM_ID1)  ) {
						if( !allSelected ) {
							operator = IVoucherConstants.EQUAL_OPERATOR;	
							controlValue.setValue(operator+"'"+VoucherStringUtil.lPadWithZero(userInput.get(IVoucherConstants.EMP_ID_PARAM_ID).toString(),5)+"'");
							controlValue.setFormattedValue(operator+"'"+VoucherStringUtil.lPadWithZero(userInput.get(IVoucherConstants.EMP_ID_PARAM_ID).toString(),5)+"'");
						}else{
							operator=IVoucherConstants.ALL_CONTROL_VALUE;
							controlValue.setValue(operator);
							controlValue.setFormattedValue(operator);
						}					
					}else if(pto.getParamId()==(IVoucherConstants.SITE_ID_PARAM_ID)){
						controlValue.setValue(SDSApplication.getSiteDetails().getId().toString());
						controlValue.setFormattedValue(SDSApplication.getSiteDetails().getId().toString());
					}else if(pto.getParamId()==(IVoucherConstants.PROPERTY_ID_PARAM_ID)){
						controlValue.setValue(SDSApplication.getSiteDetails().getId().toString());
						controlValue.setFormattedValue(SDSApplication.getSiteDetails().getId().toString());
					}else if(pto.getParamId()==(IVoucherConstants.START_TIME_PARAM_ID)){
						String startFormattedDate = userInput.get(IVoucherConstants.START_TIME_PARAM_ID).toString();
						controlValue.setValue("'"+startFormattedDate+"'");
						controlValue.setFormattedValue("'"+startFormattedDate+"'");
					}else if(pto.getParamId()==(IVoucherConstants.END_TIME_PARAM_ID)){					
						String endFormattedDate = userInput.get(IVoucherConstants.END_TIME_PARAM_ID).toString();					
						controlValue.setValue("'"+endFormattedDate+"'");
						controlValue.setFormattedValue("'"+endFormattedDate+"'");
					}else if(pto.getParamId()==(IVoucherConstants.TKT_TYPE_PARAM_ID)){						
						String controlValueForTktStatus = getControlValueForTKtStatus();						 
						controlValue.setValue(controlValueForTktStatus);
						controlValue.setFormattedValue(getFormattedValue(controlValueForTktStatus));
					}else if(pto.getParamId()==(IVoucherConstants.COU_TYPE_PARAM_ID)){
						controlValue.setValue(userInput.get(IVoucherConstants.ALL_CONTROL_VALUE).toString());
						controlValue.setFormattedValue(getFormattedValue(userInput.get(IVoucherConstants.ALL_CONTROL_VALUE).toString()));
					}else if(pto.getParamId()==(IVoucherConstants.DETAILS_SUMM_PARAM_ID)){
						controlValue.setValue("'"+userInput.get(IVoucherConstants.DETAIL_VALUE)+"'");
						controlValue.setFormattedValue("'"+userInput.get(IVoucherConstants.DETAIL_VALUE)+"'");
					}else if(pto.getParamId()==(IVoucherConstants.DATA_SRC_PARAM_ID)){
						continue;
					}
					controlValue.setParamID(pto.getParamId());
					controlValue.setDatatype(pto.getDatatype());
					controlValue.setType(pto.getParamtype());
					//controlValue.setName(pto.getParamname());
					controlValue.setName(LabelLoader.getLabelValue(pto.getI18nKey()));
					controlValues.add(i,controlValue);
					log.info("Control values: "+controlValue.getParamID()+" "+ controlValue.getDatatype()+" "+ controlValue.getValue()+" "+controlValue.getName()+" "+controlValue.getType());
					i++;
				}	
			}
			reportsOutput = showReport(userReportId,  IFormatType.TXT, null, null, controlValues);
		} catch (Exception e) {			
			e.printStackTrace();
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_EXTPN_REPORTS_FETCH));
			return null;
		}catch (Throwable e) {			
			e.printStackTrace();
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_EXTPN_REPORTS_FETCH));
			return null;
		}
		return reportsOutput;
	}

	private String getFormattedValue(String string) {
		if(string == null){
			return null;
		}
		StringTokenizer st = new StringTokenizer(string , ",");
		StringBuilder formattedValue = new StringBuilder("");
		
		while(st.hasMoreTokens()){
			formattedValue.append("'" + ReportPluginUtil.getLabelValue(st.nextToken().replaceAll("'", "")) + "'" + "," );			
		}
		return formattedValue.substring(0,formattedValue.lastIndexOf(","));
	}

	public String getControlValueForTKtStatus() {
		String retValue = null;
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED))){
			if(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_CREATED)) {
				if(retValue==null) {
					retValue = "'"+VoucherStatusEnum.ACTIVE.getStatus()+"'";
				}else {
					retValue = retValue+"'"+VoucherStatusEnum.ACTIVE.getStatus()+"'";
				}
			}
		}
		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED))){
			if(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_REDEEMED)) {
				if(retValue==null) {
					retValue = "'"+VoucherStatusEnum.REDEEMED.getStatus()+"'";
				}else {
					if(retValue.trim().length()!=0) {
						retValue = retValue+",'"+VoucherStatusEnum.REDEEMED.getStatus()+"'";
					}					
				}				
			}
		}
		if(!Util.isEmpty(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED)))	{
			if(preferenceStore.getBoolean(IVoucherPreferenceConstants.REPORT_IS_VOIDED)) {
				if(retValue==null) {
					retValue = "'"+VoucherStatusEnum.VOIDED.getStatus()+"'";
				}else {
					if(retValue.trim().length()!=0) {
						retValue = retValue+",'"+VoucherStatusEnum.VOIDED.getStatus()+"'";
					}					
				}	
			}
		}
		return retValue;
	}

	/**
	 * This method gets the operator in
	 * the form of the list
	 * @param operatorList
	 * @return
	 */
	public List<String> getOperator(String operatorList){
		List<String> tokenList = new ArrayList<String>();
		if(operatorList!=null && operatorList.trim().length()>0){
			StringTokenizer strTok = new StringTokenizer(operatorList,",");				
			while(strTok.hasMoreTokens()){				
				tokenList.add(strTok.nextToken().trim());			
			}
		}
		log.debug("Operator list: "+tokenList);
		return tokenList;
	}	

	/**
	 * This method gets the output of the file that is downloaded
	 * in the form of the string inorder to
	 * display it in the reports text area
	 * @param dir
	 * @return
	 */
	private static String getFileInDirectory(String dir){		
		StringBuilder file = new StringBuilder();
		String line = "";
		try	{
			FileReader reader = new FileReader(new File(dir));
			BufferedReader bufReader = new BufferedReader(reader);		
			while ((line=bufReader.readLine()) != null) {				
				file.append(line);				
				file.append("\r\n");
			}					
		}
		catch(Exception ec)	{
			ec.printStackTrace();
		}
		return file.toString();
	}
}
