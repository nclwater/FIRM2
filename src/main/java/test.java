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