/*****************************************************************************
 * $Id: VoucherPluginDescriptor.java,v 1.54, 2010-10-15 10:39:55Z, Ravi, Dutt$
 * $Date: 10/15/2010 5:39:55 AM$
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

package com.ballydev.sds.voucherui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.employee.biz.dto.SessionInfoDTO;
import com.ballydev.sds.framework.ActionType;
import com.ballydev.sds.framework.ISelectionAction;
import com.ballydev.sds.framework.ITSPluginDescriptor;
import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.PluginDescriptor;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SDSTreeNode;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.ViewDescriptor;
import com.ballydev.sds.framework.action.SDSAction;
import com.ballydev.sds.framework.dto.FunctionDTO;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceGroup;
import com.ballydev.sds.framework.preference.TouchScreenPreferenceItem;
import com.ballydev.sds.framework.siteconfig.SiteConfigurationKeySet;
import com.ballydev.sds.framework.util.KeyBoardUtil;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucher.enumconstants.VoucherAssetTypeEnum;
import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;
import com.ballydev.sds.voucherui.composite.SessionTrackDialog;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IImageConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.controller.VoucherHomeController;
import com.ballydev.sds.voucherui.displays.Display;
import com.ballydev.sds.voucherui.displays.DisplayNoSuchDriverException;
import com.ballydev.sds.voucherui.exception.VoucherUIExceptionHandler;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.preference.BarcodeScannerPreference;
import com.ballydev.sds.voucherui.preference.CustomerDisplayPreference;
import com.ballydev.sds.voucherui.preference.LocationPreference;
import com.ballydev.sds.voucherui.preference.PrinterPreferenceItem;
import com.ballydev.sds.voucherui.preference.ReceiptPrinterPreference;
import com.ballydev.sds.voucherui.preference.ReportPreference;
import com.ballydev.sds.voucherui.preference.TicketPrinterPreference;
import com.ballydev.sds.voucherui.service.ServiceCall;
import com.ballydev.sds.voucherui.service.VoucherServiceLocator;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.VoucherImageRegistry;
import com.ballydev.sds.voucherui.util.VoucherStringUtil;
import com.ballydev.sds.voucherui.util.VoucherUtil;

/**
 * Plugin Descriptor class to create the Touch Screen action icons
 */
public class VoucherPluginDescriptor extends PluginDescriptor implements ITSPluginDescriptor {

	private VoucherHomeController voucherController;

	/**
	 * Instance of logger
	 */
	private static final Logger log = VoucherUILogger.getLogger(IDBLabelKeyConstants.MODULE_NAME);
	
	
	private SessionTrackDialog dialog = null; 
	
	/**
	 * This variable is to track whether the session mgmt is closed automatically
	 * If so then the validation msg should not be displayed
	 */
	private boolean isAutoSignOffCalled = false;

	@Override
	public SDSTreeNode createTree() {
		return null;
	}

	@Override
	public List<ViewDescriptor> createViewDescriptors() {
		List<ViewDescriptor> voucherViews = new ArrayList<ViewDescriptor>();
		return voucherViews;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createSignOutAction()
	 */

	public boolean exitCheck() {
		boolean validToNavigate = true;
		if(AppContextValues.getInstance().getCurrentScreen()!=null && AppContextValues.getInstance().getCurrentScreen().equalsIgnoreCase(IVoucherConstants.VOU_REDEEM_SCREEN)){
			if(voucherController.getHeaderController().getRedeemVoucherController()!=null){
				voucherController.getHeaderController().getRedeemVoucherController().navCheck(true,true);
				if(!voucherController.getHeaderController().getRedeemVoucherController().isScreenReadyToOpen()){			
					validToNavigate = false;
				}
			}
		}
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
			String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
			if(Util.isEmpty(dispType) && Util.isEmpty(dispDriver)){
				dispDriver=IVoucherConstants.CUST_DISP_PIONEER_DRIVER;
				dispType=IVoucherConstants.CUST_DISP_PIONEER_MANFT+" "+IVoucherConstants.CUST_DISP_PIONEER_MODEL;
			}
			Display display = null;
			Class.forName(dispDriver);
			display = Display.getDisplay(dispType);	
			display.blankDisplay();
			display.close();
		} catch (ClassNotFoundException e) {			
			log.error("Error occured in the exit check mthd: "+e);
			//validToNavigate = false;
		} catch (DisplayNoSuchDriverException e) {			
			log.error("Error occured in the exit check mthd: "+e);
			//validToNavigate = false;
		} catch (IOException e) {
			log.error("Error occured in the exit check mthd: "+e);
			//validToNavigate = false;
		}catch (Exception e) {
			log.error("Error occured in the exit check mthd: "+e);
			//validToNavigate = false;
		}
		return validToNavigate;
	}

