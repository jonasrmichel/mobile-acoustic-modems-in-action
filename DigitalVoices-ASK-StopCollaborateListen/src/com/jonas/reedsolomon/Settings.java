package com.jonas.reedsolomon;

/**
 * Reed Solomon Coding for glyphs
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
 */
public interface Settings {
	/*
	 * Below is kParityBytes, the only parameter you should have to
	 * modify.
	 *	  
	 * It is the number of parity bytes that will be appended to
	 * your data to create a codeword.
	 * 
	 * In general, with E errors, and K erasures, you will need
	 * 2E + K bytes of parity to be able to correct the codeword
	 * back to recover the original message data.
	 *
	 * You could say that each error 'consumes' two bytes of the parity,
	 * whereas each erasure 'consumes' one byte.
	 *	
	 * Note that the maximum codeword size is 255, so the
	 * sum of your message length plus parity should be less than
	 * or equal to this maximum limit.
	 * 
	 * In practice, you will get slow error correction and decoding
	 * if you use more than a reasonably small number of parity bytes.
	 * (say, 10 or 20)
	 */
	public static final int kParityBytes = 4;
	/* maximum degree of various polynomials */
	public static final int kMaxDeg = (kParityBytes * 2);
	/* print debugging info */
	public static final boolean kDebug = false;
}
