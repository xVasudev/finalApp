package com.tesseract.studio3d.Animation;

import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tesseract.studio3d.R;

public class AnimationActivity extends Activity {

	Vector<ImageView> mimageViews;
	Vector<ImageView> layerViews;
	Vector<ImageView> CanvasImageViews;
	Vector<AnimationSet> mAnimations;
	Vector<Bitmap> layerBitmaps;
	Vector<int[]> sizes;
	RelativeLayout activityLayout,fullScreenLayout;
	File seperatedLayersFolder;
	String TAG = "AnimationActivity";

	int currentSelectedLayer = 0;
	private Animation animFadeIn;

	// This has the different layers ,Each layer has an arraylist of different
	// images ..
	String[] imageFilters = { "sepia", "stark", "sunnyside", "cool", "worn",
			"grayscale","vignette","crush","sunny","night" };


	//Vector<ArrayList<ImageToLoad>> Layers;
	LinearLayout layersLayout, filtersLayout;

	ScrollView layersScroll;

	private static HorizontalScrollView hs;

	Vector<Vector<ImageView>> filteredViews;
	boolean isStarted=false;


	double scaleValue=0.16;

	ImageButton next,focus,Replace,reset,full_screen,back;


	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		activityLayout = new RelativeLayout(this);
		activityLayout.setBackgroundColor(Color.BLACK);
		activityLayout.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.animationactivity));

		
		fullScreenLayout = new RelativeLayout(this);
		
		mimageViews = new Vector<ImageView>();

		CanvasImageViews = new Vector<ImageView>();
		layerBitmaps = new Vector<Bitmap>();

		sizes = new Vector<int[]>();

		filteredViews = new Vector<Vector<ImageView>>();


		// Create that many ImageViews as much as there are in the
		// Tesseract/Layers
		seperatedLayersFolder = new File(
				Environment.getExternalStorageDirectory()
				+ "/Studio3D/Layers/");

		System.gc();
		LoadFiles(seperatedLayersFolder);

		hs = new HorizontalScrollView(this);

		// initializeHorizontalScroller();
		initializenewHorizontallScrollView();
		animFadeIn=AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);

		initializeVerticalScroller();

		// setContentView(R.layout.timepasslayout);
		setContentView(activityLayout);

	}
	protected void onRestart() {
	    super.onRestart();  // Always call the superclass method first
	    
	    // Activity being restarted from stopped state    
	}
	
	protected void onStart() {
	    super.onStart();  // Always call the superclass method first
	}

	private void initializenewHorizontallScrollView() {

		filtersLayout = new LinearLayout(this);

		LinearLayout.LayoutParams llayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// llayoutParams.setMargins(150,300,0,0);

		filtersLayout.setLayoutParams(llayoutParams);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		// layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
		layoutParams.setMargins(185, 0, 0, 0);

		// scroller.setLayoutParams(layoutParams);

		hs.setLayoutParams(layoutParams);

		File filtersFolder = new File(Environment.getExternalStorageDirectory()
				+ "/Studio3D/Layers/Filters/");
		File[] files = filtersFolder.listFiles();

		Log.d(TAG, "Number of filtered folders :" + files.length);
		// int count=0;


		LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		imgViewParams.setMargins(5, 0, 0, 0);

		for (int count = 0; count < files.length; count++) {
			Vector<ImageView> images = new Vector<ImageView>();

			File layerFolder = new File(
					Environment.getExternalStorageDirectory()
					+ "/Studio3D/Layers/Filters/"
					+ String.valueOf(count) + "/");
			Log.d(TAG, "path" + layerFolder.getAbsolutePath());


			File[] layerFiles = layerFolder.listFiles();

			for (int filternameindex = 0; filternameindex < layerFiles.length; filternameindex++) {
				ImageView temp = new ImageView(this);
				temp.setImageBitmap(BitmapFactory.decodeFile(layerFolder
						.getAbsolutePath()
						+ "/"
						+ imageFilters[filternameindex] + ".png"));

				temp.setId(2000 + count * imageFilters.length + filternameindex);
				temp.setOnClickListener(filtersLayerClickListener);

				temp.setImageBitmap(  addBorder(  ((BitmapDrawable)temp.getDrawable()).getBitmap(),2,Color.WHITE));

				if(filternameindex!=0)
					temp.setLayoutParams(imgViewParams);

				images.add(temp);

				if (count == 0)
					filtersLayout.addView(temp);

			}

			filteredViews.add(images);

		}

		hs.addView(filtersLayout);
		activityLayout.addView(hs);

		hs.setVisibility(View.INVISIBLE);
		System.gc();

		// TODO Auto-generated method stub

	}

	private Bitmap addBorder(Bitmap bmp, int borderSize,int borderColor) {

		Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

		Canvas canvas = new Canvas(bmpWithBorder);
		Paint bp= new Paint();
		bp.setColor(borderColor);//set a color
		bp.setStrokeWidth(borderSize);// set your stroke width
		// w and h are width and height of your imageview
		int w=bmp.getWidth(),h=bmp.getHeight();

		canvas.drawBitmap(bmp, 0, 0, new Paint());

		canvas.drawLine(0, 0, w,0,bp);
		canvas.drawLine(0, 0,0, h,bp);
		canvas.drawLine(w,h,w,0,bp);
		canvas.drawLine(w, h,0,h, bp);

		//   canvas.drawBitmap(bmp, rect, rect, paint);

		return bmpWithBorder;
	}

	private void initializeVerticalScroller() {
		layersLayout = new LinearLayout(this);

		LinearLayout.LayoutParams llayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layersLayout.setLayoutParams(llayoutParams);
		layersLayout.setOrientation(LinearLayout.VERTICAL);
		// layersLayout.setPadding(25,120,0,0);

		layersScroll = new ScrollView(this);

		layersScroll.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		layersScroll.setPadding(12, 130, 0, 0);
		// layersLayout.setSc

		layersScroll.addView(layersLayout);
		activityLayout.addView(layersScroll);

	}



	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// Toast.makeText(AnimationActivity.this, "Position "+position,
			// Toast.LENGTH_SHORT).show();

			applyFiltertoView(position);

		}
	};

	private void startAnimation() {

		int[] loc = new int[2];
		float xPos, yPos;

		// This will make the small layer icons ...

		for (int i = 0; i < mimageViews.size(); i++) {

			AnimationSet tempAnimation = new AnimationSet(true);

			mimageViews.get(i).getLocationOnScreen(loc);

			/*
			 * How is this evaluated,
			 * 
			 * The layers are going to be displayed from 25,150 onwards .Then
			 * after every image,we leave a space of 2.5*height(). So animation
			 */

			xPos = (-this.getWindowManager().getDefaultDisplay().getWidth() / 2)
					+ (float) 
					scaleValue * mimageViews.get(i).getWidth() / 2 + 25; // From

			yPos = (-this.getWindowManager().getDefaultDisplay().getHeight() / 2)
					+ (float) (150 + (0.5 + 2.5 )* i
							* (
									scaleValue * mimageViews.get(i).getHeight()) / 2);

			Log.d(TAG,
					"position " + 150 + (0.5 + i)
					* (
							scaleValue * mimageViews.get(i).getHeight()) / 2);
			// TranslateAnimation anim = new TranslateAnimation( 0,(float)
			// (mimageViews.get(i).getTop()-25) , 0,
			// 0+mimageViews.get(i).getLeft()+100*i );

			TranslateAnimation anim = new TranslateAnimation(0, xPos, 0, yPos);
			// anim.setDuration(1000);
			Log.d(TAG, "location - " + loc[0] + "  " + loc[1]);
			anim.setFillAfter(true);
			anim.setFillEnabled(true);

			ScaleAnimation scaleanimation = new ScaleAnimation(1, (float) 
					scaleValue,
					1, (float) 
					scaleValue, Animation.RELATIVE_TO_SELF, (float) 0.5,
					Animation.RELATIVE_TO_SELF, (float) 0.5);
			scaleanimation.setDuration(1000);
			scaleanimation.setFillEnabled(true);
			scaleanimation.setFillAfter(true);

			tempAnimation.addAnimation(scaleanimation);
			tempAnimation.addAnimation(anim);
			tempAnimation.setDuration(1000);

			tempAnimation.willChangeTransformationMatrix();

			// This takes care that the ImageViews stay in their final positions
			// after animating .
			tempAnimation.setFillEnabled(true);
			tempAnimation.setFillAfter(true);

			if (i == 0)
				tempAnimation.setAnimationListener(animFinishListener);

			mimageViews.get(i).startAnimation(tempAnimation);

		}

		// This will animate the Canvas Views in the middle ..

		for (int i = 0; i < CanvasImageViews.size(); i++) {
			AnimationSet tempAnimation = new AnimationSet(true);

			TranslateAnimation anim = new TranslateAnimation(0, 75, 0, 0);

			anim.setFillAfter(true);
			anim.setFillEnabled(true);

			ScaleAnimation scaleanimation = new ScaleAnimation(1, (float) 0.5,
					1, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5,
					Animation.RELATIVE_TO_SELF, (float) 0.5);

			scaleanimation.setDuration(1000);
			scaleanimation.setFillEnabled(true);
			scaleanimation.setFillAfter(true);

			tempAnimation.addAnimation(scaleanimation);
			tempAnimation.addAnimation(anim);
			tempAnimation.setDuration(1000);

			tempAnimation.willChangeTransformationMatrix();

			// This takes care that the ImageViews stay in their final positions
			// after animating .
			tempAnimation.setFillEnabled(true);
			tempAnimation.setFillAfter(true);

			CanvasImageViews.get(i).startAnimation(tempAnimation);

		}

	}

	public void applyFiltertoView(int currentSelectedFilter) {
		// Bitmap
		// imgViewBitmap=((BitmapDrawable)CanvasImageViews.get(currentSelectedLayer).getDrawable()).getBitmap();
		Bitmap imgViewBitmap = layerBitmaps.get(currentSelectedLayer);

		Bitmap modifiedBitmap = Bitmap.createScaledBitmap(imgViewBitmap,
				imgViewBitmap.getWidth(), imgViewBitmap.getHeight(), true);
		Canvas imageViewCanvas = new Canvas(modifiedBitmap);

		imageViewCanvas.drawBitmap(modifiedBitmap, 0, 0, new Paint());

		ColorMatrix cm = new ColorMatrix();

		String filterName = imageFilters[currentSelectedFilter];

		if (filterName.equalsIgnoreCase("stark")) {

			Paint spaint = new Paint();
			ColorMatrix scm = new ColorMatrix();

			scm.setSaturation(0);
			final float m[] = scm.getArray();
			final float c = 1;
			scm.set(new float[] { m[0] * c, m[1] * c, m[2] * c, m[3] * c,
					m[4] * c + 15, m[5] * c, m[6] * c, m[7] * c, m[8] * c,
					m[9] * c + 8, m[10] * c, m[11] * c, m[12] * c, m[13] * c,
					m[14] * c + 10, m[15], m[16], m[17], m[18], m[19] });

			spaint.setColorFilter(new ColorMatrixColorFilter(scm));
			Matrix smatrix = new Matrix();
			imageViewCanvas.drawBitmap(modifiedBitmap, smatrix, spaint);

			cm.set(new float[] { 1, 0, 0, 0, -90, 0, 1, 0, 0, -90, 0, 0, 1, 0,
					-90, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("sunnyside")) {

			cm.set(new float[] { 1, 0, 0, 0, 10, 0, 1, 0, 0, 10, 0, 0, 1, 0,
					-60, 0, 0, 0, 1, 0 });
		} else if (filterName.equalsIgnoreCase("worn")) {

			cm.set(new float[] { 1, 0, 0, 0, -60, 0, 1, 0, 0, -60, 0, 0, 1, 0,
					-90, 0, 0, 0, 1, 0 });
		} else if (filterName.equalsIgnoreCase("grayscale")) {

			float[] matrix = new float[] { 0.3f, 0.59f, 0.11f, 0, 0, 0.3f,
					0.59f, 0.11f, 0, 0, 0.3f, 0.59f, 0.11f, 0, 0, 0, 0, 0, 1,
					0, };

			cm.set(matrix);

		} else if (filterName.equalsIgnoreCase("cool")) {

			cm.set(new float[] { 1, 0, 0, 0, 10, 0, 1, 0, 0, 10, 0, 0, 1, 0,
					60, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("filter0")) {

			cm.set(new float[] { 1, 0, 0, 0, 30, 0, 1, 0, 0, 10, 0, 0, 1, 0,
					20, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("filter1")) {

			cm.set(new float[] { 1, 0, 0, 0, -33, 0, 1, 0, 0, -8, 0, 0, 1, 0,
					56, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("night")) {

			cm.set(new float[] { 1, 0, 0, 0, -42, 0, 1, 0, 0, -5, 0, 0, 1, 0,
					-71, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("crush")) {

			cm.set(new float[] { 1, 0, 0, 0, -68, 0, 1, 0, 0, -52, 0, 0, 1, 0,
					-15, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("filter4")) {

			cm.set(new float[] { 1, 0, 0, 0, -24, 0, 1, 0, 0, 48, 0, 0, 1, 0,
					59, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("sunny")) {

			cm.set(new float[] { 1, 0, 0, 0, 83, 0, 1, 0, 0, 45, 0, 0, 1, 0, 8,
					0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("filter6")) {

			cm.set(new float[] { 1, 0, 0, 0, 80, 0, 1, 0, 0, 65, 0, 0, 1, 0,
					81, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("filter7")) {

			cm.set(new float[] { 1, 0, 0, 0, -44, 0, 1, 0, 0, 38, 0, 0, 1, 0,
					46, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("filter8")) {

			cm.set(new float[] { 1, 0, 0, 0, 84, 0, 1, 0, 0, 63, 0, 0, 1, 0,
					73, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("random")) {

			// pick an integer between -90 and 90 apply
			int min = -90;
			int max = 90;
			Random rand = new Random();

			int five = rand.nextInt(max - min + 1) + min;

			int ten = rand.nextInt(max - min + 1) + min;
			int fifteen = rand.nextInt(max - min + 1) + min;

			Log.d(TAG, "five " + five);
			Log.d(TAG, "ten " + ten);
			Log.d(TAG, "fifteen " + fifteen);

			cm.set(new float[] { 1, 0, 0, 0, five, 0, 1, 0, 0, ten, 0, 0, 1, 0,
					fifteen, 0, 0, 0, 1, 0 });

		} else if (filterName.equalsIgnoreCase("sepia")) {

			float[] sepMat = { 0.3930000066757202f, 0.7689999938011169f,
					0.1889999955892563f, 0, 0, 0.3490000069141388f,
					0.6859999895095825f, 0.1679999977350235f, 0, 0,
					0.2720000147819519f, 0.5339999794960022f,
					0.1309999972581863f, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 };
			cm.set(sepMat);
		}
		else if(filterName.equalsIgnoreCase("vignette"))
		{

			Bitmap border = null;
			Bitmap scaledBorder = null;

			border = BitmapFactory.decodeResource(this.getResources(), R.drawable.vignette);

			int width = modifiedBitmap.getWidth();
			int height = modifiedBitmap.getHeight();

			scaledBorder = Bitmap.createScaledBitmap(border,width,height, false);
			if (scaledBorder != null && border != null) {
				imageViewCanvas.drawBitmap(scaledBorder, 0, 0, new Paint());
			}
		}

		Paint paint = new Paint();

		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		Matrix matrix = new Matrix();

		if(!filterName.equalsIgnoreCase("vignette"))
			imageViewCanvas.drawBitmap(modifiedBitmap, matrix, paint);

		CanvasImageViews.get(currentSelectedLayer).setImageBitmap(
				modifiedBitmap);

	}

	/**
	 * Have to call the startAnimation from here and not onCreate because if to
	 * get an ImageView.getWidth(),we have to call it from onWindowFocusChanged
	 * and not from onCreate,start or resume Refer
	 * http://stackoverflow.com/questions
	 * /7924296/how-to-use-onwindowfocuschanged-method
	 */

	public void onWindowFocusChanged(boolean hasFocus) {

		if(!isStarted)
		{
			layersLayout.removeAllViews();
			startAnimation();
			isStarted=!isStarted;
		}
	}

	private void LoadFiles(File seperatedLayersFolder) {

		File[] files = seperatedLayersFolder.listFiles();

		Log.d(TAG, "Number of files:" + files.length);

		for (File file : files) {
			Log.d("File path:", "Path=" + file.getPath());
			// Create 2 ImageViews,1 for the layer and the other for the bitmap.

			if (file.getName().toUpperCase().endsWith(("JPG"))
					|| file.getName().toUpperCase().endsWith(("PNG"))) {
				mimageViews.add(createImageView(file.getPath()));
				CanvasImageViews.add(createImageView(file.getPath()));
				layerBitmaps.add(BitmapFactory.decodeFile(file.getPath()));

			}

		}

		// Copy the ImageViews which will be drawn to the Canvas..

	}

	private ImageView createImageView(String imageLocation) {

		ImageView newimageView = new ImageView(this);

		Bitmap tempBitmap;

		tempBitmap = BitmapFactory.decodeFile(imageLocation);
		newimageView.setImageBitmap(tempBitmap);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);

		newimageView.setLayoutParams(layoutParams);

		/* Bug in Android 2.3.If this is not set,a trail is left behind */
		newimageView.setPadding(1, 1, 1, 1);

		activityLayout.addView(newimageView);

		Log.d(TAG, " Width " + newimageView.getWidth());
		System.gc();
		return newimageView;
	}

	public void resetBorders()
	{
		Iterator<Vector<ImageView>> itr = filteredViews.iterator();

		Log.d(TAG,"sizes "+filteredViews.size()+"  "+filteredViews.get(1).size());


		while(itr.hasNext())
		{


			Vector<ImageView> row= (Vector<ImageView>)itr.next();

			for(int i=0;i<row.size();i++)
			{
				ImageView temp=row.elementAt(i);
				temp.setImageBitmap(addBorder(  ((BitmapDrawable)temp.getDrawable()).getBitmap(),2,Color.WHITE));

			}


		}

		//		
		//		
		//		
		//		
		//		for(int i=0;i<filteredViews.size()-1;i++)
		//			{
		//				for(int j=0;j<filteredViews.get(i).size()-2;j++)
		//				{
		//					ImageView temp;
		//				    temp=(ImageView)findViewById(2000+i*imageFilters.length+j);
		//				    Log.d(TAG,"width "+temp.getWidth()+" height ="+temp.getHeight());
		//					temp.setImageBitmap(addBorder(  ((BitmapDrawable)temp.getDrawable()).getBitmap(),2,Color.WHITE));
		//	
		//				}
		//			
		//	}

	}

	AnimationListener animFinishListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub

			/**
			 * This is a weird error.Android will make an exception when you
			 * change the view hierarchy in animationEnd.You have to postpone
			 * the call by using the handler
			 * .Refer:http://stackoverflow.com/questions
			 * /5569267/nullpointerexception
			 * -that-doesnt-point-to-any-line-in-my-code
			 */
			new Handler().post(new Runnable() {
				public void run() {
					// Resize and reassign the views to the Linear layout ..

					for (int i = 0; i < mimageViews.size(); i++) {

						mimageViews.get(i).setImageBitmap(
								Bitmap.createScaledBitmap(layerBitmaps.get(i),
										(int) Math.round((
												scaleValue * layerBitmaps
												.get(i).getWidth())),
												(int) Math.round((
														scaleValue * layerBitmaps
														.get(i).getHeight())), true));
						// layerBitmaps.get(i).createScaledBitmap(src, dstWidth,
						// dstHeight, filter)
						if(i==0)
							mimageViews.get(i).setImageBitmap(  addBorder(  ((BitmapDrawable)mimageViews.get(i).getDrawable()).getBitmap(),2,Color.RED));
						else mimageViews.get(i).setImageBitmap(  addBorder(  ((BitmapDrawable)mimageViews.get(i).getDrawable()).getBitmap(),2,Color.WHITE));

						Log.d(TAG,
								"Width small"
										+ (int) (2 * layerBitmaps.get(i)
												.getWidth()) / 10);

						int width = 2 * layerBitmaps.get(i).getWidth();
						width = width / 10;

						Log.d(TAG,
								"Width small ==  "
										+ Math.round((
												scaleValue * layerBitmaps
												.get(i).getWidth())));

						// mimageViews.get(i).setBackgroundColor(Color.WHITE);
						// mimageViews.get(i).setPadding(1, 1, 1, 1)

						if (i > 0)
							mimageViews.get(i).setPadding(
									0,
									(int) Math.round(1 * (
											scaleValue * mimageViews
											.get(i).getHeight()) / 2) - 30, 0,
											0);

						Log.d(TAG,
								"Height"
										+ (int) Math
										.round(2.5 * (
												scaleValue * mimageViews
												.get(i).getHeight()) / 2));

						Log.d(TAG,
								"sma height"
										+ (2.5 * (
												scaleValue * mimageViews.get(i)
												.getHeight()) / 2));

						activityLayout.removeView(mimageViews.get(i));

						mimageViews.get(i).setClickable(true);
						mimageViews.get(i).bringToFront();
						mimageViews.get(i).setOnTouchListener(
								imageLayerClickListener);

						mimageViews.get(i).setId(1000 + i); // Need to set an
						// id..1

						LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						imgViewParams.setMargins(0, 50*i, 0, 0);

						mimageViews.get(i).setLayoutParams(imgViewParams);

						layersLayout.addView(mimageViews.get(i));
						//layersLayout.setVisibility(View.INVISIBLE);

						Log.d(TAG,
								"width"
										+ ((int) (2 * layerBitmaps.get(i)
												.getWidth()) / 10));

					}
				}
			});

			/*
			 * Animate the HS
			 */

			hs.setAnimation(animFadeIn);
			layersLayout.setAnimation(animFadeIn);
			//layersLayout.setVisibility(View.VISIBLE);
			hs.setVisibility(View.VISIBLE);

			// Add the buttons ..
			addButtonstoActivity();

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}
	};

	public OnTouchListener imageLayerClickListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			// Log.d(TAG,"touched");

			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				for (int i = 0; i < mimageViews.size(); i++) {

					Log.d(TAG,"View id="+i+" size"+mimageViews.size());
					ImageView temp2 =(ImageView)mimageViews.get(i);
					temp2.setImageBitmap(addBorder(  ((BitmapDrawable)temp2.getDrawable()).getBitmap(),5,Color.WHITE));

					if (v.getId() == mimageViews.get(i).getId()) {
						currentSelectedLayer = i;

						// add a red border to show it is selected ..

						ImageView temp =(ImageView)v;
						temp.setImageBitmap(addBorder(  ((BitmapDrawable)temp.getDrawable()).getBitmap(),5,Color.RED));

						hs.removeAllViews();
						filtersLayout.removeAllViews();

						Log.d(TAG,
								"size i=" + i + "size of "
										+ filteredViews.size() + " : "
										+ filteredViews.get(i).size());

						for (int j = 0; j < filteredViews.get(i).size(); j++)
							filtersLayout.addView(filteredViews.get(i).get(j));

						//
						hs.addView(filtersLayout);
						hs.invalidate();

						Log.d(TAG, "passing");
					}

				}

			}
			layersScroll.invalidate();

			return false;
		}

	};

	public OnTouchListener filtersLayerTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			// change the filters ..

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int id = v.getId() - 2000;
				applyFiltertoView(id % imageFilters.length);

				Log.d(TAG, "ID" + id);
			}
			return false;
		}
	};

	public OnClickListener filtersLayerClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			resetBorders();

			int id = v.getId() - 2000;
			applyFiltertoView(id % imageFilters.length);
			ImageView temp =(ImageView)v;
			temp.setImageBitmap(addBorder(  ((BitmapDrawable)temp.getDrawable()).getBitmap(),2,Color.RED));

			Log.d(TAG, "ID" + id);

		}

	};


	protected void addButtonstoActivity() {
		// TODO Auto-generated method stub

		full_screen=new ImageButton(this);
		full_screen.setImageDrawable(getResources().getDrawable(R.drawable.fullscreen));
		full_screen.setBackgroundColor(Color.TRANSPARENT);
		RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		layoutParams.setMargins(0, 120,20, 0);
		full_screen.setLayoutParams(layoutParams);
		full_screen.setId(54345);
		activityLayout.addView(full_screen);
		full_screen.setOnClickListener(buttonClickListener);

		layoutParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		TextView valueTV = new TextView(this);
		valueTV.setText("h");
		valueTV.setId(54321);
		valueTV.setLayoutParams(layoutParams);
		valueTV.setVisibility(View.INVISIBLE);
		activityLayout.addView(valueTV);

		Replace=new ImageButton(this);
		Replace.setImageDrawable(getResources().getDrawable(R.drawable.replace));
		Replace.setBackgroundColor(Color.TRANSPARENT);
		layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.LEFT_OF, 54321);

		//layoutParams.setMargins(0, 0,40, 0);

		Replace.setLayoutParams(layoutParams);
		activityLayout.addView(Replace);

		layoutParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.RIGHT_OF, 54321);

		layoutParams.setMargins(175, 0,0, 0);

		focus=new ImageButton(this);
		focus.setImageDrawable(getResources().getDrawable(R.drawable.bluricon));
		focus.setLayoutParams(layoutParams);
		focus.setBackgroundColor(Color.TRANSPARENT);
		activityLayout.addView(focus);



		layoutParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		//layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		layoutParams.addRule(RelativeLayout.BELOW, 54345);

		layoutParams.setMargins(0, 90,20, 0);

		reset=new ImageButton(this);

		reset.setId(9876);
		reset.setOnClickListener(buttonClickListener);
		reset.setImageDrawable(getResources().getDrawable(R.drawable.reset));
		reset.setLayoutParams(layoutParams);
		reset.setBackgroundColor(Color.TRANSPARENT);
		activityLayout.addView(reset);

		layoutParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.setMargins(30, 0,0,0);
	
		back=new ImageButton(this);
		
		back.setOnClickListener(buttonClickListener);
		back.setImageDrawable(getResources().getDrawable(R.drawable.back));
		back.setLayoutParams(layoutParams);
		back.setBackgroundColor(Color.TRANSPARENT);
		
		activityLayout.addView(back);


	}

	public OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
       
			if(v.getId()==54345)
			{
				Vector<ImageView> fullImgViews=new Vector<ImageView>();
				RelativeLayout.LayoutParams imgViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				fullScreenLayout.removeAllViews();
				setContentView(fullScreenLayout);
				
				for(int i=0;i<CanvasImageViews.size();i++)
					{   
					    ImageView temp=new ImageView(getBaseContext());
					    temp.setImageDrawable(CanvasImageViews.get(i).getDrawable());
					    temp.setLayoutParams(imgViewParams);
					    fullImgViews.add(temp);
						fullScreenLayout.addView(temp);
					}
				
				imgViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				
				ImageButton backButton;
				backButton=new ImageButton(getBaseContext());
				
				backButton.setBackgroundColor(Color.TRANSPARENT);
				backButton.setImageDrawable(getResources().getDrawable(R.drawable.normalscreen));
				
//				imgViewParams.setMargins(0, 0, 0, 0);
				imgViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				backButton.setLayoutParams(imgViewParams);
				fullScreenLayout.addView(backButton);
				
				backButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setContentView(activityLayout); 
					}
				});
				//backButton.set
			}
			
			if(v.getId()==9876)
			{
				
			for(int i=0;i<CanvasImageViews.size();i++)
			{
			
				CanvasImageViews.get(i).setImageBitmap(layerBitmaps.get(i));
				
			}
			resetBorders();

		}
		}
	};
}
