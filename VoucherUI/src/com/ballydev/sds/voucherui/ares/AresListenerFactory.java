package com.ballydev.sds.voucherui.ares;

import com.ballydev.sds.framework.ares.Ares;
import com.ballydev.sds.framework.ares.AresEventListener;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.readers.MetroLogicScan;

/**
 * AresListenerFactory is a Factory class, which can be used to get the appropriate Listener Instance
 * depending on the User Preferences for HS barcode Scanner. 
 * @author JMada
 *
 */
public final class AresListenerFactory {
	
	public static AresEventListener getAresEventListener(Ares ares,SDSBaseController controller)
	{
		/*Returning the instance of MultiScanListener for all HighSpeed Scanners. If need to customize we can return that particular
		instance.*/
		if(!(ares instanceof MetroLogicScan))
			return new MultiScanListener(ares,controller);
		else 
			return null;
		
	}

}
