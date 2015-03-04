/*****************************************************************************
 * $Id: BatchReconciliationForm.java,v 1.5.5.0, 2013-10-25 16:52:01Z, Sornam, Ramanathan$
 * $Date: 10/25/2013 11:52:01 AM$
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
import java.util.Date;
import java.util.List;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;

public class BatchReconciliationForm extends BaseReconciliationForm
{
	private String questionableDetailsList;

	private List<String> scannedTickets;

	private Long batchId;

	/**
	 * startDate
	 */
	private Date startTime;

	/**
	 * endDate 
	 */
	private Date endTime;

	private Boolean employeeAssetToggleBtn;

	ArrayList<BatchSummaryDTO> batchSummaries = new ArrayList<BatchSummaryDTO>();
	
	BatchSummaryDTO batchSummaryDTO = new BatchSummaryDTO();
	
	private int noRead = 0;
	
	/** String to hold the value of the xml file selected for upload. */
	private String txtUploadXmlFile;

	public String getTxtUploadXmlFile() {
		return txtUploadXmlFile;
	}

	/**
	 * Sets the string to hold the value of the xml file selected for upload.
	 *
	 * @param txtUploadXmlFile the txtUploadXmlFile to set
	 */
	public void setTxtUploadXmlFile(String txtUploadXmlFile) {	
		String oldValue = this.txtUploadXmlFile;
		String newValue = txtUploadXmlFile;	
		this.txtUploadXmlFile = txtUploadXmlFile;
		firePropertyChange("txtUploadXmlFile",oldValue,newValue);
	}

	public ArrayList<BatchSummaryDTO> getBatchSummaries() {	
		return batchSummaries;
	}

	public void setBatchSummaries(ArrayList<BatchSummaryDTO> batchSummaries) {
		this.batchSummaries = batchSummaries;
	}



	public List<String> getScannedTickets() {
		return scannedTickets;

	}
	
	public void removeScanBarcode(ArrayList<String> removeList){
		
		if( scannedTickets == null ){
			scannedTickets = new ArrayList<String>();
		}
		
		if(removeList != null){
			scannedTickets.removeAll(removeList);
		}
	}

	public void scanBarcode(){
		if( scannedTickets == null ){
			scannedTickets = new ArrayList<String>();
		}
		String barcode = getBarcode().trim();
		if(scannedTickets.contains(barcode) ){
			MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.BARCODE_ALREDY_EXISTS));
		}else{
			scannedTickets.add(getBarcode().trim());
			int notRead = 0;
			int accepted = 0;
			setDetails(barcode);
			setAccepted(getScannedTickets()==null?"0":getScannedTickets().size()+"");
			//setUncommitted(getScannedTickets()==null?"0":getScannedTickets().size()+"");
			if(getAccepted()!="")
				accepted = Integer.parseInt(getAccepted());
			if(getNotRead()!="")
				notRead = Integer.parseInt(getNotRead());
			setUncommitted(accepted+"");
			setTotal(accepted + notRead +"");
		}
	}

	public void resetScannedTickets(){
		if( scannedTickets != null ){
			scannedTickets.removeAll(scannedTickets);
		}
		setBarcode(null);
		setAccepted(null);
		setUncommitted(null);
		setTotal(null);
		noRead = 0;
		setNotRead(null);
	}

	public void setDetails(String barcode){
		StringBuffer buffer = new StringBuffer(getScannedDetailsList()==null?"":getScannedDetailsList());
		buffer.append(barcode);
		buffer.append("\n");
		setScannedDetailsList(buffer.toString());
	}

	public void setBarcode(String barcode){		
		super.setBarcode(barcode);		
	}

	public String getQuestionableDetailsList() {
		return questionableDetailsList;
	}

	public void setQuestionableDetailsList(String questionableDetailsList) {
		this.questionableDetailsList = questionableDetailsList;
	}


	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the employeeAssetToggleBtn
	 */
	public Boolean getEmployeeAssetToggleBtn() {
		return employeeAssetToggleBtn;
	}

	/**
	 * @param employeeAssetToggleBtn the employeeAssetToggleBtn to set
	 */
	public void setEmployeeAssetToggleBtn(Boolean employeeAssetToggleBtn) {
		this.employeeAssetToggleBtn = employeeAssetToggleBtn;
	}

	/**
	 * @return the batchSummaryDTP
	 */
	public BatchSummaryDTO getBatchSummaryDTO() {
		return batchSummaryDTO;
	}

	/**
	 * @param batchSummaryDTP the batchSummaryDTP to set
	 */
	public void setBatchSummaryDTO(BatchSummaryDTO batchSummaryDTO) {
		this.batchSummaryDTO = batchSummaryDTO;
	}

	public final void incrementNoRead() {
		++noRead;		
	}

	@Override
	public final String getNotRead() {
		if(noRead == 0)
			return "";
		else
			return noRead+"";
	}
	
	


}
