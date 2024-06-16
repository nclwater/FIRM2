package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils;

/**
 * A static class to produce agent IDs
 */
public class AgentIDProducer {

    private static int ids = 0;

    /**
     * Increment the id value
     *
     * @return a new id
     */
    public static int getNewId() {
        return ++ids;
    }
}
