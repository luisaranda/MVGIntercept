/*****************************************************************************
 * $Id: AEDEX.java,v 1.9, 2009-04-16 07:09:00Z, Nithyakalyani, Raman$
 * $Date: 4/16/2009 2:09:00 AM$
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

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.constants.IVoucherPreferenceConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * This class implements the Display interface for an AEDEX protocol
 *
 */
public class AEDEX extends Display {

	/** Name of the application locking the serial port. */
	private static final String APPNAME = "Pioneer STLH-RORJ";

	/** How long to wait before giving up on port opening. */
	private static final int WAITTIME = 2000;

	/** How many rows are in this display */
	private static final int ROWS = 2;

	/** How many columns are in this display */
	private static final int COLS = 20;

	/** Size of the largest scrolling message */
	private static final int MAX_SCROLL = 50;
	/**
	 *Logger Instance 
	 */

	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);


	/**
	 * When the class is loaded by the Java Virtual Machine, register
	 * this class with the Display class.
	 */
	static {
		Display.registerDisplay(APPNAME, new AEDEX().getClass());
	}

	/** Serial port that the display is connect to. */
	private SerialPort serialPort;

	/** Output stream to the display. */
	private OutputStream outputStream;

	/**
	 * Default Constructor.  Do nothing.
	 */
	public AEDEX() {
		;
	}

	/**
	 * Open the serial port.
	 *
	 * @param device name of the device to open
	 *
	 * @throws IOException if there is an error opening the serial port
	 */
	public void open(String device) throws IOException {
		try {
			/* Get the port identifier for the port. */
			CommPortIdentifier portId =
				CommPortIdentifier.getPortIdentifier(device);
			log.debug("Port ID: " + portId);

			serialPort = (SerialPort)portId.open(APPNAME,
					WAITTIME);

			serialPort.setSerialPortParams(9600,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			log.debug("serialPort: " + serialPort);

			outputStream = serialPort.getOutputStream();

			log.debug("outputStream: " + outputStream);

		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}catch(Error err) {
			throw new IOException();
		}
	}

	/**
	 * Close the serial port to the attached display.
	 *
	 * @throws IOException if there is an error on the serial port
	 */
	public void close() {
		try {

			outputStream.close();
			serialPort.close();
		} catch (Exception ignore) {

		}
	}

	/**
	 * Clear the display.
	 *
	 * @throws IOException if there is an error writing to the serial
	 *         port.
	 */
	public void blankDisplay() throws IOException {
		write("!#9 ");
	}


	/**
	 * Scroll a message across the first line in the display. The message
	 * will be truncated to the maximum size of the scroll message if the
	 * message is too long.
	 *
	 * @param msg the message to be scrolled on the first line
	 *
	 * @throws IOException if there is an error writing to the serial
	 *         port
	 */
	public void scrollMsg(String msg) throws IOException {
		if (msg.length() > MAX_SCROLL) {
			msg = msg.substring(0, MAX_SCROLL);
		}
		if(msg!=null) {
			write("!#4" + msg);
		}
	}

	/**
	 * Display a message on the given line on the display.  The message
	 * will be truncated to the maximum size of the display.
	 *
	 * @param line the line number that the msg to be displayed
	 * @param msg the message to be displayed
	 */
	public void displayMsg(int line, String msg) throws IOException,
	IllegalArgumentException {
		if (line < 1 || line > ROWS) {
			throw new IllegalArgumentException("Row " + line +
			" is invalid");
		}
		if (msg.length() >= COLS) {
			msg = msg.substring(0, COLS);
		}
		if(msg!=null) {
			write("!#" + line + msg);
		}
	}

	/**
	 * Display a message on the given line on the display.  The message
	 * will be truncated to the maximum size of the display.
	 *
	 * @param line the line number that the msg to be displayed
	 * @param msg the message to be displayed
	 */
	public void displayMsg(String msg) throws IOException,
	IllegalArgumentException {	
		String msgInLine1 = null;
		String msgInLine2 = null;
		if (msg.length() >= COLS) {			
			msgInLine1 = msg.substring(0, COLS);			
			msgInLine2 = msg.substring(COLS ,msg.length());			
		}else{
			msgInLine1 = msg;	
		}		
		if(msgInLine1!=null) {		
			write("!#" + 1 + msgInLine1);
		}if(msgInLine2!=null) {			
			write("!#" + 2 + msgInLine2);
		}
	}

	/**
	 * Return the number of columns for this display.
	 *
	 * @returns int number of columns
	 */
	public int getColumns() {
		return COLS;
	}

	/**
	 * Return the number of rows for this display.
	 *
	 * @returns int number of rows
	 */
	public int getRows() {
		return ROWS;
	}

	/**
	 * Override java.lang.Object.toString() for this class
	 *
	 * @return string describing this object
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("AEXDEX display: ");
		try {
			if (serialPort != null) {
				sb.append(serialPort.toString());
			} else {
				sb.append("not attached to serial port");
			}
		} catch (Exception ignore) {
			/* Do nothing */ ;
		}
		return sb.toString();
	}

	/**
	 * Write a buffer to the serial port.
	 *
	 * @param buffer the buffer to send
	 *
	 * @throws IOException if there is a problem writing to the serial
	 *         port.
	 */
	private void write(String buffer) throws IOException {
		//log.debug("Write Buffer: [" + buffer + "]");
		if(outputStream!=null){
			outputStream.write(buffer.getBytes());
			outputStream.write('\r');
			outputStream.write('\n');
		}
	}

	/* (non-Javadoc)
	 * @see com.ballydev.sds.voucherui.displays.Display#dispMsg(java.lang.String)
	 */
	@Override
	public void dispMsg(String msg) throws IOException {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();	
		boolean isCustomerDisplayCenter = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_CENTER);		
		if(isCustomerDisplayCenter) {
			displayMsg( msg);
			return;
		}
		boolean isCustomerDisplayScroll = preferenceStore.getBoolean(IVoucherPreferenceConstants.IS_DISPLAY_SCROLL);
		if(isCustomerDisplayScroll) {
			scrollMsg(msg);
			return;
		}

		displayMsg(msg);

	}


}
