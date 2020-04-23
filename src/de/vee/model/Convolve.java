/*
 * Dancing Numbers - Logistic growth applied to Covid-19
 *
 * A naive approach to predict the spread of virus
 *
 *
 * Copyright (c) 2020 V. Eitner
 *
 * This file is part of "Dancing Numbers".
 *
 * "Dancing Numbers" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * "Dancing Numbers" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Dancing Numbers".  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package de.vee.model;

import java.util.Arrays;

public class Convolve {
    private double[] x = null;
    private LogisticFunc g = null;

    public Convolve() {
        super();
    }

    public Convolve(double[] x, LogisticFunc g) {
        this.x = x;
        this.g = g;
    }


    private static int getBins(double[] x, double days) {
        int bins = 0;
        double x0 = x[x.length - 1];
        for (int i = x.length - 2; i >= 0; i--) {
            bins++;
            if (x0 - x[i] > 2 * days) break; //aequidistant
        }
        return bins;
    }

    /**
     * Convolution to calculate death rate - look back
     *
     * @param x          x-values
     * @param g          function
     * @param percentage factor
     * @param shift      shift
     * @param p          p*shift = sigma
     * @return f*g(t-days)
     */
    static double[] eval(double[] x, LogisticFunc g, double percentage, double shift, double p) {
        int n = x.length;
        double[] y1 = new double[n];
        int bins = getBins(x, shift);
        Convolution c = new Convolution(Math.abs(p * shift), bins, shift);
        for (int i = 0; i < n; i++) {
            y1[i] = 0.;
            for (int k = 0; k < bins; k++) {
                int j = i - bins + k + 1;
                if (j > -1) {
                    y1[i] += c.getBin(k - bins / 2) * g.derivative(x[j]);
                }
            }
            y1[i] *= percentage;
        }
        return y1;
    }

    /**
     * Convolution to estimate ICU - look ahead
     *
     * @param x          x-values
     * @param y          y-values
     * @param percentage factor
     * @param shift      shift
     * @param p          p*shift = sigma
     * @return f*g(t-days)
     */
    public static double[] eval(double[] x, double[] y, double percentage, double shift, double p) {
        return eval(x, y, percentage, shift, p, getBins(x, shift));
    }

    public static double[] eval(double[] x, double[] y, double percentage, double shift, double p, int bins) {
        int n = x.length;
        double[] y1 = new double[n];
        Convolution c = new Convolution(Math.abs(p * shift), bins, shift);
        for (int i = 0; i < n; i++) {
            y1[i] = 0.;
            for (int k = 0; k < bins; k++) {
                int j = i + k;
                if (j < n) {
                    y1[i] += c.getBin(k - bins / 2) * y[j];
                }
            }
            y1[i] *= percentage;
        }
        return y1;
    }

    public static double[][] smooth(double[] x, double[] y) {
        double dx = 1.;
        double[] x1 = new double[x.length * 2 + 30];
        double[] y1 = new double[x.length * 2 + 30];
        int i = 0;
        int k = 0;
        while (i < x.length) {
            x1[k] = x[i];
            y1[k] = y[i];
            if (i == x.length - 1) break;
            while (x[i + 1] > x1[k]) {
                k++;
                x1[k] = x1[k - 1] + dx;
            }
            i++;
        }

        y1 = eval(x1, y1, 1., 7., 1., 7); //smooth twice
//        y1 = eval(x1, y1, 1., 7., 1., 7);

        double fx = 0.85;
        for (i = Math.max(0, k - 7); i < Math.min(y.length, k + 2 * 15); i++) {
            y1[i] = fx * y1[i - 1] + (1 - fx) * y1[i]; //simply weigh the last points
        }
        k += 1;

        double[][] result = new double[2][k];
        result[0] = Arrays.copyOf(x1, k);
        result[1] = Arrays.copyOf(y1, k);
        return result;
    }

}
