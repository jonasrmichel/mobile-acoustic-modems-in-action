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
 * Multiplication and Arithmetic on Galois Field GF(256)
 *
 * From Mee, Daniel, "Magnetic Recording, Volume III", Ch. 5 by Patel.
 * 
 */
public class Galois implements Settings {
	/* This is one of 14 irreducible polynomials
	 * of degree 8 and cycle length 255. (Ch 5, pp. 275, Magnetic Recording)
	 * The high order 1 bit is implicit
	 * x^8 + x^4 + x^3 + x^2 + 1 
	 */
	static int kPPoly = 0x1D;
	
	static int gexp[] = new int[512];
	static int glog[] = new int[256];
	
	static void init_galois_tables() {
		/* initialize the table of powers of alpha */
		init_exp_table();
	}
	
	static void init_exp_table() {
		int i, z;
		int pinit, p1, p2, p3, p4, p5, p6, p7, p8;

		pinit = p2 = p3 = p4 = p5 = p6 = p7 = p8 = 0;
		p1 = 1;

		gexp[0] = 1;
		gexp[255] = gexp[0];
		glog[0] = 0; /* shouldn't log[0] be an error? */

		for (i = 1; i < 256; i++) {
			pinit = p8;
			p8 = p7;
			p7 = p6;
			p6 = p5;
			p5 = p4 ^ pinit;
			p4 = p3 ^ pinit;
			p3 = p2 ^ pinit;
			p2 = p1;
			p1 = pinit;
			gexp[i] = p1 + p2 * 2 + p3 * 4 + p4 * 8 + p5 * 16 + p6 * 32 + p7 * 64 + p8 * 128;
			gexp[i + 255] = gexp[i];
		}

		for (i = 1; i < 256; i++) {
			for (z = 0; z < 256; z++) {
				if (gexp[z] == i) {
					glog[i] = z;
					break;
				}
			}
		}
	}
	
	/* multiplication using logarithms */
	static int gmult(int a, int b) {
		int i, j;
		if (a == 0 || b == 0)
			return (0);
		i = glog[a];
		j = glog[b];
		return (gexp[i + j]);
	}

	static int ginv(int elt) {
		return (gexp[255 - glog[elt]]);
	}
}
