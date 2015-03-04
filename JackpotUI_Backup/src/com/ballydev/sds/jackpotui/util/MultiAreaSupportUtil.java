/*****************************************************************************
 * $Id: MultiAreaSupportUtil.java,v 1.3, 2009-03-21 10:02:36Z, Ambereen Drewitt$
 * $Date: 3/21/2009 4:02:36 AM$
 *****************************************************************************
 *           Copyright  f-(c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.jackpotui.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.dto.AreaDTO;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.framework.preference.SDSPreferenceStore;
import com.ballydev.sds.jackpotui.constants.IAppConstants;
import com.ballydev.sds.jackpotui.constants.ISiteConfigConstants;

/**
 * Multi Area Support utility class
 * @author dambereen
 * @version $Revision: 4$
 */
public class MultiAreaSupportUtil {

	/**
	 * Logger Instance
	 */
	private static final Logger log = JackpotUILogger
			.getLogger(IAppConstants.MODULE_NAME);

	
	/**
	 * Method to check if the "Multi Area Support For Routing / Slip Commands" is enabled or not
	 * @return boolean
	 */
	public static boolean isMultiAreaSupportEnabled(){
		boolean multiAreaEnabled=false;
		try{
			SessionUtility sessionUtility = new SessionUtility();
			String boolValueGot = sessionUtility.getSiteConfigurationValue(ISiteConfigConstants.MULTI_AREA_SUPPORT_FOR_SLIP_COMMANDS);
			log.debug("MultiArea param value: "+boolValueGot);
			if(boolValueGot!=null){
				if(boolValueGot.trim().equalsIgnoreCase("yes")){
					multiAreaEnabled=true;
				}else if(boolValueGot.trim().equalsIgnoreCase("no")){
					multiAreaEnabled=false;
				}
			}
		}catch (Exception e) {
			log.error("Exception in MultiAreaSupportUtil isMultiAreaSupportEnabled",e);
		}
		return multiAreaEnabled;
	}
	
	/**
	 * Method to get the Area Details from Framework - Site Config 
	 * @return List<String>
	 */
	public static List<String> getAreaDetails(){
		List<String> lstAreasGot=null;
		try{
			log.debug("new SessionUtility().getLoggedInUserID() in getAreaDetails: "+new SessionUtility().getLoggedInUserID());
			if(new SessionUtility().getLoggedInUserID()!=null){
				
				log.info("Inside to get the area details");
				SessionUtility sessionUtility=new SessionUtility();
				SiteDTO siteDTO=sessionUtility.getSiteDetails();
				if(siteDTO!=null){
					Integer siteIdGot=siteDTO.getId();
					if(siteIdGot!=null && siteIdGot.intValue()!=0){					
						List<AreaDTO> lstAreaDTOGot=siteDTO.getListAreaDTO();
						if(lstAreaDTOGot!=null && lstAreaDTOGot.size()!=0){
							log.debug("lstAreaDTOGot.size(): "+lstAreaDTOGot.size());
							if(lstAreasGot==null){
								lstAreasGot=new ArrayList<String>();
							}
							for(int i=0;i<lstAreaDTOGot.size();i++){
								AreaDTO areaDTOGot=lstAreaDTOGot.get(i);
								if(areaDTOGot!=null){
									String areaNameToAdd=areaDTOGot.getAreaName();
									if(areaNameToAdd!=null && !((areaNameToAdd.trim()+" ").equalsIgnoreCase(" ")) ){
										lstAreasGot.add(areaNameToAdd);
									}
								}
							}							
						}					
					}
				}
			}			
		}catch (Exception e) {
			log.error("Exception in MultiAreaSupportUtil getAreaDetails",e);
		}
		return lstAreasGot;
	}
	
