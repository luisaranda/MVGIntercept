/*****************************************************************************
 * $Id: BallySecureRandom.java,v 1.1, 2010-01-12 08:46:20Z, Lokesh, Krishna Sinha$Date: 
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

import java.net.InetAddress;
import java.util.Random;
import java.security.SecureRandom;

public class BallySecureRandom extends SecureRandom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static byte[] bytes = null;

	static {
		int hashCode = 0;

		try {
			hashCode = InetAddress.getLocalHost().hashCode();
			hashCode %= Math.E;
			if (hashCode < 0) {
				hashCode *= -1;
			}
		} catch (Exception ignore) {}
		bytes = new byte[hashCode];
		Random random = new Random(System.currentTimeMillis());
		random.nextBytes(bytes);
		bytes = new byte[56];
		random.nextBytes(bytes);
		int j = 24;
		int k = 55;
		try {
			hashCode = InetAddress.getLocalHost().hashCode();
		} catch (Exception ignore) {}
		for (int i = 0; i < 100; i++) {
			bytes[k] = (byte)(bytes[k] + bytes[j]);
			j--;
			k--;
			if (j == 0) {
				j = 55;
			} else if (k == 0) {
				k = 55;
			}
		}
	}

	public BallySecureRandom() {
		super(bytes);
	}

	public static void main(String[] args) {
		new BallySecureRandom();
	}
}
