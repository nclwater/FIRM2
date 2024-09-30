import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ncl.nclwater.firm2.firm2.Firm2;
import uk.ac.ncl.nclwater.firm2.firm2.model.FloodModelParameters;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.net.URI;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static FloodModelParameters floodModelParameters;

    /**
     * This is where the program starts
     */
    public static void main(String[] args) {
        URI configSourceUri = new File(System.getProperty("user.dir") +
                "/DATA/inputs/log4j2.properties").toURI();
        System.out.println("Log4j config source URI: " + configSourceUri);
        logger.info("Logging model output to {}",configSourceUri);
        Configurator.reconfigure(configSourceUri);

        new Firm2();
    }
}
