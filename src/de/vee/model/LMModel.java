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

import datan.*;
import datangraphics.DatanGraphics;

import java.util.Arrays;
import java.util.Locale;

import static de.vee.model.Bisection.find;
import static de.vee.model.FunFactory.createFunction;

public class LMModel extends Model {

    private double mf;
    private DatanMatrix cx;

    LMModel(double[] x, double[] y, double[] dr, Input input) {
        super(x, y, dr, input);
    }

    void writeLine(String text) {
        System.out.println(text);
    }

    double[][] optimize() {
        super.optimize();
        if (!initialized) return null;
        result = new double[this.x.length][input.v.length];
        LsqFunction f = new Gompertz(input.v, input.N);
// perform fit
        DatanVector tt = new DatanVector(this.x);
        DatanVector yy = new DatanVector(this.y);
        double[] deltayy = new double[tt.getNumberOfElements()];
        Arrays.setAll(deltayy, operand -> 1.);
        DatanVector dyy = new DatanVector(deltayy);
        int[] selector = new int[tt.getNumberOfElements()];
        Arrays.setAll(selector, operand -> operand > start ? 0 : 1);

        int[] selector1 = {1, 1, 1, 1, 0, 0, 0};
        int[] selector2 = {0, 0, 0, 0, 1, 1, 1};
        double[] my = {0, 0, 0, 0, 0, 0, 0};
        DatanVector x = new DatanVector(input.v).getSubvector(selector1); // first approximation
        int[] list = new int[x.getNumberOfElements()];
        Arrays.setAll(list, operand -> operand < 3 ? 1 : 0);
        double[] deaths = dr;
        for (int j = start; j < tt.getNumberOfElements(); j++) {
            selector[j] = 1;
            int finalJ = j;
            boolean considerDeathsOnly = false;
            double N = input.N;
            LsqMar lm = new LsqMar(tt.getSubvector(selector), yy.getSubvector(selector), dyy.getSubvector(selector), x, list, f) {
                @Override
                protected double minimumFunction(DatanVector z) {
                    double[] zz = z.toArray();
                    double[] ydata = y;
                    double[] xdata = LMModel.super.x;
                    int imax = finalJ;
                    int m = 0;
                    for (int i = 0; i < imax; i++) {
                        m = i;
                        if (ydata[i] > 0) {
                            break;
                        }
                    }
                    double[] x = Arrays.copyOf(input.v, input.v.length);
                    if (considerDeathsOnly) {
                        System.arraycopy(zz, 0, x, 4, zz.length);
                    } else {
                        System.arraycopy(zz, 0, x, 0, zz.length);
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
                            if (d > 1) { //we are close enough for reduction
                                d = 1.e-3 * Math.pow(1.e1, -d);
                            } else {
                                d = 1.e-3; //1e-3: only a little "hint"
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
                            if (ymax < 1.) ymax = 1.;
                            d1 = dy0[i] / ymax;
                            d2 = deaths[i] / ymax;
                            if (d2 > 0.) {
                                double v = d1 - d2;
                                sq += 1. / ymax * v * v;
                                n += 1;
                            }
                        }
                    }
                    double[] min = {1e-4, 10, 1e-3, 1};
                    for (int i = 0; i < zz.length; i++) {
                        if (zz[i] < min[i]) {
                            double d = Math.abs(zz[i] - min[i]) / min[i];
                            sq *= d;
                        }
                    }
                    return Math.sqrt(sq) / (n + 1);

                }
            };
            lm = new LsqMar(tt.getSubvector(selector), yy.getSubvector(selector), dyy.getSubvector(selector), x, list, f);

// write results
            writeLine("\n Fit: First Approximation x = " + x.toString());
            mf = lm.getChiSquare();
            writeLine("Minimum function M = " + String.format(Locale.US, "%10.5f", mf));
            x = lm.getResult();
            writeLine("Result x = " + x.toString());
            cx = lm.getCovarianceMatrix();
            if (cx != null) {
                writeLine("CovarianceMatrix cx = ");
                writeLine(cx.toString());
            }
            double[] r = x.toArray();
            int k = r.length;
            for (int i = 0; i < selector1.length; i++) {
                int l = i;
                if (selector1[i] == 1) {
                    if (i >= k) {
                        l = i - k;
                    }
                    result[j][i] = r[l];
                }
            }
            // asymmetric errors
            LsqAsm la = new LsqAsm(tt.getSubvector(selector), yy.getSubvector(selector), dyy.getSubvector(selector), x, list, cx, mf, f);
            double[][] dxasy = la.getAsymmetricErrors(0.);
            DatanMatrix as = new DatanMatrix(dxasy);
            writeLine("Asymmetic errors:");
            writeLine(as.toString());
//            plotDataAndFittedCurve();
//            if (j==tt.getNumberOfElements()-1)
//            plotParameterPlane(j, x, tt.getSubvector(selector), yy.getSubvector(selector), dyy.getSubvector(selector), dxasy, f);
        }
        return result;
    }


