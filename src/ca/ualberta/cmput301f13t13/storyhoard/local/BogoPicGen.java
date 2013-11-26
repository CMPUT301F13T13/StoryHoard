package ca.ualberta.cmput301f13t13.storyhoard.local;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

/**
 * This class is not really part of the StoryHoard application. It is only used
 * for generating bitmaps in the StoryHoard unit tests.
 * 
 * CODE RE-USE </br>
 * This class was taken directly from: </br>
 * 
 * URL: https://github.com/abramhindle/BogoPicGen </br>
 * Date: Nov. 17, 2013 </br>
 *  
 * @author Abram Hindle
 *
 */
public class BogoPicGen {
	public static Bitmap generateBitmap(int width, int height) {
		// Algorithms based on:
		// 	http://countercomplex.blogspot.com/2011/10/some-deep-analysis-of-one-line-music.html
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888 );
		int t = (int) (255.0 * Math.random());
		int r = (int) (255.0 * Math.random());
		int g = (int) (255.0 * Math.random());
		int b = (int) (255.0 * Math.random());
		int offset1 = (int) (255 * Math.random());
		int offset2 = (int) (255 * Math.random());
		int offset3 = (int) (255 * Math.random());
		int rm1 = 1 + (int)( 12 * Math.random());
		int gm1 = 1 + (int)( 12 * Math.random());
		int bm1 = 1 + (int)( 12 * Math.random());
		int rm2 = 1 + (int)( 12 * Math.random());
		int gm2 = 1 + (int)( 12 * Math.random());
		int bm2 = 1 + (int)( 12 * Math.random());
		int rm3 = 1 + (int)( 12 * Math.random());
		int gm3 = 1 + (int)( 12 * Math.random());
		int bm3 = 1 + (int)( 12 * Math.random());

		int [] mods = { 65535, width, height, 255, 64, 32, 512, 1024 , width, height, width, height}; 
		int rmod = mods[(int)(mods.length * Math.random())];
		int gmod = mods[(int)(mods.length * Math.random())];
		int bmod = mods[(int)(mods.length * Math.random())];
		
		int rs1 = 1 + (int)( 11 * Math.random());
		int gs1 = 1 + (int)( 11 * Math.random());
		int bs1 = 1 + (int)( 11 * Math.random());
		int rs2 = 1 + (int)( 11 * Math.random());
		int gs2 = 1 + (int)( 11 * Math.random());
		int bs2 = 1 + (int)( 11 * Math.random());

		int rf = (int)(2*Math.random());
		int bf = (int)(2*Math.random());
		int gf = (int)(2*Math.random());
		
		int [] pixels = new int[width*height];
		float [] hsv = new float[3];
		boolean isHSV = (Math.random() > 0.5);
		int constantT = (Math.random() > 0.5)?1:0;
		int c;
		//public void setPixels (int[] pixels, int offset, int stride, int x, int y, int width, int height) 
		for (int i = 0; i < height; i++) {		
			for (int j = 0 ; j < width; j++) {
				r = (r+offset1+(b*rf|t*rm1&t>>rs1|t*rm2&t>>rs2|t*rm3&t/1024)-1)%rmod;
				g = (g+offset2+(r*gf|t*gm1&t>>gs1|t*gm2&t>>gs2|t*gm3&t/1024)-1)%gmod;
				b = (b+offset3+(g*bf|t*bm1&t>>bs1|t*bm2&t>>bs2|t*bm3&t/1024)-1)%bmod;
				if (isHSV) {
					hsv[0] = (float)r / (float)255.0;
					hsv[1] = (float)g / (float)255.0;
					hsv[2] = (float)b / (float)255.0;
					//	int c = Color.rgb(r,g,b);
					c = Color.HSVToColor(hsv);
				} else {
					c = Color.rgb(r,g,b);					
				}
				pixels[i*width + j] = c;
				t = t + constantT * 1;
				
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * Creates a new bitmap, save sit on to SD card and sets path to it.
	 * Once again, this code is only for use in the JUnit tests.
	 * 
	 * CODE REUSE: </br>
	 * This code was modified from the code at:</br>
	 * URL: http://stackoverflow.com/questions/7021728/android-writing-bitmap-to-sdcard </br>
	 * Date: Nov. 2, 2013 </br>
	 * License`: CC-BY-SA
	 */ 
	public static String createPath(String fname) {
		Bitmap bm = BogoPicGen.generateBitmap(50, 50);
		File mFile1 = Environment.getExternalStorageDirectory();

		String fileName = fname;

		File mFile2 = new File(mFile1,fileName);
		try {
			FileOutputStream outStream;

			outStream = new FileOutputStream(mFile2);

			bm.compress(Bitmap.CompressFormat.JPEG, 75, outStream);

			outStream.flush();

			outStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String path = mFile1.getAbsolutePath().toString()+"/"+fileName;

		Log.i("maull", "Your IMAGE ABSOLUTE PATH:-"+path); 

		File temp=new File(path);

		if(!temp.exists()){
			Log.e("file","no image file at location :"+path);
		}
		
		return path;
	}	
}