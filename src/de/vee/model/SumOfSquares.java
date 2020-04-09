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

import java.util.Arrays;

import static de.vee.model.FunFactory.createFunction;

public class SumOfSquares implements RealValueFun {
    boolean considerDeathsOnly;
    public double[] v;
    private double[] xdata;
    private double[] ydata;
    private double[] deaths;
    double[] my;

    int imax;
    private double N;

    SumOfSquares(double[] xdata, double[] ydata, double[] deaths, double max, double N, double[] my) {
        this.xdata = xdata;
        this.ydata = ydata;
        this.deaths = deaths;
        this.N = N;
        this.my = my;
        setMax(max);
    }

    private void setMax(double max) {
        for (int i = 0; i < xdata.length; i++) {
            if (xdata[i] > max) break;
            imax = i;
        }
    }

    public double funk(double[] z) {
        int m = 0;
        for (int i = 0; i < imax; i++) {
            m = i;
            if (ydata[i] > 0) {
                break;
            }
        }
        double[] x = v;
        if (considerDeathsOnly) {
            System.arraycopy(z, 0, x, 4, z.length);
        } else {
            System.arraycopy(z, 0, x, 0, z.length);
        }
        LogisticFunc f = createFunction(x, N, xdata[m], ydata[m]);
        double sq = 0;
        double ymax = ydata[imax - 1];
        int n = 0;
        if (!considerDeathsOnly) {
            double yo = 0;
            double y1o = 0;
            for (int i = 0; i < imax; i++) {
                double d1 = f.evaluate(xdata[i]) / ymax;
                double d2 = ydata[i] / ymax;
                double dv = d1 - d2;
                sq += dv * dv; //cumulative
                double dr2 = d2 - yo;
                yo = d2;
                double dr1 = (d1 - y1o);
                y1o = d1;
                dv = dr1 - dr2;
                sq += dv * dv; //rate
            }
            n = 2 * imax;

            if (my[2] > 0) {//inflection point used for damping
                double inp0 = f.inflectionPoint(my);
                double inp1 = f.inflectionPoint(x);
                double urf = 0.7; //relaxation factor
                inp0 = (1. - urf) * inp0 + (urf) * inp1;
                double d = xdata[imax - 1] - inp0 - 12.; //time diff to inflection
                if (d > 0) { //we are close enough for reduction
                    d = 1.e-3 / d; //1e-3: only a little "hint"
                } else {
                    d = 1.e-3;
                }
//                double diff = (inp1-inp0)*d; //downscale
                double diff = Math.abs((inp1 - inp0) / inp1) * d; //downscale
                double sq1 = diff * diff;
                sq += sq1;
                n += 1;
            }
        }
        if ((deaths != null) && (considerDeathsOnly)) {
            ymax = deaths[imax - 1];
            double[] dy = DeathRate.getRate(xdata, f, x[4], x[5], x[6]);
            double[] dy0 = DeathRate.getDeaths(xdata, f, x[4], x[5], x[6]);
            double[] dr = Arrays.copyOf(deaths, imax);
            double ymax1 = 0;
            for (int i = dr.length - 1; i > 0; i--) {
                dr[i] -= dr[i - 1];
                if (ymax1 < dr[i]) ymax1 = dr[i];
            }
            if (ymax1 < 1.) ymax1 = 1.;
            for (int i = 0; i < imax; i++) {
                double d1 = dy[i] / ymax1;
                double d2 = dr[i] / ymax1;
                if (d2 > 0.) {
                    double v = d1 - d2;
                    sq += v * v;
                    n += 1;
                }
                //also include the cumulative curve to avoid zero percentage and/or shift to infinitive
                d1 = dy0[i] / ymax;
                d2 = deaths[i] / ymax;
                if (d2 > 0.) {
                    double v = d1 - d2;
                    sq += 1. / ymax * v * v;
                    n += 1;
                }
            }
        }
        return Math.sqrt(sq) / (n + 1);
    }

}
