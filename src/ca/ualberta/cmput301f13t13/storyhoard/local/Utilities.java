/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.local;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import android.os.Environment;
import android.provider.Settings;

/**
 * Class meant for general functions that can be used by any class.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 */
public class Utilities {

	/**
	 * This functions gets the id of the device and returns it as a string
	 * 
	 * CODE REUSE:
	 * This code is modified from:
	 * 
	 * URL: http://developer.samsung.com/android/technical-docs/How-to-retrieve-the-Device-Unique-ID-from-android-device
	 * Date: Nov. 5th, 2013
	 * 
	 * 
	 */
	public static String getPhoneId(Context context) {
		String PhoneId = Settings.Secure.getString(context.getContentResolver(), 
				Settings.Secure.ANDROID_ID);
		return PhoneId;
	}

	/**
	 * Saves a bitmap to a location on the phone's sd card. Returns
	 * the path of where the image was saved to. </br></br>
	 * 
	 * CODE REUSE: </br>
	 * This code was modified from the code at:</br>
	 * URL: http://stackoverflow.com/questions/7021728/android-writing-bitmap-to-sdcard </br>
	 * Date: Nov. 2, 2013 </br>
	 * License`: CC-BY-SA
	 */
	public static String saveImageToSD(Bitmap bmp) {
	        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
	        File folderF = new File(folder);
	        if (!folderF.exists()) {
	                folderF.mkdir();
	        }
	
	        String imageFilePath;
	        imageFilePath = folder + "/"
	                                + String.valueOf(System.currentTimeMillis()) + ".jpg";
	        File imageFile = new File(imageFilePath);
	
	        FileOutputStream fout;
	        try {
	                fout = new FileOutputStream(imageFile);
	                bmp.compress(Bitmap.CompressFormat.JPEG, 85, fout);
	
	                fout.flush();
	                fout.close();
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	        
	        return imageFilePath;
	}	
}
