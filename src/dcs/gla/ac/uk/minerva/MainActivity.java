package dcs.gla.ac.uk.minerva;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		OnInitListener {
	private int CHECK_CODE = 0;
	private TextToSpeech minervaTTS;
	private String title;
	private String description;
	private MapView myOpenMapView;
	private MapController myMapController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		readFile();
		
        myOpenMapView = (MapView)findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(4);
		
		TextView titleTextView=(TextView) findViewById(R.id.titleTextView);
		TextView descriptionTextView=(TextView) findViewById(R.id.textViewDesc);
		
		Button speakButton = (Button) findViewById(R.id.play_btn);
		speakButton.setOnClickListener(this);
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, CHECK_CODE);
		
		titleTextView.setText(title);
		descriptionTextView.setText(description);
	}
	
	@Override
	protected void onDestroy() {
        // 
        if (minervaTTS != null) {
        	minervaTTS.stop();
        	minervaTTS.shutdown();
        }
        super.onDestroy();
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

	@Override
	public void onClick(View v) {
		TextView text = (TextView) findViewById(R.id.textViewDesc);
		String desc = text.getText().toString();
		audioDesc(desc);
	}

	private void audioDesc(String desc) {
		minervaTTS.speak(desc, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			minervaTTS.setLanguage(Locale.UK);
		} else {
			Toast.makeText(this, "Error, Text To Speech initialisation failed",
					Toast.LENGTH_LONG).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				minervaTTS = new TextToSpeech(this, this);
			} else {
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
	}

	private void readFile() {
		AssetManager am = this.getAssets();
		BufferedReader buf;
		title = "";
		description = "";
		try {
			InputStream inputStream = am.open("Info.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			buf = new BufferedReader(inputStreamReader);
			String temp;

			if ((temp = buf.readLine()) != null) {
				title = temp;
			}
			while ((temp = buf.readLine()) != null) {
				description = description+temp;
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
