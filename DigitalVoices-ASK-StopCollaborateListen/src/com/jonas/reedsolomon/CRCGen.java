package com.jonas.reedsolomon;

/**
 * Copyright Henry Minsky (hqm@alum.mit.edu) 1991-2009
 * (Ported to Java by Jonas Michel 2012)
 * 
 * This is a direct port of RSCODE by Henry Minsky
 * http://rscode.sourceforge.net/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. 
 *
 * Commercial licensing is available under a separate license, please
 * contact author for details.
 *
 * Source code is available at http://code.google.com/p/mobile-acoustic-modems-in-action/
 *  
 *  
 * CRC-CCITT generator simulator for byte wide data.
 * 
 * CRC-CCITT = x^16 + x^12 + x^5 + 1
 *
 */
public class CRCGen implements Settings {

	/*
	 * Reads in a sequence of bytes and returns its 16 bit Cylcic Redundancy
	 * Check (CRC-CCIIT 0xFFFF).
	 * 
	 * 1 + x + x^5 + x^12 + x^16 is irreducible polynomial.
	 * 
	 * Copyright 2000Ð2011, Robert Sedgewick and Kevin Wayne
	 * source: http://introcs.cs.princeton.edu/java/51data/CRC16CCITT.java.html
	 */
	public static int crc_16_ccitt(byte[] msg, int len) {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

		boolean bit, c15;
		for (int b = 0; b < len; b++) {
			for (int i = 0; i < 8; i++) {
				bit = ((msg[b] >> (7 - i) & 1) == 1);
				c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}
		crc &= 0xffff;
		
		return crc;
	}
	
	/* Computes the CRC-8-CCITT checksum on array of byte data, length len */
	public static byte crc_8_ccitt(byte[] msg, int len) {
		int crc = (byte) 0xFF; // initial value
		int polynomial = 0x07; // (0, 1, 2) : 0x07 / 0xE0 / 0x83

		boolean bit, c7;
		for (int b = 0; b < len; b++) {
			for (int i = 0; i < 8; i++) {
				bit = ((msg[b] >> (7 - i) & 1) == 1);
				c7 = ((crc >> 7 & 1) == 1);
				crc <<= 1;
				if (c7 ^ bit)
					crc ^= polynomial;
			}
		}
		crc &= 0xffff;

		return (byte) crc;
	}
	
	/* Computes the CRC-CCITT checksum on array of byte data, length len */
	public static int crc_ccitt(byte[] msg, int len) {
		int i, acc = 0;

		for (i = 0; i < len; i++) {
			acc = crchware((0xFF & (int)msg[i]), 0x1021, acc);
		}
		
		return acc;
	}
	
	/* models crc hardware (minor variation on polynomial division algorithm) */
	public static int crchware(int data, int genpoly, int accum) {
		int i;
		data <<= 8;
		for (i = 8; i > 0; i--) {
			if (((data ^ accum) & 0x8000) == 1)
				accum = (((accum << 1) ^ genpoly) & 0xFFFF);
			else
				accum = ((accum << 1) & 0xFFFF);
			data = ((data << 1) & 0xFFFF);
		}
		return accum;
	}
}
