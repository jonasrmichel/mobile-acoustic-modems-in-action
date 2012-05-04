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
 * Berlekamp-Peterson and Berlekamp-Massey Algorithms for error-location
 *
 * From Cain, Clark, "Error-Correction Coding For Digital Communications", pp. 205.
 *
 * This finds the coefficients of the error locator polynomial.
 *
 * The roots are then found by looking for the values of a^n
 * where evaluating the polynomial yields zero.
 *
 * Error correction is done using the error-evaluator equation  on pp 207.
 *
 */
public class Berlekamp implements Settings {
	/*
	 * The Error Locator Polynomial, also known as Lambda or Sigma. Lambda[0] == 1
	 */
	static int Lambda[] = new int[Settings.kMaxDeg];

	/* The Error Evaluator Polynomial */
	static int Omega[] = new int[Settings.kMaxDeg];

	/* error locations found using Chien's search */
	static int ErrorLocs[] = new int[256];
	static int NErrors;

	/* erasure flags */
	static int ErasureLocs[] = new int[256];
	static int NErasures;

	/*
	 * From Cain, Clark, "Error-Correction Coding For Digital Communications", pp. 216.
	 */
	static void Modified_Berlekamp_Massey() {
		int n, L, L2, k, d, i;
		int psi[] = new int[Settings.kMaxDeg], psi2[] = new int[Settings.kMaxDeg], D[] = new int[Settings.kMaxDeg];
		int gamma[] = new int[Settings.kMaxDeg];

		/* initialize Gamma, the erasure locator polynomial */
		init_gamma(gamma);

		/* initialize to z */
		copy_poly(D, gamma);
		mul_z_poly(D);

		copy_poly(psi, gamma);
		k = -1;
		L = NErasures;

		for (n = NErasures; n < Settings.kParityBytes; n++) {

			d = compute_discrepancy(psi, RS.synBytes, L, n);

			if (d != 0) {

				/* psi2 = psi - d*D */
				for (i = 0; i < Settings.kMaxDeg; i++)
					psi2[i] = psi[i] ^ Galois.gmult(d, D[i]);

				if (L < (n - k)) {
					L2 = n - k;
					k = n - L;
					/* D = scale_poly(Galois.ginv(d), psi); */
					for (i = 0; i < Settings.kMaxDeg; i++)
						D[i] = Galois.gmult(psi[i], Galois.ginv(d));
					L = L2;
				}

				/* psi = psi2 */
				for (i = 0; i < Settings.kMaxDeg; i++)
					psi[i] = psi2[i];
			}

			mul_z_poly(D);
		}

		for (i = 0; i < Settings.kMaxDeg; i++)
			Lambda[i] = psi[i];
		compute_modified_omega();

	}

	/*
	 * given Psi (called Lambda in Modified_Berlekamp_Massey) and RS.synBytes,
	 * compute the combined erasure/error evaluator polynomial as Psi*S mod z^4
	 */
	static void compute_modified_omega() {
		int i;
		int product[] = new int[Settings.kMaxDeg * 2];

		mult_polys(product, Lambda, RS.synBytes);
		zero_poly(Omega);
		for (i = 0; i < Settings.kParityBytes; i++)
			Omega[i] = product[i];

	}

	/* polynomial multiplication */
	static void mult_polys(int dst[], int p1[], int p2[]) {
		int i, j;
		int tmp1[] = new int[Settings.kMaxDeg * 2];

		for (i = 0; i < (Settings.kMaxDeg * 2); i++)
			dst[i] = 0;

		for (i = 0; i < Settings.kMaxDeg; i++) {
			for (j = Settings.kMaxDeg; j < (Settings.kMaxDeg * 2); j++)
				tmp1[j] = 0;

			/* scale tmp1 by p1[i] */
			for (j = 0; j < Settings.kMaxDeg; j++)
				tmp1[j] = Galois.gmult(p2[j], p1[i]);
			/* and mult (shift) tmp1 right by i */
			for (j = (Settings.kMaxDeg * 2) - 1; j >= i; j--)
				tmp1[j] = tmp1[j - i];
			for (j = 0; j < i; j++)
				tmp1[j] = 0;

			/* add into partial product */
			for (j = 0; j < (Settings.kMaxDeg * 2); j++)
				dst[j] ^= tmp1[j];
		}
	}

	/* gamma = product (1-z*a^Ij) for erasure locs Ij */
	static void init_gamma(int gamma[]) {
		int e;
		int tmp[] = new int[Settings.kMaxDeg];

		zero_poly(gamma);
		zero_poly(tmp);
		gamma[0] = 1;

		for (e = 0; e < NErasures; e++) {
			copy_poly(tmp, gamma);
			scale_poly(Galois.gexp[ErasureLocs[e]], tmp);
			mul_z_poly(tmp);
			add_polys(gamma, tmp);
		}
	}

