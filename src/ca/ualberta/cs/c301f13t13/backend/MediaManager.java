/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

import android.content.Context;

import ca.ualberta.cs.c301f13t13.gui.SHView;

/**
 * @author Owner
 *
 */
public class MediaManager extends Model<SHView> implements StoringManager{
	private Context context;
	
	/**
	 * Initializes a new MediaManager object.
	 */
	public MediaManager(Context context) {
		this.context = context;
	}
	
	@Override
	public void insert(Object object, DBHelper helper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Object oldObject, Object newObject, DBHelper helper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setSearchCriteria(Object object, ArrayList<String> sArgs) {
		// TODO Auto-generated method stub
		return null;
	}

}
