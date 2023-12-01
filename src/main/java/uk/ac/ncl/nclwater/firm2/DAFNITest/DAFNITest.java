package uk.ac.ncl.nclwater.firm2.DAFNITest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * DAFNITest class
 *
 * Models running in a Docker container in DAFNI will have data available
 * in the following directories mounted from the host:
 *   /data          - host storage mount point
 *   /data/inputs   - created by the host, always present
 *   /data/outputs  - must be created by the model
 *
 * When running this test application locally, the /data and /data/inputs
 * directories should be created elsewhere (in the Docker container, by the
 * build process, or manually), to mimic DAFNI's behaviour.
 *
 * This application will create /data/outputs, if it does not already exist.
 * This directory may already have been created in the Docker container,
 * manually, or in accompanying code (e.g. a shell script).
 */
public class DAFNITest {
    public static void main(String[] args) {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        /* Parameters are passed into DAFNI models using environment variables */
        Map<String, String> environment = System.getenv(); /* Not needed ? */
        String app_data_root = System.getenv().getOrDefault("app_data_root", "data");
        String app_data_inputs = System.getenv().getOrDefault("app_data_inputs", app_data_root + "/inputs");
        String app_data_outputs = System.getenv().getOrDefault("app_data_outputs", app_data_root + "/outputs");

        /* Check for /data and /data/inputs */
        if (!new File(app_data_root).isDirectory()) {
            System.out.println("Data root directory does not exist: " + app_data_root);
            System.exit(1);
        }
        else if (!new File(app_data_inputs).isDirectory()) {
            System.out.println("Data inputs directory does not exist: " + app_data_inputs);
            System.exit(1);
        }

        /* Create /data/outputs if necessary */
        if (!new File(app_data_outputs).isDirectory()) {
            try {
                if (!new File(app_data_outputs).mkdirs()) {
                    System.out.println("Couldn't create outputs directory: " + app_data_outputs);
                    System.exit(1);
                }
            } catch (SecurityException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
                System.exit(-1);
            }
        }

        long currentTimeMillis = System.currentTimeMillis();
        try {
            File myObj = new File(app_data_inputs + "/" + "input_" + currentTimeMillis + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getAbsolutePath());
            } else {
                System.out.println("File " + myObj.getAbsolutePath() + " already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
            System.exit(-1);
        }

        currentTimeMillis = System.currentTimeMillis();
        try {
            File myObj = new File(app_data_outputs + "/" + "output_" + currentTimeMillis + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getAbsolutePath());
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
