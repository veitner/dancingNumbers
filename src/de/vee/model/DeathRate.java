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

public class DeathRate {

    public static double[] getRate(double[] x, LogisticFunc f, double deathRate, double shift, double p) {
        if (shift < 1e-3) {
            return new double[x.length];
        }
        return Convolve.eval(x, f, deathRate, shift, p); //convolution
    }

    public static double[] getDeaths(double[] x, LogisticFunc f, double deathRate, double shift, double p) {
        return accumulate(getRate(x, f, deathRate, shift, p));
    }

    public static double[] getDeaths(double[] y) {
        return accumulate(y);
    }

    public static double[] accumulate(double[] y) {
        double[] dy = new double[y.length];
        dy[0] = y[0];
        for (int i = 1; i < dy.length; i++) {
            dy[i] = dy[i - 1] + y[i]; //accumulate
        }
        return dy;
    }


}
