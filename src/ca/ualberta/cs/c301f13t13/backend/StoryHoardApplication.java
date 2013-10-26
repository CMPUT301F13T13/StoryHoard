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
 * Used the following code as reference:
 * URL: https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
 * Date: Oct. 25, 2013
 * Author: Abram Hindle
 */

package ca.ualberta.cs.c301f13t13.backend;

import android.app.Application;
import android.content.Context;

/**
 * @author Owner
 *
 */
public class StoryHoardApplication extends Application{
    // Singletons
    transient private static StoryManager stoMan = null;
//    transient private static ChapterManager chapMan = null;
//    transient private static ChoiceManager choMan = null;
//    transient private static StoryController storyController = null;
    
    public static StoryManager getStoryManager(Context context) {
        if (stoMan == null) {
        	stoMan = new StoryManager(context);
        }
        return stoMan;
    }
    
//		UNCOMMENT ONCE MISSING CLASSES HAVE BEEN CREATED    
//    public static ChapterManager getChapterManager() {
//        if (chapMan == null) {
//            chapMan = new ChapterManagerp();
//        }
//        return chapMan;
//    }  
//    
//    public static ChoiceManager getChoiceManager() {
//        if (choMan == null) {
//            choMan = new ChoiceManager();
//        }
//        return choMan;
//    }     
//
//    public static StoryController getStoryController() {
//        if (storyController == null) {
//            storyController = new storyController(getStoryManager());
//        }
//        return storyController;
//    }   
}
