import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class test {
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        System.setProperty("java.util.logging.config.file", "/home/jannetta/IdeaProjects/FIRM2/DATA/inputs/timelinelogger.properties");

    }
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {

        logger.debug("This is a debug message");
        logger.trace("This is a trace message");
        logger.info("This is an info message");
        logger.error("This is a error message");
        logger.warn("This is a warn message");


    }

}