	/**
	 * Method to get the  
	 * @param areaNames
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getAreaIdInfo(ArrayList<String> areaNames){
		ArrayList<String> lstRtnAreaIds = new ArrayList<String>();
		try{
			log.debug("new SessionUtility().getLoggedInUserID() in getAreaDetails: "+new SessionUtility().getLoggedInUserID());
			if(new SessionUtility().getLoggedInUserID()!=null){
				
				log.debug("Inside to get the area details");
				SessionUtility sessionUtility=new SessionUtility();
				SiteDTO siteDTO=sessionUtility.getSiteDetails();
				log.debug("siteDTO: "+siteDTO);
				if(siteDTO!=null){
					Integer siteIdGot=siteDTO.getId();
					if(siteIdGot!=null && siteIdGot.intValue()!=0){					
						List<AreaDTO> lstAreaDTOGot = siteDTO.getListAreaDTO();
						if(lstAreaDTOGot!=null && lstAreaDTOGot.size()!=0 && areaNames!=null && areaNames.size()!=0){
							log.info("lstAreaDTOGot.size(): "+lstAreaDTOGot.size());
							for(int i=0; i<lstAreaDTOGot.size(); i++)
							{								
								for(int j=0; j<areaNames.size(); j++)
								{
									if(lstAreaDTOGot.get(i).getAreaName()!=null && lstAreaDTOGot.get(i).getAreaName().equalsIgnoreCase(areaNames.get(j))){
										lstRtnAreaIds.add(lstAreaDTOGot.get(i).getAreaId());
										log.info("Area Ids: "+lstAreaDTOGot.get(i).getAreaId());
									}
								}
								
							}							
						}					
					}
				}
			}			
		}catch (Exception e) {
			log.error("Exception in MultiAreaSupportUtil getAreaIdInfo",e);
		}
		return lstRtnAreaIds;
	}
	
	/**
	 * Method to get the Area Ids for the selected area names in the Jackpot Area Preferences
	 * @return
	 */
	public static ArrayList<String> getAreaIdsForPrefAreaNames()
	{
		ArrayList<String> prefStoreAreaIds = new ArrayList<String>();
		ArrayList<String> areaNames = new ArrayList<String>();
		String alreadySelectedAreas = SDSPreferenceStore.getStringStoreValue(IAppConstants.JACKPOT_AREA_PREFERENCE_KEY);
		if(alreadySelectedAreas!=null && !((alreadySelectedAreas.trim()+" ").equalsIgnoreCase(" ")) ){
			
			String[] selectedAreasGot = alreadySelectedAreas.split("@");
			for(int j=0;j<selectedAreasGot.length;j++){
				String areaFromStore = selectedAreasGot[j];
				if(areaFromStore!=null && !((areaFromStore.trim()+" ").equalsIgnoreCase(" ")) )
				{				
					areaNames.add(areaFromStore);
				}
			}										
		}else{
			log.info("In getAreaIdsForPrefAreaNames() 'alreadySelectedAreas' is null");
		}
		
		if(areaNames!=null && areaNames.size()!=0){
			prefStoreAreaIds = MultiAreaSupportUtil.getAreaIdInfo(areaNames);
		}		
		
		return prefStoreAreaIds;
	}
	
	/**
	 * Method to validate a slot / stand for multi area suppport
	 * @param areaShortName
	 * @param slotOrStandConst
	 * @return
	 */
	public static boolean validateSlotForSelectedArea(String areaShortName) 
	{
		log.info("inside validateSlotForSelectedArea");
		log.debug("Asset areaShortName : "+areaShortName);
		boolean areaCheck = false;
		if(MultiAreaSupportUtil.isMultiAreaSupportEnabled()) {
			ArrayList<String> areaLst = null;
			areaLst = MultiAreaSupportUtil.getAreaIdsForPrefAreaNames();
			
			/** MULTI AREA SUPPORT CHECK */
			if(areaLst!=null && areaLst.size()!=0){		
				System.out.println("");
				log.debug("Asset Short Name: "+areaShortName);
				if(areaLst.contains(areaShortName.trim())) {										
					areaCheck = true;
				}				
			}else {
				areaCheck = true;
			}
		}
		log.debug("areaCheck flag: "+areaCheck);
		return areaCheck;		
	}
	
	
}
