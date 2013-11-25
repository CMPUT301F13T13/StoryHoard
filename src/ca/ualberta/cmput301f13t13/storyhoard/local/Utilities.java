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


import android.content.Context;

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
}
