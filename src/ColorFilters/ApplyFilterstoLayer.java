package ColorFilters;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import com.tesseract.studio3d.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class ApplyFilterstoLayer 
{
	String TAG="com.tesseract.studio3d.instagramEffects";

	String[] imageFilters = { "sepia", "stark", "sunnyside", "cool", "worn",
			"grayscale","vignette","crush","sunny","night" };
	
	Context activityContext;
	
	String imagePath;
	
	public ApplyFilterstoLayer(Context context,String path,int folderNum)
	{
		imagePath=path;
		activityContext=context;
		
		applyInstagramEffectstoImage(imagePath,folderNum);
		
	}

	public void applyInstagramEffectstoImage(String imgFile,int folderNum) {
		// TODO Auto-generated method stub

		// Make an instagram version of the filters ..
		for (int i = 0; i < imageFilters.length; i++) {

			Bitmap tempbitmap = BitmapFactory.decodeFile(imgFile);
			
			// Apply Different Filters and save

			Paint paint = new Paint();
			ColorMatrix cm = new ColorMatrix();
			
			int width_x=(int) Math.round(0.22*tempbitmap.getWidth()),width_y=(int) Math.round(0.22*tempbitmap.getHeight());
			
			Bitmap resizedbitmap = Bitmap.createScaledBitmap(tempbitmap, width_x, width_y, true);
			
			Log.d(TAG,"Width=="+width_x);

			Canvas canvas = new Canvas(resizedbitmap);

			applySpecificFiltertoimage(imageFilters[i], resizedbitmap, canvas,
					cm, paint, folderNum);

		}
		
	}
		
		public void applySpecificFiltertoimage(String filterName,
				Bitmap canvas_bitmap, Canvas canvas, ColorMatrix cm, Paint paint,
				int i) 
		{

			canvas.drawBitmap(canvas_bitmap, 0, 0, new Paint());

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
				canvas.drawBitmap(canvas_bitmap, smatrix, spaint);

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
				
				border = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.vignette);
				
				int width = canvas_bitmap.getWidth();
				int height = canvas_bitmap.getHeight();
				
				scaledBorder = Bitmap.createScaledBitmap(border,width,height, false);
				if (scaledBorder != null && border != null) {
					canvas.drawBitmap(scaledBorder, 0, 0, new Paint());
				}
			}

			paint.setColorFilter(new ColorMatrixColorFilter(cm));
			Matrix matrix = new Matrix();
			
			
			if(!filterName.equalsIgnoreCase("vignette"))
				canvas.drawBitmap(canvas_bitmap, matrix, paint);
			
			
			
			paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(20);
			String filename=filterName;
			filename=filename.substring(0, 1).toUpperCase() + filename.substring(1);
			
			//input.substring(0, 1).toUpperCase() + input.substring(1);
			
			canvas.drawText(filename, (float) (canvas_bitmap.getWidth()/2-10*filterName.length()/2),
					(float) (canvas_bitmap.getHeight()*0.3), paint);

			/* code... */

			String fileName = Environment.getExternalStorageDirectory()
					+ "/Studio3D/Layers/Filters/" + i + "/" + filterName + ".png";
		
			OutputStream stream = null;
			try {
				
				stream = new FileOutputStream(fileName);
				
				/*
				 * Write bitmap to file using JPEG or PNG and 80% quality hint for
				 * JPEG.
				 */
				canvas_bitmap.compress(CompressFormat.PNG, 100, stream);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.d("Filter name", "path " + filterName);
		}


	}
	
