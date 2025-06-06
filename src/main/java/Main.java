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
