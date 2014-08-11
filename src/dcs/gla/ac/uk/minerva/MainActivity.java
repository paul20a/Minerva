package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity  implements OnClickListener{

	// private int CHECK_CODE = 0;
	// private TextToSpeech minervaTTS;
	public static final String ERROR_TAG="error";
	public ArrayList<POI> pList;
	Resources resources; 
	ViewPager vPager;
	static MediaPlayer mediaPlayer;
	AudioManager a;
	mFragmentStatePagerAdapter sPagerAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.point_pager);
		// retrieve information from intent
		Intent intent = this.getIntent();
		Bundle b=intent.getExtras();
		pList=b.getParcelableArrayList("pList");
		int Start=b.getInt("pos");
		sPagerAdapter = new mFragmentStatePagerAdapter(getSupportFragmentManager(),pList.size());
		vPager = (ViewPager) findViewById(R.id.point_pager);
		vPager.setAdapter(sPagerAdapter);
		vPager.setCurrentItem(Start);
		
		Button speakBtn = (Button) findViewById(R.id.play_btn);
		speakBtn.setOnClickListener(this);
		Button stopBtn = (Button) findViewById(R.id.stop_btn);
		stopBtn.setOnClickListener(this);
		
		vPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
				int rID =resources.getIdentifier(pList.get(position).getAudio(), "raw",getPackageName()); 
				mediaPlayer.reset();
				mediaPlayer=MediaPlayer.create(MainActivity.this, rID);
			}
		});
		
	} 
	
	
	@Override
	public void onStart() {
		// Intent checkTTSIntent = new Intent();
		// checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		// startActivityForResult(checkTTSIntent, CHECK_CODE);
		a = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		int rID =resources.getIdentifier(pList.get(vPager.getCurrentItem()).getAudio(), "raw",getPackageName()); 
		mediaPlayer = MediaPlayer.create(this, rID);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * private void audioDesc(String desc) { // speak minervaTTS.speak(desc,
	 * TextToSpeech.QUEUE_FLUSH, null); }
	 */

	/*
	 * @Override public void onInit(int status) { // set language if tts check
	 * status is successful if (status == TextToSpeech.SUCCESS) {
	 * minervaTTS.setLanguage(Locale.UK); } else { Toast.makeText(this,
	 * "Error, Text To Speech initialisation failed", Toast.LENGTH_LONG).show();
	 * } }
	 */

	/*
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
					AudioManager.STREAM_MUSIC,
					AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
			if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				try {
					mediaPlayer.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					Log.e(ERROR_TAG,"i'm broke",e);
				}
				
			}
			
			// onclick play tts
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
				a.abandonAudioFocus(afChangeListener);
				mediaPlayer.stop();
			}
		}
	};
}
