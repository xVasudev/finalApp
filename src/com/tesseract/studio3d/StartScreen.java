package com.tesseract.studio3d;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
	private ImageView clickButton;
	CustomFileObserver fileObserver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Log.d(TAG,"window width ="+getWindowManager().getDefaultDisplay().getWidth()+" height ="+getWindowManager().getDefaultDisplay().getHeight());
		
		setContentView(R.layout.activity_start_screen);
		
		// Initialize the Studio 3D Directory if it doesnt exist ..
		
		File sdCardDirectory = Environment.getExternalStorageDirectory();
        File mainDir = new File (sdCardDirectory.getAbsolutePath()+"/Studio3D");
        File layersDir = new File (sdCardDirectory.getAbsolutePath()+"/Studio3D/Layers");
        
        Log.d(TAG, "directories created");
        
        if(!mainDir.exists())
        	mainDir.mkdirs();
		
        if(!layersDir.exists())
			layersDir.mkdirs();
        
		fileObserver=new CustomFileObserver(getBaseContext());
		
		setupLogo();
		addTesseractLogo();
		assignOnClickListeners();
		
		
		
	}

	private void assignOnClickListeners() {
		// TODO Auto-generated method stub
		
		
		clickButton=(ImageView)findViewById(R.id.cameraView);
				
		clickButton.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	String packageName = "com.android.camera"; //Or whatever package should be launched

	        	if(packageName.equals("com.android.camera")){ //Camera
	        	    try
	        	    {
	        	       
	        	    	Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.camera");

	        	    	intent.putExtra("android.intent.extras.CAMERA_FACING", 2);
	        	    	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        	        //Log.d("Test","num"+Camera.getNumberOfCameras());
	        	        startActivity(intent);

	        	    }
	        	    catch(ActivityNotFoundException e){
	        	        Intent intent = new Intent();
	        	        ComponentName comp = new ComponentName("com.android.camera", "com.android.camera.CameraEntry");
	        	        intent.setComponent(comp);
	        	        startActivity(intent);
	        	    }
	        	}
	        	else{ //Any other
	        	    Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
	        	    startActivity(intent);
	        	}
	        }
	    });
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
