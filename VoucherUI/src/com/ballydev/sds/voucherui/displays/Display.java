/*****************************************************************************
 * $Id: Display.java,v 1.3, 2010-01-12 08:43:47Z, Lokesh, Krishna Sinha$
 * $Date: 1/12/2010 2:43:47 AM$
 *****************************************************************************
 *           Copyright (c) 2007 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.displays;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * This class is the interface to the two-line display attached to the STC
 * application.
 *
 */
@SuppressWarnings("unchecked")
public abstract class Display {

	/** List of driver classes that implement the Display interface. */
	private static Hashtable drivers;

	/** List of protocols that are supported by this interface. */
	private static Hashtable protocols;

	/**
	 *Logger Instance 
	 */

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);



	static {
		try {
			drivers = new Hashtable();
			protocols = new Hashtable();
		} catch (Exception ignore) {
			log.debug("Error loading default driver");
		}
	}

	/**
	 * Clear the display.  This method is driver specific.
	 *
	 * @throws IOException if there is an error writing to the serial
	 *         port
	 */
	abstract public void blankDisplay() throws IOException;

	/**
	 * Scroll a message across the first line in the display. The size
	 * of the message is driver specific. The message will be truncated
	 * to the maximum size of the scroll message if the message is too
	 * long.
	 *
	 * @param msg the message to be scrolled on the first line
	 *
	 * @throws IOException if there is an error writing to the serial
	 *         port
	 */
	abstract public void scrollMsg(String msg) throws IOException;

	/**
	 * Display a message on a line of the display.  The size
	 * of the message is driver specific. The message will be truncated
	 * to the maximum size of the display if the message is too long.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	abstract public void dispMsg(String msg) throws IOException;

	/**
	 * Display a message on a line of the display.  The size
	 * of the message is driver specific. The message will be truncated
	 * to the maximum size of the display if the message is too long.
	 *
	 * @param line which line to display the text on
	 * @param msg the message to be shown on the display
	 *
	 * @throws IOException if there is an error writing to the serial
	 *         port
	 * @throws IllegalArgumentException if the line number is greater
	 *         than the physical number of lines in the display
	 */
	abstract public void displayMsg(int line, String msg) throws
	IOException, IllegalArgumentException;

	/**
	 * Get the number of columns in the display.  The number of columns
	 * is driver specific.
	 *
	 * @return the number of columns in the display
	 */
	abstract public int getColumns();

	/**
	 * Get the number of rows in the display. The number of rows is
	 * driver specific.
	 *
	 * @return the number of rows in the displa
	 */
	abstract public int getRows();

	/**
	 * Open the serial port
	 *
	 * @param device name of the device to open
	 *
	 * @throws IOException if there is an error opening the serial port.
	 */
	abstract public void open(String device) throws IOException;

	/**
	 * Close the serial port
	 * 
	 * @throws IOException if there is an error opening the serial port.
	 */
	abstract public void close();

	/**
	 * Return a <code>Display</code> object that implements this
	 * protocol.
	 *
	 * @param protocol which protocol we are implementing
	 *
	 * @return a <code>Display</code> object that implement the protocol
	 *           requested.
	 *
	 * @throws DisplayNoSuchDriverException
	 */
	public static Display getDisplay(String protocol) throws
	DisplayNoSuchDriverException {
		if (!drivers.containsKey(protocol)) {
			throw new DisplayNoSuchDriverException(protocol);
		}

		Class obj = (Class)drivers.get(protocol);

		try {
			if (!protocols.containsKey(protocol)) {
				Object display = obj.newInstance();
				protocols.put(protocol, display);
			}
			return (Display)protocols.get(protocol);
		} catch (Exception e) {
			throw new DisplayNoSuchDriverException(protocol);
		}
	}

	/**
	 * Register a new Display driver.
	 *
	 * @param protocol the protocol the new display implements
	 * @param display the new driver class to associated with the protocol
	 */
	protected static void registerDisplay(String protocol, Class display) {
		if (!drivers.containsKey(protocol)) {
			drivers.put(protocol, display);
			log.debug("Register: " + protocol);
			log.debug("Display : " + display);
		}
	}
}
