package com.ballydev.sds.slipsui.service; 

import com.ballydev.sds.slips.dto.SlipsAssetInfoDTO;
import com.ballydev.sds.slips.dto.SlipsAssetParamType;
import com.ballydev.sds.slips.dto.SlipsCashlessAccountDTO;
import com.ballydev.sds.slips.dto.SlipsDTO;
import com.ballydev.sds.slips.dto.SlipsReportDTO;
import com.ballydev.sds.slips.dto.SlipsXMLInfoDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slips.filter.SlipsFilter;
import com.ballydev.sds.slips.util.SlipsOperationType;

public interface ISlipsService {

	/**
	 * Method to update the slip details for A BEEF SLIP based on the FieldType (Slot/Stand) 
	 * that is set in the SlipsFilter 
	 * @param slipsDTO
	 * @param filter
	 * @return
	 * @throws SlipsEngineServiceException_Exception
	 */
	public abstract SlipsDTO processBeefSlip(SlipsDTO slipsDTO,
			SlipsFilter filter) throws SlipsEngineServiceException;

	public abstract SlipsXMLInfoDTO getSlipsXMLInfo()
			throws SlipsEngineServiceException;

	/**
	 * A method to get the slip status based on the slip type set in the filter and the sequence no
	 * @param sequenceNumber
	 * @return statusFlagDesc
	 * @throws SlipsEngineServiceException_Exception
	 */
	public abstract String getSlipStatus(long sequenceNumber, SlipsFilter filter)
			throws SlipsEngineServiceException;

	public abstract short getSlipStatusId(long sequenceNumber, SlipsFilter filter)throws SlipsEngineServiceException ;
	
	/**
	 * Method to get the status id and cashless acc no for the Dispute Slip Seq and Site ID
	 * @param sequenceNumber
	 * @param siteId
	 * @return
	 * @throws SlipsEngineServiceException
	 * @author dambereen
	 */
	public SlipsDTO getSlipStatusId(long sequenceNumber, int siteId)throws SlipsEngineServiceException;
	
	
	/**
	 * A method to update the status of a slip based on the slip type and its sequence
	 * @param sequenceNumber
	 * @return True/False
	 * @throws SlipsEngineServiceException_Exception
	 */
	public abstract boolean postSlipVoidStatus(long sequenceNumber,
			String printerUsed, SlipsFilter filter, String voidEmployeeLoginId)
			throws SlipsEngineServiceException;

	/**
	 * A method to log the transaction for reprint slip.
	 * @param sequenceNumber
	 * @return True/False
	 * @throws SlipsEngineServiceException_Exception
	 */
	public abstract boolean postSlipReprint(long sequenceNumber,
			String printerUsed, SlipsFilter filter,
			String reprintEmployeeLoginId)
			throws SlipsEngineServiceException;

	/**
	 * Method to get the reprint slip details based on the slip type id set in the SlipsFilter
	 * @param sequenceNumber
	 * @param filter
	 * @return
	 * @throws SlipsEngineServiceException_Exception
	 */
	public abstract SlipsDTO getReprintSlipDetails(long sequenceNumber,
			SlipsFilter filter) throws SlipsEngineServiceException;

	/**
	 * Method to get the void slip details based on the slip type id set in the SlipsFilter
	 * @param sequenceNumber
	 * @param filter
	 * @return
	 * @throws SlipsEngineServiceException_Exception
	 */
	public abstract SlipsDTO getVoidSlipDetails(long sequenceNumber,
			SlipsFilter filter) throws SlipsEngineServiceException;

	/**
	 * * Remote method that returns the asset details based on the type of Object and AssetParamType 
	 * (ASSET_CONFIG_LOCATION, ASSET_CONFIG_NUMBER or ASSET_CONFIG_ID) thats passed
	 * @param object
	 * @param assetParamType
	 * @param siteId
	 * @return
	 * @throws SlipsEngineServiceException
	 */
	public abstract SlipsAssetInfoDTO getSlipsAssetInfo(Object object,
			SlipsAssetParamType assetParamType, int siteId)
			throws SlipsEngineServiceException;


	public abstract SlipsReportDTO getSlipsReportInfo(int siteId, String fromDate, String toDate)	throws SlipsEngineServiceException;
	
	public abstract SlipsReportDTO getSlipsReportInfo(int siteId, String employeeId, String fromDate, String toDate)throws SlipsEngineServiceException;
	

	/**
	 * Method to get the details to reprint the PRIOR (P) slip (the latest processed slip)
	 * @param siteId
	 * @return 
	 * @throws JackpotEngineServiceException_Exception
	 */
	public abstract Object getReprintPriorSlipDetails(int siteId)
			throws SlipsEngineServiceException;
	
	/**
	 * getSlipDetails - Method to get slip details based on siteId and sequenceNumber
	 * @param siteId
	 * @param sequenceNumber
	 * @return
	 * @throws SlipsEngineServiceException
	 */
	public SlipsDTO getSlipsDetails(int siteId, long sequenceNumber) throws SlipsEngineServiceException;

	/**
	 * Method to validate the Cashless Account No
	 * @param siteNo
	 * @param cashlessAccNo
	 * @throws SlipsEngineServiceException
	 */
	public SlipsCashlessAccountDTO validateCashlessAccountNo(int siteNo, String cashlessAccNo, SlipsOperationType slipsOperationType) throws SlipsEngineServiceException;
	
}