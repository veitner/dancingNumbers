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

public abstract class Model {
    static final boolean VERBOSE = true;
    int start;
    final double[] x;
    final double[] y;
    final double[] dr;
    double[][] result;
    Input input;
    boolean initialized = false;
    double rms = 0;

    Model(double[] x, double[] y, double[] dr, Input input) {
        this.start = 0;
        this.x = x;
        this.y = y;
        this.dr = dr;
        this.input = input;
    }

    public double[][] getResult() {
        if (result == null) {
            optimize();
        }
        if (result == null) {
            System.out.println("Fit failed :(");
            throw new RuntimeException("Fit failed");
        }
        return result;
    }


    private boolean estimateInitialValues() {
        double x1 = 0;
        double y1 = -1;
        double x2 = 0;
        double y2 = 0;
        for (int i = 2; i < x.length; i++) { //2: ignore the first entries
            if (y1 < 0) {
                if (y[i] > 10) {
                    x1 = x[i];
                    y1 = y[i];
                    start = i;
                }
            } else {
                if ((y[i] > 20 * y1) || (i == x.length - 1)) {
                    x2 = x[i];
                    y2 = y[i];
                    break;
                }
            }
        }
        if ((y1 > 0) && (y2 > y1)) {
            input.v[0] = 0.02;
            double a0 = input.v[0];
            double N = input.N;
            input.v[1] = -Math.log(y2 / a0 / N) * Math.exp(-x2 * Math.log(Math.log(y2 / a0 / N) / Math.log(y1 / a0 / N)) / (x2 - x1));
            input.v[2] = -Math.log(Math.log(y2 / a0 / N) / Math.log(y1 / a0 / N)) / (x2 - x1);
            input.dels[1] = input.v[1] / 5.;
            input.dels[2] = input.v[2] / 5.;
            return true;
        }
        return false;
    }

    double[][] optimize() {
        initialized = estimateInitialValues();
        return null;
    }

    ;


    protected double[] reduceBy(double[] d, double f) {
        double[] h = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            h[i] = f * d[i];
        }
        return h;
    }


    public abstract double getDeathRate(double needle);

    public abstract double getShift(double needle);

    public abstract double getP(double needle);

    public int getStart() {
        return this.start;
    }
}
