package com.fresko;

import java.io.IOException;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.util.Log;

public class Retriver extends Activity {
	Connector connector;
	WorkSurface surface;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        connector = new Connector(Connector.DEFAULT_SERVICE_URL);
        try {
			Drawable[][] array = connector.connectAsArrayOfImages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("Fresko", e);
			e.printStackTrace();
		}
        surface = new WorkSurface(this);
        setContentView(surface);
        Log.w("Fresko", "End of onCreate");
    }
}
