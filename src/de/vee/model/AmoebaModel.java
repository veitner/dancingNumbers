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
    private static final int NX1 = 4;
    private static final int NX2 = 3;

    public AmoebaModel(double[] x, double[] y, double[] dr, Input input) {
        super(x, y, dr, input);
    }


    double[][] optimize() {
        super.optimize();
        if (!initialized) return null;
        Amoeba amoeba = new Amoeba(1E-6);

        double[][] a = new double[x.length][input.v.length];

        double[] my = new double[input.v.length];

        SumOfSquares opt1 = new SumOfSquares(x, y, dr, 3, input.N, my);

        double[] dels = input.dels;

        double[][] constraints = new double[7][2];
        constraints[0][0] = 1e-7; //a0_min
        constraints[0][1] = 0.25; //a0_max
        constraints[1][0] = 1e-6; //a1_min
        constraints[1][1] = 5e3;  //a1_max
        constraints[2][0] = 1e-6; //a2_min
        constraints[2][1] = 9e-1; //a2_max
        constraints[3][0] = 0.5; //a3_min
        constraints[3][1] = 20; //a3_max
        constraints[4][0] = 1e-3; //percentage_min
        constraints[4][1] = 0.3; //percentage_max
        constraints[5][0] = 0.1; //shift_min
        constraints[5][1] = 12; //shift_max
        constraints[6][0] = 0.05; //p_min
        constraints[6][1] = 3.; //p_max

//        constraints = null;

        opt1.v = Arrays.copyOf(input.v, input.v.length);
        for (int j = start; j < x.length; j++) {
            opt1.imax = j + 1;
            if (j > start + 15) { //another magic number: let stabilize the meanvalues first
                opt1.my = my;
            }
            //first look for cases
            double[] h = Arrays.copyOf(dels, NX1);
            double[] v = Arrays.copyOf(opt1.v, NX1);
            double[][] ctrs = null;
            if (constraints != null) {
                ctrs = Arrays.copyOf(constraints, NX1);
            }
            opt1.considerDeathsOnly = false;
            int evaluations = 100;
            while (evaluations > 2) {
                double[] r = amoeba.minimize(v, h = reduceBy(h, 0.97), ctrs, opt1);
                v = amoeba.minimize(r, h = reduceBy(h, 0.97), ctrs, opt1);
//            System.out.printf("%f %f %f %f %f\n", v[0], v[1], v[2], v[3], opt1.funk(v));
                evaluations = amoeba.getEvaluations();
            }
            System.arraycopy(opt1.v, 0, a[j], 0, opt1.v.length);
            if (VERBOSE) {
                System.out.printf("%f %f %f %f %f %f %f rms=%f\n", a[j][0], a[j][1], a[j][2], a[j][3], a[j][4], a[j][5], a[j][6], rms);
            }
            if (input.withInflectionPoint) {
                if (j - start > 3) {
                    my = calMy(a, start + 3, j);
//                    double[] sd = calSd(a, my, start + 3, j);
                }
            }
//            if (j < 1e9) continue;
            //and then for percentage
            h = new double[3];
            double[] v1 = new double[NX2];
            System.arraycopy(dels, 4, h, 0, NX2);
            System.arraycopy(input.v, 4, v1, 0, NX2);
            System.arraycopy(input.v, 4, opt1.v, 4, NX2);
            if (constraints != null) {
                System.arraycopy(constraints, 4, ctrs, 0, NX2);
            }

            opt1.considerDeathsOnly = true;
            if (opt1.funk(v1) < 1E-6) continue; //no deaths so far
//            v1[1] = 5; //reset day of max

            evaluations = 100;
            while (evaluations > 2) {
                double[] r = amoeba.minimize(v1, h = reduceBy(h, 0.95), ctrs, opt1);
                v1 = amoeba.minimize(r, h = reduceBy(h, 0.95), ctrs, opt1);
//            System.out.printf("%f %f %f %f %f\n", v[0], v[1], v[2], v[3], opt1.funk(v));
                evaluations = amoeba.getEvaluations();
            }
            System.arraycopy(opt1.v, 0, a[j], 0, opt1.v.length);
//            System.arraycopy(v1, 0, a[j], 4, 1);
            rms = amoeba.getMin();
            if (VERBOSE) {
                System.out.printf("%f %f %f %f %f %f %f rms=%f\n", a[j][0], a[j][1], a[j][2], a[j][3], a[j][4], a[j][5], a[j][6], rms);
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

}
