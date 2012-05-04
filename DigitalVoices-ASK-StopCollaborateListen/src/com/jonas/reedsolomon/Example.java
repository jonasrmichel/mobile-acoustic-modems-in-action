package com.jonas.reedsolomon;

/**
 * Example use of Reed-Solomon library 
 *
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
 * This same code demonstrates the use of the encoder and 
 * decoder/error-correction routines. 
 *
 * We are assuming we have at least four bytes of parity 
 * (kParityBytes >= 4).
 * 
 * This gives us the ability to correct up to two errors, or 
 * four erasures. 
 *
 * In general, with E errors, and K erasures, you will need
 * 2E + K bytes of parity to be able to correct the codeword
 * back to recover the original message data.
 *
 * You could say that each error 'consumes' two bytes of the parity,
 * whereas each erasure 'consumes' one byte.
 *
 * Thus, as demonstrated below, we can inject one error (location unknown)
 * and two erasures (with their locations specified) and the 
 * error-correction routine will be able to correct the codeword
 * back to the original message.
 * 
 */

public class Example implements Settings {
	static String msg = "The fat cat in the hat sat on a rat.";
	static byte codeword[] = new byte[256];

	/* Some debugging routines to introduce errors or erasures into a codeword. */

	/* Introduce a byte error at LOC */
	static void byte_err(int err, int loc, byte[] dst) {
		System.out.println("Adding Error at loc " + loc + ", data 0x"
				+ Integer.toHexString(dst[loc - 1]));
		dst[loc - 1] ^= err;
	}

	/*
	 * Pass in location of error (first byte position is labeled starting at 1,
	 * not 0), and the codeword.
	 */
	static void byte_erasure(int loc, byte dst[]) {
		System.out.println("Erasure at loc " + loc + ", data 0x"
				+ Integer.toHexString(dst[loc - 1]));
		dst[loc - 1] = 0;
	}
	
	/*
	 * Trim off excess zeros and parity bytes.
	 */
	static byte[] rtrim(byte[] bytes) {
		int t = bytes.length - 1;
		while (bytes[t] == 0)
			t -= 1;
		byte[] trimmed = new byte[(t+1) - Settings.kParityBytes];
		for (int i = 0; i < trimmed.length; i++) {
			trimmed[i] = bytes[i];
		}
		return trimmed;
	}

	public static void main(String[] args) {
		int erasures[] = new int[16];
		int nerasures = 0;

		/* Initialization the ECC library */

		RS.initialize_ecc();

		/* ************** */

		/* Encode data into codeword, adding kParityBytes parity bytes */
		RS.encode_data(msg.getBytes(), msg.length(), codeword);

		System.out.println("Encoded data is: " + new String(rtrim(codeword)));

		int ML = msg.length() + Settings.kParityBytes;

		/* Add one error and two erasures */
		byte_err(0x35, 3, codeword);

		byte_erasure(17, codeword);
		byte_erasure(19, codeword);

		System.out.println("With some errors: " + new String(rtrim(codeword)));

		/*
		 * We need to indicate the position of the erasures. Erasure positions
		 * are indexed (1 based) from the end of the message...
		 */

		erasures[nerasures++] = ML - 17;
		erasures[nerasures++] = ML - 19;

		/* Now decode -- encoded codeword size must be passed */
		RS.decode_data(codeword, ML);

		/* check if syndrome is all zeros */
		if (RS.check_syndrome() != 0) {
			Berlekamp.correct_errors_erasures(codeword, ML, nerasures, erasures);

			System.out.println("Corrected codeword: " + new String(rtrim(codeword)));
		}
	}
}
