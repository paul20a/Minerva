package dcs.gla.ac.uk.minerva;

import java.util.Locale;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		OnInitListener {
	private int CHECK_CODE = 0;
	private TextToSpeech minervaTTS;
	private String title;
	private String description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// retrieve information from intent
		Intent intent = this.getIntent();
		title = intent.getStringExtra(SelectActivity.NAME);
		description = intent.getStringExtra(SelectActivity.DESCRIPTION);
		// locate widgets
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		TextView descriptionTextView = (TextView) findViewById(R.id.textViewDesc);
		// set-up TTS
		// THIS MAY NOT BE REQUIRED ANYOMRE REPLACE WITH MP3????
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, CHECK_CODE);
		
		Button speakBtn = (Button) findViewById(R.id.play_btn);
		speakBtn.setOnClickListener(this);
		Button stopBtn = (Button) findViewById(R.id.stop_btn);
		stopBtn.setOnClickListener(this);
		
		// Set fields
		titleTextView.setText(title);
		descriptionTextView.setText(description);
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		Drawable marker = this.getResources().getDrawable(
				R.drawable.chinese_hillside);
		imageView.setImageDrawable(marker);

	}

	@Override
	protected void onDestroy() {
		// kill TTS ondestroy to avoid leak
		if (minervaTTS != null) {
			minervaTTS.stop();
			minervaTTS.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void audioDesc(String desc) {
		// speak
		minervaTTS.speak(desc, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		// set language if tts check status is successful
		if (status == TextToSpeech.SUCCESS) {
			minervaTTS.setLanguage(Locale.UK);
		} else {
			Toast.makeText(this, "Error, Text To Speech initialisation failed",
					Toast.LENGTH_LONG).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If tts passed check
		if (requestCode == CHECK_CODE) {
			// if correct language and no instance created initialise
			if ((resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
					&& minervaTTS == null) {
				minervaTTS = new TextToSpeech(this, this);
				// install if tts not present
			} else if (minervaTTS == null) {
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_btn:
			// onclick play tts
			if (minervaTTS != null) {
			TextView text = (TextView) findViewById(R.id.textViewDesc);
			String desc = text.getText().toString();
			audioDesc(desc);
			}
			break;
		case R.id.stop_btn:
			if (minervaTTS != null) {
				minervaTTS.stop();
			}
		}
	}

}
