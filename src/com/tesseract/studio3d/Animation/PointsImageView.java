package com.tesseract.studio3d.Animation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ColorFilters.ApplyFilterstoLayer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;



public class PointsImageView extends ImageView {
	 
	Bitmap imageViewBitmap,imageViewBitmap2;
	Paint paint;
	static Path path;
	Random randomGenerator = new Random();
	static List<Point> points = new ArrayList<Point>();
	static int count =0;
	static boolean isAnimate=false;
	int animationTime;
	long start_time,lastindex_time;
	int currentindex=0;
	XmlPullParserFactory pullParserFactory;
	private final static String TAG ="XMLParser";
	
	static float ContourPoints[];
	
	public Mat mRgba;
	
	public Mat disp;
	public Mat finalImage;
	public Mat limg;	
	public Mat foreground,background;
	int converted_xcoord=0,converted_ycoord=0;
	int currentMode=-1;
	
	ProgressDialog conversionProgress;
	
	File leftimgFile;
	
	public PointsImageView(Context context, AttributeSet set) {         
		 super(context, set);     
		
		 /* Get the number of files in the directory and create that many layers.Might want to implement this later.
		  * 
		  * But right now just create 2 bitmaps.
		  */
		 
		 imageViewBitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/Studio3D/img_left.jpg");
		 imageViewBitmap2=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/Studio3D/img_right.jpg"); 
		 
		 Log.d("passing","passing");
		 
		 Log.d(TAG,"bitmap dimensions:"+imageViewBitmap.getWidth()+imageViewBitmap.getHeight());
		 
		 System.loadLibrary("depth_magic");
		 
		 segmentImagesandgetContours();
		 
		// loadandparseXML();
		 configurePaint();
		 
		 
	
		 animationTime=0;
		 
		 start_time=lastindex_time=System.currentTimeMillis();
		 
		} 
	
	static public void initializeMats()
	{
		
	}
	 private void segmentImagesandgetContours() {
		// TODO Auto-generated method stub
		
	    	
	    	
	}

	public static void startAnimation(boolean isStart)
	{

		isAnimate=true;
		sortPoints();
		initializePath();
		Log.d(TAG,"Size"+ContourPoints.length);
	}
	
