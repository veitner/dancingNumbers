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

import com.nr.RealValueFun;

//public class SumOfSquares implements RealValueFun, MultivariateFunction {
public class SumOfSquares implements RealValueFun {
    double[][] ctrs;
    double[] slice;
    final double[] xdata;
    final double[] ydata;
    double[] my;
    private double chisq;
    private double theil;
    private double rms;

    int imax;
    protected double N;
    private double corrC = -1;
    private boolean stats_ok = false;

    /*
        SimpleBounds getSimpleBounds() {
            double[] lb = new double[ctrs.length];
            double[] ub = new double[ctrs.length];
            for (int i=0; i<lb.length; i++) {
                lb[i] = ctrs[i][0];
                ub[i] = ctrs[i][1];
            }
            return new SimpleBounds(lb,ub);
        }
    */
    SumOfSquares(double[] xdata, double[] ydata, double max, double N, double[] my, double[][] ctrs, double[] slice) {
        this.xdata = xdata;
        this.ydata = ydata;
        this.N = N;
        this.my = my;
        this.ctrs = ctrs;
        this.slice = slice;
        setMax(max);
    }

    private void setMax(double max) {
        for (int i = 0; i < xdata.length; i++) {
            if (xdata[i] > max) break;
            imax = i;
        }
    }

    double getPenalty(double[] x) {
        double penalty = 1.;
        for (int i = 0; i < x.length; i++) {
            if (x[i] < ctrs[i][0]) {
                double d = Math.abs(x[i] - ctrs[i][0]);
                penalty *= (10. + d);
                x[i] = ctrs[i][0];
            } else {
                if (x[i] > ctrs[i][1]) {
                    double d = Math.abs(x[i] - ctrs[i][1]);
                    penalty *= (1. + d);
                    x[i] = ctrs[i][1];
                }
            }
        }
        return penalty;
    }

    //    @Override
    public double value(double[] point) {
        return funk(point);
    }

    private void stats(double[] x, double[] y) {
        chisq = 0.0;
        theil = 0.0;
        double ysq = 0.0;
        int n = Math.min(x.length, y.length);
        for (int i = 0; i < n; ++i) {
            chisq += (x[i] - y[i]) * (x[i] - y[i]);
            ysq += (y[i] * y[i]);
        }
        theil = Math.sqrt(chisq / ysq);

        rms = 0.0;
        for (int i = 0; i < n; ++i) {
            rms += ((x[i] - y[i]) * (x[i] - y[i]) + 1.) /
                    (y[i] * y[i] + 1.);
        }
        rms = Math.sqrt(rms / n);
//        rms = 100.0 * Math.sqrt(rms / n);
        double[] cor = {corrC};
        if (correlation(x, y, n, cor) == 0) {
            corrC = cor[0];
        }
    }

    /*
	compute mean and standard dev
*/
    private void stasum(double[] x, int n, double[] xbar, double[] sd) {
        int i;
        xbar[0] = 0;
        sd[0] = 0;
        if (x == null) {
            return;
        }
        if (n < 1) {
            return;
        }
        for (i = 0; i < n; i++) {
            xbar[0] = (xbar[0]) + x[i];
        }
        xbar[0] = (xbar[0]) / n;
        if (n > 1) {
            for (i = 0; i < n; i++) {
                sd[0] = (sd[0]) + (x[i] - xbar[0]) * (x[i] - xbar[0]);
            }
            sd[0] = Math.sqrt(sd[0] / (n - 1));
        }
    }


    /*
     * find correlation coefficient
     */
    private int correlation(double[] x, double[] y, int n, double[] cor) {
        double[] xbar = {0}, xsd = {0};
        double[] ybar = {0}, ysd = {0};
        int i;
        cor[0] = 0.0;
        if (n < 2) {
            return -1;
        }
        stasum(x, n, xbar, xsd);
        stasum(y, n, ybar, ysd);
        if (xsd[0] == 0.0 || ysd[0] == 0.0) {
            return -1;
        }
        for (i = 0; i < n; i++) {
            cor[0] += (x[i] - xbar[0]) * (y[i] - ybar[0]);
        }
        cor[0] /= ((n - 1) * xsd[0] * ysd[0]);
        return 0;
    }

    public double funk(double[] x) {
        stats_ok = false;
        double penalty = getPenalty(x);
        LogisticFunc f = FunFactory.createFunction(x, slice, N);//createFunction(x, N);
        double sq = 0;
        double yo = 0;
        double y1o = 0;
        for (int i = 0; i < imax; i++) {
            double d1 = f.evaluate(xdata[i]);
            double d2 = ydata[i];
            double dv = d1 - d2;
            sq += dv * dv; //cumulative
            double dr2 = d2 - yo;
            yo = d2;
            double dr1 = (d1 - y1o);
            y1o = d1;
            dv = dr1 - dr2;
            sq += dv * dv; //rate
        }

        if (my[2] > 0) {//inflection point used for damping
            double inp0 = f.inflectionPoint(my);
            double inp1 = f.inflectionPoint(x);
            double urf = 0.7; //relaxation factor
            inp0 = (1. - urf) * inp0 + (urf) * inp1;
            double d = xdata[imax - 1] - inp0 - 12.; //time diff to inflection
            if (d > 1) { //we are close enough for reduction
                d = 1.e-3 * Math.pow(1.e1, -d);
            } else {
                d = 1.e-3; //1e-3: only a little "hint"
            }
//                double diff = (inp1-inp0)*d; //downscale
            double diff = Math.abs((inp1 - inp0) / inp1) * d; //downscale
            double sq1 = diff * diff;
            sq += sq1;
        }
//        return penalty * sq;
        return penalty * Math.sqrt(sq) / (2 * imax + 1);
    }

    void calculateStats(double[] x, LogisticFunc f) {
        double[] y = new double[imax];
        for (int i = 0; i < imax; i++) {
            y[i] = f.evaluate(xdata[i]);
        }
        stats(y, ydata);
        stats_ok = true;
    }

    double getTheil() {
        return stats_ok ? theil : -1;
    }

    double getChisq() {
        return stats_ok ? chisq : -1;
    }

    double getRms() {
        return stats_ok ? rms : -1;
    }

    double getCorrC() {
        return stats_ok ? corrC : -1;
    }

    String printStats(double[] x) {
        LogisticFunc f = FunFactory.createFunction(x, slice, N);
        calculateStats(x, f);
        return "y = " + f.printFormula() + String.format("\nChi-Square: %g; Correlation coefficient: %f; RMS error: %g; Theil U coefficient: %g;", chisq, corrC, rms, theil);
    }

}
