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

public class SumOfSquares implements RealValueFun {
    double[][] ctrs;
    double[] slice;
    final double[] xdata;
    final double[] ydata;
    double[] my;

    int imax;
    protected double N;

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

    public double funk(double[] x) {
        double penalty = getPenalty(x);
        LogisticFunc f = new SuperPose(x, slice, N);//createFunction(x, N);
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
        return penalty * sq;
//        return penalty * Math.sqrt(sq) / (n + 1);
    }

}
