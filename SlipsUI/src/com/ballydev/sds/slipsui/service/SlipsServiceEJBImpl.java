/**
 * 
 */
package com.ballydev.sds.slipsui.service; 

import javax.naming.CommunicationException;
import javax.naming.NameNotFoundException;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.constant.ICommonMessages;
import com.ballydev.sds.framework.service.SDSServiceLocator;
import com.ballydev.sds.framework.util.ProgressIndicatorUtil;
import com.ballydev.sds.slips.dto.SlipsAssetInfoDTO;
import com.ballydev.sds.slips.dto.SlipsAssetParamType;
import com.ballydev.sds.slips.dto.SlipsCashlessAccountDTO;
import com.ballydev.sds.slips.dto.SlipsDTO;
import com.ballydev.sds.slips.dto.SlipsReportDTO;
import com.ballydev.sds.slips.dto.SlipsXMLInfoDTO;
import com.ballydev.sds.slips.exception.SlipsEngineServiceException;
import com.ballydev.sds.slips.filter.SlipsFilter;
import com.ballydev.sds.slips.service.ISlipsEngine;
import com.ballydev.sds.slips.util.SlipsOperationType;
import com.ballydev.sds.slipsui.constants.IAppConstants;
import com.ballydev.sds.slipsui.util.SlipsExceptionUtil;

/**
 * @author gsrinivasulu
 *
 */
public class SlipsServiceEJBImpl implements ISlipsService {

	private ISlipsEngine slipsEngine;
	
	public SlipsServiceEJBImpl() throws SlipsEngineServiceException {
		initialize();
	}
	
