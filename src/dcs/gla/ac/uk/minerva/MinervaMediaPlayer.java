package dcs.gla.ac.uk.minerva;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;

/**
 * MediaPlayer control class to allow the MediaPlayer to be used easily over
 * different activities and fragments
 * 
 * @author Paul Cairney
 */
public class MinervaMediaPlayer implements OnAudioFocusChangeListener {
	public static final String RES_PREFIX = "android.resource://";
	public MediaPlayer mediaPlayer = new MediaPlayer();
	private AudioManager a;
	private int streamType;
	private Activity context;

	/**
	 * 
	 * Parameterised constructor
	 * 
	 * @param context - Actiity context
	 */
	public MinervaMediaPlayer(Activity context) {
		this.context = context;
		SharedPreferences settings = context
				.getPreferences(ActivityMain.MODE_PRIVATE);
		streamType = settings
				.getInt("audioOut", AudioManager.STREAM_VOICE_CALL);
		context.setVolumeControlStream(streamType);
		a = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * @param i - resource identifier
	 * @return - true if successful.
	 */
	public boolean setupMediaPlayer(int i) {
		// reset and create media player
		if (mediaPlayer != null)
			mediaPlayer.reset();
		// try to initialise media using audio file
		try {
			mediaPlayer.setDataSource(
					context,
					Uri.parse(RES_PREFIX
							+ context.getResources().getResourcePackageName(i)
							+ "/"
							+ context.getResources().getResourceTypeName(i)
							+ "/" + i));
			mediaPlayer.setAudioStreamType(streamType);
			mediaPlayer.prepare();

		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | NotFoundException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * begin playing audio
	 */
	public void play() {
		int result = a.requestAudioFocus(this, streamType,
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			try {
				mediaPlayer.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	/**
	 * pause audio
	 */
	public void pause() {
		mediaPlayer.pause();
	}

	/* (non-Javadoc)
	 * @see android.media.AudioManager.OnAudioFocusChangeListener#onAudioFocusChange(int)
	 */
	@Override
	public void onAudioFocusChange(int focusChange) {
		if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			mediaPlayer.pause();
		} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			mediaPlayer.start();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			mediaPlayer.stop();
			a.abandonAudioFocus(this);
		}
	}

	/**
	 * move back to start of track
	 */
	public void restart() {
		boolean playing = mediaPlayer.isPlaying();
		mediaPlayer.seekTo(0);
		if (playing) {
			mediaPlayer.start();
		}
	}

	/**
	 * release mediaPlayer resources
	 */
	public void release() {
		mediaPlayer.release();
		mediaPlayer = null;
	}

	/**
	 * @param s - name of audio file
	 * @return associated resource identifier
	 */
	public int getAudioFile(String s) {
		// get audio file
		return context.getResources().getIdentifier(s, "raw",
				context.getPackageName());
	}

	/**
	 * @param s name of audio file
	 */
	public void continuePlayingOnChange(String s) {
		boolean playing = mediaPlayer.isPlaying();
		int t = mediaPlayer.getCurrentPosition();
		setupMediaPlayer(getAudioFile(s));
		mediaPlayer.seekTo(t);
		if (playing) {
			mediaPlayer.start();
		}
		context.setVolumeControlStream(streamType);
	}

	/**
	 * 
	 * change current stream type
	 * 
	 * @return String for options menu
	 */
	public String changeStreamType() {
		String s = "";
		if (streamType == AudioManager.STREAM_VOICE_CALL) {
			streamType = AudioManager.STREAM_MUSIC;
			s = "Speaker";
		} else {
			streamType = AudioManager.STREAM_VOICE_CALL;
			s = "Earpiece";
		}
		savePref();
		return s;
	}

	/**
	 * 
	 * setup default stream type
	 * 
	 * @return String for options menu
	 */
	public String initialiseStreamType() {
		if (streamType == AudioManager.STREAM_MUSIC) {
			return "Speaker";
		} else {
			return "Earpiece";
		}
	}

	/**
	 * save stream type to preferences
	 */
	public void savePref() {
		SharedPreferences settings = context
				.getPreferences(ActivityMain.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("audioOut", streamType);
		editor.commit();
	}
}
