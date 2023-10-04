import java.util.HashMap;

/**
 * Helper function to store the Key-value pair
 */
public class KeyValueStore {
    private final HashMap<String, String> operation = new HashMap<>();

    /**
     * Handles put operator
     * @param key Key value for storage
     * @param value value intended to store
     * @param putCount to keep track of 5 count or not
     * @return the response statement that we want to return to the client
     */
    public synchronized String put(String key, String value, int putCount) {
        if (operation.containsKey(key)) {
            return "Key already exist, try another key.";
        } else {
            operation.put(key, value);
            putCount++;
            return putCount + "PUTs*OK saved operation: {key= " + key + ", value= " + value + "}\n" + "Current operations: " + operation;
        }
    }

    /**
     * Handles get operator
     * @param key Key value intended for retrieval
     * @param getCount to keep track of 5 count or not
     * @return the response statement that we want to return to the client
     */
    public synchronized String get(String key, int getCount) {
        if (!operation.containsKey(key)) {
            return "Key does not exist.";
        } else {
            operation.get(key);
            getCount++;
            return getCount + "GETs*Here is your value " + operation.get(key);
        }
    }

    /**
     * Handles del operator
     * @param key Key value intended for removal
     * @param delCount to keep track of 5 count or not
     * @return the response statement that we want to return to the client
     */
    public synchronized String delete(String key, int delCount) {
        if (!operation.containsKey(key)) {
            return "Key does not exist.\noperations left: " + operation;
        } else {
            operation.remove(key);
            delCount++;
            return delCount + "DELETEs*Deleted key as requested.\nOperations left: "+ operation;
        }
    }
}