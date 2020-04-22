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

import com.nr.min.Amoeba;

import java.util.Arrays;

import static de.vee.model.Bisection.find;

public class AmoebaModel extends Model {
    private static final int NX1 = Gompertz.DOF();

    public AmoebaModel(double[] x, double[] y, double[] dr, Input input) {
        super(x, y, dr, input);
    }


    double[][] optimize() {
        super.optimize();
        if (!initialized) return null;
        Amoeba amoeba = new Amoeba(1E-6);

        double[][] a = new double[x.length][input.v.length + input.v2.length];

        double[] my = new double[input.v.length];

        double[][] constraints = input.getConstraints();

        SumOfSquares sumOfSquares = new SumOfSquares(x, y, 3, input.N, my, constraints, null);
        SumOfSquaresOfDeathRate sumOfSquares2 = new SumOfSquaresOfDeathRate(x, dr, 3, input.N, my, input.getConstraintsForDeathRate(), null);

        double[] dels = input.dels;

        double[] v = Arrays.copyOf(input.v, input.v.length);
        double[] v1 = Arrays.copyOf(input.v2, input.v2.length);
        double[][] ctrs = null;
        if (constraints != null) {
            ctrs = Arrays.copyOf(constraints, NX1);
        }
        double[] slice = {};
        double[] h0 = Arrays.copyOf(dels, NX1);
        for (int j = start; j < x.length; j++) {
            System.out.printf("Day %.0f of %.0f\n", x[j], x[x.length - 1]);
            sumOfSquares.imax = j + 1;
            int nslice = 0;
            for (double u : input.slice) {
                if (x[j] > u - SuperPose.SIGMA * 0.5) {
                    nslice++;
                }
            }
            if (nslice > slice.length) {
                slice = Arrays.copyOf(input.slice, nslice);
                sumOfSquares.slice = slice;
                double[] vv = new double[(nslice + 1) * NX1];
                double[] hh = new double[(nslice + 1) * NX1];
                double[][] ctrsc = new double[(nslice + 1) * NX1][2];
                System.arraycopy(v, 0, vv, 0, v.length);
                for (int i = v.length; i < vv.length; i++) {
                    vv[i] = v[i - v.length];
                }
                v = vv;
                for (int i = 0; i <= nslice; i++) {
                    int ii = i * NX1;
                    System.arraycopy(h0, 0, hh, ii, NX1);
                    System.arraycopy(ctrs, 0, ctrsc, ii, NX1);
                }
                h0 = hh;
                ctrs = ctrsc;
                sumOfSquares.ctrs = ctrs;
            }
            double[] h = Arrays.copyOf(h0, h0.length);


            if (j > start + 15) { //another magic number: let stabilize the meanvalues first
                sumOfSquares.my = my;
            }
            //first look for cases
            int evaluations = 100;
            while (evaluations > 2) {
                double[] r = amoeba.minimize(v, h = reduceBy(h, 0.97), sumOfSquares);
                double min1 = amoeba.fmin;
                v = amoeba.minimize(r, h = reduceBy(h, 0.97), sumOfSquares);
                double min2 = amoeba.fmin;
//            System.out.printf("%f %f %f %f %f\n", v[0], v[1], v[2], v[3], opt1.funk(v));
                evaluations = amoeba.nfunc;
                //break off
                if (Math.abs(min2 - min1) < 1E-9 * min2 + 1E-15) {
                    // break;
                }
            }
            rms = amoeba.fmin;
            a[j] = Arrays.copyOf(v, v.length + v1.length);
//            System.arraycopy(v, 0, a[j], 0, v.length);
            if (VERBOSE) {
                double[] aa = a[j];
                System.out.print("CumD:");
                for (int i = 0; i < v.length; i++) {
                    System.out.printf(" %f", aa[i]);
                }
                System.out.printf(" rms=%f\n", rms);
            }
            if (input.withInflectionPoint) {
                if (j - start > 3) {
                    my = calMy(a, start + 3, j);
//                    double[] sd = calSd(a, my, start + 3, j);
                }
            }
//            if (j < 1e9) continue;
            //and then for percentage
            sumOfSquares2.f = new SuperPose(v, slice, input.N);
            sumOfSquares2.imax = j + 1;
            h = Arrays.copyOf(input.dels2, input.dels.length);

            if (sumOfSquares2.funk(v1) < 1E-6) continue; //no deaths so far
            if (x[j] < 40) continue;
//            v1[1] = 5; //reset day of max

            evaluations = 100;
            int c = 0;
            while ((evaluations > 2) && (c < 10)) {
                double[] r = amoeba.minimize(v1, h = reduceBy(h, 0.95), sumOfSquares2);
                double min1 = amoeba.fmin;
                v1 = amoeba.minimize(r, h = reduceBy(h, 0.95), sumOfSquares2);
                double min2 = amoeba.fmin;
//            System.out.printf("%f %f %f %f %f\n", v[0], v[1], v[2], v[3], opt1.funk(v));
                evaluations = amoeba.nfunc;
                //break off
                if (Math.abs(min2 - min1) < 1E-16) {
                    c++;
                }
            }
//            double[] aa = new double[v.length + v1.length];
//            System.arraycopy(a[j], 0, aa, 0, v.length);
            System.arraycopy(v1, 0, a[j], v.length, v1.length);
//            a[j] = aa;
            rms = amoeba.fmin;
            if (VERBOSE) {
                int k = a[j].length - 1;
                System.out.printf("NDist: %f %f %f rms=%f\n", a[j][k - 2], a[j][k - 1], a[j][k], rms);
            }
        }

        this.result = a;
        return a;

/*
        double[] w = new double[4];
        w[0] = 0.000062;
        w[1] = 11.647160;
        w[2] = 0.139257;
        w[3] = 1.975206;

        Gompertz g1 = new Gompertz(w,opt1.N);

        v[0] = 0.000079;
        v[1] = 295.474564;
        v[2] = 0.147843;
        v[3] = 24.074659;
        Gompertz g2 = new Gompertz(v,opt1.N);

        File f = new File("china_g.dat");
        try {
            FileWriter fw = new FileWriter(f);
            for (int k = 0; k < dels[0].length; k++) {
                double x = dels[0][k];
                double y = g1.evaluate(x);
                double y2 = g2.evaluate(x);
                double z = (dels[1][k]-y)/y;
                y2+=z*y2;
                fw.write(String.format("%f %f\n", x, y2));
            }
            fw.flush();
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }*/

    }

