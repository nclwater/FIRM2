import org.jfree.ui.RefineryUtilities;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.view.TimeLineMainPanel;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TimeLineGenerator extends JFrame {
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        System.setProperty("java.util.logging.config.file", "C:\\Users\\janne\\IdeaProjects\\F2_\\DATA\\inputs\\timelinelogger.properties");

    }
    static org.slf4j.Logger  logger = LoggerFactory.getLogger(TimeLineGenerator.class);

    public TimeLineGenerator() {
        super("Timeline Generator Utility");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(new TimeLineMainPanel());
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
        setSize(1024, 768);
    }
    public static void main(String[] args) {
        String sourceFilename = System.getProperty("user.dir") +
                "/DATA/inputs/timelinelogger.properties";
        if (!Files.exists(Paths.get(sourceFilename))) {
            System.out.println("Logging configuration file " + sourceFilename + " not found");
        } else {
            URI configSourceUri = (new File(sourceFilename)).toURI();
            System.out.println("Logging config source URI: " + configSourceUri);
            logger.info("Logging model output to {}", configSourceUri);
            new TimeLineGenerator();
        }
    }

}
