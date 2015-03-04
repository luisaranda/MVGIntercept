/**
 * $Id: Ithaca.java,v 1.35, 2011-03-21 11:29:10Z, SDS Check in user$
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

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ballydev.sds.framework.LabelLoader;
import com.ballydev.sds.framework.SDSApplication;
import com.ballydev.sds.framework.SessionUtility;
import com.ballydev.sds.framework.dto.SiteDTO;
import com.ballydev.sds.voucherui.constants.IDBLabelKeyConstants;
import com.ballydev.sds.voucherui.constants.IDBPreferenceLabelConstants;
import com.ballydev.sds.voucherui.constants.IPrintTicketConstants;
import com.ballydev.sds.voucherui.constants.ITicketConstants;
import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;
import com.ballydev.sds.voucherui.util.DateUtil;
import com.ballydev.sds.voucherui.util.SiteUtil;
import com.ballydev.sds.voucherui.util.Text;
import com.ballydev.sds.voucherui.util.TicketDetails;

/**
 * Document ME!
 *
 * @author $Author: SDS Check in user$
 * @version $Revision: 36$
 */
public class Ithaca extends Printer {
	/** Name of the application locking the serial port. */
	private static final String APPNAME = "Ithaca";

	/** How long to wait before giving up on port opening. */
	private static final int WAITTIME = 2000;

	/** How many dots are on a page in landscape. */
	private static final int DOTS_PER_TKT = 1238;

	/** Print in the smallest font. */
	private static final int SMALL = 12;

	/** Print in the default font. */
	private static final int DEFAULT = 16;

	/** Print in the medium font. */
	private static final int MEDIUM = 20;

	/** Print in the largest font. */
	private static final int LARGE = 28;

	/** Is the printer ready? */
	@SuppressWarnings("unused")
	private static final int PRINTER_READY_950 = 0x0E;
	
	/** Is the ticket in the printer? */
	private static final int TICKET_IN_PRINTER_950 = 0x04;

	/** Is the printer ticket stock low? */
	private static final int TICKET_LOW_950 = 0x04;
	
	/** Is the paper jammed? */
	private static final int PAPER_JAM_950 = 0x7f;

	/** Is the printer's head up? */
	@SuppressWarnings("unused")
	private static final int HEAD_IS_UP_950 = 0x40;

	/** Is the printer ready? */
	private static final int PRINTER_READY = 0x100;
	
	/** Is the ticket at the top of form? */
	private static final int TOP_OF_FORM = 0x200;

	/** Is the printer's head up? */
	private static final int HEAD_IS_UP = 0x800;

	/** Is the printer open? */
	private static final int PRINTER_OPEN = 0x1000;

	/** Is the printer out of tickets? */
	private static final int OUT_OF_TICKET = 0x2000;

	/** Is the printer ticket stock low? */
	private static final int TICKET_LOW = 0x1;

	/** Is the ticket in the printer? */
	private static final int TICKET_IN_PRINTER = 0x2;

	/** Is the barcode complete? */
	private static final int BARCODE_COMPLETE = 0x10;

	/** Is the barcode complete? */
	private static final int BARCODE_COMPLETE_950 = 0x200;

	/** Is the paper jammed? */
	private static final int PAPER_JAM = 0x80;

	private String casinoName;
	
	private String address;

	/** Input stream to the printer */
	private InputStream inputStream;

	/** Output stream to the printer */
	private OutputStream outputStream;

	/** Serial port that the printer is connected to. */
	private SerialPort serialPort;

//	private boolean threadStop = false;

//	private boolean wait = false;

	private int printerStatus = 0;

	/** Logger Instance */
	private static final Logger log = VoucherUILogger.getLogger(ITicketConstants.MODULE_NAME);
	
	private String mdlName = "950";
	
	private boolean isSmallTkt = false;
	
	/**
	 * When the class is loaded by the Java Virtual Machine, register this
	 * class with the Printer class.
	 */
	static {
		Printer.registerPrinter(IVoucherConstants.TKTSCANNER_ITHACA_MANFT, IVoucherConstants.TKTSCANNER_ITHACA_MODEL, new Ithaca().getClass());		
		Printer.registerPrinter(IVoucherConstants.TKTSCANNER_ITHACA_750_MANFT, IVoucherConstants.TKTSCANNER_ITHACA_750_MODEL, new Ithaca().getClass());		
		Printer.registerPrinter(IVoucherConstants.TKTSCANNER_ITHACA_950_MANFT, IVoucherConstants.TKTSCANNER_ITHACA_950_MODEL, new Ithaca().getClass());		
		Printer.registerPrinter(IVoucherConstants.TKTSCANNER_NANOPTIX_MANFT, IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL, new Ithaca().getClass());
	}
	
