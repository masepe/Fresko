package com.fresko;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Retriver extends Activity {
	Connector connector;
	WorkSurface surface;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		connector = new Connector(Connector.DEFAULT_SERVICE_URL);

		setContentView(R.layout.main);
		surface = (WorkSurface) findViewById(R.id.workTable);

		final Button buttonLoad = (Button) findViewById(R.id.buttonLoad);
		buttonLoad.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (surface.getChunks() == null) {
					Bitmap[][] array = null;
					try {
						array = connector.connectAsArrayOfImages();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.w("Fresko", e);
						e.printStackTrace();
					}
					surface.setChunks(array);
				}
			}
		});
		Button buttonSend = (Button) findViewById(R.id.buttonSend);
		Log.w("Fresko", "End of onCreate");
	}
}
