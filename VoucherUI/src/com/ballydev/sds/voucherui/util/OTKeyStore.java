package com.ballydev.sds.voucherui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.ballydev.sds.voucherui.constants.IVoucherConstants;
import com.ballydev.sds.voucherui.log.VoucherUILogger;

/**
 * This class helps store and retrieve the key that the user who verifies
 * an offline Ticket needs to know and enter.
 *
 */
/*	NOTE: The logic for encrypting and storing the key 
 * is the same as that used for Storing offiline ticket info.*/
public class OTKeyStore {

	/** Private key for sealed object. */

	private final byte[] raw = { 14, 7, 54, -6, 78, 40, -14 };
	private String fileName = "otkey";

	@SuppressWarnings("unused")
	private static final Logger log = VoucherUILogger.getLogger(IVoucherConstants.MODULE_NAME);

	/**
	 * 
	 * @param fileName 	File name where the key will be stored. 
	 * 					File name to be provided without the folder path.
	 */
	public OTKeyStore() {

		String dirName = PreferencesUtil.getOfflineTicketDirectory();

		File dir = new File(dirName);

		if(!dir.exists())
		{
			System.out.println("making Dir");
			dir.mkdirs();
		}
		this.fileName = dir+"/"+fileName;
	}

	public void setCurrentKey(String key)
	{
		try {


			SecureRandom sr = new BallySecureRandom();

			SecretKeySpec keySpec = 

				new SecretKeySpec(raw, "Blowfish");

			Cipher cipher = Cipher.getInstance("Blowfish");

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, sr);

			SealedObject so = new SealedObject(key, cipher);

			ObjectOutputStream output = new ObjectOutputStream(

					new FileOutputStream(fileName));

			output.writeObject(so);

			output.close();

		} catch (Exception e) {

			System.out.println("Error storing: " + e.getMessage());

		}
	}

	public String getCurrentKey()
	{
		String currKey = null;
		try {

			ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));

			SecureRandom sr = new BallySecureRandom();

			SecretKeySpec keySpec =	new SecretKeySpec(raw, "Blowfish");

			Cipher cipher = Cipher.getInstance("Blowfish");

			cipher.init(Cipher.DECRYPT_MODE, keySpec, sr);

			SealedObject so = (SealedObject) input.readObject();

			currKey = (String) so.getObject(cipher);

		}
		catch (FileNotFoundException notFound) {
//			log.error("Could not find source file to retrieve key.");
			notFound.printStackTrace();
		}
		catch (Exception e) {
//			log.error("OTKeyStore - Error in constructor: ", e);
			e.printStackTrace();
		}

		return currKey;
	}

	public static void main(String[] args) {
		OTKeyStore store = new OTKeyStore();
		store.setCurrentKey("bally");
		System.out.println(store.getCurrentKey());
//		System.out.println("Resetting key...");
//		store.setCurrentKey("bally1");
//		System.out.println(store.getCurrentKey());
	}

}
