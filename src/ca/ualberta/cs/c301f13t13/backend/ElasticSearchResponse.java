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
package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Role: Holds elastic search results. More complex than the simple elastic
 * search results.
 * 
 * CODE REUSE: This code was taken directly from 
 * URL: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ElasticSearchSearchResponse.java 
 * Date: Nov. 4th, 2013 
 * Licensed under CC0 (available at http://creativecommons.org/choose/zero/)
 * 
 * Original authors:
 * @author Abram Hindle
 * @author Chenlei Zhang
 * 
 * Modifications by:
 * @author Ashley
 * @author Stephanie
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