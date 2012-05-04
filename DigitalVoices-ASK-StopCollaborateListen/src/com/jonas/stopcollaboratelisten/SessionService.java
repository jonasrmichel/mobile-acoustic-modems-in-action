package com.jonas.stopcollaboratelisten;

/**
 * Copyright 2102 by the authors. All rights reserved.
 *
 * Author: Jonas Michel
 * 
 * This is the application's main service. This service maintains
 * the app's state machine and interacts with the playing and listening
 * threads accordingly.
 * 
 */

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class SessionService extends Service {
	private final IBinder mBinder = new SessionBinder();
	
	private MicrophoneListener microphoneListener = null;
	private StreamDecoder sDecoder = null;
	private ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();
	
	private SessionStatus mStatus;
	
	private Timer mTimer = null;
	private TimeoutTimerTask timeoutTask = null;

	private int sessionTimeout = -1;
	private int timeoutCounter = -1; 
	
	private String correctBroadcast = "";
	private boolean haveCorrectBroadcast = false;
	private boolean amInitiator = false;
	
	// possible status states of a collaborative sharing session
	public static enum SessionStatus {
		PLAYING, 
		LISTENING,
		HELPING, 
		SOS, 
		FINISHED,
		NONE
	}
	
	/**
     * Target we publish for StreamDecoder to send messages to
     */
    private IncomingHandler mHandler = new IncomingHandler();
    static final int MSG_RECEIVED_GOOD_BROADCAST = 0;
    static final int MSG_RECEIVED_BAD_BROADCAST = 1;
    static final int MSG_RECEIVED_SOS = 2;
    
    /**
     * Handler of incoming messages from StreamDecoder
     */
    private class IncomingHandler extends Handler {
    	@Override
        public void handleMessage(Message msg) {
    		switch (msg.what) {
            case (MSG_RECEIVED_GOOD_BROADCAST):
            	if (!haveCorrectBroadcast) {
            		correctBroadcast = new String(sDecoder.getReceivedBytes());
            		setTimeout(correctBroadcast, false);
            	}
        		haveCorrectBroadcast = true;
            	break;
            case (MSG_RECEIVED_BAD_BROADCAST):
            	if (haveCorrectBroadcast) {
            		resetTimeout(false);
            	} else {
            		long tSOS = playSOS(0);
    	    		
		    		if (tSOS > -1)
		    			// start listening when playing is finished
		    			mTimer.schedule(new StatusUpdateTimerTask(SessionStatus.LISTENING), tSOS);
            	}
            	break;
            case (MSG_RECEIVED_SOS):
            	if (haveCorrectBroadcast) {
            		resetTimeout(true);
            		
            		long tWait = (long) (Constants.kSetupJitter * Constants.kSamplesPerDuration / Constants.kSamplingFrequency * 1000);
            		long tHelp = playData(correctBroadcast, SessionStatus.HELPING, tWait) + tWait;
            		
            		if (tHelp > -1)
            			// start listening when playing is finished
            			mTimer.schedule(new StatusUpdateTimerTask(SessionStatus.LISTENING), tHelp);
            	}
            	break;
            default:
            	super.handleMessage(msg);
            }
    	}
    }

	public class SessionBinder extends Binder {
		SessionService getService() {
            return SessionService.this;
        }
    }
	
	@Override
    public void onCreate() {
		mStatus = SessionStatus.NONE;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	@Override
    public void onDestroy() {
		stopListening();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public SessionStatus getStatus() {
		return mStatus;
	}
	
	public String getListenString() {
		return correctBroadcast;
	}
	
	public String getBacklogStatus() {
		if (sDecoder == null)
			return "";
		else
			return sDecoder.getStatusString();
	}
	
	public void listen() {
		stopListening();

		mStatus = SessionStatus.LISTENING;
		
		decodedStream.reset();

		// the StreamDecoder uses the Decoder to decode samples put in its
		// AudioBuffer
		// StreamDecoder starts a thread
		sDecoder = new StreamDecoder(decodedStream, mHandler);

		// the MicrophoneListener feeds the microphone samples into the
		// AudioBuffer
		// MicrophoneListener starts a thread
		microphoneListener = new MicrophoneListener(sDecoder.getAudioBuffer());
		
		System.out.println("Listening");
	}

	 void stopListening() {
		if (microphoneListener != null)
			microphoneListener.quit();

		microphoneListener = null;

		if (sDecoder != null)
			sDecoder.quit();

		sDecoder = null;
		
		mStatus = SessionStatus.NONE;
	}

	public long playData(String input, SessionStatus playStatus, long delay) {
		stopListening();
		
		long millisPlayTime = -1;
		try {
			mStatus = playStatus;
			
			// try to play the file
			System.out.println("Performing " + input);
			byte[] inputBytes = input.getBytes();
			AudioUtils.performArray(inputBytes, delay);
			
			/**
			 *  length of play time (ms) = 
			 *  nDurations * samples/duration * 1/fs * 1000
			 */
			millisPlayTime = (long) ( (Constants.kPlayJitter + Constants.kDurationsPerHail + Constants.kBytesPerDuration * inputBytes.length + Constants.kDurationsPerCRC) *
					Constants.kSamplesPerDuration / Constants.kSamplingFrequency * 1000);
			
		}

		catch (Exception e) {
			System.out.println("Could not encode " + input + " because of " + e);
		}
		
		return millisPlayTime;
	}
	
	public long playSOS(long delay) {
		stopListening();
		
		long millisPlayTime = -1;
		try {
			mStatus = SessionStatus.SOS;
			
			// try to play the file
			AudioUtils.performSOS(delay);
			
			/**
			 *  length of play time (ms) = 
			 *  nDurations * samples/duration * 1/fs * 1000
			 */
			millisPlayTime = (long) ( (Constants.kPlayJitter + Constants.kDurationsPerSOS) *
					Constants.kSamplesPerDuration / Constants.kSamplingFrequency * 1000);
			
		}

		catch (Exception e) {
			System.out.println("Could not perform SOS because of " + e);
		}
		
		return millisPlayTime;
	}
	
	/* Start a collaborative sharing session */
	public void startSession(String input) {
		stopListening();
		
		// we are the session initiator, so we start with the "correct" broadcast
		correctBroadcast = input;
		haveCorrectBroadcast = true;
		amInitiator = true;
		
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		
		mTimer = new Timer();
		
		long tPlay = playData(correctBroadcast, SessionStatus.PLAYING, 0);
		
		if (tPlay > -1)
			// start listening when playing is finished
			mTimer.schedule(new StatusUpdateTimerTask(SessionStatus.LISTENING), tPlay);
		
		setTimeout(correctBroadcast, true);
	}
	
	public void sessionFinished() {
		stopListening();
		mTimer.cancel();
		mTimer = null;
		mStatus = SessionStatus.FINISHED;
	}
	
	public void sessionReset() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		
		mTimer = new Timer();
			
		correctBroadcast = "";
		haveCorrectBroadcast = false;
		amInitiator = false;
	}
	
	private void setTimeout(String input, boolean afterPlay) {
		int secondsPlay = (int) ( (Constants.kPlayJitter + Constants.kDurationsPerHail + Constants.kBytesPerDuration * input.length() + Constants.kDurationsPerCRC) 
				* Constants.kSamplesPerDuration / Constants.kSamplingFrequency );
		// set timeout to be 3x time to play the broadcast
		sessionTimeout = 3 * secondsPlay;
		timeoutCounter = sessionTimeout;
		timeoutTask = new TimeoutTimerTask();
		if (afterPlay)
			mTimer.schedule(timeoutTask, secondsPlay * 1000, 1000);
		else
			mTimer.schedule(timeoutTask, 0, 1000);
	}
	
	private void resetTimeout(boolean afterPlay) {
		if (afterPlay) {
			timeoutTask.cancel();
			timeoutTask = null;
			timeoutTask = new TimeoutTimerTask();
			timeoutCounter = sessionTimeout;
			mTimer.schedule(timeoutTask, sessionTimeout / 3 * 1000, 1000);
		} else
			timeoutCounter = sessionTimeout;
	}
	
	/**
	 *	TimerTask to schedule status updates
	 */
	private class StatusUpdateTimerTask extends TimerTask {
		SessionStatus newStatus_;
		
		public StatusUpdateTimerTask (SessionStatus newStatus) {
			newStatus_ = newStatus;
		}
		
		@Override
		public void run() {
			mStatus = newStatus_;
			
			if (mStatus == SessionStatus.LISTENING)
				listen();
			
			this.cancel();
		}			
	}
	
	/**
	 *	TimerTask to manage session timeout
	 */
	private class TimeoutTimerTask extends TimerTask {

		public void run() {
			if (--timeoutCounter == 0) {
				sessionFinished();
				this.cancel();
			}
		}
		
	}
	
}