	private static void sortPoints() {
		// TODO Auto-generated method stub
		
		Log.d(TAG,"points "+Arrays.toString(ContourPoints));
		float temp=100000;
		
		for(int i=1;i<ContourPoints.length-1;i+=2)
		{	for(int j=i+2;j<ContourPoints.length-1;j+=2)
				{
					if(ContourPoints[j]<ContourPoints[i])
						{
							temp=ContourPoints[j];
							ContourPoints[j]=ContourPoints[i];
							ContourPoints[i]=temp;
							temp=ContourPoints[j+1];
							ContourPoints[j+1]=ContourPoints[i+1];
							ContourPoints[i+1]=temp;
					    }
				}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Log.d("RE","Reached");
	    int desiredWidth = imageViewBitmap.getWidth();
	    int desiredHeight = imageViewBitmap.getHeight();

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int width;
	    int height;

	    //Measure Width
	    if (widthMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        width = Math.min(desiredWidth, widthSize);
	    } else {
	        //Be whatever you want
	        width = desiredWidth;
	    }

	    //Measure Height
	    if (heightMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        height = Math.min(desiredHeight, heightSize);
	    } else {
	        //Be whatever you want
	        height = desiredHeight;
	    }

	    //MUST CALL THIS
	    setMeasuredDimension(width, height);
	    
	    Log.d(TAG,"View Width"+width+" View Height"+ height);
	}
	
    public PointsImageView(Context context) {
        super(context);
    }
    
    public void configurePaint()
    {
    	paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
        paint.setColor(Color.RED);                    // set the color
        paint.setStrokeWidth(3);               // set the size
        paint.setDither(true);                    // set the dither to true
        paint.setStyle(Paint.Style.STROKE);       // set to STOKE
        paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        paint.setPathEffect(new CornerPathEffect(20) );   // set the path effect when they join.
        paint.setAntiAlias(true);  
        
        
       
        
    }
    
    public static void initializePath()
    {
     
    	Log.d(TAG,"size"+points.size());
	  
     	path= new Path();
  		path.reset();
  		path.moveTo(ContourPoints[0], ContourPoints[1]);
  		
 		Log.d(TAG,"Contour Point"+ContourPoints[0]+"  "+ContourPoints[1]);
    	
    }
    
    public void loadandparseXML()
    {

    	try 
    	{
    	      pullParserFactory = XmlPullParserFactory.newInstance();
//    	      pullParserFactory.setNamespaceAware(true);
    	      
    	      XmlPullParser parser = pullParserFactory.newPullParser();
    	       parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    	    
    	      InputStream in_s = getContext().getAssets().open("points.xml");
    	            parser.setInput(in_s, null);
    	     
    	              
    	      parseXML(parser);
    	      
    	      Log.d("1","1");
    	      
    	    } catch (XmlPullParserException e) {
    	      e.printStackTrace();
    	    } catch (IOException e) {
    	      // TODO Auto-generated catch block
    	      e.printStackTrace();
    	}
    	
    }
    
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
     
      int eventType = parser.getEventType();
      Point currentPoint = null;
          
      Log.d("2.1","1"+eventType);
    
      while( eventType != XmlPullParser.END_DOCUMENT)
      {

          String name ="";
          
          switch (eventType)
          {
              case XmlPullParser.START_DOCUMENT:
                
            	  points = new ArrayList<Point>();
            	  Log.d(TAG,"1.11");
                
                  break;
                  
              case XmlPullParser.START_TAG:
                  
            	  name = parser.getName();
                  
                  if (name.equals("point"))
                  {
                	   Log.d(TAG,"start");
                      currentPoint = new Point();
                      
                  } else if (currentPoint != null)
                  {
                      if (name.equals("x"))
                        currentPoint.x =Integer.parseInt(parser.nextText().toString());
                      else if (name.equals("y"))
                        currentPoint.y = Integer.parseInt( parser.nextText().toString());
                      
                  }
                  break;
                  
              case XmlPullParser.END_TAG:
                  name = parser.getName();
                  if (name.equalsIgnoreCase("point") && currentPoint != null){
                    points.add(currentPoint);
                  }
          }
          eventType = parser.next();
          Log.d(TAG,"Event :"+eventType);
      
      }
      Log.d(TAG,"Size :"+points.size()); // I get the correct size here :64
 //   printpoints(points);
          
    }
    
    
    
    private void printpoints(ArrayList<Point> points)
    {
      
    	Log.d(TAG,"sd");
    	
      String content = "";

      
      Log.d(TAG,"size"+points.size());
     
      int i=0;
     
      for(;i<points.size();i++)
      {
    	  Point currPoint  = points.get(i);
    	  content = content + "x : " +  currPoint.x + " ";
          content = content + "y : " +  currPoint.y + " ";
          
         // points.add(new Point(currPoint.x,currPoint.y));
          
      Log.d(TAG, "Val"+i);
    	  
      }
    	  
      
      Log.d("The XML String","content: "+content);
       
    }
   
    public void onDraw(Canvas canvas)
    {
    	super.onDraw(canvas);
      
    	
    	canvas.drawBitmap(imageViewBitmap, 0, 0,paint); 
	    canvas.drawBitmap(imageViewBitmap2, 0, 0,paint);
	    
	    if(isAnimate)
	    {
	    	if((System.currentTimeMillis()-lastindex_time)>(animationTime/ContourPoints.length)&&currentindex<ContourPoints.length-2)
	    		{
	    			currentindex+=2;
	    			lastindex_time=System.currentTimeMillis();
	    		}   
	    	
	    	//else if(currentindex==ContourPoints.length-2)
	    	else if(currentindex==ContourPoints.length-2)
	    		{
//	    			MainActivity.strokeFinished();
	    			isAnimate=!isAnimate;
	    			Intent camera_intent=new Intent(getContext(),AnimationActivity.class);
	    		    getContext().startActivity(camera_intent);
	    		}
	    	
	    	path.lineTo(ContourPoints[currentindex], ContourPoints[currentindex+1]);
	    	canvas.drawPath(path, paint);
		    
	    }
	    
	       
//	    path.lineTo(points.get(currentindex).x, points.get(currentindex).y);
	    
	     
	    invalidate();
	    
	    Log.d(TAG,"Index"+currentindex);
	    
    	
    }
    public boolean onTouchEvent(MotionEvent event) {
    	   // TODO Auto-generated method stub
    	   
    	Log.d("X = "+event.getX(),"Y = "+event.getY());
    	   return true; //processed
    	  }
    
