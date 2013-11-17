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
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.Collection;

/**
 * Role: Hits object for the elastic search.
 * 
 * CODE REUSE: This code was taken directly from 
 * URL: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/Hits.java
 * Date: Nov. 4th, 2013 
 * Licensed under CC0 (available at http://creativecommons.org/choose/zero/)

 * @author Abram Hindle
 * @author Chenlei Zhang
 * @author Ashley Brown
 * @author Stephanie Gil
 */
public class Hits<T> {
	    int total;
	    double max_score;
	    Collection<SimpleESResponse<T>> hits;
	    public Collection<SimpleESResponse<T>> getHits() {
	        return hits;
	    }
	    public String toString() {
	        return (super.toString()+","+total+","+max_score+","+hits);
	    }
}