    void writeLine(String text) {
        System.out.println(text);
    }

    /**
     * Mean value
     *
     * @param a     input values
     * @param start begin index
     * @param end   end index
     * @return mean values
     */
    private double[] calMy(double[][] a, int start, int end) {
        double[] my = Arrays.copyOf(a[start], a[start].length);
        int n = end - start;
        for (int i = start + 1; i < end; i++) {
            for (int k = 0; k < my.length; k++) {
                my[k] += a[i][k];
            }
        }
        for (int k = 0; k < my.length; k++) {
            my[k] /= n;
        }
        return my;
    }

    /**
     * Standard deviation
     *
     * @param a     input values
     * @param my    mean values
     * @param start begin index
     * @param end   end index
     * @return standard deviation
     */
    private double[] calSd(double[][] a, double[] my, int start, int end) {
        double[] sd = new double[my.length];
        int n = end - start;
        for (int k = 0; k < my.length; k++) sd[k] = 0.;
        if (n < 1) return sd;
        for (int i = start; i < end; i++) {
            for (int k = 0; k < my.length; k++) {
                double d = a[i][k] - my[k];
                sd[k] += (d * d);
            }
        }
        for (int k = 0; k < my.length; k++) {
            sd[k] = Math.sqrt(1. / (n - 1) * sd[k]);
        }
        return sd;
    }

    @Override
    public double getDeathRate(double needle) {
        int i = find(x, needle);
        int k = result[i].length - 3;
        return result[i][k];
    }

    @Override
    public double getShift(double needle) {
        int i = find(x, needle);
        int k = result[i].length - 2;
        return result[i][k];
    }

    @Override
    public double getP(double needle) {
        int i = find(x, needle);
        int k = result[i].length - 1;
        return result[i][k];
    }

}