	/** PrinterListeners listening to this object. */
	private Vector listeners;
	
	/** Constructor */
	public Ithaca() {
		listeners     = new Vector();
	}

	public String modelName( String modelName ) {
		mdlName = modelName;
		return mdlName;
	}

	public Boolean tktSmallSize(boolean isSmallSize) throws IOException {
		isSmallTkt = isSmallSize;
		return isSmallSize;
	}

	public boolean isReady() throws PrinterException {
		printerStatus = 0;
		try {
			print("is Ready? Called");
			printerStatus = getPrinterStatus();
			print("is Ready?: " + printerStatus);
		} catch (Exception e) {
			print("Is Ready? Error: " + e.getMessage());
			throw new PrinterException(e.getMessage());
		}

		return (printerStatus & PRINTER_READY) == PRINTER_READY;
	}

	public boolean isVoucherLow() {
		return (printerStatus & TICKET_LOW) == TICKET_LOW;
	}

	public boolean isVoucherEmpty() {
		return (printerStatus & OUT_OF_TICKET) == OUT_OF_TICKET;
	}

	public boolean isVoucherJam() {
		return (printerStatus & PAPER_JAM) == PAPER_JAM;
	}

	public boolean isPrinterOpen() {
		return (printerStatus & PRINTER_OPEN) == PRINTER_OPEN;
	}

	public boolean isTopOfForm() throws PrinterException {
		int status = 0;

		try {
			status = getPrinterStatus();
		} catch (Exception e) {
			print("Is Top Of Form? Error: " + e.getMessage());
			throw new PrinterException(e.getMessage());
		}

		return (status & TOP_OF_FORM) == TOP_OF_FORM;
	}

	public void close() {
		if(serialPort != null) {
			serialPort.close();
		}
	}

	public void addPrinterEventListener(PrinterEventListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void notify(PrinterEvent pe) {
		for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
			((PrinterEventListener)e.nextElement()).printerEvent(pe);
		}
	}

	public void open(String device) {
		try {
			close();
			/* Get the port identifier for the port */
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(device);

			serialPort = (SerialPort)portId.open(APPNAME, WAITTIME);

			serialPort.setSerialPortParams( 9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			serialPort.setFlowControlMode( SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);

			serialPort.enableReceiveTimeout(250);

			if (serialPort.isReceiveTimeoutEnabled()) {
				print("Receiver Time Out Enabled");
			}

			inputStream      = serialPort.getInputStream();
			outputStream     = serialPort.getOutputStream();

			if (!echoData()) {
				print("Printer not attached");
			} else {
				print("Firmware Rev: " + getFirmwareVersion());
				System.out.println("Firmware Rev: " + getFirmwareVersion());
			}
		} catch (Exception e) {
			print(e.getMessage());
		} catch(Error err) {
			print(err.getMessage());			
		}
		print("Ithaca Open Complete");
	}

	public synchronized void printVoidReceipt( ) throws PrinterException {
		getCasinoDetails();
		String barcode = "000000000000000000";
		try {
//			wait = true;
			reset();			
			truncate();		
			defaultFont();		
			landscape();
			printText("\n\n\nVOID "+LabelLoader.getLabelValue(IDBPreferenceLabelConstants.CASHIER_RECEIPT));
			printText("---------------");
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.VALIDATION_NO));
			printText(Text.expandBarcode(barcode));
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.MAC_NO)+ barcode.substring(4, 9));
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.OPERATOR_NO) +" 0000");
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.ISSUE_DATE));
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PAID_DATE) );
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.PAID_TIME) );
			printText(LabelLoader.getLabelValue(IDBPreferenceLabelConstants.TKT_AMOUNT) + LabelLoader.getLabelValue(IDBPreferenceLabelConstants.INVALID_TKT_AMT));

			formFeed();

			try {
				Thread.sleep(2000);
			} catch (Exception ignore) {
			}

