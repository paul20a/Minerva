package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 * Activity that displays a Point on screen using an instance of FragmentViewPoint 
 * 
 * @author Paul
 *
 */
public class ActivityMain extends ActionBarActivity implements OnClickListener {
	public static final String RES_PREFIX = "android.resource://";
	private ArrayList<Waypoint> pList;
	private Resources resources;
	private ViewPager vPager;
	protected MediaPlayer mediaPlayer;
	private AudioManager a;
	private MinervaFragmentStatePagerAdapter sPagerAdapter;
	private int streamType;

	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		BitmapProcessor.EmptyBitmapCache();
		resources = getResources();
		// included to set logo to relevant icon
		getActionBar()
				.setIcon(
						resources.getIdentifier("logo", "raw",
								"dcs.gla.ac.uk.minerva"));
		// allow up nav
		getActionBar().setHomeButtonEnabled(true);

		setContentView(R.layout.point_pager);
		// retrieve information from intent
		Bundle b = this.getIntent().getExtras();
		pList = b.getParcelableArrayList("pList");
		int Start = b.getInt("pos");
		// setup pager and adapter
		sPagerAdapter = new MinervaFragmentStatePagerAdapter(
				getSupportFragmentManager(), pList);
		vPager = (ViewPager) findViewById(R.id.point_pager);
		vPager.setAdapter(sPagerAdapter);
		vPager.setCurrentItem(Start);

		// setup buttons
		ImageButton speakBtn = (ImageButton) findViewById(R.id.play_btn);
		speakBtn.setOnClickListener(this);
		ImageButton pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
		pauseBtn.setOnClickListener(this);
		ImageButton replayBtn = (ImageButton) findViewById(R.id.replay_btn);
		replayBtn.setOnClickListener(this);
		vPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// check audio file id for current page
				boolean audioPresent=checkAudio(vPager.getCurrentItem());
				if (audioPresent) {
					setMediaButtonsEnabled(audioPresent);
					setupMediaPlayer(getAudioFile(position));
				}

			}
		});

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	public void onStart() {
		// check if audio file is available
		int i = vPager.getCurrentItem();
		checkAudio(i);
		// get audio output method from shared preferences
		SharedPreferences settings = getPreferences(ActivityMain.MODE_PRIVATE);
		streamType = settings
				.getInt("audioOut", AudioManager.STREAM_VOICE_CALL);
		mediaPlayer = new MediaPlayer();
		setVolumeControlStream(streamType);
		a = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		boolean audioPresent=checkAudio(i);
		if (audioPresent) {
			setMediaButtonsEnabled(audioPresent);
			setupMediaPlayer(getAudioFile(i));
		}
		super.onStart();
	}

	/**
	 * @param i
	 * @return
	 */
	private int getAudioFile(int i){
		// get audio file
		return resources.getIdentifier(pList.get(i).getAudio(), "raw",
				getPackageName());
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
					this,
					Uri.parse(RES_PREFIX
							+ resources.getResourcePackageName(i) + "/"
							+ resources.getResourceTypeName(i) + "/" + i));
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

	private boolean checkAudio(int i) {
		if (pList.get(i).getAudio() != null) {
			return true;
		}
		return false;
	}

	private void setMediaButtonsEnabled(boolean enable){
		ImageButton a = (ImageButton) this.findViewById(R.id.play_btn);
		ImageButton b = (ImageButton) this.findViewById(R.id.pause_btn);
		ImageButton c = (ImageButton) this.findViewById(R.id.replay_btn);
		
		a.setEnabled(enable);
		b.setEnabled(enable);
		c.setEnabled(enable);
	}
	
	@Override
	protected void onStop(){
		// release the mediaPlayer
		super.onStop();
		mediaPlayer.release();
		mediaPlayer = null;
		// update preferences to store audio output
		SharedPreferences settings = getPreferences(ActivityMain.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("audioOut", streamType);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.findItem(R.id.audio_settings);
		if (streamType == AudioManager.STREAM_MUSIC) {
			item.setTitle("Speaker");
		} else if (streamType == AudioManager.STREAM_VOICE_CALL) {
			item.setTitle("Earpiece");
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int i = vPager.getCurrentItem();
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			break;
			case R.id.audio_file_search:
				FragmentManager m= getFragmentManager();
				FragmentDialogAudioLookup dialog=new FragmentDialogAudioLookup();
				dialog.show(m, "Audio Playback");
				break;
		case R.id.audio_settings:
			if (streamType == AudioManager.STREAM_VOICE_CALL) {
				streamType = AudioManager.STREAM_MUSIC;
				item.setTitle("Speaker");
			} else if (streamType == AudioManager.STREAM_MUSIC) {
				streamType = AudioManager.STREAM_VOICE_CALL;
				item.setTitle("Earpiece");
			}
			//check if audio is applicable
			if (checkAudio(i)) {
				//setup media player to same state user had before using different output
				boolean playing = mediaPlayer.isPlaying();
				int t = mediaPlayer.getCurrentPosition();
				setupMediaPlayer(getAudioFile(i));
				mediaPlayer.seekTo(t);
				if (playing) {
					mediaPlayer.start();
				}
			}
			setVolumeControlStream(streamType);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_btn:
			// Request audio focus for playback
			int result = a.requestAudioFocus(afChangeListener, streamType,
					AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
			if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				try {
					mediaPlayer.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
				}
			}
			break;

		case R.id.pause_btn:
			mediaPlayer.pause();
			break;
		case R.id.replay_btn:
			mediaPlayer.seekTo(0);
			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
			}
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// check audio state to continue
		outState.putBoolean("isPlaying", mediaPlayer.isPlaying());
		outState.putInt("progress", mediaPlayer.getCurrentPosition());
		super.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		savedInstanceState.getBoolean("isPlaying");
		mediaPlayer.seekTo(savedInstanceState.getInt("progress"));
		if (savedInstanceState.getBoolean("isPlaying")) {
			mediaPlayer.start();
		}
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				mediaPlayer.pause();
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				mediaPlayer.start();
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				mediaPlayer.stop();
				a.abandonAudioFocus(afChangeListener);
			}
		}
	};
}