    @Override
    public double getDeathRate(double needle) {
        int i = find(x, needle);
        return result[i][4];
    }

    @Override
    public double getShift(double needle) {
        int i = find(x, needle);
        return result[i][5];
    }

    @Override
    public double getP(double needle) {
        int i = find(x, needle);
        return result[i][6];
    }

    protected void plotParameterPlane(int n, DatanVector x, DatanVector t, DatanVector y, DatanVector deltay, double[][] dxasy, LsqFunction f) {
        double x1 = x.getElement(0);
        double x2 = x.getElement(1);
        double dx1 = Math.sqrt(cx.getElement(0, 0));
        double dx2 = Math.sqrt(cx.getElement(1, 1));
        double rho = (cx.getElement(1, 0)) / (dx1 * dx2);
// prepare size of plot
        double xmin = x1 - 2. * dx1;
        double xmax = x1 + 2. * dx1;
        double ymin = x2 - 2. * dx2;
        double ymax = x2 + 2. * dx2;
        DatanGraphics.openWorkstation(getClass().getName(), getClass().getName() + "_ParameterPlane.ps");
        DatanGraphics.setFormat(0., 0.);
        DatanGraphics.setWindowInComputingCoordinates(xmin, xmax, ymin, ymax);
        DatanGraphics.setViewportInWorldCoordinates(.2, .9, .16, .86);
        DatanGraphics.setWindowInWorldCoordinates(-.414, 1., 0., 1.);
        DatanGraphics.setBigClippingWindow();
        DatanGraphics.chooseColor(2);
        DatanGraphics.drawFrame();
        DatanGraphics.drawScaleX("x_1");
        DatanGraphics.drawScaleY("x_2");
        DatanGraphics.drawBoundary();
        DatanGraphics.chooseColor(5);
// draw data point with errors (and correlation)
        DatanGraphics.drawDatapoint(1, 1., x1, x2, dx1, dx2, rho);
        DatanGraphics.chooseColor(2);
        DatanGraphics.drawCaption(1., "Parameter plane");
// draw confidence contour
        double fcont = mf + 1.;
        int nx = 100;
        int ny = 100;
        double dx = (xmax - xmin) / (int) nx;
        double dy = (ymax - ymin) / (int) ny;
        myContourUserFunction mcuf = new myContourUserFunction(n, x, t, y, deltay, f);
        DatanGraphics.setBigClippingWindow();
        DatanGraphics.chooseColor(1);
        DatanGraphics.drawContour(xmin, ymin, dx, dy, nx, ny, fcont, mcuf);
// draw asymmetric errors as horizontal and vertical bars
        DatanGraphics.chooseColor(3);
        double[] xpl = new double[2];
        double[] ypl = new double[2];
        for (int i = 0; i < 2; i++) {
            if (i == 0) xpl[0] = x1 - dxasy[0][0];
            else xpl[0] = x1 + dxasy[0][1];
            xpl[1] = xpl[0];
            ypl[0] = ymin;
            ypl[1] = ymax;
            DatanGraphics.drawPolyline(xpl, ypl);
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) ypl[0] = x2 - dxasy[1][0];
            else ypl[0] = x2 + dxasy[1][1];
            ypl[1] = ypl[0];
            xpl[0] = xmin;
            xpl[1] = xmax;
            DatanGraphics.drawPolyline(xpl, ypl);
        }
        DatanGraphics.closeWorkstation();
    }

    private class myContourUserFunction extends DatanUserFunction {
        private final DatanVector x;
        private final DatanVector t;
        private final DatanVector y;
        private final DatanVector dy;
        private final LsqFunction f;
        int n;

        public myContourUserFunction(int n, DatanVector x, DatanVector t, DatanVector y, DatanVector dy, LsqFunction f) {
            this.n = n;
            this.t = t;
            this.y = y;
            this.dy = dy;
            this.f = f;
            this.x = x;
        }

        public double getValue(double x1, double x2) {
            double r;
            DatanVector a = new DatanVector(x);
            a.setElement(0, x1);
            a.setElement(1, x2);
            r = 0.;
            for (int i = 0; i < n; i++) {
                double d = (y.getElement(i) - f.getValue(a, t.getElement(i))) / dy.getElement(i);
                double s = d * d;
                r = r + s;
            }
            return r;
        }
    }


}