	private void initialize() throws SlipsEngineServiceException {
		SDSServiceLocator<ISlipsEngine> serviceLocator = new SDSServiceLocator<ISlipsEngine>();
		try {
			slipsEngine = serviceLocator.fetchEJBService(IAppConstants.MODULE_NAME);
		}catch (NameNotFoundException e) {
			throw new SlipsEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
		}catch (CommunicationException e) {
			throw new SlipsEngineServiceException(LabelLoader.getLabelValue(ICommonMessages.COMMON_MSG_SERVICE_DOWN_ERROR));
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getReprintPriorSlipDetails(int)
	 */
	public Object getReprintPriorSlipDetails(int siteId)
			throws SlipsEngineServiceException {
		SlipsDTO slipsDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			slipsDTO=slipsEngine.getReprintPriorSlipDetails(siteId);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		} catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);		
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return slipsDTO;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getReprintSlipDetails(long, com.ballydev.sds.slips.filter.SlipsFilter)
	 */
	public SlipsDTO getReprintSlipDetails(long sequenceNumber,
			SlipsFilter filter) throws SlipsEngineServiceException {
		SlipsDTO slipsDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			slipsDTO=slipsEngine.getReprintSlipDetails(sequenceNumber, filter);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return slipsDTO;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getSlipStatus(long, com.ballydev.sds.slips.filter.SlipsFilter)
	 */
	public String getSlipStatus(long sequenceNumber, SlipsFilter filter)
			throws SlipsEngineServiceException {
		String status = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			status=slipsEngine.getSlipStatus(sequenceNumber, filter);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return status;
	}

	public short getSlipStatusId(long sequenceNumber, SlipsFilter filter) throws SlipsEngineServiceException {
		short statusId = 0;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			statusId=slipsEngine.getSlipStatusId(sequenceNumber, filter);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return statusId;
	}
	
	public SlipsDTO getSlipStatusId(long sequenceNumber, int siteId) throws SlipsEngineServiceException {
		SlipsDTO slipsDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			slipsDTO = slipsEngine.getSlipStatusId(sequenceNumber, siteId);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return slipsDTO;
	}
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getSlipsAssetInfo(java.lang.Object, com.ballydev.sds.slips.dto.SlipsAssetParamType)
	 */
	public SlipsAssetInfoDTO getSlipsAssetInfo(Object object,
			SlipsAssetParamType assetParamType, int siteId)
			throws SlipsEngineServiceException {
		SlipsAssetInfoDTO assetDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			assetDTO=slipsEngine.getSlipsAssetInfo(object, assetParamType, siteId);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return assetDTO;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getSlipsReportInfo(int, java.lang.Long, java.lang.Long)
	 */
	public SlipsReportDTO getSlipsReportInfo(int siteId, String fromDate,
			String toDate) throws SlipsEngineServiceException {
		SlipsReportDTO reportDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			reportDTO=slipsEngine.getSlipsReportInfo(siteId, fromDate,toDate);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return reportDTO;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getSlipsReportInfo(int, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public SlipsReportDTO getSlipsReportInfo(int siteId, String employeeId, String fromDate, String toDate) throws SlipsEngineServiceException {
		SlipsReportDTO reportDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			reportDTO=slipsEngine.getSlipsReportInfoByEmployee(siteId, employeeId, fromDate, toDate);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return reportDTO;
	}


	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getSlipsXMLInfo()
	 */
	public SlipsXMLInfoDTO getSlipsXMLInfo() throws SlipsEngineServiceException {
		SlipsXMLInfoDTO infoDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			infoDTO=slipsEngine.getSlipsXMLInfo();
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return infoDTO;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getVoidSlipDetails(long, com.ballydev.sds.slips.filter.SlipsFilter)
	 */
	public SlipsDTO getVoidSlipDetails(long sequenceNumber, SlipsFilter filter)
			throws SlipsEngineServiceException {
		SlipsDTO slipsDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			slipsDTO=slipsEngine.getVoidSlipDetails(sequenceNumber, filter);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return slipsDTO;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#postSlipReprint(long, java.lang.String, com.ballydev.sds.slips.filter.SlipsFilter, java.lang.String)
	 */
	public boolean postSlipReprint(long sequenceNumber, String printerUsed,
			SlipsFilter filter, String reprintEmployeeLoginId)
			throws SlipsEngineServiceException {
		boolean status = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			status=slipsEngine.postSlipReprint(sequenceNumber,printerUsed, filter,reprintEmployeeLoginId);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);		
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#postSlipVoidStatus(long, java.lang.String, com.ballydev.sds.slips.filter.SlipsFilter, java.lang.String)
	 */
	public boolean postSlipVoidStatus(long sequenceNumber, String printerUsed,
			SlipsFilter filter, String voidEmployeeLoginId)
			throws SlipsEngineServiceException {
		boolean status = false;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			status=slipsEngine.postSlipVoidStatus(sequenceNumber,printerUsed, filter,voidEmployeeLoginId);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);		
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#processBeefSlip(com.ballydev.sds.slips.dto.SlipsDTO, com.ballydev.sds.slips.filter.SlipsFilter)
	 */
	public SlipsDTO processBeefSlip(SlipsDTO slipsDTO, SlipsFilter filter)
			throws SlipsEngineServiceException {
		SlipsDTO slipDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			slipDTO=slipsEngine.processBeefSlip(slipsDTO, filter);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return slipDTO;
	}	
	
	/* (non-Javadoc)
	 * @see com.ballydev.sds.slipsui.service.ISlipsService#getSlipsDetails(int, long)
	 */
	public SlipsDTO getSlipsDetails(int siteId, long sequenceNumber)
			throws SlipsEngineServiceException {
		SlipsDTO slipDTO = null;
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			slipDTO=slipsEngine.getSlipsDetails(siteId, sequenceNumber);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return slipDTO;
	}

	/**
	 * Method to validate the Cashless Account No
	 * @param siteNo
	 * @param cashlessAccNo
	 * @throws SlipsEngineServiceException
	 * @author dambereen
	 */
	public SlipsCashlessAccountDTO validateCashlessAccountNo(int siteNo, String cashlessAccNo, SlipsOperationType slipsOperationType) throws SlipsEngineServiceException {
		ProgressIndicatorUtil.openInProgressWindow();
		try{
			return slipsEngine.validateCashlessAccountNo(siteNo, cashlessAccNo, slipsOperationType);
		}
		catch(SlipsEngineServiceException exception){
			throw(exception);
		}catch (Exception e) {	
			SlipsExceptionUtil.getGeneralException(e);			
		}
		finally{
			ProgressIndicatorUtil.closeInProgressWindow();
		}
		return null;
	}	
}