	@Override
	public List<SDSAction> createToolBarActions() 
	{	
		return null;
	}


	public SDSAction getTouchScreenRoot() {
		SDSAction touchScreenRootAction = new SDSAction(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MODULE_NAME), ImageDescriptor.createFromFile(
				VoucherPluginDescriptor.class,
				IImageConstants.VOUCHER_UI_BUTTON_IMG), null);

		return touchScreenRootAction;
	}

	public TouchScreenPreferenceGroup createPreferencePages(List<FunctionDTO> functionsList) {
		TouchScreenPreferenceGroup voucherPreferenceGroup = new TouchScreenPreferenceGroup(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.LBL_MODULE_NAME));
		List<TouchScreenPreferenceItem> items = new ArrayList<TouchScreenPreferenceItem>(0);
		items.add(new LocationPreference());
		items.add(new BarcodeScannerPreference());
		items.add(new TicketPrinterPreference());
		items.add(new ReceiptPrinterPreference());
		items.add(new CustomerDisplayPreference());		
		items.add(new ReportPreference());
		//items.add(new MultiareaPreference());
		items.add(new PrinterPreferenceItem());
		voucherPreferenceGroup.setPreferencePageList(items);
		return voucherPreferenceGroup;
	}

	public Control createPluginControls(Composite parentHeaderComposite, Composite parentCenterComposite) {
		try {		
			new VoucherImageRegistry().loadImages();
			SessionUtility sessionUtility = new SessionUtility();
			String slotText = sessionUtility.getSiteConfigurationValue(IVoucherConstants.SLOT_TEXT);
			String ticketText = sessionUtility.getSiteConfigurationValue(IVoucherConstants.TICKET_TEXT);				
			if(slotText==null){
				slotText= IVoucherConstants.DEFAULT_SLOT_TEXT;
			}
			if( ticketText == null ) {
				ticketText= IVoucherConstants.DEFAULT_TICKET_TEXT;
			}
			AppContextValues.getInstance().setSlotText(VoucherStringUtil.initCap(slotText));
			AppContextValues.getInstance().setTicketText(VoucherStringUtil.initCap(ticketText));
			voucherController = new VoucherHomeController(parentHeaderComposite,parentCenterComposite,SWT.NONE);

		} catch (Exception e) {
			log.error("Error occured in the createPluginControls: "+e);
		}
		return voucherController.getComposite();
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createSiteConfigurationKeys()
	 */
	@Override
	public SiteConfigurationKeySet createSiteConfigurationKeys() {
		SiteConfigurationKeySet siteConfigurationKeySet = new SiteConfigurationKeySet();
		Set<String> categoryKeySet = new HashSet<String>();
		categoryKeySet.add("VOUCHER.PARAMETERS");
		Set<String> configurationKeySet = new HashSet<String>();
		configurationKeySet.add("NUMBER.DAYS.TO.EXPIRE");
		configurationKeySet.add("ALLOW.VOIDED.TICKETS.IN.STC");
		configurationKeySet.add("ALLOW.EXPIRED.TICKETS.IN.STC");
		configurationKeySet.add("ALLOW.PENDING.TICKETS.IN.STC");
		configurationKeySet.add("ALLOW.TICKETS.NOT.EFFFECTIVE");
		configurationKeySet.add("ALLOW.VOIDABLE.PROMO.TICKETS");
		configurationKeySet.add("ALLOW.EXPIRED.CASHABLE.PROMO");
		configurationKeySet.add("ALLOW.PENDING.CASHABLE.PROMO");
		configurationKeySet.add("ALLOW.CP.TKS.NOT.EFFECTIVE");
		configurationKeySet.add("MAX.TICKET.REDEMPTION.AMOUNT");
		configurationKeySet.add("VCH.TICKET.PRINT.ENABLED");
		configurationKeySet.add("VCH.RESTRICT.CASHRNG.STC");
		configurationKeySet.add("NUMBR.TKTS.TO.PRINTD");	
		siteConfigurationKeySet.setConfigurationKeySet(configurationKeySet);
		return siteConfigurationKeySet;
	}

	/**
	 * @return the voucherController
	 */
	public VoucherHomeController getVoucherController() {
		return voucherController;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#nullServiceObject()
	 */
	@Override
	public void nullServiceObject() {
		super.nullServiceObject();
		VoucherServiceLocator.makeServiceNull();		
	}

	/**
	 * This method checks whether the value in the preference page
	 * is a valid location or not
	 * @return
	 */
	public boolean isValidAsset() throws Exception{
		boolean isValidLocation = false;
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		String locationValueFromPreference = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
		if( locationValueFromPreference.trim().length() > 0 ) {
			try {					
				VoucherAssetTypeEnum assetTypeEnum = ServiceCall.getInstance().isAssetAvailable(locationValueFromPreference.trim(),(int)SDSApplication.getUserDetails().getSiteId());
				if( assetTypeEnum == null ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_INVALID_CASHIER_LOCATION));
				}
				if(assetTypeEnum.equals(VoucherAssetTypeEnum.CASHIER_WORK_STATION)){
					isValidLocation = true;
				}
			} catch(VoucherEngineServiceException ex)	{							
				String exceptionMsg = null;
				exceptionMsg = VoucherUtil.getExMessageForDisplay(ex);
				if(exceptionMsg!=null && exceptionMsg.trim().length()>0){
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(VoucherUtil.getExMessageForDisplay(ex));
				}else{
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VOU_ASSET_EXPTN));
				}
				throw ex;
			}
		}
		return isValidLocation;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.ITSPluginDescriptor#checkStateBeforeAction(com.ballydev.sds.framework.ActionType)
	 */
	public boolean checkStateBeforeAction(ActionType type) {
		boolean retVal = false;	
		retVal = exitCheck();
		if( type.equals(ActionType.PLUGIN_SELECT) ){
			
			if( !SiteUtil.isVoucherEnabled() ) {
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CHK_ENABLE));
				return false;
			}
			try {					
				IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
				String locationValueFromPreference = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
				
				if( locationValueFromPreference.trim().length() == 0 ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CASHIER_LOCATION_NOT_SET));
					return false;
				} 
				if( !isValidAsset() ){
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_INVALID_CASHIER_LOCATION));
					return false;
				}
				try {
					SessionUtility sessionUtility = new SessionUtility();
					SessionInfoDTO sessionInfoDTO = null;
					sessionInfoDTO = ServiceCall.getInstance().getSessionStatus(sessionUtility.getLoggedInUserID(), locationValueFromPreference, (int)sessionUtility.getUserDetails().getSiteId());
					if( !sessionInfoDTO.isSuccess()){
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(sessionInfoDTO.getErrorCodeDesc()) );
						return false;
					}
					
					if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_CREATE_SIGN_ON_SIGN_OFF ) ) {
						
						if( sessionInfoDTO.getSessionId() == null ) { 
							ServiceCall.getInstance().createSession(locationValueFromPreference);
							AppContextValues.getInstance().setVoucherOpen(true);
							
						} else if( sessionInfoDTO.getLocation() == null && (sessionInfoDTO.getSessStateId() == 1 || sessionInfoDTO.getSessStateId() == 6) && sessionInfoDTO.getSource().equals("USER.SESS.STATE.CASHIER") ) {
							
							ServiceCall.getInstance().signOnSession(locationValueFromPreference);
							AppContextValues.getInstance().setVoucherOpen(true);
							
						} else if( sessionInfoDTO.getSessStateId() == 3 && sessionInfoDTO.getSource().equals("USER.SESS.STATE.CASHIER") ) {
							
							int response =MessageDialogUtil.displayTouchScreenYesNoCancelDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CASHIER_SESSION_ALREADY_LOGGED_ON));				
							if( response == 2 ) {
								//cancel button- don't do anything so return false
								return false;
							} else if( response == 0 ) {
								try {
									ServiceCall.getInstance().createSessionWhenPaused(locationValueFromPreference);
								} catch (Exception ex){								
									log.error("Exception while creating the cashier workstation login : ",ex);
									VoucherUIExceptionHandler.handleException(ex);			
								}							
							} else if( response == 1 ) {
								//update the session status to 0
								try {
									ServiceCall.getInstance().updateSessionForceStatus(locationValueFromPreference,IVoucherConstants.SESSION_NOT_PAUSED);
								} catch(VoucherEngineServiceException ex)	{							
									log.error("VoucherEngineServiceException while updating the cashier workstation login", ex);				
									VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
								}
								catch (Exception ex){								
									log.error("Exception while updating the cashier workstation login : ",ex);
									VoucherUIExceptionHandler.handleException(ex);			
								}
							} else {
								return false;
							}
						}
						else {
							
							//String empId = ServiceCall.getInstance().getEmpIdForLockedWorkstation(locationValueFromPreference, (int)SDSApplication.getUserDetails().getSiteId());
							String empId = sessionInfoDTO.getEmpLoginId();
							if( empId == null ) {
								empId = sessionUtility.getLoggedInUserID();//LabelLoader.getLabelValue("VOU.LKP.AMT.UNKNOWN");
							}
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(
									IDBLabelKeyConstants.VOU_CASHIER_LOCATION_ALREADY_LOGGED_ON) + " " + empId + " " + LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CASHIER_LOC_ALREADY_LOGGED_ON_LAST) );
							return false;
						}
					} else if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_SIGN_ON_AND_SIGN_OFF) ) {
						
						if( sessionInfoDTO.getSessionId() == null ) { 
							MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CASHIER_LOC_NO_SESSION_FOR_LOGIN));
							return false;
						} else if( sessionInfoDTO.getLocation() == null && (sessionInfoDTO.getSessStateId() == 1 || sessionInfoDTO.getSessStateId() == 6)	&& sessionInfoDTO.getSource().equals("USER.SESS.STATE.CASHIER") ) {
							ServiceCall.getInstance().signOnSession(locationValueFromPreference);
							
						} else if(sessionInfoDTO.getSessStateId() == 3) {
							int response =MessageDialogUtil.displayTouchScreenYesNoCancelDialogOnActiveShell(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CASHIER_SESSION_ALREADY_LOGGED_ON));				
							if( response == 2 ) {
								//cancel button- don't do anything so return false
								return false;
							} else if( response == 0 ) {
								try {
									MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_PERMISSION_TO_CREATE_SESSION));
									return false;
								} catch (Exception ex){								
									log.error("Exception while creating the cashier workstation login : ",ex);
									VoucherUIExceptionHandler.handleException(ex);			
								}							
							} else if( response == 1 ) {
								//update the session status to 0
								try {
									ServiceCall.getInstance().updateSessionForceStatus(locationValueFromPreference,IVoucherConstants.SESSION_NOT_PAUSED);
									
								} catch(VoucherEngineServiceException ex)	{							
									log.error("VoucherEngineServiceException while updating the cashier workstation login", ex);				
									VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
								}
								catch (Exception ex){								
									log.error("Exception while updating the cashier workstation login : ",ex);
									VoucherUIExceptionHandler.handleException(ex);			
								}
							} else {
								return false;
							}
						}
						else {
							String empId = ServiceCall.getInstance().getEmpIdForLockedWorkstation(locationValueFromPreference, (int)SDSApplication.getUserDetails().getSiteId());
							if( empId == null ) {
								empId = sessionUtility.getLoggedInUserID();;//LabelLoader.getLabelValue("VOU.LKP.AMT.UNKNOWN");
							}
							MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(
									IDBLabelKeyConstants.VOU_CASHIER_LOCATION_ALREADY_LOGGED_ON) + " " + empId + " " + LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_CASHIER_LOC_ALREADY_LOGGED_ON_LAST) );
							return false;
						}
					} else {
						MessageDialogUtil.displayTouchScreenInfoDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_NO_PERMISSION_TO_CREATE_AND_SIGNON_SESSION));
						return false;
					}
					
				} catch(VoucherEngineServiceException ex) {
					log.error("VoucherEngineServiceException while checking for the cashier workstation login", ex);
					VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
					return false;
				}
				catch (Exception ex){
					log.error("Exception while checking for the cashier workstation login : ",ex);
					VoucherUIExceptionHandler.handleException(ex);
					return false;
				}				
			} catch (Exception e) {
				log.error("Error occured in the check state before action: "+e);
				return false;
			}
			return true;
		} 
		else if( type.equals(ActionType.PLUGIN_CLOSE) || type.equals(ActionType.EXIT) || type.equals(ActionType.SIGNOUT) ) {
			if( voucherController != null ) {
				/**CHECK THE RETVAL VALUE(GOT FROM THE EXITCHECK MTHD) IF IT RETURNS FALSE,
				 * TICKET IS IN PENDING STATE AND HENCE DON'T DO ANYTHING
				 * AND STAY IN THE CURRENT SCREEN*/
				if( retVal ) {
					KeyBoardUtil.closeKeyBoard();
					dialog = new SessionTrackDialog(voucherController.getMiddleComposite().getShell());
					dialog.setLocation(400,150);
					dialog.open(18,true);
					String response = dialog.getResponse();
					if( response == null && !isAutoSignOffCalled ) {
						MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_SELECT_ANY_OPTION));
						isAutoSignOffCalled =false;
						return false;
					}

					IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
					String locationValueFromPreference = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
					if( response!= null && response.equalsIgnoreCase(IVoucherConstants.VOU_EXIT_WITHOUT_CLOSING) ) {
						try {
							//update the session pause status to 1- to indicate the log out is done but session is not exited
							ServiceCall.getInstance().updateSessionForceStatus(locationValueFromPreference,IVoucherConstants.SESSION_PAUSED);
						} catch(VoucherEngineServiceException ex) {
							log.error("VoucherEngineServiceException while updating the cashier workstation login", ex);
							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
						} catch (Exception ex){
							log.error("Exception while updating the cashier workstation login : ",ex);
							VoucherUIExceptionHandler.handleException(ex);
						}
						setConfirmationRequired(false);
						AppContextValues.getInstance().setVoucherOpen(false);
					} else if( response!= null && response.equalsIgnoreCase(IVoucherConstants.VOU_EXIT_AFTER_CLOSING) ) {
						try {
							if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_CREATE_SIGN_ON_SIGN_OFF) ) {
								ServiceCall.getInstance().removeWorkStation(locationValueFromPreference);
							} else if( VoucherUtil.isValidUserForThisFucntion(IVoucherConstants.USER_FUNCTION_SIGN_ON_AND_SIGN_OFF) ) {
								ServiceCall.getInstance().signOffSession(locationValueFromPreference);
							}
						} catch(VoucherEngineServiceException ex) {
							log.error("VoucherEngineServiceException while checking for the cashier workstation login", ex);
							VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
						} catch (Exception ex) {
							log.error("Exception while checking for the cashier workstation login : ",ex);
							VoucherUIExceptionHandler.handleException(ex);
						}
						AppContextValues.getInstance().setVoucherOpen(false);
						setConfirmationRequired(false);
					} else if( response != null && response.equalsIgnoreCase(IVoucherConstants.VOU_SESS_CANCEL)) {
						return false;
					}
					//Erasing the message in customer display device 
					if( response != null && ! response.equalsIgnoreCase(IVoucherConstants.VOU_SESS_CANCEL) ) {
						eraseMessage();
					}
				}
			}
		}
		return retVal;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createAutoSignOffAction()
	 */
	@Override
	public ISelectionAction createAutoSignOffAction() {
		if( dialog != null ) {
			isAutoSignOffCalled = true;
			dialog.closeShell();
		}
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		String locationValueFromPreference = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
		try {
			//update the session pause status to 1- to indicate the log out is done but session is not exited
			ServiceCall.getInstance().updateSessionForceStatus(locationValueFromPreference,IVoucherConstants.SESSION_PAUSED);
		} catch(VoucherEngineServiceException ex) {
			log.error("VoucherEngineServiceException while updating the cashier workstation login", ex);
			VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));
		}
		catch (Exception ex) {
			log.error("Exception while updating the cashier workstation login : ",ex);
			VoucherUIExceptionHandler.handleException(ex);
		}
		return super.createAutoSignOffAction();
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#getDependentModules()
	 */
	@Override
	public List<String> getDependentModules() {
		List<String> modulesList = new ArrayList<String>();
		modulesList.add(IVoucherConstants.REPORTS_MODULE_NAME);
		return modulesList;
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.framework.PluginDescriptor#createSignOutAction()
	 */
	@Override
	public ISelectionAction createSignOutAction() {
		/*if(!isRemoveWorkStationCalled) {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			String locationValueFromPreference = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
			try {
				ServiceCall.getInstance().removeWorkStation(locationValueFromPreference);
			}catch(VoucherEngineServiceException ex)	{							
				log.error("VoucherEngineServiceException while checking for the cashier workstation login", ex);				
				VoucherUIExceptionHandler.handleClientException(VoucherUtil.getExMessageForDisplay(ex));			
			}
			catch (Exception ex){
				log.error("Exception while checking for the cashier workstation login : ",ex);
				VoucherUIExceptionHandler.handleException(ex);			
			}
			AppContextValues.getInstance().setSessionStarted(false);
			AppContextValues.getInstance().setVoucherOpen(false);
		}*/
		return super.createSignOutAction();
	}
	
	/**
	 * Method to display the welcome message in the customer display. Used for erasing any previous message
	 */
	public void eraseMessage() {		
		Display display = null;
		try {
			IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
			boolean isCustomerDisplay = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_CUSTOMER_DISPLAY);
			if( isCustomerDisplay ) {
				String dispModel = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MODEL);
				String dispManufacturer = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_MANUFACTURER);
				String dispPort = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_PORT);
				String dispDriver = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_DRIVER);
				String dispType = preferenceStore.getString(IVoucherPreferenceConstants.CUSTOMER_DISPLAY_TYPE);
				if( Util.isEmpty(dispModel)|| Util.isEmpty(dispManufacturer)||Util.isEmpty(dispPort) ) {
					MessageDialogUtil.displayTouchScreenErrorMsgDialog(LabelLoader.getLabelValue(IDBLabelKeyConstants.VOU_DISP_PREF_VALUE_TO_SET));
					return;
				}
				log.debug("disp model: "+dispModel);
				log.debug("disp manuf: "+dispManufacturer);
				log.debug("disp port: "+dispPort);
				log.debug("disp driver: "+dispDriver);
				log.debug("disp type: "+dispType);			
				Class.forName(dispDriver);
				display = Display.getDisplay(dispType);
				if( display != null ){				
					display.close();
				}
				display.open(dispPort);
				display.blankDisplay();
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();	
			log.error("Error occured while displaying welcome message: ", e1);
		} catch (DisplayNoSuchDriverException e1) {			
			e1.printStackTrace();
			log.error("Error occured while displaying welcome message: ", e1);
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("Error occured while displaying welcome message: ", e1);																	
		}
	}
}
