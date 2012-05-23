package com.fresko;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class Retriver extends Activity {
	Connector connector;
	WorkSurface surface;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        connector = new Connector(Connector.DEFAULT_SERVICE_URL);
		Bitmap[][] array = null;
        try {
			array = connector.connectAsArrayOfImages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("Fresko", e);
			e.printStackTrace();
		}
        setContentView(R.layout.main);
        surface = (WorkSurface)findViewById(R.id.workTable);
        surface.setChunks(array);
        Button buttonLoad = (Button)findViewById(R.id.buttonLoad);
        Button buttonSend = (Button)findViewById(R.id.buttonSend);
        Log.w("Fresko", "End of onCreate");
    }
}