//			wait = false;
		} catch (Exception error) {
			throw new PrinterException(error.getMessage());
		}
	}


	public synchronized void printReceipt( TicketDetails td, String employee, String printerModel) throws PrinterException {

 		getCasinoDetails();
		try {    		
			printTicket(casinoName,address,td, printerModel);
		} catch (Exception e) {			
			if(!e.getLocalizedMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT)){
				throw new PrinterException(e.getLocalizedMessage());
			}
		}
	}

	public synchronized void printReceipt( List<TicketDetails> tdList, String employee, String printerModel)
	throws PrinterException {
		getCasinoDetails();
		for(int i=0;i<tdList.size();i++){
			TicketDetails details =tdList.get(i); 
			try {				
				printTicket(casinoName,address,details ,printerModel);
			} catch (Exception e) {				
				throw new PrinterException(e.getLocalizedMessage());
			}
		}
	}

	public int getTicketAmount(double doubleValue) {
		int lVal = (int) (doubleValue * 1000);
		if(lVal % 10 > 5) lVal++;
		lVal = lVal/10;    
		return lVal;
	}

	public long calcTicketAmount(double doubleValue) {
		long lVal = (long) (doubleValue * 1000);
		if(lVal % 10 > 5) lVal++;
		lVal = lVal/10;    
		return lVal;
	}

	public void printText(String buffer) throws IOException {
		write(buffer.getBytes());
		outputStream.write('\n');
	}

	public void printText(String buffer, int size, boolean high)
	throws IOException {
		int horizontal = 0;
		if( isSmallTkt ) {
			horizontal = ((DOTS_PER_TKT - ((buffer.length()) * size)) / 2) - 295;
		}
		else {
			horizontal = ((DOTS_PER_TKT - ((buffer.length()) * size)) / 2) - 128;
		}

		if (horizontal < 0) {
			horizontal = 0;
		}
		setHorizontal(horizontal);

		switch (size) {
		case SMALL:
			smallFont();

			break;

		case DEFAULT:
			defaultFont();

			break;

		case MEDIUM:
			mediumFont();

			break;

		case LARGE:
			largeFont();

			break;
		}

		if (high) {
			setDoubleHigh();
		}

		write(buffer.getBytes());
		outputStream.write('\n');

		if (high) {
			cancelDoubleHigh();
		}
	}
	public void getCasinoDetails(){
//		String city;
//		String country;
		
		SiteDTO  siteDTO = SDSApplication.getSiteDetails();
		if( siteDTO != null ) {
			casinoName	= siteDTO.getLongName();
			address 	= siteDTO.getAddress1();			
//			city 		= siteDTO.getCity();
//			country 	= siteDTO.getCountry();
		}else{
			casinoName 	= IPrintTicketConstants.CASINO_NAME;
			address 	= IPrintTicketConstants.ADDRESS;
//			city 		= IPrintTicketConstants.CITY;
//			country		= IPrintTicketConstants.COUNTRY;
		}
	}

	public void printText(String buffer, int size) throws IOException {
		printText(buffer, size, false);
	}

	public synchronized void printVoidTicket( boolean isSmallSize ) throws 
	IOException, PrinterException {
		getCasinoDetails();
		/*macro(1); 
		macro(2, "VOID VOUCHER ");
		//macro(30);
		macro(31);
		macro(32);
		macro(33);
		//macro(34);
		//macro(35);
		macro(36);
		try {
			macro(3, "Test Voucher");
			macro(14, "Test Voucher");
			macro(255, casinoName);
		} catch (Exception ignore) {}
		//macro(17, Version.getCurrentVersion());
		macro(40, "Bally Technologies");
		macro(12, DateUtil.toString(Calendar.getInstance()));*/

		reset();
		truncate();
		if( isSmallSize ) {
			smallTicket();
		}
		defaultFont();	
		landscape();
		printText("VOID VOUCHER ",LARGE);
		try {
			printText("Test Voucher",MEDIUM);
			printText(casinoName,MEDIUM);
		} catch (Exception ignore) {}

		printText("Bally Technologies",MEDIUM);
		printBarcode("123412345123451234", "");
		printText("1234-12345-12345-1234", SMALL);
		printText( DateUtil.toString(Calendar.getInstance()),SMALL);
		formFeed();
		try {
			Thread.sleep(4000);
		}
		catch (Exception ignore) {}
	}

	public synchronized void printVoidTicket() throws IOException, PrinterException {
		getCasinoDetails();
		reset();
		truncate();
		defaultFont();	
		landscape();
		printText("",LARGE);
		printText("",LARGE);
		printText("",LARGE);
		printText("VOID VOUCHER ",LARGE);
		try {
			printText("Test Voucher",MEDIUM);
			printText(casinoName,MEDIUM);
		} catch (Exception ignore) {}

		printText("Bally Technologies",MEDIUM);
		printBarcode("123412345123451234", IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL);
		printText("",MEDIUM);
		printText("1234-12345-12345-1234", SMALL);
		printText( DateUtil.toString(Calendar.getInstance()),SMALL);
		formFeed();
		try {
			Thread.sleep(4000);
		}
		catch (Exception ignore) {}
	}

	/*	private int queryMacro(int number) throws PrinterException {
		int rv = 0;
		byte[] buffer = new byte[3];
		buffer[0] = 0x1d;
		buffer[1] = 0x51;
		buffer[2] = (byte) number;
		try {
			write(buffer);
			buffer = new byte[2];
			read(buffer);
			rv = buffer[0] << 8;
			rv |= buffer[1]; 
		} 
		catch (Exception e) {
			throw new PrinterException(e.getMessage());
		}
		return rv;
	}
	private void defineMacro() throws PrinterException {
		byte[] buffer = new byte[29];
		buffer[0] = 0x1d;
		buffer[1] = 0x4d;
		buffer[2] = (byte) 0xff;
		buffer[3] = 0x1b;
		buffer[4] = 0x74;
		buffer[5] = 0x01;
		buffer[6] = 0x1b;
		buffer[7] = 0x21;
		buffer[8] = 0x02;
		buffer[9] = 0x1d;
		buffer[10] = 0x21;	
		buffer[11] = 0x00;
		buffer[12] = 0x1b;
		buffer[13] = 0x47;
		buffer[14] = 0x01;
		buffer[15] = 0x1d;
		buffer[16] = 0x24;
		buffer[17] = 0x00;
		buffer[18] = 0x55;
		buffer[19] = 0x1d;
		buffer[20] = 0x46;
		buffer[21] = 0x01;
		buffer[22] = 0x00;
		buffer[23] = 0x00;
		buffer[24] = 0x03;
		buffer[25] = (byte) 0xc0;
		buffer[26] = 0x1d;
		buffer[27] = 0x4d;
		buffer[28] = (byte) 0xff;

		try {
			write(buffer);
		}
		catch (Exception e) {
			print("DefineMacro: " + e.getMessage());
			throw new PrinterException(e.getMessage());
		}
	}

	private void macro(int number) throws PrinterException {
		byte[] buffer = new byte[3];
		buffer[0] = 0x1d;
		buffer[1] = 0x4f;
		buffer[2] = (byte) number; 
		try {
			write(buffer);
		} catch (IOException e) {
			print("Form Feed Error: " + e.getMessage());
			throw new PrinterException(e.getMessage());
		}
	}

	private void macro(int number, String arg) throws PrinterException {
		byte[] buffer = new byte[3 + arg.length() + 1];
		buffer[0] = 0x1d;
		buffer[1] = 0x4f;
		buffer[2] = (byte) number; 
		System.arraycopy(arg.getBytes(), 0, buffer, 3, arg.length());
		buffer[arg.length() + 3] = 0x0d;
		try {
			write(buffer);
		} catch (IOException e) {
			print("Form Feed Error: " + e.getMessage());
			throw new PrinterException(e.getMessage());
		}
	}
*/
	public synchronized void printTicket(String casinoName, String address, TicketDetails td, String printerModel) throws IOException, PrinterException {
		getCasinoDetails();
		print("printTicket: started");
		reset();
		print("printTicket: after reset");
		truncate();
		if( isSmallTkt ) {
			smallTicket();
		}
		print("printTicket: after truncate");
		defaultFont();
		printText("    " + Text.expandBarcode(td.getBarcode()));
		landscape();
		if( isSmallTkt ) {
			printText(IPrintTicketConstants.TICKET_TYPE, LARGE);
		} else {
			if(printerModel.equals(IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL)) {
				printText("", LARGE);
				printText("", LARGE);
				printText("", LARGE);
			}
			printText(IPrintTicketConstants.TICKET_TYPE, LARGE, true);
		}
		printText(casinoName, MEDIUM);
		printText(address, SMALL);

		printBarcode(td.getBarcode(), printerModel);
		String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
		
		if( stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) ) {
			printText(IPrintTicketConstants.VALIDATION_TXT + td.getBarcode(), SMALL);
		} else {
			printText(IPrintTicketConstants.VALIDATION_TXT + Text.expandBarcode(td.getBarcode()), SMALL);
		}

		printText(
				IPrintTicketConstants.DATE_TXT + Text.formatTimestampWithSec(td.getEffective()) + 
				IPrintTicketConstants.VOUCHER_TXT + td.getBarcode().substring(9, 14), SMALL);
		if(printerModel.equals(IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL))
			printText("", MEDIUM);
		try {        	
			printText(IPrintTicketConstants.AMT_TXT+Text.convertDollarForTicket((long)(td.getAmount())), LARGE);
		} catch (Exception e) {
			print("printTicket: Exception: " + e.getMessage());
			throw new IOException(e.getMessage());
		}
		printText(Text.convertAmount((long)(td.getAmount())), SMALL);
		
		if( stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) ) {
			printText(
				IPrintTicketConstants.EXPIRE_TXT + Text.formatTimestamp(td.getExpire()) + cashierWorkStation(td) +
				IPrintTicketConstants.EMP_TXT + td.getBarcode().substring(2, 6), SMALL);
		} else {
			printText(
				IPrintTicketConstants.EXPIRE_TXT + Text.formatTimestamp(td.getExpire()) + cashierWorkStation(td) +
				IPrintTicketConstants.EMP_TXT + td.getBarcode().substring(5, 9), SMALL);
		}
		formFeed();
	
		try {
			if( (isSmallTkt && mdlName.equals("750")) || mdlName.equals("950") ) {
				Thread.sleep(5000);
			} else {
				Thread.sleep(2500);
			}
		} catch (Exception ignore) {}
		
		int status = getPrinterStatus();
		
		if( !mdlName.equals("950") && (status & BARCODE_COMPLETE) != BARCODE_COMPLETE ){
			throw new PrinterException(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_CONNECTION_ERROR)+ "\n"+ getStatus());
		} else if( mdlName.equals("950") && (status & BARCODE_COMPLETE_950) != BARCODE_COMPLETE_950 ) {
			throw new PrinterException(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_CONNECTION_ERROR)+ "\n"+ getStatus());
		} 

		print("printTicket: complete");
	}

	/**
	 * This API will return the cashier location to be printed on the ticket, for Washington requirement.
	 * @param tktDetail
	 * @return cashierWS
	 */
	private String cashierWorkStation(TicketDetails tktDetail) {
		String cashierWS = "";
		if ( SiteUtil.isWashingtonTktCriteriaEnabled() ) {
			cashierWS = tktDetail.getLocation();
		}
		return cashierWS;
	}

	public boolean removePrinterEventListener(PrinterEventListener l) {
		return listeners.remove(l);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		try {         
			sb.append(getStatus());            
		} catch (Exception e) {
			sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_CONNECTION_ERROR));
		}

		return sb.toString();
	}

	private void setBarcodeWidth(int width) throws IOException {
		byte[] buffer = new byte[3];
		buffer[0]     = 0x1d;
		buffer[1]     = 0x77;
		buffer[2]     = (byte)width;
		write(buffer);
	}

	private void setDoubleHigh() throws IOException {
		byte[] buffer = new byte[2];
		buffer[0]     = 0x1d;
		buffer[1]     = 0x12;
		write(buffer);
	}

