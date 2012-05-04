package com.jonas.stopcollaboratelisten;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class PlayThread extends Thread {

	private byte[] buffer;
	private long delay = 0;
	
	public PlayThread( byte[] b, long d )
	{
		buffer = new byte[b.length * 2];
		delay = d;
		
		// convert from 8 bit per sample to little-endian 16 bit per sample, IOW 16-bit PCM
		int i, j;
		for(i=0, j =0; i < b.length; i++, j += 2)
		{
		    buffer[j] = 0;
		    buffer[j+1] = b[i];
		}
		
		start();
	}
	
	public void run()
	{
		// delay play if necessary
		while (delay > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			delay -= 1000;
		}
		
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
	  
	    AudioTrack atrack = new AudioTrack(AudioManager.STREAM_MUSIC,
	    							(int) Encoder.kSamplingFrequency,
	                                AudioFormat.CHANNEL_CONFIGURATION_MONO,
	                                AudioFormat.ENCODING_PCM_16BIT,  // ENCODING_PCM_8BIT sounds very scratchy, so we use 16 bit and double up the data
	                                buffer.length, 
	                                AudioTrack.MODE_STATIC);
        try {
        	atrack.write(buffer, 0, buffer.length);
        } catch (Exception e) {
                e.printStackTrace();
        }
        
        atrack.play();
	}
	
}
