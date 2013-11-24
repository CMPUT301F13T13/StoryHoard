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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Role: Holds a collection of the SimpleESResponses. Needed when
 * doing an advanced elastic search which involves logical operators
 * in the query.
 * 
 * </br>
 * Eg. Where story title has "dog AND cow OR hen"
 * 
 * </br>
 * CODE REUSE: This code was taken directly from 
 * </br>
 * URL: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ElasticSearchSearchResponse.java 
 * </br>
 * Date: Nov. 4th, 2013 
 * </br>
 * Licensed under CC0 (available at http://creativecommons.org/choose/zero/)
 * 
 * @author Abram Hindle
 * @author Chenlei Zhang
 * @author Ashley Brown
 * @author Stephanie Gil
 * 
 * @see SimpleESResponse
 * @see ESUpdates
 */
public class ElasticSearchResponse<T> {
	int took;
	boolean timed_out;
	transient Object _shards;
	Hits<T> hits;
	boolean exists;

	public Collection<SimpleESResponse<T>> getHits() {
		return hits.getHits();
	}

	public Collection<T> getSources() {
		Collection<T> out = new ArrayList<T>();
		for (SimpleESResponse<T> essrt : getHits()) {
			out.add(essrt.getSource());
		}
		return out;
	}

	public String toString() {
		return (super.toString() + ":" + took + "," + _shards + "," + exists
				+ "," + hits);
	}
}
