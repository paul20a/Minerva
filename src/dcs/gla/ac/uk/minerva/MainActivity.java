package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	// private int CHECK_CODE = 0;
	// private TextToSpeech minervaTTS;
	public static final String RES_PREFIX = "android.resource://";
	public static final String ERROR_TAG = "error";
	public ArrayList<POI> pList;
	Resources resources;
	ViewPager vPager;
	static MediaPlayer mediaPlayer;
	AudioManager a;
	mFragmentStatePagerAdapter sPagerAdapter;
	int StreamType=AudioManager.STREAM_VOICE_CALL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();
		setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
		setContentView(R.layout.point_pager);
		// retrieve information from intent
		Intent intent = this.getIntent();
		Bundle b = intent.getExtras();
		pList = b.getParcelableArrayList("pList");
		int Start = b.getInt("pos");

		// setup pager
		sPagerAdapter = new mFragmentStatePagerAdapter(
				getSupportFragmentManager(), pList.size());
		vPager = (ViewPager) findViewById(R.id.point_pager);
		vPager.setAdapter(sPagerAdapter);
		vPager.setCurrentItem(Start);

		// setup buttons
		Button speakBtn = (Button) findViewById(R.id.play_btn);
		speakBtn.setOnClickListener(this);
		Button stopBtn = (Button) findViewById(R.id.stop_btn);
		stopBtn.setOnClickListener(this);

		vPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// retrieve audio file id for current page
				setupMediaPlayerEarPiece(position);
			}
		});

	}

	@Override
	public void onStart() {
		// TEXT TO SPEECH CODE
		// Intent checkTTSIntent = new Intent();
		// checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		// startActivityForResult(checkTTSIntent, CHECK_CODE);
		mediaPlayer=new MediaPlayer();
		a = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		setupMediaPlayerEarPiece(vPager.getCurrentItem());
		super.onStart();
	}

	private void setupMediaPlayerEarPiece(int i) {
		int rID = resources.getIdentifier(pList.get(i).getAudio(), "raw",
				getPackageName());
		// reset and create media player
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(
					this,
					Uri.parse(RES_PREFIX
							+ resources.getResourcePackageName(rID) + "/"
							+ resources.getResourceTypeName(rID) + "/"
							+ rID));
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | NotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupMediaPlayerSpeaker(int i) {
		mediaPlayer.reset();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		int rID = resources.getIdentifier(pList.get(i).getAudio(), "raw",
				getPackageName());
		mediaPlayer=MediaPlayer.create(MainActivity.this, rID);

	}
	@Override
	protected void onStop() {
		// TEXT TO SPEECH CODE
		// kill TTS ondestroy to avoid leak
		// if (minervaTTS != null) {
		// minervaTTS.stop();
		// minervaTTS.shutdown();
		// }
		mediaPlayer.release();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.speaker_settings) {
	        // Display the fragment as the main content.
		    item.setEnabled(false);
		    MenuItem i=(MenuItem) findViewById(R.id.earpiece_settings);
		    i.setEnabled(true);
		    setupMediaPlayerSpeaker(vPager.getCurrentItem());
		    StreamType=AudioManager.STREAM_MUSIC;
		}
		if (id == R.id.earpiece_settings) {
	        // Display the fragment as the main content.
		    item.setEnabled(false);
		    MenuItem i=(MenuItem) findViewById(R.id.speaker_settings);
		    i.setEnabled(true); 
		    setupMediaPlayerEarPiece(vPager.getCurrentItem()); 
		    StreamType=AudioManager.STREAM_VOICE_CALL;
		}
		return super.onOptionsItemSelected(item);
	}

	// TEXT TO SPEECH CODE
	/*
	 * private void audioDesc(String desc) { // speak minervaTTS.speak(desc,
	 * TextToSpeech.QUEUE_FLUSH, null); }
	 * 
	 * @Override public void onInit(int status) { // set language if tts check
	 * status is successful if (status == TextToSpeech.SUCCESS) {
	 * minervaTTS.setLanguage(Locale.UK); } else { Toast.makeText(this,
	 * "Error, Text To Speech initialisation failed", Toast.LENGTH_LONG).show();
	 * } }
	 * 
	 * 
	 * protected void onActivityResult(int requestCode, int resultCode, Intent
	 * data) { super.onActivityResult(requestCode, resultCode, data); // If tts
	 * passed check if (requestCode == CHECK_CODE) { // if correct language and
	 * no instance created initialise if ((resultCode ==
	 * TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) && minervaTTS == null) {
	 * minervaTTS = new TextToSpeech(this, this); // install if tts not present
	 * } else if (minervaTTS == null) { Intent installTTSIntent = new Intent();
	 * installTTSIntent .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	 * startActivity(installTTSIntent); } } }
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_btn:
			// Request audio focus for playback
			
			int result = a.requestAudioFocus(afChangeListener,
					StreamType,
					AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
			if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				try {
					mediaPlayer.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					Log.e(ERROR_TAG, "i'm broke", e);
				}
			}
			// TEXT TO SPEECH CODE
			/*
			 * if (minervaTTS != null) { TextView text = (TextView)
			 * findViewById(R.id.textViewDesc); String desc =
			 * text.getText().toString(); audioDesc(desc); }
			 */
			break;
			
		case R.id.stop_btn:
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TEXT TO SPEECH CODE
			/*
			 * if (minervaTTS != null) { minervaTTS.stop(); }
			 */
		}

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