    public native void getDisparity(long matAddrRgba, long matAddrfinalImage);
    public native void crop5(long matAddrRgba, long matAddrfinalImage);
    public native float[] getThreshold(long matAddrRgba, long matAddrDisp, long matAddrfinalImage,long matAddrBackground, long matAddrForeground, int ji1, int ji2,int choice);
   
    
    public class ProgressDialogClass extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			conversionProgress.setMessage("Processing Image - Computing Disparity ");
			getDisparity(mRgba.getNativeObjAddr(), disp.getNativeObjAddr());
			
			mRgba = Highgui.imread(Environment.getExternalStorageDirectory().getPath()+"/Studio3D/Layers/img_full.jpg");
			
			
			leftimgFile = new File(Environment.getExternalStorageDirectory().getPath()+"/Studio3D/Layers/img_left.jpg");
	    				
			conversionProgress.setMessage("Processing Image - Cropping Image ");
			crop5(mRgba.getNativeObjAddr(), limg.getNativeObjAddr());
		    
			conversionProgress.setMessage("Processing Image - Splitting Layers ");
			ContourPoints= getThreshold(mRgba.getNativeObjAddr(), disp.getNativeObjAddr(), finalImage.getNativeObjAddr(),background.getNativeObjAddr(),foreground.getNativeObjAddr(), (int)converted_xcoord, (int)converted_ycoord,currentMode);
	        			
			// Instagram Filter conversion,Browse the seperated layers directory
			// and convert ..
			conversionProgress.setMessage("Processing Image - Applying Color Filters ");
			
			applyFilterstoLayers();
			
		
			return "";

		}

		@Override
		protected void onPostExecute(String result) {

			conversionProgress.dismiss();
			// startanimation

			Log.d("done", "done");

			PointsImageView.startAnimation(true);

			// might want to change "executed" for the returned string passed
			// into onPostExecute() but that is upto you
		}

		@Override
		protected void onPreExecute() {
		//	conversionProgress.setTitle("Processing Image");
			conversionProgress.setMessage("Please wait while we process your image ...");
			conversionProgress.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
		
		public void applyFilterstoLayers() {

			File sdCard = Environment.getExternalStorageDirectory();
			File filtersdir = new File(Environment.getExternalStorageDirectory()
					+ "/Studio3D/Layers/Filters/");

			File seperatedLayersFolder = new File(Environment.getExternalStorageDirectory()
							+ "/Studio3D/Layers/");

			// Find all the files in the folder..

			File[] files = seperatedLayersFolder.listFiles();

			Log.d(TAG, "Number of files:" + files.length);
			int count = 0;

			for (File file : files) {

				// Log.d("File path:","Path="+file.getPath());

				if (file.getName().toUpperCase().endsWith(("JPG"))
						|| file.getName().toUpperCase().endsWith(("PNG"))) {
					
					File dir = new File(sdCard.getAbsolutePath()
							+ "/Studio3D/Layers/Filters/" + count + "/");

					Log.d(TAG, "path" + dir.getPath());
					dir.mkdirs();

					ApplyFilterstoLayer Filters;
												
			    	Filters=new ApplyFilterstoLayer(getContext(),leftimgFile.getAbsolutePath(),count);

					count++;
				}
			}
		}		
		
		
		

	}

    
}