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

import edu.princeton.cs.ErrorFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Convolution {
    private static final double TINY = 1E-10;
    private double[] f;
    private double variance;
    private static final double N = 3.;  //99.7 % are within -N*variance..N*variance
    private static final double sqrt2r = 1. / Math.sqrt(2.);

/*
    private double f(double t) {
        return 0.7071067814e0 / variance * Math.pow(Math.PI, -0.5e0) * Math.exp(-t * t * Math.pow(variance, -0.2e1) / 0.2e1);
    }
*/


    Convolution(double sigma, int bins, double shift) {
        if (sigma < 1E-6) throw new IllegalArgumentException(String.format("sigma must not be that small (%f)", sigma));
        this.variance = sigma * sigma;
        f = new double[bins];
        double w = 2 * N * shift / bins;
        double t0 = -N * shift;
        double t1 = t0 + w;
        double sum = 0.;
        for (int i = 0; i < bins; i++) {
            f[i] = evaluate(t0, t1);
            t0 = t1;
            t1 += w;
            sum += f[i];
        }
        for (int i = 0; i < bins; i++) {
            f[i] /= sum; //scale it to one
        }
    }

    public void print() {
        try {
            FileWriter fw = new FileWriter(new File("gauss.txt"));
            for (int i = 0; i < f.length; i++) {
                fw.write(String.format("%f %f\n", (double) i, f[i]));
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    double getBin(int i) {
        int k = (i + f.length / 2) % (f.length);
        if (k < 0) k += (f.length);
        return f[k];
    }

    private double evaluate(double t0, double t1) {
        if (Math.abs(t0) < TINY) t0 = TINY;
        if (Math.abs(t1) < TINY) t1 = TINY;
        //1/sqrt(2)=0.7071067812e0
        return 0.5e0 * (ErrorFunction.erf(sqrt2r / variance * t1) - ErrorFunction.erf(sqrt2r / variance * t0));

    }

}