/*	private void setDoubleWide() throws IOException {
		byte[] buffer = new byte[1];
		buffer[0] = 0x0e;
		write(buffer);
	}
*/
	private String getFirmwareVersion() throws IOException {
		StringBuffer sb = new StringBuffer();
		byte[] buffer   = new byte[2];
		buffer[0]       = 0x1b;
		buffer[1]       = 0x56;
		write(buffer);

		int c = read();
		sb.append((char)c);
		c = read();
		sb.append((char)c);

		return sb.toString();
	}

	private void setHorizontal(int dots) throws IOException {
		byte[] buffer = new byte[4];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x58;
		buffer[2]     = (byte)(dots / 256);
		buffer[3]     = (byte)(dots % 256);
		write(buffer);
	}

	private synchronized int getPrinterStatus() throws IOException {
		
        int status    = 0;
        int c         = 0;
		if( isSmallTkt && mdlName.equals("750") ) {
			byte[] buffer = new byte[2];
			try {
				reset();
			} catch (Exception e) {
				print(e.getMessage());
			}
	        buffer[0]     = 0x1d;
	        buffer[1]     = 0x7a;

	        write(buffer);
	        
	        c = read();
	
			if( c == -1 ) {
				return -1;
			}
		
	        status     = c << 8;
	        buffer[0]  = 0x1d;
	        buffer[1]  = 0x53;

	        write(buffer);
	        c = read();
	
			if (c == -1) {
				return -1;
			}
	        status |= c;
		}
	        
	    else {
			byte[] buffer = new byte[2];
			buffer[0] = 0x1d;
			buffer[1] = 0x79;
			write(buffer);
	
			buffer = new byte[4];
			read(buffer);

			status = buffer[2] << 8;
			status |= buffer[3];
        }
		return status;
	}

	public String getStatus() throws IOException {
        StringBuffer sb = new StringBuffer();
        
			int status = getPrinterStatus();
			
			sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_STATUS));

			if( status == -1 ) {
			    sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_NOT_ATTACHED));
			    return sb.toString();
			}

			if( mdlName.equals("950") ) {
				if( (status & PRINTER_READY) == PRINTER_READY ) {
					sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_READY) + "\n");
				} else {
					sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_NOT_READY) + "\n");
				}
				
				if( (status & PAPER_JAM_950) == PAPER_JAM_950 ) {
					sb.append("\t"+LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_PAPER_JAM)+"\n");
