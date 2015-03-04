package com.ballydev.sds.voucherui.ares;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.ares.Ares;
import com.ballydev.sds.framework.ares.AresBarcodeEvent;
import com.ballydev.sds.framework.ares.AresErrorEvent;
import com.ballydev.sds.framework.ares.AresException;
import com.ballydev.sds.framework.ares.AresNoReadEvent;
import com.ballydev.sds.framework.ares.AresStatusEvent;
import com.ballydev.sds.framework.controller.SDSBaseController;
import com.ballydev.sds.framework.util.MessageDialogUtil;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.controller.ReconciliationController;
import com.ballydev.sds.voucherui.form.BatchReconciliationForm;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

public final class MultiScanListener extends BaseAresEventListener  {	
	
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);
	private SDSBaseController controller = null;
	private Ares ares = null;

	public MultiScanListener(Ares ares,SDSBaseController controller) {
		super();
		this.controller = controller;
		this.ares = ares;
	}
	

	@Override
	public void processAresBarcodeEvent(AresBarcodeEvent ae) {
		
			try {
				final String barcode = ae.getBarcode();
				if( barcode != null && barcode.length() == 18 ) {
						if(log.isDebugEnabled())
							log.debug("Barcode: " + barcode);
					
						ares.accept();
						((ReconciliationController)controller).addToQueue(barcode);
				}
				else {
					log.error("Invalid barcode length :"+barcode);
					((BatchReconciliationForm)controller.getSdsForm()).incrementNoRead();
					ares.reject();
				}
			} catch (Exception e) {
				log.error(e);
			}
		
	}


	@Override
	public void processAresStatusEvent(final AresStatusEvent ae) {
		
		try {
			switch(ae.getStatusType()) {
			
			case AresStatusEvent.MULTI_FEED_STOP:
				if(log.isDebugEnabled())
					log.debug("Multi Feed stop Event");
				((BatchReconciliationForm)controller.getSdsForm()).incrementNoRead();
				break;
				
			case AresStatusEvent.START_CHECK:
			  processStartCheck();
			  break;
			  
			case AresStatusEvent.STOP:
				((ReconciliationController)controller).addToQueue(IVoucherConstants.RECON_CTRL_BTN_SUBMIT);				
				break;
				
			default:
				MessageDialogUtil.displayTouchScreenErrorMsgDialog(ae.toString());
			
			}
		} catch (Exception e) {
			log.error(e);
		}
		
	}


	private void processStartCheck() throws Exception, AresException {
		
			controller.getComposite().getDisplay().syncExec(new Runnable() {
				public void run() {
					try {
						controller.populateForm(controller.getComposite());
					} catch (Exception e) {
						log.error(e);
					}
				}
			});
			
			final String employeeId = ((BatchReconciliationForm)controller.getSdsForm()).getEmployeeAssetId();
			log.info("START CHECK: scanLocation = " + employeeId);				
			
				if (employeeId != null && !employeeId.trim().equals("")) {					
					ares.startOk();
					
				} else {
					log.info("Start NOT ok, employeeAssetId validation failed");					
					ares.startNotOk();					
				}
	}


	@Override
	public void processAresErrorEvent(final AresErrorEvent ae) {
		MessageDialogUtil.displayTouchScreenErrorMsgDialog(ae.toString());
	}


	@Override
	public void processAresNoReadEvent(final AresNoReadEvent ae) {
		
		((BatchReconciliationForm)controller.getSdsForm()).incrementNoRead();
		
		 try {
			ares.reject();
		} catch (AresException e) {
			log.error(e);
		}
		
	}

	@Override
	public SDSBaseController getController() {
		
		return this.controller;
	}
	

}
