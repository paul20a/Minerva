package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
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
	protected MinervaMediaPlayer player;
	private MinervaFragmentStatePagerAdapter sPagerAdapter;
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
					player.setupMediaPlayer(player.getAudioFile(pList.get(position).getAudio()));
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
		player = new MinervaMediaPlayer(this);
		boolean audioPresent=checkAudio(i);
		if (audioPresent) {
			setMediaButtonsEnabled(audioPresent);
			player.setupMediaPlayer(player.getAudioFile(pList.get(i).getAudio()));
		}
		super.onStart();
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
		player.savePref();
		player.release();
		// update preferences to store audio output

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.findItem(R.id.audio_settings);
		item.setTitle(player.initialiseStreamType());
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
				item.setTitle(player.changeStreamType());
			//check if audio is applicable
			if (checkAudio(i)) {
				//setup media player to same state user had before using different output
				player.continuePlayingOnChange(pList.get(i).getAudio());
			}
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
			player.play();
			break;
		case R.id.pause_btn:
			player.pause();
			break;
		case R.id.replay_btn:
		    player.restart();
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// check audio state to continue
		outState.putBoolean("isPlaying", MinervaMediaPlayer.mediaPlayer.isPlaying());
		outState.putInt("progress",  MinervaMediaPlayer.mediaPlayer.getCurrentPosition());
		super.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		savedInstanceState.getBoolean("isPlaying");
		 MinervaMediaPlayer.mediaPlayer.seekTo(savedInstanceState.getInt("progress"));
		if (savedInstanceState.getBoolean("isPlaying")) {
			 player.play();
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
}