//				} else if( (status & HEAD_IS_UP_950) == HEAD_IS_UP_950 ) {
//					sb.append("\t"+LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_HEAD_IS_UP)+"\n");
//				} else if( (status & PRINTER_OPEN) == PRINTER_OPEN ) {
//					sb.append("\t"+LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_PRINTER_OPEN)+"\n");
				}
				
				if( (status & TICKET_IN_PRINTER_950) != TICKET_IN_PRINTER_950 ) {
					sb.append("\t" + LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_TICKET_NOT_IN_PRINTER) + "\n");
				}
				if( (status & TICKET_LOW_950) == TICKET_LOW_950 ) {
					sb.append("\t" + LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_TICKET_LOW) + "\n");
				}
			}
			
			else {
				if( (status & PRINTER_READY) == PRINTER_READY ) {
					sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_READY));
				} else {
					sb.append(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_NOT_READY));
				}
				
				sb.append("\n");
				
				if( (status & PAPER_JAM) == PAPER_JAM ) {
					sb.append("\t"+LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_PAPER_JAM)+"\n");
				} else if( (status & HEAD_IS_UP) == HEAD_IS_UP ) {
					sb.append("\t"+LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_HEAD_IS_UP)+"\n");
				} else if( (status & PRINTER_OPEN) == PRINTER_OPEN ) {
					sb.append("\t"+LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_PRINTER_OPEN)+"\n");
				}
				
				if( (status & TICKET_IN_PRINTER) != TICKET_IN_PRINTER ) {
					sb.append("\t" + LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_TICKET_NOT_IN_PRINTER) + "\n");
				} else if( (status & TICKET_LOW) == TICKET_LOW ) {
					sb.append("\t" + LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_ERROR_TICKET_LOW) + "\n");
				}
			}

        return sb.toString();
    }

	private void setVertical(int millimeters) throws IOException {
		byte[] buffer = new byte[3];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x59;
		buffer[2]     = (byte)millimeters;
		write(buffer);
	}

	private void setHeight() throws IOException {
		byte[] buffer = new byte[3];
		
		buffer[0] = 0x1d;
		buffer[1] = 0x68;
		buffer[2] = 0x6E;
		write(buffer);
	}		

	private void cancelDoubleHigh() throws IOException {
		byte[] buffer = new byte[2];
		buffer[0]     = 0x1d;
		buffer[1]     = 0x13;
		write(buffer);
	}
	
	private void setCR() throws IOException {
		byte[] buffer = new byte[1];
		
		buffer[0] = 0x0d;
		write(buffer);
	}		

