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


package uk.ac.ncl.nclwater.firm2.examples;

import java.lang.Math;

public class BNGToLatLonConverter {




    /**
     * Calculate the distance between two pairs of British National Grid co-ordinates
     * @param E1 - first easting co-ordinate
     * @param N1 - first northing co-ordinat
     * @param E2 - second easting co-ordinate
     * @param N2 - second northing co-ordinate
     * @return the difference between two pairs of co-ordinates
     */
    public static double calculateDistance(double E1, double N1, double E2, double N2) {
        double dE = E2 - E1;
        double dN = N2 - N1;
        return Math.sqrt(dE * dE + dN * dN);
    }


    /**
     * Convert British National Grid co-ordinates to longitude and latitude
     * @param E - easting co-ordinate
     * @param N - northing co-ordinate
     * @return the longitude and latitude as an array[2] of double
     */
    public static double[] BNGToLatLon(double E, double N) {
        // Define constants for the transformation
        final double a = 6377563.396;  // Airy 1830 major semi-axis
        final double b = 6356256.910;  // Airy 1830 minor semi-axis
        final double F0 = 0.9996012717;  // scale factor on the central meridian
        final double lat0 = 49 * Math.PI / 180;  // Latitude of true origin (radians)
        final double lon0 = -2 * Math.PI / 180;  // Longitude of true origin and central meridian (radians)
        final double N0 = -100000;  // Northing of true origin (m)
        final double E0 = 400000;  // Easting of true origin (m)
        final double e2 = 1 - (b * b) / (a * a);  // eccentricity squared
        final double n = (a - b) / (a + b);
        final double n2 = n * n;
        final double n3 = n * n * n;

        // Initial calculations
        double lat = lat0;
        double M = 0;

        while (Math.abs(N - N0 - M) >= 0.00001) {  // Accuracy of < 0.01mm
            lat = (N - N0 - M) / (a * F0) + lat;
            M = b * F0 * (
                    (1 + n + (5.0 / 4.0) * n2 + (5.0 / 4.0) * n3) * (lat - lat0)
                            - (3 * n + 3 * n2 + (21.0 / 8.0) * n3) * Math.sin(lat - lat0) * Math.cos(lat + lat0)
                            + ((15.0 / 8.0) * n2 + (15.0 / 8.0) * n3) * Math.sin(2 * (lat - lat0)) * Math.cos(2 * (lat + lat0))
                            - (35.0 / 24.0) * n3 * Math.sin(3 * (lat - lat0)) * Math.cos(3 * (lat + lat0))
            );
        }

        // Calculate longitude
        double cosLat = Math.cos(lat);
        double sinLat = Math.sin(lat);
        double nu = a * F0 / Math.sqrt(1 - e2 * sinLat * sinLat);
        double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * sinLat * sinLat, 1.5);
        double eta2 = nu / rho - 1;

        double tanLat = Math.tan(lat);
        double tan2lat = tanLat * tanLat;
        double tan4lat = tan2lat * tan2lat;
        double tan6lat = tan4lat * tan2lat;
        double secLat = 1 / cosLat;
        double nu3 = nu * nu * nu;
        double nu5 = nu3 * nu * nu;
        double nu7 = nu5 * nu * nu;

        double VII = tanLat / (2 * rho * nu);
        double VIII = tanLat / (24 * rho * nu3) * (5 + 3 * tan2lat + eta2 - 9 * eta2 * tan2lat);
        double IX = tanLat / (720 * rho * nu5) * (61 + 90 * tan2lat + 45 * tan4lat);
        double X = secLat / nu;
        double XI = secLat / (6 * nu3) * (nu / rho + 2 * tan2lat);
        double XII = secLat / (120 * nu5) * (5 + 28 * tan2lat + 24 * tan4lat);
        double XIIA = secLat / (5040 * nu7) * (61 + 662 * tan2lat + 1320 * tan4lat + 720 * tan6lat);

        double dE = E - E0;
        double dE2 = dE * dE;
        double dE3 = dE2 * dE;
        double dE4 = dE3 * dE;
        double dE5 = dE4 * dE;
        double dE6 = dE5 * dE;
        double dE7 = dE6 * dE;

        lat = lat - VII * dE2 + VIII * dE4 - IX * dE6;
        double lon = lon0 + X * dE - XI * dE3 + XII * dE5 - XIIA * dE7;

        lat = lat * 180 / Math.PI;
        lon = lon * 180 / Math.PI;

        return new double[]{lat, lon};
    }

    public static void main(String[] args) {
        double E1 = 294191.255;  // Easting
        double N1 = 378471.609;  // Northing
        double[] latLon1 = BNGToLatLon(E1, N1);
        System.out.println("Latitude: " + latLon1[0] + ", Longitude: " + latLon1[1]);
        double E2 = 294173.568;
        double N2 = 378465.056;
        double[] latLon2 = BNGToLatLon(E2, N2);
        System.out.println("Latitude: " + latLon2[0] + ", Longitude: " + latLon2[1]);
        double distance = calculateDistance(E1, N1, E2, N2);
        System.out.println("distance = " + distance + "m");
    }
}
