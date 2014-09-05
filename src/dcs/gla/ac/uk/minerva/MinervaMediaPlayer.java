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

public class MinervaMediaPlayer implements OnAudioFocusChangeListener {
	public static final String RES_PREFIX = "android.resource://";
	public static MediaPlayer mediaPlayer=new MediaPlayer();;
	private AudioManager a;
	private int streamType;
	private Activity context;

	public MinervaMediaPlayer(Activity context) {
		this.context = context;
		SharedPreferences settings = context.getPreferences(ActivityMain.MODE_PRIVATE);
		streamType = settings
				.getInt("audioOut", AudioManager.STREAM_VOICE_CALL);
		context.setVolumeControlStream(streamType);		
		a = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * @param i
	 * @return
	 */
	public int setupMediaPlayer(int i) {
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
			mediaPlayer.setScreenOnWhilePlaying(true);
			mediaPlayer.prepare();

		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | NotFoundException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
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

	public void pause() {
		mediaPlayer.pause();
	}

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


	public void restart() {
		boolean playing = mediaPlayer.isPlaying();
		mediaPlayer.seekTo(0);
		if (playing) {
			mediaPlayer.start();
		}
	}

	public void release() {
		mediaPlayer.release();
		mediaPlayer = null;
	}

	/**
	 * @param i
	 * @return
	 */
	public int getAudioFile(String s) {
		// get audio file
		return context.getResources().getIdentifier(s, "raw",
				context.getPackageName());
	}

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

	public String changeStreamType() {
		if (streamType == AudioManager.STREAM_VOICE_CALL) {
			streamType = AudioManager.STREAM_MUSIC;
			return "Speaker";
		} else {
			streamType = AudioManager.STREAM_VOICE_CALL;
			return "Earpiece";
		}
	}

	public String initialiseStreamType() {
		if (streamType == AudioManager.STREAM_MUSIC) {
			return "Speaker";
		} else {
			return "Earpiece";
		}
	}

	public void savePref() {
		SharedPreferences settings = context.getPreferences(ActivityMain.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("audioOut", streamType);
		editor.commit();
	}
}
