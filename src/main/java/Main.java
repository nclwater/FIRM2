import uk.ac.ncl.nclwater.firm2.firm2.Firm2;
import uk.ac.ncl.nclwater.firm2.firm2.model.FloodModelParameters;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        String sourceFilename ="logging.properties";
        System.setProperty("java.util.logging.config.file", sourceFilename);

    }
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static FloodModelParameters floodModelParameters;

    /**
     * This is where the program starts
     */
    public static void main(String[] args) {
        String sourceFilename = "logging.properties";
        if (!Files.exists(Paths.get(sourceFilename))) {
            System.out.println("Logging configuration file " + sourceFilename + " not found");
            System.exit(1);
        } else {
            URI configSourceUri = (new File(sourceFilename)).toURI();
            logger.info("Logging model output to {}", configSourceUri);
            new Firm2();
        }
    }
}
