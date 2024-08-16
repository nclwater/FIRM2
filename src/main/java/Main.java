import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.Firm2;
import uk.ac.ncl.nclwater.firm2.firm2.model.FloodModelParameters;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static FloodModelParameters floodModelParameters;    /**
     * This is where the program starts
     */
    public static void main(String[] args) {
        System.setProperty("log4j.debug", "");
        logger.debug("Simulation starting ...");
        new Firm2();
    }
}
