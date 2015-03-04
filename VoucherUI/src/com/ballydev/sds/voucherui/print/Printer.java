/*
 * $Id: Printer.java,v 1.8, 2011-03-21 11:29:10Z, SDS Check in user$
 *
 * Copyright Bally Gaming Inc. 2001-2004
 *
 * All programs and files on this medium are operated under U.S. patents
 * Nos. 5,429,361 & 5,470,079.
 *
 * All programs and files on this medium are copyrighted works and all
 * rights are reserved.
 * 
 * Duplication of these in whole or in part is prohibited without written
 * permission from Bally Gaming Inc., Las Vegas, Nevada, U.S.A.
 *
 */
package com.ballydev.sds.voucherui.print;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.util.TicketDetails;


/**
 * This class is the interface to the printer attached to the STC application.
 *
 * @author $Author: SDS Check in user$
 * @version $Revision: 9$
 */
@SuppressWarnings("unchecked")
public abstract class Printer {
	/** List of driver classes that implement the Printer interface. */
	private static Hashtable drivers;

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);


	static {
		try {
			drivers = new Hashtable();
		} catch (Exception ignore) {
			log.error("Error loading default driver");
		}
	}

	abstract public void printTicket(String casinoName, String address, TicketDetails td, String printerType) throws IOException, PrinterException;

	abstract public void printVoidTicket(boolean isSmallSize) throws IOException, PrinterException;

	abstract public void printVoidTicket() throws IOException, PrinterException;

	/**
	 * Determine if the printer is ready to print.  This method is driver
	 * specific.
	 *
	 * @return <code>true</code> if this printer is ready to print;
	 *         <code>false</code> otherwise.
	 *
	 * @throws PrinterException if there is an error reading and writing to the
	 *         serial port.
	 */
	public abstract boolean isReady() throws PrinterException;

	public abstract boolean isVoucherLow();

	public abstract boolean isVoucherEmpty();

	public abstract boolean isVoucherJam();

	public abstract boolean isPrinterOpen();

	public abstract String getStatus() throws IOException;
	
	public abstract String modelName(String modelName) throws IOException;

	public abstract Boolean tktSmallSize(boolean isSmallSize) throws IOException;

	/**
	 * Determine if the paper in the printer is at the top of form.  This
	 * method is driver specific.
	 */
	public abstract boolean isTopOfForm() throws PrinterException;

	/**
	 * Form feed the printer.  This method is driver specific.
	 */
	public abstract void formFeed() throws PrinterException;

	/**
	 * Add the <code>PrinterEventListener</code> to the list of objects that
	 * are interested in receiving printer events from this printer.
	 *
	 * @param l the Print Event Listener interested in receiving printer events
	 *        from this printer
	 *
	 * @see PrinterEventListener
	 */
	public abstract void addPrinterEventListener(PrinterEventListener l);

	/**
	 * Close the printer.
	 * @throws PrinterException if there is an error closing the serial port
	 */
	public abstract void close() throws PrinterException;

	public abstract void reset() throws PrinterException;
	
	public abstract void notify(PrinterEvent pe);

	public abstract void open(String device) throws PrinterException;

	public abstract void printVoidReceipt() throws PrinterException;

	public abstract void printReceipt(TicketDetails td, String employee, String printType) throws PrinterException;

	public abstract void printCustomerReceipt(TicketDetails td, String employee) throws PrinterException;

	public abstract void printReceipt(List<TicketDetails> tdList, String employee, String printType) throws PrinterException;

	public abstract boolean removePrinterEventListener(PrinterEventListener l);

	private static Hashtable printers = new Hashtable();

	public static Printer getPrinter(String manufacturer, String model)
	throws PrinterNoSuchDriverException {
		String key = manufacturer + "." + model;

		if (!drivers.containsKey(key)) {
			throw new PrinterNoSuchDriverException(key);
		}

		Class obj = (Class)drivers.get(key);

		try {
			if (!printers.containsKey(key)) {
				Object printer = obj.newInstance();
				printers.put(key, printer);
				log.debug("After putting the key to the printer obj");
			}

			return (Printer)printers.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PrinterNoSuchDriverException(key);
		}
	}

	protected static void registerPrinter(
			String manufacturer, String model, Class printer) {
		try {
			String key = manufacturer + "." + model;
			log.debug("Register: " + key);
			log.debug("Printer: " + printer);

			if (!drivers.containsKey(key)) {
				drivers.put(key, printer);
				log.debug("After putting the key to the printer obj");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
