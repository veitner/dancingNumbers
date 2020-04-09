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

public class Convolve {
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
     * @param x x-values
     * @param g function
     * @param percentage factor
     * @param shift shift
     * @param p p*shift = sigma
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
     * @param x x-values
     * @param y y-values
     * @param percentage factor
     * @param shift shift
     * @param p p*shift = sigma
     * @return f*g(t-days)
     */
    public static double[] eval(double[] x, double[] y, double percentage, double shift, double p) {
        int n = x.length;
        double[] y1 = new double[n];
        int bins = getBins(x, shift);
        Convolution c = new Convolution(Math.abs(p * shift), bins, shift);
        for (int i = 0; i < n; i++) {
            y1[i] = 0.;
            for (int k = 0; k < bins; k++) {
                int j = i + k;
                if (j > 1 && j < n)
                    if (y[j] < y[j - 1]) {
                        y[j] *= 1;
                    }
                if (j < n) {
                    y1[i] += c.getBin(k - bins / 2) * y[j];
                }
            }
            y1[i] *= (percentage * shift);
        }
        return y1;
    }

}