/*	private void audioAlert() throws IOException {
		byte[] buffer = new byte[1];
		buffer[0] = 0x7;
		write(buffer);
	}

	private void cancelDoubleWide() throws IOException {
		byte[] buffer = new byte[1];
		buffer[0] = 0x14;
		write(buffer);
	}
*/
	private void defaultFont() throws IOException {
		byte[] buffer = new byte[2];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x4d;
		write(buffer);
	}

	private boolean echoData() throws IOException {
		byte[] buffer = new byte[3];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x57;
		buffer[2]     = 'a';
		write(buffer);

		int c = read();

		if (c != 'a') {
			return false;
		}

		outputStream.write('b');
		c = read();

		if (c != 'b') {
			return false;
		}

		return true;
	}

	public void formFeed() throws PrinterException {
		byte[] buffer = new byte[1];
		buffer[0] = 0x0c;
		try {
			write(buffer);
		} catch (IOException e) {
			print("Form Feed Error: " + e.getMessage());
			throw new PrinterException(e.getMessage());
		}
	}

	private void landscape() throws IOException {
		byte[] buffer = new byte[3];
		buffer[0]     = 0x1d;
		buffer[1]     = 0x56;
		buffer[2]     = 1;
		write(buffer);
	}

	private void largeFont() throws IOException {
		byte[] buffer = new byte[2];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x54;
		write(buffer);
	}

	private void mediumFont() throws IOException {
		byte[] buffer = new byte[2];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x55;
		write(buffer);
	}

