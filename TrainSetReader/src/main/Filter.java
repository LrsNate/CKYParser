package main;

/**
 * 
 * Represents a characteristic function of a set of objects.
 * Used to filter out the new non-terminals created during the conversion to CNF form
 * (the specific (conventional) form of labels of these non-terminals is stored in this class).
 * @param <T> The type of the objects in the set.
 */
public interface Filter<T> {
	/**
	 * A characteristic function:
	 * given an object it tells whether it belongs or not to the set 
	 * that this function is representing.
	 * @param obj The object for which one needs to know whether it belongs to a certain set.
	 * @return True if the object belongs to the set under consideration.
	 */
	boolean filter(T obj);
}
