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
        System.setProperty("java.util.logging.config.file", "C:\\Users\\janne\\IdeaProjects\\F2_\\DATA\\inputs\\logging.properties");

    }
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static FloodModelParameters floodModelParameters;

    /**
     * This is where the program starts
     */
    public static void main(String[] args) {
        String sourceFilename = System.getProperty("user.dir") +
                "/DATA/inputs/log4j2.properties";
        if (!Files.exists(Paths.get(sourceFilename))) {
            System.out.println("Log4J configuration file " + sourceFilename + " not found");
        } else {
            URI configSourceUri = (new File(sourceFilename)).toURI();
            System.out.println("Log4j config source URI: " + configSourceUri);
            logger.info("Logging model output to {}", configSourceUri);
            new Firm2();
        }
    }
}
