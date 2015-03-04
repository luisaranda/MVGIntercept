package com.ballydev.sds.voucherui.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.framework.util.Util;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * This is a class that returns certain information that 
 * will be stored as prefrences.
 * 
 * @author skarthik
 */
//NOTE:For now this returns dummy data. This Needs to be changed when the f/w is able to provide this info.
public class PreferencesUtil {

	private static Properties preferenceProperties;
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	static
	{
		try
		{
			preferenceProperties = new Properties();
			preferenceProperties.load(PreferencesUtil.class.getResourceAsStream("/config/DummyPreferences.properties"));
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static String getClientAssetNumber()
	{
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

		String retVal=null;
		try
		{
			if(!Util.isEmpty(preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID)))
			{
				retVal = preferenceStore.getString(IVoucherPreferenceConstants.LOCATION_ID);
			}
			else
			{
				log.error("Location not configured in preferences");
			}

		}
		catch(Exception e) {
			log.error("Location not configured in preferences");
		}
		return retVal;
	}

	public static String getTicketExpiryDate()
	{
		String retVal="";
		//TODO: replace this with a call to the f/w when it is ready
		retVal = (String) preferenceProperties.get("TICKET.EXPIRY.DATE");
		return retVal;
	}

	public static String getOfflineTicketDirectory()
	{
		return "/sds/ot";//TODO: Figure out from where to get this
	}
}
