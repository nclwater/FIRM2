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


package uk.ac.ncl.nclwater.firm2.Logging;

import java.io.IOException;
import java.util.logging.FileHandler;

public class CustomHandler extends FileHandler {

    public CustomHandler() throws IOException, SecurityException {
    }

    public CustomHandler(String pattern) throws IOException, SecurityException {
        super(pattern);
    }

    public CustomHandler(String pattern, boolean append) throws IOException, SecurityException {
        super(pattern, append);
    }

    public CustomHandler(String pattern, int limit, int count) throws IOException, SecurityException {
        super(pattern, limit, count);
    }

    public CustomHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
        super(pattern, limit, count, append);
    }

    public CustomHandler(String pattern, long limit, int count, boolean append) throws IOException {
        super(pattern, limit, count, append);
    }
}
