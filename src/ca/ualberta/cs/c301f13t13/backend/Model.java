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
 * 
 * 
 * Citing: 
 * URL: https://raw.github.com/abramhindle/FillerCreepForAndroid/master/src/es/softwareprocess/fillercreep/FModel.java
 * Date: Oct. 24, 2013
 * Author: Abram Hindle
 */
package ca.ualberta.cs.c301f13t13.backend;

import ca.ualberta.cs.c301f13t13.gui.View;

import java.util.ArrayList;

/**
 * @editor Stephanie Gil
 *
 */
@SuppressWarnings("rawtypes")
public class Model<V extends View> {
    private ArrayList<V> views;

    public Model() {
        views = new ArrayList<V>();
    }

    public void addView(V view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }

    public void deleteView(V view) {
        views.remove(view);
    }

    @SuppressWarnings("unchecked")
	public void notifyViews() {
        for (V view : views) {
            view.update(this);
        }
    }
	
}
