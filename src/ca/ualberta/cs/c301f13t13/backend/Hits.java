/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.Collection;

/**
 * @author Owner
 *
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
