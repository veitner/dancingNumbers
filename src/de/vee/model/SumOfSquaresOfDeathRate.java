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

public class SumOfSquaresOfDeathRate extends SumOfSquares implements RealValueFun {
    LogisticFunc f;

    SumOfSquaresOfDeathRate(double[] xdata, double[] ydata, double max, double N, double[] my, double[][] ctrs, LogisticFunc f) {
        super(xdata, ydata, max, N, my, ctrs, null);
        this.f = f;
    }

    public double funk(double[] x) {
        double penalty = getPenalty(x);

        double sq = 0;

        double[] dy = DeathRate.getRate(xdata, f, x[0], x[1], x[2]);
        double[] dy0 = DeathRate.getDeaths(dy);
        double[] dr = Arrays.copyOf(ydata, imax);
        for (int i = dr.length - 1; i > 0; i--) {
            dr[i] -= dr[i - 1];
        }
        for (int i = 0; i < imax; i++) {
            double d1 = dy[i];
            double d2 = dr[i];
            if (d2 > 0.) {
                double v = d1 - d2;
                sq += v * v;
            }
            //also include the cumulative curve to avoid zero percentage and/or shift to infinity
            d1 = dy0[i];
            d2 = ydata[i];
            if (d2 > 0.) {
                double v = d1 - d2;
                sq += v * v;
            }
        }
//        return penalty * sq;
        return penalty * Math.sqrt(sq) / (2 * imax + 1);
    }

}
