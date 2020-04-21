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

import java.util.Locale;

public class SuperPose implements LogisticFunc {
    private static final double CUT = 1E-9;
    private static final double DELTA = 1E-6;
    int n;
    Gompertz[] f;
    Box[] b;
    double[][] a;
    double sigma = 1;

    public SuperPose(double[] x, double[] slice, double N) {
        super();
        if (slice != null) {
            n = slice.length + 1;
        } else {
            n = 1;
        }
        double[] sl = new double[n];
        if (slice != null) {
            System.arraycopy(slice, 0, sl, 0, sl.length - 1);
        }
        sl[sl.length - 1] = 1e4;
        f = new Gompertz[n];
        b = new Box[n];
        a = new double[n][Gompertz.DOF()];
        for (int i = 0; i < n; i++) {
            int ii = i * Gompertz.DOF();
            a[i][0] = x[ii];
            a[i][1] = x[ii + 1];
            a[i][2] = x[ii + 2];
//            a[i][3] = x[ii + 3];
        }
        if (x.length > n * Gompertz.DOF() + 3) {
            sigma = Math.max(1.e-3, x[n * Gompertz.DOF()]); //one sigma to blend all the elements
        } else {
            sigma = 3.5;//1.;
        }
        double a0 = -1e3;
        for (int i = 0; i < n; i++) {
            f[i] = new Gompertz(a[i], N);
            double b0 = sl[i];
            b[i] = new Box(a0, b0);
            a0 = b0;
        }
    }

    @Override
    public double evaluate(double t) {
        double r = 0.;
        if (n < 2) return f[0].evaluate(t);
        for (int i = 0; i < n; i++) {
            r += f[i].evaluate(t) * b[i].f(sigma, t);
        }
        return r;
    }

    @Override
    public double derivative(double t) {
        double arg = Math.abs(t);
        if (arg < CUT) arg = CUT;
        double del = DELTA * arg;
        double fp = evaluate(t + del);
        double fm = evaluate(t - del);
        return (fp - fm) / (del + del);
    }

    @Override
    public double inflectionPoint(double[] x) {
        return 0;
    }

    @Override
    public double[][] getSeriesData(int maxDays) {
        double[][] data = new double[2][maxDays + 1];
        for (int i = 0; i <= maxDays; i++) {
            double y = evaluate(i);
            data[0][i] = i;
            data[1][i] = y;
        }
        return data;
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        double[] x = {0.004309, 61.136566, 0.057402, 0.516606, 0.003458, 382.962434, 0.068848, 19.998910};
        x = new double[]{0.005544, 52.482711, 0.052924, 0.514993, 0.003541, 471.445919, 0.071033, 19.999095};
        x = new double[]{0.001938, 2622.559772, 0.087320, 19.812765, 0.092982, 23.023833, 0.017190, 19.998927};
        x = new double[]{0.001950, 1059.220288, 0.087082, 9.590092, 0.064376, 19.510918, 0.018663, 8.202688, 0.001961, 4999.999996, 0.116769, 0.653546, 0.001934, 1045.000705, 0.087083, 10.795184};
//        double[] slice = {60};
        double[] slice = {68, 77, 83};
        SuperPose sc = new SuperPose(x, slice, 1e8);
        for (int i = 0; i < 150; i++) {
            double t = i;
            double y = sc.evaluate(t);
            System.out.printf("%f %f\n", t, y);
        }
    }
}
