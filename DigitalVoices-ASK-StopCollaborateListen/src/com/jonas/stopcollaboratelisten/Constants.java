package com.jonas.stopcollaboratelisten;


/**
 * Copyright 2002 by the authors. All rights reserved.
 *
 * Author: Cristina V Lopes
 * (Modified by Jonas Michel, 2012)
 */

public interface Constants {
    
  public static final double kLowFrequency = 600; //the lowest frequency used
  public static final double kFrequencyStep = 50; //the distance between frequencies

  public static final int kBytesPerDuration = 1; //how wide is the data stream 
  								// (rps - that is, the pre-encoding data stream, not the audio)
  								// (rps - kFrequencies must be this long)
  
  public static final int kBitsPerByte = 8; //unlikely to change, I know

  // Amplitude of each frequency in a frame.
  public static final double kAmplitude = 0.125d; /* (1/8) */

  // Sampling frequency (number of sample values per second)
  public static final double kSamplingFrequency = 22050; //rps - reduced to 11025 from 22050 
  		// to enable the decoder to keep up with the audio on Motorola CLIQ  

  // Sound duration of encoded byte (in seconds)
  public static final double kDuration = 0.2; // rps - increased from 0.1 to improve reliability on Android 

  // Number of samples per duration
  public static final int kSamplesPerDuration = (int)(kSamplingFrequency * kDuration);

  //This is used to convert the floats of the encoding to the bytes of the audio
  public static final int kFloatToByteShift = 128;

  // The length, in durations, of the hail sequence
  public static final int kDurationsPerHail = 3; 

  // The frequency used in the initial hail of the key
  public static final int kHailFrequency = 3000;

  //The frequencies we use for each of the 8 bits
  public static final int kBaseFrequency = 1000;
  public static final int[] kFrequencies = {kBaseFrequency,   //1000
					    (int)(kBaseFrequency * (float)27/24), //1125
					    (int)(kBaseFrequency * (float)30/24), //1250
					    (int)(kBaseFrequency * (float)36/24), //1500 
					    (int)(kBaseFrequency * (float)40/24), //1666
					    (int)(kBaseFrequency * (float)48/24), //2000
					    (int)(kBaseFrequency * (float)54/24), //2250
					    (int)(kBaseFrequency * (float)60/24)};//2500
  
  //The length, in durations, of the hail sequence
  public static final int kDurationsPerSOS = 1;
  
  // The frequency used to signal for help
  public static final int kSOSFrequency = (int)(kBaseFrequency * (float)30/24); //1125
  
  // The length, in durations, of some session timing jitters
  public static final int kPlayJitter = 5;
  public static final int kSetupJitter = 10;
  
  // The length, in durations, of the CRC
  public static final int kDurationsPerCRC = 1;
}
