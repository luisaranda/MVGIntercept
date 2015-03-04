/**
 * 
 */
package com.ballydev.sds.voucherui.service;

import com.ballydev.sds.voucher.exception.VoucherEngineServiceException;


/**
 * @author Nithya kalyani R
 * @version $Revision: 4$ 
 */
public class VoucherServiceLocator {

	private static VoucherServiceLocator voucherServiceLocator;

	private static IVoucherService voucherService;

	private VoucherServiceLocator() {

	}

	/**
	 * This method gets the service reference
	 * @return
	 * @throws VoucherEngineServiceException
	 */
	public static IVoucherService getService() throws VoucherEngineServiceException {
		try {
			if(voucherServiceLocator == null) {
				voucherServiceLocator = new VoucherServiceLocator();				
				voucherService = new VoucherEJBService();
			}			
		} catch (Exception e) {
			throw new VoucherEngineServiceException(e);
		}
		return voucherService;
	}
	
	/**
	 * This method makes the service to null
	 */
	public static void makeServiceNull(){
		voucherService = null;
		voucherServiceLocator = null;
	}
}