	void compute_next_omega(int d, int A[], int dst[], int src[]) {
		int i;
		for (i = 0; i < Settings.kMaxDeg; i++) {
			dst[i] = src[i] ^ Galois.gmult(d, A[i]);
		}
	}

	static int compute_discrepancy(int lambda[], int S[], int L, int n) {
		int i, sum = 0;

		for (i = 0; i <= L; i++)
			sum ^= Galois.gmult(lambda[i], S[n - i]);
		return (sum);
	}

	/********** polynomial arithmetic *******************/

	static void add_polys(int dst[], int src[]) {
		int i;
		for (i = 0; i < Settings.kMaxDeg; i++)
			dst[i] ^= src[i];
	}

	static void copy_poly(int dst[], int src[]) {
		int i;
		for (i = 0; i < Settings.kMaxDeg; i++)
			dst[i] = src[i];
	}

	static void scale_poly(int k, int poly[]) {
		int i;
		for (i = 0; i < Settings.kMaxDeg; i++)
			poly[i] = Galois.gmult(k, poly[i]);
	}

	static void zero_poly(int poly[]) {
		int i;
		for (i = 0; i < Settings.kMaxDeg; i++)
			poly[i] = 0;
	}

	/* multiply by z, i.e., shift right by 1 */
	static void mul_z_poly(int src[]) {
		int i;
		for (i = Settings.kMaxDeg - 1; i > 0; i--)
			src[i] = src[i - 1];
		src[0] = 0;
	}

	/*
	 * Finds all the roots of an error-locator polynomial with coefficients
	 * Lambda[j] by evaluating Lambda at successive values of alpha.
	 * 
	 * This can be tested with the decoder's equations case.
	 */

	static void Find_Roots() {
		int sum, r, k;
		NErrors = 0;

		for (r = 1; r < 256; r++) {
			sum = 0;
			/* evaluate lambda at r */
			for (k = 0; k < Settings.kParityBytes + 1; k++) {
				sum ^= Galois.gmult(Galois.gexp[(k * r) % 255], Lambda[k]);
			}
			if (sum == 0) {
				ErrorLocs[NErrors] = (255 - r);
				NErrors++;
				if (Settings.kDebug)
					System.err.println("Root found at r = " + r
							+ ", (255-r) = " + (255 - r));
			}
		}
	}

	/*
	 * Combined Erasure And Error Magnitude Computation
	 * 
	 * Pass in the codeword, its size in bytes, as well as an array of any known
	 * erasure locations, along the number of these erasures.
	 * 
	 * Evaluate Omega(actually Psi)/Lambda' at the roots alpha^(-i) for error
	 * locs i.
	 * 
	 * Returns 1 if everything ok, or 0 if an out-of-bounds error is found
	 */

	static int correct_errors_erasures(byte[] codeword, int csize, int nerasures, int erasures[]) {
		int r, i, j, err;

		/*
		 * If you want to take advantage of erasure correction, be sure to set
		 * NErasures and ErasureLocs[] with the locations of erasures.
		 */
		NErasures = nerasures;
		for (i = 0; i < NErasures; i++)
			ErasureLocs[i] = erasures[i];

		Modified_Berlekamp_Massey();
		Find_Roots();
		
		if ((NErrors <= Settings.kParityBytes) && NErrors > 0) {
			
			/* first check for illegal error locs */
			for (r = 0; r < NErrors; r++) {
				if (ErrorLocs[r] >= csize) {
					if (Settings.kDebug)
						System.err.println("Error loc i=" + i + " outside of codeword length " + csize);
					return (0);
				}
			}

			for (r = 0; r < NErrors; r++) {
				int num, denom;
				i = ErrorLocs[r];
				/* evaluate Omega at alpha^(-i) */

				num = 0;
				for (j = 0; j < Settings.kMaxDeg; j++)
					num ^= Galois.gmult(Omega[j],
							Galois.gexp[((255 - i) * j) % 255]);

				/*
				 * evaluate Lambda' (derivative) at alpha^(-i) ; all odd powers
				 * disappear
				 */
				denom = 0;
				for (j = 1; j < Settings.kMaxDeg; j += 2) {
					denom ^= Galois.gmult(Lambda[j],
							Galois.gexp[((255 - i) * (j - 1)) % 255]);
				}

				err = Galois.gmult(num, Galois.ginv(denom));
				if (Settings.kDebug)
					System.err.println("Error magnitude 0x"
							+ Integer.toHexString(err) + " at loc "
							+ (csize - i));

				codeword[csize - i - 1] ^= err;
			}
			return (1);
		} else {
			if (Settings.kDebug && NErrors > 0)
				System.err.println("Uncorrectable codeword");
			return (0);
		}
	}
}
