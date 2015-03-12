/***************************************************************************************************************
 *  $Id :  $
 * $Date : $
 * $Log$
 ***************************************************************************************************************
 *           Copyright (c) 2003 Bally Gaming Inc.  1977 - 2003
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 ****************************************************************************************************************/
package com.ballydev.sds.jackpot.constants;
/**
 * 
 * 
 * @author Govindharaju Mohanasundaram email:GMohanasundaram@ballytech.com
 * 
 * @version $Revision: 7$
 */
public interface IS2SConstants {

	String JPT_INFO_REQ = "jackpotInfoReq";

	String ACTIVE_JPT_REQ = "activeJackpotReq";

	String VOID_JPT_REQ = "voidJackpotReq";

	String REDEEM_JPT_REQ = "redeemJackpotReq";
	
	String REQ_PROCESSOR_PKG="com.ballydev.sds.jackpot.s2sreqprocessor.";
	
	String RES_BUILDER_PKG="com.ballydev.sds.jackpot.s2sresbuilder.";
	
	String SITE_ID_PROPERTY="SITE_ID";
	
	String FILTER_SLOT_NUMBER="getJackpotDetailsForSlotNumber";
	
	int SITE_ID=1;
	
	int ASSET_INVALID_SLOT_ERROR_CODE = 1;
	
	String S2S_PROPS_FILE_PATH="/sds/ApplicationResources.properties";
	
	String S2S_SITE_ID="SITE.ID";
	
	String JACKPOT_ENGINE="JackpotEngine";
	
	String JACKPOT_FACADE="JackpotFacade";
	
	String JACKPOT_BO="JackpotBO";
	
	String ASSET_BO="AssetBO";
	
	String FRAMEWORK_FACADE="FrameworkFacade";
	
	String SDS_EAR="SDS";
	
	String EJB="local";

	
	 String SERVER_WEBSERVICE_COMMUNICATION = "server.webservice";
	
	 String SERVER_ARCHIVE_NAME = "server.archive.ear";
	
	 String SERVER = "server";
	
	 String JAVA_NAMING_FACTORY_INITAL = "java.naming.factory.initial";
	
	 String JAVA_NAMING_FACTORY_URL_PACKAGES = "java.naming.factory.url.pkgs";
	
	 String JAVA_NAMING_PROVIDER_URL = "java.naming.provider.url";
	 
	 String S2S_KIOSK="S2S Kiosk";
	
	
	

	
	

}
