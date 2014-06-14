package main;

/**
 * 
 * Represents a characteristic function of a set of objects
 * @author yuliya
 *
 * @param <T> : the type of the objects in the set
 */
public interface Filter<T> {
	/**
	 * a characteristic function:
	 * given an object it tells whether it belongs or not to the set 
	 * that this function is representing
	 * @param obj : the object for which one needs to know whether it belongs to a certain set
	 * @return : true if the object belongs to the set under consideration
	 */
	boolean filter(T obj);
}
