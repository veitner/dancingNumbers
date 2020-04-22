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

public class SuperPose implements LogisticFunc {
    static double SIGMA = 3.5;//1.;
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
        }
        if (x.length > n * Gompertz.DOF() + 3) {
            sigma = Math.max(1.e-3, x[n * Gompertz.DOF()]); //one sigma to blend all the elements
        } else {
            sigma = SIGMA;
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

}
