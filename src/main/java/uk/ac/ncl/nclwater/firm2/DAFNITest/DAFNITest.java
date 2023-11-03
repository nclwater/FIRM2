package uk.ac.ncl.nclwater.firm2.DAFNITest;

import java.io.File;
import java.io.IOException;

public class DAFNITest {

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            File myObj = new File("/data/inputs/input_" + currentTimeMillis + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getAbsolutePath());
            } else {
                System.out.println("File " + myObj.getAbsolutePath() + " already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        currentTimeMillis = System.currentTimeMillis();
        try {
            File myObj = new File("/data/outputs/output_" + currentTimeMillis + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
