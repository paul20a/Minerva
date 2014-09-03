package dcs.gla.ac.uk.minerva;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AudioLookupDialogFragment extends DialogFragment implements
		OnClickListener {

	ImageButton btnPlay;
	ImageButton btnPause;
	ImageButton btnStop;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.getDialog().setTitle("Numbered Audio Search");
		View v = inflater.inflate(R.layout.lookup_layout, container);
		Button btnGo = (Button) v.findViewById(R.id.goBtn);
		btnPlay = (ImageButton) v.findViewById(R.id.play_btn);
		btnPause = (ImageButton) v.findViewById(R.id.pause_btn);
		btnStop = (ImageButton) v.findViewById(R.id.replay_btn);
		btnGo.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnPlay.setEnabled(false);
		btnPause.setEnabled(false);
		btnStop.setEnabled(false);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goBtn:
			searchAudioFiles();
			break;
		}
	}

	private void searchAudioFiles() {
		int r = Integer.parseInt(((EditText) getView().findViewById(
				R.id.numberInTxt)).getText().toString().trim());
		MainActivity m = (MainActivity) getActivity();
		r = m.getResources().getIdentifier("_" + r, "raw", m.getPackageName());
		int i = m.setupMediaPlayer(r);
		if (i == 1) {
			btnPlay.setEnabled(true);
			btnPause.setEnabled(true);
			btnStop.setEnabled(true);
			m.mediaPlayer.start();
		} else {
			Toast.makeText(getActivity(), "Audio file not found",
					Toast.LENGTH_SHORT).show();
		}
	}
}
