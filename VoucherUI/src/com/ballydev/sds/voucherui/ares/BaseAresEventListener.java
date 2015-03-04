package com.ballydev.sds.voucherui.ares;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.ares.AresBarcodeEvent;
import com.ballydev.sds.framework.ares.AresErrorEvent;
import com.ballydev.sds.framework.ares.AresEvent;
import com.ballydev.sds.framework.ares.AresEventListener;
import com.ballydev.sds.framework.ares.AresNoReadEvent;
import com.ballydev.sds.framework.ares.AresStatusEvent;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * Abstract class to implement the common functionality across all HS Scanners.
 * @author JMada
 *
 */
public abstract class BaseAresEventListener implements AresEventListener {

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	
	public void aresEvent(AresEvent ae)
	{
		if( getController().getComposite() != null && !(getController().getComposite().isDisposed())) {
			
			if(ae instanceof AresBarcodeEvent)
				processAresBarcodeEvent((AresBarcodeEvent)ae);
			
			else if(ae instanceof AresErrorEvent)
				processAresErrorEvent((AresErrorEvent)ae);
			
			else if(ae instanceof AresNoReadEvent)
				processAresNoReadEvent((AresNoReadEvent)ae);
			
			else if(ae instanceof AresStatusEvent)
				processAresStatusEvent((AresStatusEvent)ae);
		}
		else {
			log.info("Controller "+getController().getCurrentCompositeName()+" is disposed, skipping event "+ae.toString());
		}
		
	}
	
	public abstract void processAresBarcodeEvent(AresBarcodeEvent ae);
	
	public void processAresErrorEvent(AresErrorEvent ae){		
			log.error("Ares ErrorEvent "+ae.toString());
	}; 
	
	public void processAresNoReadEvent(AresNoReadEvent ae){};
	
	public abstract void processAresStatusEvent(AresStatusEvent ae);
	
	public abstract SDSBaseController getController();
}
