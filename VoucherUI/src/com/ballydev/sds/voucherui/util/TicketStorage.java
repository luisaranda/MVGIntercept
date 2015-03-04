/*****************************************************************************
 * $Id: TicketStorage.java,v 1.1, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$Date: 
 *****************************************************************************
 *           Copyright (c) 2006 Bally Technology  1977 - 2007
 *  All programs and files on this medium are operated under U.S.
 *  patents Nos. 5,429,361 & 5,470,079
 *
 *  All programs and files on this medium are copyrighted works and all rights
 *  are reserved.  Duplication of these in whole or in part is prohibited
 *  without written permission from Bally Gaming Inc.,
 *  Las Vegas, Nevada, U.S.A
 *****************************************************************************/
package com.ballydev.sds.voucherui.util;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;


/**
 * Class that is ued to store offline ticket information in files.
 * 
 * NOTE: This class was taken from exisitng versions of SDS. 
 * Some changes may be neeed to meet SDS 11.0 requirements.
 */
public class TicketStorage implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/** Max count of ticket details in storage. */

	private final int MAX_COUNT = 50;



	/** Private key for sealed object. */

	private final byte[] raw = {14, 7, 54, -6, 78, 40, -14};



	/** File name for the Ticket Storage. */

	private String fileName = null;
       
	/** Vector containing ticket details of the last MAX_COUNT tickets */

	@SuppressWarnings("unchecked")
	private Vector details  = null;
        
        private long lAmount    = 0;           
        private static TicketStorage ticketStore = null;
        private boolean isOTC = false;
        
        public static TicketStorage getInstance(String fileName) {
            if (ticketStore == null) ticketStore = new TicketStorage(fileName);
            if (!ticketStore.getFileName().equals(fileName)) 
                ticketStore = new TicketStorage(fileName);
            return ticketStore;
        }
        
	public void setOTC(boolean isOTC) {
		this.isOTC = isOTC;
	}
	
	/**
	 * does not exist then initialize the details vector to a new 
	 * vector.
	 */

	@SuppressWarnings("unchecked")
	public TicketStorage(String fileName) {

		this.fileName = fileName;
        this.isOTC = false;
        
		try {

			ObjectInputStream input = new ObjectInputStream(

				new FileInputStream(fileName));

			SecureRandom sr = new BallySecureRandom();

			SecretKeySpec keySpec = 

				new SecretKeySpec(raw, "Blowfish");

			Cipher cipher = Cipher.getInstance("Blowfish");

			cipher.init(Cipher.DECRYPT_MODE, keySpec, sr);

			SealedObject so = (SealedObject)input.readObject();

			details = (Vector)so.getObject(cipher);

		} 

		catch (FileNotFoundException notFound) {

			details = new Vector();

		}

		catch (Exception e) {

			System.out.println("TicketStorage - Error in constructor: " + e);
		}

	}

	@SuppressWarnings("unchecked")
	public Vector getDetails() {
		return (Vector)details.clone();
	}

	@SuppressWarnings("unchecked")
	public boolean isElement(Hashtable htTicket) {
		String barcode = (String) htTicket.get("barcode");
		for (int i = 0; i < details.size(); ++i) {
			Hashtable element = (Hashtable) details.elementAt(i);
			String barcodeInStorage = (String) element.get("barcode");
			
			if (barcodeInStorage.equals(barcode))
				return true;
		}
		
		return false;
	}
	
    @SuppressWarnings("unchecked")
	public void add(Hashtable htTicket) {
    	details.add(htTicket);
        store();
    }

	@SuppressWarnings("unchecked")
	public void add(TicketDetails td) {

		while (details.size() >= MAX_COUNT) {

			details.removeElementAt(0);

		}

		Hashtable element = new Hashtable();

		element.put("time",  td.getTime());

		if (isOTC) {
			element.put("location", td.getLocation());
		} else {
			element.put("employee", td.getEmployee());
		}
		
		element.put("barcode", td.getBarcode());

		element.put("command", td.getCommand());

		String amount = null;

		try {

			amount = (" " + 

			Text.convertDollar(calcTicketAmount(td.getAmount())));

		}

		catch(TextException e) {

			amount = "Error";

		}

		element.put("amount", amount);
                
                add(element);
		//details.add(element);
		//store();

	}

        public long calcTicketAmount(double doubleValue) {
            long lVal = (long) (doubleValue * 1000);
            if(lVal % 10 > 5) lVal++;
            lVal = lVal/10;    
            return lVal;
        }
        
        public String getFileName() {
            return this.fileName;
        }
        
        @SuppressWarnings("unchecked")
		public String getFirstRecordDate() {
            Hashtable hRecords = null;
            String strDate = "";
            try {
                if (details.size() > 0) {
                    hRecords = (Hashtable) details.elementAt(0);
                    strDate  = (String) hRecords.get("time");
                }
            } catch (Exception e) {
                System.out.println("Error storing: " + e.getMessage());
            }
            return strDate;
        }

	@SuppressWarnings("unchecked")
	public String toString(String strStartHH, String strStartMM, String strEndHH, String strEndMM) {

		Hashtable element = null;
        lAmount    = 0;
        StringBuffer sb = new StringBuffer();

		//sb.append("File Name: " + fileName + "\n");

		if (isOTC) {
            sb.append("Date                  Location       Command        Barcode                   Value($)\n");
            sb.append("--------------------------------------------------------------------------------------\n");
		} else {
            sb.append("Date                  Employee       Command        Barcode                   Value($)\n");
            sb.append("--------------------------------------------------------------------------------------\n");
		}
		
		int numRecordsShown = 0;
	    for (int i = 0; i < details.size(); i++) {
	    	//sb.append(" ");
	        element = (Hashtable)details.elementAt(i);
	        try {
	        	String strTime = (String) element.get("time");
	            int startTime = getMinutes(strStartHH, strStartMM);
	            int endTime = getMinutes(strEndHH, strEndMM);

	            String strHH = getStrHoursFromTime(strTime);
	            String strMM = getStrMinutesFromTime(strTime);
	            int time = getMinutes(strHH, strMM);
	        	
	            if ((time < startTime) || (time > endTime)) {
	            	continue;
	            } else {
	            	numRecordsShown++;
	            }
	            
	        	sb.append(Text.fill(strTime, 22, ' ', true));
	        		if (isOTC) {
	        			sb.append(Text.fill(
	        					(String)element.get("location"), 15, ' ', true));
	        		} else {
	        			sb.append(Text.fill(
	        					(String)element.get("employee"), 15, ' ', true));
	        		}
	                sb.append(Text.fill(
	                    (String)element.get("command"), 15, ' ', true));
	            }catch (Exception e) {
	                System.out.println("Fill exception :" + e);
	                sb.append(element.get("time"));
	                if (isOTC) {
	                	sb.append(element.get("location"));
	                } else {
	                	sb.append(element.get("employee"));
	                }
	                sb.append(element.get("command"));
	            }
	            if (element.get("command").equals("CreateTicket")) {
	                    sb.append(Text.maskBarcode((String)element.get("barcode")));
	                    sb.append(getAmount(element.get("amount")));
	            } else {
	                    sb.append(Text.expandBarcode((String)element.get("barcode")));
	            }
	            if (element.get("command").equals("Redeem") || 
	                element.get("command").equals("RedeemUpdate") || 
	                element.get("command").equals("Override") ||
	                element.get("command").equals("Payout")) {
	                sb.append(getAmount(element.get("amount")));
	            } else if (element.get("command").equals("Void") ||
	                element.get("command").equals("SystemVoid"))
	                sb.append("   n/a");
	            sb.append("\n");
	    }
	    
/*	    if (details.size() <= 0) 
	        sb.append("There are no records available\n");
*/	
	    if (numRecordsShown <= 0)
	    	sb.append("There are no records available\n");
	    
	    sb.append("\n--------------------------------------------------------------------------------------\n");
	    try {
/*	        sb.append("Number of records : " + 
	                Text.fill("" + details.size(), 38, ' ', true));
*/	        
	    	sb.append("Number of records : " + 
	                Text.fill("" + numRecordsShown, 38, ' ', true));
	    	sb.append("Total Amount : " + 
	            Text.fill(Text.convertDollar(lAmount), 11, ' ', false));
	    } catch (Exception e) {
	        System.out.println("Error convert to Dollar: " + e);
	    }
	        
	    return sb.toString();
	}
       
    private int getMinutes(String strHH, String strMM) {
    	int minutes = 0;
    	
    	try {
    		minutes = Integer.parseInt(strHH)*60 + Integer.parseInt(strMM);
    	} catch(Exception e) {    		
    	}
    	
    	return minutes;
    }

    private String getStrHoursFromTime(String strTime) {
    	int index = strTime.lastIndexOf("-")+1;
    	return (strTime.substring(index, index+2));
    }
    
    private String getStrMinutesFromTime(String strTime) {
    	int index = strTime.indexOf(".")+1;
    	return (strTime.substring(index, index+2));
    }

	@SuppressWarnings("unchecked")
	public String toString() {

		Hashtable element = null;
                
                lAmount    = 0;

		StringBuffer sb = new StringBuffer();

		//sb.append("File Name: " + fileName + "\n");

		if (isOTC) {
            sb.append("Date                  Location       Command        Barcode                   Value($)\n");
            sb.append("--------------------------------------------------------------------------------------\n");
		} else {
            sb.append("Date                  Employee       Command        Barcode                   Value($)\n");
            sb.append("--------------------------------------------------------------------------------------\n");
		}
		
                for (int i = 0; i < details.size(); i++) {
                        //sb.append(" ");
                        element = (Hashtable)details.elementAt(i);
                        try {
                            sb.append(Text.fill(
                                (String)element.get("time"), 22, ' ', true));
                    		if (isOTC) {
                    			sb.append(Text.fill(
                    					(String)element.get("location"), 15, ' ', true));
                    		} else {
                    			sb.append(Text.fill(
                    					(String)element.get("employee"), 15, ' ', true));
                    		}
                            sb.append(Text.fill(
                                (String)element.get("command"), 15, ' ', true));
                        }catch (Exception e) {
                            System.out.println("Fill exception :" + e);
                            sb.append(element.get("time"));
                            if (isOTC) {
                            	sb.append(element.get("location"));
                            } else {
                            	sb.append(element.get("employee"));
                            }
                            sb.append(element.get("command"));
                        }
                        if (element.get("command").equals("CreateTicket")) {
                                sb.append(Text.maskBarcode(
					(String)element.get("barcode")));
                                sb.append(getAmount(element.get("amount")));
                        } else {
                                sb.append(Text.expandBarcode(
					(String)element.get("barcode")));
                        }
                        if (element.get("command").equals("Redeem") || 
                            element.get("command").equals("RedeemUpdate") || 
                            element.get("command").equals("Override") ||
                            element.get("command").equals("Payout")) {
                            sb.append(getAmount(element.get("amount")));
                        } else if (element.get("command").equals("Void") ||
                            element.get("command").equals("SystemVoid"))
                            sb.append("   n/a");
			sb.append("\n");
                }
                
                if (details.size() <= 0) 
                    sb.append("There are no records available\n");

                sb.append("\n--------------------------------------------------------------------------------------\n");
                try {
                    sb.append("Number of records : " + 
                            Text.fill("" + details.size(), 38, ' ', true));
                    sb.append("Total Amount : " + 
                        Text.fill(Text.convertDollar(lAmount), 11, ' ', false));
                } catch (Exception e) {
                    System.out.println("Error convert to Dollar: " + e);
                }
                
		return sb.toString();

	}

        public String getAmount(Object pObj) {
            String sReturn = "";
            try {
              sReturn = Text.fill((String)pObj, 11, ' ', false);
            } catch (Exception e) {
                sReturn = "  " + pObj;
            }
            addAmount((String)pObj);
            return sReturn;
        }
        
        public void addAmount(String pAmt) {
            String amt  = "";
            @SuppressWarnings("unused")
			int idx     = 0;
            if (pAmt != null && pAmt.trim().length() > 0) {
                amt     = pAmt.trim();
                if (amt.indexOf("$") >=0) amt = amt.substring(amt.indexOf("$")+1);
                try {
                    double dAmt = Double.parseDouble(amt);
                    lAmount += calcticketAmount(dAmt);
                }catch (Exception e) {
                    System.out.println("Error Adding Amount : " + e);
                }
            }
        }

        public long calcticketAmount(double doubleValue) {
            long lVal = (long) (doubleValue * 1000);
            if (lVal % 10 > 5)
                    lVal++;
            lVal = lVal / 10;
            return lVal;
	}

	public static void main(String[] args) {/*

		TicketStorage storage = new TicketStorage("storage");

		TicketDetails td = new TicketDetails("CreateTicket", 

			"Success", "000000000000000000", "C000000001", 10.0, 

			"0", "Success", "00000000", new GregorianCalendar(),

			new GregorianCalendar(), "00001111", 

			"1/12/03 12:03:04", "false");

		storage.add(td);

		System.out.println(storage);

	*/}



	private void store() {

		try {

			SecureRandom sr = new BallySecureRandom();

			SecretKeySpec keySpec = 

				new SecretKeySpec(raw, "Blowfish");

			Cipher cipher = Cipher.getInstance("Blowfish");

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, sr);

			SealedObject so = new SealedObject(details, cipher);

			ObjectOutputStream output = new ObjectOutputStream(

				new FileOutputStream(fileName));

			output.writeObject(so);

			output.close();

		} catch (Exception e) {

			System.out.println("Error storing: " + e.getMessage());

		}

	}

}

