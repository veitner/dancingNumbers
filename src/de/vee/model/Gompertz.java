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

import java.util.Arrays;

class Gompertz implements LogisticFunc {
    private double[] a;
    private double N;

    Gompertz(double[] a, double N) {
        this.a = Arrays.copyOf(a, a.length);
        this.N = N;
    }

    public double evaluate(double x) {
        return a[0] * N * Math.exp(-a[1] * Math.exp(-a[2] * (x + a[3])));
    }

    public double derivative(double x) {
/*
        double h = 0.5;
        double dd = evaluate(x + h) - evaluate(x - h);
*/
        return a[0] * N * a[1] * a[2] * Math.exp(-a[2] * (x + a[3])) * Math.exp(-a[1] * Math.exp(-a[2] * (x + a[3])));
    }

    @Override
    public double inflectionPoint(double[] x) {
        return (x[2] * x[3] - Math.log(x[2])) / x[2];
    }

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
