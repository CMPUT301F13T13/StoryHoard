/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

/**
 * @author Owner
 *
 */
public class SimpleESResponse<T> {
    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    
    public T getSource() {
        return _source;
    }
}
