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
