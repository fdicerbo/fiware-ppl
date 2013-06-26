/*******************************************************************************
 * Copyright (c) 2013, SAP AG
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *  
 *     - Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *     - Neither the name of the SAP AG nor the names of its contributors may
 *      be used to endorse or promote products derived from this software 
 *      without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.sap.research.primelife.utils;

import java.util.*;
import java.util.logging.*;
import java.security.*;
import java.math.*;
import java.net.InetAddress;

/**
 * This class generate random numbers, as long, int, and positive int.
 * 
 * @Version 0.1
 * @Date May 25, 2010
 * 
 */
public class IdGenerator {

	private static final Random RANDOM1;
	private static final Random RANDOM2;
	private static final Random RANDOM3;
	private static final long globalProcessID;
	private static final Logger logger = Logger.getLogger(IdGenerator.class
			.getName());

	static {
		long time = System.currentTimeMillis();
		long nanoTime = System.nanoTime();
		long freeMemory = Runtime.getRuntime().freeMemory();
		long addressHashCode;
		try {
			InetAddress inetAddress;
			inetAddress = InetAddress.getLocalHost();
			addressHashCode = inetAddress.getHostName().hashCode()
					^ inetAddress.getHostAddress().hashCode();
		} catch (Exception err) {
			logger.log(Level.WARNING, "Unable to get local host information.",
					err);
			addressHashCode = IdGenerator.class.hashCode();
		}
		globalProcessID = time ^ nanoTime ^ freeMemory ^ addressHashCode;
		RANDOM1 = new Random(time);
		RANDOM2 = new Random(nanoTime);
		RANDOM3 = new Random(addressHashCode ^ freeMemory);
	}

	private IdGenerator() {
	}

	/**
	 * Generate a random Long 
	 * @return
	 * 		A random Long number
	 */
	public static long generateLong() {
		return Math.abs(RANDOM1.nextLong() ^ RANDOM2.nextLong()
				^ RANDOM3.nextLong());
	}

	/**
	 * Generate a random Int
	 * @return
	 * 		A random Int number
	 */
	public static int generateInt() {
		return (int) generateLong();
	}

	/**
	 * Generate a random positive Int
	 * @return
	 * 		A random positive Int number
	 */
	public static int generatePositiveInt() {
		return Math.abs(generateInt());
	}

	
	public static byte[] getMD5Bytes(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return digest.digest(content.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (java.io.UnsupportedEncodingException uee) {
			throw new IllegalStateException(uee);
		}
	}

	public static String getHexString(byte[] bytes) {
		// This method cannot change even if it's wrong.
		BigInteger bigInteger = BigInteger.ZERO;
		int shift = 0;
		for (int i = bytes.length; --i >= 0;) {
			BigInteger contrib = BigInteger.valueOf(bytes[i] & 0xFF);
			contrib = contrib.shiftLeft(shift);
			bigInteger = bigInteger.add(contrib);
			shift += 8;
		}
		return bigInteger.toString(16).toUpperCase();
	}

	/**
	 * Gets a process ID that is nearly guaranteed to be globally unique.
	 */
	public static long getGlobalProcessID() {
		return globalProcessID;
	}

	public static int random(int min, int max) {
		if (max <= min) {
			return min;
		}
		return Math.abs(RANDOM1.nextInt()) % (max - min) + min;
	}
}
