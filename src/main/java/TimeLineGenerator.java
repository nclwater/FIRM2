/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.view.TimeLineMainPanel;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TimeLineGenerator extends JFrame {
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        System.setProperty("java.util.logging.config.file", "\\data\\inputs\\timelinelogger.properties");

    }
    static Logger logger = LoggerFactory.getLogger(TimeLineGenerator.class);

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
                "/timelinelogger.properties";
//        String sourceFilename = "/data/inputs/timelinelogger.properties";
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
