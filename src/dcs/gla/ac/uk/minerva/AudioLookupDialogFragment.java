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

public class AudioLookupDialogFragment extends DialogFragment implements OnClickListener  {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.getDialog().setTitle("Numbered Audio Search");
		View v=inflater.inflate(R.layout.lookup_layout, container);
		Button b=(Button) v.findViewById(R.id.goBtn);
		b.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goBtn:
			int r=Integer.parseInt(((EditText) getView().findViewById(R.id.numberInTxt)).getText().toString().trim());
			MainActivity m=(MainActivity) getActivity();
			r=m.getResources().getIdentifier("_"+r, "raw",
					m.getPackageName());
			int i=(m).setupMediaPlayer(r);
			if(i==1){
			m.mediaPlayer.start();
			}
		}
	}

}
