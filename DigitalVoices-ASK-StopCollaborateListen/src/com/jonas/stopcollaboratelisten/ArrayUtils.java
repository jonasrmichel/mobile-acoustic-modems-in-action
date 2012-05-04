package com.jonas.stopcollaboratelisten;

/**
 * Copyright 2002 by the authors. All rights reserved.
 *
 * Author: Cristina V Lopes
 */


/**
 * A set of handy array manipulation utilities
 * @author CVL
 */
public class ArrayUtils {

    /**
     * Create a new array of length 'length' and fill it from array 'array'
     * @param array the array from which to take the subsection
     * @param start the index from which to start the subsection copy
     * @param length the length of the returned array.
     * @return byte[] of length 'length', padded with zeros if array.length is shorter than 'start' + 'length'
     *
     * NOTE! if start + length goes beyond the end of array.length, the returned value will be padded with 0s.
     */
    public static byte[] subarray(byte[] array, int start, int length){
	byte[] result = new byte[length];
	for(int i=0; (i < length) && (i + start < array.length); i++){
	    result[i] = array[i + start];
	}
	return result;
    }

    /**
     * Converts the input matrix into a single dimensional array by transposing and concatenating the columns
     * @param input a 2D array whose columns will be concatenated
     * @return the concatenated array
     */
    public static byte[] concatenate(byte[][] input){
	//sum the lengths of the columns
	int totalLength = 0;
	for(int i = 0; i < input.length; i++){
	    totalLength += input[i].length;
	}
	//create the result array
	byte[] result = new byte[totalLength];

	//populate the result array
	int currentIndex = 0;
	for(int i=0; i < input.length; i++){
	    for(int j = 0; j < input[i].length; j++){
		result[currentIndex++] = input[i][j];
	    }
	}
	return result;
    }
    
    /**
     * @param input1 array of bytes (possibly null)
     * @param input2 array of bytes
     * @return the concatenated array of bytes of length (input1.length + input2.length)
     */
    public static byte[] concatenate(byte[] input1, byte[] input2) {
    	byte[] result;
    	if (input1 != null)
    		result = new byte[input1.length + input2.length];
    	else
    		result = new byte[input2.length];
    	
    	for (int i = 0; i < result.length; i++) {
    		if (input1 == null)
    			result[i] = input2[i];
    		else if (i < input1.length)
    			result[i] = input1[i];
    		else
    			result[i] = input2[input2.length - (result.length - i)];
    	}
    	return result;
    }

    /**
     * @param sequence the array of floats to return as a shifted and clipped array of bytes
     * @return byte[i] = sequence[i] * Constants.kFloatToByteShift cast to a byte
     * Note!: This doesn't handle cast/conversion issues, so don't use this unless you understand the code
     */
    public static byte[] getByteArrayFromDoubleArray(double[] sequence){
	byte[] result = new byte[sequence.length];
	for(int i=0; i < result.length; i++){
	    result[i] = (byte)((sequence[i] * Constants.kFloatToByteShift) - 1);
	}
	return result;
    }
}
