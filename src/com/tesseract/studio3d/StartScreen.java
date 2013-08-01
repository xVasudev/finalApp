package com.tesseract.studio3d;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class StartScreen extends Activity {

	float font_size=50;
	TextView logoView1,logoView2;
	Typeface logofont;
	String TAG="Studio 3D";
	private AnimationDrawable tesseractAnim;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Log.d(TAG,"window width ="+getWindowManager().getDefaultDisplay().getWidth()+" height ="+getWindowManager().getDefaultDisplay().getHeight());
		
		setContentView(R.layout.activity_start_screen);
		
		setupLogo();
		addTesseractLogo();
		
	}

	private void addTesseractLogo() {
		// TODO Auto-generated method stub
		
			  Runnable run = new Runnable() {
			        @Override
			        public void run() {
			        	tesseractAnim.start();
			        }
			    };

	        ImageView img = (ImageView)findViewById(R.id.logoAnimation);
	        tesseractAnim = (AnimationDrawable)img.getDrawable();
	        img.post(run);
	   
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_screen, menu);
		return true;
	}
	

	void setupLogo()
	{
		Typeface logofont = Typeface.createFromAsset(getAssets(),"fonts/loveloblack.otf");
        Typeface logofont2 = Typeface.createFromAsset(getAssets(),"fonts/rotos.ttf");
       
        logoView1 = (TextView) findViewById(R.id.logo1);
        logoView2 = (TextView) findViewById(R.id.logo2);
		
        logoView1.setTextColor(getResources().getColor(R.color.White));
		
		logoView1.setTextSize(font_size+10);
		logoView1.setText("STUDIO");
		logoView1.setTypeface(logofont);
		
		
		logoView2.setTextColor(getResources().getColor(R.color.White));
		logoView2.setTextSize(font_size);
		logoView2.setText(" 3D");
		logoView2.setTypeface(logofont2);

		
		
		
	}

}