/*	private void portrait() throws IOException {
		byte[] buffer = new byte[3];
		buffer[0]     = 0x1d;
		buffer[1]     = 0x56;
		buffer[2]     = 0;
		write(buffer);
	}
*/
	private void printBarcode(String barcode, String printerModel) throws IOException {
		if( isSmallTkt ) {
			setHorizontal(51);
			setVertical(18);
			setHeight();
			setBarcodeWidth(4);
		} else {
			setHorizontal(159);
			setVertical(22);
			setBarcodeWidth(5);
		}

		// if this is nanoptix
		if(printerModel.equals(IVoucherConstants.TKTSCANNER_NANOPTIX_MODEL)) {
			setCR();
		}

		byte[] buffer = new byte[22];
		buffer[0]     = 0x1d;  // (gs) Group Separator
		buffer[1]     = 0x6b;  //  k char
		buffer[2]     = 0x07;  // (bel) bell
		buffer[3]     = 18;    // (dc2) Device Control 2

		String stnd_key = new SessionUtility().getSiteConfigurationValue(IDBLabelKeyConstants.VOUCHER_BARCODE_SYSTEM_VALIDATION);
		
		if( stnd_key != null && stnd_key.equalsIgnoreCase(IDBLabelKeyConstants.VCH_BARCODE_STANDARD_VALIDATION) ) {
			System.arraycopy(barcode.getBytes(), 0, buffer, 4, 16);
		} else {
			System.arraycopy(barcode.getBytes(), 0, buffer, 4, 18);
		}

		write(buffer);
		setVertical(37);
		if( isSmallTkt ) {
			setVertical(33);
		}
	}

	private int read() throws IOException {
		//print("Reading from Itacha");
		int c = inputStream.read();
		//print("Done reading from Itacha: " + c);

		if (c == -1 || c == 0) {
			throw new IOException(IVoucherConstants.VOU_PRINT_TIMEOUT);
		}

		return c;
	}

	private int read(byte[] buffer) throws IOException {
		int bytesRead = 0;
		print("Reading(" + buffer.length + ") from Ithaca");
		while (bytesRead != buffer.length) {
			print("bytesRead: "   	+ bytesRead + 
				  "buffer.length: " + buffer.length);
			
			int c = inputStream.read(buffer, bytesRead, buffer.length - bytesRead);
			
			print(" bytes read: " + c);
			bytesRead += c;

			if (c == -1 || c == 0) {
				print("Throwing Timeout Exception");
				throw new IOException(IVoucherConstants.VOU_PRINT_TIMEOUT);
			}
		}
		return bytesRead;
	}

	public void reset() throws PrinterException {
		try {
			byte[] buffer = new byte[2];
			buffer[0]     = 0x1b;
			buffer[1]     = 0x40;
			write(buffer);
		} catch (IOException e) {
			throw new PrinterException(e.getMessage());
		}
	}

	private void smallFont() throws IOException {
		byte[] buffer = new byte[2];
		buffer[0]     = 0x1b;
		buffer[1]     = 0x50;
		write(buffer);
	}

	private void truncate() throws IOException {
		byte[] buffer = new byte[3];
		buffer[0]     = 0x1d;
		buffer[1]     = 0x54;
		buffer[2]     = 0;
		write(buffer);
	}

	 // If Small Ticket.
	private void smallTicket() throws IOException {
		byte[] buffer = new byte[3];
		buffer[0] = 0x1d;
		buffer[1] = 0x74;
		buffer[2] = 0x32;
		write(buffer);
	}

	private void write(byte[] buffer) throws IOException {
		print("Writing to ithaca");
		try {
			outputStream.write(buffer);
		} catch (RuntimeException e) {			
			throw new IOException(LabelLoader.getLabelValue(ITicketConstants.VOU_PRINTER_CONNECTION_ERROR));
		}
		print("Done writing to ithaca");
	}

	public void print(String message){
		log.debug(message);
	}

	/**
	 * Print Receipt
	 *
	 * @param td ticket detail of the receipt to print
	 * @param employee name of the cashier who is printing the receipt
	 *
	 * @throws PrinterException if there is an error with the serial port
	 */
	public synchronized void printCustomerReceipt(TicketDetails td, String employee)   throws PrinterException {   	
		getCasinoDetails();
		java.text.DateFormat dfdate = new java.text.SimpleDateFormat("MM/dd/yyyy");
		java.text.DateFormat dftime = new java.text.SimpleDateFormat("HH:mm:ss");

		String barcode     = td.getBarcode();
		String issueDate = dfdate.format(td.getEffective());
		String time = td.getTime();
		DateUtil paid = new DateUtil(time);
		Calendar paidCalendar = paid.getCalendar();

		String paidDate = dfdate.format(paidCalendar.getTime());
		String paidTime = dftime.format(paidCalendar.getTime());

		try {
			// reset();
			printText("\n\n\n"+LabelLoader.getLabelValue(ITicketConstants.CASHIER_RECEIPT));
			printText("---------------");
			printText(LabelLoader.getLabelValue(ITicketConstants.VALIDATION_NO));
			printText("\t" + Text.expandBarcode(barcode));
			if(barcode.startsWith("5") || barcode.startsWith("6"))
				//printText("Machine Number   : " + barcode.substring(4, 9));
				printText(LabelLoader.getLabelValue(ITicketConstants.MAC_NO)+ td.getLocation());
			else if(barcode.startsWith("7") && barcode.charAt(4) == '9')
				// printText("Employee ID    : " + barcode.substring(5, 9));
				printText(LabelLoader.getLabelValue(ITicketConstants.LOCATION_ID )+ td.getLocation());
			else 
				printText(LabelLoader.getLabelValue(ITicketConstants.SYS_GEN_VOU));
			printText(LabelLoader.getLabelValue(ITicketConstants.OPERATOR_NO) + employee);
			printText(LabelLoader.getLabelValue(ITicketConstants.ISSUE_DATE) + issueDate);
			printText(LabelLoader.getLabelValue(ITicketConstants.PAID_DATE) + paidDate);
			printText(LabelLoader.getLabelValue(ITicketConstants.PAID_TIME) + paidTime);

			try {
				printText(LabelLoader.getLabelValue(ITicketConstants.TKT_AMOUNT)+ Text.convertDollar((long)(td.getAmount())));
			} catch (Exception e) {
				printText(LabelLoader.getLabelValue(ITicketConstants.TKT_AMOUNT)+ LabelLoader.getLabelValue(ITicketConstants.INVALID_AMT));
			}

			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			printText("");
			formFeed();			
			int status = getPrinterStatus();
			if ((status & BARCODE_COMPLETE) != BARCODE_COMPLETE) {
				throw new PrinterException(LabelLoader.getLabelValue(ITicketConstants.VOU_RECEIPT_PRINTER_CONNECTION_ERROR)+ "\n"+ getStatus());
			}			
		} catch (Exception error) {        	
			if(!error.getLocalizedMessage().equalsIgnoreCase(IVoucherConstants.VOU_PRINT_TIMEOUT)){
				throw new PrinterException(error.getLocalizedMessage());
			}
		}
	}


	public static void main(String args[]){/*



    	try {
			VoucherImage img = new VoucherImage();
			TicketForm form = new TicketForm();
			form.setAmount(100);
			form.setBarcode("700989011111134532");
			form.setEffectiveDate(DateHelper.getXMLGregorianCalendar(new Date()));
			form.setEmployeeId("1200");
			form.setExpireDate(DateHelper.getXMLGregorianCalendar(new Date()));
			form.setTransAssetNumber("2000");
			form.setAmountType(AmountTypeEnum.CASHABLE);
			form.setStatus(VoucherStatusEnum.ACTIVE);
			form.setPlayerId("11");
			form.setPlayerCardReqd("Y");
			TicketDetails details = img.copyObj(form);
			Printer printer = null;
			Class.forName(IVoucherConstants.PRINTER_CLASS_TO_USE);
			printer = Printer.getPrinter("Ithaca","850");
			printer.open("COM1");
			printer.printReceipt(details,"1200");
		} catch (PrinterNoSuchDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 */}

}
